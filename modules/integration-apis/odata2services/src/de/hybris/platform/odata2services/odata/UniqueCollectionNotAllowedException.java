/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

public class UniqueCollectionNotAllowedException extends InvalidNavigationPropertyException
{
	private static final String MESSAGE = "Cannot generate unique navigation property for collections [%s.%s]";

	public UniqueCollectionNotAllowedException(final TypeAttributeDescriptor descriptor)
	{
		super(MESSAGE, descriptor);
	}
}
