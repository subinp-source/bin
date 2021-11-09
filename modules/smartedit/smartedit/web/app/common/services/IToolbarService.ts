import { LogService } from '@smart/utils';
import { TypedMap } from 'smarteditcommons/dtos';
import { IPermissionService, IPrioritized } from 'smarteditcommons/services/interfaces';
import { stringUtils } from 'smarteditcommons/utils/StringUtils';
import { InjectionToken, Type } from '@angular/core';

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

export const TOOLBAR_ITEM = new InjectionToken<ToolbarItemInternal>('TOOLBAR_ITEM');

export enum ToolbarItemType {
    TEMPLATE = 'TEMPLATE',
    ACTION = 'ACTION',
    HYBRID_ACTION = 'HYBRID_ACTION'
}

export enum ToolbarSection {
    left = 'left',
    middle = 'middle',
    right = 'right'
}

export enum ToolbarDropDownPosition {
    left = 'left',
    center = 'center',
    right = 'right'
}

export interface CoreToolbarItemDefinition extends IPrioritized {
    key: string;
    type: ToolbarItemType;
    icons?: string[];
    include?: string;
    component?: Type<any>;
    section?: ToolbarSection;
    iconClassName?: string;
    actionButtonFormat?: string;
    keepAliveOnClose?: boolean;
    contextTemplate?: string;
    contextTemplateUrl?: string;
    contextComponent?: Type<any>;
    dropdownPosition?: ToolbarDropDownPosition;
    permissions?: string[];
}

export interface ToolbarItemDefinition extends CoreToolbarItemDefinition {
    callback?: () => void;
    descriptionI18nKey?: string;
    nameI18nKey?: string;
    priority?: number;
}

export interface ToolbarItemInternal extends CoreToolbarItemDefinition {
    description: string;
    name: string;
    isOpen: boolean;
    isPermissionGranted: boolean;
    priority?: number;
}

