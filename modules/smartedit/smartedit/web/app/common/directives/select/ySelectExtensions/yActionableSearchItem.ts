/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from '../../../di';
import { UiSelect } from '../../../services/interfaces';
import { YSelectComponent } from '../ySelect';
import { SystemEventService } from '../../../services';

/**
 * @ngdoc directive
 * @name ySelectModule.directive:yActionableSearchItem
 * @restrict E
 * @scope
 * @param {@String=} [eventId='yActionableSearchItem_ACTION_CREATE'] The event ID that is triggered on the
 * systemEventService when the button is pressed
 * @param {@String=} [actionText='se.yationablesearchitem.action.create'] The i18n key label for the button
 * @description
 * The yActionableSearchItem Angular component is designed to work with the ySelect drop down. It allows you to add
 * a button in the resultsHeader area of the ySelect's drop-down, and trigger a user-defined action when pressed.
 */

@SeComponent({
    selector: 'y-actionable-search-item',
    templateUrl: 'yActionableSearchItemTemplate.html',
    require: {
        ySelect: '^ySelect',
        uiSelect: '^uiSelect'
    },
    inputs: ['eventId:@?', 'actionText:@?']
})
export class ActionableSearchItemComponent<T extends { id: string }> {
    public ySelect: YSelectComponent<T>;

    private uiSelect: UiSelect<T>;
    private eventId: string;
    private actionText: string;

    private defaultEventId = 'yActionableSearchItem_ACTION_CREATE';
    private defaultActionText = 'se.yationablesearchitem.action.create';

    constructor(private systemEventService: SystemEventService) {}

    public getActionText(): string {
        return this.actionText || this.defaultActionText;
    }

    public showForm(): boolean {
        return this.uiSelect && this.uiSelect.search && this.uiSelect.search.length > 0;
    }

    public getInputText(): string {
        return this.uiSelect.search;
    }

    public buttonPressed(): void {
        const evtId = this.eventId || this.defaultEventId;
        this.systemEventService.publishAsync(evtId, this.uiSelect.search || '');
        this.uiSelect.close();
    }
}
