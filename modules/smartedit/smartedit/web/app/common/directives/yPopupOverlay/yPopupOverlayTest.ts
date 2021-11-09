/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { IYPopupEngine, YPopupEngineService } from './yPopupEngineService';
import ResizeObserver from 'resize-observer-polyfill';

export function getDocumentMock(): angular.IDocumentService {
    return ({
        0: { body: document.createElement('body') },
        find(element: string): HTMLElement {
            return this[0][element];
        }
    } as unknown) as angular.IDocumentService;
}

export function getCompileMock(): angular.ICompileService {
    return (((element: string) => (scope: angular.IScope) =>
        angular.element(element)) as unknown) as angular.ICompileService;
}

export function getTimeoutMock(): angular.ITimeoutService {
    return (((callback: () => void) =>
        setTimeout(callback, 0)) as unknown) as angular.ITimeoutService;
}

export function getScopeMock(): angular.IScope {
    return {
        $new: () => getScopeMock(),
        $destroy: () => {
            //
        }
    } as angular.IScope;
}
describe('yPopupOverlay - yPopupEngineService', () => {
    let $document: angular.IDocumentService;
    let $compile: angular.ICompileService;
    let $timeout: angular.ITimeoutService;
    let yPopupEngineService: new (...args: any[]) => IYPopupEngine;
    let $scope: angular.IScope;
    let anchor: HTMLElement;
    let service: any;

    beforeEach(() => {
        anchor = angular.element('<div>Anchor</div>')[0];
        $document = getDocumentMock();
        $timeout = getTimeoutMock();
        $compile = getCompileMock();
        $scope = getScopeMock();

        yPopupEngineService = new YPopupEngineService($document, $compile, $timeout) as new (
            ...args: any[]
        ) => IYPopupEngine;
    });

    it('should initialize the engine with default attributes', () => {
        spyOn(yPopupEngineService.prototype, 'configure');

        service = new yPopupEngineService(anchor, '<div>Body</div>', $scope);

        expect(yPopupEngineService.prototype.configure).toHaveBeenCalled();

        expect(service.isOpen).toBe(false);
        expect(service.scope).toBe($scope);
        expect(service.anchorElement).toBe(anchor);
        expect(service.eventListeners.length).toBe(0);
        expect(service.template).toBe('<div>Body</div>');
    });

    it('should configure the engine with the configurations', () => {
        spyOn(yPopupEngineService.prototype, 'setTrigger');

        service = new yPopupEngineService(anchor, '<div>Body</div>', $scope);

        service.configure({
            placement: 'top',
            trigger: 'click',
            onShow() {
                this.isOpen = true;
            },
            onHide() {
                this.isOpen = false;
            },
            onChanges: () => {
                //
            }
        });

        expect(yPopupEngineService.prototype.setTrigger).toHaveBeenCalled();
        expect(service.container).toBe($document[0].body);

        expect(service.config.placement).toBe('top');
        expect(service.config.trigger).toBe('click');
        expect(service.config.onClickOutside).toBe('close');
    });

    it('should show the popup', () => {
        const onShow = jasmine.createSpy('onShow');
        service = new yPopupEngineService(anchor, '<div>An awesome popup.</div>', $scope, {
            onShow
        });
        const observeSpy = spyOn(service.resizeObserver, 'observe');
        service.show();

        expect(service.isOpen).toBe(true);
        expect(service.onShow).toHaveBeenCalled();
        expect(observeSpy).toHaveBeenCalled();
        expect(service.popupInstance).toBeDefined();
        expect((($document.find('body') as unknown) as HTMLElement).innerText).toContain(
            'An awesome popup.'
        );
    });

    it('should hide the popup', () => {
        const onHide = jasmine.createSpy('onHide');

        service = new yPopupEngineService(anchor, '<div>An awesome popup.</div>', $scope, {
            onHide
        });

        service.show();

        service.popupInstance.destroy = jasmine.createSpy('destroy');

        const unobserveSpy = spyOn(service.resizeObserver as ResizeObserver, 'unobserve');
        service.hide();

        expect(service.onHide).toHaveBeenCalled();
        expect(service.popupInstance.destroy).toHaveBeenCalled();
        expect(unobserveSpy).toHaveBeenCalled();
        expect((($document.find('body') as unknown) as HTMLElement).innerText).not.toContain(
            'An awesome popup.'
        );
    });

    it('should set the trigger to click', () => {
        spyOn(yPopupEngineService.prototype, 'configure');
        const addEventListenerSpy = spyOn(anchor, 'addEventListener');
        const removeEventListenerSpy = spyOn(anchor, 'removeEventListener');

        service = new yPopupEngineService(anchor, '<div>Body</div>', $scope);
        service.config = {};
        service.setTrigger('click');

        expect(service.config.trigger).toBe('click');
        expect(addEventListenerSpy.calls.argsFor(0)[0]).toBe('click');

        service.dispose();

        expect(removeEventListenerSpy.calls.argsFor(0)[0]).toBe('click');
        expect(service.eventListeners.length).toBe(0);
    });

    it('should set the trigger to focus', () => {
        spyOn(yPopupEngineService.prototype, 'configure');
        const addEventListenerSpy = spyOn(anchor, 'addEventListener');
        const removeEventListenerSpy = spyOn(anchor, 'removeEventListener');

        service = new yPopupEngineService(anchor, '<div>Body</div>', $scope);
        service.config = {};
        service.setTrigger('focus');

        expect(service.config.trigger).toBe('focus');
        expect(addEventListenerSpy.calls.argsFor(0)[0]).toBe('focus');
        expect(addEventListenerSpy.calls.argsFor(1)[0]).toBe('blur');
        expect(service.eventListeners.length).toBe(2);

        service.dispose();

        expect(removeEventListenerSpy).toHaveBeenCalledTimes(2);
        expect(service.eventListeners.length).toBe(0);
    });

    it('should set the trigger to hover', () => {
        spyOn(yPopupEngineService.prototype, 'configure');
        const addEventListenerSpy = spyOn(anchor, 'addEventListener');
        const removeEventListenerSpy = spyOn(anchor, 'removeEventListener');

        service = new yPopupEngineService(anchor, '<div>Body</div>', $scope);
        service.config = {};
        service.setTrigger('hover');

        expect(service.config.trigger).toBe('hover');
        expect(addEventListenerSpy.calls.argsFor(0)[0]).toBe('mouseenter');
        expect(addEventListenerSpy.calls.argsFor(1)[0]).toBe('mouseleave');
        expect(service.eventListeners.length).toBe(2);

        service.dispose();

        expect(removeEventListenerSpy).toHaveBeenCalledTimes(2);
        expect(service.eventListeners.length).toBe(0);
    });

    it('should change trigger from hover to false', () => {
        spyOn(yPopupEngineService.prototype, 'configure');
        spyOn(anchor, 'addEventListener');
        spyOn(anchor, 'removeEventListener');

        service = new yPopupEngineService(anchor, '<div>Body</div>', $scope);
        service.config = {};
        service.setTrigger('hover');

        expect(service.eventListeners.length).toBe(2);
        spyOn(service, 'hide');

        service.setTrigger(false);

        expect(anchor.removeEventListener).toHaveBeenCalledTimes(2);
        expect(service.hide).toHaveBeenCalled();
    });

    afterEach(() => {
        service.dispose();
    });
});
