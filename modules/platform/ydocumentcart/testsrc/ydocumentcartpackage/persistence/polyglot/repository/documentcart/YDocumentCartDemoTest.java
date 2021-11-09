/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jdbcwrapper.TestJdbcLogUtils;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydocumentcartpackage.constants.YDocumentCartConstants;

/**
 * This test is intended to show that Document Cart with all references:
 * <p>
 * Cart,
 * CartEntry,
 * AbstractOrderEntryProductInfo[orderEntry],
 * PromotionResult[order],
 * AbstractPromotionAction[promotionResult],
 * PromotionOrderEntryConsumed[promotionResult]
 * <p>
 * works fine.
 * <p>
 * <p>
 * Extensions required to make it working are:
 * documentcart
 * adaptivesearchbackoffice
 * adaptivesearchsamplesaddon
 * adaptivesearchwebservices
 * solrfacetsearchbackoffice
 * solrserver
 * base-accelerator/yacceleratorinitialdata
 * b2c-accelerator/electronicsstore
 * yacceleratorstorefront
 * commercewebservices
 * permissionswebservices
 * smarteditaddon
 * cmssmartedit
 * yacceleratorfulfilmentprocess
 * pcmbackoffice
 * textfieldconfiguratortemplateservices
 */
@IntegrationTest
public class YDocumentCartDemoTest extends ServicelayerBaseTest
{
	private static final Logger LOGGER = LoggerFactory.getLogger(YDocumentCartDemoTest.class);
	private final TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(Constants.TC.Cart);
	private final ItemStateRepository repository = PolyglotPersistence.getRepository(typeInfo).requireSingleRepositoryOnly();
	@Resource
	FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private TypeService typeService;

	private ProductModel product;
	private UnitModel unit;

	private CartModelsCreator cartModelsCreator;

	private TestJdbcLogUtils testJdbcLogUtils;

	@Before
	public void setUp()
	{
		final Set<String> installedExtensions = new HashSet<>();
		installedExtensions.addAll(Utilities.getInstalledExtensionNames(Registry.getMasterTenant()));
		installedExtensions.addAll(Utilities.getInstalledWebModules().keySet());

		final Set<String> requiredExtensions = Set.of(YDocumentCartConstants.EXTENSIONNAME, "adaptivesearchbackoffice",
				"adaptivesearchsamplesaddon", "adaptivesearchwebservices", "solrfacetsearchbackoffice", "solrserver",
				"yacceleratorinitialdata", "electronicsstore", "yacceleratorstorefront", "ycommercewebservices",
				"permissionswebservices", "smarteditaddon", "cmssmartedit", "yacceleratorordermanagement", "pcmbackoffice",
				"textfieldconfiguratortemplateservices");
		try
		{

			Assume.assumeTrue("all required extensions should be added",
					CollectionUtils.removeAll(requiredExtensions, installedExtensions).isEmpty());
		}

		catch (final AssumptionViolatedException e)
		{
			LOGGER.info("expected {}, actual {}", requiredExtensions, installedExtensions, e);
			throw e;
		}


		cartModelsCreator = new CartModelsCreator(modelService, userService, commonI18NService);

		final CatalogModel catalog = cartModelsCreator.createCatalog();
		product = cartModelsCreator.createProduct(cartModelsCreator.createCatalogVersion(catalog));
		unit = cartModelsCreator.createUnit();

		modelService.saveAll();

		testJdbcLogUtils = new TestJdbcLogUtils(Registry.getCurrentTenant().getDataSource().getLogUtils(), typeService);
	}

	@After
	public void tearDown() throws Exception
	{
		if (testJdbcLogUtils != null)
		{
			testJdbcLogUtils.reset();
		}
	}

	@Test
	public void testDocumentCart()
	{
		final TestJdbcLogUtils.TestStatementListener jdbcLogListener = testJdbcLogUtils.addListener(
				Set.of(CartModel._TYPECODE, CartEntryModel._TYPECODE, "TextFieldConfiguredProductInfo", "PromotionResult",
						"PromotionNullAction", "PromotionOrderEntryConsumed"),
				Set.of(OrderEntryModel._TYPECODE, OrderModel._TYPECODE));

		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartEntryModel cartEntry2 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 4L);
		modelService.saveAll();

		final ConfiguratorType configuratorType = ConfiguratorType.valueOf("TEXTFIELD");

		final ItemModel abstractOrderEntryProductInfo1 = modelService.create("TextFieldConfiguredProductInfo");
		abstractOrderEntryProductInfo1.setProperty("orderEntry", cartEntry1);
		abstractOrderEntryProductInfo1.setProperty("configuratorType", configuratorType);
		modelService.saveAll();

		final ItemModel abstractOrderEntryProductInfo2 = modelService.create("TextFieldConfiguredProductInfo");
		abstractOrderEntryProductInfo2.setProperty("orderEntry", cartEntry1);
		abstractOrderEntryProductInfo2.setProperty("configuratorType", configuratorType);
		modelService.saveAll();

