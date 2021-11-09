/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import org.junit.Test;


@UnitTest
public class IsNotDeletedPagePredicateTest
{
	private final IsNotDeletedPagePredicate predicate = new IsNotDeletedPagePredicate();

	@Test
	public void whenItemModelIsNotPageTypeShouldBeTrue()
	{
		final boolean result = predicate.test(new CMSParagraphComponentModel());

		assertThat(result, is(true));
	}

	@Test
	public void whenItemModelIsPageTypeAndActiveShouldBeTrue()
	{
		final AbstractPageModel pageModel = new AbstractPageModel();
		pageModel.setPageStatus(CmsPageStatus.ACTIVE);

		final boolean result = predicate.test(pageModel);

		assertThat(result, is(true));
	}

	@Test
	public void whenItemModelIsPageTypeAndDeletedShouldBeFalse()
	{
		final AbstractPageModel pageModel = new AbstractPageModel();
		pageModel.setPageStatus(CmsPageStatus.DELETED);

		final boolean result = predicate.test(pageModel);

		assertThat(result, is(false));
	}

}
