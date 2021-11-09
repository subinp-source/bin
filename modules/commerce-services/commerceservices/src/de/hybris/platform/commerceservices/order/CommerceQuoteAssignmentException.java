/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception thrown if quote assignment fails
 */
public class CommerceQuoteAssignmentException extends SystemException
{
	final private String assignedUser;

	public CommerceQuoteAssignmentException(final String message, final String assignedUser)
	{
		super(message);
		this.assignedUser = assignedUser;
	}

	public String getAssignedUser()
	{
		return assignedUser;
	}
}
