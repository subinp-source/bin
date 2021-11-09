/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ValidStringLengthPredicateTest
{
	private static final int MAX_LENGTH = 5;
	private static final String VALID = "valid";
	private static final String INVALID = "invalid";

	private final ValidStringLengthPredicate predicate = new ValidStringLengthPredicate();

	@Before
	public void setUp()
	{
		predicate.setMaxLength(MAX_LENGTH);
	}

	@Test
	public void shouldPass()
	{
		Assert.assertTrue(predicate.test(VALID));
	}

	@Test
	public void shouldFail()
	{
		Assert.assertFalse(predicate.test(INVALID));
	}
}