		final ItemModel promotionResultModel = modelService.create("PromotionResult");
		promotionResultModel.setProperty("order", cartModel1);
		modelService.saveAll();

		final ItemModel abstractPromotionActionModel = modelService.create("PromotionNullAction");
		abstractPromotionActionModel.setProperty("guid", UUID.randomUUID().toString());
		abstractPromotionActionModel.setProperty("promotionResult", promotionResultModel);
		modelService.saveAll();

		final ItemModel promotionOrderEntryConsumedModel = modelService.create("PromotionOrderEntryConsumed");
		promotionOrderEntryConsumedModel.setProperty("promotionResult", promotionResultModel);
		modelService.saveAll();

		assertThat(repository.get(PolyglotPersistence.identityFromLong(cartModel1.getPk().getLongValue()))).isNotNull();
		assertThat(repository.get(PolyglotPersistence.identityFromLong(cartEntry1.getPk().getLongValue()))).isNotNull();
		assertThat(repository.get(PolyglotPersistence.identityFromLong(cartEntry2.getPk().getLongValue()))).isNotNull();
		assertThat(cartModel1.getEntries()).hasSize(2);
		assertThat(repository.get(
				PolyglotPersistence.identityFromLong(abstractOrderEntryProductInfo1.getPk().getLongValue()))).isNotNull();
		assertThat(repository.get(
				PolyglotPersistence.identityFromLong(abstractOrderEntryProductInfo2.getPk().getLongValue()))).isNotNull();
		assertThat(repository.get(PolyglotPersistence.identityFromLong(promotionResultModel.getPk().getLongValue()))).isNotNull();
		assertThat(repository.get(
				PolyglotPersistence.identityFromLong(abstractPromotionActionModel.getPk().getLongValue()))).isNotNull();
		assertThat(repository.get(
				PolyglotPersistence.identityFromLong(promotionOrderEntryConsumedModel.getPk().getLongValue()))).isNotNull();

