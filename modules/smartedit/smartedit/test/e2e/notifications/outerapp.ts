/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, NgModule, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as angular from 'angular';

import {
    IExperienceService,
    INotificationConfiguration,
    INotificationService,
    SeDowngradeComponent,
    SeEntryModule
} from 'smarteditcommons';

@Component({
    selector: 'notification-mock',
    template: `
        <div>This is a test component based notification</div>
    `
})
export class MockNotificationComponent {}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div style="position: relative; top: 135px; left: 0; background-color: white">
            <div class="se-notification-tester">
                <h2>Notification Tester</h2>
                <div class="row">
                    <div class="col-xs-6">
                        <form>
                            <div class="form-group">
                                <h5>ID</h5>
                                <input
                                    id="test-notification-id"
                                    class="form-control"
                                    type="text"
                                    name="id"
                                    [(ngModel)]="configuration.id"
                                />
                            </div>
                            <div class="form-group">
                                <h5>Template</h5>
                                <input
                                    id="test-notification-template"
                                    class="form-control"
                                    type="text"
                                    name="template"
                                    [(ngModel)]="configuration.template"
                                />
                            </div>
                            <div class="form-group">
                                <h5>Template URL</h5>
                                <input
                                    id="test-notification-template-url"
                                    class="form-control"
                                    type="text"
                                    name="templateUrl"
                                    [(ngModel)]="configuration.templateUrl"
                                />
                            </div>
                        </form>
                    </div>
                </div>
                <button
                    id="test-notification-push-button"
                    class="btn btn-primary"
                    (click)="pushNotification()"
                >
                    Push
                </button>
                <button
                    id="test-notification-remove-button"
                    class="btn btn-secondary"
                    (click)="removeNotification()"
                >
                    Remove
                </button>
                <button
                    id="test-notification-remove-all-button"
                    class="btn btn-secondary"
                    (click)="removeAllNotifications()"
                >
                    Remove All
                </button>
                <button
                    id="test-notification-reset-button"
                    class="btn btn-secondary"
                    (click)="reset()"
                >
                    Reset
                </button>
            </div>

            <button
                id="test-notification-goto-storefront"
                class="btn btn-secondary"
                (click)="goToStorefront()"
            >
                Go to Storefront
            </button>

            <button
                id="test-notification-component-button"
                class="btn btn-secondary"
                (click)="showComponentBasedNotification()"
            >
                Show Component Based Notification
            </button>

            <div class="se-notification-clickthrough">
                Clickthrough Tester
                <input type="checkbox" id="test-notification-clickthrough-checkbox" />
            </div>
        </div>
    `,
    styles: [
        `
            .se-notification-tester {
                padding: 8pt;
            }

            .se-notification-clickthrough {
                position: fixed;
                bottom: 89px;
                right: 89px;
                z-index: 50;
            }

            .se-notification-panel {
                z-index: 1010;
            }
        `
    ]
})
export class AppRootComponent implements OnInit {
    public configuration: INotificationConfiguration;

    constructor(
        private notificationService: INotificationService,
        private experienceService: IExperienceService
    ) {}

    ngOnInit() {
        this.reset();

        const MOCK_NOTIFICATION_ID = 'MOCK_NOTIFICATION';
        const MOCK_NOTIFICATION_TEMPLATE = '<div>This is a mock notification.</div>';
        const MOCK_NOTIFICATION_COUNT = 5;

        let mockNotificationIndex = 0;
        // without setTimeout "ExpressionChangedAfterItHasBeenCheckedError" is thrown. Only for e2e
        setTimeout(() => {
            while (mockNotificationIndex++ < MOCK_NOTIFICATION_COUNT) {
                this.notificationService.pushNotification({
                    id: MOCK_NOTIFICATION_ID + mockNotificationIndex,
                    template: MOCK_NOTIFICATION_TEMPLATE
                });
            }
        });
    }

    public pushNotification(): void {
        if (this.configuration.template.length < 1) {
            delete this.configuration.template;
        }

        if (this.configuration.templateUrl.length < 1) {
            delete this.configuration.templateUrl;
        }

        this.notificationService.pushNotification(this.configuration);

        this.reset();
    }

    public removeNotification(): void {
        this.notificationService.removeNotification(this.configuration.id);
    }

    public removeAllNotifications(): void {
        this.notificationService.removeAllNotifications();
    }

    public reset(): void {
        this.configuration = {
            id: '',
            template: '',
            templateUrl: ''
        };
    }

    public goToStorefront(): void {
        this.experienceService.loadExperience({
            siteId: 'apparel-uk',
            catalogId: 'apparel-ukContentCatalog',
            catalogVersion: 'Staged'
        });
    }

    public showComponentBasedNotification(): void {
        this.notificationService.pushNotification({
            id: this.configuration.id,
            componentName: MockNotificationComponent.name
        });
    }
}

angular.module('notificationApp', []).run(($templateCache: angular.ITemplateCacheService) => {
    $templateCache.put(
        'testNotificationTemplate.html',
        '<div>This is a test notification template.</div>'
    );
});
angular.module('smarteditcontainer').requires.push('notificationApp');

@SeEntryModule('notificationApp')
@NgModule({
    imports: [FormsModule],
    declarations: [AppRootComponent, MockNotificationComponent],
    entryComponents: [AppRootComponent, MockNotificationComponent]
})
export class NotificationAppNg {}
