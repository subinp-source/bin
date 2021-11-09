/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import {
    CrossFrameEventService,
    IIframeClickDetectionService,
    SystemEventService,
    TestModeService,
    TypedMap
} from 'smarteditcommons';
import { jQueryHelper } from 'testhelpers';
import { PerspectiveService } from 'smarteditcontainer/services/perspectives/PerspectiveServiceOuter';
import { PerspectiveSelectorComponent } from 'smarteditcontainer/services/perspectiveSelectorWidget/PerspectiveSelectorComponent';

describe('PerspectiveSelector', () => {
    let perspectiveSelectorComponent: PerspectiveSelectorComponent;
    let iframeClickDetectionService: jasmine.SpyObj<IIframeClickDetectionService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let perspectiveService: jasmine.SpyObj<PerspectiveService>;
    const EVENT_PERSPECTIVE_ADDED: string = 'EVENT_PERSPECTIVE_ADDED';
    const EVENT_PERSPECTIVE_CHANGED: string = 'EVENT_PERSPECTIVE_CHANGED';
    const EVENT_PERSPECTIVE_REFRESHED: string = 'EVENT_PERSPECTIVE_REFRESHED';
    const EVENT_STRICT_PREVIEW_MODE_REQUESTED: string = 'EVENT_STRICT_PREVIEW_MODE_REQUESTED';
    const ALL_PERSPECTIVE: string = 'se.all';
    const EVENTS: TypedMap<string> = {
        USER_HAS_CHANGED: 'USER_HAS_CHANGED'
    };
    let unRegOverlayDisabledFn: jasmine.SpyObj<() => void>;
    let unRegPerspectiveAddedFn: jasmine.SpyObj<() => void>;
    let unRegPerspectiveChgFn: jasmine.SpyObj<() => void>;
    let unRegPerspectiveRefreshFn: jasmine.SpyObj<() => void>;
    let unRegUserHasChanged: jasmine.SpyObj<() => void>;
    let unRegStrictPreviewModeToggleFn: jasmine.SpyObj<() => void>;

    const logService = jasmine.createSpyObj('logService', ['error']);
    let yjQuery: JQueryStatic;
    let testModeService: jasmine.SpyObj<TestModeService>;

    const PERSPECTIVE_SELECTOR_CLOSE = 'perspectiveSelectorClose';
    const OVERLAY_DISABLED = 'OVERLAY_DISABLED';

    /*
     * In order to test the perspective selector component's controller, several event handlers and
     * event services need to be mocked.
     *
     * The controller registers an event handler for WHEN the overlay is disabled, WHEN a new perspective
     * is added, registers a callback to close the dropdown WHEN a click occurs in the iFrame and subscribes
     * to a cross-frame event to listen for perspective changes.
     *
     * WHEN event handlers are registered on the systemEventService and WHEN we subscribe to an event on
     * the crossFrameEventService, an un-registration function is returned. This function must be called WHEN
     * the controller is destroyed. In the perspective selector's controller, these functions are stored in
     * private variables. Therefore, to test them, we create three spies, which are returned WHEN the
     * systemEventService and crossFrameEventService mocks are called to register and subscribe. This way,
     * it is possible to verify that these functions are called WHEN the controller is destroyed.
     */
    beforeEach(() => {
        unRegOverlayDisabledFn = jasmine.createSpy('unRegOverlayDisabledFn');
        unRegPerspectiveAddedFn = jasmine.createSpy('unRegPerspectiveAddedFn');
        unRegPerspectiveChgFn = jasmine.createSpy('unRegPerspectiveChgFn');
        unRegPerspectiveRefreshFn = jasmine.createSpy('unRegPerspectiveRefreshFn');
        unRegUserHasChanged = jasmine.createSpy('unRegUserHasChanged');
        unRegStrictPreviewModeToggleFn = jasmine.createSpy('unRegStrictPreviewModeToggleFn');

        iframeClickDetectionService = jasmine.createSpyObj<IIframeClickDetectionService>(
            'iframeClickDetectionService',
            ['registerCallback']
        );

        systemEventService = jasmine.createSpyObj('systemEventService', ['subscribe']);
        systemEventService.subscribe.and.callFake((identifier: string) => {
            switch (identifier) {
                case OVERLAY_DISABLED:
                    return unRegOverlayDisabledFn;

                case EVENT_PERSPECTIVE_ADDED:
                    return unRegPerspectiveAddedFn;

                default:
                    return null;
            }
        });

        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['subscribe']);
        crossFrameEventService.subscribe.and.callFake((identifier: string) => {
            switch (identifier) {
                case EVENT_PERSPECTIVE_CHANGED:
                    return unRegPerspectiveChgFn;

                case EVENT_PERSPECTIVE_REFRESHED:
                    return unRegPerspectiveRefreshFn;

                case EVENTS.USER_HAS_CHANGED:
                    return unRegUserHasChanged;

                case EVENT_STRICT_PREVIEW_MODE_REQUESTED:
                    return unRegStrictPreviewModeToggleFn;

                default:
                    return null;
            }
        });

        yjQuery = jQueryHelper.jQuery();

        testModeService = jasmine.createSpyObj('testModeService', ['isE2EMode']);
        testModeService.isE2EMode.and.returnValue(false);

        perspectiveService = jasmine.createSpyObj('perspectiveService', [
            'getPerspectives',
            'switchTo',
            'isEmptyPerspectiveActive',
            'getActivePerspective'
        ]);
    });

    beforeEach(() => {
        perspectiveSelectorComponent = new PerspectiveSelectorComponent(
            logService,
            yjQuery,
            perspectiveService,
            iframeClickDetectionService,
            systemEventService,
            crossFrameEventService,
            testModeService
        );
    });

    beforeEach(() => {
        perspectiveService.getPerspectives.and.returnValue(Promise.resolve([]));
    });

    describe('refreshPerspectives', () => {
        it('should call perspectiveService.getPerspectives() to make sure the correct perspectives are populated', () => {
            (perspectiveSelectorComponent as any).refreshPerspectives();
            expect(perspectiveService.getPerspectives).toHaveBeenCalled();
        });
    });

    describe('initialization', () => {
        it('controller is initialized with correct data', () => {
            expect(perspectiveSelectorComponent.isOpen).toBe(false);
            expect(perspectiveSelectorComponent.isDisabled).toBe(false);
            expect(perspectiveSelectorComponent.getDisplayedPerspectives()).toEqual([]);
            expect(perspectiveSelectorComponent.getActivePerspectiveName()).toBeFalsy();
            expect(perspectiveSelectorComponent.isTooltipVisible()).toBe(false);
        });

        it('ngOnInit registers callback on iframeClickDetectionService', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(iframeClickDetectionService.registerCallback).toHaveBeenCalled();
            expect(iframeClickDetectionService.registerCallback.calls.argsFor(0).length).toEqual(2);
            expect(iframeClickDetectionService.registerCallback.calls.argsFor(0)[0]).toEqual(
                PERSPECTIVE_SELECTOR_CLOSE
            );
            expect(iframeClickDetectionService.registerCallback.calls.argsFor(0)[1]).toBeTruthy();
        });

        it('ngOnInit registers event handler on systemEventService for OVERLAY_DISABLED', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(systemEventService.subscribe).toHaveBeenCalled();
            expect(systemEventService.subscribe.calls.argsFor(0).length).toEqual(2);
            expect(systemEventService.subscribe.calls.argsFor(0)[0]).toEqual(OVERLAY_DISABLED);
            expect(systemEventService.subscribe.calls.argsFor(0)[1]).toBeTruthy();
        });

        it('ngOnInit registers event handler on systemEventService for EVENT_PERSPECTIVE_ADDED', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(systemEventService.subscribe).toHaveBeenCalled();
            expect(systemEventService.subscribe.calls.argsFor(1).length).toEqual(2);
            expect(systemEventService.subscribe.calls.argsFor(1)[0]).toEqual(
                EVENT_PERSPECTIVE_ADDED
            );
            expect(systemEventService.subscribe.calls.argsFor(1)[1]).toBeTruthy();
        });

        it('ngOnInit subscribes to EVENT_PERSPECTIVE_CHANGED on crossFrameEventService', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(crossFrameEventService.subscribe).toHaveBeenCalled();
            expect(crossFrameEventService.subscribe.calls.argsFor(0).length).toEqual(2);
            expect(crossFrameEventService.subscribe.calls.argsFor(0)[0]).toEqual(
                EVENT_PERSPECTIVE_CHANGED
            );
            expect(crossFrameEventService.subscribe.calls.argsFor(0)[1]).toBeTruthy();
        });

        it('ngOnInit subscribes to EVENT_PERSPECTIVE_REFRESHED on crossFrameEventService', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(crossFrameEventService.subscribe).toHaveBeenCalled();
            expect(crossFrameEventService.subscribe.calls.argsFor(1).length).toEqual(2);
            expect(crossFrameEventService.subscribe.calls.argsFor(1)[0]).toEqual(
                EVENT_PERSPECTIVE_REFRESHED
            );
            expect(crossFrameEventService.subscribe.calls.argsFor(1)[1]).toBeTruthy();
        });

        it('ngOnInit subscribes to USER_HAS_CHANGED on crossFrameEventService', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(crossFrameEventService.subscribe).toHaveBeenCalled();
            expect(crossFrameEventService.subscribe.calls.argsFor(2).length).toEqual(2);
            expect(crossFrameEventService.subscribe.calls.argsFor(2)[0]).toEqual(
                EVENTS.USER_HAS_CHANGED
            );
            expect(crossFrameEventService.subscribe.calls.argsFor(2)[1]).toBeTruthy();
        });

        it('ngOnInit subscribes to EVENT_STRICT_PREVIEW_MODE_REQUESTED on crossFrameEventService', () => {
            // GIVEN/WHEN
            perspectiveSelectorComponent.ngOnInit();

            // THEN
            expect(crossFrameEventService.subscribe).toHaveBeenCalled();
            expect(crossFrameEventService.subscribe.calls.argsFor(3).length).toEqual(2);
            expect(crossFrameEventService.subscribe.calls.argsFor(3)[0]).toEqual(
                EVENT_STRICT_PREVIEW_MODE_REQUESTED
            );
            expect(crossFrameEventService.subscribe.calls.argsFor(3)[1]).toBeTruthy();
        });
    });

    describe('destruction', () => {
        it('ngOnDestroy un-registers the OVERLAY_DISABLED event handler on systemEventService', () => {
            // GIVEN
            perspectiveSelectorComponent.ngOnInit();

            // WHEN
            perspectiveSelectorComponent.ngOnDestroy();

            // THEN
            expect(unRegOverlayDisabledFn).toHaveBeenCalled();
        });

        it('ngOnDestroy un-registers the EVENT_PERSPECTIVE_ADDED event handler on systemEventService', () => {
            // GIVEN
            perspectiveSelectorComponent.ngOnInit();

            // WHEN
            perspectiveSelectorComponent.ngOnDestroy();

            // THEN
            expect(unRegPerspectiveAddedFn).toHaveBeenCalled();
        });

        it('ngOnDestroy un-registers the EVENT_PERSPECTIVE_CHANGED from crossFrameEventService', () => {
            // GIVEN
            perspectiveSelectorComponent.ngOnInit();

            // WHEN
            perspectiveSelectorComponent.ngOnDestroy();

            // THEN
            expect(unRegPerspectiveChgFn).toHaveBeenCalled();
        });

        it('ngOnDestroy un-registers the EVENTS.USER_HAS_CHANGED from crossFrameEventService', () => {
            // GIVEN
            perspectiveSelectorComponent.ngOnInit();

            // WHEN
            perspectiveSelectorComponent.ngOnDestroy();

            // THEN
            expect(unRegUserHasChanged).toHaveBeenCalled();
        });

        it('ngOnDestroy un-registers the EVENT_STRICT_PREVIEW_MODE_REQUESTED from crossFrameEventService', () => {
            // GIVEN
            perspectiveSelectorComponent.ngOnInit();

            // WHEN
            perspectiveSelectorComponent.ngOnDestroy();

            // THEN
            expect(unRegStrictPreviewModeToggleFn).toHaveBeenCalled();
        });
    });

    describe('filterPerspectives', () => {
        it('expect active perspective to not be displayed', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'perspective1'
            };

            const PERSPECTIVE_2 = {
                key: 'perspective2'
            };

            const perspectives = [PERSPECTIVE_1, PERSPECTIVE_2];

            perspectiveService.getActivePerspective.and.returnValue(PERSPECTIVE_1);
            (perspectiveSelectorComponent as any)._refreshActivePerspective();

            // WHEN
            const displayedPerspectives = (perspectiveSelectorComponent as any).filterPerspectives(
                perspectives
            );

            // THEN
            expect(displayedPerspectives).toEqual([PERSPECTIVE_2]);
        });

        it('expect all perspective to display WHEN there is no active perspectives', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'perspective1'
            };

            const PERSPECTIVE_2 = {
                key: 'perspective2'
            };

            const perspectives = [PERSPECTIVE_1, PERSPECTIVE_2];

            // WHEN
            const displayedPerspectives = (perspectiveSelectorComponent as any).filterPerspectives(
                perspectives
            );

            // THEN
            expect(displayedPerspectives).toEqual([PERSPECTIVE_1, PERSPECTIVE_2]);
        });

        it('expect all perspective not to display', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'perspective1'
            };

            const PERSPECTIVE_2 = {
                key: 'perspective2'
            };

            const PERSPECTIVE_3 = {
                key: ALL_PERSPECTIVE
            };

            const perspectives = [PERSPECTIVE_1, PERSPECTIVE_2, PERSPECTIVE_3];

            perspectiveService.getActivePerspective.and.returnValue(PERSPECTIVE_1);
            (perspectiveSelectorComponent as any)._refreshActivePerspective();

            // WHEN
            const displayedPerspectives = (perspectiveSelectorComponent as any).filterPerspectives(
                perspectives
            );

            // THEN
            expect(displayedPerspectives).toEqual([PERSPECTIVE_2]);
        });
    });

    describe('isTooltipVisible', () => {
        it('returns false WHEN no perspective is active', () => {
            // WHEN
            const isTooltipVisible = perspectiveSelectorComponent.isTooltipVisible();

            // THEN
            expect(isTooltipVisible).toEqual(false);
        });

        it('returns false WHEN a perspective is active but has no description key', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'perspective1'
            };

            perspectiveService.getActivePerspective.and.returnValue(PERSPECTIVE_1);
            (perspectiveSelectorComponent as any)._refreshActivePerspective();

            // WHEN
            const isTooltipVisible = perspectiveSelectorComponent.isTooltipVisible();

            // THEN
            expect(isTooltipVisible).toEqual(false);
        });

        it('returns true WHEN no perspective is active and has a description key', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'perspective1',
                descriptionI18nKey: 'some.description.key'
            };

            perspectiveService.getActivePerspective.and.returnValue(PERSPECTIVE_1);
            (perspectiveSelectorComponent as any)._refreshActivePerspective();

            // WHEN
            const isTooltipVisible = perspectiveSelectorComponent.isTooltipVisible();

            // THEN
            expect(isTooltipVisible).toEqual(true);
        });

        it('returns false when preview perspective is active and perspective selector is not disabled', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'se.none',
                descriptionI18nKey: 'some.description.key'
            };
            perspectiveService.getActivePerspective.and.returnValue(PERSPECTIVE_1);
            (perspectiveSelectorComponent as any)._refreshActivePerspective();

            // WHEN
            const isTooltipVisible = perspectiveSelectorComponent.isTooltipVisible();

            // THEN
            expect(isTooltipVisible).toEqual(false);
        });

        it('returns true when preview perspective is active and perspective selector is disabled', () => {
            // GIVEN
            const PERSPECTIVE_1 = {
                key: 'se.none',
                descriptionI18nKey: 'some.description.key'
            };
            perspectiveService.getActivePerspective.and.returnValue(PERSPECTIVE_1);
            (perspectiveSelectorComponent as any)._refreshActivePerspective();
            (perspectiveSelectorComponent as any).togglePerspectiveSelector(true);

            // WHEN
            const isTooltipVisible = perspectiveSelectorComponent.isTooltipVisible();

            // THEN
            expect(isTooltipVisible).toEqual(true);
        });
    });
});
