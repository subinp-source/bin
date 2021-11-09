/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util

import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel

class VirtualAttributeBuilder {

    private String item
    private IntegrationObjectVirtualAttributeDescriptorModel retrievalDescriptor
    private String attributeName

    static VirtualAttributeBuilder attribute() {
        return new VirtualAttributeBuilder()
    }

    VirtualAttributeBuilder withName(final String name)
    {
        attributeName = name
        this
    }

    VirtualAttributeBuilder forItem(final String itemCode)
    {
        item = itemCode
        this
    }

    VirtualAttributeBuilder withRetrievalDescriptor(final String code)
    {
        def desc = IntegrationTestUtil.findAny(IntegrationObjectVirtualAttributeDescriptorModel, { it.code == code })
                .orElseThrow { new IllegalArgumentException("Cannot build a Virtual Attribute with a descriptor code that does not exist yet.") }
        withRetrievalDescriptor desc
    }

    VirtualAttributeBuilder withRetrievalDescriptor(final IntegrationObjectVirtualAttributeDescriptorModel retrievalDescriptor)
    {
        this.retrievalDescriptor = retrievalDescriptor
        this
    }

    void setup() throws ImpExException {
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code);',
                "                                                   ; $item               ; $attributeName              ; $retrievalDescriptor.code;")
    }

    static void cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectVirtualAttributeDescriptorModel
        IntegrationTestUtil.removeAll IntegrationObjectItemVirtualAttributeModel
    }

    static VirtualAttributeDescriptor logicDescriptor() {
        new VirtualAttributeDescriptor()
    }

    static class VirtualAttributeDescriptor {
        private String code
        private String logicLocation
        private String type

        VirtualAttributeDescriptor withCode(String code) {
            tap { this.code = code }
        }

        VirtualAttributeDescriptor withLogicLocation(String logicLocation) {
            tap { this.logicLocation = logicLocation }
        }

        VirtualAttributeDescriptor withType(String type) {
            tap { this.type = type }
        }

        void setup() throws ImpExException {
            IntegrationTestUtil.importImpEx(
                    "INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation ; ${type ? ' type(code)' : ''}",
                    "                                                                ; $code              ; $logicLocation; ${type ?: ''}")
        }
    }
}
