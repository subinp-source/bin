/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { stringUtils, windowUtils } from '@smart/utils';

import { SeDirective } from '../../di';

import { OVERLAY_ID } from '../../utils';
import * as popper from 'popper.js';

export enum YPopoverTrigger {
    Hover = 'hover',
    Click = 'click',
    Focus = 'focus',
    OutsideClick = 'outsideClick',
    None = 'none'
}

export enum YPopoverOnClickOutside {
    Close = 'close',
    None = 'none'
}

export interface YPopoverConfig {
    placement: popper.Placement;
    trigger: YPopoverTrigger;
    container: string | HTMLElement;
    onShow: () => void;
    onHide: () => void;
    onChanges: (element: HTMLElement, data: popper.Data) => void;
    onClickOutside?: YPopoverOnClickOutside.Close;
    modifiers?: popper.Modifiers;
}

export interface YPopoverScope extends angular.IScope {
    template: string;
    placement: popper.Placement;
    title: string;
}

/**
 * @ngdoc directive
 * @name yPopoverModule.directive:yPopover
 * @scope
 * @restrict A
 *
 * @description
 * This directive attaches a customizable popover on a DOM element.
 * @param {<String=} template the HTML body to be used in the popover body, it will automatically be trusted by the directive. Optional but exactly one of either template or templateUrl must be defined.
 * @param {<String=} templateUrl the location of the HTML template to be used in the popover body. Optional but exactly one of either template or templateUrl must be defined.
 * @param {<String=} title the title to be used in the popover title section. Optional.
 * @param {<String=} placement the placement of the popover around the target element. Possible values are <b>top, left, right, bottom</b>, as well as any
 * concatenation of them with the following format: placement1-placement2 such as bottom-right. Optional, default value is top.
 * @param {=String=} trigger the event type that will trigger the popover. Possibles values are <b>hover, click, outsideClick, none</b>. Optional, default value is 'click'.
 */

@SeDirective({
    selector: '[y-popover]',
    transclude: true,
    replace: false,
    controllerAs: 'ypop',
    inputs: ['templateUrl:?', 'template:?', 'title:?', 'placement:?', 'trigger:?', 'isOpen:?']
})
export class YPopoverDirective {
    public title?: string = '';
    public template: string;
    public placement?: popper.Placement = 'top';

    private transcludedContent: JQuery<Element> = null;
    private transclusionScope: angular.IScope = null;
    private engine: any = null;
    private config: YPopoverConfig = null;
    private templateUrl?: string;
    private trigger?: YPopoverTrigger = YPopoverTrigger.Click;
    private isOpen?: boolean = false;
    private previousIsOpen: boolean = false;

    constructor(
        private $scope: YPopoverScope,
        private $timeout: angular.ITimeoutService,
        private $element: JQuery<Element>,
        private yjQuery: JQueryStatic,
        private $templateCache: angular.ITemplateCacheService,
        private yPopupEngineService: any,
        private $transclude: angular.ITranscludeFunction
    ) {
        this.$transclude((clone: JQuery<Element>, scope: angular.IScope) => {
            this.$element.append(clone);
            this.transcludedContent = clone;
            this.transclusionScope = scope;
        });
    }

    $onInit() {
        const anchor = this.$element[0];
        const overlay = windowUtils.isIframe() ? this.yjQuery('#' + OVERLAY_ID) : null;

        this.$scope.placement = this.placement;
        this.$scope.template = this.template;
        this.$scope.title = this.title;

        this.config = {
            placement: this.placement || 'top',
            trigger: this.trigger || YPopoverTrigger.Click,
            container: overlay && overlay.length ? overlay[0] : 'body',
            onShow: () => {
                this.isOpen = true;
            },
            onHide: () => {
                this.isOpen = false;
            },
            onChanges: (element: HTMLElement, data: popper.Data) => {
                this.$timeout(() => {
                    if (this.placement !== data.placement) {
                        this.placement = data.placement;
                    }
                });
            }
        };

        this.engine = new this.yPopupEngineService(
            anchor,
            this.getTemplate(),
            this.$scope,
            this.config
        );
        this.isOpen = !stringUtils.isBlank(this.isOpen) ? this.isOpen : this.engine.isOpen;
    }

    $doCheck() {
        if (this.previousIsOpen !== this.isOpen) {
            if (this.isOpen) {
                this.engine.show();
            } else {
                this.engine.hide();
            }
            this.previousIsOpen = this.isOpen;
        }
    }

    $onChanges() {
        if (this.templateUrl) {
            this.template = this.$templateCache.get(this.templateUrl);
            delete this.templateUrl;
        }
        if (this.engine) {
            this.config.placement = this.placement || 'top';
            this.config.trigger = this.trigger || YPopoverTrigger.Click;
            this.engine.configure(this.config);
        }
    }

    $onDestroy() {
        this.engine.dispose();
        this.transcludedContent.remove();
        this.transclusionScope.$destroy();
    }

    getTemplate(): string {
        return `<y-popover-popup class="se-popover-popup" data-placement="ypop.placement" data-template="ypop.template" data-title="ypop.title"></y-popover-popup>`;
    }
}
