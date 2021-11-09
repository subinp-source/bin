/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';
import { ContextualMenuDecoratorComponent } from './ContextualMenuDecoratorComponent';

@Component({
    selector: 'se-more-items-component',
    template: `
        <div class="se-contextual-more-menu fd-menu">
            <ul
                id="{{ parent.smarteditComponentId }}-{{ parent.smarteditComponentType }}-more-menu"
                class="fd-menu__list se-contextual-more-menu__list"
            >
                <li
                    *ngFor="let item of parent.getItems().moreMenuItems; let $index = index"
                    [attr.data-smartedit-id]="parent.smarteditComponentId"
                    [attr.data-smartedit-type]="parent.smarteditComponentType"
                    class="se-contextual-more-menu__item"
                    [ngClass]="item.customCss"
                >
                    <se-popup-overlay
                        [popupOverlay]="parent.itemTemplateOverlayWrapper"
                        [popupOverlayTrigger]="parent.shouldShowTemplate(item)"
                        [popupOverlayData]="{ item: item }"
                        (popupOverlayOnShow)="parent.onShowItemPopup(item)"
                        (popupOverlayOnHide)="parent.onHideItemPopup(false)"
                    >
                        <se-contextual-menu-item
                            [mode]="'compact'"
                            [index]="$index"
                            [componentAttributes]="parent.componentAttributes"
                            [slotAttributes]="parent.slotAttributes"
                            [itemConfig]="item"
                            (click)="parent.triggerMenuItemAction(item, $event)"
                            [attr.data-component-id]="parent.smarteditComponentId"
                            [attr.data-component-uuid]="
                                parent.componentAttributes.smarteditComponentUuid
                            "
                            [attr.data-component-type]="parent.smarteditComponentType"
                            [attr.data-slot-id]="parent.smarteditSlotId"
                            [attr.data-slot-uuid]="parent.smarteditSlotUuid"
                            [attr.data-container-id]="parent.smarteditContainerId"
                            [attr.data-container-type]="parent.smarteditContainerType"
                        >
                        </se-contextual-menu-item>
                    </se-popup-overlay>
                </li>
            </ul>
        </div>
    `
})
export class MoreItemsComponent {
    constructor(
        @Inject(forwardRef(() => ContextualMenuDecoratorComponent))
        public parent: ContextualMenuDecoratorComponent
    ) {}
}
