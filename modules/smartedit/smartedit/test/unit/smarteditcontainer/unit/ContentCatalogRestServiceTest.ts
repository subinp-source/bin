/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    annotationService,
    contentCatalogUpdateEvictionTag,
    pageEvictionTag,
    rarelyChangingContent,
    userEvictionTag,
    CacheConfig,
    ContentCatalogRestService,
    IBaseCatalogs,
    IRestService,
    OperationContextRegistered,
    RestServiceFactory
} from 'smarteditcommons';

describe('ContentCatalogRestService', () => {
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let innerRestService: jasmine.SpyObj<IRestService<IBaseCatalogs>>;

    beforeEach(() => {
        innerRestService = jasmine.createSpyObj<IRestService<IBaseCatalogs>>('innerRestService', [
            'activateMetadata'
        ]);
        restServiceFactory = jasmine.createSpyObj<any>('restServiceFactory', ['get']);
        restServiceFactory.get.and.returnValue(innerRestService);

        new ContentCatalogRestService(restServiceFactory);
    });

    it('service has registered operation contexts for retry policies through OperationContextRegistered class annotation', () => {
        const decoratorObj = annotationService.getClassAnnotation(
            ContentCatalogRestService,
            OperationContextRegistered as (args?: any) => ClassDecorator
        );
        expect(decoratorObj).toEqual([
            '/cmssmarteditwebservices/v1/sites/:siteUID/contentcatalogs',
            ['CMS', 'INTERACTIVE']
        ]);
    });

    it('CacheConfig annotation', () => {
        const decoratorObj = annotationService.getClassAnnotation(
            ContentCatalogRestService,
            CacheConfig
        );
        expect(decoratorObj).toEqual([
            {
                actions: [rarelyChangingContent],
                tags: [userEvictionTag, pageEvictionTag, contentCatalogUpdateEvictionTag]
            }
        ]);
    });
});
