/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.exception;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

/**
 * UnkownResource validation exception used to throw validation errors. <br/>
 */

public class UnknownResourceException extends WebserviceException
{
	private static final long serialVersionUID = 691123443175113694L;

	private static final String TYPE = "UnknownResourceError";

	public UnknownResourceException(final String message)
	{
		super(message);
	}

	@Override
	public String getSubjectType()
	{
		return null;
	}

	@Override
	public String getType()
	{
		return TYPE;
	}
}