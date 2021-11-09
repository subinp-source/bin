/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { diNameUtils, LegacySmarteditCommonsModule, SeModule } from 'smarteditcommons';

import {
    DEFAULT_CONTRACT_CHANGE_LISTENER_INTERSECTION_OBSERVER_OPTIONS,
    DEFAULT_CONTRACT_CHANGE_LISTENER_PROCESS_QUEUE_THROTTLE,
    DEFAULT_PROCESS_QUEUE_POLYFILL_INTERVAL,
    DEFAULT_REPROCESS_TIMEOUT
} from './SmartEditContractChangeListener';

/**
 * @ngdoc overview
 * @name smarteditServicesModule
 *
 * @description
 * Module containing all the services shared within the smartedit application
 */
@SeModule({
    imports: [
        'coretemplates',
        'seConstantsModule',
        'functionsModule',
        'yLoDashModule',
        LegacySmarteditCommonsModule
    ],
    providers: [
        diNameUtils.makeValueProvider({ DEFAULT_REPROCESS_TIMEOUT }),
        diNameUtils.makeValueProvider({ DEFAULT_PROCESS_QUEUE_POLYFILL_INTERVAL }),
        diNameUtils.makeValueProvider({
            DEFAULT_CONTRACT_CHANGE_LISTENER_INTERSECTION_OBSERVER_OPTIONS
        }),
        diNameUtils.makeValueProvider({ DEFAULT_CONTRACT_CHANGE_LISTENER_PROCESS_QUEUE_THROTTLE })
    ]
})
export class LegacySmarteditServicesModule {}
