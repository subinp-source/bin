/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * Indicates any problem with the key (unique) attributes definition for an integration object item.
 */
class InvalidKeyDefinitionException extends IntegrationAttributeException
{
	/**
	 * Instantiates this exception
	 * @param message a message to be used for this exception. If this message contains format parameter placeholders, e.g. {@code "%s"},
	 * then the message will be used as a template and the placeholders will be replaced in this order: {@code attributeName}, {@code ioItemCode}
	 * @param attribute descriptor of the failed attribute
	 */
	InvalidKeyDefinitionException(final String message, final TypeAttributeDescriptor attribute)
	{
		super(message, attribute);
	}

	/**
	 * Instantiates this exception
	 * @param message a message to be used for this exception. If this message contains format parameter placeholders, e.g. {@code "%s"},
	 * then the message will be used as a template and the placeholders will be replaced in this order: {@code attributeName}, {@code ioItemCode}
	 * @param attr an invalid key attribute
	 */
	InvalidKeyDefinitionException(final String message, final IntegrationObjectItemAttributeModel attr)
	{
		super(message, attr);
	}
}