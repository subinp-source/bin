/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { Injectable } from '@angular/core';
import {
    IDecoratorDisplayCondition,
    IDecoratorService,
    ILegacyDecoratorToCustomElementConverter,
    PromiseUtils,
    SeDowngradeService,
    StringUtils,
    TypedMap
} from 'smarteditcommons';

export interface DecoratorMapping {
    [index: string]: string[];
}
/**
 * @ngdoc service
 * @name smarteditServicesModule.service:decoratorService
 *
 * @description
 * This service enables and disables decorators. It also maps decorators to SmartEdit component types–regardless if they are enabled or disabled.
 *
 */

@SeDowngradeService(IDecoratorService)
@Injectable()
export class DecoratorService implements IDecoratorService {
    private _activeDecorators: TypedMap<{ displayCondition?: IDecoratorDisplayCondition }> = {};
    private componentDecoratorsMap: TypedMap<string[]> = {};

    constructor(
        private promiseUtils: PromiseUtils,
        private stringUtils: StringUtils,
        private legacyDecoratorToCustomElementConverter: ILegacyDecoratorToCustomElementConverter
    ) {}

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:decoratorService#addMappings
     * @methodOf smarteditServicesModule.service:decoratorService
     * @description
     * This method enables a list of decorators for a group of component types.
     * The list to be {@link smarteditServicesModule.service:decoratorService#methods_enable enable} is identified by a matching pattern.
     * The list is enabled when a perspective or referenced perspective that it is bound to is activated/enabled.
     * @param {Object} map A key-map value; the key is the matching pattern and the value is an array of decorator keys. The key can be an exact type, an ant-like wild card, or a full regular expression:
     * <pre>
     * decoratorService.addMappings({
     *  '*Suffix': ['decorator1', 'decorator2'],
     *  '.*Suffix': ['decorator2', 'decorator3'],
     *  'MyExactType': ['decorator3', 'decorator4'],
     *  '^((?!Middle).)*$': ['decorator4', 'decorator5']
     *  });
     * </pre>
     */
    addMappings(map: DecoratorMapping) {
        for (const regexpKey in map) {
            if (map.hasOwnProperty(regexpKey)) {
                const decoratorsArray = map[regexpKey];
                this.legacyDecoratorToCustomElementConverter.convertIfNeeded(decoratorsArray);
                this.componentDecoratorsMap[regexpKey] = lodash.union(
                    this.componentDecoratorsMap[regexpKey] || [],
                    decoratorsArray
                );
            }
        }
    }
    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:decoratorService#enable
     * @methodOf smarteditServicesModule.service:decoratorService
     * @description
     * Enables a decorator
     *
     * @param {String} decoratorKey The key that uniquely identifies the decorator.
     * @param {Function} displayCondition Returns a promise that will resolve to a boolean that determines whether the decorator will be displayed.
     */
    enable(decoratorKey: string, displayCondition?: IDecoratorDisplayCondition) {
        if (!(decoratorKey in this._activeDecorators)) {
            this._activeDecorators[decoratorKey] = {
                displayCondition
            };
        }
    }
    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:decoratorService#disable
     * @methodOf smarteditServicesModule.service:decoratorService
     * @description
     * Disables a decorator
     *
     * @param {String} decoratorKey the decorator key
     */
    disable(decoratorKey: string) {
        if (this._activeDecorators[decoratorKey]) {
            delete this._activeDecorators[decoratorKey];
        }
    }
    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:decoratorService#getDecoratorsForComponent
     * @methodOf smarteditServicesModule.service:decoratorService
     * @description
     * This method retrieves a list of decorator keys that is eligible for the specified component type.
     * The list retrieved depends on which perspective is active.
     *
     * This method uses the list of decorators enabled by the {@link smarteditServicesModule.service:decoratorService#methods_addMappings addMappings} method.
     *
     * @param {String} componentType The type of the component to be decorated.
     * @param {String} componentId The id of the component to be decorated.
     * @returns {Promise} A promise that resolves to a list of decorator keys.
     *
     */
    getDecoratorsForComponent(componentType: string, componentId?: string): Promise<string[]> {
        let decoratorArray: string[] = [];
        if (this.componentDecoratorsMap) {
            for (const regexpKey in this.componentDecoratorsMap) {
                if (this.stringUtils.regExpFactory(regexpKey).test(componentType)) {
                    decoratorArray = lodash.union(
                        decoratorArray,
                        this.componentDecoratorsMap[regexpKey]
                    );
                }
            }
        }
        const promisesToResolve: Promise<string>[] = [];
        const displayedDecorators: string[] = [];
        decoratorArray.forEach((dec: string) => {
            const activeDecorator = this._activeDecorators[dec];
            if (activeDecorator && activeDecorator.displayCondition) {
                if (typeof activeDecorator.displayCondition !== 'function') {
                    throw new Error(
                        "The active decorator's displayCondition property must be a function and must return a boolean"
                    );
                }
                const deferred = this.promiseUtils.defer<string>();
                activeDecorator
                    .displayCondition(componentType, componentId)
                    .then((display: boolean) => {
                        if (display) {
                            deferred.resolve(dec);
                        } else {
                            deferred.resolve(null);
                        }
                    });
                promisesToResolve.push(deferred.promise);
            } else if (activeDecorator) {
                displayedDecorators.push(dec);
            }
        });
        return Promise.all(promisesToResolve).then((decoratorsEnabled) => {
            return displayedDecorators.concat(
                decoratorsEnabled.filter((dec) => {
                    return dec;
                })
            );
        });
    }
}
