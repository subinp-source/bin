/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.generator;

import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

/**
 * Generates a dummy ImpEx payload for an integration object
 */
public interface IntegrationObjectImpexGenerator
{
	/**
	 * Creates a dummy Impex string from an integration object
	 *
	 * @return a formatted dummy Impex payload
	 */
	String generateImpex(IntegrationObjectModel integrationObjectModel);
}
