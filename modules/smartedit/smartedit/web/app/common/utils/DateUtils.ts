/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as moment from 'moment';

import { DATE_CONSTANTS } from './smarteditconstants';

/**
 * @ngdoc service
 * @name functionsModule.service:DateUtils
 *
 * @description
 * provides a list of useful methods used for date manipulation
 */

export class DateUtils {
    /**
     * @ngdoc method
     * @name functionsModule.service:DateUtils#formatDateAsUtc
     * @methodOf functionsModule.service:DateUtils
     *
     * @description
     * Formats provided dateTime as utc.
     *
     * @param {Object|String} dateTime DateTime to format in utc.
     *
     * @return {String} formatted string.
     */

    formatDateAsUtc(dateTime: any) {
        return moment(dateTime)
            .utc()
            .format(DATE_CONSTANTS.MOMENT_ISO);
    }
}

export const dateUtils = new DateUtils();
