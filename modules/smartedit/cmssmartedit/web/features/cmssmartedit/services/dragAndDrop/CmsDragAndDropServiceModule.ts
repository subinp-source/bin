/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { CmsDragAndDropService } from './CmsDragAndDropServiceInner';

/**
 * @ngdoc overview
 * @name cmsDragAndDropServiceModule
 * @description
 * # The cmsDragAndDropServiceModule
 *
 * The cmsDragAndDropServiceModule contains a service that provides a rich drag and drop experience tailored for CMS operations.
 *
 */
@SeModule({
    imports: [
        'yLoDashModule',
        'legacySmarteditCommonsModule',
        'slotRestrictionsServiceModule',
        'componentEditingFacadeModule',
        'translationServiceModule',
        'removeComponentServiceModule',
        'cmsSmarteditServicesModule',
        'browserServiceModule',
        'seConstantsModule'
    ],
    providers: [
        CmsDragAndDropService,
        /**
         * @ngdoc object
         * @name cmsDragAndDropServiceModule.object:DRAG_AND_DROP_EVENTS
         *
         * @description
         * Injectable angular constant<br/>
         * Constants identifying CMS drag and drop events.
         *
         */
        {
            provide: 'DRAG_AND_DROP_EVENTS',
            useValue: {
                /**
                 * @ngdoc property
                 * @name DRAG_STARTED
                 * @propertyOf cmsDragAndDropServiceModule.object:DRAG_AND_DROP_EVENTS
                 *
                 * @description
                 * Name of event executed when a drag and drop event starts.
                 */
                DRAG_STARTED: 'CMS_DRAG_STARTED',
                /**
                 * @ngdoc property
                 * @name DRAG_STOPPED
                 * @propertyOf cmsDragAndDropServiceModule.object:DRAG_AND_DROP_EVENTS
                 *
                 * @description
                 * Name of event executed when a drag and drop event stops.
                 */
                DRAG_STOPPED: 'CMS_DRAG_STOPPED',
                /**
                 * @ngdoc property
                 * @name DRAG_STOPPED
                 * @propertyOf cmsDragAndDropServiceModule.object:DRAG_AND_DROP_EVENTS
                 *
                 * @description
                 * Name of event executed when onDragOver is triggered.
                 */
                DRAG_OVER: 'CMS_DRAG_OVER',
                /**
                 * @ngdoc property
                 * @name DRAG_STOPPED
                 * @propertyOf cmsDragAndDropServiceModule.object:DRAG_AND_DROP_EVENTS
                 *
                 * @description
                 * Name of event executed when onDragLeave is triggered.
                 */
                DRAG_LEAVE: 'CMS_DRAG_LEAVE'
            }
        },
        {
            provide: 'ENABLE_CLONE_ON_DROP',
            useValue: 'enableCloneComponentOnDrop'
        }
    ]
})
export class CmsDragAndDropServiceModule {}
