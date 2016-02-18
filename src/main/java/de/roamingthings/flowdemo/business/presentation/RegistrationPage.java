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
package de.roamingthings.flowdemo.business.presentation;

import de.roamingthings.flowdemo.business.registration.boundary.FlowStarter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Backing bean for the {@code /registration/registration.xhtml} view.
 *
 * This bean is in the {@code registration} flow and retrieves the query parameter value from the
 * {@link FlowStarter}.
 */
@Named
@FlowScoped(value = "registration")
public class RegistrationPage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    FlowStarter starter;

    Logger log = Logger.getLogger(RegistrationPage.class.getName());

    private String registrationValue;

    private Date created;

    /**
     * This method gets called after the flow has been created.
     *
     * When this method is called the {@link FlowStarter} has already captured the quary parameter and is
     * injected into this bean.
     *
     * In this example the value is retrived from the initializer and stored in the {@link #registrationValue} field.
     */
    @PostConstruct
    public void init() {
        log.info("Initializing registration page bean");

        // Store the time when this bean has been created
        created = new Date();
    }

    public void initializeFlow() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        registrationValue = facesContext.getExternalContext().getRequestParameterMap().get("id");

        log.info("---> Initialize flow called with query parameter " + registrationValue);
    }

    /**
     * This method just creates a log message to demonstrate when the flow gets destroyed.
     */
    @PreDestroy
    public void destroy() {
        log.info("Destroying RegistrationPage bean");
    }

    public String initPage() {
        log.info("View action has been called on registration bean");

        return null;
    }

    public String getTime() {
        final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return formatter.format(created);
    }

    public String getRegistrationValue() {
        return registrationValue;
    }
}
