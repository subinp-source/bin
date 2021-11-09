/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultItemTypeDescriptorServiceUnitTest extends Specification {
    def integrationObjectService = Stub IntegrationObjectService
    def descriptorFactory = Stub DescriptorFactory
    def typeDescriptorService = new DefaultItemTypeDescriptorService(
            integrationObjectService: integrationObjectService, descriptorFactory: descriptorFactory)

    @Test
    @Unroll
    def "getTypeDescriptor() returns #resDesc for #condition integration object item model"() {
        given:
        integrationObjectService.findIntegrationObjectItem('MyObject', 'MyItem') >> itemModel

        when:
        Optional<TypeDescriptor> type = typeDescriptorService.getTypeDescriptor('MyObject', 'MyItem')

        then: "the type descriptor is returned"
        type?.present == result

        where:
        itemModel                                     | result | condition      | resDesc
        Optional.of(Stub(IntegrationObjectItemModel)) | true   | 'an existing'  | 'type descriptor'
        Optional.empty()                              | false  | 'not existing' | 'empty Optional'
    }

    @Test
    def "getTypeDescriptorByTypeCode() returns type descriptor for specified type"() {
        given: 'an integration object item model for the specified IO and item type'
        def integrationObjectItem = Stub(IntegrationObjectItemModel)
        integrationObjectService.findIntegrationObjectItemByTypeCode('MyObject', 'MyItem') >> integrationObjectItem
        and: 'type descriptor matching the integration object item model'
        def typeDescriptor = Stub(TypeDescriptor)
        descriptorFactory.createItemTypeDescriptor(integrationObjectItem) >> typeDescriptor

        when:
        Optional<TypeDescriptor> type = typeDescriptorService.getTypeDescriptorByTypeCode('MyObject', 'MyItem')

        then: "the type descriptor is returned"
        type.isPresent()
        type.get() == typeDescriptor
    }

    @Test
    @Unroll
    def "getTypeDescriptorByTypeCode() searches parent types when exact match for the type is not found"() {
        def objectCode = 'MyObject'
        def itemCode =  'MyItem'

        given: 'there is no integration object item model matching the IO and item type exactly, but there is match for its supertype'
        def integrationObjectItem = Stub(IntegrationObjectItemModel)
        integrationObjectService.findIntegrationObjectItemByTypeCode(objectCode, itemCode) >> {throw exception}
        integrationObjectService.findIntegrationObjectItemByParentTypeCode(_ as String , _ as String) >> integrationObjectItem
        and: 'a type descriptor is created for the integration object item model'
        def typeDescriptor = Stub(TypeDescriptor)
        descriptorFactory.createItemTypeDescriptor(integrationObjectItem) >> typeDescriptor

        when:
        Optional<TypeDescriptor> type = typeDescriptorService.getTypeDescriptorByTypeCode(objectCode, itemCode)

        then: "the type descriptor is returned"
        type.isPresent()
        type.get() == typeDescriptor

        where:
        exception << [Stub(ModelNotFoundException), Stub(UnknownIdentifierException)]
    }

    @Test
    @Unroll
    def "getTypeDescriptorByTypeCode() returns Optional.empty when item type and parent item type are not found"() {
        given:
        def objectCode = 'MyObject'
        def itemCode =  'MyItem'
        integrationObjectService.findIntegrationObjectItemByTypeCode(objectCode, itemCode) >> {throw exception}
        integrationObjectService.findIntegrationObjectItemByParentTypeCode(objectCode, itemCode) >> {throw exception}

        when:
        Optional<TypeDescriptor> type = typeDescriptorService.getTypeDescriptorByTypeCode(objectCode, itemCode)

        then: "the type descriptor is returned"
        !type.isPresent()

        where:
        exception << [Stub(ModelNotFoundException), Stub(UnknownIdentifierException)]
    }
}
