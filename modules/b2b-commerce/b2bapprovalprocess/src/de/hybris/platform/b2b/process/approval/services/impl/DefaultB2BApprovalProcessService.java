/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.process.approval.services.B2BApprovalProcessService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2b.strategies.B2BApprovalProcessLookUpStrategy;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link B2BApprovalProcessService}
 */
public class DefaultB2BApprovalProcessService implements B2BApprovalProcessService
{
	private B2BApprovalProcessLookUpStrategy b2bApprovalProcessLookUpStrategy;
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	@Override
	public Map<String, String> getProcesses(final BaseStoreModel store)
	{
		return getB2bApprovalProcessLookUpStrategy().getProcesses(store);
	}

	@Override
	public String getApprovalProcessCodeForUnit(final B2BUnitModel unit)
	{
		B2BUnitModel parent = unit;
		String approvalProcessCode = unit.getApprovalProcessCode();
		while (parent != null && StringUtils.isBlank(approvalProcessCode))
		{
			parent = getB2bUnitService().getParent(parent);
			if (parent != null && StringUtils.isNotBlank(parent.getApprovalProcessCode()))
			{
				approvalProcessCode = parent.getApprovalProcessCode();
			}
		}
		return approvalProcessCode;

	}

	protected B2BApprovalProcessLookUpStrategy getB2bApprovalProcessLookUpStrategy()
	{
		return b2bApprovalProcessLookUpStrategy;
	}

	@Required
	public void setB2bApprovalProcessLookUpStrategy(final B2BApprovalProcessLookUpStrategy b2bApprovalProcessLookUpStrategy)
	{
		this.b2bApprovalProcessLookUpStrategy = b2bApprovalProcessLookUpStrategy;
	}

	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	@Required
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}
}
