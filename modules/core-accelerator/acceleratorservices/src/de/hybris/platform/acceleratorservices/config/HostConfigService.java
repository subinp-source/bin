/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.config;

/**
 * Host config service is used to lookup a hostname specific configuration property.
 * Certain configurations are specific the the hostname that the site is accessed on,
 * these include analytics tracking packages or maps integrations.
 */
public interface HostConfigService
{
	/**
	 * Get property for host
	 * 
	 * @param property the property to get
	 * @param hostname the host name
	 * @return the property value
	 */
	String getProperty(String property, String hostname);

	/**
	 * Get a {@link ConfigLookup} by host name
	 * 
	 * @param hostname the host name
	 * @return a {@link ConfigLookup}
	 */
	ConfigLookup getConfigForHost(String hostname);
}
