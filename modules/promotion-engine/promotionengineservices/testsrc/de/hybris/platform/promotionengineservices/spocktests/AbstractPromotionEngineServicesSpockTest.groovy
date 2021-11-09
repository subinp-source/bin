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
package de.hybris.platform.promotionengineservices.spocktests

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import de.hybris.platform.basecommerce.model.site.BaseSiteModel
import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.category.CategoryService
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.Registry
import de.hybris.platform.core.enums.GroupType
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.order.AbstractOrderEntryModel
import de.hybris.platform.core.model.order.CartEntryModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.core.order.EntryGroup
import de.hybris.platform.europe1.model.PriceRowModel
import de.hybris.platform.jalo.JaloSession
import de.hybris.platform.jalo.order.price.PriceInformation
import de.hybris.platform.jalo.user.User
import de.hybris.platform.order.CalculationService
import de.hybris.platform.order.CartFactory
import de.hybris.platform.order.CartService
import de.hybris.platform.order.daos.DeliveryModeDao
import de.hybris.platform.order.exceptions.CalculationException
import de.hybris.platform.product.PriceService
import de.hybris.platform.product.ProductService
import de.hybris.platform.promotionengineservices.dao.PromotionDao
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel
import de.hybris.platform.promotionengineservices.promotionengine.PromotionEngineService
import de.hybris.platform.promotionengineservices.promotionengine.impl.DefaultPromotionEngineService
import de.hybris.platform.promotionengineservices.test.configuration.PromotionEngineTestSupportSwitchService
import de.hybris.platform.promotions.PromotionsService
import de.hybris.platform.promotions.model.AbstractPromotionActionModel
import de.hybris.platform.promotions.model.PromotionGroupModel
import de.hybris.platform.promotions.model.PromotionResultModel
import de.hybris.platform.ruleengine.MessageLevel
import de.hybris.platform.ruleengine.RuleEngineActionResult
import de.hybris.platform.ruleengine.RuleEngineService
import de.hybris.platform.ruleengine.RuleEvaluationResult
import de.hybris.platform.ruleengine.dao.DroolsKIEBaseDao
import de.hybris.platform.ruleengine.dao.EngineRuleDao
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao
import de.hybris.platform.ruleengine.dao.RulesModuleDao
import de.hybris.platform.ruleengine.enums.RuleType
import de.hybris.platform.ruleengine.init.InitializationFuture
import de.hybris.platform.ruleengine.model.*
import de.hybris.platform.ruleengineservices.action.RuleActionService
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService
import de.hybris.platform.ruleengineservices.configuration.Switch
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel
import de.hybris.platform.ruleengineservices.order.dao.ExtendedOrderDao
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO
import de.hybris.platform.ruleengineservices.rao.DiscountRAO
import de.hybris.platform.ruleengineservices.rao.ProductRAO
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO
import de.hybris.platform.ruleengineservices.rule.services.RuleService
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import de.hybris.platform.servicelayer.media.MediaService
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import de.hybris.platform.servicelayer.session.SessionService
import de.hybris.platform.servicelayer.user.UserService
import de.hybris.platform.servicelayer.user.daos.UserGroupDao
import de.hybris.platform.site.BaseSiteService
import de.hybris.platform.util.TaxValue
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Ignore
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import static com.google.common.base.Preconditions.checkState
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull
import static java.util.Collections.singleton
import static java.util.Collections.singletonList
import static java.util.Objects.nonNull
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty

