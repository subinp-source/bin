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

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.PRIMITIVE_ENTITY_PROPERTY_NAME;

import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PrimitiveCollectionPropertyProcessor extends AbstractCollectionPropertyProcessor
{
	private ModelEntityService modelEntityService;
	private CreateItemStrategy createItemStrategy;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor typeAttributeDescriptor)
	{
		return typeAttributeDescriptor.isCollection() && typeAttributeDescriptor.isPrimitive();
	}

	@Override
	protected void processItemInternal(
			final ItemModel item,
			final String propertyName,
			final Object value,
			final StorageRequest request) throws EdmException
	{
		Preconditions.checkArgument(ODataFeed.class.isAssignableFrom(value.getClass()));
		final List<ODataEntry> collectionEntries = ((ODataFeed) value).getEntries();

		final AttributeDescriptorModel attributeDescriptor = getAttributeDescriptor(item, propertyName, request);
		final Collection<Object> collection = getNewCollectionFor(attributeDescriptor);

		if (getModelService().getAttributeValue(item, propertyName) != null)
		{
			collection.addAll(getModelService().getAttributeValue(item, propertyName));
		}
		collection.addAll(
				getEntryValues(collectionEntries).stream()
						.filter(entry -> !collection.contains(entry))
						.collect(Collectors.toList()));

		getModelService().setAttributeValue(item, propertyName, collection);
	}

	@Override
	protected List<ODataEntry> deriveDataFeedEntries(final ItemConversionRequest request, final String propertyName, final Object value)
	{
		return ((Collection<?>) value).stream()
			.map(this::createPrimitiveValueEntry)
			.collect(Collectors.toList());
	}

	private ODataEntry createPrimitiveValueEntry(final Object value)
	{
		final ODataEntry entry = new ODataEntryImpl(Maps.newHashMap(), new MediaMetadataImpl(),
				new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl());
		entry.getProperties().putIfAbsent(PRIMITIVE_ENTITY_PROPERTY_NAME, value);
		return entry;
	}

	/**
	 * @deprecated not used anymore because item population is done by {@link de.hybris.platform.inboundservices.persistence.populator.PrimitiveCollectionAttributePopulator}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected List<Object> getEntryValues(final List<ODataEntry> collectionEntries)
	{
		return collectionEntries.stream()
				.map(ODataEntry::getProperties)
				.map(m -> m.get(PRIMITIVE_ENTITY_PROPERTY_NAME)).collect(Collectors.toList());
	}

	/**
	 * @deprecated not used anymore because item population is done by {@link de.hybris.platform.inboundservices.persistence.populator.PrimitiveCollectionAttributePopulator}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected Collection<Object> getNewCollectionFor(final AttributeDescriptorModel attr)
	{
		final TypeOfCollectionEnum type = ((CollectionTypeModel) attr.getAttributeType()).getTypeOfCollection();

		if (type == TypeOfCollectionEnum.SET)
		{
			return Sets.newLinkedHashSet();
		}

		return Lists.newArrayList();
	}

	/**
	 * @deprecated This is not being used in this class and will be removed
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected ModelEntityService getModelEntityService()
	{
		return modelEntityService;
	}

	/**
	 * @deprecated This is not being used in this class and will be removed
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	@Required
	public void setModelEntityService(final ModelEntityService modelEntityService)
	{
		this.modelEntityService = modelEntityService;
	}

	/**
	 * @deprecated This is not being used in this class and will be removed
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected CreateItemStrategy getCreateItemStrategy()
	{
		return createItemStrategy;
	}

	/**
	 * @deprecated This is not being used in this class and will be removed
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	@Required
	public void setCreateItemStrategy(final CreateItemStrategy createItemStrategy)
	{
		this.createItemStrategy = createItemStrategy;
	}
}
