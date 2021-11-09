/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    AbstractDecorator,
    IDecoratorService,
    ID_ATTRIBUTE,
    IRenderService,
    SeDecorator,
    SeEntryModule,
    TYPE_ATTRIBUTE,
    YJQUERY_TOKEN
} from 'smarteditcommons';

function buildComponentQuery(
    smarteditComponentId: string,
    smarteditComponentType: string,
    cssClass?: string
): string {
    let query: string = '';
    query += cssClass ? '.' + cssClass : '';
    query += '[' + ID_ATTRIBUTE + "='" + smarteditComponentId + "']";
    query += '[' + TYPE_ATTRIBUTE + "='" + smarteditComponentType + "']";
    return query;
}
// tslint:disable:max-classes-per-file

@SeDecorator()
@Component({
    selector: 'dirty-content-decorator',
    template: `
        <div>
            <button
                style="position: absolute; z-index: 20; right: 250px"
                id="{{ smarteditComponentId }}-dirty-content-button"
                (click)="dirtyContent()"
            >
                {{ buttonDisplayContent }}
            </button>
            <ng-content></ng-content>
        </div>
    `
})
export class DirtyContentDecorator extends AbstractDecorator {
    buttonDisplayContent = 'Dirty Component';

    constructor(@Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic) {
        super();
    }

    dirtyContent() {
        const element = this.yjQuery(
            buildComponentQuery(this.smarteditComponentId, this.smarteditComponentType)
        );
        element.find('p').html('Some dirtied content');
    }
}
@SeDecorator()
@Component({
    selector: 'render-component-decorator',
    template: `
        <div>
            <button
                style="position: absolute; z-index: 20; right: 125px"
                id="{{ smarteditComponentId }}-render-button-inner"
                (click)="renderNewContent()"
            >
                {{ buttonDisplayContent }}
            </button>
            <ng-content></ng-content>
        </div>
    `
})
export class RenderComponentDecorator extends AbstractDecorator {
    buttonDisplayContent = 'Re-render Component';

    constructor(private renderService: IRenderService) {
        super();
    }

    renderNewContent() {
        this.renderService.renderComponent(this.smarteditComponentId, this.smarteditComponentType);
    }
}
@SeDecorator()
@Component({
    selector: 'render-slot-decorator',
    template: `
        <div>
            <button
                style="position: absolute; z-index: 20; right: 0"
                id="{{ smarteditComponentId }}-render-slot-button-inner"
                (click)="renderNewContent()"
            >
                {{ buttonDisplayContent }}
            </button>
            <ng-content></ng-content>
        </div>
    `
})
export class RenderSlotDecorator extends AbstractDecorator {
    buttonDisplayContent = 'Re-render Slot';

    constructor(private renderService: IRenderService) {
        super();
    }

    renderNewContent() {
        this.renderService.renderSlots([this.smarteditComponentId]);
    }
}

@SeEntryModule('InnerRenderDecorators')
@NgModule({
    imports: [CommonModule],
    declarations: [DirtyContentDecorator, RenderComponentDecorator, RenderSlotDecorator],
    entryComponents: [DirtyContentDecorator, RenderComponentDecorator, RenderSlotDecorator],
    providers: [
        moduleUtils.bootstrap(
            (decoratorService: IDecoratorService) => {
                decoratorService.addMappings({
                    'componentType*': ['renderComponentDecorator', 'dirtyContentDecorator'],
                    ContentSlot: ['renderSlotDecorator']
                });

                decoratorService.enable('renderComponentDecorator');
                decoratorService.enable('dirtyContentDecorator');
                decoratorService.enable('renderSlotDecorator');
            },
            [IDecoratorService]
        )
    ]
})
export class InnerRenderDecorators {}
