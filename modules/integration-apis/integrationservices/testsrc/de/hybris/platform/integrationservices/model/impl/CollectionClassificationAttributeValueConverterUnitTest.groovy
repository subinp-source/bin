package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CollectionClassificationAttributeValueConverterUnitTest extends Specification {

    def service = Stub ClassificationAttributeValueService
    def converter = new CollectionClassificationAttributeValueConverter(attributeValueService: service)

    @Unroll
    @Test
    def "isApplicable is #applicable when attribute is #condition type"() {
        expect:
        converter.isApplicable(attribute) == applicable

        where:
        condition                  | applicable | attribute
        'collection'               | true       | collectionValueAttribute()
        'localized'                | false      | localizedValueAttribute()
        'single'                   | false      | singleValueAttribute()
        'not enum'                 | false      | stringValueAttribute()
        'localized and collection' | false      | localizedCollectionValueAttribute()
    }

    @Unroll
    @Test
    def "convert will return an empty list if value is #condition"() {
        expect:
        converter.convert(collectionValueAttribute(), value) == []

        where:
        condition                     | value
        'not a string'                | 5
        'a collection of non-strings' | [1, 2, 3]
        'null'                        | null
    }

    @Unroll
    @Test
    def "convert when value has an existing value list item where value is #condition"() {
        given:
        def attrValueOptional = Optional.of(Stub(ClassificationAttributeValueModel))
        and: 'value list item is found for string class system version'
        service.find(_ as ClassificationSystemVersionModel, _ as String) >> attrValueOptional

        expect:
        converter.convert(collectionValueAttribute(), value) == [attrValueOptional.get()]

        where:
        condition               | value
        'String'                | 'string'
        'collection of Strings' | ['string']
    }

    @Unroll
    @Test
    def "convert creates new value list item that does not yet exist for value where value is #condition"() {
        given:
        def attrValue = Stub(ClassificationAttributeValueModel)
        and: 'value list item is not found for string class system version'
        service.find(_ as ClassificationSystemVersionModel, _ as String) >> Optional.empty()
        and: 'new item is created'
        service.create(_ as ClassificationSystemVersionModel, _ as String) >> attrValue

        expect:
        converter.convert(collectionValueAttribute(), value) == [attrValue]

        where:
        condition               | value
        'String'                | 'string'
        'collection of Strings' | ['string']
    }

    @Test
    def "convert only adds value list item if it is not already contained in the possible values for attribute"() {
        given:
        def attrValue1 = Stub(ClassificationAttributeValueModel)
        def attrValue2 = Stub(ClassificationAttributeValueModel)
        and: 'value list item is found for string class system version'
        service.find(_, _ as String) >>> [Optional.of(attrValue1), Optional.of(attrValue2)]

        and: 'class attribute assignment contains only attrValue1'
        def classAttrAssignment = new ClassAttributeAssignmentModel(attributeValues: [attrValue1])

        when:
        converter.convert(collectionValueAttribute(classAttrAssignment), ['value1', 'value2'])

        then:
        classAttrAssignment.attributeValues == [attrValue1, attrValue2]
    }


    private ClassificationTypeAttributeDescriptor collectionValueAttribute(ClassAttributeAssignmentModel classAttributeAssignment = enumClassAttributeAssignment()) {
        attributeDescriptor(false, true, classAttributeAssignment)
    }

    private ClassificationTypeAttributeDescriptor localizedValueAttribute() {
        attributeDescriptor(true, false)
    }

    private ClassificationTypeAttributeDescriptor singleValueAttribute() {
        attributeDescriptor(false, false)
    }

    private ClassificationTypeAttributeDescriptor stringValueAttribute() {
        attributeDescriptor(false, true, stringClassAttributeAssignment())
    }

    private ClassificationTypeAttributeDescriptor localizedCollectionValueAttribute() {
        attributeDescriptor(true, true)
    }


    private ClassificationTypeAttributeDescriptor attributeDescriptor(boolean localized, boolean collection,
                                                                      ClassAttributeAssignmentModel classAttributeAssignment = enumClassAttributeAssignment()) {
        Stub(ClassificationTypeAttributeDescriptor) {
            isLocalized() >> localized
            isCollection() >> collection
            getClassAttributeAssignment() >> classAttributeAssignment
        }
    }


    private ClassAttributeAssignmentModel enumClassAttributeAssignment() {
        Stub(ClassAttributeAssignmentModel) {
            getAttributeType() >> ClassificationAttributeTypeEnum.ENUM
        }
    }

    private ClassAttributeAssignmentModel stringClassAttributeAssignment() {
        Stub(ClassAttributeAssignmentModel) {
            getAttributeType() >> ClassificationAttributeTypeEnum.STRING
        }
    }
}
