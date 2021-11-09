/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload

import de.hybris.platform.integrationservices.util.JsonBuilder

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class IntegrationObjectItemClassificationAttributePayloadBuilder {
    
    private String integrationObjectCode
    private String integrationObjectItemCode
    private String type
    private String attributeName
    private String classAttributeAssignment

    IntegrationObjectItemClassificationAttributePayloadBuilder() {
    }

    static def classificationAttribute() {
        new IntegrationObjectItemClassificationAttributePayloadBuilder()
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

    def withAttributeName(String attributeName) {
        this.attributeName = attributeName
        this
    }

    def withClassAttributeAssignment(String classAttributeAssignment) {
        this.classAttributeAssignment = classAttributeAssignment
        this
    }
    
    def build() {
        json()
                .withField("integrationObjectItem", attributeIntegrationObjectItem())
                .withField("attributeName", attributeName)
                .withField("classAttributeAssignment", classAttributeAssignment)
                .build()
    }

    private def attributeIntegrationObjectItem() {
        json().withCode(integrationObjectItemCode)
                .withField("type", json().withCode(type))
                .withField("integrationObject", json().withCode(integrationObjectCode))
                .build()
    }
}
