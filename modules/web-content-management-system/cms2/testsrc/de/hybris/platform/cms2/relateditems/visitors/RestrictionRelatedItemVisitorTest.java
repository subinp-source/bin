/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relateditems.visitors;


import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.relateditems.visitors.page.ComponentRelatedItemVisitor;
import de.hybris.platform.cms2.relateditems.visitors.page.RestrictionRelatedItemVisitor;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RestrictionRelatedItemVisitorTest
{
	@InjectMocks
	private RestrictionRelatedItemVisitor restrictionRelatedPageVisitor;

	@Mock
	private ComponentRelatedItemVisitor componentRelatedPageVisitor;

	@Mock
	private AbstractRestrictionModel restrictionModel;

	@Mock
	private AbstractPageModel restrictionRelatedPage1;

	@Mock
	private AbstractPageModel restrictionRelatedPage2;

	@Mock
	private AbstractPageModel componentRelatedPage1;

	@Mock
	private AbstractPageModel componentRelatedPage2;

	@Mock
	private AbstractCMSComponentModel component1;

	@Mock
	private AbstractCMSComponentModel component2;

	@Before
	public void setup()
	{
		when(restrictionModel.getPages()).thenReturn(Arrays.asList(restrictionRelatedPage1, restrictionRelatedPage2));
		when(restrictionModel.getComponents()).thenReturn(Arrays.asList(component1, component2));
		when(componentRelatedPageVisitor.getRelatedItems(component1)).thenReturn(Arrays.asList(componentRelatedPage1));
		when(componentRelatedPageVisitor.getRelatedItems(component2)).thenReturn(Arrays.asList(componentRelatedPage2));
	}

	@Test
	public void shouldReturnRestrictionItselfAndAllRelatedComponentsAndAllRelatedPages()
	{
		// WHEN
		final List<CMSItemModel> relatedItems = restrictionRelatedPageVisitor.getRelatedItems(restrictionModel);

		// THEN
		assertThat(relatedItems, hasSize(7));
		assertThat(relatedItems, containsInAnyOrder(restrictionModel, component1, component2, restrictionRelatedPage1, restrictionRelatedPage2, componentRelatedPage1, componentRelatedPage2));
	}

	@Test
	public void shouldReturnRestrictionItselfOnlyIfListOfPagesAndComponentsAreNull()
	{
		// GIVEN
		when(restrictionModel.getPages()).thenReturn(null);
		when(restrictionModel.getComponents()).thenReturn(null);

		// WHEN
		final List<CMSItemModel> relatedItems = restrictionRelatedPageVisitor.getRelatedItems(restrictionModel);

		// THEN
		assertThat(relatedItems, hasSize(1));
		assertThat(relatedItems, contains(restrictionModel));
	}
}
