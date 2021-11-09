/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, Input, OnDestroy, OnInit, Type } from '@angular/core';

import {
    CrossFrameEventService,
    EVENT_SERVICE,
    HIDE_TOOLBAR_ITEM_CONTEXT,
    SHOW_TOOLBAR_ITEM_CONTEXT
} from 'smarteditcommons';

/** @internal  */

@Component({
    selector: 'se-toolbar-item-context',
    template: `
        <div
            *ngIf="displayContext"
            id="toolbar_item_context_{{ itemKey }}_btn"
            class="se-toolbar-actionable-item-context"
            [ngClass]="{ 'se-toolbar-actionable-item-context--open': isOpen }"
        >
            <!-- AngularJS -->

            <div
                *ngIf="contextTemplateUrl"
                class="se-toolbar-actionable-context__btn"
                [ngInclude]="contextTemplateUrl"
            ></div>

            <!-- Angular -->

            <div *ngIf="contextComponent" class="se-toolbar-actionable-context__btn">
                <ng-container *ngComponentOutlet="contextComponent"></ng-container>
            </div>
        </div>
    `
})
export class ToolbarItemContextComponent implements OnInit, OnDestroy {
    @Input() itemKey: string;
    @Input() isOpen: boolean;
    @Input() contextTemplateUrl: string;
    @Input() contextComponent: Type<any>;

    public displayContext: boolean = false;

    private unregShowContext: () => void;
    private unregHideContext: () => void;

    constructor(@Inject(EVENT_SERVICE) private crossFrameEventService: CrossFrameEventService) {}

    ngOnInit() {
        this.registerCallbacks();
    }

    ngOnDestroy() {
        this.unregShowContext();
        this.unregHideContext();
    }

    public showContext(show: boolean): void {
        this.displayContext = show;
    }

    private registerCallbacks(): void {
        this.unregShowContext = this.crossFrameEventService.subscribe(
            SHOW_TOOLBAR_ITEM_CONTEXT,
            (eventId: string, itemKey: string) => {
                if (itemKey === this.itemKey) {
                    this.showContext(true);
                }
            }
        );

        this.unregHideContext = this.crossFrameEventService.subscribe(
            HIDE_TOOLBAR_ITEM_CONTEXT,
            (eventId: string, itemKey: string) => {
                if (itemKey === this.itemKey) {
                    this.showContext(false);
                }
            }
        );
    }
}
