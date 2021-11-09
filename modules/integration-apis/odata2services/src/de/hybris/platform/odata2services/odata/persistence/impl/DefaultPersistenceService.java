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

import static de.hybris.platform.odata2services.odata.persistence.ConversionOptions.conversionOptionsBuilder;

import de.hybris.platform.core.locking.ItemLockedForProcessingException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.inboundservices.persistence.ContextItemModelService;
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidator;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.search.ItemSearchResult;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.odata2services.odata.persistence.ConversionOptions;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.PersistenceException;
import de.hybris.platform.odata2services.odata.persistence.PersistenceService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException;
import de.hybris.platform.odata2services.odata.persistence.hook.PersistHookExecutor;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult;
import de.hybris.platform.odata2services.odata.processor.RetrievalErrorRuntimeException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Default implementation for {@link PersistenceService}
 */
public class DefaultPersistenceService implements PersistenceService
{
	private static final Logger LOG = Log.getLogger(DefaultPersistenceService.class);

	private ModelEntityService modelEntityService;
	private ItemSearchService itemSearchService;
	private CreateItemStrategy createItemStrategy;
	private SessionService sessionService;
	private ModelService modelService;
	private PersistHookExecutor persistHookRegistry;
	private TransactionTemplate transactionTemplate;
	private ContextItemModelService itemModelService;
	private List<ItemSearchRequestValidator> deleteItemValidators = Collections.emptyList();


	@Override
	public ODataEntry createEntityData(final StorageRequest storageRequest) throws EdmException
	{
		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				saveEntitiesInTransaction(storageRequest);
			}
		});

		return getODataEntry(storageRequest);
	}

	private ODataEntry getODataEntry(final StorageRequest storageRequest) throws EdmException
	{
		final Optional<ItemModel> contextItem = storageRequest.getContextItem();
		return contextItem.isPresent()
				? toODataEntry(storageRequest, contextItem.get())
				: storageRequest.getODataEntry();
	}

	@Override
	public ODataEntry getEntityData(final ItemLookupRequest lookupRequest, final ConversionOptions options)
	{
		final ItemModel item = lookupItem(lookupRequest);
		return toODataEntry(lookupRequest, options, item);
	}

	@Override
	public ItemLookupResult<ODataEntry> getEntities(final ItemLookupRequest lookupRequest, final ConversionOptions options)
	{
		final ItemSearchResult<ItemModel> searchResult = getItemSearchService().findItems(lookupRequest);
		final ItemLookupResult<ItemModel> result = ItemLookupResult.createFrom(searchResult.getItems(),
				searchResult.getTotalCount().orElse(-1));
		return result.map(item -> toODataEntry(lookupRequest, options, item));
	}

	@Override
	public void deleteItem(final ItemLookupRequest lookupRequest)
	{
		getDeleteItemValidators().forEach(v -> v.validate(lookupRequest));
		final ItemModel item = lookupItem(lookupRequest);
		getModelService().remove(item);
	}

	private ItemModel lookupItem(final ItemLookupRequest req)
	{
		return getItemSearchService()
				.findUniqueItem(req)
				.orElseThrow(() -> new ItemNotFoundException(req));
	}

	private ODataEntry toODataEntry(final StorageRequest request, final ItemModel model) throws EdmException
	{
		final ConversionOptions conversionOptions = conversionOptionsBuilder().withIncludeCollections(false).build();
		return toODataEntry(request.toLookupRequest(), conversionOptions, model);
	}

	private ODataEntry toODataEntry(final ItemLookupRequest lookupRequest, final ConversionOptions options, final ItemModel item)
	{
		try
		{
			final ItemConversionRequest conversionRequest = lookupRequest.toConversionRequest(item, options);
			return getModelEntityService().getODataEntry(conversionRequest);
		}
		catch (final EdmException e)
		{
			throw new RetrievalErrorRuntimeException(item.getItemtype(), e);
		}
	}

	protected void saveEntitiesInTransaction(final StorageRequest context)
	{
		getTransactionTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus)
			{
				saveEntities(context);
			}
		});
	}

	protected void saveEntities(final StorageRequest request)
	{
		try
		{
			final ItemModel item = persistRequestItem(request);

			final String integrationKey = request.getIntegrationKey();
			getPersistHookRegistry().runPrePersistHook(request.getPrePersistHook(), item, integrationKey)
			                        .ifPresent(it -> persistModel(request.getPostPersistHook(), it, integrationKey));
		}
		catch (final SystemIsSuspendedException | ItemLockedForProcessingException | SystemException e)
		{
			throw new PersistenceException(e, request);
		}
	}

	private ItemModel persistRequestItem(final StorageRequest request)
	{
		return Optional.ofNullable(getItemModelService().findOrCreateItem(request))
		               .orElseThrow(() -> new ItemNotFoundException(request));
	}

	protected void persistModel(final String postPersistHookName, final ItemModel item, final String integrationKey)
	{
		LOG.trace("Saving all created ItemModels");
		getModelService().saveAll();
		persistHookRegistry.runPostPersistHook(postPersistHookName, item, integrationKey);
	}

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected CreateItemStrategy getCreateItemStrategy()
	{
		return createItemStrategy;
	}

	public void setCreateItemStrategy(final CreateItemStrategy strategy)
	{
		createItemStrategy = strategy;
	}

	@Required
	protected ModelEntityService getModelEntityService()
	{
		return modelEntityService;
	}

	public void setModelEntityService(final ModelEntityService service)
	{
		modelEntityService = service;
	}

	private ItemSearchService getItemSearchService()
	{
		return itemSearchService;
	}

	@Required
	public void setItemSearchService(final ItemSearchService service)
	{
		itemSearchService = service;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService service)
	{
		modelService = service;
	}

	protected PersistHookExecutor getPersistHookRegistry()
	{
		return persistHookRegistry;
	}

	@Required
	public void setPersistHookRegistry(final PersistHookExecutor registry)
	{
		persistHookRegistry = registry;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected TransactionTemplate getTransactionTemplate()
	{
		return transactionTemplate;
	}

	@Required
	public void setTransactionTemplate(final TransactionTemplate transactionTemplate)
	{
		this.transactionTemplate = transactionTemplate;
	}

	protected ContextItemModelService getItemModelService()
	{
		return itemModelService;
	}

	@Required
	public void setItemModelService(final ContextItemModelService service)
	{
		itemModelService = service;
	}

	protected List<ItemSearchRequestValidator> getDeleteItemValidators()
	{
		return deleteItemValidators;
	}

	public void setDeleteItemValidators(
			final List<ItemSearchRequestValidator> deleteItemValidators)
	{
		if(deleteItemValidators != null){
			this.deleteItemValidators = deleteItemValidators;
		}
	}
}
