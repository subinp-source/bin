/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class B2BCustomerPrepareInterceptorTest
{
	@InjectMocks
	private final B2BCustomerPrepareInterceptor b2bCustomerPrepareInterceptor = new B2BCustomerPrepareInterceptor();

	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	@Mock
	private UserService userService;

	@Test
	public void testNotSupportedClass()
	{
		final Object model = spy(new Object());

		b2bCustomerPrepareInterceptor.onPrepare(model, null);

		verifyZeroInteractions(model);
	}

	@Test
	public void testNullParentUnit()
	{
		final B2BCustomerModel model = new B2BCustomerModel();
		final Set<PrincipalGroupModel> groups = new HashSet<>();
		final PrincipalGroupModel principalGroupModel1 = new PrincipalGroupModel();
		groups.add(principalGroupModel1);
		model.setGroups(groups);

		when(b2bUnitService.getParent(model)).thenReturn(null);

		b2bCustomerPrepareInterceptor.onPrepare(model, null);

		assertThat(model.getGroups()).containsExactly(principalGroupModel1);
	}

	@Test
	public void testB2BUnitIsInGroups()
	{
		final B2BCustomerModel model = new B2BCustomerModel();
		final Set<PrincipalGroupModel> groups = new HashSet<>();
		final PrincipalGroupModel principalGroupModel1 = new PrincipalGroupModel();
		groups.add(principalGroupModel1);

		model.setGroups(groups);
		final B2BUnitModel parentUnit = new B2BUnitModel();
		when(b2bUnitService.getParent(model)).thenReturn(parentUnit);

		b2bCustomerPrepareInterceptor.onPrepare(model, null);

		assertThat(model.getGroups()).containsExactlyInAnyOrder(principalGroupModel1, parentUnit);
	}

	@Test
	public void testRemoveApproversNotInB2bapprovergroup()
	{
		// given
		final B2BCustomerModel customer = new B2BCustomerModel();
		final B2BCustomerModel approver1 = new B2BCustomerModel();
		final B2BCustomerModel approver2 = new B2BCustomerModel();
		final B2BCustomerModel approver3 = new B2BCustomerModel();
		customer.setApprovers(Set.of(approver1, approver2, approver3));

		final UserGroupModel b2bApproverGroup = new UserGroupModel();
		when(userService.getUserGroupForUID(B2BConstants.B2BAPPROVERGROUP)).thenReturn(b2bApproverGroup);
		when(userService.isMemberOfGroup(approver1, b2bApproverGroup)).thenReturn(false);
		when(userService.isMemberOfGroup(approver2, b2bApproverGroup)).thenReturn(true);
		when(userService.isMemberOfGroup(approver3, b2bApproverGroup)).thenReturn(false);

		// when
		b2bCustomerPrepareInterceptor.onPrepare(customer, null);

		// then
		assertThat(customer.getApprovers()).containsExactly(approver2);
	}
}