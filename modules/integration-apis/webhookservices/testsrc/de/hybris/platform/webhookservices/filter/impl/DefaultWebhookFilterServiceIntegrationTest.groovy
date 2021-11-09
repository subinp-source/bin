/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter.impl

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webhookservices.filter.WebhookFilterService
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class DefaultWebhookFilterServiceIntegrationTest extends ServicelayerSpockSpecification {

    @Resource
    private WebhookFilterService webhookFilterService
    
    @Test
    def 'filter service returns empty Optional when script returns an object not an instance of WebhookFilter'() {
        given: 'Script returning an object not of type WebhookFilter'
        def scriptCode = 'webhookFilterScript'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                "                    ; $scriptCode       ; GROOVY          ; 'String instead of a WebhookFilter'"
        )
        and:
        def item = new ProductModel()

        expect:
        webhookFilterService.filter(item, "model://$scriptCode").empty

        cleanup:
        IntegrationTestUtil.remove ScriptModel, { it.code == scriptCode }
    }
}