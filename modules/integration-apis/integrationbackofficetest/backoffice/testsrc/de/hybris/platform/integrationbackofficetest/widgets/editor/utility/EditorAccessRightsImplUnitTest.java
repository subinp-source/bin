/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRightsImpl;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EditorAccessRightsImplUnitTest
{

	@InjectMocks
	private EditorAccessRightsImpl editorAccessRights;

	@Mock
	private UserService userService;
	@Mock
	private UserModel userModel;
	@Mock
	private PrincipalGroupModel role1;
	@Mock
	private PrincipalGroupModel role2;

	private List<String> adminRoles;
	private Set<PrincipalGroupModel> groups;

	@Before
	public void setup()
	{

		adminRoles = new ArrayList<>();
		editorAccessRights.setUserService(userService);

		groups = new HashSet<>();
		groups.add(role1);
		groups.add(role2);

		when(userService.getCurrentUser()).thenReturn(userModel);
		when(userModel.getGroups()).thenReturn(groups);
		when(role1.getUid()).thenReturn("Test1");
		when(role2.getUid()).thenReturn("Test2");

	}

	@Test
	public void isUserAdminTestTrue()
	{
		adminRoles.add("Test1");
		adminRoles.add("Test555");

		editorAccessRights.setAdminRoles(adminRoles);

		assertTrue(editorAccessRights.isUserAdmin());
	}

	@Test
	public void isUserAdminTestFalse()
	{
		adminRoles.add("Test99999");
		adminRoles.add("Test");

		editorAccessRights.setAdminRoles(adminRoles);

		assertFalse(editorAccessRights.isUserAdmin());
	}

	@Test
	public void isUserAdminTestNull()
	{
		editorAccessRights.setAdminRoles(null);

		assertFalse(editorAccessRights.isUserAdmin());
	}

	@Test
	public void isUserAdminTestEmptyAdmin()
	{
		editorAccessRights.setAdminRoles(adminRoles);

		assertFalse(editorAccessRights.isUserAdmin());
	}

	@Test
	public void isUserAdminTestEmptyCurrent()
	{
		adminRoles.add("Test1");
		adminRoles.add("Test555");
		Set<PrincipalGroupModel> emptyGroups = new HashSet<>();
		when(userModel.getGroups()).thenReturn(emptyGroups);

		assertFalse(editorAccessRights.isUserAdmin());
	}

}
