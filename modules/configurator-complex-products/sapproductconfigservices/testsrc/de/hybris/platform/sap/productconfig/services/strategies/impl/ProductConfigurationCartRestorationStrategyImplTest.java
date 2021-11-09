/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationSavedCartCleanUpStrategy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductConfigurationCartRestorationStrategyImplTest
{

	@Mock
	private CommerceCartRestorationStrategy commerceCartRestorationStrategy;
	@Mock
	private ConfigurationSavedCartCleanUpStrategy cleanUpStrategy;

	@Mock
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;
	@Mock
	private CPQConfigurableChecker cpqConfigurableChecker;

	@InjectMocks
	private ProductConfigurationCartRestorationStrategyImpl classUnderTest;

	private final CommerceCartParameter parameters = new CommerceCartParameter();


	@Before
	public void setup()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(true);
	}

	@Test
	public void testRestoreCart() throws CommerceCartRestorationException
	{
		CommerceCartRestoration restoration = prepareRestoration();
		final CartModel cart = prepareCart();
		final AbstractOrderEntryModel entry = cart.getEntries().get(0);
		parameters.setCart(cart);
		given(commerceCartRestorationStrategy.restoreCart(parameters)).willReturn(restoration);
		restoration = classUnderTest.restoreCart(parameters);
		verify(cleanUpStrategy).cleanUpCart();
		verify(configurationAbstractOrderIntegrationStrategy).refreshCartEntryConfiguration(entry);
		assertEquals(1, restoration.getModifications().size());
	}

	@Test
	public void testRefreshConfigurations()
	{
		final CartModel cart = prepareCart();
		final AbstractOrderEntryModel entry = cart.getEntries().get(0);
		classUnderTest.refreshConfigurations(cart);
		verify(configurationAbstractOrderIntegrationStrategy).refreshCartEntryConfiguration(entry);
	}

	@Test
	public void testRefreshConfigurationsNoEntries()
	{
		final CartModel cart = new CartModel();
		classUnderTest.refreshConfigurations(cart);
		verify(configurationAbstractOrderIntegrationStrategy, times(0)).refreshCartEntryConfiguration(Mockito.any());
	}

	@Test
	public void testRefreshConfigurationsNotConfigurableProduct()
	{
		final CartModel cart = prepareCart();
		final AbstractOrderEntryModel entry = cart.getEntries().get(0);
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(false);
		classUnderTest.refreshConfigurations(cart);
		verify(configurationAbstractOrderIntegrationStrategy, times(0)).refreshCartEntryConfiguration(Mockito.any());
	}

	@Test
	public void testAddModificationsForConfigurableProducts()
	{
		final CommerceCartRestoration restoration = prepareRestoration();
		final CartModel cart = prepareCart();
		final AbstractOrderEntryModel entry = cart.getEntries().get(0);
		classUnderTest.addModificationsForConfigurableProducts(restoration, cart);
		final CommerceCartModification modification = restoration.getModifications().get(0);
		assertEquals(entry, modification.getEntry());
		assertEquals(ProductConfigurationCartRestorationStrategyImpl.REFRESH_INLINE_CONFIGURATION, modification.getStatusCode());
	}

	@Test
	public void testAddModificationsForConfigurableProductsNoEntries()
	{
		final CommerceCartRestoration restoration = prepareRestoration();
		final CartModel cart = new CartModel();
		classUnderTest.addModificationsForConfigurableProducts(restoration, cart);
		assertEquals(0, restoration.getModifications().size());
	}

	@Test
	public void testAddModificationsForConfigurableProductsNotConfigurableProduct()
	{
		final CommerceCartRestoration restoration = prepareRestoration();
		final CartModel cart = prepareCart();
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(false);
		classUnderTest.addModificationsForConfigurableProducts(restoration, cart);
		assertEquals(0, restoration.getModifications().size());
	}

	@Test
	public void testIsConfigurableProduct()
	{
		assertTrue(classUnderTest.isConfigurableProduct(new ProductModel()));
	}

	private CartModel prepareCart()
	{
		final CartModel cart = new CartModel();
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		final ProductModel product = new ProductModel();
		entry.setProduct(product);
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		entries.add(entry);
		cart.setEntries(entries);
		return cart;
	}

	protected CommerceCartRestoration prepareRestoration()
	{
		final CommerceCartRestoration restoration = new CommerceCartRestoration();
		final List<CommerceCartModification> modifications = new ArrayList<>();
		restoration.setModifications(modifications);
		return restoration;
	}
}
