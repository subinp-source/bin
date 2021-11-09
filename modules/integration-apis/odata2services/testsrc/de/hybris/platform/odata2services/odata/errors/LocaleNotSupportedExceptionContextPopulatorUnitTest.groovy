/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.errors

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.inboundservices.persistence.populator.AttributePersistenceException
import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataErrorContext
import org.junit.Test
import spock.lang.Specification

@UnitTest
class LocaleNotSupportedExceptionContextPopulatorUnitTest extends Specification {
    def contextPopulator = new LocaleNotSupportedExceptionContextPopulator()

    @Test
    def 'does not populates context values if context does not contain LocaleNotSupportedException'() {
        given:
        def context = new ODataErrorContext(exception: new AttributePersistenceException('message [%s %s]', Stub(TypeAttributeDescriptor), Stub(PersistenceContext)))

        when:
        contextPopulator.populate context

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
    def "provides error context values for LocaleNotSupportedException when "() {
        given:
        def language = 'en'
        def ex = new LocaleNotSupportedException(new Locale(language))
        def context = new ODataErrorContext(exception: ex)

        when:
        contextPopulator.populate context

        then:
        with(context) {
            httpStatus == HttpStatusCodes.BAD_REQUEST
            locale == Locale.ENGLISH
            errorCode == "invalid_language"
            message == "The language provided [$language] is not available in the system."
        }
    }
}
