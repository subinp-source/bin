/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class UniquePropertiesKeyGeneratorUnitTest extends Specification {
    def generator = new UniquePropertiesKeyGenerator()

    @Test
    def 'throws exception when the properties list is null'() {
        when:
        generator.generate(null)

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "the generated key is empty when properties list #condition"() {
        expect:
        generator.generate(properties).empty

        where:
        condition                                           | properties
        'is empty'                                          | []
        'has properties with null annotations' | [propertyWithAnnotations(null as List)]
        'has properties with empty annotations' | [propertyWithAnnotations([])]
        'has isUnique set to false'                         | [propertyWithAnnotations([uniqueAnnotation('false')])]
    }

    @Test
    def 'the generated key contains all properties with IsUnique annotation'() {
        given:
        def properties = [propertyWithAnnotations('One', [uniqueAnnotation('true')]),
                          propertyWithAnnotations('Two', [uniqueAnnotation('true')])]

        expect:
        generator.generate(properties)
                .map({ it.keys.collect({ it.name })})
                .orElse([]) == ['One', 'Two']
    }


    AnnotationAttribute uniqueAnnotation(String value) {
        new AnnotationAttribute().setName("s:IsUnique").setText(value)
    }

    SimpleProperty propertyWithAnnotations(List<AnnotationAttribute> annotations=[]) {
        propertyWithAnnotations('SomeProperty', annotations)
    }

    SimpleProperty propertyWithAnnotations(String name, List<AnnotationAttribute> annotations) {
        new SimpleProperty()
                .setName(name)
                .setAnnotationAttributes(annotations)
    }
}
