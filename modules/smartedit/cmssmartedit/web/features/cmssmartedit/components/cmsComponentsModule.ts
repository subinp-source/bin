/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { SynchronizationPanelModule } from 'cmscommons';

import { SlotSynchronizationPanel } from './synchronize/slots/SlotSynchronizationPanel';
import { CmsSmarteditServicesModule } from '../services';
import { SharedComponentButton } from './sharedComponent/sharedComponentButton';

@SeModule({
    imports: [
        'slotSynchronizationServiceModule',
        'pageContentSlotsServiceModule',
        CmsSmarteditServicesModule,
        SynchronizationPanelModule
    ],
    declarations: [SlotSynchronizationPanel, SharedComponentButton]
})
export class CmsComponentsModule {}
