/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces useValue:false */
import {
    ChangeDetectionStrategy,
    Component,
    HostBinding,
    Injector,
    Input,
    OnChanges
} from '@angular/core';
import { animate, style, transition, trigger } from '@angular/animations';

import { ANNOUNCEMENT_DATA, CompileHtmlNgController, IAnnouncementService } from 'smarteditcommons';
import {
    ANNOUNCEMENT_DEFAULTS,
    IAnnouncement
} from 'smarteditcontainer/services/announcement/AnnouncementServiceOuter';

import './AnnouncementComponent.scss';

/**
 * Renders an announcement in the upper right corner of the page
 * based on announcement configuration which can use one of the following templates:
 * `message`, `template`, `templateUrl` or `component`
 *
 * `template` and `templateUrl` are legacy support of AngularJS
 *
 */
/** @internal */
@Component({
    selector: 'se-announcement',
    changeDetection: ChangeDetectionStrategy.OnPush,
    animations: [
        trigger('fadeAnimation', [
            transition(':enter', [
                style({
                    opacity: 0,
                    transform: 'rotateY(90deg)'
                }),
                animate('0.5s'),
                style({
                    opacity: 1,
                    transform: 'translateX(0px)'
                })
            ]),
            transition(':leave', [
                animate('0.25s'),
                style({
                    opacity: '0',
                    transform: 'translateX(100%)'
                })
            ])
        ])
    ],
    templateUrl: './AnnouncementComponent.html'
})
export class AnnouncementComponent implements OnChanges {
    @Input() announcement: IAnnouncement;
    @HostBinding('@fadeAnimation') get fadeAnimation() {
        return true;
    }

    public isLegacyAngularJS: boolean;
    public legacyCompileHtmlNgController: CompileHtmlNgController;
    public announcementInjector: Injector;

    constructor(private announcementService: IAnnouncementService, private injector: Injector) {}

    ngOnChanges() {
        this.isLegacyAngularJS =
            typeof this.announcement.template !== 'undefined' ||
            typeof this.announcement.templateUrl !== 'undefined';

        this.legacyCompileHtmlNgController = this.hasController()
            ? { alias: '$announcementCtrl', value: this.announcement.controller }
            : undefined;

        if (!this.isLegacyAngularJS) {
            this.createAnnouncementInjector();
        }
    }

    public hasTemplate(): boolean {
        return this.announcement.hasOwnProperty('template');
    }

    public hasTemplateUrl(): boolean {
        return this.announcement.hasOwnProperty('templateUrl');
    }

    public hasComponent(): boolean {
        return this.announcement.hasOwnProperty('component');
    }

    public hasMessage(): boolean {
        return this.announcement.hasOwnProperty('message');
    }

    public hasMessageTitle(): boolean {
        return this.announcement.hasOwnProperty('messageTitle');
    }

    public isCloseable(): boolean {
        return this.announcement.hasOwnProperty('closeable')
            ? this.announcement.closeable
            : ANNOUNCEMENT_DEFAULTS.closeable;
    }

    public closeAnnouncement(): void {
        this.announcementService.closeAnnouncement(this.announcement.id);
    }

    private hasController(): boolean {
        return this.announcement.hasOwnProperty('controller');
    }

    private createAnnouncementInjector(): void {
        this.announcementInjector = Injector.create({
            parent: this.injector,
            providers: [
                {
                    provide: ANNOUNCEMENT_DATA,
                    useValue: {
                        id: this.announcement.id,
                        ...this.announcement.data
                    }
                }
            ]
        });
    }
}
