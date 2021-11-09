/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;

/**
 * @deprecated use {@link LocalizedKeyAttributeException}
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public class InvalidIntegrationKeyDefinitionException extends InvalidKeyDefinitionException
{
	private static final String MESSAGE = "Cannot generate unique property for localized attribute [%s] set on item [%s].";

	/**
	 * Constructor to create InvalidIntegrationKeyDefinitionException
	 *
	 * @param attributeModel model of the invalid attribute definition
	 */
	public InvalidIntegrationKeyDefinitionException(final IntegrationObjectItemAttributeModel attributeModel)
	{
		super(MESSAGE, attributeModel);
	}
}