/**
 * @ngdoc service
 * @name smarteditCommonsModule.IToolbarService
 *
 * @description
 * Provides an abstract extensible toolbar service. Used to manage and perform actions to either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
export abstract class IToolbarService {
    protected aliases: ToolbarItemInternal[] = [];
    protected actions: TypedMap<() => void> = {};

    constructor(
        protected logService: LogService,
        private $templateCache: angular.ITemplateCacheService,
        private permissionService: IPermissionService
    ) {}

    getActions() {
        return this.actions;
    }
    getAliases(): ToolbarItemInternal[] {
        return this.aliases;
    }
    /**
     * @ngdoc method
     * @name smarteditCommonsModule.IToolbarService#addItems
     * @methodOf smarteditCommonsModule.IToolbarService
     *
     * @description
     * Takes an array of items and maps them internally for display and trigger through an internal callback key.
     * The action's properties are made available through the included template by a variable named 'item'.
     *
     * The toolbar item can accept a context that is displayed beside the toolbar item using either ContextTemplate or ContextTemplateUrl. This context
     * can be shown or hidden by calling the events {@link seConstantsModule.object:SHOW_TOOLBAR_ITEM_CONTEXT SHOW_TOOLBAR_ITEM_CONTEXT} and
     * {@link seConstantsModule.object:HIDE_TOOLBAR_ITEM_CONTEXT HIDE_TOOLBAR_ITEM_CONTEXT} respectively. Both the events need the key of the toolbar item as data.
     *
     * @example crossFrameEventService.publish(SHOW_TOOLBAR_ITEM_CONTEXT, 'toolbar.item.key');
     *
     * @param {Object[]} items - List of items
     * @param {String} items.key - Unique identifier of the toolbar action item.
     * @param {Function=} items.callback - Callback triggered when this toolbar action item is clicked.
     * @param {String} items.nameI18nKey - Name translation key
     * @param {String} items.descriptionI18nKey - Description translation key
     * @param {String[]=} items.icons - List of image URLs for the icon images (only for ACTION and HYBRID_ACTION)
     * @param {String} items.type - TEMPLATE, ACTION, or HYBRID_ACTION
     * @param {String} items.include - Deprecated since 2005, use `component`, HTML template URL (only for TEMPLATE and HYBRID_ACTION)
     * @param {Type<any>} items.component - Component rendered withing a toolbar
     * @param {Integer} items.priority - Determines the position of the item in the toolbar, ranging from 0-1000 with the default priority being 500.
     * An item with a higher priority number will be to the right of an item with a lower priority number in the toolbar.
     * @param {String} items.section - Determines the sections(left, middle or right) of the item in the toolbar.
     * @param {String=} items.iconClassName - List of classes used to display icons from fonts
     * @param {String=} items.actionButtonFormat - Formats the button in compact/header-toolbar manner if 'compact' param is passed
     * @param {Boolean=} [items.keepAliveOnClose=false] - Keeps the dropdown content in the DOM on close.
     * @param {Type<any>>=} items.contextComponent - the context component that needs to be displayed for the toolbar item.
     * @param {String=} items.contextTemplate - Deprecated sinze 2005, use `contextComponent`, the context template that needs to be displayed for the toolbar item.
     * @param {String=} items.contextTemplateUrl - Deprecated since 2005, use `contextComponent`, the path to the context template that needs to be displayed for the toolbar item.
     * @param {String=} items.dropdownPosition - Arranges the dropdown in either of these left/center/right positions.
     * @param {String[]=} items.permissions - List of permission names used to determine whether to show or hide the toolbar item.
     */
    addItems(_items: ToolbarItemDefinition[]): Promise<void> {
        const promisesToResolve = _items
            .filter((item) => {
                // Validate provided actions -> The filter will return only valid items.
                let includeAction = true;
                if (!item.key) {
                    this.logService.error('addItems() - Cannot add item without key.');
                    includeAction = false;
                }
                if (item.contextTemplate && item.contextTemplateUrl) {
                    this.logService.error(
                        'addItems() - Toolbar item should contain only one of contextTemplate or contextTemplateUrl'
                    );
                    includeAction = false;
                }
                return includeAction;
            })
            .map((item) => {
                const key = item.key;
                this.actions[key] = item.callback;
                let generatedContextTemplateUrl;
                if (item.contextTemplate) {
                    generatedContextTemplateUrl =
                        'toolbarItemContextTemplate' +
                        btoa(stringUtils.generateIdentifier()) +
                        '.html';
                    this.$templateCache.put(generatedContextTemplateUrl, item.contextTemplate);
                } else {
                    generatedContextTemplateUrl = item.contextTemplateUrl;
                }
                const toolbarItem = {
                    key,
                    name: item.nameI18nKey,
                    iconClassName: item.iconClassName,
                    description: item.descriptionI18nKey,
                    icons: item.icons,
                    type: item.type,
                    include: item.include,
                    priority: item.priority || 500,
                    section: item.section || 'left',
                    isOpen: false,
                    component: item.component,
                    actionButtonFormat: item.actionButtonFormat,
                    keepAliveOnClose: item.keepAliveOnClose || false,
                    contextTemplateUrl: generatedContextTemplateUrl,
                    contextComponent: item.contextComponent,
                    dropdownPosition: item.dropdownPosition,
                    permissions: item.permissions
                } as ToolbarItemInternal;

                return this._populateIsPermissionGranted(toolbarItem);
            });
        return Promise.all(promisesToResolve).then((items) => {
            if (items.length > 0) {
                this.addAliases(items);
            }
            return;
        });
    }

    _populateIsPermissionGranted(toolbarItem: ToolbarItemInternal): Promise<ToolbarItemInternal> {
        if (toolbarItem.permissions) {
            return Promise.resolve(
                this.permissionService
                    .isPermitted([
                        {
                            names: toolbarItem.permissions
                        }
                    ])
                    .then((isGranted) => {
                        toolbarItem.isPermissionGranted = isGranted;
                        return toolbarItem;
                    })
            );
        } else {
            toolbarItem.isPermissionGranted = true;
            return Promise.resolve(toolbarItem);
        }
    }

    /////////////////////////////////////
    // Proxied Functions : these functions will be proxied if left unimplemented
    /////////////////////////////////////
    addAliases(items: ToolbarItemInternal[]): void {
        'proxyFunction';
        return;
    }
    removeItemByKey(key: string): void {
        'proxyFunction';
        return;
    }
    _removeItemOnInner(itemKey: string): void {
        'proxyFunction';
        return;
    }
    removeAliasByKey(itemKey: string): void {
        'proxyFunction';
        return;
    }
    triggerActionOnInner(action: { key: string }): void {
        'proxyFunction';
        return;
    }
}
