/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

public class PrimitiveAttributePopulator extends AbstractAttributePopulator
{
    private PrimitiveValueHandler primitiveValueHandler = new NullPrimitiveValueHandler();

    @Override
    protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
    {
        return attribute.isPrimitive() && !attribute.isCollection();
    }

    @Override
    protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute,
                                     final PersistenceContext context)
    {
        if (!existingKeyAttribute(item, attribute))
        {
            final Object payloadValue = context.getIntegrationItem().getAttribute(attribute);
            nonNullableAttributeValueValidation(attribute, context, payloadValue);
            final var attrValue = primitiveValueHandler.convert(attribute, payloadValue);
            attribute.accessor().setValue(item, attrValue);
        }
    }

    private void nonNullableAttributeValueValidation(final TypeAttributeDescriptor attribute, final PersistenceContext context,
                                                     final Object payloadValue)
    {
        if (payloadValue == null && !attribute.isNullable())
        {
            throw new MissingRequiredAttributeValueException(attribute, context);
        }
    }

    private boolean existingKeyAttribute(final ItemModel item, final TypeAttributeDescriptor attribute)
    {
        return !isNew(item) && attribute.isKeyAttribute();
    }

    public void setPrimitiveValueHandler(final PrimitiveValueHandler primitiveValueHandler)
    {
        this.primitiveValueHandler = primitiveValueHandler;
    }
}
