/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload


import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class IntegrationObjectItemVirtualAttributePayloadBuilder {

    private String attributeName
    private String integrationObjectCode
    private String integrationObjectItemCode
    private String type
    private String retrievalDescriptor

    IntegrationObjectItemVirtualAttributePayloadBuilder() {
    }

    static def virtualAttribute() {
        new IntegrationObjectItemVirtualAttributePayloadBuilder()
    }

    def withAttributeName(String attributeName) {
        this.attributeName = attributeName
        return this
    }

    def withIntegrationObjectCode(String integrationObjectCode) {
        this.integrationObjectCode = integrationObjectCode
        return this
    }

    def withIntegrationObjectItemCode(String integrationObjectItemCode) {
        this.integrationObjectItemCode = integrationObjectItemCode
        return this
    }

    def withType(String type) {
        this.type = type
        this
    }

    def withRetrievalDescriptor(String retrievalDescriptor) {
        this.retrievalDescriptor = retrievalDescriptor
        return this
    }

    def build() {
        json()
                .withField("attributeName", attributeName)
                .withField("integrationObjectItem", attributeIntegrationObjectItem())
                .withField("retrievalDescriptor", retrievalDescriptor)
                .build()
    }

    private def attributeIntegrationObjectItem() {
        json().withCode(integrationObjectItemCode)
                .withField("type", json().withCode(type))
                .withField("integrationObject", json().withCode(integrationObjectCode))
                .build()
    }
}
