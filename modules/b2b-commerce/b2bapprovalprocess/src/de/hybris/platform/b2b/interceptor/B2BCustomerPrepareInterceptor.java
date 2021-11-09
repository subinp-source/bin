/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.interceptor;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * Ensures that the customer belongs to appropriate groups.
 */
public class B2BCustomerPrepareInterceptor implements PrepareInterceptor
{
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private UserService userService;
	private static final Logger LOG = Logger.getLogger(B2BCustomerPrepareInterceptor.class);

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx)
	{
		if (!(model instanceof B2BCustomerModel))
		{
			return;
		}

		final B2BCustomerModel customer = (B2BCustomerModel) model;
		final B2BUnitModel parentUnit = b2bUnitService.getParent(customer);
		
		if (null != parentUnit)
		{
			makeSureThatB2BUnitIsInGroups(customer, parentUnit);
		}

		// ensure all approvers of the unit are members of the b2bapprovergroup
		if (customer.getApprovers() == null)
		{
			return;
		}

		final Set<B2BCustomerModel> approvers = new HashSet<>(customer.getApprovers());
		if (CollectionUtils.isNotEmpty(approvers))
		{
			final UserGroupModel b2bApproverGroup = userService.getUserGroupForUID(B2BConstants.B2BAPPROVERGROUP);
			final Set<B2BCustomerModel> validApprovers = approvers.stream().filter( approver -> {
				if (!userService.isMemberOfGroup(approver, b2bApproverGroup))
				{
					LOG.warn(String.format("Removed approver %s from customer %s due to lack of membership of group %s",
							approver.getUid(), customer.getUid(), B2BConstants.B2BAPPROVERGROUP));
					return false;
				}
				return true;
			}).collect(Collectors.toSet());
			customer.setApprovers(validApprovers);
		}
	}

	/**
	 * Method check if B2BUnit is in groups for customer. If not it is added to groups
	 */
	protected void makeSureThatB2BUnitIsInGroups(final B2BCustomerModel customer, final B2BUnitModel parentUnit)
	{
		if (parentUnit != null && !isB2BUnitInGroupList(customer.getGroups(), parentUnit))
		{
			final Set<PrincipalGroupModel> groups = new HashSet<>(
					(customer.getGroups() != null ? customer.getGroups() : new HashSet<PrincipalGroupModel>()));
			groups.add(parentUnit);
			customer.setGroups(groups);
		}
	}

	protected boolean isB2BUnitInGroupList(final Set<PrincipalGroupModel> groups, final B2BUnitModel parentUnit)
	{
		if (groups == null || groups.isEmpty())
		{
			return false;
		}

		for (final PrincipalGroupModel group : groups)
		{
			if (group instanceof B2BUnitModel && group.getUid().equals(parentUnit.getUid()))
			{
				return true;
			}
		}
		return false;
	}

	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	@Resource
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Resource
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
