/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.enums.RelationEndCardinalityEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.AbstractIntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.AttributeSettableChecker;
import de.hybris.platform.integrationservices.model.AttributeValueAccessor;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.MapDescriptor;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.util.Log;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * {@inheritDoc}
 * <p>This implementation is effectively immutable and therefore is thread safe.</p>
 * <p>Reuse this class through composition, not inheritance.</p>
 */
public class DefaultTypeAttributeDescriptor extends AbstractDescriptor implements TypeAttributeDescriptor
{
	private static final Logger LOG = Log.getLogger(DefaultTypeAttributeDescriptor.class);

	private final IntegrationObjectItemAttributeModel attributeModel;
	private final AttributeDescriptorModel attributeDescriptor;
	private TypeDescriptor containerItemType;
	private TypeDescriptor attributeType;
	private final AttributeValueAccessor attributeValueAccessor;
	private final AttributeSettableChecker attributeSettableChecker;

	DefaultTypeAttributeDescriptor(final IntegrationObjectItemAttributeModel model)
	{
		this(model, null);
	}

	DefaultTypeAttributeDescriptor(final IntegrationObjectItemAttributeModel model, final DescriptorFactory descriptorFactory)
	{
		super(descriptorFactory);
		Preconditions.checkArgument(model != null, "Non-null attribute model must be provided");
		attributeModel = model;
		attributeDescriptor = model.getAttributeDescriptor();
		attributeValueAccessor = getFactory().getAttributeValueAccessorFactory().create(this);
		attributeSettableChecker = getFactory().getAttributeSettableCheckerFactory().create(this);
	}

	/**
	 * Creates new instance of the {@link TypeAttributeDescriptor}
	 *
	 * @param model a model to create the descriptor for
	 * @return a type attribute descriptor for the specified attribute model
	 * @deprecated use {@link DescriptorFactory#createTypeAttributeDescriptor(AbstractIntegrationObjectItemAttributeModel)} instead.
	 * The factory can be injected into custom code as a Spring bean.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public static TypeAttributeDescriptor create(final IntegrationObjectItemAttributeModel model)
	{
		return new DefaultTypeAttributeDescriptor(model);
	}

	@Override
	public String getAttributeName()
	{
		return attributeModel.getAttributeName();
	}

	@Override
	public String getQualifier()
	{
		return attributeModel.getAttributeDescriptor().getQualifier();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true}, if the underlying {@link AttributeDescriptorModel} represents a collection or a one-to-many
	 * relationship or, in other words, if the corresponding accessor in the generated item has a {@link java.util.Collection}
	 * return type, e.g.
	 * <pre>
	 *     public class Parent {
	 *         ...
	 *         Collection&#60Child&#62 Parent.getChildren() {...}
	 *         ...
	 *     }
	 * </pre>
	 * From the implementation standpoint it does not matter how that collection is modeled by {@link CollectionTypeModel} or
	 * {@link RelationDescriptorModel} - as long as it's a collection or one of its subtypes returned this method returns
	 * {@code true}. Otherwise, it returns {@code false}
	 */
	@Override
	public boolean isCollection()
	{
		return isCollectionAttributeModel() || isToManyRelationAttributeModel();
	}

	@Override
	public CollectionDescriptor getCollectionDescriptor()
	{
		return DefaultCollectionDescriptor.create(attributeDescriptor);
	}

	@Override
	public Optional<MapDescriptor> getMapDescriptor()
	{
		return Optional.ofNullable(createMapDescriptor());
	}

	private MapDescriptor createMapDescriptor()
	{
		try
		{
			if (isMap() && !isLocalized())
			{
				return new DefaultMapDescriptor(attributeModel);
			}
		}
		catch (final IllegalArgumentException e)
		{
			LOG.warn("Failed to create a map descriptor for {}", this, e);
		}
		return null;
	}

