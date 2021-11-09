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

package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import org.apache.commons.lang3.time.DateUtils
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class FlexibleSearchQueryBuilderUnitTest extends Specification {
    private static final String CONTEXT_ITEM_TYPE = 'Product'
    private static final String CONTEXT_ALIAS = CONTEXT_ITEM_TYPE.toLowerCase()

    def service = Stub(IntegrationObjectService)
    def builder = new FlexibleSearchQueryBuilder(service)

    @Shared
    def otherItem = itemWithoutKey('other')

    @Test
    def "constructor cannot be called with null service"() {
        when:
        new FlexibleSearchQueryBuilder(null)

        then:
        def e = thrown(IllegalArgumentException)
        !e.message.isEmpty()
    }

    @Test
    def "builds empty string query when specifications not made"() {
        when: "no specification were done on the builder"
        def query = builder.build()

        then: 'builder has no specifications'
        with(builder) {
            !integrationItemModel
            !typeHierarchyRestriction
            !parameters
            !keyCondition
            !totalCount
            !start
            !count
        }
        and: 'the generated query is an empty string'
        query
        query.query == ''
        query.queryParameters.isEmpty()
    }

    @Test
    @Unroll
    def "generates SELECT FROM #fromClauseItemTypeMatchValue for a #type type integration object item with itemTypeMatch=#ItemTypeMatchEnum.ALL_SUBTYPES & configuration item search set to #configItemTypeMatch"() {
        given:
        service.findAllIntegrationObjectItems('myObject') >> [otherItem, item]

        when:
        builder.withIntegrationObjectItem('myObject', 'myItem').withTypeHierarchyRestriction(configItemTypeMatch)
        def query = builder.build()

        then: 'integration item is specified in the builder'
        builder.integrationItemModel.code == 'myItem'
        and: 'query has no WHERE clause'
        query.query == "SELECT DISTINCT {myitem:pk} FROM {myItem AS myitem}"
        query.queryParameters.isEmpty()

        where:
        type       | item                      | configItemTypeMatch
        'abstract' | abstractItem('myItem')    | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        'abstract' | abstractItem('myItem')    | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        'abstract' | abstractItem('myItem')    | ItemTypeMatch.ALL_SUBTYPES
        'enum'     | enumerationItem('myItem') | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        'enum'     | enumerationItem('myItem') | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        'enum'     | enumerationItem('myItem') | ItemTypeMatch.ALL_SUBTYPES
    }

    @Test
    @Unroll
    def "generates SELECT FROM #fromClauseItemTypeMatchValue for a composed type integration object item with itemTypeMatch=#itemTypeMatch & configuration item search set to #configItemTypeMatch"() {
        given:
        service.findAllIntegrationObjectItems('myObject') >> [otherItem, itemWithoutKey('myItem', itemTypeMatch)]

        when:
        builder.withIntegrationObjectItem('myObject', 'myItem').withTypeHierarchyRestriction(configItemTypeMatch)
        def query = builder.build()

        then: 'integration item is specified in the builder'
        builder.integrationItemModel.code == 'myItem'
        and: 'query has no WHERE clause'
        query.query == "SELECT DISTINCT {myitem:pk} FROM {myItem$fromClauseItemTypeMatchValue AS myitem}"
        query.queryParameters.isEmpty()

        where:
        configItemTypeMatch                   | itemTypeMatch                             | fromClauseItemTypeMatchValue
        ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatchEnum.ALL_SUBTYPES            | ''
        ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatchEnum.ALL_SUBTYPES            | ''
        ItemTypeMatch.ALL_SUBTYPES            | ItemTypeMatchEnum.ALL_SUBTYPES            | ''
        ItemTypeMatch.ALL_SUBTYPES            | null                                      | ''
        null                                  | null                                      | '*'

        ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | '*'
        ItemTypeMatch.ALL_SUBTYPES            | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | '*'
        ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | '*'
        ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | null                                      | '*'
        null                                  | null                                      | '*'

        ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | '!'
        ItemTypeMatch.ALL_SUBTYPES            | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | '!'
        ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | '!'
        ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | null                                      | '!'
        null                                  | null                                      | '*'
    }

    @Test
    def "illegal state if key condition added without integration object item"() {
        when:
        builder.withKeyConditionFor([:]).build()

        then:
        def e = thrown(IllegalStateException)
        !e.message.isEmpty()
    }

    @Test
    def "key condition added for integration object item without key attributes"() {
        when:
        def query = builder
                .withIntegrationObjectItem(itemWithoutKey("ValueObject"))
                .withKeyConditionFor([:])
                .build()

        then: 'builder contains empty key condition specification'
        builder.keyCondition.isEmpty()
        and: 'query does not contain a WHERE clause'
        query.query == 'SELECT DISTINCT {valueobject:pk} FROM {ValueObject* AS valueobject}'
        query.queryParameters.isEmpty()
    }

    @Test
    def "key condition uses Date for Calendar attribute values"() {
        given:
        def now = new Date()
        def item = ["key": DateUtils.toCalendar(now)]
        def itemMetadata = itemWithSimpleKey("key")

        when:
        def query = builder
                .withIntegrationObjectItem(itemMetadata)
                .withKeyConditionFor(item)
                .build()

        then: 'builder contains key condition specification'
        builder.keyCondition.is item
        and: 'query contains a WHERE clause'
        query.query == "SELECT DISTINCT {$CONTEXT_ALIAS:pk} FROM {$CONTEXT_ITEM_TYPE* AS $CONTEXT_ALIAS} WHERE {$CONTEXT_ALIAS:key} = ?key"
        query.queryParameters == [key: now]
    }

    @Test
    def 'generated WHERE clause contains qualifier name of a key condition attribute'() {
        given: 'an IO item that has key attribute named differently in the type system and in the IO'
        def itemMetadata = itemWithSimpleKey('productCode', 'code')
        and: 'the item for the key values contains values for both attribute names - type system and IO'
        def item = [productCode: 'A1', code: '000']

        when:
        def query = builder
                .withIntegrationObjectItem(itemMetadata)
                .withKeyConditionFor(item)
                .build()

        then: 'query builds WHERE clause with the type system attribute name'
        query.query.endsWith " WHERE {$CONTEXT_ALIAS:code} = ?code"
        query.queryParameters == [code: 'A1']
    }

    @Test
    def "ORDER BY is added to the query when orderByPk is called"() {
        given:
        def itemMetadata = itemWithSimpleKey("key")

        when:
        def query = builder
                .withIntegrationObjectItem(itemMetadata)
                .orderedByPK()
                .build()
                .query

        then: 'builder contains the orderBy specification'
        builder.isOrderedByPK()
        and: 'query contains the ORDER BY clause'
        query.endsWith "ORDER BY {$CONTEXT_ALIAS:pk}"
    }

    @Test
    def "can request total count in the response"() {
        when:
        def qry = builder.withTotalCount().build()

        then:
        builder.totalCount
        qry.needTotal
    }

    @Test
    def "count and start provided to search query builder"() {
        when:
        def query = builder
                .withCount(5)
                .withStart(10)
                .build()

        then: 'builder parameters are set'
        builder.count == 5
        builder.start == 10
        and: 'query parameters are set'
        query.getCount() == 5
        query.getStart() == 10
    }

    @Test
    def "query contains count and start when pagination parameters are provided"() {
        when:
        def query = builder
                .withPaginationParameters(Stub(PaginationParameters) {
                    getPageStart() >> 10
                    getPageSize() >> 5
                })
                .build()

        then: 'builder parameters are set'
        builder.count == 5
        builder.start == 10
        and: 'query parameters are set'
        query.getCount() == 5
        query.getStart() == 10
    }

    @Test
    @Unroll
    def "exception thrown when specified integration #object does not exist"() {
        given:
        service.findAllIntegrationObjectItems('myObject') >> foundItems

        when:
        builder.withIntegrationObjectItem('myObject', 'myItem')

        then:
        thrown(IllegalArgumentException)

        where:
        object        | foundItems
        'object'      | []
        'object item' | [item('myObject', 'itemOne'), item('myObject', 'itemTwo')]
    }

    @Test
    def "WHERE condition is present only once when same parameter added several times"() {
        given:
        def itemMetadata = itemWithSimpleKey("key")

        when:
        def query = builder
                .withIntegrationObjectItem(itemMetadata)
                .withParameter("param", "value1")
                .withParameter("param", "value2")
                .build()

        then: 'parameters set correctly in the builder'
        builder.parameters == [param: 'value2']
        and: 'query contains condition only for one parameter'
        query.query.endsWith " WHERE {$CONTEXT_ALIAS:param} = ?param"
        query.queryParameters == [param: 'value2']
    }

    @Test
    def "WHERE condition contains all parameters provided from a Map of parameters"() {
        given:
        def itemMetadata = itemWithSimpleKey("key")

        when:

        def query = builder
                .withIntegrationObjectItem(itemMetadata)
                .withParameters(["param1": "value1", "param2": "value2"])
                .build()

        then: 'parameters set correctly in the builder'
        builder.parameters == [param1: 'value1', param2: 'value2']
        and: 'query contains condition only for one parameter'
        query.query.endsWith " WHERE {$CONTEXT_ALIAS:param1} = ?param1 AND {$CONTEXT_ALIAS:param2} = ?param2"
        query.queryParameters == [param1: 'value1', param2: 'value2']
    }

    @Test
    def "generates SELECT FROM WHERE ORDER BY when item type, a parameter, and order by are specified"() {
        when:
        def query = builder
                .withIntegrationObjectItem(item('SomeService', 'SomeType'))
                .withParameter("param", new Object())
                .orderedByPK()
                .build()

        then:
        query.query == 'SELECT DISTINCT {sometype:pk} FROM {SomeType* AS sometype} WHERE {sometype:param} = ?param ORDER BY {sometype:pk}'
    }

    def enumerationItem(final String code) {
        def type = Stub(EnumerationMetaTypeModel) {
            getCode() >> code
        }

        Stub(IntegrationObjectItemModel) {
            getCode() >> code
            getType() >> type
            getItemTypeMatch() >> ItemTypeMatchEnum.ALL_SUBTYPES
        }
    }

    def abstractItem(final String code) {
        def type = Stub(EnumerationMetaTypeModel) {
            getCode() >> code
        }
        type.abstract >> true

        Stub(IntegrationObjectItemModel) {
            getCode() >> code
            getType() >> type
            getItemTypeMatch() >> ItemTypeMatchEnum.ALL_SUBTYPES
        }
    }

    def itemWithoutKey(final String code, final itemTypeMatch = null) {
        itemWithoutKey(code, code, itemTypeMatch)
    }

    def itemWithoutKey(final String integrationCode, final String platformCode, final itemTypeMatch = null) {
        def item = item(integrationCode, platformCode, itemTypeMatch)
        item.uniqueAttributes >> []
        item
    }

    def item(final String integrationCode, final String platformCode, final itemTypeMatch = null) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> integrationCode
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> platformCode
                getAbstract() >> false
            }
            getItemTypeMatch() >> itemTypeMatch
        }
    }

    private IntegrationObjectItemModel itemWithSimpleKey(String attrName) {
        itemWithSimpleKey(attrName, attrName)
    }

    private IntegrationObjectItemModel itemWithSimpleKey(String attrName, String qualifier) {
        def item = item('myObject', CONTEXT_ITEM_TYPE)

        def attribute = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> attrName
            getIntegrationObjectItem() >> item // it's important to refer back to the same item
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getQualifier() >> qualifier
            }
        }
        item.uniqueAttributes >> [attribute]
        item
    }

}
