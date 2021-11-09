/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
public class ConfigurationExpertModeFacadeImplTest
{

	@Mock
	private SessionService sessionService;
	@Mock
	private UserService userService;

	@Mock
	private UserModel user;

	@InjectMocks
	private ConfigurationExpertModeFacadeImpl classUnderTest;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testIsExpertModeAllowedForCurrentUserMemeberOfExpertModeGroup()
	{
		when(userService.getCurrentUser()).thenReturn(user);
		when(userService.getAllUserGroupsForUser(user)).thenReturn(getUserGroups(true));

		assertTrue(classUnderTest.isExpertModeAllowedForCurrentUser());
	}

	@Test
	public void testIsExpertModeAllowedForCurrentUserNotMemeberOfExpertModeGroup()
	{
		when(userService.getCurrentUser()).thenReturn(user);
		when(userService.getAllUserGroupsForUser(user)).thenReturn(getUserGroups(false));

		assertFalse(classUnderTest.isExpertModeAllowedForCurrentUser());
	}

	@Test
	public void testIsExpertModeActiveAttributeNotSet()
	{
		assertFalse(classUnderTest.isExpertModeActive());
	}

	@Test
	public void testIsExpertModeActivaAttributeValueTrue()
	{
		when(sessionService.getAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION)).thenReturn(Boolean.TRUE);
		Assert.assertTrue(classUnderTest.isExpertModeActive());
	}


	@Test
	public void testEnableExpertMode()
	{
		when(userService.getCurrentUser()).thenReturn(user);
		when(userService.getAllUserGroupsForUser(user)).thenReturn(getUserGroups(true));

		classUnderTest.enableExpertMode();

		verify(sessionService).setAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION, Boolean.TRUE);
	}

	@Test
	public void testEnableExpertModeNotAllowed()
	{
		when(userService.getCurrentUser()).thenReturn(user);
		when(userService.getAllUserGroupsForUser(user)).thenReturn(getUserGroups(false));

		classUnderTest.enableExpertMode();

		verify(sessionService, times(0)).setAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION, Boolean.TRUE);
	}

	@Test
	public void testDisableExpertMode()
	{
		when(userService.getCurrentUser()).thenReturn(user);
		when(userService.getAllUserGroupsForUser(user)).thenReturn(getUserGroups(true));

		classUnderTest.disableExpertMode();

		verify(sessionService).setAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION, Boolean.FALSE);
	}

	@Test
	public void testDisableExpertModeAllowed()
	{
		when(userService.getCurrentUser()).thenReturn(user);
		when(userService.getAllUserGroupsForUser(user)).thenReturn(getUserGroups(false));

		classUnderTest.disableExpertMode();

		verify(sessionService, times(0)).setAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION,
				Boolean.FALSE);
	}

	private Set<UserGroupModel> getUserGroups(final boolean withExpertModeGroup)
	{
		final Set<UserGroupModel> userGroups = new HashSet();

		final UserGroupModel group1 = new UserGroupModel();
		group1.setUid("group");
		userGroups.add(group1);
		if (withExpertModeGroup)
		{
			final UserGroupModel group2 = new UserGroupModel();
			group2.setUid(SapproductconfigservicesConstants.EXPERT_MODE_USER_GROUP);
			userGroups.add(group2);
		}

		return userGroups;
	}

}
