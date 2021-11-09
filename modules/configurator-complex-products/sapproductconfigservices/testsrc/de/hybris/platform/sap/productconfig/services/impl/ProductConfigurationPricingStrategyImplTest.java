/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationEngineException;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceSummaryModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.sap.productconfig.services.intf.PricingService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationCopyStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
@UnitTest
public class ProductConfigurationPricingStrategyImplTest
{
	private static final String EXTERNAL_CONFIGURATION = "external configuration";

	private static final String PRODUCT_CODE = "product code";

	private static final BigDecimal CURRENT_TOTAL_PRICE_VALUE = BigDecimal.valueOf(132.85);
	private static final BigDecimal NEW_CURRENT_TOTAL_PRICE_VALUE = BigDecimal.valueOf(163.27);

	private static final String EUR = "EUR";

	private static final String CURRENCY_ISO = "currency iso";

	private ProductConfigurationPricingStrategyImpl cut;

	@Mock
	private PricingService pricingService;

	private static final String CONFIG_ID = "abc123";
	private static final long keyAsLong = 12;
	private static final String CONFIG_ID_NO_PRICE = "1";
	private static final String NEW_CONFIG_ID = "2";
	private final PK primaryKey = PK.fromLong(keyAsLong);

	@Mock
	private ProductConfigurationService configurationService;
	@Mock
	private ModelService modelService;
	@Mock
	private ConfigurationCopyStrategy configCopyStrategy;
	@Mock
	private CommonI18NService i18NService;

	@Mock
	private ConfigModel modelMock;


	@Mock
	private CartEntryModel cartEntry;

	@Mock
	private ProductModel productModel;

	@Mock
	private ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy;
	@Mock
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;

	@Mock
	private CommerceCartService commerceCartService;
	private CommerceCartParameter parameters;


	private final ConfigModel configModel = new ConfigModelImpl();
	private final ConfigModel newConfigModel = createNewConfigModel();

	private final InstanceModel instanceModel = new InstanceModelImpl();

	@Mock
	private CartModel cart;

	@Mock
	private CurrencyModel currentCurrency;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		cut = new ProductConfigurationPricingStrategyImpl();

		cut.setConfigurationService(configurationService);
		cut.setPricingService(pricingService);
		cut.setCommerceCartService(commerceCartService);
		cut.setModelService(modelService);
		cut.setConfigurationAbstractOrderEntryLinkStrategy(configurationAbstractOrderEntryLinkStrategy);
		cut.setConfigCopyStrategy(configCopyStrategy);
		cut.setI18NService(i18NService);
		cut.setConfigurationAbstractOrderIntegrationStrategy(configurationAbstractOrderIntegrationStrategy);

		when(configurationService.retrieveConfigurationModel(CONFIG_ID_NO_PRICE)).thenReturn(configModel);
		when(configurationService.retrieveConfigurationModel(NEW_CONFIG_ID)).thenReturn(newConfigModel);
		when(configurationService.retrieveExternalConfiguration(CONFIG_ID_NO_PRICE)).thenReturn(EXTERNAL_CONFIGURATION);
		when(configCopyStrategy.deepCopyConfiguration(CONFIG_ID_NO_PRICE, PRODUCT_CODE, EXTERNAL_CONFIGURATION, true))
				.thenReturn(NEW_CONFIG_ID);
		when(Boolean.valueOf(pricingService.isActive())).thenReturn(Boolean.FALSE);

