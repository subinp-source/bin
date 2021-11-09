/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow;

import de.hybris.platform.cmsfacades.data.WorkflowTemplateData;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;


/**
 * Workflow template facade interface which deals with {@link WorkflowTemplateModel Workflow Templates}.
 *
 * @spring.bean cmsWorkflowTemplateFacade
 */
public interface CMSWorkflowTemplateFacade
{

	/**
	 * Retrieves the list of workflow templates for a given catalog ID and catalog version.
	 *
	 * @param catalogId
	 *           The catalog name
	 * @param versionId
	 *           The catalog version identifier
	 * @return A list of {@link WorkflowTemplateData}
	 */
	List<WorkflowTemplateData> getWorkflowTemplates(final String catalogId, final String versionId);

}
