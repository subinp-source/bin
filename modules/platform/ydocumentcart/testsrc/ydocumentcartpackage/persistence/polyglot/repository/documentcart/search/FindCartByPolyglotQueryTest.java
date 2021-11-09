/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.QueryOptions;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.persistence.flexiblesearch.polyglot.PolyglotPersistenceFlexibleSearchSupport;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.CartModelsCreator;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart.DatabaseCartStorage;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.BaseQuery;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.BaseQueryFactory;


@IntegrationTest
public class FindCartByPolyglotQueryTest extends ServicelayerBaseTest
{
	public static final String CART_DESCRIPTION = "cart description";
	public static final Double COST = 5d;
	private static final Logger LOGGER = LoggerFactory.getLogger(FindCartByPolyglotQueryTest.class);
	@Resource
	FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private BaseQueryFactory cartQueryFactory;
	private CartModelsCreator cartModelsCreator;

	private ProductModel product;
	private UnitModel unit;
	private CatalogVersionModel catalogVersion;

	@Before
	public void setUp()
	{
		cartModelsCreator = new CartModelsCreator(modelService, userService, commonI18NService);

		final CatalogModel catalog = cartModelsCreator.createCatalog();
		catalogVersion = cartModelsCreator.createCatalogVersion(catalog);
		product = cartModelsCreator.createProduct(catalogVersion);
		unit = cartModelsCreator.createUnit();

		modelService.saveAll();
	}

