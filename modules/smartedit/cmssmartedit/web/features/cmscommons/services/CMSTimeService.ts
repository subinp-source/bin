/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as moment from 'moment';
import { SeInjectable } from 'smarteditcommons';

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:CMSTimeService
 *
 * @description
 * Service for time management functionality.
 */

@SeInjectable()
export class CMSTimeService {
    constructor(private $translate: angular.translate.ITranslateService) {}

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:CMSTimeService#getTimeAgo
     * @methodOf cmsSmarteditServicesModule.service:CMSTimeService
     *
     * @description
     * Give a time difference in milliseconds, this method returns a string that determites time in ago.
     *
     * Examples:
     * If the diff is less then 24 hours, the result is in hours eg: 17 hour(s) ago.
     * If the diff is more than a day, the result is in days, eg: 2 day(s) ago.
     *
     * @param {number} timeDiff The time difference in milli seconds.
     * @returns {String} the assets root folder
     */
    getTimeAgo(timeDiff: number): string {
        const timeAgoInDays: number = Math.floor(moment.duration(timeDiff).asDays());
        const timeAgoInHours: number = Math.floor(moment.duration(timeDiff).asHours());

        if (timeAgoInDays >= 1) {
            return (
                timeAgoInDays +
                ' ' +
                this.$translate.instant('se.cms.actionitem.page.workflow.action.started.days.ago')
            );
        } else if (timeAgoInHours >= 1) {
            return (
                timeAgoInHours +
                ' ' +
                this.$translate.instant('se.cms.actionitem.page.workflow.action.started.hours.ago')
            );
        }
        return (
            '<1 ' +
            this.$translate.instant('se.cms.actionitem.page.workflow.action.started.hours.ago')
        );
    }
}
