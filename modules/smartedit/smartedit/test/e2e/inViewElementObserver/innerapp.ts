/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    moduleUtils,
    InViewElementObserver,
    SeDowngradeComponent,
    SeEntryModule,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { Component, Inject, InjectionToken, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

const TEST_TARGET_SELECTOR = new InjectionToken('TEST_TARGET_SELECTOR');

// tslint:disable:max-classes-per-file

@SeDowngradeComponent()
@Component({
    selector: 'e-debugger',
    template: `
        <button class="btn btn-info btn-sm toggleDebug" (click)="toggleDebug()">
            <span *ngIf="!showDebug">open</span><span *ngIf="showDebug">close</span> debug
        </button>
        <div *ngIf="showDebug">
            <button
                id="removeFirstComponent"
                class="btn btn-info btn-sm"
                (click)="removeFirstComponent()"
            >
                remove first eligible component
            </button>
            <button
                id="addComponentAsFirst"
                class="btn btn-info btn-sm"
                (click)="addComponentAsFirst()"
            >
                add eligible component as first
            </button>
            <pre>Total eligible components in page: <div id="total-eligible-components"><strong>{{getReallyEligibleElements()}}</strong></div></pre>
            <pre>Total eligible components according to observer: <div id="total-eligible-components-from-observer"><strong>{{getEligibleElements()}}</strong></div></pre>
            <pre>Total visible components according to observer: <div id="total-visible-eligible-components-from-observer"><strong>{{getInViewElements()}}</strong></div></pre>
        </div>
    `
})
export class ScannerDebugComponent {
    public showDebug = true;

    constructor(
        private inViewElementObserver: InViewElementObserver,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        @Inject(TEST_TARGET_SELECTOR) private TEST_TARGET_SELECTOR_VALUE: string
    ) {}

    ngOnInit() {
        this.showDebug = true;
    }

    toggleDebug() {
        this.showDebug = !this.showDebug;
    }

    getReallyEligibleElements() {
        return this.yjQuery(this.TEST_TARGET_SELECTOR_VALUE).length;
    }

    getEligibleElements() {
        return this.inViewElementObserver.getAllElements().length;
    }

    getInViewElements() {
        return this.inViewElementObserver.getInViewElements().length;
    }

    removeFirstComponent() {
        this.yjQuery(this.TEST_TARGET_SELECTOR_VALUE + ':first').remove();
    }

    addComponentAsFirst() {
        this.yjQuery('body').prepend(
            this.yjQuery(
                "<div class='smartEditComponent' data-smartedit-component-type='ContentSlot'>AAAAAA</div>"
            )
        );
    }
}
@SeEntryModule('Innerapp')
@NgModule({
    imports: [CommonModule],
    declarations: [ScannerDebugComponent],
    entryComponents: [ScannerDebugComponent],
    providers: [
        {
            provide: TEST_TARGET_SELECTOR,
            useValue: ".smartEditComponent[data-smartedit-component-type='ContentSlot']"
        },
        moduleUtils.bootstrap(
            (inViewElementObserver: InViewElementObserver, TEST_TARGET_SELECTOR_VALUE: string) => {
                inViewElementObserver.addSelector(TEST_TARGET_SELECTOR_VALUE);
            },
            [InViewElementObserver, TEST_TARGET_SELECTOR]
        )
    ]
})
export class Innerapp {}
