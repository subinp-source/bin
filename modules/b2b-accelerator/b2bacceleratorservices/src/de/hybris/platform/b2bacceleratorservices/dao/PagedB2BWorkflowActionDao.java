/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;


public interface PagedB2BWorkflowActionDao
{
	SearchPageData<WorkflowActionModel> findPagedWorkflowActionsByUserAndActionTypes(UserModel user,
			WorkflowActionType[] actionTypes, PageableData pageableData);
}
