/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.languages.controller;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmsfacades.util.models.SiteModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.LanguageListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class LanguageControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String HEADER_CACHE_CONTROL = "Cache-Control";
	private static final String INVALID = "invalid";
	private static final String GET_ALL_LANGUAGES = "/v1/sites/{siteId}/languages";

	@Resource
	private SiteModelMother siteModelMother;

	protected void createElectronicsSiteWithEmptyAppleCatalog()
	{
		siteModelMother.createElectronicsWithAppleCatalog();
	}

	@Test
	public void shouldGetAllLanguagesFromElectronicsSite() throws Exception
	{
		createElectronicsSiteWithEmptyAppleCatalog();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CmswebservicesConstants.URI_SITE_ID, SiteModelMother.ELECTRONICS);

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(GET_ALL_LANGUAGES, uriVariables)).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.OK, response);

		final MultivaluedMap<String, Object> headers = response.getHeaders();
		assertThat(headers, hasEntry(equalTo(HEADER_CACHE_CONTROL), contains("public, max-age=1800")));

		final LanguageListData entity = response.readEntity(LanguageListData.class);
		Set<String> languagesReturned = entity.getLanguages().stream()
				.map(LanguageData::getIsocode).collect(Collectors.toSet());

		assertEquals(2, entity.getLanguages().size());
		assertThat(languagesReturned, containsInAnyOrder(
				Locale.ENGLISH.toString(),
				Locale.FRENCH.toString()
		));
	}

	@Test
	public void shouldReturnNotFoundResponseCode_InvalidSiteId() throws Exception
	{
		createElectronicsSiteWithEmptyAppleCatalog();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CmswebservicesConstants.URI_SITE_ID, INVALID);

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(replaceUriVariablesWithDefaults(GET_ALL_LANGUAGES, uriVariables)).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.NOT_FOUND, response);
	}
}
