/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest
import org.apache.commons.lang3.time.DateUtils
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PrimitivePropertyProcessorUnitTest extends Specification {
    private static final String INTEGRATION_OBJECT_CODE = 'IOCode'
    private static final String INTEGRATION_OBJECT_ITEM_CODE = 'IOICode'
    private static final String PROPERTY = 'property'
    private static final Locale LOCALE = Locale.ENGLISH

    def valueAccessor = Stub AttributeValueAccessor
    def itemTypeDescriptorService = Stub ItemTypeDescriptorService
    def processor = new PrimitivePropertyProcessor(itemTypeDescriptorService: itemTypeDescriptorService)

    @Test
    @Unroll
    def "process entity does not populate ODataEntry with #condition properties"() {
        given:
        def oDataEntry = oDataEntry()

        when:
        processor.processEntity oDataEntry, conversionRequest(itemModel(), propertyNames)

        then:
        oDataEntry.properties.isEmpty()

        where:
        condition     | propertyNames
        'empty'       | []
        'unsupported' | ['integrationKey', 'localizedAttributes']
    }

    @Test
    @Unroll
    def "process entity does not populate ODataEntry when property is #name"() {
        given: 'attribute is not applicable'
        itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, INTEGRATION_OBJECT_ITEM_CODE) >> Optional.of(
                Stub(TypeDescriptor) {
                    getAttribute(name) >> Optional.of(Stub(TypeAttributeDescriptor) {
                        isPrimitive() >> primitive
                        isCollection() >> collection
                    })
                }
        )
        def oDataEntry = oDataEntry()

        when:
        processor.processEntity oDataEntry, conversionRequest()

        then: 'created type attribute descriptor with the collection and primitive flags'
        oDataEntry.properties.isEmpty()

        where:
        name                     | primitive | collection
        'an entity'              | false     | false
        'an entity collection'   | false     | true
        'a primitive collection' | true      | true
    }

    @Test
    def 'process entity does not populate ODataEntry when item descriptor is not found'() {
        given: 'type descriptor is not found'
        itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, INTEGRATION_OBJECT_ITEM_CODE) >> Optional.empty()
        def oDataEntry = oDataEntry()

        when:
        processor.processEntity oDataEntry, conversionRequest()

        then:
        oDataEntry.properties.isEmpty()
    }

    @Test
    def 'process entity does not populate ODataEntry when type attribute descriptor is not found'() {
        given: 'type attribute descriptor is not found'
        itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, INTEGRATION_OBJECT_ITEM_CODE) >> Optional.of(
                Stub(TypeDescriptor) {
                    getAttribute(PROPERTY) >> Optional.empty()
                }
        )
        def oDataEntry = oDataEntry()

        when:
        processor.processEntity oDataEntry, conversionRequest()

        then:
        oDataEntry.properties.isEmpty()
    }

    @Test
    @Unroll
    def 'process entity populates ODataEntry with attribute value #propertyValue'() {
        given: 'a descriptor with applicable attribute'
        itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, INTEGRATION_OBJECT_ITEM_CODE) >>
                Optional.of(typeDescriptor(applicableAttribute()))
        and: 'a value for the attribute'
        def item = itemModel()
        valueAccessor.getValue(item) >> propertyValue

        def oDataEntry = oDataEntry()

        when:
        processor.processEntity oDataEntry, conversionRequest(item)

        then:
        oDataEntry.properties[PROPERTY] == expected

        where:
        propertyValue | expected
        12            | 12
        new Date(123) | DateUtils.toCalendar(new Date(123))
    }

    @Test
    def 'process entity populates ODataEntry with localized attribute value'() {
        given:
        def propertyValue = 'aLocalizedValue'
        def itemModel = itemModel()

        and: 'a descriptor for a non-localized primitive attribute'
        valueAccessor.getValue(itemModel, LOCALE) >> propertyValue
        itemTypeDescriptorService.getTypeDescriptorByTypeCode(INTEGRATION_OBJECT_CODE, INTEGRATION_OBJECT_ITEM_CODE) >>
                Optional.of(typeDescriptor(applicableLocalizedAttribute()))
        and:
        def oDataEntry = oDataEntry()

        when:
        processor.processEntity oDataEntry, conversionRequest(itemModel)

        then:
        oDataEntry.properties[PROPERTY] == propertyValue
    }

    def conversionRequest(def value = null, def properties = [PROPERTY]) {
        Stub(ItemConversionRequest) {
            getValue() >> (value ?: itemModel())
            getIntegrationObjectCode() >> INTEGRATION_OBJECT_CODE
            getAcceptLocale() >> LOCALE
            getAllPropertyNames() >> properties
        }
    }

    def typeDescriptor(def attribute) {
        Stub(TypeDescriptor) {
            getAttribute(PROPERTY) >> Optional.of(attribute)
        }
    }

    def applicableAttribute() {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            isCollection() >> false
            isLocalized() >> false
            isReadable() >> true
            accessor() >> valueAccessor
        }
    }

    def applicableLocalizedAttribute() {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            isCollection() >> false
            isLocalized() >> true
            isReadable() >> true
            accessor() >> valueAccessor
        }
    }

    def itemModel() {
        Stub(ItemModel) {
            getItemtype() >> INTEGRATION_OBJECT_ITEM_CODE
        }
    }

    private static ODataEntry oDataEntry() {
        new ODataEntryImpl([:], new MediaMetadataImpl(), new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl())
    }
}