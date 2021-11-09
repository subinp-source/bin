/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

public class InvalidNavigationPropertyException extends InvalidDataException
{
	private static final String CODE = "invalid_property_definition";
	private static final String MESSAGE = "Cannot generate unique navigation property for collections [%s.%s]";
	private final String propertyName;

	/**
	 * @deprecated use {@link UniqueCollectionNotAllowedException}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	public InvalidNavigationPropertyException(final String entityType, final String propertyName)
	{
		this(MESSAGE, entityType, propertyName);
	}

	private InvalidNavigationPropertyException(final String message, final String entityType, final String propertyName)
	{
		super(String.format(message, entityType, propertyName), CODE, entityType);
		this.propertyName = propertyName;
	}

	public InvalidNavigationPropertyException(final String message, final TypeAttributeDescriptor descriptor)
	{
		this(message, descriptor.getTypeDescriptor().getTypeCode(), descriptor.getAttributeName());
	}
	
	public String getPropertyName()
	{
		return propertyName;
	}
}
