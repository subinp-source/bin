/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

import {
    AnnouncementService,
    IAnnouncement
} from 'smarteditcontainer/services/announcement/AnnouncementServiceOuter';
import { IAnnouncementService } from 'smarteditcommons';

import './AnnouncementBoardComponent.scss';

/**
 * Renders list of announcements
 */
/** @internal */
@Component({
    selector: 'se-announcement-board',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './AnnouncementBoardComponent.html'
})
export class AnnouncementBoardComponent implements OnInit {
    public announcements$: Observable<IAnnouncement[]>;
    constructor(@Inject(IAnnouncementService) private announcementService: AnnouncementService) {}

    ngOnInit() {
        this.announcements$ = this.announcementService.getAnnouncements();
    }

    public annnouncementTrackBy(index: number, item: IAnnouncement): string {
        return item.id;
    }
}
