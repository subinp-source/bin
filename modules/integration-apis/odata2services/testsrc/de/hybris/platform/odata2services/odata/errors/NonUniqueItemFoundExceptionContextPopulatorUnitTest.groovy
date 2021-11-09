/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.NonUniqueItemFoundException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class NonUniqueItemFoundExceptionContextPopulatorUnitTest extends Specification {
    def populator = new NonUniqueItemFoundExceptionContextPopulator()

    @Test
    def 'does not populate context values if context does not contain NonUniqueItemFoundException'() {
        given:
        def context = new ODataErrorContext(exception: new InvalidAttributeValueException('value', Stub(TypeAttributeDescriptor)))

        when:
        populator.populate context

        then:
        with(context) {
            !httpStatus
            !errorCode
            !message
            !locale
            !innerError
        }
    }

    @Test
    def 'populates error context values'() {
        given:
        def objCode = "someObj"
        def itemCode = "someItem"
        def intKey = "someKey"
        def searchRequest = Stub(ItemSearchRequest) {
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getIntegrationObjectCode() >> objCode
                getItemCode() >> itemCode
            }
            getRequestedItem() >> Optional.of(
                    Stub(IntegrationItem) {
                        getIntegrationKey() >> intKey
                    }
            )
        }
        def context = new ODataErrorContext(exception: new NonUniqueItemFoundException(searchRequest))

        when:
        populator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            errorCode == 'ambiguous_key'
            message == "Multiple items found when unique item of type '$itemCode' in integration object '$objCode' is searched by its key '$intKey'"
            locale == Locale.ENGLISH
        }
    }

    @Test
    def 'handles NonUniqueItemFoundException'() {
        expect:
        populator.exceptionClass == NonUniqueItemFoundException
    }
}
