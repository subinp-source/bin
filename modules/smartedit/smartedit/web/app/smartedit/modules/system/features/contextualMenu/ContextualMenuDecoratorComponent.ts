/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, DoCheck, ElementRef, Inject, Input, OnDestroy, OnInit } from '@angular/core';

import {
    ComponentAttributes,
    CLOSE_CTX_MENU,
    IContextualMenuButton,
    IContextualMenuService,
    NodeUtils,
    PopupOverlayConfig,
    REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
    SeCustomComponent,
    SystemEventService,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { ComponentHandlerService, ContextualMenu } from 'smartedit/services';
import { BaseContextualMenuComponent } from './BaseContextualMenuComponent';
import { MoreItemsComponent } from './MoreItemsComponent';
import { ContextualMenuItemOverlayComponent } from './ContextualMenuItemOverlayComponent';
import { filter } from 'rxjs/operators';

@SeCustomComponent()
@Component({
    selector: 'contextual-menu',
    templateUrl: './ContextualMenuDecoratorComponent.html'
})
export class ContextualMenuDecoratorComponent extends BaseContextualMenuComponent
    implements OnInit, DoCheck, OnDestroy {
    @Input('data-smartedit-component-type') public smarteditComponentType: string;
    @Input('data-smartedit-component-id') public smarteditComponentId: string;
    @Input('data-smartedit-container-type') public smarteditContainerType: string;
    @Input('data-smartedit-container-id') public smarteditContainerId: string;
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

    public items: ContextualMenu;
    public openItem: IContextualMenuButton = null;
    public moreMenuIsOpen = false;

    public slotAttributes: {
        smarteditSlotId: string;
        smarteditSlotUuid: string;
    };

    public itemTemplateOverlayWrapper: PopupOverlayConfig = {
        component: ContextualMenuItemOverlayComponent
    };

    public moreMenuPopupConfig: PopupOverlayConfig = {
        component: MoreItemsComponent,
        halign: 'left'
    };

    public moreButton = {
        displayClass: 'sap-icon--overflow',
        i18nKey: 'se.cms.contextmenu.title.more'
    };

    private displayedItem: IContextualMenuButton;
    private oldWidth: number = null;
    private dndUnRegFn: () => void;
    private unregisterRefreshItems: () => void;
    private _active: boolean;

    constructor(
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private element: ElementRef,
        private contextualMenuService: IContextualMenuService,
        private systemEventService: SystemEventService,
        private componentHandlerService: ComponentHandlerService,
        private nodeUtils: NodeUtils
    ) {
        super();
    }

    /*
     * will only init when element's is not 0, which is what happens after a recompile of decorators called by sakExecutor after perspective change or refresh
     */
    ngDoCheck() {
        if (this.element) {
            const width = this.element.nativeElement.offsetWidth;
            if (this.oldWidth !== width) {
                this.oldWidth = width;
                this.ngOnDestroy();
                this.onInit();
            }
        }
    }

    ngOnDestroy() {
        if (this.dndUnRegFn) {
            this.dndUnRegFn();
        }
        if (this.unregisterRefreshItems) {
            this.unregisterRefreshItems();
        }
    }

    ngOnInit() {
        this.componentAttributes = this.nodeUtils.collectSmarteditAttributesByElementUuid(
            this.smarteditElementUuid
        );

        this.slotAttributes = {
            smarteditSlotId: this.smarteditSlotId,
            smarteditSlotUuid: this.smarteditSlotUuid
        };

        this.onInit();

        this.contextualMenuService.onContextualMenuItemsAdded
            .pipe(filter((type) => type === this.smarteditComponentType))
            .subscribe((type) => this.updateItems());
    }

    public get smarteditSlotId(): string {
        return this.componentHandlerService.getParentSlotForComponent(this.element.nativeElement);
    }

    public get smarteditSlotUuid(): string {
        return this.componentHandlerService.getParentSlotUuidForComponent(
            this.element.nativeElement
        );
    }

    onInit(): void {
        this.updateItems();

        this.dndUnRegFn = this.systemEventService.subscribe(CLOSE_CTX_MENU, () =>
            this.hideAllPopups()
        );
        this.unregisterRefreshItems = this.systemEventService.subscribe(
            REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT,
            () => this.updateItems
        );
    }

    toggleMoreMenu() {
        this.moreMenuIsOpen = !this.moreMenuIsOpen;
    }
    shouldShowTemplate(menuItem: IContextualMenuButton): boolean {
        return this.displayedItem === menuItem;
    }

    onShowItemPopup(item: IContextualMenuButton) {
        this.setRemainOpen('someContextualPopupOverLay', true);

        this.openItem = item;
        (this.openItem as any).isOpen = true; // does anything use this?
    }

    onHideItemPopup(hideMoreMenu?: boolean): void {
        if (this.openItem) {
            (this.openItem as any).isOpen = false; // does anything use this?
            this.openItem = null;
        }

        this.displayedItem = null;
        this.setRemainOpen('someContextualPopupOverLay', false);
        if (hideMoreMenu) {
            this.onHideMoreMenuPopup();
        }
    }

    onShowMoreMenuPopup() {
        this.setRemainOpen('someContextualPopupOverLay', true);
    }

    onHideMoreMenuPopup() {
        this.moreMenuIsOpen = false;
        this.setRemainOpen('someContextualPopupOverLay', false);
    }

    hideAllPopups(): void {
        this.onHideMoreMenuPopup();
        this.onHideItemPopup();
    }

    getItems(): ContextualMenu {
        return this.items;
    }

    showContextualMenuBorders(): boolean {
        return this.active && this.items && this.items.leftMenuItems.length > 0;
    }

    triggerMenuItemAction(item: IContextualMenuButton, $event: Event) {
        $event.stopPropagation();
        $event.preventDefault();

        if (item.action.template || item.action.templateUrl || item.action.component) {
            if (this.displayedItem === item) {
                this.displayedItem = null;
            } else {
                this.displayedItem = item;
            }
        } else if (item.action.callback) {
            // close any popups
            this.hideAllPopups();
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
    }

    private maxContextualMenuItems(): number {
        const ctxSize = 50;
        const buttonMaxCapacity =
            Math.round(this.yjQuery(this.element.nativeElement).width() / ctxSize) - 1;
        let leftButtons = buttonMaxCapacity >= 4 ? 3 : buttonMaxCapacity - 1;
        leftButtons = leftButtons < 0 ? 0 : leftButtons;
        return leftButtons;
    }

    private updateItems(): void {
        this.contextualMenuService
            .getContextualMenuItems({
                componentType: this.smarteditComponentType,
                componentId: this.smarteditComponentId,
                containerType: this.smarteditContainerType,
                containerId: this.smarteditContainerId,
                componentAttributes: this.componentAttributes,
                iLeftBtns: this.maxContextualMenuItems(),
                element: this.yjQuery(this.element.nativeElement)
            })
            .then((newItems: ContextualMenu) => {
                this.items = newItems;
            });
    }
}
