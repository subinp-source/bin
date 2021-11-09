/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence.exception

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Specification

@UnitTest
class ItemNotFoundExceptionUnitTest extends Specification {
    @Test
    def 'create exception with PersistenceContext'() {
        given:
        def type = 'Item'
        def key = 'key'
        def context = Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getIntegrationKey() >> key
                getItemType() >> Stub(TypeDescriptor) {
                    getItemCode() >> type
                }
            }
        }

        when:
        def ex = new ItemNotFoundException(context)

        then:
        with(ex) {
            httpStatus == HttpStatusCodes.NOT_FOUND
            code == 'not_found'
            entityType == type
            integrationKey == key
            message == "[$type] with integration key [$key] was not found."
        }
    }

    @Test
    def 'create exception with null PersistenceContext'() {
        when:
        def ex = new ItemNotFoundException(null as PersistenceContext)

        then:
        with(ex) {
            httpStatus == HttpStatusCodes.NOT_FOUND
            code == 'not_found'
            !entityType
            !integrationKey
            message == '[null] with integration key [null] was not found.'
        }
    }

    @Test
    def 'creates exception with type and integration key'() {
        given:
        def type = 'EntityType'
        def key = 'integration|key'

        when:
        def ex = new ItemNotFoundException(type, key)

        then:
        with(ex) {
            httpStatus == HttpStatusCodes.NOT_FOUND
            code == 'not_found'
            entityType == type
            integrationKey == key
            message == "[$type] with integration key [$key] was not found."
        }
    }

    @Test
    def 'creates exception with type, integration key and cause exception'() {
        given:
        def type = 'EntityType'
        def key = 'integration|key'
        def rootCause = new RuntimeException()

        when:
        def ex = new ItemNotFoundException(type, key, rootCause)

        then:
        with(ex) {
            httpStatus == HttpStatusCodes.NOT_FOUND
            code == 'not_found'
            entityType == type
            integrationKey == key
            cause == rootCause
            message == "[$type] with integration key [$key] was not found."
        }
    }
}
