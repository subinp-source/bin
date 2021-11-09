/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.enums.SnIndexerOperationStatus;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.data.SnIndexerOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchRunnable;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContextFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerListener;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategy;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;


/**
 * Default implementation for {@link SnIndexerStrategy}.
 */
public class DefaultSnIndexerStrategy implements SnIndexerStrategy, ApplicationContextAware
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSnIndexerStrategy.class);

	protected static final int DEFAULT_INDEXER_THREAD_POOL_SIZE = 5;
	protected static final int DEFAULT_MAX_INDEXER_BATCH_SIZE = 500;
	protected static final int DEFAULT_MAX_INDEXER_BATCH_RETRIES = 1;
	protected static final int DEFAULT_MAX_INDEXER_RETRIES = 3;

	private SnSessionService snSessionService;
	private SnIndexerContextFactory snIndexerContextFactory;
	private SnIndexService snIndexService;
	private SnListenerFactory snListenerFactory;
	private SnSearchProviderFactory snSearchProviderFactory;

	private String indexerBatchRunnableId;
	private ApplicationContext applicationContext;

	@Override
	public SnIndexerResponse execute(final SnIndexerRequest indexerRequest) throws SnIndexerException
	{
		LOG.debug("Indexer operation started");

		final SnIndexerContext indexerContext = snIndexerContextFactory.createIndexerContext(indexerRequest);
		final List<SnIndexerListener> listeners = snListenerFactory.getListeners(indexerContext, SnIndexerListener.class);

		try
		{
			snSessionService.initializeSessionForContext(indexerContext);

			executeBeforeIndexListeners(indexerContext, listeners);

			final List<IndexerBatchGroup> indexerBatchGroups = buildIndexerBatchGroups(indexerContext);
			final int totalItems = indexerBatchGroups.stream().mapToInt(IndexerBatchGroup::getTotalItems).sum();

			if (CollectionUtils.isEmpty(indexerBatchGroups)
					&& SnIndexerOperationType.INCREMENTAL == indexerRequest.getIndexerOperationType())
			{
				LOG.debug("Skipping indexer operation: no items found matching the criteria");
				return createIndexerResponse(indexerContext, 0, 0);
			}

			final String indexId = snIndexService.getDefaultIndexId(indexerRequest.getIndexTypeId());
			indexerContext.setIndexId(indexId);

			final SnSearchProvider searchProvider = snSearchProviderFactory.getSearchProviderForContext(indexerContext);

			if (SnIndexerOperationType.FULL == indexerRequest.getIndexerOperationType())
			{
				searchProvider.exportConfiguration(indexerContext);
			}

			final SnIndexerOperation indexerOperation = searchProvider.createIndexerOperation(indexerContext,
					indexerRequest.getIndexerOperationType(), totalItems);
			indexerContext.setIndexId(indexerOperation.getIndexId());
			indexerContext.setIndexerOperationId(indexerOperation.getId());

			final SnIndexerResponse indexerResponse;

			if (CollectionUtils.isEmpty(indexerBatchGroups))
			{
				indexerResponse = createIndexerResponse(indexerContext, 0, 0);
			}
			else
			{
				indexerResponse = doExecute(indexerContext, indexerBatchGroups);
			}

			indexerContext.setIndexerResponse(indexerResponse);

			searchProvider.commit(indexerContext, indexerContext.getIndexId());
			searchProvider.updateIndexerOperationStatus(indexerContext, indexerContext.getIndexerOperationId(),
					SnIndexerOperationStatus.COMPLETED, null);

			executeAfterIndexListeners(indexerContext, listeners);

			LOG.debug("Indexer operation finished");

			return indexerResponse;
		}
		catch (final SnIndexerException e)
		{
			indexerContext.addException(e);
			executeAfterIndexErrorListeners(indexerContext, listeners);
			updateIndexerOperationStatusQuietly(indexerContext, SnIndexerOperationStatus.FAILED, e);

			LOG.error("Indexer operation failed", e);

			throw e;
		}
		catch (final SnException | RuntimeException e)
		{
			indexerContext.addException(e);
			executeAfterIndexErrorListeners(indexerContext, listeners);
			updateIndexerOperationStatusQuietly(indexerContext, SnIndexerOperationStatus.FAILED, e);

			LOG.error("Indexer operation failed", e);

			throw new SnIndexerException(e);
		}
		finally
		{
			snSessionService.destroySession();
		}
	}

	protected void updateIndexerOperationStatusQuietly(final SnIndexerContext indexerContext,
			final SnIndexerOperationStatus status, final Exception exception)
	{
		try
		{
			final String errorMessage = extractErrorMessage(exception);
			final SnSearchProvider searchProvider = snSearchProviderFactory.getSearchProviderForContext(indexerContext);
			searchProvider.updateIndexerOperationStatus(indexerContext, indexerContext.getIndexerOperationId(), status,
					errorMessage);
		}
		catch (final Exception e)
		{
			LOG.error("Update indexer operation status failed", e);
		}
	}

	protected String extractErrorMessage(final Exception exception)
	{
		return exception == null ? null : exception.getMessage();
	}

	protected void executeBeforeIndexListeners(final SnIndexerContext indexerContext, final List<SnIndexerListener> listeners)
			throws SnException
	{
		for (final SnIndexerListener listener : listeners)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.beforeIndex ...", listener.getClass().getCanonicalName());
			}

			listener.beforeIndex(indexerContext);
		}
	}

	protected void executeAfterIndexListeners(final SnIndexerContext indexerContext, final List<SnIndexerListener> listeners)
			throws SnException
	{
		for (final SnIndexerListener listener : Lists.reverse(listeners))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.afterIndex ...", listener.getClass().getCanonicalName());
			}

			listener.afterIndex(indexerContext);
		}
	}

	protected void executeAfterIndexErrorListeners(final SnIndexerContext indexerContext, final List<SnIndexerListener> listeners)
	{
		for (final SnIndexerListener listener : Lists.reverse(listeners))
		{
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Running {}.afterIndexError ...", listener.getClass().getCanonicalName());
				}

				listener.afterIndexError(indexerContext);
			}
			catch (final SnIndexerException | RuntimeException exception)
			{
				indexerContext.addException(exception);
			}
		}
	}

	protected List<IndexerBatchGroup> buildIndexerBatchGroups(final SnIndexerContext indexerContext) throws SnIndexerException
	{
		final int maxIndexerBatchSize = DEFAULT_MAX_INDEXER_BATCH_SIZE;

		final List<SnIndexerItemSourceOperation> indexerItemSourceOperations = joinAndDedupIndexerItemSourceOperations(
				indexerContext);
		final List<IndexerBatchGroup> indexerBatchGroups = new ArrayList<>();

		final int indexerBatchIndex = 0;
		int indexerBatchSize = 0;
		List<SnIndexerItemSourceOperation> indexerBatchItemSourceOperations = null;

		for (final SnIndexerItemSourceOperation indexerItemSourceOperation : indexerItemSourceOperations)
		{
			final SnDocumentOperationType documentOperationType = indexerItemSourceOperation.getDocumentOperationType();
			final SnIndexerItemSource indexerItemSource = indexerItemSourceOperation.getIndexerItemSource();
			final List<PK> indexerItemSourcePks = indexerItemSource.getPks(indexerContext);
			final int indexerItemSourcePksSize = indexerItemSourcePks.size();

			if (indexerItemSourcePksSize + indexerBatchSize <= maxIndexerBatchSize)
			{
				if (indexerBatchItemSourceOperations == null)
				{
					indexerBatchItemSourceOperations = new ArrayList<>();
				}

				indexerBatchSize += indexerItemSourcePksSize;
				indexerBatchItemSourceOperations.add(indexerItemSourceOperation);
			}
			else if (indexerItemSourcePksSize <= maxIndexerBatchSize)
			{
				if (indexerBatchItemSourceOperations != null)
				{
					indexerBatchGroups.add(buildIndexerBatchGroup(indexerContext, indexerBatchItemSourceOperations, indexerBatchIndex,
							indexerBatchSize));
				}

				indexerBatchSize = indexerItemSourcePksSize;
				indexerBatchItemSourceOperations = new ArrayList<>();
				indexerBatchItemSourceOperations.add(indexerItemSourceOperation);
			}
			else
			{
				if (indexerBatchItemSourceOperations != null)
				{
					indexerBatchGroups.add(
							buildIndexerBatchGroup(indexerContext, indexerBatchItemSourceOperations, indexerBatchIndex, indexerBatchSize));
				}
				else
				{
					indexerBatchGroups.add(buildIndexerBatchGroup(indexerContext, documentOperationType, indexerItemSourcePks,
							indexerBatchIndex, maxIndexerBatchSize));
				}

				indexerBatchSize = 0;
				indexerBatchItemSourceOperations = null;
			}

			if (indexerBatchItemSourceOperations != null)
			{
				indexerBatchGroups.add(
						buildIndexerBatchGroup(indexerContext, indexerBatchItemSourceOperations, indexerBatchIndex, indexerBatchSize));
			}
		}

		return indexerBatchGroups;
	}

	protected List<SnIndexerItemSourceOperation> joinAndDedupIndexerItemSourceOperations(final SnIndexerContext indexerContext)
			throws SnIndexerException
	{
		final List<SnIndexerItemSourceOperation> targetItemSourceOperations = new ArrayList<>();

		SnDocumentOperationType documentOperationType = null;
		Set<PK> indexerItemSourcePks = null;

		for (final SnIndexerItemSourceOperation sourceIndexerItemSourceOperation : indexerContext.getIndexerItemSourceOperations())
		{
			final SnDocumentOperationType sourceDocumentOperationType = sourceIndexerItemSourceOperation.getDocumentOperationType();
			final SnIndexerItemSource sourceIndexerItemSource = sourceIndexerItemSourceOperation.getIndexerItemSource();
			final List<PK> sourceIndexerItemSourcePks = sourceIndexerItemSource.getPks(indexerContext);

			if (CollectionUtils.isNotEmpty(sourceIndexerItemSourcePks))
			{
				if (documentOperationType == null)
				{
					documentOperationType = sourceDocumentOperationType;
					indexerItemSourcePks = new LinkedHashSet<>(sourceIndexerItemSourcePks);
				}
				else if (Objects.equal(documentOperationType, sourceDocumentOperationType))
				{
					indexerItemSourcePks.addAll(sourceIndexerItemSourcePks);
				}
				else
				{
					final SnIndexerItemSource targetIndexerItemSource = new PksSnIndexerItemSource(List.copyOf(indexerItemSourcePks));
					final SnIndexerItemSourceOperation targetItemSourceOperation = new DefaultSnIndexerItemSourceOperation(
							documentOperationType, targetIndexerItemSource);

					targetItemSourceOperations.add(targetItemSourceOperation);

					documentOperationType = null;
					indexerItemSourcePks = null;
				}
			}
		}

		if (CollectionUtils.isNotEmpty(indexerItemSourcePks) && documentOperationType != null)
		{
			final SnIndexerItemSource targetIndexerItemSource = new PksSnIndexerItemSource(List.copyOf(indexerItemSourcePks));
			final SnIndexerItemSourceOperation targetItemSourceOperation = new DefaultSnIndexerItemSourceOperation(
					documentOperationType, targetIndexerItemSource);

			targetItemSourceOperations.add(targetItemSourceOperation);
		}

		return targetItemSourceOperations;
	}

	protected IndexerBatchGroup buildIndexerBatchGroup(final SnIndexerContext indexerContext,
			final List<SnIndexerItemSourceOperation> batchIndexerItemSourceOperations, final int batchIndex, final int batchSize)
	{
		final List<IndexerBatch> indexerBatches = List
				.of(new IndexerBatch(batchIndexerItemSourceOperations, buildIndexerBatchId(batchIndex)));
		return new IndexerBatchGroup(indexerBatches, batchSize);
	}

	protected IndexerBatchGroup buildIndexerBatchGroup(final SnIndexerContext indexerContext,
			final SnDocumentOperationType documentOperationType, final List<PK> indexerItemSourcePks, final int batchIndex,
			final int maxBatchSize)
	{
		final List<IndexerBatch> indexerBatches = new ArrayList<>();

		for (int index = 0, start = 0; start < indexerItemSourcePks.size(); index++, start += maxBatchSize)
		{
			final int end = Math.min(start + maxBatchSize, indexerItemSourcePks.size());

			final List<PK> batchIndexerItemSourcePks = indexerItemSourcePks.subList(start, end);
			final SnIndexerItemSource batchIndexerItemSource = new PksSnIndexerItemSource(batchIndexerItemSourcePks);
			final List<SnIndexerItemSourceOperation> batchIndexerItemsGroups = List
					.of(new DefaultSnIndexerItemSourceOperation(documentOperationType, batchIndexerItemSource));

			final IndexerBatch indexerBatch = new IndexerBatch(batchIndexerItemsGroups, buildIndexerBatchId(batchIndex + index));

			indexerBatches.add(indexerBatch);
		}

		return new IndexerBatchGroup(indexerBatches, indexerItemSourcePks.size());
	}

	protected String buildIndexerBatchId(final int batchIndex)
	{
		return String.valueOf(batchIndex);
	}

	protected SnIndexerResponse doExecute(final SnIndexerContext indexerContext, final List<IndexerBatchGroup> indexerBatchGroups)
			throws SnIndexerException
	{
		final int totalItems = indexerBatchGroups.stream().mapToInt(IndexerBatchGroup::getTotalItems).sum();
		final int totalBatches = indexerBatchGroups.stream().map(IndexerBatchGroup::getIndexerBatches).mapToInt(List::size).sum();

		final int threadPoolSize = DEFAULT_INDEXER_THREAD_POOL_SIZE;
		final int maxRetries = Math.max(0, DEFAULT_MAX_INDEXER_RETRIES);
		final int maxBatchRetries = Math.max(0, DEFAULT_MAX_INDEXER_BATCH_RETRIES);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Number of items: {}", totalItems);
			LOG.debug("Number of indexer batches: {}", totalBatches);
			LOG.debug("Thread pool size: {}", threadPoolSize);
		}

		final ExecutorService executorService = createIndexerBatchExecutor(threadPoolSize);
		final ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
		final List<IndexerBatchWrapper> completedBatches = new ArrayList<>();

		try
		{
			for (final IndexerBatchGroup indexerBatchGroup : indexerBatchGroups)
			{
				final List<IndexerBatchWrapper> batches = new ArrayList<>();

				for (final IndexerBatch indexerBatch : indexerBatchGroup.getIndexerBatches())
				{
					final SnIndexerBatchRunnable indexerBatchRunnable = createIndexerBatchRunnable(indexerContext,
							indexerBatch.getIndexerItemSourceOperations(), indexerBatch.getIndexerBatchId());
					final IndexerBatchWrapper batch = new IndexerBatchWrapper(indexerBatch, indexerBatchRunnable, maxBatchRetries);

					batches.add(batch);
				}

				runIndexerBatches(indexerContext, completionService, maxRetries, batches, completedBatches);
			}
		}
		finally
		{
			executorService.shutdownNow();
		}

		final int processedItems = completedBatches.stream()
				.mapToInt(batch -> batch.getIndexerBatchRunnable().getIndexerBatchResponse().getProcessedItems()).sum();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Number of processed items: {}", processedItems);
		}

		return createIndexerResponse(indexerContext, totalItems, processedItems);
	}

	protected ExecutorService createIndexerBatchExecutor(final int threadPoolSize)
	{
		final ThreadFactory threadFactory = new ThreadFactory()
		{
			@Override
			public Thread newThread(final Runnable runnable)
			{
				return new RegistrableThread(runnable, "indexer-thread");
			}
		};

		return Executors.newFixedThreadPool(threadPoolSize, threadFactory);
	}

	protected SnIndexerBatchRunnable createIndexerBatchRunnable(final SnIndexerContext indexerContext,
			final List<SnIndexerItemSourceOperation> indexerItemSourceOperations, final String indexerBatchId)
			throws SnIndexerException
	{
		try
		{
			final SnIndexerBatchRunnable indexerBatchRunnable = applicationContext.getBean(indexerBatchRunnableId,
					SnIndexerBatchRunnable.class);
			indexerBatchRunnable.initialize(indexerContext, indexerItemSourceOperations, indexerBatchId);

			return indexerBatchRunnable;
		}
		catch (final BeansException e)
		{
			throw new SnIndexerException("Cannot create indexer batch runnable [" + indexerBatchRunnableId + "]", e);
		}
	}

	protected void runIndexerBatches(final SnIndexerContext indexerContext,
			final ExecutorCompletionService<String> completionService, final int retriesLeft,
			final List<IndexerBatchWrapper> batches, final List<IndexerBatchWrapper> completedBatches) throws SnIndexerException
	{
		int currentRetriesLeft = retriesLeft;
		final Map<String, IndexerBatchWrapper> failedBatches = new HashMap<>();

		LOG.debug("Submitting indexer indexerBatches (total retries left: {})", Integer.valueOf(retriesLeft));

		for (final IndexerBatchWrapper batch : batches)
		{
			final String batchId = batch.getIndexerBatch().getIndexerBatchId();
			completionService.submit(batch.getIndexerBatchRunnable(), batchId);
			failedBatches.put(batchId, batch);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Indexer batch {} has been submitted (batch retries left: {})", batchId, batch.getRetriesLeft());
			}
		}

		for (int index = 0; index < batches.size(); index++)
		{
			try
			{
				final String indexerBatchId = completionService.take().get();

				final IndexerBatchWrapper completedBatch = failedBatches.remove(indexerBatchId);
				completedBatches.add(completedBatch);
			}
			catch (final ExecutionException e)
			{
				if (currentRetriesLeft <= 0)
				{
					throw new SnIndexerException("Indexer failed, max total retries has been reached", e);
				}

				currentRetriesLeft--;
			}
			catch (final InterruptedException e)
			{
				if (currentRetriesLeft <= 0)
				{
					throw new SnIndexerException("Indexer interrupted, max total retries has been reached", e);
				}

				currentRetriesLeft--;
			}
		}

		if (!failedBatches.isEmpty())
		{
			final List<IndexerBatchWrapper> newBatches = new ArrayList<>();

			for (final IndexerBatchWrapper failedBatch : failedBatches.values())
			{
				final IndexerBatch failedIndexerBatch = failedBatch.getIndexerBatch();
				final String failedBatchId = failedIndexerBatch.getIndexerBatchId();

				if (failedBatch.getRetriesLeft() <= 0)
				{
					throw new SnIndexerException(
							MessageFormat.format("Indexer batch {0} failed, max batch retries has been reached", failedBatchId));
				}

				// recreate failed workers
				final SnIndexerBatchRunnable newIndexerBatchRunnable = createIndexerBatchRunnable(indexerContext,
						failedIndexerBatch.getIndexerItemSourceOperations(), failedBatchId);
				final IndexerBatchWrapper newBatch = new IndexerBatchWrapper(failedIndexerBatch, newIndexerBatchRunnable,
						failedBatch.getRetriesLeft() - 1);

				newBatches.add(newBatch);
			}

			runIndexerBatches(indexerContext, completionService, currentRetriesLeft, newBatches, completedBatches);
		}
	}

	protected SnIndexerResponse createIndexerResponse(final SnIndexerContext indexerContext, final Integer totalItems,
			final Integer processedItems)
	{
		final DefaultSnIndexerResponse indexerResponse = new DefaultSnIndexerResponse(indexerContext.getIndexConfiguration(),
				indexerContext.getIndexType());
		indexerResponse.setTotalItems(totalItems);
		indexerResponse.setProcessedItems(processedItems);

		return indexerResponse;
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

	public String getIndexerBatchRunnableId()
	{
		return indexerBatchRunnableId;
	}

	@Required
	public void setIndexerBatchRunnableId(final String batchRunnableBeanId)
	{
		this.indexerBatchRunnableId = batchRunnableBeanId;
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

	protected static class IndexerBatchWrapper
	{
		private final IndexerBatch indexerBatch;
		private final SnIndexerBatchRunnable indexerBatchRunnable;
		private final int retriesLeft;

		public IndexerBatchWrapper(final IndexerBatch indexerBatch, final SnIndexerBatchRunnable indexerBatchRunnable,
				final int retriesLeft)
		{
			this.indexerBatch = indexerBatch;
			this.indexerBatchRunnable = indexerBatchRunnable;
			this.retriesLeft = retriesLeft;
		}

		public IndexerBatch getIndexerBatch()
		{
			return indexerBatch;
		}

		public SnIndexerBatchRunnable getIndexerBatchRunnable()
		{
			return indexerBatchRunnable;
		}

		public int getRetriesLeft()
		{
			return retriesLeft;
		}
	}

	protected static class IndexerBatch
	{
		private final List<SnIndexerItemSourceOperation> indexerItemSourceOperations;
		private final String indexerBatchId;

		public IndexerBatch(final List<SnIndexerItemSourceOperation> indexerItemSourceOperations, final String indexerBatchId)
		{
			this.indexerItemSourceOperations = indexerItemSourceOperations;
			this.indexerBatchId = indexerBatchId;
		}

		public List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations()
		{
			return indexerItemSourceOperations;
		}

		public String getIndexerBatchId()
		{
			return indexerBatchId;
		}
	}

	protected static class IndexerBatchGroup
	{
		private final List<IndexerBatch> indexerBatches;
		private final int totalItems;

		public IndexerBatchGroup(final List<IndexerBatch> indexerBatches, final int totalItems)
		{
			this.indexerBatches = indexerBatches;
			this.totalItems = totalItems;
		}

		public List<IndexerBatch> getIndexerBatches()
		{
			return indexerBatches;
		}

		public int getTotalItems()
		{
			return totalItems;
		}
	}
}
