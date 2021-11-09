/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.config;

/**
 */
public interface ConfigLookup
{
	int getInt(String key, int defaultValue);

	long getLong(String key, long defaultValue);

	double getDouble(String key, double defaultValue);

	boolean getBoolean(String key, boolean defaultValue);

	String getString(String key, String defaultValue);
}
