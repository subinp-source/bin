/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
    moduleUtils,
    ClientPagedList,
    ClientPagedListCellComponentData,
    ClientPagedListModule,
    CLIENT_PAGED_LIST_CELL_COMPONENT_DATA_TOKEN,
    IPermissionService,
    SeDowngradeComponent,
    SeEntryModule
} from 'smarteditcommons';

@Component({
    selector: 'client-paged-list-cell-component-mock',
    template: `
        <div>mock-{{ data.item[data.key.property] }}</div>
    `
})
export class MockClientPagedListCellComponent {
    constructor(
        @Inject(CLIENT_PAGED_LIST_CELL_COMPONENT_DATA_TOKEN)
        public data: ClientPagedListCellComponentData
    ) {}
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="page-list-search form-group">
            <input type="text" class="form-control" name="query" [(ngModel)]="query" />
        </div>

        <se-client-paged-list
            [items]="items"
            [keys]="keys"
            [sortBy]="'name'"
            [reversed]="false"
            [itemsPerPage]="10"
            [query]="query"
            [displayCount]="true"
            [dropdownItems]="dropdownItems"
            [itemFilterKeys]="itemFilterKeys"
        ></se-client-paged-list>
    `,
    styles: [
        `
            .visited {
                color: purple;
                font-style: italic;
            }
        `
    ]
})
export class AppRootComponent implements ClientPagedList {
    query = '';
    keys = [
        {
            property: 'name',
            i18n: 'pagelist.headerpagetitle'
        },
        {
            property: 'uid',
            i18n: 'pagelist.headerpageid'
        },
        {
            property: 'typeCode',
            i18n: 'pagelist.headerpagetype',
            component: MockClientPagedListCellComponent
        },
        {
            property: 'template',
            i18n: 'pagelist.headerpagetemplate'
        }
    ];

    items = [
        {
            template: 'PageTemplate',
            name: 'page1TitleSuffix',
            typeCode: 'ContentPage',
            uid: 'auid1'
        },
        {
            template: 'ActionTemplate',
            name: 'welcomePage',
            typeCode: 'ActionPage',
            uid: 'uid2'
        },
        {
            template: 'PageTemplate',
            name: 'Advertise',
            typeCode: 'MyCustomType',
            uid: 'uid3',
            icon: {
                src: 'static-resources/images/best-run-sap-logo.svg',
                numberOfRestrictions: 2
            }
        },
        {
            template: 'MyCustomPageTemplate',
            name: 'page2TitleSuffix',
            typeCode: 'HomePage',
            uid: 'uid4'
        },
        {
            template: 'ZTemplate',
            name: 'page3TitleSuffix',
            typeCode: 'ProductPage',
            uid: 'uid5'
        },
        {
            template: 'PageTemplate',
            name: 'page3TitleSuffix',
            typeCode: 'ProductPage',
            uid: 'uid6'
        },
        {
            template: 'PageTemplate',
            name: 'page4TitleSuffix',
            typeCode: 'WallPage',
            uid: 'uid7'
        },
        {
            template: 'PageTemplate',
            name: 'page5TitleSuffix',
            typeCode: 'CheckoutPage',
            uid: 'uid8'
        },
        {
            template: 'PageTemplate',
            name: 'page6TitleSuffix',
            typeCode: 'PromoPage',
            uid: 'uid9'
        },
        {
            template: 'PageTemplate',
            name: 'page7TitleSuffix',
            typeCode: 'ProfilePage',
            uid: 'uid10'
        },
        {
            template: 'PageTemplate',
            name: 'page3TitleSuffix',
            typeCode: 'ProductPage',
            uid: 'uid11'
        },
        {
            template: 'PageTemplate',
            name: 'page3TitleSuffix',
            typeCode: 'ProductPage',
            uid: 'zuid12'
        }
    ];
    itemFilterKeys = ['name', 'uid', 'typeCode', 'template'];
    displayCount = true;
    itemsPerPage = 10;
    reversed = false;
    sortBy = 'name';
    dropdownItems = [
        {
            key: 'pagelist.dropdown.edit',
            callback: () => {
                this.items = [];
            }
        }
    ];
}

function registerPermissions(permissionService: IPermissionService) {
    permissionService.registerRule({
        names: ['se.some.rule'],
        verify: () =>
            Promise.resolve(window.sessionStorage.getItem('PERSPECTIVE_SERVICE_RESULT') === 'true')
    });

    permissionService.registerPermission({
        aliases: ['se.edit.page'],
        rules: ['se.some.rule']
    });
}

@SeEntryModule('clientPagedListApp')
@NgModule({
    imports: [FormsModule, CommonModule, ClientPagedListModule],
    declarations: [AppRootComponent, MockClientPagedListCellComponent],
    entryComponents: [AppRootComponent, MockClientPagedListCellComponent],
    providers: [
        moduleUtils.initialize(
            (permissionService: IPermissionService) => {
                const urlParams = new URLSearchParams(window.location.search);
                window.sessionStorage.setItem(
                    'PERSPECTIVE_SERVICE_RESULT',
                    urlParams.get('perspectiveServiceResult')
                );
                registerPermissions(permissionService);
            },
            [IPermissionService]
        )
    ]
})
export class ClientPagedListApp {}
