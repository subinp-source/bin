/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponentConstructor } from './types';
import { functionsUtils } from '@smart/utils';
import { AbstractDecorator } from './AbstractDecorator';
import { SeCustomComponent } from './SeCustomComponent';

export const SeDecorator = function() {
    return function<T extends SeComponentConstructor>(componentConstructor: T) {
        const componentName = functionsUtils.getConstructorName(componentConstructor);

        /* forbiddenNameSpaces window._:false */
        const definition = window.__smartedit__.getDecoratorPayload(
            'Component',
            componentConstructor
        );

        if (!definition) {
            throw new Error(
                `@SeDecorator ${componentName} should only be used on a @Component decorated class`
            );
        }

        if (definition.template && definition.template.indexOf('<ng-content') === -1) {
            throw new Error(
                `@SeDecorator ${componentName} should contain <ng-content> node in its template`
            );
        }

        if (!(componentConstructor.prototype instanceof AbstractDecorator)) {
            throw new Error(`@SeDecorator ${componentName} should extend AbstractDecorator`);
        }

        return SeCustomComponent()(componentConstructor);
    };
};
