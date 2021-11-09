/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.MapDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.function.Function;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@code MapDescriptor}, which provides functionality based on {@link MapTypeModel}
 */
public class DefaultMapDescriptor implements MapDescriptor
{
	private final TypeDescriptor keyType;
	private final TypeDescriptor valueType;

	public DefaultMapDescriptor(final IntegrationObjectItemAttributeModel model)
	{
		keyType = createTypeDescriptor(model, MapTypeModel::getArgumentType);
		valueType = createTypeDescriptor(model, MapTypeModel::getReturntype);
	}

	@Override
	public TypeDescriptor getKeyType()
	{
		return keyType;
	}

	@Override
	public TypeDescriptor getValueType()
	{
		return valueType;
	}

	private TypeDescriptor createTypeDescriptor(final IntegrationObjectItemAttributeModel attr,
	                                            final Function<MapTypeModel, TypeModel> typeExtractor)
	{
		Preconditions.checkArgument(attr != null, "IntegrationObjectItemAttributeModel cannot be null");
		Preconditions.checkArgument(attr.getAttributeDescriptor() != null, "Attribute descriptor cannot be null");
		Preconditions.checkArgument(attr.getAttributeDescriptor().getAttributeType() instanceof MapTypeModel,
				"Only attributes of MapTypeModel type can be used to instantiate this MapDescriptor");

		final MapTypeModel mapModel = (MapTypeModel) attr.getAttributeDescriptor().getAttributeType();
		final TypeModel type = typeExtractor.apply(mapModel);
		if (type instanceof AtomicTypeModel)
		{
			final String ioCode = attr.getIntegrationObjectItem().getIntegrationObject().getCode();
			return PrimitiveTypeDescriptor.create(ioCode, (AtomicTypeModel) type);
		}
		throw new IllegalArgumentException(
				"Only attributes of MapTypeModel with primitive attribute and return types can be used to instantiate this MapDescriptor");
	}
}
