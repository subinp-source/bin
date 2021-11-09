/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.navigation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME

@UnitTest
class LocalizedAttributeNavigationPropertyListSchemaElementGeneratorUnitTest extends Specification {

    def generator = new LocalizedAttributeNavigationPropertyListSchemaElementGenerator()

    @Test
    def 'exception is thrown when parameter is null'() {
        when:
        generator.generate null

        then:
        thrown IllegalArgumentException
    }

    @Test
    @Unroll
    def "no localized attribute navigation property is generated when the attributes are #condition"() {
        given:
        def typeDescriptor = Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }

        when:
        def navPropList = generator.generate typeDescriptor

        then:
        navPropList.isEmpty()

        where:
        condition       | attributes
        'null'          | null
        'empty list'    | []
        'not localized' | [Stub(TypeAttributeDescriptor) { isLocalized() >> false }]
    }

    @Test
    def 'localized attribute navigation property is generated when the attributes are localized'() {
        given:
        def itemCode = 'ItemCode'
        def typeDescriptor = Stub(TypeDescriptor) {
            getAttributes() >> [Stub(TypeAttributeDescriptor) { isLocalized() >> true }]
            getItemCode() >> itemCode
        }

        when:
        def navPropList = generator.generate typeDescriptor

        then:
        navPropList.size() == 1
        with(navPropList.get(0)) {
            name == LOCALIZED_ATTRIBUTE_NAME
            relationship.name == "FK_${itemCode}_${LOCALIZED_ATTRIBUTE_NAME}"
            fromRole == itemCode
            toRole == "Localized___${itemCode}"
            annotationAttributes.size() == 1
            with(annotationAttributes.get(0)) {
                name == 'Nullable'
                text == 'true'
            }
        }
    }
}
