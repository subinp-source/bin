/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { IAlertService } from '@smart/utils';
import {
    ITreeDndOptions,
    TreeConfiguration,
    TreeDndOptionsCallbacks,
    TreeDragOptions,
    YTreeDndEvent
} from './types';

/* @internal */
export const TreeDndOptionFactory = (
    $timeout: angular.ITimeoutService,
    treeConfig: TreeConfiguration,
    confirmationModalService: any,
    $q: angular.IQService,
    alertService: IAlertService
) => {
    'ngInject';

    return class implements ITreeDndOptions {
        public dragEnabled: boolean;
        public dragDelay: number;
        public callbacks: TreeDndOptionsCallbacks;

        constructor(options?: TreeDragOptions) {
            this.dragEnabled = false;
            this.dragDelay = treeConfig.dragDelay;
            this.callbacks = {};

            if (!options) {
                return;
            }

            if (options.onDropCallback) {
                this.dragEnabled = true;
                this.callbacks.dropped = (event: any) => {
                    if (event.source === null || event.dest === null) {
                        return;
                    }
                    const dndEvent = new YTreeDndEvent(
                        event.source.nodeScope.$modelValue,
                        event.dest.nodesScope.$modelValue,
                        event.dest.index,
                        event.source.nodeScope.$parentNodeScope,
                        event.dest.nodesScope.$nodeScope
                    );
                    $timeout(function() {
                        options.onDropCallback(dndEvent);
                    });
                };
            }

            if (options.beforeDropCallback) {
                this.dragEnabled = true;
                this.callbacks.beforeDrop = (event: any) => {
                    if (event.source === null || event.dest === null) {
                        return true;
                    }
                    const dndEvent = new YTreeDndEvent(
                        event.source.nodeScope.$modelValue,
                        event.dest.nodesScope.$modelValue,
                        event.dest.index
                    );
                    const condition = options.beforeDropCallback(dndEvent);
                    return $q.when(condition).then((result: any) => {
                        if (typeof result === 'object') {
                            if (result.confirmDropI18nKey) {
                                const message = {
                                    description: result.confirmDropI18nKey
                                };
                                return confirmationModalService.confirm(message);
                            }
                            if (result.rejectDropI18nKey) {
                                alertService.showDanger({
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
                };
            }

            if (options.allowDropCallback) {
                this.dragEnabled = true;
                this.callbacks.accept = (
                    sourceNodeScope: any,
                    destNodesScope: any,
                    destIndex: number
                ) => {
                    const dndEvent = new YTreeDndEvent(
                        sourceNodeScope.$modelValue,
                        destNodesScope.$modelValue,
                        destIndex
                    );
                    return options.allowDropCallback(dndEvent);
                };
            }
        }
    };
};
