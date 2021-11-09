/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.indexer.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchListener;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContextFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerListener;
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerBatchStrategy;
import de.hybris.platform.searchservices.jalo.SnIndexerOperation;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultSnIndexerBatchStrategyListenersTest
{
	private static final String INDEX_TYPE_ID = "indexType1";
	private static final String INDEX_ID = "index1";

	@Mock
	private ApplicationContext applicationContext;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private SnSessionService snSessionService;

	@Mock
	private SnIndexerContextFactory snIndexerContextFactory;

	@Mock
	private SnIndexService snIndexService;

	@Mock
	private SnListenerFactory snListenerFactory;

	@Mock
	private SnSearchProviderFactory snSearchProviderFactory;

	@Mock
	private SnIndexerItemSource indexerItemSource;

	@Mock
	private SnIndexerItemSourceOperation indexerItemSourceOperation;

	@Mock
	private SnIndexerBatchRequest indexerBatchRequest;

	@Mock
	private SnIndexerContext indexerContext;

	@Mock
	private SnIndexType indexType;

	@Mock
	private SnSearchProvider searchProvider;

	@Mock
	private SnIndexerOperation indexerOperation;

	@Mock
	private SearchResult flexibleSearchResult;

	@Mock
	private SnIndexerBatchListener listener1;

	@Mock
	private SnIndexerBatchListener listener2;

	private DefaultSnIndexerBatchStrategy snIndexerBatchStrategy;

	@Before
	public void setUp() throws SnException
	{
		MockitoAnnotations.initMocks(this);

		when(indexerBatchRequest.getIndexTypeId()).thenReturn(INDEX_TYPE_ID);
		when(indexerBatchRequest.getIndexerOperationType()).thenReturn(SnIndexerOperationType.FULL);
		when(indexerBatchRequest.getIndexerItemSourceOperations()).thenReturn(List.of(indexerItemSourceOperation));
		when(indexerItemSourceOperation.getDocumentOperationType()).thenReturn(SnDocumentOperationType.CREATE);
		when(indexerItemSourceOperation.getIndexerItemSource()).thenReturn(indexerItemSource);
		when(indexerItemSource.getPks(indexerContext)).thenReturn(List.of(PK.fromLong(1)));
		when(indexType.getId()).thenReturn(INDEX_TYPE_ID);

		when(indexerContext.getIndexType()).thenReturn(indexType);
		when(indexerContext.getIndexerRequest()).thenReturn(indexerBatchRequest);
		when(indexerContext.getIndexerItemSourceOperations()).thenReturn(List.of(indexerItemSourceOperation));

		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(flexibleSearchResult);
		when(snIndexerContextFactory.createIndexerContext(indexerBatchRequest)).thenReturn(indexerContext);
		when(snIndexService.getDefaultIndexId(INDEX_TYPE_ID)).thenReturn(INDEX_ID);
		when(snSearchProviderFactory.getSearchProviderForContext(indexerContext)).thenReturn(searchProvider);

		snIndexerBatchStrategy = new DefaultSnIndexerBatchStrategy();
		snIndexerBatchStrategy.setFlexibleSearchService(flexibleSearchService);
		snIndexerBatchStrategy.setSnSessionService(snSessionService);
		snIndexerBatchStrategy.setSnIndexerContextFactory(snIndexerContextFactory);
		snIndexerBatchStrategy.setSnIndexService(snIndexService);
		snIndexerBatchStrategy.setSnListenerFactory(snListenerFactory);
		snIndexerBatchStrategy.setSnSearchProviderFactory(snSearchProviderFactory);
		snIndexerBatchStrategy.setApplicationContext(applicationContext);
	}

	@Test
	public void noListener() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerListener> listeners = List.of();

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);

		// when
		snIndexerBatchStrategy.execute(indexerBatchRequest);

		// then
		verify(listener1, never()).beforeIndexBatch(indexerContext);
		verify(listener2, never()).beforeIndexBatch(indexerContext);
		verify(listener2, never()).beforeIndexBatch(indexerContext);
		verify(listener1, never()).beforeIndexBatch(indexerContext);
	}

	@Test
	public void singleListener() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);

		// when
		snIndexerBatchStrategy.execute(indexerBatchRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatch(indexerContext);

		verify(listener1, never()).afterIndexBatchError(indexerContext);
	}

	@Test
	public void singleListenerExceptionOnBeforeIndexBatch() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeIndexBatch(indexerContext);

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);

		verify(listener1, never()).afterIndexBatch(indexerContext);
	}

	@Test
	public void singleListenerExceptionOnExecute() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(indexerContext).setIndexerResponse(any());

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);

		verify(listener1, never()).afterIndexBatch(indexerContext);
	}

	@Test
	public void singleListenerExceptionOnAfterIndexBatch() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterIndexBatch(indexerContext);

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);
	}

	@Test
	public void multipleListeners() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);

		// when
		snIndexerBatchStrategy.execute(indexerBatchRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatch(indexerContext);

		verify(listener1, never()).afterIndexBatchError(indexerContext);
		verify(listener2, never()).afterIndexBatchError(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeIndexBatch1() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeIndexBatch(indexerContext);

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatchError(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);

		verify(listener1, never()).afterIndexBatch(indexerContext);
		verify(listener2, never()).beforeIndexBatch(indexerContext);
		verify(listener2, never()).afterIndexBatch(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeIndexBatch2() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).beforeIndexBatch(indexerContext);

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatchError(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);

		verify(listener1, never()).afterIndexBatch(indexerContext);
		verify(listener2, never()).afterIndexBatch(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnExecute() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(indexerContext).setIndexerResponse(any());

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatchError(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);

		verify(listener1, never()).afterIndexBatch(indexerContext);
		verify(listener2, never()).afterIndexBatch(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterIndexBatch1() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterIndexBatch(indexerContext);

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatch(indexerContext);
		inOrder.verify(listener1).afterIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatchError(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterIndexBatch2() throws SnIndexerException, InterruptedException
	{
		// given
		final List<SnIndexerBatchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerBatchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).afterIndexBatch(indexerContext);

		// when
		try
		{
			snIndexerBatchStrategy.execute(indexerBatchRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).beforeIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatch(indexerContext);
		inOrder.verify(listener2).afterIndexBatchError(indexerContext);
		inOrder.verify(listener1).afterIndexBatchError(indexerContext);

		verify(listener1, never()).afterIndexBatch(indexerContext);
	}
}
