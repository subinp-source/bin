/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.scripting.engine.exception.DisabledScriptException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class WebhookFilterExceptionUnitTest extends Specification {
    public static final String SCRIPT_LOCATION = 'theScriptIsHere'
    public static final String EVENT_TYPE = 'newItemEvent'

    @Test
    @Unroll
    def "exception contains the logic location, even type and correct details when the cause is #cause"() {
        when:
        def e = new WebhookFilterException(SCRIPT_LOCATION, EVENT_TYPE, causeException)

        then:
        with(e.getMessage()) {
            contains("Failed execution")
            contains(SCRIPT_LOCATION)
            contains(EVENT_TYPE)
            contains(expectedDetails)
        }

        where:
        causeException                            | expectedDetails                                 | cause
        new DisabledScriptException('anymessage') | 'The script is disable and must be re-enabled.' | 'DisabledScriptException'
        new IllegalArgumentException()            | 'The script may be disabled now.'               | 'not DisabledScriptException'
    }
}
