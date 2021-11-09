/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from '@smart/utils';
import { TranslateService } from '@ngx-translate/core';
import { Inject } from '@angular/core';
import { Datetimepicker, Tooltips } from 'eonasdan-bootstrap-datetimepicker';

import { SeDowngradeService } from 'smarteditcommons/di';
import { LanguageService } from '../../../../services/language/LanguageService';
import { RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP, TOOLTIPS_MAP } from './tokens';

/**
 * The DateTimePickerLocalizationService is responsible for both localizing the date time picker as well as the tooltips
 */
@SeDowngradeService()
export class DateTimePickerLocalizationService {
    constructor(
        private translate: TranslateService,
        @Inject(RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP)
        private resolvedLocaleToMomentLocaleMap: TypedMap<string>,
        @Inject(TOOLTIPS_MAP) private tooltipsMap: Tooltips,
        private languageService: LanguageService
    ) {}

    async localizeDateTimePicker(datetimepicker: Datetimepicker): Promise<void> {
        await this.localizeDateTimePickerUI(datetimepicker);
        this.localizeDateTimePickerTooltips(datetimepicker);
    }

    private convertResolvedToMomentLocale(resolvedLocale: string): string {
        const conversion = this.resolvedLocaleToMomentLocaleMap[resolvedLocale];
        if (conversion) {
            return conversion;
        } else {
            return resolvedLocale;
        }
    }

    private getLocalizedTooltips(): TypedMap<string> {
        const localizedTooltips: TypedMap<string> = {};

        for (const index in this.tooltipsMap) {
            if (this.tooltipsMap.hasOwnProperty(index)) {
                localizedTooltips[index] = this.translate.instant(
                    this.tooltipsMap[index as keyof Tooltips]
                );
            }
        }

        return localizedTooltips;
    }

    private compareTooltips(tooltips1: Tooltips, tooltips2: Tooltips): boolean {
        for (const index in this.tooltipsMap) {
            if (tooltips1[index as keyof Tooltips] !== tooltips2[index as keyof Tooltips]) {
                return false;
            }
        }
        return true;
    }

    private localizeDateTimePickerUI(datetimepicker: Datetimepicker): Promise<void> {
        return this.languageService.getResolveLocale().then((language: string) => {
            const momentLocale = this.convertResolvedToMomentLocale(language);

            // This if statement was added to prevent infinite recursion, at the moment it triggers twice
            // due to what seems like datetimepicker.locale(<string>) broadcasting dp.show
            if (datetimepicker.locale() !== momentLocale) {
                datetimepicker.locale(momentLocale);
            }
        });
    }

    private localizeDateTimePickerTooltips(datetimepicker: Datetimepicker): void {
        const currentTooltips = datetimepicker.tooltips();
        const translatedTooltips = this.getLocalizedTooltips();

        // This if statement was added to prevent infinite recursion, at the moment it triggers twice
        // due to what seems like datetimepicker.tooltips(<tooltips obj>) broadcasting dp.show
        if (!this.compareTooltips(currentTooltips, translatedTooltips)) {
            datetimepicker.tooltips(translatedTooltips);
        }
    }
}
