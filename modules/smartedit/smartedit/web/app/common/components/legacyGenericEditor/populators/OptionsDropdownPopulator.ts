/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { TranslateService } from '@ngx-translate/core';

import { LanguageService } from '../../../services/language/LanguageService';
import { SeInjectable } from 'smarteditcommons/di';
import { DropdownPopulatorInterface } from './DropdownPopulatorInterface';
import { DropdownPopulatorPayload } from '..';
import { GenericEditorOption } from '../../genericEditor/types';

/**
 * @ngdoc service
 * @name dropdownPopulatorModule.service:optionsDropdownPopulator
 * @description
 * implementation of {@link dropdownPopulatorModule.DropdownPopulatorInterface DropdownPopulatorInterface} for "EditableDropdown" cmsStructureType
 * containing options attribute.
 */
@SeInjectable()
export class OptionsDropdownPopulator extends DropdownPopulatorInterface {
    constructor(
        lodash: lo.LoDashStatic,
        public languageService: LanguageService,
        public translateService: TranslateService
    ) {
        super(lodash, languageService, translateService);
    }
    /**
     * @ngdoc method
     * @name dropdownPopulatorModule.service:optionsDropdownPopulator#populate
     * @methodOf dropdownPopulatorModule.service:optionsDropdownPopulator
     *
     * @description
     * Implementation of the {@link dropdownPopulatorModule.DropdownPopulatorInterface#populate DropdownPopulatorInterface.populate} method
     */
    populate(payload: DropdownPopulatorPayload): Promise<GenericEditorOption[]> {
        const options = this.populateAttributes(
            payload.field.options as GenericEditorOption[],
            payload.field.idAttribute,
            payload.field.labelAttributes
        );

        if (payload.search) {
            return this.search(options, payload.search);
        }

        return Promise.resolve(options);
    }
}
