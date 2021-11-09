/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.schema.SchemaGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.odata2webservices.odata.builders.meta.payload.ScriptPayloadBuilder
import de.hybris.platform.scripting.enums.ScriptType
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ScriptServiceIntegrationTest extends ServicelayerTransactionalSpockSpecification {

    private static final def SCRIPT_CODE = 'Test_Script'
    private static final def SCRIPT_DESCRIPTION = "test_description"
    private static final def SCRIPT_CONTENT = 'Hello World from GROOVY script'
    private static final def SCRIPT_TYPE = ScriptType.GROOVY.name()
    private static final def SERVICE_NAME = 'ScriptService'
    private static final def ENTITY_SET = "Scripts"

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "oDataSchemaGenerator")
    private SchemaGenerator generator

    def setupSpec() {
        importCsv("/impex/essentialdata-scriptservices.impex", "UTF-8")
    }

    def cleanup() {
        IntegrationTestUtil.removeSafely ScriptModel, { it.code == SCRIPT_CODE }
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == SERVICE_NAME }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == SERVICE_NAME }
    }

    @Test
    def "GET Scripts returns all scripts with properly formatted content"() {
        given: "A script is persisted"
        insertDefaultScript()

        when:
        ODataResponse response = facade.handleRequest(oDataGetContext())

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 1
        json.getString('d.results[0].code') == SCRIPT_CODE
        json.getString('d.results[0].content') == SCRIPT_CONTENT
    }

    @Test
    def "Scripts can be filtered when making a GET request"() {
        given: "A few scripts existed"
        insertOrUpdateScript SCRIPT_CODE, SCRIPT_CONTENT, SCRIPT_DESCRIPTION
        insertOrUpdateScript 'anotherScript', 'another content', 'another description'

        when:
        def params = ['$filter': "description eq '$SCRIPT_DESCRIPTION'" as String]
        ODataResponse response = facade.handleRequest oDataGetContext(params)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 1
        json.getString('d.results[0].description') == SCRIPT_DESCRIPTION

        cleanup:
        IntegrationTestUtil.removeSafely ScriptModel, { it.code == 'anotherScript' }
    }

    @Test
    @Unroll
    def "Scripts can be orderedBy when making a GET request"() {
        given: "Two scripts exist"
        insertOrUpdateScript 'scriptA', 'content does not matter'
        insertOrUpdateScript 'scriptB', 'content does not matter'

        and:
        def params = ['$orderby': "code ${sorting}" as String]
        def context = oDataGetContext(params)

        when: "an additional script is created"
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 2
        json.getString('d.results[0].code') == orderedResult[0]
        json.getString('d.results[1].code') == orderedResult[1]

        cleanup:
        IntegrationTestUtil.removeSafely ScriptModel, { it.code == 'scriptA' }
        IntegrationTestUtil.removeSafely ScriptModel, { it.code == 'scriptB' }

        where:
        sorting | orderedResult
        'asc'   | ['scriptA', 'scriptB']
        'desc'  | ['scriptB', 'scriptA']
    }

    @Test
    @Unroll
    def "POST supports #scriptType ScriptType"() {
        given: "A script to post"
        def scriptBody = ScriptPayloadBuilder.script()
                .withCode(SCRIPT_CODE)
                .withScriptType(scriptType)
                .withContent(SCRIPT_CONTENT)
                .withDescription(SCRIPT_DESCRIPTION)
                .withAutoDisabling(true)
                .withDisabled(true)
                .build()

        when:
        ODataResponse responseFromPost = scriptIsPosted scriptBody

        then: "We get 201 CREATED"
        responseFromPost.getStatus() == HttpStatusCodes.CREATED

        and: "The POSTed script has the correct scriptType"
        ScriptModel scriptModel = IntegrationTestUtil.findAny(ScriptModel, { it.code == SCRIPT_CODE && it.active }).orElse(null)
        with(scriptModel) {
            getScriptType().getCode() == scriptType
            getDescription() == SCRIPT_DESCRIPTION
            getContent() == SCRIPT_CONTENT
            isAutodisabling()
            isDisabled()
        }

        where:
        scriptType << [ScriptType.GROOVY.name(), ScriptType.BEANSHELL.name(), ScriptType.JAVASCRIPT.name()]
    }

    @Test
    @Unroll
    def "PATCH supports #scriptType ScriptType"() {
        given: "A script exist already"
        insertDefaultScript()

        and: "A script to patch"
        def (newContent, description, disabled, autodisabled) = ["new content", "test description", true, true]
        def scriptBody = ScriptPayloadBuilder.script()
                .withScriptType(scriptType)
                .withContent(newContent)
                .withDescription(description)
                .withAutoDisabling(disabled)
                .withDisabled(autodisabled)
                .build()

        when: "We patch the script"
        ODataResponse responseFromPatch = scriptIsPatched SCRIPT_CODE, scriptBody

        then: "We get successful response"
        responseFromPatch.getStatus() == HttpStatusCodes.OK

        and: "The PATCHed script has the correct scriptType"
        ScriptModel scriptModel = IntegrationTestUtil.findAny(ScriptModel, { it.code == SCRIPT_CODE && it.active }).orElse(null)
        with(scriptModel) {
            getScriptType().getCode() == scriptType
            getDescription() == description
            getContent() == newContent
            isAutodisabling() == autodisabled
            isDisabled() == disabled
        }

        where:
        scriptType << [ScriptType.GROOVY.name(), ScriptType.BEANSHELL.name(), ScriptType.JAVASCRIPT.name()]
    }

    @Test
    def "POST script with different content three consecutive times, the active script is the last one posted"() {
        given:
        def lastContent = "content 3"

        when: "same script code is posted three times"
        scriptIsPosted SCRIPT_CODE, "content 1"
        scriptIsPosted SCRIPT_CODE, "content 2"
        ODataResponse response = scriptIsPosted SCRIPT_CODE, lastContent

        then:
        response.getStatus() == HttpStatusCodes.CREATED
        IntegrationTestUtil.findAll(ScriptModel,
                { it.code == SCRIPT_CODE && it.active && it.content == lastContent }).size() == 1
    }

    @Test
    def "PATCH script with different content three consecutive times, the active script is the last one patched"() {
        given:
        def lastContent = "content 3"
        insertOrUpdateScript SCRIPT_CODE, "content 1"

        when: "script is patched twice"
        scriptIsPatched(SCRIPT_CODE, ScriptPayloadBuilder.script()
                .withContent("content 2")
                .build())
        ODataResponse response = scriptIsPatched(SCRIPT_CODE, ScriptPayloadBuilder.script()
                .withContent(lastContent)
                .build())

        then:
        response.getStatus() == HttpStatusCodes.OK
        IntegrationTestUtil.findAll(ScriptModel,
                { it.code == SCRIPT_CODE && it.active && it.content == lastContent }).size() == 1
    }

    @Test
    def "POST with missing scriptType defaults to GROOVY scriptType"() {
        given:
        def scriptBody = ScriptPayloadBuilder.script()
                .withCode(SCRIPT_CODE)
                .withContent(SCRIPT_CONTENT)
                .build()

        when:
        ODataResponse responseFromPost = scriptIsPosted scriptBody

        then:
        responseFromPost.getStatus() == HttpStatusCodes.CREATED

        and: "The scriptType is GROOVY"
        IntegrationTestUtil.findAll(ScriptModel,
                { it.code == SCRIPT_CODE && it.active && it.scriptType == ScriptType.GROOVY }).size() == 1
    }

    @Test
    def "POST with unsupported scriptType returns 400"() {
        given:
        def scriptWithInvalidScriptType = ScriptPayloadBuilder.script()
                .withCode(SCRIPT_CODE)
                .withScriptType("JAVA")
                .withContent(SCRIPT_CONTENT)
                .build()

        when: "A script is POSTed with no scriptType"
        ODataResponse response = scriptIsPosted scriptWithInvalidScriptType

        then: "response returns a 400 bad request"
        def json = extractErrorFrom response
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains('Item referenced by attribute [scriptType] in [Script] item does not exist in the system.')
    }

    @Test
    def "PATCH with unsupported scriptType returns 400"() {
        given:
        insertDefaultScript()

        when: "A script is PATCHed with unsupported scriptType"
        ODataResponse response = facade.handleRequest(oDataPatchContext(SCRIPT_CODE,
                ScriptPayloadBuilder.script()
                        .withScriptType("JAVA")
                        .build()))

        then: "response returns a 400 bad request"
        def json = extractErrorFrom response
        json.getString('error.code') == 'missing_nav_property'
        json.getString('error.message.value').contains('Item referenced by attribute [scriptType] in [Script] item does not exist in the system.')
    }

    @Test
    def "POST with content field missing"() {
        given: "An invalid script"
        def scriptWithNoContent = ScriptPayloadBuilder.script()
                .withCode(SCRIPT_CODE)
                .build()

        when: "A script is POSTed with no content"
        ODataResponse response = scriptIsPosted scriptWithNoContent

        then: "response returns a 400 bad request"
        def json = extractErrorFrom response
        json.getString('error.code') == 'missing_property'
        json.getString('error.message.value').contains('Property [content] is required for EntityType')
    }

    @Test
    def "GET only returns active scripts when scripts have multiple revisions"() {
        given:
        def scriptCodeContentMap = ["script1": "content_1", "script2": "content_2", "script3": "content_3"]

        and: "multiple scripts with multiple revisions already exist where the content has the suffix '_version'"
        [0, 1, 2].each { version ->
            scriptCodeContentMap.each { insertOrUpdateScript(it.key, "${it.value}_${version}") }
        }

        and:
        def context = oDataGetContext()

        when:
        ODataResponse response = facade.handleRequest(context)

        then: "We only get the active scripts where the content has the suffix '_2'"
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 3
        json.getCollectionOfObjects("d.results[*].code").containsAll(scriptCodeContentMap.keySet())
        json.getCollectionOfObjects("d.results[*].content")
                .containsAll(scriptCodeContentMap.values().collect { "${it}_2" as String })

        cleanup:
        scriptCodeContentMap.each { entry ->
            IntegrationTestUtil.removeSafely(ScriptModel, { it.code == entry.key })
        }
    }

    @Test
    def "\$count only counts active scripts when multiple scripts with multiple revisions exist"() {
        given:
        def scriptCodeContentMap = ["script1": "content_1", "script2": "content_2", "script3": "content_3"]

        and: "3 scripts each with 3 revisions exist"
        [0, 1, 2].each { version ->
            scriptCodeContentMap.each { insertOrUpdateScript(it.key, "${it.value}_${version}") }
        }

        and:
        def context = oDataGetContextWithNavSeg '$count'

        when:
        ODataResponse response = facade.handleRequest context

        then: "only 3 active scripts are counted"
        response.getEntity().toString() == "3"

        cleanup:
        scriptCodeContentMap.each { entry ->
            IntegrationTestUtil.removeSafely(ScriptModel, { it.code == entry.key })
        }
    }

    @Test
    def "\$inlinecount only counts active scripts when multiple scripts with multiple revision exist"() {
        given:
        def scriptCodeContentMap = ["script1": "content_1", "script2": "content_2", "script3": "content_3"]

        and: "3 scripts each with 3 revisions exist"
        [0, 1, 2].each { version ->
            scriptCodeContentMap.each { insertOrUpdateScript(it.key, "${it.value}_${version}") }
        }

        and:
        def context = oDataGetContext(['$top': "100", '$inlinecount': 'allpages'])

        when:
        ODataResponse response = facade.handleRequest context

        then:
        def json = extractEntitiesFrom response

        and: "only 3 active scripts are counted"
        json.getCollectionOfObjects("d.results").size() == 3
        json.getString('d.__count').toString() == "3"

        cleanup:
        scriptCodeContentMap.each { entry ->
            IntegrationTestUtil.removeSafely(ScriptModel, { it.code == entry.key })
        }
    }

    @Test
    def "DELETE a script returns 200"() {
        given: "A valid script is created"
        insertDefaultScript()

        when: "A script is DELETED"
        ODataResponse response = facade.handleRequest oDataDeleteContext(SCRIPT_CODE)

        then: "response returns a 200 OK"
        response.getStatus() == HttpStatusCodes.OK
    }

    @Test
    def "DELETE does not delete inactive scripts"() {
        given: "A script has 3 revisions"
        [0, 1, 2].each { version -> insertOrUpdateScript(SCRIPT_CODE, "${SCRIPT_CONTENT}_${version}") }

        when: "The script is DELETED"
        ODataResponse response = facade.handleRequest oDataDeleteContext(SCRIPT_CODE)

        then: "response returns a 200 OK"
        response.getStatus() == HttpStatusCodes.OK

        and: "Only two inactive scripts remain persisted"
        Collection<ScriptModel> scriptModels = IntegrationTestUtil.findAll(ScriptModel, { it.code == SCRIPT_CODE })
        scriptModels.size() == 2
        scriptModels.collect { it.content }.containsAll("${SCRIPT_CONTENT}_0" as String, "${SCRIPT_CONTENT}_1" as String)
        scriptModels.every { !it.active }
    }

    def insertDefaultScript() {
        insertOrUpdateScript(SCRIPT_CODE, SCRIPT_CONTENT)
    }

    def insertOrUpdateScript(def code, def content, def description = SCRIPT_DESCRIPTION, def scriptType = SCRIPT_TYPE) {
        IntegrationTestUtil.importImpEx('INSERT_UPDATE Script; code[unique = true]   ; scriptType(code)   ; description          ; content      ; active[unique = true, default = true]',
                "                    ; $code                 ; $scriptType        ; $description         ; $content     ;")
    }

    ODataContext oDataGetContext(Map params = [:]) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET))
                .withParameters(params)
                .build()
        contextGenerator.generate request
    }

    ODataContext oDataGetContextWithNavSeg(String navSeg) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET)
                        .withNavigationSegment(navSeg))
                .build()
        contextGenerator.generate request
    }

    ODataContext oDataPostContext(String body) {
        def request = ODataRequestBuilder.oDataPostRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET))
                .withBody(body)
                .build()
        contextGenerator.generate request
    }

    ODataContext oDataPatchContext(String key, String body) {
        def request = ODataRequestBuilder.oDataPatchRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(key))
                .withBody(body)
                .build()
        contextGenerator.generate request
    }

    ODataContext oDataDeleteContext(String key) {
        def request = ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_SET)
                        .withEntityKeys(key))
                .withAccepts(APPLICATION_JSON_VALUE)
                .build()
        contextGenerator.generate request
    }

    def scriptIsPosted(String body) {
        def context = oDataPostContext(body)
        facade.handleRequest(context)
    }

    def scriptIsPosted(String scriptCode, String content) {
        def scriptBody = ScriptPayloadBuilder.script()
                .withCode(scriptCode)
                .withContent(content)
                .withScriptType(ScriptType.GROOVY.name())
                .build()
        scriptIsPosted(scriptBody)
    }

    def scriptIsPatched(String key, String body) {
        def context = oDataPatchContext(key, body)
        facade.handleRequest(context)
    }

    def extractEntitiesFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.OK)
    }

    def extractErrorFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.BAD_REQUEST)
    }

    def extractBodyWithExpectedStatus(ODataResponse response, HttpStatusCodes expStatus) {
        assert response.getStatus() == expStatus
        JsonObject.createFrom response.getEntity() as InputStream
    }
}