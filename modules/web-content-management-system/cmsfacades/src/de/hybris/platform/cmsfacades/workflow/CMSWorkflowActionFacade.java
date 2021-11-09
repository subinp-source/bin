/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cmsfacades.data.CMSCommentData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowTaskData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;


/**
 * Facade interface which deals with methods to manage workflow actions and actions related comments.
 */
public interface CMSWorkflowActionFacade
{

	/**
	 * Returns a list of actions and decisions for a given workflow identified by its code.
	 *
	 * @param workflowCode
	 *           - the workflow code
	 *
	 * @return the workflow data with actions and decisions
	 *
	 * @throws UnknownIdentifierException
	 *            if a workflow cannot be found for the provided workflowCode.
	 */
	CMSWorkflowData getActionsForWorkflowCode(final String workflowCode);

	/**
	 * Returns paginated list of action comments.
	 *
	 * @param workflowCode
	 *           - the code corresponding to the {@link WorkflowModel}
	 * @param actionCode
	 *           - the code corresponding to the {@link WorkflowActionModel}
	 * @param pageableData
	 *           - the pageable dto determining the page size and index.
	 * @return the paginated result of action comments data
	 */
	SearchResult<CMSCommentData> getActionComments(String workflowCode, String actionCode, PageableData pageableData);

	/**
	 * Returns paginated list of workflow tasks.
	 *
	 * @param pageableData
	 *           - the pageable dto determining the page size and index.
	 * @return the paginated result of workflow tasks data.
	 */
	SearchResult<CMSWorkflowTaskData> findAllWorkflowTasks(final PageableData pageableData);

}
