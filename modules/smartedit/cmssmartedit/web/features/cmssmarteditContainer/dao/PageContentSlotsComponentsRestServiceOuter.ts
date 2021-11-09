/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied } from 'smarteditcommons';
import { IPageContentSlotsComponentsRestService } from 'cmscommons/dao/cmswebservices/IPageContentSlotsComponentsRestService';

@GatewayProxied('clearCache', 'getSlotsToComponentsMapForPageUid')
export class PageContentSlotsComponentsRestService extends IPageContentSlotsComponentsRestService {
    constructor() {
        super();
    }
}
