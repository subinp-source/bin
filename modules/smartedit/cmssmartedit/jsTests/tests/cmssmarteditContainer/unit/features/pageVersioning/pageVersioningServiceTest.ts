/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { IRestService, IRestServiceFactory, Page } from 'smarteditcommons';
import {
    IPageVersion,
    PageVersioningService,
    PageVersionSearchPayload
} from 'cmssmarteditcontainer/services/pageVersioning/PageVersioningService';

import { promiseHelper, PromiseType } from 'testhelpers';

describe('test PageVersioningService', () => {
    const $q = promiseHelper.$q();
    // ---------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------
    const PAGE_CONTEXT_SITE_ID = 'somePage';

    // ---------------------------------------------------------------------------
    // Variables
    // ---------------------------------------------------------------------------
    let restServiceFactory: jasmine.SpyObj<IRestServiceFactory>;
    let pageVersionService: jasmine.SpyObj<IRestService<IPageVersion>>;
    let payload1: PageVersionSearchPayload;

    const pageVersion1: IPageVersion = null;
    const promise1 = promiseHelper.buildPromise<IPageVersion>(
        'promise1',
        PromiseType.RESOLVES,
        pageVersion1
    );
    const promise2 = promiseHelper.buildPromise<Page<IPageVersion>>(
        'promise2',
        PromiseType.RESOLVES,
        {} as Page<IPageVersion>
    );

    let pageVersioningService: PageVersioningService;

    // ---------------------------------------------------------------------------
    // SetUp
    // ---------------------------------------------------------------------------
    beforeEach(() => {
        payload1 = {
            pageUuid: 'somePageUuid',
            currentPage: 123
        };

        restServiceFactory = jasmine.createSpyObj<IRestServiceFactory>('restServiceFactory', [
            'get'
        ]);
        pageVersionService = jasmine.createSpyObj<IRestService<IPageVersion>>(
            'pageVersionService',
            ['get', 'page']
        );

        restServiceFactory.get.and.returnValue(pageVersionService);
        pageVersionService.get.and.returnValue(promise1);
        pageVersionService.page.and.returnValue(promise2);
        pageVersioningService = new PageVersioningService(
            $q,
            restServiceFactory,
            PAGE_CONTEXT_SITE_ID
        );
    });

    // ---------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------
    it('WHEN the service starts THEN it prepares the restService with the appropriate URL', () => {
        // THEN
        expect(restServiceFactory.get).toHaveBeenCalledWith(
            `/cmswebservices/v1/sites/${PAGE_CONTEXT_SITE_ID}/cmsitems/:pageUuid/versions`
        );
    });

    it('WHEN findPageVersions is called THEN it calls the service with the right parameters', () => {
        // WHEN
        const result = pageVersioningService.findPageVersions(payload1);

        // THEN
        expect(pageVersionService.page).toHaveBeenCalledWith(payload1);
        expect(result).toBe(promise2);
    });
});
