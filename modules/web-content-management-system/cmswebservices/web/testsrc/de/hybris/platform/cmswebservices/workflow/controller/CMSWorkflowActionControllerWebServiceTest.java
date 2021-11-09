/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflow.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_CURRENT_PAGE;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_SIZE;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowDecisionTemplateModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.CMSCommentListWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowActionWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowOperationWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowWsDTO;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class CMSWorkflowActionControllerWebServiceTest extends WorkflowBaseIntegrationTest
{
	protected static final String WORKFLOW_ACTIONS_ENDPOINT = WORKFLOW_ENDPOINT + "/{workflowId}/actions";
	protected static final String WORKFLOW_ACTION_COMMENTS_ENDPOINT = WORKFLOW_ACTIONS_ENDPOINT + "/{workflowActionId}/comments";

	private static final String COMMENT_TEXT = "text";
	private static final String DECISION_CODE = "decisionCode";
	private static final String ACTION_CODE = "originalActionCode";
	private static final String CREATED_AGO = "createdAgoInMillis";
	private static final String AUTHOR_NAME = "authorName";

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_CURRENT_PAGE = 0;

	@Resource
	private ContentPageModelMother contentPageModelMother;

	private ContentPageModel contentPage;
	private WorkflowModel workflowModel;

	@Before
	public void setUp() throws Exception
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		contentPage = contentPageModelMother.homePage(catalogVersion);
		workflowModel = createApprovalWorkflowForPage(contentPage);
	}

	@Test
	public void shouldReturnAllActionsAndDecisionsForAWorkflow()
	{
		// WHEN
		final List<CMSWorkflowActionWsDTO> actions = getAllActions(workflowModel.getCode());

		// THEN
		assertThat(actions, hasSize(1));
		assertThat(actions, hasItems( //
				allOf(hasProperty(WorkflowActionModel.ACTIONTYPE, is(WorkflowActionType.START.name())),
						hasProperty(WorkflowActionModel.NAME, is(workflowActionTemplateModelMother.START_ACTION_NAME)), //
						hasProperty(WorkflowActionModel.STATUS, is(WorkflowActionStatus.IN_PROGRESS.name())), //
						hasProperty("isCurrentUserParticipant", is(true)) //
				)));

		assertThat(actions.get(0).getDecisions(), hasSize(2));
		assertThat(actions.get(0).getDecisions(), hasItems( //
				allOf(hasProperty(WorkflowDecisionModel.CODE, is(workflowDecisionTemplateModelMother.APPROVE_DECISION)),
						hasProperty(WorkflowDecisionModel.NAME, is(workflowDecisionTemplateModelMother.APPROVE_DECISION))), //
				allOf(hasProperty(WorkflowDecisionModel.CODE, is(workflowDecisionTemplateModelMother.REJECT_DECISION)),
						hasProperty(WorkflowDecisionModel.NAME, is(workflowDecisionTemplateModelMother.REJECT_DECISION)) //
				)));
	}

	@Test
	public void shouldReturnAllActionCommentsForAWorkflowAndAnAction() throws Exception
	{
		// GIVEN
		final List<CMSWorkflowActionWsDTO> actions = getAllActions(workflowModel.getCode());
		final CMSWorkflowActionWsDTO cmsWorkflowActionWsDTO = actions.get(0);
		assertThat(cmsWorkflowActionWsDTO.getDecisions(), hasSize(2));

		final String comment = "Comment for making reject decision";
		makeDecisionForAction(cmsWorkflowActionWsDTO.getCode(), WorkflowDecisionTemplateModelMother.REJECT_DECISION, comment);

		final String commentsEndpoint = buildWorkflowActionCommentsEndpoint(ID_APPLE.name(), STAGED.getVersion(),
				workflowModel.getCode(), cmsWorkflowActionWsDTO.getCode());

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(commentsEndpoint) //
				.queryParam(URI_PAGE_SIZE, DEFAULT_PAGE_SIZE)
				.queryParam(URI_CURRENT_PAGE, DEFAULT_CURRENT_PAGE)
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Response.Status.OK, response);

		final CMSCommentListWsDTO entity = response.readEntity(CMSCommentListWsDTO.class);

		assertThat(entity.getComments(), hasSize(1));
		assertThat(entity.getComments(), hasItems(
				allOf(hasProperty(COMMENT_TEXT, is(comment))),
				allOf(hasProperty(DECISION_CODE, is(WorkflowDecisionTemplateModelMother.REJECT_DECISION))),
				allOf(hasProperty(ACTION_CODE, is(cmsWorkflowActionWsDTO.getCode()))),
				allOf(hasProperty(CREATED_AGO)),
				allOf(hasProperty(AUTHOR_NAME, is(userService.getCurrentUser().getName())))
		));
	}

	/**
	 * Makes the decision for the action for the provided action id, decision id and the comment
	 *
	 * @param actionId
	 *           the action id
	 * @param decisionId
	 *           the decision id
	 * @param comment
	 *           the comment
	 * @throws JAXBException
	 */
	protected void makeDecisionForAction(final String actionId, final String decisionId, final String comment) throws JAXBException
	{
		final String operationsEndpoint = buildOperationsEndpoint(workflowModel.getCode());

		final CMSWorkflowOperationWsDTO cmsWorkflowOperationWsDTO = buildMakeDecisionOperationDto(actionId, decisionId, comment);

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(operationsEndpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.post(Entity
						.entity(marshallDto(cmsWorkflowOperationWsDTO, CMSWorkflowOperationWsDTO.class), MediaType.APPLICATION_JSON));

		assertResponse(Response.Status.OK, response);
	}

	/**
	 * Retrieves the list of actions for the given workflowId
	 *
	 * @param workflowId
	 *           the workflow id
	 * @return the list of {@link CMSWorkflowActionWsDTO} object
	 */
	protected List<CMSWorkflowActionWsDTO> getAllActions(final String workflowId)
	{
		final String endpoint = buildWorkflowActionsEndpoint(ID_APPLE.name(), STAGED.getVersion(), workflowId);

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final CMSWorkflowWsDTO entity = response.readEntity(CMSWorkflowWsDTO.class);

		return entity.getActions();
	}

	/**
	 * Builds and returns the workflow actions endpoint for the provided catalog id, catalog version name and workflow id.
	 *
	 * @param catalogId
	 *           the catalog Id.
	 * @param versionId
	 *           the catalog version name.
	 * @param workflowCode
	 *           the workflow code.
	 * @return the workflow actions endpoint.
	 */
	protected String buildWorkflowActionsEndpoint(final String catalogId, final String versionId, final String workflowCode)
	{
		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID, catalogId);
		endPointParams.put(CATALOG_VERSION_ID, versionId);
		endPointParams.put(WORKFLOW_ID, workflowCode);

		return replaceUriVariablesWithDefaults(WORKFLOW_ACTIONS_ENDPOINT, endPointParams);
	}

	/**
	 * Builds and returns the workflow action comments endpoint for the provided catalog id, catalog version name, workflow id and workflow action id.
	 *
	 * @param catalogId
	 *           the catalog Id.
	 * @param versionId
	 *           the catalog version name.
	 * @param workflowCode
	 *           the workflow code.
	 * @param actionCode
	 *           the workflow action code.
	 * @return the workflow action comments endpoint.
	 */
	protected String buildWorkflowActionCommentsEndpoint(final String catalogId, final String versionId, final String workflowCode,
			final String actionCode)
	{
		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID, catalogId);
		endPointParams.put(CATALOG_VERSION_ID, versionId);
		endPointParams.put(WORKFLOW_ID, workflowCode);
		endPointParams.put(WORKFLOW_ACTION_ID, actionCode);

		return replaceUriVariablesWithDefaults(WORKFLOW_ACTION_COMMENTS_ENDPOINT, endPointParams);
	}
}
