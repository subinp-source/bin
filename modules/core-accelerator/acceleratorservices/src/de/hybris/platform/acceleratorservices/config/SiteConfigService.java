/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.config;

/**
 * Site config service is used to lookup a site specific configuration property.
 * The configuration property is looked up relative to the current site.
 */
public interface SiteConfigService extends ConfigLookup
{
	/**
	 * Get property for current base site
	 * 
	 * @param property the property to get
	 * @return the property value
	 */
	String getProperty(String property);
}
