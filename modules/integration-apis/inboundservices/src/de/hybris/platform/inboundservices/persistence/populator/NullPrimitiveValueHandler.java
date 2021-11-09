/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * An implementation of {@link PrimitiveValueHandler} that returns whatever value is passed into it.
 */
public class NullPrimitiveValueHandler implements PrimitiveValueHandler
{
    @Override
    public Object convert(final TypeAttributeDescriptor attribute, final Object value)
    {
        return value;
    }
}
