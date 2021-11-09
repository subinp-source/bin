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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;


@SuppressWarnings("javadoc")
public class DigitalCameraMockImplTest
{
	private static final BigDecimal BASE_PRICE = BigDecimal.valueOf(750);
	private DigitalCameraMockImpl classUnderTest;
	private ConfigModel model;
	private CsticModel csticPixel;
	private CsticModel csticSensor;

	@Before
	public void setUp()
	{
		classUnderTest = (DigitalCameraMockImpl) new RunTimeConfigMockFactory()
				.createConfigMockForProductCode(DigitalCameraMockImpl.ROOT_INSTANCE_NAME);
		model = classUnderTest.createDefaultConfiguration();
		csticPixel = model.getRootInstance().getCstic(DigitalCameraMockImpl.CAMERA_PIXELS);
		csticSensor = model.getRootInstance().getCstic(DigitalCameraMockImpl.CAMERA_SENSOR);
		assertNotNull(csticPixel);
	}

	@Test
	public void testSize()
	{
		assertEquals(16, model.getRootInstance().getCstics().size());
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
	public void testCheckCsticDefaultConfiguration()
	{
		classUnderTest.checkCstic(model, model.getRootInstance(), csticPixel);
		assertEquals(4, csticPixel.getAssignableValues().size());
	}

	@Test
	public void testPixelsForStandardMode()
	{
		classUnderTest.checkPixels(model.getRootInstance(), csticPixel);
		assertEquals(4, csticPixel.getAssignableValues().size());
	}

	@Test
	public void testPixelsForProfMode()
	{
		setModeProf();
		classUnderTest.checkPixels(model.getRootInstance(), csticPixel);
		assertEquals(3, csticPixel.getAssignableValues().size());
	}

	@Test
	public void testSensorForDefaultConfig()
	{
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);
		assertEquals(3, csticSensor.getAssignableValues().size());
	}

