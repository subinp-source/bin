/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.useraccess

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.config.DefaultInboundServicesConfiguration
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.oauth2.constants.OAuth2Constants
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.servicelayer.type.TypeService
import de.hybris.platform.servicelayer.user.UserService
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.ADMIN_USER
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.PASSWORD
import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups

@NeedsEmbeddedServer(webExtensions = [
        Odata2webservicesConstants.EXTENSIONNAME,
        OAuth2Constants.EXTENSIONNAME])
@IntegrationTest
class InboundChannelConfigurationSecurityIntegrationTest extends ServicelayerSpockSpecification {

    private static final String USER_UID = "KaitlinBrockwayUserUid"
    private static final String CLIENT_ID = "Cred1"
    private static final String CLIENT_SECRET = "superSecretSecret"
    private static final String IO = "ChannelConfigTestIO"
    private static final String CATALOGS = "Catalogs"
    private static final String CATALOG_ID = "kittyCat1"
    private static final Entity CATALOG_PAYLOAD = Entity.json(json().withField("id", CATALOG_ID).build())

    @Resource(name = "defaultInboundServicesConfiguration")
    private DefaultInboundServicesConfiguration inboundServicesConfiguration
    @Resource
    private PermissionManagementService permissionManagementService
    @Resource
    private TypeService typeService
    @Resource
    private UserService userService
    private boolean savedLegacySecurity

