/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from '../../../di';
import {
    ITreeService,
    NavigationNode,
    TreeActions,
    TreeDragOptions,
    TreeNgModel,
    TreeNode,
    YTreeDndEvent
} from '../../tree';
import { IDropdownMenuItem } from '../../dropdown/dropdownMenu/IDropdownMenuItem';

/**
 * @ngdoc directive
 * @name yEditableListModule.directive:yEditableList
 * @scope
 * @restrict E
 * @element y-editable-list
 * @deprecated since 2005
 * @description
 * Deprecated, use se-editable-list
 * The yEditableList component allows displaying a list of items. The list can be managed dynamically, by
 * adding, removing, and re-ordering it.
 * @param {@String} id A string used to track and identify the component.
 * @param {<Array} items The collection of items to display in the component.
 * @param {=Function=} refresh A function that can be called to update the content of the list.
 * @param {<Function} onChange A function that will be called whenever the content of the list changes.
 * @param {<String=} itemTemplateUrl The path to the template to display each of the items in the list.
 * @param {<boolean=} editable The property specifies whether the content of the list can be modified.
 */

@SeComponent({
    selector: 'y-editable-list',
    templateUrl: 'yEditableListTemplate.html',
    inputs: ['id:@', 'items', 'refresh:=', 'onChange', 'itemTemplateUrl', 'editable']
})
export class YEditableListComponent {
    public dragOptions: TreeDragOptions = {} as TreeDragOptions;
    public actions: TreeActions;
    public onChange: () => void;
    public itemTemplateUrl: string;
    public editable: boolean;
    public refresh: () => void;
    public items: NavigationNode[];
    public rootId: string;
    public id: string;

    private _enableDragAndDrop: () => void;

    $onInit(): void {
        this._enableDragAndDrop = () => {
            this.dragOptions.allowDropCallback = (event: YTreeDndEvent) => {
                // Just allow dropping elements of the same list.
                return event.sourceNode.parentUid === this.rootId;
            };

            this.dragOptions.onDropCallback = () => {
                this.actions.performUpdate();
            };
        };

        this.actions = this.getTreeActions();
        this.refresh = () => {
            this.actions.refreshList();
        };

        if (!this.itemTemplateUrl) {
            this.itemTemplateUrl = 'yEditableListDefaultItemTemplate.html';
        }

        this.rootId = 'root' + this.id;

        if (this.editable === undefined) {
            this.editable = true;
        }

        if (this.editable === true) {
            this._enableDragAndDrop();
        }
    }

    private getTreeActions(): TreeActions {
        const items = this.getDropdownItems();

        return {
            fetchData: (treeService: ITreeService, nodeData: TreeNode) => {
                this.items.map((item) => {
                    if (item.id && !item.uid) {
                        item.uid = item.id;
                    }

                    item.parentUid = this.rootId;
                });
                nodeData.nodes = this.items;

                return Promise.resolve(nodeData);
            },
            getDropdownItems: () => items,
            removeItem(treeService: ITreeService, handle: TreeNgModel) {
                const nodeData = handle.$modelValue;
                const pos = this.root.nodes.indexOf(nodeData);

                this.root.nodes.splice(pos, 1);

                this.performUpdate(parent, handle);
            },
            moveUp(treeService: ITreeService, handle: TreeNgModel) {
                const nodeData = handle.$modelValue;
                const pos = this.root.nodes.indexOf(nodeData);
                const upperEntry = this.root.nodes[pos - 1];

                this.root.nodes.splice(pos - 1, 2, nodeData, upperEntry);

                this.performUpdate(parent, handle);
            },
            moveDown(treeService: ITreeService, handle: TreeNgModel) {
                const nodeData = handle.$modelValue;
                const pos = this.root.nodes.indexOf(nodeData);
                const lowerEntry = this.root.nodes[pos + 1];

                this.root.nodes.splice(pos, 2, lowerEntry, nodeData);

                this.performUpdate(parent, handle);
            },

            isMoveUpAllowed(treeService: ITreeService, handle: TreeNgModel) {
                const nodeData = handle.$modelValue;
                return this.root.nodes.indexOf(nodeData) > 0;
            },

            isMoveDownAllowed(treeService: ITreeService, handle: TreeNgModel) {
                const nodeData = handle.$modelValue;
                const entriesArrayLength = this.root.nodes.length;

                return this.root.nodes.indexOf(nodeData) !== entriesArrayLength - 1;
            },

            performUpdate: () => {
                if (this.onChange) {
                    this.onChange();
                }
            },

            refreshList() {
                this.fetchData(this.root);
            }
        } as TreeActions;
    }
    private getDropdownItems(): IDropdownMenuItem[] {
        return [
            {
                key: 'se.ydropdownmenu.remove',
                callback: (handle: TreeNgModel) => {
                    this.actions.removeItem(handle);
                }
            },
            {
                key: 'se.ydropdownmenu.move.up',
                condition: (handle: TreeNgModel) => {
                    return this.actions.isMoveUpAllowed(handle);
                },
                callback: (handle: TreeNgModel) => {
                    this.actions.moveUp(handle);
                }
            },
            {
                key: 'se.ydropdownmenu.move.down',
                condition: (handle: TreeNgModel) => {
                    return this.actions.isMoveDownAllowed(handle);
                },
                callback: (handle: TreeNgModel) => {
                    this.actions.moveDown(handle);
                }
            }
        ];
    }
}
