/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import {
    moduleUtils,
    AbstractDecorator,
    IDecoratorService,
    IUrlService,
    SeDecorator,
    SeEntryModule
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file
@SeDecorator()
@Component({
    selector: 'button-display',
    template: `
        <div>
            <button id="navigateToJSViewInner" (click)="navigateToJSView()">
                Navigate to JS View
            </button>
            <button id="navigateToAngularViewInner" (click)="navigateToAngularView()">
                Navigate to Angular View
            </button>
            <ng-content></ng-content>
        </div>
    `
})
export class ButtonDisplayDecorator extends AbstractDecorator {
    constructor(private urlService: IUrlService) {
        super();
    }

    navigateToJSView() {
        this.urlService.path('/customView');
    }
    navigateToAngularView() {
        this.urlService.path('/ng/a');
    }
}

// angular.module('CMSApp', [])

@SeEntryModule('CMSApp')
@NgModule({
    declarations: [ButtonDisplayDecorator],
    entryComponents: [ButtonDisplayDecorator],
    providers: [
        moduleUtils.provideValues({ e2eMode: true }),
        moduleUtils.bootstrap(
            (decoratorService: any) => {
                decoratorService.addMappings({
                    innerContentType: ['buttonDisplay']
                });

                decoratorService.enable('buttonDisplay');
            },
            [IDecoratorService]
        )
    ]
})
export class CMSApp {}
