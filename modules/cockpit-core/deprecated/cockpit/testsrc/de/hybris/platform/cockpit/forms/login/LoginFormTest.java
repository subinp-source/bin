package de.hybris.platform.cockpit.forms.login;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class LoginFormTest
{

	private final LoginForm loginForm = new LoginForm();

	@Test
	public void getDefaultUsernameShouldAlwaysBeBlank()
	{
		assertThat(loginForm.getDefaultUsername()).isEmpty();
	}

	@Test
	public void getDefaultUserPasswordShouldAlwaysBeBlank()
	{
		assertThat(loginForm.getDefaultUserPassword()).isEmpty();
	}
}
