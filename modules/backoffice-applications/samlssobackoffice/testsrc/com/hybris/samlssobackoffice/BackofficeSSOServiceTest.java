/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.samlssobackoffice;

import static com.google.common.collect.Sets.newHashSet;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.FALSE;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.SSO_PROPERTY_PREFIX;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.SSO_USER_GROUP;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.SSO_USER_GROUP_2;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.SSO_USER_ID;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.SSO_USER_NAME;
import static de.hybris.platform.samlsinglesignon.SSOServiceTestConstants.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.model.SamlUserGroupModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class BackofficeSSOServiceTest extends ServicelayerTransactionalTest
{
	private static final String SSO_DATABASE_USERGROUP_MAPPING = "sso.database.usergroup.mapping";

	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Resource
	private TypeService typeService;

	@Resource
	private BackofficeSSOService ssoUserService;

	private TypeModel employeeType;
	private UserGroupModel employeeGroup;
	private UserGroupModel adminGroup;

	@Before
	public void setup()
	{
		employeeType = typeService.getTypeForCode(EmployeeModel._TYPECODE);
		employeeGroup = userService.getUserGroupForUID("employeegroup");
		adminGroup = userService.getUserGroupForUID("admingroup");
	}

	@Test
	public void shouldEnableBackofficeLoginWhenPropertyMappingEnablesIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, FALSE);
		createMappingInProperties(SSO_USER_GROUP, EmployeeModel._TYPECODE, employeeGroup.getUid(), TRUE);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP));

		// then
		assertSsoUser(ssoUser, SSO_USER_ID, SSO_USER_NAME, newHashSet(employeeGroup), false, EmployeeModel._TYPECODE);
	}

	@Test
	public void shouldEnableBackofficeLoginIfAnyOfThePropertyMappingsEnableIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, FALSE);
		createMappingInProperties(SSO_USER_GROUP, EmployeeModel._TYPECODE, employeeGroup.getUid(), FALSE);
		createMappingInProperties(SSO_USER_GROUP_2, EmployeeModel._TYPECODE, adminGroup.getUid(), TRUE);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP, SSO_USER_GROUP_2));

		// then
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isFalse();
	}

	@Test
	public void shouldDisallowAccessWhenPropertyMappingIsNotSet()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, FALSE);
		Config.setParameter(SSO_PROPERTY_PREFIX + SSO_USER_GROUP + ".usertype", EmployeeModel._TYPECODE);
		Config.setParameter(SSO_PROPERTY_PREFIX + SSO_USER_GROUP + ".groups", employeeGroup.getUid());

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP));

		// then
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isTrue();
	}

	@Test
	public void shouldDisallowAccessWhenPropertyMappingDisablesIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, FALSE);
		createMappingInProperties(SSO_USER_GROUP, EmployeeModel._TYPECODE, employeeGroup.getUid(), FALSE);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP));

		// then
		assertSsoUser(ssoUser, SSO_USER_ID, SSO_USER_NAME, newHashSet(employeeGroup), true, EmployeeModel._TYPECODE);
	}


	@Test
	public void shouldEnableBackofficeLoginIfMappingInDatabaseEnablesIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, TRUE);
		createMappingInDatabase(SSO_USER_GROUP, employeeType, newHashSet(employeeGroup), true);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP));

		// then
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isFalse();
	}

	@Test
	public void shouldEnableBackofficeLoginIfAnyOfTheMappingsInDatabaseEnableIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, TRUE);
		createMappingInDatabase(SSO_USER_GROUP, employeeType, newHashSet(employeeGroup), true);
		createMappingInDatabase(SSO_USER_GROUP_2, employeeType, newHashSet(employeeGroup), false);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME,
				newHashSet(SSO_USER_GROUP, SSO_USER_GROUP_2));

		// then
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isFalse();
	}

	@Test
	public void shouldDisallowAccessWhenMappingInDatabaseDoesntEnableIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, TRUE);
		createMappingInDatabase(SSO_USER_GROUP, employeeType, newHashSet(employeeGroup), false);
		createMappingInDatabase(SSO_USER_GROUP_2, employeeType, newHashSet(employeeGroup), false);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP, SSO_USER_GROUP_2));

		// then
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isTrue();
	}

	@Test
	public void shouldDisallowAccessWhenNoneOfTheMappingsInDatabaseEnableIt()
	{
		// given
		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, TRUE);
		createMappingInDatabase(SSO_USER_GROUP, employeeType, newHashSet(employeeGroup), false);
		createMappingInDatabase(SSO_USER_GROUP_2, employeeType, newHashSet(employeeGroup), false);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP, SSO_USER_GROUP_2));

		// then
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isTrue();
	}

	@Test
	public void shouldNotChangeBackofficeLoginDisabledIfUserHasItAlreadySet()
	{
		//given
		final UserModel user = modelService.create(EmployeeModel._TYPECODE);
		user.setUid(SSO_USER_ID);
		user.setName(SSO_USER_NAME);
		user.setBackOfficeLoginDisabled(true);
		modelService.save(user);

		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, FALSE);
		createMappingInProperties(SSO_USER_GROUP, EmployeeModel._TYPECODE, employeeGroup.getUid(), TRUE);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP));

		// then
		assertSsoUser(ssoUser, SSO_USER_ID, SSO_USER_NAME, newHashSet(employeeGroup), true, EmployeeModel._TYPECODE);
	}

	@Test
	public void shouldChangeBackofficeLoginDisabledIfUserDoesntHaveItSet()
	{
		//given
		final UserModel user = modelService.create(EmployeeModel._TYPECODE);
		user.setUid(SSO_USER_ID);
		user.setName(SSO_USER_NAME);
		user.setBackOfficeLoginDisabled(null);
		modelService.save(user);

		Config.setParameter(SSO_DATABASE_USERGROUP_MAPPING, FALSE);
		createMappingInProperties(SSO_USER_GROUP, EmployeeModel._TYPECODE, employeeGroup.getUid(), TRUE);

		// when
		final UserModel ssoUser = ssoUserService.getOrCreateSSOUser(SSO_USER_ID, SSO_USER_NAME, newHashSet(SSO_USER_GROUP));

		// then
		assertSsoUser(ssoUser, SSO_USER_ID, SSO_USER_NAME, newHashSet(employeeGroup), false, EmployeeModel._TYPECODE);
	}


	private void createMappingInProperties(final String ssoUserGroup, final String userType, final String groups,
			final String enableBackofficeLogin)
	{
		Config.setParameter(SSO_PROPERTY_PREFIX + ssoUserGroup + ".usertype", userType);
		Config.setParameter(SSO_PROPERTY_PREFIX + ssoUserGroup + ".groups", groups);
		Config.setParameter(SSO_PROPERTY_PREFIX + ssoUserGroup + ".enableBackofficeLogin", enableBackofficeLogin);
	}

	private void createMappingInDatabase(final String ssoUserGroup, final TypeModel userType,
			final Set<UserGroupModel> userGroups, final boolean enableBackofficeLogin)
	{
		final SamlUserGroupModel samlUserGroupModel = new SamlUserGroupModel();
		samlUserGroupModel.setSamlUserGroup(ssoUserGroup);
		samlUserGroupModel.setUserType(userType);
		samlUserGroupModel.setUserGroups(userGroups);
		samlUserGroupModel.setEnableBackofficeLogin(enableBackofficeLogin);

		modelService.save(samlUserGroupModel);
	}

	private void assertSsoUser(final UserModel ssoUser, final String ssoUserId, final String ssoUserName,
			final Collection<UserGroupModel> userGroups, final Boolean backofficeLoginDisabled, final String typecode)
	{
		assertThat(ssoUser).isNotNull();
		assertThat(ssoUser.getUid()).isEqualTo(ssoUserId);
		assertThat(ssoUser.getName()).isEqualTo(ssoUserName);
		assertThat(ssoUser.getGroups()).containsOnlyElementsOf(userGroups);
		assertThat(ssoUser.getBackOfficeLoginDisabled()).isEqualTo(backofficeLoginDisabled);
		assertThat(ssoUser.getItemtype()).isEqualTo(typecode);
	}

}
