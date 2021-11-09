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
package de.hybris.platform.promotionengineservices.promotionengine.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.*;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.ruleengineservices.converters.populator.CartModelBuilder;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.valueOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;



@IntegrationTest
public class DefaultEntryDiscountsPromotionEngineServiceIntegrationTest extends PromotionEngineServiceBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private RuleEngineService commerceRuleEngineService;
	@Resource
	private DefaultPromotionEngineService defaultPromotionEngineService;
	@Resource
	private RuleEngineContextDao ruleEngineContextDao;

	@Resource
	private CatalogVersionDao catalogVersionDao;
	@Resource
	private PromotionDao promotionDao;
	@Resource
	private RuleEngineTestSupportService ruleEngineTestSupportService;
	@Resource
	private SessionService sessionService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private DefaultCalculationService defaultCalculationService;

	private DroolsKIEBaseModel kieBaseModel;


	private static String TEST_DEFAULT_PRODUCT_CODE = "HW1210-341122";

	@Before
	public void setUp() throws ImpExException, IOException
	{
		// setup with promotionsenginesetup impex
		super.importCsv("/promotionengineservices/test/promotionenginesetup.impex", "UTF-8");
		kieBaseModel = getKieBaseModel("promotions-base-junit");

		final Collection<CatalogVersionModel> catalogVersions = catalogVersionDao
				.findCatalogVersions("testMappingCatalog", "Online");
		final Session session = sessionService.createNewSession();
		session.setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS, catalogVersions);
	}

	@Test
	public void testOrderEntryDiscountsWithPromotionGroups() throws IOException
	{

		final AbstractOrderModel cart = buildCartForUserWithCodeAndCurrency("1234567", TEST_DEFAULT_PRODUCT_CODE);

		for(final AbstractOrderEntryModel entry: cart.getEntries()) {
			getModelService().save(entry);
			final Collection<TaxValue> entryTaxes =  new ArrayList<>();
			final TaxValue taxValue = new TaxValue("taxtest", 2.0d, false,"USD");
			entryTaxes.add(taxValue);
			entry.setTaxValues(entryTaxes);
			entry.setBasePrice(Double.valueOf(100.0d));
			entry.setQuantity(Long.valueOf(1));
			entry.setOrder(cart);
			final List<DiscountValue> entryDiscounts = new ArrayList<>();
			final DiscountValue discountValue1 = new DiscountValue("test-10", 10.0d, false, 0.0d, null, false);
			final DiscountValue discountValue2 = new DiscountValue("test-20", 20.0d, false, 0.0d, null, false);
			entryDiscounts.add(discountValue1);
			entryDiscounts.add(discountValue2);
			entry.setDiscountValues(entryDiscounts);

		}
		final String fileName = "orderFiftyPercentageDiscount";
		final AbstractRuleEngineRuleModel rule = getRuleForFile(fileName + ".drl",
				"/promotionengineservices/test/rules/");
		rule.setCode(fileName);
		rule.setVersion(valueOf(0L));
		ruleEngineTestSupportService
				.decorateRuleForTest(ImmutableMap.of("ruleOrderPercentageDiscountAction", "ruleOrderPercentageDiscountAction"))
				.accept(rule);
		modelService.save(rule);
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("promoGroup1");
		final Collection<PromotionGroupModel> groupList = new ArrayList<PromotionGroupModel>();
		groupList.add(group);
		initializeRuleEngine(rule);
		getModelService().save(cart);
		defaultPromotionEngineService.updatePromotions(groupList,cart);
		getModelService().refresh(cart);
		Assert.assertEquals(36.0, cart.getTotalPrice().doubleValue(), 0.0d);

	}


	/**
	 * Creates a (non-persisted) AbstractRuleEngineRuleModel based on the given file and path. Note that this
	 * implementation assumes that the fileName matches the rule's name (excluding the .drl extension).
	 *
	 * @param fileName
	 * @param path
	 * @return new AbstractRuleEngineRuleModel
	 * @throws IOException
	 */
	protected AbstractRuleEngineRuleModel getRuleForFile(final String fileName, final String path) throws IOException
	{
		final DroolsRuleModel rule = (DroolsRuleModel) ruleEngineTestSupportService.createRuleModel();
		rule.setCode(fileName);
		rule.setUuid(fileName.substring(0, fileName.length() - 4));
		rule.setActive(Boolean.TRUE);
		rule.setRuleContent(readRuleFile(fileName, path));
		rule.setRuleType(RuleType.PROMOTION);
		rule.setKieBase(kieBaseModel);
		return rule;
	}

	protected CartModel buildCartForUserWithCodeProductAndCurrency(final String code, final String productCode, final int items)
	{
		final CartModel cart = productCode == null ? buildCartWithCodeAndCurrency(code)
				: buildCartWithCodeProductAndCurrency(code, productCode, items);
		final UserModel user = new UserModel();
		user.setUid(UUID.randomUUID().toString());
		cart.setUser(user);
		cart.setDate(new Date());
		return cart;
	}

	protected CartModel buildCartForUserWithCodeAndCurrency(final String code, final String productCode)
	{
		return buildCartForUserWithCodeProductAndCurrency(code, productCode, 0);
	}

	protected CartModel buildCartWithCodeAndCurrency(final String code)
	{
		return buildCartWithCodeProductAndCurrency(code, null, 0);
	}

	protected CartModel buildCartWithCodeProductAndCurrency(final String code, final String productCode, final int items)
	{
		CartModelBuilder.CartModelDraft cartModelDraft = CartModelBuilder.newCart(code);
		if (Objects.nonNull(productCode))
		{
			cartModelDraft = cartModelDraft.addProduct(getOnlineCatalogVersion(), productCode, 1, 1.0, items);
		}

		setCurrency(cartModelDraft, "USD");

		return cartModelDraft.getModel();
	}

	protected void setCurrency(final CartModelBuilder.CartModelDraft cartModelDraft, final String isoCode)
	{
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(isoCode);
		cartModelDraft.getModel().setCurrency(flexibleSearchService.getModelByExample(currency));
	}

	protected void initializeRuleEngine(final AbstractRuleEngineRuleModel... rules)
	{
		final AbstractRuleEngineContextModel abstractContext = getRuleEngineContextDao()
				.findRuleEngineContextByName("promotions-junit-context");
		final List<RuleEngineActionResult> results = getCommerceRuleEngineService().initialize(Collections.singletonList(
				ruleEngineTestSupportService.getTestRulesModule(abstractContext, stream(rules).collect(Collectors.toSet()))), false,
				false).getResults();

		if (results.stream().anyMatch(RuleEngineActionResult::isActionFailed))
		{
			Assert.fail(
					"rule engine initialization failed with errors: " + results.stream().filter(RuleEngineActionResult::isActionFailed)
							.map(r -> r.getMessagesAsString(MessageLevel.ERROR)).collect(
									toList()));
		}
	}

	protected CatalogVersionModel getOnlineCatalogVersion()
	{
		return getCatalogVersionDao()
				.findCatalogVersions("testMappingCatalog", "Online").iterator().next();
	}


	protected ModelService getModelService()
	{
		return modelService;
	}

	protected RuleEngineService getCommerceRuleEngineService()
	{
		return commerceRuleEngineService;
	}

	protected RuleEngineContextDao getRuleEngineContextDao()
	{
		return ruleEngineContextDao;
	}


	protected CatalogVersionDao getCatalogVersionDao()
	{
		return catalogVersionDao;
	}

}
