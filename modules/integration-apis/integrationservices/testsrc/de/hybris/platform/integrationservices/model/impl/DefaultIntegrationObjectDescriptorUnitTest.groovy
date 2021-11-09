/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultIntegrationObjectDescriptorUnitTest extends Specification {
    @Shared
    def descriptorFactory = Stub(DescriptorFactory) {
        createItemTypeDescriptor(_) >> { args  -> new ItemTypeDescriptor(args[0] as IntegrationObjectItemModel) }
    }

    @Test
    def "integration object model is required to create a descriptor"() {
        when:
        DefaultIntegrationObjectDescriptor.create null

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "creates integration object descriptor from a model"() {
        expect:
        def object = DefaultIntegrationObjectDescriptor.create Stub(IntegrationObjectModel)
        object.factory
    }

    @Test
    def 'creates integration object descriptor from a model and factory'() {
        expect:
        def object = new DefaultIntegrationObjectDescriptor(ioModel('MyObject'), descriptorFactory)
        object.code == 'MyObject'
        object.factory.is descriptorFactory
    }

    @Test
    @Unroll
    def "retrieves type descriptor for an item when it has an IO item defined for its #condition"() {
        given: 'a product item model, which has ApparelProduct and ElectronicsProduct subtypes'
        def productModel = ioItemModel('Product', ['ApparelProduct', 'ElectronicsProduct'])
        and: 'an apparel item model, which has not subtypes in the type system'
        def apparelProductModel = ioItemModel('ApparelProduct')
        and: 'an IO with a Product and ApparelProduct items'
        def model = ioModel([productModel, apparelProductModel])
        and: 'an IO descriptor for the model'
        def object = new DefaultIntegrationObjectDescriptor(model, descriptorFactory)

        expect:
        object.getItemTypeDescriptor(item).map({it.itemCode}).orElse(null) == expectedType

        where:
        condition                 | item                            | expectedType
        'type'                    | itemModel('Product')            | 'Product'
        'type and its super type' | itemModel('ApparelProduct')     | 'ApparelProduct'
        'super type'              | itemModel('ElectronicsProduct') | 'Product'
    }

    @Test
    def "retrieves empty type descriptor for an item that has no IO item defined for its type"() {
        given: "integration object with a Product item"
        def object = new DefaultIntegrationObjectDescriptor(ioModel([ioItemModel('Product')]), descriptorFactory)

        expect: 'empty type descriptor for a type that is not in the IO model'
        object.getItemTypeDescriptor(itemModel('Category')).empty
    }

    @Test
    def "retrieves root item type descriptor when integration object has a root item defined"() {
        given:
        def ioModel = ioModel([ioRootItemModel('Product'), ioItemModel('Unit')])
        def io = new DefaultIntegrationObjectDescriptor(ioModel, descriptorFactory)

        when:
        def rootType = io.getRootItemType()

        then:
        rootType.map({it.itemCode}).orElse(null) == 'Product'
    }

    @Test
    @Unroll
    def "retrieves empty root item type descriptor when integration object has no #condition"() {
        expect:
        !io.getRootItemType().present

        where:
        condition         | io
        'root item types' | new DefaultIntegrationObjectDescriptor(ioModel([ioItemModel('NotRoot')]), descriptorFactory)
        'item types'      | new DefaultIntegrationObjectDescriptor(ioModel(), descriptorFactory)
    }

    @Test
    @Unroll
    def "getItemTypeDescriptors() returns #condition defined in the integration object"() {
        given:
        def io = new DefaultIntegrationObjectDescriptor(ioModel(itemModels), descriptorFactory)

        expect:
        io.getItemTypeDescriptors().size() == itemModels.size()

        where:
        condition                      | itemModels
        'all item types'               | [ioRootItemModel('Car'), ioItemModel('Wheel'), ioItemModel('Engine')]
        'empty set when no item types' | []
    }

    @Test
    def 'equal when other descriptor has same class and integration object model code'() {
        given:
        def descriptor = new DefaultIntegrationObjectDescriptor(ioModel('Object'), descriptorFactory)

        expect:
        descriptor == new DefaultIntegrationObjectDescriptor(ioModel('Object'), descriptorFactory)
    }

    @Test
    @Unroll
    def "not equal when other descriptor #condition"() {
        given:
        def descriptor = new DefaultIntegrationObjectDescriptor(ioModel('Object'), descriptorFactory)

        expect:
        descriptor != other

        where:
        condition                                   | other
        'is null'                                   | null
        'has different class'                       | Stub(IntegrationObjectDescriptor)
        'has different IntegrationObjectModel code' | new DefaultIntegrationObjectDescriptor(ioModel('Other'), descriptorFactory)
    }

    @Test
    def 'hash codes equal when other descriptor has same class and integration object model code'() {
        given:
        def descriptor = new DefaultIntegrationObjectDescriptor(ioModel('Object'), descriptorFactory)

        expect:
        descriptor.hashCode() == new DefaultIntegrationObjectDescriptor(ioModel('Object'), descriptorFactory).hashCode()
    }

    @Test
    def 'hash codes not equal when other descriptor has different IntegrationObjectModel code'() {
        given:
        def descriptor = new DefaultIntegrationObjectDescriptor(ioModel('Object'), descriptorFactory)

        expect:
        descriptor.hashCode() != new DefaultIntegrationObjectDescriptor(ioModel('Other'), descriptorFactory).hashCode()
    }

    private IntegrationObjectModel ioModel(String code) {
        Stub(IntegrationObjectModel) {
            getCode() >> code
        }
    }

    private IntegrationObjectModel ioModel(List<IntegrationObjectItemModel> items = []) {
        Stub(IntegrationObjectModel) {
            getItems() >> items
            getRootItem() >> { items.find { it.root } }
        }
    }

    IntegrationObjectItemModel ioRootItemModel(String type, List<String> subtypes = []) {
        ioItemModel(type, subtypes, true)
    }

    IntegrationObjectItemModel ioItemModel(String type, List<String> subtypes = [], boolean root=false) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> type
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> type
                getAllSubTypes() >> { subtypes.collect { composedType(it) } }
            }
            getRoot() >> root
        }
    }

    ComposedTypeModel composedType(String code) {
        Stub(ComposedTypeModel) {
            getCode() >> code
        }
    }

    ItemModel itemModel(String type) {
        Stub(ItemModel) {
            getItemtype() >> type
        }
    }
}
