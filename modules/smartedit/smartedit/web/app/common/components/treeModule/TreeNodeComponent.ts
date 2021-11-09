/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input } from '@angular/core';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { TreeDragAndDropService } from './TreeDragAndDropService';
import { ITreeNodeItem } from './types';

@Component({
    selector: 'se-tree-node',
    templateUrl: './TreeNodeComponent.html'
})
export class TreeNodeComponent<T, D> {
    @Input() source: ITreeNodeItem<T>[];

    constructor(private treeDragAndDropService: TreeDragAndDropService<T, D>) {}

    public get isDisabled(): boolean {
        return !this.treeDragAndDropService.isDragEnabled;
    }

    public onDrop(event: CdkDragDrop<ITreeNodeItem<T>[]>): void {
        this.treeDragAndDropService.handleDrop(event);
    }
}
