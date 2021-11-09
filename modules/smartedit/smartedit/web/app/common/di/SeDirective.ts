/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import * as angular from 'angular';
import { functionsUtils, TypedMap } from '@smart/utils';
import { SeConstructor, SeDirectiveConstructor, SeDirectiveDefinition } from './types';
import { diNameUtils } from './DINameUtils';

/** @internal */
export const parseDirectiveBindings = function(inputs: string[]) {
    let bindings: TypedMap<string>;

    if (inputs && (inputs as any).length) {
        bindings = inputs.reduce((seed: any, element) => {
            const values = element.replace(/\s/g, '').split(':');
            let bindingProperty = values[values.length - 1];
            if (
                !bindingProperty.startsWith('@') &&
                !bindingProperty.startsWith('&') &&
                !bindingProperty.startsWith('=')
            ) {
                bindingProperty = '<' + bindingProperty;
            }
            seed[values[0]] = bindingProperty;
            return seed;
        }, {});
    }
    return bindings;
};

/*
 * used to determine directive name and restrict value in AngularJS given an Angular directive
 */
/** @internal */
export const parseDirectiveName = function(
    selector: string,
    seContructor: SeConstructor
): { name: string; restrict: string } {
    const attributeDirectiveNamePattern = /^\[([-\w]+)\]$/;
    const elementDirectiveNamePattern = /^([-\w]+)$/;

    if (!selector) {
        return { name: diNameUtils.buildComponentName(seContructor), restrict: 'E' };
    } else if (selector.startsWith('.')) {
        return { name: lo.camelCase(selector.substring(1)), restrict: 'C' };
    } else if (attributeDirectiveNamePattern.test(selector)) {
        return {
            name: lo.camelCase(attributeDirectiveNamePattern.exec(selector)[1]),
            restrict: 'A'
        };
    } else if (elementDirectiveNamePattern.test(selector)) {
        return { name: lo.camelCase(elementDirectiveNamePattern.exec(selector)[1]), restrict: 'E' };
    } else {
        const directiveClassName = functionsUtils.getConstructorName(seContructor);
        throw new Error(`SeDirective ${directiveClassName} declared an unexpected selector (${selector}). 
		Make sure to use an element name or class (.class) or attribute ([attribute])`);
    }
};

/**
 * @ngdoc object
 * @name smarteditServicesModule.object:@SeDirective
 * @deprecated since 1905
 * @description
 * Class level typescript {@link http://www.typescriptlang.org/docs/handbook/decorators.html decorator factory}
 * used to declare a Smartedit web directive from a Depencency injection standpoint.
 * This directive will have an isolated scope and will bind its properties to its controller
 * @deprecated since 1905
 * @param {object} definition the component definition
 * @param {string?} definition.version if set to 'NG', it will be wired through Angular as opposed to AngularJS
 * @param {string?} definition.selector The CSS selector that triggers the instantiation of a directive.
 * selector may be declared as one of the following:
 * <ul>
 * <li>element-name: select by element name.</li>
 * <li>.class: select by class name.</li>
 * <li>[attribute]: select by attribute name.</li>
 * </ul>
 * If no selector is set, will default to an element named as the lower camel case of the component class.
 * @param {string[]?} definition.inputs the array of input data binding
 * The inputs property defines a set of directiveProperty to bindingProperty configuration:
 * <ul>
 * <li>directiveProperty specifies the component property where the value is written.</li>
 * <li>bindingProperty specifies the binding type and/or the DOM property where the value is read from.</li>
 * binding type is legacy support for "@", "&" and "=" of Angular 1.x
 * </ul>
 * example: inputs: ['bankName', 'id: account-id']
 * @param {object} definition.providers the list of {@link smarteditServicesModule.interface:SeClassProvider service classes},
 * {@link smarteditServicesModule.interface:SeFactoryProvider service factories}, {@link smarteditServicesModule.interface:SeValueProvider value},
 * or multi providers to be injected into the component.
 */
export const SeDirective = function(definition: SeDirectiveDefinition) {
    return function(directiveConstructor: SeDirectiveConstructor) {
        definition = definition as SeDirectiveDefinition;

        const directive: angular.IDirective = {
            controller: directiveConstructor,
            scope: typeof definition.scope === 'undefined' ? {} : definition.scope,
            replace: definition.replace,
            transclude: definition.transclude,
            template: definition.template,
            templateUrl: definition.templateUrl,
            controllerAs: definition.controllerAs,
            bindToController: parseDirectiveBindings(definition.inputs) || true,
            require: definition.require
        };

        const nameSet = parseDirectiveName(definition.selector, directiveConstructor);

        directive.restrict = nameSet.restrict;

        directiveConstructor.directiveName = nameSet.name;
        directiveConstructor.definition = directive;

        // will be browsed by owning @SeModule
        directiveConstructor.providers = definition.providers;

        return directiveConstructor;
    };
};
