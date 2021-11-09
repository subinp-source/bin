/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyCalculationException
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME
import static de.hybris.platform.odata2services.odata.persistence.utils.ODataEntryBuilder.oDataEntryBuilder

@UnitTest
class PathPayloadKeyValidatorUnitTest extends Specification {

    private static final String OBJECT_CODE = 'TestProduct'
    private static final String ITEM_CODE = 'Product'
    private static final String PRODUCT_CODE = 'product1'
    private static final String CATALOGVERSION_VERSION = 'Staged'
    private static final String CATALOG_ID = 'Default'
    private static final String PATH_KEY = "$CATALOGVERSION_VERSION|$CATALOG_ID|$PRODUCT_CODE"

    @Shared
    def persistenceParam = Stub(PersistenceParam) {
        getEntitySet() >> Stub(EdmEntitySet) {
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> ITEM_CODE
            }
        }
        getContext() >> Stub(ODataContext)
    }
    def integrationKeyValueGenerator = Mock(IntegrationKeyValueGenerator)
    def serviceNameExtractor = Stub(ServiceNameExtractor) {
        extract(_) >> OBJECT_CODE
    }
    def itemTypeDescriptorService = Stub(ItemTypeDescriptorService)

    def validator = new PathPayloadKeyValidator(
            keyValueGenerator: integrationKeyValueGenerator,
            serviceNameExtractor: serviceNameExtractor,
            itemTypeDescriptorService: itemTypeDescriptorService
    )

    @Test
    def "path and payload contain all key components and are the same"() {
        given:
        def pathEntry = pathEntry()
        def payloadEntry = entryWithKeyBuilder()
                .withProperty('name', 'Test Product')
                .build()

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        notThrown PathPayloadKeyMismatchException
        0 * integrationKeyValueGenerator.generate(_, _)
    }

    @Test
    def "path and payload contain all key components but are different"() {
        given:
        def pathEntry = pathEntry()
        def payloadEntry = entryWithKeyBuilder()
                .withProperty('catalogVersion', oDataEntryBuilder()
                        .withProperty('version', CATALOGVERSION_VERSION)
                        .withProperty('catalog', oDataEntryBuilder()
                                .withProperty('id', "DIFFERENT_$CATALOG_ID")))
                .build()
        def payloadKey = "$CATALOGVERSION_VERSION|DIFFERENT_$CATALOG_ID|$PRODUCT_CODE"

        and: 'typeDescriptor is found'
        itemTypeDescriptorService.getTypeDescriptor(OBJECT_CODE, ITEM_CODE) >> Optional.of(Stub(TypeDescriptor))

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        def e = thrown PathPayloadKeyMismatchException
        e.message == "Key [$payloadKey] in the payload does not match the key [$PATH_KEY] in the path"
        1 * integrationKeyValueGenerator.generate(_, payloadEntry) >> payloadKey
        0 * integrationKeyValueGenerator.generate(_, pathEntry)
    }

    @Test
    def "payload has a matching key component of the path key"() {
        given:
        def pathEntry = pathEntry()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('name', 'Test Product')
                .withProperty('catalogVersion', oDataEntryBuilder()
                        .withProperty('version', CATALOGVERSION_VERSION))
                .build()

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        notThrown PathPayloadKeyMismatchException
        0 * integrationKeyValueGenerator.generate(_, _)
    }

    @Test
    def "payload has a mismatching key component of the path key"() {
        given:
        def pathEntry = pathEntry()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('name', 'Test Product')
                .withProperty('code', "DIFFERENT_$PRODUCT_CODE")
                .withProperty('catalogVersion', oDataEntryBuilder()
                        .withProperty('version', "DIFFERENT_$CATALOGVERSION_VERSION"))
                .build()
        def payloadKey = "DIFFERENT_$CATALOGVERSION_VERSION|DIFFERENT_$PRODUCT_CODE"

        and: 'typeDescriptor is found'
        itemTypeDescriptorService.getTypeDescriptor(OBJECT_CODE, ITEM_CODE) >> Optional.of(Stub(TypeDescriptor))

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        def e = thrown PathPayloadKeyMismatchException
        e.message == "Key [$payloadKey] in the payload does not match the key [$PATH_KEY] in the path"
        1 * integrationKeyValueGenerator.generate(_, payloadEntry) >> payloadKey
        0 * integrationKeyValueGenerator.generate(_, pathEntry)
    }

    @Test
    def "payload does not contain the key"() {
        given:
        def pathEntry = pathEntry()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('name', 'Test Product')
                .build()

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        notThrown PathPayloadKeyMismatchException
        0 * integrationKeyValueGenerator.generate(_, _)
    }

    @Test
    def "generate path integration key value when it is not in the ODataEntry"() {
        given:
        def pathEntryWithoutIntegrationKeyProperty = entryWithKeyBuilder().build()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('code', "DIFFERENT_$PRODUCT_CODE")
                .build()
        def payloadKey = "DIFFERENT_$PRODUCT_CODE"

        and: 'typeDescriptor is found'
        itemTypeDescriptorService.getTypeDescriptor(OBJECT_CODE, ITEM_CODE) >> Optional.of(Stub(TypeDescriptor))

        when:
        validator.validate persistenceParam, pathEntryWithoutIntegrationKeyProperty, payloadEntry

        then:
        def e = thrown PathPayloadKeyMismatchException
        e.message == "Key [$payloadKey] in the payload does not match the key [$PATH_KEY] in the path"
        1 * integrationKeyValueGenerator.generate(_, payloadEntry) >> payloadKey
        1 * integrationKeyValueGenerator.generate(_, pathEntryWithoutIntegrationKeyProperty) >> PATH_KEY
    }

    @Test
    @Unroll
    def "does not validate when path entry #condition"() {
        given:
        def payloadEntry = Mock(ODataEntry)

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        0 * payloadEntry._

        where:
        condition              | pathEntry
        'is null'              | null
        'has empty properties' | Stub(ODataEntry) { getProperties() >> [:] }
    }

    @Test
    @Unroll
    def "does not validate when payload entry #condition"() {
        given:
        def pathEntry = Mock(ODataEntry)

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        times * pathEntry.getProperties() >> [key: 'value']

        where:
        condition              | payloadEntry                                | times
        'is null'              | null                                        | 0
        'has empty properties' | Stub(ODataEntry) { getProperties() >> [:] } | 1
    }

    @Test
    @Unroll
    def "integration key value is empty in error message when persistence parameter is #condition"() {
        given:
        def pathEntry = entryWithKeyBuilder().build()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('code', "DIFFERENT_$PRODUCT_CODE")
                .build()

        and: 'typeDescriptor is found'
        itemTypeDescriptorService.getTypeDescriptor(OBJECT_CODE, ITEM_CODE) >> Optional.of(Stub(TypeDescriptor))

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        def e = thrown PathPayloadKeyMismatchException
        e.message == 'Key [] in the payload does not match the key [] in the path'
        0 * integrationKeyValueGenerator.generate(_, _)

        where:
        condition            | persistenceParam
        'null'               | null
        'entity set is null' | Stub(PersistenceParam) { getEntitySet() >> null }
        'context is null'    | Stub(PersistenceParam) { getContext() >> null }
    }

    @Test
    def "IntegrationKeyCalculationException is thrown when getEntityType throws EdmException"() {
        given:
        def param = Stub(PersistenceParam) {
            getEntitySet() >> Stub(EdmEntitySet) {
                getEntityType() >> { throw Stub(EdmException) }
            }
            getContext() >> Stub(ODataContext)
        }
        and: 'Key in the payload does not match the key in the path'
        def pathEntry = entryWithKeyBuilder().build()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('code', "DIFFERENT_$PRODUCT_CODE")
                .build()

        when:
        validator.validate param, pathEntry, payloadEntry

        then:
        def e = thrown IntegrationKeyCalculationException
        e.message == "An exception occurred while calculating the integrationKey"
    }

    @Test
    def "IntegrationObjectItemNotFoundException is thrown when typeDescriptor is not found"() {
        given: 'Key in the payload does not match the key in the path'
        def pathEntry = entryWithKeyBuilder().build()
        def payloadEntry = oDataEntryBuilder()
                .withProperty('code', "DIFFERENT_$PRODUCT_CODE")
                .build()

        and: 'typeDescriptor is found.....NOOOOTT'
        itemTypeDescriptorService.getTypeDescriptor(OBJECT_CODE, ITEM_CODE) >> Optional.empty()

        when:
        validator.validate persistenceParam, pathEntry, payloadEntry

        then:
        def e = thrown IntegrationObjectItemNotFoundException
        with(e) {
            integrationObjectCode == OBJECT_CODE
            integrationItemType == ITEM_CODE
            message == "Integration object $OBJECT_CODE does not contain item type $ITEM_CODE"
        }
    }

    def pathEntry() {
        entryWithKeyBuilder()
                .withProperty(INTEGRATION_KEY_PROPERTY_NAME, PATH_KEY)
                .build()
    }

    def entryWithKeyBuilder() {
        oDataEntryBuilder()
                .withProperty('code', PRODUCT_CODE)
                .withProperty('catalogVersion', oDataEntryBuilder()
                        .withProperty('version', CATALOGVERSION_VERSION)
                        .withProperty('catalog', oDataEntryBuilder()
                                .withProperty('id', CATALOG_ID)))

    }
}
