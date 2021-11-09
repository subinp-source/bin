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
class PartOfPropertyAnnotationGeneratorUnitTest extends Specification {

    def generator = new PartOfPropertyAnnotationGenerator()

    @Test
    @Unroll
    def "isApplicable = #applicable when #condition"() {
        expect:
        generator.isApplicable(descriptor) == applicable

        where:
        condition            | descriptor                                            | applicable
        'descriptor is null' | null                                                  | false
        'partOf is true'     | Stub(TypeAttributeDescriptor) { isPartOf() >> true }  | true
        'partOf is false'    | Stub(TypeAttributeDescriptor) { isPartOf() >> false } | false
    }

    @Test
    def 'generate partOf annotation'() {
        when:
        def annotation = generator.generate Stub(TypeAttributeDescriptor)

        then:
        with(annotation) {
            name == 's:IsPartOf'
            text == 'true'
        }
    }
}