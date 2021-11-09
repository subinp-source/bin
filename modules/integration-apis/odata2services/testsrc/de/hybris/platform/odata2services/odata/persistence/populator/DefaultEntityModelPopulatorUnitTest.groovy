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

package de.hybris.platform.odata2services.odata.persistence.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.populator.processor.PrimitivePropertyProcessor
import de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessor
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.exception.MessageReference
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultEntityModelPopulatorUnitTest extends Specification {
    def entityModelPopulator = new DefaultEntityModelPopulator(propertyProcessors: [])

    @Test
    @Unroll
    def "populateItem() throws exception when #param is null"() {
        when:
        entityModelPopulator.populateItem item, request

        then:
        thrown IllegalArgumentException

        where:
        param             | item            | request
        'item model'      | null            | Stub(StorageRequest)
        'storage request' | Stub(ItemModel) | null
    }

    @Test
    def 'populateItem() invokes PropertyProcessors in the order they are configured'() {
        given:
        def processor1 = Mock PropertyProcessor
        def processor2 = Mock PropertyProcessor
        entityModelPopulator.propertyProcessors = [processor1, processor2]
        and:
        def item = Stub ItemModel
        def request = Stub StorageRequest

        when:
        entityModelPopulator.populateItem item, request

        then:
        1 * processor1.processItem(item, request)
        then:
        1 * processor2.processItem(item, request)
    }

    @Test
    def 'populateItem() rethrows EdmException from property processor'() throws EdmException {
        given:
        def propertyProcessor = Stub(PropertyProcessor) {
            processItem(_ as ItemModel, _ as StorageRequest) >> { throw new EdmException(Stub(MessageReference)) }
        }
        entityModelPopulator.propertyProcessors = [propertyProcessor]

        when:
        entityModelPopulator.populateItem Stub(ItemModel), Stub(StorageRequest)

        then:
        thrown EdmException
    }

    @Test
    def 'populateItem() does not invoke excluded item property processors'() {
        given:
        def processor = Mock PropertyProcessor
        entityModelPopulator.propertyProcessors = [processor]
        entityModelPopulator.excludedItemPropertyProcessors = [processor.class]

        when:
        entityModelPopulator.populateItem Stub(ItemModel), Stub(StorageRequest)

        then:
        0 * processor._
    }

    @Test
    def 'populateItem() invokes customized subclass of the excluded item property processor'() {
        given:
        def item = Stub ItemModel
        def request = Stub StorageRequest
        def processor = Mock PrimitivePropertyProcessor
        entityModelPopulator.propertyProcessors = [processor]
        entityModelPopulator.excludedItemPropertyProcessors = [PrimitivePropertyProcessor]

        when:
        entityModelPopulator.populateItem item, request

        then:
        1 * processor.processItem(item, request)
    }

    @Test
    @Unroll
    def "populateEntity() throws exception when #param is null"() {
        when:
        entityModelPopulator.populateEntity entry, request

        then:
        thrown IllegalArgumentException

        where:
        param                   | entry            | request
        'ODataEntry'            | null             | Stub(ItemConversionRequest)
        'ItemConversionRequest' | Stub(ODataEntry) | null
    }

    @Test
    def 'populateEntity() invokes PropertyProcessors in order they are configured'() {
        given:
        def processor1 = Mock PropertyProcessor
        def processor2 = Mock PropertyProcessor
        entityModelPopulator.propertyProcessors = [processor1, processor2]
        and:
        def entry = Stub ODataEntry
        def request = Stub ItemConversionRequest

        when:
        entityModelPopulator.populateEntity entry, request

        then:
        1 * processor1.processEntity(entry, request)
        then:
        1 * processor2.processEntity(entry, request)
    }

    @Test
    def 'populateEntity() rethrows EdmException from PropertyProcessors'() {
        given:
        def propertyProcessor = Stub(PropertyProcessor) {
            processEntity(_ as ODataEntry, _ as ItemConversionRequest) >> { throw new EdmException(Stub(MessageReference)) }
        }
        entityModelPopulator.propertyProcessors = [propertyProcessor]

        when:
        entityModelPopulator.populateEntity Stub(ODataEntry), Stub(ItemConversionRequest)

        then:
        thrown EdmException
    }

    @Test
    def 'populateEntity() ignores excluded property processors'() {
        given:
        def processor = Mock PropertyProcessor
        entityModelPopulator.propertyProcessors = [processor]
        entityModelPopulator.excludedItemPropertyProcessors = [processor.class]

        when:
        entityModelPopulator.populateEntity Stub(ODataEntry), Stub(ItemConversionRequest)

        then:
        1 * processor.processEntity(_, _)
    }
}
