/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { promiseHelper, LogHelper } from 'testhelpers';
import { noop } from 'lodash';
import {
    IDropdownPopulator,
    ISEDropdownServiceConstructor,
    SystemEventService,
    SEDropdownServiceFactory
} from 'smarteditcommons';

import { DropdownPopulatorFetchPageResponse, DropdownPopulatorPagePayload } from '../../populators';

describe('seDropdownService', () => {
    let optionsDropdownPopulator: jasmine.SpyObj<IDropdownPopulator>;
    let uriDropdownPopulator: jasmine.SpyObj<IDropdownPopulator>;
    let SEDropdownService: ISEDropdownServiceConstructor;
    let componentXDropdownPopulator: jasmine.SpyObj<IDropdownPopulator>;
    let componentYdropdownADropdownPopulator: jasmine.SpyObj<IDropdownPopulator>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let getKeyHoldingDataFromResponse: jasmine.Spy;
    interface MockFetchPageResponse {
        someArray: any[];
    }
    const mockFetchPageResponseKey: keyof Pick<MockFetchPageResponse, 'someArray'> = 'someArray';

    const $q = promiseHelper.$q();

    const options = [
        {
            id: 'id1',
            label: 'label1 - sample'
        },
        {
            id: 'id2',
            label: 'label2 - sample option'
        },
        {
            id: 'id3',
            label: 'label3 - option'
        }
    ];

    const mockSitesPage1 = [
        {
            id: 'site1',
            label: 'Site1'
        },
        {
            id: 'site2',
            label: 'Site2'
        },
        {
            id: 'site3',
            label: 'Site3'
        },
        {
            id: 'site4',
            label: 'Site4'
        },
        {
            id: 'site5',
            label: 'Site5'
        },
        {
            id: 'site6',
            label: 'Site6'
        },
        {
            id: 'site7',
            label: 'Site7'
        },
        {
            id: 'site8',
            label: 'Site8'
        },
        {
            id: 'site9',
            label: 'Site9'
        },
        {
            id: 'site10',
            label: 'Site10'
        }
    ];

    const mockSitesPage2 = [
        {
            id: 'site11',
            label: 'Site11'
        },
        {
            id: 'site12',
            label: 'Site12'
        },
        {
            id: 'site13',
            label: 'Site13'
        },
        {
            id: 'site14',
            label: 'Site14'
        },
        {
            id: 'site15',
            label: 'Site15'
        },
        {
            id: 'site16',
            label: 'Site16'
        },
        {
            id: 'site17',
            label: 'Site17'
        },
        {
            id: 'site18',
            label: 'Site18'
        },
        {
            id: 'site19',
            label: 'Site19'
        },
        {
            id: 'site20',
            label: 'Site20'
        }
    ];

    const mockSites = [...mockSitesPage1, ...mockSitesPage2];

    const mockFetchPage1Response: MockFetchPageResponse = {
        someArray: mockSitesPage1
    };

    const fieldWithUri = {
        cmsStructureType: 'EditableDropdown',
        qualifier: 'dropdownA',
        i18nKey: 'type.thesmarteditComponentType.dropdownA.name',
        uri: '/someuri',
        smarteditComponentType: 'componentX'
    };

    const fieldWithDependsOn = {
        cmsStructureType: 'EditableDropdown',
        qualifier: 'dropdownA',
        i18nKey: 'type.thesmarteditComponentType.dropdownA.name',
        uri: '/someuri',
        dependsOn: 'dropdown1,dropdown2',
        smarteditComponentType: 'componentX'
    };

    const fieldWithNoneNoPopulator = {
        cmsStructureType: 'EditableDropdown',
        qualifier: 'dropdownX',
        i18nKey: 'type.thesmarteditComponentType.dropdownA.name',
        smarteditComponentType: 'componentY'
    };

    const fieldWithBoth = {
        cmsStructureType: 'EditableDropdown',
        qualifier: 'dropdownA',
        i18nKey: 'type.thesmarteditComponentType.dropdownA.name',
        uri: '/someuri',
        options: [] as any[],
        smarteditComponentType: 'componentX'
    };

    const fieldWithPropertyType = {
        cmsStructureType: 'SingleProductSelector',
        propertyType: 'customPropertyType',
        qualifier: 'dropdownA',
        i18nKey: 'type.thesmarteditComponentType.product.name',
        required: true
    };

    const model = {
        dropdown1: '1',
        dropdown2: '2',
        dropdownA: 'id1'
    };

    const qualifier = 'dropdownA';
    const id = new Date().valueOf().toString(10);

    let mockProviders: {
        uriDropdownPopulator: typeof uriDropdownPopulator;
        optionsDropdownPopulator: typeof optionsDropdownPopulator;
        customPropertyTypeDropdownPopulator: { type: string };
        componentYdropdownADropdownPopulator: typeof componentYdropdownADropdownPopulator;
        componentXDropdownPopulator: typeof componentXDropdownPopulator;
    };
    beforeEach(() => {
        uriDropdownPopulator = jasmine.createSpyObj<IDropdownPopulator>('uriDropdownPopulator', [
            'populate',
            'fetchPage'
        ]);
        uriDropdownPopulator.populate.and.returnValue(Promise.resolve(options));

        uriDropdownPopulator.fetchPage.and.callFake((payload: DropdownPopulatorPagePayload) => {
            const { currentPage, pageSize } = payload;
            const results = [...mockSites].slice(currentPage * pageSize).slice(0, pageSize);
            const response: MockFetchPageResponse = {
                someArray: results
            };
            return Promise.resolve(response);
        });

        optionsDropdownPopulator = jasmine.createSpyObj<IDropdownPopulator>(
            'optionsDropdownPopulator',
            ['populate']
        );

        componentYdropdownADropdownPopulator = jasmine.createSpyObj(
            'componentYdropdownADropdownPopulator',
            ['populate']
        );
        componentYdropdownADropdownPopulator.populate.and.returnValue(options);

        componentXDropdownPopulator = jasmine.createSpyObj('componentXDropdownPopulator', [
            'populate'
        ]);

        componentXDropdownPopulator.populate.and.returnValue(options);

        mockProviders = {
            uriDropdownPopulator,
            optionsDropdownPopulator,
            customPropertyTypeDropdownPopulator: {
                type: 'customPropertyTypeDropdownPopulator'
            },
            componentYdropdownADropdownPopulator,
            componentXDropdownPopulator
        };
    });

    const injectProvider = (provider: keyof typeof mockProviders) => mockProviders[provider];

    let $injector: jasmine.SpyObj<angular.auto.IInjectorService>;

    beforeEach(() => {
        $injector = jasmine.createSpyObj<angular.auto.IInjectorService>('$injector', [
            'get',
            'has'
        ]);
        $injector.get.and.callFake(injectProvider);
        $injector.has.and.callFake((provider: string) => {
            return (
                [
                    'uriDropdownPopulator',
                    'optionsDropdownPopulator',
                    'customPropertyTypeDropdownPopulator',
                    'componentYdropdownADropdownPopulator',
                    'componentXDropdownPopulator'
                ].indexOf(provider) !== -1
            );
        });

        systemEventService = jasmine.createSpyObj('systemEventService', [
            'subscribe',
            'publishAsync'
        ]);

        getKeyHoldingDataFromResponse = jasmine.createSpy('getKeyHoldingDataFromResponse');

        SEDropdownService = SEDropdownServiceFactory(
            $injector,
            new LogHelper(),
            'LinkedDropdown',
            'ClickDropdown',
            'DropdownPopulator',
            systemEventService,
            getKeyHoldingDataFromResponse,
            {
                VALIDATION_ERROR: 'ValidationError',
                WARNING: 'Warning'
            }
        );
    });

    it('seDropdown initializes fine', () => {
        const seDropdown = new SEDropdownService({
            field: fieldWithNoneNoPopulator,
            model,
            qualifier,
            id
        });

        expect((seDropdown as any).field).toEqual(fieldWithNoneNoPopulator);
        expect((seDropdown as any).model).toEqual(model);
        expect(seDropdown.qualifier).toEqual(qualifier);
    });

    describe('init method - ', () => {
        it('GIVEN SEDropdownService is initialized WHEN the field object has both options and uri attributes THEN it throws an error', function() {
            const seDropdown = new SEDropdownService({
                field: fieldWithBoth,
                model,
                qualifier,
                id
            });

            expect(() => {
                return seDropdown.init();
            }).toThrow(new Error('se.dropdown.contains.both.uri.and.options'));
        });

        it('GIVEN SEDropdownService is initialized WHEN the field object has dependsOn attribute THEN init method must register an event', () => {
            const seDropdown = new SEDropdownService({
                field: fieldWithDependsOn,
                model,
                qualifier,
                id
            });

            spyOn(seDropdown as any, '_respondToChange').and.returnValue(undefined);
            uriDropdownPopulator.populate.and.returnValue($q.when(options));
            seDropdown.init();

            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                id + 'LinkedDropdown',
                jasmine.any(Function)
            );
            const respondToChangeCallback = systemEventService.subscribe.calls.argsFor(0)[1];
            respondToChangeCallback();
            expect((seDropdown as any)._respondToChange).toHaveBeenCalled();
        });
    });

    it('GIVEN SEDropdownService is initialized WHEN fetchAll is called THEN the respective populator is called with the correct payload', async () => {
        const searchKey = 'sample';
        const selection = {
            a: 'b'
        };

        const seDropdown = new SEDropdownService({
            field: fieldWithUri,
            model,
            qualifier,
            id
        });
        // here
        const searchResult = options.filter(
            (option) => option.label.toUpperCase().indexOf(searchKey.toUpperCase()) > -1
        );
        uriDropdownPopulator.populate.and.returnValue(Promise.resolve(searchResult));
        seDropdown.init();
        (seDropdown as any).selection = selection;
        await (seDropdown as any).fetchAll(searchKey);

        expect(uriDropdownPopulator.populate).toHaveBeenCalledWith({
            field: fieldWithUri,
            model,
            search: searchKey,
            selection
        });
        expect((seDropdown as any).items).toEqual([
            {
                id: 'id1',
                label: 'label1 - sample'
            },
            {
                id: 'id2',
                label: 'label2 - sample option'
            }
        ]);
    });

    it('GIVEN SEDropdownService is initialized WHEN triggerAction is called THEN publishAsync method is called with correct attributes', async () => {
        const seDropdown = new SEDropdownService({
            field: fieldWithUri,
            model,
            qualifier,
            id
        });

        uriDropdownPopulator.populate.and.returnValue(Promise.resolve(options));
        seDropdown.init();
        await (seDropdown as any).fetchAll();
        seDropdown.triggerAction();

        expect(systemEventService.publishAsync).toHaveBeenCalledWith(id + 'LinkedDropdown', {
            qualifier,
            optionObject: {
                id: 'id1',
                label: 'label1 - sample'
            }
        });
    });

    it('GIVEN SEDropdownService is initialized WHEN _respondToChange is called and if the fields dependsOn doesnot match the input qualifier THEN then nothing happens (populator not called)', () => {
        const seDropdown = new SEDropdownService({
            field: fieldWithUri,
            model,
            qualifier,
            id
        } as any);

        uriDropdownPopulator.populate.and.returnValue(Promise.resolve(options));

        (seDropdown as any)._respondToChange(qualifier, {
            id: 'id1',
            label: 'label1 - sample'
        });

        expect(uriDropdownPopulator.populate).not.toHaveBeenCalled();
    });

    it('GIVEN SEDropdownService is initialized WHEN _respondToChange is called and if the fields dependsOn matches the input qualifier THEN then reset is called on the child component and a selection is made ready for the next refresh', () => {
        const seDropdown = new SEDropdownService({
            field: fieldWithDependsOn,
            model,
            qualifier,
            id
        });

        // 2-way binding with child defined function
        seDropdown.reset = noop;
        spyOn(seDropdown, 'reset');

        seDropdown.init();

        const changeObject = {
            qualifier: 'dropdown1',
            optionObject: {}
        };

        (seDropdown as any)._respondToChange('SomeKey', changeObject);
        expect(seDropdown.reset).toHaveBeenCalled();
        expect((seDropdown as any).selection).toBe(changeObject.optionObject);
    });

    it('GIVEN SEDropdownService is initialized with a field object that has a propertyType attribute WHEN fetchAll is called THEN the respective populator is called with the correct payload', () => {
        const seDropdown = new SEDropdownService({
            field: fieldWithPropertyType,
            qualifier: undefined,
            model: undefined,
            id: undefined
        });
        seDropdown.init();
        expect((seDropdown as any).populator.type).toEqual('customPropertyTypeDropdownPopulator');
    });

    describe('fetchPage', () => {
        it('GIVEN SEDropdown is initialized WHEN fetchPage is called THEN it retrieves and returns the result with the right format', async () => {
            // GIVEN
            const seDropdown = new SEDropdownService({
                field: fieldWithUri,
                model,
                qualifier,
                id
            });

            // WHEN
            seDropdown.init();

            // THEN
            // callFake to check arguments immediately because "fetchPage"
            // modifies response object by removing the data key (for this case it is "someArray").
            getKeyHoldingDataFromResponse.and.callFake(
                (page: DropdownPopulatorFetchPageResponse) => {
                    expect(page).toEqual(
                        (mockFetchPage1Response as unknown) as DropdownPopulatorFetchPageResponse
                    );
                    return mockFetchPageResponseKey;
                }
            );
            const page1: DropdownPopulatorFetchPageResponse = await (seDropdown as any).fetchPage(
                '',
                10,
                0
            );
            const firstItem = page1.results[0];
            expect(firstItem.id).toEqual('site1');
        });

        it('GIVEN SEDropdown is initialized WHEN fetchPage is called THEN it should add the result items to the items array', async () => {
            // GIVEN
            const seDropdown = new SEDropdownService({
                field: fieldWithUri,
                model,
                qualifier,
                id
            });

            // WHEN
            seDropdown.init();

            // THEN
            const pageSize = 10;
            getKeyHoldingDataFromResponse.and.returnValue(mockFetchPageResponseKey);
            const page1: DropdownPopulatorFetchPageResponse = await (seDropdown as any).fetchPage(
                '',
                pageSize,
                0
            );
            const firstItem = page1.results[0];
            expect(firstItem.id).toEqual('site1');
            expect(page1.results.length).toEqual(10);

            const page2: DropdownPopulatorFetchPageResponse = await (seDropdown as any).fetchPage(
                '',
                pageSize,
                1
            );
            const page2FirstItem = page2.results[0];
            expect(page2FirstItem.id).toEqual('site11');
            expect(page2.results.length).toEqual(10);

            expect((seDropdown as any).items.length).toEqual(20);
        });
    });
});
