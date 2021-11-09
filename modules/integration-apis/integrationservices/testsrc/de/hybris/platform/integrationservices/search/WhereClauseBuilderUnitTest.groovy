/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.enums.RelationEndCardinalityEnum
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.RelationDescriptorModel
import de.hybris.platform.core.model.type.RelationMetaTypeModel
import de.hybris.platform.integrationservices.exception.FilterByClassificationAttributeNotSupportedException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.assertj.core.api.Assertions.assertThat

@UnitTest
class WhereClauseBuilderUnitTest extends Specification {
    def builder = WhereClauseBuilder.builder()

    @Shared
    def cardinalityMany = RelationEndCardinalityEnum.MANY

    @Shared
    def cardinalityOne = RelationEndCardinalityEnum.ONE


    @Test
    def "no where clause created"() {
        when:
        def clause = builder.build()

        then:
        clause == ""
    }

    @Test
    def "throws exception when WHERE clause includes a classificationAttribute"() {
        given:
        def attrName = "a"
        def classificationAttribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
            getAttributeName() >> attrName
        }
        def item = itemWithClassificationAttributes('itemCode', [classificationAttribute])

        def condition = new WhereClauseCondition("{$attrName} =1234")
        def filter = new WhereClauseConditions(condition)

        when:
        builder.withIntegrationObjectItem(item)
                .withFilter(filter)
                .build()

