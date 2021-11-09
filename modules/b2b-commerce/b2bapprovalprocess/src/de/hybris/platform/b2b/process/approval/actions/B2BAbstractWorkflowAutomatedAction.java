/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.actions;

import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public abstract class B2BAbstractWorkflowAutomatedAction implements AutomatedWorkflowTemplateJob
{
	private static final Logger LOG = Logger.getLogger(B2BAbstractWorkflowAutomatedAction.class.getName());
	private ModelService modelService;

	@Override
	public final WorkflowDecisionModel perform(final WorkflowActionModel action)
	{
		performAction(action);

		final List<WorkflowDecisionModel> workflowDecisionList = (List<WorkflowDecisionModel>) action.getDecisions();
		if (workflowDecisionList != null && !workflowDecisionList.isEmpty())
		{
			return workflowDecisionList.get(0);
		}

		return null;
	}

	public abstract void performAction(final WorkflowActionModel action);

	/**
	 * Assigns the PermissionStatus to all the {@link B2BPermissionResultModel}s associated with a given order and are
	 * assigned to the principal.
	 *
	 * @param order
	 *           A b2c order
	 * @param principalAssigned
	 *           A hybris principal
	 * @param status
	 *           A {@link PermissionStatus} enumeration
	 */
	protected void updatePermissionResultsStatus(final OrderModel order, final PrincipalModel principalAssigned,
			final PermissionStatus status)
	{
		final Collection<B2BPermissionResultModel> permissionResults = order.getPermissionResults() != null ? order
				.getPermissionResults() : Collections.<B2BPermissionResultModel> emptyList();
		for (final B2BPermissionResultModel b2bPermissionResultModel : permissionResults)
		{
			if (principalAssigned.equals(b2bPermissionResultModel.getApprover()))
			{
				b2bPermissionResultModel.setStatus(status);
				if (LOG.isDebugEnabled())
				{
					LOG.debug(String.format("%s|%s|%s ", b2bPermissionResultModel.getPermissionTypeCode(),
							b2bPermissionResultModel.getStatus(), b2bPermissionResultModel.getApprover().getUid()));
				}
			}
		}
		getModelService().saveAll(permissionResults);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
