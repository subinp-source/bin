/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.hybris.backoffice.model.user.BackofficeRoleModel;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeRoleServiceTest
{

	private static final String SESSION_ATTRIBUTE_ACTIVE_AUTHORITY_GROUP_ID = "cockpitActiveAuthorityGroupId";

	@InjectMocks
	private final BackofficeRoleService testSubject = new DefaultBackofficeRoleService();

	@Mock
	private PrincipalModel user;
	@Mock
	private SessionService sessionService;
	@Mock
	private UserService userService;


	@Test
	public void shouldTreatFullHierarchyOfActiveRoleAsRolePrincipalsHierarchy()
	{
		// given
		final BackofficeRoleModel activeRole = mock(BackofficeRoleModel.class);
		final UserGroupModel activeRoleUserGroup = mock(UserGroupModel.class);
		final BackofficeRoleModel inactiveRole = mock(BackofficeRoleModel.class);
		final UserGroupModel inactiveRoleUserGroup = mock(UserGroupModel.class);

		final Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy = new HashMap<>();
		hierarchy.put(user, Collections.singleton(activeRole));
		hierarchy.put(activeRole, Collections.singleton(activeRoleUserGroup));
		hierarchy.put(activeRoleUserGroup, Collections.singleton(inactiveRole));
		hierarchy.put(inactiveRole, Collections.singleton(inactiveRoleUserGroup));
		mockHierarchy(hierarchy);

		final String UID = "role";
		when(sessionService.getAttribute(SESSION_ATTRIBUTE_ACTIVE_AUTHORITY_GROUP_ID)).thenReturn(UID);
		when(userService.getUserGroupForUID(UID)).thenReturn(activeRole);

		// when
		final Collection<PrincipalGroupModel> activeRolePrincipalsHierarchy = testSubject.getActiveRolePrincipalsHierarchy();

		// then
		assertThat(activeRolePrincipalsHierarchy).containsOnly(activeRole, activeRoleUserGroup, inactiveRole,
				inactiveRoleUserGroup);
	}

	@Test
	public void shouldTreatEmptyCollectionAsRolePrincipalsHierarchyWhenNoActiveRole()
	{
		// given
		final BackofficeRoleModel inactiveRole = mock(BackofficeRoleModel.class);
		final UserGroupModel inactiveRoleUserGroup = mock(UserGroupModel.class);
		final UserGroupModel userGroup = mock(UserGroupModel.class);

		final Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy = new HashMap<>();
		hierarchy.put(user, Sets.newHashSet(userGroup, inactiveRole));
		hierarchy.put(inactiveRole, Collections.singleton(inactiveRoleUserGroup));
		mockHierarchy(hierarchy);

		// when
		final Collection<PrincipalGroupModel> activeRolePrincipalsHierarchy = testSubject.getActiveRolePrincipalsHierarchy();

		// then
		assertThat(activeRolePrincipalsHierarchy).isEmpty();
	}

	@Test
	public void shouldIgnoreRolesWithTheirHierarchyInNonRoleHierarchy()
	{
		// given
		final BackofficeRoleModel inactiveRole = mock(BackofficeRoleModel.class);
		final UserGroupModel inactiveRoleUserGroup = mock(UserGroupModel.class);
		final UserGroupModel userGroup = mock(UserGroupModel.class);

		final Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy = new HashMap<>();
		hierarchy.put(user, Sets.newHashSet(userGroup, inactiveRole));
		hierarchy.put(inactiveRole, Collections.singleton(inactiveRoleUserGroup));
		mockHierarchy(hierarchy);

		// when
		final Collection<PrincipalModel> nonRolePrincipalsHierarchy = testSubject.getNonRolePrincipalsHierarchy(user);

		// then
		assertThat(nonRolePrincipalsHierarchy).containsOnly(user, userGroup);
	}

	@Test
	public void shouldFilterOutRoles()
	{
		// given
		final BackofficeRoleModel role1 = mock(BackofficeRoleModel.class);
		final BackofficeRoleModel role2 = mock(BackofficeRoleModel.class);
		final UserGroupModel group1 = mock(UserGroupModel.class);
		final UserGroupModel group2 = mock(UserGroupModel.class);

		// when
		final Collection<PrincipalGroupModel> result = testSubject
				.filterOutRolePrincipals(Sets.newHashSet(role1, group1, role2, group2));

		// then
		assertThat(result).containsOnly(group1, group2);
	}

	protected void mockHierarchy(final Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy)
	{
		mockHierarchy(user, hierarchy);
	}

	protected void mockHierarchy(final PrincipalModel root, final Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy)
	{
		when(root.getGroups()).thenReturn(hierarchy.get(root));
		when(root.getAllGroups()).thenReturn(hierarchy.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
		final Map<PrincipalModel, Set<PrincipalGroupModel>> subHierarchy = new HashMap<>(hierarchy);
		final Set<PrincipalGroupModel> groups = subHierarchy.remove(root);
		if (CollectionUtils.isNotEmpty(groups))
		{
			groups.forEach(group -> mockHierarchy(group, subHierarchy));
		}
	}

}