	public void findCartByDescription()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		cartModelsCreator.createCart("other description", COST);
		final CartModel cartModel3 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);

		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("description", CART_DESCRIPTION);

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance().search("GET {Cart} WHERE {description}=?description ",
				queryValues, Cart.class);

		//then
		assertThat(result.getResult()).extracting(Cart::getPK).hasSize(2).containsOnly(cartModel1.getPk(), cartModel3.getPk());
	}

	@Test
	public void shouldFindCartByDescription()
	{
		assumeFullTableScanEnabled();
		findCartByDescription();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingCartByDescriptionAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();


		final Throwable throwable = catchThrowable(this::findCartByDescription);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}


	public void findCarts()
	{
		//given
		cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		cartModelsCreator.createCart(CART_DESCRIPTION, COST);

		modelService.saveAll();

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance().search("GET {Cart}", Cart.class);

		//then
		assertThat(result.getResult()).hasSize(3);
	}

	@Test
	public void shouldFindCarts()
	{
		assumeFullTableScanEnabled();
		findCarts();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingCartsAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();

		final Throwable throwable = catchThrowable(this::findCarts);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}


	public void findCartsWithOrderBy()
	{
		//given
		cartModelsCreator.createCart("cc", COST);
		cartModelsCreator.createCart("aa", COST);
		cartModelsCreator.createCart("ddd", COST);

		modelService.saveAll();

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance().search("GET {Cart} Order by {description}", Cart.class);

		//then
		assertThat(result.getResult()).hasSize(3);
		assertThat(result.getResult().get(0).getDescription()).isEqualTo("aa");
		assertThat(result.getResult().get(1).getDescription()).isEqualTo("cc");
		assertThat(result.getResult().get(2).getDescription()).isEqualTo("ddd");
	}

	@Test
	public void shouldFindCartsWithOrderBy()
	{
		assumeFullTableScanEnabled();
		findCartsWithOrderBy();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingCartsWithOrderByAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();

		final Throwable throwable = catchThrowable(this::findCartsWithOrderBy);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}


	public void findCartByDescriptionOtherThan()
	{
		//given
		cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartModel cartModel2 = cartModelsCreator.createCart("other description", COST);
		cartModelsCreator.createCart(CART_DESCRIPTION, COST);

		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("description", CART_DESCRIPTION);

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance().search("GET {Cart} WHERE {description}<>?description ",
				queryValues, Cart.class);

		//then
		assertThat(result.getResult()).extracting(Cart::getPK).hasSize(1).containsOnly(cartModel2.getPk());
	}

	@Test
	public void shouldCartByDescriptionOtherThan()
	{
		assumeFullTableScanEnabled();
		findCartByDescriptionOtherThan();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingCartByDescriptionOtherThanAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();

		final Throwable throwable = catchThrowable(this::findCartByDescriptionOtherThan);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}

	private void assumeFullTableScanEnabled()
	{
		try
		{
			Assume.assumeFalse("full table scan should be enabled",
					Config.getBoolean(DatabaseCartStorage.PROPERTY_SHOULD_THROW_EX_ON_FULL_TBL_SCAN, false));
		}
		catch (final AssumptionViolatedException e)
		{
			LOGGER.info(e.getMessage());
			throw e;
		}
	}

	private void assumeFullTableScanDisabled()
	{
		try
		{
			Assume.assumeTrue("full table scan should be disabled",
					Config.getBoolean(DatabaseCartStorage.PROPERTY_SHOULD_THROW_EX_ON_FULL_TBL_SCAN, false));
		}
		catch (final AssumptionViolatedException e)
		{
			LOGGER.info(e.getMessage());
			throw e;
		}
	}

	@Test
	public void shouldFindCartEntryByItsQuantity()
	{
		assumeFullTableScanEnabled();

		findCartEntryByItsQuantity();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingForCartEntryByItsQuantityAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();

		final Throwable throwable = catchThrowable(this::findCartEntryByItsQuantity);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}

	private void findCartEntryByItsQuantity()
	{

		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 2L);
		final CartEntryModel cartEntryModel12 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 3L);
		final CartEntryModel cartEntryModel13 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 2L);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel21 = cartModelsCreator.createCartEntry(unit, product, cartModel2, 2L);
		final CartEntryModel cartEntryModel22 = cartModelsCreator.createCartEntry(unit, product, cartModel2, 1L);

		modelService.saveAll();

		final Map<String, Long> queryValues = new HashMap<>();
		queryValues.put("quantity", 2L);

		//when
		final SearchResult<CartEntry> result = FlexibleSearch.getInstance().search("GET {CartEntry} WHERE {quantity}=?quantity ",
				queryValues, CartEntry.class);

		//then
		assertThat(result.getResult()).extracting(CartEntry::getPK).hasSize(3).containsOnly(cartEntryModel11.getPk(),
				cartEntryModel13.getPk(),
				cartEntryModel21.getPk());
	}

	@Test
	public void shouldFindCartEntryByItsCart()
	{
		assumeFullTableScanEnabled();

		findCartEntryByItsCart();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingForCartEntryByItsCartAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();

		final Throwable throwable = catchThrowable(this::findCartEntryByItsCart);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}


	private void findCartEntryByItsCart()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel21 = cartModelsCreator.createCartEntry(unit, product, cartModel2, 1L);
		final CartModel cartModel3 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel31 = cartModelsCreator.createCartEntry(unit, product, cartModel3, 1L);

		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("order1", cartModel1.getPk());
		queryValues.put("order3", cartModel3.getPk());

		//when
		final SearchResult<CartEntry> result = FlexibleSearch.getInstance()
		                                                     .search("GET {CartEntry} WHERE {order}=?order1 OR {order}=?order3 ",
				                                                     queryValues, CartEntry.class);

		//then
		assertThat(result.getResult()).extracting(CartEntry::getPK).hasSize(2).containsOnly(cartEntryModel11.getPk(),
				cartEntryModel31.getPk());
	}

	@Test
	public void shouldFindCartEntryByItsProduct()
	{
		assumeFullTableScanEnabled();

		findCartEntryByItsProduct();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingForCartEntryByItsProductAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();

		final Throwable throwable = catchThrowable(this::findCartEntryByItsProduct);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}


	private void findCartEntryByItsProduct()
	{
		//given
		final ProductModel expectedProduct = modelService.create(ProductModel.class);
		expectedProduct.setCode("expectedProduct");
		expectedProduct.setCatalogVersion(catalogVersion);

		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		cartEntryModel11.setProduct(expectedProduct);
		final CartEntryModel cartEntryModel12 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartEntryModel cartEntryModel13 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		cartEntryModel13.setProduct(expectedProduct);

		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("product", expectedProduct.getPk());

		//when
		final SearchResult<CartEntry> result = FlexibleSearch.getInstance().search("GET {CartEntry} WHERE {product}=?product",
				queryValues, CartEntry.class);

		//then
		assertThat(result.getResult()).extracting(CartEntry::getPK).hasSize(2).containsOnly(cartEntryModel11.getPk(),
				cartEntryModel13.getPk());
	}

	@Test
	public void findCartEntryByItsPK()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartEntryModel cartEntryModel12 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("pk", cartEntryModel11.getPk());

		//when
		final SearchResult<CartEntry> result = FlexibleSearch.getInstance().search("GET {CartEntry} WHERE {pk}=?pk", queryValues,
				CartEntry.class);

		//then
		assertThat(result.getResult()).extracting(CartEntry::getPK).hasSize(1).containsOnly(cartEntryModel11.getPk());
	}

	@Test
	public void findCartByItsPK()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("pk", cartModel2.getPk());

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance()
		                                                .search("GET {Cart} WHERE {pk}=?pk", queryValues, Cart.class);

		//then
		assertThat(result.getResult()).extracting(Cart::getPK).hasSize(1).containsOnly(cartModel2.getPk());
	}

	@Test
	public void findAbstractOrderEntryByPKWhereParamIsString()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);

		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("cartEntryKey", cartEntryModel11.getPk().toString());

		//when
		final SearchResult<AbstractOrderEntry> result = FlexibleSearch.getInstance()
		                                                              .search("GET { AbstractOrderEntry } WHERE {pk} = ?cartEntryKey",
				                                                              queryValues,
				                                                              AbstractOrderEntry.class);

		//then
		assertThat(result.getResult()).extracting(AbstractOrderEntry::getPK).hasSize(1).containsOnly(cartEntryModel11.getPk());
	}

	@Test
	public void findCartByPKWhereParamIsString()
	{
		//given
		cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("pk", cartModel2.getPk().toString());

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance()
		                                                .search("GET {Cart} WHERE {pk}=?pk", queryValues, Cart.class);

		//then
		assertThat(result.getResult()).extracting(Cart::getPK).hasSize(1).containsOnly(cartModel2.getPk());
	}


	@Test
	public void findCartEntryByItsCartWhereParamIsString()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, COST);
		final CartEntryModel cartEntryModel21 = cartModelsCreator.createCartEntry(unit, product, cartModel2, 1L);

		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("order1", cartModel1.getPk().toString());

		//when
		final SearchResult<CartEntry> result = FlexibleSearch.getInstance()
		                                                     .search("GET {CartEntry} WHERE {order}=?order1",
				                                                     queryValues, CartEntry.class);

		//then
		assertThat(result.getResult()).extracting(CartEntry::getPK).hasSize(1).containsOnly(cartEntryModel11.getPk());
	}

	@Test
	public void findAbstractPromotionActionWhereParamIsString()
	{
		assumeExtensionIsAdded(Set.of("promotions"));
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

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("key", promotionResultModel.getPk().toString());

		//when
		final de.hybris.platform.servicelayer.search.SearchResult<ItemModel> result = flexibleSearchService
				.search("GET {AbstractPromotionAction} WHERE {promotionResult}=?key ORDER BY {creationtime} ASC", queryValues);
		//then
		assertThat(result.getResult()).hasSize(1);
	}

	@Test
	public void shouldQueryRootDocumentWhenCartEntryOrderAttributeIsString()
	{
		final Criteria criteria = convertToCriteria("GET { CartEntry } WHERE {order} = ?pkParam",
				Map.of("pkParam", "1"));

		final BaseQuery query = cartQueryFactory.getQuery(criteria);

		assertThat(query.getRootId()).isPresent();
	}

	@Test
	public void shouldQueryByEntityIdWhenCartEntryPkAttributeIsString()
	{
		final Criteria criteria = convertToCriteria("GET { CartEntry } WHERE {pk} = ?pkParam",
				Map.of("pkParam", "1"));

		final BaseQuery query = cartQueryFactory.getQuery(criteria);

		assertThat(query.getEntityId()).isPresent();
	}


	@Test
	public void shouldQueryRootDocumentWhenCartPkIsString()
	{
		final Criteria criteria = convertToCriteria("GET { Cart } WHERE {pk} = ?pkParam",
				Map.of("pkParam", "1"));

		final BaseQuery query = cartQueryFactory.getQuery(criteria);

		assertThat(query.getRootId()).isPresent();
	}

	@Test
	public void shouldQueryByEntityIdWhenPromotionResultAttributeIsString()
	{
		assumeExtensionIsAdded(Set.of("promotions"));
		final Criteria criteria = convertToCriteria("GET {AbstractPromotionAction} WHERE {promotionResult}=?pkParam",
				Map.of("pkParam", "1"));

		final BaseQuery query = cartQueryFactory.getQuery(criteria);

		assertThat(query.getEntityId()).isPresent();
	}

	@Test
	public void shouldQueryByEntityConditionWhenGuidAttributeIsString()
	{
		assumeExtensionIsAdded(Set.of("promotions"));
		final Criteria criteria = convertToCriteria("GET {AbstractPromotionAction} WHERE {guid}=?stringParam",
				Map.of("stringParam", "1"));

		final BaseQuery query = cartQueryFactory.getQuery(criteria);

		assertThat(query.getEntityCondition()).isPresent();
	}

	@Test
	public void shouldQueryByEntityIdForPromotionOrderEntryConsumedWhenPromotionResultAttributeIsString()
	{
		assumeExtensionIsAdded(Set.of("promotions"));
		final Criteria criteria = convertToCriteria("GET {PromotionOrderEntryConsumed} WHERE {promotionResult}=?pkParam",
				Map.of("pkParam", "1"));

		final BaseQuery query = cartQueryFactory.getQuery(criteria);

		assertThat(query.getEntityCondition()).isPresent();
	}

	private void assumeExtensionIsAdded(final Set<String> requiredExtensions)
	{

		final Set<String> installedExtensions = new HashSet<>();
		installedExtensions.addAll(Utilities.getInstalledExtensionNames(Registry.getMasterTenant()));
		installedExtensions.addAll(Utilities.getInstalledWebModules().keySet());

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
	}

	private Criteria convertToCriteria(final String query, final Map<String, Object> params)
	{
		final QueryOptions queryOptions = QueryOptions.newBuilder().withQuery(query).withValues(params).build();
		final Optional<Criteria> polyglotQueryCriteriaOptional = PolyglotPersistenceFlexibleSearchSupport.tryToConvertToPolyglotCriteria(
				queryOptions);
		return polyglotQueryCriteriaOptional.orElseThrow();
	}


}
