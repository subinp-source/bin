/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relateditems.visitors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.visitors.page.SlotRelatedItemVisitor;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.core.PK;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SlotRelatedItemVisitorTest
{
	@InjectMocks
	private SlotRelatedItemVisitor slotRelatedItemVisitor;

	@Mock
	private CMSContentSlotDao cmsContentSlotDao;

	@Mock
	private ContentSlotModel contentSlotModel;

	@Mock
	private AbstractPageModel page1;

	@Mock
	private AbstractPageModel page2;

	@Test
	public void shouldReturnEmptyListIfPrimaryKeyIsNull()
	{
		// GIVEN
		when(contentSlotModel.getPk()).thenReturn(null);

		// WHEN
		final List<CMSItemModel> relatedItems = slotRelatedItemVisitor.getRelatedItems(contentSlotModel);

		// THEN
		assertThat(relatedItems, empty());
	}

	@Test
	public void shouldReturnSlotItselfAndListOfPagesIfDaoReturnsThem()
	{
		// GIVEN
		when(contentSlotModel.getPk()).thenReturn(PK.fromLong(123));
		when(cmsContentSlotDao.findPagesByContentSlot(contentSlotModel)).thenReturn(Arrays.asList(page1, page2));

		// WHEN
		final List<CMSItemModel> relatedItems = slotRelatedItemVisitor.getRelatedItems(contentSlotModel);

		// THEN
		assertThat(relatedItems, hasSize(3));
		assertThat(relatedItems, containsInAnyOrder(page1, page2, contentSlotModel));
	}
}
