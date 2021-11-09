/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDirective } from 'smarteditcommons/di';

/**
 * @ngdoc directive
 * @name smarteditCommonsModule.directive:compileHtmlLegacy
 * @scope
 * @restrict A
 * @attribute compile-html
 *
 * @description
 * Directive responsible for evaluating and compiling HTML markup.
 *
 * @param {String} String HTML string to be evaluated and compiled in the parent scope.
 * @example
 * <pre>
 *      <div compile-html="<a data-ng-click=\"injectedContext.onLink( item.path )\">{{ item[key.property] }}</a>"></div>
 * </pre>
 */
@SeDirective({
    selector: '[compile-html]'
})
export class CompileHtmlLegacyDirective {
    constructor(
        private $compile: angular.ICompileService,
        private $scope: angular.IScope,
        private $element: JQuery<HTMLElement>,
        private $attrs: angular.IAttributes
    ) {}

    $postLink() {
        this.$scope.$parent.$watch(
            (scope) => scope.$eval(this.$attrs.compileHtml),
            (value) => {
                this.$element.html(value);
                this.$compile(this.$element.contents() as any)(this.$scope.$parent);
            }
        );
    }
}
