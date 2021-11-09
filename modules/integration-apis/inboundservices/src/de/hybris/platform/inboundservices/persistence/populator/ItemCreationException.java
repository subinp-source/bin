/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.integrationservices.model.TypeDescriptor;

/**
 * An exception indicating that an item model instance has failed to create or initialize in the type system.
 */
public class ItemCreationException extends RuntimeException
{
	private static final String MESSAGE = "There was an error creating the new [%s] model instance in the type system";
	private final transient TypeDescriptor itemType;

	public ItemCreationException(final Throwable e, final TypeDescriptor type)
	{
		super(message(type), e);
		itemType = type;
	}

	private static String message(final TypeDescriptor type)
	{
		return String.format(MESSAGE, type.getTypeCode());
	}

	public TypeDescriptor getItemType()
	{
		return itemType;
	}
}
