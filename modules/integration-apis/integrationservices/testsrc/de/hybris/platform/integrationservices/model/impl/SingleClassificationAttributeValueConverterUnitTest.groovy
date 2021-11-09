/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SingleClassificationAttributeValueConverterUnitTest extends Specification {
    def service = Stub ClassificationAttributeValueService
    def converter = new SingleClassificationAttributeValueConverter(attributeValueService: service)

    @Unroll
    @Test
    def "isApplicable is #applicable when attribute is #condition type"() {
        expect:
        converter.isApplicable(attribute) == applicable

        where:
        condition    | applicable | attribute
        'single'     | true       | singleValueAttribute()
        'collection' | false      | collectionValueAttribute()
        'localized'  | false      | localizedValueAttribute()
        'not enum'   | false      | stringValueAttribute()
    }

    @Test
    def "convert returns null when value is null"() {
        expect:
        !converter.convert(singleValueAttribute(), null)
    }

    @Test
    def "convert throws exception when value is not null or string"() {
        when:
        converter.convert(singleValueAttribute(), 5)

        then:
        thrown InvalidAttributeValueException
    }

    @Test
    def "convert find an existing value list item for value"() {
        given:
        def attrValueOptional = Optional.of(Stub(ClassificationAttributeValueModel))
        and: 'value list item is found for string class system version'
        service.find(_ as ClassificationSystemVersionModel, _ as String) >> attrValueOptional

        expect:
        converter.convert(singleValueAttribute(), 'value') == attrValueOptional.get()
    }

    @Test
    def "convert creates new value list item that does not yet exist for value"() {
        given:
        def attrValue = Stub(ClassificationAttributeValueModel)
        and: 'value list item is not found for string class system version'
        service.find(_ as ClassificationSystemVersionModel, _ as String) >> Optional.empty()
        and: 'new item is created'
        service.create(_ as ClassificationSystemVersionModel, _ as String) >> attrValue

        expect:
        converter.convert(singleValueAttribute(), 'value') == attrValue
    }

    @Test
    def "convert does not add value list item that is already contained in the possible values for attribute"() {
        given:
        def attrValue = Stub(ClassificationAttributeValueModel)
        and: 'value list item is found for string class system version'
        service.find(_, _ as String) >> Optional.of(attrValue)

        and: 'class attribute assignment already contains value'
        def classAttrAssignment = new ClassAttributeAssignmentModel(attributeValues: [attrValue])

        when:
        converter.convert(singleValueAttribute(classAttrAssignment), 'someValue')

        then: 'no new values are added to class attribute assignment'
        classAttrAssignment.attributeValues == [attrValue]
    }

    @Test
    def "convert adds value list item that is not yet contained in possible values for attribute"() {
        given:
        def attrValue = Stub(ClassificationAttributeValueModel)
        and: 'new item is created'
        service.create(_ , _ as String) >> attrValue

        and: 'class attribute assignment contains no values'
        def classAttrAssignment = new ClassAttributeAssignmentModel(attributeValues: [])

        when:
        converter.convert(singleValueAttribute(classAttrAssignment), 'someValue')

        then: 'class attribute assignment contains new value list item'
        classAttrAssignment.attributeValues == [attrValue]
    }

    private ClassificationTypeAttributeDescriptor collectionValueAttribute() {
        attributeDescriptor(false, true)
    }

    private ClassificationTypeAttributeDescriptor localizedValueAttribute() {
        attributeDescriptor(true, false)
    }

    private ClassificationTypeAttributeDescriptor singleValueAttribute(ClassAttributeAssignmentModel classAttributeAssignment = enumClassAttributeAssignment()) {
        attributeDescriptor(false, false, classAttributeAssignment)
    }

    private ClassificationTypeAttributeDescriptor stringValueAttribute() {
        attributeDescriptor(false, false, stringClassAttributeAssignment())
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
