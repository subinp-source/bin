/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as angular from 'angular';

import { IAnnouncementService, SeDowngradeComponent, SeEntryModule } from 'smarteditcommons';

@Component({
    selector: 'announcement-mock',
    template: `
        <div>This is an announcement component<br />message: {{ message }}</div>
    `
})
class MockAnnouncementComponent {
    message: string = 'Component Based Announcement Message';
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="btn-group" class="smartedit-testing-overlay">
            <h2>Announcement Tester</h2>
            <button
                id="test-announcement-non-closeable-button"
                (click)="showNonCloseableAnnouncement()"
            >
                Show Non Closeable announcement
            </button>
            <button id="test-announcement-simple-button" (click)="showSimpleAnnouncement()">
                Show Simple Announcement
            </button>
            <button
                id="test-announcement-template-button"
                (click)="showTemplateBasedAnnouncement()"
            >
                Show Template Based Announcement
            </button>
            <button
                id="test-announcement-templateurl-button"
                (click)="showTemplateUrlBasedAnnouncement()"
            >
                Show TemplateUrl Based Announcement
            </button>
            <button
                id="test-announcement-templateurl-customctrl-button"
                (click)="showTemplateUrlBasedWithCustomControllerAnnouncement()"
            >
                Show TemplateUrl Based Announcement with Custom Controller
            </button>
            <button
                id="test-announcement-component-button"
                (click)="showComponentBasedAnnouncement()"
            >
                Show Component Based Announcement
            </button>
        </div>
    `,
    styles: [
        `
            .btn-group button {
                background-color: #4caf50;
                color: white;
                padding: 10px 24px;
                cursor: pointer;
                width: 80%;
                display: block;
                margin-top: 10px;
            }

            .btn-group button:hover {
                background-color: #3e8e41;
            }
        `
    ]
})
export class AppRootComponent {
    constructor(private announcementService: IAnnouncementService) {}

    public showSimpleAnnouncement(): void {
        this.announcementService.showAnnouncement({
            messageTitle: 'This is the message title',
            message: 'This is a simple announcement',
            timeout: 5000
        });
    }

    public showNonCloseableAnnouncement(): void {
        this.announcementService.showAnnouncement({
            template: '<div>This is a non closeable announcement</div>',
            closeable: false,
            timeout: 5000
        });
    }

    public showTemplateBasedAnnouncement(): void {
        this.announcementService.showAnnouncement({
            template:
                '<div>{{$announcementCtrl.taskName}}<br/>{{$announcementCtrl.pageDetails}}<br/><br/>{{$announcementCtrl.body}}</div>',
            controller() {
                'ngInject';
                this.taskName = 'Translation (DE)';
                this.pageDetails = 'Electronics Staged | Homepage';
                this.body = 'This is a template based announcement';
            },
            closeable: true
        });
    }

    public showTemplateUrlBasedAnnouncement(): void {
        this.announcementService.showAnnouncement({
            templateUrl: 'sampleAnnouncement1template.html',
            closeable: true
        });
    }

    public showTemplateUrlBasedWithCustomControllerAnnouncement(): void {
        this.announcementService.showAnnouncement({
            templateUrl: 'sampleAnnouncement2template.html',
            controller() {
                'ngInject';
                this.data = 'This is the data coming from a custom controller';
            },
            closeable: true
        });
    }

    public showComponentBasedAnnouncement(): void {
        this.announcementService.showAnnouncement({
            component: MockAnnouncementComponent
        });
    }
}

angular.module('annoucementApp', []).run(($templateCache: angular.ITemplateCacheService) => {
    $templateCache.put(
        'sampleAnnouncement1template.html',
        '<div>This is an announcement coming from a template url and static data</div>'
    );
    $templateCache.put(
        'sampleAnnouncement2template.html',
        '<div>This is an announcement<br/>Data: {{$announcementCtrl.data}}</div>'
    );
});

angular.module('smarteditcontainer').requires.push('annoucementApp');

@SeEntryModule('annoucementApp')
@NgModule({
    imports: [CommonModule],
    declarations: [AppRootComponent, MockAnnouncementComponent],
    entryComponents: [AppRootComponent, MockAnnouncementComponent]
})
export class AnnoucementAppNg {}
