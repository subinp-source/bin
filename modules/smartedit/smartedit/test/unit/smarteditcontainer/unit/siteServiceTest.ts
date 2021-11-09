/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import * as lo from 'lodash';
import {
    annotationService,
    authorizationEvictionTag,
    rarelyChangingContent,
    Cached,
    IRestService,
    OperationContextRegistered,
    RestServiceFactory,
    SITES_RESOURCE_URI
} from 'smarteditcommons';
import { SiteService } from 'smarteditcontainer/services';
import { promiseHelper, PromiseType } from 'testhelpers';

describe('siteService', () => {
    let siteService: SiteService;
    const restServiceFactory: jasmine.SpyObj<RestServiceFactory> = jasmine.createSpyObj<
        RestServiceFactory
    >('restServiceFactory', ['get']);
    const siteRestService: jasmine.SpyObj<IRestService<any>> = jasmine.createSpyObj<
        IRestService<any>
    >('siteRestService', ['get']);
    const sitesForCatalogsRestService: jasmine.SpyObj<IRestService<any>> = jasmine.createSpyObj<
        IRestService<any>
    >('sitesForCatalogsRestService', ['save']);

    const sitesDTO = {
        sites: [
            {
                contentCatalogs: [
                    'electronicsContentCatalog',
                    'electronics-euContentCatalog',
                    'electronics-ukContentCatalog'
                ],
                name: {
                    en: 'Electronics Site'
                },
                previewUrl: '/yacceleratorstorefront?site=electronics-uk',
                uid: 'electronics-uk'
            }
        ]
    };

    const sitesDTOByCatalogs = {
        sites: [
            {
                contentCatalogs: ['electronicsContentCatalog'],
                name: {
                    en: 'Electronics Site'
                },
                previewUrl: '/yacceleratorstorefront?site=electronics',
                uid: 'electronics'
            },
            {
                contentCatalogs: [
                    'electronicsContentCatalog',
                    'electronics-euContentCatalog',
                    'electronics-ukContentCatalog'
                ],
                name: {
                    en: 'Electronics Site'
                },
                previewUrl: '/yacceleratorstorefront?site=electronics-uk',
                uid: 'electronics-uk'
            },
            {
                contentCatalogs: ['electronicsContentCatalog', 'electronics-euContentCatalog'],
                name: {
                    en: 'Electronics Site'
                },
                previewUrl: '/yacceleratorstorefront?site=electronics-eu',
                uid: 'electronics-eu'
            }
        ]
    };

    const sitesDTOPromise = promiseHelper.buildPromise<any>(
        'sitesDTOPromise',
        PromiseType.RESOLVES,
        sitesDTO
    );
    const sitesDTOByCatalogsPromise = promiseHelper.buildPromise<any>(
        'sitesDTOByCatalogsPromise',
        PromiseType.RESOLVES,
        sitesDTOByCatalogs
    );

    beforeEach(() => {
        siteRestService.get.calls.reset();
        sitesForCatalogsRestService.save.calls.reset();

        siteRestService.get.and.callFake((arg: any) => {
            if (lo.isEmpty(arg)) {
                return sitesDTOPromise;
            }
            throw new Error('unexpected argument for siteRestService.get method: ' + arg);
        });
        sitesForCatalogsRestService.save.and.callFake((arg: any) => {
            if (
                arg &&
                arg.catalogIds &&
                lo.isEqual(arg.catalogIds, [
                    'electronicsContentCatalog',
                    'electronics-euContentCatalog',
                    'electronics-ukContentCatalog'
                ])
            ) {
                return sitesDTOByCatalogsPromise;
            }
            throw new Error(
                'unexpected argument for sitesForCatalogsRestService.save method: ' + arg
            );
        });

        restServiceFactory.get.and.callFake((uri: string, identifier: string) => {
            if (uri.indexOf('sites/catalogs') !== -1) {
                return sitesForCatalogsRestService;
            }
            return siteRestService;
        });

        siteService = new SiteService(restServiceFactory);
    });

    it('is initialized', () => {
        expect(restServiceFactory.get).toHaveBeenCalledWith(SITES_RESOURCE_URI);

        const decoratorObj = annotationService.getClassAnnotation(
            SiteService,
            OperationContextRegistered as (args?: any) => ClassDecorator
        );
        expect(decoratorObj).toEqual(['SITES_RESOURCE_URI', 'CMS']);
    });

    it('is calling getAccessibleSites method', async (done) => {
        const result = await siteService.getAccessibleSites();
        expect(result).toEqual(sitesDTO.sites);
        expect(siteRestService.get).toHaveBeenCalledWith({});

        done();
    });

    it('is calling getSites method', async (done) => {
        const result = await siteService.getSites();
        expect(result).toEqual(sitesDTOByCatalogs.sites);
        expect(siteRestService.get).toHaveBeenCalledWith({});
        expect(sitesForCatalogsRestService.save).toHaveBeenCalledWith({
            catalogIds: [
                'electronicsContentCatalog',
                'electronics-euContentCatalog',
                'electronics-ukContentCatalog'
            ]
        });
        expect(siteRestService.get.calls.count()).toEqual(1);
        expect(sitesForCatalogsRestService.save.calls.count()).toEqual(1);

        done();
    });

    it('checks Cached annotation on getSites() method ', () => {
        const decoratorObj = annotationService.getMethodAnnotation(SiteService, 'getSites', Cached);
        expect(decoratorObj).toEqual(
            jasmine.objectContaining([
                {
                    actions: [rarelyChangingContent],
                    tags: [authorizationEvictionTag]
                }
            ])
        );
    });

    it('is calling getSiteById method', async (done) => {
        const uid = 'electronics';
        siteRestService.get.calls.reset();
        const result = await siteService.getSiteById(uid);
        expect(result).toEqual(sitesDTOByCatalogs.sites.find((site) => site.uid === uid));
        expect(siteRestService.get).toHaveBeenCalled();
        expect(siteRestService.get.calls.count()).toEqual(1);
        expect(sitesForCatalogsRestService.save.calls.count()).toEqual(1);

        done();
    });
});
