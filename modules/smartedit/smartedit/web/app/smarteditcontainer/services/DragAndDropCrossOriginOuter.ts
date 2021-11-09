/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { Inject } from '@angular/core';

import {
    CrossFrameEventService,
    DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME,
    IDragAndDropCrossOrigin,
    IMousePosition,
    SeDowngradeService,
    SEND_MOUSE_POSITION_THROTTLE,
    SMARTEDIT_DRAG_AND_DROP_EVENTS,
    SMARTEDIT_IFRAME_DRAG_AREA,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { IframeManagerService } from './iframe/IframeManagerService';

/**
 * Polyfill for HTML5 Drag and Drop in a cross-origin setup.
 * Most browsers (except Firefox) do not allow on-page drag-and-drop from non-same-origin frames.
 * This service is a polyfill to allow it, by listening the 'dragover' event over a sibling <div> of the iframe and sending the mouse position to the inner frame.
 * The inner frame 'DragAndDropCrossOriginInner' will use document.elementFromPoint (or isPointOverElement helper function for IE only) to determine the current hovered element and then dispatch drag events onto elligible droppable elements.
 *
 * More information about security restrictions:
 * https://bugs.chromium.org/p/chromium/issues/detail?id=251718
 * https://bugs.chromium.org/p/chromium/issues/detail?id=59081
 * https://www.infosecurity-magazine.com/news/new-google-chrome-clickjacking-vulnerability/
 * https://bugzilla.mozilla.org/show_bug.cgi?id=605991
 */

/** @internal */
@SeDowngradeService(IDragAndDropCrossOrigin)
export class DragAndDropCrossOrigin extends IDragAndDropCrossOrigin {
    private throttledSendMousePosition: (mousePosition: IMousePosition) => void;

    constructor(
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private crossFrameEventService: CrossFrameEventService,
        private iframeManagerService: IframeManagerService
    ) {
        super();
    }

    initialize(): void {
        this.throttledSendMousePosition = lo.throttle(
            this.sendMousePosition,
            SEND_MOUSE_POSITION_THROTTLE
        );
        this.crossFrameEventService.subscribe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
            this.onDragStart
        );
        this.crossFrameEventService.subscribe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END,
            this.onDragEnd
        );

        this.crossFrameEventService.subscribe(DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME.START, () => {
            if (this.isEnabled()) {
                this.iframeManagerService.getIframe().css('pointer-events', 'none');
            }
        });
        this.crossFrameEventService.subscribe(DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME.END, () => {
            if (this.isEnabled()) {
                this.iframeManagerService.getIframe().css('pointer-events', 'auto');
            }
        });
    }

    private isEnabled() {
        return this.iframeManagerService.isCrossOrigin();
    }

    private onDragStart = () => {
        if (!this.isEnabled()) {
            return;
        }

        this.crossFrameEventService.publish(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_CROSS_ORIGIN_START
        );

        this.syncIframeDragArea()
            .show()
            .off('dragover') // `off()` is necessary since dragEnd event is not always fired.
            .on('dragover', (e: JQuery.Event) => {
                e.preventDefault(); // `preventDefault()` is necessary for the 'drop' event callback to be fired.
                const mousePosition: IMousePosition = this.getPositionRelativeToIframe(
                    e.pageX,
                    e.pageY
                );
                this.throttledSendMousePosition(mousePosition);
                return false;
            })
            .off('drop')
            .on('drop', (e: JQuery.Event) => {
                e.preventDefault();
                e.stopPropagation();
                const mousePosition: IMousePosition = this.getPositionRelativeToIframe(
                    e.pageX,
                    e.pageY
                );
                this.crossFrameEventService.publish(
                    SMARTEDIT_DRAG_AND_DROP_EVENTS.DROP_ELEMENT,
                    mousePosition
                );
                return false;
            });
    };

    private onDragEnd = () => {
        if (!this.isEnabled()) {
            return;
        }

        this.getIframeDragArea()
            .off('dragover')
            .off('drop')
            .hide();
    };

    private sendMousePosition = (mousePosition: IMousePosition) => {
        this.crossFrameEventService.publish(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.TRACK_MOUSE_POSITION,
            mousePosition
        );
    };

    private getIframeDragArea(): JQuery {
        return this.yjQuery('#' + SMARTEDIT_IFRAME_DRAG_AREA);
    }

    private getPositionRelativeToIframe(posX: number, posY: number): IMousePosition {
        const iframeOffset: JQuery.Coordinates = this.getIframeDragArea().offset();
        return {
            x: posX - iframeOffset.left,
            y: posY - iframeOffset.top
        };
    }

    private syncIframeDragArea(): JQuery {
        this.getIframeDragArea().width(this.iframeManagerService.getIframe().width());
        this.getIframeDragArea().height(this.iframeManagerService.getIframe().height());

        const iframeOffset: JQuery.Coordinates = this.iframeManagerService.getIframe().offset();
        this.getIframeDragArea().css({
            top: iframeOffset.top,
            left: iframeOffset.left
        });

        return this.getIframeDragArea();
    }
}
