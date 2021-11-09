/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, HostListener, Inject, Input, OnDestroy, OnInit } from '@angular/core';

import {
    ALL_PERSPECTIVE,
    CrossFrameEventService,
    EVENT_PERSPECTIVE_ADDED,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_REFRESHED,
    EVENT_SERVICE,
    EVENT_STRICT_PREVIEW_MODE_REQUESTED,
    EVENTS,
    IIframeClickDetectionService,
    IPerspective,
    IPerspectiveService,
    LogService,
    NONE_PERSPECTIVE,
    SeDowngradeComponent,
    SystemEventService,
    TestModeService,
    YJQUERY_TOKEN
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'se-perspective-selector',
    templateUrl: './PerspectiveSelectorComponent.html'
})
export class PerspectiveSelectorComponent implements OnInit, OnDestroy {
    @Input() public isOpen = false;
    public isDisabled = false;

    private perspectives: IPerspective[] = [];
    private displayedPerspectives: IPerspective[] = [];

    private unRegOverlayDisabledFn: () => void;
    private unRegPerspectiveAddedFn: () => void;
    private unRegPerspectiveChgFn: () => void;
    private unRegUserHasChanged: () => void;
    private unRegPerspectiveRefreshFn: () => void;
    private unRegStrictPreviewModeToggleFn: () => void;

    private activePerspective: IPerspective = null;

    constructor(
        private logService: LogService,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private perspectiveService: IPerspectiveService,
        private iframeClickDetectionService: IIframeClickDetectionService,
        private systemEventService: SystemEventService,
        @Inject(EVENT_SERVICE) private crossFrameEventService: CrossFrameEventService,
        private testModeService: TestModeService
    ) {}

    @HostListener('document:click', ['$event']) onDocumentClick(event: Event) {
        this.onClickHandler(event);
    }

    ngOnInit() {
        this.activePerspective = null;

        this.iframeClickDetectionService.registerCallback('perspectiveSelectorClose', () =>
            this.closeDropdown()
        );

        this.unRegOverlayDisabledFn = this.systemEventService.subscribe('OVERLAY_DISABLED', () =>
            this.closeDropdown()
        );
        this.unRegPerspectiveAddedFn = this.systemEventService.subscribe(
            EVENT_PERSPECTIVE_ADDED,
            () => this.onPerspectiveAdded()
        );
        this.unRegPerspectiveChgFn = this.crossFrameEventService.subscribe(
            EVENT_PERSPECTIVE_CHANGED,
            () => this.refreshPerspectives()
        );
        this.unRegPerspectiveRefreshFn = this.crossFrameEventService.subscribe(
            EVENT_PERSPECTIVE_REFRESHED,
            () => this.refreshPerspectives()
        );
        this.unRegUserHasChanged = this.crossFrameEventService.subscribe(
            EVENTS.USER_HAS_CHANGED,
            () => this.onPerspectiveAdded()
        );
        this.unRegStrictPreviewModeToggleFn = this.crossFrameEventService.subscribe(
            EVENT_STRICT_PREVIEW_MODE_REQUESTED,
            (eventId: string, isDisabled: boolean) => this.togglePerspectiveSelector(isDisabled)
        );

        this.onPerspectiveAdded();
    }

    ngOnDestroy() {
        this.unRegOverlayDisabledFn();
        this.unRegPerspectiveAddedFn();
        this.unRegPerspectiveChgFn();
        this.unRegPerspectiveRefreshFn();
        this.unRegUserHasChanged();
        this.unRegStrictPreviewModeToggleFn();
    }

    selectPerspective(event: Event, choice: string): void {
        event.preventDefault();

        try {
            this.perspectiveService.switchTo(choice);
            this.closeDropdown();
        } catch (e) {
            this.logService.error('selectPerspective() - Cannot select perspective.', e);
        }
    }

    getDisplayedPerspectives(): IPerspective[] {
        return this.displayedPerspectives;
    }

    getActivePerspectiveName(): string {
        return this.activePerspective ? this.activePerspective.nameI18nKey : '';
    }

    hasActivePerspective(): boolean {
        return this.activePerspective !== null;
    }

    isTooltipVisible(): boolean {
        return (
            !!this.activePerspective &&
            !!this.activePerspective.descriptionI18nKey &&
            this.checkTooltipVisibilityCondition()
        );
    }

    private checkTooltipVisibilityCondition(): boolean {
        if (
            this.activePerspective.key !== NONE_PERSPECTIVE ||
            (this.activePerspective.key === NONE_PERSPECTIVE && this.isDisabled)
        ) {
            return true;
        }
        return false;
    }

    private filterPerspectives(perspectives: IPerspective[]): IPerspective[] {
        return perspectives.filter((perspective) => {
            const isActivePerspective =
                this.activePerspective && perspective.key === this.activePerspective.key;
            const isAllPerspective = perspective.key === ALL_PERSPECTIVE;
            return !isActivePerspective && (!isAllPerspective || this.testModeService.isE2EMode());
        });
    }

    private closeDropdown(): void {
        this.isOpen = false;
    }

    private onPerspectiveAdded(): void {
        this.perspectiveService.getPerspectives().then((result: IPerspective[]) => {
            this.perspectives = result;
            this.displayedPerspectives = this.filterPerspectives(this.perspectives);
        });
    }

    private refreshPerspectives(): void {
        this.perspectiveService.getPerspectives().then((result) => {
            this.perspectives = result;
            this._refreshActivePerspective();
            this.displayedPerspectives = this.filterPerspectives(this.perspectives);
        });
    }

    private _refreshActivePerspective(): void {
        this.activePerspective = this.perspectiveService.getActivePerspective();
    }

    private onClickHandler(event: Event): void {
        if (
            this.yjQuery(event.target).parents('.se-perspective-selector').length <= 0 &&
            this.isOpen
        ) {
            this.closeDropdown();
        }
    }

    private togglePerspectiveSelector(value: boolean): void {
        this.isDisabled = value;
    }
}
