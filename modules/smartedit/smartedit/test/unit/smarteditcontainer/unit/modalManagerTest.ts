/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import * as angular from 'angular';
import * as uib from 'angular-ui-bootstrap';
import {
    IModalButtonOptions,
    IModalConfig,
    ModalButtonActions,
    ModalButtonStyles,
    ModalManager
} from 'smarteditcommons';

describe('ModalManager', () => {
    let $translateWrapper: jasmine.SpyObj<{ $translate: angular.translate.ITranslateService }>;
    let modalInstance: jasmine.SpyObj<uib.IModalInstanceService>;

    const defaultButtonConfig: IModalButtonOptions = {
        id: 'test_button',
        style: ModalButtonStyles.Primary,
        action: ModalButtonActions.None,
        label: 'test_label'
    };

    const defaultModalConfig: IModalConfig = {
        templateInline: '<span>Hello</span>'
    };

    function getModalManager(): ModalManager {
        return new ModalManager(defaultModalConfig, $translateWrapper.$translate, modalInstance);
    }

    beforeEach(() => {
        $translateWrapper = jasmine.createSpyObj('$translateWrapper', ['$translate']);
        $translateWrapper.$translate.and.returnValue(Promise.resolve());

        modalInstance = jasmine.createSpyObj('modalInstance', ['close', 'dismiss']);
    });

    it('adds button', () => {
        const manager: ModalManager = getModalManager();

        expect(manager.getButtons().length).toBe(0);

        manager.addButton(defaultButtonConfig);

        expect(manager.getButton(defaultButtonConfig.id).id).toBe(defaultButtonConfig.id);
        expect(manager.getButtons().length).toBe(1);
    });

    it('removes a button', function() {
        const manager: ModalManager = getModalManager();

        manager.addButton({ ...defaultButtonConfig, id: '1' });
        manager.addButton({ ...defaultButtonConfig, id: '2' });
        manager.addButton({ ...defaultButtonConfig, id: '3' });

        expect(manager.getButtons().length).toBe(3);

        manager.removeButton('2');

        expect(manager.getButtons().length).toBe(2);
        expect(manager.getButton('2')).toBeUndefined();
    });

    it('removes all buttons', function() {
        const manager: ModalManager = getModalManager();

        manager.addButton({ ...defaultButtonConfig, id: '1' });
        manager.addButton({ ...defaultButtonConfig, id: '2' });
        manager.addButton({ ...defaultButtonConfig, id: '3' });

        expect(manager.getButtons().length).toBe(3);

        manager.removeAllButtons();

        expect(manager.getButtons().length).toBe(0);
    });

    it('disables a button', function() {
        const manager: ModalManager = getModalManager();

        expect(manager.getButtons().length).toBe(0);

        manager.addButton(defaultButtonConfig);

        expect(manager.getButton(defaultButtonConfig.id).disabled).toBe(false);

        manager.disableButton(defaultButtonConfig.id);

        expect(manager.getButton(defaultButtonConfig.id).disabled).toBe(true);
    });

    it('enable a button', function() {
        const manager: ModalManager = getModalManager();

        expect(manager.getButtons().length).toBe(0);

        manager.addButton({ ...defaultButtonConfig, disabled: true });

        expect(manager.getButton(defaultButtonConfig.id).disabled).toBe(true);

        manager.enableButton(defaultButtonConfig.id);

        expect(manager.getButton(defaultButtonConfig.id).disabled).toBe(false);
    });

    it('show/hide the dismiss X button', function() {
        const manager: ModalManager = getModalManager();

        manager.setShowHeaderDismiss(true);
        expect(manager._showDismissButton()).toBe(true);

        manager.setShowHeaderDismiss(false);
        expect(manager._showDismissButton()).toBe(false);
    });

    it('closes a modal with a default close button', function() {
        const manager: ModalManager = getModalManager();

        manager.addButton({
            ...defaultButtonConfig,
            action: ModalButtonActions.Close
        });

        const close = spyOn(manager, 'close').and.callThrough();
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        expect(close).toHaveBeenCalled();
    });

    it('dismiss a modal with a default dismiss button', function() {
        const manager: ModalManager = getModalManager();

        manager.addButton({
            ...defaultButtonConfig,
            action: ModalButtonActions.Dismiss
        });

        const dismiss = spyOn(manager, 'dismiss').and.callThrough();
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        expect(dismiss).toHaveBeenCalled();
    });

    it('press a button with action NONE and the modal will not be closed or dismissed', function() {
        const manager: ModalManager = getModalManager();

        manager.addButton({
            ...defaultButtonConfig,
            action: ModalButtonActions.None
        });

        const close = spyOn(manager, 'close').and.callThrough();
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        expect(close).not.toHaveBeenCalled();
        expect(dismiss).not.toHaveBeenCalled();
    });

    it('dismiss the modal with the default dismiss X button', function() {
        const manager: ModalManager = getModalManager();

        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager._handleDismissButton();

        expect(dismiss).toHaveBeenCalled();
    });

    it('use a callback to cancel the dismiss action of the dismiss X button', function(done) {
        const manager: ModalManager = getModalManager();
        const callback: () => Promise<any> = () => Promise.reject();

        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.setDismissCallback(callback);
        manager._handleDismissButton();

        callback().catch(() => {
            expect(dismiss).not.toHaveBeenCalled();
            done();
        });
    });

    it('use a callback to allow the dismiss action of the dismiss X button', function(done) {
        const manager: ModalManager = getModalManager();
        const callback = () => Promise.resolve();
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.setDismissCallback(callback);
        manager._handleDismissButton();

        callback().then(() => {
            expect(dismiss).toHaveBeenCalled();
            done();
        });
    });

    it('keep a modal open using a global button callback when button action is NONE', function(done) {
        const manager: ModalManager = getModalManager();
        const returnData: string = 'bla';
        const callback = () => Promise.resolve(returnData);
        const close = spyOn(manager, 'close').and.callThrough();

        manager.addButton(defaultButtonConfig);
        manager.setButtonHandler(callback);
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().then(() => {
            expect(close).not.toHaveBeenCalledWith(returnData);
            done();
        });
    });

    it('dismiss a modal using a global button callback and get the callbacks return data', function(done) {
        const manager: ModalManager = getModalManager();
        const returnData: string = 'bla';
        const callback = () => Promise.resolve(returnData);
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Dismiss });
        manager.setButtonHandler(callback);
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().then(() => {
            expect(dismiss).toHaveBeenCalledWith(returnData);
            done();
        });
    });

    it('close a modal using a global button callback and get the callbacks return data', function(done) {
        const manager: ModalManager = getModalManager();
        const returnData: string = 'bla';
        const callback = () => Promise.resolve(returnData);
        const close = spyOn(manager, 'close').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Close });
        manager.setButtonHandler(callback);
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().then(() => {
            expect(close).toHaveBeenCalledWith(returnData);
            done();
        });
    });

    it('cancel the dismiss of a modal using a global button callback when promise is rejected', function(done) {
        const manager: ModalManager = getModalManager();
        const callback = () => Promise.reject();
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Dismiss });
        manager.setButtonHandler(callback);
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().catch(() => {
            expect(dismiss).not.toHaveBeenCalled();
            done();
        });
    });

    it('cancel the close of a modal using a global button callback when promise is rejected', function(done) {
        const manager: ModalManager = getModalManager();
        const callback = () => Promise.reject();
        const close = spyOn(manager, 'close').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Close });
        manager.setButtonHandler(callback);
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().catch(() => {
            expect(close).not.toHaveBeenCalled();
            done();
        });
    });

    it('keep a modal open using a button callback when button action is NONE', function(done) {
        const manager: ModalManager = getModalManager();
        const callback = () => Promise.reject();
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.None, callback });
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().catch(() => {
            expect(dismiss).not.toHaveBeenCalled();
            done();
        });
    });

    it('dismiss a modal using a button callback and get the callbacks return data', function(done) {
        const manager: ModalManager = getModalManager();
        const data: string = 'bla';
        const callback = () => Promise.resolve(data);
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Dismiss, callback });
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().then(() => {
            expect(dismiss).toHaveBeenCalledWith(data);
            done();
        });
    });

    it('close a modal using a button callback and get the callbacks return data', function(done) {
        const manager: ModalManager = getModalManager();
        const data: string = 'bla';
        const callback = () => Promise.resolve(data);
        const close = spyOn(manager, 'close').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Close, callback });
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().then(() => {
            expect(close).toHaveBeenCalledWith(data);
            done();
        });
    });

    it('cancel the dismiss of a modal using a global button callback when promise is rejected', function(done) {
        const manager: ModalManager = getModalManager();
        const callback = () => Promise.reject();
        const dismiss = spyOn(manager, 'dismiss').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Dismiss, callback });
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().catch(() => {
            expect(dismiss).not.toHaveBeenCalled();
            done();
        });
    });

    it('cancel the close of a modal using a button callback when promise is rejected', function(done) {
        const manager: ModalManager = getModalManager();
        const callback = () => Promise.reject();
        const close = spyOn(manager, 'close').and.callThrough();

        manager.addButton({ ...defaultButtonConfig, action: ModalButtonActions.Close, callback });
        manager._buttonPressed(manager.getButton(defaultButtonConfig.id));

        callback().catch(() => {
            expect(close).not.toHaveBeenCalled();
            done();
        });
    });

    it('add a button with an invalid style and get an exception', function() {
        const manager: ModalManager = getModalManager();

        const fn = () =>
            manager.addButton({
                ...defaultButtonConfig,
                style: 'some_invalid_style' as ModalButtonStyles
            });

        expect(fn).toThrow(
            new Error('modalService.ModalManager._createButton.illegal.button.style')
        );
    });

    it('add a button with an invalid action and get an exception', function() {
        const manager: ModalManager = getModalManager();

        const fn = () =>
            manager.addButton({
                ...defaultButtonConfig,
                action: 'some_invalid_action' as ModalButtonActions
            });

        expect(fn).toThrow(
            new Error('modalService.ModalManager._createButton.illegal.button.action')
        );
    });
});
