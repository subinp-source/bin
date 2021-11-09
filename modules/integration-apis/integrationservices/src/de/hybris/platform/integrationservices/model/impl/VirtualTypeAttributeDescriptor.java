/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.AttributeValueAccessor;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * {@inheritDoc}
 * <p>Implementation of {@code TypeAttributeDescriptor} based on {@code IntegrationObjectItemVirtualAttributeModel}</p>
 * <p>This implementation is effectively immutable and therefore is thread safe.</p>
 * <p>Reuse this class through composition, not inheritance.</p>
 */
public class VirtualTypeAttributeDescriptor extends AbstractDescriptor implements TypeAttributeDescriptor
{
	private final IntegrationObjectItemVirtualAttributeModel attributeModel;
	private final AttributeValueAccessor attributeValueAccessor;
	private TypeDescriptor containerItemType;
	private TypeDescriptor attributeType;

	VirtualTypeAttributeDescriptor(@NotNull final IntegrationObjectItemVirtualAttributeModel attribute,
	                               final DescriptorFactory descriptorFactory)
	{
		super(descriptorFactory);
		Preconditions.checkArgument(attribute != null, "Non-null attribute model must be provided");
		attributeModel = attribute;
		attributeValueAccessor = getFactory().getAttributeValueAccessorFactory().create(this);
	}

	@Override
	public String getAttributeName()
	{
		return attributeModel.getAttributeName();
	}

	@Override
	public String getQualifier()
	{
		return attributeModel.getAttributeName();
	}

	@Override
	public boolean isCollection()
	{
		return false;
	}

	@Override
	public TypeDescriptor getAttributeType()
	{
		if (attributeType == null)
		{
			attributeType = new PrimitiveTypeDescriptor(getTypeDescriptor().getIntegrationObjectCode(), derivePrimitiveType());
		}
		return attributeType;
	}

	@Override
	public TypeDescriptor getTypeDescriptor()
	{
		if (containerItemType == null)
		{
			containerItemType = getFactory().createItemTypeDescriptor(attributeModel.getIntegrationObjectItem());
		}
		return containerItemType;
	}

	@Override
	public boolean isNullable()
	{
		return true;
	}

	@Override
	public boolean isAutoCreate()
	{
		return false;
	}

	@Override
	public boolean isLocalized()
	{
		return false;
	}

	@Override
	public boolean isPrimitive()
	{
		return true;
	}

	@Override
	public boolean isWritable()
	{
		return false;
	}

	@Override
	public boolean isInitializable()
	{
		return false;
	}

	@Override
	public boolean isSettable(final Object item)
	{
		return false;
	}

	@Override
	public CollectionDescriptor getCollectionDescriptor()
	{
		return null;
	}

	@Override
	public AttributeValueAccessor accessor()
	{
		return attributeValueAccessor;
	}

	public String getLogicLocation()
	{
		return attributeModel.getRetrievalDescriptor().getLogicLocation();
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o != null && getClass() == o.getClass())
		{
			final VirtualTypeAttributeDescriptor that = (VirtualTypeAttributeDescriptor) o;
			return Objects.equals(getAttributeName(), that.getAttributeName()) &&
					Objects.equals(containerItemTypeName(), that.containerItemTypeName());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(containerItemTypeName(), getAttributeName());
	}

	@Override
	public String toString()
	{
		return "VirtualAttributeTypeAttributeDescriptor {" +
				"itemType='" + containerItemTypeName() +
				"', attributeName='" + getAttributeName() +
				"'}";
	}

	private AtomicTypeModel derivePrimitiveType()
	{
		final TypeModel type = attributeModel.getRetrievalDescriptor().getType();
		if (type instanceof AtomicTypeModel)
		{
			return (AtomicTypeModel) type;
		}
		else
		{
			throw new IllegalArgumentException(
					"Only virtual attributes with primitive type can be used");
		}
	}

	private String containerItemTypeName()
	{
		return getTypeDescriptor().getItemCode();
	}
}
