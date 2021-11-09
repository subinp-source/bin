/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order.populators;

import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionResultData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link OrderHistoryData} with {@link OrderModel}.
 */
public class B2BOrderHistoryPopulator implements Populator<OrderModel, OrderHistoryData>
{
	private Converter<B2BCostCenterModel, B2BCostCenterData> b2BCostCenterConverter;
	private Converter<B2BPermissionResultModel, B2BPermissionResultData> b2BPermissionResultConverter;
	private Converter<PrincipalModel, PrincipalData> principalConverter;
	private WorkflowActionService workflowActionService;


	@Override
	public void populate(final OrderModel source, final OrderHistoryData target) throws ConversionException
	{
		for (final AbstractOrderEntryModel entry : source.getEntries())
		{
			if (entry.getCostCenter() != null)
			{
				target.setCostCenter(b2BCostCenterConverter.convert(entry.getCostCenter()));
				break;
			}
		}

		target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
		target.setB2bPermissionResults(Converters.convertAll(source.getPermissionResults(), getB2BPermissionResultConverter()));

		if (source.getWorkflow() != null)
		{

			final Set<PrincipalModel> actionUsers = new HashSet<PrincipalModel>();
			final List<WorkflowActionModel> startWorkflowActions = workflowActionService.getStartWorkflowActions(source
					.getWorkflow());
			for (final WorkflowActionModel action : startWorkflowActions)
			{
				actionUsers.add(action.getPrincipalAssigned());
			}
			target.setManagers(Converters.convertAll(actionUsers, getPrincipalConverter()));
		}
	}

	protected Converter<B2BPermissionResultModel, B2BPermissionResultData> getB2BPermissionResultConverter()
	{
		return b2BPermissionResultConverter;
	}

	@Required
	public void setB2BPermissionResultConverter(final Converter<B2BPermissionResultModel, B2BPermissionResultData> b2BPermissionResultConverter)
	{
		this.b2BPermissionResultConverter = b2BPermissionResultConverter;
	}

	protected Converter<PrincipalModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(final Converter<PrincipalModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}

	public void setB2BCostCenterConverter(
			final Converter<B2BCostCenterModel, B2BCostCenterData> b2BCostCenterConverter)
	{
		this.b2BCostCenterConverter = b2BCostCenterConverter;
	}

	protected WorkflowActionService getWorkflowActionService()
	{
		return workflowActionService;
	}

	@Required
	public void setWorkflowActionService(final WorkflowActionService workflowActionService)
	{
		this.workflowActionService = workflowActionService;
	}
}
