/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.accountsettings;

import static com.hybris.backoffice.widgets.accountsettings.AccountSettingsController.COMP_ID_ACCOUNT_SETTINGS_BUTTON;
import static com.hybris.backoffice.widgets.accountsettings.AccountSettingsController.CONTEXT_COMPONENT_CHANGE_PASSWORD;
import static com.hybris.backoffice.widgets.accountsettings.AccountSettingsController.CONTEXT_TYPE_CODE;
import static com.hybris.backoffice.widgets.accountsettings.AccountSettingsController.ENABLE_CHANGE_PASSWORD_SETTING;
import static com.hybris.backoffice.widgets.accountsettings.AccountSettingsController.SOCKET_ACCOUNT_SETTINGS_OUTPUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredViewEvent;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;


@DeclaredViewEvent(eventName = Events.ON_CLICK, componentID = AccountSettingsController.COMP_ID_ACCOUNT_SETTINGS_BUTTON)
@ExtensibleWidget(level = ExtensibleWidget.ALL)
public class AccountSettingsControllerTest extends AbstractWidgetUnitTest<AccountSettingsController>
{

	@Spy
	@InjectMocks
	private AccountSettingsController controller;

	@Test
	public void testShowOpenBtn()
	{
		doReturn(true).when(controller).isChangePasswordEnabled();
		controller.preInitialize(new Div());
		verify(controller.getWidgetSettings()).put(ENABLE_CHANGE_PASSWORD_SETTING, true);
	}

	@Test
	public void testHideOpenBtn()
	{
		doReturn(false).when(controller).isChangePasswordEnabled();
		controller.preInitialize(new Div());
		verify(controller.getWidgetSettings()).put(ENABLE_CHANGE_PASSWORD_SETTING, false);
	}

	@Test
	public void shouldSendOutputToWizard()
	{
		executeViewEvent(COMP_ID_ACCOUNT_SETTINGS_BUTTON, Events.ON_CLICK);
		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);
		verify(widgetInstanceManager).sendOutput(eq(SOCKET_ACCOUNT_SETTINGS_OUTPUT), argumentCaptor.capture());
		final Map<String, Object> outputCtx = argumentCaptor.getValue();
		assertThat(outputCtx).containsKeys(ConfigurableFlowContextParameterNames.TYPE_CODE.getName());
		assertThat(outputCtx.get(ConfigurableFlowContextParameterNames.TYPE_CODE.getName())).isEqualTo(CONTEXT_TYPE_CODE);
		assertThat(outputCtx).containsKeys(ConfigurableFlowController.SETTING_CONFIGURABLE_FLOW_CONFIG_CTX);
		assertThat(outputCtx.get(ConfigurableFlowController.SETTING_CONFIGURABLE_FLOW_CONFIG_CTX))
				.isEqualTo(CONTEXT_COMPONENT_CHANGE_PASSWORD);
	}

	@Override
	protected AccountSettingsController getWidgetController()
	{
		return controller;
	}
}
