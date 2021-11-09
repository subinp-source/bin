/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.payment.populator;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentInfoData;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.commercefacades.order.data.PaymentModeData;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;


@UnitTest
public class ChinesePaymentModePopulatorTest
{
	private static String PSP_LOGO_URL = "testUrl";

	@Mock
	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;
	@Mock
	private ChinesePaymentService chinesePaymentService;

	private static final String PAYMENT_MODE_CODE = "testPaymentModeCode";

	private PaymentModeModel source;
	private PaymentModeData target;
	ChinesePaymentModePopulator chinesePaymentModePopulator = new ChinesePaymentModePopulator();

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		chinesePaymentModePopulator.setChinesePaymentServicesStrategy(chinesePaymentServicesStrategy);
		Mockito.when(chinesePaymentServicesStrategy.getPaymentService(PAYMENT_MODE_CODE)).thenReturn(chinesePaymentService);
		Mockito.when(chinesePaymentService.getPspLogoUrl()).thenReturn(PSP_LOGO_URL);
		source = new PaymentModeModel();
		source.setCode(PAYMENT_MODE_CODE);
		target = new PaymentModeData();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateSourceNull()
	{
		chinesePaymentModePopulator.populate(null, target);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetNull()
	{
		chinesePaymentModePopulator.populate(source, null);
	}

	@Test
	public void testPopulate()
	{
		chinesePaymentModePopulator.populate(source, target);
		Assert.assertTrue(PSP_LOGO_URL.equals(target.getPspLogoUrl()));

	}

	@Test
	public void testPopulate_handelException()
	{
		Mockito.when(chinesePaymentService.getPspLogoUrl()).thenReturn(null);
		chinesePaymentModePopulator.populate(source, target);
		assertEquals(StringUtils.EMPTY, target.getPspLogoUrl());
	}

}
