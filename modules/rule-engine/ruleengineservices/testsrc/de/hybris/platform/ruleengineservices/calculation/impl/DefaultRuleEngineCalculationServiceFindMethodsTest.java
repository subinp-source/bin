/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@UnitTest
public class DefaultRuleEngineCalculationServiceFindMethodsTest
{
	@InjectMocks
	private DefaultRuleEngineCalculationService service;

	@Before
	public void setUp()
	{
		service = new DefaultRuleEngineCalculationService();
		initMocks(this);
	}

	@Test
	public void testFindOrderEntryRAOValidationOfNullOrder() throws Exception
	{
		try
		{
			service.findOrderEntryRAO(null, null);
			fail("IllegalArgumentException expected.");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("order must not be null"));
		}
	}

	@Test
	public void testFindOrderEntryRAOValidationOfNullLineItem() throws Exception
	{
		try
		{
			service.findOrderEntryRAO(mock(AbstractOrderRAO.class), null);
			fail("IllegalArgumentException expected.");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("lineItem must not be null"));
		}
	}

	@Test
	public void testFindOrderEntryRAOWithNullOrderEntries() throws Exception
	{
		final AbstractOrderRAO orderRAO = mock(AbstractOrderRAO.class);
		final NumberedLineItem numberedLineItem = mock(NumberedLineItem.class);

		//explicitly return null:
		when(orderRAO.getEntries()).thenReturn(null);

		assertThat(service.findOrderEntryRAO(orderRAO, numberedLineItem), is(nullValue()));
	}

	@Test
	public void testFindOrderEntryRAOWithOrderEntryWithoutEntryNumber() throws Exception
	{
		final AbstractOrderRAO orderRAO = mock(AbstractOrderRAO.class);
		final NumberedLineItem numberedLineItem = mock(NumberedLineItem.class);

		final OrderEntryRAO orderEntryRAO = mock(OrderEntryRAO.class);
		//explicitly return null:
		when(orderEntryRAO.getEntryNumber()).thenReturn(null);
		final Set<OrderEntryRAO> item = Collections.singleton(orderEntryRAO);
		when(orderRAO.getEntries()).thenReturn(item);

		assertThat(service.findOrderEntryRAO(orderRAO, numberedLineItem), is(nullValue()));
	}

	@Test
	public void testFindOrderEntryRAOWithOrderEntryWithoutMatchingEntryNumber() throws Exception
	{
		final Integer entryNumber1 = Integer.valueOf(234);
		final Integer entryNumber2 = Integer.valueOf(123);

		final AbstractOrderRAO orderRAO = mock(AbstractOrderRAO.class);
		final NumberedLineItem numberedLineItem = mock(NumberedLineItem.class);
		when(numberedLineItem.getEntryNumber()).thenReturn(entryNumber1);
		final OrderEntryRAO orderEntryRAO = mock(OrderEntryRAO.class);
		//explicitly return null:
		when(orderEntryRAO.getEntryNumber()).thenReturn(entryNumber2);
		final Set<OrderEntryRAO> item = Collections.singleton(orderEntryRAO);
		when(orderRAO.getEntries()).thenReturn(item);

		assertThat(service.findOrderEntryRAO(orderRAO, numberedLineItem), is(nullValue()));
	}

	@Test
	public void testFindOrderEntryRAOWithOrderEntryWithAMatchingEntryNumber() throws Exception
	{
		final Integer entryNumber1 = Integer.valueOf(234);
		final Integer entryNumber2 = entryNumber1;

		final AbstractOrderRAO orderRAO = mock(AbstractOrderRAO.class);
		final NumberedLineItem numberedLineItem = mock(NumberedLineItem.class);
		when(numberedLineItem.getEntryNumber()).thenReturn(entryNumber1);
		final OrderEntryRAO orderEntryRAO = mock(OrderEntryRAO.class);
		//explicitly return null:
		when(orderEntryRAO.getEntryNumber()).thenReturn(entryNumber2);
		final Set<OrderEntryRAO> item = Collections.singleton(orderEntryRAO);
		when(orderRAO.getEntries()).thenReturn(item);

		assertThat(service.findOrderEntryRAO(orderRAO, numberedLineItem), is(orderEntryRAO));
	}

}
