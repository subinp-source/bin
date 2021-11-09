/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    ElementRef,
    Inject,
    OnDestroy,
    OnInit
} from '@angular/core';
import { Observable } from 'rxjs';

import {
    EVENT_NOTIFICATION_CHANGED,
    IBound,
    INotificationConfiguration,
    INotificationMouseLeaveDetectionService,
    INotificationService,
    SystemEventService,
    WindowUtils,
    YJQuery,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import {
    IframeManagerService,
    NotificationMouseLeaveDetectionService,
    NotificationService
} from '../../services';

/**
 * The NotificationPanel component is responsible for getting the list of
 * notifications to display and handling showing and hiding the list when the mouse
 * pointer enters and leaves the portion of the screen occupied by the list.
 */

@Component({
    selector: 'se-notification-panel',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './NotificationPanelComponentTemplate.html'
})
export class NotificationPanelComponent implements OnInit, AfterViewInit, OnDestroy {
    public isMouseOver: boolean;
    public notifications$: Observable<INotificationConfiguration[]>;

    private notificationPanelBounds: IBound = null;
    private iFrameNotificationPanelBounds: IBound = null;
    private addMouseMoveEventListenerTimeout: number = null;
    private unRegisterNotificationChangedEventHandler: () => void;
    private $element: JQuery<Element>;

    constructor(
        @Inject(INotificationService) private notificationService: NotificationService,
        @Inject(INotificationMouseLeaveDetectionService)
        private notificationMouseLeaveDetectionService: NotificationMouseLeaveDetectionService,
        private systemEventService: SystemEventService,
        private iframeManagerService: IframeManagerService,
        private windowUtils: WindowUtils,
        private element: ElementRef,
        @Inject(YJQUERY_TOKEN) private yjQuery: YJQuery,
        private cd: ChangeDetectorRef
    ) {}

    ngOnInit() {
        this.notifications$ = this.notificationService.getNotifications();

        this.isMouseOver = false;

        this.windowUtils.getWindow().addEventListener('resize', () => this.onResize());

        this.unRegisterNotificationChangedEventHandler = this.systemEventService.subscribe(
            EVENT_NOTIFICATION_CHANGED,
            () => this.onNotificationChanged()
        );
    }

    ngAfterViewInit() {
        this.$element = this.yjQuery(this.element.nativeElement);
    }

    ngOnDestroy() {
        this.windowUtils.getWindow().removeEventListener('resize', () => this.onResize());

        this.notificationMouseLeaveDetectionService.stopDetection();

        this.unRegisterNotificationChangedEventHandler();
    }

    /**
     * Called when hovered on notification panel or
     * when the panel was not displayed and then notification was added.
     */
    public onMouseEnter(): void {
        this.isMouseOver = true;
        this.cd.detectChanges();

        if (!this.hasBounds()) {
            this.calculateBounds();
        }

        /*
         * We use a small timeout to delay the activation of mouse tracking. If the listener
         * is immediately atimeoutdded, it triggers an event with mouse coordiantes that are outside
         * of the notification panel, thus making it re-appear.
         */
        this.addMouseMoveEventListenerTimeout =
            this.addMouseMoveEventListenerTimeout ||
            ((setTimeout(() => this.addMouseMoveEventListener(), 10) as unknown) as number);
    }

    /**
     * Due to the fact that this function is called from a timeout, no digest cycle is
     * triggered. It has to be triggered for the template to be refreshed to make the
     * notification panel re-appear.
     */
    private onMouseLeave(): void {
        this.isMouseOver = false;
        this.cd.detectChanges();
    }

    private getIFrame(): HTMLElement {
        return this.iframeManagerService.getIframe()[0];
    }

    private getNotificationPanel(): JQuery<Element> {
        return this.$element.find('.se-notification-panel');
    }

    private calculateNotificationPanelBounds(): void {
        const notificationPanel: JQuery<Element> = this.getNotificationPanel();
        const notificationPanelPosition: JQuery.Coordinates = notificationPanel.position();

        this.notificationPanelBounds = {
            x: Math.floor(notificationPanelPosition.left),
            y: Math.floor(notificationPanelPosition.top),
            width: Math.floor(notificationPanel.width()),
            height: Math.floor(notificationPanel.height())
        };
    }

    private calculateIFrameNotificationPanelBounds(): void {
        const iFrame: HTMLElement = this.getIFrame();

        if (iFrame) {
            this.iFrameNotificationPanelBounds = {
                x: this.notificationPanelBounds.x - iFrame.offsetLeft,
                y: this.notificationPanelBounds.y - iFrame.offsetTop,
                width: this.notificationPanelBounds.width,
                height: this.notificationPanelBounds.height
            };
        }
    }

    private calculateBounds(): void {
        this.calculateNotificationPanelBounds();
        this.calculateIFrameNotificationPanelBounds();
    }

    private invalidateBounds(): void {
        this.notificationPanelBounds = null;
        this.iFrameNotificationPanelBounds = null;
    }

    private hasBounds(): boolean {
        const hasNotificationPanelBounds: boolean = !!this.notificationPanelBounds;
        const hasIFrameBounds: boolean = this.getIFrame()
            ? !!this.iFrameNotificationPanelBounds
            : true;

        return hasNotificationPanelBounds && hasIFrameBounds;
    }

    /*
     * This method stops mouse leave detection across frames and and invalidates the
     * notification panel bounds. If the area was hidden, it is forced to re-appear.
     */
    private cancelDetection(): void {
        this.invalidateBounds();
        this.notificationMouseLeaveDetectionService.stopDetection();

        if (this.isMouseOver) {
            this.onMouseLeave();
        }
    }

    private onResize(): void {
        this.cancelDetection();
    }

    /**
     * This method is called when notifications has been added or removed.
     *
     * If the panel is hidden, it will re-appear.
     *
     * If the panel is hidden and a mouse position is over the panel and then the notification suddenly appears,
     * it persists hidden. For example: pressing ESC key for the Hotkey notification.
     *
     */
    private onNotificationChanged(): void {
        // onNotificationChanged is triggered after onMouseEnter has already calculated the bounds,
        // so we prevent calling the cancelDetection method which will invalidate the bounds causing an error to be thrown.
        if (!this.isMouseOver) {
            this.cancelDetection();
        }
    }

    private addMouseMoveEventListener(): void {
        this.addMouseMoveEventListenerTimeout = null;

        this.notificationMouseLeaveDetectionService.startDetection(
            this.notificationPanelBounds,
            this.iFrameNotificationPanelBounds,
            () => this.onMouseLeave()
        );
    }
}
