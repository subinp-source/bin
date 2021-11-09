/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineseprofileservices.strategies.impl.ChineseVerificationCodeGenerationStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ChineseVerificationCodeGenerationStrategyTest
{

	private ChineseVerificationCodeGenerationStrategy strategy;

	@Before
	public void prepare()
	{
		strategy = new ChineseVerificationCodeGenerationStrategy();
		strategy.setLength(4);
	}

	@Test
	public void test_generate()
	{
		final String code = strategy.generate();
		Assert.assertEquals(4, code.length());
	}
}
