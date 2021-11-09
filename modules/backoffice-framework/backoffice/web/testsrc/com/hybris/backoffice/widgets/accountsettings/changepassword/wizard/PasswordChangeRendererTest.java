/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.accountsettings.changepassword.wizard;

import static com.hybris.backoffice.widgets.accountsettings.changepassword.wizard.PasswordChangeConstants.PARAM_PASSWORD_CHANGE_DATA_MODEL_KEY;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.platform.core.model.user.UserModel;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.hybris.backoffice.widgets.accountsettings.changepassword.ChangePasswordRendererUtil;
import com.hybris.backoffice.widgets.accountsettings.changepassword.PasswordChangeData;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@RunWith(MockitoJUnitRunner.class)
public class PasswordChangeRendererTest
{
	@Mock
	private WidgetInstanceManager widgetInstanceManager;

	@Mock
	private UserService userService;

	@Mock
	private ChangePasswordRendererUtil changePasswordRendererUtil;

	@Spy
	@InjectMocks
	private PasswordChangeRenderer renderer;

	private Component parent;
	private UserModel user;
	private PasswordChangeData passwordChangeData;
	private Textbox oldInput;
	private Textbox newInput;
	private Textbox confirmInput;
	private Map parameters;

	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
		parent = new Div();
		user = new UserModel();
		passwordChangeData = new PasswordChangeData();
		oldInput = new Textbox();
		newInput = new Textbox();
		confirmInput = new Textbox();
		final String changePasswordDataValue = "passwordChangeData";
		final ViewType viewType = mock(ViewType.class);
		final DataType dataType = mock(DataType.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(widgetModel.getValue(changePasswordDataValue, PasswordChangeData.class)).willReturn(passwordChangeData);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(userService.getCurrentUser()).willReturn(user);
		given(changePasswordRendererUtil.createPasswordLine(any(), anyString())).willReturn(oldInput, newInput, confirmInput);

		parameters = new HashMap<String, String>();
		parameters.put(PARAM_PASSWORD_CHANGE_DATA_MODEL_KEY, changePasswordDataValue);
		renderer.render(parent, viewType, parameters, dataType, widgetInstanceManager);
	}

	@Test
	public void shouldCheckPasswordWhenOldPasswordChanged()
	{
		CockpitTestUtil.simulateEvent(oldInput, Events.ON_CHANGE, null);
		verify(changePasswordRendererUtil).onOldPasswordTextChanged(eq(oldInput), eq(passwordChangeData));
	}

	@Test
	public void shouldCheckPasswordWhenNewPasswordChanged()
	{
		CockpitTestUtil.simulateEvent(newInput, Events.ON_CHANGE, null);
		verify(changePasswordRendererUtil).onNewPasswordTextChanged(eq(user), eq(newInput), eq(passwordChangeData));
	}

	@Test
	public void shouldCheckPasswordWhenConfirmPasswordChanged()
	{
		CockpitTestUtil.simulateEvent(confirmInput, Events.ON_CHANGE, null);
		verify(changePasswordRendererUtil).onConfirmPwdTextChanged(eq(user), eq(confirmInput), eq(passwordChangeData));
	}

}
