/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.promotion.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCommercePromotionGroupStrategyTest
{
	protected DefaultCommercePromotionGroupStrategy promotionGroupStrategy;

	@Mock
	protected PromotionsService promotionsService;
	@Mock
	protected PromotionGroupModel defaultPromotionGroup;
	@Mock
	protected PromotionGroupModel baseSitePromotionGroup;
	@Mock
	protected AbstractOrderModel abstractOrder;
	@Mock
	protected BaseSiteModel baseSite;


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		promotionGroupStrategy = new DefaultCommercePromotionGroupStrategy();
		promotionGroupStrategy.setPromotionsService(promotionsService);
	}

	@Test
	public void getDefaultPromotionsGroupTest()
	{
		given(promotionsService.getDefaultPromotionGroup()).willReturn(defaultPromotionGroup);

		final PromotionGroupModel result = promotionGroupStrategy.getDefaultPromotionGroup();

		Assert.assertEquals(defaultPromotionGroup, result);
	}

	@Test
	public void getDefaultPromotionsGroupForOrderTest()
	{
		given(abstractOrder.getSite()).willReturn(baseSite);
		given(baseSite.getDefaultPromotionGroup()).willReturn(baseSitePromotionGroup);

		final PromotionGroupModel result = promotionGroupStrategy.getDefaultPromotionGroup(abstractOrder);

		Assert.assertEquals(baseSitePromotionGroup, result);
	}

	@Test
	public void getDefaultPromotionsGroupWithNullParameterTest()
	{
		given(promotionsService.getDefaultPromotionGroup()).willReturn(defaultPromotionGroup);

		final PromotionGroupModel result = promotionGroupStrategy.getDefaultPromotionGroup(null);

		Assert.assertEquals(defaultPromotionGroup, result);
	}

	@Test
	public void getDefaultPromotionsGroupWithNullBaseSiteTest()
	{
		given(abstractOrder.getSite()).willReturn(null);
		given(promotionsService.getDefaultPromotionGroup()).willReturn(defaultPromotionGroup);

		final PromotionGroupModel result = promotionGroupStrategy.getDefaultPromotionGroup(abstractOrder);

		Assert.assertEquals(defaultPromotionGroup, result);
	}

	@Test
	public void getDefaultPromotionsGroupWithNullPromotionGroupForBaseSiteTest()
	{
		given(abstractOrder.getSite()).willReturn(baseSite);
		given(baseSite.getDefaultPromotionGroup()).willReturn(null);
		given(promotionsService.getDefaultPromotionGroup()).willReturn(defaultPromotionGroup);

		final PromotionGroupModel result = promotionGroupStrategy.getDefaultPromotionGroup(abstractOrder);

		Assert.assertEquals(defaultPromotionGroup, result);
	}
}
