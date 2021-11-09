/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    IDecoratorService,
    IRestServiceFactory,
    RestServiceFactory,
    SeDowngradeComponent,
    SeEntryModule,
    WindowUtils
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file
@SeDowngradeComponent()
@Component({
    selector: 'fake1',
    template: `
        <div>
            <div (mouseleave)="onMouseLeave()">
                <div (mouseenter)="onMouseEnter()">
                    <ng-content></ng-content>

                    <span id="fake1" *ngIf="found">fake1</span>
                </div>
            </div>
        </div>
    `
})
export class Fake1DecoratorComponent {
    public found: boolean = false;
    public visible: boolean = false;

    constructor(public restServiceFactory: IRestServiceFactory) {}

    ngOnInit() {
        this.restServiceFactory
            .get('https://api1/somepath/id')
            .get()
            .then(
                () => {
                    this.found = true;
                    console.log('fake1Decorator get api1 success');
                },
                () => {
                    console.log('fake1Decorator get api1 failed');
                }
            );
    }

    onMouseLeave(): void {
        setTimeout(() => {
            this.visible = false;
        }, 1000);
    }

    onMouseEnter(): void {
        this.visible = true;
    }
}

@SeDowngradeComponent()
@Component({
    selector: 'fake2',
    template: `
        <div>
            <div (mouseleave)="onMouseLeave()">
                <div (mouseenter)="onMouseEnter()">
                    <ng-content></ng-content>

                    <span id="fake2" *ngIf="found">fake2</span>
                </div>
            </div>
        </div>
    `
})
export class Fake2DecoratorComponent {
    public found: boolean = false;
    public visible: boolean = false;

    constructor(private restServiceFactory: IRestServiceFactory) {}

    ngOnInit() {
        // so that we make sure login 2 pops up after login 1

        setTimeout(() => {
            this.restServiceFactory
                .get('https://api2/someotherpath/id')
                .get()
                .then(
                    () => {
                        this.found = true;
                        console.log('fake1Decorator get api2 success');
                    },
                    () => {
                        console.log('fake1Decorator get api2 failed');
                    }
                );
        }, 1000);
    }

    onMouseLeave(): void {
        setTimeout(() => {
            this.visible = false;
        }, 1000);
    }

    onMouseEnter(): void {
        this.visible = true;
    }
}

@SeEntryModule('DummyCmsDecoratorsModule')
@NgModule({
    imports: [CommonModule],
    declarations: [Fake1DecoratorComponent, Fake2DecoratorComponent],
    entryComponents: [Fake1DecoratorComponent, Fake2DecoratorComponent],
    providers: [
        moduleUtils.bootstrap(
            (
                decoratorService: IDecoratorService,
                windowUtils: WindowUtils,
                restServiceFactory: RestServiceFactory
            ) => {
                decoratorService.addMappings({
                    componentType1: ['fake1'],
                    componentType2: ['fake2']
                });

                decoratorService.enable('fake1');
                decoratorService.enable('fake2');

                windowUtils.runIntervalOutsideAngular(() => {
                    if (sessionStorage.getItem('sso.authenticate.failure') !== 'true') {
                        restServiceFactory.get('/smartedit/configuration').query();
                    }
                }, 500);
            },
            [IDecoratorService, WindowUtils, IRestServiceFactory]
        )
    ]
})
export class DummyCmsDecoratorsModule {}

window.pushModules(DummyCmsDecoratorsModule);
