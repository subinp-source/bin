/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { NotificationService } from 'smartedit/services';

import { annotationService, GatewayProxied } from 'smarteditcommons';

describe('notificationService', () => {
    let notificationService: NotificationService;
    /*
     * This setup method creates a mock for the gateway proxy. It is used to validate
     * that the notification service initializes itself properly to be proxied
     * across the two applications.
     */

    beforeEach(() => {
        notificationService = new NotificationService();
    });

    describe('initialization', () => {
        it('extends the INotificationService', () => {
            expect(notificationService.pushNotification).toBeEmptyFunction();
            expect(notificationService.removeNotification).toBeEmptyFunction();
            expect(notificationService.removeAllNotifications).toBeEmptyFunction();
        });

        it('invokes the gatway proxy with the proper parameter values', () => {
            const decoratorObj = annotationService.getClassAnnotation(
                NotificationService,
                GatewayProxied
            );
            expect(decoratorObj).toEqual([
                'pushNotification',
                'removeNotification',
                'removeAllNotifications'
            ]);
        });
    });
});
