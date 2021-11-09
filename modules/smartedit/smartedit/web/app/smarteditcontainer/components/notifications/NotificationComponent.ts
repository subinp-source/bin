/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { INotificationConfiguration } from 'smarteditcommons';

/**
 * This component renders an notification based on a one of the template type provided with configuration object.
 * See configuration in {@link smarteditServicesModule.interface:INotificationConfiguration INotificationConfiguration}.
 *
 * Here is an example of a notification object with a template URL:
 * {
 *     id: 'notification.identifier',
 *     templateUrl: 'notificationTemplateUrl.html'
 * }
 */
@Component({
    selector: 'se-notification',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './NotificationComponentTemplate.html'
})
export class NotificationComponent implements OnInit {
    @Input() notification: INotificationConfiguration;

    public id: string;

    ngOnInit() {
        this.id =
            this.notification && this.notification.id
                ? 'se-notification-' + this.notification.id
                : '';
    }

    public hasTemplate(): boolean {
        return this.notification.hasOwnProperty('template');
    }

    public hasTemplateUrl(): boolean {
        return this.notification.hasOwnProperty('templateUrl');
    }

    public hasComponent(): boolean {
        return !!this.notification.componentName;
    }
}
