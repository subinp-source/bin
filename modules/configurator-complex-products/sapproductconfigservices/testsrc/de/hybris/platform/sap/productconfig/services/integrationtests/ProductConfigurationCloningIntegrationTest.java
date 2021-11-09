/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.integrationtests;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@IntegrationTest
public class ProductConfigurationCloningIntegrationTest extends CPQServiceLayerTest
{
	@Resource
	private CartService cartService;
	@Resource
	private OrderService orderService;
	@Resource
	private ProductService productService;
	@Resource
	private ModelService modelService;

	private ProductModel product0;
	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		product0 = productService.getProductForCode("testProduct0");
		product1 = productService.getProductForCode("testProduct1");
		product2 = productService.getProductForCode("testProduct2");
		product3 = productService.getProductForCode("testProduct3");
	}

	// Add 3 entries, remove middle entry, add new entry at position of 3rd entry
	// Added:
	// 0 -- 1 -- 2
	//Removed:
	// 0 -- X -- 2
	//Add to position 2 - possible since we are still in carts domain
	// 0 -- X -- (2) -- 3

	// Cloning to order needs to work regardless

	@Test
	public void testReplaceRemovedEntry() throws Exception
	{
		final CartModel cart = cartService.getSessionCart();

		final CartEntryModel cartEntry0 = cartService.addNewEntry(cart, product0, 1, null, -1, false);
		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry1 = cartService.addNewEntry(cart, product1, 1, null, -1, false);
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		cartService.saveOrder(cart);

		final CartEntryModel cartEntry2 = cartService.addNewEntry(cart, product2, 1, null, -1, false);
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);

		assertEquals(Integer.valueOf(0), cartEntry0.getEntryNumber());
		assertEquals(Integer.valueOf(1), cartEntry1.getEntryNumber());
		assertEquals(Integer.valueOf(2), cartEntry2.getEntryNumber());

		// Remove the second entry
		modelService.remove(cartEntry1);
		modelService.refresh(cart);

		// Add a new entry at the same position (2) of the third entry - which should be moved out of the way

		final CartEntryModel cartEntry2b = cartService.addNewEntry(cart, product3, 1, null, 2, false);

		assertEquals("New entry should be at position 2", Integer.valueOf(2), cartEntry2b.getEntryNumber());
		assertEquals("The third entry should be moved to position 3", Integer.valueOf(3), cartEntry2.getEntryNumber());
		cartService.saveOrder(cart);
		assertEquals("New entry should be at position 2", Integer.valueOf(2), cartEntry2b.getEntryNumber());
		assertEquals("The third entry should be moved to position 3", Integer.valueOf(3), cartEntry2.getEntryNumber());

		orderService.createOrderFromCart(cart);
	}
}
