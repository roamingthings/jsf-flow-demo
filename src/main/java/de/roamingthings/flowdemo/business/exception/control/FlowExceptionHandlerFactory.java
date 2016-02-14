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

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Initialize and register the {@link FlowExceptionHandler} as a JSF {@link ExceptionHandler}.
 */
public class FlowExceptionHandlerFactory extends ExceptionHandlerFactory {
    private ExceptionHandlerFactory exceptionHandlerFactory;

    public FlowExceptionHandlerFactory() {
    }

    public FlowExceptionHandlerFactory(ExceptionHandlerFactory
                                                  exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new FlowExceptionHandler
                (exceptionHandlerFactory.getExceptionHandler());

        return handler;
    }
}
