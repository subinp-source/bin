/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.HybrisEnumValue
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.enumeration.EnumerationValueModel
import de.hybris.platform.integrationservices.model.AttributeValueGetter
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class EntityPropertyProcessorUnitTest extends Specification {
    private static final String INTEGRATION_OBJECT_CODE = 'IntegrationObjectType'
    private static final String TEST_ATTRIBUTE = "refAttribute"
    private static final String ITEM_TYPE = 'SomeType'
    private static final String ENTITY_NAME = 'SomeEntityType'

    def valueGetter = Stub AttributeValueGetter
    def entityService = Stub ModelEntityService
    def itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
        getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, ITEM_TYPE) >> Optional.of(typeDescriptor(applicableDescriptor()))
    }
    def propertyProcessor = new EntityPropertyProcessor(
            itemTypeDescriptorService: itemTypeDescriptorService,
            modelEntityService: entityService)

    @Test
    @Unroll
    def "converted entity is empty when item property #condition"() {
        given: 'attribute descriptor is no applicable'
        propertyProcessor.itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
            getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, ITEM_TYPE) >> Optional.of(typeDescriptor(descriptor))
        }
        def rootEntry = oDataEntry()

        when:
        propertyProcessor.processEntity rootEntry, conversionRequest(item())

        then:
        rootEntry.properties.isEmpty()

        where:
        descriptor                                                    | condition
        descriptor(collection: true, readable: true)                  | 'is collection'
        descriptor(primitive: true, readable: true)                   | 'is primitive'
        descriptor(collection: true, primitive: true, readable: true) | 'is primitive collection'
        descriptor(readable: false)                                   | 'is not readable'
        descriptor(map: true, readable: true)                         | 'is a map'
        null                                                          | 'descriptor is null'
    }

    @Test
    @Unroll
    def "If typeAttributeDescriptor is #description1 for ItemTypeCode then typeAttributeDescriptor for EntityName is #description2"() {
        given:
        propertyProcessor.itemTypeDescriptorService = Mock(ItemTypeDescriptorService)
        propertyProcessor.itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, ITEM_TYPE) >> Optional.of(typeDescriptor(descriptorForItemType))
        def rootEntry = oDataEntry()
        def conversionRequest = conversionRequest(item())

        when:
        propertyProcessor.processEntity rootEntry, conversionRequest

        then:
        isCalled * propertyProcessor.itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, ENTITY_NAME) >> Optional.of(typeDescriptor())

        where:
        descriptorForItemType                        | isCalled    | description1 | description2
        null                                         | 1           | 'null'       | 'searched'
        descriptor(readable: true)                   | 0           | 'not null'   | 'not searched'
    }

    @Test
    def "converted entity is empty when entityType has no properties"() {
        given:
        def rootEntry = oDataEntry()

        and: 'entity type has no properties'
        def conversionRequest = conversionRequest(item(), [])

        when:
        propertyProcessor.processEntity(rootEntry, conversionRequest)

        then:
        rootEntry.properties.isEmpty()
    }

    @Test
    def 'converted entity is empty when item should not be converted'() {
        given: 'request item should not be converted to an entity'
        def request = Stub(ItemConversionRequest) {
            getIntegrationObjectCode() >> INTEGRATION_OBJECT_CODE
            getValue() >> item()
            getAllPropertyNames() >> [TEST_ATTRIBUTE]
            isPropertyValueShouldBeConverted(TEST_ATTRIBUTE) >> false
        }
        and: 'an OData entry to be populated'
        def entry = oDataEntry()

        when:
        propertyProcessor.processEntity entry, request

        then:
        entry.properties.isEmpty()
    }

    @Test
    @Unroll
    def "item converted with supported property #condition that should be converted"() {
        given:
        def oDataEntry = oDataEntry()
        and: 'the conversion request that should convert the item'
        def request = conversionRequest(obj)
        and: 'the item has nested value'
        def nestedEntry = Stub(ODataEntry)
        entityService.getODataEntry(request) >> nestedEntry

        when:
        propertyProcessor.processEntity(oDataEntry, request)

        then:
        oDataEntry.properties[TEST_ATTRIBUTE] == nestedEntry

        where:
        condition           | obj
        'item model'        | item()
        'enum'              | Stub(EnumerationValueModel) { getItemtype() >> ITEM_TYPE }
        'hybris enum value' | Stub(HybrisEnumValue) { getType() >> ITEM_TYPE }
    }

    def typeDescriptor(def attribute) {
        Stub(TypeDescriptor) {
            getAttribute(TEST_ATTRIBUTE) >> Optional.ofNullable(attribute)
        }
    }

    private TypeAttributeDescriptor applicableDescriptor() {
        descriptor(collection: false, primitive: false, readable: true)
    }

    private TypeAttributeDescriptor descriptor(Map<String, Boolean> params) {
        Stub(TypeAttributeDescriptor) {
            isCollection() >> (params['collection'] ?: false)
            isPrimitive() >> (params['primitive'] ?: false)
            isReadable() >> (params['readable'] ?: false)
            isMap() >> (params['map'] ?: false)
            getter() >> valueGetter
        }
    }

    private static ODataEntry oDataEntry() {
        new ODataEntryImpl([:], new MediaMetadataImpl(), new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl())
    }

    private ItemConversionRequest conversionRequest(def value, List properties = [TEST_ATTRIBUTE]) {
            Stub(ItemConversionRequest) {
                getIntegrationObjectCode() >> INTEGRATION_OBJECT_CODE
                getValue() >> value
                isPropertyValueShouldBeConverted(TEST_ATTRIBUTE) >> true
                getAllPropertyNames() >> properties
                getEntityType() >> Stub(EdmEntityType){
                    getName() >> ENTITY_NAME
                }
            }
        }

    private ItemModel item() {
        Stub(ItemModel) {
            getItemtype() >> ITEM_TYPE
        }
    }
}
