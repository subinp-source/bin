/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * A value handler for converting primitive value from Olingo format to a format accepted by the model service.
 */
public interface PrimitiveValueHandler
{
    /**
     * Converts primitive value to a format that is accepted by the model service.
     * @param attribute the attribute that contains the value
     * @param value value to be converted
     * @return the converted value
     */
    Object convert(TypeAttributeDescriptor attribute, Object value);
}
