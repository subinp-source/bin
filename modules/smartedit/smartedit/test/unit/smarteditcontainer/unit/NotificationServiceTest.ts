/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { NotificationService } from 'smarteditcontainer/services';
import {
    annotationService,
    EVENT_NOTIFICATION_CHANGED,
    GatewayProxied,
    INotificationConfiguration,
    LogService,
    SystemEventService
} from 'smarteditcommons';

describe('notificationService', () => {
    const DUMMY_ID = 'dummy.id';
    const DUMMY_ID1 = DUMMY_ID + '1';
    const DUMMY_ID2 = DUMMY_ID + '2';
    const DUMMY_ID3 = DUMMY_ID + '3';
    const DUMMY_INVALID_ID = 'dummy.invalid.id';

    const DUMMY_TEMPLATE = '<div>this is a dummy template</div>';
    const DUMMY_TEMPLATE_URL = 'dummyTemplateUrl.html';

    const DUMMY_CONFIGURATION: INotificationConfiguration = {
        id: DUMMY_ID,
        template: DUMMY_TEMPLATE
    };

    const systemEventService: jasmine.SpyObj<SystemEventService> = jasmine.createSpyObj(
        'systemEventService',
        ['publishAsync']
    );
    const logService = jasmine.createSpyObj<LogService>('logService', ['debug']);
    let notificationService: NotificationService;

    beforeEach(() => {
        systemEventService.publishAsync.and.returnValue(null);
        notificationService = new NotificationService(systemEventService, logService);
    });

    describe('initialization', () => {
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

    describe('destruction', () => {
        it('GIVEN a service was initialized WHEN destroying an instance THEN it should unsubscribe', () => {
            const notificationsUnsubscribeSpy = spyOn(
                (notificationService as any).notifications,
                'unsubscribe'
            );
            const notificationChangeActionUnsubscribeSpy = spyOn(
                (notificationService as any).notificationsChangeAction,
                'unsubscribe'
            );

            notificationService.ngOnDestroy();

            expect(notificationsUnsubscribeSpy).toHaveBeenCalled();
            expect(notificationChangeActionUnsubscribeSpy).toHaveBeenCalled();
        });
    });

    describe('pushNotification', () => {
        it('throws an error if no configuration is given', () => {
            expect(function() {
                notificationService.pushNotification(null);
            }).toThrowError('notificationService.pushNotification: Configuration is required');
        });

        it('throws an error if the configuration contains a unique identifier that is undefined or null or empty string', () => {
            expect(function() {
                notificationService.pushNotification({
                    id: '',
                    template: DUMMY_TEMPLATE
                });
            }).toThrowError(
                'notificationService.pushNotification: Notification ID cannot be undefined or null or empty'
            );
        });

        it('throws an error if the configuration does not contain a template or template URL', () => {
            expect(function() {
                notificationService.pushNotification({
                    id: DUMMY_ID
                });
            }).toThrowError(
                'notificationService.pushNotification: Configuration must contain a componentName, template, templateUrl'
            );
        });

        it('throws an error if the configuration contains both a template and a template URL', () => {
            expect(function() {
                notificationService.pushNotification({
                    id: DUMMY_ID,
                    template: DUMMY_TEMPLATE,
                    templateUrl: DUMMY_TEMPLATE_URL
                });
            }).toThrowError(
                'notificationService.pushNotification: Only one template type is allowed for Configuration: componentName, template, templateUrl'
            );
        });

        it('does not add a new notification when given configuration contains a unique identifier that already exists', () => {
            // Given
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            notificationService.pushNotification(DUMMY_CONFIGURATION);

            notificationService
                .getNotifications()
                .subscribe((notifications) => expect(notifications.length).toEqual(1));
        });

        it('creates an notification with the proper ID and template', () => {
            // Given
            const configuration = {
                id: DUMMY_ID,
                template: DUMMY_TEMPLATE
            };

            // When
            notificationService.pushNotification(configuration);

            // Then
            expect(notificationService.getNotification(DUMMY_ID)).toEqual(
                jasmine.objectContaining(configuration)
            );
        });

        it('creates an notification with the proper ID and template URL', () => {
            // Given
            const configuration = {
                id: DUMMY_ID,
                templateUrl: DUMMY_TEMPLATE_URL
            };

            // When
            notificationService.pushNotification(configuration);

            // Then
            expect(notificationService.getNotification(DUMMY_ID)).toEqual(
                jasmine.objectContaining(configuration)
            );
        });

        it('sends an "EVENT_NOTIFICATION_CHANGED" event when an notification is added to the list', () => {
            // When
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // Then
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(
                EVENT_NOTIFICATION_CHANGED
            );
        });
    });

    describe('removeNotification', () => {
        it('removes the notification with the given ID from the list', () => {
            // Given
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // When
            notificationService.removeNotification(DUMMY_ID);

            // Then
            expect(notificationService.getNotification(DUMMY_ID)).toBeFalsy();
        });

        it('does not remove notification when no notification with the given ID exists', () => {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            });

            // When
            notificationService.removeNotification(DUMMY_INVALID_ID);

            // Then
            notificationService
                .getNotifications()
                .subscribe((notifications) => expect(notifications.length).toEqual(2));
        });

        it('sends an "EVENT_NOTIFICATION_CHANGED" event when an notification is removed from the list', () => {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID,
                template: DUMMY_TEMPLATE
            });

            systemEventService.publishAsync.calls.reset();

            // When
            notificationService.removeNotification(DUMMY_ID);

            // Then
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(
                EVENT_NOTIFICATION_CHANGED
            );
        });
    });

    describe('removeAllNotifications', () => {
        it('removes all the notificationes from the list', () => {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID3,
                template: DUMMY_TEMPLATE
            });

            // When
            notificationService.removeAllNotifications();

            // Then
            expect(notificationService.getNotification(DUMMY_ID1)).toBeFalsy();
            expect(notificationService.getNotification(DUMMY_ID2)).toBeFalsy();
            expect(notificationService.getNotification(DUMMY_ID3)).toBeFalsy();
        });

        it('sends an "EVENT_NOTIFICATION_CHANGED" event when the notificationes are removed from the list', () => {
            // Given
            notificationService.pushNotification({
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            });

            notificationService.pushNotification({
                id: DUMMY_ID3,
                template: DUMMY_TEMPLATE
            });

            systemEventService.publishAsync.calls.reset();

            // When
            notificationService.removeAllNotifications();

            // Then
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(
                EVENT_NOTIFICATION_CHANGED
            );
        });
    });

    describe('getNotification', () => {
        it('returns the notification with the given ID', () => {
            // Given
            notificationService.pushNotification(DUMMY_CONFIGURATION);

            // When
            const result = notificationService.getNotification(DUMMY_ID);

            // Then
            expect(result).toEqual(jasmine.objectContaining(DUMMY_CONFIGURATION));
        });
    });

    describe('getNotifications', () => {
        it('returns the list of notificationes', () => {
            // Given
            const configuration1 = {
                id: DUMMY_ID1,
                template: DUMMY_TEMPLATE
            };

            const configuration2 = {
                id: DUMMY_ID2,
                template: DUMMY_TEMPLATE
            };

            const configuration3 = {
                id: DUMMY_ID3,
                template: DUMMY_TEMPLATE
            };

            notificationService.pushNotification(configuration1);
            notificationService.pushNotification(configuration2);
            notificationService.pushNotification(configuration3);

            // When
            const notifications$ = notificationService.getNotifications();

            // Then
            notifications$.subscribe((notifications) => {
                expect(notifications.length).toBe(3);
                expect(notifications[0]).toEqual(jasmine.objectContaining(configuration1));
                expect(notifications[1]).toEqual(jasmine.objectContaining(configuration2));
                expect(notifications[2]).toEqual(jasmine.objectContaining(configuration3));
            });
        });
    });
});
