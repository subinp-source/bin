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
public class DataToModelEnumPredicateTest
{

	private final DataToModelEnumPredicate predicate = new DataToModelEnumPredicate();

	@Test
	public void returnsTrueWhenTypeIsAssignableFromHybrisEnumValue()
	{

		// WHEN
		final boolean test = predicate.test("de.hybris.platform.cms2.enums.CmsApprovalStatus");

		// THEN
		assertThat(test, equalTo(true));
	}

	@Test
	public void returnsFalseWhenTypeIsNotAssignableFromHybrisEnumValue()
	{

		// WHEN
		final boolean test = predicate.test("some.other.type");

		// THEN
		assertThat(test, equalTo(false));
	}

}
