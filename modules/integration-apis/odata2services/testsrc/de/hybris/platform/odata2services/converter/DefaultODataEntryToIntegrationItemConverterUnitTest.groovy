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

package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator
import de.hybris.platform.integrationservices.item.DefaultIntegrationItem
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.item.LocalizedValue
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.odata.persistence.ODataContextLanguageExtractor
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.ep.feed.ODataFeed
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull

@UnitTest
class DefaultODataEntryToIntegrationItemConverterUnitTest extends Specification {
    private static final def EXISTING_OBJECT_CODE = 'TestObject'
    private static final def EXISTING_ITEM_TYPE_CODE = 'TestItem'
    private static final def REQUEST_LANGUAGE = Locale.GERMAN

    def keyValueGenerator = Stub(IntegrationKeyValueGenerator)
    def serviceNameExtractor = Stub(ServiceNameExtractor) {
        extract(_ as ODataContext) >> EXISTING_OBJECT_CODE
        extract(null) >> EXISTING_OBJECT_CODE
    }
    def typeDescriptorService = Stub(ItemTypeDescriptorService) {
        getTypeDescriptor(EXISTING_OBJECT_CODE, EXISTING_ITEM_TYPE_CODE) >> Optional.of(typeDescriptor())
    }
    def languageExtractor = Stub(ODataContextLanguageExtractor) {
        extractFrom(_, 'Content-Language') >> REQUEST_LANGUAGE
    }

    def converter = new DefaultODataEntryToIntegrationItemConverter(
            keyValueGenerator: keyValueGenerator,
            itemTypeDescriptorService: typeDescriptorService,
            serviceNameExtractor: serviceNameExtractor,
            languageExtractor: languageExtractor)

    @Test
    @Unroll
    def "throws exception when method called without type descriptor and #param is null"() {
        when:
        converter.convert(ctx, set as EdmEntitySet, entry)

        then:
        thrown(IllegalArgumentException)

        where:
        param           | ctx       | set         | entry
        'OData context' | null      | entitySet() | oDataEntry([:])
        'OData entry'   | context() | entitySet() | null
        'entity set'    | context() | null        | oDataEntry([:])
    }

    @Test
    @Unroll
    def "throws exception when method called with type descriptor and #param is null"() {
        when:
        converter.convert(ctx, type as TypeDescriptor, entry)

        then:
        thrown(IllegalArgumentException)

        where:
        param             | ctx       | type                 | entry
        'OData context'   | null      | Stub(TypeDescriptor) | oDataEntry([:])
        'Type descriptor' | context() | null                 | oDataEntry([:])
        'OData entry'     | context() | Stub(TypeDescriptor) | null
    }

    @Test
    def "throws exception when service name and item type do not resolve to an existing type descriptor"() {
        given: "entry item type is not found"
        typeDescriptorService.getTypeDescriptor(EXISTING_OBJECT_CODE, 'MissingType') >> Optional.empty()

        when: "ODataEntry is converted"
        converter.convert(context(), entitySet('MissingType'), oDataEntry([:]))

        then: "the exception is thrown"
        thrown(IntegrationObjectItemNotFoundException)
    }

    @Test
    def 'converts entry to integration item when type descriptor is provided'() {
        expect: 'ODataEntry is converted to not null integration item'
        converter.convert(context(), typeDescriptor(), oDataEntry([:]))
    }

    @Test
    def 'converted integration item has integration key set'() {
        given:
        def itemType = typeDescriptor()
        and:
        def keyValue = 'some-value'
        keyValueGenerator.generate(itemType, _) >> keyValue

        when:
        def item = converter.convert context(), itemType, entitySet(), oDataEntry()

        then:
        item.integrationKey == keyValue
    }

    @Test
    def 'correctly populates ConversionParameters for the attribute value conversion'() {
        given:
        def attribute = attribute 'name'
        def attributeValue = new Object()
        def context = context()
        def itemType = typeDescriptor()
        and:
        def attributeValueConverter = Mock PayloadAttributeValueConverter
        converter.attributeValueConverter = attributeValueConverter

        when:
        converter.convert context, itemType, oDataEntry([(attribute.attributeName): attributeValue])

        then:
        1 * attributeValueConverter.convertAttributeValue(_) >> { args ->
            def params = args[0]
            assert params.attributeName == attribute.attributeName
            assert params.attributeValue == attributeValue
            assert params.context.is(context)
            assert params.contentLocale == REQUEST_LANGUAGE
            assert params.integrationItem.itemType.is(itemType)
        }
    }

