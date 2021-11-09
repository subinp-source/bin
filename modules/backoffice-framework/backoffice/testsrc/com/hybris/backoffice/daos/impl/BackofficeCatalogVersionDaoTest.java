/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.daos.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import com.hybris.backoffice.user.BackofficeRoleService;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BackofficeCatalogVersionDaoTest
{

	@InjectMocks
	private BackofficeCatalogVersionDao testSubject;

	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private BackofficeRoleService backofficeRoleService;

	@Mock
	private PrincipalModel user;

	@Captor
	private ArgumentCaptor<Map<String, Object>> queryParamsCaptor;

	@Test
	public void shouldIncludeOnlyDirectGroupsWhenThereIsNoActiveRole()
	{
		// given
		final UserGroupModel groupModel = mock(UserGroupModel.class);
		final BackofficeRoleModel inactiveRole = mock(BackofficeRoleModel.class);
		final UserGroupModel inactiveRoleGroupModel = mock(UserGroupModel.class);
		when(user.getAllGroups()).thenReturn(Sets.newHashSet(groupModel, inactiveRole, inactiveRoleGroupModel));
		when(backofficeRoleService.shouldTreatRolesAsGroups()).thenReturn(false);
		when(backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.empty());
		when(backofficeRoleService.getNonRolePrincipalsHierarchy(user)).thenReturn(Sets.newHashSet(user, groupModel));
		when(flexibleSearchService.search(any(), any())).thenReturn(new SearchResultImpl<>(new ArrayList<>(), 0, 0, 0));

		// when
		testSubject.findReadableCatalogVersions(user);

		// then
		verify(flexibleSearchService).search(any(), queryParamsCaptor.capture());

		final Object principalsQueryParam = queryParamsCaptor.getValue().get("principals");
		assertThat(principalsQueryParam).isInstanceOf(Collection.class);
		assertThat(((Collection) principalsQueryParam)).containsOnly(user, groupModel);
	}

	@Test
	public void shouldIncludeDirectGroupsAndActiveRole()
	{
		// given
		final UserGroupModel groupModel = mock(UserGroupModel.class);
		final BackofficeRoleModel activeRole = mock(BackofficeRoleModel.class);
		final UserGroupModel activeRoleGroupModel = mock(UserGroupModel.class);
		final BackofficeRoleModel inactiveRole = mock(BackofficeRoleModel.class);
		final UserGroupModel inactiveRoleGroupModel = mock(UserGroupModel.class);
		when(user.getAllGroups())
				.thenReturn(Sets.newHashSet(groupModel, activeRole, activeRoleGroupModel, inactiveRole, inactiveRoleGroupModel));
		when(activeRole.getAllGroups()).thenReturn(Collections.singleton(activeRoleGroupModel));
		when(backofficeRoleService.shouldTreatRolesAsGroups()).thenReturn(false);
		when(backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.of(activeRole));
		when(backofficeRoleService.getNonRolePrincipalsHierarchy(user)).thenReturn(Sets.newHashSet(user, groupModel));
		when(flexibleSearchService.search(any(), any())).thenReturn(new SearchResultImpl<>(new ArrayList<>(), 0, 0, 0));

		// when
		testSubject.findReadableCatalogVersions(user);

		// then
		verify(flexibleSearchService).search(any(), queryParamsCaptor.capture());

		final Object principalsQueryParam = queryParamsCaptor.getValue().get("principals");
		assertThat(principalsQueryParam).isInstanceOf(Collection.class);
		assertThat(((Collection) principalsQueryParam)).containsOnly(user, groupModel, activeRole, activeRoleGroupModel);
	}

	@Test
	public void shouldConsiderEverythingInLegacyMode()
	{
		final UserGroupModel activeRoleGroup = mock(UserGroupModel.class);
		final BackofficeRoleModel activeRole = mock(BackofficeRoleModel.class);
		final UserGroupModel userGroup = mock(UserGroupModel.class);
		final UserGroupModel inactiveRoleGroup = mock(UserGroupModel.class);
		final UserGroupModel inactiveRoleSuperGroup = mock(UserGroupModel.class);
		final BackofficeRoleModel inactiveRole = mock(BackofficeRoleModel.class);

		when(backofficeRoleService.shouldTreatRolesAsGroups()).thenReturn(true);
		when(user.getAllGroups()).thenReturn(
				Sets.newHashSet(userGroup, inactiveRole, activeRole, activeRoleGroup, inactiveRoleGroup, inactiveRoleSuperGroup));
		when(flexibleSearchService.search(any(), any())).thenReturn(new SearchResultImpl<>(new ArrayList<>(), 0, 0, 0));

		testSubject.findReadableCatalogVersions(user);

		verify(flexibleSearchService).search(any(), queryParamsCaptor.capture());

		final Object principalsQueryParam = queryParamsCaptor.getValue().get("principals");
		assertThat(principalsQueryParam).isInstanceOf(Collection.class);
		assertThat(((Collection) principalsQueryParam)).containsOnly(user, userGroup, activeRole, activeRoleGroup,
				inactiveRoleGroup, inactiveRole, inactiveRoleSuperGroup);
	}

}
