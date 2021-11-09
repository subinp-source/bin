/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies.impl;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineseprofileservices.sms.ChineseSmsService;
import de.hybris.platform.chineseprofileservices.strategies.impl.ChineseVerificationCodeSendingStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ChineseVerificationCodeSendingStrategyTest
{

	@Mock
	private ChineseSmsService chineseSmsService;

	private ChineseVerificationCodeSendingStrategy strategy;

	private String mobileNumber = "13812345678";

	private String msg = "8596";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		strategy = new ChineseVerificationCodeSendingStrategy();
		strategy.setChineseSmsService(chineseSmsService);
	}

	@Test
	public void test_send()
	{
		strategy.send(mobileNumber, msg);
		verify(chineseSmsService).sendMsg(mobileNumber, msg);
	}
}
