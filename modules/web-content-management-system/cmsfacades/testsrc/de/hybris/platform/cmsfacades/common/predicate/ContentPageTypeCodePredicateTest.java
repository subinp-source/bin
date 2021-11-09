/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentPageTypeCodePredicateTest
{
	@InjectMocks
	private ContentPageTypeCodePredicate predicate;

	@Test
	public void shouldReturnTrueIfPageIsContentPage()
	{
		// WHEN
		boolean result = predicate.test(ContentPageModel._TYPECODE);

		// THEN
		Assert.assertTrue(result);
	}

	@Test
	public void shouldReturnFalseIfPageIsNotContentPage()
	{
		// WHEN
		boolean result = predicate.test("fakePageCode");

		// THEN
		Assert.assertFalse(result);
	}
}