	private boolean isToManyRelationAttributeModel()
	{
		if (isRelationModel())
		{
			final RelationDescriptorModel relationModel = (RelationDescriptorModel) attributeDescriptor;
			final RelationEndCardinalityEnum targetCardinality = Boolean.TRUE.equals(relationModel.getIsSource())
					? relationModel.getRelationType().getTargetTypeCardinality()
					: relationModel.getRelationType().getSourceTypeCardinality();
			return targetCardinality == RelationEndCardinalityEnum.MANY;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>This implementation derives the correct {@link TypeModel} depending on whether the attribute represents a collection,
	 * a relation or a simple attribute and then creates {@link TypeDescriptor}, which is cached in this class.</p>
	 */
	@Override
	public TypeDescriptor getAttributeType()
	{
		if (attributeType == null)
		{
			final IntegrationObjectItemModel referencedItemModel = attributeModel.getReturnIntegrationObjectItem();
			attributeType = referencedItemModel != null
					? typeDescriptor(referencedItemModel)
					: createNonReferencedDescriptor();
		}
		return attributeType;
	}

	private TypeDescriptor createNonReferencedDescriptor()
	{
		return isMap() && !isLocalized() ?
				MapTypeDescriptor.create(attributeModel) :
				PrimitiveTypeDescriptor.create(getTypeDescriptor().getIntegrationObjectCode(), derivePrimitiveTypeModel());
	}

	private AtomicTypeModel derivePrimitiveTypeModel()
	{
		TypeModel typeModel = isCollectionAttributeModel()
				? ((CollectionTypeModel) attributeDescriptor.getAttributeType()).getElementType()
				: attributeDescriptor.getAttributeType();

		if (isLocalized() && isMap(typeModel))
		{
			typeModel = ((MapTypeModel) typeModel).getReturntype();
		}
		return getAtomicType(typeModel);
	}

	private AtomicTypeModel getAtomicType(final TypeModel typeModel)
	{
		if (typeModel instanceof AtomicTypeModel)
		{
			return (AtomicTypeModel) typeModel;
		}
		throw new IllegalStateException("Modeling error: " + toString() + " is not a primitive attribute");
	}

	@Override
	public TypeDescriptor getTypeDescriptor()
	{
		if (containerItemType == null)
		{
			containerItemType = typeDescriptor(attributeModel.getIntegrationObjectItem());
		}
		return containerItemType;
	}

	private TypeDescriptor typeDescriptor(final IntegrationObjectItemModel integrationObjectItem)
	{
		return getFactory().createItemTypeDescriptor(integrationObjectItem);
	}

	@Override
	public Optional<TypeAttributeDescriptor> reverse()
	{
		return isRelationModel()
				? deriveReverseRelationAttribute()
				: Optional.empty();
	}

	private Optional<TypeAttributeDescriptor> deriveReverseRelationAttribute()
	{
		final String attributeName = getRelationAttribute().getQualifier();
		return getAttributeType().getAttribute(attributeName);
	}

	@Override
	public boolean isNullable()
	{
		return (isOptional() || attributeDescriptor.getDefaultValue() != null);
	}

	@Override
	public boolean isPartOf()
	{
		return falseIfNull(attributeModel.getPartOf());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true}, if the attribute is defined with {@code partOf == true} in the type system or the corresponding
	 * {@link IntegrationObjectItemAttributeModel} has {@code autoCreate == true}; {@code false}, otherwise.
	 */
	@Override
	public boolean isAutoCreate()
	{
		return isAttributeModelAutoCreate() || isPartOf();
	}

	private boolean isOptional()
	{
		final Boolean optional = attributeDescriptor.getOptional();
		return optional == null || optional;
	}

	private boolean isCollectionAttributeModel()
	{
		return attributeDescriptor.getAttributeType() instanceof CollectionTypeModel;
	}

	private boolean isRelationModel()
	{
		return attributeDescriptor instanceof RelationDescriptorModel;
	}

	private RelationDescriptorModel getRelationAttribute()
	{
		final RelationDescriptorModel model = ((RelationDescriptorModel) attributeDescriptor).getRelationType()
		                                                                                     .getTargetAttribute();
		return model == null ? ((RelationDescriptorModel) attributeDescriptor).getRelationType().getSourceAttribute() : model;
	}

	private boolean isAttributeModelAutoCreate()
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
		return Boolean.TRUE.equals(attributeDescriptor.getLocalized());
	}

	@Override
	public boolean isPrimitive()
	{
		return getAttributeType().isPrimitive();
	}

	@Override
	public boolean isReadable()
	{
		final Boolean readable = attributeDescriptor.getReadable();
		return readable == null || readable;
	}

	@Override
	public boolean isMap()
	{
		return isMap(attributeDescriptor.getAttributeType());
	}

	private static boolean isMap(final TypeModel typeModel)
	{
		return typeModel instanceof MapTypeModel;
	}

	@Override
	public boolean isWritable()
	{
		final Boolean writable = attributeDescriptor.getWritable();
		return writable == null || writable;
	}

	@Override
	public boolean isInitializable()
	{
		final Boolean initial = attributeDescriptor.getInitial();
		return initial != null && initial;
	}

	@Override
	public boolean isSettable(final Object item)
	{
		return item instanceof ItemModel &&
				attributeSettableChecker.isSettable((ItemModel) item, this);
	}

	@Override
	public boolean isKeyAttribute()
	{
		return getTypeDescriptor().getKeyDescriptor().isKeyAttribute(getAttributeName());
	}

	@Override
	public AttributeValueAccessor accessor()
	{
		return attributeValueAccessor;
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
			final DefaultTypeAttributeDescriptor that = (DefaultTypeAttributeDescriptor) o;
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
		return "DefaultTypeAttributeDescriptor {" +
				"itemType='" + containerItemTypeName() +
				"', attributeName='" + getAttributeName() +
				"'}";
	}

	private String containerItemTypeName()
	{
		return getTypeDescriptor().getItemCode();
	}
}
