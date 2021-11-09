/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsweb.controllers.integration;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.testsupport.client.WsRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.xyformsweb.constants.XyformswebConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions = { XyformswebConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class IsAuthorizedOrbeonFormControllerTest extends ServicelayerTest
{
	private String headerName;
	private String headerValue;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultUsers();
		importCsv("/xyformsweb/test/orbeoncustomer-data.impex", "utf-8");
		headerName = Registry.getCurrentTenantNoFallback().getConfig().getParameter("xyformsservices.orbeon.hybris-proxy-header");
		headerValue = Registry.getCurrentTenantNoFallback().getConfig().getParameter("xyformsservices.orbeon.hybris-proxy-value");
	}

	@Test
	public void shouldResponseUnauthorizedStatusCodeToGetIfUnauthorized()
	{
		final int status = getWsRequestBuilder()
				.path("/fr/service/hybris/crud/test_auth/test1/form/form.xhtml")
				.build()
				.accept(MediaType.APPLICATION_XHTML_XML)
				.get()
				.getStatus();

		Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), status);
	}

	@Test
	public void shouldResponseOkStatusCodeIfAuthorized()
	{
		final int status = getWsRequestBuilder()
				.path("/fr/service/hybris/crud/test_auth/test1/form/form.xhtml")
				.build()
				.header(headerName, headerValue)
				.accept(MediaType.APPLICATION_XHTML_XML)
				.get()
				.getStatus();

		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), status);
	}

	@Test
	public void shouldResponseUnauthorizedStatusCodeToPutIfUnauthorized() throws IOException
	{
		final int status = getWsRequestBuilder()
				.path("/fr/service/hybris/crud/test_auth/test2/form/form.xhtml")
				.build()
				.accept(MediaType.APPLICATION_XHTML_XML)
				.put(Entity.entity(getTestYFormXml(), MediaType.APPLICATION_XHTML_XML))
				.getStatus();

		Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), status);
	}

	@Test
	public void shouldResponseOkStatusCodeToPutIfAuthorized() throws IOException
	{
		final int status = getWsRequestBuilder()
				.path("/fr/service/hybris/crud/test_auth/test2/form/form.xhtml")
				.build()
				.accept(MediaType.APPLICATION_XHTML_XML)
				.header(headerName, headerValue)
				.put(Entity.entity(getTestYFormXml(), MediaType.APPLICATION_XHTML_XML))
				.getStatus();

		Assert.assertEquals(Response.Status.OK.getStatusCode(), status);
	}

	private String getTestYFormXml() throws IOException
	{
		final String resource = this.getClass().getResource("/xyformsweb/test/test-form.xhtml").getPath();
		return Files.readString(Path.of(resource));
	}

	private WsRequestBuilder getWsRequestBuilder()
	{
		return new WsRequestBuilder()
				.extensionName(XyformswebConstants.EXTENSIONNAME);
	}

	public String getHeaderName()
	{
		return headerName;
	}

	public void setHeaderName(final String headerName)
	{
		this.headerName = headerName;
	}

	public String getHeaderValue()
	{
		return headerValue;
	}

	public void setHeaderValue(final String headerValue)
	{
		this.headerValue = headerValue;
	}
}
