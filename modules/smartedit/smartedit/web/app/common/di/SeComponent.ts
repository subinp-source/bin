/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { functionsUtils, TypedMap } from '@smart/utils';
import { SeComponentConstructor, SeComponentDefinition } from './types';
import { parseDirectiveBindings, parseDirectiveName } from './SeDirective';

/**
 * @ngdoc object
 * @name smarteditServicesModule.object:@SeComponent
 * @deprecated since 1905
 * @description
 * Class level typescript {@link http://www.typescriptlang.org/docs/handbook/decorators.html decorator factory}
 * used to declare a Smartedit web component from a Depencency injection standpoint.
 * The controller alias will be $ctrl.
 * inherits properties from {@link smarteditServicesModule.object:@SeDirective}
 * @deprecated since 1905
 * @param {object} definition the component definition
 * @param {string?} definition.version if set to 'NG', it will be wired through Angular as opposed to AngularJS
 * @param {string?} definition.custom for Angular components, if set to true, the component will be made available as a web component but with a selector to which  is appended.
 * @param {string?} definition.templateUrl the HTML file location for this component
 * @param {string?} definition.template the inline HTML template for this component
 * @param {object?} definition.entryComponents the array of {@link smarteditServicesModule.object:@SeComponent @SeComponent} that this new one requires.
 * @param {object} definition.providers the list of {@link smarteditServicesModule.interface:SeClassProvider service classes},
 * {@link smarteditServicesModule.interface:SeFactoryProvider service factories}, {@link smarteditServicesModule.interface:SeValueProvider value},
 * or multi providers to be injected into the component.
 */

export const SeComponent = function(definition: SeComponentDefinition) {
    return function<T extends SeComponentConstructor>(componentConstructor: T) {
        definition = definition as SeComponentDefinition;

        const nameSet = parseDirectiveName(definition.selector, componentConstructor);

        const component: angular.IComponentOptions = {
            controller: componentConstructor,
            controllerAs: '$ctrl',
            transclude: definition.transclude || true,
            bindings: parseDirectiveBindings(definition.inputs),
            require: definition.require as TypedMap<string>
        };

        if (definition.templateUrl) {
            component.templateUrl = definition.templateUrl;
        } else if (definition.template) {
            component.template = definition.template;
        }

        if (nameSet.restrict !== 'E') {
            const componentName = functionsUtils.getConstructorName(componentConstructor);
            throw new Error(
                `component ${componentName} declared a selector on class or attribute. version 1808 of Smartedit DI limits SeComponents to element selectors`
            );
        }

        componentConstructor.componentName = nameSet.name;
        componentConstructor.definition = component;

        // will be browsed by owning @SeModule
        componentConstructor.entryComponents = definition.entryComponents;
        componentConstructor.providers = definition.providers;

        return componentConstructor;
    };
};
