/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.generator;

import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

/**
 * Generates a dummy JSON payload for an integration object
 */
public interface IntegrationObjectJsonGenerator
{
	/**
	 * Creates a dummy JSON string from an integration object
	 *
	 * @param integrationObjectModel a given integration object
	 * @return a formatted dummy JSON payload
	 */
	String generateJson(final IntegrationObjectModel integrationObjectModel);
}
