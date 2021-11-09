/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;

/**
 * Indicates a problem with Integration Object item definition, when one of its key attributes is also localized. Localized
 * attributes cannot be declared as unique for an item.
 */
public class LocalizedKeyAttributeException extends InvalidIntegrationKeyDefinitionException
{
	/**
	 * Constructor to create InvalidIntegrationKeyDefinitionException
	 *
	 * @param attributeModel model of the invalid attribute definition
	 */
	public LocalizedKeyAttributeException(final IntegrationObjectItemAttributeModel attributeModel)
	{
		super(attributeModel);
	}
}
