/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { SeInjectable } from '../../di';

export interface YPopupOveryayPosition {
    width: number;
    height: number;
    top: number;
    left: number;
}

/**
 * @ngdoc service
 * @name yPopupOverlayModule.service:yPopupOverlayUtilsDOMCalculations
 *
 * @description
 * Contains some {@link yPopupOverlayModule.directive:yPopupOverlay yPopupOverlay} helper functions for
 * calculating positions and sizes on the DOM
 */

@SeInjectable()
export class YPopupOverlayUtilsDOMCalculations {
    constructor(
        private $window: angular.IWindowService,
        private $document: angular.IDocumentService
    ) {}

    /**
     * @ngdoc method
     * @name yPopupOverlayModule.service:yPopupOverlayUtilsDOMCalculations#adjustHorizontalToBeInViewport
     * @methodOf yPopupOverlayModule.service:yPopupOverlayUtilsDOMCalculations
     *
     * @description
     * Modifies the input rectangle to be absolutely positioned horizontally in the viewport.<br />
     * Does not modify vertical positioning.
     *
     * @param {Object} absPosition A rectangle object representing the size and absolutely positioned location of the overlay
     * @param {number} absPosition.left The left side of the overlay element
     * @param {number} absPosition.width The width of the overlay element
     */
    public adjustHorizontalToBeInViewport(absPosition: YPopupOveryayPosition): void {
        // HORIZONTAL POSITION / SIZE
        // if width of popup is wider then viewport, set it full width
        if (absPosition.width >= this.$window.innerWidth) {
            absPosition.left = 0;
            absPosition.width = this.$window.innerWidth;
        } else {
            const scrollWidth = this.getScrollBarWidth(); // maybe replace this with proper calculated value but im not sure if its worth the cpu cost
            // var scrollWidth = getScrollBarWidth();
            // if right edge of popup would be off the viewport on the right, then
            // move it left until right edge of popup is on right side of viewport
            if (
                absPosition.left - this.$window.pageXOffset + absPosition.width >=
                this.$window.innerWidth - scrollWidth
            ) {
                absPosition.left = this.$window.innerWidth - absPosition.width - scrollWidth;
            }
            // if left edge is off the viewport to left, move to left edge
            if (absPosition.left - this.$window.pageXOffset <= 0) {
                absPosition.left = this.$window.pageXOffset;
            }
        }
    }

    /**
     * @ngdoc method
     * @name yPopupOverlayModule.service:yPopupOverlayUtilsDOMCalculations#calculatePreferredPosition
     * @methodOf yPopupOverlayModule.service:yPopupOverlayUtilsDOMCalculations
     *
     * @description
     * Calculates the preferred position of the overlay, based on the size and position of the anchor
     * and the size of the overlay element
     *
     * @param {Object} anchorBoundingClientRect A bounding rectangle representing the overlay's anchor
     * @param {number} anchorBoundingClientRect.top The top of the anchor, absolutely positioned
     * @param {number} anchorBoundingClientRect.right The right of the anchor, absolutely positioned
     * @param {number} anchorBoundingClientRect.bottom The bottom of the anchor, absolutely positioned
     * @param {number} anchorBoundingClientRect.left The left of the anchor, absolutely positioned
     * @param {number} targetWidth The width of the overlay element
     * @param {number} targetHeight The height of the overlay element
     * @param {string =} [targetValign='bottom'] The preferred vertical alignment, either 'top' or 'bottom'
     * @param {string =} [targetHalign='right'] The preferred horizontal alignment, either 'left' or 'right'
     *
     * @returns {Object} A new size and position for the overlay
     */
    public calculatePreferredPosition(
        anchorBoundingClientRect: ClientRect,
        targetWidth: number,
        targetHeight: number,
        targetValign: 'top' | 'bottom',
        targetHalign: 'left' | 'right'
    ): YPopupOveryayPosition {
        const scrollX = this.$window.pageXOffset;
        const scrollY = this.$window.pageYOffset;
        const position = {
            width: targetWidth,
            height: targetHeight,
            top: 0,
            left: 0
        };

        switch (targetValign) {
            case 'top':
                position.top = anchorBoundingClientRect.top + scrollY - targetHeight;
                break;

            case 'bottom':
            /* falls through */
            default:
                position.top = anchorBoundingClientRect.bottom + scrollY;
        }

        switch (targetHalign) {
            case 'left':
                position.left = anchorBoundingClientRect.right + scrollX - targetWidth;
                break;

            case 'right':
            /* falls through */
            default:
                position.left = anchorBoundingClientRect.left + scrollX;
        }
        return position;
    }

    private getScrollBarWidth(): number {
        const isScrollHeightExceedingClientHeight =
            this.$document[0].body.scrollHeight > this.$document[0].body.clientHeight;

        if (!isScrollHeightExceedingClientHeight) {
            return 0;
        }

        const inner = this.$document[0].createElement('p');
        const outer = this.$document[0].createElement('div');

        inner.style.width = '100%';
        inner.style.height = '200px';

        outer.style.position = 'absolute';
        outer.style.top = '0px';
        outer.style.left = '0px';
        outer.style.visibility = 'hidden';
        outer.style.width = '200px';
        outer.style.height = '150px';
        outer.style.overflow = 'hidden';

        outer.appendChild(inner);

        this.$document[0].body.appendChild(outer);

        const w1 = inner.offsetWidth;
        const w2 = outer.clientWidth;

        outer.style.overflow = 'scroll';

        this.$document[0].body.removeChild(outer);

        return w1 - w2;
    }
}
