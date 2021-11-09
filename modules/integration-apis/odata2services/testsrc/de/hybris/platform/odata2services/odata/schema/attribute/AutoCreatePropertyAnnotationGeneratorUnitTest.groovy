/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AutoCreatePropertyAnnotationGeneratorUnitTest extends Specification {

    def generator = new AutoCreatePropertyAnnotationGenerator()

    @Test
    @Unroll
    def "isApplicable = #applicable when #condition"() {
        expect:
        generator.isApplicable(descriptor) == applicable

        where:
        condition             | descriptor                                                | applicable
        'descriptor is null'  | null                                                      | false
        'autoCreate is true'  | Stub(TypeAttributeDescriptor) { isAutoCreate() >> true }  | true
        'autoCreate is false' | Stub(TypeAttributeDescriptor) { isAutoCreate() >> false } | false
    }

    @Test
    def 'generate auto create annotation'() {
        when:
        def annotation = generator.generate Stub(TypeAttributeDescriptor)

        then:
        with(annotation) {
            name == 's:IsAutoCreate'
            text == 'true'
        }
    }
}
