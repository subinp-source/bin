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

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.integrationservices.exception.CircularKeyReferenceException
import de.hybris.platform.integrationservices.integrationkey.KeyValue
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ItemKeyDescriptorUnitTest extends Specification {
    private static final String ITEM_CODE = 'SomeItemCode'

    @Test
    def "cannot be instantiated with null integration object item"() {
        when:
        ItemKeyDescriptor.create(null)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    @Unroll
    def "calculates empty key when item has no #desc"() {
        given:
        def descriptor = ItemKeyDescriptor.create(model)

        expect:
        descriptor.calculateKey([seq: 1, item: [code: 'abc']]) == new KeyValue()

        where:
        desc             | model
        'attributes'     | item()
        'key attributes' | item([nonUniqueAttribute('seq'), nonUniqueAttribute('item', item())])
    }

    @Test
    def "calculates key when item has multiple simple key attributes only"() {
        given:
        def itemIO = item('Item', [uniqueAttribute('key1'), uniqueAttribute('key2'), nonUniqueAttribute('qty')])
        def descriptor = ItemKeyDescriptor.create(itemIO)

        expect:
        descriptor.calculateKey([key1: 1, key2: 2, qty: 3]) == new KeyValue.Builder()
                .withValue(findAttribute(itemIO, 'key1'), 1)
                .withValue(findAttribute(itemIO, 'key2'), 2)
                .build()
    }

    @Test
    def "calculates key when item has multiple reference key attributes only"() {
        given:
        def companyIO = item 'Company', [uniqueAttribute('name')]
        def positionIO = item 'Position', [uniqueAttribute('code')]
        def descriptor = ItemKeyDescriptor.create(item([
                uniqueAttribute('company', companyIO),
                uniqueAttribute('position', positionIO),
                nonUniqueAttribute('description')]))
        def dataItem = [description: 'Good Job', company: [name: 'SAP'], position: [code: 'dev123']]

        expect:
        descriptor.calculateKey(dataItem) == new KeyValue.Builder()
                .withValue(findAttribute(companyIO, 'name'), 'SAP')
                .withValue(findAttribute(positionIO, 'code'), 'dev123')
                .build()
    }

    @Test
    def "calculate key when reference key attribute is null"() {
        given:
        def companyIO = item 'Company', [uniqueAttribute('name')]
        def descriptor = ItemKeyDescriptor.create(item([
                uniqueAttribute('company', companyIO),
                nonUniqueAttribute('description')]))
        def dataItem = [description: 'Good Job', company: null, position: null]

        expect:
        descriptor.calculateKey(dataItem) == new KeyValue.Builder()
                .withValue(findAttribute(companyIO, 'name'), null)
                .build()
    }

    @Test
    def "calculate key when reference key attribute with nested simple and reference key attributes is null"() {
        given:
        def nestedCompanyRefKeyIO = item('A', [uniqueAttribute('innerId')])
        def companyIO = item 'Company', [uniqueAttribute('name'), uniqueAttribute('loopback', nestedCompanyRefKeyIO)]
        def descriptor = ItemKeyDescriptor.create(item([
                uniqueAttribute('company', companyIO),
                nonUniqueAttribute('description')]))
        def dataItem = [description: 'Good Job', company: null]

        expect:
        descriptor.calculateKey(dataItem) == new KeyValue.Builder()
                .withValue(findAttribute(companyIO, 'name'), null)
                .withValue(findAttribute(nestedCompanyRefKeyIO, 'innerId'), null)
                .build()
    }

    @Test
    def 'calculates key when item key attribute refers another item with multiple simple key attributes'() {
        given:
        def ioAddress = item 'Address', [uniqueAttribute('name'), uniqueAttribute('zipCode')]
        def descriptor = ItemKeyDescriptor.create(item([uniqueAttribute('city', ioAddress), nonUniqueAttribute('description')]))
        def dataItem = [description: 'Good Place', city: [name: 'Boulder', zipCode: '80306']]

        expect:
        descriptor.calculateKey(dataItem) == new KeyValue.Builder()
                .withValue(findAttribute(ioAddress, 'name'), 'Boulder')
                .withValue(findAttribute(ioAddress, 'zipCode'), '80306')
                .build()
    }

    @Test
    def "calculates key when item has simple and reference key attributes"() {
        given:
        def catalogIO = item 'Catalog', [uniqueAttribute('id')]
        def catalogVersionIO = item 'CatalogVersion', [uniqueAttribute('version'), uniqueAttribute('catalog', catalogIO)]
        def productIO = item('Product', [uniqueAttribute('code'),
                              uniqueAttribute('catalogVersion', catalogVersionIO),
                              nonUniqueAttribute('name')])
        def descriptor = ItemKeyDescriptor.create(productIO)
        def dataItem = [code: 'XYZ', name: 'Good Product', catalogVersion: [version: 'Staged', catalog: [id: 'Default']]]

        expect:
        descriptor.calculateKey(dataItem) == new KeyValue.Builder()
                .withValue(findAttribute(productIO, 'code'), 'XYZ')
                .withValue(findAttribute(catalogVersionIO, 'version'), 'Staged')
                .withValue(findAttribute(catalogIO, 'id'), 'Default')
                .build()
    }

    @Test
    def "calculates key when item references itself in a non-key attributes"() {
        given:
        def itemIO = item 'SomeItem', [uniqueAttribute('code'), nonUniqueAttribute('otherItem', item('SomeItem'))]
        def descriptor = ItemKeyDescriptor.create(itemIO)

        expect:
        descriptor.calculateKey([code: 1, otherItem: [code: 1]]) == new KeyValue.Builder()
                .withValue(findAttribute(itemIO, 'code'), 1)
                .build()
    }

    @Test
    @Unroll
    def "throws CircularKeyReferenceException when key attribute reference chain is #condition"() {
        given:
        def validKey = uniqueAttribute 'code'

        when:
        ItemKeyDescriptor.create(item('A', [validKey, invalidKey]))

        then:
        def e = thrown CircularKeyReferenceException
        with(e) {
            integrationItemCode == type
            attributeName == 'loopback'
            referencedType == refType
            message.contains "${type}.loopback"
            message.contains refType
        }

        where:
        condition               | type | refType | invalidKey
        'A -> A'                | 'A'  | 'A'     | uniqueAttribute('loopback', item('A'))
        'A -> B -> B'           | 'B'  | 'B'     | uniqueAttribute('b', item('B', [uniqueAttribute('loopback', item('B'))]))
        'A -> B -> A'           | 'B'  | 'A'     | uniqueAttribute('b', item('B', [uniqueAttribute('loopback', item('A'))]))
        'A -> B -> C -> B'      | 'C'  | 'B'     | uniqueAttribute('b', item('B', [uniqueAttribute('c', item('C', [uniqueAttribute('loopback', item('B'))]))]))
        'A -> B -> C -> A'      | 'C'  | 'A'     | uniqueAttribute('b',
                                                         item('B', [uniqueAttribute('c',
                                                                 item('C', [uniqueAttribute('loopback',
                                                                         item('A'))]))]))
        'A -> B -> C -> D -> B' | 'D'  | 'B'     | uniqueAttribute('b',
                                                         item('B', [uniqueAttribute('c',
                                                                item('C', [uniqueAttribute('d',
                                                                        item('D', [uniqueAttribute('loopback',
                                                                                item('B'))]))]))]))
    }

    @Test
    def 'descriptor can be created when same type appears in different key attribute references and does not form a loop'() {
        when:
        ItemKeyDescriptor.create(item('TypeA', [
                uniqueAttribute('ref1', item('TypeB', [uniqueAttribute('id')])),
                uniqueAttribute('ref2', item('TypeB', [uniqueAttribute('id')]))]))

        then:
        notThrown CircularKeyReferenceException
    }

    @Test
    @Unroll
    def "isKeyAttribute return #res for #attr"() {
        given:
        def desc = ItemKeyDescriptor.create(item(
                [uniqueAttribute('key'), uniqueAttribute('nestedItemKey', item('NestedItem')), nonUniqueAttribute('non-key')]))

        expect:
        desc.isKeyAttribute(attr) == res

        where:
        attr            | res
        'key'           | true
        'nestedItemKey' | true
        'non-key'       | false
        ''              | false
        null            | false
    }

    def item(List<IntegrationObjectItemAttributeModel> attributes) {
        item(ITEM_CODE, attributes)
    }

    def item(String type = 'DoesNotMatter', List<IntegrationObjectItemAttributeModel> attributes = []) {
        def item = Stub(IntegrationObjectItemModel) {
            getCode() >> type
            getIntegrationObject() >> Stub(IntegrationObjectModel)
        }
        def attachedToItemAttributes = attributes.collect { attribute(item, it) }
        item.getAttributes() >> attachedToItemAttributes
        item.getKeyAttributes() >> attachedToItemAttributes.findAll { it.unique }
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
            getAttributeDescriptor() >>  attributeDescriptor()
        }
    }

    def attribute(IntegrationObjectItemModel item, IntegrationObjectItemAttributeModel attr) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> attr.attributeName
            getUnique() >> attr.unique
            getIntegrationObjectItem() >> item
            getReturnIntegrationObjectItem() >> attr.returnIntegrationObjectItem
            getAttributeDescriptor() >> attributeDescriptor()
        }
    }

    /**
     * Used to lookup attributes in the specified item model because an exact match of the attribute is need for correct
     * {@code equals()} behavior
     * @param item an item containing the attribute
     * @param attrName name of the attribute to find
     * @return a matching attribute model or {@code null}, if such attribute is not found
     */
    def findAttribute(IntegrationObjectItemModel item, String attrName) {
        item.attributes.find { it.attributeName == attrName }
    }

    /**
     * @return Valid key attribute descriptor.
     */
    def attributeDescriptor() {
        Stub(AttributeDescriptorModel) {
            getLocalized() >> false
        }
    }
}
