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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Backing bean for the {@code registration/summary.xhtml} view.
 *
 * This bean is in the {@code registration} flow scope and uses the values held by the {@link RegistrationPage}.
 */
@Named
@FlowScoped("registration")
public class SummaryPage implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = getLogger(SummaryPage.class.getName());

    @Inject
    RegistrationPage registrationPage;

    private Date created;
    private String registrationValue;

    @PostConstruct
    public void init() {
        log.info("Initializing summary page bean");

        registrationValue = registrationPage.getRegistrationValue();
        created = new Date();
    }

    /**
     * This method just creates a log message to demonstrate when the flow gets destroyed.
     */
    @PreDestroy
    public void destroy() {
        log.info("Destroying SummaryPage bean");
    }

    public String getTime() {
        final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return formatter.format(created);
    }

    public String getRegistrationValue() {
        return registrationValue;
    }
}
