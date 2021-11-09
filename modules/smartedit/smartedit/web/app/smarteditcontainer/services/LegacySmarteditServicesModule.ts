/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    diNameUtils,
    CATALOG_VERSION_PERMISSIONS_RESOURCE_URI_CONSTANT,
    DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME,
    LegacySmarteditCommonsModule,
    ModalServiceModule,
    SeModule
} from 'smarteditcommons';

import { YAnnouncementBoardComponent, YAnnouncementComponent } from 'smarteditcontainer/components';
import { FilterByFieldFilter } from './filters/FilterByFieldFilter';

/**
 * @ngdoc overview
 * @name smarteditServicesModule
 *
 * @description
 * Module containing all the services shared within the smartedit container application
 */
@SeModule({
    declarations: [FilterByFieldFilter, YAnnouncementBoardComponent, YAnnouncementComponent],
    imports: [
        'seConstantsModule',
        LegacySmarteditCommonsModule,
        'ngCookies',
        'functionsModule',
        'resourceLocationsModule',
        'yLoDashModule',
        ModalServiceModule
    ],
    providers: [
        /**
         * @ngdoc object
         * @name smarteditServicesModule.object:CATALOG_VERSION_PERMISSIONS_RESOURCE_URI
         *
         * @description
         * Path to fetch permissions of a given catalog version.
         */

        diNameUtils.makeValueProvider({
            CATALOG_VERSION_PERMISSIONS_RESOURCE_URI: CATALOG_VERSION_PERMISSIONS_RESOURCE_URI_CONSTANT
        }),
        diNameUtils.makeValueProvider({
            DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME
        })
    ]
})
export class LegacySmarteditServicesModule {}
