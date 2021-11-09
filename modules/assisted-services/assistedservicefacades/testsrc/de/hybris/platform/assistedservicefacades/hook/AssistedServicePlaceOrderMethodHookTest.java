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
package de.hybris.platform.assistedservicefacades.hook;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.utils.AssistedServiceSession;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class AssistedServicePlaceOrderMethodHookTest
{

	@Mock
	private AssistedServiceFacade assistedServiceFacade;

	@Mock
	private ModelService modelService;


	@InjectMocks
	private AssistedServicePlaceOrderMethodHook orderMethodHook = new AssistedServicePlaceOrderMethodHook();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void afterLoggedOutPlaceOrderTest() throws InvalidCartException
	{
		when(assistedServiceFacade.isAssistedServiceAgentLoggedIn()).thenReturn(false);
		orderMethodHook.afterPlaceOrder(null, null);
		verify(modelService, never()).save(any());
		verify(modelService, never()).refresh(any());
	}

	@Test
	public void afterPlaceOrderTest() throws InvalidCartException
	{
		final AssistedServiceSession asmSession = mock(AssistedServiceSession.class);
		final UserModel agent = mock(UserModel.class);

		when(assistedServiceFacade.isAssistedServiceAgentLoggedIn()).thenReturn(true);
		when(asmSession.getAgent()).thenReturn(agent);
		when(assistedServiceFacade.getAsmSession()).thenReturn(asmSession);
		final CommerceOrderResult orderResult = new CommerceOrderResult();
		final OrderModel order = new OrderModel();
		orderResult.setOrder(order);
		final CommerceCheckoutParameter checkoutParameter = new CommerceCheckoutParameter();

		doNothing().when(modelService).save(any());
		doNothing().when(modelService).refresh(any());

		orderMethodHook.afterPlaceOrder(checkoutParameter, orderResult);

		assertEquals(order.getPlacedBy(), agent);

		verify(modelService).save(order);
		verify(modelService).refresh(order);
	}
}
