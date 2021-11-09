/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.falseIfNull

@UnitTest
class StandardAttributeValueGetterUnitTest extends Specification {

    private static final def SPANISH = new Locale("es")

    def attributeDescriptor = attributeDescriptor([readable: true, isinstance: true])
    def modelService = Mock ModelService
    def attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor, modelService)

    @Test
    @Unroll
    def "throws exception when instantiated with null #paramName"() {
        when:
        new StandardAttributeValueGetter(descriptor, service)

        then:
        def e = thrown IllegalArgumentException
        e.message.toLowerCase().contains paramName

        where:
        paramName              | descriptor                    | service
        'attribute descriptor' | null                          | Stub(ModelService)
        'model service'        | Stub(TypeAttributeDescriptor) | null
    }

    @Test
    def 'uses attribute qualifier when getting value from the model service'() {
        given: 'attribute name differs from attribute qualifier'
        attributeDescriptor.getAttributeName() >> 'name'
        attributeDescriptor.getQualifier() >> 'qualifier'
        and: 'an item model to get value for'
        def item = Stub ItemModel
        and: 'the model service returns different values for attribute name and qualifier'
        modelService.getAttributeValue(item, attributeDescriptor.attributeName) >> 'name value'
        modelService.getAttributeValue(item, attributeDescriptor.qualifier) >> 'qualifier value'

        expect:
        attributeValueGetter.getValue(item) == 'qualifier value'
    }

    @Test
    @Unroll
    def "uses attribute qualifier when getting #locale locale value from the model service"() {
        given: 'attribute name differs from attribute qualifier'
        attributeDescriptor.getAttributeName() >> 'name'
        attributeDescriptor.getQualifier() >> 'qualifier'
        and: 'an item model to get value for'
        def item = Stub ItemModel
        and: 'the model service returns different values for attribute name and qualifier'
        modelService.getAttributeValue(item, attributeDescriptor.attributeName, locale) >> 'name value'
        modelService.getAttributeValue(item, attributeDescriptor.qualifier, locale) >> 'qualifier value'

        expect:
        attributeValueGetter.getValue(item, locale) == 'qualifier value'

        where:
        locale << [Locale.ENGLISH, null]
    }

    @Test
    def "uses attribute qualifiers when getting multiple locales values from the model service"() {
        given: 'attribute name differs from attribute qualifier'
        attributeDescriptor.getAttributeName() >> 'name'
        attributeDescriptor.getQualifier() >> 'qualifier'
        and: 'an item model to get value for'
        def item = Stub ItemModel
        and: 'the model service returns different values for attribute name and qualifier'
        modelService.getAttributeValues(item, attributeDescriptor.attributeName, Locale.ENGLISH, SPANISH) >> nameValues
        modelService.getAttributeValues(item, attributeDescriptor.qualifier, Locale.ENGLISH, SPANISH) >> qualifierValues

        def values = attributeValueGetter.getValues(item, Locale.ENGLISH, SPANISH)
        expect:
        values == qualifierValues

        where:
        nameValues                                                      | qualifierValues
        [(Locale.ENGLISH): 'name value', (SPANISH): 'valor del nombre'] | [(Locale.ENGLISH): 'qualifier value', (SPANISH): 'valor del calificador']
    }

    @Test
    def 'returns null value for a null item model'() {
        when:
        def value = attributeValueGetter.getValue null

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def 'returns null localized value for a null item model'() {
        when:
        def value = attributeValueGetter.getValue null, Locale.ENGLISH

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def 'returns null localized values for a null item model'() {
        when:
        def value = attributeValueGetter.getValues null, Locale.ENGLISH, SPANISH

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def "trying to read a non-readable attribute should return null"() {
        given:
        attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor([readable: false, isinstance: true]), modelService)

        when:
        def value = attributeValueGetter.getValue Stub(ItemModel)

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def "trying to read a non-readable, localized attribute should return null"() {
        given:
        attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor([readable: false, isinstance: true]), modelService)

        when:
        def value = attributeValueGetter.getValue(Stub(ItemModel), SPANISH)

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def "trying to read a non-readable, localized attribute values should return null"() {
        given:
        attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor([readable: false, isinstance: true]), modelService)

        when:
        def value = attributeValueGetter.getValues(Stub(ItemModel), SPANISH, Locale.ENGLISH)

        then:
        0 * modelService.getAttributeValues(*_)
        value == [:]
    }

    @Test
    def "trying to read a readable attribute should return expected value"() {
        given: 'an item'
        def item = Stub ItemModel
        and: 'the attribute value for the item'
        def value = new Object()

        when:
        modelService.getAttributeValue(item, _ as String) >> value

        then:
        attributeValueGetter.getValue(item).is value
    }

    @Test
    def "trying to read a localized readable attribute should return expected value"() {
        given:
        modelService.getAttributeValue(_, _ as String, SPANISH) >> Stub(Object)

        when:
        def value = attributeValueGetter.getValue(Stub(ItemModel), SPANISH)

        then:
        value
    }

    @Test
    def "trying to read localized readable attribute values should return expected values"() {
        given:
        def expected = [(SPANISH): 'partido']
        and:
        modelService.getAttributeValues(_, _ as String, SPANISH, Locale.ENGLISH) >> expected

        when:
        def value = attributeValueGetter.getValues(Stub(ItemModel), SPANISH, Locale.ENGLISH)

        then:
        value == expected
    }

    @Test
    def "trying to read an attribute not present on model should return null"() {
        given: "the model is not an instance of the type attribute descriptor"
        attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor([readable: true, isinstance: false]), modelService)
        and: "the type attribute descriptor qualifier does not exist on the model"
        attributeDescriptor.getQualifier() >> ''
        def model = Stub(Object) {
            getClass() >> ItemModel.class
        }

        when:
        def value = attributeValueGetter.getValue model

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def "trying to read a localized attribute not present on model should return null"() {
        given: "the model is not an instance of the type attribute descriptor"
        attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor([readable: true, isinstance: false]), modelService)
        and: "the type attribute descriptor qualifier does not exist on the model"
        attributeDescriptor.getQualifier() >> ''
        def model = Stub(Object) {
            getClass() >> ItemModel.class
        }

        when:
        def value = attributeValueGetter.getValue(model, SPANISH)

        then:
        0 * modelService.getAttributeValue(*_)
        !value
    }

    @Test
    def "trying to read localized attribute values for an attribute not present on model should return null"() {
        given: "the model is not an instance of the type attribute descriptor"
        attributeValueGetter = new StandardAttributeValueGetter(attributeDescriptor([readable: true, isinstance: false]), modelService)
        and: "the type attribute descriptor qualifier does not exist on the model"
        attributeDescriptor.getQualifier() >> ''
        def model = Stub(Object) {
            getClass() >> ItemModel.class
        }

        when:
        def value = attributeValueGetter.getValues(model, SPANISH, Locale.ENGLISH)

        then:
        0 * modelService.getAttributeValues(*_)
        value == [:]
    }

    private TypeAttributeDescriptor attributeDescriptor(final Map<String, Boolean> params) {
        Stub(TypeAttributeDescriptor) {
            isReadable() >> params['readable']
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                isInstance(_) >> { falseIfNull(params['isinstance']) }
            }
        }
    }
}
