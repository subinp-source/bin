/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent.exceptions;

/**
 * Exception to be thrown when a user tries to withdraw a consent that is already withdrawn.
 *
 */
public class CommerceConsentWithdrawnException extends RuntimeException
{
	private static final long serialVersionUID = 1223787471985556550L;

	public CommerceConsentWithdrawnException(final String message)
	{
		super(message);
	}

}
