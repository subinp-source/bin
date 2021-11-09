/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { AbstractCachedRestService, CacheConfig, RestServiceFactory } from '@smart/utils';
import { rarelyChangingContent, userEvictionTag } from 'smarteditcommons/services/cache';
import { IBaseCatalogs } from 'smarteditcommons/dtos/ICatalog';
import { SeDowngradeService } from '../../../di';

@SeDowngradeService()
@CacheConfig({ actions: [rarelyChangingContent], tags: [userEvictionTag] })
export class ProductCatalogRestService extends AbstractCachedRestService<IBaseCatalogs> {
    constructor(restServiceFactory: RestServiceFactory) {
        super(restServiceFactory, '/cmssmarteditwebservices/v1/sites/:siteUID/productcatalogs');
    }
}
