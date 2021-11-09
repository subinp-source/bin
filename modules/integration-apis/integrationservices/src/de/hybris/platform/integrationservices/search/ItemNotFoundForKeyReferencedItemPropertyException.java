/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.model.TypeDescriptor;

/**
 * Exception indicating that an item referenced in the payload property does not exist in the persistent storage.
 */
public class ItemNotFoundForKeyReferencedItemPropertyException extends RuntimeException
{
	private final String propertyName;
	private final String entityTypeName;

	/**
	 * Instantiates this exception with the provided item type and reference property name in that type, which cannot resolve
	 * to an item existing in the persistent storage.
	 *
	 * @param itemType type of the item containing a missing referenced item
	 * @param propertyName name of the property referencing an item that is not found in the persistent storage
	 */
	public ItemNotFoundForKeyReferencedItemPropertyException(final TypeDescriptor itemType, final String propertyName)
	{
		this(itemType.getItemCode(), propertyName);
	}

	/**
	 * Constructor to create ItemNotFoundForKeyReferencedItemPropertyException
	 *
	 * @param type code of the item type in the integration object that references a missing item
	 * @param property name of the property referencing an item that is not found in the persistent storage
	 */
	public ItemNotFoundForKeyReferencedItemPropertyException(final String type, final String property)
	{
		super(String.format("[%s] was not found for [%s].", property, type));

		entityTypeName = type;
		propertyName = property;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public String getEntityTypeName()
	{
		return entityTypeName;
	}
}
