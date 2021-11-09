/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.platform.apiregistryservices.exceptions.EventPropertyConfigurationException;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Utility class to validate EventPropertyConfiguration
 */
public class EventPropertyConfigurationValidationUtils
{
	private static final int EVENT_PROPERTY_SPLIT_LIMIT = 2;

	private EventPropertyConfigurationValidationUtils()
	{
		throw new IllegalStateException("Utility class");
	}

	public static void validPropertyMapping(final String reflectedClassString, final String propertyMapping)
			throws EventPropertyConfigurationException
	{
		final String delimiter = EventExportUtils.getDelimiter();
		if (StringUtils.isEmpty(propertyMapping) || !EventExportUtils.canSplitReference(propertyMapping, delimiter))
		{
			throw new EventPropertyConfigurationException(String.format(
					"EventPropertyConfiguration for the Class : %s , is not valid. PropertyMapping : %s, is empty or has invalid format.",
					reflectedClassString, propertyMapping));
		}

		Class reflectedClass;
		try
		{
			reflectedClass = Class.forName(reflectedClassString);
		}
		catch (final ClassNotFoundException e)
		{
			throw new EventPropertyConfigurationException(
					String.format("EventPropertyConfiguration for the Class : %s , is not valid. The event Class does not exist.",
							reflectedClassString));
		}

		if (!existsAttribute(reflectedClass, propertyMapping.split(delimiter, EVENT_PROPERTY_SPLIT_LIMIT)[1], delimiter))
		{
			throw new EventPropertyConfigurationException(String.format(
					"PropertyMapping of an EventPropertyConfiguration is not valid. Property : %s , does not exist in Class : %s.",
					propertyMapping, reflectedClass));
		}

	}

	private static boolean existsAttribute(final Class reflectedClass, final String reference, final String delimiter)
	{
		final String[] splitMappingReference = EventExportUtils.splitReference(reference, delimiter);

		final Optional<PropertyDescriptor> method = findMethod(reflectedClass, splitMappingReference[0]);

		if (method.isEmpty())
		{
			return false;
		}

		return splitMappingReference.length <= 1
				|| existsAttribute(method.get().getPropertyType(), splitMappingReference[1], delimiter);
	}

	private static Optional<PropertyDescriptor> findMethod(final Class reflectedClass, final String propertyName)
	{
		return Arrays.stream(PropertyUtils.getPropertyDescriptors(reflectedClass))
				.filter(method -> method.getName().equals(propertyName)).findFirst();
	}
}
