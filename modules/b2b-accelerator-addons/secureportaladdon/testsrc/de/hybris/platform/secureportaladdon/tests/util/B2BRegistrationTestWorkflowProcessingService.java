/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.tests.util;

import de.hybris.platform.workflow.impl.DefaultWorkflowProcessingService;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;


/**
 * Test Service to override default automated worflow action activation. It allows individual activations of automated
 * workflow actions.
 */
public class B2BRegistrationTestWorkflowProcessingService extends DefaultWorkflowProcessingService
{
	/**
	 * The method below calls activates all subsequent automated actions. It is kept empty for use in a Workflow
	 * Integration test in which it is desired to activate an individual automated workflow action.
	 */
	@Override
	protected void chosen(final WorkflowDecisionModel selDec)
	{
		//Read Java - Doc Comments. This method must be kept empty
	}

}
