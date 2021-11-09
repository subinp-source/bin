/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.jalo.JaloInvalidParameterException
import de.hybris.platform.servicelayer.exceptions.ModelSavingException
import de.hybris.platform.servicelayer.exceptions.SystemException
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.edm.EdmException
import org.junit.Test
import spock.lang.Specification

@UnitTest
class PersistenceExceptionUnitTest extends Specification {
    private static final def INTERCEPTOR_MESSAGE = "interceptor message"
    private static final def SYSTEM_MESSAGE = "system message"
    private static final def INVALID_PARAMETER_MESSAGE = "invalid parameter message"
    private static final def ENTITY_TYPE_NAME = "EntityTypeName"

    @Test
    def "exception has BAD_REQUEST status with cause exception message when cause is InterceptorException"() throws EdmException {
        given:
        def e = new ModelSavingException("testMessage", new InterceptorException(INTERCEPTOR_MESSAGE))

        when:
        def persistenceException = new PersistenceException(e, request())

        then:
        with(persistenceException) {
            message.contains(INTERCEPTOR_MESSAGE)
            message.contains(ENTITY_TYPE_NAME)
            httpStatus == HttpStatusCodes.BAD_REQUEST
            code == 'invalid_attribute_value'
        }
    }

    @Test
    def "exception has BAD_REQUEST status with cause exception message when cause is JaloInvalidParameterException"() throws EdmException {
        given:
        def e = new ModelSavingException("testMessage", new JaloInvalidParameterException(INVALID_PARAMETER_MESSAGE, 0))

        when:
        def persistenceException = new PersistenceException(e, request())

        then:
        with(persistenceException) {
            message.contains(INVALID_PARAMETER_MESSAGE)
            message.contains(ENTITY_TYPE_NAME)
            httpStatus == HttpStatusCodes.BAD_REQUEST
            code == 'invalid_attribute_value'
        }
    }

    @Test
    def "exception has INTERNAL_ERROR status with cause exception message when the cause is of type SystemException"() throws EdmException {
        given:
        def e = new SystemException(SYSTEM_MESSAGE)

        when:
        def persistenceException = new PersistenceException(e, request())

        then:
        with(persistenceException) {
            message.contains(SYSTEM_MESSAGE)
            message.contains(ENTITY_TYPE_NAME)
            httpStatus == HttpStatusCodes.INTERNAL_SERVER_ERROR
            code == 'internal_error'
        }
    }

    @Test
    def "no additional message is included when the exception is not of type SystemException & cause is not of the type InterceptorException"() throws EdmException {
        given:
        def eMessage = "testMessage"
        def eCauseMessage = "test e cause message"
        def e = new RuntimeException(eMessage, new IllegalArgumentException(eCauseMessage))

        when:
        def persistenceException = new PersistenceException(e, request())

        then:
        with(persistenceException) {
            !message.contains(eMessage)
            !message.contains(eCauseMessage)
            message.contains(ENTITY_TYPE_NAME)
        }
    }

    PersistenceContext request() {
        Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getItemType() >> Stub(TypeDescriptor) {
                    getItemCode() >> ENTITY_TYPE_NAME
                }
            }
        }
    }
}
