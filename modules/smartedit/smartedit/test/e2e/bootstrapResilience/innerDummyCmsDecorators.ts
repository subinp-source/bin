/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    moduleUtils,
    AbstractDecorator,
    IDecoratorService,
    SeDecorator,
    SeEntryModule
} from 'smarteditcommons';
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DUMMY_SERVICE_CLASS_TOKEN } from './outerMocks';

// tslint:disable:max-classes-per-file

export class DummyServiceClass {
    getDecoratorClass() {
        return 'greenBackground';
    }
}

@SeDecorator()
@Component({
    selector: 'text-display',
    template: `
        <div class="{{ class }}">
            <div class="row" *ngIf="!active"></div>

            {{ textDisplayContent }}

            <ng-content></ng-content>
        </div>
    `
})
export class TextDisplayDecorator extends AbstractDecorator {
    public textDisplayContent: string;
    public class: string;

    constructor(@Inject(DUMMY_SERVICE_CLASS_TOKEN) private service: DummyServiceClass) {
        super();
    }
    ngOnInit() {
        this.textDisplayContent = this.smarteditComponentId + '_Text_from_dummy_decorator';
        this.class = this.service.getDecoratorClass();
    }
}

@SeEntryModule('InnerDummyCmsDecoratorsModule')
@NgModule({
    imports: [CommonModule],
    declarations: [TextDisplayDecorator],
    entryComponents: [TextDisplayDecorator],
    providers: [
        { provide: DUMMY_SERVICE_CLASS_TOKEN, useClass: DummyServiceClass },
        moduleUtils.bootstrap(
            (decoratorService: IDecoratorService) => {
                decoratorService.addMappings({
                    componentType1: ['textDisplay']
                });

                decoratorService.enable('textDisplay');
            },
            [IDecoratorService]
        )
    ]
})
export class InnerDummyCmsDecoratorsModule {}
