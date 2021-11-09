/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Replaces existing collection values with given collection of primitives
 */
public class ReplacePrimitiveCollectionAttributePopulator extends AbstractAttributePopulator
{
    private PrimitiveValueHandler primitiveValueHandler = new NullPrimitiveValueHandler();

    protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
    {
        return context.isReplaceAttributes() && attribute.isPrimitive() && attribute.isCollection();
    }

    @Override
    protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute,
                                     final PersistenceContext context)
    {
        final Collection<?> newValues = derivePayloadValues(attribute, context);
        attribute.accessor().setValue(item, newValues);
    }

    private Collection<?> derivePayloadValues(final TypeAttributeDescriptor attribute, final PersistenceContext context)
    {
        final var payloadValues = context.getIntegrationItem().getAttribute(attribute);
        return payloadValues instanceof Collection
                ? convertPayloadValuesToCollectionTypeDefinedInTheAttribute(attribute, (Collection<?>) payloadValues)
                : null;
    }

    private Collection<?> convertPayloadValuesToCollectionTypeDefinedInTheAttribute(final TypeAttributeDescriptor attribute,
                                                                                    final Collection<?> payloadValues)
    {
        final var newValue = attribute.getCollectionDescriptor().newCollection();
        final var convertedValues = convertValues(attribute, payloadValues);
        newValue.addAll(convertedValues);
        return newValue;
    }

    private List<Object> convertValues(final TypeAttributeDescriptor attribute, final Collection<?> payloadValues)
    {
        return payloadValues.stream()
                            .filter(Objects::nonNull)
                            .map(value -> primitiveValueHandler.convert(attribute, value))
                            .collect(Collectors.toList());
    }

    public void setPrimitiveValueHandler(final PrimitiveValueHandler primitiveValueHandler)
    {
        this.primitiveValueHandler = primitiveValueHandler;
    }
}