	@Test
	public void testSensorForModeProf()
	{
		setModeProf();
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);
		final List<CsticValueModel> assignableValues = csticSensor.getAssignableValues();
		assertEquals(2, assignableValues.size());
		assertEquals(DigitalCameraMockImpl.FULL_FRAME, assignableValues.get(0).getName());
	}

	@Test
	public void testSensorForModeStandard()
	{
		setModeStandard();
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);
		final List<CsticValueModel> assignableValues = csticSensor.getAssignableValues();
		assertEquals(2, assignableValues.size());
		assertEquals(DigitalCameraMockImpl.COMPACT, assignableValues.get(0).getName());
	}

	@Test
	public void testNoConflictingSensors()
	{
		setModeStandard();
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);
		final List<CsticValueModel> assignableValues = csticSensor.getAssignableValues();
		assertEquals(DigitalCameraMockImpl.COMPACT, assignableValues.get(0).getName());
		csticSensor.setSingleValue(DigitalCameraMockImpl.COMPACT);
		setModeProf();
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);
		assertTrue(CollectionUtils.isEmpty(csticSensor.getAssignedValues()));
	}

	@Test
	public void testNoConflictingSensorsFullFrame()
	{
		setModeProf();
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);

		csticSensor.setSingleValue(DigitalCameraMockImpl.FULL_FRAME);

		setModeStandard();
		classUnderTest.checkSensor(model.getRootInstance(), csticSensor);

		assertTrue(CollectionUtils.isEmpty(csticSensor.getAssignedValues()));
	}

	@Test
	public void testNoConflictingPixels()
	{
		setModeStandard();
		classUnderTest.checkPixels(model.getRootInstance(), csticPixel);

		csticPixel.setSingleValue(DigitalCameraMockImpl.P8);

		setModeProf();
		classUnderTest.checkPixels(model.getRootInstance(), csticPixel);

		assertTrue(CollectionUtils.isEmpty(csticPixel.getAssignedValues()));
	}

	@Test
	public void testCreateDefaultConfigurationForVariantProfBlack()
	{
		classUnderTest = (DigitalCameraMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(
				DigitalCameraMockImpl.ROOT_INSTANCE_NAME, DigitalCameraMockImpl.VARIANT_CODE_CONF_CAMERA_SL_PROF_BLACK);
		model = classUnderTest.createDefaultConfiguration();

		checkAssignedValue(DigitalCameraMockImpl.MODE_PROF, DigitalCameraMockImpl.CAMERA_MODE);
		checkAssignedValue(DigitalCameraMockImpl.BLACK, DigitalCameraMockImpl.CAMERA_COLOUR);
		checkAssignedValue(DigitalCameraMockImpl.P20, DigitalCameraMockImpl.CAMERA_PIXELS);
		checkAssignedValue(DigitalCameraMockImpl.FULL_FRAME, DigitalCameraMockImpl.CAMERA_SENSOR);
		checkAssignedValue(DigitalCameraMockImpl.F17, DigitalCameraMockImpl.APERTURE);
		checkAssignedValue(DigitalCameraMockImpl.OPTICAL, DigitalCameraMockImpl.CAMERA_VIEWFINDER);
		checkAssignedValue(DigitalCameraMockImpl.SDXC, DigitalCameraMockImpl.CAMERA_SD_CARD);
		checkAssignedValue(DigitalCameraMockImpl.FORMAT_RAW, DigitalCameraMockImpl.FORMAT_PICTURE);
		checkAssignedValue(DigitalCameraMockImpl.ISO_25600, DigitalCameraMockImpl.CAMERA_MAX_ISO);
		checkAssignedValue(DigitalCameraMockImpl.PIXELS10, DigitalCameraMockImpl.DISPLAY);
		checkAssignedValue(DigitalCameraMockImpl.YES, DigitalCameraMockImpl.CAMERA_TOUCHSCREEN);
		checkAssignedValue(DigitalCameraMockImpl.YES, DigitalCameraMockImpl.CAMERA_TILTABLE);
		checkAssignedValue(DigitalCameraMockImpl.LEICA, DigitalCameraMockImpl.LENS_MANU);
		checkAssignedValue(DigitalCameraMockImpl.WIDE_ZOOM, DigitalCameraMockImpl.LENS_TYPE);
		checkAssignedValue(DigitalCameraMockImpl.IMAGE_STABILIZATION, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.WATERPROOF, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.MOVIE, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.WI_FI_NFC, DigitalCameraMockImpl.C_OPTIONS);
	}

	@Test
	public void testCreateDefaultConfigurationForVariantProfMetallic()
	{
		classUnderTest = (DigitalCameraMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(
				DigitalCameraMockImpl.ROOT_INSTANCE_NAME, DigitalCameraMockImpl.VARIANT_CODE_CONF_CAMERA_SL_PROF_METALLIC);
		model = classUnderTest.createDefaultConfiguration();

		checkAssignedValue(DigitalCameraMockImpl.MODE_PROF, DigitalCameraMockImpl.CAMERA_MODE);
		checkAssignedValue(DigitalCameraMockImpl.WHITE, DigitalCameraMockImpl.CAMERA_COLOUR);
		checkAssignedValue(DigitalCameraMockImpl.P20, DigitalCameraMockImpl.CAMERA_PIXELS);
		checkAssignedValue(DigitalCameraMockImpl.FULL_FRAME, DigitalCameraMockImpl.CAMERA_SENSOR);
		checkAssignedValue(DigitalCameraMockImpl.F17, DigitalCameraMockImpl.APERTURE);
		checkAssignedValue(DigitalCameraMockImpl.OPTICAL, DigitalCameraMockImpl.CAMERA_VIEWFINDER);
		checkAssignedValue(DigitalCameraMockImpl.SDXC, DigitalCameraMockImpl.CAMERA_SD_CARD);
		checkAssignedValue(DigitalCameraMockImpl.FORMAT_RAW, DigitalCameraMockImpl.FORMAT_PICTURE);
		checkAssignedValue(DigitalCameraMockImpl.ISO_25600, DigitalCameraMockImpl.CAMERA_MAX_ISO);
		checkAssignedValue(DigitalCameraMockImpl.PIXELS10, DigitalCameraMockImpl.DISPLAY);
		checkAssignedValue(DigitalCameraMockImpl.YES, DigitalCameraMockImpl.CAMERA_TOUCHSCREEN);
		checkAssignedValue(DigitalCameraMockImpl.YES, DigitalCameraMockImpl.CAMERA_TILTABLE);
		checkAssignedValue(DigitalCameraMockImpl.LEICA, DigitalCameraMockImpl.LENS_MANU);
		checkAssignedValue(DigitalCameraMockImpl.TELEPHOTO_ZOOM_100, DigitalCameraMockImpl.LENS_TYPE);
		checkAssignedValue(DigitalCameraMockImpl.IMAGE_STABILIZATION, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.WATERPROOF, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.MOVIE, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.WI_FI_NFC, DigitalCameraMockImpl.C_OPTIONS);
	}

	@Test
	public void testCreateDefaultConfigurationForVariantStdBlack()
	{
		classUnderTest = (DigitalCameraMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(
				DigitalCameraMockImpl.ROOT_INSTANCE_NAME, DigitalCameraMockImpl.VARIANT_CODE_CONF_CAMERA_SL_STD_BLACK);
		model = classUnderTest.createDefaultConfiguration();

		checkAssignedValue(DigitalCameraMockImpl.MODE_STANDARD, DigitalCameraMockImpl.CAMERA_MODE);
		checkAssignedValue(DigitalCameraMockImpl.BLACK, DigitalCameraMockImpl.CAMERA_COLOUR);
		checkAssignedValue(DigitalCameraMockImpl.P12, DigitalCameraMockImpl.CAMERA_PIXELS);
		checkAssignedValue(DigitalCameraMockImpl.COMPACT, DigitalCameraMockImpl.CAMERA_SENSOR);
		checkAssignedValue(DigitalCameraMockImpl.F28, DigitalCameraMockImpl.APERTURE);
		checkAssignedValue(DigitalCameraMockImpl.REAR_LCD_ONLY, DigitalCameraMockImpl.CAMERA_VIEWFINDER);
		checkAssignedValue(DigitalCameraMockImpl.SDHC, DigitalCameraMockImpl.CAMERA_SD_CARD);
		checkAssignedValue(DigitalCameraMockImpl.FORMAT_JPEG, DigitalCameraMockImpl.FORMAT_PICTURE);
		checkAssignedValue(DigitalCameraMockImpl.ISO_12800, DigitalCameraMockImpl.CAMERA_MAX_ISO);
		checkAssignedValue(DigitalCameraMockImpl.PIXELS5, DigitalCameraMockImpl.DISPLAY);
		checkAssignedValue(DigitalCameraMockImpl.NO, DigitalCameraMockImpl.CAMERA_TOUCHSCREEN);
		checkAssignedValue(DigitalCameraMockImpl.NO, DigitalCameraMockImpl.CAMERA_TILTABLE);
		checkAssignedValue(DigitalCameraMockImpl.CARL, DigitalCameraMockImpl.LENS_MANU);
		checkAssignedValue(DigitalCameraMockImpl.STANDARD_ZOOM, DigitalCameraMockImpl.LENS_TYPE);
		checkAssignedValue(DigitalCameraMockImpl.MOVIE, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.WI_FI_NFC, DigitalCameraMockImpl.C_OPTIONS);

	}

	@Test
	public void testCreateDefaultConfigurationForVariantStdMetallic()
	{
		classUnderTest = (DigitalCameraMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode(
				DigitalCameraMockImpl.ROOT_INSTANCE_NAME, DigitalCameraMockImpl.VARIANT_CODE_CONF_CAMERA_SL_STD_METALLIC);
		model = classUnderTest.createDefaultConfiguration();

		checkAssignedValue(DigitalCameraMockImpl.MODE_STANDARD, DigitalCameraMockImpl.CAMERA_MODE);
		checkAssignedValue(DigitalCameraMockImpl.WHITE, DigitalCameraMockImpl.CAMERA_COLOUR);
		checkAssignedValue(DigitalCameraMockImpl.P12, DigitalCameraMockImpl.CAMERA_PIXELS);
		checkAssignedValue(DigitalCameraMockImpl.COMPACT, DigitalCameraMockImpl.CAMERA_SENSOR);
		checkAssignedValue(DigitalCameraMockImpl.F35, DigitalCameraMockImpl.APERTURE);
		checkAssignedValue(DigitalCameraMockImpl.REAR_LCD_ONLY, DigitalCameraMockImpl.CAMERA_VIEWFINDER);
		checkAssignedValue(DigitalCameraMockImpl.SDHC, DigitalCameraMockImpl.CAMERA_SD_CARD);
		checkAssignedValue(DigitalCameraMockImpl.FORMAT_JPEG, DigitalCameraMockImpl.FORMAT_PICTURE);
		checkAssignedValue(DigitalCameraMockImpl.ISO_12800, DigitalCameraMockImpl.CAMERA_MAX_ISO);
		checkAssignedValue(DigitalCameraMockImpl.PIXELS5, DigitalCameraMockImpl.DISPLAY);
		checkAssignedValue(DigitalCameraMockImpl.NO, DigitalCameraMockImpl.CAMERA_TOUCHSCREEN);
		checkAssignedValue(DigitalCameraMockImpl.NO, DigitalCameraMockImpl.CAMERA_TILTABLE);
		checkAssignedValue(DigitalCameraMockImpl.CARL, DigitalCameraMockImpl.LENS_MANU);
		checkAssignedValue(DigitalCameraMockImpl.TELEPHOTO_ZOOM, DigitalCameraMockImpl.LENS_TYPE);
		checkAssignedValue(DigitalCameraMockImpl.MOVIE, DigitalCameraMockImpl.C_OPTIONS);
		checkAssignedValue(DigitalCameraMockImpl.WI_FI_NFC, DigitalCameraMockImpl.C_OPTIONS);

	}

	protected void checkAssignedValue(final String value, final String csticName)
	{
		final CsticModel cstic = model.getRootInstance().getCstic(csticName);
		assertNotNull("We expect a cstic for: " + csticName, cstic);
		final List<CsticValueModel> assignedValues = cstic.getAssignedValues();
		assertFalse("List of values must not be empty for " + csticName, assignedValues.isEmpty());
		assertTrue("We expect to find value " + value,
				assignedValues.stream().map(csticValue -> csticValue.getName()).anyMatch(name -> name.equals(value)));
	}

	protected void setModeStandard()
	{
		final CsticModel csticMode = model.getRootInstance().getCstic(DigitalCameraMockImpl.CAMERA_MODE);
		csticMode.setSingleValue(DigitalCameraMockImpl.MODE_STANDARD);
	}



	protected void setModeProf()
	{
		final CsticModel csticMode = model.getRootInstance().getCstic(DigitalCameraMockImpl.CAMERA_MODE);
		csticMode.setSingleValue(DigitalCameraMockImpl.MODE_PROF);
	}


}
