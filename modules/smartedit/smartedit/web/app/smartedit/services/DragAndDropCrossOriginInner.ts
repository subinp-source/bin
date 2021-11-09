/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CrossFrameEventService,
    DragAndDropScrollingService,
    InViewElementObserver,
    IDragAndDropCrossOrigin,
    IDragEventType,
    IMousePosition,
    PolyfillService,
    SeDowngradeService,
    SMARTEDIT_DRAG_AND_DROP_EVENTS,
    SMARTEDIT_ELEMENT_HOVERED,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';

/** @internal */
@SeDowngradeService(IDragAndDropCrossOrigin)
export class DragAndDropCrossOrigin extends IDragAndDropCrossOrigin {
    private currentElementHovered: JQuery<Element>;
    private lastElementHovered: JQuery<Element>;
    private isSearchingElement: boolean;

    constructor(
        @Inject(DOCUMENT) private document: Document,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private crossFrameEventService: CrossFrameEventService,
        private inViewElementObserver: InViewElementObserver,
        private dragAndDropScrollingService: DragAndDropScrollingService,
        private polyfillService: PolyfillService
    ) {
        super();
    }

    initialize(): void {
        this.crossFrameEventService.subscribe<IMousePosition>(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.TRACK_MOUSE_POSITION,
            this.onTrackMouseInner.bind(this)
        );
        this.crossFrameEventService.subscribe<IMousePosition>(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DROP_ELEMENT,
            this.onDropElementInner.bind(this)
        );
        this.crossFrameEventService.subscribe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_CROSS_ORIGIN_START,
            this.onDnDCrossOriginStart.bind(this)
        );
    }

    private onDnDCrossOriginStart = (eventId: string) => {
        this.dragAndDropScrollingService.toggleThrottling(
            this.polyfillService.isEligibleForThrottledScrolling()
        );
    };

    private onTrackMouseInner = (eventId: string, eventData: IMousePosition) => {
        if (this.isSearchingElement) {
            return;
        }
        this.isSearchingElement = true;

        /**
         * Get the element from mouse position.
         * In IE11, document.elementFromPoint returns null because of the #ySmartEditFrameDragArea positioned over the iframe and has pointer-events (necessary to listen on 'dragover' to track the mouse position).
         * To polyfill document.elementFromPoint in IE11 in this scenario, we call isPointOverElement() on each elligible droppable element.
         *
         * Note: in IE11, a switch of pointer-events value to none for the #ySmartEditFrameDragArea will return a value when calling $document.elementFromPoint, BUT it is causing cursor flickering and too much latency. The 'isPointOverElement' approach give better results.
         */
        this.currentElementHovered = this.yjQuery(
            this.inViewElementObserver.elementFromPoint(eventData)
        ) as JQuery<HTMLElement>;
        const mousePositionInPage: IMousePosition = this.getMousePositionInPage(eventData);

        if (this.lastElementHovered && this.lastElementHovered.length) {
            if (
                (this.currentElementHovered.length &&
                    this.lastElementHovered[0] !== this.currentElementHovered[0]) ||
                !this.currentElementHovered.length
            ) {
                this.dispatchDragEvent(
                    this.lastElementHovered[0],
                    IDragEventType.DRAG_LEAVE,
                    mousePositionInPage
                );
                this.lastElementHovered.data(SMARTEDIT_ELEMENT_HOVERED, false);
            }
        }

        if (this.currentElementHovered.length) {
            if (!this.currentElementHovered.data(SMARTEDIT_ELEMENT_HOVERED)) {
                this.dispatchDragEvent(
                    this.currentElementHovered[0],
                    IDragEventType.DRAG_ENTER,
                    mousePositionInPage
                );
                this.currentElementHovered.data(SMARTEDIT_ELEMENT_HOVERED, true);
            }

            this.dispatchDragEvent(
                this.currentElementHovered[0],
                IDragEventType.DRAG_OVER,
                mousePositionInPage
            );
        }

        this.lastElementHovered = this.currentElementHovered;
        this.isSearchingElement = false;
    };

    private onDropElementInner = (eventId: string, mousePosition: IMousePosition) => {
        if (this.currentElementHovered.length) {
            this.currentElementHovered.data(SMARTEDIT_ELEMENT_HOVERED, false);
            this.dispatchDragEvent(
                this.currentElementHovered[0],
                IDragEventType.DROP,
                mousePosition
            );
            this.dispatchDragEvent(
                this.currentElementHovered[0],
                IDragEventType.DRAG_LEAVE,
                mousePosition
            );
        }
    };

    private dispatchDragEvent(
        element: Element,
        type: IDragEventType,
        mousePosition: IMousePosition
    ): void {
        const evt: CustomEvent = this.document.createEvent('CustomEvent');
        evt.initCustomEvent(type, true, true, null);
        (evt as any).dataTransfer = {
            data: {},
            setData(_type: string, val: object) {
                this.data[_type] = val;
            },
            getData(_type: string) {
                return this.data[_type];
            }
        };
        (evt as any).pageX = mousePosition.x;
        (evt as any).pageY = mousePosition.y;
        element.dispatchEvent(evt);
    }

    private getMousePositionInPage(mousePosition: IMousePosition): IMousePosition {
        const scrollingElement: JQuery = this.yjQuery(
            this.document.scrollingElement || this.document.documentElement
        ) as JQuery<HTMLElement>;
        return {
            x: mousePosition.x + scrollingElement.scrollLeft(),
            y: mousePosition.y + scrollingElement.scrollTop()
        };
    }
}
