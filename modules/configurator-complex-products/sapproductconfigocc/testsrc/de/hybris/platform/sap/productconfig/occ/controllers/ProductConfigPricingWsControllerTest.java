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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationPricingFacade;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.PriceValueUpdateData;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UniqueUIKeyGenerator;
import de.hybris.platform.sap.productconfig.occ.ConfigurationSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.PriceSummaryWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductConfigPricingWsControllerTest
{
	private static final String CONFIG_ID = "config id";
	private static final String SEPARATOR = "@";
	private static final String GROUP_ID = "2-CPQ_RECEIVER" + SEPARATOR + "_GEN";
	private static final String CSTIC_NAME_1 = "Cstic1";
	private static final String CSTIC_NAME_2 = "Cstic2";
	@Mock
	private ConfigurationPricingFacade configPricingFacade;
	@Mock
	private ConfigurationFacade configurationFacade;
	@Mock
	private UniqueUIKeyGenerator uniqueUIKeyGenerator;
	@Mock
	private DataMapper dataMapper;

	@InjectMocks
	private ProductConfiguratorCCPController classUnderTest;
	private ConfigurationSupplementsWsDTO updatePricingInput;
	private PricingData priceSummary;
	private List<PriceValueUpdateData> valuePrices;
	private final List<CsticSupplementsWsDTO> cstics = new ArrayList<>();
	private final CsticSupplementsWsDTO cstic = new CsticSupplementsWsDTO();
	private final String csticUiKey = "1-GROUP-CSTIC";
	private final PriceValueUpdateData valuePrice = new PriceValueUpdateData();
	private final CsticSupplementsWsDTO csticResult = new CsticSupplementsWsDTO();
	private final ConfigurationData configurationData = new ConfigurationData();
	private final List<UiGroupData> uiGroups = new ArrayList<>();
	private final UiGroupData rootGroup = new UiGroupData();
	private final List<UiGroupData> subGroups = new ArrayList<>();
	private final UiGroupData subGroup = new UiGroupData();
	private final List<CsticData> characteristics = new ArrayList<>();
	private final CsticData firstCstic = new CsticData();
	private final CsticData secondCstic = new CsticData();
	private final List<String> valuePriceInput = new ArrayList<>();


	@Before
	public void setup()
	{

		updatePricingInput = new ConfigurationSupplementsWsDTO();
		priceSummary = new PricingData();
		priceSummary.setCurrentTotal(new PriceData());
		valuePrices = new ArrayList();
		valuePrices.add(valuePrice);
		when(configPricingFacade.getPriceSummary(CONFIG_ID)).thenReturn(priceSummary);
		when(configPricingFacade.getValuePrices(valuePriceInput, CONFIG_ID)).thenReturn(valuePrices);
		when(configPricingFacade.getValuePrices(Collections.emptyList(), CONFIG_ID)).thenReturn(new ArrayList<>());
		when(configurationFacade.getConfiguration(Mockito.any(ConfigurationData.class))).thenReturn(configurationData);
		when(dataMapper.map(priceSummary, PriceSummaryWsDTO.class)).thenReturn(new PriceSummaryWsDTO());
		when(dataMapper.map(valuePrice, CsticSupplementsWsDTO.class)).thenReturn(csticResult);
		when(uniqueUIKeyGenerator.getKeySeparator()).thenReturn(SEPARATOR);
		cstics.add(cstic);
		cstic.setCsticUiKey(csticUiKey);

		configurationData.setGroups(uiGroups);
		uiGroups.add(rootGroup);
		rootGroup.setSubGroups(subGroups);
		subGroups.add(subGroup);
		subGroup.setId(GROUP_ID);
		subGroup.setCstics(characteristics);
		characteristics.add(firstCstic);
		characteristics.add(secondCstic);
		firstCstic.setName(CSTIC_NAME_1);
		secondCstic.setName(CSTIC_NAME_2);
		valuePriceInput.add(GROUP_ID + SEPARATOR + CSTIC_NAME_1);
		valuePriceInput.add(GROUP_ID + SEPARATOR + CSTIC_NAME_2);
	}



	@Test
	public void testCompilePricingResult()
	{
		final ConfigurationSupplementsWsDTO pricingResult = classUnderTest.compilePricingResult(CONFIG_ID, priceSummary,
				valuePrices);
		assertNotNull(pricingResult);
		final List<CsticSupplementsWsDTO> resultCstics = pricingResult.getAttributes();
		assertNotNull(resultCstics);
		assertEquals(1, resultCstics.size());
		assertNotNull(resultCstics.get(0));
	}

	@Test
	public void testCompilePricingResultEmptyCsticList()
	{
		final ConfigurationSupplementsWsDTO pricingResult = classUnderTest.compilePricingResult(CONFIG_ID, priceSummary,
				Collections.emptyList());
		assertNotNull(pricingResult);
		final List<CsticSupplementsWsDTO> resultCstics = pricingResult.getAttributes();
		assertNotNull(resultCstics);
		assertTrue(resultCstics.isEmpty());
	}

	@Test
	public void testCompilePricingResultHeaderAttributes()
	{
		final ConfigurationSupplementsWsDTO pricingResult = classUnderTest.compilePricingResult(CONFIG_ID, priceSummary,
				Collections.emptyList());
		assertNotNull(pricingResult);
		assertFalse(pricingResult.isPricingError());
		assertEquals(CONFIG_ID, pricingResult.getConfigId());
	}

	@Test
	public void testGetUiGroup()
	{
		final UiGroupData uiGroup = classUnderTest.getUiGroup(CONFIG_ID, GROUP_ID);
		assertNotNull(uiGroup);
		assertEquals(GROUP_ID, uiGroup.getId());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetUiGroupNothingFound()
	{
		classUnderTest.getUiGroup(CONFIG_ID, "Not existing");
	}

	@Test
	public void testGetFlattened()
	{
		final Stream<UiGroupData> flattened = classUnderTest.getFlattened(rootGroup);
		assertEquals(2, flattened.collect(Collectors.toList()).size());
	}

	@Test
	public void testGetFlattenedCanDealWithNullSubGroups()
	{
		final Stream<UiGroupData> flattened = classUnderTest.getFlattened(subGroup);
		assertEquals(1, flattened.collect(Collectors.toList()).size());
	}

	@Test
	public void testCompileValuePriceInputForGroup()
	{
		final List<String> valuePriceInput = classUnderTest.compileValuePriceInput(subGroup);
		assertNotNull(valuePriceInput);
		assertEquals(2, valuePriceInput.size());
		assertEquals(GROUP_ID + SEPARATOR + CSTIC_NAME_1, valuePriceInput.get(0));
	}

	@Test
	public void testCompileValuePriceInputCanDealWithNullCstics()
	{
		subGroup.setCstics(null);
		final List<String> valuePriceInput = classUnderTest.compileValuePriceInput(subGroup);
		assertNotNull(valuePriceInput);
		assertTrue(valuePriceInput.isEmpty());
	}

	@Test
	public void testCompileCsticKey()
	{
		assertEquals(GROUP_ID + SEPARATOR + CSTIC_NAME_1, classUnderTest.compileCsticKey(firstCstic, subGroup));
	}

	@Test
	public void testPricing()
	{
		final ConfigurationSupplementsWsDTO supplementsWsDTO = classUnderTest.getPricing(CONFIG_ID, GROUP_ID);
		assertNotNull(supplementsWsDTO);
		final List<CsticSupplementsWsDTO> csticPricingResult = supplementsWsDTO.getAttributes();
		assertEquals(1, csticPricingResult.size());
	}

	@Test
	public void testPricingNoGroupSpecified()
	{
		final ConfigurationSupplementsWsDTO supplementsWsDTO = classUnderTest.getPricing(CONFIG_ID, null);
		assertNotNull(supplementsWsDTO);
		final List<CsticSupplementsWsDTO> csticPricingResult = supplementsWsDTO.getAttributes();
		assertTrue(csticPricingResult.isEmpty());
	}

}
