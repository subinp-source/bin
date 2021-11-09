/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cmsfacades.common.service.TimeDiffService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowActionData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowDecisionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@link CMSWorkflowActionData} instance from the {@link WorkflowActionModel} source data model.
 */
public class CMSWorkflowActionDataPopulator implements Populator<WorkflowActionModel, CMSWorkflowActionData>
{

	private CMSWorkflowParticipantService cmsWorkflowParticipantService;
	private TimeDiffService timeDiffService;

	@Override
	public void populate(final WorkflowActionModel workflowActionModel, final CMSWorkflowActionData workflowActionData)
	{
		workflowActionData.setActionType(workflowActionModel.getActionType().name());
		workflowActionData.setCode(workflowActionModel.getCode());
		workflowActionData.setName(workflowActionModel.getName());
		workflowActionData.setDescription(workflowActionModel.getDescription());
		workflowActionData.setStatus(workflowActionModel.getStatus().name());
		workflowActionData.setModifiedtime(workflowActionModel.getModifiedtime());
		workflowActionData.setDecisions(
				workflowActionModel.getDecisions().stream().map(this::getWorkflowDecisionData).collect(Collectors.toList()));
		workflowActionData
				.setIsCurrentUserParticipant(getCmsWorkflowParticipantService().isWorkflowActionParticipant(workflowActionModel));
		if (workflowActionModel.getStatus().equals(WorkflowActionStatus.IN_PROGRESS))
		{
			workflowActionData.setStartedAgoInMillis(getTimeDiffService().difference(workflowActionModel.getActivated()));
		}
	}

	/**
	 * Method that converts the {@link WorkflowDecisionModel workflowDecisionModel} to {@link CMSWorkflowDecisionData
	 * cmsWorkflowDecisionData}.
	 *
	 * @param workflowDecisionModel
	 *           The {@link WorkflowDecisionModel workflowDecisionModel}.
	 * @return The {@link CMSWorkflowDecisionData cmsWorkflowDecisionData}.
	 */
	protected CMSWorkflowDecisionData getWorkflowDecisionData(final WorkflowDecisionModel workflowDecisionModel)
	{
		final CMSWorkflowDecisionData workflowDecisionData = new CMSWorkflowDecisionData();
		workflowDecisionData.setCode(workflowDecisionModel.getCode());
		workflowDecisionData.setName(workflowDecisionModel.getName());
		workflowDecisionData.setDescription(workflowDecisionModel.getDescription());
		return workflowDecisionData;
	}


	protected CMSWorkflowParticipantService getCmsWorkflowParticipantService()
	{
		return cmsWorkflowParticipantService;
	}

	@Required
	public void setCmsWorkflowParticipantService(final CMSWorkflowParticipantService cmsWorkflowParticipantService)
	{
		this.cmsWorkflowParticipantService = cmsWorkflowParticipantService;
	}

	protected TimeDiffService getTimeDiffService()
	{
		return timeDiffService;
	}

	@Required
	public void setTimeDiffService(final TimeDiffService timeDiffService)
	{
		this.timeDiffService = timeDiffService;
	}
}
