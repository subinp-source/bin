/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationbackoffice.config;

import de.hybris.platform.integrationservices.config.BaseIntegrationServicesConfiguration;

/**
 * Contains the default configuration for properties in the integrationbackoffice extension.
 */
public class DefaultIntegrationBackofficeConfiguration extends BaseIntegrationServicesConfiguration
		implements IntegrationBackofficeConfiguration
{
	private static final String CCV2_SERVICE_BACKOFFICE_URL = "ccv2.services.backoffice.url.0";
	private static final String DEFAULT_CCV2_SERVICE_BACKOFFICE_URL = "https://localhost:9002";

	@Override
	public String getServiceBackofficeApiUrl()
	{
		return getStringProperty(CCV2_SERVICE_BACKOFFICE_URL, DEFAULT_CCV2_SERVICE_BACKOFFICE_URL);
	}
}
