/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    DropdownPopulatorInterface,
    DropdownPopulatorPayload,
    GenericEditorOption,
    LanguageService,
    SeDowngradeService
} from 'smarteditcommons';

/**
 * Implementation of smarteditcommons' DropdownPopulatorInterface for language dropdown in
 * experience selector to populate the list of languages by making a REST call to retrieve the list of langauges for a given site.
 *
 */
@SeDowngradeService()
export class PreviewDatalanguageDropdownPopulator extends DropdownPopulatorInterface {
    constructor(languageService: LanguageService) {
        super(lo, languageService);
    }

    /**
     * Returns a promise resolving to a list of languages for a given Site ID (based on the selected catalog). The site Id is generated from the
     * selected catalog in the 'catalog' dropdown.
     */

    public populate(payload: DropdownPopulatorPayload): Promise<GenericEditorOption[]> {
        if (payload.model[payload.field.dependsOn]) {
            const siteUid = (payload.model[payload.field.dependsOn] as string).split('|')[0];
            return this.getLanguageDropdownChoices(siteUid, payload.search);
        }
        return Promise.resolve([]);
    }

    /** @internal */
    private async getLanguageDropdownChoices(
        siteUid: string,
        search: string
    ): Promise<GenericEditorOption[]> {
        try {
            const languages = await this.languageService.getLanguagesForSite(siteUid);
            let languagesDropdownChoices = languages.map(({ isocode, nativeName }) => {
                const dropdownChoices = {} as GenericEditorOption;
                dropdownChoices.id = isocode;
                dropdownChoices.label = nativeName;
                return dropdownChoices;
            });

            if (search) {
                languagesDropdownChoices = languagesDropdownChoices.filter(
                    (language) =>
                        (language.label as string).toUpperCase().indexOf(search.toUpperCase()) > -1
                );
            }
            return languagesDropdownChoices;
        } catch (e) {
            throw new Error(e);
        }
    }
}
