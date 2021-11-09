/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.delete

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory
import de.hybris.platform.odata2services.odata.persistence.PersistenceService
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DeleteHandlerUnitTest extends Specification {

    def itemLookupFactory = Stub ItemLookupRequestFactory
    def persistenceService = Stub PersistenceService

    def handler = new DeleteHandler(itemLookupRequestFactory: itemLookupFactory, persistenceService: persistenceService)

    @Test
    def "item is deleted"() {
        given:
        itemLookupFactory.create(_ as DeleteUriInfo, _ as ODataContext) >> Stub(ItemLookupRequest)

        when:
        def response = handler.handle deleteParam()

        then:
        with(response) {
            status == HttpStatusCodes.OK
            entity == ''
        }
    }

    @Test
    def "fails to delete item"() {
        given:
        itemLookupFactory.create(_ as DeleteUriInfo, _ as ODataContext) >> Stub(ItemLookupRequest)

        and:
        persistenceService.deleteItem(_ as ItemLookupRequest) >> { throw new EdmException(EdmException.COMMON) }

        when:
        def response = handler.handle deleteParam()

        then:
        with(response) {
            status == HttpStatusCodes.INTERNAL_SERVER_ERROR
            entity == ''
        }
    }

    def deleteParam() {
        DefaultDeleteParam.deleteParam()
            .withUriInfo(Stub(DeleteUriInfo))
            .withContext(Stub(ODataContext))
            .build()
    }
}
