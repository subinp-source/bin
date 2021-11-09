/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema.entity

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.MapDescriptor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind
import org.apache.olingo.odata2.api.edm.provider.EntityType
import org.apache.olingo.odata2.api.edm.provider.Key
import org.apache.olingo.odata2.api.edm.provider.Property
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class MapEntryEntityTypeGeneratorUnitTest extends Specification {
    private static final def ITEM_MODEL = new IntegrationObjectItemModel()

    def itemType = Stub TypeDescriptor
    def factory = Stub(DescriptorFactory) {
        createItemTypeDescriptor(ITEM_MODEL) >> itemType
    }
    SchemaElementGenerator<Optional<Key>, List<Property>> keyGenerator = Stub {
        generate(_ as List<Property>) >> Optional.empty()
    }
    def generator = new MapEntryEntityTypeGenerator(descriptorFactory: factory, keyGenerator: keyGenerator)

    @Test
    def 'generates empty entity list when item does not have map attributes'() {
        given:
        itemType.getAttributes() >> [nonMapAttribute()]

        expect:
        generator.generate(ITEM_MODEL).empty
    }

    @Test
    def 'generates empty entity list when attribute is localized'() {
        given:
        itemType.getAttributes() >> [localizedAttribute()]

        expect:
        generator.generate(ITEM_MODEL).empty
    }

    @Test
    def 'generates entity type for a map attribute'() {
        given:
        itemType.getAttributes() >> [mapAttribute('SomeMapType')]

        when:
        def items = generator.generate(ITEM_MODEL)

        then: 'EntityType is generated'
        items.size() == 1
        and: 'the generated EntityType has expected name'
        def entity = items.first()
        entity.name == 'SomeMapType'
        and: 'the generated EntityType has two properties'
        entity.properties.size() == 2
    }

    @Test
    def 'generated entity type contains correct key property'() {
        given:
        itemType.getAttributes() >> [mapAttribute('SomeMapType')]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        def property = findProperty(items.first(), 'key')
        property.annotationAttributes.collect({ it.name }) == ['Nullable', 's:IsUnique']
        property.annotationAttributes.collect({ it.text }) == ['false', 'true']
    }

    @Test
    @Unroll
    def "generated key property correctly presents #platformType in EDM"() {
        given: "attribute map key has #platformType type"
        def mapDescriptor = mapDescriptor(platformType)
        itemType.getAttributes() >> [mapAttribute(mapDescriptor)]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        def property = findProperty(items.first(), 'key')
        property.type == edmType

        where:
        platformType           | edmType
        'java.math.BigDecimal' | EdmSimpleTypeKind.Decimal
        'java.math.BigInteger' | EdmSimpleTypeKind.String
        'java.lang.Boolean'    | EdmSimpleTypeKind.Boolean
        'java.lang.Byte'       | EdmSimpleTypeKind.SByte
        'java.lang.Character'  | EdmSimpleTypeKind.String
        'java.util.Date'       | EdmSimpleTypeKind.DateTime
        'java.lang.Double'     | EdmSimpleTypeKind.Double
        'java.lang.Float'      | EdmSimpleTypeKind.Single
        'java.lang.Integer'    | EdmSimpleTypeKind.Int32
        'java.lang.Long'       | EdmSimpleTypeKind.Int64
        'java.lang.Short'      | EdmSimpleTypeKind.Int16
        'java.lang.String'     | EdmSimpleTypeKind.String
    }

    @Test
    def 'exception is thrown when map attribute key type is not a known primitive'() {
        given:
        def mapDescriptor = mapDescriptor('UnknownType', 'java.lang.String')
        itemType.getAttributes() >> [mapAttribute(mapDescriptor)]

        when:
        generator.generate(ITEM_MODEL)

        then:
        thrown IllegalArgumentException
    }

    @Test
    def 'generated entity type contains correct value property'() {
        given:
        itemType.getAttributes() >> [mapAttribute('SomeMapType')]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        def property = findProperty(items.first(), 'value')
        property.annotationAttributes.collect({ it.name }) == ['Nullable']
        property.annotationAttributes.collect({ it.text }) == ['true']
    }

    @Test
    @Unroll
    def "generated value property correctly presents #platformType in EDM"() {
        given: "attribute map key has #platformType type"
        def mapDescriptor = mapDescriptor('java.lang.String', platformType)
        itemType.getAttributes() >> [mapAttribute(mapDescriptor)]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        def property = findProperty(items.first(), 'value')
        property.type == edmType

        where:
        platformType           | edmType
        'java.math.BigDecimal' | EdmSimpleTypeKind.Decimal
        'java.math.BigInteger' | EdmSimpleTypeKind.String
        'java.lang.Boolean'    | EdmSimpleTypeKind.Boolean
        'java.lang.Byte'       | EdmSimpleTypeKind.SByte
        'java.lang.Character'  | EdmSimpleTypeKind.String
        'java.util.Date'       | EdmSimpleTypeKind.DateTime
        'java.lang.Double'     | EdmSimpleTypeKind.Double
        'java.lang.Float'      | EdmSimpleTypeKind.Single
        'java.lang.Integer'    | EdmSimpleTypeKind.Int32
        'java.lang.Long'       | EdmSimpleTypeKind.Int64
        'java.lang.Short'      | EdmSimpleTypeKind.Int16
        'java.lang.String'     | EdmSimpleTypeKind.String
    }

    @Test
    def 'exception is thrown when map attribute value type is not a known primitive'() {
        given:
        def mapDescriptor = mapDescriptor('java.lang.String', 'UnknownType')
        itemType.getAttributes() >> [mapAttribute(mapDescriptor)]

        when:
        generator.generate(ITEM_MODEL)

        then:
        thrown IllegalArgumentException
    }

    @Test
    def 'generated entity type contains correct key produced by the key generator'() {
        given: 'key generator produces the key'
        def key = new Key()
        generator.keyGenerator = Stub(SchemaElementGenerator) {
            generate(_ as List<Property>) >> Optional.of(key)
        }
        and: 'the item has a map attribute'
        itemType.getAttributes() >> [mapAttribute()]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        items.first().key.is key
    }

    @Test
    def 'generated entity type has null key when generator did not produce a key'() {
        given: 'key generator produces the key'
        generator.keyGenerator = Stub(SchemaElementGenerator) {
            generate(_ as List<Property>) >> Optional.empty()
        }
        and: 'the item has a map attribute'
        itemType.getAttributes() >> [mapAttribute()]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        !items.first().key
    }

    @Test
    def 'generated entity type has null key when generator is not injected'() {
        given: 'generator does not have a key generator injected'
        generator.keyGenerator = null
        and: 'the item has a map attribute'
        itemType.getAttributes() >> [mapAttribute()]

        when:
        def items = generator.generate(ITEM_MODEL)

        then:
        !items.first().key
    }

    @Test
    def 'generates empty entity list when descriptor factory is not injected'() {
        given: 'generator does not have a descriptor factory injected'
        generator.descriptorFactory = null

        expect:
        generator.generate(ITEM_MODEL).empty
    }

    TypeAttributeDescriptor mapAttribute(String mapTypeCode = 'Map') {
        mapAttribute(mapDescriptor(), mapTypeCode)
    }

    MapDescriptor mapDescriptor(String keyType='java.lang.String', String valueType='java.lang.String') {
        Stub(MapDescriptor) {
            getKeyType() >> primitiveTypeDescriptor(keyType)
            getValueType() >> primitiveTypeDescriptor(valueType)
        }
    }

    TypeAttributeDescriptor mapAttribute(MapDescriptor mapType, String mapTypeCode = 'Map') {
        Stub(TypeAttributeDescriptor) {
            isMap() >> true
            getMapDescriptor() >> Optional.of(mapType)
            getAttributeType() >> typeDescriptor(mapTypeCode)
        }
    }

    TypeDescriptor primitiveTypeDescriptor(String code) {
        Stub(TypeDescriptor) {
            getTypeCode() >> code
            isPrimitive() >> true
        }
    }

    TypeDescriptor typeDescriptor(String code) {
        Stub(TypeDescriptor) {
            getTypeCode() >> code
        }
    }

    TypeAttributeDescriptor nonMapAttribute() {
        Stub(TypeAttributeDescriptor) {
            isMap() >> false
        }
    }

    TypeAttributeDescriptor localizedAttribute() {
        Stub(TypeAttributeDescriptor) {
            isMap() >> true
            isLocalized() >> true
        }
    }

    TypeDescriptor nonPrimitiveTypeDescriptor() {
        Stub(TypeDescriptor) {
            isPrimitive() >> false
        }
    }

    SimpleProperty findProperty(EntityType entity, String name) {
        entity.properties.find { it.name == name } as SimpleProperty
    }
}
