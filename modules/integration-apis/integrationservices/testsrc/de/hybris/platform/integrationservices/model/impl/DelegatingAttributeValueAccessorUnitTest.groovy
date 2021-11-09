/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.AttributeValueGetter
import de.hybris.platform.integrationservices.model.AttributeValueSetter
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DelegatingAttributeValueAccessorUnitTest extends Specification {
    private static final ItemModel ITEM = new ItemModel()
    private static final Locale LOCALE = Locale.CANADA
    private static final VALUE = new Object()

    def getter = Stub(AttributeValueGetter) {
        getValue(ITEM) >> VALUE
        getValue(ITEM, LOCALE) >> VALUE
    }
    def setter = Mock AttributeValueSetter
    def accessor = new DelegatingAttributeValueAccessor(getter, setter)

    @Test
    @Unroll
    def "cannot be instantiated with null #param"() {
        when:
        new DelegatingAttributeValueAccessor(get, set)

        then:
        def e = thrown IllegalArgumentException
        e.message.contains param

        where:
        param    | get                        | set
        'getter' | null                       | Stub(AttributeValueSetter)
        'setter' | Stub(AttributeValueGetter) | null
    }

    @Test
    def 'delegates getValue(ItemModel) to getter'() {
        expect:
        accessor.getValue(ITEM).is VALUE
    }

    @Test
    def 'delegates getValue(ItemModel, Locale) to getter'() {
        expect:
        accessor.getValue(ITEM, LOCALE) == VALUE
    }

    @Test
    def 'delegates getValues(ItemModel, Locale...) to getter'() {
        given:
        def values = [:]
        getter.getValues(ITEM, Locale.CANADA, Locale.CHINA) >> values

        expect:
        accessor.getValues(ITEM, Locale.CANADA, Locale.CHINA).is values
    }

    @Test
    def 'delegates setValue(ItemModel, Object) to setter'() {
        when:
        accessor.setValue(ITEM, VALUE)

        then:
        1 * setter.setValue(ITEM, VALUE)
    }
}
