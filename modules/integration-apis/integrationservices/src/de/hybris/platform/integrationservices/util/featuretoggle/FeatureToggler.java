/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util.featuretoggle;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * FeatureToggler enables developers to toggle on/off features in their code.
 * Developers need to create an enumeration that extends from {@link Feature},
 * define the feature name in a properties file, and set it to true or false.
 * For example {@code my.feature=true}. Developers can toggle feature dynamically by looking up the property
 * in the HAC -> Platform -> Configuration.
 *
 * @see IntegrationApiFeature
 */
public class FeatureToggler
{
	private static ConfigurationService configurationService;

	private FeatureToggler()
	{
		//this class should not be instantiated
	}

	/**
	 * Indicates whether a feature is on (true) or off (false).
	 * @param feature The feature to query
	 * @return True if feature is enabled, else false
	 */
	public static boolean isFeatureEnabled(final Feature feature)
	{
		return feature != null && getConfigurationService().getConfiguration().getBoolean(feature.getProperty(), false);
	}

	private static ConfigurationService getConfigurationService()
	{
		if (configurationService == null)
		{
			configurationService = (ConfigurationService) Registry.getApplicationContext().getBean("configurationService");
		}
		return configurationService;
	}
}
