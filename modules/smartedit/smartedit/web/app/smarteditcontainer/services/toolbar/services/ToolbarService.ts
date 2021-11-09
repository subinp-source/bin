/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    GatewayProxy,
    IPermissionService,
    IToolbarService,
    LogService,
    ToolbarItemInternal,
    ToolbarSection
} from 'smarteditcommons';

export class ToolbarService extends IToolbarService {
    private onAliasesChange: (aliases: ToolbarItemInternal[]) => void = null;

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

    addAliases(aliases: ToolbarItemInternal[]): void {
        this.aliases = this.aliases.map((alias) => this.get(aliases, alias) || alias);
        this.aliases = [
            ...(this.aliases || []),
            ...(aliases || []).filter((alias) => !this.get(this.aliases, alias))
        ];

        this.aliases = this.sortAliases(this.aliases);

        if (this.onAliasesChange) {
            this.onAliasesChange(this.aliases);
        }
    }

    /**
     * @ngdoc method
     * @name smarteditCommonsModule.IToolbarService#removeItemByKey
     * @methodOf smarteditCommonsModule.IToolbarService
     *
     * @description
     * This method removes the action and the aliases of the toolbar item identified by
     * the provided key.
     *
     * @param {String} itemKey - Identifier of the toolbar item to remove.
     */
    removeItemByKey(itemKey: string): void {
        if (itemKey in this.actions) {
            delete this.actions[itemKey];
        } else {
            this._removeItemOnInner(itemKey);
        }
        this.removeAliasByKey(itemKey);
    }

    removeAliasByKey(itemKey: string): void {
        let aliasIndex = 0;
        for (; aliasIndex < this.aliases.length; aliasIndex++) {
            if (this.aliases[aliasIndex].key === itemKey) {
                break;
            }
        }
        if (aliasIndex < this.aliases.length) {
            this.aliases.splice(aliasIndex, 1);
        }
        if (this.onAliasesChange) {
            this.onAliasesChange(this.aliases);
        }
    }
    setOnAliasesChange(onAliasesChange: (aliases: ToolbarItemInternal[]) => void) {
        this.onAliasesChange = onAliasesChange;
    }
    triggerAction(action: ToolbarItemInternal) {
        if (action && this.actions[action.key]) {
            this.actions[action.key].call(action);
            return;
        }
        this.triggerActionOnInner(action);
    }

    private get(aliases: ToolbarItemInternal[], alias: ToolbarItemInternal): ToolbarItemInternal {
        return aliases.find(({ key }: ToolbarItemInternal) => key === alias.key);
    }

    private sortAliases(aliases: ToolbarItemInternal[]): ToolbarItemInternal[] {
        let samePriority = false;
        let warning = 'In ' + this.gatewayId + ' the items ';
        let _section: ToolbarSection = null;

        const result = [...(aliases || [])].sort((a, b) => {
            if (a.priority === b.priority && a.section === b.section) {
                _section = a.section;
                warning += a.key + ' and ' + b.key + ' ';
                samePriority = true;
                return a.key > b.key ? 1 : -1; // or the opposite ?
            }
            return a.priority - b.priority;
        });

        if (samePriority) {
            this.logService.warn(
                'WARNING: ' + warning + 'have the same priority withing section:' + _section
            );
        }

        return result;
    }
}
