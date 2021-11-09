/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PromiseUtils, TypedMap } from '@smart/utils';
import { IDecoratorDisplayCondition, StringUtils } from 'smarteditcommons';
import { DecoratorService } from 'smartedit/services/DecoratorService';
import { LegacyDecoratorToCustomElementConverter } from 'smartedit/services/sakExecutor/LegacyDecoratorToCustomElementConverter';

describe('decoratorService', () => {
    let promiseUtils: PromiseUtils;
    let stringUtils: StringUtils;
    let decoratorService: DecoratorService;
    let legacyDecoratorToCustomElementConverter: jasmine.SpyObj<
        LegacyDecoratorToCustomElementConverter
    >;
    let anyDecoratorService: {
        _activeDecorators: TypedMap<{ displayCondition?: IDecoratorDisplayCondition }>;
    };

    beforeEach(() => {
        promiseUtils = new PromiseUtils();
        stringUtils = new StringUtils();
        legacyDecoratorToCustomElementConverter = jasmine.createSpyObj<
            LegacyDecoratorToCustomElementConverter
        >('legacyDecoratorToCustomElementConverter', ['convertIfNeeded']);

        decoratorService = new DecoratorService(
            promiseUtils,
            stringUtils,
            legacyDecoratorToCustomElementConverter
        );

        anyDecoratorService = decoratorService as any;
    });

    async function setupAsyncOnGetDecoratorsForComponent(
        componentType: string,
        componentId?: string
    ) {
        return decoratorService.getDecoratorsForComponent(componentType, componentId);
    }

    it('getDecoratorsForComponent will retain a unique set of decorators for a given type', (done) => {
        anyDecoratorService._activeDecorators = {
            decorator0: {},
            decorator1: {},
            decorator2: {},
            decorator3: {}
        };
        decoratorService.addMappings({
            type1: ['decorator1', 'decorator2'],
            type2: ['decorator0']
        });
        decoratorService.addMappings({
            type1: ['decorator2', 'decorator3']
        });

        const promise = setupAsyncOnGetDecoratorsForComponent('type1');
        promise.then((decorators) => {
            expect(decorators).toEqual(['decorator1', 'decorator2', 'decorator3']);
            done();
        });
    });

    it('getDecoratorsForComponent will remove decorator from activeDecorators when displayCondtion function returns false', () => {
        // GIVEN
        const deferred = promiseUtils.defer<boolean>();
        deferred.resolve(false);
        anyDecoratorService._activeDecorators = {
            decorator0: {
                displayCondition: () => {
                    return deferred.promise;
                }
            },
            decorator1: {},
            decorator2: {}
        };
        decoratorService.addMappings({
            type1: ['decorator0', 'decorator1', 'decorator2']
        });

        const promise = setupAsyncOnGetDecoratorsForComponent('type1');
        promise.then((decorators) => {
            expect(decorators).toEqual(['decorator1', 'decorator2']);
        });
    });

    it('activeDecorator.displayConditon should be called with parameters componentType and componentId', () => {
        // GIVEN
        const deferred = promiseUtils.defer<boolean>();
        deferred.resolve(true);
        const decorator0 = {
            displayCondition: () => {
                return deferred.promise;
            }
        };
        spyOn(decorator0, 'displayCondition').and.callThrough();

        anyDecoratorService._activeDecorators = {
            decorator0
        };

        decoratorService.addMappings({
            type1: ['decorator0']
        });
        expect(decorator0.displayCondition).not.toHaveBeenCalled();

        // WHEN
        decoratorService.getDecoratorsForComponent('type1', 'id123');

        // THEN
        expect(decorator0.displayCondition).toHaveBeenCalledWith('type1', 'id123');
    });

    it('activeDecorator.displayCondition should log error if displayCondition does not return boolean or not a function', () => {
        const decorator0 = {
            displayCondition: ('I am not a function' as any) as IDecoratorDisplayCondition
        };

        anyDecoratorService._activeDecorators = {
            decorator0
        };
        decoratorService.addMappings({
            type1: ['decorator0']
        });

        expect(() => {
            decoratorService.getDecoratorsForComponent('type1');
        }).toThrow(
            new Error(
                "The active decorator's displayCondition property must be a function and must return a boolean"
            )
        );
    });

    it('getDecoratorsForComponent will retain a unique set of decorators from all matching regexps', (done) => {
        anyDecoratorService._activeDecorators = {
            decorator1: {},
            decorator2: {},
            decorator3: {},
            decorator4: {},
            decorator5: {},
            decorator6: {}
        };
        decoratorService.addMappings({
            '*Suffix': ['decorator1', 'decorator2'],
            '.*Suffix': ['decorator2', 'decorator3'],
            TypeSuffix: ['decorator3', 'decorator4'],
            '^((?!Middle).)*$': ['decorator4', 'decorator5'],
            PrefixType: ['decorator5', 'decorator6']
        });

        let promise = setupAsyncOnGetDecoratorsForComponent('TypeSuffix');
        promise.then((decorators) => {
            expect(decorators).toEqual([
                'decorator1',
                'decorator2',
                'decorator3',
                'decorator4',
                'decorator5'
            ]);

            promise = setupAsyncOnGetDecoratorsForComponent('TypeSuffixes');
            promise.then((decorators2) => {
                expect(decorators2).toEqual([
                    'decorator2',
                    'decorator3',
                    'decorator4',
                    'decorator5'
                ]);

                promise = setupAsyncOnGetDecoratorsForComponent('MiddleTypeSuffix');
                promise.then((decorators3) => {
                    expect(decorators3).toEqual(['decorator1', 'decorator2', 'decorator3']);
                    done();
                });
            });
        });
    });

    it('enable adds decorators to the Array of active decorators and can be invoked multiple times', () => {
        const displayCondition = {
            displayCondition: undefined as IDecoratorDisplayCondition
        };
        expect(anyDecoratorService._activeDecorators).toEqual({});
        decoratorService.enable('key1');
        expect(anyDecoratorService._activeDecorators).toEqual({
            key1: displayCondition
        });
        decoratorService.enable('key2');
        expect(anyDecoratorService._activeDecorators).toEqual({
            key1: displayCondition,
            key2: displayCondition
        });
        decoratorService.enable('key1');
        expect(anyDecoratorService._activeDecorators).toEqual({
            key1: displayCondition,
            key2: displayCondition
        });
    });

    it('disable removes decorators from the Array of active decorators and can be invoked multiple times', () => {
        const displayCondition = {
            displayCondition: undefined as IDecoratorDisplayCondition
        };
        anyDecoratorService._activeDecorators = {
            key1: displayCondition,
            key2: displayCondition,
            key3: displayCondition
        };
        decoratorService.disable('key1');
        expect(anyDecoratorService._activeDecorators).toEqual({
            key2: displayCondition,
            key3: displayCondition
        });
        decoratorService.disable('key2');
        expect(anyDecoratorService._activeDecorators).toEqual({
            key3: displayCondition
        });
        decoratorService.disable('key1');
        expect(anyDecoratorService._activeDecorators).toEqual({
            key3: displayCondition
        });
    });

    it('getDecoratorsForComponent will filter based on enabled activeDecorators', (done) => {
        decoratorService.addMappings({
            type1: ['decorator1', 'decorator2']
        });

        let promise = setupAsyncOnGetDecoratorsForComponent('type1');
        promise.then((decorators) => {
            expect(decorators).toEqual([]);
            decoratorService.enable('decorator1');

            promise = setupAsyncOnGetDecoratorsForComponent('type1');
            promise.then((decorators2) => {
                expect(decorators2).toEqual(['decorator1']);

                decoratorService.enable('decorator2');

                promise = setupAsyncOnGetDecoratorsForComponent('type1');
                promise.then((decorators3) => {
                    expect(decorators3).toEqual(['decorator1', 'decorator2']);

                    decoratorService.enable('decorator3');

                    promise = setupAsyncOnGetDecoratorsForComponent('type1');
                    promise.then((decorators4) => {
                        expect(decorators4).toEqual(['decorator1', 'decorator2']);

                        done();
                    });
                });
            });
        });
    });

    it('getDecoratorsForComponent will filter based on disabled activeDecorators', (done) => {
        decoratorService.addMappings({
            type1: ['decorator1', 'decorator2']
        });

        decoratorService.enable('decorator2');

        let promise = setupAsyncOnGetDecoratorsForComponent('type1');
        promise.then((decorators) => {
            expect(decorators).toEqual(['decorator2']);

            decoratorService.disable('decorator2');

            promise = setupAsyncOnGetDecoratorsForComponent('type1');
            promise.then((decorators2) => {
                expect(decorators2).toEqual([]);

                decoratorService.disable('decorator3');

                promise = setupAsyncOnGetDecoratorsForComponent('type1');
                promise.then((decorators3) => {
                    expect(decorators3).toEqual([]);
                    done();
                });
            });
        });
    });
});
