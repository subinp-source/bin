/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.user;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


@IntegrationTest
public class BackofficeUserServiceIntegrationTest extends ServicelayerTest
{

	private static final String CONFIG_KEY_LEGACY_ADMIN_CHECK_ENABLED = "backoffice.admincheck.legacy.enabled";

	@Resource(name = "backofficeUserService")
	private BackofficeUserService testSubject;

	@Resource
	private SessionService sessionService;

	@Resource
	private BackofficeRoleService backofficeRoleService;


	@Before
	public void setUp() throws Exception
	{
		importEssentialData();
		importTestData();
	}

	@Test
	public void isAdminShouldReturnTrueForAdmingroupUser()
	{
		final UserModel user = testSubject.getUserForUID("directadmin");
		testSubject.setCurrentUser(user);

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	@Test
	public void isAdminShouldReturnTrueForBackofficeadmingroupUser()
	{
		final UserModel user = testSubject.getUserForUID("directbackofficeadmin");
		testSubject.setCurrentUser(user);

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	@Test
	public void isAdminShouldReturnTrueForAdminSubgroupUser()
	{
		final UserModel user = testSubject.getUserForUID("indirectadmin");
		testSubject.setCurrentUser(user);

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	@Test
	public void isAdminShouldReturnTrueForBackofficeadminSubgroupUser()
	{
		final UserModel user = testSubject.getUserForUID("indirectbackofficeadmin");
		testSubject.setCurrentUser(user);

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	@Test
	public void isAdminShouldReturnTrueForActiveAdminrole()
	{
		final UserModel user = testSubject.getUserForUID("roleadmin");
		testSubject.setCurrentUser(user);
		backofficeRoleService.setActiveRole("backofficeadministratorrole");

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	@Test
	public void isAdminShouldReturnFalseForActiveUserrole()
	{
		final UserModel user = testSubject.getUserForUID("roleadmin");
		testSubject.setCurrentUser(user);
		backofficeRoleService.setActiveRole("backofficeuserrole");

		assertThat(testSubject.isAdmin(user)).isFalse();
	}

	@Test
	public void isAdminLegacyModeShouldReturnTrueForActiveUserrole()
	{
		final UserModel user = testSubject.getUserForUID("roleadmin");
		testSubject.setCurrentUser(user);
		backofficeRoleService.setActiveRole("backofficeuserrole");
		Config.setParameter(CONFIG_KEY_LEGACY_ADMIN_CHECK_ENABLED, "true");

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	@Test
	public void isAdminShouldUseLegacyModeForNonCurrentUser()
	{
		final UserModel user = testSubject.getUserForUID("roleadmin");
		testSubject.setCurrentUser(testSubject.getUserForUID("indirectadmin"));
		backofficeRoleService.setActiveRole("backofficeuserrole");

		assertThat(testSubject.isAdmin(user)).isTrue();
	}

	private void importEssentialData() throws ImpExException
	{
		final ImpExResource essentialGroups = new ClasspathImpExResource("/impex/essentialdataGroups.impex",
				StandardCharsets.UTF_8.name());
		final ImpExResource essentialAuditReport = new ClasspathImpExResource(
				"/impex/essentialdataCreateAuditWorkflowTemplate.impex", StandardCharsets.UTF_8.name());
		final ImpExResource essentialUsersAndGroups = new ClasspathImpExResource("/impex/essentialdataUsersAndGroups.impex",
				StandardCharsets.UTF_8.name());
		final ImpExResource projectUsersAndGroups = new ClasspathImpExResource("/impex/projectdataUsersAndGroups.impex",
				StandardCharsets.UTF_8.name());

		importData(essentialGroups);
		importData(essentialAuditReport);
		importData(essentialUsersAndGroups);
		importData(projectUsersAndGroups);
	}

	private void importTestData() throws ImpExException
	{
		final ImpExResource testUsersAndGroups = new ClasspathImpExResource("/test/userservice/usersAndGroups.impex",
				StandardCharsets.UTF_8.name());
		importData(testUsersAndGroups);
	}

}
