/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/* forbiddenNameSpaces useValue:false */

import * as angular from 'angular';
import {
    ChangeDetectorRef,
    Component,
    Inject,
    Injector,
    Input,
    OnDestroy,
    OnInit
} from '@angular/core';

import {
    CompileHtmlNgController,
    EVENTS,
    IIframeClickDetectionService,
    IToolbarServiceFactory,
    LEGACY_LOCATION,
    SeDowngradeComponent,
    SystemEventService,
    STORE_FRONT_CONTEXT,
    ToolbarItemInternal,
    TOOLBAR_ITEM
} from 'smarteditcommons';
import { ToolbarService } from '../services/ToolbarService';

/**
 * @ngdoc component
 * @name smarteditServicesModule.component:ToolbarComponent
 * @element se-toolbar
 *
 * @description
 * Toolbar HTML mark-up that compiles into a configurable toolbar with an assigned {@link
 * smarteditCommonsModule.IToolbarService ToolbarService} for functionality. The toolbar listens for
 * {@link seConstantsModule.object:EVENTS PAGE_SELECTED} event and invokes the callback that closes all action items.
 *
 * @param {String} imageRoot Root folder path for images
 * @param {String} cssClass Space-separated string of CSS classes for toolbar item styling
 * @param {String} toolbarName Toolbar name used by the gateway proxy service
 */

/** @internal  */

@SeDowngradeComponent()
@Component({
    selector: 'se-toolbar',
    templateUrl: './ToolbarComponent.html'
})
export class ToolbarComponent implements OnInit, OnDestroy {
    static readonly CLOSE_ALL_ACTION_ITEMS = 'closeAllActionItems';

    @Input() cssClass: string;
    @Input() toolbarName: string;
    @Input() imageRoot: string = '';

    public aliases: ToolbarItemInternal[] = [];

    private unregCloseActions: () => void = null;
    private unregCloseAll: () => void = null;
    private unregRecalcPermissions: () => void = null;
    private toolbarService: ToolbarService;

    constructor(
        private toolbarServiceFactory: IToolbarServiceFactory<ToolbarService>,
        private iframeClickDetectionService: IIframeClickDetectionService,
        private systemEventService: SystemEventService,
        private injector: Injector,
        private cdr: ChangeDetectorRef,
        @Inject(LEGACY_LOCATION) private location: angular.ILocationService
    ) {}

    ngOnInit() {
        this.setup();
    }

    ngOnDestroy() {
        this.unregCloseActions();
        this.unregCloseAll();
        this.unregRecalcPermissions();
    }

    // used in ToolbarActionComponent
    public triggerAction(action: ToolbarItemInternal, $event: Event): void {
        $event.preventDefault();
        this.toolbarService.triggerAction(action);
    }

    // used in ToolbarActionComponent
    public getItemVisibility(item: ToolbarItemInternal): boolean {
        return (item.include || item.component) && (item.isOpen || item.keepAliveOnClose);
    }

    // used in inherited items
    public isOnStorefront(): boolean {
        return this.location.absUrl().indexOf(STORE_FRONT_CONTEXT) >= 0;
    }

    public createLegacyController(): CompileHtmlNgController {
        return {
            alias: '$ctrl',
            value: () => this
        };
    }

    public createInjector(item: ToolbarItemInternal): Injector {
        return Injector.create({
            parent: this.injector,
            providers: [
                {
                    provide: TOOLBAR_ITEM,
                    useValue: item
                }
            ]
        });
    }

    public trackByFn(_: number, item: ToolbarItemInternal): string {
        return item.key;
    }

    private closeAllActionItems(): void {
        this.aliases.forEach((alias) => {
            alias.isOpen = false;
        });
    }

    private async populatePermissions(): Promise<void> {
        const promises = this.aliases.map((alias) =>
            this.toolbarService._populateIsPermissionGranted(alias)
        );

        this.aliases = await Promise.all(promises);
    }

    private setup(): void {
        this.buildAliases();
        this.populatePermissions();
        this.registerCallbacks();
    }

    private buildAliases(): void {
        this.toolbarService = this.toolbarServiceFactory.getToolbarService(this.toolbarName);
        this.toolbarService.setOnAliasesChange(
            (aliases: ToolbarItemInternal[]): void => {
                this.aliases = aliases;
                this.cdr.detectChanges();
            }
        );

        this.aliases = this.toolbarService.getAliases();
    }

    private registerCallbacks(): void {
        this.unregCloseActions = this.iframeClickDetectionService.registerCallback(
            ToolbarComponent.CLOSE_ALL_ACTION_ITEMS + this.toolbarName,
            () => this.closeAllActionItems()
        );

        this.unregCloseAll = this.systemEventService.subscribe(EVENTS.PAGE_SELECTED, () =>
            this.closeAllActionItems()
        );

        this.unregRecalcPermissions = this.systemEventService.subscribe(
            EVENTS.PERMISSION_CACHE_CLEANED,
            () => this.populatePermissions()
        );
    }
}
