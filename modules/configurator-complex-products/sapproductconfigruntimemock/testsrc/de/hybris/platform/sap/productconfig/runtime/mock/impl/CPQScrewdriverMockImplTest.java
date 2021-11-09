/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class CPQScrewdriverMockImplTest
{
	protected static final String BASE_PRODUCT_CODE = "CONF_SCREWDRIVER_S";
	private static final BigDecimal BASE_PRICE = BigDecimal.valueOf(160);

	private CPQScrewdriverMockImpl classUnderTest;
	private ConfigModel model;
	private CsticModel csticCol;

	@Before
	public void setUp()
	{
		classUnderTest = (CPQScrewdriverMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(BASE_PRODUCT_CODE);
		getModelAndColourCstic();
	}

	protected void getModelAndColourCstic()
	{
		model = classUnderTest.createDefaultConfiguration();
		csticCol = model.getRootInstance().getCstic(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_COL);
	}

	private List<CsticValueModel> setAssignedValue(final String value)
	{
		final List<CsticValueModel> assignedValues = new ArrayList<>();
		final CsticValueModel csticValue = new CsticValueModelImpl();
		csticValue.setName(value);
		assignedValues.add(csticValue);

		return assignedValues;
	}

	@Test
	public void testSize()
	{
		assertEquals(12, model.getRootInstance().getCstics().size());
	}

	@Test
	public void testDefaultPrice()
	{
		assertEquals(BASE_PRICE, model.getBasePrice().getPriceValue());
	}

	@Test
	public void testPriceAfterUpdate()
	{
		classUnderTest.checkModel(model);
		assertEquals(BASE_PRICE, model.getBasePrice().getPriceValue());
	}

	@Test
	public void testColorProfessional()
	{
		setMode(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_PROFESSIONAL);
		testForColor(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BLUE);
	}

	@Test
	public void testColorStandard()
	{
		setMode(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_STANDARD);
		testForColor(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GREEN);
	}

	@Test
	public void testColorWithoutMode()
	{
		setMode(null);
		final List<CsticValueModel> assignedValues = csticCol.getAssignedValues();
		assertEquals(0, assignedValues.size());
	}

	@Test
	public void testGroupsCreated()
	{
		final List<CsticGroupModel> groups = model.getRootInstance().getCsticGroups();
		assertEquals(4, groups.size());

		CsticGroupModel genGroup = groups.get(0);
		assertEquals("_GEN", genGroup.getName());
		assertEquals(0, genGroup.getCsticNames().size());

		genGroup = groups.get(1);
		assertEquals("1", genGroup.getName());
		assertEquals(2, genGroup.getCsticNames().size());

		genGroup = groups.get(2);
		assertEquals("2", genGroup.getName());
		assertEquals(6, genGroup.getCsticNames().size());

		genGroup = groups.get(3);
		assertEquals("3", genGroup.getName());
		assertEquals(4, genGroup.getCsticNames().size());
	}

	@Test
	public void testCreateDefaultConfigurationForVariant_2_12()
	{
		classUnderTest = (CPQScrewdriverMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(BASE_PRODUCT_CODE,
				CPQScrewdriverMockImpl.VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_12);
		getModelAndColourCstic();

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_PROFESSIONAL, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_MODE);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BLUE, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_COL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_18V, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_POWER);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_3GEAR, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GEARS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_50_90NM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE);

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_20LEVELS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE_LEVEL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_35AH, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BATTERY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_2_12MM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_DRILL_CHUCK);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY_YES, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_LED, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_POWER_LEVEL,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ROTATION,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_SOUND,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_VIBRATION,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_SECONDBATTERY,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BITS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_CASE, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_DRILLS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
	}

	@Test
	public void testCreateDefaultConfigurationForVariant_2_18()
	{
		classUnderTest = (CPQScrewdriverMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(BASE_PRODUCT_CODE,
				CPQScrewdriverMockImpl.VARIANT_CODE_CONF_SCREWDRIVER_S_PROF_2_18);
		getModelAndColourCstic();
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_PROFESSIONAL, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_MODE);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BLUE, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_COL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_14_6V, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_POWER);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_3GEAR, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GEARS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_50_90NM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE);

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_20LEVELS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE_LEVEL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_35AH, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BATTERY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_2_18MM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_DRILL_CHUCK);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING_YES,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY_YES, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_LED, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_POWER_LEVEL,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ROTATION,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_SOUND,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_VIBRATION,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_SECONDBATTERY,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BITS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_CASE, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_DRILLS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);
	}

	@Test
	public void testCreateDefaultConfigurationForVariantStd_2_12()
	{
		classUnderTest = (CPQScrewdriverMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(BASE_PRODUCT_CODE,
				CPQScrewdriverMockImpl.VARIANT_CODE_CONF_SCREWDRIVER_S_STD_2_12);
		getModelAndColourCstic();
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_STANDARD, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_MODE);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GREEN, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_COL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_10_8V, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_POWER);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_2GEAR, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GEARS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_15_25NM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE);

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_12LEVELS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE_LEVEL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_15AH, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BATTERY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_2_12MM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_DRILL_CHUCK);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING_NO,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY_NO, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_LED, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ROTATION,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);


		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_SECONDBATTERY,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);

	}

	@Test
	public void testCreateDefaultConfigurationForVariantStd_4_9()
	{
		classUnderTest = (CPQScrewdriverMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(BASE_PRODUCT_CODE,
				CPQScrewdriverMockImpl.VARIANT_CODE_CONF_SCREWDRIVER_S_STD_4_9);
		getModelAndColourCstic();
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_STANDARD, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_MODE);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GREEN, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_COL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_10_8V, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_POWER);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_2GEAR, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_GEARS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_15_25NM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE);

		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_16LEVELS, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_TORQUE_LEVEL);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_25AH, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_BATTERY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_4_9MM, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_DRILL_CHUCK);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING_NO,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_OUTDOOR_CHARGING);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY_NO, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_WARRANTY);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_LED, CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);
		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ROTATION,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ADDITIONAL_OPTIONS);


		checkAssignedValue(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_SECONDBATTERY,
				CPQScrewdriverMockImpl.CONF_SCREWDRIVER_ACCESSORY);

	}

	protected void checkAssignedValue(final String value, final String csticName)
	{
		final CsticModel cstic = model.getRootInstance().getCstic(csticName);
		final List<CsticValueModel> assignedValues = cstic.getAssignedValues();
		assertFalse("List of values must not be empty for " + csticName, assignedValues.isEmpty());
		assertTrue("We expect to find value " + value,
				assignedValues.stream().map(csticValue -> csticValue.getName()).anyMatch(name -> name.equals(value)));
	}

	protected void setMode(final String newMode)
	{
		final CsticModel csticMode = model.getRootInstance().getCstic(CPQScrewdriverMockImpl.CONF_SCREWDRIVER_MODE);
		csticMode.setSingleValue(newMode);
		classUnderTest.checkColor(model.getRootInstance(), csticCol);
	}

	protected void testForColor(final String color)
	{
		final List<CsticValueModel> assignedValues = csticCol.getAssignedValues();
		assertEquals(1, assignedValues.size());
		assertEquals(color, assignedValues.get(0).getName());
	}
}
