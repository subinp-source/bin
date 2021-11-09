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
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull

@UnitTest
class FromClauseBuilderUnitTest extends Specification {

    private static final ITEM_CODE = "MyCode"
    private static final TABLE_NAME = ITEM_CODE.toLowerCase()

    def builder = FromClauseBuilder.builder()

    @Test
    @Unroll
    def "many-to-many relationship results in JOIN for #end and uses field 'supercategories' when attribute is named '#attribute'"() {
        given:
        def condition = new WhereClauseCondition("{$attribute} = 1234")
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withFilter(filter)
                .withIntegrationObjectItem(item)
                .build()

        then:
        query == "SELECT DISTINCT {product:pk} FROM {Product* AS product JOIN RelationName AS relationname ON {product:pk} = {relationname:$end}}"

        where:
        end      | attribute                | item
        'source' | 'attibuteTypeCategory'   | sourceItem('attibuteTypeCategory', 'Category', true)
        'source' | 'attributeNameNotMatter' | sourceItem('attributeNameNotMatter', 'Category', true)
        'target' | 'attributeNameNotMatter' | sourceItem('attributeNameNotMatter', 'Category', false)
    }

    @Test
    def "one-to-many relationship results in JOIN for source type and target type on source type's pk."(){
        given:
        def condition = new WhereClauseCondition("{europe1Price} = 1234")
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withFilter(filter)
                .withIntegrationObjectItem(oneToManyItem('europe1Price', 'PriceRow', true))
                .build()

        then:
        query == "SELECT DISTINCT {product:pk} FROM {Product* AS product JOIN PriceRow AS pricerow ON {product:pk} = {pricerow:product}}"
    }

    @Test
    def "from clause for enum type item"() {
        given:
        def condition = new WhereClauseCondition('{code} = enum2')
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withFilter(filter)
                .withIntegrationObjectItem(enumItem())
                .build()

        then:
        query == "SELECT DISTINCT {$TABLE_NAME:pk} FROM {$ITEM_CODE AS $TABLE_NAME}"
    }

    @Test
    def "from clause for multiple relations item"() {
        given:
        def condition1 = new WhereClauseCondition('{supercategories} = cat1')
        def condition2 = new WhereClauseCondition('{vendors} = vendor1')
        def filter = new WhereClauseConditions(condition1, condition2)
        def itemModel = multiRelationItem()

        when:
        def query = builder
                .withFilter(filter)
                .withIntegrationObjectItem(itemModel)
                .build()

        then:
        query == "SELECT DISTINCT {product:pk} FROM {Product* AS product JOIN CategoryProductRelation AS categoryproductrelation ON {product:pk} = {categoryproductrelation:source} JOIN ProductVendorRelation AS productvendorrelation ON {product:pk} = {productvendorrelation:source}}"
    }

    @Test
    @Unroll
    def "from clause includes MyCode#end when type hierarchy restriction is #itemTypeMatch and integration object item is not enum or abstract type"() {
        given:
        def condition = new WhereClauseCondition('{code} = mycode')
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withFilter(filter)
                .withIntegrationObjectItem(item())
                .withTypeHierarchyRestriction(itemTypeMatch)
                .build()

        then:
        query == "SELECT DISTINCT {$TABLE_NAME:pk} FROM {$ITEM_CODE$end AS $TABLE_NAME}"

        where:
        end | itemTypeMatch
        '*' | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        ''  | ItemTypeMatch.ALL_SUBTYPES
        '!' | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE
        '*' | null
    }


