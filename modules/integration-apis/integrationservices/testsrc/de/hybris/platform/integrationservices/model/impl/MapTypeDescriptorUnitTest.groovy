/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class MapTypeDescriptorUnitTest extends Specification {
    private static final String DEFAULT_IO_CODE = 'IO'
    private static final String DEFAULT_ITEM_CODE = 'String2StringMapType'
    def mapTypeDescriptor = new MapTypeDescriptor(Stub(IntegrationObjectItemAttributeModel) {
        getIntegrationObjectItem() >> defaultIntegrationObjectItem()
        getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(MapTypeModel) {
                getCode() >> DEFAULT_ITEM_CODE
                getArgumentType() >> Stub(AtomicTypeModel)
                getReturntype() >> Stub(AtomicTypeModel)
            }
        }
    })


    @Test
    def "create(null) throws exception"() {
        when:
        MapTypeDescriptor.create null

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "create when attributeDescriptor has attributeType that is not an instanceof MapTypeModel throws exception"() {
        given:
        def attribute = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> Stub(CollectionTypeModel)
            }
            getIntegrationObjectItem() >> defaultIntegrationObjectItem()
        }

        when:
        MapTypeDescriptor.create attribute

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "creates a MapTypeDescriptor with the expected codes"() {
        expect:
        with(mapTypeDescriptor) {
            integrationObjectCode == DEFAULT_IO_CODE
            itemCode == DEFAULT_ITEM_CODE
            typeCode == DEFAULT_ITEM_CODE
        }
    }

    @Test
    def "MapTypeDescriptor is never primitive, enumeration, abstract, a root item and does not have a path to the root item"() {
        expect:
        with(mapTypeDescriptor) {
            !isPrimitive()
            !isEnumeration()
            !isAbstract()
            !isRoot()
            !hasPathToRoot()
        }
    }

    @Test
    def "getPathsToRoot() is always empty"() {
        expect:
        mapTypeDescriptor.getPathsToRoot().isEmpty()
    }

    @Test
    def "getAttributes() is always empty"() {
        expect:
        mapTypeDescriptor.getAttributes().isEmpty()
    }

    @Test
    def "getAttribute(String) always returns empty result"() {
        expect:
        !mapTypeDescriptor.getAttribute('anyAttributeName').present
    }

    @Test
    def "isInstance() returns true when the object is a Map"() {
        expect:
        mapTypeDescriptor.isInstance(new HashMap())
    }

    @Test
    @Unroll
    def "isInstance() returns false when the object is #condition"() {
        expect:
        !mapTypeDescriptor.isInstance(obj)

        where:
        condition                                | obj
        'null'                                   | null
        'not an instance of the type descriptor' | Stub(MapTypeModel)
    }

    private IntegrationObjectItemModel defaultIntegrationObjectItem() {
        Stub(IntegrationObjectItemModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getCode() >> DEFAULT_IO_CODE
            }
        }
    }
}
