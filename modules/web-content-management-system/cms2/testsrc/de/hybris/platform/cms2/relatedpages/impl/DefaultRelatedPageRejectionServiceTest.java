/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relatedpages.impl;

import static de.hybris.platform.cms2.enums.CmsApprovalStatus.APPROVED;
import static de.hybris.platform.cms2.enums.CmsApprovalStatus.CHECK;
import static de.hybris.platform.cms2.model.pages.AbstractPageModel.APPROVALSTATUS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cms2.relatedpages.service.impl.DefaultRelatedPageRejectionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRelatedPageRejectionServiceTest
{
	public static final String PAGE_UID = "mock-page-id";
	public static final String PAGE_UID2 = "mock-page-id2";

	@InjectMocks
	private DefaultRelatedPageRejectionService relatedPageRejectionService;

	@Mock
	private ItemModelInternalContext abstractPageContext1;
	@Mock
	private ItemModelInternalContext abstractPageContext2;
	@Mock
	private ItemModel itemModel;
	@Mock
	private AbstractPageModel abstractPageModel1;
	@Mock
	private AbstractPageModel abstractPageModel2;
	@Mock
	private CMSItemModel cmsItemModel;
	@Mock
	private CatalogModel catalogModel;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private CatalogVersionModel activeCatalogVersionModel;

	@Mock
	private Predicate<ItemModel> pageTypePredicate;
	@Mock
	private Predicate<ItemModel> abstractPagePredicate;
	@Mock
	private RelatedItemsService relatedItemsService;
	@Mock
	private ModelService modelService;
	@Mock
	private CMSWorkflowService cmsWorkflowService;
	@Mock
	private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@Captor
	private ArgumentCaptor<Object> propertyCaptor;

	@Before
	public void setup()
	{
		when(abstractPagePredicate.test(any())).thenReturn(TRUE);
		when(abstractPageModel1.getPk()).thenReturn(PK.fromLong(123));
		when(abstractPageModel2.getPk()).thenReturn(PK.fromLong(234));
		when(abstractPageModel1.getItemModelContext()).thenReturn(abstractPageContext1);
		when(abstractPageModel2.getItemModelContext()).thenReturn(abstractPageContext2);
		when(abstractPageModel1.getCatalogVersion()).thenReturn(catalogVersionModel);
		when(abstractPageModel2.getCatalogVersion()).thenReturn(catalogVersionModel);
		when(abstractPageModel1.getUid()).thenReturn(PAGE_UID);
		when(abstractPageModel2.getUid()).thenReturn(PAGE_UID2);
		when(pageTypePredicate.test(abstractPageModel1)).thenReturn(TRUE);
		when(pageTypePredicate.test(abstractPageModel2)).thenReturn(TRUE);
		when(cmsVersionSessionContextProvider.getAllGeneratedItemsFromCached()).thenReturn(Collections.emptyMap());
		when(cmsWorkflowService.isAnyItemInWorkflow(any(List.class))).thenReturn(FALSE);
		when(cmsAdminSiteService.getOriginalItemContext()).thenReturn(null);

		when(catalogVersionModel.getCatalog()).thenReturn(catalogModel);
		when(activeCatalogVersionModel.getCatalog()).thenReturn(catalogModel);
		when(catalogModel.getActiveCatalogVersion()).thenReturn(activeCatalogVersionModel);

	}

	@Test
	public void testNotRejectPageIfApprovalStatusWasChangedDirectlyOnPage()
	{
		// GIVEN
		when(abstractPageModel1.getApprovalStatus()).thenReturn(CHECK);
		when(abstractPageContext1.loadOriginalValue(APPROVALSTATUS)).thenReturn(APPROVED);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(abstractPageModel1);

		// THEN
		verifyZeroInteractions(relatedItemsService, modelService);
	}

	@Test
	public void testNotRejectPageIfPageIsSameAsOriginalItem()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		page1IsSameAsOriginalItem();

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(abstractPageModel1);

		// THEN
		verifyZeroInteractions(relatedItemsService, modelService);
	}

	@Test
	public void testNotRejectPageIfPageInWorkflow()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		when(cmsWorkflowService.isAnyItemInWorkflow(Arrays.asList(abstractPageModel1))).thenReturn(true);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(abstractPageModel1);

		// THEN
		verifyZeroInteractions(relatedItemsService, modelService);
	}

	@Test
	public void testNotRejectRelatedPageIfPageInWorkflow()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		when(relatedItemsService.getRelatedItems(itemModel)).thenReturn(Arrays.asList(abstractPageModel1, abstractPageModel2));
		when(cmsWorkflowService.isAnyItemInWorkflow(Arrays.asList(abstractPageModel1))).thenReturn(TRUE);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(itemModel);

		// THEN
		verify(modelService).save(propertyCaptor.capture());
		final Object savedPage = propertyCaptor.getValue();
		assertThat(savedPage, equalTo(abstractPageModel2));
	}

	@Test
	public void testNotRejectPageIfNoPageRelatedToItemModel()
	{
		// GIVEN
		when(relatedItemsService.getRelatedItems(itemModel)).thenReturn(Collections.emptyList());

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(itemModel);

		// THEN
		verifyZeroInteractions(modelService);
	}

	@Test
	public void testRejectRelatedPageWhichIsDifferentFromInputPage()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		when(relatedItemsService.getRelatedItems(abstractPageModel1))
				.thenReturn(Arrays.asList(abstractPageModel1, abstractPageModel2));
		when(cmsWorkflowService.isAnyItemInWorkflow(anyList())).thenReturn(FALSE);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(abstractPageModel1);

		// THEN
		verify(modelService).save(propertyCaptor.capture());
		final Object savedPage = propertyCaptor.getValue();
		assertThat(savedPage, equalTo(abstractPageModel2));
	}

	@Test
	public void testRejectRelatedPageWhichIsDifferentFromOriginalItem()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		page1IsSameAsOriginalItem();
		when(relatedItemsService.getRelatedItems(itemModel)).thenReturn(Arrays.asList(abstractPageModel1, abstractPageModel2));
		when(cmsWorkflowService.isAnyItemInWorkflow(anyList())).thenReturn(FALSE);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(itemModel);

		// THEN
		verify(modelService).save(propertyCaptor.capture());
		final Object savedPage = propertyCaptor.getValue();
		assertThat(savedPage, equalTo(abstractPageModel2));
	}

	@Test
	public void testRejectRelatedItemsOfAbstractPageTypeOnly()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		when(relatedItemsService.getRelatedItems(itemModel)).thenReturn(Arrays.asList(abstractPageModel1, cmsItemModel));
		when(cmsWorkflowService.isAnyItemInWorkflow(anyList())).thenReturn(false);
		when(abstractPagePredicate.test(cmsItemModel)).thenReturn(false);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(itemModel);

		// THEN
		verify(modelService).save(propertyCaptor.capture());
		final Object savedPage = propertyCaptor.getValue();
		assertThat(savedPage, equalTo(abstractPageModel1));
	}

	@Test
	public void testNotRejectRelatedPagesGeneratedDuringVersionRollback()
	{
		// GIVEN
		approvalStatusWasNotChangedDirectly();
		when(relatedItemsService.getRelatedItems(itemModel)).thenReturn(Arrays.asList(abstractPageModel1));
		when(cmsWorkflowService.isAnyItemInWorkflow(anyList())).thenReturn(false);
		final Map<CMSVersionModel, ItemModel> generatedItemsFromCache = new HashMap<>();
		generatedItemsFromCache.put(null, abstractPageModel1);
		when(cmsVersionSessionContextProvider.getAllGeneratedItemsFromCached()).thenReturn(generatedItemsFromCache);

		// WHEN
		relatedPageRejectionService.rejectAllRelatedPages(itemModel);

		// THEN
		verifyZeroInteractions(modelService);
	}

	protected void approvalStatusWasNotChangedDirectly()
	{
		when(abstractPageModel1.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);
		when(abstractPageContext1.loadOriginalValue(AbstractPageModel.APPROVALSTATUS)).thenReturn(CmsApprovalStatus.APPROVED);
		when(abstractPageModel2.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);
		when(abstractPageContext2.loadOriginalValue(AbstractPageModel.APPROVALSTATUS)).thenReturn(CmsApprovalStatus.APPROVED);
	}

	protected void page1IsSameAsOriginalItem()
	{
		final Map<String, Object> originalItemContext = new HashMap<>();
		originalItemContext.put(CMSItemModel.UID, PAGE_UID);
		originalItemContext.put(CMSItemModel.CATALOGVERSION, catalogVersionModel);
		when(cmsAdminSiteService.getOriginalItemContext()).thenReturn(originalItemContext);
	}

}
