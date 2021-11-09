/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceocc.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;


/**
 * Exception threw when process uploaded file.
 */
public class FileUploadException extends WebserviceException //NOSONAR
{

	private static final String TYPE = "FileUploadError";
	private static final String SUBJECT_TYPE = "FileUpload";


	public FileUploadException(final String message)
	{
		super(message);
	}

	public FileUploadException(final String message, final String reason)
	{
		super(message, reason);
	}

	public FileUploadException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public FileUploadException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public FileUploadException(final String message, final String reason, final String subject, final Throwable cause)
	{
		super(message, reason, subject, cause);
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

	@Override
	public String getSubjectType()
	{
		return SUBJECT_TYPE;
	}

}
