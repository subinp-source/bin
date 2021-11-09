/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices


import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.EndpointModel
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel

import static de.hybris.platform.outboundservices.BasicCredentialBuilder.basicCredentialBuilder
import static de.hybris.platform.outboundservices.DestinationTargetBuilder.destinationTarget
import static de.hybris.platform.outboundservices.EndpointBuilder.endpointBuilder
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class ConsumedDestinationBuilder {
    private static final def DEFAULT_ID = 'test-destination'
    private static final def DEFAULT_URL = 'https://test.url.that.does.not.matter'

    private String id
    private String url
    private EndpointModel endpoint
    private AbstractCredentialModel credential
    private DestinationTargetModel destinationTarget
    private Map additionalParameters = [:]


    static ConsumedDestinationBuilder consumedDestinationBuilder() {
        new ConsumedDestinationBuilder()
    }

    ConsumedDestinationBuilder withId(final String id) {
        tap { this.id = id }
    }

    ConsumedDestinationBuilder withUrl(final String url) {
        tap { this.url = url }
    }

    ConsumedDestinationBuilder withEndpoint(EndpointBuilder builder) {
        withEndpoint builder.build()
    }

    ConsumedDestinationBuilder withEndpoint(EndpointModel endpoint) {
        tap { this.endpoint = endpoint }
    }

    ConsumedDestinationBuilder withCredential(AbstractCredentialBuilder builder) {
        withCredential builder.build()
    }

    ConsumedDestinationBuilder withCredential(AbstractCredentialModel credential) {
        tap { this.credential = credential }
    }

    ConsumedDestinationBuilder withDestinationTarget(String id) {
        IntegrationTestUtil.findAny(DestinationTargetModel, { it.id == id })
                .map { withDestinationTarget(it) }
                .orElseThrow { new IllegalArgumentException("Destination target with ID $id not found") }
    }

    ConsumedDestinationBuilder withDestinationTarget(DestinationTargetBuilder builder) {
        withDestinationTarget builder.build()
    }

    ConsumedDestinationBuilder withDestinationTarget(DestinationTargetModel destination) {
        tap { destinationTarget = destination }
    }

    /**
     * Specifies additional parameters to be added to the consumed destination. Subsequent calls to this method do not reset the parameters
     * previously specified.
     * @param params the parameter map.
     * @return a builder with the parameters specified.
     */
    ConsumedDestinationBuilder withAdditionalParameters(Map params) {
        tap {
            if (params) {
                additionalParameters.putAll params
            }
        }
    }

    ConsumedDestinationModel build() {
        consumedDestination(id, url, endpoint, credential, destinationTarget, additionalParameters)
    }

    static cleanup() {
        IntegrationTestUtil.removeAll ConsumedDestinationModel
        IntegrationTestUtil.removeAll EndpointModel
        IntegrationTestUtil.removeAll BasicCredentialModel
        IntegrationTestUtil.removeAll ExposedOAuthCredentialModel
        IntegrationTestUtil.removeAll ConsumedOAuthCredentialModel
        IntegrationTestUtil.removeAll OAuthClientDetailsModel
        IntegrationTestUtil.removeAll DestinationTargetModel
    }

    private static ConsumedDestinationModel consumedDestination(String id, String url, EndpointModel endpoint, AbstractCredentialModel credential, DestinationTargetModel target, Map params) {
        def idVal = deriveId(id)
        importImpEx(
                "INSERT_UPDATE ConsumedDestination; id[unique = true]; url              ; endpoint                      ;  ${credential ? 'credential' : ''} ; destinationTarget         ; additionalProperties(key, value)[map-delimiter = |]",
                "                                 ; $idVal           ; ${deriveUrl(url)}; ${deriveEndpoint(endpoint).pk};  ${credential ? credential.pk : ''}; ${deriveTarget(target).pk}; ${serializeParameters(params)}")
        getConsumedDestinationById(idVal)
    }

    private static String deriveId(String id) {
        id ?: DEFAULT_ID
    }

    private static String deriveUrl(String url) {
        url ?: DEFAULT_URL
    }

    private static EndpointModel deriveEndpoint(EndpointModel model) {
        model ?: endpointBuilder().build()
    }

    private static DestinationTargetModel deriveTarget(DestinationTargetModel model) {
        model ?: destinationTarget().build()
    }

    private static String serializeParameters(Map params) {
        def output = '|'
        params
                .collect { k, v -> "$k->$v" }
                .forEach { output += it }
        output.substring(1)
    }

    private static ConsumedDestinationModel getConsumedDestinationById(final String id) {
        IntegrationTestUtil.findAny(ConsumedDestinationModel, { it.id == id }).orElse(null)
    }

}
