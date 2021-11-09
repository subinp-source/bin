/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateocc.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.ConfigurationInfoListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.ConfigurationInfoWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.textfieldconfiguratortemplatefacades.TextFieldFacade;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfiguratorTextfieldTemplateControllerTest
{
	private static final String PRODUCT_CODE = "Product";
	private static final String BASE_SITE = "Site";
	private static final long QUANTITY = 4;
	private static final int ENTRY_NUMBER = 3;
	@Mock
	private TextFieldFacade textFieldFacade;
	@Mock
	private ProductFacade productFacade;
	@Mock
	private DataMapper dataMapper;
	@Mock
	private CartFacade cartFacade;

	@InjectMocks
	ProductConfiguratorTextfieldTemplateController classUnderTest;



	private final String fields = "DEFAULT";

	private final OrderEntryWsDTO entryWs = new OrderEntryWsDTO();
	private final ProductWsDTO productWs = new ProductWsDTO();
	private final CartModificationWsDTO cartModificationWs = new CartModificationWsDTO();
	private final ConfigurationInfoWsDTO configurationInfoWs = new ConfigurationInfoWsDTO();
	private final ConfigurationInfoListWsDTO configurationInfoListWs = new ConfigurationInfoListWsDTO();

	private final OrderEntryData entry = new OrderEntryData();
	private final CartModificationData cartModification = new CartModificationData();
	private final List<ConfigurationInfoData> configurationInfos = new ArrayList<ConfigurationInfoData>();
	private final ConfigurationInfoData configurationInfo = new ConfigurationInfoData();
	private final CartData sessionCart = new CartData();




	@Before
	public void initialize() throws CommerceCartModificationException
	{
		MockitoAnnotations.initMocks(this);
		when(productFacade.getConfiguratorSettingsForCode(PRODUCT_CODE)).thenReturn(configurationInfos);
		when(cartFacade.addToCart(PRODUCT_CODE, QUANTITY)).thenReturn(cartModification);
		when(cartFacade.updateCartEntry(Mockito.any())).thenReturn(cartModification);
		when(cartFacade.getSessionCart()).thenReturn(sessionCart);
		when(dataMapper.map(cartModification, CartModificationWsDTO.class, fields)).thenReturn(cartModificationWs);
		when(dataMapper.map(configurationInfo, ConfigurationInfoWsDTO.class)).thenReturn(configurationInfoWs);
		when(textFieldFacade.getAbstractOrderEntry(ENTRY_NUMBER, sessionCart)).thenReturn(entry);
		configurationInfos.add(configurationInfo);
		entryWs.setProduct(productWs);
		entryWs.setQuantity(QUANTITY);
		entryWs.setConfigurationInfos(Collections.emptyList());
		configurationInfoWs.setConfiguratorType(ConfiguratorType.TEXTFIELD.getCode());
		configurationInfoWs.setStatus(ProductInfoStatus.SUCCESS.getCode());
		configurationInfoListWs.setConfigurationInfos(Arrays.asList(configurationInfoWs));
		productWs.setCode(PRODUCT_CODE);
		cartModification.setQuantityAdded(QUANTITY);
		cartModification.setEntry(entry);
		entry.setConfigurationInfos(configurationInfos);
	}

	@Test
	public void testTextFieldFacade()
	{
		assertEquals(textFieldFacade, classUnderTest.getTextFieldFacade());
	}

	@Test
	public void testGetConfigurationByProductCode()
	{
		final ConfigurationInfoListWsDTO configurationInfoListWsDTO = classUnderTest.getConfigurationByProductCode(PRODUCT_CODE);
		assertNotNull(configurationInfoListWsDTO);
		assertEquals(1, configurationInfoListWsDTO.getConfigurationInfos().size());
	}

	@Test
	public void testAddCartEntryNoAddedQuantity() throws CommerceCartModificationException
	{
		cartModification.setQuantityAdded(0);
		final CartModificationWsDTO cartModificationWsDTO = classUnderTest.addCartEntry(BASE_SITE, entryWs, fields);
		assertNotNull(cartModificationWsDTO);
		verify(cartFacade, Mockito.never()).updateCartEntry(entry);
	}

	@Test
	public void testAddCartEntry() throws CommerceCartModificationException
	{
		final CartModificationWsDTO cartModificationWsDTO = classUnderTest.addCartEntry(BASE_SITE, entryWs, fields);
		assertNotNull(cartModificationWsDTO);
		verify(cartFacade).updateCartEntry(entry);
	}

	@Test(expected = CommerceCartModificationException.class)
	public void testAddCartEntryNullCartModification() throws CommerceCartModificationException
	{
		when(cartFacade.addToCart(PRODUCT_CODE, QUANTITY)).thenReturn(null);
		classUnderTest.addCartEntry(BASE_SITE, entryWs, fields);
	}

	@Test
	public void testGetConfigurationInEntry() throws CommerceCartModificationException
	{
		final ConfigurationInfoListWsDTO configurationInEntry = classUnderTest.getConfigurationInEntry(ENTRY_NUMBER);
		assertNotNull(configurationInEntry);
		assertEquals(1, configurationInEntry.getConfigurationInfos().size());
	}

	@Test
	public void testUpdateConfigurationInEntry() throws CommerceCartModificationException
	{
		final CartModificationWsDTO cartModificationWsDTO = classUnderTest.getConfigurationInEntry(ENTRY_NUMBER,
				configurationInfoListWs);
		assertNotNull(cartModificationWsDTO);
		verify(cartFacade).updateCartEntry(entry);

	}
}

