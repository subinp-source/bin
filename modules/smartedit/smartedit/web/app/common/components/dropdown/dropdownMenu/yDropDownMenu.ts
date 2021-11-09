/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from '../../../di';
import { IDropdownMenuItem } from './IDropdownMenuItem';
import { stringUtils } from '../../../utils';

import './yDropdownMenu.scss';
/**
 * @ngdoc directive
 * @name smarteditCommonsModule.directive:yDropDownMenu
 * @scope
 * @restrict E
 * @element y-drop-down-menu
 *
 * @deprecated since 2005, use {@link smarteditCommonsModule.component:DropdownMenuComponent DropdownMenuComponent}
 *
 * @description
 *
 * Deprecated since 2005, use {@link smarteditCommonsModule.component:DropdownMenuComponent DropdownMenuComponent}
 */
@SeComponent({
    selector: 'y-drop-down-menu',
    templateUrl: 'yDropDownMenuTemplate.html',
    inputs: ['dropdownItems', 'selectedItem']
})
export class YDropDownMenuComponent {
    public dropdownItems: IDropdownMenuItem[];
    public selectedItem: IDropdownMenuItem;
    public clonedDropdownItems: IDropdownMenuItem[];

    constructor(private $templateCache: angular.ITemplateCacheService) {}

    $onChanges() {
        // cloning binded object

        this.clonedDropdownItems = [...this.dropdownItems]
            .map((item) => ({
                ...item,
                condition: item.condition || (() => true)
            }))
            .map((item) => this.setTemplateUrl(item));
    }

    isDeleteButton(dropdownItem: IDropdownMenuItem): boolean {
        return dropdownItem.key && dropdownItem.key.indexOf('remove') !== -1;
    }

    private setTemplateUrl(dropdownItem: IDropdownMenuItem): IDropdownMenuItem {
        switch (this._validatePassedAttribute(dropdownItem)) {
            case 'callback':
                return {
                    ...dropdownItem,
                    templateUrl: 'yDropdownDefaultItemTemplate.html'
                };

            case 'template':
                // replacing 'template' with cached 'templateUrl'
                return {
                    ...dropdownItem,
                    templateUrl: this._cacheDropdownItemTemplate(dropdownItem),
                    template: null
                };

            default:
                return dropdownItem;
        }
    }

    private _cacheDropdownItemTemplate(dropdownItem: IDropdownMenuItem): string {
        const nameOfCachedTemplate =
            'yDropdownItem_' +
            stringUtils.getEncodedString(dropdownItem.template) +
            '_Template.html';

        if (!this.$templateCache.get(nameOfCachedTemplate)) {
            this.$templateCache.put(nameOfCachedTemplate, dropdownItem.template);
        }

        return nameOfCachedTemplate;
    }

    private _validatePassedAttribute(dropdownItem: IDropdownMenuItem): string {
        const expectedAttributesAndTypes = {
            callback: 'function',
            template: 'string',
            templateUrl: 'string'
        };
        const expectedAttributes = Object.keys(expectedAttributesAndTypes);
        const passedAttributes = Object.keys(dropdownItem);
        const validatedAttribute = passedAttributes.filter((attribute) => {
            return expectedAttributes.indexOf(attribute) !== -1;
        });

        if (validatedAttribute.length !== 1) {
            throw new Error('Please provide only one of callback, template or templateUrl.');
        }

        if (
            typeof dropdownItem[
                validatedAttribute[0] as 'callback' | 'template' | 'templateUrl'
            ] !==
            expectedAttributesAndTypes[
                validatedAttribute[0] as 'callback' | 'template' | 'templateUrl'
            ]
        ) {
            throw new Error(
                'Please provide a parameter of a proper type: callback(Function), template(String) or templateUrl(String).'
            );
        }
        return validatedAttribute[0];
    }
}
