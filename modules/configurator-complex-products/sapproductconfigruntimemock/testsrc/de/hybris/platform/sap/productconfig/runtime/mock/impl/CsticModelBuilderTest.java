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
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;

import java.util.List;

import org.fest.util.Collections;
import org.junit.Test;


@UnitTest
public class CsticModelBuilderTest
{
	private static final int NUM_SCALE_FLOAT = 2;
	private static final int TYPE_LENGTH = 5;
	private static final String CSTIC_NAME = "NAME";
	private static final String CSTIC_LANG_DEP_NAME = "Language Dep";
	private final CsticModelBuilder classUnderTest = new CsticModelBuilder();
	private final CsticValueModel value = new CsticValueModelBuilder().withName(CSTIC_NAME, CSTIC_LANG_DEP_NAME).build();
	private final List<CsticValueModel> values = Collections.list(value);

	@Test
	public void testNumericType()
	{
		final CsticModel numeric = classUnderTest.numericType(NUM_SCALE_FLOAT, TYPE_LENGTH).build();
		assertEquals(CsticModel.TYPE_FLOAT, numeric.getValueType());
		assertEquals(NUM_SCALE_FLOAT, numeric.getNumberScale());
	}


	@Test
	public void testWithValues()
	{
		final CsticModel withValues = classUnderTest.withValues(values).build();
		assertEquals(CsticModel.AUTHOR_DEFAULT, withValues.getAuthor());
		final List<CsticValueModel> assignedValues = withValues.getAssignedValues();
		assertEquals(1, assignedValues.size());
		assertEquals(CSTIC_NAME, assignedValues.get(0).getName());
	}

	@Test
	public void testAddSelectedOptionMulti()
	{
		final CsticModel multiWithOptions = classUnderTest.multiSelection().addSelectedOption(CSTIC_NAME, CSTIC_LANG_DEP_NAME)
				.build();
		checkAssignedAndAssignables(multiWithOptions);
	}

	@Test
	public void testAddSelectedOptionSingle()
	{
		final CsticModel multiWithOptions = classUnderTest.singleSelection().addSelectedOption(CSTIC_NAME, CSTIC_LANG_DEP_NAME)
				.build();
		checkAssignedAndAssignables(multiWithOptions);
	}

	protected void checkAssignedAndAssignables(final CsticModel multiWithOptions)
	{
		assertEquals(1, multiWithOptions.getAssignableValues().size());
		assertEquals(1, multiWithOptions.getAssignedValues().size());
	}

	@Test
	public void testTypeLength()
	{
		final CsticModel csticModel = classUnderTest.typeLength(TYPE_LENGTH).build();
		assertEquals(TYPE_LENGTH, csticModel.getTypeLength());
	}
	
	@Test
	public void testIncomplete()
	{
		assertTrue(classUnderTest.withDefaultUIState().build().isComplete());
		final CsticModel csticModel = classUnderTest.incomplete().build();
		assertFalse(csticModel.isComplete());
	}
	
	@Test
	public void testInconsistent()
	{
		assertTrue(classUnderTest.withDefaultUIState().build().isConsistent());
		final CsticModel csticModel = classUnderTest.inconsistent().build();
		assertFalse(csticModel.isConsistent());
	}

	@Test
	public void testNumericTypeInteger()
	{
		final CsticModel csticModel = classUnderTest.numericType(0, 3).build();
		assertEquals(CsticModel.TYPE_INTEGER, csticModel.getValueType());
		assertEquals(0, csticModel.getNumberScale());
		assertEquals(3, csticModel.getTypeLength());
	}

	@Test
	public void testNumericTypeFloat()
	{
		final CsticModel csticModel = classUnderTest.numericType(1, 3).build();
		assertEquals(CsticModel.TYPE_FLOAT, csticModel.getValueType());
		assertEquals(1, csticModel.getNumberScale());
		assertEquals(3, csticModel.getTypeLength());
	}



}
