/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model;

/**
 * Additional characteristics for an attribute of Map type, i.e. an attribute that holds values of {@code java.lang.Map} type.
 */
public interface MapDescriptor
{
	/**
	 * Provides descriptor of the {@code key} type in the Map.
	 * @return descriptor for the Map {@code key} type.
	 */
	TypeDescriptor getKeyType();

	/**
	 * Provides descriptor for the {@code value} type in the Map.
	 * @return descriptor for the Map {@code value} type.
	 */
	TypeDescriptor getValueType();
}
