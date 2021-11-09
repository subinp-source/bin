/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload


import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class IntegrationObjectPayloadBuilder {

    private String integrationObjectCode
    private String[] items;

    IntegrationObjectPayloadBuilder() {
    }

    static def integrationObject(String integrationObjectCode) {
        new IntegrationObjectPayloadBuilder(integrationObjectCode: integrationObjectCode)
    }

    def withItems(String... integrationObjectItems) {
        this.items = integrationObjectItems
        this
    }


    def build() {
        json()
                .withCode(integrationObjectCode)
                .withField("integrationType", json().withCode("INBOUND"))
                .withFieldValues("items", items)
                .build()
    }
}
