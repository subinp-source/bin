/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class CategoryIntegrationObjectItemPayloadBuilder {

    private String integrationObjectCode

    CategoryIntegrationObjectItemPayloadBuilder() {
    }

    static def categoryIntegrationObjectItem(String integrationObjectCode) {
        new CategoryIntegrationObjectItemPayloadBuilder(integrationObjectCode : integrationObjectCode)
    }

    def build() {
        json()
                .withCode("Category")
                .withField("type", json().withCode("Category"))
                .withField("integrationObject", json().withCode(integrationObjectCode))
                .withFieldValues("attributes",
                        categoryCodeAttribute(),
                        categoryNameAttribute())
                .build()
    }

    private def categoryCodeAttribute() {
        json()
                .withField("attributeName", "code")
                .withField("attributeDescriptor", attributeDescriptor("code", "Category"))
                .withField("unique", true)
                .withField("integrationObjectItem", attributeIntegrationObjectItem("Category", "Category")).build()
    }

    private def categoryNameAttribute() {
        json()
                .withField("attributeName", "name")
                .withField("attributeDescriptor", attributeDescriptor("name", "Category"))
                .withField("unique", false)
                .withField("integrationObjectItem", attributeIntegrationObjectItem("Category", "Category")).build()
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
