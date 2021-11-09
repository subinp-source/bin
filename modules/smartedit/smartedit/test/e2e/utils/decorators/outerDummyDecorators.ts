/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';

import {
    moduleUtils,
    AbstractDecorator,
    CrossFrameEventService,
    EVENT_SMARTEDIT_COMPONENT_UPDATED,
    IDecoratorService,
    IPageInfoService,
    SeDecorator,
    SeEntryModule
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file
@SeDecorator()
@Component({
    selector: 'text-display',
    template: `
        <div>
            <ng-content></ng-content>
            {{ textDisplayContent }}
        </div>
    `
})
export class TextDisplayDecorator extends AbstractDecorator {
    public textDisplayContent = 'Text_is_been_displayed_TextDisplayDecorator';
}

@SeDecorator()
@Component({
    selector: 'slot-text-display',
    template: `
        <div>
            <ng-content></ng-content>
            {{ textDisplayContent }}
        </div>
    `
})
export class SlotTextDisplayDecorator extends AbstractDecorator {
    public textDisplayContent = 'slot_text_is_been_displayed_SlotTextDisplayDecorator';
}

@SeDecorator()
@Component({
    selector: 'button-display',
    template: `
        <div>
            <ng-content></ng-content>
            <button>{{ buttonDisplayContent }}</button>
        </div>
    `
})
export class ButtonDisplayDecorator extends AbstractDecorator {
    public buttonDisplayContent = 'Button_is_been_Displayed';
}

@SeDecorator()
@Component({
    selector: 'slot-button-display',
    template: `
        <div>
            <ng-content></ng-content>
            <button>{{ buttonDisplayContent }}</button>
        </div>
    `
})
export class SlotButtonDisplayDecorator extends AbstractDecorator {
    public buttonDisplayContent = 'Slot_button_is_been_Displayed';
}

@SeDecorator()
@Component({
    selector: 'page-specific',
    template: `
        <div>
            <ng-content></ng-content>
            <label>pageUUID: {{ pageUUID }}</label>
        </div>
    `
})
export class PageSpecificDecorator extends AbstractDecorator {
    public pageUUID = '';

    constructor(private pageInfoService: IPageInfoService) {
        super();
    }

    ngOnInit() {
        this.pageInfoService.getPageUUID().then((pageUUID: string) => {
            this.pageUUID = pageUUID;
        });
    }
}

@SeDecorator()
@Component({
    selector: 'button-display-and-refresh',
    template: `
        <div>
            <div>
                <button class="main-button" (click)="onButtonClicked()">
                    {{ buttonDisplayContent }}
                </button>
                <button class="refresh-button" (click)="onRefreshButtonClicked()">
                    Refresh Content
                </button>
            </div>
            <ng-content></ng-content>
        </div>
    `
})
export class ButtonDisplayAndRefreshDecorator extends AbstractDecorator {
    public buttonDisplayContent = 'Button_is_been_Displayed';

    constructor(private crossFrameEventService: CrossFrameEventService) {
        super();
    }

    onButtonClicked() {
        this.buttonDisplayContent = 'Button_has_been_Clicked';
    }

    onRefreshButtonClicked() {
        this.crossFrameEventService.publish(EVENT_SMARTEDIT_COMPONENT_UPDATED, {
            componentId: this.smarteditComponentId,
            componentType: this.smarteditComponentType,
            requiresReplayingDecorators: true
        });
    }
}

@SeEntryModule('DummyDecoratorsModule')
@NgModule({
    declarations: [
        TextDisplayDecorator,
        SlotTextDisplayDecorator,
        ButtonDisplayDecorator,
        SlotButtonDisplayDecorator,
        PageSpecificDecorator,
        ButtonDisplayAndRefreshDecorator
    ],
    entryComponents: [
        TextDisplayDecorator,
        SlotTextDisplayDecorator,
        ButtonDisplayDecorator,
        SlotButtonDisplayDecorator,
        PageSpecificDecorator,
        ButtonDisplayAndRefreshDecorator
    ],
    providers: [
        moduleUtils.bootstrap(
            (decoratorService: IDecoratorService) => {
                decoratorService.addMappings({
                    componentType1: ['textDisplay', 'pageSpecific'],
                    componentType2: ['buttonDisplay'],
                    SimpleResponsiveBannerComponent: ['textDisplay', 'buttonDisplay'],
                    componentType4: ['textDisplay', 'buttonDisplayAndRefresh'],
                    ContentSlot: ['slotTextDisplay', 'slotButtonDisplay']
                });

                decoratorService.enable('textDisplay');
                decoratorService.enable('buttonDisplay');
                decoratorService.enable('slotTextDisplay');
                decoratorService.enable('slotButtonDisplay');
                decoratorService.enable('pageSpecific');
                decoratorService.enable('buttonDisplayAndRefresh');
            },
            [IDecoratorService]
        )
    ]
})
export class DummyDecoratorsModule {}
