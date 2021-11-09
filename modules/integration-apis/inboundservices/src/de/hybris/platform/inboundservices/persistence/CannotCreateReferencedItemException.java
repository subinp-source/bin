/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.inboundservices.persistence.populator.AttributePersistenceException;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

public class CannotCreateReferencedItemException extends AttributePersistenceException
{
	private static final String MSG = "Item referenced by attribute [%s] in [%s] item does not exist in the system. " +
			"Cannot create referenced item for this attribute because it is not partof or autocreate for the " +
			"item that it belongs to.";

	public CannotCreateReferencedItemException(final TypeAttributeDescriptor attrDescriptor, final PersistenceContext context)
	{
		super(toMessage(attrDescriptor), attrDescriptor, context);
	}

	private static String toMessage(final TypeAttributeDescriptor descriptor)
	{
		return String.format(MSG, descriptor.getAttributeName(), descriptor.getTypeDescriptor().getItemCode());
	}
}
