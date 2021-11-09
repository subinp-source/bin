/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class EntityMapPropertyProcessorUnitTest extends Specification {
    private static final def TEST_PROPERTY = "testMapTypeProperty"
    private static final def INTEGRATION_OBJECT_CODE = "TestIntegrationObject"
    private static final def TEST_TYPE = "TestType"
    private def IO_ITEM = Stub IntegrationObjectItemModel

    def valueAccessor = Stub AttributeValueAccessor

    def integrationObjectService = Stub(IntegrationObjectService) {
        findItemAttributeName(_ as String, _ as String, _ as String) >> TEST_PROPERTY
        findIntegrationObjectItemByTypeCode(INTEGRATION_OBJECT_CODE, TEST_TYPE) >> IO_ITEM
    }

    def itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
        getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, TEST_TYPE) >> Optional.of(typeDescriptor(applicableAttribute()))
    }

    def processor = new EntityMapPropertyProcessor(
            integrationObjectService: integrationObjectService,
            itemTypeDescriptorService: itemTypeDescriptorService
    )

    @Test
    def "populates OData entry attribute when item has empty map value and should be converted"() {
        given: "item is being converted has empty map value for #TEST_PROPERTY attribute"
        def obj = item()
        valueAccessor.getValue(obj) >> [:]
        and: 'request should convert the attribute'
        def request = conversionRequest(obj)
        request.isPropertyValueShouldBeConverted(TEST_PROPERTY) >> true
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, request

        then: 'the converted entry contains empty feed for the attribute'
        entry.properties.containsKey TEST_PROPERTY
        entry.properties[TEST_PROPERTY].entries.empty
    }

    @Test
    def "populates OData entry attribute when item has multiple entries in map value and should be converted"() {
        given: "item is being converted has a map with 2 entries for #TEST_PROPERTY attribute"
        def obj = item()
        def key1 = 'k1', val1 = 'v1'
        def key2 = 'k2', val2 = 'v2'
        valueAccessor.getValue(obj) >> [(key1): val1, (key2): val2]
        and: 'request should convert the attribute'
        def request = conversionRequest(obj)
        request.isPropertyValueShouldBeConverted(TEST_PROPERTY) >> true
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, request

        then: 'the converted entry contains empty feed for the attribute'
        entry.properties.containsKey TEST_PROPERTY
        entry.properties[TEST_PROPERTY].entries.size == 2
        entry.properties[TEST_PROPERTY].entries[0].data.key == key1
        entry.properties[TEST_PROPERTY].entries[0].data.value == val1
        entry.properties[TEST_PROPERTY].entries[1].data.key == key2
        entry.properties[TEST_PROPERTY].entries[1].data.value == val2
    }

    @Test
    @Unroll
    def "populates OData entry attribute when its value should be converted and contains a map of #elDesc argument and return type"() {
        given: "item model being converted contains a map with an argument and return type of #elDesc"
        def item = item()
        valueAccessor.getValue(item) >> [(key1): val1]
        and: 'request should convert the attribute'
        def request = conversionRequest(item)
        request.isPropertyValueShouldBeConverted(TEST_PROPERTY) >> true
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, request

        then:
        entry.properties[TEST_PROPERTY]?.entries.size == 1
        entry.properties[TEST_PROPERTY]?.entries[0].data.key == key1
        entry.properties[TEST_PROPERTY]?.entries[0].data.value == val1

        where:
        elDesc       | key1                        | val1
        'String'     | 'k1'                        | 'v1'
        'BigDecimal' | new BigDecimal(854.775807)  | new BigDecimal(854.775806)
        'BigInteger' | new BigInteger('-92233720') | new BigInteger('-92233720')
        'Boolean'    | true                        | false
        'Byte'       | Byte.valueOf('123')         | Byte.valueOf('122')
        'Character'  | 'ch' as Character           | 'ch' as Character
        'Date'       | new Date(1574665200000L)    | new Date(1574665100000L)
        'Double'     | 1.7976931348623157d         | 1.7976931348623156d
        'Float'      | 3.4028234f                  | 3.4028233f
        'Integer'    | Integer.MAX_VALUE           | Integer.MAX_VALUE - 1
        'Long'       | Long.valueOf(92233721)      | Long.valueOf(922337120)
        'Short'      | Short.MAX_VALUE             | Short.MAX_VALUE - 1
    }

    @Test
    @Unroll
    def "does not populate OData entry attribute when attribute is not applicable"() {
        given: "item is being converted has map value for #TEST_PROPERTY attribute"
        def obj = item()
        valueAccessor.getValue(obj) >> ['key1': 'value1']
        and: 'request should convert the attribute'
        def request = conversionRequest(obj)
        request.isPropertyValueShouldBeConverted(TEST_PROPERTY) >> true
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()
        and: 'a non-applicable typeAttributeDescriptor is found'
        processor.itemTypeDescriptorService = Stub(ItemTypeDescriptorService) {
            getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, TEST_TYPE) >> Optional.of(typeDescriptor(attribute))
        }

        when:
        processor.processEntity entry, request

        then:
        entry.properties.isEmpty()

        where:
        attribute << [attribute(['localized': true, 'map': true]), attribute(['localized': false, 'map': false])]
    }

    @Test
    def "does not populate OData entry attribute when map value is null"() {
        given: "item is being converted has a null map value for #TEST_PROPERTY attribute"
        def obj = item()
        valueAccessor.getValue(obj) >> null
        and: 'request should convert the attribute'
        def request = conversionRequest(obj)
        request.isPropertyValueShouldBeConverted(TEST_PROPERTY) >> true
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, request

        then:
        entry.properties.isEmpty()
    }

    @Test
    def 'does not populate OData entry attribute when request should not convert attribute value'() {
        given: "item model being converted contains a map"
        def item = item()
        valueAccessor.getValue(item) >> [key1: 'value1']
        and: 'the conversion request contains navigation segment for a different attribute'
        def request = conversionRequest(item)
        request.isPropertyValueShouldBeConverted(TEST_PROPERTY) >> false
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, request

        then:
        entry.properties.isEmpty()
    }

    @Test
    def 'does not populate OData entry attribute when its value is not a map in the item model'() {
        given: 'item model being converted contains a non-map value for the attribute'
        def item = item()
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, conversionRequest(item)

        then:
        entry.properties.isEmpty()
    }

    @Test
    def "does not populate OData entry when integration object item is not found for an item model value"() {
        given: 'integration object item is not found'
        processor.integrationObjectService = Stub(IntegrationObjectService) {
            findIntegrationObjectItemByTypeCode(_, _) >> { throw new ModelNotFoundException('does not matter') }
        }
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, conversionRequest(item())

        then:
        entry.properties.isEmpty()
    }

    @Test
    def "does not populate OData entry when attribute descriptor is not found in the IO for an item model value"() {
        given: 'integration object item does not have the attribute'
        processor.descriptorFactory = Stub(DescriptorFactory) {
            createItemTypeDescriptor(_) >> Stub(TypeDescriptor) {
                getAttribute(TEST_PROPERTY) >> Optional.empty()
            }
        }
        and: 'an ODataEntry to be populated'
        def entry = oDataEntry()

        when:
        processor.processEntity entry, conversionRequest(item())

        then:
        entry.properties.isEmpty()
    }

    private ItemModel item() {
        Stub(ItemModel) {
            getItemtype() >> TEST_TYPE
        }
    }

    private ItemConversionRequest conversionRequest(def value) {
        Stub(ItemConversionRequest) {
            getIntegrationObjectCode() >> INTEGRATION_OBJECT_CODE
            getAcceptLocale() >> Locale.ENGLISH
            getValue() >> value
            getAllPropertyNames() >> [TEST_PROPERTY]
        }
    }

    private static ODataEntryImpl oDataEntry(def values = [:]) {
        new ODataEntryImpl(values, new MediaMetadataImpl(), new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl())
    }

    def typeDescriptor(def attribute) {
        Stub(TypeDescriptor) {
            getAttribute(TEST_PROPERTY) >> Optional.of(attribute)
        }
    }

    def applicableAttribute() {
        Stub(TypeAttributeDescriptor) {
            isLocalized() >> false
            isMap() >> true
            isReadable() >> true
            accessor() >> valueAccessor
        }
    }

    def attribute(Map<String, Boolean> params) {
        Stub(TypeAttributeDescriptor) {
            isLocalized() >> params['localized']
            isMap() >> params['map']
            isReadable() >> true
            accessor() >> valueAccessor
        }
    }
}
