/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

public class MissingRequiredMapKeyValueException extends AttributePersistenceException
{
	public MissingRequiredMapKeyValueException(final TypeAttributeDescriptor descriptor,
	                                           final PersistenceContext context)
	{
		super("Property [%s.key] of type " + descriptor.getAttributeType().getTypeCode() + " is required for EntityType [%s].", descriptor, context);
	}
}
