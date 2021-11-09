/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.pages.controller;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.enums.CMSPageOperation;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmsfacades.util.models.PageTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.SiteModelMother;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.cmssmarteditwebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmssmarteditwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class PageOperationsControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String PAGE_OPERATIONS_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/pages/{pageId}/operations";
	private static final String OPERATION = "operation";
	private static final String PAGE_ID = "pageId";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentPageModelMother contentPageModelMother;
	@Resource
	private SiteModelMother siteModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	private CatalogVersionModel stagedCatalogVersion;
	private CatalogVersionModel onlineCatalogVersion;
	private ContentPageModel contentPage;

	@Before
	public void setup()
	{
		siteModelMother.createNorthAmericaElectronicsWithAppleStagedCatalog();
		stagedCatalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		onlineCatalogVersion = catalogVersionModelMother.createAppleOnlineCatalogVersionModel();

		contentPage = contentPageModelMother.homePage(onlineCatalogVersion);
	}

	@Test
	public void givenPageExistsInTargetCatalogVersion_WhenDeleteOperationIsTriggered_ThenPageStatusIsChangedToDeleted()
	{

		catalogVersionModelMother.createCatalogSyncronizationSyncItemCronJobModel(stagedCatalogVersion, onlineCatalogVersion);

		final Map<String, String> params = new HashMap<>();
		params.put(URI_SITE_ID, SiteModelMother.ELECTRONICS);
		params.put(URI_CATALOG_ID, stagedCatalogVersion.getCatalog().getId());
		params.put(PAGE_ID, contentPage.getUid());

		final Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(OPERATION, CMSPageOperation.TRASH_PAGE);
		inputMap.put(CmssmarteditwebservicesConstants.URI_SOURCE_CATALOG_VERSION, stagedCatalogVersion.getVersion());
		inputMap.put(CmssmarteditwebservicesConstants.URI_TARGET_CATALOG_VERSION, onlineCatalogVersion.getVersion());

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(PAGE_OPERATIONS_ENDPOINT, params)) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.post(Entity.entity(inputMap, MediaType.APPLICATION_JSON));

		assertResponse(Status.OK, response);

		assertThat(contentPage.getPageStatus(), is(CmsPageStatus.DELETED));

	}

}
