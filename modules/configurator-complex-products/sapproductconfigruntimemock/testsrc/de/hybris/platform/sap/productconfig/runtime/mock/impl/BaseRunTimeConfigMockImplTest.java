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
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class BaseRunTimeConfigMockImplTest
{
	private static final String FIRST_VALUE_NAME = "Value1";
	private static final String CSTIC_NAME = "Cstic";
	private static final String SECOND_VALUE_NAME = "Value2";
	private static final String THIRD_VALUE_NAME = "Value3";
	private static final String CSTIC_NAME_PRECONDITION = "CsticStatingPrecondition";
	private static final String VALUE_NAME_PRECONDITION = "ValuePartOfPrecondition";
	private static final String CONFLICT_TEXT = "This is a conflict";
	BaseRunTimeConfigMockImpl classUnderTest = new BaseRunTimeConfigMockImpl();
	private ConfigModel model;
	private CsticModel cstic;
	private CsticModel csticPrecondition;

	private BigDecimal obsoletePrice;

	private BigDecimal surcharge;
	private BigDecimal secondSurcharge;
	private BigDecimal thirdSurcharge;

	private CsticValueModel firstValue;
	private CsticValueModel secondValue;
	private CsticValueModel thirdValue;
	private CsticValueModel valuePartOfPrecondition;
	private InstanceModel instance;
	private final List<SolvableConflictModel> conflicts = new ArrayList<>();


	@Before
	public void initialize()
	{
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

		final CsticValueModelBuilder preconditionValueBuilder = new CsticValueModelBuilder().withName(VALUE_NAME_PRECONDITION,
				null);
		valuePartOfPrecondition = preconditionValueBuilder.build();
		final CsticModelBuilder preconditionCsticBuilder = new CsticModelBuilder().withName(CSTIC_NAME_PRECONDITION, null);
		preconditionCsticBuilder.addValue(valuePartOfPrecondition);
		csticPrecondition = preconditionCsticBuilder.build();

		instance = new InstanceModelImpl();
		instance.setCstics(Arrays.asList(cstic, csticPrecondition));

		model = new ConfigModelImpl();
		model.setCurrentTotalPrice(classUnderTest.createPrice(BigDecimal.ZERO));
		model.setSelectedOptionsPrice(classUnderTest.createPrice(BigDecimal.ZERO));
		model.setBasePrice(classUnderTest.createPrice(BigDecimal.ZERO));
		model.setRootInstance(instance);

		surcharge = BigDecimal.valueOf(123);
		secondSurcharge = BigDecimal.valueOf(200);
		thirdSurcharge = BigDecimal.valueOf(150);

		obsoletePrice = BigDecimal.valueOf(150);

		classUnderTest.showDeltaPrices(true);
	}

	@Test
	public void testCreateDefaultConfiguration()
	{
		assertNull(classUnderTest.createDefaultConfiguration());
	}

	@Test
	public void testHandleValuePriceCheckTotal()
	{
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		final BigDecimal total = model.getCurrentTotalPrice().getPriceValue();
		assertEquals("Total price does not match", surcharge, total);
	}

	@Test
	public void testHandleValuePriceCheckDeltaPrice()
	{
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		final PriceModel deltaPrice = firstValue.getDeltaPrice();
		assertFalse("Delta price should not be NO_PRICE", PriceModel.NO_PRICE.equals(deltaPrice));
		final BigDecimal delta = deltaPrice.getPriceValue();
		assertEquals("Delta price does not match", BigDecimal.ZERO, delta);
	}

	@Test
	public void testHandleValuePriceCheckForAssignedValue()
	{
		classUnderTest.showDeltaPrices(false);
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		final BigDecimal priceOptionValue = firstValue.getValuePrice().getPriceValue();
		assertEquals("Value price does not match", surcharge, priceOptionValue);
	}

	@Test
	public void testHandleValuePriceCheckFor2AssignableValues()
	{
		classUnderTest.showDeltaPrices(false);
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		classUnderTest.handleValuePrice(model, cstic, THIRD_VALUE_NAME, thirdSurcharge);
		final BigDecimal priceOptionValue = secondValue.getValuePrice().getPriceValue();
		assertEquals("Value price does not match", secondSurcharge, priceOptionValue);
	}


	@Test
	public void testHandleValuePriceCheckDeltaForAssignableValue()
	{
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		final BigDecimal deltaOptionValue = secondValue.getDeltaPrice().getPriceValue();
		assertEquals("Delta price does not match", secondSurcharge.subtract(surcharge), deltaOptionValue);
	}

	@Test
	public void testHandleValuePriceCheckDeltaFor2AssignableValues()
	{
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		classUnderTest.handleValuePrice(model, cstic, THIRD_VALUE_NAME, thirdSurcharge);
		final BigDecimal deltaOptionValue = secondValue.getDeltaPrice().getPriceValue();
		assertEquals("Delta price does not match", secondSurcharge.subtract(surcharge), deltaOptionValue);
	}

	@Test
	public void testResetValuePrices()
	{
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		classUnderTest.handleValuePrice(model, cstic, THIRD_VALUE_NAME, thirdSurcharge);
		classUnderTest.resetValuePrices(cstic);

		final BigDecimal priceOptionValue = secondValue.getValuePrice().getPriceValue();
		assertEquals("Value price does not match", BigDecimal.ZERO, priceOptionValue);
	}

	@Test
	public void testResetValuePricesDoesNotCreateNoPrice()
	{
		classUnderTest.handleValuePrice(model, cstic, FIRST_VALUE_NAME, surcharge);
		classUnderTest.handleValuePrice(model, cstic, SECOND_VALUE_NAME, secondSurcharge);
		classUnderTest.resetValuePrices(cstic);

		final PriceModel deltaPrice = firstValue.getDeltaPrice();
		assertFalse("Delta price should not be NO_PRICE", PriceModel.NO_PRICE.equals(deltaPrice));
		assertEquals("Delta price has value zero", BigDecimal.ZERO, deltaPrice.getPriceValue());
	}

	@Test
	public void testHandleValuePriceInCaseNotSelected()
	{
		//the matching non selected value needs to get a value price, and needs to have its delta price changed
		classUnderTest.handlePriceInCaseValueIsNotSelected(cstic, SECOND_VALUE_NAME, secondSurcharge, null);
		assertEquals(secondSurcharge, secondValue.getValuePrice().getPriceValue());
		assertEquals(secondSurcharge, secondValue.getDeltaPrice().getPriceValue());
	}

	@Test
	public void testHandleValuePriceInCaseNotSelectedOtherOption()
	{
		//other options should not been touched
		classUnderTest.handlePriceInCaseValueIsNotSelected(cstic, SECOND_VALUE_NAME, secondSurcharge, null);
		assertEquals(BigDecimal.ZERO, firstValue.getValuePrice().getPriceValue());
		assertEquals(BigDecimal.ZERO, firstValue.getDeltaPrice().getPriceValue());
	}

	@Test
	public void testHandleValuePriceInCaseSelected()
	{
		classUnderTest.handlePriceInCaseValueIsSelected(model, cstic, surcharge, FIRST_VALUE_NAME, null);
		assertEquals(surcharge, model.getSelectedOptionsPrice().getPriceValue());
		assertEquals(surcharge, model.getCurrentTotalPrice().getPriceValue());
		assertEquals(BigDecimal.ZERO, model.getBasePrice().getPriceValue());
	}

	@Test
	public void testHandleValueObsoletePriceInCaseSelected()
	{
		classUnderTest.showDeltaPrices(false);
		classUnderTest.handlePriceInCaseValueIsSelected(model, cstic, surcharge, FIRST_VALUE_NAME, obsoletePrice);
		assertEquals(obsoletePrice, firstValue.getValuePrice().getObsoletePriceValue());
	}

	@Test
	public void testHandleValueObsoletePriceInCaseNotSelected()
	{
		classUnderTest.showDeltaPrices(false);
		classUnderTest.handlePriceInCaseValueIsNotSelected(cstic, FIRST_VALUE_NAME, surcharge, obsoletePrice);
		assertEquals(obsoletePrice, firstValue.getValuePrice().getObsoletePriceValue());
	}

	@Test
	public void testHandleDeltaObsoletePriceInCaseSelected()
	{
		classUnderTest.showDeltaPrices(true);
		classUnderTest.handlePriceInCaseValueIsSelected(model, cstic, surcharge, FIRST_VALUE_NAME, obsoletePrice);
		assertEquals(obsoletePrice.subtract(surcharge), firstValue.getDeltaPrice().getObsoletePriceValue());
	}

	@Test
	public void testHandleDeltaObsoletePriceInCaseNotSelected()
	{
		classUnderTest.showDeltaPrices(true);
		classUnderTest.handlePriceInCaseValueIsNotSelected(cstic, FIRST_VALUE_NAME, surcharge, obsoletePrice);
		assertEquals(obsoletePrice.subtract(surcharge), firstValue.getDeltaPrice().getObsoletePriceValue());
	}

	@Test
	public void testHandleValuePriceInCaseSelectedReducesDeltaForUnselectedSingleValue()
	{
		classUnderTest.handlePriceInCaseValueIsSelected(model, cstic, surcharge, FIRST_VALUE_NAME, null);
		assertEquals(BigDecimal.ZERO.subtract(surcharge), secondValue.getDeltaPrice().getPriceValue());
	}

	@Test
	public void testHandleValuePriceInCaseSelectedDoesntReducesDeltaForUnselectedMultiValue()
	{
		cstic.setMultivalued(true);
		classUnderTest.handlePriceInCaseValueIsSelected(model, cstic, surcharge, FIRST_VALUE_NAME, null);
		assertEquals(BigDecimal.ZERO, secondValue.getDeltaPrice().getPriceValue());
	}

	@Test(expected = IllegalStateException.class)
	public void testHandleValuePriceInCaseSelectedNoMatchingAssignedValue()
	{
		classUnderTest.handlePriceInCaseValueIsSelected(model, cstic, surcharge, "Not existing", null);

	}

	@Test
	public void testCheckPreconditionViolatedPreconditionIsMatched()
	{
		classUnderTest.checkPreconditionViolated(instance, cstic, conflicts, CSTIC_NAME_PRECONDITION, VALUE_NAME_PRECONDITION,
				CONFLICT_TEXT);
		assertEquals(0, conflicts.size());
	}

	@Test
	public void testCheckPreconditionViolatedPreconditionIsNotMatched()
	{
		classUnderTest.checkPreconditionViolated(instance, cstic, conflicts, CSTIC_NAME_PRECONDITION,
				"ValueNotMatchingPreconditions", CONFLICT_TEXT);
		assertEquals(1, conflicts.size());
	}

	@Test(expected = IllegalStateException.class)
	public void testCheckPreconditionViolatedPreconditionCsticDoesNotExist()
	{
		classUnderTest.checkPreconditionViolated(instance, cstic, conflicts, "Not existing", VALUE_NAME_PRECONDITION,
				CONFLICT_TEXT);

	}

	@Test
	public void testCalculateTotalSavingsNoOldSavings()
	{
		model.setCurrentTotalSavings(null);
		final BigDecimal selectedSurcharge = BigDecimal.valueOf(100);
		// obsoletePrice is 150
		classUnderTest.calculateTotalSavings(model, selectedSurcharge, obsoletePrice);
		assertEquals(BigDecimal.valueOf(50), model.getCurrentTotalSavings().getPriceValue());
	}

	@Test
	public void testCalculateTotalSavings()
	{
		model.setCurrentTotalSavings(classUnderTest.createPrice(BigDecimal.valueOf(20)));
		final BigDecimal selectedSurcharge = BigDecimal.valueOf(100);
		// obsoletePrice is 150
		classUnderTest.calculateTotalSavings(model, selectedSurcharge, obsoletePrice);
		assertEquals(BigDecimal.valueOf(70), model.getCurrentTotalSavings().getPriceValue());
	}

	@Test
	public void testCalculateTotalSavingsNoDiscount()
	{
		model.setCurrentTotalSavings(classUnderTest.createPrice(BigDecimal.valueOf(20)));
		final BigDecimal selectedSurcharge = BigDecimal.valueOf(150);
		// obsoletePrice is 150
		classUnderTest.calculateTotalSavings(model, selectedSurcharge, obsoletePrice);
		assertEquals(BigDecimal.valueOf(20), model.getCurrentTotalSavings().getPriceValue());
	}

	@Test
	public void testCalculateTotalSavingsNoObsoletePrice()
	{
		model.setCurrentTotalSavings(classUnderTest.createPrice(BigDecimal.valueOf(20)));
		final BigDecimal selectedSurcharge = BigDecimal.valueOf(150);
		final BigDecimal obsoletePriceNull = null;
		classUnderTest.calculateTotalSavings(model, selectedSurcharge, obsoletePriceNull);
		assertEquals(BigDecimal.valueOf(20), model.getCurrentTotalSavings().getPriceValue());
	}

	@Test
	public void testCheckCsticKeepsAssignedValuesIfNoRetraction()
	{
		cstic.setRetractTriggered(false);
		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(1, cstic.getAssignedValues().size());
	}

	@Test
	public void testCheckCsticDeletedAssignedValuesdOnRetract()
	{
		cstic.setRetractTriggered(true);
		classUnderTest.checkCstic(model, instance, cstic);
		assertEquals(0, cstic.getAssignedValues().size());
		assertFalse(cstic.isRetractTriggered());
	}

}
