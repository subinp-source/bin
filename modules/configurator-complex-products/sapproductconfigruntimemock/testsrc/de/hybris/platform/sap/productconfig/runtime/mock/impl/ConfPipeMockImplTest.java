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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
public class ConfPipeMockImplTest
{
	private static final String PRODUCT_CODE_VARIANT = "CONF_PIPE-1-1-T";
	private ConfPipeMockImpl classUnderTest;
	private ConfigModel model;
	private InstanceModel instance;
	private InstanceModel baseSubInstance;

	@Before
	public void setUp()
	{
		classUnderTest = (ConfPipeMockImpl) new RunTimeConfigMockFactory().createConfigMockForProductCode("CONF_PIPE");
		model = classUnderTest.createDefaultConfiguration();
		classUnderTest.showDeltaPrices(false);
		instance = model.getRootInstance();
		
	}

	protected List<CsticValueModel> setAssignedValue(final String value)
	{
		final List<CsticValueModel> assignedValues = new ArrayList<>();
		final CsticValueModel csticValue = new CsticValueModelImpl();
		csticValue.setName(value);
		assignedValues.add(csticValue);

		return assignedValues;
	}

	@Test
	public void testCreateType()
	{
		CsticModel csticModel = classUnderTest.createType();
		assertNotNull(csticModel);
		assertEquals(ConfPipeMockImpl.TYPE_NAME, csticModel.getName());
	}
	
	@Test
	public void testCreateInnerDiameter()
	{
		CsticModel csticModel = classUnderTest.createInnerDiameter();
		assertNotNull(csticModel);
		assertEquals(ConfPipeMockImpl.INNER_DIA_NAME, csticModel.getName());
	}
	
	@Test
	public void testCreateLength()
	{
		CsticModel csticModel = classUnderTest.createLength();
		assertNotNull(csticModel);
		assertEquals(ConfPipeMockImpl.LENGTH_NAME, csticModel.getName());
	}
	
	@Test
	public void testCreateOuterDiameter()
	{
		CsticModel csticModel = classUnderTest.createOuterDiameter();
		assertNotNull(csticModel);
		assertEquals(ConfPipeMockImpl.OUTER_DIA_NAME, csticModel.getName());
	}
	
	@Test
	public void testIsChangeableVariant()
	{
		assertTrue(classUnderTest.isChangeabeleVariant());
	}
	
	@Test
	public void testSetVariantCode() {
		classUnderTest.setVariantCode(PRODUCT_CODE_VARIANT);
		assertEquals("1",classUnderTest.variantParams.get(ConfPipeMockImpl.OUTER_DIA_NAME));
		assertEquals("1",classUnderTest.variantParams.get(ConfPipeMockImpl.INNER_DIA_NAME));
		assertEquals("T",classUnderTest.variantParams.get(ConfPipeMockImpl.TYPE_NAME));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetVariantCodeWrongPattern() {
		classUnderTest.setVariantCode("NoVariant");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetVariantCodeWrongPatternToManyAttribs() {
		classUnderTest.setVariantCode(PRODUCT_CODE_VARIANT+"-2");
	}
	
	@Test
	public void testSetVariantCodeNull() {
		classUnderTest.setVariantCode(null);
		assertNull(classUnderTest.variantParams.get(ConfPipeMockImpl.OUTER_DIA_NAME));
	}	
}
