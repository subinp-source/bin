/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;

import org.springframework.validation.Errors;


/*
 * Suppress sonar warning (squid:S1948 | Fields in a "Serializable" class should either be transient or serializable) :
 * the implementation of validationErrors is of type DefaultValidationErrors which is already Serializable.
 */
@SuppressWarnings("squid:S1948")
/**
 * Exception thrown when there is any problem when validating request data.
 */
public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 5922002536003254842L;

	protected Errors validationObject;
	private final ValidationErrors validationErrors;

	public ValidationException(final Errors validationObject)
	{
		super("Validation error");
		this.validationObject = validationObject;
		this.validationErrors = null;
	}

	public ValidationException(final ValidationErrors validationErrors)
	{
		super("Validation error");
		this.validationErrors = validationErrors;
	}

	public Errors getValidationObject()
	{
		return validationObject;
	}

	public ValidationErrors getValidationErrors()
	{
		return validationErrors;
	}
}
