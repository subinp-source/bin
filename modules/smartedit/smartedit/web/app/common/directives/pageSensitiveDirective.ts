import { SeDirective } from '../di';
import { EVENTS, IEventService } from '@smart/utils';
/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/**
 * @ngdoc overview
 * @name pageSensitiveDirectiveModule
 * @description
 * This module defines the {@link pageSensitiveDirectiveModule.directive:pageSensitive pageSensitive} attribute directive.
 */

/**
 * @ngdoc directive
 * @name pageSensitiveDirectiveModule.directive:pageSensitive
 * @restrict A
 * @description
 * Will cause an Angular re-compilation of the node declaring this directive whenever the page identifier in smartEdit layer changes
 */

@SeDirective({
    selector: 'page-sensitive',
    replace: false,
    transclude: true,
    template: `<div class='se-page-sensitive' data-ng-if='ctrl.hasContent' data-ng-transclude></div>`,
    scope: true,
    controllerAs: 'ctrl'
})
export class PageSensitiveDirective {
    public hasContent: boolean = true;
    private unRegisterPageChangeListener: () => void;

    constructor(private crossFrameEventService: IEventService) {}

    $onInit() {
        this.unRegisterPageChangeListener = this.crossFrameEventService.subscribe(
            EVENTS.PAGE_CHANGE,
            () => {
                this.hasContent = false;

                setTimeout(() => {
                    this.hasContent = true;
                }, 0);
            }
        );
    }

    $onDestroy() {
        this.unRegisterPageChangeListener();
    }
}