    def setupSpec() {
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'

        givenUserExistsWithUidAndGroups(USER_UID, PASSWORD)
        givenUserExistsWithUidAndGroups(ADMIN_USER, PASSWORD, "integrationadmingroup")
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                'INSERT_UPDATE IntegrationClientCredentialsDetails; clientId[unique=true]; clientSecret  ; authorities                ; user(uid)',
                "                                                 ; $CLIENT_ID           ; $CLIENT_SECRET; ROLE_INTEGRATIONADMINGROUP ; $USER_UID"
        )
        UserAccessTestUtils.givenCatalogItemExistsForIO(IO)
    }

    def setup() {
        savedLegacySecurity = inboundServicesConfiguration.isLegacySecurity()
    }

    def cleanup() {
        removeAllPermissions(CatalogModel._TYPECODE, ADMIN_USER)
        removeAllPermissions(CatalogModel._TYPECODE, USER_UID)
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        inboundServicesConfiguration.setLegacySecurity(savedLegacySecurity)
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG_ID }
        IntegrationTestUtil.findAny(EmployeeModel, { it.uid == ADMIN_USER || it.uid == USER_UID }).ifPresent {
            IntegrationTestUtil.remove it
        }
        IntegrationTestUtil.removeAll IntegrationClientCredentialsDetailsModel
        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }

    @Test
    @Unroll
    def "BasicAuth #method request will result in a 404 when the IntegrationObject is not found"() {
        given:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)

        when:
        def response = basicAuthRequest()
                .path(name)
                .path(CATALOGS)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method(method)

        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode

        where:
        isLegacySecurity | method   | name
        true             | 'GET'    | "nonExistentIO"
        false            | 'GET'    | "nonExistentIO"
        true             | 'PATCH'  | "nonExistentIO"
        false            | 'PATCH'  | "nonExistentIO"
        true             | 'POST'   | "nonExistentIO"
        false            | 'POST'   | "nonExistentIO"
        true             | 'DELETE' | "nonExistentIO"
        false            | 'DELETE' | "nonExistentIO"
        true             | 'GET'    | "%20"
    }

    @Test
    @Unroll
    def "OAuth #method request will result in a 404 when the IntegrationObject is not found"() {
        given:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        when:
        def response = oAuth2RequestWithClientCredentialsGrantType()
                .path(name)
                .path(CATALOGS)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method(method)
        then:
        response.status == HttpStatusCodes.NOT_FOUND.statusCode
        where:
        isLegacySecurity | method   | name
        true             | 'GET'    | "nonExistentIO"
        false            | 'GET'    | "nonExistentIO"
        true             | 'PATCH'  | "nonExistentIO"
        false            | 'PATCH'  | "nonExistentIO"
        true             | 'POST'   | "nonExistentIO"
        false            | 'POST'   | "nonExistentIO"
        true             | 'DELETE' | "nonExistentIO"
        false            | 'DELETE' | "nonExistentIO"
        true             | 'GET'    | "%20"
    }

    @Test
    @Unroll
    def "OAuth2 #method request is authenticated when an OAUTH InboundChannel exists for the ChannelConfigTestIO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=OAUTH"
        inboundChannelConfigurationExistsWithAuthenticationType("OAUTH")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.READ, USER_UID)
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.REMOVE, USER_UID)

        when:
        def response = oAuth2RequestWithClientCredentialsGrantType()
                .path(IO)
                .path("$CATALOGS('$CATALOG_ID')")
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method(method)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        isLegacySecurity | method   | path
        true             | 'GET'    | CATALOGS
        false            | 'GET'    | CATALOGS
        true             | 'DELETE' | "$CATALOGS('$CATALOG_ID')"
        false            | 'DELETE' | "$CATALOGS('$CATALOG_ID')"
    }

    @Test
    @Unroll
    def "OAuth2 #method persistence request is authenticated when an OAUTH InboundChannel exists for the ChannelConfigTestIO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=OAUTH"
        inboundChannelConfigurationExistsWithAuthenticationType("OAUTH")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.CREATE, USER_UID)
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.CHANGE, USER_UID)

        when:
        def response = oAuth2RequestWithClientCredentialsGrantType()
                .path(IO)
                .path(path)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method(method, CATALOG_PAYLOAD)

        then:
        response.status == responseStatus.statusCode

        where:
        isLegacySecurity | method  | path                       | responseStatus
        true             | 'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        false            | 'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        true             | 'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
        false            | 'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "Basic Auth #method request is denied when an OAUTH InboundChannel exists for the ChannelConfigTestIO"() {
        given: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=OAUTH"
        inboundChannelConfigurationExistsWithAuthenticationType("OAUTH")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)

        when:
        def response = basicAuthRequest()
                .path(IO)
                .path(CATALOGS)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method method

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        isLegacySecurity | method
        true             | 'GET'
        false            | 'GET'
        true             | 'POST'
        false            | 'POST'
        true             | 'DELETE'
        false            | 'DELETE'
        true             | 'PATCH'
        false            | 'PATCH'
    }

    @Test
    @Unroll
    def "Oauth2 #method requests are denied when a BASIC InboundChannel exists for the ChannelConfigTestIO"() {
        given: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=BASIC"
        inboundChannelConfigurationExistsWithAuthenticationType("BASIC")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)

        when:
        def response = oAuth2RequestWithClientCredentialsGrantType()
                .path(IO)
                .path(CATALOGS)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method method

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        isLegacySecurity | method
        true             | 'GET'
        false            | 'GET'
        true             | 'POST'
        false            | 'POST'
        true             | 'DELETE'
        false            | 'DELETE'
        true             | 'PATCH'
        false            | 'PATCH'
    }

    @Test
    @Unroll
    def "Basic Auth #method requests are authenticated when a BASIC InboundChannel exists for the ChannelConfigTestIO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=BASIC"
        inboundChannelConfigurationExistsWithAuthenticationType("BASIC")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.READ, ADMIN_USER)
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.REMOVE, ADMIN_USER)

        when:
        def response = basicAuthRequest()
                .path(IO)
                .path(path)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method(method)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        isLegacySecurity | method   | path
        true             | 'GET'    | CATALOGS
        false            | 'GET'    | CATALOGS
        true             | 'DELETE' | "$CATALOGS('$CATALOG_ID')"
        false            | 'DELETE' | "$CATALOGS('$CATALOG_ID')"
    }

    @Test
    @Unroll
    def "Basic Auth #method persistence requests are authenticated when a BASIC InboundChannel exists for the ChannelConfigTestIO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=BASIC"
        inboundChannelConfigurationExistsWithAuthenticationType("BASIC")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.CREATE, ADMIN_USER)
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.CHANGE, ADMIN_USER)

        when:
        def response = basicAuthRequest()
                .path(IO)
                .path(path)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method(method, CATALOG_PAYLOAD)

        then:
        response.status == responseStatus.statusCode

        where:
        isLegacySecurity | method  | path                       | responseStatus
        true             | 'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        false            | 'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        true             | 'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
        false            | 'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "OAuth2 #method requests are denied when integrationservices.legacy.authentication.enabled is false and no InboundChannelConfiguration exists for ChannelConfigTestIO"() {
        given:
        inboundServicesConfiguration.setLegacySecurity(false)

        when:
        def response = oAuth2RequestWithClientCredentialsGrantType()
                .path(IO)
                .path(CATALOGS)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method method

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        method << ['GET', 'POST', 'DELETE', 'PATCH']
    }

    @Test
    @Unroll
    def "Basic Auth #method requests are denied when integrationservices.legacy.authentication.enabled is false and no InboundChannelConfiguration exists for ChannelConfigTestIO"() {
        given:
        inboundServicesConfiguration.setLegacySecurity(false)

        when:
        def response = basicAuthRequest()
                .path(IO)
                .path(CATALOGS)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method method

        then:
        response.status == HttpStatusCodes.FORBIDDEN.statusCode

        where:
        method << ['GET', 'POST', 'DELETE', 'PATCH']
    }

    @Test
    @Unroll
    def "Oauth2 requests are denied when a GET request is made and no InboundChannel exists for ChannelConfigTestIO and integrationservices.legacy.authentication.enabled is true"() {
        given:
        inboundServicesConfiguration.setLegacySecurity(true)

        when:
        def response = oAuth2RequestWithClientCredentialsGrantType()
                .path(IO)
                .path(CATALOGS)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .delete()

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        isLegacySecurity | method
        true             | 'GET'
        false            | 'GET'
        true             | 'POST'
        false            | 'POST'
        true             | 'DELETE'
        false            | 'DELETE'
        true             | 'PATCH'
        false            | 'PATCH'
    }

    @Test
    @Unroll
    def "Basic auth request is authenticated when a #method request is made and no InboundChannel exists for ChannelConfigTestIO and integrationservices.legacy.authentication.enabled is true"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and:
        inboundServicesConfiguration.setLegacySecurity(true)

        when:
        def response = basicAuthRequest()
                .path(IO)
                .path(path)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method(method)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        where:
        method   | path
        'GET'    | CATALOGS
        'DELETE' | "$CATALOGS('$CATALOG_ID')"
    }

    @Test
    @Unroll
    def "Basic auth request is authenticated when a #method persistence request is made and no InboundChannel exists for ChannelConfigTestIO and integrationservices.legacy.authentication.enabled is true"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and:
        inboundServicesConfiguration.setLegacySecurity(true)

        when:
        def response = basicAuthRequest()
                .path(IO)
                .path(path)
                .credentials(ADMIN_USER, PASSWORD)
                .build()
                .method(method, CATALOG_PAYLOAD)

        then:
        response.status == responseStatus.statusCode

        where:
        method  | path                       | responseStatus
        'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "Oauth2 password #method request is authenticated with OAuthClientDetails when an OAUTH InboundChannel exists for the IO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=OAUTH"
        inboundChannelConfigurationExistsWithAuthenticationType("OAUTH")
        and:
        def oAuthClientId = "someOtherClientId"
        def oAuthClientSecret = "someOtherClientSecret"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE OAuthClientDetails; clientId[unique=true]; clientSecret      ; authorities               ',
                "                                ; $oAuthClientId       ; $oAuthClientSecret; ROLE_INTEGRATIONADMINGROUP"
        )
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.READ, ADMIN_USER)
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.REMOVE, ADMIN_USER)

        when:
        def response = oAuth2RequestWithPasswordGrantType()
                .client(oAuthClientId, oAuthClientSecret)
                .resourceOwner(ADMIN_USER, PASSWORD)
                .path(IO)
                .path("$CATALOGS('$CATALOG_ID')")
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method(method)

        then:
        response.status == HttpStatusCodes.OK.statusCode

        cleanup:
        IntegrationTestUtil.removeAll OAuthClientDetailsModel

        where:
        isLegacySecurity | method   | path
        true             | 'GET'    | CATALOGS
        false            | 'GET'    | CATALOGS
        true             | 'DELETE' | "$CATALOGS('$CATALOG_ID')"
        false            | 'DELETE' | "$CATALOGS('$CATALOG_ID')"

    }

    @Test
    @Unroll
    def "Oauth2 password #method persistence request is authenticated with OAuthClientDetails when an OAUTH InboundChannel exists for the IO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=OAUTH"
        inboundChannelConfigurationExistsWithAuthenticationType("OAUTH")
        and:
        def oAuthClientId = "someOtherClientId"
        def oAuthClientSecret = "someOtherClientSecret"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE OAuthClientDetails; clientId[unique=true]; clientSecret      ; authorities               ',
                "                                ; $oAuthClientId       ; $oAuthClientSecret; ROLE_INTEGRATIONADMINGROUP"
        )
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.CREATE, ADMIN_USER)
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.CHANGE, ADMIN_USER)

        when:
        def response = oAuth2RequestWithPasswordGrantType()
                .client(oAuthClientId, oAuthClientSecret)
                .resourceOwner(ADMIN_USER, PASSWORD)
                .path(IO)
                .path(path)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method(method, CATALOG_PAYLOAD)

        then:
        response.status == responseStatus.statusCode

        cleanup:
        IntegrationTestUtil.removeAll OAuthClientDetailsModel

        where:
        isLegacySecurity | method  | path                       | responseStatus
        true             | 'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        false            | 'POST'  | CATALOGS                   | HttpStatusCodes.CREATED
        true             | 'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
        false            | 'PATCH' | "$CATALOGS('$CATALOG_ID')" | HttpStatusCodes.OK
    }

    @Test
    @Unroll
    def "OAuth2 password #method request is denied with IntegrationClientCredentialsDetails when an OAUTH InboundChannel exists for the ChannelConfigTestIO"() {
        given: "Catalog with catalog id exists"
        IntegrationTestUtil.createCatalogWithId(CATALOG_ID)
        and: "An InboundChannel exists for ChannelConfigTestIO with authenticationType=OAUTH"
        inboundChannelConfigurationExistsWithAuthenticationType("OAUTH")
        and:
        inboundServicesConfiguration.setLegacySecurity(isLegacySecurity)
        and:
        addPermission(CatalogModel._TYPECODE, PermissionsConstants.READ, ADMIN_USER)

        when:
        def response = oAuth2RequestWithPasswordGrantType()
                .client(CLIENT_ID, CLIENT_SECRET)
                .resourceOwner(ADMIN_USER, PASSWORD)
                .path(IO)
                .path("$CATALOGS('$CATALOG_ID')")
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .method(method)

        then:
        response.status == HttpStatusCodes.UNAUTHORIZED.statusCode

        where:
        isLegacySecurity | method | path
        true             | 'GET'  | CATALOGS
        false            | 'GET'  | CATALOGS
    }

    def inboundChannelConfigurationExistsWithAuthenticationType(final String authType) {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType(code)',
                "                                         ; $IO                                   ; $authType"
        )
    }

    /**
     * @return an OAuth2 request builder with Grant-Type= "client_credentials"
     */
    def oAuth2RequestWithClientCredentialsGrantType() {
        new WsSecuredRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .grantClientCredentials()
                .client(CLIENT_ID, CLIENT_SECRET)
    }

    /**
     * @return an OAuth2 request builder with Grant-Type= "password"
     */
    def oAuth2RequestWithPasswordGrantType() {
        new WsSecuredRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .grantResourceOwnerPasswordCredentials()
    }

    def basicAuthRequest() {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .accept(MediaType.APPLICATION_JSON)
    }

    def addPermission(final String typeCode, final String permission, final String userUid) {
        def composedType = typeService.getComposedTypeForCode(typeCode)
        permissionManagementService.addTypePermission(composedType, new PermissionAssignment(permission, userService.getUserForUID(userUid)))
    }

    def removeAllPermissions(final String typeCode, final String userUid) {
        removePermission(typeCode, PermissionsConstants.READ, userUid)
        removePermission(typeCode, PermissionsConstants.CREATE, userUid)
        removePermission(typeCode, PermissionsConstants.CHANGE, userUid)
        removePermission(typeCode, PermissionsConstants.REMOVE, userUid)
    }

    def removePermission(final String typeCode, final String permission, final String userUid) {
        def composedType = typeService.getComposedTypeForCode(typeCode)
        permissionManagementService.removeTypePermission(composedType, new PermissionAssignment(permission, userService.getUserForUID(userUid)))
    }
}
