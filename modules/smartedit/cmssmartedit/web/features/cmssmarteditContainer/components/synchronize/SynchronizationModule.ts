/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';

import { PageSynchronizationPanel } from './pages/PageSynchronizationPanel';
import { PageSynchronizationHeaderComponent } from './pages/PageSynchronizationHeaderComponent';

@SeModule({
    imports: ['translationServiceModule', 'functionsModule'],
    declarations: [PageSynchronizationPanel, PageSynchronizationHeaderComponent],
    providers: [
        {
            provide: 'PAGE_SYNC_STATUS_READY',
            useValue: 'PAGE_SYNC_STATUS_READY'
        }
    ]
})
export class SynchronizationModule {}
