/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext
import de.hybris.platform.integrationservices.virtualattributes.LogicParams
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeExecutionException
import de.hybris.platform.scripting.engine.ScriptExecutable
import de.hybris.platform.scripting.engine.ScriptExecutionResult
import de.hybris.platform.scripting.engine.ScriptingLanguagesService
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException
import de.hybris.platform.scripting.engine.exception.ScriptingException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ScriptLogicExecutorUnitTest extends Specification {
    private static final def CONTEXT_PARAMETERS = LogicParams.create new ItemModel()
    private static final def ATTRIBUTE_NAME = 'virtual'
    private static final def ITEM_CODE = 'SomeItem'

    def context = Stub(LogicExecutorContext) {
        getDescriptor() >> Stub(TypeAttributeDescriptor) {
            getAttributeName() >> ATTRIBUTE_NAME
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getItemCode() >> ITEM_CODE
            }
        }
    }
    def service = Stub ScriptingLanguagesService

    @Test
    @Unroll
    def "cannot be instantiated when #param is null"() {
        when:
        new ScriptLogicExecutor(context, service)

        then:
        def e = thrown IllegalArgumentException
        e.message == "$param must be provided"

        where:
        param                       | context                    | service
        'LogicExecutorContext'      | null                       | Stub(ScriptingLanguagesService)
        'ScriptingLanguagesService' | Stub(LogicExecutorContext) | null
    }

    @Test
    def 'returns result of the script execution when execution is successful'() {
        given: 'execution completes successfully'
        def result = new Object()
        def script = Stub(ScriptExecutable)
        service.getExecutableByURI(_ as String) >> script
        script.execute([itemModel: CONTEXT_PARAMETERS.item]) >> Stub(ScriptExecutionResult) {
            getScriptResult() >> result
        }

        expect:
        new ScriptLogicExecutor(context, service).execute(CONTEXT_PARAMETERS) == result
    }

    @Test
    @Unroll
    def "throws VirtualAttributeExecutionException if the script execution crashes with #cause.class.simpleName"() {
        given: 'execution crashes'
        def script = Stub(ScriptExecutable)
        service.getExecutableByURI(_ as String) >> script
        script.execute([itemModel: CONTEXT_PARAMETERS.item]) >> { throw cause }

        when:
        new ScriptLogicExecutor(context, service).execute(CONTEXT_PARAMETERS)

        then:
        def e = thrown VirtualAttributeExecutionException
        e.message.contains "$context.logicLocation"
        e.message.contains "$context.descriptor"
        e.message.contains "$CONTEXT_PARAMETERS"
        e.cause == cause
        e.executorContext == context
        e.scriptParameters == CONTEXT_PARAMETERS

        where:
        cause << [new RuntimeException(), new ScriptExecutionException('')]
    }

    @Test
    def 'throws VirtualAttributeConfigurationException if the script execution crashes with ScriptingException'() {
        given: 'execution crashes'
        def exception = new ScriptingException('')
        def script = Stub(ScriptExecutable)
        service.getExecutableByURI(_ as String) >> script
        script.execute([itemModel: CONTEXT_PARAMETERS.item]) >> { throw exception }

        when:
        new ScriptLogicExecutor(context, service).execute(CONTEXT_PARAMETERS)

        then:
        def e = thrown VirtualAttributeConfigurationException
        e.message.contains ATTRIBUTE_NAME
        e.message.contains ITEM_CODE
        e.cause == exception
        e.executorContext == context
        e.message.contains "Configuration for virtual attribute virtual in item SomeItem is invalid. "
    }
}
