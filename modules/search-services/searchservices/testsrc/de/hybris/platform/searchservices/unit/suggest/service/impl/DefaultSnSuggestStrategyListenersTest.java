/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.suggest.service.impl;

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
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.searchservices.suggest.SnSuggestException;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContext;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContextFactory;
import de.hybris.platform.searchservices.suggest.service.SnSuggestListener;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.impl.DefaultSnSuggestStrategy;
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
public class DefaultSnSuggestStrategyListenersTest
{
	private static final String INDEX_TYPE_ID = "indexType1";
	private static final String INDEX_ID = "index1";
	private static final String QUERY = "cam";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private SnSessionService snSessionService;

	@Mock
	private SnSuggestContextFactory snSuggestContextFactory;

	@Mock
	private SnIndexService snIndexService;

	@Mock
	private SnListenerFactory snListenerFactory;

	@Mock
	private SnSearchProviderFactory snSearchProviderFactory;

	@Mock
	private SnSuggestQuery suggestQuery;

	@Mock
	private SnSuggestRequest suggestRequest;

	@Mock
	private SnSuggestContext suggestContext;

	@Mock
	private SnIndexType type;

	@Mock
	private SnIndex index;

	@Mock
	private SnSearchProvider searchProvider;

	@Mock
	private SnSuggestListener listener1;

	@Mock
	private SnSuggestListener listener2;

	private DefaultSnSuggestStrategy snSuggestStrategy;

	@Before
	public void setUp() throws SnException
	{
		MockitoAnnotations.initMocks(this);

		when(suggestQuery.getQuery()).thenReturn(QUERY);
		when(suggestRequest.getIndexTypeId()).thenReturn(INDEX_TYPE_ID);
		when(suggestRequest.getSuggestQuery()).thenReturn(suggestQuery);
		when(type.getId()).thenReturn(INDEX_TYPE_ID);
		when(index.getId()).thenReturn(INDEX_ID);

		when(suggestContext.getSuggestRequest()).thenReturn(suggestRequest);

		when(snSuggestContextFactory.createSuggestContext(suggestRequest)).thenReturn(suggestContext);
		when(snIndexService.getDefaultIndexId(INDEX_TYPE_ID)).thenReturn(INDEX_ID);
		when(snSearchProviderFactory.getSearchProviderForContext(suggestContext)).thenReturn(searchProvider);

		snSuggestStrategy = new DefaultSnSuggestStrategy();
		snSuggestStrategy.setSnSessionService(snSessionService);
		snSuggestStrategy.setSnSuggestContextFactory(snSuggestContextFactory);
		snSuggestStrategy.setSnIndexService(snIndexService);
		snSuggestStrategy.setSnListenerFactory(snListenerFactory);
		snSuggestStrategy.setSnSearchProviderFactory(snSearchProviderFactory);
	}

	@Test
	public void noListener() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of();

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);

		// when
		snSuggestStrategy.execute(suggestRequest);

		// then
		verify(listener1, never()).beforeSuggest(suggestContext);
		verify(listener2, never()).beforeSuggest(suggestContext);
		verify(listener2, never()).beforeSuggest(suggestContext);
		verify(listener1, never()).beforeSuggest(suggestContext);
	}

	@Test
	public void singleListener() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);

		// when
		snSuggestStrategy.execute(suggestRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggest(suggestContext);

		verify(listener1, never()).afterSuggestError(suggestContext);
	}

	@Test
	public void singleListenerExceptionOnBeforeSuggest() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeSuggest(suggestContext);

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);

		verify(listener1, never()).afterSuggest(suggestContext);
	}

	@Test
	public void singleListenerExceptionOnExecute() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(suggestContext).setSuggestResponse(any());

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);

		verify(listener1, never()).afterSuggest(suggestContext);
	}

	@Test
	public void singleListenerExceptionOnAfterSuggest() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterSuggest(suggestContext);

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);
	}

	@Test
	public void multipleListeners() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);

		// when
		snSuggestStrategy.execute(suggestRequest);

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener2).beforeSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggest(suggestContext);

		verify(listener1, never()).afterSuggestError(suggestContext);
		verify(listener2, never()).afterSuggestError(suggestContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeSuggest1() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).beforeSuggest(suggestContext);

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggestError(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);

		verify(listener1, never()).afterSuggest(suggestContext);
		verify(listener2, never()).beforeSuggest(suggestContext);
		verify(listener2, never()).afterSuggest(suggestContext);
	}

	@Test
	public void multipleListenersExceptionOnBeforeSuggest2() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).beforeSuggest(suggestContext);

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener2).beforeSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggestError(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);

		verify(listener1, never()).afterSuggest(suggestContext);
		verify(listener2, never()).afterSuggest(suggestContext);
	}

	@Test
	public void multipleListenersExceptionOnExecute() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(suggestContext).setSuggestResponse(any());

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener2).beforeSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggestError(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);

		verify(listener1, never()).afterSuggest(suggestContext);
		verify(listener2, never()).afterSuggest(suggestContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterSuggest1() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener1).afterSuggest(suggestContext);

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener2).beforeSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggest(suggestContext);
		inOrder.verify(listener1).afterSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggestError(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);
	}

	@Test
	public void multipleListenersExceptionOnAfterSuggest2() throws SnSuggestException
	{
		// given
		final List<SnSuggestListener> listeners = List.of(listener1, listener2);

		when(snListenerFactory.getListeners(suggestContext, SnSuggestListener.class)).thenReturn(listeners);
		doThrow(RuntimeException.class).when(listener2).afterSuggest(suggestContext);

		// when
		try
		{
			snSuggestStrategy.execute(suggestRequest);
		}
		catch (final SnSuggestException e)
		{
			// NOOP
		}

		// then
		final InOrder inOrder = Mockito.inOrder(listener1, listener2);
		inOrder.verify(listener1).beforeSuggest(suggestContext);
		inOrder.verify(listener2).beforeSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggest(suggestContext);
		inOrder.verify(listener2).afterSuggestError(suggestContext);
		inOrder.verify(listener1).afterSuggestError(suggestContext);

		verify(listener1, never()).afterSuggest(suggestContext);
	}
}
