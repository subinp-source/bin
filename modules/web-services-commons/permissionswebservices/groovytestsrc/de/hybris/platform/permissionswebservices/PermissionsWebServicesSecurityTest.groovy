/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.permissionswebservices

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.oauth2.constants.OAuth2Constants
import de.hybris.platform.permissionswebservices.constants.PermissionswebservicesConstants
import de.hybris.platform.permissionswebservices.controllers.AbstractPermissionsWebServicesTest
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import org.junit.Assert
import org.junit.Before
import org.junit.Test


@IntegrationTest
@NeedsEmbeddedServer(webExtensions =
[PermissionswebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME])
public class PermissionsWebServicesSecurityTest extends AbstractPermissionsWebServicesTest {
	static final String SUBGROUP2 = "subgroup2"
	private static final String NOT_EXISTING_SCOPE = "not_existing_scope";
	private static final String PERMISSIONSWEBSERVICES_SCOPE = "permissionswebservices";
	private static final String BASIC_SCOPE = "basic";
	private static final String TESTADMIN_UID = "testadmin";
	private static final String TESTADMIN_PASSWORD = "1234";

	WsSecuredRequestBuilder wsSecuredRequestBuilder

	@Before
	void setup() {
		wsSecuredRequestBuilder = new WsSecuredRequestBuilder()
				.extensionName(PermissionswebservicesConstants.EXTENSIONNAME)
				.path("v1")
				.client("mobile_android", "secret");
		importData(new ClasspathImpExResource("/permissionswebservices/test/testpermissions.impex", "UTF-8"));
		insertGlobalPermission(SUBGROUP2, "globalpermission1");
	}

	@Test
	public void shouldReturn401ForAdminWithNotExistingScope() throws IOException {

		//when posting with a not existing scope
		final Response result = wsSecuredRequestBuilder//
				.path("permissions")//
				.path("principals")//
				.path("admin")//
				.path("types")//
				.queryParam("types", "User,Order")//
				.queryParam("permissionNames", "read,change,create,remove,changerights")//
				.resourceOwner(TESTADMIN_UID, TESTADMIN_PASSWORD)//
				.scope(NOT_EXISTING_SCOPE)//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.accept(MediaType.APPLICATION_JSON)//
				.get();

		//then we receive a 401
		Assert.assertEquals(401, result.getStatus());
	}

	@Test
	public void shouldReturn403ForAdminWithBasicScope() throws IOException {

		//when posting with a not existing scope
		final Response result = wsSecuredRequestBuilder//
				.path("permissions")//
				.path("principals")//
				.path("admin")//
				.path("types")//
				.queryParam("types", "User,Order")//
				.queryParam("permissionNames", "read,change,create,remove,changerights")//
				.resourceOwner(TESTADMIN_UID, TESTADMIN_PASSWORD)//
				.scope(BASIC_SCOPE)//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.accept(MediaType.APPLICATION_JSON)//
				.get();

		//then we receive a 403
		Assert.assertEquals(403, result.getStatus());
	}

	@Test
	public void shouldReturn200ForAdminWithProperScope() throws IOException {

		//when posting with a with proper scope
		final Response result = wsSecuredRequestBuilder//
				.path("permissions")//
				.path("principals")//
				.path("admin")//
				.path("types")//
				.queryParam("types", "User,Order")//
				.queryParam("permissionNames", "read,change,create,remove,changerights")//
				.resourceOwner(TESTADMIN_UID, TESTADMIN_PASSWORD)//
				.scope(PERMISSIONSWEBSERVICES_SCOPE)//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.accept(MediaType.APPLICATION_JSON)//
				.get();

		//then we receive a 200
		Assert.assertEquals(200, result.getStatus());
	}

	@Test
	public void shouldReturn200ForAdminWithNoSpecificScope() throws IOException {

		//when posting with no scope specified
		final Response result = wsSecuredRequestBuilder//
				.path("permissions")//
				.path("principals")//
				.path("admin")//
				.path("types")//
				.queryParam("types", "User,Order")//
				.queryParam("permissionNames", "read,change,create,remove,changerights")//
				.resourceOwner(TESTADMIN_UID, TESTADMIN_PASSWORD)//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.accept(MediaType.APPLICATION_JSON)//
				.get();

		//then we receive a 200
		Assert.assertEquals(200, result.getStatus());
	}
}
