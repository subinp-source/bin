/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * This is a "root" super class for all exception indicating problem with attribute reading/writing for a specific item model.
 * If such exception is thrown for one item model, it does not mean it's going to be thrown for another item model. If
 * a problem is systemic and is caused by an invalid attribute definition then those exceptions should extend
 * {@link IntegrationAttributeException} bypassing this class in their hierarchy.
 */
public abstract class IntegrationAttributeProcessingException extends IntegrationAttributeException
{
	/**
	 * Instantiates this exception
	 *
	 * @param message    a message to be used for this exception. If this message contains format parameter placeholders, e.g. {@code "%s"},
	 *                   then the message will be used as a template and the placeholders will be replaced in this order:
	 *                   {@code attributeName}, {@code ioItemCode}
	 * @param descriptor descriptor of the attribute, for which the exception is thrown.
	 */
	public IntegrationAttributeProcessingException(final String message,
	                                               final TypeAttributeDescriptor descriptor)
	{
		super(message, descriptor);
	}

	/**
	 * Instantiates this exception
	 *
	 * @param message    a message to be used for this exception. If this message contains format parameter placeholders, e.g. {@code "%s"},
	 *                   then the message will be used as a template and the placeholders will be replaced in this order:
	 *                   {@code attributeName}, {@code ioItemCode}
	 * @param descriptor descriptor of the attribute, for which the exception is thrown.
	 * @param cause      an intercepted exception that caused this exception to be thrown
	 */
	public IntegrationAttributeProcessingException(final String message,
	                                               final TypeAttributeDescriptor descriptor,
	                                               final Throwable cause)
	{
		super(message, descriptor, cause);
	}
}
