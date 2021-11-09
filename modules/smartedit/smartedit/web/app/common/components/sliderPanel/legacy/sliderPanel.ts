/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as lodash from 'lodash';

import { SeComponent } from '../../../di';
import { SliderPanelConfiguration } from '../interfaces';
import { SliderPanelServiceFactory } from '../SliderPanelServiceFactory';
import { SliderPanelService } from '../SliderPanelService';

/* forbiddenNameSpaces angular.module:false */

/**
 * @ngdoc object
 * @name sliderPanelModule.object:CSS_CLASSNAMES
 * @description
 * This object defines injectable Angular constants that store the CSS class names used in the controller to define the
 * rendering and animation of the slider panel.
 */

export const CSS_CLASSNAMES = {
    /**
     * @ngdoc property
     * @name SLIDERPANEL_ANIMATED {String}
     * @propertyOf sliderPanelModule.object:CSS_CLASSNAMES
     *
     * @description
     * The class name applied to the slide panel container to trigger the sliding action in the CSS animation.
     */
    SLIDERPANEL_ANIMATED: 'sliderpanel--animated',

    /**
     * @ngdoc property
     * @name SLIDERPANEL_SLIDEPREFIX {String}
     * @propertyOf sliderPanelModule.object:CSS_CLASSNAMES
     *
     * @description
     * A common prefix for the class names that defines how the content of the slider panel is to be rendered.
     */
    SLIDERPANEL_SLIDEPREFIX: 'sliderpanel--slidefrom'
};

/**
 * @ngdoc overview
 * @name sliderPanelModule
 * @requires sliderPanelServiceModule
 * @requires ngAnimate
 * @description
 * This module defines the slider panel Angular component and its associated constants and controller.
 *
 * ## Basic Implementation
 *
 * To define a new slider panel, you must make some basic modifications to your Angular module and controller, as well
 * as to your HTML template.
 *
 * - ### Angular Module
 *
 * You must add the sliderPanelModule as a dependency to your Angular module.
 *
 * <pre>
 * angular.module('yourApp', ['sliderPanelModule']) { ... }
 * </pre>
 *
 * - ### Angular Controller
 *
 * Within the Angular controller, you must add a function to be instantiated so that the controller will trigger the
 * display of the slider panel.
 * <pre>
 * angular.module('yourApp', ['sliderPanelModule'])
 * .controller('yourController', function() {
 *    ...
 *    this.showSliderPanel = function() {};
 *   ...
 * });
 * </pre>
 *
 * - ### HTML template
 *
 * To include HTML content in the slider panel, you must embed the HTML content in a `<y-slider-panel> </y-slider-panel>` tag.<br />
 * For more information, see the definition of {@link sliderPanelModule.directive:ySliderPanel ySliderPanel} Angular component.
 *
 * <pre>
 * <y-slider-panel data-slider-panel-show="$ctrl.sliderPanelShow">
 *   <content>
 *       any HTML content
 *   </content>
 * </y-slider-panel>
 * </pre>
 *
 * You can then make the slider panel visible by calling the "Show Slider Panel" function defined in the associated controller; for example:
 *
 * <pre>
 * <button class="btn btn-default" ng-click="$ctrl.sliderPanelShow();">
 * Show Slider Panel
 * </button>
 * </pre>
 *
 * ## Advanced Configurations
 *
 * A default set of configurations is applied to all slider panels. You can overwrite and update the default configuration.
 *
 * To update the configuration of a specific slider panel, you must instantiate a JSON object that contains the expected
 * configuration in the Angular controller and provide it to the slider panel controller using the HTML template, for example:
 *
 * <pre>
 *      <y-slider-panel ... data-slider-panel-configuration="$ctrl.sliderPanelConfiguration">
 * </pre>
 *
 * If you define this type of configuration set, SmartEdit will automatically merge it with the slider panel's default configuration.
 * For information about the available settings, see the SliderPanelService.getNewServiceInstance method.
 */

/**
 * @ngdoc directive
 * @name sliderPanelModule.directive:ySliderPanel
 * @restrict E
 * @deprecated since 2005
 * @param {Object} dataSliderPanelConfiguration (optional) A JSON object containing the configuration to be applied on slider panel.
 * @param {Function} dataSliderPanelHide (optional) A function shared in a two ways binding by the main controller and the slider panel and used to trigger the hiding of the slider panel.
 * @param {Function} dataSliderPanelShow A function shared in a two ways binding by the main controller and the slider panel and used to trigger the display of the slider panel.
 * @description
 * Deprecated, use se-slider-panel.
 * The ySliderPanel Angular component allows for the dynamic display of any HTML content on a sliding panel.
 */

