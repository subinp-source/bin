/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.event;

/**
 * This exception is thrown when event type for {@link ItemSavedEventType} is invalid.
 */
public class InvalidEventTypeException extends RuntimeException
{
	/**
	 * Instantiates an InvalidEventTypeException
	 * @param type The event type integer that is invalid
	 */
	public InvalidEventTypeException(final int type){
		super(String.format("The event type [%d] is invalid", type));
	}
}
