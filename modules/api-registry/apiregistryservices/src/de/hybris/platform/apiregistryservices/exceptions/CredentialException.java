/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.exceptions;

/**
 * Credential usage or retrieval Exception
 */
public class CredentialException extends Exception
{
	public CredentialException(final String message)
	{
		super(message);
	}

	public CredentialException(final String message, final Throwable t)
	{
		super(message, t);
	}
}
