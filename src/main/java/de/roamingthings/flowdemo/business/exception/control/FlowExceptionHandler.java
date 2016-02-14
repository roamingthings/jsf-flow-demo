/*
 Copyright 2016 Alexander Sparkowsky

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package de.roamingthings.flowdemo.business.exception.control;

import javax.enterprise.context.ContextNotActiveException;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This {@link ExceptionHandler} is responsible for wrapping an {@link ContextNotActiveException} that is caught when
 * a flow scoped bean is called outside an active flow.
 *
 * It will redirect the request to an error page that can be configured by a servlet context init parameter in the
 * {@code web.xml} descriptor.
 */
public class FlowExceptionHandler extends ExceptionHandlerWrapper {
    private static final Logger logger =
            Logger.getLogger(FlowExceptionHandler.class.getName());

    /** Init parameter name */
    private static final String INIT_PARAMETER_CONTEXT_INACTIVE_EXCEPTION_OUTCOME = "de.roamingthings.exception.CONTEXT_INACTIVE_EXCEPTION_OUTCOME";

    /** Default error view */
    public static final String OUTCOME_DEFAULT_CONTEXT_INACTIVE = "/contextInactive";

    private ExceptionHandler exceptionHandler;

    FlowExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Wrapped {@link ExceptionHandler}.
     */
    @Override
    public ExceptionHandler getWrapped() {
        return exceptionHandler;
    }

    /**
     * This method checks all queued exception for an exception containing a {@link ContextNotActiveException} as
     * cause.
     * @throws FacesException
     */
    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> queue =
                getUnhandledExceptionQueuedEvents().iterator();

        // Handle all `ContextNotActiveExceptions` from the queue
        while (queue.hasNext()) {

            //take exceptions one by one
            ExceptionQueuedEvent item = queue.next();
            ExceptionQueuedEventContext exceptionQueuedEventContext =
                    (ExceptionQueuedEventContext) item.getSource();
            Throwable throwable = exceptionQueuedEventContext.getException();
            Throwable cause = throwable.getCause();

            if (cause instanceof ContextNotActiveException) {
              ContextNotActiveException contextException = (ContextNotActiveException) cause;
                try {
                    final FacesContext facesContext = FacesContext.getCurrentInstance();
                    final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
                    final HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

                    // Log the error
                    logger.log(Level.WARNING,
                            String.format("Access to inactive context detected with request <%s> in session <%s>: ",
                                    request.getRequestURI(),
                                    (session != null) ? session.getId() : "none"),
                            contextException);

                    // Redirect to error view
                    String errorOutcome = getOutcome(facesContext);

                    // Navigate to the error outcome
                    Map<String, Object> requestMap =
                            facesContext.getExternalContext().getRequestMap();
                    NavigationHandler nav =
                            facesContext.getApplication().getNavigationHandler();

                    // Put the error into the request to be handled by the error page.
                    requestMap.put("error", contextException);
                    nav.handleNavigation(facesContext, null, errorOutcome);
                    facesContext.renderResponse();
                } finally {
                    //remove the exception from the queue
                    queue.remove();
                }
            }
        }

        // Let the wrapped handler do its work
        getWrapped().handle();
    }

    /**
     * Get the outcome for the error page.
     *
     * If an outcome had been configured in the {@code web.xml} by setting the init parameter
     * {@code de.roamingthings.exception.CONTEXT_INACTIVE_EXCEPTION_OUTCOME}. If the parameter is not present, the
     * default value {@link #OUTCOME_DEFAULT_CONTEXT_INACTIVE} will be returned.
     *
     * @param facesContext    This instance is used to retrieve the {@link ServletContext}.
     * @return The outcome for the error view.
     */
    private static String getOutcome(FacesContext facesContext) {
        // Try to get the ServletContext and context parameter for the resulting outcome
        final Object servletContextCandidate = facesContext.getExternalContext().getContext();
        String errorOutcome = null;

        if (servletContextCandidate instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) servletContextCandidate;
            errorOutcome = servletContext.getInitParameter(INIT_PARAMETER_CONTEXT_INACTIVE_EXCEPTION_OUTCOME);
        }

        errorOutcome = (errorOutcome != null) ? errorOutcome : OUTCOME_DEFAULT_CONTEXT_INACTIVE;
        return errorOutcome;
    }
}