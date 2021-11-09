/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.read

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException
import de.hybris.platform.integrationservices.search.ItemSearchException
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.filter.IntegrationKeyNestedFilteringNotSupportedException
import de.hybris.platform.odata2services.odata.OData2ServicesException
import de.hybris.platform.odata2services.odata.asserts.ODataResponseAssertion
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException
import de.hybris.platform.odata2services.odata.persistence.exception.PropertyNotFoundException
import de.hybris.platform.odata2services.odata.processor.RetrievalErrorRuntimeException
import de.hybris.platform.odata2services.odata.processor.reader.EntityReader
import de.hybris.platform.odata2services.odata.processor.reader.EntityReaderRegistry
import org.apache.http.HttpHeaders
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.apache.olingo.odata2.api.uri.UriInfo
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ReadHandlerUnitTest extends Specification {

    def itemLookupRequestFactory = Stub ItemLookupRequestFactory
    def entityReaderRegistry = Stub EntityReaderRegistry
    def handler = new ReadHandler(itemLookupRequestFactory: itemLookupRequestFactory, entityReaderRegistry: entityReaderRegistry)

    @Test
    def "read item"() {
        given:
        def contentType = 'application/xml'
        def context = Stub ODataContext
        def uriInfo = Stub UriInfo
        def param = DefaultReadParam.readParam()
                .withResponseContentType(contentType)
                .withContext(context)
                .withUriInfo(uriInfo)
                .build()

        and:
        def itemLookupRequest = Stub(ItemLookupRequest) {
            getAcceptLocale() >> Locale.FRENCH
        }
        itemLookupRequestFactory.create(uriInfo, context, contentType) >> itemLookupRequest

        and:
        def json = JsonBuilder.json().withField('code', 'value').build()
        entityReaderRegistry.getReader(uriInfo) >> Stub(EntityReader) {
            read(itemLookupRequest) >> ODataResponse.entity(json).status(HttpStatusCodes.OK).build()
        }

        when:
        def response = handler.handle param

        then:
        response.getHeader(HttpHeaders.CONTENT_LANGUAGE) == Locale.FRENCH.getLanguage()
        ODataResponseAssertion.assertionOf(response)
                .hasStatus(HttpStatusCodes.OK)
                .jsonBody()
                .hasPathWithValue('code', 'value')
    }

    @Test
    @Unroll
    def "fail to read with #exception"() {
        given:
        def param = DefaultReadParam.readParam()
                .withUriInfo(Stub(UriInfo))
                .build()

        and:
        entityReaderRegistry.getReader(_ as UriInfo) >> Stub(EntityReader) {
            read(_ as ItemLookupRequest) >> { throw Stub(exception) }
        }

        when:
        handler.handle param

        then:
        thrown exception

        where:
        exception << [PropertyNotFoundException,
                      ItemNotFoundException,
                      OData2ServicesException,
                      IntegrationAttributeException,
                      TypeAttributeDescriptorNotFoundException,
                      ItemSearchException,
                      IntegrationKeyNestedFilteringNotSupportedException,
                      TypeAccessPermissionException]
    }

    @Test
    @Unroll
    def "fail to read with RuntimeException"() {
        given:
        def param = DefaultReadParam.readParam()
                .withUriInfo(Stub(UriInfo))
                .build()

        and:
        entityReaderRegistry.getReader(_ as UriInfo) >> Stub(EntityReader) {
            read(_ as ItemLookupRequest) >> { throw new RuntimeException('IGNORE - testing exception') }
        }

        when:
        handler.handle param

        then:
        thrown RetrievalErrorRuntimeException
    }
}