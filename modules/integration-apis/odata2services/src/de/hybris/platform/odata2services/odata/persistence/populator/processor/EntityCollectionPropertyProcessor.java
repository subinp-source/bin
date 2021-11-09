/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isAutoCreate;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isPartOf;
import static de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest.itemConversionRequestBuilder;
import static de.hybris.platform.odata2services.odata.persistence.StorageRequest.storageRequestBuilder;

import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.CollectionDescriptor;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingNavigationPropertyException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmAnnotatable;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EntityCollectionPropertyProcessor extends AbstractCollectionPropertyProcessor
{
	private ModelEntityService modelEntityService;
	private CreateItemStrategy createItemStrategy;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor typeAttributeDescriptor)
	{
		return typeAttributeDescriptor.isCollection() && !typeAttributeDescriptor.isPrimitive();
	}

	@Override
	protected void processItemInternal(final ItemModel item, final String entryPropertyName, final Object value,
			final StorageRequest request) throws EdmException
	{
		Preconditions.checkArgument(ODataFeed.class.isAssignableFrom(value.getClass()));
		final List<ODataEntry> collectionEntries = ((ODataFeed) value).getEntries();

		final Collection<ItemModel> newCollection =
				getNewCollectionEntries(request, collectionEntries, entryPropertyName, item);

		final AttributeDescriptorModel attributeDescriptor = getAttributeDescriptor(item, entryPropertyName, request);
		final String itemAttributeName = attributeDescriptor.getQualifier();

		final Collection<ItemModel> collection = getNewCollectionFor(attributeDescriptor);
		if (getModelService().getAttributeValue(item, itemAttributeName) != null)
		{
			collection.addAll(getModelService().getAttributeValue(item, itemAttributeName));
		}
		collection.addAll(newCollection.stream()
				.filter(v -> !collection.contains(v))
				.collect(Collectors.toList()));

		getModelService().setAttributeValue(item, itemAttributeName, collection);
	}

	/**
	 * @deprecated Since 1905.07-CEP Use {@link CollectionDescriptor#newCollection() }
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected Collection<ItemModel> getNewCollectionFor(final AttributeDescriptorModel attr)
	{
		final TypeModel type = attr.getAttributeType();
		return type instanceof CollectionTypeModel && ((CollectionTypeModel) type).getTypeOfCollection() == TypeOfCollectionEnum.SET
				? Sets.newLinkedHashSet()
				: Lists.newArrayList();
	}

	/**
	 * @deprecated Since 1905.07-CEP Use AbstractCollectionAttributePopulator#getNewCollection(ItemModel, TypeAttributeDescriptor, PersistenceContext)
	 *
	 * Migrate all processors of type #PropertyProcessor to #AttributePopulator and use #AbstractCollectionAttributePopulator, if needed
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected Collection<ItemModel> getNewCollectionEntries(final StorageRequest request,
			final List<ODataEntry> entries, final String propertyName, final ItemModel parent) throws EdmException
	{
		final EdmAnnotatable property = (EdmAnnotatable) request.getEntityType().getProperty(propertyName);
		final boolean partOf = isPartOf(property);
		final EdmEntitySet relatedEntitySet = request.getEntitySetReferencedByProperty(propertyName);

		final ReferencedIntegrationItems nestedItems = ReferencedIntegrationItems.createFrom(request, propertyName);
		final Collection<ItemModel> collectionItems = Lists.newLinkedList();
		for (final ODataEntry oDataEntry : entries)
		{
			final IntegrationItem nestedItem = nestedItems.findItemFor(oDataEntry);
			final StorageRequest innerStorageRequest = storageRequestBuilder().from(request)
					.withEntitySet(relatedEntitySet)
					.withODataEntry(oDataEntry)
					.withIntegrationItem(nestedItem)
					.build();
			final ItemModel item = getModelEntityService()
					.createOrUpdateItem(innerStorageRequest, getCreateItemStrategy());

			if (!partOf && !isAutoCreate(property) && getModelService().isNew(item))
			{
				throw new MissingNavigationPropertyException(request.getEntityType().getName(), propertyName);
			}

			if (partOf && getModelService().isNew(item))
			{
				item.setOwner(parent);
			}
			collectionItems.add(item);
		}
		return collectionItems;
	}

	@Override
	protected List<ODataEntry> deriveDataFeedEntries(final ItemConversionRequest request, final String propertyName, final Object value) throws EdmException
	{
		final ItemConversionRequest relatedRequest = request.propertyConversionRequest(propertyName, value);
		return getListOfODataEntries((Collection<?>) value, relatedRequest);
	}

	protected List<ODataEntry> getListOfODataEntries(final Collection<?> values, final ItemConversionRequest request)
			throws EdmException
	{
		final List<ODataEntry> list = new ArrayList<>(values.size());
		for (final Object value : values)
		{
			final ItemConversionRequest newRequest = itemConversionRequestBuilder().from(request)
					.withValue(value)
					.build();
			final ODataEntry newEntry = modelEntityService.getODataEntry(newRequest);
			list.add(newEntry);
		}
		return list;
	}

	protected ModelEntityService getModelEntityService()
	{
		return modelEntityService;
	}

	@Required
	public void setModelEntityService(final ModelEntityService modelEntityService)
	{
		this.modelEntityService = modelEntityService;
	}

	protected CreateItemStrategy getCreateItemStrategy()
	{
		return createItemStrategy;
	}

	@Required
	public void setCreateItemStrategy(final CreateItemStrategy createItemStrategy)
	{
		this.createItemStrategy = createItemStrategy;
	}

	/**
	 * A helper class for matching OData entries nested in a collection property to their corresponding {@code IntegrationItem}s.
	 * This is a temporary solution while OData entries are still used for persistence. Once they are replaced with
	 * {@code IntegrationItem} this class won't be needed.
	 */
	private static class ReferencedIntegrationItems
	{
		private final Map<String, IntegrationItem> referencedItems;

		private ReferencedIntegrationItems(final Map<String, IntegrationItem> items)
		{
			referencedItems = items;
		}

		/**
		 * Creates new instance of {@code ReferencedIntegrationItems}
		 *
		 * @param request a request containing referenced items.
		 * @param property name of the navigation property that refers to a collection of {@code IntegrationItem}s.
		 * @return new instance
		 */
		private static ReferencedIntegrationItems createFrom(final StorageRequest request, final String property)
		{
			final Collection<IntegrationItem> items = request.getIntegrationItem().getReferencedItems(property);
			final Map<String, IntegrationItem> itemsByKey = new HashMap<>(items.size());
			items.forEach(item -> itemsByKey.put(item.getIntegrationKey(), item));
			return new ReferencedIntegrationItems(itemsByKey);
		}

		IntegrationItem findItemFor(final ODataEntry entry)
		{
			final Object keyPropValue = entry.getProperties().get(IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME);
			final String key = keyPropValue != null
					? String.valueOf(keyPropValue)
					: null;
			return referencedItems.get(key);
		}
	}
}
