/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowEditableItemData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.List;


/**
 * Facade interface which deals with methods to manage workflows with CmsItems attached to them.
 */
public interface CMSWorkflowFacade
{
	/**
	 * Creates and starts a workflow in the given catalog version with the workflow data provided.
	 *
	 * @param workflowData
	 *           - the data used to create the workflow.
	 * @return The data of the newly created and started workflow
	 */
	CMSWorkflowData createAndStartWorkflow(CMSWorkflowData workflowData);

	/**
	 * Performs different operations defined by {@link CMSWorkflowOperation} on the workflow.
	 *
	 * @param workflowCode
	 *           - the code corresponding to the {@link WorkflowModel}
	 * @param cmsWorkflowOperationData
	 *           - the DTO object containing all the information about operation to be performed
	 * @return The data of the modified workflow.
	 * @throws UnknownIdentifierException
	 *            if a workflow cannot be found for the provided workflowCode.
	 */
	CMSWorkflowData performOperation(final String workflowCode, CMSWorkflowOperationData cmsWorkflowOperationData);

	/**
	 * Finds all workflows containing the given attachment for the specified workflow statuses.
	 *
	 * @param workflowData
	 *           - the workflow dto containing the attachment uuid and the workflow statuses that will be used to filter
	 *           the result set.
	 * @param pageableData
	 *           - the pageable dto determining the page size and index.
	 * @return the paginated result of workflow data
	 */
	SearchResult<CMSWorkflowData> findAllWorkflows(CMSWorkflowData workflowData, PageableData pageableData);

	/**
	 * Updates the workflow with the provided data.
	 *
	 * @param workflowCode
	 *           - the code corresponding to the {@link WorkflowModel}
	 * @param workflowData
	 *           - the workflow dto to be updated.
	 * @return the updated workflow data.
	 * @throws UnknownIdentifierException
	 *            if a workflow cannot be found for the provided workflowCode.
	 */
	CMSWorkflowData editWorkflow(final String workflowCode, final CMSWorkflowData workflowData);

	/**
	 * Returns the workflow by its code.
	 *
	 * @param workflowCode
	 *           - the workflow code
	 * @return the workflow data
	 * @throws UnknownIdentifierException
	 *            if a workflow cannot be found for the provided workflowCode.
	 */
	CMSWorkflowData getWorkflowForCode(final String workflowCode);

	/**
	 * Returns a list of {@link CMSWorkflowEditableItemData} that informs whether each item is editable or not by the session user.
	 * @param itemUids the list of item uids to verify.
	 * @return the list of {@link CMSWorkflowEditableItemData}
	 */
	List<CMSWorkflowEditableItemData> getSessionUserEditableItems(final List<String> itemUids);
}
