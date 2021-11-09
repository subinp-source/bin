/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { TranslateService } from '@ngx-translate/core';

import {
    DropdownPopulatorPayload,
    IRestServiceFactory,
    LanguageService,
    UriDropdownPopulator
} from 'smarteditcommons';
import { promiseHelper } from 'testhelpers';

describe('uriDropdownPopulator', function() {
    let uriDropdownPopulator: UriDropdownPopulator;
    const translateService = jasmine.createSpyObj<TranslateService>('translateService', [
        'instant'
    ]);
    translateService.instant.and.callFake((key: string) => key);
    const restServiceFactory: jasmine.SpyObj<IRestServiceFactory> = jasmine.createSpyObj<
        IRestServiceFactory
    >('restServiceFactory', ['get']);
    const restServiceForOptions: jasmine.SpyObj<IRestServiceFactory> = jasmine.createSpyObj<
        IRestServiceFactory
    >('restServiceFactory', ['get']);
    const $q = promiseHelper.$q();

    const languageService = jasmine.createSpyObj<LanguageService>('languageService', [
        'getResolveLocale'
    ]);
    const getKeyHoldingDataFromResponse: any = jasmine.createSpy('getKeyHoldingDataFromResponse');
    let payload: DropdownPopulatorPayload;
    let options: any[];

    beforeEach(function() {
        languageService.getResolveLocale.and.returnValue($q.when('en'));

        payload = {
            field: {
                cmsStructureType: 'EditableDropdown',
                qualifier: 'dropdownA',
                i18nKey: 'theKey',
                idAttribute: 'uid',
                labelAttributes: ['label1', 'label2'],
                uri: '/someuri'
            },
            model: {
                dropdown1: '1',
                dropdown2: '2'
            },
            selection: null,
            search: null
        };

        options = [
            {
                id: '1',
                label: 'opt1-yes',
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '2',
                label: 'opt2-no',
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '3',
                label: 'opt3-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '4',
                label1: 'opt4-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '5',
                label2: 'opt5-yes',
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                uid: '6',
                label: 'opt6-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            }
        ];

        restServiceForOptions.get.and.callFake(function(params: any) {
            const filteredOptions = params
                ? options.filter(function(option: any) {
                      return (
                          option.dropdown1 === params.dropdown1 &&
                          option.dropdown2 === params.dropdown2
                      );
                  })
                : options;

            return $q.when({
                options: filteredOptions
            });
        });

        restServiceFactory.get.and.returnValue(restServiceForOptions);

        getKeyHoldingDataFromResponse.and.returnValue('someArray');

        const getDataFromResponse = (response: any) => {
            return response[
                Object.keys(response).filter(function(key) {
                    return response[key] instanceof Array;
                })[0]
            ];
        };

        uriDropdownPopulator = new UriDropdownPopulator(
            lo,
            restServiceFactory,
            getDataFromResponse,
            getKeyHoldingDataFromResponse,
            languageService,
            translateService
        );
    });

    it('GIVEN uri populator is called WHEN I call populate method without a dependsOn attribute THEN should return a promise by making a REST call to the uri in the fields attribute and return a list of options', function() {
        const promise = uriDropdownPopulator.populate(payload);

        expect(restServiceFactory.get).toHaveBeenCalledWith('/someuri');
        expect(restServiceForOptions.get).toHaveBeenCalled();

        expect(promise).toBeResolvedWithData(options);
    });

    it('GIVEN uri populator is called WHEN I call populate method with a dependsOn attribute THEN should return a promise by making a REST call to the uri in the fields attribute with the right params and return a list of options', function() {
        payload.field.dependsOn = 'dropdown1,dropdown2';
        const promise = uriDropdownPopulator.populate(payload);

        expect(restServiceFactory.get).toHaveBeenCalledWith('/someuri');
        expect(restServiceForOptions.get).toHaveBeenCalledWith({
            dropdown1: '1',
            dropdown2: '2'
        });

        expect(promise).toBeResolvedWithData([
            {
                id: '3',
                label: 'opt3-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '4',
                label: 'opt4-yes-no',
                label1: 'opt4-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '6',
                uid: '6',
                label: 'opt6-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            }
        ]);
    });

    it('GIVEN uri populator is called WHEN I call populate method with a search attribute THEN should return a promise by making a REST call to the uri in the fields attribute and return a list of options filtered based on the search string', function() {
        delete payload.field.dependsOn;
        payload.search = 'yes';
        const promise = uriDropdownPopulator.populate(payload);

        expect(restServiceFactory.get).toHaveBeenCalledWith('/someuri');
        expect(restServiceForOptions.get).toHaveBeenCalled();

        expect(promise).toBeResolvedWithData([
            {
                id: '1',
                label: 'opt1-yes',
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '3',
                label: 'opt3-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '4',
                label: 'opt4-yes-no',
                label1: 'opt4-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '5',
                label: 'opt5-yes',
                label2: 'opt5-yes',
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '6',
                uid: '6',
                label: 'opt6-yes-no',
                dropdown1: '1',
                dropdown2: '2'
            }
        ]);
    });

    it('GIVEN uri populator is called WHEN I call populate method with a search attribute THEN should return a promise by making a REST call to the uri in the fields attribute and return a list of localized options filtered based on the search string and language', function() {
        options = [
            {
                id: '1',
                label: {
                    en: 'opt1-yes-en',
                    fr: 'opt1-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '2',
                label: {
                    en: 'opt2-no-en',
                    fr: 'opt2-no-fr'
                },
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '3',
                label: {
                    en: 'opt3-yes-en',
                    fr: 'opt3-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '4',
                label1: {
                    en: 'opt4-yes-en',
                    fr: 'opt4-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '5',
                label2: {
                    en: 'opt5-yes-en',
                    fr: 'opt5-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                uid: '6',
                label: {
                    en: 'opt6-yes-no-en',
                    fr: 'opt6-yes-no-fr'
                },
                dropdown1: '1',
                dropdown2: '2'
            }
        ];

        delete payload.field.dependsOn;
        payload.search = 'yes-en';
        const promise = uriDropdownPopulator.populate(payload);

        expect(restServiceFactory.get).toHaveBeenCalledWith('/someuri');
        expect(restServiceForOptions.get).toHaveBeenCalled();

        expect(promise).toBeResolvedWithData([
            {
                id: '1',
                label: {
                    en: 'opt1-yes-en',
                    fr: 'opt1-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '1'
            },
            {
                id: '3',
                label: {
                    en: 'opt3-yes-en',
                    fr: 'opt3-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '2'
            },
            {
                id: '4',
                label1: {
                    en: 'opt4-yes-en',
                    fr: 'opt4-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '2',
                label: {
                    en: 'opt4-yes-en',
                    fr: 'opt4-yes-fr'
                }
            },
            {
                id: '5',
                label2: {
                    en: 'opt5-yes-en',
                    fr: 'opt5-yes-fr'
                },
                dropdown1: '1',
                dropdown2: '1',
                label: {
                    en: 'opt5-yes-en',
                    fr: 'opt5-yes-fr'
                }
            }
        ]);
    });
});
