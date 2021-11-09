/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { clone } from 'lodash';

import {
    stringUtils,
    GatewayProxied,
    IAnnouncementConfig,
    IAnnouncementService,
    LogService,
    SeDowngradeService
} from 'smarteditcommons';

export interface IAnnouncement extends IAnnouncementConfig {
    timer?: number;
    id: string;
}

export const ANNOUNCEMENT_DEFAULTS = {
    timeout: 5000,
    closeable: true
};

@SeDowngradeService(IAnnouncementService)
@GatewayProxied('showAnnouncement', 'closeAnnouncement')
@Injectable()
export class AnnouncementService extends IAnnouncementService {
    private announcements$ = new BehaviorSubject<IAnnouncement[]>([]);

    constructor(private logService: LogService) {
        super();
    }

    public showAnnouncement(announcementConfig: IAnnouncementConfig): Promise<string> {
        this.validateAnnouncementConfig(announcementConfig);

        const announcement: IAnnouncement = clone<IAnnouncementConfig>(
            announcementConfig
        ) as IAnnouncement;
        announcement.id = stringUtils.encode(announcementConfig);

        announcement.timeout =
            !!announcement.timeout && announcement.timeout > 0
                ? announcement.timeout
                : ANNOUNCEMENT_DEFAULTS.timeout;
        if (announcement.timeout > 0) {
            announcement.timer = (setTimeout(() => {
                this._closeAnnouncement(announcement);
            }, announcement.timeout) as unknown) as number;
        }
        this.announcements$.next([...this.announcements$.getValue(), announcement]);

        return Promise.resolve(announcement.id);
    }

    public getAnnouncements(): Observable<IAnnouncement[]> {
        return this.announcements$.asObservable();
    }

    public closeAnnouncement(announcementId: string): Promise<void> {
        const announcement = this.announcements$
            .getValue()
            .find((announcementObj) => announcementObj.id === announcementId);
        if (announcement) {
            this._closeAnnouncement(announcement);
        }

        return Promise.resolve();
    }

    private _closeAnnouncement(announcement: IAnnouncement): void {
        if (announcement.timer) {
            clearTimeout(announcement.timer);
        }

        const announcements = this.announcements$.getValue();
        const newAnnouncements = announcements.filter(
            (announcementObj) => announcementObj.id !== announcement.id
        );
        this.announcements$.next(newAnnouncements);
    }

    /**
     * Validates a given announcement data.
     * An announcement must contain only one of either message, template, or templateUrl property.
     */
    private validateAnnouncementConfig(announcementConfig: IAnnouncementConfig): void {
        const { message, template, templateUrl, component } = announcementConfig;

        if (!message && !template && !templateUrl && !component) {
            this.logService.warn(
                'AnnouncementService._validateAnnouncementConfig - announcement must contain at least a message, template, templateUrl or component property',
                announcementConfig
            );
        }

        if (
            (message && (template || templateUrl || component)) ||
            (template && (message || templateUrl || component)) ||
            (templateUrl && (message || template || component)) ||
            (component && (message || template || templateUrl))
        ) {
            throw new Error(
                'AnnouncementService._validateAnnouncementConfig - only one template type is allowed for an announcement: message, template, templateUrl or component'
            );
        }
    }
}
