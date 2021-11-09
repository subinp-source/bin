/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.integrationservices.config.ItemSearchConfiguration
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.IntegrationItemToMapConverter
import de.hybris.platform.integrationservices.search.ItemNotFoundForKeyReferencedItemPropertyException
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.integrationservices.search.ItemTypeMatch
import de.hybris.platform.integrationservices.search.PaginationParameters
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultFlexSearchQueryBuilderFactoryUnitTest extends Specification {

    private static final String CONTEXT_IO_CODE = 'MyProductIO'
    private static final String CONTEXT_ITEM_CODE = 'Product'

    def integrationObjectService = Stub(IntegrationObjectService)
    def integrationObjectMapConverter = Stub(IntegrationItemToMapConverter)
    def itemSearchService = Stub(ItemSearchService)
    def itemSearchConfiguration = Stub(ItemSearchConfiguration)

    def factory = new DefaultFlexSearchQueryBuilderFactory(
            integrationObjectService: integrationObjectService,
            integrationItemToMapConverter: integrationObjectMapConverter,
            itemSearchService: itemSearchService,
            itemSearchConfiguration: itemSearchConfiguration
    )

    @Test
    def 'factory getItemTypeMatch defaults to ALL_SUB_AND_SUPER_TYPES when itemSearchConfiguration is null'() {
        given: "a DefaultFlexSearchQueryBuilderFactory without a itemSearchConfiguration"
        def queryBuilderFactory = new DefaultFlexSearchQueryBuilderFactory(
                integrationObjectService: integrationObjectService,
                integrationItemToMapConverter: integrationObjectMapConverter,
                itemSearchService: itemSearchService
        )

        and: 'This is a basic setup to be able to call factory.createQueryBuilder'
        def itemSearchRequest = itemSearchRequest([Stub(TypeAttributeDescriptor)])
        integrationObjectMapConverter.convert(itemSearchRequest.getRequestedItem().get()) >> [does_not_matter: 'does not matter']
        def productModel = item()
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]

        when:
        def builder = queryBuilderFactory.createQueryBuilder itemSearchRequest

        then:
        builder.typeHierarchyRestriction == ItemTypeMatch.DEFAULT
    }

    @Unroll
    @Test
    def 'factory passes ItemTypeMatch #itemTypeMatch to flexible search query builder'() {
        given: 'This is a basic setup to be able to call factory.createQueryBuilder'
        def itemSearchRequest = itemSearchRequest([Stub(TypeAttributeDescriptor)])
        integrationObjectMapConverter.convert(itemSearchRequest.getRequestedItem().get()) >> [does_not_matter: 'does not matter']
        def productModel = item()
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]
        and: "itemSearchConfiguration.getItemTypeMatch() returns #itemTypeMatch"
        itemSearchConfiguration.getItemTypeMatch() >> itemTypeMatch

        when:
        def builder = factory.createQueryBuilder itemSearchRequest

        then:
        builder.typeHierarchyRestriction == itemTypeMatch

        where:
        itemTypeMatch << [ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES, ItemTypeMatch.ALL_SUBTYPES, ItemTypeMatch.RESTRICT_TO_ITEM_TYPE]
    }

    @Test
    def 'query contains only primitive key attributes when key and non key primitive attributes are present in the request item'() {
        given: 'search request for type with only primitive attributes'
        def codeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def nameAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def itemSearchRequest = itemSearchRequest([codeAttributeDescriptor, nameAttributeDescriptor])
        and: 'key map converter creates a flat map of the keys'
        integrationObjectMapConverter.convert(itemSearchRequest.getRequestedItem().get()) >> [code: 'product1']
        and: 'Integration Object service finds the Integration Object Item model'
        def productModel = item([uniqueAttribute('code'), nonUniqueAttribute('name')])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]

        when:
        def builder = factory.createQueryBuilder itemSearchRequest

        then:
        with(builder) {
            integrationItemModel.is productModel
            keyCondition == [code: 'product1']
            parameters.isEmpty()
            !orderedByPK
        }
    }

    @Test
    def 'query contains primitive and reference key attributes when both are present in the request item'() {
        given: 'search request for type with primitive and reference attributes'
        def codeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def catalogVersionAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
            isKeyAttribute() >> true
            getAttributeName() >> 'catalog_version'
            getQualifier() >> 'catalogVersion'
        }
        def itemSearchRequest = itemSearchRequest([codeAttributeDescriptor, catalogVersionAttributeDescriptor])
        and: 'key map converter creates a flat map of the keys'
        integrationObjectMapConverter.convert(itemSearchRequest.requestedItem.get()) >> [code: 'product1', catalogVersion: 'staging']
        and: 'Integration Object service finds the Integration Object Item model'
        def productModel = item([uniqueAttribute('code')])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]
        and: 'Lookup to resolve the navigation property parameter values returns a model'
        def navigationKeyAttributePk = PK.fromLong(442019)
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.of(Stub(ItemModel) {
            getPk() >> navigationKeyAttributePk
        })

        when:
        def builder = factory.createQueryBuilder(itemSearchRequest)

        then:
        with(builder) {
            integrationItemModel.is productModel
            keyCondition == [code: 'product1', catalogVersion: 'staging']
            parameters == [catalogVersion: navigationKeyAttributePk]
            !orderedByPK
        }
    }

    @Test
    def 'query contains only key attributes when primitive key and reference non key attributes present in the request item'() {
        given: 'search request for type with primitive key attribute and non key reference attribute'
        def codeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def unitAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
            isKeyAttribute() >> false
            getAttributeName() >> 'unit'
        }
        def itemSearchRequest = itemSearchRequest([codeAttributeDescriptor, unitAttributeDescriptor])
        and: 'key map converter creates a flat map of the keys'
        integrationObjectMapConverter.convert(itemSearchRequest.getRequestedItem().get()) >> ['code': 'product1']
        and: 'Integration Object service finds the Integration Object Item model'
        def productModel = item([uniqueAttribute('code')])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]

        when:
        def builder = factory.createQueryBuilder(itemSearchRequest)

        then:
        with(builder) {
            integrationItemModel.is productModel
            keyCondition == [code: 'product1']
            parameters.isEmpty()
            !orderedByPK
            !start
            !count
        }
    }

    @Test
    def 'exception is thrown when referenced key item is provided but cannot be resolved'() {
        given: 'search request for type with primitive and reference attributes'
        def codeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def catalogVersionAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
            isKeyAttribute() >> true
            getAttributeName() >> 'catalog_version'
            getQualifier() >> 'catalogVersion'
        }
        def itemSearchRequest = itemSearchRequest([codeAttributeDescriptor, catalogVersionAttributeDescriptor])
        and: 'key map converter creates a flat map of the keys'
        integrationObjectMapConverter.convert(itemSearchRequest.requestedItem.get()) >> ['code': 'does not matter']
        and: 'Integration Object service find the Integration Object Item model'
        def productModel = item([uniqueAttribute('code')])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]
        and: 'Lookup to resolve the navigation property does not return a model'
        itemSearchService.findUniqueItem(_ as ItemSearchRequest) >> Optional.empty()

        when:
        factory.createQueryBuilder(itemSearchRequest)

        then:
        def e = thrown(ItemNotFoundForKeyReferencedItemPropertyException)
        e.message.contains(CONTEXT_ITEM_CODE)
        e.message.contains('catalog_version')
    }

    @Test
    def 'query contains null reference key attributes when not present in the request item'() {
        given: 'search request for type with primitive and reference attributes'
        def productKeyAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> false
            isKeyAttribute() >> true
            getAttributeName() >> 'productName'
            getQualifier() >> 'product'
        }
        def itemSearchRequest = itemSearchRequest([productKeyAttributeDescriptor])
        itemSearchRequest.requestedItem.get().getReferencedItem(productKeyAttributeDescriptor) >> null
        and: 'key map converter creates a flat map of the keys'
        integrationObjectMapConverter.convert(itemSearchRequest.requestedItem.get()) >> [productName: null]
        and: 'Integration Object service finds the Integration Object Item model'
        def priceRowModel = item([uniqueAttribute('productName', item())])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [priceRowModel]

        when:
        def builder = factory.createQueryBuilder(itemSearchRequest)

        then:
        with(builder) {
            integrationItemModel.is priceRowModel
            keyCondition == [productName: null]
            parameters == [product: null]
            !orderedByPK
        }
    }

    @Test
    def 'query does not contain parameters when no key attributes are present in the request item and no pagination is present'() {
        given: 'search request with no key attributes or filter'
        def codeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def itemSearchRequest = Stub(ItemSearchRequest) {
            getTypeDescriptor() >> typeDescriptor([codeAttributeDescriptor])
            getFilter() >> null
            getRequestedItem() >> Optional.empty()
            getPaginationParameters() >> Optional.empty()
            getAcceptLocale() >> Locale.ENGLISH
        }
        and: 'Integration Object service finds the Integration Object Item model'
        def productModel = item([uniqueAttribute('code')])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]

        when:
        def builder = factory.createQueryBuilder(itemSearchRequest)

        then:
        with(builder) {
            integrationItemModel.is productModel
            !keyCondition
            parameters.isEmpty()
            orderedByPK
        }
    }

    @Test
    def 'query contains pagination parameters when they are present in the request'() {
        given: 'search request with no key attributes or filter'
        def pageStart = 3
        def pageSize = 5
        def codeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isPrimitive() >> true
        }
        def itemSearchRequest = Stub(ItemSearchRequest) {
            getTypeDescriptor() >> typeDescriptor([codeAttributeDescriptor])
            getFilter() >> null
            getPaginationParameters() >> Optional.of(paginationParameters(pageStart, pageSize))
            getRequestedItem() >> Optional.empty()
            getAcceptLocale() >> Locale.ENGLISH
        }
        and: 'Integration Object service finds the Integration Object Item model'
        def productModel = item([uniqueAttribute('code')])
        integrationObjectService.findAllIntegrationObjectItems(CONTEXT_IO_CODE) >> [productModel]

        when:
        def builder = factory.createQueryBuilder(itemSearchRequest)

        then:
        with(builder) {
            integrationItemModel.is productModel
            !keyCondition
            parameters.isEmpty()
            orderedByPK
            start == pageStart
            count == pageSize
            totalCount
        }
    }

    def paginationParameters(int pageStart, int pageSize) {
        Stub(PaginationParameters) {
            getPageStart() >> pageStart
            getPageSize() >> pageSize
        }
    }

    def itemSearchRequest(Collection attributeDescriptors) {
        Stub(ItemSearchRequest) {
            getRequestedItem() >> Optional.of(Stub(IntegrationItem) {
                getIntegrationObjectCode() >> CONTEXT_IO_CODE
                getItemType() >> typeDescriptor(attributeDescriptors)
            })
            getTypeDescriptor() >> typeDescriptor(attributeDescriptors)
            getAcceptLocale() >> Locale.ENGLISH
            getPaginationParameters() >> Optional.empty()
        }
    }

    def typeDescriptor(Collection attributeDescriptors) {
        Stub(TypeDescriptor) {
            getItemCode() >> CONTEXT_ITEM_CODE
            getAttributes() >> attributeDescriptors
            getIntegrationObjectCode() >> CONTEXT_IO_CODE
        }
    }

    def item(List<IntegrationObjectItemAttributeModel> attributes = []) {
        def item = Stub(IntegrationObjectItemModel) {
            getCode() >> CONTEXT_ITEM_CODE
            getIntegrationObject() >> Stub(IntegrationObjectModel)
        }
        def attachedToItemAttributes = attributes.collect { attribute(item, it) }
        item.getAttributes() >> attachedToItemAttributes
        item.getKeyAttributes() >> attachedToItemAttributes.findAll { it.unique }
        item.getUniqueAttributes() >> attachedToItemAttributes.findAll { it.unique }
        item
    }

    def nonUniqueAttribute(String name, IntegrationObjectItemModel item = null) {
        attribute(name, item)
    }

    def uniqueAttribute(String name, IntegrationObjectItemModel item = null) {
        attribute(name, item, true)
    }

    def attribute(String name, IntegrationObjectItemModel item = null, boolean unique = false) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> name
            getUnique() >> unique
            getReturnIntegrationObjectItem() >> item
            getAttributeDescriptor() >> attributeDescriptor(name)
        }
    }

    def attribute(IntegrationObjectItemModel item, IntegrationObjectItemAttributeModel attr) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> attr.attributeName
            getUnique() >> attr.unique
            getIntegrationObjectItem() >> item
            getReturnIntegrationObjectItem() >> attr.returnIntegrationObjectItem
            getAttributeDescriptor() >> attributeDescriptor(attr.attributeName)
        }
    }

    def attributeDescriptor(String qualifier) {
        Stub(AttributeDescriptorModel) {
            getLocalized() >> false
            getQualifier() >> qualifier
        }
    }
}
