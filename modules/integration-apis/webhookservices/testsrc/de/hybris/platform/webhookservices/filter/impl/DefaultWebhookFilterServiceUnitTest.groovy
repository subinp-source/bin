/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.scripting.engine.ScriptExecutable
import de.hybris.platform.scripting.engine.ScriptingLanguagesService
import de.hybris.platform.scripting.engine.exception.ScriptCompilationException
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException
import de.hybris.platform.variants.model.VariantProductModel
import de.hybris.platform.webhookservices.filter.WebhookFilter
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultWebhookFilterServiceUnitTest extends Specification {

    private static final SCRIPT_URI = 'model://script'
    private static final ITEM = new ItemModel()

    def scriptingService = Mock ScriptingLanguagesService
    def filterService = new DefaultWebhookFilterService(scriptingService)

    @Test
    def 'exception is thrown when filter service is created with null scripting service'() {
        when:
        new DefaultWebhookFilterService(null)

        then:
        def e = thrown IllegalArgumentException
        e.message == 'Scripting language service must be provided'
    }

    @Test
    @Unroll
    def "filter returns empty Optional when item #item and filter location #location"() {
        when:
        def result = filterService.filter item, location

        then:
        result.empty
        0 * scriptingService._

        where:
        item | location
        null | SCRIPT_URI
        ITEM | 'invalid'
    }

    @Test
    @Unroll
    def "filter returns the item in the Optional when no script is provided with script uri = #location"() {
        when:
        def result = filterService.filter ITEM, location

        then:
        result == Optional.of(ITEM)

        where:
        location << [null, '']
    }

    @Test
    @Unroll
    def "filter returns #expected when WebhookFilter returns #condition"() {
        given:
        def scriptWebhookFilter = Stub(WebhookFilter) {
            filter(inputItem) >> returnValue
        }

        and:
        scriptingService.getExecutableByURI(SCRIPT_URI) >> Stub(ScriptExecutable) {
            getAsInterface(WebhookFilter) >> scriptWebhookFilter
        }

        expect:
        filterService.filter(inputItem, SCRIPT_URI) == expected

        where:
        condition                                         | inputItem                 | returnValue                            | expected
        'empty Optional'                                  | ITEM                      | Optional.empty()                       | returnValue
        'the same item as the input item in the Optional' | ITEM                      | Optional.of(ITEM)                      | returnValue
        'a child type of the input item type'             | new ProductModel()        | Optional.of(new VariantProductModel()) | returnValue
        'a parent type of the input item type'            | new VariantProductModel() | Optional.of(new ProductModel())        | Optional.empty()
        'a type differs from the input item type'         | new UnitModel()           | Optional.of(new ProductModel())        | Optional.empty()
        'null instead of an Optional'                     | ITEM                      | null                                   | Optional.empty()
    }

    @Test
    def "filter returns empty Optional when scripting service throws exception"() {
        given:
        scriptingService.getExecutableByURI(SCRIPT_URI) >> {
            throw new ScriptCompilationException("IGNORE - Testing scripting engine failure")
        }
        
        expect:
        filterService.filter(ITEM, SCRIPT_URI).empty
    }

    @Test
    def "filter returns empty Optional when script executable throws exception"() {
        given:
        scriptingService.getExecutableByURI(SCRIPT_URI) >> Stub(ScriptExecutable) {
            getAsInterface(WebhookFilter) >> {
                throw new ScriptExecutionException('IGNORE - Testing script execution exception')
            }
        }

        expect:
        filterService.filter(ITEM, SCRIPT_URI).empty
    }

    @Test
    def "filter returns empty Optional when webhook filter throws exception"() {
        given:
        scriptingService.getExecutableByURI(SCRIPT_URI) >> Stub(ScriptExecutable) {
            getAsInterface(WebhookFilter) >> Stub(WebhookFilter) {
                filter(ITEM) >> {
                    throw new NullPointerException('IGNORE - Testing webhook filter exception')
                }
            }
        }

        expect:
        filterService.filter(ITEM, SCRIPT_URI).empty
    }
}
