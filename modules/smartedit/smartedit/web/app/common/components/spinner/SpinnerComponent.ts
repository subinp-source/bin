/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { SeDowngradeComponent } from '../../di';

@SeDowngradeComponent()
@Component({
    selector: 'se-spinner',
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: `
        <div
            *ngIf="isSpinning"
            class="se-spinner panel-body"
            [ngClass]="{ 'se-spinner--fluid': isFluid }"
        >
            <div class="spinner">
                <div class="spinner-container spinner-container1">
                    <div class="spinner-circle1"></div>
                    <div class="spinner-circle2"></div>
                    <div class="spinner-circle3"></div>
                    <div class="circle4"></div>
                </div>
                <div class="spinner-container spinner-container2">
                    <div class="spinner-circle1"></div>
                    <div class="spinner-circle2"></div>
                    <div class="spinner-circle3"></div>
                    <div class="circle4"></div>
                </div>
                <div class="spinner-container spinner-container3">
                    <div class="spinner-circle1"></div>
                    <div class="spinner-circle2"></div>
                    <div class="spinner-circle3"></div>
                    <div class="circle4"></div>
                </div>
            </div>
        </div>
    `
})
export class SpinnerComponent {
    @Input() isSpinning: boolean;
    @Input() isFluid: boolean = true;
}
