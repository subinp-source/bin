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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class CPQBandsawMockImplTest
{
	private CPQBandsawMockImpl classUnderTest;
	private ConfigModel model;
	private InstanceModel instance;
	private InstanceModel baseSubInstance;

	@Before
	public void setUp()
	{
		classUnderTest = (CPQBandsawMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode("CONF_BANDSAW_ML");
		model = classUnderTest.createDefaultConfiguration();
		classUnderTest.showDeltaPrices(false);
		instance = model.getRootInstance();
		baseSubInstance = instance.getSubInstance(CPQBandsawMockImpl.BASE_SUBINSTANCE_ID);
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
	public void testCheckCstic()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_OTHERAPPTYPES);

		classUnderTest.checkCstic(model, instance, cstic);

		assertEquals(true, cstic.isConsistent());
		assertEquals(true, cstic.isComplete());
	}

	@Test
	public void testIntervalSplitLeftAngle()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_L);
		final List<Float> floatIntervals = new ArrayList<>();
		classUnderTest.extractIntervalBoundaries(cstic, floatIntervals);

		final Float lowerBoundary = floatIntervals.get(0);
		final Float upperBoundary = floatIntervals.get(1);

		assertEquals("-10.0", lowerBoundary.toString());
		assertEquals("-5.0", upperBoundary.toString());

	}

	@Test
	public void testIntervalSplitRightAngle()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_R);
		final List<Float> floatIntervals = new ArrayList<>();
		classUnderTest.extractIntervalBoundaries(cstic, floatIntervals);

		final Float lowerBoundary = floatIntervals.get(0);
		final Float upperBoundary = floatIntervals.get(1);

		assertEquals("45.0", lowerBoundary.toString());
		assertEquals("60.0", upperBoundary.toString());

	}

	@Test
	public void testisInIntervalSuccess()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_R);
		final List<Float> floatIntervals = new ArrayList<>();

		final Float lowerBoundary = Float.valueOf("-10.0");
		final Float upperBoundary = Float.valueOf("-5.0");
		floatIntervals.add(lowerBoundary);
		floatIntervals.add(upperBoundary);
		cstic.setSingleValue("-7");

		final boolean isInInterval = classUnderTest.isInInterval(model, instance, cstic, floatIntervals);
		Assert.assertTrue("Value is not in Interval.", isInInterval);

	}

	@Test
	public void testisInIntervalFailure()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_R);
		final List<Float> floatIntervals = new ArrayList<>();

		final Float lowerBoundary = Float.valueOf("-10.0");
		final Float upperBoundary = Float.valueOf("-5.0");
		floatIntervals.add(lowerBoundary);
		floatIntervals.add(upperBoundary);
		cstic.setSingleValue("-12");

		final boolean isInInterval = classUnderTest.isInInterval(model, instance, cstic, floatIntervals);
		Assert.assertFalse("Value is in Interval.", isInInterval);

	}






	@Test
	public void testCupboardDefailt()
	{
		final CsticModel cupboard = baseSubInstance.getCstic(CPQBandsawMockImpl.CONF_BS_BASE_CUPBOARD);
		assertEquals(false, cupboard.isVisible());
	}

	@Test
	public void testBaseMount()
	{
		final List<CsticValueModel> assignedValues = setAssignedValue(CPQBandsawMockImpl.CONF_BS_BASE_MOUNTED);
		final CsticModel cupboard = baseSubInstance.getCstic(CPQBandsawMockImpl.CONF_BS_BASE_CUPBOARD);
		final CsticModel baseMounting = baseSubInstance.getCstic(CPQBandsawMockImpl.CONF_BS_BASE_MOUNTING);

		baseMounting.setAssignedValues(assignedValues);
		classUnderTest.checkCstic(model, baseSubInstance, cupboard);

		assertEquals(false, cupboard.isVisible());
	}

	@Test
	public void testStandMount()
	{
		final List<CsticValueModel> assignedValues = setAssignedValue(CPQBandsawMockImpl.CONF_BS_BASE_STAND);
		final CsticModel cupboard = baseSubInstance.getCstic(CPQBandsawMockImpl.CONF_BS_BASE_CUPBOARD);
		final CsticModel baseMounting = baseSubInstance.getCstic(CPQBandsawMockImpl.CONF_BS_BASE_MOUNTING);

		baseMounting.setAssignedValues(assignedValues);
		classUnderTest.checkCstic(model, baseSubInstance, cupboard);

		assertEquals(true, cupboard.isVisible());
	}

	@Test
	public void testTiltTableDefault()
	{
		final CsticModel tiltAngleL = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_L);
		final CsticModel tiltAngleR = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_R);

		assertEquals(false, tiltAngleL.isVisible());
		assertEquals(false, tiltAngleR.isVisible());
	}

	@Test
	public void testTiltTableFalse()
	{
		final List<CsticValueModel> assignedValues = setAssignedValue(CPQBandsawMockImpl.CONF_BS_TILT_NO);
		final CsticModel tiltAngleL = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_L);
		final CsticModel tiltAngleR = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_R);
		final CsticModel tiltTable = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILTTABLE);

		tiltTable.setAssignedValues(assignedValues);
		classUnderTest.checkCstic(model, instance, tiltAngleL);
		classUnderTest.checkCstic(model, instance, tiltAngleR);

		assertEquals(false, tiltAngleL.isVisible());
		assertEquals(false, tiltAngleR.isVisible());
	}

	@Test
	public void testTiltTableTrue()
	{
		final List<CsticValueModel> assignedValues = setAssignedValue(CPQBandsawMockImpl.CONF_BS_TILT_YES);
		final CsticModel tiltAngleL = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_L);
		final CsticModel tiltAngleR = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILT_ANGLE_R);
		final CsticModel tiltTable = instance.getCstic(CPQBandsawMockImpl.CONF_BS_TILTTABLE);

		tiltTable.setAssignedValues(assignedValues);
		classUnderTest.checkCstic(model, instance, tiltAngleL);
		classUnderTest.checkCstic(model, instance, tiltAngleR);

		assertEquals(true, tiltAngleL.isVisible());
		assertEquals(true, tiltAngleR.isVisible());
	}


	@Test
	public void testGroupsCreated()
	{
		final List<CsticGroupModel> groups = instance.getCsticGroups();
		assertEquals(3, groups.size());

		CsticGroupModel genGroup = groups.get(0);
		assertEquals("_GEN", genGroup.getName());
		assertEquals(0, genGroup.getCsticNames().size());

		genGroup = groups.get(1);
		assertEquals("1", genGroup.getName());
		assertEquals(8, genGroup.getCsticNames().size());

		genGroup = groups.get(2);
		assertEquals("2", genGroup.getName());
		assertEquals(3, genGroup.getCsticNames().size());
	}

	@Test
	public void testSubInstancesCreated()
	{
		final List<InstanceModel> instances = instance.getSubInstances();
		assertEquals(2, instances.size());

		InstanceModel instance = instances.get(0);
		assertEquals("CONF_BANDSAW_BASE", instance.getName());
		assertEquals(1, instance.getCsticGroups().size());
		assertEquals(2, instance.getCstics().size());

		instance = instances.get(1);
		assertEquals("CONF_BANDSAW_SAWINGUNIT", instance.getName());
		assertEquals(1, instance.getCsticGroups().size());
		assertEquals(4, instance.getCstics().size());
	}

	@Test
	public void testCheckModel()
	{
		model.setBasePrice(PriceModel.NO_PRICE);
		classUnderTest.checkModel(model);
		assertEquals(CPQBandsawMockImpl.BASE_PRICE, model.getBasePrice().getPriceValue().toBigInteger());
	}

	@Test
	public void testCheckCsticThroatWidth()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_THROATWIDTH);

		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(CPQBandsawMockImpl.SURCHARGE_CONF_BS_LARGEWIDTH,
				cstic.getAssignableValues().get(2).getValuePrice().getPriceValue().longValue());
	}

	@Test
	public void testCheckCsticMotor()
	{
		final CsticModel cstic = instance.getCstic(CPQBandsawMockImpl.CONF_BS_MOTOR);

		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(CPQBandsawMockImpl.SURCHARGE_CONF_BS_MOTOR_EXTRA,
				cstic.getAssignableValues().get(1).getValuePrice().getPriceValue().longValue());
	}

}
