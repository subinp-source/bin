/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;

public class MissingKeyReferencedAttributeValueException extends IntegrationAttributeException
{
	private static final String MESSAGE = "Key referenced attribute [%s] is required for [%s].";

	/**
	 * Constructor to create MissingKeyNavigationPropertyException
	 *
	 * @param attr the IntegrationObjectItemAttributeModel value that is missing
	 */
	public MissingKeyReferencedAttributeValueException(final IntegrationObjectItemAttributeModel attr)
	{
		super(String.format(MESSAGE, attr.getAttributeName(), attr.getIntegrationObjectItem().getCode()), attr);
	}
}
