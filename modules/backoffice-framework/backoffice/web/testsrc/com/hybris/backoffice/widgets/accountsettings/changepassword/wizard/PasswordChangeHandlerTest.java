/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.accountsettings.changepassword.wizard;

import static com.hybris.backoffice.widgets.accountsettings.changepassword.wizard.PasswordChangeConstants.NOTIFICATION_AREA;
import static com.hybris.backoffice.widgets.accountsettings.changepassword.wizard.PasswordChangeConstants.NOTIFICATION_TYPE_PASSWORD_CHANGED;
import static com.hybris.backoffice.widgets.accountsettings.changepassword.wizard.PasswordChangeConstants.PARAM_PASSWORD_CHANGE_DATA_MODEL_KEY;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.accountsettings.changepassword.ChangePasswordRendererUtil;
import com.hybris.backoffice.widgets.accountsettings.changepassword.PasswordChangeData;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


@RunWith(MockitoJUnitRunner.class)
public class PasswordChangeHandlerTest
{
	@Mock
	private UserService userService;

	@Mock
	private ModelService modelService;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ChangePasswordRendererUtil changePasswordRendererUtil;

	@Mock
	private FlowActionHandlerAdapter adapter;

	@Spy
	@InjectMocks
	private PasswordChangeHandler handler;

	@Mock
	private CustomType customType;

	@Mock
	private UserModel user;

	private PasswordChangeData passwordChangeData;

	private Map parameters;

	@Before
	public void setUp()
	{
		passwordChangeData = new PasswordChangeData();
		passwordChangeData.setConfirmPassword("valid-password");
		final String changePasswordDataValue = "passwordChangeData";
		parameters = new HashMap<String, String>();
		parameters.put(PARAM_PASSWORD_CHANGE_DATA_MODEL_KEY, changePasswordDataValue);
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);

		given(adapter.getWidgetInstanceManager()).willReturn(widgetInstanceManager);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(changePasswordDataValue, PasswordChangeData.class)).willReturn(passwordChangeData);
		given(userService.getCurrentUser()).willReturn(user);
	}

	@Test
	public void shouldChangeUserPasswordWhenValidOldPassword()
	{
		given(changePasswordRendererUtil.validateOldPassword(user, passwordChangeData)).willReturn(true);

		handler.perform(customType, adapter, parameters);

		verify(userService).setPassword(user, passwordChangeData.getConfirmPassword());
		verify(modelService).save(user);
		verify(notificationService).notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_PASSWORD_CHANGED,
				NotificationEvent.Level.SUCCESS);
		verify(adapter).done();
	}

	@Test
	public void shouldDoNothingWhenInvalidOldPassword()
	{
		given(changePasswordRendererUtil.validateOldPassword(user, passwordChangeData)).willReturn(false);

		handler.perform(customType, adapter, parameters);

		verify(userService, never()).setPassword(user, passwordChangeData.getConfirmPassword());
		verify(modelService, never()).save(user);
		verify(notificationService, never()).notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_PASSWORD_CHANGED,
				NotificationEvent.Level.SUCCESS);
		verify(adapter, never()).done();
	}

}
