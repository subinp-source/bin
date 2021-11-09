/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices

import de.hybris.platform.apiregistryservices.model.EndpointModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class EndpointBuilder {
    private static final String DEFAULT_ID = "local-hybris"
    private static final String DEFAULT_VERSION = "unknown"
    private static final String DEFAULT_NAME = "local-hybris"
    private static final String DEFAULT_URL = 'https://metadataurlthatdoesnotmatterhere'

    private String id
    private String version
    private String name
    private String specUrl

    static EndpointBuilder endpointBuilder() {
        new EndpointBuilder()
    }

    EndpointBuilder withId(String id) {
        this.id = id
        this
    }

    EndpointBuilder withVersion(String version) {
        this.version = version
        this
    }

    EndpointBuilder withName(String name) {
        this.name = name
        this
    }

    EndpointBuilder withSpecUrl(String specUrl) {
        this.specUrl = specUrl
        this
    }

    EndpointModel build() {
        endpoint(id, version, name, specUrl)
    }

    private static EndpointModel endpoint(String id, String version, String name, String url) {
        def idVal = deriveId(id)
        def versionVal = deriveVersion(version)
        importImpEx(
                'INSERT_UPDATE Endpoint; id[unique = true]; version[unique = true]; name               ; specUrl',
                "                      ; $idVal           ; $versionVal           ; ${deriveName(name)}; ${deriveUrl(url)}")
        getEndpointByIdAndVersion(idVal, versionVal)
    }

    private static String deriveId(String id) {
        id ?: DEFAULT_ID
    }

    private static String deriveVersion(String v) {
        v ?: DEFAULT_VERSION
    }

    private static String deriveName(String n) {
        n ?: DEFAULT_NAME
    }

    private static String deriveUrl(String url) {
        url ?: DEFAULT_URL
    }

    private static EndpointModel getEndpointByIdAndVersion(String id, String version) {
        IntegrationTestUtil.findAny(EndpointModel, { it.id == id && it.version == version }).orElse(null)
    }
}
