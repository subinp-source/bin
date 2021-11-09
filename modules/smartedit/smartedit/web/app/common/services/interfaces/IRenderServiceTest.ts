/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WindowUtils } from 'smarteditcommons/utils';
import { CrossFrameEventService } from '../crossFrame/CrossFrameEventService';
import { INotificationService, IPageInfoService, IRenderService } from '.';
import { IPerspectiveService } from '../perspectives/IPerspectiveService';
import { SystemEventService } from '../SystemEventService';
import { jQueryHelper, promiseHelper } from 'testhelpers';
import { ModalService } from '../modal';

describe('IRenderService abstract class', () => {
    class MockRenderService extends IRenderService {
        constructor(
            _yjQuery: JQueryStatic,
            _systemEventService: SystemEventService,
            _notificationService: INotificationService,
            _pageInfoService: IPageInfoService,
            _perspectiveService: IPerspectiveService,
            _crossFrameEventService: CrossFrameEventService,
            _windowUtils: WindowUtils,
            _modalService: ModalService
        ) {
            super(
                _yjQuery,
                _systemEventService,
                _notificationService,
                _pageInfoService,
                _perspectiveService,
                _crossFrameEventService,
                _windowUtils,
                _modalService
            );
        }
    }

    let renderService: MockRenderService;

    let yjQuery: any;
    const $q = promiseHelper.$q();
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let notificationService: jasmine.SpyObj<INotificationService>;
    let pageInfoService: jasmine.SpyObj<IPageInfoService>;
    let perspectiveService: jasmine.SpyObj<IPerspectiveService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let modalService: jasmine.SpyObj<ModalService>;

    const windowUtils = new WindowUtils();

    const ESC_KEY_CODE = 27;
    const HOTKEY_NOTIFICATION_ID = 'HOTKEY_NOTIFICATION_ID';

    beforeEach(() => {
        renderService = null;
        systemEventService = jasmine.createSpyObj('systemEventService', ['publishAsync']);
        notificationService = jasmine.createSpyObj('notificationService', [
            'pushNotification',
            'removeNotification'
        ]);
        pageInfoService = jasmine.createSpyObj('pageInfoService', ['getPageUUID']);
        perspectiveService = jasmine.createSpyObj('perspectiveService', [
            'isHotkeyEnabledForActivePerspective'
        ]);
        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', [
            'publish',
            'subscribe'
        ]);

        modalService = jasmine.createSpyObj('modalService', ['hasOpenModals']);
        modalService.hasOpenModals.and.returnValue(false);

        yjQuery = jQueryHelper.jQuery();
        yjQuery.and.callFake((arg: any) => {
            if (typeof arg === 'string') {
                arg = jQueryHelper.wrap(arg);
            }
            (arg as any).on = (): void => null;
            return arg;
        });
    });

    function initService() {
        renderService = new MockRenderService(
            yjQuery,
            systemEventService,
            notificationService,
            pageInfoService,
            perspectiveService,
            crossFrameEventService,
            windowUtils,
            modalService
        );
    }

    function getEventByKeyCode(keyCode: number): JQuery.Event {
        const event: JQuery.Event = window.smarteditJQuery.Event('keyup');
        event.which = keyCode;
        return event;
    }

    it('IRenderService declares the expected set of empty functions', () => {
        expect(IRenderService.prototype.renderComponent).toBeEmptyFunction();
        expect(IRenderService.prototype.renderRemoval).toBeEmptyFunction();
        expect(IRenderService.prototype.toggleOverlay).toBeEmptyFunction();
        expect(IRenderService.prototype.refreshOverlayDimensions).toBeEmptyFunction();
        expect(IRenderService.prototype.blockRendering).toBeEmptyFunction();
        expect(IRenderService.prototype.isRenderingBlocked).toBeEmptyFunction();
    });

    it('IRenderService initializes successfully and binds events', () => {
        spyOn(IRenderService.prototype as any, '_bindEvents').and.callThrough();

        const mockDocument: jasmine.SpyObj<Document> = jasmine.createSpyObj('document', [
            'addEventListener'
        ]);
        spyOn(IRenderService.prototype as any, '_getDocument').and.returnValue(mockDocument);

        initService();

        expect(mockDocument.addEventListener.calls.count()).toBe(2);
        expect(mockDocument.addEventListener.calls.argsFor(0)).toEqual([
            'keyup',
            jasmine.any(Function)
        ]);
        expect(mockDocument.addEventListener.calls.argsFor(1)).toEqual([
            'click',
            jasmine.any(Function)
        ]);
    });

    it('WHEN ESC key is pressed in a non storefront view THEN _keyPressEvent is not triggered', () => {
        pageInfoService.getPageUUID.and.returnValue(
            $q.reject({
                name: 'InvalidStorefrontPageError'
            })
        );

        spyOn(IRenderService.prototype as any, '_keyPressEvent');

        initService();

        (renderService as any)._keyUpEventHandler(getEventByKeyCode(ESC_KEY_CODE));

        expect((renderService as any)._keyPressEvent).not.toHaveBeenCalled();
    });

    it('WHEN ESC key is pressed in storefront view with no perspective set THEN _keyPressEvent is not triggered', () => {
        pageInfoService.getPageUUID.and.returnValue($q.when('somePageUuid'));
        perspectiveService.isHotkeyEnabledForActivePerspective.and.returnValue($q.when(false));

        initService();

        spyOn(renderService as any, '_keyPressEvent');

        (renderService as any)._keyUpEventHandler(getEventByKeyCode(ESC_KEY_CODE));

        expect((renderService as any)._keyPressEvent).not.toHaveBeenCalled();
    });

    it('WHEN NON-ESC key is pressed THEN _keyPressEvent is not triggered', () => {
        pageInfoService.getPageUUID.and.returnValue($q.when('somePageUuid'));
        perspectiveService.isHotkeyEnabledForActivePerspective.and.returnValue($q.when(true));

        initService();

        spyOn(renderService as any, '_keyPressEvent');

        (renderService as any)._keyUpEventHandler(getEventByKeyCode(17)); // press other key than ESC

        expect((renderService as any)._keyPressEvent).not.toHaveBeenCalled();
    });

    it('WHEN ESC key is pressed in storefront view with some perspective set THEN _keyPressEvent is triggered', (done) => {
        pageInfoService.getPageUUID.and.returnValue($q.when('somePageUuid'));
        perspectiveService.isHotkeyEnabledForActivePerspective.and.returnValue($q.when(true));

        initService();

        spyOn(renderService as any, '_keyPressEvent');

        (renderService as any)._keyUpEventHandler(getEventByKeyCode(ESC_KEY_CODE)).then(function() {
            expect((renderService as any)._keyPressEvent).toHaveBeenCalled();
            done();
        });
    });

    it('GIVEN when a modal window is open WHEN ESC key is pressed THEN nothing happens', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        spyOn(IRenderService.prototype as any, '_shouldEnableKeyPressEvent').and.returnValue(
            Promise.resolve(true)
        );
        modalService.hasOpenModals.and.returnValue(true);

        initService();

        (renderService as any)._keyUpEventHandler();

        expect(renderService.blockRendering).not.toHaveBeenCalled();
        expect(renderService.renderPage).not.toHaveBeenCalled();
        expect(notificationService.pushNotification).not.toHaveBeenCalled();
        expect(notificationService.removeNotification).not.toHaveBeenCalled();
    });

    it('GIVEN when all modal window are closed and the rendering is already blocked WHEN ESC key is pressed THEN rendering is unblocked, renderPage is called to re-render the overlay AND the hotkey notification is hidden', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        modalService.hasOpenModals.and.returnValue(false);

        initService();

        (renderService as any)._keyPressEvent();

        expect(renderService.blockRendering).toHaveBeenCalledWith(false);
        expect(renderService.renderPage).toHaveBeenCalledWith(true);
        expect(notificationService.removeNotification).toHaveBeenCalledWith(HOTKEY_NOTIFICATION_ID);
    });

    it('GIVEN when all modal window are closed and the rendering is not blocked WHEN ESC key is pressed THEN rendering is blocked, renderPage is called but without re-rendering the overlay, an event is triggered and the hotkey notification is shown', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(false));
        spyOn(IRenderService.prototype, 'blockRendering').and.callThrough();
        spyOn(IRenderService.prototype, 'renderPage');

        modalService.hasOpenModals.and.returnValue(false);

        initService();

        (renderService as any)._keyPressEvent();

        expect(renderService.blockRendering).toHaveBeenCalledWith(true);
        expect(renderService.renderPage).toHaveBeenCalledWith(false);
        expect(systemEventService.publishAsync).toHaveBeenCalledWith('OVERLAY_DISABLED');
        expect(notificationService.pushNotification).toHaveBeenCalledWith(
            (renderService as any).HOTKEY_NOTIFICATION_CONFIGURATION
        );
    });

    it('GIVEN when the rendering is not blocked WHEN Click event is triggered THEN nothing happens', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(false));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');

        initService();

        (renderService as any)._clickEvent();

        expect(renderService.blockRendering).not.toHaveBeenCalled();
        expect(renderService.renderPage).not.toHaveBeenCalled();
        expect(notificationService.removeNotification).not.toHaveBeenCalled();
    });

    it('GIVEN when the rendering is blocked WHEN Click event is triggered inside the frame THEN nothing happens', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        spyOn(windowUtils, 'isIframe').and.returnValue(true);

        initService();

        (renderService as any)._clickEvent();

        expect(renderService.blockRendering).not.toHaveBeenCalled();
        expect(renderService.renderPage).not.toHaveBeenCalled();
        expect(notificationService.removeNotification).not.toHaveBeenCalledWith();
    });

    it('GIVEN when the rendering is blocked WHEN Click event is triggered outside of the frame THEN rendering is unblocked, renderPage is called to re-render the overlay and the hotkey notification is hidden', (done) => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        spyOn(windowUtils, 'isIframe').and.returnValue(false);

        initService();

        (renderService as any)._clickEvent().then(
            () => {
                expect(renderService.blockRendering).toHaveBeenCalledWith(false);
                expect(renderService.renderPage).toHaveBeenCalledWith(true);
                expect(notificationService.removeNotification).toHaveBeenCalledWith(
                    HOTKEY_NOTIFICATION_ID
                );
                done();
            },
            () => {
                fail('renderService._clickEvent() should have resolved');
            }
        );
    });

    it('GIVEN when all modal window are closed and the rendering is already blocked WHEN ESC key is pressed THEN rendering is unblocked, renderPage is called to re-render the overlay and the hotkey notification is hidden', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');

        modalService.hasOpenModals.and.returnValue(false);

        initService();

        (renderService as any)._keyPressEvent();

        expect(renderService.blockRendering).toHaveBeenCalledWith(false);
        expect(renderService.renderPage).toHaveBeenCalledWith(true);
        expect(notificationService.removeNotification).toHaveBeenCalledWith(HOTKEY_NOTIFICATION_ID);
    });

    it('GIVEN when all modal window are closed and the rendering is not blocked WHEN ESC key is pressed THEN rendering is blocked, renderPage is called but without re-rendering the overlay, an event is triggered and the hotkey notification is shown', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(false));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        modalService.hasOpenModals.and.returnValue(false);

        initService();

        (renderService as any)._keyPressEvent();

        expect(renderService.blockRendering).toHaveBeenCalledWith(true);
        expect(renderService.renderPage).toHaveBeenCalledWith(false);
        expect(systemEventService.publishAsync).toHaveBeenCalledWith('OVERLAY_DISABLED');
        expect(notificationService.pushNotification).toHaveBeenCalledWith(
            (renderService as any).HOTKEY_NOTIFICATION_CONFIGURATION
        );
    });

    it('GIVEN when the rendering is not blocked WHEN Click event is triggered THEN nothing happens', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(false));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');

        initService();

        (renderService as any)._clickEvent();

        expect(renderService.blockRendering).not.toHaveBeenCalled();
        expect(renderService.renderPage).not.toHaveBeenCalled();
        expect(notificationService.removeNotification).not.toHaveBeenCalled();
    });

    it('GIVEN when the rendering is blocked WHEN Click event is triggered inside the frame THEN nothing happens', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        spyOn(windowUtils, 'isIframe').and.returnValue(true);

        initService();

        (renderService as any)._clickEvent();

        expect(renderService.blockRendering).not.toHaveBeenCalled();
        expect(renderService.renderPage).not.toHaveBeenCalled();
        expect(notificationService.removeNotification).not.toHaveBeenCalled();
    });

    it('GIVEN when the rendering is blocked WHEN Click event is triggered outside of the frame THEN rendering is unblocked, renderPage is called to re-render the overlay and the hotkey notification is hidden', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');

        spyOn(windowUtils, 'isIframe').and.returnValue(false);

        initService();

        (renderService as any)._clickEvent();

        expect(IRenderService.prototype.blockRendering).toHaveBeenCalledWith(false);
        expect(IRenderService.prototype.renderPage).toHaveBeenCalledWith(true);
        expect(notificationService.removeNotification).toHaveBeenCalledWith(HOTKEY_NOTIFICATION_ID);
    });

    it('WHEN Click event is triggered outside of the frame THEN a cross frame event is published', () => {
        spyOn(IRenderService.prototype, 'isRenderingBlocked').and.returnValue($q.when(true));
        spyOn(IRenderService.prototype, 'blockRendering');
        spyOn(IRenderService.prototype, 'renderPage');
        spyOn(windowUtils, 'isIframe').and.returnValue(false);

        initService();

        (renderService as any)._clickEvent();

        expect(crossFrameEventService.publish).toHaveBeenCalled();
    });
});
