/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ElementRef,
    Input,
    OnChanges,
    Renderer2,
    SimpleChange,
    SimpleChanges
} from '@angular/core';
import * as angular from 'angular';
import { UpgradeModule } from '@angular/upgrade/static';
import { TypedMap } from '@smart/utils';
import { isEqual } from 'lodash';

export interface CompileHtmlNgController {
    /**
     * Argument of "ng-controller" directive, defining an alias by which a controller can be referenced in the template
     *
     * ng-controller="ctrl as $ctrlAlias"
     */
    alias: string;
    /**
     * Controller constructor function or an array containing string providers and controller constructor function as a last element
     *
     * @example
     * value: function ctrl() {
     *   this.taskName = 'Translation (DE)';
     * }
     *
     * value: [
     *   'experienceService',
     *    function(experienceService) {
     *      // can access experienceService
     *    }
     * ]
     */
    value: angular.IControllerConstructor | any[];
}

export interface DynamicScope extends angular.IScope {
    [key: string]: any;
}

/** @internal */
export abstract class CompileHtml implements OnChanges {
    @Input() scope: TypedMap<any>;
    /**
     * AngularJS legacy support.
     *
     * Wraps template into the element with "ng-controller" directive.
     * Attaches Controller Constructor Function to the view.
     *
     * usage example:
     *
     * Given
     *  "template": '<div>task: {{$announcementCtrl.taskName}}</div>'
     *
     *  "controller": {
     *     alias: '$announcementCtrl',
     *     value: function ctrl() {
     *         'ngInject';
     *         this.taskName = 'Translation (DE)';
     *     }
     *   }
     *
     * Wraps into a <div> with ng-controller
     *
     *     <div ng-controller="$announcementCtrl.compileHtmlNgController.value as $announcementCtrl">
     *         <div>task: {{$announcementCtrl.taskName}}</div>
     *     </div>
     *
     * Which will be rendered as
     *
     *     <div ng-controller="$announcementCtrl.compileHtmlNgController.value as $announcementCtrl">
     *         <div>task: 'Translation (DE)'</div>
     *     </div>
     */
    @Input() compileHtmlNgController: CompileHtmlNgController;

    /**
     * Template url or HTML Template string to be compiled by directive e.g. <div>some text</div>
     */
    private template: string;
    private $scope: DynamicScope;

    constructor(
        private elementRef: ElementRef,
        private renderer: Renderer2,
        private upgrade: UpgradeModule
    ) {}

    ngOnChanges(changes: SimpleChanges) {
        const templateChanged = this.hasInputChanged(changes.template);
        const scopeChanged = this.hasInputChanged(changes.scope);

        this.template = templateChanged ? changes.template.currentValue : this.template;

        if (this.template && (scopeChanged || templateChanged)) {
            this.setCompiledHTML();
        }
    }

    ngOnDestroy() {
        this.resetScope();
        this.removeCompiledHTML();
    }

    private resetScope() {
        if (this.$scope) {
            this.$scope.$destroy();
        }
    }

    private removeCompiledHTML() {
        if (this.elementRef.nativeElement.firstChild) {
            this.renderer.removeChild(
                this.elementRef.nativeElement,
                this.elementRef.nativeElement.firstChild
            );
        }
    }

    private hasInputChanged(change: SimpleChange): boolean {
        return !!change && !isEqual(change.previousValue, change.currentValue);
    }

    private setCompiledHTML(): void {
        this.removeCompiledHTML();
        this.resetScope();

        this.renderer.appendChild(this.elementRef.nativeElement, this.compile());
    }

    private compile(): HTMLElement {
        let template: string = this.isTemplateUrl(this.template)
            ? this.$templateCache.get(this.template)
            : this.template;
        this.$scope = this.$rootScope.$new(false) as DynamicScope;

        if (!template) {
            throw new Error(
                `did not find cached template for file ${this.template} when building tab`
            );
        }

        Object.assign(this.$scope, this.scope || {});

        if (this.compileHtmlNgController) {
            this.$scope[this.compileHtmlNgController.alias] = this;
            template = this.wrapTemplateIntoNgController(
                template,
                this.compileHtmlNgController.alias
            );
        }

        return this.$compile(template)(this.$scope)[0];
    }

    private wrapTemplateIntoNgController(template: string, controllerAlias: string): string {
        return `<div ng-controller="${controllerAlias}.compileHtmlNgController.value as ${controllerAlias}">${template}</div>`;
    }

    private isTemplateUrl(template: string): boolean {
        return /.html$/.test(template);
    }

    private get $templateCache(): angular.ITemplateCacheService {
        return this.upgrade.$injector.get('$templateCache');
    }

    private get $compile(): angular.ICompileService {
        return this.upgrade.$injector.get('$compile');
    }

    private get $rootScope(): angular.IScope {
        return this.upgrade.$injector.get('$rootScope');
    }
}
