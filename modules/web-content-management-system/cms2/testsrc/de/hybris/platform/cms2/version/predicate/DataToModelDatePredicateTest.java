/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.predicate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.sql.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
public class DataToModelDatePredicateTest
{
	private final DataToModelDatePredicate predicate = new DataToModelDatePredicate();

	@Test
	public void returnsTrueWhenTypeIsAssignableFromDate()
	{
		// WHEN
		final boolean test = predicate.test(Date.class.getCanonicalName());

		// THEN
		assertThat(test, equalTo(true));
	}

	@Test
	public void returnsFalseWhenTypeIsNotAssignableFromDate()
	{
		// WHEN
		final boolean test = predicate.test(String.class.getCanonicalName());

		// THEN
		assertThat(test, equalTo(false));
	}

}
