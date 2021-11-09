/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.strategy.impl;

import de.hybris.platform.b2b.occ.strategy.OrgUnitUserRoleManagementStrategy;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BApproverFacade;

import org.springframework.beans.factory.annotation.Required;


public class OrgUnitApproversManagementStrategy implements OrgUnitUserRoleManagementStrategy
{
	protected B2BApproverFacade b2bApproverFacade;

	@Override
	public void addRoleToUser(String unitId, String userId)
	{
		getB2bApproverFacade().addApproverToUnit(unitId, userId);
	}

	@Override
	public void removeRoleFromUser(String unitId, String userId)
	{
		getB2bApproverFacade().removeApproverFromUnit(unitId, userId);
	}


	protected B2BApproverFacade getB2bApproverFacade()
	{
		return b2bApproverFacade;
	}

	@Required
	public void setB2bApproverFacade(B2BApproverFacade b2bApproverFacade)
	{
		this.b2bApproverFacade = b2bApproverFacade;
	}
}
