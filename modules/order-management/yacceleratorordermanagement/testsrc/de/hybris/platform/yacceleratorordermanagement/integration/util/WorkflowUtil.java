/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.yacceleratorordermanagement.integration.util;

import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Collections;


/**
 * This class is used for workflow interactions
 */
public class WorkflowUtil extends ProcessUtil
{
	public static final String PICKING_TEMPLATE_CODE = "NPR_Picking";
	public static final String PACKING_TEMPLATE_CODE = "NPR_Packing";
	public static final String SHIPPING_TEMPLATE_CODE = "NPR_Shipping";
	public static final String PICKUP_TEMPLATE_CODE = "NPR_Pickup";

	private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;
	private ImpersonationService impersonationService;

	/**
	 * Moves the task assignment workflow to its next planned task
	 *
	 * @param orderProcessModel
	 * 		the associated {@link OrderProcessModel}
	 * @param consignment
	 * 		the targeted {@link ConsignmentModel} for which we want to move the workflow forward
	 * @param templateCode
	 * 		the {@link de.hybris.platform.workflow.model.WorkflowTemplateModel#CODE} to be moved
	 */
	public void moveConsignmentWorkflow(final OrderProcessModel orderProcessModel, final ConsignmentModel consignment,
			final String templateCode)
	{
		final WorkflowModel workflowModel = getNewestWorkflowService().getWorkflowForCode(consignment.getTaskAssignmentWorkflow());
		final WorkflowActionModel workflowActionModel = workflowModel.getActions().stream()
				.filter(action -> action.getTemplate().getCode().equals(templateCode)).findFirst().get();
		workflowActionModel.setPrincipalAssigned(getUserService().getAdminUser());
		getModelService().save(workflowActionModel);

		final ImpersonationContext context = new ImpersonationContext();
		context.setSite(orderProcessModel.getOrder().getSite());
		context.setUser(getUserService().getAdminUser());

		getImpersonationService().executeInContext(context, () ->
		{
			getWarehousingConsignmentWorkflowService().decideWorkflowAction(consignment, templateCode, null);
			waitUntilWorkflowProcessIsNotRunning(workflowModel, timeOut);
			return null;
		});
		try
		{
			waitUntilConsignmentProcessIsNotRunning(orderProcessModel, consignment, timeOut);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Wait until the workflow process is not running
	 *
	 * @param workflow
	 * @param timeOut
	 * @throws InterruptedException
	 */
	public void waitUntilWorkflowProcessIsNotRunning(final WorkflowModel workflow, final int timeOut)
	{
		int timeCount = 0;
		do
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
			getModelService().refresh(workflow);
		}
		while (CronJobStatus.ABORTED.equals(workflow.getStatus()) && timeCount++ < timeOut);
	}

	/**
	 * Setup links between actions and decisions
	 */
	public void setupRelations()
	{
		getWorkflowActionTemplates().Packing()
				.setIncomingTemplateDecisions(Collections.singletonList(getWorkflowDecisionTemplates().Picking()));
		getWorkflowActionTemplates().Pickup()
				.setIncomingTemplateDecisions(Collections.singletonList(getWorkflowDecisionTemplates().AutoPackingPickup()));
		getWorkflowActionTemplates().Shipping()
				.setIncomingTemplateDecisions(Collections.singletonList(getWorkflowDecisionTemplates().AutoPackingShipping()));
		getWorkflowActionTemplates().End().setIncomingTemplateDecisions(
				Arrays.asList(getWorkflowDecisionTemplates().AutoPickup(), getWorkflowDecisionTemplates().AutoShipping()));

		getAutomatedWorkflowActionTemplates().AutoPacking()
				.setIncomingTemplateDecisions(Collections.singletonList(getWorkflowDecisionTemplates().Packing()));
		getAutomatedWorkflowActionTemplates().AutoShipping()
				.setIncomingTemplateDecisions(Collections.singletonList(getWorkflowDecisionTemplates().Shipping()));
		getAutomatedWorkflowActionTemplates().AutoPickup()
				.setIncomingTemplateDecisions(Collections.singletonList(getWorkflowDecisionTemplates().Pickup()));
	}

	public WarehousingConsignmentWorkflowService getWarehousingConsignmentWorkflowService()
	{
		return warehousingConsignmentWorkflowService;
	}

	public void setWarehousingConsignmentWorkflowService(
			final WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService)
	{
		this.warehousingConsignmentWorkflowService = warehousingConsignmentWorkflowService;
	}

	public ImpersonationService getImpersonationService()
	{
		return impersonationService;
	}

	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}
}
