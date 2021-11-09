/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.type.ComposedTypeModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowAttachmentTypeReferenceSearchFacadeTest
{
	private static final int PAGE_SIZE = 10;

	@Spy
	@InjectMocks
	private WorkflowAttachmentTypeReferenceSearchFacade facade;

	@Mock
	private WorkflowsTypeFacade workflowsTypeFacade;
	private ComposedTypeModel product;

	@Before
	public void setUp()
	{
		final ComposedTypeModel category = mock(ComposedTypeModel.class);
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchQueryText()).thenReturn("");
		doReturn(Integer.valueOf(PAGE_SIZE)).when(searchQueryData).getPageSize();

		product = mock(ComposedTypeModel.class);
		when(product.getName()).thenReturn("Product");
		when(product.getCode()).thenReturn("Product");
		when(category.getName()).thenReturn("Category");
		when(category.getCode()).thenReturn("Category");
		when(workflowsTypeFacade.getSupportedAttachmentTypes()).thenReturn(Lists.newArrayList(product, category));
	}

	@Test
	public void testSearchWithoutCriteria()
	{
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchQueryText()).thenReturn("");
		doReturn(Integer.valueOf(PAGE_SIZE)).when(searchQueryData).getPageSize();


		final Pageable<ComposedTypeModel> search = facade.search(searchQueryData);

		assertThat(search.getPageSize()).isEqualTo(PAGE_SIZE);
		assertThat(search.getTotalCount()).isEqualTo(2);
	}

	@Test
	public void testSearchWithText()
	{
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchQueryText()).thenReturn("pro");
		doReturn(Integer.valueOf(PAGE_SIZE)).when(searchQueryData).getPageSize();


		final Pageable<ComposedTypeModel> search = facade.search(searchQueryData);

		assertThat(search.getPageSize()).isEqualTo(PAGE_SIZE);
		assertThat(search.getTotalCount()).isEqualTo(1);
		assertThat(search.getCurrentPage().get(0)).isSameAs(product);
	}

}
