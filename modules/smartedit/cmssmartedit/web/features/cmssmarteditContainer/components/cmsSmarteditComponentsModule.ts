/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { PageComponentsModule } from './pages';
import { GenericEditorWidgetsModule } from './genericEditor';
import { PageVersionsModule } from './versioning/pageVersionsMenuModule';
import { CmsComponentsModule } from './cmsComponents';
import { NavigationModule } from './navigation/NavigationModule';
import { WorkflowModule } from './workflow/WorkflowModule';
import { SynchronizationModule } from './synchronize/SynchronizationModule';

/**
 * @ngdoc overview
 * @name cmsSmarteditComponentsModule
 *
 * @description
 * Module containing all the components defined within the CmsSmartEdit container.
 */
@SeModule({
    imports: [
        PageVersionsModule,
        PageComponentsModule,
        GenericEditorWidgetsModule,
        CmsComponentsModule,
        NavigationModule,
        WorkflowModule,
        SynchronizationModule
    ]
})
export class CmsSmarteditComponentsModule {}
