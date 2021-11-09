/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, EventEmitter, Input, Output, Type } from '@angular/core';

import { SeDowngradeComponent } from '../../di';
import { IDropdownMenuItem } from '../dropdown/dropdownMenu';
import {
    ITreeNodeItem,
    TreeDragAndDropEvent,
    TreeDragAndDropOptions,
    TreeNodeActions,
    TreeService
} from '../treeModule';
import {
    EditableListNodeItem,
    EditableListNodeItemDTO
} from 'smarteditcommons/components/list/EditableListNodeItem';
import { EditableListDefaultItem } from 'smarteditcommons/components/list/EditableListDefaultItem';

/**
 * The EditableListComponent component allows displaying a list of items. The list can be managed dynamically, by
 * adding, removing, and re-ordering it.
 *
 * @example
 * <se-editable-list
 *      [itemComponent]="myComponent"
 *      [(items)]="items"
 *      [onChange]="onChange"
 *      [editable]="editable"
 *      [(refresh)]="refresh"
 * ></se-editable-list>
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-editable-list',
    templateUrl: './EditableListComponent.html'
})
export class EditableListComponent {
    public dragOptions: TreeDragAndDropOptions<EditableListNodeItem> = {} as TreeDragAndDropOptions<
        EditableListNodeItem
    >;
    public actions: TreeNodeActions<EditableListNodeItem>;
    public rootId: string;

    @Input() public refresh: () => void;
    @Input() public items: EditableListNodeItemDTO[];
    @Input() public onChange: () => void;
    @Input() public itemTemplateUrl: string;
    @Input() public itemComponent: Type<any>;
    @Input() public editable: boolean;
    @Input() public id: string;

    @Output() refreshChange: EventEmitter<() => void> = new EventEmitter();
    @Output() itemsChange: EventEmitter<ITreeNodeItem<EditableListNodeItem>[]> = new EventEmitter();

    private _enableDragAndDrop: () => void;

    ngOnInit(): void {
        this._enableDragAndDrop = () => {
            this.dragOptions.allowDropCallback = (
                event: TreeDragAndDropEvent<EditableListNodeItem>
            ) => {
                // Just allow dropping elements of the same list.
                return event.sourceNode.parentUid === this.rootId;
            };
        };

        this.actions = this.getTreeActions();
        this.refreshChange.emit(() => this.actions.refreshList());

        if (!this.itemTemplateUrl && !this.itemComponent) {
            this.itemComponent = EditableListDefaultItem;
        }

        this.rootId = 'root' + this.id;

        if (this.editable === undefined) {
            this.editable = true;
        }

        if (this.editable === true) {
            this._enableDragAndDrop();
        }
    }

    public handleTreeUpdated(items: ITreeNodeItem<EditableListNodeItem>[]): void {
        if (items && items.length) {
            this.itemsChange.emit(items);
            this.actions.performUpdate();
        }
    }

    public getDropdownItems(): IDropdownMenuItem[] {
        return [
            {
                key: 'se.ydropdownmenu.remove',
                callback: (handle: EditableListNodeItem) => {
                    this.actions.removeItem(handle);
                }
            },
            {
                key: 'se.ydropdownmenu.move.up',
                condition: (handle: EditableListNodeItem): boolean => {
                    return this.actions.isMoveUpAllowed(handle);
                },
                callback: (handle: EditableListNodeItem) => {
                    this.actions.moveUp(handle);
                }
            },
            {
                key: 'se.ydropdownmenu.move.down',
                condition: (handle: EditableListNodeItem): boolean => {
                    return this.actions.isMoveDownAllowed(handle);
                },
                callback: (handle: EditableListNodeItem) => {
                    this.actions.moveDown(handle);
                }
            }
        ];
    }
    private getTreeActions(): TreeNodeActions<EditableListNodeItem> {
        const items = this.getDropdownItems();

        return {
            fetchData: (
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>,
                nodeData: EditableListNodeItem
            ) => {
                const nodeItems = this.items.map(
                    (dto: EditableListNodeItemDTO) => new EditableListNodeItem(dto)
                );

                nodeItems.forEach((item: EditableListNodeItem) => {
                    if (item.id && !item.uid) {
                        item.uid = item.id;
                    }

                    item.setParent(nodeData);
                });

                nodeData.removeAllNodes().addNodes(nodeItems);

                treeService.update();

                return Promise.resolve(nodeData);
            },
            getDropdownItems: () => items,
            removeItem(
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>,
                nodeData: EditableListNodeItem
            ) {
                nodeData.parent.removeNode(nodeData);
                treeService.update();
            },
            moveUp(
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>,
                nodeData: EditableListNodeItem
            ) {
                treeService.rearrange(
                    nodeData as ITreeNodeItem<EditableListNodeItem>,
                    treeService.root as ITreeNodeItem<EditableListNodeItem>,
                    nodeData.position - 1
                );
            },
            moveDown(
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>,
                nodeData: EditableListNodeItem
            ) {
                treeService.rearrange(
                    nodeData as ITreeNodeItem<EditableListNodeItem>,
                    treeService.root as ITreeNodeItem<EditableListNodeItem>,
                    nodeData.position + 1
                );
            },

            isMoveUpAllowed(
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>,
                nodeData: EditableListNodeItem
            ): boolean {
                return nodeData.position > 0;
            },

            isMoveDownAllowed(
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>,
                nodeData: EditableListNodeItem
            ): boolean {
                return treeService.root.nodes.length !== nodeData.position + 1;
            },

            performUpdate: (
                treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>
            ) => {
                if (this.onChange) {
                    this.onChange();
                }
            },

            refreshList(treeService: TreeService<EditableListNodeItem, EditableListNodeItemDTO>) {
                this.fetchData(treeService.root);
            }
        };
    }
}
