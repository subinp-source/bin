/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.consent.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception indicating that a user already gave a consent.
 */
public class CommerceConsentGivenException extends SystemException
{
	private static final long serialVersionUID = -2774890542600485732L;

	public CommerceConsentGivenException(final String message)
	{
		super(message);
	}
}
