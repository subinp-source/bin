/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';

import {
    CompileHtmlNgController,
    IContextualMenuButton,
    POPUP_OVERLAY_DATA
} from 'smarteditcommons';
import { ContextualMenuDecoratorComponent } from './ContextualMenuDecoratorComponent';

@Component({
    template: `
        <div
            *ngIf="item.action.template || item.action.templateUrl || item.action.component"
            class="se-contextual-extra-menu"
        >
            <!-- AngularJS -->

            <div *ngIf="item.action.template">
                <div
                    [seCompileHtml]="item.action.template"
                    [compileHtmlNgController]="legacyController"
                ></div>
            </div>
            <div *ngIf="item.action.templateUrl">
                <div
                    [ngInclude]="item.action.templateUrl"
                    [compileHtmlNgController]="legacyController"
                ></div>
            </div>

            <!-- Angular -->

            <ng-container *ngIf="item.action.component">
                <ng-container *ngComponentOutlet="item.action.component"></ng-container>
            </ng-container>
        </div>
    `,
    selector: 'se-contextual-menu-item-overlay'
})
export class ContextualMenuItemOverlayComponent {
    public legacyController: CompileHtmlNgController;
    constructor(
        @Inject(POPUP_OVERLAY_DATA) private data: { item: IContextualMenuButton },
        @Inject(forwardRef(() => ContextualMenuDecoratorComponent))
        private parent: ContextualMenuDecoratorComponent
    ) {}

    ngOnInit() {
        this.createLegacyController();
    }

    public get item(): IContextualMenuButton {
        return this.data.item;
    }

    private createLegacyController(): void {
        this.legacyController = {
            alias: 'ctrl',
            value: () => this.parent
        };
    }
}