@SeComponent({
    selector: 'y-slider-panel',
    templateUrl: 'sliderPanelTemplate.html',
    transclude: true,
    inputs: ['sliderPanelConfiguration', 'sliderPanelHide:=', 'sliderPanelShow:=']
})
export class YSliderPanelComponent {
    public sliderPanelConfiguration: SliderPanelConfiguration;
    public sliderPanelHide: () => angular.IPromise<any>;
    public sliderPanelShow: () => angular.IPromise<any>;
    public isShown: boolean;
    public sliderPanelDismissAction: () => void;
    public slideClassName: string;

    private sliderPanelService: SliderPanelService;
    private uniqueId: string;
    private inlineStyling: {
        container: CSSStyleDeclaration;
        content: CSSStyleDeclaration;
    } = { container: {} as CSSStyleDeclaration, content: {} as CSSStyleDeclaration };

    constructor(
        private $animate: angular.animate.IAnimateService,
        private $element: JQuery,
        private $timeout: angular.ITimeoutService,
        private $window: angular.IWindowService,
        private yjQuery: JQueryStatic,
        private sliderPanelServiceFactory: SliderPanelServiceFactory
    ) {}

    $onInit() {
        this.isShown = false;
        this.uniqueId = lodash.uniqueId();

        // setting new instance of slider panel service
        this.sliderPanelService = this.sliderPanelServiceFactory.getNewServiceInstance(
            this.$element,
            this.$window,
            this.sliderPanelConfiguration
        );

        // variables made available on the html template
        this.sliderPanelConfiguration = this.sliderPanelService.sliderPanelConfiguration;

        this.slideClassName =
            CSS_CLASSNAMES.SLIDERPANEL_SLIDEPREFIX + this.sliderPanelConfiguration.slideFrom;

        this.inlineStyling = {
            container: this.sliderPanelService.inlineStyling.container,
            content: this.sliderPanelService.inlineStyling.content
        };

        this.sliderPanelShow = () => this.showSlider();
        this.sliderPanelHide = () => this.hideSlider();

        this.sliderPanelDismissAction =
            this.sliderPanelConfiguration.modal &&
            this.sliderPanelConfiguration.modal.dismiss &&
            this.sliderPanelConfiguration.modal.dismiss.onClick
                ? this.sliderPanelConfiguration.modal.dismiss.onClick
                : this.hideSlider;

        // applying event handler for screen resize
        this.addScreenResizeEventHandler();

        if (this.sliderPanelConfiguration.displayedByDefault) {
            this.showSlider();
        }
    }

    $onDestroy() {
        this.yjQuery(window).off('resize.doResize');
    }

    public hideSlider(): angular.IPromise<any> {
        return this.$animate
            .removeClass(this.$element, CSS_CLASSNAMES.SLIDERPANEL_ANIMATED)
            .then(() => {
                this.isShown = false;
            });
    }

    public showSlider(): angular.IPromise<any> {
        // container inline styling
        this.sliderPanelService.updateContainerInlineStyling(false);
        this.inlineStyling.container = this.sliderPanelService.inlineStyling.container;

        // container greyed out overlay
        let isSecondarySliderPanel = false;

        angular.forEach(
            angular.element('y-slider-panel.sliderpanel--animated .se-slider-panel-container'),
            (sliderPanelContainer: JQuery<HTMLElement>) => {
                const container = angular.element(sliderPanelContainer);
                if (!isSecondarySliderPanel) {
                    if (
                        container.css('height') === this.inlineStyling.container.height &&
                        container.css('width') === this.inlineStyling.container.width &&
                        container.css('left') === this.inlineStyling.container.left &&
                        container.css('top') === this.inlineStyling.container.top
                    ) {
                        isSecondarySliderPanel = true;
                    }
                }
            }
        );

        // if no related configuration has been set, the no greyed out overlay is set to true for all secondary slider panels.
        this.sliderPanelConfiguration.noGreyedOutOverlay =
            typeof this.sliderPanelConfiguration.noGreyedOutOverlay === 'boolean'
                ? this.sliderPanelConfiguration.noGreyedOutOverlay
                : isSecondarySliderPanel;

        // triggering slider panel display
        this.isShown = true;

        return this.$animate.addClass(this.$element, CSS_CLASSNAMES.SLIDERPANEL_ANIMATED);
    }

    public isSaveDisabled(): boolean {
        if (
            this.sliderPanelConfiguration.modal &&
            this.sliderPanelConfiguration.modal.save &&
            this.sliderPanelConfiguration.modal.save.isDisabledFn
        ) {
            return this.sliderPanelConfiguration.modal.save.isDisabledFn();
        }
        return false;
    }

    private addScreenResizeEventHandler(): void {
        this.yjQuery(window).on('resize.sliderPanelRedraw_' + this.uniqueId, () => {
            if (this.isShown) {
                this.$timeout(() => {
                    this.sliderPanelService.updateContainerInlineStyling(true);
                    this.inlineStyling.container = this.sliderPanelService.inlineStyling.container;
                });
            }
        });
    }
}
