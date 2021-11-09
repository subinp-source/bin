/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.predicate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
public class DataToModelPKPredicateTest
{
	private final DataToModelPKPredicate predicate = new DataToModelPKPredicate();

	@Test
	public void returnsTrueWhenTypeIsPK()
	{

		// WHEN
		final boolean test = predicate.test("de.hybris.platform.core.PK");

		// THEN
		assertThat(test, equalTo(true));
	}

	@Test
	public void returnsFalseWhenTypeIsNotPK()
	{

		// WHEN
		final boolean test = predicate.test("some.other.type");

		// THEN
		assertThat(test, equalTo(false));
	}

}
