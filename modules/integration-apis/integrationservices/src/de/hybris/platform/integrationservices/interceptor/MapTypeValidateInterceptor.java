/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * Interceptor to validate that only {@link de.hybris.platform.core.model.type.MapTypeModel} with primitive
 * {@code argumentType} and {@code returnType} are allowed, or localized and {@code returnType} is primitive.
 */
public class MapTypeValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemAttributeModel>
{
    private static final String ERROR_MSG = "Map type attribute [%s] must have AtomicTypeModel argumentType and returnType, or must be " +
            "localized with AtomicTypeModel returnType";

    @Override
    public void onValidate(final IntegrationObjectItemAttributeModel attributeModel,
                           final InterceptorContext interceptorContext) throws InterceptorException
    {
        if (!isSupportedMapType(attributeModel))
        {
            throw new InterceptorException(String.format(ERROR_MSG, attributeModel.getAttributeName()), this);
        }
    }

    private boolean isSupportedMapType(final IntegrationObjectItemAttributeModel attributeModel)
    {
        final var descriptor = attributeModel.getAttributeDescriptor();
        return descriptor == null || !isMap(descriptor) || isPrimitiveMap(descriptor) || isLocalized(descriptor);
    }

    private boolean isMap(final AttributeDescriptorModel descriptor)
    {
        return descriptor.getAttributeType() instanceof MapTypeModel;
    }

    private boolean isLocalized(final AttributeDescriptorModel descriptor)
    {
        final Boolean localized = descriptor.getLocalized();
        return Boolean.TRUE.equals(localized) &&
                descriptor.getAttributeType() instanceof MapTypeModel;
    }

    private boolean isPrimitiveMap(final AttributeDescriptorModel descriptor)
    {
        final var type = descriptor.getAttributeType();
        return type instanceof MapTypeModel &&
                ((MapTypeModel) type).getReturntype() instanceof AtomicTypeModel &&
                ((MapTypeModel) type).getArgumentType() instanceof AtomicTypeModel;
    }
}
