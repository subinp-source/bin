/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.actions;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.exceptions.DestinationNotFoundException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;

import java.net.SocketTimeoutException;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import com.hybris.cockpitng.util.notifications.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@UnitTest
public class TestConsumedDestinationUrlActionTest extends AbstractActionUnitTest<TestDestinationUrlAction>
{
	@Mock
	private ActionContext<AbstractDestinationModel> ctx;

   @Mock
	private DestinationService<ConsumedDestinationModel> destinationService;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ConsumedDestinationModel consumedDestinationModel;

	@InjectMocks
	private TestDestinationUrlAction action = new TestDestinationUrlAction();

	@Override
	public TestDestinationUrlAction getActionInstance()
	{
		return action;
	}

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(consumedDestinationModel.getId()).thenReturn("testing");
		when(ctx.getData()).thenReturn(consumedDestinationModel);
	}

	@Test
	public void pingConsumedDestinationWithSuccess() throws DestinationNotFoundException
	{
		doNothing().when(destinationService).testDestinationUrl(any());
		doNothing().when(notificationService).notifyUser(anyString(), anyString(), any(), any());
		assertEquals(ActionResult.SUCCESS, action.perform(ctx).getResultCode());
	}

	@Test
	public void pingConsumedDestinationWithNotFoundException() throws DestinationNotFoundException
	{
		doThrow(new DestinationNotFoundException("testing", new HttpClientErrorException(HttpStatus.NOT_FOUND))).when(destinationService).testDestinationUrl(any());
		doNothing().when(notificationService).notifyUser(anyString(), anyString(), any(), any());
		assertEquals(ActionResult.ERROR, action.perform(ctx).getResultCode());
	}

	@Test
	public void pingConsumedDestinationWithTimeOutException() throws DestinationNotFoundException
	{
		doThrow(new DestinationNotFoundException("testing", new SocketTimeoutException("time out"))).when(destinationService).testDestinationUrl(any());
		doNothing().when(notificationService).notifyUser(anyString(), anyString(), any(), any());
		assertEquals(ActionResult.ERROR, action.perform(ctx).getResultCode());
	}

	@Test
	public void pingConsumedDestinationWithServerException() throws DestinationNotFoundException
	{
		doThrow(new DestinationNotFoundException("testing", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))).when(destinationService).testDestinationUrl(any());
		doNothing().when(notificationService).notifyUser(anyString(), anyString(), any(), any());
		assertEquals(ActionResult.ERROR, action.perform(ctx).getResultCode());
	}


}
