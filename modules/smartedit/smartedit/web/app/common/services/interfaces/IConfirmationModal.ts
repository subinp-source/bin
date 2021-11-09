/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { TypedMap } from '@smart/utils';

export interface ConfirmationModalScope extends angular.IScope {
    [key: string]: any;
}

/**
 * @ngdoc interface
 * @name confirmationModalServiceModule.interface:ConfirmationModalConfig
 * @description configuration interface for Angular based confirmation modal
 */

export interface ConfirmationModalConfig {
    /**
     * @ngdoc property
     * @name description
     * @propertyOf confirmationModalServiceModule.interface:ConfirmationModalConfig
     * @description Message string to be displayed in the confirmation modal
     */

    description?: string;

    /**
     * @ngdoc property
     * @name descriptionPlaceholders
     * @propertyOf confirmationModalServiceModule.interface:ConfirmationModalConfig
     * @description Object containing translations for description
     */

    descriptionPlaceholders?: TypedMap<string>;

    /**
     * @ngdoc property
     * @name title
     * @propertyOf confirmationModalServiceModule.interface:ConfirmationModalConfig
     * @description Confirmation modal title
     */

    title?: string;

    /**
     * @ngdoc property
     * @name showOkButtonOnly
     * @propertyOf confirmationModalServiceModule.interface:ConfirmationModalConfig
     * @description Flags whether only confirm button should be displayed
     */

    showOkButtonOnly?: boolean;
}

/**
 * @ngdoc interface
 * @name confirmationModalServiceModule.interface:LegacyConfirmationModalConfig
 * @description configuration interface for AngularJS based confirmation modal. Extends from {@link  confirmationModalServiceModule.interface:ConfirmationModalConfig ConfirmationModalConfig}
 */

export interface LegacyConfirmationModalConfig extends ConfirmationModalConfig {
    /**
     * @ngdoc property
     * @name scope
     * @propertyOf confirmationModalServiceModule.interface:LegacyConfirmationModalConfig
     * @description Object with properties to be passed to AngularJS modal controller scope
     */

    scope?: ConfirmationModalScope;

    /**
     * @ngdoc property
     * @name template
     * @propertyOf confirmationModalServiceModule.interface:LegacyConfirmationModalConfig
     * @description Inline template to be rendered within the modal
     */

    template?: string;

    /**
     * @ngdoc property
     * @name templateUrl
     * @propertyOf confirmationModalServiceModule.interface:LegacyConfirmationModalConfig
     * @description Template url to be included within the modal
     */

    templateUrl?: string;
}
