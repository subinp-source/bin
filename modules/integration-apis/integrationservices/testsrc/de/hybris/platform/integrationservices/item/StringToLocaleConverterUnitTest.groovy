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

package de.hybris.platform.integrationservices.item

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class StringToLocaleConverterUnitTest extends Specification {
    def converter = new StringToLocaleConverter()

    @Test
    @Unroll
    def "converts #input to #output"() {
        expect:
        converter.convert(input) == output

        where:
        input            | output
        null             | null
        ''               | new Locale('')
        'en'             | new Locale('en')
        'en-'            | new Locale('en')
        'en-us'          | new Locale('en', 'US')
        'es-CO'          | new Locale('es', 'CO')
        'en_CA'          | new Locale('en', 'CA')
        'es-es'          | new Locale('es', 'ES')
        'es-es_'         | new Locale('es', 'ES')
        'es_es-an'       | new Locale('es', 'ES')
        'en-US-TX'       | new Locale('en', 'US')
        'en-US-tx-posix' | new Locale('en', 'US')
    }
}
