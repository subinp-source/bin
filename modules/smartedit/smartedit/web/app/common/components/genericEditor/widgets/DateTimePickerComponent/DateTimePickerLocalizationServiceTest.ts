/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from '@smart/utils';
import { TranslateService } from '@ngx-translate/core';
import { Tooltips } from 'eonasdan-bootstrap-datetimepicker';

import { DateTimePickerLocalizationService } from './DateTimePickerLocalizationService';
import { LanguageService } from '../../../../services';
import { TOOLTIPS_MAP_VALUE } from './constants';

describe('dateTimePickerLocalizationService', () => {
    let dateTimePickerLocalizationService: DateTimePickerLocalizationService;
    let languageService: jasmine.SpyObj<LanguageService>;
    let resolvedLocaleToMomentLocaleMap: TypedMap<string>;
    let translate: jasmine.SpyObj<TranslateService>;
    let datetimepicker: any;
    let momentLocale: string;
    let otherLocale: string;
    let untranslatedTooltips: Tooltips;
    let translatedTooltips: Tooltips;

    beforeEach(() => {
        languageService = jasmine.createSpyObj('languageService', ['getResolveLocale']);
        languageService.getResolveLocale.and.returnValue(Promise.resolve('en'));

        momentLocale = 'zz';
        otherLocale = 'aa';
        resolvedLocaleToMomentLocaleMap = {
            en: momentLocale
        };

        translate = jasmine.createSpyObj('translate', ['instant']);
        translate.instant.and.callFake(function(_string: string) {
            return '_' + _string;
        });

        datetimepicker = jasmine.createSpyObj('datetimepicker', ['locale', 'tooltips']);

        untranslatedTooltips = {
            today: 'se.datetimepicker.today',
            clear: 'se.datetimepicker.clear',
            close: 'se.datetimepicker.close',
            selectMonth: 'se.datetimepicker.selectmonth',
            prevMonth: 'se.datetimepicker.previousmonth',
            nextMonth: 'se.datetimepicker.nextmonth',
            selectYear: 'se.datetimepicker.selectyear',
            prevYear: 'se.datetimepicker.prevyear',
            nextYear: 'se.datetimepicker.nextyear',
            selectDecade: 'se.datetimepicker.selectdecade',
            prevDecade: 'se.datetimepicker.prevdecade',
            nextDecade: 'se.datetimepicker.nextdecade',
            prevCentury: 'se.datetimepicker.prevcentury',
            nextCentury: 'se.datetimepicker.nextcentury',
            pickHour: 'se.datetimepicker.pickhour',
            incrementHour: 'se.datetimepicker.incrementhour',
            decrementHour: 'se.datetimepicker.decrementhour',
            pickMinute: 'se.datetimepicker.pickminute',
            incrementMinute: 'se.datetimepicker.incrementminute',
            decrementMinute: 'se.datetimepicker.decrementminute',
            pickSecond: 'se.datetimepicker.picksecond',
            incrementSecond: 'se.datetimepicker.incrementsecond',
            decrementSecond: 'se.datetimepicker.decrementsecond',
            togglePeriod: 'se.datetimepicker.toggleperiod',
            selectTime: 'se.datetimepicker.selecttime'
        };

        translatedTooltips = {
            today: '_se.datetimepicker.today',
            clear: '_se.datetimepicker.clear',
            close: '_se.datetimepicker.close',
            selectMonth: '_se.datetimepicker.selectmonth',
            prevMonth: '_se.datetimepicker.previousmonth',
            nextMonth: '_se.datetimepicker.nextmonth',
            selectYear: '_se.datetimepicker.selectyear',
            prevYear: '_se.datetimepicker.prevyear',
            nextYear: '_se.datetimepicker.nextyear',
            selectDecade: '_se.datetimepicker.selectdecade',
            prevDecade: '_se.datetimepicker.prevdecade',
            nextDecade: '_se.datetimepicker.nextdecade',
            prevCentury: '_se.datetimepicker.prevcentury',
            nextCentury: '_se.datetimepicker.nextcentury',
            pickHour: '_se.datetimepicker.pickhour',
            incrementHour: '_se.datetimepicker.incrementhour',
            decrementHour: '_se.datetimepicker.decrementhour',
            pickMinute: '_se.datetimepicker.pickminute',
            incrementMinute: '_se.datetimepicker.incrementminute',
            decrementMinute: '_se.datetimepicker.decrementminute',
            pickSecond: '_se.datetimepicker.picksecond',
            incrementSecond: '_se.datetimepicker.incrementsecond',
            decrementSecond: '_se.datetimepicker.decrementsecond',
            togglePeriod: '_se.datetimepicker.toggleperiod',
            selectTime: '_se.datetimepicker.selecttime'
        };

        dateTimePickerLocalizationService = new DateTimePickerLocalizationService(
            translate,
            resolvedLocaleToMomentLocaleMap,
            TOOLTIPS_MAP_VALUE,
            languageService
        );
    });

    describe('localizeDateTimePicker', () => {
        it('should not localize the tool nor tooltips when both are already localized', async (done) => {
            datetimepicker.locale.and.callFake(localeReturnSame);
            datetimepicker.tooltips.and.callFake(tooltipsReturnSame);

            await dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).not.toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).not.toHaveBeenCalledWith(translatedTooltips);

            done();
        });

        it('should localize only the tool not the tooltips when tooltips are already localized but the tool itself is not', async (done) => {
            datetimepicker.locale.and.callFake(localeReturnDifferent);
            datetimepicker.tooltips.and.callFake(tooltipsReturnSame);

            await dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.locale.calls.count()).toEqual(2);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).not.toHaveBeenCalledWith(translatedTooltips);

            done();
        });

        it('should localize only the tooltips nor the tool when the tool is already localized but not the tooltips', async (done) => {
            datetimepicker.locale.and.callFake(localeReturnSame);
            datetimepicker.tooltips.and.callFake(tooltipsReturnDifferent);

            await dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).not.toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).toHaveBeenCalledWith(translatedTooltips);
            expect(datetimepicker.tooltips.calls.count()).toEqual(2);

            done();
        });

        it('should localize both the tool and tooltips when they are not already localized', async (done) => {
            datetimepicker.locale.and.callFake(localeReturnDifferent);
            datetimepicker.tooltips.and.callFake(tooltipsReturnDifferent);

            await dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.locale.calls.count()).toEqual(2);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).toHaveBeenCalledWith(translatedTooltips);
            expect(datetimepicker.tooltips.calls.count()).toEqual(2);

            done();
        });
    });

    const localeReturnSame = (locale: string): string => {
        if (locale) {
            return undefined;
        } else {
            return momentLocale;
        }
    };

    const localeReturnDifferent = (locale: string): string => {
        if (locale) {
            return undefined;
        } else {
            return otherLocale;
        }
    };

    const tooltipsReturnSame = (tooltips: Tooltips): Tooltips => {
        if (tooltips) {
            return undefined;
        } else {
            return translatedTooltips;
        }
    };

    const tooltipsReturnDifferent = (tooltips: Tooltips): Tooltips => {
        if (tooltips) {
            return undefined;
        } else {
            return untranslatedTooltips;
        }
    };
});
