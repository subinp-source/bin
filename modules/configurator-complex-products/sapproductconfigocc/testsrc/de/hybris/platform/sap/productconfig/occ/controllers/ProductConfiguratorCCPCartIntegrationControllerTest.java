/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.occ.ProductConfigOrderEntryWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfiguratorCCPCartIntegrationControllerTest
{
	private static final String PRODUCT_CODE = "product";
	private static final String BASE_SITE = "baseSiteId";

	@Mock
	private ConfigurationCartIntegrationFacade configCartFacade;
	@Mock
	private CartFacade cartFacade;
	@Mock
	private DataMapper dataMapper;

	@InjectMocks
	private ProductConfiguratorCCPCartIntegrationController classUnderTest;
	private final ProductData productData = new ProductData();
	private final CartData sessionCart = new CartData();
	private final int entryNumber = 3;
	private final List<OrderEntryData> entries = new ArrayList<>();
	private final OrderEntryData cartEntry = new OrderEntryData();
	private final String itemPK = "123";



	private String configId;


	private final CartModificationWsDTO cartModificationWsDTO = new CartModificationWsDTO();
	private final CartModificationData cartModification = new CartModificationData();


	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);
		sessionCart.setEntries(entries);
		entries.add(cartEntry);
		cartEntry.setEntryNumber(Long.valueOf(entryNumber).intValue());
		cartEntry.setItemPK(itemPK);
		cartModification.setEntry(cartEntry);
		Mockito.when(dataMapper.map(cartModification, CartModificationWsDTO.class)).thenReturn(cartModificationWsDTO);

	}

	@Test
	public void testGetKbKey()
	{
		productData.setCode(PRODUCT_CODE);
		final KBKeyData kbKey = classUnderTest.getKbKey(productData);
		assertNotNull(kbKey);
		assertEquals(PRODUCT_CODE, kbKey.getProductCode());
	}

	@Test(expected = CartEntryException.class)
	public void testGetCartEntryForNumberNoEntries()
	{
		entries.clear();
		classUnderTest.getCartEntryForNumber(sessionCart, entryNumber);
	}

	@Test(expected = CartEntryException.class)
	public void testGetCartEntryForNumberWrongNumber()
	{
		cartEntry.setEntryNumber(Integer.valueOf(10));
		classUnderTest.getCartEntryForNumber(sessionCart, entryNumber);
	}

	@Test
	public void testGetCartEntryForNumber()
	{
		final OrderEntryData orderEntryData = classUnderTest.getCartEntryForNumber(sessionCart, entryNumber);
		assertNotNull(orderEntryData);
		assertEquals(Integer.valueOf(Long.valueOf(entryNumber).intValue()), orderEntryData.getEntryNumber());
	}

	@Test
	public void testAddCartEntryInternal() throws CommerceCartModificationException
	{
		final ProductWsDTO productWs = getProductWsDTO("KD990SOL");

		final ProductConfigOrderEntryWsDTO orderEntry = getProductConfigOrderEntryWsDTO(productWs);

		final CartModificationData cartModification = new CartModificationData();

		Mockito.when(configCartFacade.addProductConfigurationToCart(orderEntry.getProduct().getCode(), orderEntry.getQuantity(),
				orderEntry.getConfigId())).thenReturn(cartModification);

		classUnderTest.addCartEntryInternal(orderEntry);
		Mockito.verify(configCartFacade).addProductConfigurationToCart(orderEntry.getProduct().getCode(), orderEntry.getQuantity(),
				orderEntry.getConfigId());

	}

	private ProductWsDTO getProductWsDTO(final String productCode)
	{
		final ProductWsDTO productWs = new ProductWsDTO();
		productWs.setCode(productCode);
		return productWs;
	}

	private ConfigurationData getConfigurationData(final String productCode)
	{
		final ConfigurationData config = new ConfigurationData();
		config.setConfigId(configId);
		return config;
	}

	private ProductConfigOrderEntryWsDTO getProductConfigOrderEntryWsDTO(final ProductWsDTO productWs)
	{
		final ProductConfigOrderEntryWsDTO orderEntry = new ProductConfigOrderEntryWsDTO();
		orderEntry.setConfigId(configId);
		orderEntry.setEntryNumber(3);
		orderEntry.setQuantity(1L);
		orderEntry.setProduct(productWs);
		return orderEntry;
	}


	@Test
	public void testUpdateConfiguration() throws CommerceCartModificationException
	{
		final ProductWsDTO productWs = getProductWsDTO(PRODUCT_CODE);
		final ProductConfigOrderEntryWsDTO orderEntry = getProductConfigOrderEntryWsDTO(productWs);
		Mockito.when(configCartFacade.updateProductConfigurationInCart(PRODUCT_CODE, configId)).thenReturn(cartModification);
		final CartModificationData cartAfterUpdate = classUnderTest.updateCartEntryInternal(orderEntry);

		assertNotNull(cartAfterUpdate.getEntry());
	}

	@Test
	public void testUpdateConfigurationAPI() throws CommerceCartModificationException
	{
		final ProductWsDTO productWs = getProductWsDTO(PRODUCT_CODE);
		final ProductConfigOrderEntryWsDTO orderEntry = getProductConfigOrderEntryWsDTO(productWs);
		Mockito.when(configCartFacade.updateProductConfigurationInCart(PRODUCT_CODE, configId)).thenReturn(cartModification);
		final CartModificationWsDTO cartAfterUpdateWs = classUnderTest.updateCartEntry(BASE_SITE, entryNumber, orderEntry);

		assertEquals(cartModificationWsDTO, cartAfterUpdateWs);
	}

	@Test
	public void testConfigureCartEntry() throws CommerceCartModificationException
	{

		final ProductWsDTO productWs = getProductWsDTO(PRODUCT_CODE);
		final ProductConfigOrderEntryWsDTO orderEntry = getProductConfigOrderEntryWsDTO(productWs);

		final ConfigurationData configuration = getConfigurationData(PRODUCT_CODE);
		Mockito.when(cartFacade.getSessionCart()).thenReturn(sessionCart);
		Mockito.when(configCartFacade.configureCartItem(itemPK)).thenReturn(configuration);


		final ConfigurationData configurationData = classUnderTest.configureCartEntryInternal(orderEntry.getEntryNumber());
		assertEquals(configId, configurationData.getConfigId());

	}
}