    @Unroll
    @Test
    def "from clause for an #type item is always ALL_SUBTYPES even when the type hierarchy restriction is #itemTypeMatch"() {
        given:
        def condition = new WhereClauseCondition('{code} = ')
        def filter = new WhereClauseConditions(condition)

        when:
        def query = builder
                .withFilter(filter)
                .withIntegrationObjectItem(item)
                .withTypeHierarchyRestriction(itemTypeMatch)
                .build()
        then:
        query == "SELECT DISTINCT {$TABLE_NAME:pk} FROM {$ITEM_CODE${ItemTypeMatch.ALL_SUBTYPES.getValue()} AS $TABLE_NAME}"

        where:
        type        | itemTypeMatch                         | item
        'abstract'  | null                                  | item([abstract: true])
        'abstract'  | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | item([abstract: true])
        'abstract'  | ItemTypeMatch.ALL_SUBTYPES            | item([abstract: true])
        'abstract'  | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | item([abstract: true])
        'enum type' | null                                  | enumItem()
        'enum type' | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | enumItem()
        'enum type' | ItemTypeMatch.ALL_SUBTYPES            | enumItem()
        'enum type' | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | enumItem()
    }

    private IntegrationObjectItemModel sourceItem(String ioAttributeName, String TypeCode, boolean isSource) {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                def relation = Stub(RelationDescriptorModel) {
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getTargetType() >> Stub(ComposedTypeModel) {
                            getCode() >> TypeCode
                        }
                        getSourceTypeCardinality() >> RelationEndCardinalityEnum.MANY
                        getTargetTypeCardinality() >> RelationEndCardinalityEnum.MANY
                    }
                    getRelationName() >> "RelationName"
                    getIsSource() >> isSource
                }
                getDeclaredattributedescriptors() >> [relation]
                getCode() >> "Product"
            }
            getAttributes() >> [attribute(ioAttributeName, TypeCode)]
        }
    }

    private IntegrationObjectItemModel oneToManyItem(String ioAttributeName, String TypeCode, boolean isSource) {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                def relation = Stub(RelationDescriptorModel) {
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getTargetType() >> Stub(ComposedTypeModel) {
                            getCode() >> TypeCode
                        }
                        getSourceTypeCardinality() >> RelationEndCardinalityEnum.ONE
                        getTargetTypeCardinality() >> RelationEndCardinalityEnum.MANY
                    }
                    getRelationName() >> "RelationName"
                    getIsSource() >> isSource
                }
                getDeclaredattributedescriptors() >> [relation]
                getCode() >> "Product"
            }
            getAttributes() >> [attribute(ioAttributeName, TypeCode)]
        }
    }

    private IntegrationObjectItemModel enumItem() {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(EnumerationMetaTypeModel) {
                getDeclaredattributedescriptors() >> []
                getInheritedattributedescriptors() >> []
                getCode() >> ITEM_CODE
            }
        }
    }

    private IntegrationObjectItemModel multiRelationItem() {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                def relation1 = Stub(RelationDescriptorModel) {
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getTargetType() >> Stub(ComposedTypeModel) {
                            getCode() >> "Category"
                        }
                        getSourceTypeCardinality() >> RelationEndCardinalityEnum.MANY
                        getTargetTypeCardinality() >> RelationEndCardinalityEnum.MANY
                    }
                    getRelationName() >> "CategoryProductRelation"
                    getIsSource() >> true
                }

                def relation2 = Stub(RelationDescriptorModel) {
                    getRelationType() >> Stub(RelationMetaTypeModel) {
                        getTargetType() >> Stub(ComposedTypeModel) {
                            getCode() >> "Vendor"
                        }
                        getSourceTypeCardinality() >> RelationEndCardinalityEnum.MANY
                        getTargetTypeCardinality() >> RelationEndCardinalityEnum.MANY
                    }
                    getRelationName() >> "ProductVendorRelation"
                    getIsSource() >> true
                }
                getDeclaredattributedescriptors() >> [relation1, relation2]
                getInheritedattributedescriptors() >> []
                getCode() >> "Product"
            }
            getAttributes() >> [attribute("supercategories", "Category"), attribute("vendors", "Vendor")]
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
            }
        }
    }

    private IntegrationObjectItemModel item(final Map<String, Boolean> params = [:]) {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                getDeclaredattributedescriptors() >> []
                getInheritedattributedescriptors() >> []
                getCode() >> ITEM_CODE
                getAbstract() >> falseIfNull(params['abstract'])
            }
        }
    }
}
