package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Utilities;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IntegrationTest
public class CartRestorationTest extends ServicelayerBaseTest
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CartRestorationTest.class);
	private static final String RESTORED = "restored";
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private CartService cartService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void before()
	{
		final List<String> installedExtensionNames = Utilities.getInstalledExtensionNames(Registry.getSlaveJunitTenant());
		try
		{

			Assume.assumeTrue("basecommerce extension should be added", installedExtensionNames.contains("commerceservices"));
		}
		catch (final AssumptionViolatedException e)
		{
			LOGGER.info("basecommerce extension should be added", e);
			throw e;
		}
	}

	@Test
	public void shouldCloneCart()
	{
		final CartModel cart = createCart();
		final CartModel restoredCart = restoreCartForTest(cart);

		assertThat(restoredCart.<String>getProperty("guid")).isEqualTo(cart.getProperty("guid"));
		assertThat(restoredCart.getName()).isEqualTo(cart.getName() + RESTORED);
	}

	@Test
	public void shouldFindTwoCarts()
	{
		final CartModel cart = createCart();
		restoreCartForTest(cart);

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("guid", cart.getProperty("guid"));

		final SearchResult<CartModel> search = flexibleSearchService.search(
				"GET {Cart} WHERE {guid}=?guid", queryValues);

		assertThat(search.getCount()).isEqualTo(2);
	}


	@Test
	public void shouldFindCartoForGuidAndSite()
	{
		final CartModel cart = createCart();
		final ItemModel site = createSite();
		cart.setProperty("site", site);
		modelService.saveAll();

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("guid", cart.getProperty("guid"));
		queryValues.put("site", ((ItemModel) cart.getProperty("site")).getPk().toString());

		final SearchResult<CartModel> search = flexibleSearchService.search(
				"GET {Cart} WHERE {guid} = ?guid AND {site} = ?site", queryValues);

		assertThat(search.getCount()).isEqualTo(1);
	}

	@Test
	public void shouldFindCartoForUserAndSite()
	{
		final CartModel cart = createCart();
		final ItemModel site = createSite();
		cart.setProperty("site", site);
		modelService.saveAll();
		;

		final Map<String, String> queryValues = new HashMap<>();
		queryValues.put("user", ((ItemModel) cart.getProperty("user")).getPk().toString());
		queryValues.put("site", ((ItemModel) cart.getProperty("site")).getPk().toString());

		final SearchResult<CartModel> search = flexibleSearchService.search(
				"GET {Cart} WHERE {user} = ?user AND {site} = ?site", queryValues);

		assertThat(search.getCount()).isEqualTo(1);
	}

	private CartModel restoreCartForTest(final CartModel cart)
	{
		final CartModel clonedCart = cartService.clone(null, null, cart, null);
		clonedCart.setProperty("paymentTransactions", null);
		clonedCart.setCode(null);
		clonedCart.setProperty("guid", cart.getProperty("guid"));
		clonedCart.setName(cart.getName() + RESTORED);
		modelService.save(clonedCart);
		return clonedCart;
	}

	private CartModel createCart()
	{
		final CartModel cartModel = modelService.create(CartModel.class);
		cartModel.setUser(userService.getAdminUser());
		cartModel.setDate(new Date());
		cartModel.setCurrency(commonI18NService.getBaseCurrency());
		cartModel.setDescription(UUID.randomUUID().toString());
		cartModel.setProperty("saveTime", new Date());
		cartModel.setName(UUID.randomUUID().toString());
		cartModel.setProperty("savedBy", userService.getCurrentUser());
		cartModel.setProperty("guid", UUID.randomUUID().toString());
		modelService.saveAll();
		return cartModel;
	}

	private ItemModel createSite()
	{
		final ItemModel site = modelService.create("BaseSite");
		site.setProperty("uid", UUID.randomUUID().toString());
		site.setProperty("name", UUID.randomUUID().toString());
		modelService.saveAll();
		return site;
	}
}