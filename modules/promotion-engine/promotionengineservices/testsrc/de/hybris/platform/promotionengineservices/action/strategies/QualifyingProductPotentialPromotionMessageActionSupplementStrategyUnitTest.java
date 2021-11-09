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
package de.hybris.platform.promotionengineservices.action.strategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.droolsruleengineservices.compiler.impl.DefaultDroolsRuleActionContext;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOConsumptionSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class QualifyingProductPotentialPromotionMessageActionSupplementStrategyUnitTest
{
	private static final String UUID = "234234-dfged-23423sdfr23-sdfwer23-edrwe";

	private static final Integer PRODUCT_QUANTITY_PARAM = Integer.valueOf(3);

	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";

	@InjectMocks
	private QualifyingProductPotentialPromotionMessageActionSupplementStrategy strategy;

	@Mock
	private DisplayMessageRAO displayMessageRAO;

	@Mock
	private RuleActionContext context;

	@Mock
	private CartRAO cartRao;

	@Mock
	private RAOConsumptionSupport consumptionSupport;

	Map<String, Object> srcParameters = new HashMap<>();

	private OrderEntryRAO orderEntry1;
	private OrderEntryRAO orderEntry2;

	@Before
	public void setUp()
	{
		context = new DefaultDroolsRuleActionContext(new HashMap<>()
		{
			{
				put(CartRAO.class.getName(), cartRao);
			}
		}, null);
		context.setParameters(srcParameters);

		orderEntry1 = new OrderEntryRAO();
		orderEntry1.setQuantity(1);
		orderEntry1.setProductCode(PRODUCT1_CODE);
		orderEntry1.setEntryNumber(Integer.valueOf(1));
		orderEntry2 = new OrderEntryRAO();
		orderEntry2.setQuantity(1);
		orderEntry2.setProductCode(PRODUCT2_CODE);
		orderEntry2.setEntryNumber(Integer.valueOf(2));
		when(cartRao.getEntries()).thenReturn(new HashSet<>()
		{
			{
				add(orderEntry1);
				add(orderEntry2);
			}
		});

		srcParameters.put(QualifyingProductPotentialPromotionMessageActionSupplementStrategy.PRODUCTS_QUANTITY_PARAMETER,
				PRODUCT_QUANTITY_PARAM);
		srcParameters
				.put(QualifyingProductPotentialPromotionMessageActionSupplementStrategy.PRODUCTS_QUANTITY_PARAMETER_UUID, UUID);
		srcParameters.put(QualifyingProductPotentialPromotionMessageActionSupplementStrategy.PRODUCTS_PARAMETER,
				Arrays.asList(PRODUCT1_CODE));
		srcParameters.put(QualifyingProductPotentialPromotionMessageActionSupplementStrategy.PRODUCTS_PARAMETER_UUID, UUID);
	}

	@Test
	public void testShouldPerformActionForQualifiedProductQuantity()
	{
		final HashMap<String, Object> targetParams = new HashMap<>();
		when(displayMessageRAO.getParameters()).thenReturn(targetParams);
		when(consumptionSupport.getConsumedQuantityForOrderEntry(any())).thenReturn(0);
		Assert.assertTrue(strategy.shouldPerformAction(displayMessageRAO, context));
		Assert.assertEquals(PRODUCT_QUANTITY_PARAM.intValue() - orderEntry1.getQuantity(),
				((Integer) targetParams.get(UUID)).intValue());
	}
}
