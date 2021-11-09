/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcms.relateditem.visitors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.PK;
import de.hybris.platform.personalizationcms.container.dao.CxContainerDao;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;

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
public class CxComponentRelatedItemVisitorTest
{
	@InjectMocks
	private CxComponentRelatedItemVisitor cxComponentRelatedItemVisitor;

	@Mock
	private SimpleCMSComponentModel cmsComponentModel;

	@Mock
	private CxCmsComponentContainerModel cxCmsComponentContainerModel1;

	@Mock
	private CxCmsComponentContainerModel cxCmsComponentContainerModel2;

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

	@Mock
	private CxContainerDao cxContainerDao;

	@Before
	public void setUp()
	{
		when(cxContainerDao.getCxContainersByDefaultComponent(cmsComponentModel))
				.thenReturn(Arrays.asList(cxCmsComponentContainerModel1, cxCmsComponentContainerModel2));
		when(cxCmsComponentContainerModel1.getSlots()).thenReturn(Arrays.asList(contentSlotModel1, contentSlotModel2));
		when(cxCmsComponentContainerModel2.getSlots()).thenReturn(null);
		when(contentSlotModel1.getPk()).thenReturn(PK.fromLong(123l));
		when(contentSlotModel2.getPk()).thenReturn(PK.fromLong(234l));
		when(cmsPageService.getPagesForContentSlots(Arrays.asList(contentSlotModel1, contentSlotModel2)))
				.thenReturn(Arrays.asList(abstractPageModel2, abstractPageModel1));
	}

	@Test
	public void shouldReturnComponentItselfIfItIsNotRelatedToAnyCxContainer()
	{
		// GIVEN
		when(cxContainerDao.getCxContainersByDefaultComponent(cmsComponentModel)).thenReturn(Collections.emptyList());
		when(cmsPageService.getPagesForContentSlots(Collections.emptyList())).thenReturn(Collections.emptyList());

		// WHEN
		final List<CMSItemModel> relatedItems = cxComponentRelatedItemVisitor.getRelatedItems(cmsComponentModel);

		// THEN
		assertThat(relatedItems, hasSize(1));
		assertThat(relatedItems, contains(cmsComponentModel));
	}

	@Test
	public void shouldFilterOutSlotsWithoutPrimaryKey()
	{
		// GIVEN
		when(contentSlotModel2.getPk()).thenReturn(null);
		when(cmsPageService.getPagesForContentSlots(Arrays.asList(contentSlotModel1)))
				.thenReturn(Arrays.asList(abstractPageModel1));

		// WHEN
		final List<CMSItemModel> relatedItems = cxComponentRelatedItemVisitor.getRelatedItems(cmsComponentModel);

		// THEN
		assertThat(relatedItems, hasSize(5));
		assertThat(relatedItems, containsInAnyOrder(cmsComponentModel, cxCmsComponentContainerModel2, cxCmsComponentContainerModel1,
				contentSlotModel1, abstractPageModel1));
	}

	@Test
	public void shouldReturnComponentAndAllRelatedCxContainersAndAllRelatedSlotsAndAllRelatedPages()
	{
		// WHEN
		final List<CMSItemModel> relatedItems = cxComponentRelatedItemVisitor.getRelatedItems(cmsComponentModel);

		// THEN
		assertThat(relatedItems, hasSize(7));
		assertThat(relatedItems, containsInAnyOrder(cmsComponentModel, cxCmsComponentContainerModel2, cxCmsComponentContainerModel1,
				contentSlotModel1, contentSlotModel2, abstractPageModel1, abstractPageModel2));
	}
}
