/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import * as lo from 'lodash';
import { stringUtils, TypedMap } from '@smart/utils';

import { DragAndDropScrollingService } from './DragAndDropScrollingService';
import { InViewElementObserver } from './InViewElementObserver';
import { SystemEventService } from '../SystemEventService';
import { SeDowngradeService } from '../../di/SeDowngradeService';
import { YJQUERY_TOKEN } from '../vendors/YjqueryModule';
import { IDragAndDropCrossOrigin } from '../interfaces/IDragAndDropCrossOrigin';
import {
    OVERLAY_COMPONENT_CLASS,
    SMARTEDIT_DRAG_AND_DROP_EVENTS
} from '../../utils/smarteditconstants';

export interface DragAndDropConfiguration {
    id: string;
    sourceSelector: string | string[];
    targetSelector: string;
    startCallback: (event: Event) => void;
    dragEnterCallback?: (event: Event) => void;
    dragOverCallback?: (event: Event) => void;
    dropCallback?: (event: Event) => void;
    outCallback?: (event: Event) => void;
    stopCallback: (event: Event) => void;
    helper?: () => string;
    enableScrolling: boolean;

    /* @Internal */
    _cachedDragImage?: HTMLImageElement;
}

@SeDowngradeService()
export class DragAndDropService {
    private static readonly DRAGGABLE_ATTR = 'draggable';
    private static readonly DROPPABLE_ATTR = 'data-droppable';

    private configurations: TypedMap<DragAndDropConfiguration> = {};
    private isDragAndDropExecuting = false;

    constructor(
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private dragAndDropScrollingService: DragAndDropScrollingService,
        private inViewElementObserver: InViewElementObserver,
        private systemEventService: SystemEventService,
        private dragAndDropCrossOrigin: IDragAndDropCrossOrigin
    ) {
        this.dragAndDropCrossOrigin.initialize();
    }

    /**
     * This method registers a new instance of the drag and drop service.
     * Note: Registering doesn't start the service. It just provides the configuration, which later must be applied with the apply method.
     *
     */
    register(configuration: DragAndDropConfiguration): void {
        // Validate
        if (!configuration || !configuration.id) {
            throw new Error('dragAndDropService - register(): Configuration needs an ID.');
        }

        this.configurations[configuration.id] = configuration;

        if (!stringUtils.isBlank(configuration.targetSelector)) {
            this.inViewElementObserver.addSelector(configuration.targetSelector);
        }
    }

    /**
     * This method removes the drag and drop instances specified by the provided IDs.
     *
     */
    unregister(configurationsIDList: string[]): void {
        configurationsIDList.forEach((configurationID: string) => {
            const configuration = this.configurations[configurationID];
            if (configuration) {
                this.deactivateConfiguration(configuration);
                this.deactivateScrolling(configuration);
                delete this.configurations[configurationID];
            }
        });
    }

    /**
     * This method applies all drag and drop configurations registered.
     *
     */
    applyAll(): void {
        lo.forEach(this.configurations, (currentConfig: DragAndDropConfiguration) => {
            this.apply(currentConfig.id);
        });
    }

    /**
     * This method apply the configuration specified by the provided ID in the current page. After this method is executed drag and drop can be started by the user.
     *
     */
    apply(configurationID: string): void {
        const configuration = this.configurations[configurationID];
        if (configuration) {
            this.update(configuration.id);
            this.cacheDragImages(configuration);
            this.initializeScrolling(configuration);
        }
    }

    /**
     * This method updates the drag and drop instance specified by the provided ID in the current page. It is important to execute this method every time a draggable or droppable element
     * is added or removed from the page DOM.
     *
     */
    update(configurationID: string): void {
        const configuration = this.configurations[configurationID];
        if (configuration) {
            this.deactivateConfiguration(configuration);
            this._update(configuration);
        }
    }

