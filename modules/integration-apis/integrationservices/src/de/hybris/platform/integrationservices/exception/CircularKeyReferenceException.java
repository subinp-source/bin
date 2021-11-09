/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;

/**
 * Indicates that an integration object item has a key defined so, that at least one of the key attribute references forms a
 * loop. For example, type A key depends on type B, which has a key attribute referring type A.
 */
public class CircularKeyReferenceException extends InvalidKeyDefinitionException
{
	private static final String MESSAGE_TEMPLATE = "Key attribute '%s.%s' forms a circular reference back to '%s'";
	private final String referencedType;

	/**
	 * Instantiates this exception.
	 * @param attr an invalid key attribute model
	 */
	public CircularKeyReferenceException(final IntegrationObjectItemAttributeModel attr)
	{
		super(message(attr), attr);
		referencedType = attr.getReturnIntegrationObjectItem().getCode();
	}

	/**
	 * Retrieves integration item code, to which the loop is formed.
	 * @return code of the integration item, to which the invalid attribute references.
	 */
	public String getReferencedType()
	{
		return referencedType;
	}

	private static String message(final IntegrationObjectItemAttributeModel attr)
	{
		return String.format(MESSAGE_TEMPLATE, attr.getIntegrationObjectItem().getCode(), attr.getAttributeName(), attr.getReturnIntegrationObjectItem().getCode());
	}
}