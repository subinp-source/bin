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
package de.hybris.platform.odata2services.odata.persistence.impl;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.CannotCreateReferencedItemException;
import de.hybris.platform.inboundservices.persistence.ContextItemModelService;
import de.hybris.platform.inboundservices.persistence.ItemModelPopulator;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService;
import de.hybris.platform.inboundservices.persistence.validation.ItemPersistRequestValidator;
import de.hybris.platform.inboundservices.persistence.validation.PersistenceContextValidator;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.search.ItemSearchResult;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupStrategy;
import de.hybris.platform.odata2services.odata.persistence.populator.EntityModelPopulator;
import de.hybris.platform.odata2services.odata.persistence.validator.CreateItemValidator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;

/**
 * Default implementation for {@link ModelEntityService}
 */
public class DefaultModelEntityService implements ModelEntityService, ContextItemModelService, ContextReferencedItemModelService
{
	private static final Logger LOG = Log.getLogger(DefaultModelEntityService.class);
	private EntityModelPopulator entityModelPopulator;
	private ItemModelPopulator itemModelPopulator;
	private ItemSearchService searchService;
	private List<CreateItemValidator> createItemValidators = Collections.emptyList();
	private IntegrationKeyValueGenerator<EdmEntitySet, ODataEntry> integrationKeyValueGenerator;
	private CreateItemStrategy createItemStrategy;
	private ModelService modelService;
	private List<PersistenceContextValidator> persistenceContextValidators = Collections.emptyList();
	private List<ItemPersistRequestValidator> itemPersistRequestValidators = Collections.emptyList();
	private IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry> keyValueGenerator;
	private ItemTypeDescriptorService itemTypeDescriptorService;
	private ContextReferencedItemModelService contextReferencedItemModelService;
	private ContextItemModelService contextItemModelService;

	/**
	 * @deprecated use {@link de.hybris.platform.inboundservices.persistence.impl.DefaultContextReferencedItemModelService#deriveItemsReferencedInAttributeValue(PersistenceContext, TypeAttributeDescriptor)}
	 */
	@Deprecated(since = "21.05.0-RC1", forRemoval = true)
	@Override
	public Collection<ItemModel> deriveItemsReferencedInAttributeValue(final PersistenceContext context,
	                                                                   final TypeAttributeDescriptor attribute)
	{
		return contextReferencedItemModelService != null
				? contextReferencedItemModelService.deriveItemsReferencedInAttributeValue(context, attribute)
				: deriveItemsReferencedInAttributeValueFallback(context, attribute);
	}

	private List<ItemModel> deriveItemsReferencedInAttributeValueFallback(final PersistenceContext context,
	                                                                      final TypeAttributeDescriptor attribute)
	{
		return context.getReferencedContexts(attribute).stream()
		              .map(refItemCtx -> deriveReferencedItemModel(attribute, refItemCtx))
		              .collect(Collectors.toList());
	}

	/**
	 * @deprecated use {@link de.hybris.platform.inboundservices.persistence.impl.DefaultContextReferencedItemModelService#deriveReferencedItemModel(TypeAttributeDescriptor, PersistenceContext)}
	 */
	@Deprecated(since = "21.05.0-RC1", forRemoval = true)
	@Override
	public ItemModel deriveReferencedItemModel(final TypeAttributeDescriptor attribute,
	                                           final PersistenceContext referencedItemContext)
	{
		return contextReferencedItemModelService != null
				? contextReferencedItemModelService.deriveReferencedItemModel(attribute, referencedItemContext)
				: deriveReferencedItemModelFallback(attribute, referencedItemContext);
	}

	private ItemModel deriveReferencedItemModelFallback(final TypeAttributeDescriptor attribute,
	                                                    final PersistenceContext referencedItemContext)
	{
		final ItemModel item = findOrCreateItem(referencedItemContext);
		return Optional.ofNullable(item)
		               .orElseThrow(() -> new CannotCreateReferencedItemException(attribute, referencedItemContext));
	}

