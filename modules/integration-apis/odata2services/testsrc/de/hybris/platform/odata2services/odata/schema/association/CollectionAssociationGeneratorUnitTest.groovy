/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.association

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.core.model.type.TypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.apache.commons.lang.StringUtils
import org.apache.olingo.odata2.api.edm.EdmMultiplicity
import org.apache.olingo.odata2.api.edm.FullQualifiedName
import org.apache.olingo.odata2.api.edm.provider.Association
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CollectionAssociationGeneratorUnitTest extends Specification {
    private static final String ATTRIBUTE_NAME = 'addresses'
    private static final String SOURCE = "MyB2BUnit"
    private static final String TARGET = "MyAddress"
    def generator = new CollectionAssociationGenerator()

    @Test
    @Unroll
    def "is applicable is #expectedResult when isCollection=#collection, isMap=#map, and isLocalized=#localized"() {
        given:
        def attributeModel = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> attributeType
                getLocalized() >> localized
            }
        }

        expect:
        generator.isApplicable(attributeModel as IntegrationObjectItemAttributeModel) == expectedResult

        where:
        collection | map   | localized | expectedResult | attributeType
        true       | false | false     | true           | Stub(CollectionTypeModel)
        true       | false | true      | true           | Stub(CollectionTypeModel)
        false      | true  | true      | false          | Stub(MapTypeModel)
        false      | true  | false     | true           | Stub(MapTypeModel)
        false      | false | false     | false          | Stub(TypeModel)
    }

    @Test
    @Unroll
    def "is applicable is #expectedResult when isCollection=#collection, isMap=#map, and isLocalized=#localized as TypeAttributeDescriptor"() {
        given:
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isCollection() >> collection
            isMap() >> map
            isLocalized() >> localized
        }

        expect:
        generator.isApplicable(typeAttributeDescriptor as TypeAttributeDescriptor) == expectedResult

        where:
        collection | map   | localized | expectedResult
        true       | false | false     | true
        true       | false | true      | true
        false      | true  | true      | false
        false      | true  | false     | true
        false      | false | false     | false
    }

    @Test
    def "is not applicable when IntegrationObjectItemAttributeModel is null"() {
        expect:
        !generator.isApplicable(null as IntegrationObjectItemAttributeModel)
    }

    @Test
    def "is not applicable when TypeAttributeDescriptor is null"() {
        expect:
        !generator.isApplicable(null as TypeAttributeDescriptor)
    }

    @Test
    def "generate for collection attribute as IntegrationObjectItemAttributeModel"() {
        given:
        def collectionAttributeModel = attributeModel(
                [
                        'attributeName'              : ATTRIBUTE_NAME,
                        'integrationObjectItem'      : SOURCE,
                        'returnIntegrationObjectItem': TARGET
                ])

        and: "attribute has a attributeDescriptor of CollectionTypeModel"
        collectionAttributeModel.getAttributeDescriptor() >> collectionAttributeDescriptorModel()

        when:
        def association = generator.generate(collectionAttributeModel as IntegrationObjectItemAttributeModel)

        then:
        equalToExpectedAssociation(association, ['attributeName': ATTRIBUTE_NAME, 'source': SOURCE, 'target': TARGET, 'toRole': TARGET])
    }

    @Test
    def "generate for collection attribute as TypeAttributeDescriptor"() {
        given:
        def collectionAttributeDescriptor = typeAttributeDescriptor(
                [
                        'attributeName'              : ATTRIBUTE_NAME,
                        'integrationObjectItem'      : SOURCE,
                        'returnIntegrationObjectItem': TARGET
                ])

        and:
        collectionAttributeDescriptor.isCollection() >> true

        when:
        def association = generator.generate(collectionAttributeDescriptor as TypeAttributeDescriptor)

        then:
        equalToExpectedAssociation(association, ['attributeName': ATTRIBUTE_NAME, 'source': SOURCE, 'target': TARGET, 'toRole': TARGET])
    }

    @Test
    def "generate for collection when source is same as target type as IntegrationObjectItemAttributeModel"() {
        given:
        def collectionAttributeModel = attributeModel(
                [
                        'attributeName'              : ATTRIBUTE_NAME,
                        'integrationObjectItem'      : SOURCE,
                        'returnIntegrationObjectItem': SOURCE
                ])

        and: "attribute has a attributeDescriptor of CollectionTypeModel"
        collectionAttributeModel.getAttributeDescriptor() >> collectionAttributeDescriptorModel()

        when:
        final Association association = generator.generate(collectionAttributeModel as IntegrationObjectItemAttributeModel)

        then:
        equalToExpectedAssociation(association, ['attributeName': ATTRIBUTE_NAME, 'source': SOURCE, 'target': SOURCE, 'toRole': StringUtils.capitalize(ATTRIBUTE_NAME)])
    }

    @Test
    def "generate for collection when source is same as target type as TypeAttributeDescriptor"() {
        given:
        def collectionAttributeDescriptor = typeAttributeDescriptor(
                [
                        'attributeName'              : ATTRIBUTE_NAME,
                        'integrationObjectItem'      : SOURCE,
                        'returnIntegrationObjectItem': SOURCE
                ])

        and:
        collectionAttributeDescriptor.isCollection() >> true

        when:
        final Association association = generator.generate(collectionAttributeDescriptor as TypeAttributeDescriptor)

        then:
        equalToExpectedAssociation(association, ['attributeName': ATTRIBUTE_NAME, 'source': SOURCE, 'target': SOURCE, 'toRole': StringUtils.capitalize(ATTRIBUTE_NAME)])
    }

    @Test
    def "generate for map as IntegrationObjectItemAttributeModel"() {
        given:
        def mapTypeAttributeModel = attributeModel(
                [
                        'attributeName'              : ATTRIBUTE_NAME,
                        'integrationObjectItem'      : SOURCE,
                        'returnIntegrationObjectItem': TARGET
                ])

        and: "attribute has a attributeDescriptor of MapTypeModel"
        mapTypeAttributeModel.getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(MapTypeModel)
        }

        when:
        final Association association = generator.generate(mapTypeAttributeModel as IntegrationObjectItemAttributeModel)

        then:
        equalToExpectedAssociation(association, ['attributeName': ATTRIBUTE_NAME, 'source': SOURCE, 'target': TARGET, 'toRole': TARGET])
    }

    @Test
    def "generate for map as TypeAttributeDescriptor"() {
        given:
        def mapTypeAttributeDescriptor = typeAttributeDescriptor(
                [
                        'attributeName'              : ATTRIBUTE_NAME,
                        'integrationObjectItem'      : SOURCE,
                        'returnIntegrationObjectItem': TARGET
                ])

        and: 'attributeDescriptor is a map'
        mapTypeAttributeDescriptor.isMap() >> true

        when:
        final Association association = generator.generate(mapTypeAttributeDescriptor as TypeAttributeDescriptor)

        then:
        equalToExpectedAssociation(association, ['attributeName': ATTRIBUTE_NAME, 'source': SOURCE, 'target': TARGET, 'toRole': TARGET])
    }

    private IntegrationObjectItemAttributeModel attributeModel(Map<String, String> params) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> params['attributeName']
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getCode() >> params['integrationObjectItem']
            }
            getReturnIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getCode() >> params['returnIntegrationObjectItem']
            }
        }
    }

    private TypeAttributeDescriptor typeAttributeDescriptor(Map<String, String> params) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> params['attributeName']
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getItemCode() >> params['integrationObjectItem']
            }
            getAttributeType() >> Stub(TypeDescriptor) {
                getItemCode() >> params['returnIntegrationObjectItem']
            }
        }
    }

    private AttributeDescriptorModel collectionAttributeDescriptorModel() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(CollectionTypeModel)
        }
    }

    private void equalToExpectedAssociation(Association association, Map<String, String> params) {
        with(association) {
            name == "FK_" + params['source'] + "_" + params['attributeName']
            with(end1) {
                type == new FullQualifiedName("HybrisCommerceOData", params['source'])
                role == params['source']
                multiplicity == EdmMultiplicity.ZERO_TO_ONE
            }
            with(end2) {
                type == new FullQualifiedName("HybrisCommerceOData", params['target'])
                role == params['toRole']
                multiplicity == EdmMultiplicity.MANY
            }
        }
    }
}
