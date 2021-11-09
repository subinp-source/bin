/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.integrationkey.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidIntegrationKeyException
import de.hybris.platform.odata2services.odata.persistence.exception.MissingKeyException
import org.apache.olingo.odata2.api.edm.EdmAnnotationAttribute
import org.apache.olingo.odata2.api.edm.EdmAnnotations
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty
import org.apache.olingo.odata2.api.edm.EdmProperty
import org.apache.olingo.odata2.api.edm.EdmType
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.uri.KeyPredicate
import org.apache.olingo.odata2.core.edm.EdmBoolean
import org.apache.olingo.odata2.core.edm.EdmByte
import org.apache.olingo.odata2.core.edm.EdmDateTime
import org.apache.olingo.odata2.core.edm.EdmDateTimeOffset
import org.apache.olingo.odata2.core.edm.EdmDecimal
import org.apache.olingo.odata2.core.edm.EdmDouble
import org.apache.olingo.odata2.core.edm.EdmGuid
import org.apache.olingo.odata2.core.edm.EdmInt16
import org.apache.olingo.odata2.core.edm.EdmInt32
import org.apache.olingo.odata2.core.edm.EdmInt64
import org.apache.olingo.odata2.core.edm.EdmString
import org.apache.olingo.odata2.core.edm.EdmTime
import org.apache.olingo.odata2.core.edm.provider.EdmAnnotationsImplProv
import org.apache.olingo.odata2.core.edm.provider.EdmSimplePropertyImplProv
import org.assertj.core.util.DateUtil
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.ALIAS_ANNOTATION_ATTR_NAME

@UnitTest
class DefaultIntegrationKeyToODataEntryGeneratorUnitTest extends Specification {
    def PRODUCT_ENTITY_NAME = "Product"
    def PRODUCT_CODE_PROPERTY = "code"

    def integrationKeyGenerator = new DefaultIntegrationKeyToODataEntryGenerator()

    def setup() {
        integrationKeyGenerator.setEncoding("UTF-8")
    }

    @Test
    def "generate for null EdmEntitySet and non-null integrationKey throws exception"() {
        when:
        integrationKeyGenerator.generate(null, "IntegrationKey")

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Cannot calculate ODataEntry for null edm entity set"
    }

    @Test
    def "generate for null EdmEntitySet and single KeyPredicate throws exception"() {
        when:
        integrationKeyGenerator.generate(null, [keyPredicate()])

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Cannot calculate ODataEntry for null edm entity set"
    }

    @Test
    def "generate for null integrationKey throws exception"() {
        when:
        integrationKeyGenerator.generate(Stub(EdmEntitySet), null as String)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Cannot calculate ODataEntry for null integrationKey"
    }

    @Test
    @Unroll
    def "generate for null KeyPredicates throws exception"() {
        when:
        integrationKeyGenerator.generate(Stub(EdmEntitySet), keyPredicates)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Cannot calculate ODataEntry for null or empty key predicates"

        where:
        msg     | keyPredicates
        "null"  | null as List
        "empty" | []
    }

