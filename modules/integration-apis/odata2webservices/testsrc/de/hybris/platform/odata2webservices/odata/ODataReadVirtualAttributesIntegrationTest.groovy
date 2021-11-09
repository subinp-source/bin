/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.test.TestEmployeeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource
import java.text.SimpleDateFormat

import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataReadVirtualAttributesIntegrationTest extends ServicelayerSpockSpecification {
    private static final def IO = 'GET_VA'
    private static final def ITEM_TYPE = 'User'
    private static final def VIRTUAL_ATTRIBUTE = 'displayLastLogin'
    private static final def USER_ID = 'tester'
    private static final def DATE_FORMAT = 'M-d-yy H:m:s'
    private static final def TODAY = new SimpleDateFormat(DATE_FORMAT).format(new Date())
    private static final def SCRIPT_CODE = 'lastLoginScript'
    private static final def SCRIPT = """
        import de.hybris.platform.core.model.user.UserModel
        import java.text.SimpleDateFormat
        
        def user = itemModel as UserModel
        new SimpleDateFormat('$DATE_FORMAT').format(user.lastLogin)
        """

    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        def descriptorCode = "${SCRIPT_CODE}Descriptor"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation       ; type(code)',
                "                                                         ; $descriptorCode    ; model://$SCRIPT_CODE; java.lang.String",
        )
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $IO",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)  ; root',
                "                                   ; $IO               ; $ITEM_TYPE         ; TestEmployee; true",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor',
                "                                            ; $IO:$ITEM_TYPE      ; uid                         ; User:uid",
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code)',
                "                                                   ; $IO:$ITEM_TYPE      ; $VIRTUAL_ATTRIBUTE          ; $descriptorCode")
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll IntegrationObjectVirtualAttributeDescriptorModel
    }

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique=true]; scriptType(code); autodisabling; content',
                "                    ; $SCRIPT_CODE     ; GROOVY          ; false        ; \"$SCRIPT\"")
    }

    def cleanup() {
        IntegrationTestUtil.remove ScriptModel, { it.code == SCRIPT_CODE }
        IntegrationTestUtil.remove TestEmployeeModel, { it.uid == USER_ID }
    }

    @Test
    @Unroll
    def "GET #uri returns a virtual attribute value when the virtual attribute script executes successfully"() {
        given: 'there is a user who already has logged into the system'
        IntegrationTestUtil.importImpEx(
                "INSERT TestEmployee; uid[unique = true]; lastLogin[dateformat = $DATE_FORMAT]",
                "                   ; $USER_ID          ; $TODAY")

        when:
        def response = facade.handleRequest get(uid)

        then:
        response.status == HttpStatusCodes.OK
        def body = JsonObject.createFrom response.entityAsStream
        body.getObject(path) == pathValue

        where:
        uri                  | uid     | path                                                 | pathValue
        '/Users'             | null    | "d.results[?(@.uid == 'tester')].$VIRTUAL_ATTRIBUTE" | [TODAY]
        "/Users('$USER_ID')" | USER_ID | "d.$VIRTUAL_ATTRIBUTE"                               | TODAY
    }

    @Test
    @Unroll
    def "GET #uri responds with error when the virtual attribute script crashes for an item specific problem"() {
        given: 'the user has not logged into the system and lastLogin is null'
        IntegrationTestUtil.importImpEx(
                'INSERT TestEmployee; uid[unique = true]',
                "                   ; $USER_ID")

        when:
        def response = facade.handleRequest get(uid)

        then: 'an error is reported because the script does not handle null lastLogin'
        response.status == HttpStatusCodes.BAD_REQUEST
        def body = JsonObject.createFrom response.entityAsStream
        body.getString('error.code') == 'runtime_error'
        body.getString('error.message.value').contains ITEM_TYPE
        body.getString('error.message.value').contains VIRTUAL_ATTRIBUTE

        where:
        uri                  | uid
        '/Users'             | null
        "/Users('$USER_ID')" | USER_ID
    }

    @Test
    @Unroll
    def "GET #uri responds with error when the virtual attribute script crashes for a systemic problem"() {
        given: 'the user has logged into the system and lastLogin is not null'
        IntegrationTestUtil.importImpEx(
                "INSERT TestEmployee; uid[unique = true]; lastLogin[dateformat = $DATE_FORMAT]",
                "                   ; $USER_ID          ; $TODAY")
        and: 'the script was deleted'
        IntegrationTestUtil.remove ScriptModel, { it.code == SCRIPT_CODE }

        when:
        def response = facade.handleRequest get(uid)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def body = JsonObject.createFrom response.entityAsStream
        body.getString('error.code') == 'misconfigured_attribute'
        body.getString('error.message.value').contains ITEM_TYPE
        body.getString('error.message.value').contains VIRTUAL_ATTRIBUTE

        where:
        uri                  | uid
        '/Users'             | null
        "/Users('$USER_ID')" | USER_ID
    }

    @Test
    def 'GET does not support filtering by virtual attribute'() {
        when:
        def response = facade.handleRequest getWithParams($filter: VIRTUAL_ATTRIBUTE + " eq '" + TODAY + "'")

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def body = JsonObject.createFrom response.entityAsStream
        body.getString('error.code') == 'filter_by_virtual_attribute_not_supported'
        body.getString('error.message.value').contains VIRTUAL_ATTRIBUTE
    }

    @Test
    def 'GET does not support order by virtual attribute'() {
        when:
        def response = facade.handleRequest getWithParams($orderby: VIRTUAL_ATTRIBUTE)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        def body = JsonObject.createFrom response.entityAsStream
        body.getString('error.code') == 'order_by_virtual_attribute_not_supported'
        body.getString('error.message.value').contains VIRTUAL_ATTRIBUTE
    }

    @Test
    def 'GET with order by a standard attribute is still possible when virtual attributes are present in the IO'() {
        when:
        def response = facade.handleRequest getWithParams($orderby: 'uid')

        then:
        response.status == HttpStatusCodes.OK
    }

    private static ODataContext getWithParams(Map params) {
        get(null, params)
    }

    private static ODataContext get(String uid, Map params = [:]) {
        createContext ODataRequestBuilder.oDataGetRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(IO)
                        .withEntitySet('Users')
                        .withEntityKeys(uid))
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
    }
}
