/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import static de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum.REFERENCE;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.integrationservices.model.AttributeSettableChecker;
import de.hybris.platform.integrationservices.model.AttributeValueAccessor;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * {@inheritDoc}
 * <p>Implementation of {@code TypeAttributeDescriptor} based on {@code IntegrationObjectItemClassificationAttributeModel}</p>
 * <p>This implementation is effectively immutable and therefore is thread safe.</p>
 * <p>Reuse this class through composition, not inheritance.</p>
 */
public class ClassificationTypeAttributeDescriptor extends AbstractDescriptor implements TypeAttributeDescriptor
{
	private final IntegrationObjectItemClassificationAttributeModel attributeModel;
	private final ClassAttributeAssignmentModel classAttributeAssignment;
	private TypeDescriptor containerItemType;
	private TypeDescriptor attributeType;
	private final AttributeValueAccessor attributeValueAccessor;
	private final AttributeSettableChecker attributeSettableChecker;

	ClassificationTypeAttributeDescriptor(@NotNull final IntegrationObjectItemClassificationAttributeModel attribute)
	{
		this(attribute, null);
	}

	ClassificationTypeAttributeDescriptor(@NotNull final IntegrationObjectItemClassificationAttributeModel attribute, final DescriptorFactory descriptorFactory)
	{
		super(descriptorFactory);
		Preconditions.checkArgument(attribute != null, "Non-null attribute model must be provided");
		classAttributeAssignment = attribute.getClassAttributeAssignment();
		attributeModel = attribute;
		attributeValueAccessor = getFactory().getAttributeValueAccessorFactory().create(this);
		attributeSettableChecker = getFactory().getAttributeSettableCheckerFactory().create(this);
	}

	@Override
	public String getAttributeName()
	{
		return attributeModel.getAttributeName();
	}

	@Override
	public String getQualifier()
	{
		return classAttributeAssignment.getClassificationAttribute().getCode();
	}

	@Override
	public boolean isCollection()
	{
		return falseIfNull(classAttributeAssignment.getMultiValued());
	}

	@Override
	public TypeDescriptor getAttributeType()
	{
		if (attributeType == null)
		{
			attributeType = deriveAttributeType();
		}
		return attributeType;
	}

	private TypeDescriptor deriveAttributeType()
	{
		return isPrimitive() ?
				PrimitiveTypeDescriptor.create(getTypeDescriptor().getIntegrationObjectCode(), derivePrimitiveTypeModel()) :
				getFactory().createItemTypeDescriptor(attributeModel.getReturnIntegrationObjectItem());
	}

	private AtomicTypeModel derivePrimitiveTypeModel()
	{
		final ClassificationAttributeTypeEnum attributeTypeEnum = classAttributeAssignment.getAttributeType();
		final AtomicTypeModel typeModel = new AtomicTypeModel();
		switch (attributeTypeEnum)
		{
			case STRING:
			case ENUM:
				typeModel.setCode(String.class.getName());
				typeModel.setJavaClass(String.class);
				break;
			case NUMBER:
				typeModel.setCode(Double.class.getName());
				typeModel.setJavaClass(Number.class);
				break;
			case BOOLEAN:
				typeModel.setCode(Boolean.class.getName());
				typeModel.setJavaClass(Boolean.class);
				break;
			case DATE:
				typeModel.setCode(Date.class.getName());
				typeModel.setJavaClass(Date.class);
				break;
			default:
				throw new IllegalArgumentException("Modeling error: [" + attributeTypeEnum.getCode() + "] is not a primitive classification attribute");
		}
		return typeModel;
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
		return !classAttributeAssignment.getMandatory();
	}

	@Override
	public boolean isAutoCreate()
	{
		return falseIfNull(attributeModel.getAutoCreate());
	}

	private static boolean falseIfNull(final Boolean value)
	{
		return Boolean.TRUE.equals(value);
	}

	@Override
	public boolean isLocalized()
	{
		return Boolean.TRUE.equals(classAttributeAssignment.getLocalized());
	}

	@Override
	public boolean isPrimitive()
	{
		return !REFERENCE.equals(classAttributeAssignment.getAttributeType());
	}

	@Override
	public boolean isWritable()
	{
		return true;
	}

	@Override
	public boolean isInitializable()
	{
		return true;
	}

	@Override
	public boolean isSettable(final Object item)
	{
		return item instanceof ItemModel &&
				attributeSettableChecker.isSettable((ItemModel) item, this);
	}

	@Override
	public boolean isReadable()
	{
		return !ClassificationAttributeVisibilityEnum.NOT_VISIBLE.equals(classAttributeAssignment.getVisibility());
	}

	@Override
	public CollectionDescriptor getCollectionDescriptor()
	{
		return ClassificationCollectionDescriptor.create(classAttributeAssignment);
	}

	@Override
	public AttributeValueAccessor accessor()
	{
		return attributeValueAccessor;
	}

	private String containerItemTypeName()
	{
		return getTypeDescriptor().getItemCode();
	}


	ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		return classAttributeAssignment;
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
			final ClassificationTypeAttributeDescriptor that = (ClassificationTypeAttributeDescriptor) o;
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
		return "ClassificationTypeAttributeDescriptor {" +
				"itemType='" + containerItemTypeName() +
				"', attributeName='" + getAttributeName() +
				"'}";
	}
}
