/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectItemsContext
import de.hybris.platform.integrationservices.util.IntegrationObjectsContext
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import org.junit.Rule
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

@IntegrationTest
class IntegrationObjectServiceIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO_2 = "OutboundProduct"
    private static final String IO_1 = "InboundProduct"
    private static final String PRODUCT = "Product"
    private static final String PRODUCT_ALIAS = "ProductCode"
    private static final String CATEGORY = "Category"
    private static final String CATALOG = "Catalog"
    private static final String CTLG_VERSION = "CatalogVersion"

    private static final String ATTRIBUTE_NAME = "name"
    private static final String ATTRIBUTE_ALIAS_NAME = "aliasName"

    @Rule
    public IntegrationObjectItemsContext integrationObjectItemsContext = IntegrationObjectItemsContext.create()
    @Rule
    public IntegrationObjectsContext integrationObjectsContext = IntegrationObjectsContext.create()

    @Resource
    private IntegrationObjectService integrationObjectService

    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    @Unroll
    def "findAllDependencyTypes returns not found when #msg"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT"
        )

        expect:
        integrationObjectService.findAllDependencyTypes(itemCode, objectCode).isEmpty()

        where:
        msg                                              | objectCode              | itemCode
        'IntegrationObjectItem with code does not exist' | IO_1                    | "nonExistingItemCode"
        'IntegrationObject with code does not exist'     | "nonExistingObjectCode" | PRODUCT
    }

    @Test
    def "findAllDependencyTypes when matching Item exists with no dependencies"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT"
        )

        when:
        def result = integrationObjectService.findAllDependencyTypes(PRODUCT, IO_1)

        then:
        result.size() == 1
        with(result[0]) {
            code == PRODUCT
            with(integrationObject) {
                code == IO_1
            }
        }
    }

    @Test
    def "findAllDependencyTypes when multiple IntegrationObjectItems exist with same code for different IntegrationObjects"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_2",
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_2             ; $PRODUCT           ; $PRODUCT",
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT"
        )

        when:
        def result = integrationObjectService.findAllDependencyTypes(PRODUCT, IO_1)

        then:
        result.size() == 1
        with(result[0]) {
            code == PRODUCT
            with(integrationObject) {
                code == IO_1
            }
        }
    }

    @Test
    def "findAllDependencyTypes finds additional Item through attribute"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT",
                "                                   ; $IO_1             ; Unit               ; Unit",
                '$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute ; $integrationItem; $attributeName; $attributeDescriptor; $attributeType',
                "                                             ; $IO_1:$PRODUCT  ; unit          ; $PRODUCT:unit       ; $IO_1:Unit"
        )

        when:
        def result = integrationObjectService.findAllDependencyTypes(PRODUCT, IO_1)

        then:
        final product = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == PRODUCT }).get()
        final unit = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == "Unit" }).get()
        result.size() == 2
        result.containsAll(product, unit)
    }

    @Test
    def "findAllDependencyTypes finds additional Items through nested attributes"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT",
                "                                   ; $IO_1             ; $CATALOG           ; $CATALOG",
                "                                   ; $IO_1             ; $CTLG_VERSION      ; $CTLG_VERSION",
                "                                   ; $IO_1             ; Unit               ; Unit",
                '$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute ; $integrationItem   ; $attributeName ; $attributeDescriptor    ; $attributeType',
                "                                             ; $IO_1:$PRODUCT     ; catalogVersion ; $PRODUCT:catalogVersion ; $IO_1:$CTLG_VERSION",
                "                                             ; $IO_1:$CTLG_VERSION; catalog        ; $CTLG_VERSION:catalog   ; $IO_1:$CATALOG"
        )

        when:
        def result = integrationObjectService.findAllDependencyTypes(PRODUCT, IO_1)

        then:
        final product = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == PRODUCT }).get()
        final catalog = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == CATALOG }).get()
        final catalogVersion = IntegrationTestUtil.findAny(IntegrationObjectItemModel, {
            it.code == CTLG_VERSION
        }).get()
        result.size() == 3
        result.containsAll(product, catalog, catalogVersion)
    }

    @Test
    def "findAllDependencyTypes with circular dependency does not cause a stack overflow"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $CATEGORY          ; $CATEGORY",
                "                                   ; $IO_1             ; $CTLG_VERSION      ; $CTLG_VERSION",
                '$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$attributeType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute ; $integrationItem   ; $attributeName ; $attributeDescriptor        ; $attributeType',
                "                                             ; $IO_1:$CATEGORY    ; catalogVersion ; $CATEGORY:catalogVersion    ; $IO_1:$CTLG_VERSION",
                "                                             ; $IO_1:$CTLG_VERSION; rootCategories ; $CTLG_VERSION:rootCategories; $IO_1:$CATEGORY"
        )

        when:
        def result = integrationObjectService.findAllDependencyTypes(CATEGORY, IO_1)

        then:
        final category = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == CATEGORY }).get()
        final catalogVersion = IntegrationTestUtil.findAny(IntegrationObjectItemModel, {
            it.code == CTLG_VERSION
        }).get()
        result.size() == 2
        result.containsAll(category, catalogVersion)
    }

    @Test
    def "findAllIntegrationObjectItems when no matching IntegrationObject is defined"() {
        expect:
        integrationObjectService.findAllIntegrationObjectItems("anyIntegrationObject").isEmpty()
    }

    @Test
    def "findAllIntegrationObjectItems for IntegrationObject.code"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1                     ; $PRODUCT           ; $PRODUCT",
                "                                   ; $IO_1                     ; $CATEGORY          ; $CATEGORY"
        )

        when:
        def result = integrationObjectService.findAllIntegrationObjectItems(IO_1)

        then:
        final product = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == PRODUCT }).get()
        final category = IntegrationTestUtil.findAny(IntegrationObjectItemModel, { it.code == CATEGORY }).get()
        result.size() == 2
        result.containsAll(product, category)
    }

    @Test
    def "findAllIntegrationObjects when IntegrationObjectItem is defined in 1 of 2 existing IntegrationObjects"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                "                               ; $IO_2",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT",
                "                                   ; $IO_1             ; $CATEGORY          ; $CATEGORY",
                "                                   ; $IO_2             ; $PRODUCT           ; $PRODUCT"
        )

        when:
        final Set<IntegrationObjectModel> resultCatalog = integrationObjectService.findAllIntegrationObjects(CATEGORY)

        then:
        resultCatalog.size() == 1
        def catalogCodes = []
        resultCatalog.forEach({ i -> catalogCodes.add(i.code) })
        catalogCodes == [IO_1]
    }

    @Test
    def "findAllIntegrationObjects when IntegrationObjectItem with code is defined in more than 1 IntegrationObject"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                "                               ; $IO_2",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT",
                "                                   ; $IO_1             ; $CATEGORY          ; $CATEGORY",
                "                                   ; $IO_2             ; $PRODUCT           ; $PRODUCT"
        )

        when:
        final Set<IntegrationObjectModel> resultProduct = integrationObjectService.findAllIntegrationObjects(PRODUCT)

        then:
        resultProduct.size() == 2
        def codes = []
        resultProduct.forEach({ i -> codes.add(i.code) })
        codes.containsAll(IO_2, IO_1)
    }

    @Test
    def "findAllIntegrationObjects returns empty when no IntegrationObjects exist"() {
        expect:
        integrationObjectService.findAllIntegrationObjects(PRODUCT).isEmpty()
    }

    @Test
    def "findAllIntegrationObjects with null param causes an IllegalArgumentException"() {
        when:
        integrationObjectService.findAllIntegrationObjects(null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Type code provided cannot be empty or null."
    }

    @Test
    def "findItemTypeCode when IntegrationObjectItem code differs from its type"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT_ALIAS     ; $PRODUCT"
        )

        expect:
        PRODUCT == integrationObjectService.findItemTypeCode(IO_1, PRODUCT_ALIAS)
    }

    @Test
    @Unroll
    def "findItemTypeCode when #msg"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT"
        )

        expect:
        integrationObjectService.findItemTypeCode(objectCode, itemCode).isEmpty()

        where:
        msg                                         | objectCode              | itemCode
        "no IntegrationObject with code exists"     | "nonExistingObjectCode" | PRODUCT
        "no IntegrationObjectItem with code exists" | IO_1                    | "nonExistingItemCode"
    }

    @Test
    def "findAttributeDescriptor when matching IntegrationObjectItemAttribute exists"() {
        given:
        createBasicIntegrationObject()

        expect:
        integrationObjectService.findAttributeDescriptor(IO_1, PRODUCT_ALIAS, ATTRIBUTE_ALIAS_NAME).qualifier == ATTRIBUTE_NAME
    }

    @Test
    @Unroll
    def "findAttributeDescriptor when no IntegrationObjectItemAttribute with #property exists"() {
        given:
        createBasicIntegrationObject()

        when:
        integrationObjectService.findAttributeDescriptor(objectCode, itemCode, attrName)

        then:
        def e = thrown(AttributeDescriptorNotFoundException)
        e.message == String.format("Property [%s] is required for EntityType [%s] in IntegrationObject [%s].",
                attrName, itemCode, objectCode)

        where:
        msg                                            | objectCode     | itemCode       | attrName
        'integrationObjectItem.integrationObject.code' | "NOT_EXISTING" | PRODUCT_ALIAS  | ATTRIBUTE_ALIAS_NAME
        'integrationObjectItem.code'                   | IO_1           | "NOT_EXISTING" | ATTRIBUTE_ALIAS_NAME
        'name'                                         | IO_1           | PRODUCT_ALIAS  | "NOT_EXISTING"
    }

    @Test
    def "findItemAttributeName when matching IntegrationObjectItemAttribute exists"() {
        given:
        createBasicIntegrationObject()

        expect:
        integrationObjectService.findItemAttributeName(IO_1, PRODUCT_ALIAS, ATTRIBUTE_ALIAS_NAME) == ATTRIBUTE_NAME
    }

    @Test
    @Unroll
    def "findItemAttributeName when no IntegrationObjectItemAttribute with #property exists"() {
        given:
        createBasicIntegrationObject()

        when:
        integrationObjectService.findItemAttributeName(objectCode, itemCode, attrName)

        then:
        def e = thrown(AttributeDescriptorNotFoundException)
        e.message == String.format("Property [%s] is required for EntityType [%s] in IntegrationObject [%s].",
                attrName, itemCode, objectCode)

        where:
        msg                                            | objectCode     | itemCode       | attrName
        'integrationObjectItem.integrationObject.code' | "NOT_EXISTING" | PRODUCT_ALIAS  | ATTRIBUTE_ALIAS_NAME
        'integrationObjectItem.code'                   | IO_1           | "NOT_EXISTING" | ATTRIBUTE_ALIAS_NAME
        'name'                                         | IO_1           | PRODUCT_ALIAS  | "NOT_EXISTING"
    }

    @Test
    def "findIntegrationObject"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1"
        )

        expect:
        integrationObjectService.findIntegrationObject(IO_1).code == IO_1
    }

    @Test
    def "findIntegrationObjectNotFoundException"() {
        when:
        integrationObjectService.findIntegrationObject("invalid")

        then:
        def e = thrown(ModelNotFoundException)
        e.message.contains("invalid")
    }

    @Test
    def "findIntegrationObjectItemByTypeCode throws an exception when no matching IntegrationObjectItem exists"() {
        when:
        integrationObjectService.findIntegrationObjectItemByTypeCode("someIOCode", "invalid")

        then:
        def e = thrown(ModelNotFoundException)
        e.message == "The Integration Object Definition of 'someIOCode' was not found"
    }

    @Test
    def "findIntegrationObjectItemByTypeCode when matching IntegrationObjectItem exists"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; $PRODUCT           ; $PRODUCT"
        )

        when:
        def model = integrationObjectService.findIntegrationObjectItemByTypeCode(IO_1, PRODUCT)

        then:
        with(model) {
            code == PRODUCT
            with(integrationObject) {
                code == IO_1
            }
        }
    }

    @Test
    def "findIntegrationObjectItemByTypeCode when more than one IntegrationObjectItem with the same type and integrationObject.code exists throws an exception"() {
        given:
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; Product           ; Product",
                "                                   ; $IO_1             ; OtherProduct      ; Product"
        )

        when:
        integrationObjectService.findIntegrationObjectItemByTypeCode(IO_1, "Product")

        then:
        def e = thrown(AmbiguousIdentifierException)
        e.message.contains("The Integration Object and the ItemModel class provided have more than one match, "
                + "please adjust the Integration Object definition of '$IO_1'")
    }

    @Test
    def "findIntegrationObjectItemByParentTypeCode finds first parent that the current itemType extends"() {
        given: 'TestItem <- TestItemType2 <- TestItemType3'
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; TestItem           ; TestItem",
                "                                   ; $IO_1             ; TestItemType2      ; TestItemType2"
        )

        when:
        def foundItem = integrationObjectService.findIntegrationObjectItemByParentTypeCode(IO_1, "TestItemType3")

        then:
        with(foundItem) {
            code == 'TestItemType2'
            with(integrationObject) {
                code == IO_1
            }
        }
    }

    @Test
    def "findIntegrationObjectItemByParentTypeCode when more than one IntegrationObjectItem with the same type and integrationObject.code exists throws an exception"() {
        given: 'TestItemType2 <- TestItemType3'
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                '$io = integrationObject(code)',
                'INSERT_UPDATE IntegrationObjectItem; $io[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1             ; TestItemType2      ; TestItemType2",
                "                                   ; $IO_1             ; TestItemType2Again ; TestItemType2"
        )

        when:
        integrationObjectService.findIntegrationObjectItemByParentTypeCode(IO_1, "TestItemType3")

        then:
        def e = thrown(AmbiguousIdentifierException)
        e.message.contains("The Integration Object and the ItemModel class provided have more than one match, "
                + "please adjust the Integration Object definition of '$IO_1'")
    }

    @Test
    def "findIntegrationObjectItemByParentTypeCode when no matching IntegrationObjectItem exists"() {
        when:
        integrationObjectService.findIntegrationObjectItemByParentTypeCode(IO_1, "TestItemType3")

        then:
        def e = thrown(ModelNotFoundException)
    }

    @Test
    def "findIntegrationObjectItem when matching IntegrationObjectItem exists"() {
        given:
        createBasicIntegrationObject()

        expect:
        integrationObjectService.findIntegrationObjectItem(IO_1, PRODUCT_ALIAS).isPresent()
    }

    @Test
    @Unroll
    def "findIntegrationObjectItem when #msg"() {
        given:
        createBasicIntegrationObject()

        expect:
        integrationObjectService.findIntegrationObjectItem(objectCode, itemCode).isEmpty()

        where:
        msg                                                       | objectCode     | itemCode
        'integrationObject for the specified code does not exist' | "NOT_EXISTING" | PRODUCT_ALIAS
        'integrationObjectItem does not exist'                    | IO_1           | "NOT_EXISTING"
        'integrationObjectItem code is null'                      | IO_1           | null
    }

    private static void createBasicIntegrationObject() {
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO_1",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO_1                      ; $PRODUCT_ALIAS     ; $PRODUCT",
                '$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute ; $integrationItem                ; $attributeName        ; $attributeDescriptor    ',
                "                                             ; $IO_1:$PRODUCT_ALIAS ; $ATTRIBUTE_ALIAS_NAME ; $PRODUCT:$ATTRIBUTE_NAME"
        )
    }
}
