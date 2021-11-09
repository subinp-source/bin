/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/**
 * Tests for {@link DefaultCommerceCartRestorationStrategy}
 * Contains tests for use of the recalculation ignore property of the {@link CommerceCartParameter}
 */
@UnitTest
public class DefaultCommerceCartRestorationStrategyTest
{

	private static final int VALIDITY_PERIOD_IN_SECONDS = 1;
	private final DefaultCommerceCartRestorationStrategy commerceCartRestorationStrategy = new DefaultCommerceCartRestorationStrategy();
	@Mock
	private CartModel cartModel;
	@Mock
	private BaseSiteModel currentBaseSiteModel;
	@Mock
	private CurrencyModel currencyModel;
	@Mock
	private CartService cartService;
	@Mock
	private ModelService modelService;
	@Mock
	private TimeService timeService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		final Date currentTime = new Date();
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartModel.getModifiedtime()).willReturn(currentTime);
		given(timeService.getCurrentTime()).willReturn(currentTime);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCommonI18NService.getCurrentCurrency()).willReturn(currencyModel);

		commerceCartRestorationStrategy.setCartValidityPeriod(VALIDITY_PERIOD_IN_SECONDS);
		commerceCartRestorationStrategy.setTimeService(timeService);
		commerceCartRestorationStrategy.setBaseSiteService(baseSiteService);
		commerceCartRestorationStrategy.setCommerceCommonI18NService(commerceCommonI18NService);
		commerceCartRestorationStrategy.setCartService(cartService);
		commerceCartRestorationStrategy.setCommerceCartCalculationStrategy(commerceCartCalculationStrategy);
		commerceCartRestorationStrategy.setModelService(modelService);
	}

	@Test
	public void testRestoreCartWithCartRecalculation() throws CommerceCartRestorationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setCart(cartModel);

		commerceCartRestorationStrategy.restoreCart(parameter);

		verify(cartService).setSessionCart(cartModel);
		verify(commerceCartCalculationStrategy).recalculateCart(parameter);
	}

	@Test
	public void testRestoreCartWithoutCartRecalculation() throws CommerceCartRestorationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setIgnoreRecalculation(true);
		parameter.setCart(cartModel);

		commerceCartRestorationStrategy.restoreCart(parameter);

		verify(cartService).setSessionCart(cartModel);
		verify(commerceCartCalculationStrategy, never()).recalculateCart(any(CommerceCartParameter.class));
	}
}
