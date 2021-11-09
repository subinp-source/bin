/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.relatedpages.service.RelatedPageRejectionService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RelatedPagePrepareInterceptorTest
{

	@InjectMocks
	private RelatedPagePrepareInterceptor interceptor;
	@Mock
	private RelatedPageRejectionService relatedPageRejectionService;
	@Mock
	private Predicate<ItemModel> cmsItemTypePredicate;
	@Mock
	private Predicate<ItemModel> contentSlotForPageModelPredicate;

	@Mock
	private InterceptorContext interceptorContext;
	@Mock
	private CatalogModel catalog;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private CatalogVersionModel activeCatalogVersion;
	@Mock
	private CMSItemModel cmsItem;
	@Mock
	private ItemModel item;
	@Mock
	private ContentSlotForPageModel contentSlotForPage;

	@Before
	public void setUp()
	{
		when(catalogVersion.getCatalog()).thenReturn(catalog);
		when(activeCatalogVersion.getCatalog()).thenReturn(catalog);
		when(catalog.getActiveCatalogVersion()).thenReturn(activeCatalogVersion);
		when(contentSlotForPageModelPredicate.test(item)).thenReturn(Boolean.FALSE);
	}

	@Test
	public void testRejectRelatedPagesForItemModel() throws InterceptorException
	{
		when(cmsItemTypePredicate.test(item)).thenReturn(Boolean.FALSE);

		interceptor.onPrepare(item, interceptorContext);

		verify(relatedPageRejectionService).rejectAllRelatedPages(item);
	}

	@Test
	public void testRejectRelatedPagesForNonActiveCMSItemModel() throws InterceptorException
	{
		when(cmsItemTypePredicate.test(cmsItem)).thenReturn(Boolean.TRUE);
		when(cmsItem.getCatalogVersion()).thenReturn(catalogVersion);

		interceptor.onPrepare(cmsItem, interceptorContext);

		verify(relatedPageRejectionService).rejectAllRelatedPages(cmsItem);
	}

	@Test
	public void testSkipRejectRelatedPagesForDisabledInterceptor() throws InterceptorException
	{
		interceptor.setEnabled(Boolean.FALSE);

		interceptor.onPrepare(item, interceptorContext);

		verifyZeroInteractions(cmsItemTypePredicate, relatedPageRejectionService);
	}

	@Test
	public void testSkipRejectRelatedPagesForOnlineCMSItem() throws InterceptorException
	{
		when(cmsItemTypePredicate.test(cmsItem)).thenReturn(Boolean.TRUE);
		when(cmsItem.getCatalogVersion()).thenReturn(activeCatalogVersion);

		interceptor.onPrepare(cmsItem, interceptorContext);

		verifyZeroInteractions(relatedPageRejectionService);
	}

	@Test
	public void testSkipRejectRelatedPagesForOnlineContentSlotForPage() throws InterceptorException
	{
		// GIVEN
		when(cmsItemTypePredicate.test(contentSlotForPage)).thenReturn(Boolean.FALSE);
		when(contentSlotForPageModelPredicate.test(contentSlotForPage)).thenReturn(Boolean.TRUE);
		when(contentSlotForPage.getCatalogVersion()).thenReturn(activeCatalogVersion);

		// WHEN
		interceptor.onPrepare(contentSlotForPage, interceptorContext);

		// THEN
		verifyZeroInteractions(relatedPageRejectionService);
	}

}
