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

import de.hybris.platform.converters.Populator;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.domain.AbstractCharge.ChargeType;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItemLookupStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOConsumptionSupport;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRuleEngineCalculationServiceCalculationMethodsTest
{
	@Mock
	private Converter<AbstractOrderRAO, Order> abstractOrderRaoToOrderConverter;
	@Mock
	private NumberedLineItemLookupStrategy lineItemLookupStrategy;
	@Mock
	private RAOConsumptionSupport raoConsumptionSupport;
	@Mock
	private Populator<ProductModel, OrderEntryRAO> orderEntryRaoProductPopulator;

	@InjectMocks
	private DefaultRuleEngineCalculationService service;

	private final Currency currency = new Currency("GBP", 2);
	private final AbstractOrderRAO defaultCartRao = new AbstractOrderRAO();

	/**
	 * Test that the calculate totals method uses the converter to create the order, and calls the recalculateTotals
	 * method
	 *
	 * @throws Exception
	 */
	@Test
	public void testCalculateTotals()
	{
		final Order cart = new Order(currency, null);
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);
		when(abstractOrderRaoToOrderConverter.convert(defaultCartRao)).thenReturn(cart);

		serviceSpy.calculateTotals(defaultCartRao);

		verify(abstractOrderRaoToOrderConverter).convert(defaultCartRao);
		verify(serviceSpy).recalculateTotals(defaultCartRao, cart);
	}

	@Test
	public void testRecalculateTotalsWithNoCartLineItems()
	{
		final Order cart = mock(Order.class);
		final BigDecimal total = BigDecimal.valueOf(20, 2);
		final BigDecimal subtotal = BigDecimal.valueOf(10, 2);
		final BigDecimal shippingCost = BigDecimal.valueOf(10, 2);
		final BigDecimal paymentCost = BigDecimal.valueOf(10, 2);
		stubCartValues(cart, total, subtotal, shippingCost, paymentCost);

		final DefaultRuleEngineCalculationService serviceSpy = spy(service);

		serviceSpy.recalculateTotals(defaultCartRao, cart);

		assertCartRaoBasics(defaultCartRao, total, subtotal, shippingCost, paymentCost);
		verify(lineItemLookupStrategy, never()).lookup(anyObject(), anyObject());

	}

	@Test
	public void testRecalculateTotals()
	{
		final OrderEntryRAO entryRao = createOrderEntryRAO();
		defaultCartRao.setEntries(Collections.singleton(entryRao));

		final Order cart = mock(Order.class);
		final BigDecimal total = BigDecimal.valueOf(200, 2);
		final BigDecimal subtotal = BigDecimal.valueOf(300, 2);
		final BigDecimal shippingCost = BigDecimal.valueOf(400, 2);
		final BigDecimal paymentCost = BigDecimal.valueOf(500, 2);
		stubCartValues(cart, total, subtotal, shippingCost, paymentCost);

		final BigDecimal lineCost = BigDecimal.valueOf(100, 2);

		final NumberedLineItem lineItem = mock(NumberedLineItem.class);
		final Money lineItemCost = new Money(lineCost, currency);
		when(lineItem.getTotal(cart)).thenReturn(lineItemCost);
		when(lineItemLookupStrategy.lookup(cart, entryRao)).thenReturn(lineItem);

		service.recalculateTotals(defaultCartRao, cart);

		assertCartRaoBasics(defaultCartRao, total, subtotal, shippingCost, paymentCost);
		verify(lineItemLookupStrategy, times(1)).lookup(cart, entryRao);
		assertThat(entryRao.getTotalPrice(), is(lineItemCost.getAmount()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRecalculateTotalsWithIllegalCartLineItem()
	{
		final OrderEntryRAO entryRao = createOrderEntryRAO();
		final Order cart = mock(Order.class);
		defaultCartRao.setEntries(Collections.singleton(entryRao));
		final BigDecimal total = BigDecimal.valueOf(400, 2);
		final BigDecimal subTotal = BigDecimal.valueOf(300, 2);
		final BigDecimal shippingCost = BigDecimal.valueOf(200, 2);
		final BigDecimal paymentCost = BigDecimal.valueOf(100, 2);
		stubCartValues(cart, total, subTotal, shippingCost, paymentCost);

		doThrow(IllegalArgumentException.class).when(lineItemLookupStrategy).lookup(cart, entryRao);

		service.recalculateTotals(defaultCartRao, cart);
	}

	@Test
	public void testCalculateSubTotalsWhenExcludedProductsIsEmpty()
	{
		//setup
		final CartRAO cartRao = new CartRAO();
		cartRao.setSubTotal(BigDecimal.ZERO);
		final Collection<ProductRAO> excludedProducts = Collections.EMPTY_SET;

		//execute
		final BigDecimal calculateSubTotals = service.calculateSubTotals(cartRao, excludedProducts);

		//assert
		assertThat(calculateSubTotals, is(BigDecimal.ZERO));
	}

	@Test
	public void testCalculateSubTotalsValidationWhenCartIsNull()
	{
		final CartRAO cartRao = null;
		try
		{
			service.calculateSubTotals(cartRao, null);
			fail("expected IllegalArgumentException");

		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("Cart must not be null."));
		}
	}

	@Test
	public void testCalculateSubTotalsWithNonMatchingExcludedProductsEntryProductIsNull()
	{
		//setup
		final ProductRAO product = new ProductRAO();
		product.setCode("productCode");

		final CartRAO cartRao = new CartRAO();
		final OrderEntryRAO entryRao = createOrderEntryRAO();
		cartRao.setEntries(Collections.singleton(entryRao));
		final Collection<ProductRAO> excludedProducts = Collections.singletonList(product);
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);

		final Order cart = mock(Order.class);
		final BigDecimal total = BigDecimal.valueOf(400, 2);
		final BigDecimal subTotal = BigDecimal.valueOf(300, 2);
		final BigDecimal shippingCost = BigDecimal.valueOf(200, 2);
		final BigDecimal paymentCost = BigDecimal.valueOf(100, 2);
		stubCartValues(cart, total, subTotal, shippingCost, paymentCost);
		when(abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(cart);

		//execute
		final BigDecimal calculateSubTotals = serviceSpy.calculateSubTotals(cartRao, excludedProducts);

		//assert
		assertThat(calculateSubTotals, is(subTotal));
		verify(serviceSpy).recalculateTotals(cartRao, cart);

	}

	/**
	 * Test the method DefaultRuleEngineCalculationService#calculateSubTotals.
	 *
	 * Note that this method will not test recalculate totals: it will only verify that the calculateSubTotals method
	 * does invoke recalculate totals!
	 *
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCalculateSubTotalsWithNonMatchingExcludedProducts()
	{
		//setup
		final ProductRAO product = new ProductRAO();
		product.setCode("productCode");

		final CartRAO cartRao = new CartRAO();
		final OrderEntryRAO entryRao = createOrderEntryRAO();
		entryRao.setProductCode("entryCode");
		cartRao.setEntries(Collections.singleton(entryRao));
		final Collection<ProductRAO> excludedProducts = Collections.singletonList(product);

		final Order cart = mock(Order.class);
		final BigDecimal total = BigDecimal.valueOf(400, 2);
		final BigDecimal subTotal = BigDecimal.valueOf(300, 2);
		final BigDecimal shippingCost = BigDecimal.valueOf(200, 2);
		final BigDecimal paymentCost = BigDecimal.valueOf(100, 2);

		stubCartValues(cart, total, subTotal, shippingCost, paymentCost);

		when(abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(cart);
		when(lineItemLookupStrategy.lookup(cart, entryRao)).thenThrow(IllegalArgumentException.class);

		service.calculateSubTotals(cartRao, excludedProducts);
	}

	@Test
	public void testCalculateSubTotalsWithMatchingExcludedProducts()
	{
		//setup
		final ProductRAO product = new ProductRAO();
		product.setCode("productCode");

		final CartRAO cartRao = new CartRAO();
		final OrderEntryRAO entryRao = createOrderEntryRAO();
		entryRao.setProductCode("productCode");
		cartRao.setEntries(Collections.singleton(entryRao));
		final Collection<ProductRAO> excludedProducts = Collections.singletonList(product);
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);

		final Order cart = mock(Order.class);
		final BigDecimal total = BigDecimal.valueOf(400, 2);
		final BigDecimal subTotal = BigDecimal.valueOf(300, 2);
		final BigDecimal shippingCost = BigDecimal.valueOf(200, 2);
		final BigDecimal paymentCost = BigDecimal.valueOf(100, 2);
		stubCartValues(cart, total, subTotal, shippingCost, paymentCost);
		when(abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(cart);

		//execute
		final BigDecimal calculateSubTotals = serviceSpy.calculateSubTotals(cartRao, excludedProducts);

		//assert
		assertThat(calculateSubTotals, is(subTotal));
		verify(serviceSpy).recalculateTotals(cartRao, cart);

	}

	private OrderEntryRAO createOrderEntryRAO()
	{
		final OrderEntryRAO entryRao = new OrderEntryRAO();
		entryRao.setEntryNumber(Integer.valueOf(1));
		return entryRao;
	}

	private void stubCartValues(final Order cart, final BigDecimal total, final BigDecimal subtotal, final BigDecimal shippingCost,
			final BigDecimal paymentCost)
	{
		when(cart.getTotal()).thenReturn(new Money(total, currency));
		when(cart.getSubTotal()).thenReturn(new Money(subtotal, currency));
		when(cart.getTotalChargeOfType(ChargeType.SHIPPING)).thenReturn(new Money(shippingCost, currency));
		when(cart.getTotalChargeOfType(ChargeType.PAYMENT)).thenReturn(new Money(paymentCost, currency));
		when(cart.getTotalCharge()).thenReturn(new Money(shippingCost.add(paymentCost), currency));
	}

	private void assertCartRaoBasics(final AbstractOrderRAO cartRao, final BigDecimal total, final BigDecimal subtotal,
			final BigDecimal shippingCost, final BigDecimal paymentCost)
	{
		assertThat(cartRao.getDeliveryCost(), is(shippingCost));
		assertThat(cartRao.getTotal(), is(total.subtract(shippingCost).subtract(paymentCost)));
		assertThat(cartRao.getTotalIncludingCharges(), is(total));
		assertThat(cartRao.getSubTotal(), is(subtotal));
		assertThat(cartRao.getPaymentCost(), is(paymentCost));
	}

}
