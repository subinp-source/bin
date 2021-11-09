/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interceptor to ensure that a valid {@code returnIntegrationObjectItem} is provided if when a referenced type
 * {@link IntegrationObjectItemAttributeModel} is being created.
 */
public class StandardAttributeReturnIntegrationObjectItemValidateInterceptor
		implements ValidateInterceptor<IntegrationObjectItemAttributeModel>
{
	private static final String MISSING_RETURN_ITEM = "Missing returnIntegrationObjectItem for referenced type attribute [%s].";
	private static final String WRONG_RETURN_ITEM = "Invalid returnIntegrationObjectItem for referenced type attribute [%s]. " +
			"ReturnIntegrationObjectItem type [%s] does not match the attributeDescriptor type or any of its super/sub types.";

	@Override
	public void onValidate(final IntegrationObjectItemAttributeModel attributeModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if (isReferencedType(attributeModel))
		{
			if (isReturnIntegrationObjectItemNotProvided(attributeModel))
			{
				throw new InterceptorException(String.format(MISSING_RETURN_ITEM, attributeModel.getAttributeName()), this);
			}
			else if (hasReturnItemNotMatchingDescriptor(attributeModel))
			{
				throw new InterceptorException(String.format(WRONG_RETURN_ITEM, attributeModel.getAttributeName(),
						attributeModel.getReturnIntegrationObjectItem().getType().getCode()), this);
			}
		}
	}

	private TypeModel getReferenceAttributeType(final IntegrationObjectItemAttributeModel attributeModel)
	{
		final AttributeDescriptorModel attributeDescriptor = attributeModel.getAttributeDescriptor();
		if (isACollection(attributeDescriptor.getAttributeType()))
		{
			return ((CollectionTypeModel) attributeDescriptor.getAttributeType()).getElementType();
		}
		return attributeDescriptor.getAttributeType();
	}

	private boolean hasReturnItemNotMatchingDescriptor(final IntegrationObjectItemAttributeModel attributeModel)
	{
		final String attrDescriptorTypeCode = getReferenceAttributeType(attributeModel).getCode();
		final ComposedTypeModel typeModel = attributeModel.getReturnIntegrationObjectItem().getType();
		return !isTypeMatchingDescriptorTypes(attrDescriptorTypeCode, typeModel);
	}

	private static boolean isTypeMatchingDescriptorTypes(final String descriptorCode, final ComposedTypeModel typeModel)
	{
		final List<String> typeCodes = Stream.of(
				typeModel.getAllSubTypes(),
				typeModel.getAllSuperTypes(),
				Collections.singletonList(typeModel))
		                                     .flatMap(Collection::stream)
		                                     .map(TypeModel::getCode)
		                                     .collect(Collectors.toList());
		return typeCodes.contains(descriptorCode);
	}

	private boolean isReturnIntegrationObjectItemNotProvided(final IntegrationObjectItemAttributeModel attributeModel)
	{
		return attributeModel.getReturnIntegrationObjectItem() == null;
	}

	private boolean isReferencedType(final IntegrationObjectItemAttributeModel attributeModel)
	{
		final AttributeDescriptorModel descriptor = attributeModel.getAttributeDescriptor();
		return Optional.ofNullable(descriptor)
		               .map(d -> !(isPrimitive(d) || isPrimitiveCollection(d) || isMap(d) || isEnum(descriptor)))
		               .orElse(false);
	}

	private boolean isEnum(final AttributeDescriptorModel descriptor)
	{
		return descriptor.getAttributeType() instanceof EnumerationMetaTypeModel;
	}

	private boolean isMap(final AttributeDescriptorModel descriptor)
	{
		return descriptor.getAttributeType() instanceof MapTypeModel;
	}

	private boolean isPrimitive(final AttributeDescriptorModel descriptor)
	{
		return descriptor.getAttributeType() instanceof AtomicTypeModel;
	}

	private boolean isPrimitiveCollection(final AttributeDescriptorModel descriptor)
	{
		final TypeModel type = descriptor.getAttributeType();
		return isACollection(type) &&
				((CollectionTypeModel) type).getElementType() instanceof AtomicTypeModel;
	}

	private boolean isACollection(final TypeModel type)
	{
		return type instanceof CollectionTypeModel;
	}
}
