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
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.CartModelsCreator;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart.DatabaseCartStorage;


@IntegrationTest
public class FindCartUsingPolyglotOrderedQueryTest extends ServicelayerBaseTest
{

	private static final Logger LOGGER = LoggerFactory.getLogger(FindCartUsingPolyglotOrderedQueryTest.class);

	public static final String CART_DESCRIPTION = "cart description";
	public static final String CART_ENTRY_INFORMATION = "cart entry Information";

	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;

	private ProductModel product;
	private UnitModel unit;
	private CatalogVersionModel catalogVersion;

	private CartModelsCreator cartModelsCreator;

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

	public void findCartByDescriptionOrdered()
	{

		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, 1d);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, 10d);
		final CartModel cartModel3 = cartModelsCreator.createCart(CART_DESCRIPTION, 2d);
		final CartModel cartModel4 = cartModelsCreator.createCart(CART_DESCRIPTION, 9d);
		final CartModel cartModel5 = cartModelsCreator.createCart(CART_DESCRIPTION, 3d);
		final CartModel cartModel6 = cartModelsCreator.createCart(CART_DESCRIPTION, 8d);

		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("description", CART_DESCRIPTION);

		//when
		final SearchResult<Cart> result = FlexibleSearch.getInstance()
		                                                .search("GET {Cart} WHERE {description}=?description ORDER BY {paymentCost}",
				                                                queryValues, Cart.class);

		//then
		assertThat(result.getResult()).extracting(Cart::getPK).hasSize(6).containsExactly(cartModel1.getPk(), cartModel3.getPk(),
				cartModel5.getPk(), cartModel6.getPk(),
				cartModel4.getPk(), cartModel2.getPk());
	}

	@Test
	public void shouldCartByDescriptionOrdered()
	{
		assumeFullTableScanEnabled();
		findCartByDescriptionOrdered();
	}

	@Test
	public void shouldThrowExceptionWhenSearchingCartByDescriptionOrderedAndFullScanNotAllowed()
	{
		assumeFullTableScanDisabled();


		final Throwable throwable = catchThrowable(this::findCartByDescriptionOrdered);

		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
		                     .hasMessageMatching(
				                     "Searching by entity attributes '.*' is not supported because it requires to read full carts table.*");
	}

	@Test
	public void findCartEntryByCartOrdered()
	{
		//given
		final CartModel cartModel1 = cartModelsCreator.createCart(CART_DESCRIPTION, 1d);
		final CartEntryModel cartEntryModel11 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 1L);
		final CartEntryModel cartEntryModel12 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 4L);
		final CartEntryModel cartEntryModel13 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 2L);
		final CartEntryModel cartEntryModel14 = cartModelsCreator.createCartEntry(unit, product, cartModel1, 3L);
		final CartModel cartModel2 = cartModelsCreator.createCart(CART_DESCRIPTION, 1d);
		final CartEntryModel cartEntryModel21 = cartModelsCreator.createCartEntry(unit, product, cartModel2, 1L);

		modelService.saveAll();

		final Map<String, PK> queryValues = new HashMap<>();
		queryValues.put("order", cartModel1.getPk());

		//when
		final SearchResult<CartEntry> result = FlexibleSearch.getInstance()
		                                                     .search("GET {CartEntry} WHERE {order}=?order ORDER BY {quantity} DESC",
				                                                     queryValues, CartEntry.class);

		//then
		assertThat(result.getResult()).extracting(CartEntry::getPK).hasSize(4).containsExactly(cartEntryModel12.getPk(),
				cartEntryModel14.getPk(),
				cartEntryModel13.getPk(),
				cartEntryModel11.getPk());
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
}
