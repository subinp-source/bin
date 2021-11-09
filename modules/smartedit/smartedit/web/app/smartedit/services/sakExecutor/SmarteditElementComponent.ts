/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, ElementRef, Input } from '@angular/core';

import {
    ComponentUpdatedEventInfo,
    CrossFrameEventService,
    ELEMENT_UUID_ATTRIBUTE,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_REFRESHED,
    EVENT_SMARTEDIT_COMPONENT_UPDATED,
    PolyfillService,
    SeCustomComponent,
    SystemEventService,
    SMARTEDIT_DRAG_AND_DROP_EVENTS
} from 'smarteditcommons';
import { SakExecutorService } from './SakExecutorService';

const smarteditElementComponentSelector = 'smartedit-element';

@SeCustomComponent()
@Component({
    selector: smarteditElementComponentSelector,
    template: `
        <div id="projectedContent"><ng-content></ng-content></div>
    `
})
export class SmarteditElementComponent {
    @Input('data-smartedit-component-id')
    public smarteditComponentId: string;

    @Input('data-smartedit-component-uuid')
    public smarteditComponentUuid: string;

    @Input('data-smartedit-component-type')
    public smarteditComponentType: string;

    @Input('data-smartedit-container-id')
    public smarteditContainerId: string;

    @Input('data-smartedit-container-type')
    public smarteditContainerType: string;

    public active: boolean = false;

    private componentDecoratorEnabled: boolean = true;

    private projectedContent: HTMLElement;
    private bundle: HTMLElement;

    private unregisterPerspectiveChangeEvent: () => void;
    private unregisterPerspectiveRefreshedEvent: () => void;
    private unregisterComponentUpdatedEvent: () => void;
    private unregisterDnDEnd: () => void;
    private unregisterDnDStart: () => void;
    private mousenterListener: (event: Event) => void;
    private mouseleaveListener: (event: Event) => void;

    constructor(
        private elementRef: ElementRef,
        private sakExecutorService: SakExecutorService,
        private systemEventService: SystemEventService,
        private crossFrameEventService: CrossFrameEventService,
        private polyfillService: PolyfillService
    ) {}

    ngOnInit() {
        const projectedContentWrapper = this.element.querySelector('#projectedContent');
        this.projectedContent = projectedContentWrapper.children[0] as HTMLElement;

        this.element.removeChild(projectedContentWrapper);
        this.appendDecorators();
    }

    ngAfterViewInit() {
        this.mousenterListener = (event: Event) => {
            if (!this.active) {
                this.active = this.componentDecoratorEnabled;
                this.propagateActiveStateToChildren();
            }
        };

        this.mouseleaveListener = (event: Event) => {
            if (this.active) {
                this.active = false;
                this.propagateActiveStateToChildren();
            }
        };

        // Register Event Listeners
        this.element.addEventListener('mouseenter', this.mousenterListener);
        this.element.addEventListener('mouseleave', this.mouseleaveListener);

        this.unregisterPerspectiveChangeEvent = this.crossFrameEventService.subscribe(
            EVENT_PERSPECTIVE_CHANGED,
            (eventId: string, notPreview: boolean) => {
                notPreview && this.appendDecorators();
            }
        );
        this.unregisterPerspectiveRefreshedEvent = this.crossFrameEventService.subscribe(
            EVENT_PERSPECTIVE_REFRESHED,
            (eventId: string, notPreview: boolean) => {
                notPreview && this.appendDecorators();
            }
        );
        this.unregisterComponentUpdatedEvent = this.crossFrameEventService.subscribe(
            EVENT_SMARTEDIT_COMPONENT_UPDATED,
            this.onComponentUpdated.bind(this)
        );

        // we can't listen to these events in the controller because they could be sent before the component compilation.
        this.unregisterDnDStart = this.systemEventService.subscribe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
            (
                eventId: string,
                smarteditComponentClosestToDraggedElement: angular.IAugmentedJQuery
            ) => {
                if (
                    this.polyfillService.isEligibleForEconomyMode() &&
                    smarteditComponentClosestToDraggedElement &&
                    this.uuid ===
                        smarteditComponentClosestToDraggedElement.attr(ELEMENT_UUID_ATTRIBUTE)
                ) {
                    this.componentDecoratorEnabled = false;
                    this.removeDecorators();
                }
            }
        );

        this.unregisterDnDEnd = this.systemEventService.subscribe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END,
            () => {
                if (this.polyfillService.isEligibleForEconomyMode()) {
                    this.componentDecoratorEnabled = true;
                    this.appendDecorators();
                }
            }
        );
    }

    ngOnDestroy() {
        this.removeDecorators();
        this.unregisterPerspectiveChangeEvent();
        this.unregisterPerspectiveRefreshedEvent();
        this.unregisterComponentUpdatedEvent();
        this.unregisterDnDEnd();
        this.unregisterDnDStart();
        this.element.removeEventListener('mouseenter', this.mousenterListener);
        this.element.removeEventListener('mouseleave', this.mouseleaveListener);

        delete this.bundle;
    }

    private removeDecorators = () => {
        // removing previous content of placeHolder
        if (this.bundle && this.element.contains(this.bundle)) {
            this.element.removeChild(this.bundle);
        }
        this.bundle = this.projectedContent;
        this.element.appendChild(this.bundle);
    };

    private appendDecorators = () => {
        // removing previous content of placeHolder
        if (this.bundle && this.element.contains(this.bundle)) {
            this.element.removeChild(this.bundle);
        }

        this.sakExecutorService
            .wrapDecorators(this.projectedContent, this.element)
            .then((bundle: HTMLElement) => {
                this.bundle = bundle;
                this.element.appendChild(this.bundle);
            });
    };

    private onComponentUpdated(eventId: string, componentUpdatedData: ComponentUpdatedEventInfo) {
        const requiresReplayingDecorators =
            componentUpdatedData && componentUpdatedData.requiresReplayingDecorators;
        const isCurrentComponent =
            componentUpdatedData &&
            componentUpdatedData.componentId === this.smarteditComponentId &&
            componentUpdatedData.componentType === this.smarteditComponentType;

        if (isCurrentComponent && requiresReplayingDecorators) {
            this.appendDecorators();
        }
    }

    private propagateActiveStateToChildren = () => {
        this.element.querySelectorAll('[active]').forEach((element: HTMLElement) => {
            if (this.uuid === this.getUuid(element)) {
                element.setAttribute('active', this.active + '');
            }
        });
    };

    private getUuid(element: HTMLElement): string {
        return element.getAttribute(ELEMENT_UUID_ATTRIBUTE);
    }

    private get uuid(): string {
        return this.element.getAttribute(ELEMENT_UUID_ATTRIBUTE);
    }

    private get element(): HTMLElement {
        return this.elementRef.nativeElement;
    }
}
