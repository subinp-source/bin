/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.cmswebservices.media.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_CURRENT_PAGE;
import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_SIZE;
import static de.hybris.platform.core.model.media.MediaContainerModel.CATALOGVERSION;
import static de.hybris.platform.core.model.media.MediaContainerModel.QUALIFIER;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.MediaContainerModelMother;
import de.hybris.platform.cmsfacades.util.models.MediaModelMother;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.dto.MediaContainerListWsDTO;
import de.hybris.platform.cmswebservices.dto.MediaContainerWsDTO;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Maps;


@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class MediaContainerControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/mediacontainers";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private MediaContainerModelMother mediaContainerModelMother;

	private CatalogVersionModel catalogVersion;
	private String catalogVersionUUID;

	@Before
	public void setUp()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		mediaContainerModelMother.createMediaContainerModelWithLogos(catalogVersion);
		mediaContainerModelMother.createEmptyMediaContainerModel(catalogVersion);

		catalogVersionUUID = getCatalogVersionUuid(catalogVersion);
	}

	@Test
	public void shouldGetMediaContainerByCode()
	{
		final String endpoint = replaceUriVariablesWithDefaults(ENDPOINT, Maps.newHashMap());

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.path(MediaContainerModelMother.MEDIA_CONTAINER_QUALIFIER).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.OK, response);

		final MediaContainerWsDTO entity = response.readEntity(MediaContainerWsDTO.class);
		assertThat(entity.getQualifier(), equalTo(MediaContainerModelMother.MEDIA_CONTAINER_QUALIFIER));
		assertThat(entity.getCatalogVersion(), equalTo(catalogVersionUUID));
		assertThat(entity.getThumbnailUrl(), equalTo(MediaModelMother.MediaTemplate.LOGO.getUrl()));
		assertThat(entity.getMediaContainerUuid(), not(nullValue()));
		assertThat(entity.getMedias().values(), hasSize(2));
	}

	@Test
	public void shouldFailGetMediaContainerWithInvalidCode()
	{
		final String endpoint = replaceUriVariablesWithDefaults(ENDPOINT, Maps.newHashMap());

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.path("invalid-id").build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.NOT_FOUND, response);

		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertThat(errors.getErrors(), hasSize(1));
		assertThat(errors.getErrors().get(0).getType(), equalTo("CMSItemNotFoundError"));
	}

	@Test
	public void shouldFindMediaContainerForText() throws JsonMappingException, JsonProcessingException
	{
		mediaContainerModelMother.createEmptyMediaContainerModelWithQualifier(catalogVersion,
				"simple-responsive-media-container-empty");
		mediaContainerModelMother.createEmptyMediaContainerModelWithQualifier(catalogVersion, "empty-simple-container");
		mediaContainerModelMother.createEmptyMediaContainerModelWithQualifier(catalogVersion, "Aempty-container");
		mediaContainerModelMother.createEmptyMediaContainerModelWithQualifier(catalogVersion, "Bempty-container");

		final String endpoint = replaceUriVariablesWithDefaults(ENDPOINT, Maps.newHashMap());

		final Response response = getCmsManagerWsSecuredRequestBuilder() //
				.path(endpoint) //
				.queryParam(URI_PAGE_SIZE, 2) //
				.queryParam(URI_CURRENT_PAGE, 0) //
				.queryParam("mask", "simple-responsive").build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.OK, response);

		final MediaContainerListWsDTO entity = response.readEntity(MediaContainerListWsDTO.class);
		assertThat(entity.getMediaContainers(), hasSize(2));
		assertThat(entity.getMediaContainers(), hasItems(
				allOf(hasProperty(QUALIFIER, equalTo(MediaContainerModelMother.EMPTY_MEDIA_CONTAINER_QUALIFIER)),
						hasProperty(CATALOGVERSION, equalTo(catalogVersionUUID)), hasProperty("mediaContainerUuid", not(nullValue()))),
				allOf(hasProperty(QUALIFIER, equalTo(MediaContainerModelMother.MEDIA_CONTAINER_QUALIFIER)),
						hasProperty(CATALOGVERSION, equalTo(catalogVersionUUID)), hasProperty("mediaContainerUuid", not(nullValue())),
						hasProperty("thumbnailUrl", equalTo(MediaModelMother.MediaTemplate.LOGO.getUrl())))));
	}
}
