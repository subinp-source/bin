/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.model.AbstractIntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.KeyDescriptor;
import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * {@inheritDoc}
 * <p>This implementation is effectively immutable and therefore is thread safe</p>
 * <p>Reuse this implementation through composition not inheritance</p>
 */
public class ItemTypeDescriptor extends AbstractDescriptor implements TypeDescriptor
{
	private final IntegrationObjectItemModel itemTypeModel;
	private final Map<String, TypeAttributeDescriptor> attributeDescriptors;
	private IntegrationObjectDescriptor integrationObjectDescriptor;

	ItemTypeDescriptor(@NotNull final IntegrationObjectItemModel model)
	{
		this(model, null);
	}

	ItemTypeDescriptor(@NotNull final IntegrationObjectItemModel model, final DescriptorFactory factory)
	{
		super(factory);
		Preconditions.checkArgument(model != null, "Non-null integration object item model must be provided");
		itemTypeModel = model;
		attributeDescriptors = initializeAttributeDescriptors(model);
	}

	/**
	 * @deprecated use {@link DescriptorFactory#createItemTypeDescriptor(IntegrationObjectItemModel)} instead.
	 * The factory can be injected into custom code as a Spring bean.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public static TypeDescriptor create(final IntegrationObjectItemModel model)
	{
		return new ItemTypeDescriptor(model);
	}

	private Map<String, TypeAttributeDescriptor> initializeAttributeDescriptors(final IntegrationObjectItemModel model)
	{
		return Stream.of(model.getAttributes(), model.getClassificationAttributes(), model.getVirtualAttributes())
		             .filter(Objects::nonNull)
		             .flatMap(Collection::stream)
		             .map(this::createAttributeDescriptor)
		             .collect(Collectors.toMap(TypeAttributeDescriptor::getAttributeName, Function.identity()));
	}

	@Override
	public String getIntegrationObjectCode()
	{
		return getIntegrationObjectDescriptor().getCode();
	}

	@Override
	public String getItemCode()
	{
		return itemTypeModel.getCode();
	}

	@Override
	public String getTypeCode()
	{
		return itemTypeModel.getType().getCode();
	}

	@Override
	public Optional<TypeAttributeDescriptor> getAttribute(final String attrName)
	{
		return Optional.ofNullable(attributeDescriptors.get(attrName));
	}

	@Override
	public Collection<TypeAttributeDescriptor> getAttributes()
	{
		return new HashSet<>(attributeDescriptors.values());
	}

	@Override
	public boolean isPrimitive()
	{
		return false;
	}

	@Override
	public boolean isEnumeration()
	{
		return itemTypeModel.getType() instanceof EnumerationMetaTypeModel;
	}

	@Override
	public boolean isAbstract()
	{
		return falseIfNull(itemTypeModel.getType().getAbstract());
	}

	@Override
	public boolean isInstance(final Object obj)
	{
		if (obj instanceof ItemModel)
		{
			final Collection<String> typeHierarchy = itemTypeModel.getType().getAllSubTypes().stream()
			                                                      .map(ComposedTypeModel::getCode)
			                                                      .collect(Collectors.toSet());
			typeHierarchy.add(itemTypeModel.getType().getCode());
			return typeHierarchy.contains(((ItemModel) obj).getItemtype());
		}
		else if (obj instanceof HybrisEnumValue)
		{
			return ((HybrisEnumValue) obj).getType().equals(itemTypeModel.getType().getCode());
		}
		return false;
	}

	@Override
	public boolean isRoot()
	{
		return Boolean.TRUE.equals(itemTypeModel.getRoot());
	}

	@Override
	public KeyDescriptor getKeyDescriptor()
	{
		return ItemKeyDescriptor.create(itemTypeModel);
	}

	@Override
	public List<ReferencePath> getPathsToRoot()
	{
		return getIntegrationObjectDescriptor().getRootItemType()
		                                       .map(rootType -> AttributePathFinder.findPaths(this, rootType))
		                                       .orElse(Collections.emptyList());
	}

	@Override
	public boolean hasPathToRoot()
	{
		return !getPathsToRoot().isEmpty();
	}

	private TypeAttributeDescriptor createAttributeDescriptor(final AbstractIntegrationObjectItemAttributeModel model)
	{
		return getFactory().createTypeAttributeDescriptor(model);
	}

	private IntegrationObjectDescriptor getIntegrationObjectDescriptor()
	{
		if (integrationObjectDescriptor == null)
		{
			integrationObjectDescriptor = getFactory().createIntegrationObjectDescriptor(itemTypeModel.getIntegrationObject());
		}
		return integrationObjectDescriptor;
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
			final ItemTypeDescriptor that = (ItemTypeDescriptor) o;
			return Objects.equals(integrationObjectName(), that.integrationObjectName())
					&& Objects.equals(getItemCode(), that.getItemCode());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(integrationObjectName(), getItemCode());
	}

	@Override
	public String toString()
	{
		return "ItemTypeDescriptor{" +
				"integrationObject='" + integrationObjectName() +
				"', typeCode='" + getItemCode() +
				"'}";
	}

	private String integrationObjectName()
	{
		return itemTypeModel.getIntegrationObject() != null
				? getIntegrationObjectCode()
				: "";
	}

	IntegrationObjectItemModel getItemTypeModel()
	{
		return itemTypeModel;
	}
}