		when(modelMock.getId()).thenReturn(CONFIG_ID);
		when(cartEntry.getPk()).thenReturn(primaryKey);
		when(cartEntry.getProduct()).thenReturn(productModel);
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);
		when(cartEntry.getOrder()).thenReturn(cart);
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(primaryKey.toString()))
				.thenReturn(CONFIG_ID_NO_PRICE);
		when(i18NService.getCurrentCurrency()).thenReturn(currentCurrency);
		when(currentCurrency.getIsocode()).thenReturn(EUR);

		configModel.setRootInstance(instanceModel);
		configModel.setId(CONFIG_ID_NO_PRICE);
		instanceModel.setSubInstances(Collections.EMPTY_LIST);

		parameters = new CommerceCartParameter();
		parameters.setConfigId(CONFIG_ID);
	}

	@Test
	public void testUpdateCartEntryBasePriceNoPrice()
	{
		when(configurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);
		when(modelMock.getCurrentTotalPrice()).thenReturn(PriceModel.NO_PRICE);

		final boolean entryUpdated = cut.updateCartEntryBasePrice(cartEntry);

		assertFalse("Entry should not be updated", entryUpdated);

	}

	@Test
	public void testUpdateCartEntryBasePriceNullPrice()
	{
		when(configurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);
		when(modelMock.getCurrentTotalPrice()).thenReturn(null);

		final boolean entryUpdated = cut.updateCartEntryBasePrice(cartEntry);

		assertFalse("Entry should not be updated", entryUpdated);

	}

	@Test
	public void testUpdateCartEntryBasePrice()
	{
		final ConfigModel cfgModel = createConfigModel();
		when(configurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(cfgModel);
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(any())).thenReturn(CONFIG_ID);

		final boolean entryUpdated = cut.updateCartEntryBasePrice(cartEntry);

		verify(cartEntry, times(1)).setBasePrice(eq(Double.valueOf(cfgModel.getCurrentTotalPrice().getPriceValue().doubleValue())));

		assertTrue("Entry should be updated", entryUpdated);

	}

	@Test
	public void testUpdateCartEntryBasePriceNoPriceChange()
	{
		final ConfigModel cfgModel = createConfigModel();
		when(configurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(cfgModel);
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(any())).thenReturn(CONFIG_ID);
		when(cartEntry.getBasePrice()).thenReturn(CURRENT_TOTAL_PRICE_VALUE.doubleValue());

		final boolean entryUpdated = cut.updateCartEntryBasePrice(cartEntry);

		verify(cartEntry, times(0)).setBasePrice(eq(Double.valueOf(cfgModel.getCurrentTotalPrice().getPriceValue().doubleValue())));

		assertFalse("Entry should not be updated", entryUpdated);

	}

	private ConfigModel createConfigModel()
	{
		final PriceModel currentTotalPrice = new PriceModelImpl();
		final ConfigModel configModel = new ConfigModelImpl();
		configModel.setId(CONFIG_ID);
		currentTotalPrice.setCurrency(EUR);
		currentTotalPrice.setPriceValue(CURRENT_TOTAL_PRICE_VALUE);
		configModel.setCurrentTotalPrice(currentTotalPrice);
		return configModel;
	}

	private ConfigModel createNewConfigModel()
	{
		final PriceModel currentTotalPrice = new PriceModelImpl();
		final ConfigModel configModel = new ConfigModelImpl();
		configModel.setId(NEW_CONFIG_ID);
		currentTotalPrice.setCurrency(EUR);
		currentTotalPrice.setPriceValue(NEW_CURRENT_TOTAL_PRICE_VALUE);
		configModel.setCurrentTotalPrice(currentTotalPrice);
		return configModel;
	}

	@Test
	public void testRetrieveCurrentTotalPriceSSC() throws ConfigurationEngineException
	{
		final ConfigModel cfgModel = createConfigModel();
		testRetrieveCurrentTotalPriceSSC(cfgModel.getCurrentTotalPrice());
	}

	private void testRetrieveCurrentTotalPriceSSC(final PriceModel price)
	{
		when(configurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);
		when(modelMock.getCurrentTotalPrice()).thenReturn(price);
		when(Boolean.valueOf(pricingService.isActive())).thenReturn(Boolean.FALSE);
		final PriceModel result = cut.retrieveCurrentTotalPrice(CONFIG_ID, cartEntry);
		assertNotNull(result);
		assertEquals(price, result);
		verify(pricingService, times(0)).getPriceSummary(CONFIG_ID);
		verify(configurationService).retrieveConfigurationModel(CONFIG_ID);
	}

	@Test
	public void testRetrieveCurrentTotalPriceSSCPriceNA() throws ConfigurationEngineException
	{
		testRetrieveCurrentTotalPriceSSC(PriceModel.PRICE_NA);
	}

	@Test
	public void testRetrieveCurrentTotalPriceSSCNoPrice() throws ConfigurationEngineException
	{
		testRetrieveCurrentTotalPriceSSC(PriceModel.NO_PRICE);
	}

	@Test
	public void testRetrieveCurrentTotalPriceSessionCurrencyChanged() throws ConfigurationEngineException
	{
		configModel.setCurrentTotalPrice(new PriceModelImpl());
		configModel.getCurrentTotalPrice().setCurrency("");
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(primaryKey.toString()))
				.thenReturn(CONFIG_ID_NO_PRICE, NEW_CONFIG_ID);
		when(Boolean.valueOf(pricingService.isActive())).thenReturn(Boolean.FALSE);
		final PriceModel result = cut.retrieveCurrentTotalPrice(CONFIG_ID_NO_PRICE, cartEntry);
		verify(configurationAbstractOrderEntryLinkStrategy, times(2)).getConfigIdForCartEntry(primaryKey.toString());
		verify(configurationAbstractOrderEntryLinkStrategy, times(1)).getDraftConfigIdForCartEntry(primaryKey.toString());
		assertNotNull(result);
		assertEquals(NEW_CURRENT_TOTAL_PRICE_VALUE.doubleValue(), result.getPriceValue().doubleValue(), 0.001);
	}

	@Test
	public void testRetrieveCurrentTotalPriceCPS()
	{
		final PriceSummaryModel priceSummary = new PriceSummaryModel();
		priceSummary.setCurrentTotalPrice(new PriceModelImpl());
		priceSummary.getCurrentTotalPrice().setCurrency(EUR);
		when(pricingService.getPriceSummary(CONFIG_ID)).thenReturn(priceSummary);
		when(Boolean.valueOf(pricingService.isActive())).thenReturn(Boolean.TRUE);
		final PriceModel result = cut.retrieveCurrentTotalPrice(CONFIG_ID, cartEntry);
		assertNotNull(result);
		verify(pricingService).getPriceSummary(CONFIG_ID);
		verify(configurationService, times(0)).retrieveConfigurationModel(CONFIG_ID);
	}

	@Test
	public void testRetrieveCurrentTotalPriceCPSNull()
	{
		when(pricingService.getPriceSummary(CONFIG_ID)).thenReturn(null);
		when(Boolean.valueOf(pricingService.isActive())).thenReturn(Boolean.TRUE);
		final PriceModel result = cut.retrieveCurrentTotalPrice(CONFIG_ID, cartEntry);
		assertNull(result);
		verify(pricingService).getPriceSummary(CONFIG_ID);
	}

	@Test
	public void testRetrieveCurrentTotalPriceCPSPriceNA()
	{
		final PriceSummaryModel priceSummary = new PriceSummaryModel();
		priceSummary.setCurrentTotalPrice(PriceModel.PRICE_NA);
		testRetrieveCurrentTotalPrice(priceSummary);
	}

	@Test
	public void testRetrieveCurrentTotalPriceCPSNoPrice()
	{
		final PriceSummaryModel priceSummary = new PriceSummaryModel();
		priceSummary.setCurrentTotalPrice(PriceModel.NO_PRICE);
		testRetrieveCurrentTotalPrice(priceSummary);
	}

	private void testRetrieveCurrentTotalPrice(final PriceSummaryModel priceSummary)
	{
		when(pricingService.getPriceSummary(CONFIG_ID)).thenReturn(priceSummary);
		when(Boolean.valueOf(pricingService.isActive())).thenReturn(Boolean.TRUE);
		final PriceModel result = cut.retrieveCurrentTotalPrice(CONFIG_ID, cartEntry);
		assertNotNull(result);
		assertEquals(priceSummary.getCurrentTotalPrice(), result);
		verify(pricingService).getPriceSummary(CONFIG_ID);
	}

	@Test
	public void testGetParametersForCartUpdate()
	{
		final CommerceCartParameter result = cut.getParametersForCartUpdate(cartEntry);
		assertEquals(cartEntry.getOrder(), result.getCart());
		assertEquals(CONFIG_ID_NO_PRICE, result.getConfigId());
		assertTrue(result.isEnableHooks());
	}

	@Test
	public void testhasBasePriceChanged()
	{
		final PriceModel price = new PriceModelImpl();
		price.setPriceValue(new BigDecimal(4));
		price.setCurrency(CURRENCY_ISO);
		final Double result = cut.hasBasePriceChanged(cartEntry, price);
		assertNotNull(result);
		assertEquals(Double.valueOf(4), result);
	}

	@Test
	public void testhasBasePriceChangedNot()
	{
		final PriceModel price = new PriceModelImpl();
		price.setCurrency(CURRENCY_ISO);
		price.setPriceValue(new BigDecimal(cartEntry.getBasePrice()));
		assertNull(cut.hasBasePriceChanged(cartEntry, price));
	}

	@Test
	public void testUpdateCartEntryPricesEntrySaved()
	{
		prepareModelsForUpdateCartEntryPrices();
		assertTrue(cut.updateCartEntryPrices(cartEntry, true, null));
		verify(commerceCartService).calculateCart(any(CommerceCartParameter.class));
		verify(modelService).save(cartEntry);
	}

	@Test
	public void testUpdateCartEntryPricesNoCalculate()
	{
		prepareModelsForUpdateCartEntryPrices();
		assertTrue(cut.updateCartEntryPrices(cartEntry, false, null));
		verify(commerceCartService, times(0)).calculateCart(any(CommerceCartParameter.class));
		verify(modelService).save(cartEntry);
		verify(modelService).save(cartEntry.getOrder());
	}

	@Test
	public void testUpdateCartEntryPricesCartSaved()
	{
		prepareModelsForUpdateCartEntryPrices();
		assertTrue(cut.updateCartEntryPrices(cartEntry, false, null));
		verify(modelService).save(cartEntry.getOrder());
	}


	@Test
	public void testUpdateCartEntryPrices_passedParameter()
	{
		prepareModelsForUpdateCartEntryPrices();
		final CommerceCartParameter passedParameter = new CommerceCartParameter();
		assertTrue(cut.updateCartEntryPrices(cartEntry, true, passedParameter));
		verify(modelService).save(cartEntry);
		verify(commerceCartService).calculateCart(passedParameter);
	}

	protected void prepareModelsForUpdateCartEntryPrices()
	{
		final PriceModel price = new PriceModelImpl();
		price.setPriceValue(new BigDecimal(2));
		price.setCurrency(EUR);
		configModel.setCurrentTotalPrice(price);
		configModel.setId("123");
	}

	@Test
	public void testUpdateCartEntryPricesNoUpdate()
	{
		configModel.setCurrentTotalPrice(null);
		assertFalse(cut.updateCartEntryPrices(cartEntry, true, null));
		verify(modelService, times(0)).save(cartEntry);
		verify(commerceCartService, times(0)).calculateCart(any(CommerceCartParameter.class));
	}

	@Test
	public void testIsPricingErrorPresent()
	{
		configModel.setPricingError(true);
		assertTrue(cut.isCartPricingErrorPresent(configModel));

		configModel.setPricingError(false);
		assertFalse(cut.isCartPricingErrorPresent(configModel));
	}

	@Test
	public void testCalculateBasePriceForConfiguration()
	{
		prepareModelsForUpdateCartEntryPrices();
		when(cart.getNet()).thenReturn(Boolean.TRUE);
		final PriceValue result = cut.calculateBasePriceForConfiguration(cartEntry);
		assertNotNull(result);
		assertEquals(EUR, result.getCurrencyIso());
		assertEquals(2, result.getValue(), 0.01);
		assertTrue(result.isNet());
	}

	@Test
	public void testCalculateBasePriceForConfigurationPriceNull()
	{
		testCalculateBasePriceForConfigurationNoPriceObtainable(null);
	}

	@Test
	public void testCalculateBasePriceForConfigurationNoPrice()
	{
		testCalculateBasePriceForConfigurationNoPriceObtainable(PriceModel.NO_PRICE);
	}

	@Test
	public void testCalculateBasePriceForConfigurationPriceNA()
	{
		testCalculateBasePriceForConfigurationNoPriceObtainable(PriceModel.PRICE_NA);
	}

	private void testCalculateBasePriceForConfigurationNoPriceObtainable(final PriceModel unobtainablePrice)
	{
		configModel.setCurrentTotalPrice(unobtainablePrice);
		when(currentCurrency.getIsocode()).thenReturn(CURRENCY_ISO);

		final PriceValue result = cut.calculateBasePriceForConfiguration(cartEntry);
		assertNotNull(result);
		assertEquals(CURRENCY_ISO, result.getCurrencyIso());
		assertEquals(0, result.getValue(), 0.01);
		assertFalse(result.isNet());
	}

	@Test
	public void testHasBasePriceChangedInvalidPriceObtained()
	{
		configModel.setCurrentTotalPrice(PriceModel.NO_PRICE);
		assertFalse(cut.hasBasePriceChanged(cartEntry));
	}

	@Test
	public void testHasBasePriceChangedPriceRemainsTheSame()
	{
		prepareModelsForUpdateCartEntryPrices();
		when(cartEntry.getBasePrice()).thenReturn(configModel.getCurrentTotalPrice().getPriceValue().doubleValue());
		assertFalse(cut.hasBasePriceChanged(cartEntry));
	}

	@Test
	public void testHasBasePriceChanged()
	{
		prepareModelsForUpdateCartEntryPrices();
		assertTrue(cut.hasBasePriceChanged(cartEntry));
	}

	@Test
	public void testGetConfigIdDraft()
	{
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(primaryKey.toString())).thenReturn(null);
		when(configurationAbstractOrderEntryLinkStrategy.getDraftConfigIdForCartEntry(primaryKey.toString()))
				.thenReturn(CONFIG_ID_NO_PRICE);
		final String result = cut.getConfigIdForCartEntry(cartEntry);
		assertNotNull(result);
		assertEquals(CONFIG_ID_NO_PRICE, result);
	}

	@Test
	public void testRecreateConfigurationWithCurrentCurrency()
	{
		cut.recreateConfigurationWithCurrentCurrency(CONFIG_ID_NO_PRICE, cartEntry, false);
		verify(configCopyStrategy).deepCopyConfiguration(CONFIG_ID_NO_PRICE, PRODUCT_CODE, EXTERNAL_CONFIGURATION, true);
		verify(configurationService).releaseSession(CONFIG_ID_NO_PRICE);
		verify(configurationAbstractOrderEntryLinkStrategy).setConfigIdForCartEntry(primaryKey.toString(), NEW_CONFIG_ID);
	}

	@Test
	public void testRecreateConfigurationWithCurrentCurrencyDraft()
	{
		cut.recreateConfigurationWithCurrentCurrency(CONFIG_ID_NO_PRICE, cartEntry, true);
		verify(configCopyStrategy).deepCopyConfiguration(CONFIG_ID_NO_PRICE, PRODUCT_CODE, EXTERNAL_CONFIGURATION, true);
		verify(configurationService).releaseSession(CONFIG_ID_NO_PRICE);
		verify(configurationAbstractOrderEntryLinkStrategy).setDraftConfigIdForCartEntry(primaryKey.toString(), NEW_CONFIG_ID);
	}

	@Test
	public void testRecreateConfigurationWithCurrentCurrencyNullConfigId()
	{
		cut.recreateConfigurationWithCurrentCurrency(null, cartEntry, true);
		verify(configCopyStrategy, times(0)).deepCopyConfiguration(any(), any(), any(), eq(true));
		verify(configurationService, times(0)).releaseSession(any());
		verify(configurationAbstractOrderEntryLinkStrategy, times(0)).setDraftConfigIdForCartEntry(any(), any());
	}

	@Test
	public void testReloadCurrentTotalWithCurrentCurrency()
	{
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(primaryKey.toString()))
				.thenReturn(CONFIG_ID_NO_PRICE, NEW_CONFIG_ID);
		final PriceModel result = cut.reloadCurrentTotalWithCurrentCurrency(cartEntry);
		verify(configurationAbstractOrderEntryLinkStrategy, times(2)).getConfigIdForCartEntry(primaryKey.toString());
		verify(configurationAbstractOrderEntryLinkStrategy, times(1)).getDraftConfigIdForCartEntry(primaryKey.toString());
		assertNotNull(result);
		assertEquals(NEW_CURRENT_TOTAL_PRICE_VALUE.doubleValue(), result.getPriceValue().doubleValue(), 0.001);
	}

	@Test
	public void testCalculateBasePriceModelForConfigurationConfigIdNullAsyncPricing()
	{
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(primaryKey.toString())).thenReturn(null);
		when(pricingService.isActive()).thenReturn(true);
		assertNull(cut.calculateBasePriceModelForConfiguration(cartEntry));
	}

	@Test
	public void testCalculateBasePriceModelForConfigurationConfigIdNullSyncPricing()
	{
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(primaryKey.toString())).thenReturn(null);
		when(pricingService.isActive()).thenReturn(false);
		when(configurationAbstractOrderIntegrationStrategy.getConfigurationForAbstractOrderEntry(cartEntry))
				.thenReturn(newConfigModel);
		assertNotNull(cut.calculateBasePriceModelForConfiguration(cartEntry));
	}

	@Test
	public void testSetters()
	{
		final ProductConfigurationPricingStrategyImpl classUnderTestBare = new ProductConfigurationPricingStrategyImpl();
		classUnderTestBare.setCommerceCartService(commerceCartService);
		classUnderTestBare.setConfigCopyStrategy(configCopyStrategy);
		classUnderTestBare.setConfigurationAbstractOrderEntryLinkStrategy(configurationAbstractOrderEntryLinkStrategy);
		classUnderTestBare.setConfigurationService(configurationService);
		classUnderTestBare.setI18NService(i18NService);
		classUnderTestBare.setModelService(modelService);
		classUnderTestBare.setPricingService(pricingService);
		assertEquals(commerceCartService, classUnderTestBare.getCommerceCartService());
		assertEquals(configCopyStrategy, classUnderTestBare.getConfigCopyStrategy());
		assertEquals(configurationAbstractOrderEntryLinkStrategy, classUnderTestBare.getAbstractOrderEntryLinkStrategy());
		assertEquals(configurationService, classUnderTestBare.getConfigurationService());
		assertEquals(i18NService, classUnderTestBare.getI18NService());
		assertEquals(modelService, classUnderTestBare.getModelService());
		assertEquals(pricingService, classUnderTestBare.getPricingService());
	}

	@Test(expected = IllegalStateException.class)
	public void testValidateCurrencyInvalid()
	{
		final PriceModelImpl price = new PriceModelImpl();
		price.setCurrency(CURRENCY_ISO);
		cut.validateCurrency(price);
	}

	@Test
	public void testValidateCurrencyValid()
	{
		final PriceModelImpl price = new PriceModelImpl();
		price.setCurrency(EUR);
		cut.validateCurrency(price);
		cut.validateCurrency(null);
		cut.validateCurrency(PriceModel.NO_PRICE);
		cut.validateCurrency(PriceModel.PRICE_NA);
	}


}
