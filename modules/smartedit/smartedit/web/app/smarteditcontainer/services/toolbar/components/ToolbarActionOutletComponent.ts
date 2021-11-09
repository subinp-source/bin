/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    forwardRef,
    Component,
    Inject,
    Injector,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges
} from '@angular/core';

import {
    CompileHtmlNgController,
    ToolbarItemInternal,
    ToolbarItemType,
    ToolbarSection
} from 'smarteditcommons';
import { ToolbarComponent } from './ToolbarComponent';

/** @internal  */

@Component({
    selector: 'se-toolbar-action-outlet',
    template: `
        <div
            class="se-template-toolbar__action-template"
            [ngClass]="{
                'se-toolbar-action': isSectionRight,
                'se-toolbar-action--active': isSectionRight && isPermitionGranted
            }"
        >
            <!-- AngularJS TEMPLATE type-->

            <div
                *ngIf="item.include && item.type === type.TEMPLATE"
                [ngInclude]="item.include"
                [compileHtmlNgController]="legacyController"
                [scope]="{ item: item }"
            ></div>

            <!-- Angular TEMPLATE type-->

            <ng-container *ngIf="item.component && item.type === type.TEMPLATE">
                <ng-container
                    *ngComponentOutlet="item.component; injector: actionInjector"
                ></ng-container>
            </ng-container>

            <!--ACTION and HYBRID_ACTION types-->

            <div *ngIf="!item.include || item.type !== type.TEMPLATE">
                <se-toolbar-action [item]="item"></se-toolbar-action>
            </div>
        </div>
    `
})
export class ToolbarActionOutletComponent implements OnInit, OnChanges {
    @Input() item: ToolbarItemInternal;

    public legacyController: CompileHtmlNgController;
    public actionInjector: Injector;
    public type = ToolbarItemType;

    constructor(@Inject(forwardRef(() => ToolbarComponent)) private toolbar: ToolbarComponent) {}

    ngOnInit() {
        this.setup();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.item) {
            this.setup();
        }
    }

    public get isSectionRight(): boolean {
        return this.item.section === ToolbarSection.right;
    }

    public get isPermitionGranted(): boolean {
        return this.item.isPermissionGranted;
    }

    private setup() {
        this.legacyController = this.toolbar.createLegacyController();
        this.actionInjector = this.toolbar.createInjector(this.item);
    }
}
