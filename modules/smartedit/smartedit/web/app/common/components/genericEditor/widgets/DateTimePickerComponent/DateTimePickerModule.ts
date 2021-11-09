/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { DateFormatterDirective } from './DateFormatterDirective';
import { DateTimePickerLocalizationService } from './DateTimePickerLocalizationService';
import { DateTimePickerComponent } from './DateTimePickerComponent';
import { RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP, TOOLTIPS_MAP } from './tokens';
import { RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP_VALUE, TOOLTIPS_MAP_VALUE } from './constants';

/**
 * The date time picker service module is a module used for displaying a date time picker
 *
 * Use the se-date-time-picker to open the date time picker.
 *
 * Once the se-date-time-picker is opened, its DateTimePickerLocalizationService is used to localize the tooling.
 */
@NgModule({
    imports: [CommonModule, FormsModule, TranslateModule.forChild()],
    providers: [
        {
            provide: RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP,
            useValue: RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP_VALUE
        },
        {
            provide: TOOLTIPS_MAP,
            useValue: TOOLTIPS_MAP_VALUE
        },
        DateTimePickerLocalizationService
    ],
    declarations: [DateFormatterDirective, DateTimePickerComponent],
    entryComponents: [DateTimePickerComponent],
    exports: [DateFormatterDirective, DateTimePickerComponent]
})
export class DateTimePickerModule {}
