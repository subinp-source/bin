/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticTypeMapper;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.FirstOrLastGroupType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.populator.SolvableConflictPopulator;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroupImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.sap.productconfig.services.analytics.intf.AnalyticsService;
import de.hybris.platform.sap.productconfig.services.impl.SimpleConfigurationVariantUtilImpl;
import de.hybris.platform.sap.productconfig.services.intf.PricingService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationClassificationCacheStrategy;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ConfigurationBaseFacadeImplTest
{
	private static final String KB_BUILD_NO = "buildNo";
	private static final String KB_VERSION = "version";
	private static final String KB_NAME = "name";
	private static final String KB_LOGSYS = "logsys";
	private static final String NAME = "A";
	private static final String DESCRIPTION = "B";
	private static final String PRODUCT_CODE = "product_123";

	private final ConfigurationBaseFacadeImpl classUnderTest = new ConfigurationBaseFacadeImpl();
	private ConfigurationMessageMapperImpl configurationMessageMapper;
	private UiGroupData uiGroup;
	private List<UiGroupData> subGroups;
	private boolean oneSubGroupConfigurable;
	private final UiGroupData subGroup = new UiGroupData();
	private final GroupStatusCheckerImpl groupStatusChecker = new GroupStatusCheckerImpl();

	@Mock
	private ProductService productServiceMock;
	@Mock
	private ConfigurationClassificationCacheStrategy onfigurationClassificationCacheStrategyMock;
	@Mock
	private PricingService pricingServiceMock;
	@Mock
	private SolvableConflictPopulator conflictsPopulatorMock;
	@Mock
	private ProductModel productModelMock;
	@Mock
	private VariantProductModel variantProductModelMock;
	@Mock
	private ConfigPricing configPricingMock;
	@Mock
	private SimpleConfigurationVariantUtilImpl configurationVariantUtilMock;
	@Mock
	private AnalyticsService analyticsServiceMock;
	@Mock
	private CsticTypeMapper csticTypeMapperMock;
	@Mock
	private ProductConfigurationService configurationServiceMock;
	@Mock
	private ConfigurationAbstractOrderEntryLinkStrategy mockConfigurationAbstractOrderEntryLinkStrategy;
	@Mock
	private ConfigurationExpertModeFacadeImpl configurationExpertModeFacade;

	private ConfigModel configModel;
	private ConfigurationData configData;
	private InstanceModel rootInstance;
	private final KBKeyData kbKey = new KBKeyData();
	private final KBKey kbModel = new KBKeyImpl(null, KB_NAME, KB_LOGSYS, KB_VERSION);

	protected void callPopulateAndCheckPricingMode()
	{
		given(Boolean.valueOf(analyticsServiceMock.isActive())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(pricingServiceMock.isActive())).willReturn(Boolean.TRUE);
		classUnderTest.populateConfigDataFromModel(configData, configModel);
		assertTrue(configData.isAsyncPricingMode());
		assertFalse(configData.isPricingError());
	}

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setProductService(productServiceMock);
		classUnderTest.setConfigurationVariantUtil(configurationVariantUtilMock);
		classUnderTest.setUiKeyGenerator(new UniqueUIKeyGeneratorImpl());
		classUnderTest.setClassificationCacheStrategy(onfigurationClassificationCacheStrategyMock);
		classUnderTest.setPricingService(pricingServiceMock);
		classUnderTest.setConflictPopulator(conflictsPopulatorMock);
		classUnderTest.setConfigPricing(configPricingMock);
		classUnderTest.setOfferVariantSearch(false);
		classUnderTest.setAnalyticsService(analyticsServiceMock);
		configurationMessageMapper = new ConfigurationMessageMapperImpl();
		configurationMessageMapper.setConfigurationAbstractOrderEntryLinkStrategy(mockConfigurationAbstractOrderEntryLinkStrategy);
		classUnderTest.setMessagesMapper(configurationMessageMapper);
		classUnderTest.setConfigurationService(configurationServiceMock);
		classUnderTest.setConfigurationExpertModeFacade(configurationExpertModeFacade);
		classUnderTest.setGroupStatusChecker(groupStatusChecker);

		given(productServiceMock.getProductForCode(PRODUCT_CODE)).willReturn(productModelMock);

		configModel = new ConfigModelImpl();
		configModel.setKbKey(kbModel);
		rootInstance = new InstanceModelImpl();
		configModel.setRootInstance(rootInstance);
		configData = new ConfigurationData();
		kbKey.setProductCode(PRODUCT_CODE);
		configData.setKbKey(kbKey);

		when(configurationExpertModeFacade.isExpertModeActive()).thenReturn(false);
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn(null);
	}

	@Test
	public void testOneGroupConfigurableFalse()
	{
		oneSubGroupConfigurable = false;
		classUnderTest.checkAdoptSubGroup(uiGroup, subGroups, oneSubGroupConfigurable);
		assertNull(subGroup.getName());
		assertNull(subGroup.getDescription());
	}

	@Test
	public void testOneGroupConfigurableTrue()
	{
		oneSubGroupConfigurable = true;
		classUnderTest.checkAdoptSubGroup(uiGroup, subGroups, oneSubGroupConfigurable);
		assertNotNull(subGroup.getName());
		assertNotNull(subGroup.getDescription());
	}

	@Before
	public void createTestData()
	{
		uiGroup = new UiGroupData();
		subGroups = new ArrayList<>();
		uiGroup.setName(NAME);
		uiGroup.setDescription(DESCRIPTION);
		uiGroup.setSubGroups(subGroups);
		subGroups.add(subGroup);
	}

	@Test
	public void testShowVariants_disabled()
	{
		classUnderTest.setOfferVariantSearch(false);
		assertFalse("Do not show the variants, as varaint search is disbaled", classUnderTest.showVariants(PRODUCT_CODE));
	}

	@Test
	public void testShowVariants_enabledNoVariantsExistingNull()
	{
		classUnderTest.setOfferVariantSearch(true);
		assertFalse("Do not show the variants search, as no variants exist for thze given product",
				classUnderTest.showVariants(PRODUCT_CODE));
	}

	@Test
	public void testShowVariants_enabledNoVariantsEmptyList()
	{
		classUnderTest.setOfferVariantSearch(true);
		given(productModelMock.getVariants()).willReturn(Collections.emptyList());
		assertFalse("Do not show the variants search, as no variants exist for thze given product",
				classUnderTest.showVariants(PRODUCT_CODE));
	}

	@Test
	public void testShowVariants_enabledVariantsFound()
	{
		classUnderTest.setOfferVariantSearch(true);
		given(productModelMock.getVariants()).willReturn(Collections.singletonList(new VariantProductModel()));
		given(Boolean.valueOf(configurationVariantUtilMock.isCPQBaseProduct(productModelMock))).willReturn(Boolean.TRUE);
		assertTrue("At least one varaint exists, so show the variant search", classUnderTest.showVariants(PRODUCT_CODE));
	}

	@Test
	public void testPopulateConfigDataFromModelCompleteConsistent()
	{
		configModel.setComplete(true);
		configModel.setConsistent(true);
		classUnderTest.populateConfigDataFromModel(configData, configModel);
		assertTrue(configData.isComplete());
		assertTrue(configData.isConsistent());
	}

	@Test
	public void testPopulateConfigDataFromModelPricingAndAnalyticsMode()
	{
		configModel.setSingleLevel(true);
		callPopulateAndCheckPricingMode();
		assertTrue(configData.isAnalyticsEnabled());
	}

	@Test
	public void testPopulateConfigDataFromModelPricingAndAnalyticsModeMultiLevel()
	{
		configModel.setSingleLevel(false);
		callPopulateAndCheckPricingMode();
		assertFalse(configData.isAnalyticsEnabled());
	}

	@Test
	public void testPopulateConfigDataFromModelNotCompleteConsistent()
	{
		classUnderTest.populateConfigDataFromModel(configData, configModel);
		assertFalse(configData.isComplete());
		assertFalse(configData.isConsistent());
	}

	@Test
	public void testPopulateConfigDataFromModelEmptyGroupLists()
	{
		classUnderTest.populateConfigDataFromModel(configData, configModel);
		final List<UiGroupData> groups = configData.getGroups();
		assertNotNull(groups);
		assertEquals(0, groups.size());
		final List<UiGroupData> groupsFlat = configData.getCsticGroupsFlat();
		assertNotNull(groupsFlat);
		assertEquals(0, groupsFlat.size());
	}

	@Test
	public void testGetListOfCsticDataInconsistent()
	{
		final CsticModel invisibleCstic = createInvisibleCsticAndPrepareMapper();
		invisibleCstic.setConsistent(false);
		final List<CsticModel> csticModelList = new ArrayList<>();
		csticModelList.add(invisibleCstic);
		final List<CsticData> listOfCsticData = classUnderTest.getListOfCsticData(null, csticModelList, null, null);
		assertNotNull(listOfCsticData);
		assertEquals(1, listOfCsticData.size());
	}

	@Test
	public void testGetListOfCsticDataConsistent()
	{
		final CsticModel invisibleCstic = createInvisibleCsticAndPrepareMapper();
		invisibleCstic.setConsistent(true);
		final List<CsticModel> csticModelList = new ArrayList<>();
		csticModelList.add(invisibleCstic);
		final List<CsticData> listOfCsticData = classUnderTest.getListOfCsticData(null, csticModelList, null, null);
		assertNotNull(listOfCsticData);
		assertEquals(0, listOfCsticData.size());
	}

	@Test
	public void testGetListOfCsticDataConsistentWithoutModel()
	{
		final CsticModel invisibleCstic = createInvisibleCsticAndPrepareMapper();
		invisibleCstic.setConsistent(true);
		final List<CsticModel> csticModelList = new ArrayList<>();
		csticModelList.add(invisibleCstic);
		final List<CsticData> listOfCsticData = classUnderTest.getListOfCsticData(csticModelList, null, null);
		assertNotNull(listOfCsticData);
		assertEquals(0, listOfCsticData.size());
	}


	@Test
	public void testGetListOfCsticDataConsistentInvisibleExpertModeTrue()
	{
		when(configurationExpertModeFacade.isExpertModeActive()).thenReturn(true);
		final CsticModel invisibleCstic = createInvisibleCsticAndPrepareMapper();
		invisibleCstic.setConsistent(true);
		final List<CsticModel> csticModelList = new ArrayList<>();
		csticModelList.add(invisibleCstic);
		final List<CsticData> listOfCsticData = classUnderTest.getListOfCsticData(null, csticModelList, null, null);
		assertNotNull(listOfCsticData);
		assertEquals(1, listOfCsticData.size());
	}

	protected CsticModel createInvisibleCsticAndPrepareMapper()
	{
		classUnderTest.setCsticTypeMapper(csticTypeMapperMock);
		final CsticData value = new CsticData();
		Mockito.when(csticTypeMapperMock.mapCsticModelToData(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(value);
		final CsticModel invisibleCstic = new CsticModelImpl();
		invisibleCstic.setVisible(false);
		return invisibleCstic;
	}

	@Test
	public void testGetConfigurationModelBaseProduct()
	{
		given(Boolean.valueOf(configurationVariantUtilMock.isCPQVariantProduct(productModelMock))).willReturn(Boolean.FALSE);
		final ConfigModel configModel = classUnderTest.getConfigurationModel(kbKey);
		verify(configurationServiceMock).createDefaultConfiguration(Mockito.any());
	}

	@Test
	public void testGetConfigurationModelVariantProduct()
	{
		final ProductModel baseProduct = new ProductModel();
		baseProduct.setCode("XXX");
		given(productServiceMock.getProductForCode(PRODUCT_CODE)).willReturn(variantProductModelMock);
		given(Boolean.valueOf(configurationVariantUtilMock.isCPQVariantProduct(variantProductModelMock))).willReturn(Boolean.TRUE);
		given(variantProductModelMock.getBaseProduct()).willReturn(baseProduct);
		final ConfigModel configModel = classUnderTest.getConfigurationModel(kbKey);
		verify(configurationServiceMock).createConfigurationForVariant(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testPopulateProductCodeFromCaller()
	{
		configData = classUnderTest.convert(kbKey, configModel);
		assertEquals(PRODUCT_CODE, configData.getKbKey().getProductCode());
	}

	@Test
	public void testConvertCreatesNewKbKey()
	{
		configData = classUnderTest.convert(kbKey, configModel);
		assertNotNull(configData.getKbKey());
		assertNotSame(configData.getKbKey(), kbKey);
	}

	@Test
	public void testConvertKbKeyFillsProductData()
	{
		final KBKeyData convertedKbKey = classUnderTest.convertKbKey(configModel.getKbKey(), PRODUCT_CODE);
		assertEquals(PRODUCT_CODE, convertedKbKey.getProductCode());
		assertEquals(KB_LOGSYS, convertedKbKey.getKbLogsys());
		assertEquals(KB_NAME, convertedKbKey.getKbName());
		assertEquals(KB_VERSION, convertedKbKey.getKbVersion());
	}


	@Test
	public void testPopulateKbKeyFromModel()
	{
		classUnderTest.populateKbKey(kbKey, configModel.getKbKey());

		assertEquals(KB_LOGSYS, kbKey.getKbLogsys());
		assertEquals(KB_NAME, kbKey.getKbName());
		assertEquals(KB_VERSION, kbKey.getKbVersion());
	}


	@Test
	public void testPopulateKbBuildNumber()
	{
		configModel.setKbBuildNumber(KB_BUILD_NO);
		configData = classUnderTest.convert(kbKey, configModel);
		assertEquals(KB_BUILD_NO, configData.getKbBuildNumber());
	}

	@Test
	public void testCreateCsticGroup()
	{
		final CsticGroup csticModelGroup = new CsticGroupImpl();
		csticModelGroup.setName("GroupName");
		csticModelGroup.setDescription("GroupDescription");
		final InstanceModel instance = new InstanceModelImpl();
		final UiGroupData uiGroup = classUnderTest.createCsticGroup(null, csticModelGroup, instance, null);
		assertNotNull(uiGroup);
		assertEquals("GroupName", uiGroup.getName());
		assertEquals("GroupDescription", uiGroup.getDescription());
		assertTrue(uiGroup.isCollapsed());
		assertEquals(GroupType.CSTIC_GROUP, uiGroup.getGroupType());
		assertEquals(FirstOrLastGroupType.INTERJACENT, uiGroup.getFirstOrLastGroup());
	}

	public void testCreateCsticGroupWithoutModel()
	{
		final CsticGroup csticModelGroup = new CsticGroupImpl();
		csticModelGroup.setName("GroupName");
		csticModelGroup.setDescription("GroupDescription");
		final InstanceModel instance = new InstanceModelImpl();
		final UiGroupData uiGroup = classUnderTest.createCsticGroup(csticModelGroup, instance, null);
		assertNotNull(uiGroup);
		assertEquals("GroupName", uiGroup.getName());
		assertEquals("GroupDescription", uiGroup.getDescription());
		assertTrue(uiGroup.isCollapsed());
		assertEquals(GroupType.CSTIC_GROUP, uiGroup.getGroupType());
		assertEquals(FirstOrLastGroupType.INTERJACENT, uiGroup.getFirstOrLastGroup());
	}

}