		assertThat(jdbcLogListener.getLoggedStatements()).isEmpty();
	}


	@Test
	public void findAbstractOrderEntryProductInfo()
	{
		final TestJdbcLogUtils.TestStatementListener jdbcLogListener = testJdbcLogUtils.addListener(
				Set.of(CartModel._TYPECODE, CartEntryModel._TYPECODE, "TextFieldConfiguredProductInfo", "PromotionResult",
						"PromotionNullAction", "PromotionOrderEntryConsumed"),
				Set.of(OrderEntryModel._TYPECODE, OrderModel._TYPECODE));

		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final CartEntryModel cartEntry1 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		modelService.saveAll();

		final ConfiguratorType configuratorType = ConfiguratorType.valueOf("TEXTFIELD");

		final ItemModel abstractOrderEntryProductInfo1 = modelService.create("TextFieldConfiguredProductInfo");
		abstractOrderEntryProductInfo1.setProperty("orderEntry", cartEntry1);
		abstractOrderEntryProductInfo1.setProperty("configuratorType", configuratorType);
		modelService.saveAll();

		final ItemModel abstractOrderEntryProductInfo2 = modelService.create("TextFieldConfiguredProductInfo");
		abstractOrderEntryProductInfo2.setProperty("orderEntry", cartEntry1);
		abstractOrderEntryProductInfo2.setProperty("configuratorType", configuratorType);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("key", cartEntry1.getPk());

		//when
		final SearchResult<AbstractOrderEntryProductInfoModel> searchResult = flexibleSearchService.search(
				"GET {AbstractOrderEntryProductInfo} WHERE {orderEntry}=?key ORDER BY {orderEntryPOS} DESC, {creationtime} ASC ",
				queryValues);
		//then
		assertThat(searchResult.getResult()).hasSize(2);
		assertThat(searchResult.getResult()).extracting(AbstractOrderEntryProductInfoModel::getPk)
		                                    .containsExactly(abstractOrderEntryProductInfo2.getPk(),
				                                    abstractOrderEntryProductInfo1.getPk());

		assertThat(jdbcLogListener.getLoggedStatements()).isEmpty();
	}


	@Test
	public void findAbstractPromotionAction()
	{
		final TestJdbcLogUtils.TestStatementListener jdbcLogListener = testJdbcLogUtils.addListener(
				Set.of(CartModel._TYPECODE, CartEntryModel._TYPECODE, "TextFieldConfiguredProductInfo", "PromotionResult",
						"PromotionNullAction", "PromotionOrderEntryConsumed"),
				Set.of(OrderEntryModel._TYPECODE, OrderModel._TYPECODE));

		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		modelService.saveAll();

		final ItemModel promotionResultModel = modelService.create("PromotionResult");
		promotionResultModel.setProperty("order", cartModel1);
		modelService.saveAll();

		final ItemModel abstractPromotionActionModel = modelService.create("PromotionNullAction");
		abstractPromotionActionModel.setProperty("guid", UUID.randomUUID().toString());
		abstractPromotionActionModel.setProperty("promotionResult", promotionResultModel);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("key", promotionResultModel.getPk());

		//when
		final SearchResult<ItemModel> result = flexibleSearchService
				.search("GET {AbstractPromotionAction} WHERE {promotionResult}=?key ORDER BY {creationtime} ASC", queryValues);
		//then
		assertThat(result.getResult()).hasSize(1);
		//one query because it is uncertain where parent promotionresult is stored
		assertThat(jdbcLogListener.getLoggedStatements()).hasSize(1);
	}

	@Test
	public void findPromotionResult()
	{
		final TestJdbcLogUtils.TestStatementListener jdbcLogListener = testJdbcLogUtils.addListener(
				Set.of(CartModel._TYPECODE, CartEntryModel._TYPECODE, "TextFieldConfiguredProductInfo", "PromotionResult",
						"PromotionNullAction", "PromotionOrderEntryConsumed"),
				Set.of(OrderEntryModel._TYPECODE, OrderModel._TYPECODE));

		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		modelService.saveAll();

		final ItemModel promotionResultModel = modelService.create("PromotionResult");
		promotionResultModel.setProperty("order", cartModel1);
		final float certainty = 0.4324321F;
		promotionResultModel.setProperty("certainty", certainty);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("key", cartModel1.getPk());

		//when
		final SearchResult<ItemModel> result = flexibleSearchService
				.search("GET {PromotionResult} WHERE {order}=?key ", queryValues);
		//then
		assertThat(result.getResult()).hasSize(1);
		assertThat(result.getResult().get(0).<Float>getProperty("certainty")).isEqualTo(certainty);
		assertThat(jdbcLogListener.getLoggedStatements()).isEmpty();
	}

	@Test
	public void findPromotionOrderEntryConsumed()
	{
		final TestJdbcLogUtils.TestStatementListener jdbcLogListener = testJdbcLogUtils.addListener(
				Set.of(CartModel._TYPECODE, CartEntryModel._TYPECODE, "TextFieldConfiguredProductInfo", "PromotionResult",
						"PromotionNullAction", "PromotionOrderEntryConsumed"),
				Set.of(OrderEntryModel._TYPECODE, OrderModel._TYPECODE));

		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		modelService.saveAll();

		final ItemModel promotionResultModel = modelService.create("PromotionResult");
		promotionResultModel.setProperty("order", cartModel1);
		final float certainty = 0.4324321F;
		promotionResultModel.setProperty("certainty", certainty);
		modelService.saveAll();


		final ItemModel promotionOrderEntryConsumed = modelService.create("PromotionOrderEntryConsumed");
		promotionOrderEntryConsumed.setProperty("promotionResult", promotionResultModel);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("promotionResult", promotionResultModel.getPk());


		//when
		final String query = "GET {PromotionOrderEntryConsumed} WHERE  {promotionResult} = ?promotionResult";
		final SearchResult<ItemModel> result = flexibleSearchService
				.search(query, queryValues);

		//then
		assertThat(result.getResult()).hasSize(1);
		assertThat(result.getResult().get(0).getPk()).isEqualTo(promotionOrderEntryConsumed.getPk());
		//one query because it is uncertain where parent promotionResult is stored
		assertThat(jdbcLogListener.getLoggedStatements()).hasSize(1);
	}

	@Test
	public void findCartByUserAndSite()
	{
		final TestJdbcLogUtils.TestStatementListener jdbcLogListener = testJdbcLogUtils.addListener(
				Set.of(CartModel._TYPECODE, CartEntryModel._TYPECODE, "TextFieldConfiguredProductInfo", "PromotionResult",
						"PromotionNullAction", "PromotionOrderEntryConsumed"),
				Set.of(OrderEntryModel._TYPECODE, OrderModel._TYPECODE));

		//given
		final CartModel cartModel = cartModelsCreator.createCart(CartModel.DESCRIPTION, 1d);
		final ItemModel baseSite = modelService.create("BaseSite");
		baseSite.setProperty("uid", UUID.randomUUID().toString());
		cartModel.setProperty("site", baseSite);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("user", cartModel.getUser().getPk());
		queryValues.put("site", baseSite.getPk());

		//when
		final String query = "GET {Cart} WHERE {user} = ?user AND {site} = ?site AND {saveTime} IS NULL ORDER BY {modifiedtime} DESC";
		final SearchResult<ItemModel> result = flexibleSearchService.search(query, queryValues);

		//then
		assertThat(result.getResult()).hasSize(1);
		assertThat(result.getResult().get(0).getPk()).isEqualTo(cartModel.getPk());
		assertThat(jdbcLogListener.getLoggedStatements()).isEmpty();
	}
}
