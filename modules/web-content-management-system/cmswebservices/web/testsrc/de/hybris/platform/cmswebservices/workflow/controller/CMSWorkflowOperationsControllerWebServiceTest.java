/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflow.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowOperationWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowWsDTO;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
public class CMSWorkflowOperationsControllerWebServiceTest extends WorkflowBaseIntegrationTest
{
	private static final String WORKFLOW_CODE = "workflowCode";

	private static final String INVALID_WORKFLOW_ID = "invalidWorkflowId";

	private static final String UNKNOWN_IDENTIFIER_ERROR_TYPE = "UnknownIdentifierError";
	private static final String ILLEGAL_ARGUMENT_ERROR_TYPE = "IllegalArgumentError";

	private ContentPageModel contentPage;
	private WorkflowModel workflowModel;

	@Resource
	private ModelService modelService;

	@Resource
	private ContentPageModelMother contentPageModelMother;

	@Before
	public void setUp() throws Exception
	{
		setupMockCatalogVersions();
		contentPage = contentPageModelMother.homePage(catalogVersion);

		workflowModel = createApprovalWorkflowForPage(contentPage);
	}

	@Test
	public void givenAnInvalidWorkflowCodeThenCancelWorkflowEndpointWillThrowUnknownIdentifierException() throws JAXBException
	{
		final String endpoint = buildOperationsEndpoint(INVALID_WORKFLOW_ID);
		final CMSWorkflowOperationWsDTO requestDto = buildCancelOperationDto();

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.post(Entity.entity(marshallDto(requestDto, CMSWorkflowOperationWsDTO.class), MediaType.APPLICATION_JSON));

		assertResponse(Response.Status.NOT_FOUND, response);

		final ErrorListWsDTO result = response.readEntity(ErrorListWsDTO.class);
		assertThat(result.getErrors(), iterableWithSize(1));
		final Set<String> types = result.getErrors().stream().map(ErrorWsDTO::getType).collect(Collectors.toSet());
		assertThat(types.contains(UNKNOWN_IDENTIFIER_ERROR_TYPE), is(true));
	}

	@Test
	public void givenAnInvalidPayloadThenCancelWorkflowEndpointWillThrowAnException() throws JAXBException
	{
		final String endpoint = buildOperationsEndpoint(INVALID_WORKFLOW_ID);

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.post(Entity.entity(marshallDto(new CMSWorkflowOperationWsDTO(), CMSWorkflowOperationWsDTO.class),
						MediaType.APPLICATION_JSON));

		assertResponse(Response.Status.BAD_REQUEST, response);

		final ErrorListWsDTO result = response.readEntity(ErrorListWsDTO.class);
		assertThat(result.getErrors(), iterableWithSize(1));
		final Set<String> types = result.getErrors().stream().map(ErrorWsDTO::getType).collect(Collectors.toSet());
		assertThat(types.contains(ILLEGAL_ARGUMENT_ERROR_TYPE), is(true));
	}

	@Test
	public void givenAValidWorkflowCodeThenCancelWorkflowEndpointWillCancelTheWorkflow() throws JAXBException
	{
		final String endpoint = buildOperationsEndpoint(workflowModel.getCode());
		final CMSWorkflowOperationWsDTO requestDto = buildCancelOperationDto();

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.post(Entity.entity(marshallDto(requestDto, CMSWorkflowOperationWsDTO.class), MediaType.APPLICATION_JSON));

		assertResponse(Response.Status.OK, response);

		final CMSWorkflowWsDTO entity = response.readEntity(CMSWorkflowWsDTO.class);
		assertThat(entity, allOf(hasProperty(WorkflowModel.STATUS, is(CronJobStatus.ABORTED.getCode())), //
				hasProperty(WORKFLOW_CODE, is(workflowModel.getCode())) //
		));
	}


	protected void setupMockCatalogVersions()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
	}


	protected CMSWorkflowOperationWsDTO buildCancelOperationDto()
	{
		final CMSWorkflowOperationWsDTO dto = new CMSWorkflowOperationWsDTO();
		dto.setOperation(CMSWorkflowOperation.CANCEL_WORKFLOW.name());
		return dto;
	}

}
