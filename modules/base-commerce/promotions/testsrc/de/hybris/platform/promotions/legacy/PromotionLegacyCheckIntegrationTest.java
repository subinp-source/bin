/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.promotions.legacy;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.util.legacy.LegacyException;
import de.hybris.platform.promotions.util.legacy.LegacyModeChecker;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;


@IntegrationTest
public class PromotionLegacyCheckIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private PromotionsService defaultPromotionsService;
	@Resource
	private LegacyModeChecker legacyModeChecker;

	@Before
	public void setUp()
	{
		legacyModeChecker.setLegacyModeEnabled(false);
	}

	@After
	public void after()
	{
		legacyModeChecker.setLegacyModeEnabled(true);
	}

	@Test(expected = LegacyException.class)
	public void shouldThrowLegacyException()
	{
		defaultPromotionsService.getDefaultPromotionGroup();
	}
}
