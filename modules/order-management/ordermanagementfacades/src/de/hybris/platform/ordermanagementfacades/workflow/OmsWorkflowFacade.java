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
package de.hybris.platform.ordermanagementfacades.workflow;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowCodesDataList;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowData;

import java.util.List;


/**
 * Order management facade exposing operations related to {@link de.hybris.platform.workflow.model.WorkflowModel}
 */
public interface OmsWorkflowFacade
{
	/**
	 * Creates and starts an error recovery workflow with the given template for the given {@link ItemModel}.
	 *
	 * @param item
	 * 		the {@link ItemModel} for which to start the workflow
	 * @param workflowName
	 * 		name given to the workflow at creation time
	 * @param workflowTemplateCode
	 * 		template code of the workflow to be started
	 * @param userGroupUid
	 * 		the uid of the group to be assigned to the workflow actions
	 * @param errorType
	 * 		the type of the error. This {@link String} will be persisted inside of the created	workflow
	 * @param errorDescription
	 * 		the description of the error. This {@link String} will be persisted inside of the created	workflow
	 * @return instance of created {@link WorkflowData}
	 */
	WorkflowData startErrorRecoveryWorkflow(ItemModel item, String workflowName, String workflowTemplateCode, String userGroupUid,
			String errorType, String errorDescription);

	/**
	 * Creates and starts a workflow with the given template for the given {@link ItemModel}.
	 *
	 * @param item
	 * 		the {@link ItemModel} for which to start the workflow
	 * @param workflowName
	 * 		name given to the workflow at creation time
	 * @param workflowTemplateCode
	 * 		template code of the workflow to be started
	 * @param userGroupUid
	 * 		the uid of the group to be assigned to the workflow actions
	 * @return instance of created {@link WorkflowData}
	 */
	WorkflowData startWorkflow(ItemModel item, String workflowName, String workflowTemplateCode, String userGroupUid);

	/**
	 * API to get all active workflow actions for the current user in the system
	 *
	 * @return a list of {@link WorkflowActionData}
	 */
	List<WorkflowActionData> getWorkflowActions();

	/**
	 * Decides the {@link de.hybris.platform.workflow.model.WorkflowActionModel} with the provided {@link de.hybris.platform.workflow.model.WorkflowDecisionModel#NAME}
	 *
	 * @param workflowCode
	 * 		the {@link de.hybris.platform.workflow.model.WorkflowModel#CODE} which the action belongs to
	 * @param workflowDecisionCode
	 * 		the decision to take on the action
	 */
	void decideAction(String workflowCode, String workflowDecisionCode);

	/**
	 * Decides a list of workflows containing actions with the provided workflow decision name
	 *
	 * @param workflowCodes
	 * 		the WorkflowCodesDataList containing a list of workflow codes which the actions belong to
	 * @param workflowDecisionCode
	 * 		the decision to take on the actions
	 */
	void decideActions(WorkflowCodesDataList workflowCodes, String workflowDecisionCode);

}
