/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    GatewayProxied,
    IPageInfoService,
    LogService,
    SeDowngradeService,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { Inject } from '@angular/core';

/** @internal */
@SeDowngradeService(IPageInfoService)
@GatewayProxied('getPageUID', 'getPageUUID', 'getCatalogVersionUUIDFromPage')
export class PageInfoService extends IPageInfoService {
    public static PATTERN_SMARTEDIT_CATALOG_VERSION_UUID: RegExp = /smartedit-catalog-version-uuid\-(\S+)/;

    private static PATTERN_SMARTEDIT_PAGE_UID: RegExp = /smartedit-page-uid\-(\S+)/;

    private static PATTERN_SMARTEDIT_PAGE_UUID: RegExp = /smartedit-page-uuid\-(\S+)/;

    /* @internal */
    constructor(
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private logService: LogService
    ) {
        super();
    }

    /**
     * When the time comes to deprecate these 3 functions from componentHandlerService in the inner app, we will need
     * to migrate their implementations to here.
     */

    getPageUID(): Promise<string> {
        return this.try(() =>
            this.getBodyClassAttributeByRegEx(PageInfoService.PATTERN_SMARTEDIT_PAGE_UID)
        );
    }

    getPageUUID(): Promise<string> {
        return this.try(() =>
            this.getBodyClassAttributeByRegEx(PageInfoService.PATTERN_SMARTEDIT_PAGE_UUID)
        );
    }

    getCatalogVersionUUIDFromPage(): Promise<string> {
        return this.try(() =>
            this.getBodyClassAttributeByRegEx(
                PageInfoService.PATTERN_SMARTEDIT_CATALOG_VERSION_UUID
            )
        );
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:ComponentHandlerService#getBodyClassAttributeByRegEx
     * @methodOf smarteditServicesModule.service:ComponentHandlerService
     *
     * @param {RegExp} pattern Pattern of class names to search for
     *
     * @return {String} Class attributes from the body element of the storefront
     */
    getBodyClassAttributeByRegEx(pattern: RegExp): string {
        try {
            const bodyClass: string = (this.yjQuery('body') as JQuery).attr('class');
            return pattern.exec(bodyClass)[1];
        } catch {
            throw {
                name: 'InvalidStorefrontPageError',
                message: 'Error: the page is not a valid storefront page.'
            };
        }
    }

    /** @internal */
    try(func: () => string): Promise<string> {
        try {
            return Promise.resolve(func());
        } catch (e) {
            this.logService.warn(
                'Missing SmartEdit attributes on body element of the storefront - SmartEdit will resume once the attributes are added'
            );
            return Promise.reject(e);
        }
    }
}
