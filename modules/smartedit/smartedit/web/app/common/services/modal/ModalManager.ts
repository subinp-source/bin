/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as uib from 'angular-ui-bootstrap';
import * as lo from 'lodash';

import { IModalButtonOptions } from './IModalButtonOptions';
import { defaultButtonOptions } from './constants';
import { ModalButtonStyles } from './ModalButtonStyles';
import { ModalButtonActions } from './ModalButtonActions';
import { IModalConfig } from './IModalConfig';

/**
 * @ngdoc service
 * @name modalServiceModule.service:ModalManager
 *
 * @description
 * The ModalManager is a service designed to provide easy runtime modification to various aspects of a modal window,
 * such as the modifying the title, adding a buttons, setting callbacks, etc...
 *
 * The ModalManager constructor is not exposed publicly, but an instance of ModalManager is added to the scope of
 * the modal content implicitly through the scope chain/prototyping. As long as you don't create an
 * {@link https://docs.angularjs.org/guide/scope isolated scope} for the modal, you can access it through $scope.modalManager
 *
 * <pre>
 *  .controller('modalTestController', function($scope, $log) {
 *    var buttonHandlerFn = function (buttonId) {
 *        $log.debug("button with id", buttonId, "was pressed!");
 *    };
 *    $scope.modalManager.setButtonHandler(buttonHandlerFn);
 *    ...
 * </pre>
 *
 */
export class ModalManager {
    public title: string = '';
    public titleSuffix: string;

    private buttonEventCallback: (id: string) => void | Promise<any>;
    private showDismissX: boolean = true;
    private buttons: IModalButtonOptions[] = [];
    private closeFunction: (data: any) => void;
    private dismissCallback: () => Promise<any>;
    private dismissFunction: (data: any) => void;
    private _defaultButtonOptions: IModalButtonOptions = defaultButtonOptions;

