/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.actions;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import com.hybris.cockpitng.util.notifications.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;




@UnitTest
public class RegisterExposedDestinationActionTest extends AbstractActionUnitTest<RegisterExposedDestinationAction>
{
	private static final String TEST_API_NAME = "test name";
	private static final String TEST_MESSAGE_CONFIRM = "Are you sure you want to register [{0}] API on Kyma?";

	@Mock
	private ActionContext<ExposedDestinationModel> ctx;

	@Mock
	private ApiRegistrationService apiRegistrationService;

	@Mock
	private ExposedDestinationModel destinationModel;

	@Mock
	private NotificationService notificationService;

	@Mock
	private CockpitGlobalEventPublisher eventPublisher;

	@InjectMocks
	private RegisterExposedDestinationAction action = new RegisterExposedDestinationAction();

	@Override
	public RegisterExposedDestinationAction getActionInstance()
	{
		return action;
	}

	@Before
	public void setUp() throws ApiRegistrationException
	{
		MockitoAnnotations.initMocks(this);

		doNothing().when(apiRegistrationService).registerExposedDestination(any());

		when(destinationModel.getId()).thenReturn(TEST_API_NAME);
		when(destinationModel.isActive()).thenReturn(true);

		when(ctx.getData()).thenReturn(destinationModel);
		when(ctx.getLabel(any(), any())).thenReturn(TEST_MESSAGE_CONFIRM);
	}

	@Test
	public void testCannotPerformWithNull() throws ApiRegistrationException
	{
		when(ctx.getData()).thenReturn(null);

		assertThat(action.canPerform(ctx)).isFalse();
		verify(apiRegistrationService, never()).registerExposedDestination(null);
	}


	@Test
	public void testCannotPerformWithExportFlagFalse() throws ApiRegistrationException
	{
		when(destinationModel.isActive()).thenReturn(false);

		assertThat(action.canPerform(ctx)).isFalse();
		verify(apiRegistrationService, never()).registerExposedDestination(null);
	}

	@Test
	public void testWebserviceRegistration() throws ApiRegistrationException
	{
		when(destinationModel.getAdditionalProperties()).thenReturn(ImmutableMap.of("type", "web"));
		doReturn(new DestinationTargetModel()).when(destinationModel).getDestinationTarget();
		assertThat(action.canPerform(ctx)).isTrue();
		assertEquals(ActionResult.SUCCESS, action.perform(ctx).getResultCode());
		verify(apiRegistrationService, times(1)).registerExposedDestination(destinationModel);
	}

	@Test
	public void testEventsRegistration() throws ApiRegistrationException
	{
		when(destinationModel.getAdditionalProperties()).thenReturn(ImmutableMap.of("type", "events"));
		doReturn(new DestinationTargetModel()).when(destinationModel).getDestinationTarget();
		assertThat(action.canPerform(ctx)).isTrue();
		assertEquals(ActionResult.SUCCESS, action.perform(ctx).getResultCode());
		verify(apiRegistrationService, times(1)).registerExposedDestination(destinationModel);
	}

	@Test
	public void testConformationMessage()
	{
		assertEquals(TEST_MESSAGE_CONFIRM, action.getConfirmationMessage(ctx));
	}

	@Test
	public void testExposedDestinationRegisteringWithException() throws ApiRegistrationException
	{
		doReturn(new DestinationTargetModel()).when(destinationModel).getDestinationTarget();
	   doThrow(new ApiRegistrationException("testing", new HttpClientErrorException(HttpStatus.NOT_FOUND))).when(apiRegistrationService).registerExposedDestination(any());
		doNothing().when(notificationService).notifyUser(anyString(), anyString(), any(), any());
		assertThat(action.canPerform(ctx)).isTrue();
		assertEquals(ActionResult.ERROR, action.perform(ctx).getResultCode());
	}

}
