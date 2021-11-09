/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.facades;

import de.hybris.platform.secureportaladdon.model.B2BRegistrationModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;


/**
 * Facade that handles workflow specific actions for B2B registrations
 */
public interface B2BRegistrationWorkflowFacade
{
	/**
	 * Launches a new workflow instance
	 * 
	 * @param workflowTemplateModel
	 *           The workflow template definition to use
	 * @param b2bRegistrationModel
	 *           All registration specific information
	 */
	public void launchWorkflow(WorkflowTemplateModel workflowTemplateModel, B2BRegistrationModel b2bRegistrationModel);

}
