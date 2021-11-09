/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

import org.springframework.validation.AbstractBindingResult;


/**
 * Error class to be used for testing purpose only; implements the {@link org.springframework.validation.Errors}
 * interface.
 */
public class ValidationError extends AbstractBindingResult
{
	private static final long serialVersionUID = 2687980944733761368L;

	public ValidationError(final String objectName)
	{
		super(objectName);
	}

	@Override
	public Object getTarget()
	{
		return null;
	}

	@Override
	protected Object getActualFieldValue(final String s)
	{
		return null;
	}

}