    @Test
    def "generate for KeyPredicate containing non-IntegrationKey property name"() {
        given:
        def entityTypeName = "TestEntityType"
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> entityTypeName
            }
        }
        def keyPredicate = keyPredicate("NOT A VALID NAME")

        when:
        integrationKeyGenerator.generate(entitySet, [keyPredicate])

        then:
        def e = thrown(InvalidIntegrationKeyException)
        e.entityType == entityTypeName
    }

    @Test
    def "generate with more than 1 KeyPredicate throws exception"() {
        given:
        def entityTypeName = "TestEntityType"
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> entityTypeName
            }
        }

        when:
        integrationKeyGenerator.generate(entitySet, [keyPredicate(), keyPredicate()])

        then:
        def e = thrown(InvalidIntegrationKeyException)
        e.entityType == entityTypeName
    }

    @Test
    def "generate for empty integrationKey value"() {
        given:
        def productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME, "")

        when:
        integrationKeyGenerator.generate(productEntitySet, "someCode")

        then:
        def e = thrown(MissingKeyException)
        e.message.contains(PRODUCT_ENTITY_NAME)
    }

    @Test
    def "generate with invalid number of integrationKey values"() {
        given:
        final EdmEntitySet productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME,
                alias(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY))

        when:
        integrationKeyGenerator.generate(productEntitySet, "IntegrationKey1|IntegrationKey2")

        then:
        def e = thrown(InvalidIntegrationKeyException)
        e.message == "The integration key [IntegrationKey1|IntegrationKey2] is invalid. Please consult the IntegrationKey definition of [Product] for configuration details."
    }

    @Test
    @Unroll
    def "generate with single integrationKey value for #msg"() {
        given:
        def propertyName = "somePropertyName"
        final EdmEntitySet productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME, alias(PRODUCT_ENTITY_NAME, propertyName))
        givenPropertyForEntitySet(productEntitySet, propertyName, propertyType)

        when:
        final ODataEntry oDataEntry = integrationKeyGenerator.generate(productEntitySet, keyValue)

        then:
        oDataEntry.getProperties() == [(propertyName): propertyValue, (INTEGRATION_KEY_PROPERTY_NAME): keyValue]

        where:
        msg                 | propertyValue                                           | propertyType            | keyValue
        "EdmString"         | "product|code"                                          | new EdmString()         | "product%7Ccode"
        "EdmBoolean"        | false                                                   | new EdmBoolean()        | "false"
        "EdmDateTime"       | DateUtil.toCalendar(new Date(0))                        | new EdmDateTime()       | "0"
        "EdmDateTimeOffset" | DateUtil.toCalendar(new Date(1))                        | new EdmDateTimeOffset() | "1"
        "EdmTime"           | DateUtil.toCalendar(new Date(2))                        | new EdmTime()           | "2"
        "EdmGuid"           | UUID.fromString("4082356e-c6e4-4098-97ed-369eeb6385fd") | new EdmGuid()           | "4082356e-c6e4-4098-97ed-369eeb6385fd"
        "EdmByte"           | ((byte) 32)                                             | new EdmByte()           | "32"
        "EdmDecimal"        | 1.1D                                                    | new EdmDecimal()        | "1.1"
        "EdmDouble"         | 1.2D                                                    | new EdmDouble()         | "1.2"
        "EdmInt16"          | 16L                                                     | new EdmInt16()          | "16"
        "EdmInt32"          | 32L                                                     | new EdmInt32()          | "32"
        "EdmInt64"          | 64L                                                     | new EdmInt64()          | "64"
    }

    @Test
    def "generate with single integrationKey value in KeyPredicates"() {
        given:
        final EdmEntitySet productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME,
                alias(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY))
        givenPropertiesForEntitySet(productEntitySet, PRODUCT_CODE_PROPERTY)

        final String productCode = "product%7Ccode"
        def keyPredicate = Stub(KeyPredicate) {
            getLiteral() >> productCode
            getProperty() >> Stub(EdmProperty) {
                getName() >> INTEGRATION_KEY_PROPERTY_NAME
            }
        }

        when:
        final ODataEntry oDataEntry = integrationKeyGenerator.generate(productEntitySet, [keyPredicate])

        then:
        oDataEntry.getProperties() == [(PRODUCT_CODE_PROPERTY): "product|code", (INTEGRATION_KEY_PROPERTY_NAME): productCode]
    }

    @Test
    def "generate with invalid encoding does not encode OData properties"() {
        given:
        integrationKeyGenerator.setEncoding("SOME_WEIRD_ENCODING")

        final EdmEntitySet productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME,
                alias(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY))
        givenPropertiesForEntitySet(productEntitySet, PRODUCT_CODE_PROPERTY)

        final String productCode = "product%7Ccode"

        when:
        final ODataEntry oDataEntry = integrationKeyGenerator.generate(productEntitySet, productCode)

        then:
        oDataEntry.getProperties() == [(PRODUCT_CODE_PROPERTY): productCode, (INTEGRATION_KEY_PROPERTY_NAME): productCode]
    }

    @Test
    def "generate when EdmEntitySet is missing key throws exception"() {
        given:
        def entityTypeName = "ExceptionType"
        def entitySet = Stub(EdmEntitySet) {
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> entityTypeName
                getKeyProperties() >> [Stub(EdmSimplePropertyImplProv) {
                    isSimple() >> true
                    getAnnotations() >> Stub(EdmAnnotationsImplProv) {
                        getAnnotationAttributes() >> []
                    }
                }]
            }
        }

        when:
        integrationKeyGenerator.generate(entitySet, "IntegrationKey")

        then:
        def e = thrown(MissingKeyException)
        e.message.contains(entityTypeName)
    }

    @Test
    def "generate for integrationKey with multiple values"() {
        given:
        def productName = "name"
        def productCatalogVersion = "catalogVersion"
        def catalogVersionEntityTypeName = "CatalogVersion"
        def catalogVersionCode2 = "code2"
        def catalogVersionVersion = "version"
        def catalogVersionCatalog = "catalog"
        def catalogEntityTypeName = "Catalog"
        def catalogId = "id"
        final EdmEntitySet productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME,
                alias(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY) + "|" +
                        alias(PRODUCT_ENTITY_NAME, productName) + "|" +
                        alias(catalogVersionEntityTypeName, catalogVersionVersion) + "|" +
                        alias(catalogVersionEntityTypeName, catalogVersionCode2) + "|" +
                        alias(catalogEntityTypeName, catalogId))

        final EdmEntitySet catalogVersionEntitySet = givenEntitySetForTypeWithKey(catalogVersionEntityTypeName,
                alias(catalogVersionEntityTypeName, catalogVersionVersion) + "|" +
                        alias(catalogVersionEntityTypeName, catalogVersionCode2) + "|" +
                        alias(catalogEntityTypeName, catalogId))

        final EdmEntitySet catalogEntitySet = givenEntitySetForTypeWithKey(catalogEntityTypeName,
                alias(catalogEntityTypeName, catalogId))

        givenNavigationPropertyForEntitySet(catalogVersionEntityTypeName, productCatalogVersion, catalogVersionEntitySet, productEntitySet)

        givenNavigationPropertyForEntitySet(catalogEntityTypeName, catalogVersionCatalog, catalogEntitySet, catalogVersionEntitySet)

        givenPropertiesForEntitySet(productEntitySet, PRODUCT_CODE_PROPERTY, productName)
        givenPropertiesForEntitySet(catalogVersionEntitySet, catalogVersionVersion, catalogVersionCode2)
        givenPropertiesForEntitySet(catalogEntitySet, catalogId)

        final String integrationKey = "product%7Ccode|some%7Cname|default%7Cvalue|some%7CcatalogVersion%7ccode2|online%7Cvalue"

        when:
        final ODataEntry oDataEntry = integrationKeyGenerator.generate(productEntitySet, integrationKey)

        then:
        with(oDataEntry) {
            properties.size() == 4
            properties.get(PRODUCT_CODE_PROPERTY) == "product|code"
            properties.get(productName) == "some|name"
            properties.get(INTEGRATION_KEY_PROPERTY_NAME) == integrationKey
            with(properties.get(productCatalogVersion)) {
                properties.size() == 3
                properties.get(catalogVersionVersion) == "default|value"
                properties.get(catalogVersionCode2) == "some|catalogVersion|code2"
                with(properties.get(catalogVersionCatalog)) {
                    properties == [(catalogId): "online|value"]
                }
            }
        }
    }

    @Test
    def "generate for integrationKey with null key navigationProperty"() {
        given:
        def productName = "name"
        def productCatalogVersion = "catalogVersion"
        def catalogVersionEntityTypeName = "CatalogVersion"
        def catalogVersionCode2 = "code2"
        def catalogVersionVersion = "version"
        def catalogVersionCatalog = "catalog"
        def catalogEntityTypeName = "Catalog"
        def catalogId = "id"
        final EdmEntitySet productEntitySet = givenEntitySetForTypeWithKey(PRODUCT_ENTITY_NAME,
                alias(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY) + "|" +
                        alias(PRODUCT_ENTITY_NAME, productName) + "|" +
                        alias(catalogVersionEntityTypeName, catalogVersionVersion) + "|" +
                        alias(catalogVersionEntityTypeName, catalogVersionCode2) + "|" +
                        alias(catalogEntityTypeName, catalogId))

        final EdmEntitySet catalogVersionEntitySet = givenEntitySetForTypeWithKey(catalogVersionEntityTypeName,
                alias(catalogVersionEntityTypeName, catalogVersionVersion) + "|" +
                        alias(catalogVersionEntityTypeName, catalogVersionCode2) + "|" +
                        alias(catalogEntityTypeName, catalogId))

        final EdmEntitySet catalogEntitySet = givenEntitySetForTypeWithKey(catalogEntityTypeName,
                alias(catalogEntityTypeName, catalogId))

        givenNavigationPropertyForEntitySet(catalogVersionEntityTypeName, productCatalogVersion, catalogVersionEntitySet, productEntitySet)

        givenNavigationPropertyForEntitySet(catalogEntityTypeName, catalogVersionCatalog, catalogEntitySet, catalogVersionEntitySet)

        givenPropertiesForEntitySet(productEntitySet, PRODUCT_CODE_PROPERTY, productName)
        givenPropertiesForEntitySet(catalogVersionEntitySet, catalogVersionVersion, catalogVersionCode2)
        givenPropertiesForEntitySet(catalogEntitySet, catalogId)

        and: "integrationKey includes a null Product.catalogVersion"
        final String integrationKey = "product%7Ccode|some%7Cname|null|null|null"

        when:
        final ODataEntry oDataEntry = integrationKeyGenerator.generate(productEntitySet, integrationKey)

        then:
        oDataEntry.getProperties() == [(PRODUCT_CODE_PROPERTY): "product|code", (productName): "some|name", (INTEGRATION_KEY_PROPERTY_NAME): integrationKey]
    }

    def givenNavigationPropertyForEntitySet(final String navigationPropertyToRoleType, final String navigationPropertyName, final EdmEntitySet relatedEntitySet, final EdmEntitySet entitySet) {
        def edmNavigationProperty = Stub(EdmNavigationProperty) {
            getToRole() >> navigationPropertyToRoleType
            getName() >> navigationPropertyName
            getAnnotations() >> Stub(EdmAnnotations) {
                getAnnotationAttributes() >> [Stub(EdmAnnotationAttribute) {
                    getName() >> "s:IsUnique"
                    getText() >> "true"
                }]
            }
        }
        entitySet.getEntityType().getNavigationPropertyNames() >> [navigationPropertyName]
        entitySet.getEntityType().getProperty(navigationPropertyName) >> edmNavigationProperty
        entitySet.getRelatedEntitySet(edmNavigationProperty) >> relatedEntitySet
    }

    def givenPropertiesForEntitySet(final EdmEntitySet entitySet, final String... propertyNames) {
        propertyNames.each {
            entitySet.getEntityType().getProperty(it as String) >> Stub(EdmProperty) {
                getName() >> it
                getType() >> Stub(EdmType)
            }
        }

        entitySet.getEntityType().getPropertyNames() >> propertyNames.toList()
    }

    def givenPropertyForEntitySet(final EdmEntitySet entitySet, final String propertyName, final Object propertyValue) {
        entitySet.getEntityType().getProperty(propertyName) >> Stub(EdmProperty) {
            getName() >> propertyName
            getType() >> propertyValue
        }

        entitySet.getEntityType().getPropertyNames() >> [propertyName]
    }

    def alias(final String entityName, final String propertyName) {
        return entityName + "_" + propertyName
    }

    def givenEntitySetForTypeWithKey(final String typeName, final String aliasString) {
        Stub(EdmEntitySet) {
            getEntityType() >> Stub(EdmEntityType) {
                getName() >> typeName
                getKeyProperties() >> [givenKeyPropertyWithAliasStringExists(aliasString)]
            }
        }
    }

    def givenKeyPropertyWithAliasStringExists(final String aliasString) {
        Stub(EdmSimplePropertyImplProv) {
            isSimple() >> true
            getAnnotations() >> Stub(EdmAnnotations) {
                getAnnotationAttributes() >> [
                        Stub(AnnotationAttribute) {
                            getName() >> ALIAS_ANNOTATION_ATTR_NAME
                            getText() >> aliasString
                        }
                ]
            }
        }
    }

    def keyPredicate(final String name = INTEGRATION_KEY_PROPERTY_NAME) {
        Stub(KeyPredicate) {
            getProperty() >> Stub(EdmProperty) {
                getName() >> name
            }
        }
    }
}
