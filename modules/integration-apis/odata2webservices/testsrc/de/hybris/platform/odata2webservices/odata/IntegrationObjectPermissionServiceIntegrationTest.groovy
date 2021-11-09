package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.core.model.security.UserRightModel
import de.hybris.platform.core.model.user.UserModel
import de.hybris.platform.europe1.model.PriceRowModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.config.ConfigurationService
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants
import de.hybris.platform.servicelayer.type.TypeService
import de.hybris.platform.servicelayer.user.UserService
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class IntegrationObjectPermissionServiceIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = 'IntegrationObjectPermissionServiceIntegrationTestIO'
    private static final String PRICE_ROWS = "PriceRows"
    private static final String ERROR_CODE = "forbidden"
    private static final String DEFAULT_CURRENCY = 'USD'
    private static final String DEFAULT_UNIT = 'pieces'
    private static final String NEW_UNIT = 'newPieces'
    private static final String DEFAULT_PRICE = '5.0'
    private static final String DIFFERENT_PRICE = '10.00'

    @Resource
    private PermissionManagementService permissionManagementService
    @Resource
    private TypeService typeService
    @Resource
    private UserService userService
    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "defaultConfigurationService")
    private ConfigurationService configurationService

    def setupSpec() {
        importCsv '/impex/essentialdata-odata2services.impex', 'UTF-8'
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO                ",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code);root[default = false]',
                "                                   ; $IO                                   ; PriceRow           ; PriceRow  ;true                 ",
                "                                   ; $IO                                   ; Currency           ; Currency  ;                     ",
                "                                   ; $IO                                   ; Unit               ; Unit      ;                     ",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $IO:Currency                                                       ; isocode                     ; Currency:isocode                                   ;                                                           ; true                   ",
                "                                            ; $IO:PriceRow                                                       ; currency                    ; PriceRow:currency                                  ; $IO:Currency                                              ; true                   ",
                "                                            ; $IO:PriceRow                                                       ; unit                        ; PriceRow:unit                                      ; $IO:Unit                                                  ;                        ; true",
                "                                            ; $IO:PriceRow                                                       ; price                       ; PriceRow:price                                     ;                                                           ; true                   ",
                "                                            ; $IO:PriceRow                                                       ; matchValue                  ; PriceRow:matchValue                                ;                                                           ;                        ",
                "                                            ; $IO:Unit                                                           ; code                        ; Unit:code                                          ;                                                           ; true                   ",
                "                                            ; $IO:Unit                                                           ; unitType                    ; Unit:unitType                                      ;                                                           ;                        ",
        )
        importImpEx(
                'INSERT_UPDATE Currency;isocode[unique=true]',
                "                      ;$DEFAULT_CURRENCY   "
        )
    }

    def setup() {
        removePermissions(PriceRowModel._TYPECODE, PermissionsConstants.READ, PermissionsConstants.CREATE, PermissionsConstants.CHANGE, PermissionsConstants.REMOVE)
        removePermissions(CurrencyModel._TYPECODE, PermissionsConstants.READ, PermissionsConstants.CREATE, PermissionsConstants.CHANGE, PermissionsConstants.REMOVE)
        removePermissions(UserModel._TYPECODE, PermissionsConstants.READ, PermissionsConstants.CREATE, PermissionsConstants.CHANGE, PermissionsConstants.REMOVE)

        importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true]',
                "                  ;$DEFAULT_UNIT    ;EA                   ",
                'INSERT_UPDATE PriceRow;price[unique=true];currency(isocode)[unique=true];unit(code)',
                "                          ;$DEFAULT_PRICE;$DEFAULT_CURRENCY             ;$DEFAULT_UNIT")
    }

    def cleanup() {
        disableAccessRights()
        IntegrationTestUtil.removeSafely(UnitModel, { it.code == NEW_UNIT })
        IntegrationTestUtil.remove(PriceRowModel, { it.price == DEFAULT_PRICE || it.price == DIFFERENT_PRICE })
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeSafely(IntegrationObjectModel, { it.code == IO })
        IntegrationTestUtil.removeSafely(UnitModel, { it.code == DEFAULT_UNIT })
        IntegrationTestUtil.removeSafely(CurrencyModel, { it.isocode == DEFAULT_CURRENCY })
        IntegrationTestUtil.removeSafely(UserRightModel, {
            it.code == PermissionsConstants.READ ||
                    PermissionsConstants.CREATE ||
                    PermissionsConstants.CHANGE ||
                    PermissionsConstants.REMOVE
        })
    }

    @Test
    def "grant user read access when read permission is granted to parent item only"() {
        given:
        enableAccessRights()
        addPermission(PriceRowModel._TYPECODE, PermissionsConstants.READ)
        def context = constructGetRequestContext(PRICE_ROWS)

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
    }

    @Test
    def "deny user read access when read permission is not granted"() {
        given:
        enableAccessRights()
        def context = constructGetRequestContext(PRICE_ROWS)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.FORBIDDEN
        def json = extractErrorResponse(response)
        json.getString("error.code") == ERROR_CODE
        json.getString("error.message.value").contains('read')
        json.getString("error.message.value").contains('PriceRow')
    }

    @Test
    def "grant user read access when read permission is not granted and access rights are not enabled"() {
        given:
        def context = constructGetRequestContext(PRICE_ROWS)

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
    }

    @Test
    def "grant user create access when create permission is granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        addPermission(PriceRowModel._TYPECODE, PermissionsConstants.CREATE)
        def context = constructPostRequestContext(PRICE_ROWS, constructPostRequestBody(DIFFERENT_PRICE))

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.CREATED

        cleanup:
        removeReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        removePermission(PriceRowModel._TYPECODE, PermissionsConstants.CREATE)
    }

    @Test
    def "deny user create access when create permission is not granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        def context = constructPostRequestContext(PRICE_ROWS, constructPostRequestBody(DIFFERENT_PRICE))

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.FORBIDDEN
        def json = extractErrorResponse(response)
        json.getString("error.code") == ERROR_CODE
        json.getString("error.message.value").contains('create')
        json.getString("error.message.value").contains('PriceRow')
    }

    @Test
    def "user has no post access when create permission is not granted and the exact same item already exists"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        def context = constructPostRequestContext(PRICE_ROWS, constructPostRequestBody(DEFAULT_PRICE))

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.FORBIDDEN
        def json = extractErrorResponse(response)
        json.getString("error.code") == ERROR_CODE
        json.getString("error.message.value").contains('create')
        json.getString("error.message.value").contains('PriceRow')
    }

    @Test
    def "grant user create access when create permission is not granted and access rights are not enabled"() {
        given:
        def context = constructPostRequestContext(PRICE_ROWS, constructPostRequestBody(DEFAULT_PRICE))

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.CREATED
    }

    @Test
    def "grant user change access when change permission is granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        addPermission(PriceRowModel._TYPECODE, PermissionsConstants.CHANGE)
        def context = constructPatchRequestContext(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK

        cleanup:
        removeReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        removePermission(PriceRowModel._TYPECODE, PermissionsConstants.CHANGE)
    }

    @Test
    def "deny user change access when change permission is not granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        addPermission(UnitModel._TYPECODE, PermissionsConstants.CREATE)
        addPermission(UnitModel._TYPECODE, PermissionsConstants.CHANGE)
        def context = constructPatchRequestContext(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.FORBIDDEN
        def json = extractErrorResponse(response)
        json.getString("error.code") == ERROR_CODE
        json.getString("error.message.value").contains('change')
        json.getString("error.message.value").contains('PriceRow')
    }

    @Test
    def "user does not have patch access when change permission is not granted and the exact same item already exists"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        def context = constructPatchRequestContextForSameItem(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.FORBIDDEN
        def json = extractErrorResponse(response)
        json.getString("error.code") == ERROR_CODE
        json.getString("error.message.value").contains('change')
        json.getString("error.message.value").contains('PriceRow')
    }

    @Test
    def "grant user change access when change permission is not granted and access rights are not enabled"() {
        given:
        def context = constructPatchRequestContext(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
    }

    @Test
    def "grant user delete access when delete permission is granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        addPermission(PriceRowModel._TYPECODE, PermissionsConstants.REMOVE)
        def context = constructDeleteRequestContext(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK

        cleanup:
        removeReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        removePermission(PriceRowModel._TYPECODE, PermissionsConstants.REMOVE)
    }

    @Test
    def "deny user delete access when delete permission is not granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        def context = constructDeleteRequestContext(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.FORBIDDEN
        def json = extractErrorResponse(response)
        json.getString("error.code") == ERROR_CODE
        json.getString("error.message.value").contains('remove')
        json.getString("error.message.value").contains('PriceRow')

        cleanup:
        removeReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
    }

    @Test
    def "grant user delete access when delete permission is not granted and access rights are not enabled"() {
        given:
        addReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
        def context = constructDeleteRequestContext(PRICE_ROWS, "$DEFAULT_CURRENCY|$DEFAULT_PRICE")

        when:
        def response = facade.handleRequest(context)

        then:
        response.getStatus() == HttpStatusCodes.OK

        cleanup:
        removeReadPermissionForTypes(PriceRowModel._TYPECODE, CurrencyModel._TYPECODE, UnitModel._TYPECODE)
    }

    @Test
    @Unroll
    def "grant user metadata access when #permission permission is granted and access rights are enabled"() {
        given:
        enableAccessRights()
        addPermission(PriceRowModel._TYPECODE, permission)
        def context = constructMetadataRequestContext()

        when:
        def response = facade.handleGetSchema(context)

        then:
        response.getStatus() == HttpStatusCodes.OK

        cleanup:
        removePermission(PriceRowModel._TYPECODE, permission)

        where:
        permission << [PermissionsConstants.READ, PermissionsConstants.CREATE, PermissionsConstants.CHANGE, PermissionsConstants.REMOVE]
    }

    @Test
    def "deny user metadata access when no permission is granted and access rights are enabled"() {
        given:
        enableAccessRights()
        def context = constructMetadataRequestContext()

        when:
        facade.handleGetSchema(context)

        then:
        def e = thrown(TypeAccessPermissionException)
        e.message.contains 'PriceRow'
    }

    @Test
    def "grant user metadata access when no permission is granted and access rights are not enabled"() {
        given:
        def context = constructMetadataRequestContext()

        when:
        def response = facade.handleGetSchema(context)

        then:
        response.getStatus() == HttpStatusCodes.OK
    }

    def constructGetRequestContext(final String entitySetName) {
        def request = requestBuilder(ODataRequestBuilder.oDataGetRequest(), entitySetName).build()
        contextGenerator.generate(request)
    }

    def constructPostRequestContext(final String entitySetName, JsonBuilder requestBody) {
        def requestBuilder = requestBuilder(ODataRequestBuilder.oDataPostRequest(), entitySetName)
        createContext(requestBuilder.withBody(requestBody).build())
    }

    def constructPatchRequestContextForSameItem(final String entitySetName, final String entityKey) {
        def builder = requestBuilderWithKey(ODataRequestBuilder.oDataPatchRequest(), entitySetName, entityKey)
        createContext(builder.withBody(json().withField('price', DEFAULT_PRICE)
                .withField("currency", json().withField("isocode", DEFAULT_CURRENCY).build())
                .withField("unit", json().withField("code", DEFAULT_UNIT).build())).build())
    }

    def constructPatchRequestContext(final String entitySetName, final String entityKey) {
        def builder = requestBuilderWithKey(ODataRequestBuilder.oDataPatchRequest(), entitySetName, entityKey)
        createContext(builder.withBody(json().withField('price', DEFAULT_PRICE)
                .withField('matchValue', 9)
                .withField("currency", json().withField("isocode", DEFAULT_CURRENCY).build())
                .withField("unit", json().withField("code", DEFAULT_UNIT).build())).build())
    }

    def constructDeleteRequestContext(final String entitySetName, final String entityKey) {
        def builder = requestBuilderWithKey(ODataRequestBuilder.oDataDeleteRequest(), entitySetName, entityKey)
        createContext(builder.build())
    }

    def constructMetadataRequestContext() {
        def builder = ODataRequestBuilder.oDataGetRequest()
                .withPathInfo(PathInfoBuilder.pathInfo().withServiceName(IO).withRequestPath('$metadata'))
        createContext(builder.build())
    }

    def requestBuilderWithKey(final ODataRequestBuilder oDataRequestBuilder, final String entitySetName, final String entityKey, final String navigationProperty = "") {
        oDataRequestBuilder
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(pathInfoWithKey(entitySetName, entityKey, navigationProperty))
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
    }

    def requestBuilder(final ODataRequestBuilder oDataRequestBuilder, final String entitySetName, final String navProperty = "") {
        oDataRequestBuilder
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(pathInfo(entitySetName, navProperty))
                .withContentType(APPLICATION_JSON_VALUE)
                .withAccepts(APPLICATION_JSON_VALUE)
    }

    def pathInfo(final String entitySetName, final String navProperty) {
        PathInfoBuilder.pathInfo().withServiceName(IO).withEntitySet(entitySetName).withNavigationSegment(navProperty)
    }

    def pathInfo(final String entitySetName) {
        PathInfoBuilder.pathInfo().withServiceName(IO).withEntitySet(entitySetName)
    }

    def pathInfoWithKey(final String entitySetName, final String entityKey, final String navProperty) {
        pathInfo(entitySetName).withNavigationSegment(navProperty).withEntityKeys(entityKey)
    }

    def extractSuccessResponse(final ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.OK)
    }

    def extractErrorResponse(final ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.FORBIDDEN)
    }

    def extractBodyWithExpectedStatus(final ODataResponse response, final HttpStatusCodes expStatus) {
        assert response.getStatus() == expStatus
        JsonObject.createFrom(response.getEntityAsStream())
    }

    def addPermission(final String typeCode, final String permission) {
        def composedType = typeService.getComposedTypeForCode(typeCode)
        permissionManagementService.addTypePermission(composedType, new PermissionAssignment(permission, userService.getCurrentUser()))
    }

    def removePermission(final String typeCode, final String permission) {
        def composedType = typeService.getComposedTypeForCode(typeCode)
        permissionManagementService.removeTypePermission(composedType, new PermissionAssignment(permission, userService.getCurrentUser()))
    }

    def removePermissions(final String typeCode, final String[] permissions = []) {
        permissions.each { permission -> removePermission(typeCode, permission) }
    }

    def removeReadPermissionForTypes(final String[] typeCodes) {
        typeCodes.each { typeCode -> removePermission(typeCode, PermissionsConstants.READ) }
    }

    def addReadPermissionForTypes(final String[] typeCodes) {
        typeCodes.each { typeCode -> addPermission(typeCode, PermissionsConstants.READ) }
    }

    def constructPostRequestBody(String price) {
        json().withField('price', price)
                .withField("currency", json().withField("isocode", DEFAULT_CURRENCY).build())
                .withField("unit", json().withField("code", DEFAULT_UNIT).withField("unitType", "EA").build())
    }

    def enableAccessRights() {
        setAccessRights(true)
    }

    def disableAccessRights() {
        setAccessRights(false)
    }

    def setAccessRights(final boolean enabled) {
        configurationService.getConfiguration().setProperty("integrationservices.authorization.accessrights.enabled", String.valueOf(enabled))
    }
}
