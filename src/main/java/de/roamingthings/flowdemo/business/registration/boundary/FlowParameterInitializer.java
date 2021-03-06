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
package de.roamingthings.flowdemo.business.registration.boundary;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * This class is responsible for getting the request parameter from the query by providing a {@code viewAction} and
 * a {@code viewParameter}.
 */
@Named
@RequestScoped
public class FlowParameterInitializer {
    static Logger log = Logger.getLogger(FlowParameterInitializer.class.getName());

    private String id;

    /**
     * This method gets called as a {@code viewAction} from the start view of the flow ({@code registration.xhtml}
     * <em>before</em> the flow scope is started.
     *
     * @return The outcome to start the flow (in this case {@code registration}.
     */
    public String initPage() {
        // Check if there is already a flow
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getApplication().getFlowHandler().getCurrentFlow() == null) {
            // ### If not already in the flow, return the outcome to start it
            log.fine("Starting registration flow with id query parameter" + id);
            return "registration";
        } {
            // ### If already in the flow don't return any outcome
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
