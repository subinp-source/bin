/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';

import { SystemEventService } from '../../../services';

export interface ActionableSearchItem {
    /** Event Id to publish by `SystemEventService` */
    eventId?: string;
    /** Action button label */
    actionText?: string;
}

/**
 * Component displayed when there is no search results for given string in `SelectComponent`.
 * Contains of text and the Action Button which can be clicked by a user to publish
 * specified Event Id by `SystemEventService`.
 *
 * For example: It can be used to invoke an Event that will add a new item to the list.
 */
@Component({
    selector: 'se-actionable-search-item',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.se-actionable-search-item]': 'true'
    },
    templateUrl: './ActionableSearchItemComponent.html'
})
export class ActionableSearchItemComponent {
    @Input() search: string;
    @Input() eventId?: ActionableSearchItem['eventId'];
    @Input() actionText?: ActionableSearchItem['actionText'];
    @Output() actionButtonClick = new EventEmitter<void>();

    private readonly defaultEventId = 'yActionableSearchItem_ACTION_CREATE';
    private readonly defaultActionText = 'se.yationablesearchitem.action.create';

    constructor(private systemEventService: SystemEventService) {}

    /** @internal */
    public getActionText(): string {
        return this.actionText || this.defaultActionText;
    }

    /** @internal */
    public onButtonClick(): void {
        const evtId = this.eventId || this.defaultEventId;
        this.systemEventService.publishAsync(evtId, this.search || '');
        this.actionButtonClick.emit();
    }
}
