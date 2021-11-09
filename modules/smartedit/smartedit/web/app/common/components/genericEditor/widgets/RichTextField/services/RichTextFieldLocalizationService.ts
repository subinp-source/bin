/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import { TypedMap } from '@smart/utils';

import { SeDowngradeService } from 'smarteditcommons/di';
import { LanguageService } from '../../../../../services/language/LanguageService';
import { RESOLVED_LOCALE_TO_CKEDITOR_LOCALE_MAP } from '../tokens';

@SeDowngradeService()
export class RichTextFieldLocalizationService {
    constructor(
        private languageService: LanguageService,
        @Inject(RESOLVED_LOCALE_TO_CKEDITOR_LOCALE_MAP)
        private resolvedLocaleToCKEDITORLocaleMap: TypedMap<string>
    ) {}

    localizeCKEditor(): Promise<void> {
        return this.languageService.getResolveLocale().then((locale) => {
            CKEDITOR.config.language = this.convertResolvedToCKEditorLocale(locale);
        });
    }

    private convertResolvedToCKEditorLocale(resolvedLocale: string): string {
        const conversion = this.resolvedLocaleToCKEDITORLocaleMap[resolvedLocale];
        if (conversion) {
            return conversion;
        }
        return resolvedLocale;
    }
}
