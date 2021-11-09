/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.configurablebundleservices.bundle.BundleRuleService;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;


/**
 * Integration test suite for {@link FindBundlePricingWithCurrentPriceFactoryStrategy} and
 * {@link DefaultBundleRuleService}
 */
@IntegrationTest
public class DefaultBundleRuleServiceNestedIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultBundleRuleServiceNestedIntegrationTest.class);
	private static final String TEST_BASESITE_UID = "testSite";

    protected static final String PRODUCT01 = "PRODUCT01";
	protected static final String PRODUCT02 = "PRODUCT02";
	protected static final String PRODUCT05 = "PRODUCT05";
	protected static final String PRODUCT06 = "PRODUCT06";
	protected static final String PREMIUM01 = "PREMIUM01";
    protected static final String PREMIUM02 = "PREMIUM02";

	protected static final String REGULAR_COMPONENT = "ProductComponent1";
    protected static final String OPTIONAL_COMPONENT = "OptionalComponent";
	protected static final String LEAF_BUNDLE_TEMPLATE_1 = "LeafBundleTemptate1";
	protected static final String LEAF_BUNDLE_TEMPLATE_2 = "LeafBundleTemptate2";

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	protected CartModel cart;
	protected UnitModel unitModel;
	protected CurrencyModel currency;
	@Resource
	protected UserService userService;
	@Resource
	private UnitService unitService;
	@Resource
	protected CommerceCartService commerceCartService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	protected ProductService productService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private BundleRuleService bundleRuleService;
	@Resource
	private BundleTemplateService bundleTemplateService;
	@Resource
	private EntryGroupService entryGroupService;
	@Resource
	private CommonI18NService commonI18NService;

	@Before
	public void setUp() throws Exception
	{
		LOG.debug("Preparing test data");
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		try
		{
			importCsv("/configurablebundleservices/test/cartRegistration.impex", "utf-8");
			importCsv("/configurablebundleservices/test/nestedBundleTemplates.impex", "utf-8");
		}
		finally
		{
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
		}
		cart = userService.getUserForUID("bundle").getCarts().iterator().next();
		unitModel = unitService.getUnitForCode("pieces");
		currency = commonI18NService.getCurrency("USD");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
	}

    @Test
    public void shouldDisableInOneComponent() throws CommerceCartModificationException
    {
		final CommerceCartModification commerceCartModification = addToCart(PRODUCT01, REGULAR_COMPONENT, null);
		thrown.expect(CommerceCartModificationException.class);
        thrown.expectMessage("1");
        addToCart(PRODUCT06, REGULAR_COMPONENT, commerceCartModification.getEntryGroupNumbers());
    }

	@Test
	public void gettingDisablingRulesShouldHandleNullOrder()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter order can not be null");
		bundleRuleService.getDisableProductBundleRules(getProduct(PRODUCT01), new EntryGroup(), null);
	}

	@Test
	public void gettingDisablingRuleShouldHandleNullGroup()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter groupNumber can not be null");
		bundleRuleService.getDisableProductBundleRules(getProduct(PRODUCT01), new EntryGroup(), cart);
	}

    @Test
    public void shouldDisableInDifferentComponents() throws CommerceCartModificationException
    {
		final CommerceCartModification commerceCartModification = addToCart(PRODUCT01, REGULAR_COMPONENT, Collections.emptySet());
		final EntryGroup regularComponentGroup = bundleTemplateService.getBundleEntryGroup(cart, commerceCartModification.getEntryGroupNumbers());
		assertNotNull(regularComponentGroup);
		final EntryGroup rootGroup = entryGroupService.getRoot(cart, regularComponentGroup.getGroupNumber());
		assertNotNull(rootGroup);
		EntryGroup optionalComponentGroup = entryGroupService.getLeaves(rootGroup).stream().
			filter(entryGroup -> OPTIONAL_COMPONENT.equals(entryGroup.getExternalReferenceId())).
			findAny().orElse(null);
		assertNotNull(optionalComponentGroup);
		thrown.expect(CommerceCartModificationException.class);
        thrown.expectMessage("1");
		addToCart(PRODUCT06, null, Collections.singleton(optionalComponentGroup.getGroupNumber()));
		assertEquals(1, cart.getEntries().size());
    }

    @Test
    public void shouldGetDisablingRuleForBundleProduct()
    {
        final DisableProductBundleRuleModel rule = bundleRuleService
                .getDisableRuleForBundleProduct(getBundleTemplate(REGULAR_COMPONENT), getProduct(PRODUCT01), getProduct(PRODUCT06));
        assertNotNull(rule);
    }

    @Test
    public void shouldNotReturnDisablingRulesForOneProduct()
    {
        final DisableProductBundleRuleModel rule = bundleRuleService
                .getDisableRuleForBundleProduct(getBundleTemplate(REGULAR_COMPONENT), getProduct(PRODUCT01), getProduct(PRODUCT05));
        assertNull(rule);
    }

    @Test
    public void shouldNotReturnDisablingRulesForForeignProduct()
    {
        final DisableProductBundleRuleModel rule = bundleRuleService
                .getDisableRuleForBundleProduct(getBundleTemplate(REGULAR_COMPONENT), getProduct(PRODUCT01), getProduct(PREMIUM01));
        assertNull(rule);
    }

    @Test
    public void shouldRespectComponentWhileGettingDisablingRules()
    {
        final ProductModel conditionalProduct = getProduct(PRODUCT01);
        final ProductModel targetProduct = getProduct(PRODUCT06);
        final DisableProductBundleRuleModel rule = bundleRuleService
                .getDisableRuleForBundleProduct(getBundleTemplate(OPTIONAL_COMPONENT), conditionalProduct, targetProduct);
        assertNotNull(rule);
        assertThat(rule.getConditionalProducts(), hasItem(conditionalProduct));
        assertThat(rule.getTargetProducts(), hasItem(targetProduct));
    }

    @Test
    public void gettingDisablingRulesShouldHandleNullBundleId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter targetComponent can not be null");
        bundleRuleService.getDisableRuleForBundleProduct(null, getProduct(PRODUCT01), getProduct(PRODUCT05));
    }

    @Test
    public void gettingDisablingRulesShouldHandleFirstProductOfNull()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter product1 can not be null");
        bundleRuleService.getDisableRuleForBundleProduct(getBundleTemplate(OPTIONAL_COMPONENT), null, getProduct(PRODUCT05));
    }

    @Test
    public void gettingDisablingRulesShouldHandleSecondProductOfNull()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter product2 can not be null");
        bundleRuleService.getDisableRuleForBundleProduct(getBundleTemplate(OPTIONAL_COMPONENT), getProduct(PRODUCT01), null);
    }

	@Test
	public void shouldNotReturnChangePriceRuleWhenNoSuchConditionalProductsInChangePriceRule()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_1);
		final ProductModel conditionalProduct = getProduct(PRODUCT06);
		final ProductModel targetProduct = getProduct(PRODUCT02);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNull(priceRule);
	}

	@Test
	public void shouldNotReturnChangePriceRuleWhenNoSuchTargetProductsInChangePriceBundleRule()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_1);
		final ProductModel conditionalProduct = getProduct(PRODUCT06);
		final ProductModel targetProduct = getProduct(PRODUCT01);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNull(priceRule);
	}

	@Test
	public void shouldNotReturnChangePriceRuleWhenNoSuchCurrencyInChangePriceRule()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_2);
		final ProductModel conditionalProduct = getProduct(PREMIUM02);
		final ProductModel targetProduct = getProduct(PREMIUM01);
		final CurrencyModel jpyCurrency = commonI18NService.getCurrency("JPY");

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, jpyCurrency);
		assertNull(priceRule);
	}

	@Test
	public void shouldNotReturnChangePriceRuleForDifferentCurrencies()
	{
		final CurrencyModel jpyCurrency = commonI18NService.getCurrency("JPY");
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_1);
		final ProductModel conditionalProduct = getProduct(PRODUCT01);
		final ProductModel targetProduct = getProduct(PRODUCT02);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, jpyCurrency);
		assertNotNull(priceRule);
		assertEquals("price__PRODUCT02_with_PRODUCT01_JPY", priceRule.getId());
		assertEquals(100, priceRule.getPrice().intValue());
		assertEquals(jpyCurrency, priceRule.getCurrency());

		priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNotNull(priceRule);
		assertEquals("price_PRODUCT02_with_PRODUCT01", priceRule.getId());
		assertEquals(1, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());
	}

	@Test
	public void shouldReturnChangePriceRuleForProductsInOneComponent()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_1);
		final ProductModel conditionalProduct = getProduct(PRODUCT01);
		final ProductModel targetProduct = getProduct(PRODUCT02);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNotNull(priceRule);
		assertEquals("price_PRODUCT02_with_PRODUCT01", priceRule.getId());
		assertEquals(1, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());
	}

	@Test
	public void shouldReturnChangePriceRuleForProductsInDifferentComponents()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_1);
		final ProductModel conditionalProduct = getProduct(PREMIUM01);
		final ProductModel targetProduct = getProduct(PRODUCT01);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNotNull(priceRule);
		assertEquals("price_PRODUCT01_with_PREMIUM01", priceRule.getId());
		assertEquals(99, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());
	}

	@Test
	public void shouldNotReturnChangePriceRuleWhenRuleTypeIsAll()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_1);
		final ProductModel conditionalProduct = getProduct(PRODUCT05);
		final ProductModel targetProduct = getProduct(PRODUCT01);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNull(priceRule);
	}

	@Test
	public void shouldReturnChangePriceRuleWithLowestPrice()
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_2);
		final ProductModel conditionalProduct = getProduct(PREMIUM02);
		final ProductModel targetProduct = getProduct(PREMIUM01);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRule(
				component, targetProduct, conditionalProduct, currency);
		assertNotNull(priceRule);
		assertEquals("price_PREMIUM01_with_PREMIUM02_cheap", priceRule.getId());
		assertEquals(99, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());
	}

	@Test
	public void shouldReturnChangePriceRuleWithLowestPriceByOrderEntry() throws CommerceCartModificationException
	{
		final BundleTemplateModel component = getBundleTemplate(LEAF_BUNDLE_TEMPLATE_2);
		final ProductModel conditionalProduct = getProduct(PREMIUM02);
		final ProductModel targetProduct = getProduct(PREMIUM01);

		CommerceCartModification mod = addToCart(PREMIUM02, LEAF_BUNDLE_TEMPLATE_2, null);
		assertEquals(1, cart.getEntries().size());

		mod = addToCart(targetProduct.getCode(), null, mod.getEntryGroupNumbers());
		assertEquals(2, cart.getEntries().size());

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService().getChangePriceBundleRuleForOrderEntry(cart
				.getEntries().get(1));

		assertNotNull(priceRule);
		assertTrue(priceRule.getTargetProducts().contains(targetProduct));
		assertTrue(priceRule.getConditionalProducts().contains(conditionalProduct));
		assertEquals("price_PREMIUM01_with_PREMIUM02_cheap", priceRule.getId());
		assertEquals(99, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());
	}

	@Test
	public void shouldNotReturnChangePriceRuleWithLowestPriceForForeignProduct() throws CommerceCartModificationException
	{
		final ProductModel targetProduct = getProduct(PREMIUM02);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService()
				.getChangePriceBundleRuleWithLowestPrice(targetProduct, currency);

		assertNull(priceRule);
	}

	@Test
	public void shouldNotReturnChangePriceRuleWithLowestPriceForForeignCurrency() throws CommerceCartModificationException
	{
		final CurrencyModel jpyCurrency = commonI18NService.getCurrency("JPY");
		final ProductModel targetProduct = getProduct(PRODUCT01);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService()
				.getChangePriceBundleRuleWithLowestPrice(targetProduct, jpyCurrency);

		assertNull(priceRule);
	}

	@Test
	public void shouldNotReturnChangePriceRuleWithLowestPriceIfRuleTypeAll() throws CommerceCartModificationException
	{
		final ProductModel targetProduct = getProduct(PRODUCT06);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService()
				.getChangePriceBundleRuleWithLowestPrice(targetProduct, currency);

		assertNotNull(priceRule);
		assertTrue(priceRule.getTargetProducts().contains(targetProduct));
		assertEquals("price_PRODUCT02_or_PRODUCT06_with_PREMIUM01_and_PRODUCT05", priceRule.getId());
		assertEquals(400, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());
	}

	@Test
	public void shouldReturnChangePriceRuleWithLowestPriceForCorrespondingProduct() throws CommerceCartModificationException
	{
		final CurrencyModel jpyCurrency = commonI18NService.getCurrency("JPY");
		final ProductModel targetProduct = getProduct(PRODUCT02);

		ChangeProductPriceBundleRuleModel priceRule = getBundleRuleService()
				.getChangePriceBundleRuleWithLowestPrice(targetProduct, currency);

		assertNotNull(priceRule);
		assertTrue(priceRule.getTargetProducts().contains(targetProduct));
		assertEquals("price_PRODUCT02_with_PRODUCT01", priceRule.getId());
		assertEquals(1, priceRule.getPrice().intValue());
		assertEquals(currency, priceRule.getCurrency());

		priceRule = getBundleRuleService()
				.getChangePriceBundleRuleWithLowestPrice(targetProduct, jpyCurrency);

		assertNotNull(priceRule);
		assertTrue(priceRule.getTargetProducts().contains(targetProduct));
		assertEquals("price__PRODUCT02_with_PRODUCT01_JPY", priceRule.getId());
		assertEquals(100, priceRule.getPrice().intValue());
		assertEquals(jpyCurrency, priceRule.getCurrency());
	}

	protected BundleRuleService getBundleRuleService()
	{
		return bundleRuleService;
	}

	public void setBundleRuleService(final BundleRuleService bundleRuleService)
	{
		this.bundleRuleService = bundleRuleService;
	}

	protected CommerceCartModification addToCart(final String productCode, final String componentId, final Set<Integer> groupNumbers)
			throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cart);
		parameter.setProduct(getProduct(productCode));
		parameter.setQuantity(1);
		parameter.setUnit(unitModel);
		parameter.setBundleTemplate(componentId == null? null : getBundleTemplate(componentId));
		parameter.setEntryGroupNumbers(groupNumbers);
		return commerceCartService.addToCart(parameter);
	}

	protected CommerceCartModification addToCart(final String productCode)
			throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setCart(cart);
		parameter.setProduct(getProduct(productCode));
		parameter.setQuantity(1);
		parameter.setUnit(unitModel);
		return commerceCartService.addToCart(parameter);
	}

	protected ProductModel getProduct(final String code)
	{
		return productService.getProductForCode(getCatalog(), code);
	}

	protected BundleTemplateModel getBundleTemplate(final String templateId)
	{
		final BundleTemplateModel exampleModel = new BundleTemplateModel();
		exampleModel.setId(templateId);
		exampleModel.setCatalogVersion(getCatalog());
		return flexibleSearchService.getModelByExample(exampleModel);
	}

	protected CatalogVersionModel getCatalog()
	{
		return catalogVersionService.getCatalogVersion("testCatalog", "Online");
	}
}