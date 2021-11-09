/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relateditems.visitors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.visitors.page.ComponentRelatedItemVisitor;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.PK;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ComponentRelatedItemVisitorTest
{
	@InjectMocks
	private ComponentRelatedItemVisitor componentRelatedItemVisitor;

	@Mock
	private AbstractCMSComponentModel cmsComponentModel;

	@Mock
	private ContentSlotModel contentSlotModel1;

	@Mock
	private ContentSlotModel contentSlotModel2;

	@Mock
	private AbstractPageModel abstractPageModel1;

	@Mock
	private AbstractPageModel abstractPageModel2;

	@Mock
	private CMSPageService cmsPageService;

	@Before
	public void setup()
	{
		when(contentSlotModel1.getPk()).thenReturn(PK.fromLong(123l));
		when(contentSlotModel2.getPk()).thenReturn(PK.fromLong(234l));
		when(cmsComponentModel.getSlots()).thenReturn(Arrays.asList(contentSlotModel1, contentSlotModel2));
		when(cmsPageService.getPagesForContentSlots(Arrays.asList(contentSlotModel1, contentSlotModel2))).thenReturn(Arrays.asList(abstractPageModel2, abstractPageModel1));
	}

	@Test
	public void shouldReturnOnlyItemItselfIfSlotsAttributeReturnsNull()
	{
		// GIVEN
		when(cmsComponentModel.getSlots()).thenReturn(null);
		when(cmsPageService.getPagesForContentSlots(Collections.emptyList())).thenReturn(Collections.emptyList());

		// WHEN
		final List<CMSItemModel> relatedItems = componentRelatedItemVisitor.getRelatedItems(cmsComponentModel);

		// THEN
		assertThat(relatedItems, hasSize(1));
		assertThat(relatedItems, contains(cmsComponentModel));
	}

	@Test
	public void shouldFilterOutSlotsWithoutPrimaryKey()
	{
		// GIVEN
		when(contentSlotModel2.getPk()).thenReturn(null);

		// WHEN
		componentRelatedItemVisitor.getRelatedItems(cmsComponentModel);

		// THEN
		verify(cmsPageService).getPagesForContentSlots(Collections.singletonList(contentSlotModel1));
	}

	@Test
	public void shouldReturnItemAndAllRelatedSlotsAndAllRelatedPages()
	{
		// WHEN
		final List<CMSItemModel> relatedItems = componentRelatedItemVisitor.getRelatedItems(cmsComponentModel);

		// THEN
		assertThat(relatedItems, hasSize(5));
		assertThat(relatedItems, containsInAnyOrder(cmsComponentModel, contentSlotModel2, contentSlotModel1, abstractPageModel1, abstractPageModel2));
	}
}
