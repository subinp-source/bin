/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.accountsettings.changepassword;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import de.hybris.platform.servicelayer.user.impl.DefaultPasswordPolicyViolation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;


@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordRendererUtilTest
{

	@Mock
	private ModelService modelService;

	@Mock
	private PasswordPolicyService passwordPolicyService;

	@Spy
	@InjectMocks
	private ChangePasswordRendererUtil rendererUtil;

	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void shouldCreateNoticeLine()
	{
		final Div container = new Div();
		final String labelKey = "lable.hi";
		rendererUtil.createNoticeLine(container, labelKey);
		final Div lineDiv = (Div) container.getFirstChild();
		assertThat(lineDiv.getChildren().size()).isEqualTo(1);
	}

	@Test
	public void shouldCreateValidationInfoLine()
	{
		final Div container = new Div();
		rendererUtil.createValidationInfoLine(container);
		assertThat(container.getChildren().size()).isEqualTo(1);
		final Div validationDiv = (Div) container.getFirstChild();
		assertThat(validationDiv.isVisible()).isFalse();
		assertThat(validationDiv.getChildren().size()).isEqualTo(0);
	}

	@Test
	public void shouldCreatePasswordLine()
	{
		final Div container = new Div();
		final String labelKey = "lable.newpassword";
		final Textbox passwordBox = rendererUtil.createPasswordLine(container, labelKey);
		assertThat(container.getChildren().size()).isEqualTo(1);
		final Div lineDiv = (Div) container.getFirstChild();
		assertThat(lineDiv.getChildren().size()).isEqualTo(2);
		final Textbox box = (Textbox) lineDiv.getLastChild();
		assertThat(box.getType()).isEqualTo("password");
		assertThat(box.isInstant()).isTrue();
		assertThat(box).isSameAs(passwordBox);
	}

	@Test
	public void shouldDisplayValidationWhenInvalidOldPassword() throws EJBPasswordEncoderNotFoundException
	{
		final Div container = new Div();
		rendererUtil.createValidationInfoLine(container);
		final UserModel user = new UserModel();
		final PasswordChangeData passwordChangeData = new PasswordChangeData();
		final String password = "invalid-old-password";
		final Textbox input = new Textbox(password);

		doReturn(false).when(rendererUtil).checkPassword(any(), any());

		rendererUtil.onOldPasswordTextChanged(input, passwordChangeData);
		rendererUtil.validateOldPassword(user, passwordChangeData);
		assertThat(passwordChangeData.getOldPassword()).isEqualTo(password);
		assertThat(passwordChangeData.getValidations().size()).isEqualTo(1);
		assertThat(rendererUtil.getValidationDiv().isVisible()).isTrue();
	}

	@Test
	public void shouldDisplayValidationWhenInvalidNewPassword()
	{
		final Div container = new Div();
		rendererUtil.createValidationInfoLine(container);
		final UserModel user = new UserModel();
		final PasswordChangeData passwordChangeData = new PasswordChangeData();
		final String password = "invalid-new-password";
		final Textbox input = new Textbox(password);
		final String violationMessage = "The password must contain at least one or more special characters.";
		final List<PasswordPolicyViolation> passwordPolicyViolations = new ArrayList<>();
		passwordPolicyViolations.add(new DefaultPasswordPolicyViolation("2020", violationMessage));

		doReturn(passwordPolicyViolations).when(passwordPolicyService).verifyPassword(eq(user), eq(password), anyString());

		rendererUtil.onNewPasswordTextChanged(user, input, passwordChangeData);
		assertThat(passwordChangeData.getNewPassword()).isEqualTo(password);
		assertThat(passwordChangeData.isValidationPassed()).isFalse();
		assertThat(passwordChangeData.getValidations().size()).isEqualTo(1);
		assertThat(passwordChangeData.getValidations().get(0)).isEqualTo(violationMessage);
		assertThat(rendererUtil.getValidationDiv().isVisible()).isTrue();
	}

	@Test
	public void shouldDisplayValidationWhenInvalidConfirmPassword()
	{
		final Div container = new Div();
		rendererUtil.createValidationInfoLine(container);
		final UserModel user = new UserModel();
		final PasswordChangeData passwordChangeData = new PasswordChangeData();
		passwordChangeData.setNewPassword("password-new");
		final String password = "password-confirm";
		final Textbox input = new Textbox(password);

		rendererUtil.onConfirmPwdTextChanged(user, input, passwordChangeData);

		assertThat(passwordChangeData.getConfirmPassword()).isEqualTo(password);
		assertThat(passwordChangeData.isValidationPassed()).isFalse();
		assertThat(passwordChangeData.getValidations().size()).isEqualTo(1);
		assertThat(rendererUtil.getValidationDiv().isVisible()).isTrue();
	}

	@Test
	public void shouldHideValidationWhenAllPasswordValid() throws EJBPasswordEncoderNotFoundException
	{
		final Div container = new Div();
		rendererUtil.createValidationInfoLine(container);
		final UserModel user = new UserModel();
		final PasswordChangeData passwordChangeData = new PasswordChangeData();
		final String password = "password2020";
		passwordChangeData.setNewPassword(password);
		final Textbox input = new Textbox(password);

		doReturn(new ArrayList<PasswordPolicyViolation>()).when(passwordPolicyService).verifyPassword(eq(user), eq(password),
				any());
		doReturn(true).when(rendererUtil).checkPassword(any(), any());

		rendererUtil.onConfirmPwdTextChanged(user, input, passwordChangeData);

		assertThat(passwordChangeData.isValidationPassed()).isTrue();
		assertThat(rendererUtil.getValidationDiv().isVisible()).isFalse();
	}
}
