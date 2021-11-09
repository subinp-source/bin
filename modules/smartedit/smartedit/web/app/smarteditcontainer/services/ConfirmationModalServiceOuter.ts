/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ConfirmationModalConfig,
    ConfirmDialogComponent,
    FundamentalModalButtonAction,
    FundamentalModalButtonStyle,
    GatewayProxied,
    IConfirmationModalService,
    IFundamentalModalButtonOptions,
    IModalButtonOptions,
    IModalService,
    LegacyConfirmationModalConfig,
    ModalButtonActions,
    ModalButtonStyles,
    SeDowngradeService,
    TypedMap
} from 'smarteditcommons';
import { Type } from '@angular/core';

/**
 * @ngdoc overview
 * @name confirmationModalServiceModule
 * @description
 * # The confirmationModalServiceModule
 *
 * The confirmation modal service module provides a service that allows opening a confirmation (an OK/Cancel prompt with
 * a title and content) within a modal.
 *
 * This module is dependent on the {@link modalServiceModule modalServiceModule}.
 */

/**
 * @ngdoc service
 * @name confirmationModalServiceModule.service:confirmationModalService
 *
 * @description
 * Service used to open a confirmation modal in which an end-user can confirm or cancel an action. A confirmation modal
 * consists of a title, content, and an OK and cancel button. This modal may be used in any context in which a
 * confirmation is required.
 */

@SeDowngradeService(IConfirmationModalService)
@GatewayProxied('confirm')
export class ConfirmationModalService {
    constructor(private modalService: IModalService) {}

    /**
     * @ngdoc method
     * @name confirmationModalServiceModule.service:confirmationModalService#open
     * @methodOf confirmationModalServiceModule.service:confirmationModalService
     *
     * @description
     * Uses the {@link modalServiceModule.modalService modalService} to open a confirmation modal.
     *
     * The confirmation modal is initialized by a default i18N key as a title or by an override title passed through the
     * input configuration object. The configuration object must have one and only one of the following parameters set: description, template, or templateUrl
     *
     *
     * @param {LegacyConfirmationModalConfig} LegacyConfirmationModalConfig {@link confirmationModalServiceModule.interface:LegacyConfirmationModalConfig config}
     * used to support AngularJS confirmation dialogs
     *
     * @param {ConfirmationModalConfig} ConfirmationModalConfig {@link confirmationModalServiceModule.interface:ConfirmationModalConfig config}
     * used to support Angular confirmation dialogs
     *
     * @returns {Promise | angular.IPromise} A promise that is resolved when the OK button is actioned or is rejected when the Cancel
     * button is actioned.
     */

    confirm(configuration: LegacyConfirmationModalConfig): angular.IPromise<any> | Promise<any>;
    confirm(configuration: ConfirmationModalConfig): Promise<any>;
    confirm(
        configuration: LegacyConfirmationModalConfig | ConfirmationModalConfig
    ): Promise<any> | angular.IPromise<any> {
        const validationMessage: string | undefined = this._validateConfirmationParameters(
            configuration.description,
            (configuration as LegacyConfirmationModalConfig).template,
            (configuration as LegacyConfirmationModalConfig).templateUrl
        );

        if (validationMessage) {
            return Promise.reject(validationMessage);
        }

        return this.isLegacyConfirm(configuration)
            ? this.angularJsConfirm(configuration)
            : this.angularConfirm(configuration);
    }

    private angularConfirm(configuration: ConfirmationModalConfig): Promise<any> {
        const ref = this.modalService.open<{
            description: string;
            descriptionPlaceholders: TypedMap<string>;
        }>({
            component: ConfirmDialogComponent as Type<any>,
            data: {
                description: configuration.description,
                descriptionPlaceholders: configuration.descriptionPlaceholders
            },
            config: {
                focusTrapped: false,
                modalPanelClass: 'se-confirmation-dialog',
                container: (document.querySelector('[uib-modal-window]') as HTMLElement) || 'body'
            },
            templateConfig: {
                title: configuration.title || 'se.confirmation.modal.title',
                buttons: this.getButtons(configuration),
                isDismissButtonVisible: true
            }
        });

        return new Promise((resolve, reject) => {
            ref.afterClosed.subscribe(resolve, reject);
        });
    }

    private angularJsConfirm(
        configuration: LegacyConfirmationModalConfig
    ): angular.IPromise<any> | Promise<any> {
        return this.modalService.open({
            size: 'md',
            title: configuration.title || 'se.confirmation.modal.title',
            templateInline: configuration.template,
            templateUrl: configuration.templateUrl,
            controller: this._initializeControllerObjectWithScope(configuration),
            cssClasses: 'yFrontModal',
            buttons: this.getLegacyButtons(configuration)
        });
    }

    private isLegacyConfirm(
        configuration: LegacyConfirmationModalConfig | ConfirmationModalConfig
    ): boolean {
        const config: LegacyConfirmationModalConfig = configuration as LegacyConfirmationModalConfig;

        return !!config.template || !!config.templateUrl || !!config.scope;
    }

    private getLegacyButtons(configuration: LegacyConfirmationModalConfig): IModalButtonOptions[] {
        return [
            !configuration.showOkButtonOnly && {
                id: 'confirmCancel',
                label: 'se.confirmation.modal.cancel',
                style: ModalButtonStyles.Default,
                action: ModalButtonActions.Dismiss
            },
            {
                id: 'confirmOk',
                label: 'se.confirmation.modal.ok',
                action: ModalButtonActions.Close
            }
        ].filter((x) => !!x);
    }

    private getButtons(configuration: ConfirmationModalConfig): IFundamentalModalButtonOptions[] {
        return [
            !configuration.showOkButtonOnly && {
                id: 'confirmCancel',
                label: 'se.confirmation.modal.cancel',
                style: FundamentalModalButtonStyle.Default,
                action: FundamentalModalButtonAction.Dismiss
            },
            {
                id: 'confirmOk',
                label: 'se.confirmation.modal.ok',
                style: FundamentalModalButtonStyle.Primary,
                action: FundamentalModalButtonAction.Close
            }
        ].filter((x) => !!x);
    }

    private _validateConfirmationParameters(
        description: string,
        template: string,
        templateUrl: string
    ): string | undefined {
        const checkMoreThanOnePropertySet: string[] = [description, template, templateUrl];
        const numOfProperties: number = checkMoreThanOnePropertySet.filter(
            (property: string) => property !== undefined
        ).length;

        if (numOfProperties === 0) {
            return 'You must have one of the following configuration properties configured: description, template, or templateUrl';
        } else if (numOfProperties > 1) {
            return 'You have more than one of the following configuration properties configured: description, template, or templateUrl';
        }

        return undefined;
    }

    private _initializeControllerObjectWithScope(
        configuration: ConfirmationModalConfig | LegacyConfirmationModalConfig
    ): angular.IControllerConstructor | undefined {
        const config: LegacyConfirmationModalConfig = configuration as LegacyConfirmationModalConfig;

        return config.scope
            ? function() {
                  for (const key in config.scope) {
                      if (config.scope.hasOwnProperty(key)) {
                          this[key] = config.scope[key];
                      }
                  }
              }
            : undefined;
    }
}
