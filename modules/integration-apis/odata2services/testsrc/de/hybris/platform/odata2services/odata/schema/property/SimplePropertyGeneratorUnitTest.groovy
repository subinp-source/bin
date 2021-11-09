/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema.property

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SimplePropertyGeneratorUnitTest extends Specification {
    def generator = new SimplePropertyGenerator()

    @Test
    @Unroll
    def "generates property of #systemType type"() {
        given:
        def attrDesc = Stub(TypeAttributeDescriptor) {
            getAttributeName() >> 'value'
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> systemType.name
            }
        }

        when:
        def property = generator.generate attrDesc

        then:
        property.name == attrDesc.attributeName
        property.type == edmType
        !property.annotationAttributes

        where:
        systemType       | edmType
        BigDecimal.class | EdmSimpleTypeKind.Decimal
        BigInteger.class | EdmSimpleTypeKind.String
        Boolean.class    | EdmSimpleTypeKind.Boolean
        Byte.class       | EdmSimpleTypeKind.SByte
        Character.class  | EdmSimpleTypeKind.String
        Date.class       | EdmSimpleTypeKind.DateTime
        Double.class     | EdmSimpleTypeKind.Double
        Float.class      | EdmSimpleTypeKind.Single
        Integer.class    | EdmSimpleTypeKind.Int32
        Long.class       | EdmSimpleTypeKind.Int64
        Short.class      | EdmSimpleTypeKind.Int16
        String.class     | EdmSimpleTypeKind.String
        Object.class     | EdmSimpleTypeKind.String
    }

    @Test
    def 'throws exception for unsupported types'() {
        when:
        generator.generate Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> Calendar.class.name
            }
        }

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "generated property #condition annotations when annotations generator is present and returns #annotations"() {
        given: 'an attribute descriptor'
        def attribute = Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> String.class.name
            }
        }
        and: 'an annotations generator is injected'
        generator.annotationsGenerator = Stub(SchemaElementGenerator) {
            generate(attribute) >> annotations
        }

        when:
        def property = generator.generate attribute

        then:
        property.annotationAttributes == expectedAnnotations

        where:
        condition | annotations                                            | expectedAnnotations
        'has no'  | null                                                   | null
        'has no'  | []                                                     | []
        'has'     | [new AnnotationAttribute(), new AnnotationAttribute()] | annotations
    }
}
