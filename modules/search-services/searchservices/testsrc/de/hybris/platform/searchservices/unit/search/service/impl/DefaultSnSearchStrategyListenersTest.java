/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.search.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.jalo.SnIndex;
import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import de.hybris.platform.searchservices.search.service.SnSearchContextFactory;
import de.hybris.platform.searchservices.search.service.SnSearchListener;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.impl.DefaultSnSearchStrategy;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultSnSearchStrategyListenersTest
{
	private static final String INDEX_TYPE_ID = "indexType1";
	private static final String INDEX_ID = "index1";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private SnSessionService snSessionService;

	@Mock
	private SnSearchContextFactory snSearchContextFactory;

	@Mock
	private SnIndexService snIndexService;

	@Mock
	private SnListenerFactory snListenerFactory;

	@Mock
	private SnSearchProviderFactory snSearchProviderFactory;

	@Mock
	private SnSearchQuery searchQuery;

	@Mock
	private SnSearchRequest searchRequest;

	@Mock
	private SnSearchContext searchContext;

	@Mock
	private SnIndexType type;

	@Mock
	private SnIndex index;

	@Mock
	private SnSearchProvider searchProvider;

	@Mock
	private SnSearchListener listener1;

	@Mock
	private SnSearchListener listener2;

	private DefaultSnSearchStrategy snSearchStrategy;

	@Before
	public void setUp() throws SnException
	{
		MockitoAnnotations.initMocks(this);

		when(searchRequest.getIndexTypeId()).thenReturn(INDEX_TYPE_ID);
		when(searchRequest.getSearchQuery()).thenReturn(searchQuery);
		when(type.getId()).thenReturn(INDEX_TYPE_ID);
		when(index.getId()).thenReturn(INDEX_ID);

		when(searchContext.getSearchRequest()).thenReturn(searchRequest);

		when(snSearchContextFactory.createSearchContext(searchRequest)).thenReturn(searchContext);
		when(snIndexService.getDefaultIndexId(INDEX_TYPE_ID)).thenReturn(INDEX_ID);
		when(snSearchProviderFactory.getSearchProviderForContext(searchContext)).thenReturn(searchProvider);

		snSearchStrategy = new DefaultSnSearchStrategy();
		snSearchStrategy.setSnSessionService(snSessionService);
		snSearchStrategy.setSnSearchContextFactory(snSearchContextFactory);
		snSearchStrategy.setSnIndexService(snIndexService);
		snSearchStrategy.setSnListenerFactory(snListenerFactory);
		snSearchStrategy.setSnSearchProviderFactory(snSearchProviderFactory);
	}

	@Test
	public void noListener() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of();

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);

		// when
		snSearchStrategy.execute(searchRequest);

		// then
		verify(listener1, never()).beforeSearch(searchContext);
		verify(listener2, never()).beforeSearch(searchContext);
		verify(listener2, never()).beforeSearch(searchContext);
		verify(listener1, never()).beforeSearch(searchContext);
	}

	@Test
	public void singleListener() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);

		// when
		snSearchStrategy.execute(searchRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener1).afterSearch(searchContext);

		verify(listener1, never()).afterSearchError(searchContext);
	}

	@Test
	public void singleListenerExceptionOnBeforeSearch() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeSearch(searchContext);

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);

		verify(listener1, never()).afterSearch(searchContext);
	}

	@Test
	public void singleListenerExceptionOnExecute() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(searchContext).setSearchResponse(any());

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);

		verify(listener1, never()).afterSearch(searchContext);
	}

	@Test
	public void singleListenerExceptionOnAfterSearch() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterSearch(searchContext);

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener1).afterSearch(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);
	}

	@Test
	public void multipleListeners() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);

		// when
		snSearchStrategy.execute(searchRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener2).beforeSearch(searchContext);
		inOrder.verify(listener2).afterSearch(searchContext);
		inOrder.verify(listener1).afterSearch(searchContext);

		verify(listener1, never()).afterSearchError(searchContext);
		verify(listener2, never()).afterSearchError(searchContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeSearch1() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeSearch(searchContext);

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener2).afterSearchError(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);

		verify(listener1, never()).afterSearch(searchContext);
		verify(listener2, never()).beforeSearch(searchContext);
		verify(listener2, never()).afterSearch(searchContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeSearch2() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).beforeSearch(searchContext);

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener2).beforeSearch(searchContext);
		inOrder.verify(listener2).afterSearchError(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);

		verify(listener1, never()).afterSearch(searchContext);
		verify(listener2, never()).afterSearch(searchContext);
	}

	@Test
	public void multipleListenersExceptionOnExecute() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(searchContext).setSearchResponse(any());

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener2).beforeSearch(searchContext);
		inOrder.verify(listener2).afterSearchError(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);

		verify(listener1, never()).afterSearch(searchContext);
		verify(listener2, never()).afterSearch(searchContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterSearch1() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterSearch(searchContext);

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener2).beforeSearch(searchContext);
		inOrder.verify(listener2).afterSearch(searchContext);
		inOrder.verify(listener1).afterSearch(searchContext);
		inOrder.verify(listener2).afterSearchError(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterSearch2() throws SnSearchException
	{
		// given
		final List<SnSearchListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(searchContext, SnSearchListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).afterSearch(searchContext);

		// when
		try
		{
			snSearchStrategy.execute(searchRequest);
		}
		catch (final SnSearchException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSearch(searchContext);
		inOrder.verify(listener2).beforeSearch(searchContext);
		inOrder.verify(listener2).afterSearch(searchContext);
		inOrder.verify(listener2).afterSearchError(searchContext);
		inOrder.verify(listener1).afterSearchError(searchContext);

		verify(listener1, never()).afterSearch(searchContext);
	}
}
