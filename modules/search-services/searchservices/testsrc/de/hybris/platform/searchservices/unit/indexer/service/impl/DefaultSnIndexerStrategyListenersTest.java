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
import de.hybris.platform.searchservices.indexer.data.SnIndexerOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchRunnable;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContextFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerListener;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerStrategy;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;

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
public class DefaultSnIndexerStrategyListenersTest
{
	private static final String INDEX_TYPE_ID = "type1";
	private static final String INDEX_ID = "index1";

	private static final String INDEXER_BATCH_RUNNABLE_ID = "indexerBatchRunnable";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private ApplicationContext applicationContext;

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
	private SnIndexerRequest indexerRequest;

	@Mock
	private SnIndexerContext indexerContext;

	@Mock
	private SnIndexType indexType;

	@Mock
	private SnSearchProvider searchProvider;

	@Mock
	private SnIndexerOperation indexerOperation;

	@Mock
	private SnIndexerListener listener1;

	@Mock
	private SnIndexerListener listener2;

	@Mock
	private SnIndexerBatchRunnable indexerBatchRunnable;

	@Mock
	private SnIndexerBatchResponse indexerBatchResponse;

	private DefaultSnIndexerStrategy snIndexerStrategy;

	@Before
	public void setUp() throws SnException
	{
		MockitoAnnotations.initMocks(this);

		when(indexerRequest.getIndexerOperationType()).thenReturn(SnIndexerOperationType.FULL);
		when(indexerRequest.getIndexerItemSourceOperations()).thenReturn(List.of(indexerItemSourceOperation));
		when(indexerRequest.getIndexTypeId()).thenReturn(INDEX_TYPE_ID);
		when(indexerItemSourceOperation.getDocumentOperationType()).thenReturn(SnDocumentOperationType.CREATE);
		when(indexerItemSourceOperation.getIndexerItemSource()).thenReturn(indexerItemSource);
		when(indexerItemSource.getPks(indexerContext)).thenReturn(List.of(PK.fromLong(1)));
		when(indexType.getId()).thenReturn(INDEX_TYPE_ID);

		when(indexerContext.getIndexType()).thenReturn(indexType);
		when(indexerContext.getIndexerRequest()).thenReturn(indexerRequest);
		when(indexerContext.getIndexerItemSourceOperations()).thenReturn(List.of(indexerItemSourceOperation));

		when(snIndexerContextFactory.createIndexerContext(indexerRequest)).thenReturn(indexerContext);
		when(snIndexService.getDefaultIndexId(INDEX_TYPE_ID)).thenReturn(INDEX_ID);
		when(snSearchProviderFactory.getSearchProviderForContext(indexerContext)).thenReturn(searchProvider);
		when(searchProvider.createIndexerOperation(indexerContext, SnIndexerOperationType.FULL, 1)).thenReturn(indexerOperation);

		when(applicationContext.getBean(INDEXER_BATCH_RUNNABLE_ID, SnIndexerBatchRunnable.class)).thenReturn(indexerBatchRunnable);
		when(indexerBatchRunnable.getIndexerBatchResponse()).thenReturn(indexerBatchResponse);

		snIndexerStrategy = new DefaultSnIndexerStrategy();
		snIndexerStrategy.setSnSessionService(snSessionService);
		snIndexerStrategy.setSnIndexerContextFactory(snIndexerContextFactory);
		snIndexerStrategy.setSnIndexService(snIndexService);
		snIndexerStrategy.setSnListenerFactory(snListenerFactory);
		snIndexerStrategy.setSnSearchProviderFactory(snSearchProviderFactory);
		snIndexerStrategy.setIndexerBatchRunnableId(INDEXER_BATCH_RUNNABLE_ID);
		snIndexerStrategy.setApplicationContext(applicationContext);
	}

	@Test
	public void noListener() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of();

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);

		// when
		snIndexerStrategy.execute(indexerRequest);

		// then
		verify(listener1, never()).beforeIndex(indexerContext);
		verify(listener2, never()).beforeIndex(indexerContext);
		verify(listener2, never()).beforeIndex(indexerContext);
		verify(listener1, never()).beforeIndex(indexerContext);
	}

	@Test
	public void singleListener() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);

		// when
		snIndexerStrategy.execute(indexerRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener1).afterIndex(indexerContext);

		verify(listener1, never()).afterIndexError(indexerContext);
	}

	@Test
	public void singleListenerExceptionOnBeforeIndex() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeIndex(indexerContext);

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);

		verify(listener1, never()).afterIndex(indexerContext);
	}

	@Test
	public void singleListenerExceptionOnExecute() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(indexerContext).setIndexerResponse(any());

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);

		verify(listener1, never()).afterIndex(indexerContext);
	}

	@Test
	public void singleListenerExceptionOnAfterIndex() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterIndex(indexerContext);

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener1).afterIndex(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);
	}

	@Test
	public void multipleListeners() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);

		// when
		snIndexerStrategy.execute(indexerRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener2).beforeIndex(indexerContext);
		inOrder.verify(listener2).afterIndex(indexerContext);
		inOrder.verify(listener1).afterIndex(indexerContext);

		verify(listener1, never()).afterIndexError(indexerContext);
		verify(listener2, never()).afterIndexError(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeIndex1() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeIndex(indexerContext);

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener2).afterIndexError(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);

		verify(listener1, never()).afterIndex(indexerContext);
		verify(listener2, never()).beforeIndex(indexerContext);
		verify(listener2, never()).afterIndex(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeIndex2() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).beforeIndex(indexerContext);

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener2).beforeIndex(indexerContext);
		inOrder.verify(listener2).afterIndexError(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);

		verify(listener1, never()).afterIndex(indexerContext);
		verify(listener2, never()).afterIndex(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnExecute() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(indexerContext).setIndexerResponse(any());

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener2).beforeIndex(indexerContext);
		inOrder.verify(listener2).afterIndexError(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);

		verify(listener1, never()).afterIndex(indexerContext);
		verify(listener2, never()).afterIndex(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterIndex1() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterIndex(indexerContext);

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener2).beforeIndex(indexerContext);
		inOrder.verify(listener2).afterIndex(indexerContext);
		inOrder.verify(listener1).afterIndex(indexerContext);
		inOrder.verify(listener2).afterIndexError(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterIndex2() throws SnIndexerException
	{
		// given
		final List<SnIndexerListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(indexerContext, SnIndexerListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).afterIndex(indexerContext);

		// when
		try
		{
			snIndexerStrategy.execute(indexerRequest);
		}
		catch (final SnIndexerException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeIndex(indexerContext);
		inOrder.verify(listener2).beforeIndex(indexerContext);
		inOrder.verify(listener2).afterIndex(indexerContext);
		inOrder.verify(listener2).afterIndexError(indexerContext);
		inOrder.verify(listener1).afterIndexError(indexerContext);

		verify(listener1, never()).afterIndex(indexerContext);
	}
}
