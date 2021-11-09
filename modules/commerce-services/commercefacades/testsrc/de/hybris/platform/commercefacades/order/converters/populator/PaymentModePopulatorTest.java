/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.PaymentModeData;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class PaymentModePopulatorTest
{
	private static final String PAYMENT_MODE_CODE = "testCode";
	private static final String PAYMENT_MODE_NAME = "testName";
	private static final String PAYMENT_MODE_DESCRIPTION = "testDescription";

	@Mock
	PaymentModeModel paymentModeModel;

	private final PaymentModePopulator paymentModePopulator = new PaymentModePopulator();



	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		given(paymentModeModel.getCode()).willReturn(PAYMENT_MODE_CODE);
		given(paymentModeModel.getName()).willReturn(PAYMENT_MODE_NAME);
		given(paymentModeModel.getDescription()).willReturn(PAYMENT_MODE_DESCRIPTION);

	}

	@Test
	public void testConvert()
	{
		final PaymentModeData paymentModeData = new PaymentModeData();
		paymentModePopulator.populate(paymentModeModel, paymentModeData);
		Assert.assertEquals(PAYMENT_MODE_CODE, paymentModeData.getCode());
		Assert.assertEquals(PAYMENT_MODE_NAME, paymentModeData.getName());
		Assert.assertEquals(PAYMENT_MODE_DESCRIPTION, paymentModeData.getDescription());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSourceNull()
	{
		paymentModePopulator.populate(null, mock(PaymentModeData.class));
		Assert.fail(" IllegalArgumentException should be thrown. ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testtargetNull()
	{
		paymentModePopulator.populate(paymentModeModel, null);
		Assert.fail(" IllegalArgumentException should be thrown. ");
	}
}
