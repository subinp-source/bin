/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { IModalButtonOptions } from './IModalButtonOptions';

/**
 * @ngdoc interface
 * @name modalServiceModule.interface:IModalConfig
 *
 * @description
 * Interface for IModalConfig
 */

export interface IModalConfig {
    /**
     * @ngdoc property
     * @name title: String
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * title?: String
     *
     */
    title?: string;
    /**
     * @ngdoc property
     * @name titleSuffix
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * titleSuffix?: String
     *
     */
    titleSuffix?: string;
    /**
     * @ngdoc property
     * @name cssClasses
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * cssClasses?: String
     *
     */
    cssClasses?: string;
    /**
     * @ngdoc property
     * @name buttons
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * buttons?: {@link modalServiceModule.interface:IModalButtonOptions IModalButtonOptions[]}
     *
     */
    buttons?: IModalButtonOptions[];
    /**
     * @ngdoc property
     * @name size
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * size?: String
     *
     */
    size?: string;
    /**
     * @ngdoc property
     * @name templateInline
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * templateInline?: String
     *
     */
    templateInline?: string;
    /**
     * @ngdoc property
     * @name templateUrl
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * templateUrl?: String
     *
     */
    templateUrl?: string;
    /**
     * @ngdoc property
     * @name animation
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * animation?: Boolean
     *
     */
    animation?: boolean;
    /**
     * @ngdoc property
     * @name modalInstance
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * modalInstance?: IModalInstanceService
     *
     */
    controllerAs?: string;
    /**
     * @ngdoc property
     * @name inlineTemplateSelector
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * inlineTemplateSelector?: String
     *
     */
    inlineTemplateSelector?: string;
    /**
     * @ngdoc property
     * @name controller
     * @propertyOf modalServiceModule.interface:IModalConfig
     * @description
     * controller?: IControllerConstructor | String
     *
     */
    controller?: angular.IControllerConstructor | string;
}
