/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class CatalogIntegrationObjectItemPayloadBuilder {

    private String integrationObjectCode

    CatalogIntegrationObjectItemPayloadBuilder() {
    }

    static def catalogIntegrationObjectItem(String integrationObjectCode) {
        new CatalogIntegrationObjectItemPayloadBuilder(integrationObjectCode: integrationObjectCode)
    }

    def build() {
        json()
                .withCode("Catalog")
                .withField("type", json().withCode("Catalog"))
                .withField("integrationObject", json().withCode(integrationObjectCode))
                .withFieldValues("attributes", catalogIdAttribute())
                .build()
    }

    private def catalogIdAttribute() {
        json()
                .withField("attributeName", "id")
                .withField("attributeDescriptor", attributeDescriptor("id", "Catalog"))
                .withField("unique", true)
                .withField("integrationObjectItem", attributeIntegrationObjectItem("Catalog", "Catalog")).build()
    }

    private def attributeDescriptor(String attributeName, String enclosingType) {
        json().withField("qualifier", attributeName)
                .withField("enclosingType", json().withCode(enclosingType)).build()
    }

    private def attributeIntegrationObjectItem(String code, String type) {
        json().withCode(code)
                .withField("type", json().withCode(type))
                .withField("integrationObject", json().withCode(integrationObjectCode)).build()
    }
}
