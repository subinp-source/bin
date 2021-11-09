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
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// tslint:disable:max-classes-per-file

@SeDecorator()
@Component({
    selector: 'text-display',
    template: `
        <div>
            <div class="row" *ngIf="!active"></div>

            {{ textDisplayContent }}
            <ng-content></ng-content>
        </div>
        '
    `
})
export class TextDisplayDecorator extends AbstractDecorator {
    public textDisplayContent = '';

    ngOnInit() {
        this.textDisplayContent = this.smarteditComponentId + '_Text_from_dummy_decorator';
    }
}
@SeEntryModule('InnerDecoratorsModule')
@NgModule({
    imports: [CommonModule],
    providers: [
        moduleUtils.bootstrap(
            (decoratorService: IDecoratorService) => {
                decoratorService.addMappings({
                    componentType1: ['textDisplay']
                });

                decoratorService.enable('textDisplay');
            },
            [IDecoratorService]
        )
    ],
    declarations: [TextDisplayDecorator],
    entryComponents: [TextDisplayDecorator]
})
export class InnerDecoratorsModule {}
