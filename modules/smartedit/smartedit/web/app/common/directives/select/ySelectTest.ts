/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as lodash from 'lodash';
import { LogService, TypedMap } from '@smart/utils';

import { VALIDATION_MESSAGE_TYPES } from '../../utils';
import { YSelectApi, YSelectComponent } from './ySelect';
import { FetchStrategy } from '../../components/select';

interface SelectTestConfig<T> {
    id?: string;
    model?: string | string[];
    source?: T[];
    multiSelect?: boolean;
    fetchStrategy?: FetchStrategy<T>;
    getSelectorApi?: (api: { $api: YSelectApi }) => void;
}

describe('ySelectModule - ', () => {
    let $templateCache: jasmine.SpyObj<angular.ITemplateCacheService>;
    let logService: jasmine.SpyObj<LogService>;

    let controller: YSelectComponent<any>;
    const languages = [
        {
            id: 'en',
            label: 'English'
        },
        {
            id: 'de',
            label: 'German'
        },
        {
            id: 'ru',
            label: 'Russian'
        }
    ];

    const products = [
        {
            id: 'product1',
            label: 'Test Product 1',
            image: '',
            price: 123
        },
        {
            id: 'product2',
            label: 'Test Product 2',
            image: '',
            price: 234
        },
        {
            id: 'product3',
            label: 'Test Product 3',
            image: '',
            price: 567
        }
    ];

    const testScopeSingle = {
        id: 'example',
        model: 'en',
        source: languages,
        fetchStrategy: {
            fetchAll: () => {
                return Promise.resolve([...languages]);
            }
        },
        getSelectorApi(api: { $api: YSelectApi }) {
            this._api = api;
        }
    };

    const testScopeMulti = {
        id: 'example',
        model: ['product2'],
        source: products,
        multiSelect: true,
        fetchStrategy: {
            fetchAll: () => {
                return Promise.resolve([...products]);
            }
        },
        getSelectorApi(api: { $api: YSelectApi }) {
            this._api = api;
        }
    };

    function initialize(
        conf: SelectTestConfig<TypedMap<any>>,
        override?: SelectTestConfig<TypedMap<any>>
    ) {
        const config = { ...conf, ...(override || {}) };

        logService = jasmine.createSpyObj('logService', ['debug']);
        $templateCache = jasmine.createSpyObj('$templateCache', ['put', 'get']);

        controller = new YSelectComponent(logService, $templateCache);

        controller.model = config.model;
        controller.id = config.id;
        controller.fetchStrategy = config.fetchStrategy;
        controller.multiSelect = config.multiSelect;
        controller.getApi = config.getSelectorApi;

        controller.exposedModel = {
            $viewChangeListeners: { push: lodash.noop },
            $render: lodash.noop,
            $setViewValue: lodash.noop,
            $modelValue: null
        } as angular.INgModelController;
    }

    describe('Validation', () => {
        it('will throw validation errors if component is not provided with fetchStrategy', () => {
            initialize(testScopeSingle, { fetchStrategy: {} });

            expect(() => controller.$onChanges()).toThrow(
                new Error('neither fetchAll nor fetchPage have been specified in fetchStrategy')
            );
        });

        it('will throw validation errors if component is provided with invalid fetchStrategy', () => {
            initialize(testScopeSingle, {
                fetchStrategy: {
                    fetchAll: () => Promise.resolve() as Promise<any>,
                    fetchPage: () => Promise.resolve() as Promise<any>
                }
            });

            expect(() => controller.$onChanges()).toThrow(
                new Error(
                    'only one of either fetchAll or fetchPage must be specified in fetchStrategy'
                )
            );
        });

        it('will throw validation errors if component is provided with fetchPage but not with fetchEntity or fetchEntities', () => {
            initialize(testScopeSingle, {
                fetchStrategy: {
                    fetchPage: () => Promise.resolve() as Promise<any>,
                    fetchEntity: null,
                    fetchEntities: null
                }
            });

            expect(() => controller.$onChanges()).toThrow(
                new Error(
                    `fetchPage has been specified in fetchStrategy but neither fetchEntity nor fetchEntities are available to load item identified by ${
                        testScopeSingle.model
                    }`
                )
            );
        });
    });

    describe('Initialization', () => {
        it('should initialize with the expected id', () => {
            initialize(testScopeSingle);

            expect(controller.id).toBe('example');
        });

        it('should initialize items with the fetch all strategy and expect the items', (done) => {
            initialize(testScopeSingle);

            controller.$onChanges().then(() => {
                expect(controller.items).toEqual(languages);
                done();
            });
        });

        it('is properly initialized after $onInit lifecycle hook', () => {
            initialize(testScopeSingle);

            controller.$onInit();

            expect(controller.items).toEqual([]);
            expect(controller.searchEnabled).toBe(true);
            expect(controller.resetSearchInput).toBe(true);
            expect(controller.reset).toBeDefined();
        });

        it('should initialize items with the fetch all strategy and expect the items for multiselect', (done) => {
            initialize(testScopeMulti);

            controller.$onChanges().then(() => {
                expect(controller.items).toEqual(products);
                done();
            });
        });
    });

    describe('API exposure', () => {
        it('setting the validation state to error through the api should have the same constant VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR', () => {
            initialize(testScopeSingle, {
                getSelectorApi: (api: { $api: YSelectApi }) => {
                    api.$api.setValidationState(VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR);
                }
            });

            controller.$onInit();

            expect(controller.validationState).toEqual(VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR);
        });

        it('resets the validation state after it had been set with error ', () => {
            initialize(testScopeSingle, {
                getSelectorApi: (api: { $api: YSelectApi }) => {
                    api.$api.setValidationState(VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR);
                    api.$api.resetValidationState();
                }
            });

            controller.$onInit();

            expect(controller.validationState).toBeUndefined();
        });
    });
});
