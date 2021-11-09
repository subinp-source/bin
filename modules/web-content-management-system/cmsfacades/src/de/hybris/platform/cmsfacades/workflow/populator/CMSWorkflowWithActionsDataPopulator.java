/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import de.hybris.platform.cmsfacades.data.CMSWorkflowActionData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@link CMSWorkflowData} instance from the {@link WorkflowModel} source data model.
 */
public class CMSWorkflowWithActionsDataPopulator implements Populator<WorkflowModel, CMSWorkflowData>
{
	private Converter<WorkflowActionModel, CMSWorkflowActionData> cmsWorkflowActionDataConverter;
	private Predicate<WorkflowActionModel> automatedWorkflowActionTypePredicate;

	@Override
	public void populate(final WorkflowModel workflowModel, final CMSWorkflowData workflowData)
	{
		workflowData.setWorkflowCode(workflowModel.getCode());
		workflowData.setTemplateCode(workflowModel.getJob().getCode());


		final List<CMSWorkflowActionData> actionListData = workflowModel.getActions().stream() //
				.filter(action -> getAutomatedWorkflowActionTypePredicate().negate().test(action)) //
				.map(action -> getCmsWorkflowActionDataConverter().convert(action)) //
				.collect(Collectors.toList());
		workflowData.setActions(actionListData);
	}

	protected Converter<WorkflowActionModel, CMSWorkflowActionData> getCmsWorkflowActionDataConverter()
	{
		return cmsWorkflowActionDataConverter;
	}

	@Required
	public void setCmsWorkflowActionDataConverter(
			final Converter<WorkflowActionModel, CMSWorkflowActionData> cmsWorkflowActionDataConverter)
	{
		this.cmsWorkflowActionDataConverter = cmsWorkflowActionDataConverter;
	}

	protected Predicate<WorkflowActionModel> getAutomatedWorkflowActionTypePredicate()
	{
		return automatedWorkflowActionTypePredicate;
	}

	@Required
	public void setAutomatedWorkflowActionTypePredicate(final Predicate<WorkflowActionModel> automatedWorkflowActionTypePredicate)
	{
		this.automatedWorkflowActionTypePredicate = automatedWorkflowActionTypePredicate;
	}

}
