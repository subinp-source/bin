/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util

import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.DescriptorFactory
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.getService
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class IntegrationObjectTestUtil {

    static IntegrationObjectModel createIntegrationObject(final String integrationObjectCode) throws ImpExException {
        importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "                               ; $integrationObjectCode"
        )
        findIntegrationObjectByCode(integrationObjectCode)
    }

    static IntegrationObjectItemModel createIntegrationObjectRootItem(final IntegrationObjectModel integrationObject, final String itemCode) throws ImpExException {
        return createIntegrationObjectItem(integrationObject, itemCode, true)
    }

    static IntegrationObjectItemModel createIntegrationObjectItem(final IntegrationObjectModel integrationObject, final String itemCode, final Boolean isRoot = false) throws ImpExException {
        importImpEx(
                "INSERT_UPDATE IntegrationObjectItem ; integrationObject(code)[unique = true]; code[unique = true] ; type(code) ; root[default=false]",
                "                                    ; ${integrationObject.getCode()}        ; $itemCode           ; $itemCode  ; $isRoot"
        )
        findIntegrationObjectItemByCodeAndIntegrationObject(itemCode, integrationObject)
    }

    static IntegrationObjectModel findIntegrationObjectByCode(final String code) {
        return IntegrationTestUtil.findAny(IntegrationObjectModel, { it.code == code }).orElse(null)
    }

    static IntegrationObjectItemModel findIntegrationObjectItemByCodeAndIntegrationObject(final String code, IntegrationObjectModel integrationObject) {
        return IntegrationTestUtil.findAny(IntegrationObjectItemModel, {
            it.code == code && it.integrationObject == integrationObject
        }).orElse(null)
    }

    /**
     * Searches for a descriptor of the specified integration object.
     *
     * @param code code of the integration object to search descriptor for
     * @return descriptor of the specified integration object or {@code null}, if such object does not exist.
     */
    static IntegrationObjectDescriptor findIntegrationObjectDescriptorByCode(final String code) {
        def factory = getService 'integrationServicesDescriptorFactory', DescriptorFactory
        def model = IntegrationTestUtil.findAny(IntegrationObjectModel, { it.code == code })
        return model
                .map({factory.createIntegrationObjectDescriptor(it)})
                .orElse(null)
    }

    static InboundChannelConfigurationModel createInboundChannelConfigurationModel(
            final IntegrationObjectModel integrationObject, final AuthenticationType authenticationType) {
        importImpEx(
                "INSERT_UPDATE InboundChannelConfiguration; " +
                        "integrationObject(code)[unique = true]; authenticationType(code)",
                "; ${integrationObject.getCode()}; ${authenticationType.getCode()};"
        )
        findInboundChannelConfigurationObject(integrationObject.getCode())
    }

    static IntegrationObjectModel createNullIntegrationTypeIntegrationObject(final String integrationObjectCode)
            throws ImpExException {
        importImpEx("INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
                "                               ; $integrationObjectCode; ;"
        )
        findIntegrationObjectByCode(integrationObjectCode)
    }

    static InboundChannelConfigurationModel findInboundChannelConfigurationObject(final String iccCode) {
        return IntegrationTestUtil.findAny(InboundChannelConfigurationModel,
                { iccCode == it?.getIntegrationObject()?.getCode() })
                .orElse(null)
    }

    static cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }
}
