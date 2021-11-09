/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OnlyHasSupportedCharactersPredicateTest
{
	private static final String PAGE_ID = "test-page-id";
	private static final String INVALID = "Invalid_Page_ID_has=equals";

	@InjectMocks
	private OnlyHasSupportedCharactersPredicate predicate;

	@Test
	public void shouldFail_InvalidId()
	{

		final boolean result = predicate.test(INVALID);
		assertFalse(result);
	}

	@Test
	public void shouldPass_ValidId()
	{

		final boolean result = predicate.test(PAGE_ID);
		assertTrue(result);
	}

}
