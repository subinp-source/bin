/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, IPageInfoService, SeDowngradeService } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IPageInfoService)
@GatewayProxied('getPageUID', 'getPageUUID', 'getCatalogVersionUUIDFromPage')
export class PageInfoService extends IPageInfoService {
    constructor() {
        super();
    }
}
