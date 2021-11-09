/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.inbox.controller;

import static de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants.URI_CURRENT_PAGE;
import static de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants.URI_PAGE_SIZE;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowActionTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowDecisionTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowTemplateModelMother;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowActionWsDTO;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowAttachmentData;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowTaskListWsDTO;
import de.hybris.platform.cmssmarteditwebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmssmarteditwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class InboxControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String WORKFLOW_TASKS_ENDPOINT = "v1/inbox/workflowtasks";
	private static final String PAGE_UID = "pageUid";
	private static final String PAGE_NAME = "pageName";
	private static final String CATALOG_ID = "catalogId";
	private static final String CATALOG_NAME = "catalogName";
	private static final String CATALOG_VERSION = "catalogVersion";
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_CURRENT_PAGE = 0;

	private ContentPageModel contentPage;
	protected CatalogVersionModel catalogVersion;

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentPageModelMother contentPageModelMother;
	@Resource
	private WorkflowModelMother workflowModelMother;
	@Resource
	private WorkflowTemplateModelMother workflowTemplateModelMother;
	@Resource
	private WorkflowActionTemplateModelMother workflowActionTemplateModelMother;
	@Resource
	private WorkflowDecisionTemplateModelMother workflowDecisionTemplateModelMother;
	@Resource
	private UserService userService;

	@Before
	public void setup() throws Exception
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		contentPage = contentPageModelMother.homePage(catalogVersion);

		importCsv("/test/cmsTypePermissionTestData.impex", "UTF-8");

		final UserModel cmsmanager = userService.getUserForUID("cmsmanager");
		userService.setCurrentUser(cmsmanager);
	}

	@Test
	public void shouldFindAllWorkflowTasks() throws JAXBException
	{
		createApprovalWorkflowForPage(contentPage);

		final Response response = getCmsManagerWsSecuredRequestBuilder().path(WORKFLOW_TASKS_ENDPOINT)
				.queryParam(URI_PAGE_SIZE, DEFAULT_PAGE_SIZE).queryParam(URI_CURRENT_PAGE, DEFAULT_CURRENT_PAGE).build()
				.accept(MediaType.APPLICATION_JSON).get();

		assertResponse(Response.Status.OK, response);

		final CMSWorkflowTaskListWsDTO entity = response.readEntity(CMSWorkflowTaskListWsDTO.class);
		assertThat(entity.getPagination().getCount(), equalTo(1));
		assertThat(entity.getPagination().getTotalCount(), equalTo(1L));
		assertThat(entity.getPagination().getPage(), equalTo(0));

		assertThat(entity.getTasks(), hasSize(1));

		final CMSWorkflowActionWsDTO action = entity.getTasks().get(0).getAction();
		assertThat(action.isIsCurrentUserParticipant(), is(true));
		assertThat(action.getStatus(), is(WorkflowActionStatus.IN_PROGRESS.name()));

		final List<CMSWorkflowAttachmentData> attachments = entity.getTasks().get(0).getAttachments();
		assertThat(attachments, hasSize(1));
		assertThat(attachments,
				hasItems(allOf(hasProperty(PAGE_UID, is(ContentPageModelMother.UID_HOMEPAGE))),
						allOf(hasProperty(PAGE_NAME, is(ContentPageModelMother.NAME_HOMEPAGE))),
						allOf(hasProperty(CATALOG_ID, is(ContentCatalogModelMother.CatalogTemplate.ID_APPLE.name()))),
						allOf(hasProperty(CATALOG_NAME,
								is(ContentCatalogModelMother.CatalogTemplate.ID_APPLE.getFirstInstanceOfHumanName()))),
						allOf(hasProperty(CATALOG_VERSION, is(CatalogVersionModelMother.CatalogVersion.STAGED.getVersion())))));
	}

	protected WorkflowModel createApprovalWorkflowForPage(final CMSItemModel page)
	{
		final WorkflowTemplateModel workflowTemplateModel = workflowTemplateModelMother
				.createApprovalWorkflowTemplate(Collections.singletonList(catalogVersion));
		final WorkflowActionTemplateModel workflowActionTemplateModel = workflowActionTemplateModelMother
				.startApprovalWorkflowAction(Collections.singletonList(catalogVersion));
		workflowDecisionTemplateModelMother.createWorkflowApproveDecision(workflowActionTemplateModel);
		workflowDecisionTemplateModelMother.createWorkflowRejectDecision(workflowActionTemplateModel);

		return workflowModelMother.createAndStartApprovalWorkflow(workflowTemplateModel, Collections.singletonList(page));

	}
}