    /**
     * This method forces the page to prepare for a drag and drop operation. This method is necessary when the drag and drop operation is started somewhere else,
     * like on a different iFrame.
     *
     */
    markDragStarted(): void {
        this.setDragAndDropExecutionStatus(true);
        this.dragAndDropScrollingService.enable();
    }

    // Method used to stop drag and drop from another frame.
    /**
     * This method forces the page to clean after a drag and drop operation. This method is necessary when the drag and drop operation is stopped somewhere else,
     * like on a different iFrame.
     *
     */
    markDragStopped(): void {
        this.setDragAndDropExecutionStatus(false);
        this.dragAndDropScrollingService.disable();
    }

    private _update(configuration: DragAndDropConfiguration): void {
        const sourceSelectors = lo.isArray(configuration.sourceSelector)
            ? configuration.sourceSelector
            : [configuration.sourceSelector];

        sourceSelectors.forEach((sourceSelector: string) => {
            const draggableElements = this.getSelector(sourceSelector + ':not([draggable])');
            draggableElements.attr(DragAndDropService.DRAGGABLE_ATTR, 'true');

            draggableElements.on('dragstart', this.onDragStart.bind(this, configuration));
            draggableElements.on('dragend', this.onDragEnd.bind(this, configuration));
        });

        const droppableElements = this.getSelector(
            configuration.targetSelector + ':not([draggable])'
        );
        droppableElements.attr(DragAndDropService.DROPPABLE_ATTR, 'true'); // Not needed by HTML5. It's to mark element as processed.

        droppableElements.on('dragenter', this.onDragEnter.bind(this, configuration));
        droppableElements.on('dragover', this.onDragOver.bind(this, configuration));
        droppableElements.on('drop', this.onDrop.bind(this, configuration));
        droppableElements.on('dragleave', this.onDragLeave.bind(this, configuration));
    }

    private deactivateConfiguration(configuration: DragAndDropConfiguration): void {
        const sourceSelectors = Array.isArray(configuration.sourceSelector)
            ? configuration.sourceSelector.join(',')
            : configuration.sourceSelector;
        const draggableElements = this.getSelector(sourceSelectors);
        const droppableElements = this.getSelector(configuration.targetSelector);

        draggableElements.removeAttr(DragAndDropService.DRAGGABLE_ATTR);
        droppableElements.removeAttr(DragAndDropService.DROPPABLE_ATTR);

        draggableElements.off('dragstart');
        draggableElements.off('dragend');

        droppableElements.off('dragenter');
        droppableElements.off('dragover');
        droppableElements.off('dragleave');
        droppableElements.off('drop');
    }

    // Draggable Listeners
    private onDragStart(
        configuration: DragAndDropConfiguration,
        yjQueryEvent: JQueryEventObject
    ): void {
        // The native transferData object is modified outside the setTimeout since it can only be modified
        // inside the dragStart event handler (otherwise an exception is thrown by the browser).
        const evt = yjQueryEvent.originalEvent as DragEvent;
        this.setDragTransferData(configuration, evt);

        // Necessary because there's a bug in Chrome (and probably Safari) where dragEnd is triggered right after
        // dragStart whenever DOM is modified in the event handler. The problem can be circumvented by using setTimeout.
        setTimeout(() => {
            const component = this.yjQuery(yjQueryEvent.target).closest(
                '.' + OVERLAY_COMPONENT_CLASS
            );

            this.setDragAndDropExecutionStatus(true, component);

            this.dragAndDropScrollingService.enable();

            if (configuration.startCallback) {
                configuration.startCallback(evt);
            }
        }, 0);
    }

    private onDragEnd(
        configuration: DragAndDropConfiguration,
        yjQueryEvent: JQueryEventObject
    ): void {
        const evt = yjQueryEvent.originalEvent as DragEvent;

        this.dragAndDropScrollingService.disable();

        if (this.isDragAndDropExecuting && configuration.stopCallback) {
            configuration.stopCallback(evt);
        }

        this.setDragAndDropExecutionStatus(false);
    }

