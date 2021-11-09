/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflow.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowActionTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowDecisionTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowTemplateModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowOperationWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowWsDTO;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;


@Ignore("Helper methods for cms workflow integration tests")
@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
@ContextConfiguration(locations =
{ "classpath:/cmswebservices-spring-test.xml" })
public class WorkflowBaseIntegrationTest extends ApiBaseIntegrationTest
{
	protected static final String WORKFLOW_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/workflows";
	protected static final String EDIT_WORKFLOW_ENDPOINT = WORKFLOW_ENDPOINT + "/{workflowId}";
	private static final String OPERATIONS_ENDPOINT = WORKFLOW_ENDPOINT + "/{workflowId}/operations";

	protected static final String CATALOG_ID = "catalogId";
	protected static final String CATALOG_VERSION_ID = "versionId";
	protected static final String WORKFLOW_ID = "workflowId";
	protected static final String WORKFLOW_ACTION_ID = "workflowActionId";

	protected CatalogVersionModel catalogVersion;

	@Resource
	protected UserService userService;

	@Resource
	protected CatalogVersionModelMother catalogVersionModelMother;

	@Resource
	protected WorkflowModelMother workflowModelMother;

	@Resource
	protected WorkflowTemplateModelMother workflowTemplateModelMother;

	@Resource
	protected WorkflowActionTemplateModelMother workflowActionTemplateModelMother;

	@Resource
	protected WorkflowDecisionTemplateModelMother workflowDecisionTemplateModelMother;

	@Before
	public void setUpWorkflowTestData() throws Exception
	{
		importCsv("/test/cmsTypePermissionTestData.impex", "UTF-8");

		final UserModel cmsmanager = userService.getUserForUID("cmsmanager");
		userService.setCurrentUser(cmsmanager);
	}

	/**
	 * Builds and returns the workflow endpoint for the provided catalogId and catalog version name.
	 *
	 * @param catalogId
	 *           the catalog Id.
	 * @param versionId
	 *           the catalog version name.
	 * @return the workflow endpoint.
	 */
	protected String buildWorkflowEndpoint(final String catalogId, final String versionId)
	{
		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID, catalogId);
		endPointParams.put(CATALOG_VERSION_ID, versionId);

		return replaceUriVariablesWithDefaults(WORKFLOW_ENDPOINT, endPointParams);
	}

	/**
	 * Builds and returns the workflow endpoint for the provided catalogId, catalog version name and workflow code.
	 *
	 * @param catalogId
	 *           the catalog Id.
	 * @param versionId
	 *           the catalog version name.
	 * @param workflowCode
	 *           the workflow code.
	 * @return the workflow endpoint.
	 */
	protected String buildEditWorkflowEndpoint(final String catalogId, final String versionId, final String workflowCode)
	{
		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID, catalogId);
		endPointParams.put(CATALOG_VERSION_ID, versionId);
		endPointParams.put(WORKFLOW_ID, workflowCode);

		return replaceUriVariablesWithDefaults(EDIT_WORKFLOW_ENDPOINT, endPointParams);
	}

	/**
	 * Builds and returns the {@link CMSWorkflowWsDTO} for the provided input params.
	 *
	 * @param templateCode
	 *           the workflow template code.
	 * @param description
	 *           description for the workflow.
	 * @param attachments
	 *           the list of attachements for the workflow.
	 * @return the {@link CMSWorkflowWsDTO} object.
	 */
	protected CMSWorkflowWsDTO buildWorkflowDto(final String templateCode, final String description,
			final List<String> attachments)
	{
		final CMSWorkflowWsDTO dto = new CMSWorkflowWsDTO();
		dto.setCreateVersion(false);
		dto.setTemplateCode(templateCode);
		dto.setDescription(description);
		dto.setAttachments(attachments);

		return dto;
	}

	protected Response createAndStartWorkflow(final String endpoint, final CMSWorkflowWsDTO requestDto) throws JAXBException
	{
		return getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.post(Entity.entity(marshallDto(requestDto, CMSWorkflowWsDTO.class), MediaType.APPLICATION_JSON));
	}

	/**
	 * Creates and starts a workflow for a given page using workflow model mother
	 *
	 * @param page
	 *           the cms item page
	 * @return the {@link WorkflowModel} object
	 */
	protected WorkflowModel createApprovalWorkflowForPage(final CMSItemModel page)
	{
		final WorkflowTemplateModel workflowTemplateModel = workflowTemplateModelMother.createApprovalWorkflowTemplate(Collections
				.singletonList(catalogVersion));
		final WorkflowActionTemplateModel workflowActionTemplateModel = workflowActionTemplateModelMother
				.startApprovalWorkflowAction(Collections.singletonList(catalogVersion));
		workflowDecisionTemplateModelMother.createWorkflowApproveDecision(workflowActionTemplateModel);
		workflowDecisionTemplateModelMother.createWorkflowRejectDecision(workflowActionTemplateModel);

		return workflowModelMother
				.createAndStartApprovalWorkflow(workflowTemplateModel, Collections.singletonList(page));
	}

	/**
	 * Builds and returns the workflow operation endpoint for the provided workflowId
	 *
	 * @param workflowId
	 *           the workflow id
	 * @return the workflow operations endpoint
	 */
	protected String buildOperationsEndpoint(final String workflowId)
	{
		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID, ID_APPLE.name());
		endPointParams.put(CATALOG_VERSION_ID, STAGED.getVersion());
		endPointParams.put(WORKFLOW_ID, workflowId);

		return replaceUriVariablesWithDefaults(OPERATIONS_ENDPOINT, endPointParams);
	}

	/**
	 * Builds and returns the {@link CMSWorkflowOperationWsDTO} for the provided actionId, decisionId and comment.
	 *
	 * @param actionId
	 *           the action id
	 * @param decisionId
	 *           the decision id
	 * @param comment
	 *           the comment string
	 * @return the {@link CMSWorkflowOperationWsDTO} object
	 */
	protected CMSWorkflowOperationWsDTO buildMakeDecisionOperationDto(final String actionId, final String decisionId,
			final String comment)
	{
		final CMSWorkflowOperationWsDTO dto = new CMSWorkflowOperationWsDTO();
		dto.setOperation(CMSWorkflowOperation.MAKE_DECISION.name());
		dto.setActionCode(actionId);
		dto.setComment(comment);
		dto.setDecisionCode(decisionId);
		return dto;
	}
}

