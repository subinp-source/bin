/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.SnRuntimeException;
import de.hybris.platform.searchservices.core.service.SnIdentityProvider;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierType;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.document.data.SnDocument;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchOperationRequest;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchOperationResponse;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchRequest;
import de.hybris.platform.searchservices.document.data.SnDocumentBatchResponse;
import de.hybris.platform.searchservices.enums.SnDocumentOperationStatus;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchListener;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchStrategy;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContextFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;


/**
 * Default implementation for {@link SnIndexerBatchStrategy}.
 */
public class DefaultSnIndexerBatchStrategy implements SnIndexerBatchStrategy, ApplicationContextAware
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSnIndexerBatchStrategy.class);

	private FlexibleSearchService flexibleSearchService;
	private SnSessionService snSessionService;
	private SnIndexerContextFactory snIndexerContextFactory;
	private SnIndexService snIndexService;
	private SnListenerFactory snListenerFactory;
	private SnSearchProviderFactory snSearchProviderFactory;
	private SnQualifierTypeFactory snQualifierTypeFactory;

	private ApplicationContext applicationContext;

	@Override
	public SnIndexerBatchResponse execute(final SnIndexerBatchRequest indexerBatchRequest)
			throws SnIndexerException, InterruptedException
	{
		LOG.debug("Indexer batch operation {} started", indexerBatchRequest.getIndexerBatchId());

		final SnIndexerContext indexerContext = snIndexerContextFactory.createIndexerContext(indexerBatchRequest);
		indexerContext.setIndexId(indexerBatchRequest.getIndexId());
		indexerContext.setIndexerOperationId(indexerBatchRequest.getIndexerOperationId());

		final List<SnIndexerBatchListener> listeners = snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class);

		try
		{
			snSessionService.initializeSessionForContext(indexerContext);

			executeBeforeIndexBatchListeners(indexerContext, listeners);

			SnIndexerBatchResponse indexerBatchResponse;

			if (CollectionUtils.isEmpty(indexerContext.getIndexerItemSourceOperations()))
			{
				indexerBatchResponse = createIndexerBatchResponse(indexerContext, 0, 0);
			}
			else
			{
				indexerBatchResponse = doExecute(indexerContext, indexerBatchRequest.getIndexerBatchId());
			}

			indexerContext.setIndexerResponse(indexerBatchResponse);

			executeAfterIndexBatchListeners(indexerContext, listeners);

			LOG.debug("Indexer batch operation {} finished", indexerBatchRequest.getIndexerBatchId());

			return indexerBatchResponse;
		}
		catch (final SnIndexerException e)
		{
			indexerContext.addException(e);
			executeAfterIndexBatchErrorListeners(indexerContext, listeners);

			LOG.error(MessageFormat.format("Indexer batch operation {0} failed", indexerBatchRequest.getIndexerBatchId()), e);

			throw e;
		}
		catch (final SnException | RuntimeException e)
		{
			indexerContext.addException(e);
			executeAfterIndexBatchErrorListeners(indexerContext, listeners);

			LOG.error(MessageFormat.format("Indexer batch operation {0} failed", indexerBatchRequest.getIndexerBatchId()), e);

			throw new SnIndexerException(e);
		}
		finally
		{
			snSessionService.destroySession();
		}
	}

	protected void executeBeforeIndexBatchListeners(final SnIndexerContext indexerContext,
			final List<SnIndexerBatchListener> listeners) throws SnException
	{
		for (final SnIndexerBatchListener listener : listeners)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.beforeIndexBatch ...", listener.getClass().getCanonicalName());
			}

			listener.beforeIndexBatch(indexerContext);
		}
	}

	protected void executeAfterIndexBatchListeners(final SnIndexerContext indexerContext,
			final List<SnIndexerBatchListener> listeners) throws SnException
	{
		for (final SnIndexerBatchListener listener : Lists.reverse(listeners))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.afterIndexBatch ...", listener.getClass().getCanonicalName());
			}

			listener.afterIndexBatch(indexerContext);
		}
	}

	protected void executeAfterIndexBatchErrorListeners(final SnIndexerContext indexerContext,
			final List<SnIndexerBatchListener> listeners)
	{
		for (final SnIndexerBatchListener listener : Lists.reverse(listeners))
		{
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Running {}.afterIndexBatchError ...", listener.getClass().getCanonicalName());
				}

				listener.afterIndexBatchError(indexerContext);
			}
			catch (final SnIndexerException | RuntimeException exception)
			{
				indexerContext.addException(exception);
			}
		}
	}

	protected SnIndexerBatchResponse doExecute(final SnIndexerContext indexerContext, final String indexerBatchId)
			throws SnException, InterruptedException
	{
		int totalItems = 0;
		int processedItems = 0;

		final List<SnIndexerItemSourceOperation> indexerItemSourceOperations = indexerContext.getIndexerItemSourceOperations();
		final SnIdentityProvider identityProvider = createIdentityProvider(indexerContext);
		final List<SnDocumentBatchOperationRequest> documentBatchOperationRequests = new ArrayList<>();

		for (final SnIndexerItemSourceOperation indexerItemSourceOperation : indexerItemSourceOperations)
		{
			if (indexerItemSourceOperation.getIndexerItemSource() != null)
			{
				final List<PK> pks = indexerItemSourceOperation.getIndexerItemSource().getPks(indexerContext);
				if (CollectionUtils.isNotEmpty(pks))
				{
					totalItems += pks.size();
					addIndexDocumentBatchOperationRequests(indexerContext, identityProvider, documentBatchOperationRequests, indexerItemSourceOperation, pks);
				}
			}
		}

		if (documentBatchOperationRequests.isEmpty())
		{
			LOG.warn("Skipping indexer batch, no document operations");
		}
		else
		{
			final SnDocumentBatchRequest documentBatchRequest = new SnDocumentBatchRequest();
			documentBatchRequest.setId(indexerBatchId);
			documentBatchRequest.setRequests(documentBatchOperationRequests);

			final SnSearchProvider searchProvider = snSearchProviderFactory.getSearchProviderForContext(indexerContext);
			final SnDocumentBatchResponse documentBatchResponse = searchProvider.executeDocumentBatch(indexerContext,
					indexerContext.getIndexId(), documentBatchRequest, indexerContext.getIndexerOperationId());

			for (final SnDocumentBatchOperationResponse documentBatchOperationResponse : documentBatchResponse.getResponses())
			{
				if (!Objects.equals(documentBatchOperationResponse.getStatus(), SnDocumentOperationStatus.FAILED))
				{
					processedItems++;
				}
			}
		}

		return createIndexerBatchResponse(indexerContext, totalItems, processedItems);
	}

	private void addIndexDocumentBatchOperationRequests(final SnIndexerContext indexerContext,
			final SnIdentityProvider identityProvider, final List<SnDocumentBatchOperationRequest> documentBatchOperationRequests,
			final SnIndexerItemSourceOperation indexerItemSourceOperation, List<PK> pks)
			throws SnException, InterruptedException
	{
		if (SnDocumentOperationType.CREATE.equals(indexerItemSourceOperation.getDocumentOperationType())
				|| SnDocumentOperationType.CREATE_UPDATE.equals(indexerItemSourceOperation.getDocumentOperationType()))
		{
			addIndexDocumentBatchOperationRequests(indexerContext, documentBatchOperationRequests, pks, identityProvider,
					indexerItemSourceOperation.getDocumentOperationType());
		}
		else if (SnDocumentOperationType.DELETE.equals(indexerItemSourceOperation.getDocumentOperationType()))
		{
			addDeleteDocumentBatchOperationRequests(indexerContext, documentBatchOperationRequests, pks, identityProvider);
		}
		else
		{
			throw new SnRuntimeException(MessageFormat.format("Cannot process document operation type ''{0}''",
					indexerItemSourceOperation.getDocumentOperationType()));
		}
	}

	protected void addIndexDocumentBatchOperationRequests(final SnIndexerContext indexerContext,
			final List<SnDocumentBatchOperationRequest> documentBatchOperationRequests, final List<PK> pks,
			final SnIdentityProvider identityProvider, final SnDocumentOperationType operationType)
			throws SnException, InterruptedException
	{
		final List<ItemModel> items = collectItems(indexerContext, pks);
		final Collection<ValueProviderWrapper> valueProviderWrappers = createValueProviders(indexerContext);

		for (final ItemModel item : items)
		{
			if (Thread.interrupted())
			{
				throw new InterruptedException();
			}

			final String documentId = identityProvider.getIdentifier(indexerContext, item);

			final SnDocument document = new SnDocument();
			document.setId(documentId);

			for (final ValueProviderWrapper valueProviderWrapper : valueProviderWrappers)
			{
				final SnIndexerValueProvider valueProvider = valueProviderWrapper.getValueProvider();
				final List<DefaultSnIndexerFieldWrapper> fieldWrappers = valueProviderWrapper.getFieldWrappers();
				valueProvider.provide(indexerContext, fieldWrappers, item, document);
			}

			final SnDocumentBatchOperationRequest documentBatchOperationRequest = new SnDocumentBatchOperationRequest();
			documentBatchOperationRequest.setId(documentId);
			documentBatchOperationRequest.setOperationType(operationType);
			documentBatchOperationRequest.setDocument(document);

			documentBatchOperationRequests.add(documentBatchOperationRequest);
		}
	}

	protected void addDeleteDocumentBatchOperationRequests(final SnIndexerContext indexerContext,
			final List<SnDocumentBatchOperationRequest> documentBatchOperationRequests, final List<PK> pks,
			final SnIdentityProvider identityProvider) throws SnException, InterruptedException
	{
		final List<ItemModel> items = collectItems(indexerContext, pks);

		for (final ItemModel item : items)
		{
			if (Thread.interrupted())
			{
				throw new InterruptedException();
			}

			final String documentId = identityProvider.getIdentifier(indexerContext, item);

			final SnDocumentBatchOperationRequest documentBatchOperationRequest = new SnDocumentBatchOperationRequest();
			documentBatchOperationRequest.setId(documentId);
			documentBatchOperationRequest.setOperationType(SnDocumentOperationType.DELETE);

			documentBatchOperationRequests.add(documentBatchOperationRequest);
		}
	}

	protected SnIdentityProvider createIdentityProvider(final SnIndexerContext indexerContext) throws SnIndexerException
	{
		final SnIndexType indexType = indexerContext.getIndexType();

		try
		{
			return applicationContext.getBean(indexType.getIdentityProvider(), SnIdentityProvider.class);
		}
		catch (final BeansException e)
		{
			throw new SnIndexerException("Cannot create identity provider [" + indexType.getIdentityProvider() + "]", e);
		}
	}

	protected List<ItemModel> collectItems(final SnIndexerContext indexerContext, final List<PK> pks)
	{
		final SnIndexType indexType = indexerContext.getIndexType();

		final String query = "SELECT {pk} FROM {" + indexType.getItemComposedType() + "} where {pk} in (?pks)";
		final Map<String, Object> queryParameters = Collections.<String, Object> singletonMap("pks", pks);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query, queryParameters);
		final SearchResult<ItemModel> flexibleSearchResult = flexibleSearchService.search(flexibleSearchQuery);

		return flexibleSearchResult.getResult();
	}

	protected Collection<ValueProviderWrapper> createValueProviders(final SnIndexerContext indexerContext)
			throws SnIndexerException
	{
		final SnIndexType indexType = indexerContext.getIndexType();

		if (MapUtils.isEmpty(indexType.getFields()))
		{
			return Collections.emptyList();
		}

		final Map<String, List<SnQualifier>> availableQualifiers = collectAvailableQualifiers(indexerContext, indexType);
		final Map<String, ValueProviderWrapper> valueProviderWrappers = new HashedMap();

		for (final SnField field : indexType.getFields().values())
		{
			String valueProviderId = null;
			Map<String, String> valueProviderParameters = null;

			if (field.getValueProvider() != null)
			{
				valueProviderId = field.getValueProvider();
				valueProviderParameters = field.getValueProviderParameters();
			}
			else if (indexType.getDefaultValueProvider() != null)
			{
				valueProviderId = indexType.getDefaultValueProvider();
				valueProviderParameters = indexType.getDefaultValueProviderParameters();
			}

			if (StringUtils.isNotBlank(valueProviderId))
			{
				ValueProviderWrapper valueProviderWrapper = valueProviderWrappers.get(valueProviderId);
				if (valueProviderWrapper == null)
				{
					valueProviderWrapper = new ValueProviderWrapper(createValueProvider(valueProviderId));
					valueProviderWrappers.put(valueProviderId, valueProviderWrapper);
				}

				final DefaultSnIndexerFieldWrapper fieldWrapper = new DefaultSnIndexerFieldWrapper();
				fieldWrapper.setField(field);
				fieldWrapper.setValueProviderId(valueProviderId);
				fieldWrapper.setValueProviderParameters(valueProviderParameters);

				final Optional<SnQualifierType> qualifierTypeOptional = snQualifierTypeFactory.getQualifierType(indexerContext,
						field);
				if (qualifierTypeOptional.isPresent())
				{
					final SnQualifierType qualifierType = qualifierTypeOptional.get();
					fieldWrapper.setQualifiers(availableQualifiers.get(qualifierType.getId()));
				}

				valueProviderWrapper.getFieldWrappers().add(fieldWrapper);
			}
		}

		return valueProviderWrappers.values();
	}

	protected Map<String, List<SnQualifier>> collectAvailableQualifiers(final SnIndexerContext indexerContext,
			final SnIndexType indexType)
	{
		if (MapUtils.isEmpty(indexType.getFields()))
		{
			return Collections.emptyMap();
		}

		final Map<String, List<SnQualifier>> availableQualifiers = new HashMap<>();

		for (final SnField field : indexType.getFields().values())
		{
			final Optional<SnQualifierType> qualifierTypeOptional = snQualifierTypeFactory.getQualifierType(indexerContext, field);
			if (qualifierTypeOptional.isPresent())
			{
				final SnQualifierType qualifierType = qualifierTypeOptional.get();
				availableQualifiers.computeIfAbsent(qualifierType.getId(),
						key -> qualifierType.getQualifierProvider().getAvailableQualifiers(indexerContext));
			}
		}

		return availableQualifiers;
	}

	protected SnIndexerValueProvider createValueProvider(final String valueProviderId) throws SnIndexerException
	{
		try
		{
			return applicationContext.getBean(valueProviderId, SnIndexerValueProvider.class);
		}
		catch (final BeansException e)
		{
			throw new SnIndexerException("Cannot create value provider [" + valueProviderId + "]", e);
		}
	}

	protected SnIndexerBatchResponse createIndexerBatchResponse(final SnIndexerContext indexerContext, final Integer totalItems,
			final Integer processedItems)
	{
		final DefaultSnIndexerBatchResponse indexerBatchResponse = new DefaultSnIndexerBatchResponse(
				indexerContext.getIndexConfiguration(), indexerContext.getIndexType());
		indexerBatchResponse.setTotalItems(totalItems);
		indexerBatchResponse.setProcessedItems(processedItems);

		return indexerBatchResponse;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public SnSessionService getSnSessionService()
	{
		return snSessionService;
	}

	@Required
	public void setSnSessionService(final SnSessionService snSessionService)
	{
		this.snSessionService = snSessionService;
	}

	public SnIndexerContextFactory getSnIndexerContextFactory()
	{
		return snIndexerContextFactory;
	}

	@Required
	public void setSnIndexerContextFactory(final SnIndexerContextFactory snIndexerContextFactory)
	{
		this.snIndexerContextFactory = snIndexerContextFactory;
	}

	public SnIndexService getSnIndexService()
	{
		return snIndexService;
	}

	@Required
	public void setSnIndexService(final SnIndexService snIndexService)
	{
		this.snIndexService = snIndexService;
	}

	public SnListenerFactory getSnListenerFactory()
	{
		return snListenerFactory;
	}

	@Required
	public void setSnListenerFactory(final SnListenerFactory snListenerFactory)
	{
		this.snListenerFactory = snListenerFactory;
	}

	public SnSearchProviderFactory getSnSearchProviderFactory()
	{
		return snSearchProviderFactory;
	}

	@Required
	public void setSnSearchProviderFactory(final SnSearchProviderFactory snSearchProviderFactory)
	{
		this.snSearchProviderFactory = snSearchProviderFactory;
	}

	public SnQualifierTypeFactory getSnQualifierTypeFactory()
	{
		return snQualifierTypeFactory;
	}

	@Required
	public void setSnQualifierTypeFactory(final SnQualifierTypeFactory snQualifierTypeFactory)
	{
		this.snQualifierTypeFactory = snQualifierTypeFactory;
	}

	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	protected static class ValueProviderWrapper
	{
		private final SnIndexerValueProvider valueProvider;
		private final List<DefaultSnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();

		public ValueProviderWrapper(final SnIndexerValueProvider valueProvider)
		{
			this.valueProvider = valueProvider;
		}

		public SnIndexerValueProvider getValueProvider()
		{
			return valueProvider;
		}

		public List<DefaultSnIndexerFieldWrapper> getFieldWrappers()
		{
			return fieldWrappers;
		}
	}
}
