/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:INotificationConfiguration
 *
 * @description
 * Interface for Notification Configuration
 */
export interface INotificationConfiguration {
    /**
     * @ngdoc property
     * @name id
     * @propertyOf smarteditServicesModule.interface:INotificationConfiguration
     * @description The notification's unique identifier
     */
    id: string;

    /**
     * @ngdoc property
     * @name template
     * @propertyOf smarteditServicesModule.interface:INotificationConfiguration
     * @description Either one of componentName, template or templateUrl must be present to display a notification.
     * @deprecated Deprecated since 2005. Use {@link smarteditServicesModule.interface:INotificationConfiguration#component component}
     */
    template?: string;

    /**
     * @ngdoc property
     * @name templateUrl
     * @propertyOf smarteditServicesModule.interface:INotificationConfiguration
     * @description Either one of componentName, template or templateUrl must be present to display a notification.
     * @deprecated Deprecated since 2005. Use {@link smarteditServicesModule.interface:INotificationConfiguration#component component}
     */
    templateUrl?: string;

    /**
     * @ngdoc property
     * @name componentName
     * @propertyOf smarteditServicesModule.interface:INotificationConfiguration
     * @description
     * Component class name, decorated with @SeCustomComponent.
     * Component must be also registered in @NgModule entryComponents array.
     *
     * Either one of componentName, template or templateUrl must be present to display a notification.
     *
     * @example
     * @SeCustomComponent()
     * @Component({
     *   selector: 'se-my-custom-component',
     *   templateUrl: './SeMyComponent.html'
     * })
     * export class MyCustomComponent {}
     *
     * componentName = 'MyCustomComponent'
     * or
     * componentName = MyCustomComponent.name
     */
    componentName?: string;
}

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:INotificationService
 *
 * @description
 * INotificationService provides a service to display visual cues to inform
 * the user of the state of the application in the container or the iFramed application.
 * The interface defines the methods required to manage notifications that are to be displayed to the user.
 */
export abstract class INotificationService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:INotificationService#pushNotification
     * @methodOf smarteditServicesModule.interface:INotificationService
     *
     * @description
     * This method creates a new notification based on the given configuration and
     * adds it to the top of the list.
     *
     * The configuration must contain either one of componentName, template or templateUrl.
     *
     * @param {Object} configuration The notification's configuration {@link smarteditServicesModule.interface:INotificationConfiguration INotificationConfiguration}
     *
     * @throws An error if no configuration is given.
     * @throws An error if the configuration does not contain a unique identifier.
     * @throws An error if the configuration's unique identifier is an empty string.
     * @throws An error if the configuration does not contain a componenName, template or templateUrl.
     * @throws An error if the configuration contains more than one template type.
     */
    pushNotification(configuration: INotificationConfiguration): PromiseLike<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:INotificationService#removeNotification
     * @methodOf smarteditServicesModule.interface:INotificationService
     *
     * @description
     * This method removes the notification with the given ID from the list.
     *
     * @param {String} notificationId The notification's unique identifier.
     */
    removeNotification(notificationId: string): PromiseLike<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:INotificationService#removeAllNotifications
     * @methodOf smarteditServicesModule.interface:INotificationService
     *
     * @description
     * This method removes all notifications.
     */
    removeAllNotifications(): PromiseLike<void> {
        'proxyFunction';
        return null;
    }
}
