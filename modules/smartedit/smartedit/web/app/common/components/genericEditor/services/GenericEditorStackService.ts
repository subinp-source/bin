/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { Injectable } from '@angular/core';
import { LogService, TypedMap } from '@smart/utils';
import { SystemEventService } from '../../../services/SystemEventService';
import { GenericEditorInfo } from '../types';
import { SeDowngradeService } from '../../../di/SeDowngradeService';

@SeDowngradeService()
@Injectable()
export class GenericEditorStackService {
    static EDITOR_PUSH_TO_STACK_EVENT = 'EDITOR_PUSH_TO_STACK_EVENT';
    static EDITOR_POP_FROM_STACK_EVENT = 'EDITOR_POP_FROM_STACK_EVENT';

    private _editorsStacks: TypedMap<GenericEditorInfo[]>;

    constructor(private systemEventService: SystemEventService, private logService: LogService) {
        this._editorsStacks = {};

        this.systemEventService.subscribe(
            GenericEditorStackService.EDITOR_PUSH_TO_STACK_EVENT,
            this.pushEditorEventHandler.bind(this)
        );
        this.systemEventService.subscribe(
            GenericEditorStackService.EDITOR_POP_FROM_STACK_EVENT,
            this.popEditorEventHandler.bind(this)
        );
    }

    // --------------------------------------------------------------------------------------
    // API
    // --------------------------------------------------------------------------------------
    isAnyGenericEditorOpened(): boolean {
        return lodash.size(this._editorsStacks) >= 1;
    }

    areMultipleGenericEditorsOpened(): boolean {
        return (
            lodash.size(this._editorsStacks) > 1 ||
            lodash.some(this._editorsStacks, (stack) => {
                return stack.length > 1;
            })
        );
    }

    getEditorsStack(editorStackId: string): GenericEditorInfo[] {
        return this._editorsStacks[editorStackId] || null;
    }

    isTopEditorInStack(editorStackId: string, editorId: string): boolean {
        let result = false;
        const stack = this._editorsStacks[editorStackId];
        if (stack) {
            const topEditor = stack[stack.length - 1];
            result = topEditor && topEditor.editorId === editorId;
        }

        return result;
    }

    // --------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------
    private pushEditorEventHandler(eventId: string, editorToPushInfo: GenericEditorInfo): void {
        this.validateId(editorToPushInfo);

        const stackId = editorToPushInfo.editorStackId;
        if (!this._editorsStacks[stackId]) {
            this._editorsStacks[stackId] = [];
        }

        this._editorsStacks[stackId].push({
            component: editorToPushInfo.component,
            componentType: editorToPushInfo.componentType,
            editorId: editorToPushInfo.editorId
        });
    }

    private popEditorEventHandler(eventId: string, editorToPopInfo: GenericEditorInfo): void {
        this.validateId(editorToPopInfo);

        const stackId = editorToPopInfo.editorStackId;
        const stack = this._editorsStacks[stackId];
        if (!stack) {
            this.logService.warn(
                'genericEditorStackService - Stack of editors not found. Cannot pop editor.'
            );
            return;
        }

        stack.pop();
        if (stack.length === 0) {
            delete this._editorsStacks[stackId];
        }
    }

    private validateId(editorInfo: GenericEditorInfo): void {
        if (!editorInfo.editorStackId) {
            throw new Error('genericEditorStackService - Must provide a stack id.');
        }
    }
}
