/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;


public interface WorkflowTemplateStrategy
{

	/**
	 * Create a Workflow for the list of users with a specific code and description
	 * 
	 * @param users
	 * @param code
	 * @param description
	 * @return WorkflowTemplateModel
	 */
	WorkflowTemplateModel createWorkflowTemplate(final List<? extends UserModel> users, final String code, final String description);

	String getWorkflowTemplateType();
}
