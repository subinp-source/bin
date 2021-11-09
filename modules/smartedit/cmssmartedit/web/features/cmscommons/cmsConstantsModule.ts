/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { diNameUtils, SeModule } from 'smarteditcommons';
import {
    DEFAULT_SYNCHRONIZATION_EVENT,
    DEFAULT_SYNCHRONIZATION_POLLING,
    DEFAULT_SYNCHRONIZATION_STATUSES
} from 'cmscommons/components/synchronize/synchronizationConstants';

@SeModule({
    providers: [
        diNameUtils.makeValueProvider({ DEFAULT_SYNCHRONIZATION_STATUSES }),
        diNameUtils.makeValueProvider({ DEFAULT_SYNCHRONIZATION_POLLING }),
        diNameUtils.makeValueProvider({ DEFAULT_SYNCHRONIZATION_EVENT })
    ]
})
export class CmsConstantsModule {}
