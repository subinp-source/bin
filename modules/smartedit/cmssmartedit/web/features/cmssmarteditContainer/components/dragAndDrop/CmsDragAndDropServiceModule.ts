/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { CmsDragAndDropService } from './CmsDragAndDropServiceOuter';

@SeModule({
    imports: [],
    providers: [
        CmsDragAndDropService,
        {
            provide: 'DRAG_AND_DROP_EVENTS',
            useValue: {
                DRAG_STARTED: 'CMS_DRAG_STARTED',
                DRAG_STOPPED: 'CMS_DRAG_STOPPED'
            }
        },
        {
            provide: 'ENABLE_CLONE_ON_DROP',
            useValue: 'enableCloneComponentOnDrop'
        }
    ]
})
export class CmsDragAndDropServiceModule {}