        then:
        def e = thrown FilterByClassificationAttributeNotSupportedException
        e.attribute == classificationAttribute
        e.message == "Filtering by classification attribute ${attrName} is not supported."
    }

    @Test
    def "throws exception when WHERE clause includes a virtualAttribute"() {
        given:
        def attrName = "virtualAttributeTest"
        def virtualAttribute = Stub(IntegrationObjectItemVirtualAttributeModel) {
            getAttributeName() >> attrName
        }
        def item = itemWithVirtualAttributes('itemCode', [virtualAttribute])

        def condition = new WhereClauseCondition("{$attrName} =1234")
        def filter = new WhereClauseConditions(condition)

        when:
        builder.withIntegrationObjectItem(item)
                .withFilter(filter)
                .build()

        then:
        def e = thrown FilterByVirtualAttributeNotSupportedException
        e.attribute == virtualAttribute
        e.message == "Filtering by virtual attribute ${attrName} is not supported."
    }

    @Test
    def "generates WHERE clause if at least one parameter added and no filter"() {
        when:
        def query = builder
                .withIntegrationObjectItem(item("someitem"))
                .withParameters(["someNumber": 10]).build()

        then:
        query.trim().capitalize().startsWith 'WHERE '
        assertThat(query.substring(6).trim())
                .startsWith("{someitem:someNumber}")
                .contains('=')
                .endsWith("?someNumber")
        and: 'no conjunction when there is no filter'
        !query.contains("AND")
    }

    @Test
    def "connects WHERE conditions with AND when several parameters added"() {
        when:
        def query = builder
                .withIntegrationObjectItem(item("someitem"))
                .withParameters(["param1": "one", "param2": "two"])
                .build()

        then:
        query.trim().capitalize().startsWith 'WHERE '
        assertThat(query.substring(6).trim())
                .contains("{someitem:param1}")
                .containsIgnoringCase(" AND ")
                .contains("{someitem:param2}")
    }

    @Test
    def "generates WHERE clause with null values"() {
        when:
        def parameters = new HashMap()
        parameters.put("value1", 10)
        parameters.put("value2", null)
        parameters.put("value3", "null")
        def query = builder
                .withIntegrationObjectItem(item("someitem"))
                .withParameters(parameters).build()

        then:
        query.trim().capitalize().startsWith 'WHERE '
        assertThat(query.substring(6).trim())
                .contains("{someitem:value1} = ?value1")
                .containsIgnoringCase(" AND ")
                .contains("{someitem:value2} IS NULL")
                .contains("{someitem:value3} IS NULL")
    }

    @Test
    @Unroll
    def "generates join when filtering on property that has a many-to-many #end relation"() {
        given:
        def condition = new WhereClauseCondition("{$propertyName} = 1234")
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withIntegrationObjectItem(item)
                .withFilter(filter)
                .build()

        then:
        query.contains "{relationname:$end}"

        where:
        end      | propertyName      | item
        'source' | 'supercategories' | itemWithSourceRelation('supercategories', false, cardinalityMany, cardinalityMany)
        'target' | 'products'        | itemWithSourceRelation('products', true, cardinalityMany, cardinalityMany)
    }

    @Test
    def "generates join for multiple filtering conditions"() {
        given:
        def manyToManyCondition = new WhereClauseCondition('{supercategories} = 1234', ConjunctiveOperator.AND)
        def oneToOneCondition = new WhereClauseCondition('{code} = mycode')
        def filter = new WhereClauseConditions(manyToManyCondition, oneToOneCondition)

        when:
        def query = builder
                .withIntegrationObjectItem(itemWithSourceRelation('supercategories', false, cardinalityMany, cardinalityMany))
                .withFilter(filter)
                .build()

        then:
        query == ' WHERE {relationname:source} = 1234 AND {product:code} = mycode'
    }

    @Test
    def 'generates type system attribute name when IO attribute name differs in the simple attribute filter condition'() {
        given: 'an IO item with attribute "id" that is called "code" in the type system'
        def ioItemModel = Stub(IntegrationObjectItemModel) {
            getAttributes() >> [attribute('id', [qualifier: 'code', localized: false] as Map<String, Boolean>)]
            getType() >> Stub(ComposedTypeModel) { getCode() >> 'Product' }
        }
        and: 'the filter condition uses IO attribute name'
        def condition = new WhereClauseCondition('{id} = some_value')
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withIntegrationObjectItem(ioItemModel)
                .withFilter(filter)
                .withLocale(Locale.ENGLISH)
                .build()

        then: 'type system name is used in the where clause'
        query == ' WHERE {product:code} = some_value'
    }

    @Test
    def 'generates type system attribute name when IO attribute name differs in the reference attribute filter condition'() {
        given: 'an IO item with attribute "categories" that is called "supercategories" in the type system'
        def ioItemModel = itemWithSourceRelation('categories', 'supercategories', false,
                cardinalityMany, cardinalityMany, 'supercategories')
        and: 'the filter condition uses IO attribute name'
        def condition = new WhereClauseCondition('{categories} IN (pk)')
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withIntegrationObjectItem(ioItemModel)
                .withFilter(filter)
                .build()

        then: 'type system attribute name is used in the where clause'
        query == ' WHERE {relationname:source} IN (pk)'
    }

    @Test
    @Unroll
    def 'where clause is #expectedWhere when the source cardinality is #sourceCardinality and the target cardinality is #targetCardinality'() {
        given:
        def ioItemModel = itemWithSourceRelation(attributeName, targetCode, isItemModelSource,
                sourceCardinality, targetCardinality, sourceCode)
        and: 'the filter condition uses the source attribute'
        def condition = new WhereClauseCondition("{$attributeName} IN (pk)")
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withIntegrationObjectItem(ioItemModel)
                .withFilter(filter)
                .build()

        then:
        query == expectedWhere

        where:
        expectedWhere                                  | sourceCardinality | targetCardinality | attributeName         | sourceCode        | targetCode            | isItemModelSource
        ' WHERE {relationname:source} IN (pk)'         | cardinalityMany   | cardinalityMany   | 'targetAttributeName' | 'sourceAttribute' | 'sourceAttribute'     | false
        ' WHERE {product:iOAttributeTypeCode} IN (pk)' | cardinalityOne    | cardinalityOne    | 'IOAttributeName'     | 'Product'         | 'iOAttributeTypeCode' | true
        ' WHERE {targetcode:pk} IN (pk)'               | cardinalityOne    | cardinalityMany   | 'targetAttributeName' | 'sourceCode'      | 'targetCode'          | true
        ' WHERE {sourcecode:targetCode} IN (pk)'       | cardinalityMany   | cardinalityOne    | 'targetAttributeName' | 'sourceCode'      | 'targetCode'          | true
    }

    @Test
    def 'where clause with renamed localized attributes'() {
        given: 'an IO item with renamed localized attribute "name" was renamed to "aliasName"'
        def ioItemModel = Stub(IntegrationObjectItemModel) {
            getAttributes() >> [attribute('aliasName', [qualifier: 'name', localized: true] as Map<String, Boolean>)]
            getType() >> Stub(ComposedTypeModel) { getCode() >> 'Product' }
        }
        and: 'the filter condition uses IO attribute name'
        def condition = new WhereClauseCondition('{aliasName} = some_value')
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withIntegrationObjectItem(ioItemModel)
                .withFilter(filter)
                .withLocale(Locale.ENGLISH)
                .build()

        then: 'type system name is used in the where clause'
        query == ' WHERE {product:name[en]} = some_value'
    }

    @Test
    def "where clause contains the filter conditions and parameters when they are present"() {
        when:
        def query = builder
                .withIntegrationObjectItem(item("someitem"))
                .withParameters(["someNumber": 10])
                .withFilter(SimplePropertyWhereClauseCondition.eq("someString", 'test').toWhereClauseConditions())
                .build()

        then:
        query.contains " WHERE {someitem:someNumber} = ?someNumber AND {someitem:someString} = 'test'"
    }

    def itemWithManyToManySourceRelation(String attributeName, String qualifier = attributeName) {
        itemWithSourceRelation(attributeName, qualifier, cardinalityMany, cardinalityMany)
    }

    def itemWithSourceRelation(String attributeName, String qualifier = attributeName, RelationEndCardinalityEnum sourceCardinality, RelationEndCardinalityEnum targetCardinality) {
        Stub(IntegrationObjectItemModel) {
            def attribute = Stub(IntegrationObjectItemAttributeModel) {
                getAttributeDescriptor() >> Stub(RelationDescriptorModel) {
                    getQualifier() >> qualifier
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getSourceTypeRole() >> qualifier
                        getSourceTypeCardinality() >> sourceCardinality
                        getTargetTypeCardinality() >> targetCardinality
                    }
                    getRelationName() >> "RelationName"
                    getPersistenceClass() >> Object.class
                }
                getAttributeName() >> attributeName
            }
            getCode() >> "Product"
            getAttributes() >> [attribute]
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> 'Product'
            }
        }
    }

    def itemWithSourceRelation(String attributeName, String targetCode = attributeName, Boolean isSource,
                               RelationEndCardinalityEnum sourceCardinality, RelationEndCardinalityEnum targetCardinality, sourceCode = 'Product') {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                def relation = Stub(RelationDescriptorModel) {
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getTargetType() >> Stub(ComposedTypeModel) {
                            getCode() >> targetCode
                        }
                        getSourceTypeCardinality() >> sourceCardinality
                        getTargetTypeCardinality() >> targetCardinality
                    }
                    getRelationName() >> "RelationName"
                    getIsSource() >> isSource  // if false, target and source are switched.
                }
                getDeclaredattributedescriptors() >> [relation]
                getCode() >> sourceCode
            }
            getAttributes() >> [attribute(attributeName, targetCode)]
        }
    }

    IntegrationObjectItemAttributeModel attribute(String name, String typeCode) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> name
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getPersistenceClass() >> Object.class
                getAttributeType() >> Stub(ComposedTypeModel) {
                    getCode() >> typeCode
                }
                getQualifier() >> typeCode
            }
        }
    }

    def itemWithTargetRelation(String attributeName) {
        Stub(IntegrationObjectItemModel) {
            def attribute = Stub(IntegrationObjectItemAttributeModel) {
                getAttributeDescriptor() >> Stub(RelationDescriptorModel) {
                    getQualifier() >> attributeName
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getTargetTypeRole() >> attributeName
                        getSourceTypeCardinality() >> cardinalityMany
                        getTargetTypeCardinality() >> cardinalityMany
                    }
                    getRelationName() >> "RelationName"
                    getPersistenceClass() >> Object.class
                }
                getAttributeName() >> attributeName
            }
            getCode() >> "Product"
            getAttributes() >> [attribute]
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> 'Product'
            }
        }
    }

    def enumerationItem(final String code) {
        def type = Stub(EnumerationMetaTypeModel) {
            getCode() >> code
        }

        Stub(IntegrationObjectItemModel) {
            getCode() >> code
            getType() >> type
        }
    }

    def item(final String code) {
        def item = item(code, code)
        item.getClassificationAttributes() >> []
        item
    }

    def item(final String integrationCode, final String platformCode) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> integrationCode
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> platformCode
            }
            getUniqueAttributes() >> []
        }
    }

    def itemWithClassificationAttributes(final String code, final List classificationAttributes) {
        def item = item(code, code)
        item.getClassificationAttributes() >> classificationAttributes
        item
    }

    def itemWithVirtualAttributes(final String code, final List virtualAttributes) {
        def item = item(code, code)
        item.getVirtualAttributes() >> virtualAttributes
        item
    }

    IntegrationObjectItemAttributeModel attribute(final String ioName, final Map<String, Boolean> params) {
        new IntegrationObjectItemAttributeModel(
                attributeName: ioName,
                attributeDescriptor: Stub(AttributeDescriptorModel) {
                    getLocalized() >> params['localized']
                    getQualifier() >> params['qualifier']
                    getPersistenceClass() >> Object.class
                }
        )
    }
}