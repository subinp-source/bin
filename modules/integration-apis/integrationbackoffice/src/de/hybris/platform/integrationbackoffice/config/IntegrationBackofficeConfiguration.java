/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationbackoffice.config;

/**
 * Configuration interface for the integrationbackoffice extension.
 */
public interface IntegrationBackofficeConfiguration
{
	/**
	 * Provides the value of the prefix used in the persisted media
	 *
	 * @return String with the value of the prefix
	 */
	String getServiceBackofficeApiUrl();
}
