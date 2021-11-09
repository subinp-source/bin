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
package de.hybris.platform.droolsruleengineservices.commerce;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.droolsruleengineservices.impl.AbstractRuleEngineServicesTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import org.apache.commons.collections.map.SingletonMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Tests the RAOActions.
 */
@IntegrationTest
public class DefaultRAOActionTest extends AbstractRuleEngineServicesTest
{

	private static final String USD = "USD";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/droolsruleengineservices/test/ruleenginesetup.impex", "utf-8");
	}

	@Test
	public void testAddOrderLevelDiscountAbsolute() throws IOException
	{
		final String ruleName = legacyRRDMode ? "orderlevel-discount-10USD_rrd" : "orderlevel-discount-10USD";

		final DroolsRuleModel rule1 = getRuleForFile(ruleName + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderFixedDiscountAction", "ruleOrderFixedDiscountAction"));
		initializeRuleEngine(rule1);

		// simple cart with 2 entries and delivery cost
		final CartRAO cart = createCartRAO("simple03", USD);
		cart.setEntries(set(createOrderEntryRAO(cart, "12.34", USD, 2, 0), createOrderEntryRAO(cart, "23.45", USD, 3, 1)));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		// pre-calculate totals so that rule can actually be triggered
		// note: this calculation happens as part of the DefaultCartRAOProvider
		// so here we have to do it explicitly
		getRuleEngineCalculationService().calculateTotals(cart);

		Assert.assertEquals(new BigDecimal("100.03"), cart.getTotalIncludingCharges());
		Assert.assertEquals(new BigDecimal("95.03"), cart.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), cart.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());


		// now execute the engine that should trigger the 10USD oder-level discount rule

		final RuleEvaluationContext context = prepareContext(Collections.singleton(cart));

		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		assertNotNull(resultRAO);
		assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		assertTrue("should be DiscountRAO", resultAction instanceof DiscountRAO);
		final DiscountRAO discount = (DiscountRAO) resultAction;
		assertEquals(new BigDecimal("10.00"), discount.getValue());

		// assert the meta-data has been set correctly
		assertEquals(ruleName + ".drl", discount.getFiredRuleCode());

		// assert the numbers add up
		// total is 95.03 - 10 = 85.03
		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("95.03"), cart.getSubTotal());
		assertEquals(new BigDecimal("85.03"), cart.getTotal());
		assertEquals(new BigDecimal("90.03"), cart.getTotalIncludingCharges());

	}


	@Test
	public void testChangeDeliveryModeFreeShipping() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "shipping-free_rrd" : "shipping-free";

		final DroolsRuleModel rule1 = getRuleForFile(ruleName1 + ".drl", "/droolsruleengineservices/test/rules/evaluation/",
				"de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleChangeDeliveryModeAction", "ruleChangeDeliveryModeAction"));

		final String ruleName2 = legacyRRDMode ? "shipping-overnight-free_rrd" : "shipping-overnight-free";
		final DroolsRuleModel rule2 = getRuleForFile(ruleName2 + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleChangeDeliveryModeAction", "ruleChangeDeliveryModeAction"));
		initializeRuleEngine(rule1, rule2);

		// simple cart with 2 entries and payment cost
		final CartRAO cart = createCartRAO("simple01", USD);
		cart.setEntries(set(createOrderEntryRAO(cart, "12.34", USD, 2, 0), createOrderEntryRAO(cart, "23.45", USD, 3, 1)));
		cart.setPaymentCost(new BigDecimal("5.00"));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		// pre-calculate totals so that rule can actually be triggered
		// note: this calculation happens as part of the DefaultCartRAOProvider
		// so here we have to do it explicitly
		getRuleEngineCalculationService().calculateTotals(cart);

		assertEquals(new BigDecimal("95.03"), cart.getTotal());
		assertEquals(new BigDecimal("105.03"), cart.getTotalIncludingCharges());
		assertEquals(new BigDecimal("95.03"), cart.getSubTotal());
		assertEquals(new BigDecimal("5.00"), cart.getPaymentCost());
		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());


		// now execute the engine that should trigger the free-shipping discount rule
		final Set<Object> facts = new HashSet<Object>()
		{
			{
				add(cart);
				add(createDeliveryModeRAO("free-overnight-domestic", "0.00", USD));
				add(createDeliveryModeRAO("free-domestic", "0.00", USD));
			}
		};
		final RuleEvaluationContext context = prepareContext(facts);

		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);
		Assert.assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		Assert.assertTrue("should be ShipmentRAO", resultAction instanceof ShipmentRAO);
		final ShipmentRAO discount = (ShipmentRAO) resultAction;
		assertEquals(new BigDecimal("0.00"), discount.getMode().getCost());

		// assert the meta-data has been set correctly
		assertEquals(ruleName1 + ".drl", discount.getFiredRuleCode());

		// assert the numbers add up
		// total is 100.03 - 10 = 90.03
		assertEquals(new BigDecimal("0.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("95.03"), cart.getSubTotal());
		assertEquals(new BigDecimal("95.03"), cart.getTotal());
		assertEquals(new BigDecimal("100.03"), cart.getTotalIncludingCharges());

	}

	@Test
	public void testChangeDeliveryModeFreeOvernightShipping() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "shipping-free_rrd" : "shipping-free";

		final DroolsRuleModel rule1 = getRuleForFile(ruleName1 + ".drl", "/droolsruleengineservices/test/rules/evaluation/",
				"de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleChangeDeliveryModeAction", "ruleChangeDeliveryModeAction"));

		final String ruleName2 = legacyRRDMode ? "shipping-overnight-free_rrd" : "shipping-overnight-free";
		final DroolsRuleModel rule2 = getRuleForFile(ruleName2 + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleChangeDeliveryModeAction", "ruleChangeDeliveryModeAction"));
		initializeRuleEngine(rule1, rule2);

		// simple cart with 2 entries and payment cost
		final CartRAO cart = createCartRAO("simple01", USD);
		cart.setEntries(set(createOrderEntryRAO(cart, "12.34", USD, 4, 0), createOrderEntryRAO(cart, "23.45", USD, 6, 1)));
		cart.setPaymentCost(new BigDecimal("5.00"));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		// pre-calculate totals so that rule can actually be triggered
		// note: this calculation happens as part of the DefaultCartRAOProvider
		// so here we have to do it explicitly
		getRuleEngineCalculationService().calculateTotals(cart);

		assertEquals(new BigDecimal("190.06"), cart.getTotal());
		assertEquals(new BigDecimal("200.06"), cart.getTotalIncludingCharges());
		assertEquals(new BigDecimal("190.06"), cart.getSubTotal());
		assertEquals(new BigDecimal("5.00"), cart.getPaymentCost());
		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());


		// now execute the engine that should trigger the free-shipping discount rule
		final Set<Object> facts = new HashSet<Object>()
		{
			{
				add(cart);
				add(createDeliveryModeRAO("free-overnight-domestic", "0.00", USD));
				add(createDeliveryModeRAO("free-domestic", "0.00", USD));
			}
		};
		final RuleEvaluationContext context = prepareContext(facts);

		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);
		Assert.assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		Assert.assertTrue("should be ShipmentRAO", resultAction instanceof ShipmentRAO);
		final ShipmentRAO discount = (ShipmentRAO) resultAction;
		Assert.assertEquals(new BigDecimal("0.00"), discount.getMode().getCost());

		// assert the meta-data has been set correctly
		assertEquals(ruleName2 + ".drl", discount.getFiredRuleCode());

		// assert the numbers add up
		// total is 100.03 - 10 = 90.03
		assertEquals(new BigDecimal("0.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("190.06"), cart.getSubTotal());
		assertEquals(new BigDecimal("190.06"), cart.getTotal());
		assertEquals(new BigDecimal("195.06"), cart.getTotalIncludingCharges());

	}

	@Test
	public void testAddProductDiscount() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "contains-rule_rrd" : "contains-rule";

		final DroolsRuleModel rule1 = getRuleForFile(ruleName1 + ".drl", "/droolsruleengineservices/test/rules/evaluation/",
				"de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderEntryPercentageDiscountAction", "ruleOrderEntryPercentageDiscountAction"));
		initializeRuleEngine(rule1);

		final CartRAO cart = createCartRAO("addProductDiscount", USD);

		final ProductRAO product = new ProductRAO();
		product.setCode("1234");
		final OrderEntryRAO entry = createOrderEntryRAO("25.00", USD, 2, 0, cart, product);
		cart.setEntries(set(entry));

		final HashSet<Object> facts = new HashSet<>();
		facts.add(cart);
		facts.add(entry);

		final RuleEvaluationContext context = prepareContext(facts);

		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);
		Assert.assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		Assert.assertTrue("should be DiscountRAO", resultAction instanceof DiscountRAO);
	}


	@Test
	public void testAddFixedPriceProductDiscount() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "fixedPrice-rule_rrd" : "fixedPrice-rule";
		final DroolsRuleModel rule1 = getRuleForFile(ruleName1 + ".drl", "/droolsruleengineservices/test/rules/evaluation/",
				"de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderEntryFixedPriceAction", "ruleOrderEntryFixedPriceAction"));
		initializeRuleEngine(rule1);


		final CartRAO cart = createCartRAO("fixedPriceTest1", USD);
		final ProductRAO product1 = new ProductRAO();
		product1.setCode("123456");

		final ProductRAO product2 = new ProductRAO();
		product2.setCode("987654");

		final OrderEntryRAO orderEntryRAO1 = createOrderEntryRAO("25.00", USD, 2, 0, cart, product1);
		final OrderEntryRAO orderEntryRAO2 = createOrderEntryRAO("25.00", USD, 3, 1, cart, product2);

		cart.setEntries(set(orderEntryRAO1, orderEntryRAO2));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		final HashSet<Object> facts = new HashSet<>();
		facts.add(cart);
		facts.add(orderEntryRAO1);
		facts.add(orderEntryRAO2);
		facts.add(product1);
		facts.add(product2);

		getRuleEngineCalculationService().calculateTotals(cart);

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("125.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("125.00"), cart.getTotal());
		assertEquals(new BigDecimal("130.00"), cart.getTotalIncludingCharges());

		final RuleEvaluationContext context = prepareContext(facts);

		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);

		assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		assertTrue("DiscountRAO type check", resultAction instanceof DiscountRAO);
		final DiscountRAO discount = (DiscountRAO) resultAction;

		assertEquals(ruleName1 + ".drl", discount.getFiredRuleCode());

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("115.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("115.00"), cart.getTotal());
		assertEquals(new BigDecimal("120.00"), cart.getTotalIncludingCharges());

	}

	@Test
	public void testAddFixedPriceProductDiscountConsumedEntries() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "fixedPrice_ConsumedEntries-rule_rrd" : "fixedPrice_ConsumedEntries-rule";
		final DroolsRuleModel rule1 = getRuleForFile(ruleName1 + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderEntryFixedPriceAction", "ruleOrderEntryFixedPriceAction"));
		initializeRuleEngine(rule1);


		final CartRAO cart = createCartRAO("fixedPriceConsumedEntriesTest", USD);
		final ProductRAO product1 = new ProductRAO();
		product1.setCode("123456");

		final ProductRAO product2 = new ProductRAO();
		product2.setCode("987654");

		final OrderEntryRAO orderEntryRAO1 = createOrderEntryRAO("25.00", USD, 2, 0, cart, product1);
		final OrderEntryRAO orderEntryRAO2 = createOrderEntryRAO("25.00", USD, 3, 1, cart, product2);

		cart.setEntries(set(orderEntryRAO1, orderEntryRAO2));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		final HashSet<Object> facts = new HashSet<>();
		facts.add(cart);
		facts.add(orderEntryRAO1);
		facts.add(orderEntryRAO2);
		facts.add(product1);
		facts.add(product2);

		getRuleEngineCalculationService().calculateTotals(cart);

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("125.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("125.00"), cart.getTotal());
		assertEquals(new BigDecimal("130.00"), cart.getTotalIncludingCharges());

		final RuleEvaluationContext context = prepareContext(facts);

		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);

		assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		assertTrue("DiscountRAO type check", resultAction instanceof DiscountRAO);
		final DiscountRAO discount = (DiscountRAO) resultAction;

		assertEquals("Number of consumed entries should be 1", 1, discount.getConsumedEntries().size());
		final OrderEntryConsumedRAO consumed = discount.getConsumedEntries().iterator().next();
		assertEquals("Consumed quantity should be 2", 2, consumed.getQuantity());
		assertEquals("Consumed entry should have firedRuleCode=" + ruleName1 + ".drl", ruleName1 + ".drl",
				consumed.getFiredRuleCode());

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("115.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("115.00"), cart.getTotal());
		assertEquals(new BigDecimal("120.00"), cart.getTotalIncludingCharges());

	}

	@Test
	public void testTwoEntryRulesWithAvailableQuantityCondition() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "availableQuantity_entryAbsoluteDiscount_rrd"
				: "availableQuantity_entryAbsoluteDiscount";
		final String ruleName2 = legacyRRDMode ? "availableQuantity_fixedPrice_rrd" : "availableQuantity_fixedPrice";

		final DroolsRuleModel rule1 = getRuleForFile(ruleName1 + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderEntryFixedDiscountAction", "ruleOrderEntryFixedDiscountAction"));
		rule1.setMaxAllowedRuns(Integer.valueOf(1));

		final DroolsRuleModel rule2 = getRuleForFile(ruleName2 + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderEntryFixedPriceAction", "ruleOrderEntryFixedPriceAction"));
		rule2.setMaxAllowedRuns(Integer.valueOf(1));
		initializeRuleEngine(rule1, rule2);

		final CartRAO cart = createCartRAO("twoEntryRulesWithAvailableQuantityConditionTest", USD);

		final ProductRAO product1 = createProduct("1422222");
		final ProductRAO product2 = createProduct("987654");

		final OrderEntryRAO orderEntryRAO1 = createOrderEntryRAO("152.00", USD, 2, 0, cart, product1);

		final OrderEntryRAO orderEntryRAO2 = createOrderEntryRAO("20.00", USD, 3, 1, cart, product2);

		cart.setEntries(set(orderEntryRAO1, orderEntryRAO2));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		final HashSet<Object> facts = (HashSet<Object>) addFacts(cart, orderEntryRAO1, orderEntryRAO2, product1, product2);

		getRuleEngineCalculationService().calculateTotals(cart);

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("364.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("364.00"), cart.getTotal());
		assertEquals(new BigDecimal("369.00"), cart.getTotalIncludingCharges());

		final RuleEvaluationContext context = prepareContext(facts);
		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);

		assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		assertTrue("DiscountRAO type check", resultAction instanceof DiscountRAO);

		final DiscountRAO discount = (DiscountRAO) resultAction;
		final OrderEntryRAO orderEntryRao = (OrderEntryRAO) discount.getAppliedToObject();

		assertEquals("Promotion should be applied on product 1422222", "1422222", orderEntryRao.getProductCode());
		assertEquals("Number of consumed entries should be 1", 1, discount.getConsumedEntries().size());
		final OrderEntryConsumedRAO consumed = discount.getConsumedEntries().iterator().next();
		assertEquals("Consumed quantity should be 2", 2, consumed.getQuantity());
		assertEquals("Consumed entry should have firedRuleCode=availableQuantity_entryAbsoluteDiscount.drl",
				ruleName1 + ".drl", consumed.getFiredRuleCode());

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("344.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("344.00"), cart.getTotal());
		assertEquals(new BigDecimal("349.00"), cart.getTotalIncludingCharges());

	}

	@Test
	public void testRuleForMoreEntriesAllConsumed() throws IOException
	{
		final String ruleName1 = legacyRRDMode ? "moreEntriesAllConsumed_rrd" : "moreEntriesAllConsumed";
		final DroolsRuleModel rule = getRuleForFile(ruleName1 + ".drl",
				"/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.ruleengineservices.test",
				new SingletonMap("ruleOrderEntryFixedPriceAction", "ruleOrderEntryFixedPriceAction"));
		rule.setMaxAllowedRuns(Integer.valueOf(1));
		initializeRuleEngine(rule);

		final CartRAO cart = createCartRAO("moreEntriesAllConsumed", USD);

		final ProductRAO product1 = createProduct("1422222");
		final ProductRAO product2 = createProduct("123456");
		final ProductRAO product3 = createProduct("654321");
		final ProductRAO product4 = createProduct("555555");

		final OrderEntryRAO orderEntryRAO1 = createOrderEntryRAO("150.00", USD, 1, 0, cart, product1);
		final OrderEntryRAO orderEntryRAO2 = createOrderEntryRAO("200.00", USD, 1, 1, cart, product2);
		final OrderEntryRAO orderEntryRAO3 = createOrderEntryRAO("100.00", USD, 1, 2, cart, product3);
		//not affected by the rule, should not be consumed
		final OrderEntryRAO orderEntryRAO4 = createOrderEntryRAO("10.00", USD, 1, 3, cart, product4);

		cart.setEntries(set(orderEntryRAO1, orderEntryRAO2, orderEntryRAO3, orderEntryRAO4));
		cart.setDeliveryCost(new BigDecimal("5.00"));

		final HashSet<Object> facts = (HashSet<Object>) addFacts(cart, product1, product2, product3, product4, orderEntryRAO1,
				orderEntryRAO2, orderEntryRAO3, orderEntryRAO4);

		getRuleEngineCalculationService().calculateTotals(cart);

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("460.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("460.00"), cart.getTotal());
		assertEquals(new BigDecimal("465.00"), cart.getTotalIncludingCharges());

		final RuleEvaluationContext context = prepareContext(facts);
		final RuleEvaluationResult result = getCommerceRuleEngineService().evaluate(context);
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);

		assertEquals("Should have 3 actions", 3, resultRAO.getActions().size());

		final Iterator iterator = resultRAO.getActions().iterator();
		final AbstractRuleActionRAO resultAction1 = (AbstractRuleActionRAO) iterator.next();
		assertTrue("DiscountRAO type check for first action", resultAction1 instanceof DiscountRAO);

		final AbstractRuleActionRAO resultAction2 = (AbstractRuleActionRAO) iterator.next();
		assertTrue("DiscountRAO type check for second action", resultAction2 instanceof DiscountRAO);

		final AbstractRuleActionRAO resultAction3 = (AbstractRuleActionRAO) iterator.next();
		assertTrue("DiscountRAO type check for third action", resultAction3 instanceof DiscountRAO);

		final DiscountRAO discount1 = (DiscountRAO) resultAction1;
		final OrderEntryRAO orderEntryRAORetrieved1 = (OrderEntryRAO) discount1.getAppliedToObject();
		final DiscountRAO discount2 = (DiscountRAO) resultAction2;
		final OrderEntryRAO orderEntryRAORetrieved2 = (OrderEntryRAO) discount2.getAppliedToObject();
		final DiscountRAO discount3 = (DiscountRAO) resultAction3;
		final OrderEntryRAO orderEntryRAORetrieved3 = (OrderEntryRAO) discount3.getAppliedToObject();

		final List<String> consumedProductCodes = Arrays.asList(orderEntryRAORetrieved1.getProductCode(),
				orderEntryRAORetrieved2.getProductCode(), orderEntryRAORetrieved3.getProductCode());

		assertTrue(consumedProductCodes.contains("1422222"));
		assertTrue(consumedProductCodes.contains("123456"));
		assertTrue(consumedProductCodes.contains("654321"));


		assertEquals("Number of consumed entries for first entry should be 1", 1, discount1.getConsumedEntries().size());
		final OrderEntryConsumedRAO consumed1 = discount1.getConsumedEntries().iterator().next();
		assertEquals("Consumed quantity for first entry should be 1", 1, consumed1.getQuantity());
		assertEquals("Consumed entry should have firedRuleCode=moreEntriesAllConsumed.drl", ruleName1 + ".drl",
				consumed1.getFiredRuleCode());


		assertEquals("Number of consumed entries for second entry should be 1", 1, discount2.getConsumedEntries().size());
		final OrderEntryConsumedRAO consumed2 = discount2.getConsumedEntries().iterator().next();
		assertEquals("Consumed quantity for second entry should be 1", 1, consumed2.getQuantity());
		assertEquals("Consumed entry should have firedRuleCode=moreEntriesAllConsumed.drl", ruleName1 + ".drl",
				consumed2.getFiredRuleCode());

		assertEquals("Number of consumed entries for second entry should be 1", 1, discount3.getConsumedEntries().size());
		final OrderEntryConsumedRAO consumed3 = discount3.getConsumedEntries().iterator().next();
		assertEquals("Consumed quantity for second entry should be 1", 1, consumed3.getQuantity());
		assertEquals("Consumed entry should have firedRuleCode=moreEntriesAllConsumed.drl", ruleName1 + ".drl",
				consumed3.getFiredRuleCode());

		assertEquals(new BigDecimal("5.00"), cart.getDeliveryCost());
		assertEquals(new BigDecimal("160.00"), cart.getSubTotal());
		assertEquals(new BigDecimal("160.00"), cart.getTotal());
		assertEquals(new BigDecimal("165.00"), cart.getTotalIncludingCharges());
	}

	protected ProductRAO createProduct(final String code)
	{
		final ProductRAO product1 = new ProductRAO();
		product1.setCode(code);
		return product1;
	}

	protected OrderEntryRAO createOrderEntryRAO(final String basePrice, final String currencyIso, final int quantity,
			final int entryNumber, final CartRAO cart, final ProductRAO product)
	{
		final OrderEntryRAO orderEntryRAO1 = createOrderEntryRAO(basePrice, currencyIso, quantity, entryNumber);
		orderEntryRAO1.setProductCode(product.getCode());
		orderEntryRAO1.setOrder(cart);
		return orderEntryRAO1;
	}

	protected Set<Object> addFacts(final Object... singleFacts)
	{
		final Set<Object> facts = new HashSet<Object>();
		for (final Object singleFact : singleFacts)
		{
			facts.add(singleFact);
		}
		return facts;
	}
}
