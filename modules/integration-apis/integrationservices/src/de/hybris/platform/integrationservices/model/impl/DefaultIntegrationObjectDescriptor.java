/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

/**
 * Default implementation of {@link IntegrationObjectDescriptor} based on {@link IntegrationObjectModel} data structure.
 */
public class DefaultIntegrationObjectDescriptor extends AbstractDescriptor implements IntegrationObjectDescriptor
{
	private final IntegrationObjectModel integrationObjectModel;
	private Set<TypeDescriptor> integrationObjectItems;

	private DefaultIntegrationObjectDescriptor(final IntegrationObjectModel model)
	{
		this(model, null);
	}

	DefaultIntegrationObjectDescriptor(final IntegrationObjectModel model, final DescriptorFactory factory)
	{
		super(factory);
		Preconditions.checkArgument(model != null, "IntegrationObjectModel is required");
		integrationObjectModel = model;
	}

	/**
	 * @deprecated use {@link DescriptorFactory#createIntegrationObjectDescriptor(IntegrationObjectModel)} instead.
	 * The factory can be injected into custom code as a Spring bean.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public static IntegrationObjectDescriptor create(final IntegrationObjectModel model)
	{
		return new DefaultIntegrationObjectDescriptor(model);
	}

	@Override
	public String getCode()
	{
		return integrationObjectModel.getCode();
	}

	@Override
	public Set<TypeDescriptor> getItemTypeDescriptors()
	{
		if (integrationObjectItems == null)
		{
			integrationObjectItems = integrationObjectModel.getItems().stream()
					.map(item -> getFactory().createItemTypeDescriptor(item))
					.collect(Collectors.toSet());
		}
		return integrationObjectItems;
	}

	@Override
	public Optional<TypeDescriptor> getItemTypeDescriptor(final ItemModel item)
	{
		final Optional<TypeDescriptor> exactMatch = findMatchingTypeDescriptor(d -> d.getTypeCode().equals(item.getItemtype()));
		return exactMatch.isPresent()
				? exactMatch
				: findMatchingTypeDescriptor(type -> type.isInstance(item));
	}

	private Optional<TypeDescriptor> findMatchingTypeDescriptor(final Predicate<TypeDescriptor> predicate)
	{
		return getItemTypeDescriptors().stream()
				.filter(predicate)
				.findAny();
	}

	@Override
	public Optional<TypeDescriptor> getRootItemType()
	{
		final IntegrationObjectItemModel rootItem = integrationObjectModel.getRootItem();
		return Optional.ofNullable(rootItem)
				.map(model -> getFactory().createItemTypeDescriptor(model));
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final DefaultIntegrationObjectDescriptor that = (DefaultIntegrationObjectDescriptor) o;

		return integrationObjectModel.getCode().equals(that.integrationObjectModel.getCode());
	}

	@Override
	public int hashCode()
	{
		return integrationObjectModel.getCode().hashCode();
	}

	@Override
	public String toString()
	{
		return "IntegrationObject{" +
				"code=" + integrationObjectModel.getCode() +
				'}';
	}
}
