/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    rarelyChangingContent,
    BrowserService,
    Cached,
    EVENT_SERVICE,
    IEventService,
    ILanguage,
    ILanguageServiceConstants,
    IRestService,
    IStorageService,
    LanguageService as SmartutilsLanguageService,
    LogService,
    LANGUAGE_SERVICE_CONSTANTS,
    PromiseUtils,
    RestServiceFactory
} from '@smart/utils';
import { SeDowngradeService } from '../../di';
import { TranslateService } from '@ngx-translate/core';
import { Inject, Injector } from '@angular/core';

/**
 * @ngdoc service
 * @name smarteditCommonsModule.service:LanguageService
 */

@SeDowngradeService()
export class LanguageService extends SmartutilsLanguageService {
    constructor(
        protected logService: LogService,
        protected translateService: TranslateService,
        protected promiseUtils: PromiseUtils,
        @Inject(EVENT_SERVICE) protected eventService: IEventService,
        protected browserService: BrowserService,
        protected storageService: IStorageService,
        protected injector: Injector,
        @Inject(LANGUAGE_SERVICE_CONSTANTS)
        protected languageServiceConstants: ILanguageServiceConstants
    ) {
        super(
            logService,
            translateService,
            promiseUtils,
            eventService,
            browserService,
            storageService,
            injector,
            languageServiceConstants
        );
    }
    /**
     * @ngdoc method
     * @name smarteditCommonsModule.service:LanguageService#getLanguagesForSite
     * @methodOf smarteditCommonsModule.service:LanguageService
     *
     * @description
     * Fetches a list of language descriptors for the specified storefront site UID.
     * The object containing the list of sites is fetched using REST calls to the cmswebservices languages API.
     *
     * @param {string} siteUID the site unique identifier.
     *
     * @returns {Promise<ILanguage[]>} A promise that resolves to an array of ILanguage.
     */
    @Cached({ actions: [rarelyChangingContent] })
    getLanguagesForSite(siteUID: string): Promise<ILanguage[]> {
        return this.languageRestService
            .get({
                siteUID
            })
            .then(
                (languagesList) => {
                    return languagesList!.languages;
                },
                (error: any) => {
                    this.logService.error(
                        'LanguageService.getLanguagesForSite() - Error loading languages'
                    );
                    return Promise.reject(error);
                }
            );
    }

    protected get languageRestService(): IRestService<{ languages: ILanguage[] }> {
        return this.injector
            .get<RestServiceFactory>(RestServiceFactory)
            .get<{ languages: ILanguage[] }>(this.languageServiceConstants.LANGUAGE_RESOURCE_URI);
    }
}
