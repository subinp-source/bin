/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IToolbarService } from '../IToolbarService';

/**
 * @ngdoc service
 * @name smarteditServicesModule.toolbarServiceFactory
 *
 * @description
 * The toolbar service factory generates instances of the {@link smarteditCommonsModule.IToolbarService ToolbarService} based on
 * the gateway ID (toolbar-name) provided. Only one ToolbarService instance exists for each gateway ID, that is, the
 * instance is a singleton with respect to the gateway ID.
 */
export abstract class IToolbarServiceFactory<T extends IToolbarService = IToolbarService> {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.toolbarServiceFactory#getToolbarService
     * @methodOf smarteditServicesModule.toolbarServiceFactory
     *
     * @description
     * Returns a single instance of the ToolbarService for the given gateway identifier. If one does not exist, an
     * instance is created and cached.
     *
     * @param {string} gatewayId The toolbar name used for cross iframe communication (see {@link
     * smarteditCommonsModule.service:GatewayProxy gatewayProxy})
     * @returns {ToolbarService} Corresponding ToolbarService instance for given gateway ID.
     */
    getToolbarService(gatewayId: string): T {
        'proxyFunction';
        return null;
    }
}
