/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.pages.controller;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmsfacades.util.models.PageTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.SiteModelMother;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.cmssmarteditwebservices.data.ComponentTypeData;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSComponentTypeListWsDTO;
import de.hybris.platform.cmssmarteditwebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmssmarteditwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class PageComponentTypesControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String PAGE_COMPONENT_TYPES_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/pages/{pageId}/types";
	private static final String PAGE_ID = "pageId";
	private static final String PAGE_SIZE = "pageSize";
	private static final String CURRENT_PAGE = "currentPage";
	private static final String MASK = "mask";
	private static final String FIELDS_NAME = "fields";
	private static final String INVALID_FIELD_NAME = "SomeInvalidName";
	private static final String VALID_FIELD_NAME = "BASIC";

	@Resource
	private SiteModelMother siteModelMother;

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;

	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	@Resource
	private ContentPageModelMother contentPageModelMother;

	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;

	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;

	private CatalogVersionModel catalogVersion;
	private AbstractPageModel homePage;
	private PageTemplateModel pageTemplate;

	@Before
	public void setUp()
	{
		// Create catalog
		siteModelMother.createNorthAmericaElectronicsWithAppleStagedCatalog();
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		pageTemplate = pageTemplateModelMother.homepageTemplate(catalogVersion);
		homePage = contentPageModelMother.homePage(catalogVersion);

		// Set type restrictions
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		contentSlotForPageModelMother.FooterHomepage_FlashComponentOnly(catalogVersion);
		contentSlotNameModelMother.Header(pageTemplate);
		contentSlotNameModelMother.Footer(pageTemplate);
	}

	@Test
	public void shouldReturnComponentTypesAllowedInPage()
	{
		// GIVEN
		final Map<String, String> params = new HashMap<>();
		params.put(URI_SITE_ID, SiteModelMother.ELECTRONICS);
		params.put(URI_CATALOG_ID, catalogVersion.getCatalog().getId());
		params.put(PAGE_ID, homePage.getUid());

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(PAGE_COMPONENT_TYPES_ENDPOINT, params)) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(CURRENT_PAGE, 0) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Status.OK, response);

		final CMSComponentTypeListWsDTO result = response.readEntity(CMSComponentTypeListWsDTO.class);
		assertThat(result.getPagination(), allOf(hasProperty("totalCount", greaterThanOrEqualTo(2L)), hasProperty("page", is(0)),
				hasProperty("count", greaterThanOrEqualTo(2))));

		final List<ComponentTypeData> componentTypes = result.getComponentTypes();
		assertThat(componentTypes.size(), greaterThanOrEqualTo(2));
		assertThat(componentTypes, hasItems( //
				allOf(//
						hasProperty("code", equalTo(CMSParagraphComponentModel._TYPECODE)) //
				), //
				allOf(//
						hasProperty("code", equalTo(CMSLinkComponentModel._TYPECODE)) //
				)));
	}

	@Test
	public void shouldFilterComponentTypesBasedOnMask()
	{
		// GIVEN
		final Map<String, String> params = new HashMap<>();
		params.put(URI_SITE_ID, SiteModelMother.ELECTRONICS);
		params.put(URI_CATALOG_ID, catalogVersion.getCatalog().getId());
		params.put(PAGE_ID, homePage.getUid());

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(PAGE_COMPONENT_TYPES_ENDPOINT, params)) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(CURRENT_PAGE, 0) //
				.queryParam(MASK, "Paragraph").build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Status.OK, response);

		final CMSComponentTypeListWsDTO result = response.readEntity(CMSComponentTypeListWsDTO.class);
		assertThat(result.getPagination(),
				allOf(hasProperty("totalCount", is(1L)), hasProperty("page", is(0)), hasProperty("count", is(1))));

		final List<ComponentTypeData> componentTypes = result.getComponentTypes();
		assertThat(componentTypes.size(), is(1));
		assertThat(componentTypes, hasItems( //
				allOf(//
						hasProperty("code", equalTo(CMSParagraphComponentModel._TYPECODE)) //
				)));
	}

	@Test
	public void shouldThrowAnErrorWhenRequestedInvalidFields()
	{
		// GIVEN
		final Map<String, String> params = new HashMap<>();
		params.put(URI_SITE_ID, SiteModelMother.ELECTRONICS);
		params.put(URI_CATALOG_ID, catalogVersion.getCatalog().getId());
		params.put(PAGE_ID, homePage.getUid());

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(PAGE_COMPONENT_TYPES_ENDPOINT, params)) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(CURRENT_PAGE, 0) //
				.queryParam(FIELDS_NAME, INVALID_FIELD_NAME) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Status.BAD_REQUEST, response);

		final ErrorListWsDTO result = response.readEntity(ErrorListWsDTO.class);
		assertThat(result.getErrors().size(), is(1));
		assertThat(result.getErrors().get(0), allOf( //
				hasProperty("message", equalTo("Incorrect field:'SomeInvalidName'")), //
				hasProperty("type", equalTo("ConversionError")) //
		));
	}

	@Test
	public void shouldNotThrowAnErrorWhenRequestedValidFields()
	{
		// GIVEN
		final Map<String, String> params = new HashMap<>();
		params.put(URI_SITE_ID, SiteModelMother.ELECTRONICS);
		params.put(URI_CATALOG_ID, catalogVersion.getCatalog().getId());
		params.put(PAGE_ID, homePage.getUid());

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(PAGE_COMPONENT_TYPES_ENDPOINT, params)) //
				.queryParam(PAGE_SIZE, 10) //
				.queryParam(CURRENT_PAGE, 0) //
				.queryParam(FIELDS_NAME, VALID_FIELD_NAME) //
				.build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		// THEN
		assertResponse(Status.OK, response);
	}
}
