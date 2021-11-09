/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflow.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static de.hybris.platform.cmsfacades.util.models.WorkflowTemplateModelMother.APPROVAL_WORKFLOW_TEMPLATE_CODE;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_CURRENT_PAGE;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_SIZE;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowTemplateModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowListWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowWsDTO;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Collections;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class CMSWorkflowControllerWebServiceTest extends WorkflowBaseIntegrationTest
{
	private static final String FIELD_ATTACHMENT = "attachment";
	private static final String FIELD_STATUSES = "statuses";
	private static final String FIELD_WORKFLOW_CODE = "workflowCode";
	private static final String FIELD_TEMPLATE_CODE = "templateCode";

	private static final String INVALID_TEMPLATE_CODE = "invalidTemplate";
	private static final String INVALID_WORKFLOW_ID = "invalidWorkflowId";

	private static final String WORKFLOW_DESCRIPTION = "workflowDescription";
	private static final String UPDATE_WORKFLOW_DESCRIPTION = "newWorkflowDescription";

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_CURRENT_PAGE = 0;

	private ContentPageModel contentPage;

	@Resource
	private ContentPageModelMother contentPageModelMother;

	@Before
	public void setUp()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		contentPage = contentPageModelMother.homePage(catalogVersion);
	}

	@Test
	public void givenValidPayload_WhenCreateWorkflowCalled_ThenItCreatesWorkflow() throws Exception
	{
		// GIVEN
		workflowActionTemplateModelMother.startApprovalWorkflowAction(Collections.singletonList(catalogVersion));

		final String endpoint = buildWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion());
		final String pageUuid = getUuid(catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), contentPage.getUid());
		final CMSWorkflowWsDTO requestDto = buildWorkflowDto( //
				APPROVAL_WORKFLOW_TEMPLATE_CODE, WORKFLOW_DESCRIPTION, Collections.singletonList(pageUuid));

		// WHEN
		final Response response = createAndStartWorkflow(endpoint, requestDto);

		// THEN
		assertResponse(Response.Status.CREATED, response);

		final CMSWorkflowWsDTO entity = response.readEntity(CMSWorkflowWsDTO.class);
		assertThat(entity, allOf( //
				hasProperty(WorkflowModel.DESCRIPTION, is(WORKFLOW_DESCRIPTION)), //
				hasProperty(WorkflowModel.ATTACHMENTS, is(requestDto.getAttachments())), //
				hasProperty(WorkflowModel.STATUS, is(CronJobStatus.RUNNING.getCode())), //
				hasProperty(FIELD_TEMPLATE_CODE, is(WorkflowTemplateModelMother.APPROVAL_WORKFLOW_TEMPLATE_CODE)), //
				hasProperty(FIELD_WORKFLOW_CODE, notNullValue()) //
		));

		assertThat(contentPage.getApprovalStatus(), is(CmsApprovalStatus.CHECK));
	}

	@Test
	public void givenInvalidTemplateCode_WhenCreateWorkflowCalled_ThenItReturnsError() throws Exception
	{
		// GIVEN
		workflowActionTemplateModelMother.startApprovalWorkflowAction(Collections.singletonList(catalogVersion));

		final String endpoint = buildWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion());
		final String pageUuid = getUuid(catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), contentPage.getUid());
		final CMSWorkflowWsDTO requestDto = buildWorkflowDto( //
				INVALID_TEMPLATE_CODE, WORKFLOW_DESCRIPTION, Collections.singletonList(pageUuid));

		// WHEN
		final Response response = createAndStartWorkflow(endpoint, requestDto);

		// THEN
		assertResponse(Response.Status.BAD_REQUEST, response);
	}

	@Test
	public void givenInvalidPayload_WhenCreateWorkflowCalled_ThenItReturnsError() throws Exception
	{
		// GIVEN
		workflowActionTemplateModelMother.startApprovalWorkflowAction(Collections.singletonList(catalogVersion));

		final String endpoint = buildWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion());
		final CMSWorkflowWsDTO requestDto = buildWorkflowDto( //
				APPROVAL_WORKFLOW_TEMPLATE_CODE, WORKFLOW_DESCRIPTION, null);

		// WHEN
		final Response response = createAndStartWorkflow(endpoint, requestDto);

		// THEN
		assertResponse(Response.Status.BAD_REQUEST, response);
	}

	@Test
	public void shouldFindAllWorkflows()
	{
		createApprovalWorkflowForPage(contentPage);

		final String endpoint = buildWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion());
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.queryParam(URI_PAGE_SIZE, DEFAULT_PAGE_SIZE) //
				.queryParam(URI_CURRENT_PAGE, DEFAULT_CURRENT_PAGE) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final CMSWorkflowListWsDTO entity = response.readEntity(CMSWorkflowListWsDTO.class);
		assertThat(entity.getPagination().getCount(), equalTo(1));
		assertThat(entity.getPagination().getTotalCount(), equalTo(1L));
		assertThat(entity.getPagination().getPage(), equalTo(0));
	}

	@Test
	public void shouldFindAllAbortedWorkflows()
	{
		createApprovalWorkflowForPage(contentPage);

		final String endpoint = buildWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion());
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.queryParam(FIELD_STATUSES, CronJobStatus.ABORTED.getCode()) //
				.queryParam(URI_PAGE_SIZE, DEFAULT_PAGE_SIZE) //
				.queryParam(URI_CURRENT_PAGE, DEFAULT_CURRENT_PAGE) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final CMSWorkflowListWsDTO entity = response.readEntity(CMSWorkflowListWsDTO.class);
		assertThat(entity.getPagination().getCount(), equalTo(0));
		assertThat(entity.getPagination().getTotalCount(), equalTo(0L));
		assertThat(entity.getPagination().getPage(), equalTo(0));
	}

	@Test
	public void shouldFindAllActiveWorkflowsForAttachedItem()
	{
		createApprovalWorkflowForPage(contentPage);

		final String endpoint = buildWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion());
		final String pageUUid = getUuid(catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), contentPage.getUid());
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.queryParam(FIELD_ATTACHMENT, pageUUid) //
				.queryParam(FIELD_STATUSES, CronJobStatus.RUNNING.getCode()) //
				.queryParam(URI_PAGE_SIZE, DEFAULT_PAGE_SIZE) //
				.queryParam(URI_CURRENT_PAGE, DEFAULT_CURRENT_PAGE) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final CMSWorkflowListWsDTO entity = response.readEntity(CMSWorkflowListWsDTO.class);
		assertThat(entity.getPagination().getCount(), equalTo(1));
		assertThat(entity.getPagination().getTotalCount(), equalTo(1L));
		assertThat(entity.getPagination().getPage(), equalTo(0));
	}

	@Test
	public void givenInvalidWorkflowId_WhenEditWorkflowCalled_ThenItReturnsError() throws Exception
	{
		// WHEN
		final String endpoint = buildEditWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion(), INVALID_WORKFLOW_ID);
		final CMSWorkflowWsDTO requestDto = buildWorkflowDto(null, null, null);

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.put(Entity.entity(marshallDto(requestDto, CMSWorkflowWsDTO.class), MediaType.APPLICATION_JSON));

		// THEN
		assertResponse(Response.Status.NOT_FOUND, response);
	}

	@Test
	public void givenValidWorkflowIdAndInvalidVersion_WhenEditWorkflowCalled_ThenItReturnsValidationError() throws Exception
	{
		// GIVEN
		final WorkflowModel workflowModel = createApprovalWorkflowForPage(contentPage);
		final String endpoint = buildEditWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion(), workflowModel.getCode());
		final CMSWorkflowWsDTO requestDto = buildWorkflowDto(null, null, Collections.singletonList("FAKE_PAGE"));

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.put(Entity.entity(marshallDto(requestDto, CMSWorkflowWsDTO.class), MediaType.APPLICATION_JSON));

		// THEN
		assertResponse(Response.Status.BAD_REQUEST, response);
	}

	@Test
	public void givenValidPayload_WhenEditWorkflowCalled_ThenItUpdatesTheItem() throws Exception
	{
		// GIVEN
		final WorkflowModel workflowModel = createApprovalWorkflowForPage(contentPage);
		final String endpoint = buildEditWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion(), workflowModel.getCode());
		final CMSWorkflowWsDTO requestDto = buildWorkflowDto(null, UPDATE_WORKFLOW_DESCRIPTION, null);

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.put(Entity.entity(marshallDto(requestDto, CMSWorkflowWsDTO.class), MediaType.APPLICATION_JSON));

		// THEN
		assertResponse(Response.Status.OK, response);

		final CMSWorkflowWsDTO entity = response.readEntity(CMSWorkflowWsDTO.class);
		assertThat(entity, allOf( //
				hasProperty(WorkflowModel.DESCRIPTION, is(UPDATE_WORKFLOW_DESCRIPTION)), //
				hasProperty(FIELD_WORKFLOW_CODE, is(workflowModel.getCode())) //
		));
	}

	@Test
	public void givenInvalidWorkflowId_WhenGetWorkflowCalled_ThenItReturnsNotFoundError()
	{
		// GIVEN
		final String endpoint = buildEditWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion(), INVALID_WORKFLOW_ID);

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Response.Status.NOT_FOUND, response);
	}

	@Test
	public void givenValidWorkflowId_WhenGetWorkflowCalled_ThenItReturnsItem()
	{
		// GIVEN
		final WorkflowModel workflowModel = createApprovalWorkflowForPage(contentPage);
		final String endpoint = buildEditWorkflowEndpoint(ID_APPLE.name(), STAGED.getVersion(), workflowModel.getCode());

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Response.Status.OK, response);
	}

}
