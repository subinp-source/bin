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
package de.hybris.platform.odata2services.odata.schema.association

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import org.apache.olingo.odata2.api.edm.provider.Association
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AssociationListGeneratorRegistryUnitTest extends Specification {
    def descriptorFactory = Stub(DescriptorFactory) {
        createItemTypeDescriptor(_) >> Stub(TypeDescriptor)
    }
    def registry = new AssociationListGeneratorRegistry(descriptorFactory: descriptorFactory)

    @Test
    def "resulting list contains 0 associations from null generator list"() {
        given:
        registry.setAssociationListGenerators(null)

        expect:
        registry.generate(itemsDoNotMatter()).isEmpty()
    }


    @Test
    def "generate with null integration object items throws exception"() {
        given:
        registry.setAssociationListGenerators(null)

        when:
        registry.generate null as List

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    @Unroll
    def "resulting list contains #resultSize navigation properties from #generators.size generators"() {
        given:
        registry.setAssociationListGenerators(generators)

        when:
        def result = registry.generate itemsDoNotMatter()

        then:
        result.size() == resultSize

        where:
        generators                                                         | resultSize
        [generatorWithNumberOfResults(2), generatorWithNumberOfResults(1)] | 3
        [generatorWithNumberOfResults(1), generatorWithNumberOfResults(0)] | 1
        [generatorWithNumberOfResults(5)]                                  | 5
        []                                                                 | 0
    }

    @Test
    @Unroll
    def "generates empty associations list when #condition"() {
        given:
        registry.associationGenerators = generators

        expect:
        registry.generateFor(descriptors).empty

        where:
        condition                    | descriptors           | generators
        'descriptors are null'       | null                  | [generatorWithNumberOfResults(1)]
        'nested generators are []'   | someTypeDescriptors() | []
        'nested generators are null' | someTypeDescriptors() | null
    }

    @Test
    @Unroll
    def "combines generated associations when one nested generator produces #cnt1 associations and the other one produces #cnt2 associations"() {
        given:
        registry.associationGenerators = [generatorWithNumberOfResults(cnt1), generatorWithNumberOfResults(cnt2)]

        expect:
        registry.generateFor(someTypeDescriptors()).size() == resultSize

        where:
        cnt1 | cnt2 | resultSize
        2    | 1    | 3
        2    | 0    | 2
        0    | 1    | 1
        0    | 0    | 0
    }

    @Test
    def 'combined associations produced by the deprecated and new sets of nested generators are unique'() {
        given:
        registry.associationListGenerators = [Stub(SchemaElementGenerator) {
            generate(_) >> [association('name-1')]
        }]
        registry.associationGenerators = [Stub(SchemaElementGenerator) {
            generate(_) >> [association('name-1'), association('name-2')]
        }]
        and:
        def items = [Stub(IntegrationObjectItemModel)]

        when:
        def associations = registry.generate(items)

        then:
        associations.collect { it.name } == ['name-1', 'name-2']
    }

    @Test
    @Unroll
    def "nested association generators can be reset to #generators"() {
        given: 'the registry has a nested generator, which produces 2 associations'
        registry.associationGenerators = [generatorWithNumberOfResults(2)]

        when: 'the association generators are reset'
        registry.associationGenerators = generators

        then: 'result contains only association produced by the new nested generators'
        registry.generateFor(someTypeDescriptors()).empty

        where:
        generators << [null, [], [generatorWithNumberOfResults(0)]]
    }

    List<IntegrationObjectItemModel> itemsDoNotMatter() {
        []
    }

    List<TypeDescriptor> someTypeDescriptors() {
        [Stub(TypeDescriptor)]
    }

    def generatorWithNumberOfResults(def count) {
        Stub(SchemaElementGenerator) {
            generate(_) >> [Stub(Association)] * count
        }
    }

    def association(def name) {
        Stub(Association) {
            getName() >> name
        }
    }
}
