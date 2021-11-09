/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.association

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils
import org.apache.olingo.odata2.api.edm.EdmMultiplicity
import org.apache.olingo.odata2.api.edm.FullQualifiedName
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME

@UnitTest
class LocalizedAttributeAssociationListSchemaElementGeneratorUnitTest extends Specification {

    def generator = new LocalizedAttributeAssociationListSchemaElementGenerator()

    @Test
    @Unroll
    def "no localized association is generated when input is #descriptors"() {
        expect:
        generator.generate(descriptors as Collection).empty

        where:
        descriptors << [null, []]
    }

    @Test
    @Unroll
    def "no localized attribute association is generated when the attributes are #condition"() {
        given:
        def typeDescriptor = Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }

        when:
        def associations = generator.generate([typeDescriptor])

        then:
        associations.isEmpty()

        where:
        condition       | attributes
        'null'          | null
        'empty list'    | []
        'not localized' | [Stub(TypeAttributeDescriptor) { isLocalized() >> false }]
    }

    @Test
    def 'localized attribute association is generated when the attributes are localized'() {
        given:
        def itemCode = 'ItemCode'
        def typeDescriptor = Stub(TypeDescriptor) {
            getAttributes() >> [Stub(TypeAttributeDescriptor) { isLocalized() >> true }]
            getItemCode() >> itemCode
        }

        when:
        def associations = generator.generate([typeDescriptor])

        then:
        associations.size() == 1
        with(associations.get(0)) {
            name == "FK_${itemCode}_${LOCALIZED_ATTRIBUTE_NAME}"
            with(end1) {
                type == new FullQualifiedName(SchemaUtils.NAMESPACE, itemCode)
                role == itemCode
                multiplicity == EdmMultiplicity.ONE
            }
            with(end2) {
                type == new FullQualifiedName(SchemaUtils.NAMESPACE, "Localized___${itemCode}")
                role == "Localized___${itemCode}"
                multiplicity == EdmMultiplicity.MANY
            }
        }
    }
}
