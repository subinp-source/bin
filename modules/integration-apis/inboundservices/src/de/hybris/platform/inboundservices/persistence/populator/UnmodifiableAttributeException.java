/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * Indicates an attempt to set a value of an unmodifiable attribute.
 */
public class UnmodifiableAttributeException extends AttributePersistenceException
{
	private static final String MESSAGE = "Value for attribute [%s.%s] cannot be set or changed";

	public UnmodifiableAttributeException(final TypeAttributeDescriptor descriptor, final PersistenceContext context, final Throwable cause)
	{
		super(toMessage(descriptor), descriptor, context, cause);
	}

	private static String toMessage(final TypeAttributeDescriptor descriptor)
	{
		return String.format(MESSAGE, descriptor.getTypeDescriptor().getItemCode(), descriptor.getAttributeName());
	}
}
