/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';

import { SeDowngradeComponent } from '../../../di';
import { SystemEventService } from '../../../services';
import { EventMessageData } from './EventMessageData';

/**
 * Deprecated, use se-event-message.
 * The se-event-message is a wrapper around se-message, used to display or hide the message based on events sent through the systemEventService.
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-event-message',
    template: `
        <div>
            <se-message [type]="type" *ngIf="show">
                <ng-container *ngIf="showTitle()" se-message-title>
                    {{ title | translate }}
                </ng-container>
                <ng-container *ngIf="showDescription()" se-message-description>
                    {{ description | translate }}
                </ng-container>
            </se-message>
        </div>
    `
})
export class EventMessageComponent implements OnInit, OnChanges, OnDestroy {
    @Input() type: string;
    @Input() title: string;
    @Input() description: string;
    @Input() showEvent: string;
    @Input() hideEvent: string;
    @Input() showToStart: string | boolean;

    public show: boolean = false;

    private unregisterShowEventHandler: () => void;
    private unregisterHideEventHandler: () => void;

    constructor(private systemEventService: SystemEventService) {}

    ngOnInit(): void {
        this.show = this.showToStart === 'true' || this.showToStart === true;

        this.watchShowEventChange();
        this.watchHideEventChange();
    }

    ngOnDestroy() {
        this.removeShowEventHandler();
        this.removeHideEventHandler();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.showEvent) {
            this.watchShowEventChange();
        }
        if (changes.hideEvent) {
            this.watchHideEventChange();
        }
    }

    showDescription(): boolean {
        return typeof this.description === 'string' && this.description.length > 0;
    }

    showTitle(): boolean {
        return typeof this.title === 'string' && this.title.length > 0;
    }

    showEventHandler(eventId: string, eventData: EventMessageData): void {
        if (eventData.description && eventData.description.length) {
            this.description = eventData.description;
        }
        if (eventData.title && eventData.title.length) {
            this.title = eventData.title;
        }
        this.show = true;
    }

    private removeHideEventHandler(): void {
        if (this.unregisterHideEventHandler) {
            this.unregisterHideEventHandler();
        }
    }

    private removeShowEventHandler(): void {
        if (this.unregisterShowEventHandler) {
            this.unregisterShowEventHandler();
        }
    }

    private watchShowEventChange(): void {
        this.removeShowEventHandler();
        this.unregisterShowEventHandler = this.systemEventService.subscribe(
            this.showEvent,
            (eventId: string, eventData: any) => this.showEventHandler(eventId, eventData)
        );
    }

    private watchHideEventChange(): void {
        this.removeHideEventHandler();
        this.unregisterHideEventHandler = this.systemEventService.subscribe(
            this.hideEvent,
            () => (this.show = false)
        );
    }
}
