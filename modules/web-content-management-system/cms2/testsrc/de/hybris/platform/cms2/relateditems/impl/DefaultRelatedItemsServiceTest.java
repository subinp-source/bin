/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relateditems.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.core.model.ItemModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRelatedItemsServiceTest
{
	@InjectMocks
	private DefaultRelatedItemsService defaultRelatedItemsService;

	@Mock
	private ItemModel itemModel;

	@Mock
	private CMSItemModel visitor1CmsItemModel1;
	@Mock
	private CMSItemModel visitor1CmsItemModel2;

	@Mock
	private CMSItemModel visitor2CmsItemModel1;
	@Mock
	private CMSItemModel visitor2CmsItemModel2;

	@Mock
	private RelatedItemVisitor visitor1;

	@Mock
	private RelatedItemVisitor visitor2;

	@Mock
	private Predicate<ItemModel> predicate1;

	@Mock
	private Predicate<ItemModel> predicate2;

	private List<RelatedItemVisitor> visitors;

	@Before
	public void setup()
	{
		// visitors
		visitors = Arrays.asList(visitor1, visitor2);
		when(visitor1.getConstrainedBy()).thenReturn(predicate1);
		when(visitor2.getConstrainedBy()).thenReturn(predicate2);
		defaultRelatedItemsService.setVisitors(visitors);

		// related items
		when(visitor1.getRelatedItems(itemModel)).thenReturn(Arrays.asList(visitor1CmsItemModel1, visitor1CmsItemModel2));
		when(visitor2.getRelatedItems(itemModel)).thenReturn(Arrays.asList(visitor2CmsItemModel1, visitor2CmsItemModel2));
	}

	@Test
	public void shouldReturnRelatedItemsFromFirstVisitorAndNotFromTheSecondOne()
	{
		// GIVEN
		when(visitor1.getConstrainedBy().test(itemModel)).thenReturn(true);
		when(visitor2.getConstrainedBy().test(itemModel)).thenReturn(false);

		// WHEN
		final List<CMSItemModel> relatedItems = defaultRelatedItemsService.getRelatedItems(itemModel);

		// THEN
		assertThat(relatedItems, hasSize(2));
		assertThat(relatedItems, containsInAnyOrder(visitor1CmsItemModel1, visitor1CmsItemModel2));
	}

	@Test
	public void shouldNotReturnAnyRelatedItemsIfNoVisitorForItemModel()
	{
		// GIVEN
		when(visitor1.getConstrainedBy().test(itemModel)).thenReturn(false);
		when(visitor2.getConstrainedBy().test(itemModel)).thenReturn(false);

		// WHEN
		final List<CMSItemModel> relatedItems = defaultRelatedItemsService.getRelatedItems(itemModel);

		// THEN
		assertThat(relatedItems, empty());
	}

	@Test
	public void shouldReturnVisitorsInReversedOrderAfterPropertiesSet() throws Exception
	{
		// GIVEN
		defaultRelatedItemsService.afterPropertiesSet();

		// WHEN
		final List<RelatedItemVisitor> visitors = defaultRelatedItemsService.getVisitors();

		// THEN
		assertThat(visitors, hasSize(2));
		assertThat(visitors.get(0), is(visitor2));
		assertThat(visitors.get(1), is(visitor1));
	}
}
