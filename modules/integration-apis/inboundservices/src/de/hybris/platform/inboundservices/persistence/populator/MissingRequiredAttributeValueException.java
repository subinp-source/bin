/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * An exception indicating that a value cannot be null for the given attribute
 */
public class MissingRequiredAttributeValueException extends AttributePersistenceException
{
	/**
	 * Instantiates the exception
	 * @param descriptor The {@link TypeAttributeDescriptor} that helps to formulate the exception details
	 * @param context The {@link PersistenceContext} that helps to formulate the exception details
	 */
	public MissingRequiredAttributeValueException(final TypeAttributeDescriptor descriptor, final PersistenceContext context)
	{   
		super("Property [%s] is required for EntityType [%s].", descriptor, context);
	}
}
