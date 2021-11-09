/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relateditems.visitors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.visitors.page.PageRelatedItemVisitor;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageRelatedItemVisitorTest
{
	@InjectMocks
	private PageRelatedItemVisitor pageRelatedPageVisitor;

	@Mock
	private AbstractPageModel abstractPageModel;

	@Test
	public void shouldReturnItself()
	{
		// WHEN
		final List<CMSItemModel> relatedItems = pageRelatedPageVisitor.getRelatedItems(abstractPageModel);

		// THEN
		assertThat(relatedItems, hasSize(1));
		assertThat(relatedItems, contains(abstractPageModel));
	}
}
