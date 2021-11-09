/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, NgModule } from '@angular/core';
import * as angular from 'angular';

import {
    IDropdownMenuItem,
    SeDowngradeComponent,
    SeEntryModule,
    SharedComponentsModule
} from 'smarteditcommons';

@Component({
    selector: 'dropdown-menu-item-mock',
    template: `
        <div>{{ key }}</div>
    `
})
export class MockDropdownMenuItemComponent {
    key: string = 'Mock Item Key';
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <h2>DropdownMenu Tester</h2>
            <div class="btn-group">
                <button
                    id="test-dropdown-menu-item-component-button"
                    (click)="openComponentBasedDropdownMenuItem()"
                >
                    Open with Component Based Dropdown Menu Item
                </button>
                <button
                    id="test-dropdown-menu-item-default-component-button"
                    (click)="openDefaultComponentBasedDropdownMenuItem()"
                >
                    Open with Default Component Based Dropdown Menu Item
                </button>
                <button
                    id="test-dropdown-menu-item-template-button"
                    (click)="openTemplateBasedDropdownMenuItem()"
                >
                    Open with Template Based Dropdown Menu Item
                </button>
                <button
                    id="test-dropdown-menu-item-templateurl-button"
                    (click)="openTemplateUrlBasedDropdownMenuItem()"
                >
                    Open with TemplateUrl Based Dropdown Menu Item
                </button>
            </div>
            <se-dropdown-menu
                [dropdownItems]="dropdownItems"
                [selectedItem]="selectedItem"
            ></se-dropdown-menu>
        </div>
    `,
    styles: [
        `
            se-dropdown-menu {
                display: block;
                margin: 0 auto;
                width: 20px;
            }
        `
    ]
})
export class AppRootComponent {
    dropdownItems: IDropdownMenuItem[] = [];
    selectedItem: any;

    public openComponentBasedDropdownMenuItem(): void {
        this.dropdownItems = [
            {
                component: MockDropdownMenuItemComponent
            }
        ];
    }

    public openDefaultComponentBasedDropdownMenuItem(): void {
        this.dropdownItems = [
            {
                key: 'Default Component Item Key',
                callback() {
                    //
                }
            }
        ];
    }

    public openTemplateBasedDropdownMenuItem(): void {
        this.dropdownItems = [
            {
                template: '<div>Default template Item Key</div>'
            }
        ];
    }

    public openTemplateUrlBasedDropdownMenuItem(): void {
        this.dropdownItems = [
            {
                templateUrl: 'sampleDropdownMenuTemplate.html'
            }
        ];
    }
}

angular.module('dropdownMenuApp', []).run(($templateCache: angular.ITemplateCacheService) => {
    $templateCache.put(
        'sampleDropdownMenuTemplate.html',
        '<div>Default templateUrl Item Key</div>'
    );
});

@SeEntryModule('dropdownMenuApp')
@NgModule({
    imports: [SharedComponentsModule],
    declarations: [AppRootComponent, MockDropdownMenuItemComponent],
    entryComponents: [AppRootComponent, MockDropdownMenuItemComponent]
})
export class DropdownMenuApp {}
