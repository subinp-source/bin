/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.pagescontentslotstyperestrictions.controller;

import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.MULTI_COUNTRY_ID_EUROPE_CARS;
import static de.hybris.platform.cmsfacades.util.models.ContentPageModelMother.UID_HOMEPAGE_EU;
import static de.hybris.platform.cmsfacades.util.models.ContentSlotModelMother.UID_HEADER_EU;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_CATALOG_ID;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_VERSION_ID;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentPageModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentSlotModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmsfacades.util.models.PageTemplateModelMother;
import de.hybris.platform.cmsfacades.util.models.ParagraphComponentModelMother;
import de.hybris.platform.cmsfacades.util.models.SiteModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.ContentSlotTypeRestrictionsWsDTO;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.unimi.dsi.fastutil.Hash;
import org.junit.Test;

import com.google.common.collect.Maps;


/**
 * Integration test for {@link PageContentSlotTypeRestrictionController}
 */
@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class PageContentSlotTypeRestrictionControllerWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String SLOT_ID = "slotId";

	private static final String PAGE_UID = "pageUid";

	private static final String SINGLE_SLOT_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/pages/{pageUid}/contentslots/{slotId}/typerestrictions";

	private static final String MULTI_SLOT_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/pages/{pageUid}/typerestrictions";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;

	@Resource
	private SiteModelMother siteModelMother;

	@Resource
	private ContentCatalogModelMother contentCatalogModelMother;

	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;

	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;

	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;

	@Test
	public void shouldGetRestrictionsForContentSlot() throws Exception
	{

		setupNonMultiCountryEnvironment();

		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(PAGE_UID, ContentPageModelMother.UID_HOMEPAGE);
		variables.put(SLOT_ID, ContentSlotModelMother.UID_HEADER);

		final String endpoint = replaceUriVariablesWithDefaults(SINGLE_SLOT_ENDPOINT, variables);
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.OK, response);

		final ContentSlotTypeRestrictionsWsDTO entity = response.readEntity(ContentSlotTypeRestrictionsWsDTO.class);
		assertThat(entity.getValidComponentTypes(), not(empty()));
	}

	@Test
	public void shouldGetRestrictionsForContentSlot_InMultiCountrySetup() throws Exception
	{
		setupMultiCountryEnvironment();

		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(URI_CATALOG_ID, MULTI_COUNTRY_ID_EUROPE_CARS.name());
		variables.put(URI_VERSION_ID, STAGED.getVersion());
		variables.put(PAGE_UID, UID_HOMEPAGE_EU);
		variables.put(SLOT_ID, UID_HEADER_EU);

		final String endpoint = replaceUriVariablesWithDefaults(SINGLE_SLOT_ENDPOINT, variables);
		final Response response = getMultiCountryCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.OK, response);

		final ContentSlotTypeRestrictionsWsDTO entity = response.readEntity(ContentSlotTypeRestrictionsWsDTO.class);
		assertThat(entity.getValidComponentTypes(), not(empty()));
	}

	@Test
	public void shouldGetRestrictionsForListOfContentSlots() throws Exception
	{
		// GIVEN
		setupNonMultiCountryEnvironment();

		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(PAGE_UID, ContentPageModelMother.UID_HOMEPAGE);

		final String endpoint = replaceUriVariablesWithDefaults(MULTI_SLOT_ENDPOINT, variables);
		final HashMap<String, List<String>> payload = Maps.newHashMap();
		payload.put("slotIds", List.of(ContentSlotModelMother.UID_HEADER, ContentSlotModelMother.UID_FOOTER));

		// WHEN
		final Response response = getCmsManagerWsSecuredRequestBuilder() //
					.path(endpoint).build()
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		// THEN
		assertResponse(Status.OK, response);

		final List<ContentSlotTypeRestrictionsWsDTO> entities = response.readEntity(new GenericType<List<ContentSlotTypeRestrictionsWsDTO>>() {});
		assertThat(entities.size(), is(2));
		assertThat(entities, hasItems( //
				allOf(//
						hasProperty("contentSlotUid", equalTo(ContentSlotModelMother.UID_HEADER)), //
						hasProperty("validComponentTypes", hasItems(CMSLinkComponentModel._TYPECODE, BannerComponentModel._TYPECODE, CMSParagraphComponentModel._TYPECODE)) //
				), //
				allOf(//
						hasProperty("contentSlotUid", equalTo(ContentSlotModelMother.UID_FOOTER)), //
						hasProperty("validComponentTypes", hasItems(CMSLinkComponentModel._TYPECODE, BannerComponentModel._TYPECODE, CMSParagraphComponentModel._TYPECODE)) //
				) //
		));
	}

	@Test
	public void shouldGetRestrictionsForListOfContentSlots_InMultiCountrySetup() throws Exception
	{
		// GIVEN
		setupMultiCountryEnvironment();

		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(URI_CATALOG_ID, MULTI_COUNTRY_ID_EUROPE_CARS.name());
		variables.put(URI_VERSION_ID, STAGED.getVersion());
		variables.put(PAGE_UID, UID_HOMEPAGE_EU);

		final String endpoint = replaceUriVariablesWithDefaults(MULTI_SLOT_ENDPOINT, variables);
		final HashMap<String, List<String>> payload = Maps.newHashMap();
		payload.put("slotIds", List.of(ContentSlotModelMother.UID_HEADER_EU));

		// WHEN
		final Response response = getMultiCountryCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint).build()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		// THEN
		assertResponse(Status.OK, response);

		final List<ContentSlotTypeRestrictionsWsDTO> entities = response.readEntity(new GenericType<List<ContentSlotTypeRestrictionsWsDTO>>() {});
		assertThat(entities.size(), is(1));
		assertThat(entities, hasItems( //
				allOf(//
						hasProperty("contentSlotUid", equalTo(ContentSlotModelMother.UID_HEADER_EU)), //
						hasProperty("validComponentTypes", hasItems(CMSLinkComponentModel._TYPECODE, CMSParagraphComponentModel._TYPECODE)) //
				)
		));
	}

	public void setupNonMultiCountryEnvironment()
	{
		final CatalogVersionModel catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		final PageTemplateModel pageTemplate = pageTemplateModelMother.homepageTemplate(catalogVersion);
		final ContentCatalogModel contentCatalog = contentCatalogModelMother.createAppleContentCatalogModel(catalogVersion);
		((ContentCatalogModel) catalogVersion.getCatalog())
		.setCmsSites(Arrays.asList(siteModelMother.createElectronics(contentCatalog)));

		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		contentSlotForPageModelMother.FooterHomepage_FlashComponentOnly(catalogVersion);
		contentSlotNameModelMother.Header(pageTemplate);
		contentSlotNameModelMother.Footer(pageTemplate);
	}

	public void setupMultiCountryEnvironment() throws ImpExException
	{
		importCsv("/cmswebservices/test/impex/essentialMultiCountryTestDataAuth.impex", "utf-8");

		final CatalogVersionModel catalogVersion = catalogVersionModelMother.createCarEuropeStagedCatalogVersionModel();
		contentSlotForPageModelMother.HeaderHomepageEurope_ParagraphOnly(catalogVersion);
	}
}
