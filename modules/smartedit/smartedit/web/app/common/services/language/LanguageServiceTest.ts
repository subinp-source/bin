/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { LanguageService } from './LanguageService';
import {
    promiseUtils,
    BrowserService,
    IEventService,
    ILanguage,
    ILanguageServiceConstants,
    IRestService,
    IStorageService,
    LogService,
    RestServiceFactory
} from '@smart/utils';
import { TranslateService } from '@ngx-translate/core';
import { Injector } from '@angular/core';
import { coreAnnotationsHelper } from 'testhelpers';

describe('Language Service - Site Languages', () => {
    let languageService: LanguageService;

    const SITE_UID = 'apparel-de';

    const MOCK_LANGUAGES: any = {
        languages: [
            {
                nativeName: 'English',
                isocode: 'en',
                name: 'English',
                active: true,
                required: true
            },
            {
                nativeName: 'Deutsch',
                isocode: 'de',
                name: 'German',
                active: true,
                required: false
            }
        ]
    };

    const logService: jasmine.SpyObj<LogService> = jasmine.createSpyObj('log', [
        'error',
        'debug',
        'warn'
    ]);

    const translateService: jasmine.SpyObj<TranslateService> = jasmine.createSpyObj<
        TranslateService
    >('translateService', ['use', 'get']);

    let eventService: jasmine.SpyObj<IEventService>;
    let browserService: jasmine.SpyObj<BrowserService>;
    let injector: jasmine.SpyObj<Injector>;
    const constants: ILanguageServiceConstants = {
        I18N_LANGUAGES_RESOURCE_URI: 'I18N_LANGUAGES_RESOURCE_URI',
        LANGUAGE_RESOURCE_URI: 'LANGUAGE_RESOURCE_URI'
    };
    const storageService: jasmine.SpyObj<IStorageService> = jasmine.createSpyObj('storageService', [
        'getValueFromLocalStorage',
        'setValueInLocalStorage'
    ]);
    const restServiceFactory: jasmine.SpyObj<RestServiceFactory> = jasmine.createSpyObj<
        RestServiceFactory
    >('restServiceFactory', ['get']);
    const languageRestService: jasmine.SpyObj<
        IRestService<{
            languages: ILanguage[];
        }>
    > = jasmine.createSpyObj<IRestService<{ languages: ILanguage[] }>>('languageRestService', [
        'get'
    ]);

    beforeEach(() => {
        eventService = jasmine.createSpyObj('eventService', ['publish', 'subscribe']);
        browserService = jasmine.createSpyObj('browserService', ['getBrowserLocale']);

        injector = jasmine.createSpyObj('injector', ['get']);

        restServiceFactory.get.and.returnValue(languageRestService);
        injector.get.and.returnValue(restServiceFactory);

        coreAnnotationsHelper.init();

        languageService = new LanguageService(
            logService,
            translateService,
            promiseUtils,
            eventService,
            browserService,
            storageService,
            injector,
            constants
        );
    });

    it('GIVEN languages REST call fails WHEN I request all languages for a given site THEN I will receive a rejected promise', (done) => {
        languageRestService.get.and.returnValue(Promise.reject());

        const promise = languageService.getLanguagesForSite(SITE_UID) as Promise<ILanguage[]>;

        promise.catch((value) => {
            expect(value).toBeFalsy();
            done();
        });
    });

    it('GIVEN languages REST call succeeds WHEN I request all languages for a given site THEN I will receive a promise that resolves to the list of language objects', (done) => {
        languageRestService.get.and.returnValue(Promise.resolve(MOCK_LANGUAGES));

        const promise = languageService.getLanguagesForSite(SITE_UID) as Promise<ILanguage[]>;

        promise.then((value) => {
            expect(value).toEqual(MOCK_LANGUAGES.languages);
            done();
        });
    });
});
