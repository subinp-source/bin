/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.users.populator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.UserData;
import de.hybris.platform.cmsfacades.users.services.CMSUserService;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UserModelToDataPopulatorTest
{
	private final String USER_UID = "userUID";
	private final Set<String> readableLanguages = new HashSet<>(Arrays.asList("en", "fr"));
	private final Set<String> writeableLanguages = new HashSet<>(Arrays.asList("fr", "de"));

	@Mock
	private UserModel userModel;

	@Mock
	private CMSUserService cmsUserService;

	@InjectMocks
	private UserModelToDataPopulator userModelToDataPopulator;

	@Before
	public void setUp()
	{
		when(userModel.getUid()).thenReturn(USER_UID);
		when(cmsUserService.getReadableLanguagesForUser(userModel)).thenReturn(readableLanguages);
		when(cmsUserService.getWriteableLanguagesForUser(userModel)).thenReturn(writeableLanguages);
	}

	@Test
	public void givenUserModel_WhenPopulateCalled_ItSuccessfullyPopulatesUserData()
	{
		// GIVEN
		final UserData userData = new UserData();

		// WHEN
		userModelToDataPopulator.populate(userModel, userData);

		// THEN
		assertThat(userData.getUid(), is(USER_UID));
		assertThat(userData.getReadableLanguages(), is(readableLanguages));
		assertThat(userData.getWriteableLanguages(), is(writeableLanguages));
	}
}
