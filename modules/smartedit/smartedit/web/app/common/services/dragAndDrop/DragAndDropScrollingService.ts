/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { Inject } from '@angular/core';
import { WindowUtils } from '@smart/utils';
import { TranslateService } from '@ngx-translate/core';

import { YJQUERY_TOKEN } from '../vendors/YjqueryModule';
import { SeDowngradeService } from '../../di';
import { InViewElementObserver } from './InViewElementObserver';
import { SCROLL_AREA_CLASS, THROTTLE_SCROLLING_DELAY } from '../../utils';

/* @internal */
@SeDowngradeService()
export class DragAndDropScrollingService {
    private static readonly TOP_SCROLL_AREA_ID = 'top_scroll_page';
    private static readonly BOTTOM_SCROLL_AREA_ID = 'bottom_scroll_page';

    private SCROLLING_AREA_HEIGHT = 50;
    private FAST_SCROLLING_AREA_HEIGHT = 25;
    private SCROLLING_STEP = 5;
    private FAST_SCROLLING_STEP = 15;

    private topScrollArea: JQuery<HTMLElement> = null;
    private bottomScrollArea: JQuery<HTMLElement> = null;

    private throttleScrollingEnabled = false;

    private scrollLimitY: number;
    private scrollDelta: number;
    private initialized: boolean;
    private scrollable: JQuery<HTMLElement>;
    private throttledScrollPage: () => void;
    private animationFrameId: number;

    constructor(
        private windowUtils: WindowUtils,
        private translate: TranslateService,
        private inViewElementObserver: InViewElementObserver,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic
    ) {
        this.inViewElementObserver.addSelector(
            '#' + DragAndDropScrollingService.TOP_SCROLL_AREA_ID
        );
        this.inViewElementObserver.addSelector(
            '#' + DragAndDropScrollingService.BOTTOM_SCROLL_AREA_ID
        );

        this.throttledScrollPage = lo.throttle(
            this.scrollPage.bind(this),
            THROTTLE_SCROLLING_DELAY
        );
    }

    initialize(): void {
        this.scrollable = this.getSelector(
            this.windowUtils.getWindow().document.scrollingElement
        ) as JQuery<HTMLElement>;

        this.addScrollAreas();
        this.addEventListeners();

        this.scrollDelta = 0;
        this.initialized = true;
    }

    deactivate(): void {
        this.removeEventListeners();

        this.scrollDelta = 0;
        this.initialized = false;
    }

    enable(): void {
        if (this.initialized) {
            // Calculate limits based on current state.
            this.scrollLimitY =
                this.scrollable.get(0).scrollHeight - this.windowUtils.getWindow().innerHeight;
            this.showScrollAreas();
        }
    }

    disable(): void {
        if (this.initialized) {
            const scrollAreas = this.getScrollAreas();
            // following trigger necessary to remove scrollable areas when loosing track of the mouse from the outer layer
            scrollAreas.trigger('dragleave');
            scrollAreas.hide();
        }
    }

    toggleThrottling(isEnabled: boolean): void {
        this.throttleScrollingEnabled = isEnabled;
    }

    private addScrollAreas(): Promise<void> {
        this.topScrollArea = this.getSelector(
            '<div id="' +
                DragAndDropScrollingService.TOP_SCROLL_AREA_ID +
                '" class="' +
                SCROLL_AREA_CLASS +
                '"></div>'
        ).appendTo('body') as JQuery<HTMLElement>;
        this.bottomScrollArea = this.getSelector(
            '<div id="' +
                DragAndDropScrollingService.BOTTOM_SCROLL_AREA_ID +
                '" class="' +
                SCROLL_AREA_CLASS +
                '"></div>'
        ).appendTo('body') as JQuery<HTMLElement>;

        const scrollAreas = this.getScrollAreas();
        scrollAreas.height(this.SCROLLING_AREA_HEIGHT);

        this.topScrollArea.css({
            top: 0
        });
        this.bottomScrollArea.css({
            bottom: 0
        });

        scrollAreas.hide();

        let topMessage: string;
        let bottomMessage: string;

        return this.translate
            .get('se.draganddrop.uihint.top')
            .toPromise()
            .then((localizedTopMessage: string) => {
                topMessage = localizedTopMessage;
                return this.translate.get('se.draganddrop.uihint.bottom').toPromise();
            })
            .then((localizedBottomMsg: string) => {
                bottomMessage = localizedBottomMsg;

                this.topScrollArea.text(topMessage);
                this.bottomScrollArea.text(bottomMessage);
            });
    }

