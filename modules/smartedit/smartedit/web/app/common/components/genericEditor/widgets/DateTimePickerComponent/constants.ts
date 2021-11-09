/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Tooltips } from 'eonasdan-bootstrap-datetimepicker';

import { DATE_CONSTANTS } from '../../../../utils';

export const DATE_PICKER_CONFIG = {
    format: DATE_CONSTANTS.MOMENT_FORMAT,
    keepOpen: true,
    minDate: 0,
    showClear: true,
    showClose: true,
    useCurrent: false,
    widgetPositioning: {
        horizontal: 'right',
        vertical: 'bottom'
    }
};

export const RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP_VALUE = {
    in: 'id',
    zh: 'zh-cn'
};
export const TOOLTIPS_MAP_VALUE: Tooltips = {
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
