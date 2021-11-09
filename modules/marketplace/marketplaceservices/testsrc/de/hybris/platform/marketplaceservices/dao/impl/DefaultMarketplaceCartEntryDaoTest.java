/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dao.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultMarketplaceCartEntryDaoTest extends ServicelayerTransactionalTest
{
	public static final String CART_CODE = "abrodeCart";
	public static final String UNSALEABLE_PRODUCT_CODE = "HW1210-3423";

	@Resource(name = "defaultMarketplaceCartEntryDao")
	private DefaultMarketplaceCartEntryDao defaultMarketplaceCartEntryDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	private CartModel cart;
	@Before
	public void prepare() throws ImpExException
	{
		importCsv("/marketplaceservices/test/testCart.csv", "utf-8");

		final CartModel example = new CartModel();
		example.setCode(CART_CODE);
		cart = flexibleSearchService.getModelByExample(example);
	}

	@Test
	public void testFindUnSaleableCartEntries()
	{
		final List<CartEntryModel> cartEntries = defaultMarketplaceCartEntryDao.findUnSaleableCartEntries(cart);
		assertEquals(cartEntries.size(), 1);
		assertEquals(cartEntries.get(0).getProduct().getCode(), UNSALEABLE_PRODUCT_CODE);
	}
}