    private addEventListeners(): void {
        const scrollAreas = this.getScrollAreas();

        scrollAreas.on('dragenter', this.onDragEnter.bind(this));
        scrollAreas.on('dragover', this.onDragOver.bind(this));
        scrollAreas.on('dragleave', this.onDragLeave.bind(this));
    }

    private removeEventListeners(): void {
        const scrollAreas = this.getScrollAreas();

        scrollAreas.off('dragenter');
        scrollAreas.off('dragover');
        scrollAreas.off('dragleave');

        scrollAreas.remove();
    }

    // Event Listeners
    private onDragEnter(event: DragEvent): void {
        let scrollDelta = this.SCROLLING_STEP;
        const scrollArea = this.getSelector(event.target as HTMLElement);
        const scrollAreaId = scrollArea.attr('id');
        if (scrollAreaId === DragAndDropScrollingService.TOP_SCROLL_AREA_ID) {
            scrollDelta *= -1;
        }

        this.scrollDelta = scrollDelta;

        this.animationFrameId = this.windowUtils
            .getWindow()
            .requestAnimationFrame(this.scrollPage.bind(this));
    }

    private onDragOver(evt: JQueryEventObject): void {
        const event = evt.originalEvent as DragEvent;
        const scrollArea = this.getSelector(event.target as HTMLElement);
        const scrollAreaId = scrollArea.attr('id');

        if (scrollAreaId === DragAndDropScrollingService.TOP_SCROLL_AREA_ID) {
            if (event.clientY <= this.FAST_SCROLLING_AREA_HEIGHT) {
                this.scrollDelta = -this.FAST_SCROLLING_STEP;
            } else {
                this.scrollDelta = -this.SCROLLING_STEP;
            }
        } else {
            const windowHeight = this.windowUtils.getWindow().innerHeight;

            if (event.clientY >= windowHeight - this.FAST_SCROLLING_AREA_HEIGHT) {
                this.scrollDelta = this.FAST_SCROLLING_STEP;
            } else {
                this.scrollDelta = this.SCROLLING_STEP;
            }
        }
    }

    private onDragLeave(): void {
        this.scrollDelta = 0;
        this.windowUtils.getWindow().cancelAnimationFrame(this.animationFrameId);
    }

    private scrollPage(): void {
        if (this.scrollDelta) {
            const scrollTop = this.scrollable.scrollTop();
            let continueScrolling = false;

            if (this.scrollDelta > 0 && scrollTop < this.scrollLimitY) {
                continueScrolling = true;
            } else if (this.scrollDelta < 0 && scrollTop > 0) {
                continueScrolling = true;
            }

            if (continueScrolling) {
                const current = this.scrollable.scrollTop();
                const next = current + this.scrollDelta;
                this.scrollable.scrollTop(next);
                this.animationFrameId = this.windowUtils
                    .getWindow()
                    .requestAnimationFrame(
                        this.throttleScrollingEnabled
                            ? this.throttledScrollPage
                            : this.scrollPage.bind(this)
                    );
            }

            this.showScrollAreas();
        }
    }

    private getSelector(selector: string | Element): JQuery<HTMLElement> {
        return this.yjQuery(selector);
    }

    private getScrollAreas(): JQuery<HTMLElement> {
        return this.yjQuery(
            [
                '#' + DragAndDropScrollingService.TOP_SCROLL_AREA_ID,
                '#' + DragAndDropScrollingService.BOTTOM_SCROLL_AREA_ID
            ].join(',')
        );
    }

    private showScrollAreas(): void {
        const scrollTop = this.scrollable.scrollTop();

        if (scrollTop === 0) {
            this.topScrollArea.hide();
        } else {
            this.topScrollArea.show();
        }

        if (scrollTop >= this.scrollLimitY) {
            this.bottomScrollArea.hide();
        } else {
            this.bottomScrollArea.show();
        }
    }
}
