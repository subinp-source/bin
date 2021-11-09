/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { TypedMap } from '@smart/utils';

import { SeDowngradeService } from '../../di';
import { IAlertService, IConfirmationModalService } from '../../services/interfaces';
import { ITreeNodeItem, TreeDragAndDropEvent, TreeDragAndDropOptions } from './types';
import { TreeService } from './TreeService';

@SeDowngradeService()
export class TreeDragAndDropService<T, D> {
    public isDragEnabled: boolean;

    private config: TreeDragAndDropOptions<T>;

    constructor(
        private confirmationModalService: IConfirmationModalService,
        private alertService: IAlertService,
        private treeService: TreeService<T, D>
    ) {}

    public init(options: TreeDragAndDropOptions<T>) {
        this.config = options;
        this.isDragEnabled =
            !!options &&
            (!!options.onDropCallback ||
                !!options.beforeDropCallback ||
                !!options.allowDropCallback);
    }

    public handleDrop(event: CdkDragDrop<ITreeNodeItem<T>[]>): Promise<void> {
        if (!this.config || !event.isPointerOverContainer) {
            return Promise.resolve();
        }

        if (!this.allowDrop(event)) {
            return Promise.resolve();
        }

        return this.beforeDrop(event).then((result: boolean) => {
            if (result === false) {
                return;
            }

            this.onDrop(event);
            this.rearrangeNodes(event);
        });
    }

    private rearrangeNodes(event: CdkDragDrop<ITreeNodeItem<T>[]>) {
        this.treeService.rearrange(
            event.item.data,
            event.container.data[0].parent,
            event.currentIndex
        );
    }
    private onDrop(event: CdkDragDrop<ITreeNodeItem<T>[]>) {
        if (!this.config.onDropCallback) {
            return;
        }

        const dndEvent = new TreeDragAndDropEvent(
            event.item.data,
            event.container.data,
            event.item.data.parent,
            event.container.data[0].parent,
            event.currentIndex
        );

        return this.config.onDropCallback(dndEvent);
    }
    private allowDrop(event: CdkDragDrop<ITreeNodeItem<T>[]>): boolean {
        if (!this.config.allowDropCallback) {
            return true;
        }

        return this.config.allowDropCallback(
            new TreeDragAndDropEvent(
                event.item.data,
                event.container.data,
                event.item.data.parent,
                event.container.data[0].parent,
                event.currentIndex
            )
        );
    }

    private beforeDrop(event: CdkDragDrop<ITreeNodeItem<T>[]>): Promise<any> {
        if (!this.config.beforeDropCallback) {
            return Promise.resolve();
        }

        const dndEvent = new TreeDragAndDropEvent(
            event.item.data,
            event.container.data,
            event.item.data.parent,
            event.container.data[0].parent,
            event.currentIndex
        );

        return this.config
            .beforeDropCallback(dndEvent)
            .then((result: TypedMap<string> | boolean) => {
                if (typeof result === 'object') {
                    if (result.confirmDropI18nKey) {
                        const message = {
                            description: result.confirmDropI18nKey
                        };
                        return this.confirmationModalService.confirm(message);
                    }

                    if (result.rejectDropI18nKey) {
                        this.alertService.showDanger({
                            message: result.rejectDropI18nKey
                        });
                        return false;
                    }

                    throw new Error(
                        'Unexpected return value for beforeDropCallback does not contain confirmDropI18nKey nor rejectDropI18nKey: ' +
                            result
                    );
                }
                return result;
            });
    }
}
