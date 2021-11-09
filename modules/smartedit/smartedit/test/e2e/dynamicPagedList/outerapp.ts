/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    DynamicPagedListConfig,
    DynamicPagedListInjectedContext,
    DynamicPagedListModule,
    IDropdownMenuItem,
    IPermissionService,
    SeDowngradeComponent,
    SeEntryModule,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { FormsModule } from '@angular/forms';

// tslint:disable:max-classes-per-file

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="page-list-search form-group smartedit-testing-overlay">
            <input type="text" class="form-control" [(ngModel)]="query.value" name="query" />
        </div>

        <se-dynamic-paged-list
            [config]="pagedListConfig"
            [mask]="query.value"
        ></se-dynamic-paged-list>
    `
})
export class AppRootComponent {
    public query = {
        value: ''
    };

    public pagedListConfig: DynamicPagedListConfig = {
        sortBy: 'name',
        queryParams: null,
        reversed: false,
        itemsPerPage: 10,
        displayCount: true,
        uri: '/pagedItems',
        keys: [
            {
                property: 'name',
                i18n: 'pagelist.headerpagetitle',
                sortable: true
            },
            {
                property: 'uid',
                i18n: 'pagelist.headerpageid',
                sortable: false
            },
            {
                property: 'typeCode',
                i18n: 'pagelist.headerpagetype',
                sortable: false
            },
            {
                property: 'template',
                i18n: 'pagelist.headerpagetemplate',
                sortable: false
            },
            {
                property: 'dropdownitems',
                i18n: '',
                sortable: false
            }
        ],
        renderers: {
            name() {
                return '<a data-ng-click="$ctrl.config.injectedContext.changeColor($index)">{{ item[column.property] }}</a>';
            },
            uid() {
                return "<span class='custom'> {{ item[column.property] }} </span>";
            },
            dropdownitems() {
                return '<div has-operation-permission="$ctrl.config.injectedContext.permissionForDropdownItems" class="paged-list-table__body__td paged-list-table__body__td-menu"><se-dropdown-menu [dropdown-items]="$ctrl.config.injectedContext.dropdownItems" [selected-item]="item" class="pull-right"></se-dropdown-menu></div>';
            }
        },
        dropdownItems: this.getDropdownItems(),
        injectedContext: {
            changeColor: (index: number) => {
                const nth = index + 1;
                this.yjQuery(
                    '.se-paged-list-table tbody tr:nth-child(' +
                        nth +
                        ') .se-paged-list-item-name a'
                ).addClass('visited');
            },
            dropdownItems: this.getDropdownItems(),
            permissionForDropdownItems: 'se.edit.page'
        } as DynamicPagedListInjectedContext
    };

    constructor(@Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic) {}

    private getDropdownItems(): IDropdownMenuItem[] {
        return [
            {
                key: 'pagelist.dropdown.edit',
                callback: () => {
                    this.yjQuery(
                        '.se-paged-list-table tbody tr:nth-child(1) .se-paged-list-item-name a'
                    ).addClass('link-clicked');
                }
            }
        ];
    }
}
@SeEntryModule('OuterApp')
@NgModule({
    imports: [CommonModule, DynamicPagedListModule, FormsModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent],
    providers: [
        moduleUtils.bootstrap(
            (permissionService: IPermissionService) => {
                const urlParams = new URLSearchParams(window.location.search);
                window.sessionStorage.setItem(
                    'PERSPECTIVE_SERVICE_RESULT',
                    urlParams.get('perspectiveServiceResult')
                );

                permissionService.registerRule({
                    names: ['se.some.rule'],
                    verify() {
                        return Promise.resolve(
                            window.sessionStorage.getItem('PERSPECTIVE_SERVICE_RESULT') === 'true'
                        );
                    }
                });

                permissionService.registerPermission({
                    aliases: ['se.edit.page'],
                    rules: ['se.some.rule']
                });
            },
            [IPermissionService]
        )
    ]
})
export class OuterApp {}

window.pushModules(OuterApp);
