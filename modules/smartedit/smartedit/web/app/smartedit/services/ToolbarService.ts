/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxy, IPermissionService, IToolbarService, LogService } from 'smarteditcommons';

export class ToolbarService extends IToolbarService {
    constructor(
        public gatewayId: string,
        gatewayProxy: GatewayProxy,
        logService: LogService,
        $templateCache: angular.ITemplateCacheService,
        permissionService: IPermissionService
    ) {
        super(logService, $templateCache, permissionService);

        gatewayProxy.initForService(this, [
            'addAliases',
            'removeItemByKey',
            'removeAliasByKey',
            '_removeItemOnInner',
            'triggerActionOnInner'
        ]);
    }

    _removeItemOnInner(itemKey: string): void {
        if (itemKey in this.actions) {
            delete this.actions[itemKey];
        }
        this.logService.warn('removeItemByKey() - Failed to find action for key ' + itemKey);
    }
    triggerActionOnInner(action: { key: string }): void {
        if (!this.actions[action.key]) {
            this.logService.error(
                'triggerActionByKey() - Failed to find action for key ' + action.key
            );
            return;
        }
        this.actions[action.key]();
    }
}
