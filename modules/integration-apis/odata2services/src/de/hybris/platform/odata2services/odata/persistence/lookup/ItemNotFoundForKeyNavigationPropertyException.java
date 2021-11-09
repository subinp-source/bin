/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.lookup;

import de.hybris.platform.integrationservices.search.ItemNotFoundForKeyReferencedItemPropertyException;

import org.apache.olingo.odata2.api.edm.EdmEntityType;

/**
 * Exception for cases when a navigation property does not exist in the DB during lookup.
 * @deprecated use {@link ItemNotFoundForKeyReferencedItemPropertyException}
 */
@Deprecated(since = "1905.2002-CEP", forRemoval = true)
public class ItemNotFoundForKeyNavigationPropertyException extends ItemNotFoundForKeyReferencedItemPropertyException
{
	/**
	 * Constructor to create ItemNotFoundForKeyNavigationPropertyException
	 *
	 * @param entityTypeName {@link EdmEntityType} name
	 * @param propertyName   the name of the
	 */
	public ItemNotFoundForKeyNavigationPropertyException(final String entityTypeName, final String propertyName)
	{
		super(entityTypeName, propertyName);
	}
}
