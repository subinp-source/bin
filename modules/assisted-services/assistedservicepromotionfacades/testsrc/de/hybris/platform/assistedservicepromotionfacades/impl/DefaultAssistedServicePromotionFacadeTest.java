/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicepromotionfacades.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultAssistedServicePromotionFacadeTest extends ServicelayerTransactionalTest
{
	@Resource(name = "assistedServicePromotionFacade")
	DefaultAssistedServicePromotionFacade assistedServicePromotionFacade;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/import/test/promotionFacade.impex", "utf-8");
	}

	@Test
	public void getCSAPromotionsTest()
	{
		assertEquals(0, assistedServicePromotionFacade.getCSAPromotions(null).size());
		assertEquals(0, assistedServicePromotionFacade.getCSAPromotions("").size());
		assertEquals(2, assistedServicePromotionFacade.getCSAPromotions("product").size());

		assertEquals(0, assistedServicePromotionFacade.getCustomerPromotions(null).size());
		assertEquals(0, assistedServicePromotionFacade.getCustomerPromotions("").size());
		assertEquals(5, assistedServicePromotionFacade.getCustomerPromotions("product").size());
	}

	@Test
	public void getCSACouponsTest()
	{
		assertEquals(0, assistedServicePromotionFacade.getCSACoupons(null).size());
		assertEquals(0, assistedServicePromotionFacade.getCSACoupons("").size());
		assertEquals(2, assistedServicePromotionFacade.getCSACoupons("WINTER").size());
	}
}
