/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cockpit.services.config.UIRole;

import java.util.List;

import org.junit.Test;


@IntegrationTest
public class UIConfigurationServiceTest extends UIConfigurationTestBase
{

	@Test
	public void testGetPossibleRolesWithoutUser()
	{
		final List<UIRole> roles = uiConfigurationService.getPossibleRoles();
		assertNotNull("Possible roles", roles);
		assertEquals("Possible roles collection size", 1, roles.size());
	}


	@Test
	public void testGetPossibleRolesWithUser()
	{
		final List<UIRole> roles = uiConfigurationService.getPossibleRoles(productManagerUser);
		assertNotNull("Possible roles", roles);
		assertEquals("Possible roles collection size", 1, roles.size());
	}

	@Test
	public void testGetSessionRoleFromPossibleRols()
	{
		uiConfigurationService.setSessionRole((UIRole) null);
		final UIRole role = uiConfigurationService.getSessionRole();
		assertNotNull("Role", role);
		final UIRole firstPossibleRole = uiConfigurationService.getPossibleRoles().get(0);
		assertEquals("Roles", firstPossibleRole, role);
	}

}
