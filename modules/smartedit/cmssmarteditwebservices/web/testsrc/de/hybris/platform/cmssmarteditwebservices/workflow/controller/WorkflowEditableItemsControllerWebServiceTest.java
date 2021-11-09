/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.workflow.controller;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowEditableItemListWsDTO;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowEditableItemWsDTO;
import de.hybris.platform.cmssmarteditwebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions = { CmssmarteditwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class WorkflowEditableItemsControllerWebServiceTest extends ApiBaseIntegrationTest
{
	protected static final String WORKFLOW_EDITABLE_ITEMS_URL = "/v1/catalogs/{catalogId}/versions/{versionId}/workfloweditableitems";
	public static final String CMS_CATALOG = "cms_Catalog";
	public static final String CATALOG_VERSION = "CatalogVersion1";

	private static final String INVALID_UID = "InvalidUid";
	private static final String VALID_UID1 = "LinkInSlot";
	private static final String NOT_IN_WORKFLOW_ITEM = "notInWorkflowPage";

	@Before
	public void setUp() throws ImpExException
	{
		ServicelayerTest.importCsv("/test/cmsWorkflowsTestData.impex", "UTF-8");
	}

	@Test
	public void givenOneInvalidAndOneValidItem_WhenRequestEditableItems_ThenThrowsException()
	{
		// WHEN
		final Response response = makeRequest(Arrays.asList(INVALID_UID, VALID_UID1), makeRequestByCmsManager());

		// THEN
		assertResponse(Response.Status.NOT_FOUND, response);
	}

	@Test
	public void givenItemThatIsNotInWorkflow_WhenRequestEditableItems_ThenReturnsTrue()
	{
		// WHEN
		final Response response = makeRequest(Arrays.asList(NOT_IN_WORKFLOW_ITEM), makeRequestByCmsManager());

		// THEN
		assertResponse(Response.Status.OK, response);
		final CMSWorkflowEditableItemWsDTO entityByItem = getEntityByItemUid(response, NOT_IN_WORKFLOW_ITEM);
		assertTrue(entityByItem.isEditableByUser());
	}

	@Test
	public void givenItemThatIsInWorkflowAndCurrentUserIsParticipantOfActiveAction_WhenRequestEditableItems_ThenReturnsTrue()
	{
		// WHEN
		final Response response = makeRequest(Arrays.asList(VALID_UID1), makeRequestByCmsManager());

		// THEN
		assertResponse(Response.Status.OK, response);
		final CMSWorkflowEditableItemWsDTO entityByItem = getEntityByItemUid(response, VALID_UID1);
		assertTrue(entityByItem.isEditableByUser());
	}

	@Test
	public void givenItemThatIsInWorkflowAndCurrentUserIsNotParticipantOfActiveAction_WhenRequestEditableItems_ThenReturnsFalse()
	{
		// WHEN
		final Response response = makeRequest(Arrays.asList(VALID_UID1), makeRequestByCmsEditor());

		// THEN
		assertResponse(Response.Status.OK, response);
		final CMSWorkflowEditableItemWsDTO entityByItem = getEntityByItemUid(response, VALID_UID1);
		assertTrue(entityByItem.isEditableByUser());
	}

	protected CMSWorkflowEditableItemWsDTO getEntityByItemUid(final Response response, final String itemUid)
	{
		final CMSWorkflowEditableItemListWsDTO entity = response.readEntity(CMSWorkflowEditableItemListWsDTO.class);
		return entity.getEditableItems()
				.stream().filter(e -> e.getUid().equalsIgnoreCase(itemUid)).findFirst().get();
	}

	protected Supplier<WsSecuredRequestBuilder> makeRequestByCmsEditor()
	{
		return () ->  getCmsEditorWsSecuredRequestBuilder();
	}

	protected Supplier<WsSecuredRequestBuilder> makeRequestByCmsManager()
	{
		return () ->  getCmsManagerWsSecuredRequestBuilder();
	}

	protected Response makeRequest(final List<String> itemUids, final Supplier<WsSecuredRequestBuilder> requestBuilder)
	{
		final String endpoint = buildRelatedItemsWorkflowEndpoint(CMS_CATALOG, CATALOG_VERSION);

		return requestBuilder.get().path(endpoint) //
				.queryParam("itemUids", StringUtils.join(itemUids, ","))
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();
	}

	protected String buildRelatedItemsWorkflowEndpoint(final String catalogId, final String versionId)
	{
		final Map<String, String> endPointParams = new HashMap<>();
		endPointParams.put(URI_CATALOG_ID, catalogId);
		endPointParams.put(URI_VERSION_ID, versionId);

		return replaceUriVariablesWithDefaults(WORKFLOW_EDITABLE_ITEMS_URL, endPointParams);
	}
}


