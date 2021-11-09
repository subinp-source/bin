/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.apiregistryservices.exceptions;

/**
 * EventPropertyConfiguration Exception
 */
public class EventPropertyConfigurationException extends Exception
{
    public EventPropertyConfigurationException(final String message)
    {
        super(message);
    }

    public EventPropertyConfigurationException(final String message, final Throwable t)
    {
        super(message, t);
    }
}
