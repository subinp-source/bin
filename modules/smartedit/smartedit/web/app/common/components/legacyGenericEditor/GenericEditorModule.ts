/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule, SeValueProvider } from 'smarteditcommons/di';

import { GenericEditorComponent } from './GenericEditorComponent';
import { SeDropdownModule } from './components/dropdown/SeDropdownModule';
import { DropdownPopulatorModule } from './populators/DropdownPopulatorModule';

export const GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT_CONSTANT: SeValueProvider = {
    provide: 'GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT',
    useValue: 'UnrelatedValidationMessagesEvent'
};

/**
 * @ngdoc object
 * @name genericEditorModule.object:GENERIC_EDITOR_LOADED_EVENT
 * @description
 * Event to notify subscribers that GenericEditor is loaded.
 */
export const GENERIC_EDITOR_LOADED_EVENT_CONSTANT: SeValueProvider = {
    provide: 'GENERIC_EDITOR_LOADED_EVENT',
    useValue: 'genericEditorLoadedEvent'
};

/**
 * @ngdoc overview
 * @name legacyGenericEditorModule
 */

@SeModule({
    imports: [
        'smarteditServicesModule',
        'functionsModule',
        'coretemplates',
        'translationServiceModule',
        'seConstantsModule',
        'resourceLocationsModule',
        'ui.bootstrap',
        SeDropdownModule,
        DropdownPopulatorModule
    ],
    providers: [
        GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT_CONSTANT,
        GENERIC_EDITOR_LOADED_EVENT_CONSTANT
    ],
    declarations: [GenericEditorComponent]
})
export class GenericEditorModule {}
