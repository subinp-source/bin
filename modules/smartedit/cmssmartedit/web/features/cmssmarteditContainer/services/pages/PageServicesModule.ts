/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { PageService } from './PageServiceOuter';
import { PageRestoreModalService } from './pageRestore/PageRestoreModalService';

@SeModule({
    imports: [
        'pagesRestServiceModule',
        'pagesFallbacksRestServiceModule',
        'pagesVariationsRestServiceModule',
        'cmsSmarteditServicesModule'
    ],
    providers: [PageService, PageRestoreModalService]
})
export class PageServicesModule {}
