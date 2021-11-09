/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload


import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class ProductIntegrationObjectItemPayloadBuilder {

    private String integrationObjectCode
    private String integrationObjectItemCode
    private String[] classificationAttributes
    private String[] virtualAttributes

    ProductIntegrationObjectItemPayloadBuilder() {
    }

    static def productIntegrationObjectItem(String integrationObjectCode) {
        new ProductIntegrationObjectItemPayloadBuilder(integrationObjectCode : integrationObjectCode)
    }

    def withIntegrationObjectItemCode(String integrationObjectItemCode) {
        this.integrationObjectItemCode = integrationObjectItemCode
        this
    }

    def withClassificationAttributes(String... classificationAttibutes) {
        this.classificationAttributes = classificationAttibutes
        this
    }

    def withVirtualAttributes(String... virtualAttributes) {
        this.virtualAttributes = virtualAttributes
        this
    }

    def build() {
        def body = json()
                .withCode(integrationObjectItemCode)
                .withField("type", json().withCode("Product"))
                .withField("integrationObject", json().withCode(integrationObjectCode))
                .withFieldValues("attributes",
                        productCodeAttribute(),
                        productCatalogVersionAttribute())
        if (classificationAttributes != null) {
            body.withFieldValues("classificationAttributes", classificationAttributes)
        }
        if (virtualAttributes != null) {
            body.withFieldValues("virtualAttributes", virtualAttributes)
        }
        body.build()
    }

    private def productCodeAttribute() {
        json()
                .withField("attributeName", "code")
                .withField("attributeDescriptor", attributeDescriptor("code", "Product"))
                .withField("unique", true)
                .withField("integrationObjectItem", attributeIntegrationObjectItem(integrationObjectItemCode, "Product")).build()
    }

    private def productCatalogVersionAttribute() {
        json()
                .withField("attributeName", "catalogVersion")
                .withField("attributeDescriptor", attributeDescriptor("catalogVersion", "Product"))
                .withField("unique", true)
                .withField("integrationObjectItem", attributeIntegrationObjectItem(integrationObjectItemCode, "Product"))
                .withField("returnIntegrationObjectItem", attributeIntegrationObjectItem("CatalogVersion", "CatalogVersion")).build()
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
