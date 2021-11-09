/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FunctionsModule } from 'smarteditcommons/utils';
import { SeModule } from 'smarteditcommons/di';
import { DropdownPopulatorInterface } from './DropdownPopulatorInterface';
import { OptionsDropdownPopulator } from './OptionsDropdownPopulator';
import { UriDropdownPopulator } from './UriDropdownPopulator';

/**
 * @ngdoc overview
 * @name dropdownPopulatorModule
 */
@SeModule({
    imports: ['yLoDashModule', 'smarteditServicesModule', FunctionsModule],
    providers: [
        {
            provide: 'DropdownPopulatorInterface',
            useFactory: () => DropdownPopulatorInterface
        },
        OptionsDropdownPopulator,
        UriDropdownPopulator
    ]
})
export class DropdownPopulatorModule {}
