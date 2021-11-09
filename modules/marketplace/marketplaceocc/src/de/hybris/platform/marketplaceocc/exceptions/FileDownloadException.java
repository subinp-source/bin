/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceocc.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;


/**
 * Exception throw when process download file.
 */
public class FileDownloadException extends WebserviceException //NOSONAR
{
	private static final String TYPE = "FileDownloadError";
	private static final String SUBJECT_TYPE = "FileDownload";


	public FileDownloadException(final String message)
	{
		super(message);
	}

	public FileDownloadException(final String message, final String reason)
	{
		super(message, reason);
	}

	public FileDownloadException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public FileDownloadException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public FileDownloadException(final String message, final String reason, final String subject, final Throwable cause)
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
