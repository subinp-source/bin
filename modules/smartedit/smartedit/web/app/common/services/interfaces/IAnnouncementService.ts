/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken, Type } from '@angular/core';
import { TypedMap } from '@smart/utils';

export type AnnouncementData<T = TypedMap<any>> = T & {
    id: string;
};

export const ANNOUNCEMENT_DATA = new InjectionToken<AnnouncementData>('ANNOUNCEMENT_DATA');

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IAnnouncementConfig
 *
 * @description
 * Interface for Announcement configuration
 */
export interface IAnnouncementConfig {
    /**
     * @ngdoc property
     * @name message
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The announcement's message to display.
     * Either one of message, template or templateUrl must be present to display an announcement.
     */
    message?: string;

    /**
     * @ngdoc property
     * @name message
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description Optional title for the announcement's message.
     * This is only used when message is available.
     */
    messageTitle?: string;

    /**
     * @ngdoc property
     * @name template
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The announcement's HTML template to display.
     * Either one of message, template, templateUrl or component must be present to display an announcement.
     * @deprecated since 2005
     */
    template?: string;

    /**
     * @ngdoc property
     * @name component
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The announcement's component to display.
     * Either one of message, template, templateUrl or component must be present to display an announcement.
     */
    component?: Type<any>;

    /**
     * Data to pass along to the component through the ANNOUNCEMENT_DATA token.
     */
    data?: AnnouncementData;

    /**
     * @ngdoc property
     * @name templateUrl
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The announcement's template url to display.
     * Either one of message, template, templateUrl or component must be present to display an announcement.
     * @deprecated since 2005
     */
    templateUrl?: string;

    /**
     * @ngdoc property
     * @name controller
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The custom controller that defines the given template or templateUrl.
     * The content of the controller in template or templateUrl can be accessed using '$announcementCtrl' alias.
     * @deprecated since 2005
     */
    controller?: angular.IControllerConstructor;

    /**
     * @ngdoc property
     * @name closeable
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The optional boolean that determines if a close button must be displayed or not.
     * The default is true.
     */
    closeable?: boolean;

    /**
     * @ngdoc property
     * @name timeout
     * @propertyOf smarteditServicesModule.interface:IAnnouncementConfig
     * @description The optional timeout in milliseconds that determines when to close the announcement.
     * The default is 5000.
     */
    timeout?: number;
}

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IAnnouncementService
 *
 * @description
 * Interface for Announcement service
 */
export abstract class IAnnouncementService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IAnnouncementService#showAnnouncement
     * @methodOf smarteditServicesModule.interface:IAnnouncementService
     *
     * @description
     * This method creates a new announcement and displays it.
     *
     * The configuration must contain either a description, template or template URL, but not multiple.
     *
     * @param {IAnnouncementConfig} announcementConfig The announcement configuration defined by {@link smarteditServicesModule.interface:IAnnouncementConfig IAnnouncementConfig}
     * @returns {PromiseLike<string>} returns a promise with announcement id
     */
    showAnnouncement(announcementConfig: IAnnouncementConfig): PromiseLike<string> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IAnnouncementService#closeAnnouncement
     * @methodOf smarteditServicesModule.interface:IAnnouncementService
     *
     * @description
     * This method is used to close the announcement by given announcement id
     * @param {string} announcementId the announcement id
     * @returns {PromiseLike<void>} An empty promise
     */
    closeAnnouncement(announcementId: string): PromiseLike<void> {
        'proxyFunction';
        return null;
    }
}
