/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.controller.bookmark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.widgets.controller.bookmark.DefaultBookmarkHandler;


@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeBookmarkHandlerTest
{

	private static final String VALID_PK = "8796104557599";
	private static final String INVALID_PK = "this is not a PK";

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private PermissionFacade permissionFacade;

	private DefaultBackofficeBookmarkHandler handler;

	@Before
	public void setUp()
	{
		handler = spy(new DefaultBackofficeBookmarkHandler(flexibleSearchService));
		handler.setPermissionFacade(permissionFacade);
		doReturn(true).when(permissionFacade).canReadType(anyString());
	}

	@Test
	public void loadObjectShouldReturnTheOnlySearchResult() throws ObjectNotFoundException
	{
		//given
		final Object singleResult = new Object();
		doReturn(prepareSearchResult(Collections.singletonList(singleResult))).when(flexibleSearchService)
				.search(any(FlexibleSearchQuery.class));

		//when
		final Object result = handler.loadObject(VALID_PK);

		//then
		assertThat(result).isSameAs(singleResult);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void loadObjectShouldFailOnAmbiguousResult() throws ObjectNotFoundException
	{
		//given
		doReturn(prepareSearchResult(Lists.emptyList())).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

		//when
		handler.loadObject(VALID_PK);
	}

	@Test(expected = IllegalStateException.class)
	public void loadObjectShouldFailOnNoResults() throws ObjectNotFoundException
	{
		//given
		doReturn(prepareSearchResult(Arrays.asList(new Object(), new Object()))).when(flexibleSearchService)
				.search(any(FlexibleSearchQuery.class));

		//when
		handler.loadObject(VALID_PK);
	}

	@Test
	public void loadObjectShouldCallSuperMethodIfPKCantBeParsed() throws ObjectNotFoundException
	{
		//given
		doReturn(null).when((DefaultBookmarkHandler) handler).loadObject(any());

		//when
		handler.loadObject(INVALID_PK);

		//then
		verify((DefaultBookmarkHandler) handler).loadObject(INVALID_PK);
	}

	@Test
	public void loadObjectShouldReturnNullForTypesWithNoReadRights() throws ObjectNotFoundException
	{
		//given
		doReturn(false).when(permissionFacade).canReadType(anyString());

		//when
		final Object result = handler.loadObject(VALID_PK);

		//then
		assertThat(result).isNull();
	}

	private <T> SearchResult<T> prepareSearchResult(final List<T> list)
	{
		return new SearchResultImpl<>(list, list.size(), 0, 0);
	}

}
