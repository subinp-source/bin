/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */
import {
    Component,
    DoCheck,
    ElementRef,
    Inject,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    SimpleChanges
} from '@angular/core';
import { throttle } from 'lodash';
import { filter } from 'rxjs/operators';

import {
    ComponentAttributes,
    HIDE_SLOT_MENU,
    IContextualMenuButton,
    IContextualMenuService,
    IPermissionService,
    NodeUtils,
    REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
    SeCustomComponent,
    SystemEventService,
    SHOW_SLOT_MENU,
    YJQUERY_TOKEN
} from 'smarteditcommons';

import { BaseContextualMenuComponent } from '../contextualMenu/BaseContextualMenuComponent';

@SeCustomComponent()
@Component({
    selector: 'slot-contextual-menu',
    templateUrl: './SlotContextualMenuDecoratorComponent.html'
})
export class SlotContextualMenuDecoratorComponent extends BaseContextualMenuComponent
    implements OnInit, OnDestroy, OnChanges, DoCheck {
    @Input('data-smartedit-component-type') public smarteditComponentType: string;
    @Input('data-smartedit-component-id') public smarteditComponentId: string;
    @Input('data-smartedit-container-type') public smarteditContainerType: string;
    @Input('data-smartedit-container-id') public smarteditContainerId: string;
    @Input('data-smartedit-slot-id') public smarteditSlotId: string;
    @Input('data-smartedit-slot-uuid') public smarteditSlotUuid: string;
    @Input('data-smartedit-catalog-version-uuid') public smarteditCatalogVersionUuid: string;
    @Input('data-smartedit-element-uuid') public smarteditElementUuid: string;
    @Input() public componentAttributes: ComponentAttributes;
    // @ts-ignore
    @Input() public set active(_active: string) {
        this._active = _active === 'true';
    }
    // @ts-ignore
    public get active(): boolean {
        return this._active;
    }
    public showItems = false;
    public items: IContextualMenuButton[];
    public itemsrc: string;
    public showAtBottom: boolean;

    private oldRightMostOffsetFromPage: number = null;
    private maxContextualMenuItems = 3;
    private permissionsObject: any;
    private showSlotMenuUnregFn: () => void;
    private hideSlotMenuUnregFn: () => void;
    private refreshContextualMenuUnregFn: () => void;
    private hideSlotUnSubscribeFn: () => void;
    private showSlotUnSubscribeFn: () => void;
    private _active: boolean;

    constructor(
        private element: ElementRef,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private systemEventService: SystemEventService,
        private contextualMenuService: IContextualMenuService,
        private permissionService: IPermissionService,
        private nodeUtils: NodeUtils
    ) {
        super();
        this.showAtBottom = this.element.nativeElement.getAttribute('show-at-bottom') || false;
        const THROTTLE_DELAY = 200;
        this.positionPanelHorizontally = throttle(
            () => this.positionPanelHorizontally(),
            THROTTLE_DELAY
        );
        this.positionPanelVertically = throttle(
            () => this.positionPanelVertically(),
            THROTTLE_DELAY
        );
        this.hidePadding = throttle(this.hidePadding, THROTTLE_DELAY);
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.active) {
            this.hidePadding();
            if (this.active) {
                this.positionPanelVertically();
                this.positionPanelHorizontally();
            }
        }
    }

    ngOnInit() {
        this.componentAttributes = this.nodeUtils.collectSmarteditAttributesByElementUuid(
            this.smarteditElementUuid
        );

        this.updateItems();

        this.permissionsObject = [
            {
                names: ['se.slot.not.external'],
                context: {
                    slotCatalogVersionUuid: this.componentAttributes.smarteditCatalogVersionUuid
                }
            }
        ];

        this.permissionService.isPermitted(this.permissionsObject).then((isAllowed) => {
            this.showItems = isAllowed;

            this.showSlotMenuUnregFn = this.systemEventService.subscribe(
                this.smarteditComponentId + SHOW_SLOT_MENU,
                (eventId: string, slotId: string): void => {
                    this.remainOpenMap.slotMenuButton = true;
                    this.positionPanelVertically();
                    this.positionPanelHorizontally();
                }
            );

            this.hideSlotMenuUnregFn = this.systemEventService.subscribe(
                HIDE_SLOT_MENU,
                (): void => {
                    if (this.remainOpenMap.slotMenuButton) {
                        delete this.remainOpenMap.slotMenuButton;
                    }
                    this.hidePadding();
                }
            );

            this.refreshContextualMenuUnregFn = this.systemEventService.subscribe(
                REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
                () => this.updateItems()
            );
        });

        this.contextualMenuService.onContextualMenuItemsAdded
            .pipe(filter((type) => type === this.smarteditComponentType))
            .subscribe(() => this.updateItems());
    }

    ngDoCheck() {
        const rightMostOffsetFromPage = this.getRightMostOffsetFromPage();
        if (
            this.active &&
            !isNaN(rightMostOffsetFromPage) &&
            rightMostOffsetFromPage !== this.oldRightMostOffsetFromPage
        ) {
            this.oldRightMostOffsetFromPage = rightMostOffsetFromPage;
            this.positionPanelHorizontally(rightMostOffsetFromPage);
        }
    }

    ngOnDestroy() {
        if (this.showSlotMenuUnregFn) {
            this.showSlotMenuUnregFn();
        }
        if (this.hideSlotMenuUnregFn) {
            this.hideSlotMenuUnregFn();
        }
        if (this.refreshContextualMenuUnregFn) {
            this.refreshContextualMenuUnregFn();
        }
        if (this.hideSlotUnSubscribeFn) {
            this.hideSlotUnSubscribeFn();
        }
        if (this.showSlotUnSubscribeFn) {
            this.showSlotUnSubscribeFn();
        }
    }

    updateItems(): void {
        this.contextualMenuService
            .getContextualMenuItems({
                componentType: this.smarteditComponentType,
                componentId: this.smarteditComponentId,
                containerType: this.smarteditContainerType,
                containerId: this.smarteditContainerId,
                componentAttributes: this.componentAttributes,
                iLeftBtns: this.maxContextualMenuItems,
                element: this.yjQuery(this.element.nativeElement)
            })
            .then((newItems) => {
                this.items = [...newItems.leftMenuItems, ...newItems.moreMenuItems];
            });
    }

    triggerMenuItemAction(item: IContextualMenuButton, $event: Event): void {
        item.action.callback(
            {
                componentType: this.smarteditComponentType,
                componentId: this.smarteditComponentId,
                containerType: this.smarteditContainerType,
                containerId: this.smarteditContainerId,
                componentAttributes: this.componentAttributes,
                slotId: this.smarteditSlotId,
                slotUuid: this.smarteditSlotUuid,
                element: this.yjQuery(this.element.nativeElement)
            },
            $event
        );
    }

    private hidePadding(): void {
        this.yjQuery(this.element.nativeElement)
            .find('.se-decorative-body__padding--left')
            .css('display', 'none');
        this.yjQuery(this.element.nativeElement)
            .find('.se-decorative-body__padding--right')
            .css('display', 'none');
    }

    private getRightMostOffsetFromPage(): number {
        const $decorativePanel = this.yjQuery(this.element.nativeElement).find(
            '.se-decorative-panel-area'
        );
        return this.yjQuery(this.element.nativeElement).offset().left + $decorativePanel.width();
    }

    private positionPanelHorizontally(rightMostOffsetFromPage?: number): void {
        const $decorativePanel = this.yjQuery(this.element.nativeElement).find(
            '.se-decorative-panel-area'
        );

        rightMostOffsetFromPage =
            rightMostOffsetFromPage !== undefined
                ? rightMostOffsetFromPage
                : this.getRightMostOffsetFromPage();

        // Calculate if the slot is overflowing the body width.
        const isOnLeft = rightMostOffsetFromPage >= this.yjQuery('body').width();

        if (isOnLeft) {
            const offset =
                $decorativePanel.outerWidth() -
                this.yjQuery(this.element.nativeElement)
                    .find('.se-wrapper-data')
                    .width();
            $decorativePanel.css('margin-left', -offset);
            this.yjQuery(this.element.nativeElement)
                .find('.se-decorative-body__padding--left')
                .css('margin-left', -offset);
        }

        // Hide all paddings and show the left or right one.
        this.hidePadding();
        this.yjQuery(this.element.nativeElement)
            .find(
                isOnLeft
                    ? '.se-decorative-body__padding--left'
                    : '.se-decorative-body__padding--right'
            )
            .css('display', 'flex');
    }

    private positionPanelVertically(): void {
        const decorativePanelArea = this.yjQuery(this.element.nativeElement).find(
            '.se-decorative-panel-area'
        );
        const decoratorPaddingContainer = this.yjQuery(this.element.nativeElement).find(
            '.se-decoratorative-body-area'
        );
        let marginTop;
        const height = decorativePanelArea.height();
        if (this.yjQuery(this.element.nativeElement).offset().top <= height) {
            const borderOffset = 6; // accounts for 3px border around the slots

            marginTop = decoratorPaddingContainer.height() + borderOffset;
            decoratorPaddingContainer.css('margin-top', -(marginTop + height));
        } else {
            marginTop = -32;
        }
        decorativePanelArea.css('margin-top', marginTop);
    }
}
