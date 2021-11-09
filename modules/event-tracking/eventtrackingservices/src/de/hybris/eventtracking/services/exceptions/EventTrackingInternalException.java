/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.exceptions;

public class EventTrackingInternalException extends RuntimeException {

    public EventTrackingInternalException() {
        super();
    }

    public EventTrackingInternalException(String message) {
        super(message);
    }

    public EventTrackingInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
