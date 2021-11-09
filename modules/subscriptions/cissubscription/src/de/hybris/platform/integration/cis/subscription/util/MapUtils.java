/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.util;

import java.io.*;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


/**
 * Utility class for Maps.
 */
public final class MapUtils
{

	private MapUtils()
	{
		// utility class -> no public constructor
	}

	/**
	 * Loads a property class into a set with entry mappings.
	 *
	 * @param propertiesFilePath file path to the property file (e.g. ./filename.properties for files in /resources)
	 * @return a set of entry mappings
	 */
	public static Set<Entry<Object, Object>> loadPropertiesToSet(final String propertiesFilePath)
	{
		final Properties properties = new Properties();
		try (final InputStream resourceAsStream = MapUtils.class.getResourceAsStream(propertiesFilePath))
		{
			properties.load(resourceAsStream);
		}
		catch (final IOException e)
		{
			throw new IllegalArgumentException("Failed to load properties from " + propertiesFilePath + ": " + e.getMessage(), e);
		}

		return properties.entrySet();
	}

	/**
	 * Loads an external (non-classpath) property class into a set with entry mappings.
	 *
	 * @param propertiesFilePath file path to the property file
	 * @return a set of entry mappings
	 */
	public static Set<Entry<Object, Object>> loadExternalPropertiesToSet(final String propertiesFilePath)
	{
		final Properties properties = new Properties();

		try (final InputStream resourceAsStream = new FileInputStream(propertiesFilePath))
		{
			properties.load(resourceAsStream);
		}
		catch (final IOException e)
		{
			throw new IllegalArgumentException("Failed to load properties from " + propertiesFilePath + ": " + e.getMessage(), e);
		}

		return properties.entrySet();
	}
}