/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.association

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.olingo.odata2.api.edm.provider.Association
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AssociationListSchemaElementGeneratorUnitTest extends Specification {

    def registry = Stub(AssociationGeneratorRegistry)
    def generator = new AssociationListSchemaElementGenerator(associationGeneratorRegistry: registry)

    @Test
    @Unroll
    def "no associations are generated when the type descriptor collection is #descriptors"() {
        expect:
        generator.generate(descriptors as Collection).empty

        where:
        descriptors << [null, []]
    }

    @Test
    @Unroll
    def "no associations are generated when the type descriptor' attributes are #attributes"() {
        given:
        def descriptor = Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }

        expect:
        generator.generate([descriptor]).empty

        where:
        attributes << [null, []]
    }

    @Test
    def "generate associations"() {
        given: 'attribute has an association'
        def attributeWithAssociation = Stub TypeAttributeDescriptor
        def association = Stub(Association)
        registry.getAssociationGenerator(attributeWithAssociation) >> Optional.of(Stub(AssociationGenerator) {
            generate(attributeWithAssociation) >> association
        })

        and: 'attribute does not have an association'
        def attributeWithNullAssociation = Stub(TypeAttributeDescriptor)
        registry.getAssociationGenerator(attributeWithNullAssociation) >> Optional.of(Stub(AssociationGenerator) {
            generate(attributeWithNullAssociation) >> null
        })

        and: 'attribute has no association generator'
        def attributeWithNoAssociationGenerator = Stub(TypeAttributeDescriptor)
        registry.getAssociationGenerator(attributeWithNoAssociationGenerator) >> Optional.empty()

        and: 'type descriptors and their attributes'
        def descriptorWithAssociation = Stub(TypeDescriptor) {
            getAttributes() >> [attributeWithAssociation]
        }

        def descriptorWithNoAssociation = Stub(TypeDescriptor) {
            getAttributes() >> [attributeWithNoAssociationGenerator, attributeWithNullAssociation]
        }

        when:
        def associations = generator.generate([descriptorWithAssociation, descriptorWithNoAssociation])

        then:
        associations == [association]
    }
}