    @Test
    @Unroll
    def "integration item has #attrDesc attribute value converted from the payload value by PayloadAttributeValueConverter"() {
        given: 'an item type with some attribute'
        def attribute = attribute 'someAttribute'
        def itemType = typeDescriptor attribute
        converter.itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
            getTypeDescriptor(EXISTING_OBJECT_CODE, EXISTING_ITEM_TYPE_CODE) >> Optional.of(itemType)
        }
        and: 'the payload contains a value for the attribute'
        def attrValue = 'does not matter - it will be different depending on the attribute type'
        def entry = oDataEntry([(attribute.attributeName): attrValue])
        and: 'the payload value is converted by PayloadAttributeValueConverter'
        converter.attributeValueConverter = Stub(PayloadAttributeValueConverter) {
            convertAttributeValue(_) >> convertedAttrValue
        }

        when:
        def item = converter.convert context(), entitySet(), entry

        then:
        item.getAttribute(attribute) == convertedAttrValue

        where:
        attrDesc              | convertedAttrValue
        'primitive'           | 'some primitive value'
        'collection'          | []
        'localized'           | LocalizedValue.EMPTY
        'reference'           | Stub(IntegrationItem)
    }

    @Unroll
    @Test
    def "'localizedAttributes' property value overwrites same property with same locale on the item when #property goes first in the payload"() {
        given: 'an item type with localized attribute "name"'
        def attribute = attribute 'name'
        def itemType = typeDescriptor attribute
        and: 'attribute values are converted by the attribute value converter'
        converter.attributeValueConverter = Stub(PayloadAttributeValueConverter) {
            convertAttributeValue({ it.attributeName == attribute.attributeName }) >> LocalizedValue.of(Locale.ENGLISH, 'to be discarded')
            convertAttributeValue({ it.attributeName == 'localizedAttributes' }) >> LocalizedAttributes.createWithValues(
                    [(attribute.attributeName): LocalizedValue.of(Locale.ENGLISH, 'from localizedAttributes')])
        }

        when: "ODataEntry is converted"
        def item = converter.convert context(), itemType, entry

        then: 'integration item has value from the localizedAttributes'
        item?.getAttribute(attribute) == LocalizedValue.of(Locale.ENGLISH, 'from localizedAttributes')

        where:
        property              | entry
        'the property'        | oDataEntry([name: 'discard', localizedAttributes: oDataFeed()])
        'localizedAttributes' | oDataEntry([localizedAttributes: oDataFeed(), name: 'discard'])
    }

    @Test
    def 'does not populate missing reference key attribute when container item is not provided'() {
        given: 'a reference attribute'
        def attribute = typeAttributeDescriptor('refItem', [isKey: true])
        and: 'payload does not contain value for the attribute'
        def entry = oDataEntry([:])

        when: 'ODataEntry is converted'
        def item = converter.convert(context(), entitySet(), entry)

        then: 'attribute is not populated'
        item.getAttribute(attribute.attributeName) == null
    }

    @Test
    @Unroll
    def "does not populate missing #attrKind attribute when container item is null"() {
        given: 'a reference attribute'
        def attribute = typeAttributeDescriptor('refItem', attrDesc)
        and: 'payload does not contain value for the attribute'
        def entry = oDataEntry([:])

        when: 'ODataEntry is converted'
        def item = converter.convert(context(), typeDescriptor(attribute), entry, null)

        then: 'attribute is not populated'
        item.getAttribute(attribute.attributeName) == null

        where:
        attrKind             | attrDesc
        'reference key'      | [isKey: true]
        'required reference' | [isKey: false, isNullable: false]
    }

    @Test
    @Unroll
    def "does not populate missing #attrKind attribute when there is no matching item in the container context"() {
        given: 'a reference attribute'
        def attribute = typeAttributeDescriptor('refItem', attrDesc)
        and: 'payload does not contain value for the attribute'
        def entry = oDataEntry([:])
        and: 'the container item does not have items matching the attribute type in the context'
        def container = Stub(IntegrationItem) {
            getContextItem(attribute.attributeType) >> Optional.empty()
        }

        when: 'ODataEntry is converted'
        def item = converter.convert(context(), typeDescriptor(attribute), entry, container)

        then: 'attribute is not populated'
        item.getAttribute(attribute) == null

        where:
        attrKind             | attrDesc
        'reference key'      | [isKey: true]
        'required reference' | [isKey: false, isNullable: false]
    }

    @Test
    @Unroll
    def "populates missing #attrKind attribute in #properties payload when there is a matching item in the container context"() {
        given: 'a reference attribute'
        def attribute = typeAttributeDescriptor('refItem', attrDesc)
        and: 'payload does not contain value for the attribute'
        def entry = oDataEntry(properties)
        and: 'the container item has a matching item in the context'
        def contextItem = Stub IntegrationItem
        def container = Stub(IntegrationItem) {
            getContextItem(attribute.attributeType) >> Optional.of(contextItem)
        }

        when: 'ODataEntry is converted'
        def item = converter.convert(context(), typeDescriptor(attribute), entry, container)

        then: 'attribute is not populated'
        item.getAttribute(attribute) == contextItem

        where:
        attrKind             | attrDesc                          | properties
        'reference key'      | [isKey: true]                     | [:]
        'required reference' | [isKey: false, isNullable: false] | [:]
        'reference key'      | [isKey: true]                     | [refItem: null]
        'required reference' | [isKey: false, isNullable: false] | [refItem: null]
    }

    @Test
    @Unroll
    def "does not populate missing required non-key attribute when the attribute #property=true"() {
        given: 'a reference attribute that does not meet criteria for populating value from the context'
        def attribute = typeAttributeDescriptor('refItem', [(property): true])
        and: 'payload does not contain value for the attribute'
        def entry = oDataEntry([:])
        and: 'the container item has a matching item in the context'
        def container = Stub(IntegrationItem) {
            getContextItem(attribute.attributeType) >> Optional.of(Stub(IntegrationItem))
        }

        when: 'ODataEntry is converted'
        def item = converter.convert(context(), typeDescriptor(attribute), entry, container)

        then: 'attribute is not populated'
        item.getAttribute(attribute) == null

        where:
        property << ['isCollection', 'isMap', 'isPrimitive']
    }

    @Test
    def 'missing key reference attributes are populated before non-key attributes'() {
        given: 'a key reference attribute'
        def keyAttribute = typeAttributeDescriptor('keyAttr', [isKey: true])
        and: 'a required reference attribute'
        def requiredAttribute = typeAttributeDescriptor('requiredAttr', [isNullable: false])
        and: 'an optional attribute'
        def optionalAttribute = typeAttributeDescriptor('optionalAttr', [isNullable: true])
        and: 'a type with all the attributes'
        def itemType = typeDescriptor([optionalAttribute, requiredAttribute, keyAttribute])
        and: 'payload without values for the key and required attributes'
        def entry = oDataEntry([(optionalAttribute.attributeName): 'some value'])
        and: 'the integration item is mocked to verify call order'
        def contextValue = Stub IntegrationItem
        def integrationItem = Mock(DefaultIntegrationItem) {
            getItemType() >> itemType
            getContextItem(keyAttribute.attributeType) >> Optional.of(contextValue)
            getContextItem(requiredAttribute.attributeType) >> Optional.of(contextValue)
        }
        def converterSpy = Spy converter
        converterSpy.createIntegrationItem(_ as TypeDescriptor, null) >> integrationItem

        when: 'ODataEntry is converted'
        def item = converterSpy.convert(context(), itemType, entry)

        then: 'key attribute is set first'
        1 * integrationItem.setAttribute(keyAttribute.attributeName, contextValue)
        then: 'non-key attributes set'
        1 * integrationItem.setAttribute(optionalAttribute.attributeName, 'some value')
        then: 'missing required non-key attributes set'
        1 * integrationItem.setAttribute(requiredAttribute.attributeName, contextValue)
        and:
        item.is integrationItem
    }

    private TypeAttributeDescriptor typeAttributeDescriptor(String attrName, Map<String, Boolean> properties) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> attrName
            getAttributeType() >> Stub(TypeDescriptor)
            isNullable() >> falseIfNull(properties['isNullable'])
            isKeyAttribute() >> falseIfNull(properties['isKey'])
            isPrimitive() >> falseIfNull(properties['isPrimitive'])
            isCollection() >> falseIfNull(properties['isCollection'])
            isMap() >> falseIfNull(properties['isMap'])
        }
    }

    def typeDescriptor(TypeAttributeDescriptor attr) {
        typeDescriptor([attr])
    }

    def typeDescriptor(List<TypeAttributeDescriptor> attributes = []) {
        def type = Stub(TypeDescriptor) {
            getAttributes() >> attributes
        }
        attributes.each { type.getAttribute(it.attributeName) >> Optional.of(it) }
        type
    }

    def context() {
        Stub(ODataContext)
    }

    def entitySet() {
        entitySet(EXISTING_ITEM_TYPE_CODE)
    }

    def entitySet(String type) {
        def edmType = entityType(type)
        entitySet(edmType)
    }

    def entitySet(EdmEntityType type) {
        Stub(EdmEntitySet) {
            getEntityType() >> type
        }
    }

    def entityType(String name) {
        Stub(EdmEntityType) {
            getName() >> name
        }
    }

    def attribute(String name) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
        }
    }

    def oDataEntry(Map<String, Object> properties=[:]) {
        Stub(ODataEntry) {
            getProperties() >> properties
            getProperty(_ as String) >> { properties.get(it) }
        }
    }

    def oDataFeed(final Collection<ODataEntry> entries = []) {
        Stub(ODataFeed) {
            getEntries() >> entries
        }
    }
}
