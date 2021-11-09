/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';

import { ClonePageWizardService } from './clonePageWizardService';
import { ClonePageBuilderFactory } from './clonePageBuilderFactory';

/**
 * @ngdoc overview
 * @name clonePageWizardServiceModule
 * @description
 * # The clonePageWizardServiceModule
 *
 * The clone page service module provides the functionality necessary to enable the cloning of pages through a modal wizard.
 *
 * Use the {@link clonePageWizardServiceModule.service:clonePageWizardService clonePageWizardService} to open the add page wizard modal.
 *
 */

@SeModule({
    imports: [
        'newPageDisplayConditionModule',
        'yLoDashModule',
        'smarteditServicesModule',
        'selectPageTypeModule',
        'selectPageTemplateModule',
        'contextAwarePageStructureServiceModule',
        'confirmationModalServiceModule',
        'resourceLocationsModule',
        'typeStructureRestServiceModule',
        'componentCloneOptionFormModule',
        'componentCloneInfoFormModule',
        'restrictionsStepHandlerFactoryModule',
        'genericEditorModule',
        'restrictionsServiceModule',
        'pageRestrictionsInfoMessageModule',
        'selectTargetCatalogVersionModule',
        'clonePageAlertServiceModule',
        'pageFacadeModule',
        'functionsModule',
        'cmsSmarteditServicesModule'
    ],
    providers: [
        ClonePageWizardService,
        { provide: 'ClonePageBuilderFactory', useFactory: ClonePageBuilderFactory }
    ]
})
export class ClonePageWizardServiceModule {}
