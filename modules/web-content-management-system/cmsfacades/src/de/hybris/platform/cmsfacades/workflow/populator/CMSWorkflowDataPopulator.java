/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@link CMSWorkflowData} instance from the {@link WorkflowModel} source data model.
 */
public class CMSWorkflowDataPopulator implements Populator<WorkflowModel, CMSWorkflowData>
{
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;

	@Override
	public void populate(final WorkflowModel workflowModel, final CMSWorkflowData workflowData) throws ConversionException
	{
		workflowData.setWorkflowCode(workflowModel.getCode());
		workflowData.setStatus(workflowModel.getStatus().getCode());
		workflowData.setDescription(workflowModel.getDescription());
		workflowData.setIsAvailableForCurrentPrincipal(getCmsWorkflowParticipantService().isWorkflowParticipant(workflowModel));
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

}
