/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';

import { ITreeNodeItem, TREE_NODE } from '../treeModule';
import { EditableListNodeItem } from './EditableListNodeItem';
import { EditableListComponent } from './EditableListComponent';

@Component({
    selector: 'se-editable-list-default-item',
    template: `
        <div>
            <span>{{ node.uid }}</span>
            <se-dropdown-menu
                *ngIf="parent.editable"
                [dropdownItems]="parent.getDropdownItems()"
                [selectedItem]="node"
                class="pull-right se-tree-node__actions--more-menu"
            ></se-dropdown-menu>
        </div>
    `
})
export class EditableListDefaultItem {
    constructor(
        @Inject(forwardRef(() => EditableListComponent)) public parent: EditableListComponent,
        @Inject(TREE_NODE) public node: ITreeNodeItem<EditableListNodeItem>
    ) {}
}
