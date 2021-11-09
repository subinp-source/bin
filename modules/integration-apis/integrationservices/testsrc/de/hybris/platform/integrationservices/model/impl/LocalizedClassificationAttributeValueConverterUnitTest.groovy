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
class LocalizedClassificationAttributeValueConverterUnitTest extends Specification {

    def service = Stub ClassificationAttributeValueService
    def converter = new LocalizedClassificationAttributeValueConverter(attributeValueService: service)

    @Unroll
    @Test
    def "isApplicable is #applicable when attribute is #condition type"() {
        expect:
        converter.isApplicable(attribute) == applicable

        where:
        condition                    | applicable | attribute
        'localized enum'             | true       | localizedValueAttribute()
        'localized and a collection' | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true; isCollection() >> true }
        'localized but not enum'     | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> true; getClassAttributeAssignment() >> stringClassAttributeAssignment() }
        'enum but not localized'     | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> false }
        'single'                     | false      | Stub(ClassificationTypeAttributeDescriptor) { isLocalized() >> false; isCollection() >> false }
    }

    @Unroll
    @Test
    def "convert will return an empty map when #condition"() {
        expect:
        converter.convert(localizedValueAttribute(), value) == [:]

        where:
        condition                     | value
        'value is not a Map'          | 'string'
        'value is null'               | null
        'value is a collection'       | ['one', 'two']
        'entry.value is not a string' | [(Locale.ENGLISH): 1]
        'entry.key is not a locale'   | ['key': 'string']
        'value is empty map'          | [:]
        'entry.value is null'         | [(Locale.ENGLISH): null]
    }

    @Test
    def "convert localized string where value has an existing enumValue"() {
        given:
        def attrValue = Stub(ClassificationAttributeValueModel)
        and: 'enumValue is found for string class system version'
        service.find(_ as ClassificationSystemVersionModel, _ as String) >> Optional.of(attrValue)

        when:
        def convertedValue = converter.convert(localizedValueAttribute(), [(Locale.ENGLISH): 'value'])

        then:
        convertedValue == [(Locale.ENGLISH): attrValue]
    }

    @Test
    def "convert creates a new enumValue that does not yet exist for value"() {
        given:
        def attrValue = Stub ClassificationAttributeValueModel
        and: 'enumValue is not found for string class system version'
        service.find(_ as ClassificationSystemVersionModel, _ as String) >> Optional.empty()
        and: 'new enumValue is created'
        service.create(_ as ClassificationSystemVersionModel, _ as String) >> attrValue

        when:
        def convertedValue = converter.convert(localizedValueAttribute(), [(Locale.ENGLISH): 'value'])

        then:
        convertedValue == [(Locale.ENGLISH): attrValue]
    }

    @Test
    def "convert adds enumValue only if it is not already contained in the possible values for attribute"() {
        given:
        def attrValue1 = Stub ClassificationAttributeValueModel
        def attrValue2 = Stub ClassificationAttributeValueModel
        and: 'enumValue is found for string class system version'
        service.find(_, _ as String) >>> [Optional.of(attrValue1), Optional.of(attrValue2)]

        and: 'class attribute assignment contains only attrValue1'
        def classAttrAssignment = new ClassAttributeAssignmentModel(attributeValues: [attrValue1])

        when:
        converter.convert(localizedValueAttribute(classAttrAssignment),
                [(Locale.ENGLISH): 'value1', (Locale.CANADA): 'value2'])

        then:
        classAttrAssignment.attributeValues == [attrValue1, attrValue2]
    }


    private ClassificationTypeAttributeDescriptor localizedValueAttribute(ClassAttributeAssignmentModel classAttributeAssignment = enumClassAttributeAssignment()) {
        attributeDescriptor(true, false, classAttributeAssignment)
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
