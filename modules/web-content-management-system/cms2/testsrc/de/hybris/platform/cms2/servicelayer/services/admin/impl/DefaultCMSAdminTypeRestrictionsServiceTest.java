/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSTypeRestrictionDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSAdminTypeRestrictionsServiceTest
{
	private static final String POSITION = "position";
	private static final String INVALID = "invalid";

	@InjectMocks
	private DefaultCMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService;

	@Mock
	private CMSContentSlotDao contentSlotDao;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CMSTypeRestrictionDao cmsTypeRestrictionDao;

	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private AbstractPageModel page;
	@Mock
	private ContentSlotModel contentSlot;
	@Mock
	private ContentSlotForPageModel contentSlotForPage;
	@Mock
	private PageTemplateModel pageTemplate;
	@Mock
	private ContentSlotNameModel contentSlotName;
	@Mock
	private ComponentTypeGroupModel componentTypeGroup;
	@Mock
	private CMSComponentTypeModel componentType;


	@Before
	public void setUp()
	{
		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(contentSlotDao.findContentSlotRelationsByPageAndContentSlot(page, contentSlot, catalogVersion))
				.thenReturn(Collections.singletonList(contentSlotForPage));
		when(contentSlotForPage.getPosition()).thenReturn(POSITION);
		when(page.getMasterTemplate()).thenReturn(pageTemplate);
		when(pageTemplate.getAvailableContentSlots()).thenReturn(Collections.singletonList(contentSlotName));
		when(contentSlotName.getName()).thenReturn(POSITION);
		when(contentSlotName.getCompTypeGroup()).thenReturn(componentTypeGroup);
		when(componentTypeGroup.getCmsComponentTypes()).thenReturn(Collections.singleton(componentType));
		when(contentSlot.getCatalogVersion()).thenReturn(catalogVersion);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailGetTypeRestrictions_NoContentSlotForPage() throws CMSItemNotFoundException
	{
		final List<ContentSlotForPageModel> contentSlotsForPage = Lists.newArrayList(contentSlotForPage);

		when(contentSlotDao.findContentSlotRelationsByPageAndContentSlot(page, contentSlot, catalogVersion))
				.thenReturn(contentSlotsForPage);

		when(cmsAdminTypeRestrictionsService
				.getMergedTypeRestrictionForContentSlot(page.getMasterTemplate().getAvailableContentSlots(), "0"))
						.thenThrow(new CMSItemNotFoundException("message"));

		cmsAdminTypeRestrictionsService.getTypeRestrictionsContentSlotForPage(page, contentSlot);
	}


	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailGetTypeRestrictions_MultipleContentSlotForPage() throws CMSItemNotFoundException
	{
		when(contentSlotDao.findContentSlotRelationsByPageAndContentSlot(page, contentSlot, catalogVersion))
				.thenReturn(Lists.newArrayList(contentSlotForPage, contentSlotForPage));

		when(cmsAdminTypeRestrictionsService
				.getMergedTypeRestrictionForContentSlot(page.getMasterTemplate().getAvailableContentSlots(), "0"))
						.thenThrow(new CMSItemNotFoundException("message"));

		cmsAdminTypeRestrictionsService.getTypeRestrictionsContentSlotForPage(page, contentSlot);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailGetTypeRestrictions_NoContentSlotForNameInTemplate() throws CMSItemNotFoundException
	{
		when(contentSlotName.getName()).thenReturn(INVALID);

		cmsAdminTypeRestrictionsService.getTypeRestrictionsContentSlotForPage(page, contentSlot);
	}

	@Test
	public void shouldGetNoTypeRestrictions_NoneFound() throws CMSItemNotFoundException
	{
		when(componentTypeGroup.getCmsComponentTypes()).thenReturn(Collections.emptySet());

		final Set<CMSComponentTypeModel> componentTypes = cmsAdminTypeRestrictionsService
				.getTypeRestrictionsContentSlotForPage(page, contentSlot);

		Assert.assertTrue(componentTypes.isEmpty());
	}

	@Test
	public void shouldGetTypeRestrictions_CompTypeGroupOnly() throws CMSItemNotFoundException
	{
		final Set<CMSComponentTypeModel> componentTypes = cmsAdminTypeRestrictionsService
				.getTypeRestrictionsContentSlotForPage(page, contentSlot);

		Assert.assertEquals(1, componentTypes.size());
		Assert.assertTrue(componentTypes.contains(componentType));
	}

	@Test
	public void shouldGetTypeRestrictions_ValidComponentTypesOnly() throws CMSItemNotFoundException
	{
		when(componentTypeGroup.getCmsComponentTypes()).thenReturn(null);
		when(contentSlotName.getValidComponentTypes()).thenReturn(Collections.singleton(componentType));

		final Set<CMSComponentTypeModel> componentTypes = cmsAdminTypeRestrictionsService
				.getTypeRestrictionsContentSlotForPage(page, contentSlot);

		Assert.assertEquals(1, componentTypes.size());
		Assert.assertTrue(componentTypes.contains(componentType));
	}

	@Test
	public void shouldGetTypeRestrictions_CompTypeGroupAndValidComponentTypes() throws CMSItemNotFoundException
	{
		when(contentSlotName.getValidComponentTypes()).thenReturn(Collections.singleton(componentType));

		final Set<CMSComponentTypeModel> componentTypes = cmsAdminTypeRestrictionsService
				.getTypeRestrictionsContentSlotForPage(page, contentSlot);

		Assert.assertEquals(1, componentTypes.size());
		Assert.assertTrue(componentTypes.contains(componentType));
	}

	@Test
	public void shouldGetTypeRestrictionsForPage()
	{
		// GIVEN
		final List<CMSComponentTypeModel> expectedComponentTypes = Collections.singletonList(componentType);
		when(cmsTypeRestrictionDao.getTypeRestrictionsForPageTemplate(pageTemplate)).thenReturn(expectedComponentTypes);

		// WHEN
		final Set<CMSComponentTypeModel> result = cmsAdminTypeRestrictionsService.getTypeRestrictionsForPage(page);

		// THEN
		Assert.assertThat(result.size(), is(1));
		Assert.assertThat(result, hasItem(componentType));
	}
}
