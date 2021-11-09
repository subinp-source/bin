/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.utils;

import de.hybris.platform.util.Config;


/**
 * Helper class for configuration/property retrieval.
 */
public class KymaConfigurationUtils
{
	private static final String TARGET_SYSTEM_NAME = "kymaintegrationservices.target_system.name";
	private static final String DEFAULT_TARGET_SYSTEM_NAME = "Extension Factory";

	private KymaConfigurationUtils()
	{
	}

	/**
	 * Utility method to get the target system name for the Kyma Integration Product.
	 */
	public static String getTargetName()
	{
		return Config.getString(TARGET_SYSTEM_NAME, DEFAULT_TARGET_SYSTEM_NAME);
	}
}
