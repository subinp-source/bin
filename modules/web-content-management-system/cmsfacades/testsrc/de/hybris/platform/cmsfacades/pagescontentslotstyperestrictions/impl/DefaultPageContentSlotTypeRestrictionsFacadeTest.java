/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.impl;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminTypeRestrictionsService;
import de.hybris.platform.cmsfacades.CMSPageContentSlotListData;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.ContentSlotTypeRestrictionsData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.validator.ContentSlotTypeRestrictionsGetValidator;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageContentSlotTypeRestrictionsFacadeTest
{
	private static final String VALID_PAGE_UID = "validPageUid";
	private static final String VALID_CONTENTSLOT_UID = "validContentSlotUid";

	private static final String PAGE_1_UID = "pageId1";
	private static final String SLOT_1_UID = "slotId1";
	private static final String SLOT_2_UID = "slotId2";
	private static final String TYPE_CODE_1 = "someTypeCode1";
	private static final String TYPE_CODE_2 = "someTypeCode2";

	@InjectMocks
	private DefaultPageContentSlotTypeRestrictionsFacade defaultContentSlotDetailsFacade;

	@Mock
	private CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService;
	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private CMSAdminPageService cmsAdminPageService;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private CatalogVersionModel parentCatalogVersionModel;
	@Mock
	private ContentCatalogModel catalogModel;
	@Mock
	private ContentCatalogModel parentCatalogModel;
	@Mock
	private CMSSiteModel cmsSite;
	@Mock
	private CatalogLevelService catalogLevelService;
	@Mock
	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CMSCatalogVersionService cmsCatalogVersionService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private ContentSlotTypeRestrictionsGetValidator contentSlotTypeRestrictionsGetValidator;
	@Mock
	private AbstractPageModel pageModel;
	@Mock
	private ContentSlotModel slot1;
	@Mock
	private ContentSlotModel slot2;
	@Mock
	private CMSComponentTypeModel type1;
	@Mock
	private CMSComponentTypeModel type2;
	@Mock
	private CMSPageContentSlotListData cmsPageContentSlotListData;

	private ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);

	@Before
	public void setup() throws Exception
	{
		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersionModel);
		when(catalogVersionModel.getCatalog()).thenReturn(catalogModel);
		when(catalogModel.getCmsSites()).thenReturn(Arrays.asList(cmsSite));
		when(parentCatalogModel.getActiveCatalogVersion()).thenReturn(parentCatalogVersionModel);

		doAnswer(invocation -> {
			final Object[] args = invocation.getArguments();
			final Supplier supplier = (Supplier)args[0];
			return supplier.get();
		}).when(sessionSearchRestrictionsDisabler).execute(any());

		when(cmsAdminTypeRestrictionsService.getTypeRestrictionsForContentSlot(
				cmsAdminPageService.getPageForIdFromActiveCatalogVersion(VALID_PAGE_UID),
				cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions(VALID_CONTENTSLOT_UID, Arrays.asList(catalogVersionModel, parentCatalogVersionModel))))
		.thenReturn(new HashSet<CMSComponentTypeModel>());

		when(catalogVersionService.getSessionCatalogVersions())
				.thenReturn(Arrays.asList(parentCatalogVersionModel, catalogVersionModel));

		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(PAGE_1_UID)).thenReturn(pageModel);
		when(slot1.getUid()).thenReturn(SLOT_1_UID);
		when(slot2.getUid()).thenReturn(SLOT_2_UID);

		when(type1.getCode()).thenReturn(TYPE_CODE_1);
		when(type2.getCode()).thenReturn(TYPE_CODE_2);

		when(cmsPageContentSlotListData.getPageId()).thenReturn(PAGE_1_UID);
	}

	@Test
	public void getTypeRestrictionsForPageContentSlots_forCatalogWithParents_Returns_ContentSlotTypeRestrictionsDataList() throws Exception
	{
		// GIVEN
		final List<CatalogVersionModel> catalogVersionModelList = new ArrayList<>();
		catalogVersionModelList.add(catalogVersionModel);
		catalogVersionModelList.add(parentCatalogVersionModel);
		when(cmsCatalogVersionService.getSuperCatalogsActiveCatalogVersions(catalogModel, cmsSite)).thenReturn(catalogVersionModelList);

		// WHEN
		final ContentSlotTypeRestrictionsData actualTypeRestrictions = defaultContentSlotDetailsFacade
				.getTypeRestrictionsForContentSlotUID(VALID_PAGE_UID, VALID_CONTENTSLOT_UID);

		// THEN
		assertFalse(actualTypeRestrictions.getContentSlotUid() == null);
	}

	@Test
	public void getTypeRestrictionsForPageContentSlots_forCatalogWithNoParents_Returns_ContentSlotTypeRestrictionsDataList() throws Exception
	{
		// GIVEN
		final List<CatalogVersionModel> catalogVersionModelList = new ArrayList<>();
		catalogVersionModelList.add(catalogVersionModel);
		when(cmsCatalogVersionService.getSuperCatalogsActiveCatalogVersions(catalogModel, cmsSite)).thenReturn(catalogVersionModelList);

		// WHEN
		final ContentSlotTypeRestrictionsData actualTypeRestrictions = defaultContentSlotDetailsFacade
				.getTypeRestrictionsForContentSlotUID(VALID_PAGE_UID, VALID_CONTENTSLOT_UID);

		// THEN
		assertFalse(actualTypeRestrictions.getContentSlotUid() == null);
	}

	@Test(expected = ValidationException.class)
	public void getTypeRestrictionsForContentSlots_ShouldThrowValidationException_WhenValidationFails() throws Exception
	{
		// GIVEN
		final List<String> slotIds = Arrays.asList(SLOT_1_UID, SLOT_2_UID);

		when(cmsPageContentSlotListData.getSlotIds()).thenReturn(slotIds);
		doThrow(ValidationException.class).when(facadeValidationService).validate(contentSlotTypeRestrictionsGetValidator, cmsPageContentSlotListData);

		// WHEN / THEN
		defaultContentSlotDetailsFacade.getTypeRestrictionsForContentSlots(cmsPageContentSlotListData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void getTypeRestrictionsForContentSlots_ShouldThrowCMSItemNotFoundException_WhenSlotNotFound() throws Exception
	{
		// GIVEN
		final List<String> slotIds = Arrays.asList(SLOT_1_UID, SLOT_2_UID);
		when(cmsPageContentSlotListData.getSlotIds()).thenReturn(slotIds);
		when(cmsAdminContentSlotService.getContentSlots(any())).thenThrow(UnknownIdentifierException.class);

		// WHEN / THEN
		defaultContentSlotDetailsFacade.getTypeRestrictionsForContentSlots(cmsPageContentSlotListData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void getTypeRestrictionsForContentSlots_ShouldThrowCMSItemNotFoundException_WhenDuplicateSlotsFound() throws Exception
	{
		// GIVEN
		final List<String> slotIds = Arrays.asList(SLOT_1_UID, SLOT_2_UID);
		when(cmsPageContentSlotListData.getSlotIds()).thenReturn(slotIds);
		when(cmsAdminContentSlotService.getContentSlots(any())).thenThrow(AmbiguousIdentifierException.class);

		// WHEN / THEN
		defaultContentSlotDetailsFacade.getTypeRestrictionsForContentSlots(cmsPageContentSlotListData);
	}

	@Test
	public void getTypeRestrictionsForContentSlots_ShouldRemoveDuplicateRequestedSlotIds() throws Exception
	{
		// GIVEN
		final List<String> slotIds = Arrays.asList(SLOT_1_UID, SLOT_2_UID, SLOT_1_UID);
		when(cmsPageContentSlotListData.getSlotIds()).thenReturn(slotIds);
		when(cmsAdminContentSlotService.getContentSlots(slotIds)).thenReturn(Arrays.asList(slot1, slot2));

		// WHEN
		defaultContentSlotDetailsFacade.getTypeRestrictionsForContentSlots(cmsPageContentSlotListData);

		// THEN
		verify(cmsAdminContentSlotService).getContentSlots(listCaptor.capture());
		final List<String> receivedList = listCaptor.getValue();

		assertThat(receivedList.size(), is(2));
		assertThat(receivedList, hasItems(SLOT_1_UID, SLOT_2_UID));
	}

	@Test
	public void getTypeRestrictionsForContentSlots_ShouldReturnRestrictionsForTheGivenSlots() throws Exception
	{
		// GIVEN
		final List<String> slotIds = Arrays.asList(SLOT_1_UID, SLOT_2_UID);
		when(cmsPageContentSlotListData.getSlotIds()).thenReturn(slotIds);
		when(cmsAdminContentSlotService.getContentSlots(slotIds)).thenReturn(Arrays.asList(slot1, slot2));

		final Set<CMSComponentTypeModel> slot1TypeRestrictions =  Set.of(type1);
		final Set<CMSComponentTypeModel> slot2TypeRestrictions =  Set.of(type1, type2);
		when(cmsAdminTypeRestrictionsService.getTypeRestrictionsForContentSlot(pageModel, slot1)).thenReturn(slot1TypeRestrictions);
		when(cmsAdminTypeRestrictionsService.getTypeRestrictionsForContentSlot(pageModel, slot2)).thenReturn(slot2TypeRestrictions);

		// WHEN
		final List<ContentSlotTypeRestrictionsData> contentSlots = defaultContentSlotDetailsFacade.getTypeRestrictionsForContentSlots(cmsPageContentSlotListData);

		// THEN
		assertThat(contentSlots, hasItems(
				allOf( //
						hasProperty("contentSlotUid", equalTo(SLOT_1_UID)), //
						hasProperty("validComponentTypes", hasItems(TYPE_CODE_1)) //
				), //
				allOf( //
						hasProperty("contentSlotUid", equalTo(SLOT_2_UID)), //
						hasProperty("validComponentTypes", hasItems(TYPE_CODE_1, TYPE_CODE_2)) //
				) //
		));
	}
}
