/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload


import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class VirtualAttributeDescriptorPayloadBuilder {

    private  String code
    private String logicLocation
    private String type

    VirtualAttributeDescriptorPayloadBuilder() {
    }

    static def retrievalDescriptor() {
        new VirtualAttributeDescriptorPayloadBuilder()
    }

    def withCode(String code) {
        this.code = code
        this
    }

    def withLogicLocation(String logicLocation) {
        this.logicLocation = logicLocation
        this
    }

    def withType(String type) {
        this.type = type
        this
    }

    def build() {
        json()
                .withField("code", code)
                .withField("logicLocation", logicLocation)
                .withField("type", json().withField("code", type).build())
                .build()
    }
}
