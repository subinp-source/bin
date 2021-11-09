/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.odata2services.converter.ODataEntryToIntegrationItemConverter
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceParam
import org.apache.olingo.odata2.api.commons.HttpHeaders
import org.apache.olingo.odata2.api.commons.ODataHttpMethod
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.uri.PathInfo
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultStorageRequestFactoryUnitTest extends Specification {
    def SERVICE_NAME = "InboundProduct"
    def INTEGRATION_KEY_VALUE = "asdf|fdsa|asdf"
    def CONTENT_TYPE = "application/json"
    def PRE_PERSIST_HOOK = "Pre-Persist-Hook"
    def POST_PERSIST_HOOK = "Post-Persist-Hook"
    def LOCALE = Locale.ENGLISH

    def edmEntitySet = Stub EdmEntitySet
    def oDataEntry = Stub ODataEntry
    def localeExtractor = Stub(ODataContextLanguageExtractor) {
        extractFrom(_ as ODataContext, HttpHeaders.CONTENT_LANGUAGE) >> LOCALE
        localeExtractor.getAcceptLanguage(_ as ODataContext) >> Optional.empty()
    }
    def entryConverter = Stub(ODataEntryToIntegrationItemConverter) {
        convert(_ as ODataContext, edmEntitySet, oDataEntry) >> Stub(IntegrationItem) {
            getIntegrationKey() >> INTEGRATION_KEY_VALUE
            getIntegrationObjectCode() >> SERVICE_NAME
        }
    }

    private DefaultStorageRequestFactory factory = new DefaultStorageRequestFactory(
            localeExtractor: localeExtractor, entryConverter: entryConverter)

    @Test
    def "handles ODataException thrown from the ODataContext"() {
        given:
        def oDataContext = Stub(ODataContext) {
            getPathInfo() >> { throw new ODataException() }
        }

        when:
        factory.create(oDataContext, CONTENT_TYPE, edmEntitySet, oDataEntry)

        then:
        def e = thrown OdataRequestDataProcessingException
        e.cause instanceof ODataException
    }

    @Test
    def "sets accept locale to content locale when accept locale header is not provided"() {
        given:
        def oDataContext = oDataContext()
        and: 'Accept-Language is not present in the request'
        localeExtractor.extractFrom(oDataContext, HttpHeaders.CONTENT_LANGUAGE) >> LOCALE
        localeExtractor.getAcceptLanguage(oDataContext) >> Optional.empty()

        when:
        def storageRequest = factory.create(oDataContext, CONTENT_TYPE, edmEntitySet, oDataEntry)

        then:
        storageRequest.acceptLocale == LOCALE
    }

    @Test
    def "creates a storage request successfully with expected fields"() {
        given:
        def oDataContext = oDataContext()
        oDataContext.getRequestHeader(PRE_PERSIST_HOOK) >> "prePersistHookName"
        oDataContext.getRequestHeader(POST_PERSIST_HOOK) >> "postPersistHookName"
        localeExtractor.extractFrom(oDataContext, HttpHeaders.CONTENT_LANGUAGE) >> Locale.ENGLISH
        localeExtractor.getAcceptLanguage(oDataContext) >> Optional.of(Locale.GERMAN)

        when:
        def storageRequest = factory.create oDataContext, CONTENT_TYPE, edmEntitySet, oDataEntry

        then:
        with(storageRequest) {
            integrationKey == INTEGRATION_KEY_VALUE
            entitySet == edmEntitySet
            oDataEntry == oDataEntry
            acceptLocale == Locale.GERMAN
            contentLocale == Locale.ENGLISH
            prePersistHook == "prePersistHookName"
            postPersistHook == "postPersistHookName"
            integrationObjectCode == SERVICE_NAME
            serviceRoot == new URI("https://localhost:9002/odata2webservices/InboundProduct")
            contentType == CONTENT_TYPE
            requestUri == new URI("https://localhost:9002/odata2webservices/InboundProduct/Products")
            integrationItem != null
        }
    }

    @Test
    @Unroll
    def "storage request isReplaceAttributes is #expected when request method is #method"() {
        given:
        def request = Stub(ODataRequest) {
            getMethod() >> method
        }

        when:
        def storageRequest = factory.create oDataContext(request), CONTENT_TYPE, edmEntitySet, oDataEntry

        then:
        storageRequest.replaceAttributes == expected

        where:
        method   | expected
        'PATCH'  | true
        'POST'   | false
        'DELETE' | false
        'GET'    | false
    }

    @Test
    @Unroll
    def "storage request isItemCanBeCreated is #expected when context request method is #method"() {
        given:
        def request = Stub(ODataRequest) {
            getMethod() >> method
        }

        when:
        def storageRequest = factory.create oDataContext(request), CONTENT_TYPE, edmEntitySet, oDataEntry

        then:
        storageRequest.itemCanBeCreated == expected

        where:
        method                 | expected
        ODataHttpMethod.PATCH  | false
        ODataHttpMethod.POST   | true
        ODataHttpMethod.DELETE | false
        ODataHttpMethod.GET    | false
    }

    @Test
    def 'creates storage request from PersistenceParameters'() {
        given:
        def context = oDataContext()
        def params = Stub(PersistenceParam) {
            getContext() >> context
            getResponseContentType() >> CONTENT_TYPE
            getEntitySet() >> edmEntitySet
        }
        and:
        localeExtractor.extractFrom(context, HttpHeaders.CONTENT_LANGUAGE) >> Locale.ENGLISH
        localeExtractor.getAcceptLanguage(context) >> Optional.of(Locale.GERMAN)

        when:
        def storageRequest = factory.create params, oDataEntry

        then:
        storageRequest.entitySet == edmEntitySet
        storageRequest.contentType == CONTENT_TYPE
        storageRequest.contentLocale == Locale.ENGLISH
        storageRequest.acceptLocale == Locale.GERMAN
    }

    private ODataContext oDataContext() {
        oDataContext Stub(ODataRequest)
    }

    private ODataContext oDataContext(ODataRequest request) {
        Stub(ODataContext) {
            getPathInfo() >> Stub(PathInfo) {
                getServiceRoot() >> new URI("https://localhost:9002/odata2webservices/InboundProduct")
                getRequestUri() >> new URI("https://localhost:9002/odata2webservices/InboundProduct/Products")
            }
            getParameter('~odataRequest') >> request
        }
    }
}
