/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.common.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GenericTypePredicateTest
{
	public static String VALID_TYPE_CODE = "ValidTypeCode";
	public static String NON_VALID_TYPE_CODE = "NonValidTypeCode";

	@Mock
	private TypeService typeService;

	@Mock
	private ItemModel validItem;

	@Mock
	private ItemModel nonValidItem;

	@Spy
	@InjectMocks
	private GenericTypePredicate genericTypePredicate;

	@Before
	public void setUp()
	{
		when(validItem.getItemtype()).thenReturn(VALID_TYPE_CODE);
		when(nonValidItem.getItemtype()).thenReturn(NON_VALID_TYPE_CODE);
	}

	@Test
	public void givenNonValidItem_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(genericTypePredicate.getTypeCode()).thenReturn(NON_VALID_TYPE_CODE);
		when(typeService.isAssignableFrom(NON_VALID_TYPE_CODE, NON_VALID_TYPE_CODE)).thenReturn(false);

		// WHEN
		final boolean predicateValid = genericTypePredicate.test(nonValidItem);

		// THEN
		assertFalse(predicateValid);
	}

	@Test
	public void givenValidItem_WhenTestIsCalled_ThenItReturnsTrue()
	{
		// GIVEN
		when(genericTypePredicate.getTypeCode()).thenReturn(VALID_TYPE_CODE);
		when(typeService.isAssignableFrom(VALID_TYPE_CODE, VALID_TYPE_CODE)).thenReturn(true);

		// WHEN
		final boolean predicateInvalid = genericTypePredicate.test(validItem);

		// THEN
		assertTrue(predicateInvalid);
	}
}
