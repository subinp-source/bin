/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSWorkflowDaoIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String IN_WORKFLOW_HOMEPAGE = "homepage";
	private static final String IN_WORKFLOW_PRODUCT_PAGE = "productPage";
	private static final String OUTSIDE_WORKFLOW_PAGE = "notInWorkflowPage";

	private static final String WORKFLOW_1_NAME = "workflow1";
	private static final String WORKFLOW_2_NAME = "workflow2";
	private static final String WORKFLOW_INVALID_NAME = "InvalidWorkflow";

	private static final String CATALOG_ID = "cms_Catalog";
	private static final String CATALOG_VERSION_NAME = "CatalogVersion1";

	private CatalogVersionModel catalogVersion;
	private AbstractPageModel homepageInWorkflow;
	private AbstractPageModel productPageInWorkflow;
	private AbstractPageModel pageOutsideWorkflow;

	@Resource
	private CMSWorkflowDao cmsWorkflowDao;
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

		catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_NAME);
		// homepage is in workflow1
		homepageInWorkflow = cmsAdminPageService.getPageForId(IN_WORKFLOW_HOMEPAGE, Arrays.asList(catalogVersion));
		// productpage is in workflow2
		productPageInWorkflow = cmsAdminPageService.getPageForId(IN_WORKFLOW_PRODUCT_PAGE, Arrays.asList(catalogVersion));
		pageOutsideWorkflow = cmsAdminPageService.getPageForId(OUTSIDE_WORKFLOW_PAGE, Arrays.asList(catalogVersion));
	}

	@Test
	public void shouldFindAllWorkflowsForAttachedItems()
	{
		// WHEN
		final List<WorkflowModel> result = cmsWorkflowDao.findAllWorkflowsByAttachedItems(
				Arrays.asList(homepageInWorkflow, productPageInWorkflow, pageOutsideWorkflow), CMS_WORKFLOW_ACTIVE_STATUSES);

		// THEN
		assertFalse(result.isEmpty());
		assertThat(result, hasItems( //
				allOf(hasProperty(WorkflowModel.CODE, equalTo(WORKFLOW_1_NAME))),
				allOf(hasProperty(WorkflowModel.CODE, equalTo(WORKFLOW_2_NAME)))));
	}

	@Test
	public void shouldFindEmptyResultWhenItemNotAttachedToAnyWorkflow()
	{
		// WHEN
		final List<WorkflowModel> result = cmsWorkflowDao.findAllWorkflowsByAttachedItems(
				Collections.singletonList(pageOutsideWorkflow), //
				CMS_WORKFLOW_ACTIVE_STATUSES);

		// THEN
		assertTrue(result.isEmpty());
	}

	@Test
	public void shouldFindEmptyResultForInvalidWorkflowCode()
	{
		// WHEN
		final Optional<WorkflowModel> result = cmsWorkflowDao.findWorkflowForCode(WORKFLOW_INVALID_NAME);

		// THEN
		assertFalse(result.isPresent());
	}

	@Test
	public void shouldFindWorkflowByCode()
	{
		// WHEN
		final Optional<WorkflowModel> result = cmsWorkflowDao.findWorkflowForCode(WORKFLOW_1_NAME);

		// THEN
		assertThat(result.get(), hasProperty(WorkflowModel.CODE, is(WORKFLOW_1_NAME)));
	}

	@Test
	public void shouldFindWorkflowsForAttachedItemsByPage()
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setPageSize(1);

		// WHEN
		final SearchResult<WorkflowModel> result = cmsWorkflowDao.findWorkflowsByAttachedItems(
				Arrays.asList(homepageInWorkflow, productPageInWorkflow, pageOutsideWorkflow), CMS_WORKFLOW_ACTIVE_STATUSES,
				pageableData);

		// THEN
		assertThat(result.getCount(), equalTo(1));
		assertThat(result.getTotalCount(), equalTo(2));
		assertThat(result.getResult(), hasItems( //
				anyOf( //
						hasProperty(WorkflowModel.CODE, equalTo(WORKFLOW_1_NAME)),
						hasProperty(WorkflowModel.CODE, equalTo(WORKFLOW_2_NAME)) //
				) //
		));
	}


}