    constructor(
        private config: IModalConfig, // specify interface
        private $translate: angular.translate.ITranslateService,
        private modalInstance: uib.IModalInstanceService
    ) {
        if (!this.modalInstance) {
            throw new Error('no.modalInstance.injected');
        }
        if (typeof this.config.title === 'string') {
            this.title = config.title;
        }

        if (typeof this.config.titleSuffix === 'string') {
            this.titleSuffix = config.titleSuffix;
        }

        if (this.config.buttons) {
            this.buttons = [
                ...this.buttons,
                ...this.config.buttons.map((options: IModalButtonOptions) =>
                    this.createButton(options)
                )
            ];
        }

        this.closeFunction = this.modalInstance.close;
        this.dismissFunction = this.modalInstance.dismiss;
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#addButton
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {IModalButtonOptions} conf {@link modalServiceModule.interface:IModalButtonOptions IModalButtonOptions } (OPTIONAL) Button configuration
     * @returns {IModalButtonOptions} An object representing the newly added button
     */
    public addButton(newButtonConf: IModalButtonOptions): IModalButtonOptions {
        const button: IModalButtonOptions = this.createButton(newButtonConf);

        this.buttons = [...this.buttons, button];

        return button;
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#getButtons
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     * Caution!
     *
     * This is a reference to the buttons being used by the modal manager, not a clone. This should
     * only be used to read or update properties provided in the Button configuration. See
     * {@link modalServiceModule.service:ModalManager#methods_addButton addButton()} for more details.
     *
     * @returns {IModalButtonOptions[]} An array of all the buttons on the modal window, empty array if there are no buttons.
     */

    public getButtons(): IModalButtonOptions[] {
        return this.buttons;
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#removeAllButtons
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     * Remove all buttons from the modal window
     *
     */

    public removeAllButtons(): void {
        this.buttons = [];
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#removeButton
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {String} id The id of the button to be removed.
     *
     * @description
     * Remove a buttons from the modal window
     *
     */

    public removeButton(buttonId: string): void {
        this.buttons = this.buttons.filter(
            (options: IModalButtonOptions) => options.id !== buttonId
        );
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#enableButton
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {String} id The id of the button to be enabled.
     *
     * @description
     * Enables a button on the modal window, allowing it to be pressed.
     *
     */

    public enableButton(buttonId: string): void {
        this.buttons = this.buttons.map((options: IModalButtonOptions) =>
            options.id === buttonId ? { ...options, disabled: false } : options
        );
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#disableButton
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {String} id The id of the button to be disabled.
     *
     * @description
     * Disabled a button on the modal window, preventing it from be pressed.
     *
     */

    public disableButton(buttonId: string): void {
        this.buttons = this.buttons.map((options: IModalButtonOptions) =>
            options.id === buttonId ? { ...options, disabled: true } : options
        );
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#getButton
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {String} id The id of the button to be fetched
     *
     * @returns {IModalButtonOptions} The first button found with a matching id, or null
     */

    public getButton(buttonId: string): IModalButtonOptions {
        return this.buttons.find((options: IModalButtonOptions) => options.id === buttonId);
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#setShowHeaderDismiss
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {boolean} showX Flag to show/hide the X dismiss button at the top right corner of the modal window,
     * when the modal header is displayed
     *
     */

    public setShowHeaderDismiss(showButton: boolean): void {
        if (typeof showButton === 'boolean') {
            this.showDismissX = showButton;
        } else {
            throw new Error('modalService.ModalManager.showDismissX.illegal.param');
        }
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#setDismissCallback
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @param {() => Promise<any>} dismissCallback A function to be called when the X dismiss button at the top right corner of the modal window
     * is pressed. This function must either return null or a {@link https://docs.angularjs.org/api/ng/service/$q promise}.
     *
     * If the {@link https://docs.angularjs.org/api/ng/service/$q promise} is resolved, or if the function returns null or undefined, then the modal is closed and the returned
     * modal {@link https://docs.angularjs.org/api/ng/service/$q promise} is rejected.
     *
     * If the callback {@link https://docs.angularjs.org/api/ng/service/$q promise} is rejected, the modal is not closed, allowing you to provide some kind of validation
     * before closing.
     *
     */

    public setDismissCallback(callback: () => Promise<any>): void {
        this.dismissCallback = callback;
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#setButtonHandler
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     *
     * @param {Function} buttonPressedCallback The buttonPressedCallback is a function that is called when any button on the
     * modal, that has no {@link modalServiceModule.service:ModalManager#methods_addButton button callback}, is pressed. If a button has a
     * {@link modalServiceModule.service:ModalManager#methods_addButton button callback} function, then that function will be
     * called instead of the buttonPressedCallback.
     *
     * This buttonPressedCallback receives a single parameter, which is the string ID of the button that was pressed.
     * Additionally, this function must either return null, undefined or a {@link https://docs.angularjs.org/api/ng/service/$q promise}.
     *
     * If null/undefined is return, the modal will continue to process the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}
     * In this case, no data will be returned to the modal {@link https://docs.angularjs.org/api/ng/service/$q promise} if the modal is closed.
     *
     * If a promise is returned by this function, then the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}
     * may be cancelled/ignored by rejecting the promise. If the promise is resolved, the {@link modalServiceModule.service:ModalManager ModalManager}
     * will continue to process the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}.
     *
     * If by resolving the promise returned by the buttonHandlerFunction with data passed to the resolve, and the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}
     * is such that it results in the modal closing, then the modal promise is resolved/rejected with that same data. This allows you to pass data from the  buttonHandlerFunction
     * the the modalService.open(...) caller.
     *
     * See {@link modalServiceModule.service:ModalManager#methods_addButton addButton() for more details on the button callback }
     *
     *
     * A few scenarios for example:
     * #1 A button with a button callback is pressed.
     * <br/>Result: buttonPressedCallback is never called.
     *
     * #2 A button is pressed, buttonPressedCallback return null
     * <br/>Result: The modal manager will execute any action on the button
     *
     * #3 A button is pressed, buttonPressedCallback returns a promise, that promise is rejected
     * <br/>Result: Modal Manager will ignore the button action and nothing else will happen
     *
     * #4 A button with a dismiss action is pressed, buttonPressedCallback returns a promise, and that promise is resolved with data "Hello"
     * <br/>Result: ModalManager will execute the dismiss action, closing the modal, and errorCallback of the modal promise, passing "Hello" as data
     *
     *
     * Code sample of validating some data before closing the modal
     * <pre>
     * function validateSomething(): boolean {
     *     return true;
     * };
     *
     * function buttonHandlerFn (buttonId: string): Promise<any> {
     *    if (buttonId === 'submit') {
     *        const deferred = $q.defer();
     *        if (validateSomething()) {
     *          deferred.resolve("someResult");
     *         } else {
     *             deferred.reject();  // cancel the submit button's close action
     *          }
     *         return deferred.promise;
     *      }
     * };
     *
     *      $scope.modalManager.setButtonHandler(buttonHandlerFn);
     * </pre>
     */

    public setButtonHandler(buttonHandlerFunction: (id: string) => void | Promise<any>) {
        this.buttonEventCallback = buttonHandlerFunction;
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#close
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     * The close function will close the modal window, passing the provided data (if any) to the successCallback
     * of the modal {@link https://docs.angularjs.org/api/ng/service/$q promise} by resolving the {@link https://docs.angularjs.org/api/ng/service/$q promise}.
     *
     * @param {Object} data Any data to be returned to the resolved modal {@link https://docs.angularjs.org/api/ng/service/$q promise} when the modal is closed.
     *
     */

    public close(dataToReturn: any): void {
        if (this.closeFunction) {
            this.closeFunction(dataToReturn);
        }
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#dismiss
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     * The dismiss function will close the modal window, passing the provided data (if any) to the {@link https://docs.angularjs.org/api/ng/service/$q errorCallback}
     * of the modal {@link https://docs.angularjs.org/api/ng/service/$q promise} by rejecting the {@link https://docs.angularjs.org/api/ng/service/$q promise}.
     *
     * @param {Object} data Any data to be returned to the rejected modal {@link https://docs.angularjs.org/api/ng/service/$q promise} when the modal is closed.
     *
     */

    public dismiss(dataToReturn?: any): void {
        if (this.dismissFunction) {
            this.dismissFunction(dataToReturn);
        }
    }

    public _showDismissButton(): boolean {
        return this.showDismissX;
    }

    public _buttonPressed(button: IModalButtonOptions): void {
        let callbackReturnedPromise: Promise<any> | void;

        if (button.callback) {
            callbackReturnedPromise = button.callback();
        } else if (this.buttonEventCallback) {
            callbackReturnedPromise = this.buttonEventCallback(button.id);
        }

        if (button.action !== ModalButtonActions.None) {
            const exitFn = button.action === ModalButtonActions.Close ? this.close : this.dismiss;

            if (callbackReturnedPromise) {
                callbackReturnedPromise.then((data: any) => exitFn.call(this, data)).catch(lo.noop);
            } else {
                exitFn.call(this);
            }
        }

        if (callbackReturnedPromise) {
            callbackReturnedPromise.then(lo.noop).catch(lo.noop);
        }
    }

    public _handleDismissButton(): void {
        if (this.dismissCallback) {
            this.dismissCallback()
                .then((result: any) => this.dismiss(result))
                .catch(lo.noop);
        } else {
            this.dismiss();
        }
    }

    public _hasButtons(): boolean {
        return this.buttons.length > 0;
    }

    private createButton(buttonConfig: IModalButtonOptions): IModalButtonOptions {
        const config: IModalButtonOptions = {
            ...this._defaultButtonOptions,
            ...buttonConfig
        };

        this.$translate(buttonConfig.label || this._defaultButtonOptions.label).then(
            (label: string) => {
                config.label = label;
            }
        );

        const styleValidated: boolean = Object.keys(ModalButtonStyles).some(
            (key: keyof typeof ModalButtonStyles) => ModalButtonStyles[key] === config.style
        );
        const actionValidated: boolean = Object.keys(ModalButtonActions).some(
            (key: keyof typeof ModalButtonActions) => ModalButtonActions[key] === config.action
        );

        if (!styleValidated) {
            throw new Error('modalService.ModalManager._createButton.illegal.button.style');
        }

        if (!actionValidated) {
            throw new Error('modalService.ModalManager._createButton.illegal.button.action');
        }

        return config;
    }
}
