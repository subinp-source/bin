/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.integrationkey

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.KeyAttribute
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class KeyValueUnitTest extends Specification {
    def valueBuilder = new KeyValue.Builder()
            .withValue(attribute('string'), 'value')
            .withValue(attribute('integer'), 100)
    def keyValue = valueBuilder.build()

    @Test
    def "empty key value does not have attribute values"() {
        expect:
        new KeyValue().keyAttributeValues?.isEmpty()
    }

    @Test
    def "can read attribute values the key value consists of"() {
        expect:
        keyValue?.keyAttributeValues?.size() == 2
        keyValue.keyAttributeValues.contains new KeyAttributeValue(attribute('string'), 'value')
        keyValue.keyAttributeValues.contains new KeyAttributeValue(attribute('integer'), 100)
    }

    @Test
    def "attribute values with null attribute are excluded from the key value"() {
        given:
        def keyValue = new KeyValue([
                new KeyAttributeValue(attribute('attribute1'), 'valid'),
                null,
                new KeyAttributeValue(attribute('attribute2'), 'valid')])

        expect:
        keyValue?.getKeyAttributeValues()?.size() == 2
        keyValue.keyAttributeValues.contains new KeyAttributeValue(attribute('attribute1'), 'valid')
        keyValue.keyAttributeValues.contains new KeyAttributeValue(attribute('attribute2'), 'valid')
    }

    @Test
    @Unroll
    def "key attribute values are immutable for an #desc key"() {
        when:
        value.keyAttributeValues.clear()

        then:
        thrown(UnsupportedOperationException)

        where:
        desc        | value
        'empty'     | new KeyValue()
        'not empty' | new KeyValue([Stub(KeyAttributeValue)])
    }

    @Test
    @Unroll
    def "is not equal when the other object #condition"() {
        expect:
        keyValue != other

        where:
        condition | other
        'has different number of attributes' | new KeyValue.Builder().build()
        'has at least one different attribute' | new KeyValue.Builder()
                .withValue(attribute('string'), 'value')
                .withValue(attribute('long'), 100)
                .build()
        'has at least one different attribute value' | new KeyValue.Builder()
                .withValue(attribute('string'), 'value')
                .withValue(attribute('integer'), 1)
                .build()
        'is not a KeyValue' | [string: 'value', integer: 100]
        'is null' | null
    }

    @Test
    def "equals when another KeyValue has same attributes with same values"() {
        expect:
        keyValue == new KeyValue.Builder()
                .withValue(attribute('string'), 'value')
                .withValue(attribute('integer'), 100)
                .build()
    }

    @Test
    @Unroll
    def "hashCode is not equal when the other object #condition"() {
        expect:
        keyValue.hashCode() != other.hashCode()

        where:
        condition | other
        'has different number of attributes' | new KeyValue.Builder().build()
        'has at least one different attribute' | new KeyValue.Builder()
                .withValue(attribute('string'), 'value')
                .withValue(attribute('long'), 100)
                .build()
        'has at least one different attribute value' | new KeyValue.Builder()
                .withValue(attribute('string'), 'value')
                .withValue(attribute('integer'), 1)
                .build()
    }

    @Test
    def "hashCode equals when another KeyValue has same attributes with same values"() {
        expect:
        keyValue.hashCode() == new KeyValue.Builder()
                .withValue(attribute('string'), 'value')
                .withValue(attribute('integer'), 100)
                .build()
                .hashCode()
    }

    def attribute(final String name) {
        def attr = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> name
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getIntegrationObject() >> Stub(IntegrationObjectModel)
            }
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getLocalized() >> false
            }
        }
        new KeyAttribute(attr)
    }
}
