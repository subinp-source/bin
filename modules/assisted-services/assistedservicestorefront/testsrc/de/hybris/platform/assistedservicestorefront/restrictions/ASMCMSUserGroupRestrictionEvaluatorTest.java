/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.restrictions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedserviceservices.utils.AssistedServiceSession;
import de.hybris.platform.cms2.model.restrictions.ASMCMSUserGroupRestrictionModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ASMCMSUserGroupRestrictionEvaluatorTest
{
	@Mock
	private UserService userService;

	@Mock
	private SessionService sessionService;

	@InjectMocks
	private ASMCMSUserGroupRestrictionEvaluator evaluator = new ASMCMSUserGroupRestrictionEvaluator();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}



	@Test
	public void evaluateTest()
	{
		final ASMCMSUserGroupRestrictionModel groupRestriction = new ASMCMSUserGroupRestrictionModel();
		final Set<UserGroupModel> groups = new HashSet<>();
		groupRestriction.setUserGroups(groups);

		final Set<PrincipalGroupModel> userGroups = new HashSet<>();
		final UserModel user = new UserModel();
		user.setGroups(userGroups);
		when(userService.getCurrentUser()).thenReturn(user);
		assertFalse(evaluator.evaluate(groupRestriction, null));

		final AssistedServiceSession session = mock(AssistedServiceSession.class);
		when(sessionService.getAttribute(AssistedservicefacadesConstants.ASM_SESSION_PARAMETER)).thenReturn(session);
		when(session.getAgent()).thenReturn(null);
		assertFalse(evaluator.evaluate(groupRestriction, null));

		when(session.getAgent()).thenReturn(user);
		assertFalse(evaluator.evaluate(groupRestriction, null));

		final UserGroupModel group1 = new UserGroupModel();
		groups.add(group1);
		userGroups.add(group1);
		assertTrue(evaluator.evaluate(groupRestriction, null));
	}

	@Test
	public void checkWhetherUserIsPartOfRestrictedGroupTest()
	{
		final Set<UserGroupModel> groups = new HashSet<>();

		final ASMCMSUserGroupRestrictionModel groupRestriction = new ASMCMSUserGroupRestrictionModel();
		groupRestriction.setUserGroups(groups);
		groupRestriction.setIncludeSubgroups(false);

		final Set<PrincipalGroupModel> userGroups = new HashSet<>();
		final UserModel user = new UserModel();
		user.setGroups(userGroups);

		assertFalse(evaluator.checkWhetherUserIsPartOfRestrictedGroup(user, groupRestriction));


		final UserGroupModel group1 = new UserGroupModel();
		group1.setGroups(new HashSet<>());
		final UserGroupModel group2 = new UserGroupModel();
		group2.setGroups(new HashSet<>());

		groups.add(group1);
		userGroups.add(group2);

		assertFalse(evaluator.checkWhetherUserIsPartOfRestrictedGroup(user, groupRestriction));

		final Set<PrincipalGroupModel> subgroups = new HashSet<>();
		subgroups.add(group1);
		group2.setGroups(subgroups);
		groupRestriction.setIncludeSubgroups(true);
		assertTrue(evaluator.checkWhetherUserIsPartOfRestrictedGroup(user, groupRestriction));

		groupRestriction.setIncludeSubgroups(false);
		assertFalse(evaluator.checkWhetherUserIsPartOfRestrictedGroup(user, groupRestriction));

		userGroups.add(group1);
		assertTrue(evaluator.checkWhetherUserIsPartOfRestrictedGroup(user, groupRestriction));
	}


	@Test
	public void getSubgroupsTest()
	{
		final List<PrincipalGroupModel> groups = new ArrayList<>();
		assertTrue(evaluator.getSubgroups(groups).isEmpty());
		final PrincipalGroupModel group1 = new PrincipalGroupModel();
		group1.setGroups(new HashSet<>());
		final PrincipalGroupModel group2 = new PrincipalGroupModel();
		group2.setGroups(new HashSet<>());
		groups.add(group1);
		groups.add(group2);
		assertEquals(2, evaluator.getSubgroups(groups).size());
		final PrincipalGroupModel subGroup1 = new PrincipalGroupModel();
		subGroup1.setGroups(new HashSet<>());
		final PrincipalGroupModel subGroup2 = new PrincipalGroupModel();
		subGroup2.setGroups(new HashSet<>());
		group1.getGroups().add(subGroup1);
		group1.getGroups().add(subGroup2);
		final List<PrincipalGroupModel> subGroups = evaluator.getSubgroups(groups);
		assertEquals(4, subGroups.size());
		assertTrue(subGroups.contains(subGroup1));
		assertTrue(subGroups.contains(subGroup2));

	}
}
