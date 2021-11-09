/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.validation.MissingRequestedItemException
import de.hybris.platform.integrationservices.search.validation.MissingRequiredKeyAttributeValueException
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource

@Issue('https://jira.hybris.com/browse/STOUT-1829')
@IntegrationTest
class ItemSearchServiceIntegrationTest extends ServicelayerSpockSpecification {

    private static final def IO = 'TestProductIO'
    private static final def PRODUCT_ITEM_CODE = 'Product'
    private static final def CATALOGVERSION_ITEM_CODE = 'CatalogVersion'
    private static final def CATALOG_ITEM_CODE = 'Catalog'
    private static final def TEST_CATALOG = 'MyTestCatalog'
    private static final def ORIGINAL_CATALOG_VERSION = 'Original'
    private static final def ANOTHER_CATALOG_VERSION = 'Another'
    private static final def PRODUCT1_CODE = 'prod1'
    private static final def PRODUCT_1_DEFAULT = 'product1 default'
    private static final def PRODUCT_1_ANOTHER = 'product1 another catalog version'

    @Resource(name = 'integrationServicesItemSearchService')
    private ItemSearchService itemSearchService

    def importIntegrationObject() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $IO                                   ; Product            ; Product   ; true",
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                "                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType = returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor           ; $attributeType;',
                "                                            ; $IO:Product         ; code                        ; Product:code",
                "                                            ; $IO:Product         ; catalogVersion              ; Product:catalogVersion; $IO:CatalogVersion;",
                "                                            ; $IO:CatalogVersion  ; version                     ; CatalogVersion:version;                   ;",
                "                                            ; $IO:CatalogVersion  ; catalog                     ; CatalogVersion:catalog; $IO:Catalog       ;",
                "                                            ; $IO:Catalog         ; id                          ; Catalog:id            ;                   ;")
    }

    def createInitialProducts() {
        IntegrationTestUtil.importImpEx(
                "\$catalogVersion1 = $TEST_CATALOG:$ORIGINAL_CATALOG_VERSION",
                "\$catalogVersion2 = $TEST_CATALOG:$ANOTHER_CATALOG_VERSION",
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
                "; $TEST_CATALOG ; $TEST_CATALOG ; true",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                "; $TEST_CATALOG ; $ORIGINAL_CATALOG_VERSION ; true",
                "; $TEST_CATALOG ; $ANOTHER_CATALOG_VERSION  ; true",
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]; name',
                "; $PRODUCT1_CODE     ; \$catalogVersion1 ; $PRODUCT_1_DEFAULT",
                "; $PRODUCT1_CODE     ; \$catalogVersion2 ; $PRODUCT_1_ANOTHER"
        )
    }

    def setupSpec() {
        importIntegrationObject()
        createInitialProducts()
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == TEST_CATALOG }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == ORIGINAL_CATALOG_VERSION || it.version == ANOTHER_CATALOG_VERSION }
    }

    @Test
    def 'unique item is returned when found'() {
        given:
        def request = itemSearchRequest(productIntegrationItem(PRODUCT1_CODE, ANOTHER_CATALOG_VERSION, TEST_CATALOG))

        when:
        def item = itemSearchService.findUniqueItem(request)

        then:
        item.isPresent()
        with(item.get()) {
            getItemtype() == PRODUCT_ITEM_CODE
            getProperty('code') == PRODUCT1_CODE
            getProperty('name') == PRODUCT_1_ANOTHER
        }
    }

    @Test
    def 'exception is thrown when searching unique item but integration item is not present in the request'() {
        given:
        def request = new ImmutableItemSearchRequest.Builder()
                .withItemType(Stub(TypeDescriptor))
                .build()

        when:
        itemSearchService.findUniqueItem(request)

        then:
        thrown(MissingRequestedItemException)
    }

    @Test
    def 'exception is thrown when searching by incomplete integration key'() {
        given: 'a request for an item by integration key'
        def request = itemSearchRequest(productIntegrationItem(PRODUCT1_CODE, null, TEST_CATALOG))

        when:
        itemSearchService.findUniqueItem(request)

        then:
        thrown(MissingRequiredKeyAttributeValueException)
    }

    @Test
    def 'multiple items returned when finding items with no integration key'() {
        given:
        def request = new ImmutableItemSearchRequest.Builder()
                .withItemType(typeDescriptor(PRODUCT_ITEM_CODE, [], IO))
                .build()

        when:
        def result = itemSearchService.findItems(request)

        then:
        with(result) {
            items.size() == 2
        }
    }

    @Test
    def 'count returns total number of items present'() {
        given:
        def request = new ImmutableItemSearchRequest.Builder()
                .withItemType(typeDescriptor(PRODUCT_ITEM_CODE, [], IO))
                .build()

        expect:
        itemSearchService.countItems(request) == 2
    }

    private productIntegrationItem(String productCode, String catalogVersion, String catalogId) {
        def catalogIntegrationItem = catalogIntegrationItem(catalogId)
        def catalogVersionIntegrationItem = catalogVersionIntegrationItem(catalogVersion, catalogIntegrationItem)
        def productIntegrationItem = productIntegrationItem(productCode, catalogVersionIntegrationItem)
        productIntegrationItem
    }

    private catalogVersionIntegrationItem(String catalogVersion, catalogIntegrationItem) {
        Stub(IntegrationItem) {
            def versionDescriptor = primitiveKeyAttributeDescriptor('version')
            def catalogDescriptor = referenceKeyAttributeDescriptor('catalog')
            def catalogVersionAttributeDescriptors = [versionDescriptor, catalogDescriptor]
            getIntegrationObjectCode() >> IO
            getItemType() >> typeDescriptor(CATALOGVERSION_ITEM_CODE, catalogVersionAttributeDescriptors, IO)
            getAttributes() >> catalogVersionAttributeDescriptors
            getAttribute('version') >> catalogVersion
            getAttribute(versionDescriptor) >> catalogVersion
            getAttribute('catalog') >> catalogIntegrationItem
            getAttribute(catalogDescriptor) >> catalogIntegrationItem
            getReferencedItem(catalogDescriptor) >> catalogIntegrationItem
        }
    }

    private catalogIntegrationItem(String catalogId) {
        Stub(IntegrationItem) {
            def catalogIdDescriptor = primitiveKeyAttributeDescriptor('id')
            def catalogAttributeDescriptors = [catalogIdDescriptor]
            getIntegrationObjectCode() >> IO
            getItemType() >> typeDescriptor(CATALOG_ITEM_CODE, catalogAttributeDescriptors, IO)
            getAttributes() >> catalogAttributeDescriptors
            getAttribute('id') >> catalogId
            getAttribute(catalogIdDescriptor) >> catalogId
        }
    }

    private productIntegrationItem(String productCode, catalogVersionIntegrationItem) {
        Stub(IntegrationItem) {
            def codeAttributeDescriptor = primitiveKeyAttributeDescriptor('code')
            def catalogVersionDescriptor = referenceKeyAttributeDescriptor('catalogVersion')
            def productAttributeDescriptors = [codeAttributeDescriptor, catalogVersionDescriptor]
            getIntegrationObjectCode() >> IO
            getItemType() >> typeDescriptor(PRODUCT_ITEM_CODE, productAttributeDescriptors, IO)
            getAttributes() >> productAttributeDescriptors
            getAttribute('code') >> productCode
            getAttribute(codeAttributeDescriptor) >> productCode
            getAttribute('catalogVersion') >> catalogVersionIntegrationItem
            getAttribute(catalogVersionDescriptor) >> catalogVersionIntegrationItem
            getReferencedItem(catalogVersionDescriptor) >> catalogVersionIntegrationItem
        }
    }

    def primitiveKeyAttributeDescriptor(String attributeName) {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
            isKeyAttribute() >> true
            getAttributeName() >> attributeName
            getQualifier() >> attributeName
        }
    }

    def referenceKeyAttributeDescriptor(String attributeName) {
        Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
            isKeyAttribute() >> true
            getAttributeName() >> attributeName
            getQualifier() >> attributeName
        }
    }

    def itemSearchRequest(IntegrationItem integrationItem) {
        Stub(ItemSearchRequest) {
            getRequestedItem() >> Optional.of(integrationItem)
            getTypeDescriptor() >> typeDescriptor(PRODUCT_ITEM_CODE, integrationItem.getAttributes(), IO)
            getAcceptLocale() >> Locale.ENGLISH
            getPaginationParameters() >> Optional.empty()
            getFilter() >> null
        }
    }

    def typeDescriptor(String itemCode, Collection attributeDescriptors, String integrationObjectCode) {
        Stub(TypeDescriptor) {
            getItemCode() >> itemCode
            getAttributes() >> attributeDescriptors
            getIntegrationObjectCode() >> integrationObjectCode
        }
    }
}
