/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    AngularJSLazyDependenciesService,
    GatewayProxy,
    IPermissionService,
    IToolbarServiceFactory,
    LogService,
    SeDowngradeService,
    TypedMap
} from 'smarteditcommons';
import { ToolbarService } from './ToolbarService';

@SeDowngradeService(IToolbarServiceFactory)
export class ToolbarServiceFactory implements IToolbarServiceFactory {
    private toolbarServicesByGatewayId: TypedMap<ToolbarService> = {};

    constructor(
        private gatewayProxy: GatewayProxy,
        private logService: LogService,
        private lazy: AngularJSLazyDependenciesService,
        private permissionService: IPermissionService
    ) {}

    getToolbarService(gatewayId: string): ToolbarService {
        if (!this.toolbarServicesByGatewayId[gatewayId]) {
            this.toolbarServicesByGatewayId[gatewayId] = new ToolbarService(
                gatewayId,
                this.gatewayProxy,
                this.logService,
                this.lazy.$templateCache(),
                this.permissionService
            );
        }
        return this.toolbarServicesByGatewayId[gatewayId];
    }
}
