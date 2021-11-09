/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    AbstractCachedRestService,
    CacheConfig,
    OperationContextRegistered,
    RestServiceFactory
} from '@smart/utils';
import {
    contentCatalogUpdateEvictionTag,
    pageEvictionTag,
    rarelyChangingContent,
    userEvictionTag
} from 'smarteditcommons/services/cache';

import { IBaseCatalogs } from 'smarteditcommons/dtos';
import { SeDowngradeService } from '../../../di';

const CONTENT_CATALOG_VERSION_DETAILS_RESOURCE_API =
    '/cmssmarteditwebservices/v1/sites/:siteUID/contentcatalogs';

@SeDowngradeService()
@CacheConfig({
    actions: [rarelyChangingContent],
    tags: [userEvictionTag, pageEvictionTag, contentCatalogUpdateEvictionTag]
})
@OperationContextRegistered(CONTENT_CATALOG_VERSION_DETAILS_RESOURCE_API, ['CMS', 'INTERACTIVE'])
export class ContentCatalogRestService extends AbstractCachedRestService<IBaseCatalogs> {
    constructor(restServiceFactory: RestServiceFactory) {
        super(restServiceFactory, CONTENT_CATALOG_VERSION_DETAILS_RESOURCE_API);
    }
}
