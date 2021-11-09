/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDirective } from '../../di';
import * as angular from 'angular';
import { windowUtils, OVERLAY_ID } from '../../utils';
import { YPopoverTrigger } from '../../components/popover/yPopoverDirective';
import { YPopupOverlayScope, YPopupOverlayTrigger } from './yPopupEngineService';
import * as lo from 'lodash';
import { YPopupOverlayUtilsClickOrderService } from './yPopupOverlayUtilsClickOrderService';
import { YPopupOverlayUtilsDOMCalculations } from './yPopupOverlayUtilsDOMCalculations';

/**
 * A string, representing the prefix of the generated UUID for each yPopupOverlay.
 * This uuid is added as an attribute to the overlay DOM element.
 */

export interface YPopupOverlaySize {
    width: number;
    height: number;
}

export const yPopupOverlayUuidPrefix = 'ypo-uuid-_';

export interface YPopupDirectiveConfig {
    valign: 'bottom' | 'top';
    halign: 'right' | 'left';
    template: string;
    templateUrl: string;
}
/**
 *  @ngdoc directive
 *  @name yPopupOverlayModule.directive:yPopupOverlay
 *  @restrict A
 *  @deprecated since 2005
 *
 *  @description
 *  Deprecated, use {@link smarteditCommonsModule.component:PopupOverlayComponent PopupOverlayComponent}
 *  The yPopupOverlay is meant to be a directive that allows popups/overlays to be displayed attached to any element.
 *  The element that the directive is applied to is called the anchor element. Once the popup is displayed, it is
 *  positioned relative to the anchor, depending on the configuration provided.<br />
 *  <br />
 *  <h3>Scrolling Limitation</h3>
 *  In this initial implementation, it appends the popup element to the body, and positions itself relative to body.
 *  This means that it can handle default window/body scrolling, but if the anchor is contained within an inner
 *  scrollable DOM element then the positions will not work correctly.
 *
 *  @param {< Object} yPopupOverlay A popup overlay configuration object that must contain either a template or a templateUrl
 *  @param {string} yPopupOverlay.template|templateUrl An html string template or a url to an html file
 *  @param {string =} [yPopupOverlay.halign='right'] Aligns the popup horizontally
 *      relative to the anchor (element). Accepts values: 'left' or 'right'.
 *  @param {string =} [yPopupOverlay.valign='bottom'] Aligns the popup vertically
 *      relative to the anchor (element). Accepts values: 'top' or 'bottom'.
 *  @param {@ string =} yPopupOverlayTrigger 'true'|'false'|'click' Controls when the overlay is displayed.
 *      If yPopupOverlayTrigger is true, the overlay is displayed, if false (or something other then true or click)
 *      then the overlay is hidden.
 *      If yPopupOverlayTrigger is 'click' then the overlay is displayed when the anchor (element) is clicked on
 *  @param {& expression =} yPopupOverlayOnShow An angular expression executed whenever this overlay is displayed
 *  @param {& expression =} yPopupOverlayOnHide An angular expression executed whenever this overlay is hidden
 */

@SeDirective({
    selector: '[y-popup-overlay]',
    scope: false,
    inputs: [
        'yPopupOverlay:=',
        'yPopupOverlayTrigger:=',
        'yPopupOverlayOnShow:&',
        'yPopupOverlayOnHide:&'
    ]
})
export class YPopupOverlayDirective {
    public uuid: string;

    private popupElement: JQuery<Element>;
    private popupSize: YPopupOverlaySize;
    private popupDisplayed: boolean;
    private popupElementScope: angular.IScope;
    private oldTrigger: YPopupOverlayTrigger;
    private untrigger: () => void;
    private doCheckTrigger: YPopupOverlayTrigger;
    private active: boolean;

    // Attribute bindings

    private yPopupOverlayTrigger: YPopupOverlayTrigger;
    private yPopupOverlay: YPopupDirectiveConfig;
    private yPopupOverlayOnShow: () => void;
    private yPopupOverlayOnHide: () => void;

    constructor(
        private $scope: YPopupOverlayScope,
        private $element: JQuery<Element>,
        private $compile: angular.ICompileService,
        private $attrs: angular.IAttributes,
        private $timeout: angular.ITimeoutService,
        private yjQuery: JQueryStatic,
        private yPopupOverlayUtilsDOMCalculations: YPopupOverlayUtilsDOMCalculations,
        private yPopupOverlayUtilsClickOrderService: YPopupOverlayUtilsClickOrderService
    ) {
        this.$scope.closePopupOverlay = () => {
            this.hide();
        };
    }

    public $onInit() {
        this.uuid = lo.uniqueId(yPopupOverlayUuidPrefix);
        this.popupDisplayed = false;

        this.resetPopupSize();
        this.updateTriggers(YPopoverTrigger.Click);

        // only activate
        this.active = !!this.yPopupOverlay;
    }
    public $doCheck() {
        if (!this.active) {
            return;
        }

        this.checkPopupSizeChanged();

        if (this.yPopupOverlayTrigger !== this.doCheckTrigger) {
            this.doCheckTrigger = this.yPopupOverlayTrigger;
            this.updateTriggers(this.yPopupOverlayTrigger);
        }
    }

    public $onDestroy() {
        if (this.untrigger) {
            this.untrigger();
        }
        this.hide();
    }

