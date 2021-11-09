/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsocc.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

public class MessageCenterCSException extends WebserviceException
{

	private static final String TYPE = "MessageCenterCSError";
	private static final String SUBJECT_TYPE = "MessageCenterCS";
	private final String errorCode;

	public MessageCenterCSException(final String errorCode, final String message)
	{
		super(message);
		this.errorCode = errorCode;
	}


	@Override
	public String getSubjectType()
	{
		return SUBJECT_TYPE;
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

}