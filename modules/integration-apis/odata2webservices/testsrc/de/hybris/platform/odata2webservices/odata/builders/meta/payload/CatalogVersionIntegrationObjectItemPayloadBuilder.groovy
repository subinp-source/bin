/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class CatalogVersionIntegrationObjectItemPayloadBuilder {

    private String integrationObjectCode

    CatalogVersionIntegrationObjectItemPayloadBuilder() {
    }

    static def catalogVersionIntegrationObjectItem(String integrationObjectCode) {
        new CatalogVersionIntegrationObjectItemPayloadBuilder(integrationObjectCode : integrationObjectCode)
    }

    def build() {
        json()
                .withCode("CatalogVersion")
                .withField("type", json().withCode("CatalogVersion"))
                .withField("integrationObject", json().withCode(integrationObjectCode))
                .withFieldValues("attributes",
                        catalogVersionCatalogAttribute(integrationObjectCode),
                        catalogVersionVersionAttribute(integrationObjectCode))
                .build()
    }

    private def catalogVersionCatalogAttribute(String integrationObjectCode) {
        json()
                .withField("attributeName", "catalog")
                .withField("attributeDescriptor", attributeDescriptor("catalog", "CatalogVersion"))
                .withField("unique", true)
                .withField("integrationObjectItem", attributeIntegrationObjectItem("CatalogVersion", "CatalogVersion"))
                .withField("returnIntegrationObjectItem", attributeIntegrationObjectItem("Catalog", "Catalog")).build()
    }

    private def catalogVersionVersionAttribute(String integrationObjectCode) {
        json()
                .withField("attributeName", "version")
                .withField("attributeDescriptor", attributeDescriptor("version", "CatalogVersion"))
                .withField("unique", true)
                .withField("integrationObjectItem", attributeIntegrationObjectItem("CatalogVersion", "CatalogVersion")).build()
    }

    private def attributeDescriptor(String attributeName, String enclosingType) {
        json().withField("qualifier", attributeName)
                .withField("enclosingType", json().withCode(enclosingType)).build()
    }

    private def attributeIntegrationObjectItem(String code, String type) {
        json().withCode(code)
                .withField("type", json().withCode(type))
                .withField("integrationObject", json().withCode(integrationObjectCode))
    }
}
