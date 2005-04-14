/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Implementation of an ApplicationListener that listens specifically for
 * events within the CAS domain.  Upon receiving an event that it can handle,
 * the listener attempts to delegate the event to one or more event handlers
 * to process the event (i.e. a Log4JLoggedInEventHandler and a DbLoggedInEventHandler).
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public final class EventListener implements ApplicationListener, InitializingBean {

    /** The array of event handlers. */
    private EventHandler[] eventHandlers;
    
    public void onApplicationEvent(final ApplicationEvent applicationEvent) {
        if (!AbstractEvent.class.isAssignableFrom(applicationEvent.getClass())) {
            return;
        }
        
        for (int i = 0; i < this.eventHandlers.length; i++) {
            final EventHandler eventHandler = this.eventHandlers[i];
            
            if (eventHandler.supports(applicationEvent)) {
                eventHandler.handleEvent(applicationEvent);
            }
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (this.eventHandlers == null || this.eventHandlers.length == 0) {
            throw new IllegalStateException("eventHandlers cannot be null or empty on " + this.getClass().getName());
        }
    }

    /**
     * Method to set the Event Handlers to process events.
     * @param eventHandlers the handlers.
     */
    public void setEventHandlers(final EventHandler[] eventHandlers) {
        this.eventHandlers = eventHandlers;
    }
}
