/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, HostListener, Inject, Input } from '@angular/core';

import { CompileHtmlNgController, IContextualMenuButton } from 'smarteditcommons';
import { SlotContextualMenuDecoratorComponent } from './SlotContextualMenuDecoratorComponent';

@Component({
    selector: 'slot-contextual-menu-item',
    template: `
        <div *ngIf="!item.action.templateUrl || !item.action.component">
            <span
                *ngIf="item.iconIdle && parent.isHybrisIcon(item.displayClass)"
                id="{{ item.i18nKey | translate }}-{{ parent.smarteditComponentId }}-{{
                    parent.smarteditComponentType
                }}-hyicon"
                (click)="parent.triggerMenuItemAction(item, $event)"
                [ngClass]="{ clickable: true }"
            >
                <img
                    [src]="isHovered ? item.iconNonIdle : item.iconIdle"
                    id="{{ item.i18nKey | translate }}-{{ parent.smarteditComponentId }}-{{
                        parent.smarteditComponentType
                    }}-hyicon-img"
                    title="{{ item.i18nKey | translate }}"
                />
            </span>
            <img
                [title]="item.i18nKey | translate"
                *ngIf="item.iconIdle && !parent.isHybrisIcon(item.displayClass)"
                [ngClass]="{ clickable: true }"
                (click)="parent.triggerMenuItemAction(item, $event)"
                [src]="isHovered ? item.iconNonIdle : item.iconIdle"
                [alt]="item.i18nKey"
                class="{{ item.displayClass }}"
                id="{{ item.i18nKey | translate }}-{{ parent.smarteditComponentId }}-{{
                    parent.smarteditComponentType
                }}"
            />
        </div>

        <!-- AngularJS -->

        <div *ngIf="item.action.templateUrl">
            <div
                [ngInclude]="item.action.templateUrl"
                [compileHtmlNgController]="legacyController"
            ></div>
        </div>

        <!-- Angular -->

        <div *ngIf="item.action.component">
            <ng-container *ngComponentOutlet="item.action.component"></ng-container>
        </div>
    `
})
export class SlotContextualMenuItemComponent {
    @Input() item: IContextualMenuButton;

    public isHovered: boolean;
    public legacyController: CompileHtmlNgController;

    constructor(
        @Inject(forwardRef(() => SlotContextualMenuDecoratorComponent))
        public parent: SlotContextualMenuDecoratorComponent
    ) {}

    @HostListener('mouseover') onMouseOver() {
        this.isHovered = true;
    }
    @HostListener('mouseout') onMouseOut() {
        this.isHovered = false;
    }

    ngOnInit() {
        this.createLegacyController();
    }

    ngOnChanges() {
        this.createLegacyController();
    }

    private createLegacyController(): void {
        this.legacyController = {
            alias: 'ctrl',
            value: () => this.parent
        };
    }
}
