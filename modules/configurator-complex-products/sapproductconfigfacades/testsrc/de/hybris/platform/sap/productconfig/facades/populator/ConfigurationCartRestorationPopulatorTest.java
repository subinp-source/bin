/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.populator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationOrderIntegrationService;
import de.hybris.platform.sap.productconfig.services.strategies.impl.ProductConfigurationCartRestorationStrategyImpl;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("javadoc")
public class ConfigurationCartRestorationPopulatorTest
{
	private static final String CART_ID = "cartId";

	private ConfigurationCartRestorationPopulator classUnderTest;

	@Mock
	private CPQConfigurableChecker cpqConfigurableChecker;

	@Mock
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;

	@Mock
	private ConfigurationOrderEntryProductInfoModelPopulator configInfoPopulator;

	@Mock
	private ProductConfigurationOrderIntegrationService configurationPricingOrderIntegrationService;

	@Mock
	private ModelService modelService;

	private ConfigModel configModel = new ConfigModelImpl();
	private AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
	private CommerceCartRestoration source = new CommerceCartRestoration();
	private CartRestorationData target = new CartRestorationData();
	private CommerceCartModification modification;
	private List<CommerceCartModification> modifications;


	@Before
	public void setup()
	{
		classUnderTest = new ConfigurationCartRestorationPopulator(configurationAbstractOrderIntegrationStrategy,
				cpqConfigurableChecker, configInfoPopulator, configurationPricingOrderIntegrationService, modelService);

		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(true);

		configModel = new ConfigModelImpl();
		modification = new CommerceCartModification();
		entry = new AbstractOrderEntryModel();
		modification.setEntry(entry);
		modification.setStatusCode(ProductConfigurationCartRestorationStrategyImpl.REFRESH_INLINE_CONFIGURATION);
		modifications = new ArrayList<>();
		modifications.add(modification);
		source = new CommerceCartRestoration();
		source.setModifications(modifications);
		target = new CartRestorationData();
	}

	@Test
	public void testIsConfigurableProduct()
	{
		assertTrue(classUnderTest.isConfigurableProduct(new ProductModel()));
	}

	@Test
	public void testAddConfigAttributesToCartEntry()
	{
		classUnderTest.addConfigAttributesToCartEntry(configModel, entry);
		verify(configInfoPopulator).populate(configModel, new ArrayList<AbstractOrderEntryProductInfoModel>());
		verify(configurationPricingOrderIntegrationService).fillSummaryMap(entry);
		verify(modelService).save(entry);
		assertNotNull(entry.getProductInfos());
	}

	@Test
	public void testPopulate()
	{
		given(configurationAbstractOrderIntegrationStrategy.getConfigurationForAbstractOrderEntry(entry)).willReturn(configModel);
		classUnderTest.populate(source, target);
		verify(configInfoPopulator).populate(configModel, new ArrayList<AbstractOrderEntryProductInfoModel>());
		verify(configurationPricingOrderIntegrationService).fillSummaryMap(entry);
		verify(modelService).save(entry);
		assertNotNull(entry.getProductInfos());
	}

	@Test
	public void testPopulateSourceisNull()
	{
		final CommerceCartRestoration source = null;
		classUnderTest.populate(source, target);
		verify(configInfoPopulator, times(0)).populate(Mockito.any(), Mockito.any());
		verify(configurationPricingOrderIntegrationService, times(0)).fillSummaryMap(Mockito.any());
		verify(modelService, times(0)).save(Mockito.any());
	}

	@Test
	public void testPopulateUpdateNotRequired()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(false);
		classUnderTest.populate(source, target);
		verify(configInfoPopulator, times(0)).populate(Mockito.any(), Mockito.any());
		verify(configurationPricingOrderIntegrationService, times(0)).fillSummaryMap(Mockito.any());
		verify(modelService, times(0)).save(Mockito.any());
	}

	@Test
	public void testUpdateRequired()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(true);
		assertTrue(classUnderTest.isUpdateRequired(ProductConfigurationCartRestorationStrategyImpl.REFRESH_INLINE_CONFIGURATION,
				new ProductModel()));
	}

	@Test
	public void testUpdateRequiredNotConfigurableProduct()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(false);
		assertFalse(classUnderTest.isUpdateRequired(ProductConfigurationCartRestorationStrategyImpl.REFRESH_INLINE_CONFIGURATION,
				new ProductModel()));
	}

	@Test
	public void testUpdateRequiredWrongStatus()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(true);
		assertFalse(classUnderTest.isUpdateRequired("WRONG_STATUS", new ProductModel()));
	}

	@Test
	public void testUpdateRequiredNotConfigurableProductWrongStatus()
	{
		given(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any(ProductModel.class))).willReturn(false);
		assertFalse(classUnderTest.isUpdateRequired("WRONG_STATUS", new ProductModel()));
	}

}
