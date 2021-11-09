/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { SeDirective } from '../../di';

/**
 * @ngdoc overview
 * @name recompileDomModule
 * @description
 * This module defines the {@link recompileDomModule.directive:recompileDom recompileDom} component.
 */

/**
 * @ngdoc directive
 * @name recompileDomModule.directive:recompileDom
 * @restrict A
 * @requires $timeout
 * @param {= Function} recompileDom Function invoked from the outer scope to trigger the recompiling of the transcluded content.
 * @description
 * The recompile dom directive accepts a function param, and can be applied to any part of the dom.
 * Upon execution of the function, the inner contents of this dom is recompiled by Angular.
 */

@SeDirective({
    selector: '[recompile-dom]',
    replace: false,
    transclude: true,
    controllerAs: 'ctrl',
    template: `<div data-ng-if='ctrl.showContent' data-ng-transclude></div>`,
    inputs: ['recompileDom:=']
})
export class RecompileDomDirective {
    public showContent: boolean = true;
    public recompileDom: () => void;

    $postLink() {
        this.recompileDom = () => {
            this.showContent = false;
            setTimeout(() => {
                this.showContent = true;
            }, 0);
        };
    }
}
