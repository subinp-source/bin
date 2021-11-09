/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.Order;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultNumberedLineItemLookupStrategyTest
{
	@InjectMocks
	private DefaultNumberedLineItemLookupStrategy strategy;

	@Test
	public void testFindLineItemValidation()
	{
		try
		{
			strategy.lookup(null, null);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("cart must not be null"));
		}
		try
		{
			strategy.lookup(mock(Order.class), null);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("entry rao must not be null"));
		}
		try
		{
			final OrderEntryRAO entryRao = new OrderEntryRAO();
			entryRao.setEntryNumber(null);
			strategy.lookup(mock(Order.class), entryRao);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("entry rao must have an entry number!"));
		}
	}

	@Test
	public void testFindLineItemVerificationNoneFound()
	{
		final Order mock = mock(Order.class);
		final OrderEntryRAO entryRao = new OrderEntryRAO();
		entryRao.setEntryNumber(Integer.valueOf(123));
		try
		{
			strategy.lookup(mock, entryRao);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("can't find corresponding LineItem for the given orderEntryRao:" + entryRao));
		}
	}

	@Test
	public void testFindLineItemVerificationNoneOfCorrectTypeFound()
	{
		final Order cart = mock(Order.class);
		final OrderEntryRAO entryRao = new OrderEntryRAO();
		entryRao.setEntryNumber(Integer.valueOf(123));
		final LineItem incorrectTypeLineItem = mock(LineItem.class);
		final List<LineItem> incorrectTypeLineItems = Collections.singletonList(incorrectTypeLineItem);
		when(cart.getLineItems()).thenReturn(incorrectTypeLineItems);

		try
		{
			strategy.lookup(cart, entryRao);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("can't find corresponding LineItem for the given orderEntryRao:" + entryRao));
		}
	}

	@Test
	public void testFindLineItemVerificationCorrectTypeNoMatchFound()
	{
		final Order cart = mock(Order.class);
		final OrderEntryRAO entryRao = new OrderEntryRAO();
		entryRao.setEntryNumber(Integer.valueOf(123));
		final NumberedLineItem incorrectTypeLineItem = mock(NumberedLineItem.class);
		//different entry number
		when(incorrectTypeLineItem.getEntryNumber()).thenReturn(Integer.valueOf(456));
		final List<LineItem> incorrectTypeLineItems = Collections.singletonList(incorrectTypeLineItem);
		when(cart.getLineItems()).thenReturn(incorrectTypeLineItems);

		try
		{
			strategy.lookup(cart, entryRao);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("can't find corresponding LineItem for the given orderEntryRao:" + entryRao));
		}
	}

	@Test
	public void testFindLineItemVerificationCorrectTypeOneMatchFound()
	{
		final Order cart = mock(Order.class);
		final OrderEntryRAO entryRao = new OrderEntryRAO();
		final Integer entryNumber = Integer.valueOf(123);
		entryRao.setEntryNumber(entryNumber);
		final NumberedLineItem numberedLineItem = mock(NumberedLineItem.class);
		//different entry number
		when(numberedLineItem.getEntryNumber()).thenReturn(entryNumber);
		final List<LineItem> incorrectTypeLineItems = Collections.singletonList(numberedLineItem);
		when(cart.getLineItems()).thenReturn(incorrectTypeLineItems);

		final NumberedLineItem findLineItem = strategy.lookup(cart, entryRao);
		assertThat(findLineItem, is(numberedLineItem));
	}
}
