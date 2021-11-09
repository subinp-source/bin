/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces useValue:false */
import { Component, Injector, Input, OnInit } from '@angular/core';

import { CompileHtmlNgController } from 'smarteditcommons/directives';
import { IDropdownMenuItem } from './IDropdownMenuItem';
import {
    DropdownMenuItemDefaultComponent,
    DROPDOWN_MENU_ITEM_DATA
} from './DropdownMenuItemDefaultComponent';

/** @internal */
@Component({
    selector: 'se-dropdown-menu-item',
    templateUrl: './DropdownMenuItemComponent.html'
})
export class DropdownMenuItemComponent implements OnInit {
    @Input() dropdownItem: IDropdownMenuItem;
    @Input() selectedItem: any;

    public compileHtmlNgController: CompileHtmlNgController;
    public dropdownItemInjector: Injector;

    constructor(private injector: Injector) {}

    ngOnInit() {
        if (this.dropdownItem.templateUrl) {
            this.createCompileHtmlNgController();
            return;
        }

        if (this.dropdownItem.component === DropdownMenuItemDefaultComponent) {
            this.createDropdownItemInjector();
        }
    }

    private createCompileHtmlNgController(): void {
        const { selectedItem } = this;
        this.compileHtmlNgController = {
            alias: '$ctrl',
            value() {
                this.selectedItem = selectedItem;
            }
        };
    }

    /**
     * Creates Injector for DropdownMenuItemDefaultComponent
     */
    private createDropdownItemInjector(): void {
        const { selectedItem } = this;
        this.dropdownItemInjector = Injector.create({
            parent: this.injector,
            providers: [
                {
                    provide: DROPDOWN_MENU_ITEM_DATA,
                    useValue: {
                        dropdownItem: this.dropdownItem,
                        selectedItem
                    }
                }
            ]
        });
    }
}