@Ignore
class AbstractPromotionEngineServicesSpockTest extends ServicelayerSpockSpecification
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractPromotionEngineServicesSpockTest.class)

	public static final String BASESITE = "testSite"

	@Resource
	RuleEngineService commerceRuleEngineService

	@Resource
	ModelService modelService

	@Resource
	PromotionEngineService promotionEngineService

	@Resource
	FlexibleSearchService flexibleSearchService

	@Resource
	PriceService priceService

	@Resource
	CartService cartService

	@Resource
	CalculationService calculationService

	@Resource
	RuleActionService ruleActionService

	@Resource
	RuleEngineContextDao ruleEngineContextDao

	@Resource
	CommonI18NService commonI18NService

	@Resource
	UserService userService

	@Resource
	ExtendedOrderDao extendedOrderDao

	@Resource
	ProductService productService

	@Resource
	CategoryService categoryService

	@Resource
	MediaService mediaService

	@Resource
	RuleService ruleService

	@Resource
	RuleCompilerService ruleCompilerService

	@Resource
	DroolsKIEBaseDao droolsKIEBaseDao

	@Resource
	RulesModuleDao rulesModuleDao

	@Resource
	PromotionDao promotionDao

	@Resource
	DeliveryModeDao deliveryModeDao

	@Resource
	BaseSiteService baseSiteService

	@Resource
	SessionService sessionService

	@Resource
	EngineRuleDao engineRuleDao

	@Resource
	CartFactory cartFactory

	@Resource
	PromotionsService promotionsService

	@Resource
	CatalogVersionService catalogVersionService

	@Resource
	UserGroupDao userGroupDao

	@Resource
	PromotionEngineTestSupportSwitchService ruleSwitchService

	// values to reset as we don't run transactional tests
	def previousCatalogVersions
	def previousUser
	def previousCurrency
	def previousLanguage
	def previousBaseSite

	def setup()
	{
		importCsv("/promotionengineservices/test/promotionengineTestData.impex", "utf-8")
		importCsv("/promotionengineservices/test/promotionengineTestData-sourceRule.impex", "utf-8")
	}

	def cleanup()
	{
		removeAllPromotions()
	}

	def createUser(String userId, String userGroupId)
	{
		final CustomerModel userModel = new CustomerModel()
		userModel.setUid(userId)
		userModel.setGroups(Collections.singleton(userGroupDao.findUserGroupByUid(userGroupId)))
		modelService.save(userModel)
	}

	CartModel addProductToCart(String productCode, int quantity)
	{
		CartModel cart = getCartService().getSessionCart()
		getCatalogVersionService().setSessionCatalogVersion('testCatalog', 'Online')
		ProductModel product = getProductService().getProductForCode(productCode)
		getCartService().addNewEntry(cart, product, quantity, product.getUnit())
		getModelService().save(cart)
		getCalculationService().calculate(cart)
		return cart
	}

	String echoCart()
	{
		CartModel cart = getCartService().getSessionCart()
		String cartString = ''
		cartString += 'Cart ' + cart.code + '\n'
		cartString += '\n'
		cartString += 'Entries: \n'
		cartString += '\tPosition\tSKU\tQuantity\tBase Price\tTotal Price\n'

		cart.entries.each
		{
			cartString += '\t' + it.entryNumber + '\t\t' + it.product.code + '\t' + it.quantity + '\t\t' + it.basePrice + '\t\t' + it.totalPrice
			cartString += '\n'
		}
		cartString += '\n'
		cartString += 'Cart subtotal: ' + cart.subtotal + '\n'
		cartString += 'Discounts: ' + cart.totalDiscounts + '\n'
		cartString += 'Tax: ' + cart.totalTax + '\n'
		cartString += 'Total price: ' + cart.totalPrice + '\n'
		return cartString
	}

	def prepareUser(String userId, String forCurrency)
	{
		previousUser = userService.getCurrentUser()
		previousCatalogVersions = catalogVersionService.getSessionCatalogVersions()
		UserModel userModel = userId.isEmpty() ? getUserService().getAnonymousUser() : getUserService().getUserForUID(userId)
		userService.setCurrentUser(userModel)
		catalogVersionService.setSessionCatalogVersion('testCatalog', 'Online')
		previousCurrency = userModel.sessionCurrency
		previousLanguage = userModel.sessionLanguage
		previousBaseSite = baseSiteService.currentBaseSite
		userModel.setSessionCurrency(getCommonI18NService().getCurrency(forCurrency))
		userModel.setSessionLanguage(getCommonI18NService().getLanguage('en'))
		getCommonI18NService().setCurrentCurrency(getCommonI18NService().getCurrency(forCurrency))
		getBaseSiteService().setCurrentBaseSite('testSite', false)
	}

	def tearDownUser()
	{
		catalogVersionService.setSessionCatalogVersions(previousCatalogVersions)
		userService.setCurrentUser(previousUser)
		previousUser.setSessionCurrency(previousCurrency)
		previousUser.setSessionLanguage(previousLanguage)
		if(previousBaseSite != null)
			baseSiteService.setCurrentBaseSite(previousBaseSite, false)
	}

	def removeAllPromotions()
	{
		final SearchResult<PromotionSourceRuleModel> sr = getFlexibleSearchService().search(
				"SELECT {PK} FROM {PromotionSourceRule}")
		final Collection<PromotionSourceRuleModel> allPromotions = sr.getResult()
		if (CollectionUtils.isNotEmpty(allPromotions))
		{
			getModelService().removeAll(allPromotions)
		}
	}

	boolean switchConsumption(Boolean status)
	{
		return ruleSwitchService.set(Switch.CONSUMPTION, status)
	}

	// from legacy atdd keyword
	protected RuleEngineActionResult initializeRuleEngineWithRuleFromAndMaxAllowedRuns(final String rulesFileName,
			final int maxAllowedRuns) throws IOException
	{
		final String ruleContent = readRuleFile(rulesFileName)

		final Optional<DroolsKIEBaseModel> baseModel = getDroolsKieBase("promotions-base-junit")
		if (baseModel.isPresent())
		{
			final DroolsRuleModel rule = modelService.create(DroolsRuleModel.class)
			rule.setActive(Boolean.TRUE)
			rule.setRuleContent(ruleContent)
			rule.setCode(rulesFileName)
			rule.setMaxAllowedRuns(Integer.valueOf(maxAllowedRuns))
			rule.setRuleType(RuleType.PROMOTION)
			if (rulesFileName.contains(".drl"))
			{
				rule.setUuid(rulesFileName.substring(0, rulesFileName.length() - 4))
			}
			else
			{
				rule.setUuid(rulesFileName)
			}
			setGlobals(rule)
			rule.setKieBase(baseModel.get())
			getModelService().save(rule)
			final List<RuleEngineActionResult> results = getCommerceRuleEngineService()
					.initialize(Collections.singletonList(getTestRulesModule(singleton(rule))), false, false)
					.waitForInitializationToFinish().getResults()
			if (CollectionUtils.isNotEmpty(results))
			{
				final RuleEngineActionResult result = results.get(0)
				if (result.isActionFailed())
				{
					throw new IllegalStateException("error during rule initialization. Check your rules. \nDetailed error(s): "
					+ result.getMessagesAsString(MessageLevel.ERROR))
				}
				return result
			}
		}
		throw new IllegalStateException("KieBaseModel was not found, check the test setup")
	}

	// from legacy atdd keyword
	protected RuleEngineActionResult initializeRuleEngineWithRulesFrom(final String rulesFileNames) throws IOException
	{
		final String[] fileNames = rulesFileNames.split("\\s+")
		final List<String> ruleNameList = Arrays.stream(fileNames).collect(Collectors.toList())//NOSONAR
		final Map<String, String> ruleName2RuleContent = new HashMap<>()
		for (final String rulesFileName : ruleNameList)
		{
			ruleName2RuleContent.put(rulesFileName, readRuleFile(rulesFileName))
		}
		final RuleEngineActionResult result = initializeRuleEngineWithRules(ruleName2RuleContent)
		if (result.isActionFailed())
		{
			throw new IllegalStateException("error during rule initialization. Check your rules. \nDetailed error(s): "
			+ result.getMessagesAsString(MessageLevel.ERROR))
		}
		return result
	}

	// from legacy atdd keyword
	protected CartModel createCart(final String cartId, final String currency)
	{
		final CartModel result = modelService.create(CartModel.class)

		result.setCode(cartId)
		result.setCurrency(commonI18NService.getCurrency(currency != null ? currency : "USD"))
		result.setDate(new Date())
		result.setUser(userService.getAnonymousUser())
		result.setNet(Boolean.TRUE)
		modelService.save(result)

		return result
	}

	// from legacy atdd keyword
	protected CartModel createCart(final String cartId)
	{
		return createCart(cartId, null)
	}

	protected void addProductToCart(String productCode, int quantity, CartModel cart)
	{
		ProductModel product = getProductService().getProductForCode(productCode)
		final CartEntryModel entry = cartService.addNewEntry(cart, product, quantity, product.getUnit())
		entry.setTaxValues(new ArrayList<TaxValue>())

		for (final PriceRowModel pr : product.getEurope1Prices())
		{
			if (pr.getCurrency().equals(cart.getCurrency()))
			{
				entry.setBasePrice(pr.getPrice())
				entry.setTotalPrice(pr.getPrice())
			}
		}

		modelService.save(entry)
		modelService.save(cart)
		getCalculationService().calculate(cart)
	}

	// from legacy atdd keyword
	protected void addProductToCart(final ProductModel product, final String cartId)
	{

		final CartModel cart = getCartByCode(cartId)
		final CartEntryModel entry = cartService.addNewEntry(cart, product, 1, product.getUnit())
		entry.setTaxValues(new ArrayList<TaxValue>())

		for (final PriceRowModel pr : product.getEurope1Prices())
		{
			if (pr.getCurrency().equals(cart.getCurrency()))
			{
				entry.setBasePrice(pr.getPrice())
				entry.setTotalPrice(pr.getPrice())
			}
		}

		modelService.save(entry)
		modelService.save(cart)
	}

	// from legacy atdd keyword
	protected void removeProduct(final ProductModel product, final String cartId) throws CalculationException
	{
		final CartModel cart = getCartByCode(cartId)
		final Map<Integer, Long> quantityMap = new HashMap<Integer, Long>()
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().equals(product))
			{
				quantityMap.put(entry.getEntryNumber(), Long.valueOf(0L))
			}
		}
		cartService.updateQuantities(cart, quantityMap)

		calculationService.calculate(cart)
	}

	// from legacy atdd keyword
	protected void removeOneItemOfProduct(final ProductModel product, final String cartId) throws CalculationException
	{
		final CartModel cart = getCartByCode(cartId)
		final Map<Integer, Long> quantityMap = new HashMap<Integer, Long>()
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().equals(product))
			{
				quantityMap.put(entry.getEntryNumber(), Long.valueOf(entry.getQuantity().longValue() - 1))
			}
		}
		cartService.updateQuantities(cart, quantityMap)

		calculationService.calculate(cart)
	}

	// from legacy atdd keyword
	protected void updateCartQuantity(final int entryNo, final long quantity, final String cartId) throws CalculationException
	{
		final CartModel cart = getCartByCode(cartId)

		final Map<Integer, Long> quantityMap = new HashMap<Integer, Long>()
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getEntryNumber().intValue() == (entryNo))
			{
				quantityMap.put(entry.getEntryNumber(), Long.valueOf(quantity))
				break
			}
		}
		cartService.updateQuantities(cart, quantityMap)

		calculationService.calculate(cart)
	}

	// from legacy atdd keyword
	protected void updateCartEntryWithGroupNumber(final int entryNumber, final int entryGroupNumber, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)

		final CartEntryModel entry = cartService.getEntryForNumber(cart, entryNumber)
		entry.setEntryGroupNumbers(singleton(entryGroupNumber))

		getModelService().save(entry)
	}

	// from legacy atdd keyword
	protected void createEntryGroupForCart(final int groupNumber, final String groupType, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)

		final EntryGroup entryGroup = new EntryGroup()

		entryGroup.setGroupNumber(groupNumber)
		entryGroup.setGroupType(GroupType.valueOf(groupType))

		final List<EntryGroup> entryGroups = Lists.newArrayList(cart.getEntryGroups())
		entryGroups.add(entryGroup)
		cart.setEntryGroups(entryGroups)

		getModelService().save(cart)
	}

	// from legacy atdd keyword
	protected CartModel getCartByCode(final String code)
	{
		return (CartModel) extendedOrderDao.findOrderByCode(code)

	}

	// from legacy atdd keyword
	protected BigDecimal getTotalOfCart(final String cartId) throws CalculationException
	{

		final CartModel cart = getCartByCode(cartId)
		calculationService.calculate(cart)

		return BigDecimal.valueOf(cart.getTotalPrice().doubleValue())

	}

	// from legacy atdd keyword
	protected String readRuleFile(final String fileName) throws IOException
	{
		Path rulePath
		try
		{
			rulePath = Paths.get(Registry.getApplicationContext().getResource("classpath:/rules/" + fileName).getURI())
		}
		catch (final FileNotFoundException fnf)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(fnf.getMessage(), fnf)
			}
			rulePath = Paths.get(Registry.getApplicationContext()
					.getResource("classpath:../../promotionenginesamplesaddon/resources/rules/" + fileName + ".drl").getURI())
		}
		final InputStream is = Files.newInputStream(rulePath)
		final StringWriter writer = new StringWriter()
		IOUtils.copy(is, writer, Charset.forName("UTF-8"))
		return writer.toString()
	}

	// from legacy atdd keyword
	protected RuleEngineActionResult initializeRuleEngineWithRules(final Map<String, String> ruleName2media)
	{
		final Set<DroolsRuleModel> rules = new LinkedHashSet<>()
		final Optional<DroolsKIEBaseModel> baseModel = getDroolsKieBase("promotions-base-junit")
		if (baseModel.isPresent())
		{
			for (final Map.Entry<String, String> entry : ruleName2media.entrySet())
			{
				final DroolsRuleModel rule = modelService.create(DroolsRuleModel.class)
				rule.setActive(Boolean.TRUE)
				rule.setRuleContent(entry.getValue())
				rule.setCode(entry.getKey())
				rule.setRuleType(RuleType.PROMOTION)
				if (entry.getKey().contains(".drl"))
				{
					rule.setUuid(entry.getKey().substring(0, entry.getKey().length() - 4))
				}
				else
				{
					rule.setUuid(entry.getKey())
				}
				rule.setKieBase(baseModel.get())
				setGlobals(rule)
				rules.add(rule)
				modelService.save(rule)
			}
		}
		final List<RuleEngineActionResult> results = getCommerceRuleEngineService()
				.initialize(singletonList(getTestRulesModule(rules)), false, false).waitForInitializationToFinish().getResults()
		return CollectionUtils.isNotEmpty(results) ? results.get(0) : null
	}

	protected List<PromotionGroupModel> getTestPromoGroups()
	{
		final List<PromotionGroupModel> list = Lists.newArrayList()
		final PromotionGroupModel testPromo = promotionDao.findPromotionGroupByCode("testPromoGrp")
		final PromotionGroupModel electronicsPromoGrp = promotionDao.findPromotionGroupByCode("electronicsPromoGrp")
		list.add(testPromo)
		list.add(electronicsPromoGrp)
		return list
	}

	// from legacy atdd keyword
	// extracts the global definitions out of the drl and sets them at the rule
	protected void setGlobals(final DroolsRuleModel rule)
	{
		final String drl = rule.getRuleContent()
		final BufferedReader br = new BufferedReader(new StringReader(drl))
		final Map<String, String> globals = new HashMap<>()
		br.lines().filter({ it.startsWith("global") }).forEach( {
			final String[] split = it.split("\\s+")
			// 3rd string is the global bean
			if (split.length == 3)
			{
				final String string = split[2].substring(0, split[2].length() - 1)
				globals.put(string, string)
			}
		} )
		rule.setGlobals ( globals )
	}

	// from legacy atdd keyword
	protected DroolsKIEModuleModel getTestRulesModule(final Set<DroolsRuleModel> rules)
	{
		final AbstractRuleEngineContextModel abstractContext = getRuleEngineContextDao().findRuleEngineContextByName(
				"promotions-junit-context")
		checkState(abstractContext instanceof DroolsRuleEngineContextModel,
				"ruleengine context must be of type DroolsRuleEngineContextModel")

		final DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel) abstractContext

		final DroolsKIEBaseModel kieBase = context.getKieSession().getKieBase()
		kieBase.setRules(rules)
		getModelService().saveAll()
		return context.getKieSession().getKieBase().getKieModule()
	}

	// from legacy atdd keyword
	protected BigDecimal evaluatePromotionForCart(final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)

		final CurrencyModel currency = cart.getCurrency()
		Integer scale = null
		if (nonNull(currency))
		{
			scale = currency.getDigits()
		}

		((DefaultPromotionEngineService) getPromotionEngineService()).updatePromotions(getTestPromoGroups(), cart)
		BigDecimal promotionPrice = new BigDecimal(String.valueOf(cart.getTotalPrice().doubleValue()))
		logCartDetails(cart)

		if (nonNull(scale))
		{
			promotionPrice = promotionPrice.setScale(scale.intValue(), RoundingMode.HALF_UP)
		}
		return promotionPrice
	}

	// from legacy atdd keyword
	protected void logCartDetails(final CartModel cart)
	{
		LOG.info("Cart details:")
		LOG.info("Totals: {}", cart.getTotalPrice())
		LOG.info("Products:")

		cart.getEntries().forEach(
				{e ->
					LOG.info(" - code : {} , quantity: {}, base price: {}, adjusted total price: {}", e.getProduct().getCode(),
							e.getQuantity(), e.getBasePrice(), e.getTotalPrice())})

		if (isNotEmpty(cart.getGlobalDiscountValues()))
		{
			LOG.info("Global Discounts")
			cart.getGlobalDiscountValues().forEach(
					{d ->
						LOG.info(" - discount value: {}, applied value: {}, absolute: {}", d.getValue(), d.getAppliedValue(),
								d.isAbsolute())})
		}
		else
		{
			LOG.info("Global Discounts - no global discounts")
		}
		logAppliedPromotions(cart)
	}
	// from legacy atdd keyword
	protected void logAppliedPromotions(final CartModel cart)
	{
		if (isNotEmpty(cart.getAllPromotionResults()))
		{
			LOG.info("Calculation with applied promotions: ")
			for (final PromotionResultModel promotion : cart.getAllPromotionResults())
			{
				LOG.info(" - code: " + promotion.getPromotion().getCode())
			}
		}
		else
		{
			LOG.info("Calculation - none of the promotions has been applied ")
		}
	}
	// from legacy atdd keyword
	protected String getDeliveryModeForCart(final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)
		return cart.getDeliveryMode() != null ? cart.getDeliveryMode().getCode() : null
	}

	// from legacy atdd keyword
	protected void setDeliveryModeForCart(final String cartId, final String deliveryModeCode)
	{
		final List<DeliveryModeModel> deliveryModesByCode = deliveryModeDao.findDeliveryModesByCode(deliveryModeCode)
		Assert.assertEquals(1, deliveryModesByCode.size())
		final CartModel cart = getCartByCode(cartId)
		cart.setDeliveryMode(deliveryModesByCode.get(0))
	}
	// from legacy atdd keyword

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	protected AbstractRuleActionRAO evaluatePromotionForProduct(final ProductModel product)
	{
		final RuleEvaluationResult evaluatingResult = promotionEngineService.evaluate(product, getTestPromoGroups())
		final RuleEngineResultRAO result = evaluatingResult.getResult()
		final LinkedHashSet<AbstractRuleActionRAO> actions = result.getActions()
		for (final AbstractRuleActionRAO action : actions)
		{
			if (action.getAppliedToObject() instanceof ProductRAO
			&& product.getCode().equals(((ProductRAO) action.getAppliedToObject()).getCode()))
			{
				return action
			}
		}
		return null
	}

	// from legacy atdd keyword 'get product    ${productCode}'
	protected ProductModel getProduct(final String productCode)
	{
		return productService.getProductForCode(productCode)
	}

	// from legacy atdd keyword
	protected CategoryModel getCategory(final String categoryCode)
	{
		return categoryService.getCategoryForCode(categoryCode)
	}
	// from legacy atdd keyword
	protected BigDecimal getProductPrice(final ProductModel productModel)
	{
		return getProductPriceWithPromotion(productModel, null)
	}
	// from legacy atdd keyword
	protected BigDecimal getProductPriceWithPromotion(final ProductModel productModel, final DiscountRAO discount)
	{
		double discountAmount = 0.0
		if (discount != null && discount.getCurrencyIsoCode() == null)
		{
			discountAmount = discount.getValue().doubleValue()
		}

		return BigDecimal.valueOf(getWebPriceForProduct(productModel).getPriceValue().getValue() * (100.0D - discountAmount) / 100.0D)

	}

	protected boolean checkProductQuantity(final String productCode, final CartModel cart, final int quantity)
	{
		ProductModel product = getProductService().getProductForCode(productCode)
		long cartQuantity = 0
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().equals(product))
			{
				cartQuantity += entry.getQuantity().longValue()
			}
		}

		return quantity == cartQuantity

	}
	// from legacy atdd keyword
	protected void checkProductQuantity(final ProductModel product, final String cartId, final int quantity)
	{
		final CartModel cart = getCartByCode(cartId)
		long cartQuantity = 0
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().equals(product))
			{
				cartQuantity += entry.getQuantity().longValue()
			}
		}

		Assert.assertEquals(quantity, cartQuantity)

	}
	// from legacy atdd keyword
	protected void checkThatPriceLessThanFor(final BigDecimal beforePromoPrice, final BigDecimal afterPromoPrice, final String expectedDifferenceValue)
	{
		if (expectedDifferenceValue.contains("\""))
		{
			throw new RuntimeException(String.format("Get rid of invalid quote symbols in %s", expectedDifferenceValue))
		}
		if (expectedDifferenceValue.contains("%"))
		{
			final int percentageDifference = Integer.parseInt(expectedDifferenceValue.replace("%", ""))
			Assert.assertTrue(
					String.format("Before promotion price was %f, after promotions is applied price is %f but expected discount percentage value %s %", beforePromoPrice, afterPromoPrice,
					expectedDifferenceValue),
					beforePromoPrice
					.multiply(
					BigDecimal.valueOf(percentageDifference).divide(BigDecimal.valueOf(100.0), 2,
					BigDecimal.ROUND_UNNECESSARY)).subtract(afterPromoPrice).compareTo(BigDecimal.ZERO) == 0)
		}
		else
		{
			Assert.assertTrue(String.format("Before promotion price was %f, after promotions is applied price is %f but expected discount value %s", beforePromoPrice, afterPromoPrice,
					expectedDifferenceValue),
					beforePromoPrice.subtract(afterPromoPrice).subtract(new BigDecimal(expectedDifferenceValue)).compareTo(BigDecimal.ZERO) == 0)
		}
	}
	// from legacy atdd keyword
	protected void checkThatDeliveryModeNotDefined(final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)
		Assert.assertTrue("Delivery mode of cart with id '" + cartId + "' but was " + cart.getDeliveryMode(),
				cart.getDeliveryMode() == null)
	}
	// from legacy atdd keyword
	protected void setCartToUser(final String userUID, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId)
		final UserModel user = getUserService().getUserForUID(userUID)
		cart.setUser(user)
		getModelService().save(cart)
		// avoids that the cart is calculated for the wrong user
		getModelService().refresh(cart)
		JaloSession.getCurrentSession().getSessionContext().setUser((User) getModelService().getSource(user))
	}
	// from legacy atdd keyword
	protected PriceInformation getWebPriceForProduct(final ProductModel product)
	{
		validateParameterNotNull(product, "Product model cannot be null")
		final List<PriceInformation> prices = priceService.getPriceInformationsForProduct(product)
		if (CollectionUtils.isNotEmpty(prices))
		{
			PriceInformation minPriceForLowestQuantity = null
			for (final PriceInformation price : prices)
			{
				if (minPriceForLowestQuantity == null
				|| (((Long) minPriceForLowestQuantity.getQualifierValue("minqtd")).longValue() > ((Long) price
				.getQualifierValue("minqtd")).longValue()))
				{
					minPriceForLowestQuantity = price
				}
			}
			return minPriceForLowestQuantity
		}
		return null
	}
	// from legacy atdd keyword
	protected void compileRuleIncrementally(final String ruleCode, final String moduleName)
	{
		compileAndDeployRule(ruleCode, moduleName, true)
	}
	// from legacy atdd keyword
	protected void compileSourceRule(final String ruleCode, final String moduleName)
	{
		compileAndDeployRule(ruleCode, moduleName, false)
	}
	// from legacy atdd keyword
	protected void compileAndDeployRule(final String ruleCode, final String moduleName, final boolean enableIncrementalUpdate)
	{
		final AbstractRuleModel rule = getRuleService().getRuleForCode(ruleCode)
		getRuleCompilerService().compile(rule, moduleName)

		final DroolsRuleModel droolsRule = (DroolsRuleModel) getEngineRuleDao().getRuleByCode(ruleCode, moduleName)

		setGlobals(droolsRule)
		final InitializationFuture initializationFuture = getCommerceRuleEngineService().initialize(
				singletonList(getTestRulesModule(singleton(droolsRule))), false, enableIncrementalUpdate)
				.waitForInitializationToFinish()
		logModuleInitialization(initializationFuture)
	}
	// from legacy atdd keyword
	protected void compileSourceRules(final Collection<String> ruleCodes, final String moduleName)
	{
		if (ruleCodes.size() > 0)
		{
			final Set<DroolsRuleModel> droolsRules = Sets.newHashSet()
			for (final String ruleCode : ruleCodes)
			{
				final AbstractRuleModel rule = getRuleService().getRuleForCode(ruleCode)
				getRuleCompilerService().compile(rule, moduleName)

				final DroolsRuleModel droolsRule = (DroolsRuleModel) getEngineRuleDao().getRuleByCode(ruleCode, moduleName)
				setGlobals(droolsRule)
				droolsRules.add(droolsRule)
			}

			final InitializationFuture initializationFuture = getCommerceRuleEngineService().initialize(
					singletonList(getTestRulesModule(droolsRules)), false, false).waitForInitializationToFinish()
			logModuleInitialization(initializationFuture)
		}
	}
	// from legacy atdd keyword
	protected void compileSourceRules(final String ruleCodes, final String moduleName)
	{
		final String[] ruleCodeArray = ruleCodes.split("\\s+", 100)

		compileSourceRules(Arrays.asList(ruleCodeArray))
	}
	// from legacy atdd keyword
	protected void logModuleInitialization(final InitializationFuture initializationFuture)
	{
		if (isNotEmpty(initializationFuture.getResults()))
		{
			LOG.info("Rule engine initialized with following results: ")
			initializationFuture.getResults().forEach( {
				{r ->
					LOG.info(" - result, module name: {}, deployed maven version: {}, is failed: {}, features results: {} ",
							r.getModuleName(), r.getDeployedVersion(), r.isActionFailed(), isNotEmpty(r.getResults()) ? r.getResults()
							.stream().map(it.getMessage()).collect(Collectors.toList()) : " - ")} })
		}
	}
	// from legacy atdd keyword
	protected void checkOrderUsesCoupon(final String cartId, final String couponCode)
	{
		if (!orderUsesCoupon(cartId, couponCode))
		{
			Assert.fail("Coupon code " + couponCode + " is not used for cart " + cartId)
		}
	}
	// from legacy atdd keyword
	protected void checkOrderNotUsesCoupon(final String cartId, final String couponCode)
	{
		if (orderUsesCoupon(cartId, couponCode))
		{
			Assert.fail("Coupon code " + couponCode + " is used for cart " + cartId)
		}
	}
	// from legacy atdd keyword
	protected boolean orderUsesCoupon(final String cartId, final String couponCode)
	{
		final CartModel cart = getCartByCode(cartId)
		for (final AbstractPromotionActionModel action : getPromotionActionsForCart(cart))
		{
			if (action instanceof AbstractRuleBasedPromotionActionModel)
			{
				final AbstractRuleBasedPromotionActionModel ruleBasedPromotionAction = (AbstractRuleBasedPromotionActionModel) action

				return CollectionUtils.isNotEmpty(ruleBasedPromotionAction.getUsedCouponCodes()) &&
						ruleBasedPromotionAction.getUsedCouponCodes().contains(couponCode)
			}
		}
		return false
	}
	// from legacy atdd keyword
	protected BigDecimal getProductTotalFromCart(final String productId, final String cartId)
	{
		BigDecimal productTotal = BigDecimal.valueOf(0)
		final CartModel cart = getCartByCode(cartId)
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().getCode().equals(productId))
			{
				productTotal = productTotal.add(BigDecimal.valueOf(entry.getTotalPrice().doubleValue()))
			}
		}

		return productTotal
	}
	// from legacy atdd keyword
	protected void verifyPromotionActionByType(final String cartId, final String promotionActionType)
	{
		if (verifyCartHasNotPromotionActionType(cartId, promotionActionType))
		{
			Assert.fail("No '" + promotionActionType + "' found in the promotion result for the cart id " + cartId)
		}
	}
	// from legacy atdd keyword
	protected boolean verifyCartHasNotPromotionActionType(final String cartId, final String promotionActionType)
	{
		return getPromotionActionsForCart(getCartByCode(cartId)).stream().noneMatch(
				{action -> getModelService().getModelType(action).equals(promotionActionType)})
	}
	// from legacy atdd keyword
	protected List<AbstractPromotionActionModel> getPromotionActionsForCart(final CartModel cartModel)
	{

		final List<AbstractPromotionActionModel> promotionActions = new ArrayList<>()
		if (CollectionUtils.isNotEmpty(cartModel.getAllPromotionResults()))
		{
			cartModel.getAllPromotionResults().forEach({p -> promotionActions.addAll(p.getAllPromotionActions())})
		}
		return promotionActions
	}
	// from legacy atdd keyword
	protected Optional<DroolsKIEBaseModel> getDroolsKieBase(final String baseName)
	{
		final List<DroolsKIEBaseModel> allKIEBases = getDroolsKIEBaseDao().findAllKIEBases()
		return allKIEBases.stream().filter({b -> b.getName().equals(baseName)}).findFirst()
	}
	// from legacy atdd keyword
	protected AbstractRulesModuleModel getRulesModule(final String moduleName)
	{
		return getRulesModuleDao().findByName(moduleName)
	}
	// from legacy atdd keyword
	protected void setCurrentSite()
	{
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(BASESITE)
		sessionService.setAttribute("currentSite", baseSite)
	}

	CartFactory getCartFactory()
	{
		return cartFactory
	}

	PromotionsService getPromotionsService()
	{
		return promotionsService
	}

	CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService
	}

	protected ModelService getModelService()
	{
		return modelService
	}

	protected RuleEngineService getCommerceRuleEngineService()
	{
		return commerceRuleEngineService
	}

	protected PromotionEngineService getPromotionEngineService()
	{
		return promotionEngineService
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService
	}

	protected PriceService getPriceService()
	{
		return priceService
	}

	protected RuleEngineContextDao getRuleEngineContextDao()
	{
		return ruleEngineContextDao
	}

	protected RuleActionService getRuleActionService()
	{
		return ruleActionService
	}

	protected MediaService getMediaService()
	{
		return mediaService
	}

	protected UserService getUserService()
	{
		return userService
	}

	protected RuleService getRuleService()
	{
		return ruleService
	}

	protected RuleCompilerService getRuleCompilerService()
	{
		return ruleCompilerService
	}

	protected EngineRuleDao getEngineRuleDao()
	{
		return engineRuleDao
	}

	protected DroolsKIEBaseDao getDroolsKIEBaseDao()
	{
		return droolsKIEBaseDao
	}

	protected RulesModuleDao getRulesModuleDao()
	{
		return rulesModuleDao
	}

	protected CartService getCartService()
	{
		return cartService
	}

	protected CalculationService getCalculationService()
	{
		return calculationService
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService
	}

	protected ExtendedOrderDao getOrderDao()
	{
		return extendedOrderDao
	}

	protected ProductService getProductService()
	{
		return productService
	}

	protected CategoryService getCategoryService()
	{
		return categoryService
	}

	protected PromotionDao getPromotionDao()
	{
		return promotionDao
	}

	protected DeliveryModeDao getDeliveryModeDao()
	{
		return deliveryModeDao
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService
	}

	protected SessionService getSessionService()
	{
		return sessionService
	}
}
