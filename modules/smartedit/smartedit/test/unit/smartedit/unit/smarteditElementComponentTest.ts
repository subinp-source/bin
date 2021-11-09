/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ElementRef } from '@angular/core';

import { SmarteditElementComponent } from 'smartedit/services/sakExecutor/SmarteditElementComponent';
import {
    stringUtils,
    CrossFrameEventService,
    ELEMENT_UUID_ATTRIBUTE,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_REFRESHED,
    EVENT_SMARTEDIT_COMPONENT_UPDATED,
    PolyfillService,
    SystemEventService,
    SMARTEDIT_DRAG_AND_DROP_EVENTS
} from 'smarteditcommons';
import { SakExecutorService } from 'smartedit/services/sakExecutor/SakExecutorService';
import { promiseHelper } from 'testhelpers';

describe('SmarteditElementComponent Test', () => {
    const $q = promiseHelper.$q();
    let sakExecutorService: jasmine.SpyObj<SakExecutorService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let polyfillService: jasmine.SpyObj<PolyfillService>;
    let elementRef: ElementRef;
    let nativeElement: HTMLElement;
    let outputFromWrappedDecorators: HTMLElement;

    const smarteditElementUuid = 'theSmarteditElementUuid';
    let smarteditElementComponent: SmarteditElementComponent;

    let smarteditComponentClosestToDraggedElement: jasmine.SpyObj<angular.IAugmentedJQuery>;
    let perspectiveChangedCallback: (eventId: string, notPreview: boolean) => void;
    let perspectiveRefreshedCallback: (eventId: string, notPreview: boolean) => void;
    let dNDStartCallback: (
        eventId: string,
        smarteditComponentClosestToDraggedElement?: angular.IAugmentedJQuery
    ) => void;
    let dNDENDCallback: (eventId: string) => void;
    let mouseenterListenerCallback: (event?: Event) => void;
    let mouseleaveListenerCallback: (event?: Event) => void;

    const unregisterersMap = {
        unregisterPerspectiveChangeEvent: () => {
            //
        },
        unregisterPerspectiveRefreshedEvent: () => {
            //
        },
        unregisterDnDEnd: () => {
            //
        },
        unregisterDnDStart: () => {
            //
        },
        unregisterComponentUpdatedEvent: () => {
            //
        }
    };

    let unregisterPerspectiveChangeEventSpy: jasmine.Spy;
    let unregisterPerspectiveRefreshedEventSpy: jasmine.Spy;
    let unregisterDnDEndSpy: jasmine.Spy;
    let unregisterDnDStartSpy: jasmine.Spy;
    let addEventListenerSpy: jasmine.Spy;
    let removeEventListenerSpy: jasmine.Spy;

    const ORIGINAL_ELEMENT = `<smartedit-element data-smartedit-element-uuid="theSmarteditElementUuid">
			<some-projected-content>someProjectedContent</some-projected-content>
	</smartedit-element>`;

    function PROCESSED_ELEMENT(active: boolean): string {
        return `<smartedit-element data-smartedit-element-uuid="theSmarteditElementUuid">
				<outputfromwrappeddecorators active="${active}" data-smartedit-element-uuid="theSmarteditElementUuid"></outputfromwrappeddecorators>
		</smartedit-element>`;
    }

    beforeEach(() => {
        unregisterPerspectiveChangeEventSpy = spyOn(
            unregisterersMap,
            'unregisterPerspectiveChangeEvent'
        );
        unregisterPerspectiveRefreshedEventSpy = spyOn(
            unregisterersMap,
            'unregisterPerspectiveRefreshedEvent'
        );
        unregisterDnDEndSpy = spyOn(unregisterersMap, 'unregisterDnDEnd');
        unregisterDnDStartSpy = spyOn(unregisterersMap, 'unregisterDnDStart');

        smarteditComponentClosestToDraggedElement = jasmine.createSpyObj<angular.IAugmentedJQuery>(
            'smarteditComponentClosestToDraggedElement',
            ['attr']
        );
        sakExecutorService = jasmine.createSpyObj<SakExecutorService>('sakExecutorService', [
            'wrapDecorators'
        ]);

        outputFromWrappedDecorators = document.createElement('outputFromWrappedDecorators');

        sakExecutorService.wrapDecorators.and.callFake(
            (projectedContent: HTMLElement, element: HTMLElement) => {
                if (
                    projectedContent.outerHTML.replace(/\s/g, '') ===
                        '<some-projected-content>someProjectedContent</some-projected-content>' &&
                    element.tagName === 'SMARTEDIT-ELEMENT'
                ) {
                    outputFromWrappedDecorators.setAttribute('active', 'false');
                    outputFromWrappedDecorators.setAttribute(
                        'data-smartedit-element-uuid',
                        'theSmarteditElementUuid'
                    );
                    return $q.when(outputFromWrappedDecorators);
                } else {
                    throw new Error(
                        `unexpected parameters sent to sakExecutorService.wrapDecorators: projectedContent ${projectedContent} and element.tagName: ${
                            element.tagName
                        }`
                    );
                }
            }
        );

        crossFrameEventService = jasmine.createSpyObj<CrossFrameEventService>(
            'crossFrameEventService',
            ['subscribe']
        );
        crossFrameEventService.subscribe.and.callFake(
            (eventId: string, callback: (eventId: string, notPreview: boolean) => void) => {
                if (eventId === EVENT_PERSPECTIVE_CHANGED) {
                    perspectiveChangedCallback = callback;
                    return unregisterersMap.unregisterPerspectiveChangeEvent;
                } else if (eventId === EVENT_PERSPECTIVE_REFRESHED) {
                    perspectiveRefreshedCallback = callback;
                    return unregisterersMap.unregisterPerspectiveRefreshedEvent;
                } else if (eventId === EVENT_SMARTEDIT_COMPONENT_UPDATED) {
                    return unregisterersMap.unregisterComponentUpdatedEvent;
                } else {
                    throw new Error(
                        `unexpected eventId ${eventId} passed to crossFrameEventService.subscribe`
                    );
                }
            }
        );

        systemEventService = jasmine.createSpyObj<SystemEventService>('systemEventService', [
            'subscribe'
        ]);
        systemEventService.subscribe.and.callFake(
            (
                eventId: string,
                callback: (
                    eventId: string,
                    smarteditComponentClosestToDraggedElement?: angular.IAugmentedJQuery
                ) => void
            ) => {
                if (eventId === SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START) {
                    dNDStartCallback = callback;
                    return unregisterersMap.unregisterDnDStart;
                } else if (eventId === SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END) {
                    dNDENDCallback = callback;
                    return unregisterersMap.unregisterDnDEnd;
                } else {
                    throw new Error(
                        `unexpected eventId ${eventId} passed to systemEventService.subscribe`
                    );
                }
            }
        );

        polyfillService = jasmine.createSpyObj<PolyfillService>('polyfillService', [
            'isEligibleForEconomyMode'
        ]);

        elementRef = jasmine.createSpyObj<ElementRef>('elementRef', ['nativeElement']);

        nativeElement = window.smarteditJQuery(
            `<smartedit-element data-smartedit-element-uuid="${smarteditElementUuid}">
                <div id="projectedContent">
                    <some-projected-content>someProjectedContent</some-projected-content>
                </div>
            </smartedit-element>
                `
        )[0];
        document.body.appendChild(nativeElement);

        elementRef.nativeElement = nativeElement;

        addEventListenerSpy = spyOn(nativeElement, 'addEventListener');
        addEventListenerSpy.and.callFake((eventId: string, callback: (event: Event) => void) => {
            if (eventId === 'mouseenter') {
                mouseenterListenerCallback = callback;
            } else if (eventId === 'mouseleave') {
                mouseleaveListenerCallback = callback;
            } else {
                throw new Error(`unexpected eventId ${eventId} passed to addEventListener`);
            }
        });

        removeEventListenerSpy = spyOn(nativeElement, 'removeEventListener');

        smarteditElementComponent = new SmarteditElementComponent(
            elementRef,
            sakExecutorService,
            systemEventService,
            crossFrameEventService,
            polyfillService
        );

        smarteditElementComponent.ngOnInit();
        smarteditElementComponent.ngAfterViewInit();
    });

    afterEach(() => {
        document.body.removeChild(nativeElement);
    });

    it('event listeners are registered', () => {
        expect(crossFrameEventService.subscribe.calls.count()).toBe(3);
        expect(crossFrameEventService.subscribe.calls.argsFor(0)[0]).toBe(
            EVENT_PERSPECTIVE_CHANGED
        );
        expect(crossFrameEventService.subscribe.calls.argsFor(1)[0]).toBe(
            EVENT_PERSPECTIVE_REFRESHED
        );
        expect(crossFrameEventService.subscribe.calls.argsFor(2)[0]).toBe(
            EVENT_SMARTEDIT_COMPONENT_UPDATED
        );

        expect(systemEventService.subscribe.calls.count()).toBe(2);
        expect(systemEventService.subscribe.calls.argsFor(0)[0]).toBe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START
        );
        expect(systemEventService.subscribe.calls.argsFor(1)[0]).toBe(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END
        );

        expect(addEventListenerSpy.calls.count()).toBe(2);
        expect(addEventListenerSpy.calls.argsFor(0)[0]).toBe('mouseenter');
        expect(addEventListenerSpy.calls.argsFor(1)[0]).toBe('mouseleave');
    });
    // add JQuery listeners
    it('on destroy, all listeners are removed', () => {
        smarteditElementComponent.ngOnDestroy();

        expect(unregisterPerspectiveChangeEventSpy).toHaveBeenCalled();
        expect(unregisterPerspectiveRefreshedEventSpy).toHaveBeenCalled();
        expect(unregisterDnDEndSpy).toHaveBeenCalled();
        expect(unregisterDnDStartSpy).toHaveBeenCalled();

        expect(removeEventListenerSpy.calls.count()).toBe(2);
        expect(removeEventListenerSpy.calls.argsFor(0)[0]).toBe('mouseenter');
        expect(removeEventListenerSpy.calls.argsFor(1)[0]).toBe('mouseleave');
    });

    it("on init, projected content is removed, fed to sakExecutor that returns a 'non active' node appended to the native element", () => {
        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on destroy, native element returns to initial state (not a real case but easier to test removal of decorators)', () => {
        smarteditElementComponent.ngOnDestroy();

        sakExecutorService.wrapDecorators.calls.reset();

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(ORIGINAL_ELEMENT)
        );
    });

    it('in economy mode, decorators are removed from dragged element when drag and drop starts', () => {
        polyfillService.isEligibleForEconomyMode.and.returnValue(true);
        setElementUuidOfDraggedElement(smarteditElementUuid);

        sakExecutorService.wrapDecorators.calls.reset();

        dNDStartCallback(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
            smarteditComponentClosestToDraggedElement
        );

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(ORIGINAL_ELEMENT)
        );
    });

    it('in economy mode, decorators are not removed from non dragged element when drag and drop starts', () => {
        polyfillService.isEligibleForEconomyMode.and.returnValue(true);
        setElementUuidOfDraggedElement('sdfasdfads');

        sakExecutorService.wrapDecorators.calls.reset();

        dNDStartCallback(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
            smarteditComponentClosestToDraggedElement
        );

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('in non economy mode, decorators are not removed from dragged element when drag and drop starts', () => {
        polyfillService.isEligibleForEconomyMode.and.returnValue(false);
        setElementUuidOfDraggedElement(smarteditElementUuid);

        sakExecutorService.wrapDecorators.calls.reset();

        dNDStartCallback(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
            smarteditComponentClosestToDraggedElement
        );

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('in economy mode, decorators are restored from dragged element when drag and drop ends', () => {
        polyfillService.isEligibleForEconomyMode.and.returnValue(true);

        dNDStartCallback(
            SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
            smarteditComponentClosestToDraggedElement
        );

        sakExecutorService.wrapDecorators.calls.reset();

        dNDENDCallback(SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END);

        expect(sakExecutorService.wrapDecorators).toHaveBeenCalledTimes(1);

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('in non economy mode, decorators are restored from dragged element when drag and drop ends', () => {
        polyfillService.isEligibleForEconomyMode.and.returnValue(false);

        sakExecutorService.wrapDecorators.calls.reset();

        dNDENDCallback(SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END);

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on perspective change in preview, decorators are not replayed', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        perspectiveChangedCallback(EVENT_PERSPECTIVE_CHANGED, false);

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on perspective change in non preview, decorators are replayed', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        perspectiveChangedCallback(EVENT_PERSPECTIVE_CHANGED, true);

        expect(sakExecutorService.wrapDecorators).toHaveBeenCalledTimes(1);

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on perspective refresh in preview, decorators are not replayed', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        perspectiveRefreshedCallback(EVENT_PERSPECTIVE_CHANGED, false);

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on perspective refresh in non preview, decorators are replayed', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        perspectiveRefreshedCallback(EVENT_PERSPECTIVE_CHANGED, true);

        expect(sakExecutorService.wrapDecorators).toHaveBeenCalledTimes(1);

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on mouseleave if not active, decorators  are set inactive', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        smarteditElementComponent.active = false;
        mouseleaveListenerCallback();

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on mouseleave if active, decorators set inactive', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        smarteditElementComponent.active = true;
        mouseleaveListenerCallback();

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on mouseenter if not active and decorators are enabled, decorators are set active', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        smarteditElementComponent.active = false;
        (smarteditElementComponent as any).componentDecoratorEnabled = mouseenterListenerCallback();

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(true))
        );
    });

    it('on mouseenter if not active and decorators are not enabled, decorators are set inactive', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        smarteditElementComponent.active = false;
        (smarteditElementComponent as any).componentDecoratorEnabled = false;
        mouseenterListenerCallback();

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    it('on mouseenter if active decorators are set inactive', () => {
        sakExecutorService.wrapDecorators.calls.reset();

        smarteditElementComponent.active = true;
        (smarteditElementComponent as any).componentDecoratorEnabled = true;
        mouseenterListenerCallback();

        expect(sakExecutorService.wrapDecorators).not.toHaveBeenCalled();

        expect(stringUtils.formatHTML(nativeElement.outerHTML)).toBe(
            stringUtils.formatHTML(PROCESSED_ELEMENT(false))
        );
    });

    function setElementUuidOfDraggedElement(value: string) {
        smarteditComponentClosestToDraggedElement.attr.and.callFake((name: string) => {
            if (name === ELEMENT_UUID_ATTRIBUTE) {
                return value;
            } else {
                throw new Error(
                    `unexpected attribute name ${name} passed to smarteditComponentClosestToDraggedElement.attr`
                );
            }
        });
    }
});
