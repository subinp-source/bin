/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.security;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ CmswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class IsAuthorizedCmsManagerIntegrationTest extends ApiBaseIntegrationTest
{
	private static final String URI = "/v1/types";

	@Test
	public void shouldFailAuthorization_emptyToken() throws Exception
	{
		final Response response = getWsRequestBuilder() //
				.path(URI).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.UNAUTHORIZED, response);

		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertEquals(1, errors.getErrors().size());
	}

	@Test
	public void shouldFailAuthorization_invalidToken() throws Exception
	{
		final Response response = getWsSecuredRequestBuilder(OAUTH_INVALID_ID, OAUTH_INVALID_PASS) //
				.path(URI).build() //
				.accept(MediaType.APPLICATION_JSON) //
				.get();

		assertResponse(Status.UNAUTHORIZED, response);

		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertEquals(1, errors.getErrors().size());
	}

}
