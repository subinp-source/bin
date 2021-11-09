/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.impex.jalo.ImpExException;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@IntegrationTest
public class ConfigCheckoutIntegrationTest extends CPQFacadeLayerTest
{

	@Resource(name = "extendedCartConverter")
	private AbstractPopulatingConverter<CartModel, CartData> extendedCartConverter;

	@Resource(name = "commerceCheckoutCartFacade")
	private CartFacade commerceCheckoutCartFacade;

	@Before
	public void setUp() throws Exception
	{
		prepareCPQData();

		//Enforce remove of B2BCartPopulator
		removeB2BCartPopulator(extendedCartConverter);
	}

	@Override
	public void importCPQTestData() throws ImpExException, Exception
	{
		super.importCPQTestData();
		importCPQUserData();
	}

	@Test
	public void testCheckoutForVariants() throws Exception
	{
		importCsv("/sapproductconfigservices/test/sapProductConfig_variants_testData.impex", "utf-8");
		// in case sapproductconfigintegration is present we need an additional impex file
		importCsvIfExist("/sapproductconfigintegration/test/sapProductConfig_SAPVariants_testData.impex", "utf-8");

		// click directly add to cart for a product variant
		final CartModificationData addToCart = cartFacade.addToCart(PRODUCT_CODE_CPQ_LAPTOP_MUZAC, 1);
		modelService.save(cartService.getSessionCart());

		//need to work with commerceCheckoutCartFacade as it gets a different populator list injected
		//compared to the standard cart facade (search extendedCartConverter in spring.xml)
		final List<OrderEntryData> cartEntries = commerceCheckoutCartFacade.getSessionCart().getEntries();

		assertEquals("One cart entry expected", 1, cartEntries.size());
		final OrderEntryData cartEntry = cartEntries.get(0);
		final List<ConfigurationInfoData> configurationInfos = cartEntry.getConfigurationInfos();
		assertNotNull(configurationInfos);
		assertEquals("We expect to see the cart summary derived from product features during checkout", 2,
				configurationInfos.size());
	}

}
