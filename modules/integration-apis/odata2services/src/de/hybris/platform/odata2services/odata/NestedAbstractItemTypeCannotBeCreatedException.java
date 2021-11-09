/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

public class NestedAbstractItemTypeCannotBeCreatedException extends InvalidNavigationPropertyException
{
	private static final String MSG = "Invalid attribute found [%s.%s]. " +
			"Cannot define attribute of an abstract item type as autocreate or partof for the item that it belongs to.";

	public NestedAbstractItemTypeCannotBeCreatedException(final TypeAttributeDescriptor descriptor)
	{
		super(MSG, descriptor);
	}
}