    // Droppable Listeners
    private onDragEnter(
        configuration: DragAndDropConfiguration,
        yjQueryEvent: JQueryEventObject
    ): void {
        const evt = yjQueryEvent.originalEvent as DragEvent;
        evt.preventDefault();

        if (this.isDragAndDropExecuting && configuration.dragEnterCallback) {
            configuration.dragEnterCallback(evt);
        }
    }

    private onDragOver(
        configuration: DragAndDropConfiguration,
        yjQueryEvent: JQueryEventObject
    ): void {
        const evt = yjQueryEvent.originalEvent as DragEvent;
        evt.preventDefault();

        if (this.isDragAndDropExecuting && configuration.dragOverCallback) {
            configuration.dragOverCallback(evt);
        }
    }

    private onDrop(
        configuration: DragAndDropConfiguration,
        yjQueryEvent: JQueryEventObject
    ): boolean {
        const evt = yjQueryEvent.originalEvent as DragEvent;
        evt.preventDefault(); // Necessary to receive the on drop event. Otherwise, other handlers are executed.
        evt.stopPropagation();

        if (evt.relatedTarget && (evt.relatedTarget as Node).nodeType === 3) {
            return true;
        }
        if (evt.target === evt.relatedTarget) {
            return true;
        }

        if (this.isDragAndDropExecuting && configuration.dropCallback) {
            configuration.dropCallback(evt);
        }

        return false;
    }

    private onDragLeave(
        configuration: DragAndDropConfiguration,
        yjQueryEvent: JQueryEventObject
    ): void {
        const evt = yjQueryEvent.originalEvent;
        evt.preventDefault();

        if (this.isDragAndDropExecuting && configuration.outCallback) {
            configuration.outCallback(evt);
        }
    }

    // Helper Functions
    private cacheDragImages(configuration: DragAndDropConfiguration): void {
        let helperImg: string;
        if (configuration.helper) {
            helperImg = configuration.helper();
        }

        if (!helperImg) {
            return;
        }

        if (typeof helperImg === 'string') {
            configuration._cachedDragImage = new Image();
            configuration._cachedDragImage.src = helperImg;
        } else {
            configuration._cachedDragImage = helperImg;
        }
    }

    private setDragTransferData(configuration: DragAndDropConfiguration, evt: DragEvent): void {
        /*
            Note: Firefox recently added some restrictions to their drag and drop functionality; it only
            allows starting drag and drop operations if there's data present in the dataTransfer object.
            Otherwise, the whole operation fails silently. Thus, some data needs to be added.
        */
        evt.dataTransfer.setData('Text', configuration.id);

        if (configuration._cachedDragImage && evt.dataTransfer.setDragImage) {
            evt.dataTransfer.setDragImage(configuration._cachedDragImage, 0, 0);
        }
    }

    private getSelector(selector: HTMLElement | string): JQuery<HTMLElement> {
        return this.yjQuery(selector);
    }

    private setDragAndDropExecutionStatus(isExecuting: boolean, element?: JQuery<Element>): void {
        this.isDragAndDropExecuting = isExecuting;
        this.systemEventService.publish(
            isExecuting
                ? SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START
                : SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END,
            element
        );
    }

    private initializeScrolling(configuration: DragAndDropConfiguration): void {
        if (configuration.enableScrolling && this.browserRequiresCustomScrolling()) {
            this.dragAndDropScrollingService.initialize();
        }
    }

    private deactivateScrolling(configuration: DragAndDropConfiguration): void {
        if (configuration.enableScrolling && this.browserRequiresCustomScrolling()) {
            this.dragAndDropScrollingService.deactivate();
        }
    }

    private browserRequiresCustomScrolling(): boolean {
        // NOTE: It'd be better to identify if native scrolling while dragging is enabled in the browser, but
        // currently there's no way to know. Thus, browser fixing is necessary.

        return true;
    }
}
