/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input } from '@angular/core';
import { SeDowngradeComponent } from '../../di';
import { Placement } from 'popper.js';

/**
 * @ngdoc component
 * @name TooltipComponent
 * @element se-tooltip
 *
 * @description
 * Used to display content in a popover after trigger is applied
 *  E.g usage
 *  <se-tooltip [triggers]="mouseover">
 *      <span se-tooltip-trigger>Hover me</span>
 *      <p se-tooltip-body>Content</p>
 *  </se-tooltip>
 *  @param {string[]} triggers Array of strings defining what event triggers popover to appear. Accepts any DOM {@link https://www.w3schools.com/jsref/dom_obj_event.asp events}
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-tooltip',
    template: `
        <fd-popover
            [triggers]="triggers"
            [placement]="placement"
            [appendTo]="appendTo"
            [noArrow]="!isChevronVisible"
            class="se-tooltip"
        >
            <fd-popover-control>
                <ng-content select="[se-tooltip-trigger]"></ng-content>
            </fd-popover-control>
            <fd-popover-body>
                <div class="popover se-popover">
                    <h3 class="se-popover__title" *ngIf="title">{{ title | translate }}</h3>

                    <div class="se-popover__content">
                        <ng-content select="[se-tooltip-body]"></ng-content>
                    </div>
                </div>
            </fd-popover-body>
        </fd-popover>
    `
})
export class TooltipComponent {
    @Input() triggers: string[];
    @Input() placement: Placement;
    @Input() title: string;
    @Input() appendTo: HTMLElement | 'body';
    @Input() isChevronVisible: boolean;
}
