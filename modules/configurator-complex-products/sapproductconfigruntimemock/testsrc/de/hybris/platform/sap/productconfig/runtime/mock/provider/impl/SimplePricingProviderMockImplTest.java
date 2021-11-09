/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.provider.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticQualifier;
import de.hybris.platform.sap.productconfig.runtime.interf.PricingConfigurationParameter;
import de.hybris.platform.sap.productconfig.runtime.interf.PricingEngineException;
import de.hybris.platform.sap.productconfig.runtime.interf.ProviderFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigurationRetrievalOptions;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceSummaryModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceValueUpdateModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.mock.impl.CsticModelBuilder;
import de.hybris.platform.sap.productconfig.runtime.mock.impl.CsticValueModelBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SimplePricingProviderMockImplTest
{

	private static final String FIRST_VALUE_NAME = "Value1";
	private static final String CSTIC_NAME = "Cstic";
	private static final String SECOND_VALUE_NAME = "Value2";
	private static final String THIRD_VALUE_NAME = "Value3";
	private static final String CONFIG_ID = "1";
	private static final BigDecimal BASE_PRICE = BigDecimal.valueOf(234);
	private static final String ROOT_INSTANCE_ID = "1";


	SimplePricingProviderMockImpl classUnderTest = new SimplePricingProviderMockImpl();

	private ConfigModel configModel;
	private InstanceModel instanceModel;
	private CsticModel cstic;

	private BigDecimal surcharge;
	private BigDecimal secondSurcharge;
	private BigDecimal thirdSurcharge;

	private CsticValueModel firstValue;
	private CsticValueModel secondValue;
	private CsticValueModel thirdValue;
	private ConfigurationRetrievalOptions options;
	private List<PriceValueUpdateModel> updateModels;
	private PriceValueUpdateModel updateModel;
	private CsticQualifier csticQualifier;
	@Mock
	private ProviderFactory providerFactory;
	@Mock
	private ConfigurationProviderMockImpl mockConfigProvider;
	@Mock
	private PricingConfigurationParameter pricingConfigParameters;

	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);

		configModel = new ConfigModelImpl();
		configModel.setCurrentTotalPrice(createPrice(BASE_PRICE, false));
		configModel.setSelectedOptionsPrice(createPrice(BigDecimal.ZERO, false));
		configModel.setBasePrice(createPrice(BASE_PRICE, false));

		instanceModel = new InstanceModelImpl();
		instanceModel.setId(ROOT_INSTANCE_ID);
		configModel.setRootInstance(instanceModel);

		final CsticValueModelBuilder firstValueBuilder = new CsticValueModelBuilder().withName(FIRST_VALUE_NAME, null);
		firstValue = firstValueBuilder.build();
		final CsticValueModelBuilder secondValueBuilder = new CsticValueModelBuilder().withName(SECOND_VALUE_NAME, null);
		secondValue = secondValueBuilder.build();
		final CsticValueModelBuilder thirdValueBuilder = new CsticValueModelBuilder().withName(THIRD_VALUE_NAME, null);
		thirdValue = thirdValueBuilder.build();
		final CsticModelBuilder csticBuilder = new CsticModelBuilder().withName(CSTIC_NAME, null);
		csticBuilder.addValue(firstValue).addAssignableValue(secondValue).addAssignableValue(thirdValue)
				.addAssignableValue(firstValue);
		cstic = csticBuilder.build();
		instanceModel.setCstics(Arrays.asList(cstic));


		surcharge = BigDecimal.valueOf(123);
		secondSurcharge = BigDecimal.valueOf(200);
		thirdSurcharge = BigDecimal.valueOf(150);

		csticQualifier = new CsticQualifier();
		csticQualifier.setCsticName(cstic.getName());
		csticQualifier.setInstanceId(ROOT_INSTANCE_ID);
		updateModel = new PriceValueUpdateModel();
		updateModel.setCsticQualifier(csticQualifier);
		updateModel.setSelectedValues(Arrays.asList(firstValue.getName()));
		updateModels = Arrays.asList(updateModel);

		classUnderTest.setProviderFactory(providerFactory);


		Mockito.when(providerFactory.getConfigurationProvider()).thenReturn(mockConfigProvider);
		Mockito.when(providerFactory.getPricingParameter()).thenReturn(pricingConfigParameters);
		Mockito.when(mockConfigProvider.getCachedConfig(CONFIG_ID)).thenReturn(configModel);
		Mockito.when(pricingConfigParameters.showDeltaPrices(Mockito.any())).thenReturn(true);
	}

	protected PriceModel createPrice(final BigDecimal priceValue, final boolean allowZeroPrice)
	{
		PriceModel price = PriceModel.NO_PRICE;
		if (priceValue != null && (allowZeroPrice || priceValue.longValue() != 0))
		{
			price = new PriceModelImpl();
			price.setCurrency("EUR");
			price.setPriceValue(priceValue);
		}
		return price;
	}

	@Test
	public void testFillValuePrices() throws PricingEngineException
	{
		//does not change prices
		final PriceModel valuePrice = createPrice(BigDecimal.valueOf(177), false);
		firstValue.setValuePrice(valuePrice);
		classUnderTest.fillValuePrices(configModel);
		assertEquals(valuePrice, firstValue.getValuePrice());
	}

	@Test
	public void testFillValuePricesWithOptions() throws PricingEngineException
	{
		//does not change prices
		final PriceModel valuePrice = createPrice(BigDecimal.valueOf(177), false);
		firstValue.setValuePrice(valuePrice);
		classUnderTest.fillValuePrices(configModel, options);
		assertEquals(valuePrice, firstValue.getValuePrice());
	}

	@Test
	public void testFillValuePricesSelectively() throws PricingEngineException
	{
		final PriceModel valuePrice = createPrice(surcharge, false);
		firstValue.setDeltaPrice(valuePrice);

		checkPriceValueFromUpdateModel();
	}

	@Test
	public void testFillValuePricesSelectivelyForValuePricing() throws PricingEngineException
	{
		Mockito.when(pricingConfigParameters.showDeltaPrices(Mockito.any())).thenReturn(false);
		final PriceModel valuePrice = createPrice(surcharge, false);
		firstValue.setValuePrice(valuePrice);

		checkPriceValueFromUpdateModel();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetPriceSummary() throws PricingEngineException
	{
		classUnderTest.getPriceSummary(CONFIG_ID);
	}

	@Test
	public void testGetPriceSummaryWithOptions() throws PricingEngineException
	{
		final PriceSummaryModel priceSummary = classUnderTest.getPriceSummary(CONFIG_ID, options);
		assertNotNull(priceSummary);
		assertEquals(BASE_PRICE, priceSummary.getBasePrice().getPriceValue());
		assertEquals(BASE_PRICE, priceSummary.getCurrentTotalPrice().getPriceValue());
	}

	@Test
	public void testIsActive()
	{
		//per default, this pricing provider is not active
		assertFalse(classUnderTest.isActive());
	}

	@Test
	public void testRetrieveRelatedInstanceRoot()
	{
		final InstanceModel currentInstance = new InstanceModelImpl();
		currentInstance.setId("1");
		assertEquals(currentInstance, classUnderTest.retrieveRelatedInstance("1", currentInstance));
	}

	@Test
	public void testRetrieveRelatedInstanceSubinstance()
	{
		final InstanceModel rootInstance = prepareInstanceHierarchy();
		final InstanceModel retrievedInstance = classUnderTest.retrieveRelatedInstance("SI111", rootInstance);
		assertNotNull(retrievedInstance);
		assertEquals("SI111", retrievedInstance.getId());
	}

	@Test
	public void testRetrieveRelatedInstanceWrongInstance()
	{
		final InstanceModel rootInstance = prepareInstanceHierarchy();
		final InstanceModel retrievedInstance = classUnderTest.retrieveRelatedInstance("XYZ", rootInstance);
		assertNull(retrievedInstance);
	}

	private InstanceModel prepareInstanceHierarchy()
	{
		final InstanceModel root = addInstanceWithId(null, "ROOT");
		final InstanceModel si1 = addInstanceWithId(root, "SI1");
		final InstanceModel si2 = addInstanceWithId(root, "SI2");
		final InstanceModel si11 = addInstanceWithId(si1, "SI11");
		final InstanceModel si12 = addInstanceWithId(si1, "SI12");
		final InstanceModel si21 = addInstanceWithId(si2, "SI21");
		final InstanceModel si22 = addInstanceWithId(si2, "SI22");
		final InstanceModel si111 = addInstanceWithId(si11, "SI111");
		final InstanceModel si112 = addInstanceWithId(si11, "SI112");
		return root;
	}

	private InstanceModel addInstanceWithId(final InstanceModel currentInstance, final String id)
	{
		final InstanceModel instance = new InstanceModelImpl();
		instance.setId(id);
		instance.setSubInstances(new ArrayList<InstanceModel>());

		if (currentInstance != null)
		{
			final List<InstanceModel> subInstanses = currentInstance.getSubInstances();
			subInstanses.add(instance);
			currentInstance.setSubInstances(subInstanses);
		}
		return instance;
	}

	protected void checkPriceValueFromUpdateModel() throws PricingEngineException
	{
		classUnderTest.fillValuePrices(updateModels, CONFIG_ID);
		assertFalse(CollectionUtils.isEmpty(updateModels));
		final Map<String, PriceModel> valuePrices = updateModels.get(0).getValuePrices();
		assertNotNull(valuePrices);
		final PriceModel priceModel = valuePrices.get(FIRST_VALUE_NAME);
		assertNotNull(priceModel);
		assertEquals(surcharge, priceModel.getPriceValue());
	}
}
