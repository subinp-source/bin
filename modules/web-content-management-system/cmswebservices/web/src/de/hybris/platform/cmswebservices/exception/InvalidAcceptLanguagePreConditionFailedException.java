/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.exception;

/**
 * Thrown when a PUT or POST request has been placed with multiple ACCEPT-LANGUAGES.
 */
public class InvalidAcceptLanguagePreConditionFailedException extends RuntimeException {

	private static final long serialVersionUID = -3729553926413250731L;

	public InvalidAcceptLanguagePreConditionFailedException(final String message)
	{
		super(message);
	}

	public InvalidAcceptLanguagePreConditionFailedException(final Throwable cause) {
		super(cause);
	}

	public InvalidAcceptLanguagePreConditionFailedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}