	/**
	 * @deprecated use {@link de.hybris.platform.inboundservices.persistence.impl.DefaultContextItemModelService#findOrCreateItem(PersistenceContext)}
	 */
	@Deprecated(since = "21.05.0-RC1", forRemoval = true)
	@Override
	public ItemModel findOrCreateItem(final PersistenceContext context)
	{
		return contextItemModelService != null
				? contextItemModelService.findOrCreateItem(context)
				: findOrCreateItemFallback((StorageRequest) context);
	}

	private ItemModel findOrCreateItemFallback(final StorageRequest context)
	{
		try
		{
			return createOrUpdateItem(context, getCreateItemStrategy());
		}
		catch (final EdmException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	@Override
	public ItemModel createOrUpdateItem(final StorageRequest request, final CreateItemStrategy createItemStrategy)
			throws EdmException
	{
		if (request.getContextItem().isPresent())
		{
			return request.getContextItem().get();
		}

		LOG.trace("Converting IntegrationItem To ItemModel: {}", request.getIntegrationItem());
		final ItemModel item = getItem(request, createItemStrategy);
		if (item != null)
		{
			populateItem(request, item);
		}
		return item;
	}

	/**
	 * @deprecated No alternative method
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected ItemModel getItem(final StorageRequest request, final CreateItemStrategy createItemStrategy) throws EdmException
	{
		ItemModel item = lookup(request.toLookupRequest());
		if (item == null && request.isItemCanBeCreated())
		{
			item = createItem(request, createItemStrategy);
		}
		request.putItem(item);
		return item;
	}

	@Override
	public ODataEntry getODataEntry(final ItemConversionRequest conversionRequest) throws EdmException
	{
		final ODataEntry entry = new ODataEntryImpl(Maps.newHashMap(), new MediaMetadataImpl(),
				new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl());
		entityModelPopulator.populateEntity(entry, conversionRequest);
		entry.getProperties().put(INTEGRATION_KEY_PROPERTY_NAME, getIntegrationKey(conversionRequest, entry));
		return entry;
	}

	private String getIntegrationKey(final ItemConversionRequest conversionRequest, final ODataEntry entry) throws EdmException
	{
		final String typeCode = conversionRequest.getEntityType().getName();
		final TypeDescriptor typeDescriptor = getItemTypeDescriptorService().getTypeDescriptor(
				conversionRequest.getIntegrationObjectCode(), typeCode)
		                                                                    .orElseThrow(
				                                                                    () -> new IntegrationObjectItemNotFoundException(
						                                                                    conversionRequest.getIntegrationObjectCode(),
						                                                                    typeCode));

		return getKeyValueGenerator().generate(typeDescriptor, entry);
	}

	@Override
	public String addIntegrationKeyToODataEntry(final EdmEntitySet entitySet, final ODataEntry oDataEntry)
	{
		final String integrationKey = integrationKeyValueGenerator.generate(entitySet, oDataEntry);
		oDataEntry.getProperties().put(INTEGRATION_KEY_PROPERTY_NAME, integrationKey);
		return integrationKey;
	}

	@Override
	public ItemModel lookup(final ItemLookupRequest lookupRequest) throws EdmException
	{
		for (final CreateItemValidator validator : createItemValidators)
		{
			validator.beforeItemLookup(lookupRequest.getEntityType(), lookupRequest.getODataEntry());
		}

		return searchService.findUniqueItem(lookupRequest).orElse(null);
	}

	@Override
	public ItemLookupResult<ItemModel> lookupItems(final ItemLookupRequest lookupRequest)
	{
		final ItemSearchResult<ItemModel> items = searchService.findItems(lookupRequest);
		return ItemLookupResult.createFrom(items.getItems(), items.getTotalCount().orElse(-1));
	}

	@Override
	public int count(final ItemLookupRequest lookupRequest)
	{
		return searchService.countItems(lookupRequest);
	}

	protected void populateItem(final StorageRequest request, final ItemModel item)
	{
		try
		{
			for (final CreateItemValidator validator : createItemValidators)
			{
				validator.beforePopulateItem(request.getEntityType(), request.getODataEntry());
			}
			getItemModelPopulator().populate(item, request);
			for (final ItemPersistRequestValidator validator : itemPersistRequestValidators)
			{
				validator.validate(request, item);
			}
			getEntityModelPopulator().populateItem(item, request);
		}
		catch (final EdmException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	protected ItemModel createItem(final StorageRequest request, final CreateItemStrategy createItemStrategy) throws EdmException
	{
		for (final CreateItemValidator validator : createItemValidators)
		{
			validator.beforeCreateItem(request.getEntityType(), request.getODataEntry());
		}
		persistenceContextValidators.forEach(v -> v.validate(request));
		return createItemStrategy.createItem(request);
	}

	@Required
	public void setSearchService(final ItemSearchService service)
	{
		searchService = service;
	}

	/**
	 * Injects {@code ItemLookupStrategy} implementation to be used by this service
	 *
	 * @param itemLookupStrategy strategy implementation to use.
	 * @deprecated use {@link #setSearchService(ItemSearchService)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	public void setItemLookupStrategy(final ItemLookupStrategy itemLookupStrategy)
	{
		// The service is not used anymore - nothing to do here. This method is left only to keep backwards compatibility.
	}

	protected EntityModelPopulator getEntityModelPopulator()
	{
		return entityModelPopulator;
	}

	@Required
	public void setEntityModelPopulator(final EntityModelPopulator entityModelPopulator)
	{
		this.entityModelPopulator = entityModelPopulator;
	}

	protected List<CreateItemValidator> getCreateItemValidators()
	{
		return createItemValidators;
	}

	@Required
	public void setCreateItemValidators(final List<CreateItemValidator> createItemValidators)
	{
		this.createItemValidators = createItemValidators;
	}

	/**
	 * @param integrationKeyValueGenerator implementation to use.
	 * @deprecated use {@link #setKeyValueGenerator(IntegrationKeyValueGenerator)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	public void setIntegrationKeyValueGenerator(
			final IntegrationKeyValueGenerator<EdmEntitySet, ODataEntry> integrationKeyValueGenerator)
	{
		this.integrationKeyValueGenerator = integrationKeyValueGenerator;
	}

	protected ItemModelPopulator getItemModelPopulator()
	{
		return itemModelPopulator;
	}

	@Required
	public void setItemModelPopulator(final ItemModelPopulator populator)
	{
		itemModelPopulator = populator;
	}

	protected CreateItemStrategy getCreateItemStrategy()
	{
		return createItemStrategy;
	}

	@Required
	public void setCreateItemStrategy(final CreateItemStrategy strategy)
	{
		createItemStrategy = strategy;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry> getKeyValueGenerator()
	{
		return keyValueGenerator;
	}

	@Required
	public void setKeyValueGenerator(
			final IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry> keyValueGenerator)
	{
		this.keyValueGenerator = keyValueGenerator;
	}

	protected ItemTypeDescriptorService getItemTypeDescriptorService()
	{
		return itemTypeDescriptorService;
	}

	@Required
	public void setItemTypeDescriptorService(
			final ItemTypeDescriptorService itemTypeDescriptorService)
	{
		this.itemTypeDescriptorService = itemTypeDescriptorService;
	}

	public void setPersistenceContextValidators(final List<PersistenceContextValidator> persistenceContextValidators)
	{
		this.persistenceContextValidators = persistenceContextValidators;
	}

	public void setItemPersistRequestValidators(final List<ItemPersistRequestValidator> itemPersistRequestValidators)
	{
		this.itemPersistRequestValidators = itemPersistRequestValidators;
	}

	public void setContextReferencedItemModelService(
			final ContextReferencedItemModelService contextReferencedItemModelService)
	{
		this.contextReferencedItemModelService = contextReferencedItemModelService;
	}

	public void setContextItemModelService(final ContextItemModelService contextItemModelService)
	{
		this.contextItemModelService = contextItemModelService;
	}
}