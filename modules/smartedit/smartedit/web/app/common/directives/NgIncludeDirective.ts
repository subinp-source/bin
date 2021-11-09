/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Directive, ElementRef, Input, OnChanges, Renderer2, SimpleChanges } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import { TypedMap } from '@smart/utils';
import { CompileHtml, CompileHtmlNgController } from './CompileHtml';

/**
 * @ngdoc directive
 * @name NgIncludeDirective
 * @element [ngInclude]
 *
 * @description
 *  Used as support for legacy AngularJS templates in Angular components.
 *  Compiles the template provided by the templateUrl and scope
 *  @param {string} ngInclude Template url to be compiled by directive e.g. MyComponentTemplate.html
 *  @param {Object} scope Data to be consumed by AngularJS template
 *  @param {CompileHtmlController} compileHtmlNgController
 */
@Directive({ selector: '[ngInclude]' })
export class NgIncludeDirective extends CompileHtml implements OnChanges {
    @Input() ngInclude: string;
    @Input() scope: TypedMap<any>;
    @Input() compileHtmlNgController: CompileHtmlNgController;

    constructor(elementRef: ElementRef, renderer: Renderer2, upgrade: UpgradeModule) {
        super(elementRef, renderer, upgrade);
    }

    ngOnChanges(changes: SimpleChanges) {
        changes.template = changes.ngInclude;
        super.ngOnChanges(changes);
    }
}
