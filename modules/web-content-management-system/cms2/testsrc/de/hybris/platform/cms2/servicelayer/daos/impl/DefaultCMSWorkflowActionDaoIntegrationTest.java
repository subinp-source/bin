/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowActionDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowActionModel;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSWorkflowActionDaoIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String WORKFLOW_START_ACTION = "action2Start";

	@Resource
	private CMSWorkflowActionDao cmsWorkflowActionDao;
	@Resource
	private CMSAdminPageService cmsAdminPageService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/cmsWorkflowsTestData.impex", "UTF-8");
	}

	@Test
	public void shouldFindAllActiveActionsByStatusAndPrincipals()
	{

		final UserModel user = userService.getUserForUID("cmsmanager");

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setPageSize(1);

		final Collection<PrincipalModel> principals = new HashSet<>();
		principals.add(user);
		principals.addAll(user.getAllGroups());

		// WHEN
		final SearchResult<WorkflowActionModel> result = cmsWorkflowActionDao
				.findAllActiveWorkflowActionsByStatusAndPrincipals(CMS_WORKFLOW_ACTIVE_STATUSES, principals, pageableData);

		// THEN
		assertThat(result.getCount(), equalTo(1));
		assertThat(result.getTotalCount(), equalTo(3));
		assertThat(result.getResult(), hasItems(allOf(hasProperty(WorkflowActionModel.CODE, equalTo(WORKFLOW_START_ACTION)))));
	}

}
