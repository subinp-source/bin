/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import {
    objectUtils,
    stringUtils,
    IContextualMenuButton,
    IContextualMenuConfiguration,
    IContextualMenuService,
    PriorityService,
    REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
    SeDowngradeService,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { ContextualMenu } from 'smartedit/services/ContextualMenu';
import { BehaviorSubject } from 'rxjs';

/**
 * @ngdoc service
 * @name smarteditServicesModule.ContextualMenuService
 *
 * @description
 * The ContextualMenuService is used to add contextual menu items for each component.
 *
 * To add items to the contextual menu, you must call the addItems method of the contextualMenuService and pass a map
 * of the component-type array of contextual menu items mapping. The component type names are the keys in the mapping.
 * The component name can be the full name of the component type, an ant-like wildcard (such as  *middle*Suffix), or a
 * valid regex that includes or excludes a set of component types.
 *
 */
@SeDowngradeService(IContextualMenuService)
@Injectable()
export class ContextualMenuService implements IContextualMenuService {
    public onContextualMenuItemsAdded: BehaviorSubject<string> = new BehaviorSubject<string>(null);

    private _contextualMenus: TypedMap<IContextualMenuButton[]> = {};
    private _featuresList: string[];

    /* @internal */
    constructor(
        private priorityService: PriorityService,
        private systemEventService: SystemEventService
    ) {}

    /**
     * @ngdoc method
     * @name smarteditServicesModule.ContextualMenuService#addItems
     * @methodOf smarteditServicesModule.ContextualMenuService
     *
     * @description
     * The method called to add contextual menu items to component types in the SmartEdit application.
     * The contextual menu items are then retrieved by the contextual menu decorator to wire the set of menu items to the specified component.
     *
     * Sample Usage:
     * <pre>
     * contextualMenuService.addItems({
     * '.*Component': [{
     *  key: 'itemKey',
     *  i18nKey: 'CONTEXTUAL_MENU',
     *  condition: function(componentType, componentId) {
     * 	return componentId === 'ComponentType';
     * 	},
     *  callback: function(componentType, componentId) {
     * 	alert('callback for ' + componentType + "_" + componentId);
     * 	},
     *  displayClass: 'democlass',
     *  iconIdle: '.../icons/icon.png',
     *  iconNonIdle: '.../icons/icon.png',
     * }]
     * });
     * </pre>
     *
     * @param {TypedMap<IContextualMenuButton[]>} contextualMenuItemsMap A map of componentType regular experessions to list of {@link IContextualMenuButton IContextualMenuButton} contextual menu items
     *
     * The object contains a list that maps component types to arrays of {@link IContextualMenuButton IContextualMenuButton} contextual menu items. The mapping is a key-value pair.
     * The key is the name of the component type, for example, Simple Responsive Banner Component, and the value is an array of {@link IContextualMenuButton IContextualMenuButton} contextual menu items, like add, edit, localize, etc.
     */
    public addItems(contextualMenuItemsMap: TypedMap<IContextualMenuButton[]>): void {
        try {
            if (contextualMenuItemsMap !== undefined) {
                this._featuresList = this._getFeaturesList(this._contextualMenus);
                const componentTypes = Object.keys(contextualMenuItemsMap);

                componentTypes.forEach((type: string) =>
                    this._initContextualMenuItems(contextualMenuItemsMap, type)
                );
            }
        } catch (e) {
            throw new Error('addItems() - Cannot add items. ' + e);
        }
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.ContextualMenuService#removeItemByKey
     * @methodOf smarteditServicesModule.ContextualMenuService
     *
     * @description
     * This method removes the menu items identified by the provided key.
     *
     * @param {String} itemKey The key that identifies the menu items to remove.
     */
    public removeItemByKey(itemKey: string): void {
        Object.keys(this._contextualMenus).forEach((contextualMenuKey: string) => {
            const contextualMenuItems = this._contextualMenus[contextualMenuKey];
            this._contextualMenus[contextualMenuKey] = contextualMenuItems.filter(
                (item: IContextualMenuButton) => item.key !== itemKey
            );

            if (this._contextualMenus[contextualMenuKey].length === 0) {
                // Remove if the contextual menu is empty.
                delete this._contextualMenus[contextualMenuKey];
            }
        });
    }

    /**
     * Verifies whether the itemKey has already been added to contextual menu list.
     *
     * @param {String} itemKey The item key to verify.
     *
     * @returns {boolean} Return true if itemKey exists in the contextual menu list, false otherwise.
     */
    public containsItem(itemKey: string): boolean {
        const contextualMenuExists: boolean[] = Object.keys(this._contextualMenus).map(
            (contextualMenuKey: string) => {
                const contextualMenuItems = this._contextualMenus[contextualMenuKey];
                return (
                    contextualMenuItems.findIndex(
                        (item: IContextualMenuButton) => item.key === itemKey
                    ) > -1
                );
            }
        );

        return contextualMenuExists.findIndex((menuExists) => menuExists === true) > -1;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.ContextualMenuService#getContextualMenuByType
     * @methodOf smarteditServicesModule.ContextualMenuService
     *
     * @description
     * Will return an array of contextual menu items for a specific component type.
     * For each key in the contextual menus' object, the method converts each component type into a valid regex using the regExpFactory of the function module and then compares it with the input componentType and, if matched, will add it to an array and returns the array.
     *
     * @param {String} componentType The type code of the selected component
     *
     * @returns {Array} An array of contextual menu items assigned to the type.
     *
     */
    getContextualMenuByType(componentType: string): IContextualMenuButton[] {
        let contextualMenuArray: IContextualMenuButton[] = [];
        if (this._contextualMenus) {
            Object.keys(this._contextualMenus).forEach((regexpKey) => {
                if (stringUtils.regExpFactory(regexpKey).test(componentType)) {
                    contextualMenuArray = this._getUniqueItemArray(
                        contextualMenuArray,
                        this._contextualMenus[regexpKey]
                    );
                }
            });
        }

        return contextualMenuArray;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.ContextualMenuService#getContextualMenuItems
     * @methodOf smarteditServicesModule.ContextualMenuService
     *
     * @description
     * Returns an object that contains a list of contextual menu items that are displayed in the menu and menu items that are added to the More … options.
     *
     * The returned object contains two arrays. The first array contains the menu items that are displayed in the menu. The display limit size (iLeftBtns) specifies
     * the maximum number of items that can be displayed in the menu. The other array contains the menu items that are available under the More... options.
     * This method decides which items to send to each array based on their priority. Items with the lowest priority are displayed in the menu. The remaining
     * items are added to the More... menu. Items that do not have a priority are automatically assigned a default priority.
     *
     * @param {IContextualMenuConfiguration} configuration the {@link IContextualMenuConfiguration IContextualMenuConfiguration}
     * @returns {Promise} A promise that resolves to an array of contextual menu items assigned to the component type.
     *
     * The returned object contains the following properties
     * - leftMenuItems : An array of menu items that can be displayed on the component.
     * - moreMenuItems : An array of menu items that are available under the more menu items action.
     *
     */
    public getContextualMenuItems(
        configuration: IContextualMenuConfiguration
    ): Promise<ContextualMenu> {
        const { iLeftBtns } = configuration;
        delete configuration.iLeftBtns;
        const menuItems = this.getContextualMenuByType(configuration.componentType);

        const promises = menuItems.map((item: IContextualMenuButton) => {
            if (!item.condition) {
                return Promise.resolve(item);
            }

            const isItemEnabled = item.condition(configuration);

            return isItemEnabled instanceof Promise
                ? (isItemEnabled as Promise<boolean>).then((_isItemEnabled: boolean) =>
                      _isItemEnabled ? item : null
                  )
                : Promise.resolve(isItemEnabled ? item : null);
        });

        return Promise.all(promises).then((items: IContextualMenuButton[]) => {
            const leftMenuItems: IContextualMenuButton[] = [];
            const moreMenuItems: IContextualMenuButton[] = [];

            this.priorityService
                .sort(items.filter((menuItem: IContextualMenuButton) => menuItem !== null))
                .forEach((menuItem: IContextualMenuButton) => {
                    const collection =
                        leftMenuItems.length < iLeftBtns ? leftMenuItems : moreMenuItems;
                    collection.push(menuItem);
                });

            return {
                leftMenuItems,
                moreMenuItems
            };
        });
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.ContextualMenuService#refreshMenuItems
     * @methodOf smarteditServicesModule.ContextualMenuService
     *
     * @description
     * This method can be used to ask SmartEdit to retrieve again the list of items in the enabled contextual menus.
     */
    public refreshMenuItems(): void {
        this.systemEventService.publishAsync(REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT);
    }

    // Helper Methods
    private _getFeaturesList(_contextualMenus: TypedMap<IContextualMenuButton[]>): string[] {
        // Would be better to use a set for this, but it's not currently supported by all browsers.

        const featuresList = Object.keys(_contextualMenus).reduce(
            (acc: string[], key: string) => [
                ...acc,
                ..._contextualMenus[key].map((entry: IContextualMenuButton) => entry.key)
            ],
            []
        );

        return featuresList.reduce((acc: string[], current: string) => {
            return acc.indexOf(current) < 0 ? [...acc, current] : [...acc];
        }, []);
    }

    private _validateItem(item: IContextualMenuButton): void {
        if (!item.action) {
            throw new Error('Contextual menu item must provide an action: ' + JSON.stringify(item));
        }
        // FIXME: missing case for callbacks
        if (
            (!!item.action.callback && !!item.action.template) ||
            (!!item.action.callback && !!item.action.templateUrl) ||
            (!!item.action.template && !!item.action.templateUrl)
        ) {
            throw new Error(
                'Contextual menu item must have exactly ONE of action callback|callbacks|template|templateUrl'
            );
        }
    }

    private _getUniqueItemArray(
        array1: IContextualMenuButton[],
        array2: IContextualMenuButton[]
    ): IContextualMenuButton[] {
        let currItem: IContextualMenuButton;

        array2.forEach((item) => {
            currItem = item;
            if (array1.every((it: IContextualMenuButton) => currItem.key !== it.key)) {
                array1.push(currItem);
            }
        });

        return array1;
    }

    private _initContextualMenuItems(
        map: TypedMap<IContextualMenuButton[]>,
        componentType: string
    ): void {
        const componentTypeContextualMenus = map[componentType].filter(
            (item: IContextualMenuButton) => {
                this._validateItem(item);
                if (!item.key) {
                    throw new Error("Item doesn't have key.");
                }

                if (this._featuresList.indexOf(item.key) !== -1) {
                    throw new Error(`Item with that key (${item.key}) already exist.`);
                }
                return true;
            }
        );

        this._contextualMenus[componentType] = objectUtils.uniqueArray(
            this._contextualMenus[componentType] || [],
            componentTypeContextualMenus
        );

        this.onContextualMenuItemsAdded.next(componentType);
    }
}
