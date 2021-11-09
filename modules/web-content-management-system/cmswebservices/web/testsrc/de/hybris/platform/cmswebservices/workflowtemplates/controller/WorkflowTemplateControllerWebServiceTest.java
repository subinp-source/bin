/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflowtemplates.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED2;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_LAPTOPS;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.util.models.CMSSiteModelMother;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.WorkflowTemplateModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.WorkflowTemplateListWsDTO;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class WorkflowTemplateControllerWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/workflowtemplates";

	private static final String CATALOG_ID_PARAM = "catalogId";
	private static final String CATALOG_VERSION_ID_PARAM = "versionId";

	@Resource
	private CMSSiteModelMother cmsSiteModelMother;

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;

	@Resource
	private WorkflowTemplateModelMother workflowTemplateModelMother;

	private CatalogVersionModel catalogVersion1;
	private CatalogVersionModel catalogVersion2;
	private CatalogVersionModel catalogVersion3;

	@Before
	public void setUp() throws ImpExException
	{

		setupMockCatalogVersions();

		workflowTemplateModelMother.createApprovalWorkflowTemplate(Arrays.asList(catalogVersion1));
		workflowTemplateModelMother.createTranslationWorkflowTemplate(Arrays.asList(catalogVersion1, catalogVersion2));
	}

	@Test
	public void shouldReturnWorkFlowTemplatesForCatalogVersion()
	{

		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID_PARAM, ID_APPLE.name());
		endPointParams.put(CATALOG_VERSION_ID_PARAM, STAGED.getVersion());

		final String endPoint = replaceUriVariablesWithDefaults(ENDPOINT, endPointParams);

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endPoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final WorkflowTemplateListWsDTO entity = response.readEntity(WorkflowTemplateListWsDTO.class);

		assertThat(entity.getTemplates(), hasItems( //
				allOf(hasProperty(WorkflowTemplateModel.CODE, equalTo(workflowTemplateModelMother.APPROVAL_WORKFLOW_TEMPLATE_CODE)),
						hasProperty(WorkflowTemplateModel.NAME, equalTo(workflowTemplateModelMother.APPROVAL_WORKFLOW_TEMPLATE_NAME))), //
				allOf(hasProperty(WorkflowTemplateModel.CODE,
						equalTo(workflowTemplateModelMother.TRANSLATION_WORKFLOW_TEMPLATE_CODE)),
						hasProperty(WorkflowTemplateModel.NAME, equalTo(workflowTemplateModelMother.TRANSLATION_WORKFLOW_TEMPLATE_NAME)) //
				)));

	}

	@Test
	public void shouldReturnEmptyForCatalogVersionWithNoTemplates()
	{

		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(CATALOG_ID_PARAM, ID_LAPTOPS.name());
		endPointParams.put(CATALOG_VERSION_ID_PARAM, STAGED2.getVersion());

		final String endPoint = replaceUriVariablesWithDefaults(ENDPOINT, endPointParams);

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endPoint) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Response.Status.OK, response);

		final WorkflowTemplateListWsDTO entity = response.readEntity(WorkflowTemplateListWsDTO.class);
		assertThat(entity.getTemplates(), empty());

	}

	protected void setupMockCatalogVersions()
	{
		catalogVersion1 = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		catalogVersion2 = catalogVersionModelMother.createLaptopStaged1CatalogVersionModel();
		catalogVersion3 = catalogVersionModelMother.createLaptopStaged2CatalogVersionModel();
	}

}
