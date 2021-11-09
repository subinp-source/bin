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

package de.hybris.platform.odata2services.odata.processor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.processor.handler.delete.DeleteHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PatchPersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.BatchPersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.persistence.batch.ChangeSetPersistenceHandler
import de.hybris.platform.odata2services.odata.processor.handler.read.ReadHandler
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultODataProcessorFactoryUnitTest extends Specification {

    private def context = Stub(ODataContext)
    def factory = new DefaultODataProcessorFactory()

    @Test
    def "factory creates an OData2Processor"() {
        expect:
        factory.createProcessor(context) instanceof OData2Processor
    }

    @Test
    def "creates new processor instance for every invocation"() {
        when:
        def processor1 = factory.createProcessor context
        def processor2 = factory.createProcessor context

        then:
        !processor1.is(processor2)
    }

    @Test
    @Unroll
    def "injects #handler into the processor"() {
        setup:
        factory."${handlerMethod}" = handler

        when:
        def processor = factory.createProcessor() as OData2Processor

        then:
        processor."${handlerMethod}" == handler

        where:
        handlerMethod                 | handler
        'batchPersistenceHandler'     | Stub(BatchPersistenceHandler)
        'changeSetPersistenceHandler' | Stub(ChangeSetPersistenceHandler)
        'persistenceHandler'          | Stub(PersistenceHandler)
        'patchPersistenceHandler'     | Stub(PatchPersistenceHandler)
        'deleteHandler'               | Stub(DeleteHandler)
        'readHandler'                 | Stub(ReadHandler)
    }

    @Test
    def "injects ODataContext into the processor"() {
        when:
        def processor = factory.createProcessor(context)

        then:
        processor.context == context
    }
}