    /**
     * Handles click event, triggered by the
     */
    public onBodyElementClicked($event: Event): boolean {
        if (!this.popupElement) {
            return false;
        }

        const isPopupClicked = this.isChildOfElement(this.popupElement, $event.target as Element);
        const isAnchorClicked = this.isChildOfElement(this.$element, $event.target as Element);

        if (!isPopupClicked && !isAnchorClicked) {
            this.hide();
            $event.stopPropagation();
            $event.preventDefault();

            return true;
        }

        return false;
    }

    /**
     * Check if a this.yjQuery element contains a child element.
     * @param parentElement
     * @param childElement Click event target
     * @returns {boolean|*} True if parent contains child
     */

    private isChildOfElement(parentElement: JQuery<Element>, childElement: Element): boolean {
        return (
            parentElement[0] === childElement ||
            this.yjQuery.contains(parentElement[0], childElement)
        );
    }

    /**
     * Calculates the size of the popup content and stores it.
     * Returns true if the size has changed since the previous call.
     */

    private checkPopupSizeChanged(): void {
        if (this.popupElement) {
            const firstChildOfRootPopupElement = this.popupElement.children().first();

            if (!firstChildOfRootPopupElement[0]) {
                return;
            }

            const popupBounds = firstChildOfRootPopupElement[0].getBoundingClientRect();

            const changed =
                popupBounds.width !== this.popupSize.width ||
                popupBounds.height !== this.popupSize.height;
            this.popupSize = {
                width: popupBounds.width,
                height: popupBounds.height
            };
            if (changed) {
                this.updatePopupElementPositionAndSize();
            }
        }
    }

    private updatePopupElementPositionAndSize(): void {
        if (!this.popupElement) {
            return;
        }

        try {
            // Always calculate based on first child of popup, but apply css to root of popup
            // otherwise any applied css may harm the content by enforcing size
            const anchorBounds = this.$element[0].getBoundingClientRect();
            const position = this.yPopupOverlayUtilsDOMCalculations.calculatePreferredPosition(
                anchorBounds,
                this.popupSize.width,
                this.popupSize.height,
                this.yPopupOverlay.valign,
                this.yPopupOverlay.halign
            );
            this.yPopupOverlayUtilsDOMCalculations.adjustHorizontalToBeInViewport(position);
            this.popupElement.css((position as unknown) as JQLiteCssProperties);
        } catch (e) {
            // There are racing conditions where some of the elements are not ready yet...
            // Since we're constantly recalculating, this is just an easy way to avoid all these conditions
        }
    }

    private togglePoppup = ($event: Event): void => {
        $event.stopPropagation();
        $event.preventDefault();

        return this.popupDisplayed ? this.hide() : this.show();
    };

    private getTemplateString(): string {
        const outerElement = this.yjQuery('<div>');

        outerElement.attr('data-uuid', this.uuid);
        outerElement.addClass('se-popover-outer');

        let innerElement;
        if (this.yPopupOverlay.template) {
            innerElement = this.yjQuery('<div>');
            innerElement.html(this.yPopupOverlay.template);
        } else if (this.yPopupOverlay.templateUrl) {
            innerElement = this.yjQuery('<data-ng-include>');
            innerElement.attr('src', "'" + this.yPopupOverlay.templateUrl + "'");
        } else {
            throw new Error('yPositiongetTemplateString() - Missing template');
        }

        innerElement.addClass('se-popover-inner');
        outerElement.append(innerElement);

        return outerElement[0].outerHTML;
    }

    private hide(): void {
        if (!this.popupDisplayed) {
            return;
        }

        this.yPopupOverlayUtilsClickOrderService.unregister(this);

        if (this.popupElementScope) {
            this.popupElementScope.$destroy();
            this.popupElementScope = null;
        }
        if (this.popupElement) {
            this.popupElement.remove();
            this.popupElement = null;
        }
        if (this.$attrs.yPopupOverlayOnHide) {
            // We want to evaluate this angular expression inside of a digest cycle
            this.$timeout(() => this.yPopupOverlayOnHide());
        }

        this.resetPopupSize();
        this.popupDisplayed = false;
    }

    private show(): void {
        if (this.popupDisplayed) {
            return;
        }

        const popupElement = this.getTemplateString();

        this.popupElementScope = this.$scope.$new(false);
        this.popupElement = this.$compile(popupElement)(this.popupElementScope);
        const containerElement = windowUtils.isIframe()
            ? this.yjQuery('#' + OVERLAY_ID)
            : this.yjQuery('body');
        this.updatePopupElementPositionAndSize();
        this.popupElement.appendTo(containerElement);

        angular.element(() => {
            this.updatePopupElementPositionAndSize();
            this.yPopupOverlayUtilsClickOrderService.register(this);
            if (this.$attrs.yPopupOverlayOnShow) {
                // We want to evaluate this angular expression inside of a digest cycle
                this.$timeout(() => {
                    this.yPopupOverlayOnShow();
                });
            }
        });
        this.popupDisplayed = true;
    }

    private updateTriggers(trigger: YPopupOverlayTrigger): void {
        if (this.oldTrigger === trigger) {
            return;
        }

        this.oldTrigger = trigger;

        if (this.untrigger) {
            this.untrigger();
        }

        if (trigger === YPopoverTrigger.Click) {
            angular.element(this.$element).on('click', this.togglePoppup);

            this.untrigger = () => {
                angular.element(this.$element).off('click', this.togglePoppup);
            };
            return;
        }

        if (trigger === 'true' || trigger === true) {
            this.show();
        } else {
            this.hide();
        }
    }

    private resetPopupSize(): void {
        this.popupSize = {
            width: 0,
            height: 0
        };
    }
}
