/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2services.odata.ODataSchema
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-3193')
class MapTypeSchemaGenerationIntegrationTest extends ServicelayerSpockSpecification {

    private static final String TEST_IO = 'SchemaWithMapsIO'
    private static final String NULLABLE = 'Nullable'
    private static final String IS_UNIQUE = 's:IsUnique'

    @Resource(name = 'oDataSchemaGenerator')
    SchemaGenerator generator

    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def "Schema generated with attribute of type map with primitive key/value types"() {
        given: 'integration object contains a Map attribute "additionalProperties" with primitive key/value'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                                      ; $TEST_IO           ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $TEST_IO                              ; ConsumedDestination; ConsumedDestination ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $TEST_IO:ConsumedDestination                                       ; id                          ; ConsumedDestination:id                             ;                                                           ;                        ;",
                "                                            ; $TEST_IO:ConsumedDestination                                       ; additionalProperties        ; ConsumedDestination:additionalProperties           ;                                                           ;                        ; "
        )

        when:
        def schema = ODataSchema.from generator.generateSchema(getIntegrationObjectItemModelDefinitions())

        then: 'schema contains the map type for the ConsumedDestination type'
        schema.entityTypeNames.containsAll(['String2StringMapType', 'ConsumedDestination'])
        def mapType = schema.getEntityType('String2StringMapType')
        def consumedDestinationType = schema.getEntityType('ConsumedDestination')

        and: 'the Map type contains all expected properties'
        mapType.propertyNames.containsAll(['key', 'value'])
        with(mapType.getAnnotatableProperty('key')) {
            annotationNames.containsAll(NULLABLE, IS_UNIQUE)
            getAnnotation(NULLABLE).get().text == 'false'
            getAnnotation(IS_UNIQUE).get().text == 'true'
        }
        with(mapType.getAnnotatableProperty('value')) {
            annotationNames.contains(NULLABLE)
            getAnnotation(NULLABLE).get().text == 'true'
        }

        and: 'the Map type key consists of the key property'
        mapType.getKeyProperties() == ['key']

        and: 'type ConsumedDestination contains a navigation property referring to its Map attributes'
        consumedDestinationType.navigationPropertyNames == ['additionalProperties']

        and: 'the schema contains association between the ConsumedDestination type and additionalProperties'
        schema.containsAssociationBetween('ConsumedDestination', 'String2StringMapType')
        schema.defaultEntityContainer.containsAssociationSetBetween('ConsumedDestination', 'String2StringMapType')
    }

    @Test
    def "Schema generated with multiple attributes of the same map type only generate one entity type for the map"() {
        given: 'integration object contains a Map attribute "additionalProperties" with primitive key/value'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                                      ; $TEST_IO           ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]',
                "                                   ; $TEST_IO                              ; ConsumedDestination; ConsumedDestination ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $TEST_IO:ConsumedDestination                                       ; id                          ; ConsumedDestination:id                             ;                                                           ;                        ;",
                "                                            ; $TEST_IO:ConsumedDestination                                       ; additionalProperties        ; ConsumedDestination:additionalProperties           ;                                                           ;                        ;",
                "                                            ; $TEST_IO:ConsumedDestination                                       ; moreAdditionalProperties    ; ConsumedDestination:additionalProperties           ;                                                           ;                        ;"
        )

        when:
        def schema = ODataSchema.from generator.generateSchema(getIntegrationObjectItemModelDefinitions())

        then: 'schema only contains one Map type'
        schema.entityTypeNames == ['String2StringMapType', 'ConsumedDestination']

        and: 'type ConsumedDestination contains a navigation property for each one of the Map attributes'
        def consumedDestinationType = schema.getEntityType('ConsumedDestination')
        consumedDestinationType.navigationPropertyNames == ['additionalProperties', 'moreAdditionalProperties']
    }

    @Test
    def "Schema contains multiple map types for with different key/value types"() {
        given: 'integration object contains a Map attribute "additionalProperties" with primitive key/value'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                                      ; $TEST_IO           ",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]        ; type(code); root[default = false]',
                "                                   ; $TEST_IO                              ; ConsumedDestination        ; ConsumedDestination ;",
                "                                   ; $TEST_IO                              ; FlexibleSearchRetentionRule; FlexibleSearchRetentionRule ;",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]',
                "                                            ; $TEST_IO:ConsumedDestination                                       ; id                          ; ConsumedDestination:id                             ;                                                           ;                        ;",
                "                                            ; $TEST_IO:ConsumedDestination                                       ; additionalProperties        ; ConsumedDestination:additionalProperties           ;                                                           ;                        ;",
                "                                            ; $TEST_IO:FlexibleSearchRetentionRule                               ; code                        ; FlexibleSearchRetentionRule:code                   ;                                                           ;                        ;",
                "                                            ; $TEST_IO:FlexibleSearchRetentionRule                               ; queryParameters             ; FlexibleSearchRetentionRule:queryParameters        ;                                                           ;                        ;"
        )

        when:
        def schema = ODataSchema.from generator.generateSchema(getIntegrationObjectItemModelDefinitions())

        then: 'schema contains the both map types'
        schema.entityTypeNames.containsAll(['String2StringMapType', 'QueryParametersMapType', 'ConsumedDestination', 'FlexibleSearchRetentionRule'])
        def string2StingMapType = schema.getEntityType('String2StringMapType')
        def queryParamsMapType = schema.getEntityType('QueryParametersMapType')
        def consumedDestinationType = schema.getEntityType('ConsumedDestination')
        def flexibleSearchRetentionRuleType = schema.getEntityType('FlexibleSearchRetentionRule')

        and: 'all Map types contain integrationKey, key and value properties'
        string2StingMapType.propertyNames.containsAll(['key', 'value'])
        with(string2StingMapType.getAnnotatableProperty('key')) {
            annotationNames.containsAll(NULLABLE, IS_UNIQUE)
            getAnnotation(NULLABLE).get().text == 'false'
            getAnnotation(IS_UNIQUE).get().text == 'true'
        }
        with(string2StingMapType.getAnnotatableProperty('value')) {
            annotationNames.contains(NULLABLE)
            getAnnotation(NULLABLE).get().text == 'true'
        }

        queryParamsMapType.propertyNames.containsAll(['key', 'value'])
        with(queryParamsMapType.getAnnotatableProperty('key')) {
            annotationNames.containsAll(NULLABLE, IS_UNIQUE)
            getAnnotation(NULLABLE).get().text == 'false'
            getAnnotation(IS_UNIQUE).get().text == 'true'
        }
        with(queryParamsMapType.getAnnotatableProperty('value')) {
            annotationNames.contains(NULLABLE)
            getAnnotation(NULLABLE).get().text == 'true'
        }

        and: 'types contain navigation properties referring to the map attributes'
        consumedDestinationType.navigationPropertyNames == ['additionalProperties']
        flexibleSearchRetentionRuleType.navigationPropertyNames == ['queryParameters']

        and: 'the schema contains associations to both map types'
        schema.containsAssociationBetween('ConsumedDestination', 'String2StringMapType')
        schema.containsAssociationBetween('FlexibleSearchRetentionRule', 'QueryParametersMapType')
    }

    def getIntegrationObjectItemModelDefinitions() {
        IntegrationTestUtil.findAll(IntegrationObjectItemModel) as Collection<IntegrationObjectItemModel>
    }
}
