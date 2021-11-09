/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationSavedCartCleanUpStrategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProductConfigurationSaveCartMethodHookTest
{
	@InjectMocks
	private ProductConfigurationSaveCartMethodHook classUnderTest;

	@Mock
	private ConfigurationSavedCartCleanUpStrategy cleanUpStrategy;

	private final CommerceSaveCartParameter parameters = new CommerceSaveCartParameter();

	private final CommerceSaveCartResult saveCartResult = new CommerceSaveCartResult();

	@Test
	public void testBeforeSaveCart() throws CommerceSaveCartException
	{
		classUnderTest.beforeSaveCart(parameters);
		verify(cleanUpStrategy).cleanUpCart();
	}

	@Test
	public void testAfterSaveCart() throws CommerceSaveCartException
	{
		//asserting that method exists
		classUnderTest.afterSaveCart(parameters, saveCartResult);
	}

}
