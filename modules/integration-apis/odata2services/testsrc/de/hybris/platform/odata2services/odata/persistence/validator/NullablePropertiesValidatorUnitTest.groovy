/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.validator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.exception.MissingPropertyException
import org.apache.olingo.odata2.api.edm.*
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NullablePropertiesValidatorUnitTest extends Specification {

    def validator = new NullablePropertiesValidator()

    @Test
    @Unroll
    def "no exception is thrown for #condition property"() {
        given:
        def odataEntry = Stub(ODataEntry)

        and:
        def entityType = Stub(EdmEntityType) {
            getPropertyNames() >> [propertyName]
            getProperty(propertyName) >> property
        }

        when:
        validator.beforeCreateItem entityType, odataEntry

        then:
        noExceptionThrown()

        where:
        condition         | propertyName     | property
        'nullable'        | 'nullableProp'   | property(nullableAnnotation())
        'integration key' | 'integrationKey' | Stub(EdmProperty)
        'not EdmProperty' | 'nonEdmPropType' | Stub(EdmTyped)
    }

    @Test
    @Unroll
    def "exception is thrown for non-nullable properties when property is #condition"() {
        given:
        def propertyName = 'nonNullableProp'
        def entityType = Stub(EdmEntityType) {
            getPropertyNames() >> [propertyName]
            getProperty(propertyName) >> property(nonNullableAnnotation())
        }

        when:
        validator.beforeCreateItem entityType, odataEntry

        then:
        thrown MissingPropertyException

        where:
        condition         | odataEntry
        'not in payload'  | Stub(ODataEntry)
        'null in payload' | Stub(ODataEntry) { getProperties() >> [nonNullableProp: null] }
    }

    def nullableAnnotation() {
        Stub(EdmAnnotationAttribute) {
            getName() >> 'Nullable'
            getText() >> 'true'
        }
    }

    def nonNullableAnnotation() {
        Stub(EdmAnnotationAttribute) {
            getName() >> 'Nullable'
            getText() >> 'false'
        }
    }

    def property(def annotation) {
        Stub(EdmProperty) {
            getAnnotations() >> Stub(EdmAnnotations) {
                getAnnotationAttributes() >> [annotation]
            }
        }
    }
}
