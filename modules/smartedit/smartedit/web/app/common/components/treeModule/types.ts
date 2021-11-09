/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from '@smart/utils';
import { NavigationNodeEntry } from '../tree';
import { InjectionToken } from '@angular/core';
import { IDropdownMenuItem } from 'smarteditcommons/components';

export interface TreeNodeItemDTO {
    hasChildren?: boolean;
    name?: string;
    parentUid?: string;
    position?: number;
    itemType?: string;
    uid?: string;
    uuid?: string;
    title?: TypedMap<string>;
    level?: number;
}

export interface NavigationNodeItemDTO extends TreeNodeItemDTO {
    id: string;
    entries?: NavigationNodeEntry[];
}

/**
 * @ngdoc object
 * @name TreeModule.object:ITreeNodeItem
 * @description
 * An object representing tree nodes consumed by the {@link TreeModule.component:TreeComponent TreeComponent}
 */

export interface ITreeNodeItem<T> {
    /**
     * @ngdoc property
     * @name hasChildren
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Carries information whether a node has descendant nodes
     */

    hasChildren: boolean;

    /**
     * @ngdoc property
     * @name name
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * A name of the node
     */

    name: string;

    /**
     * @ngdoc property
     * @name parent
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Carries a reference to parent node
     */

    parent: ITreeNodeItem<T>;
    /**
     * @ngdoc property
     * @name parentUid
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Carries information about parent string uid
     */

    parentUid: string;

    /**
     * @ngdoc property
     * @name position
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Holds number value of node position in the tree
     */

    position: number;

    /**
     * @ngdoc property
     * @name itemType
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Type of the node, based on that value different node class may be used from {@link TreeModule.service:TreeNodeItemFactory TreeNodeItemFactory}.
     * You can override the default factory to use your custom classes.
     */

    itemType: string;

    /**
     * @ngdoc property
     * @name uid
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Uid of the node
     */

    uid: string;

    /**
     * @ngdoc property
     * @name uuid
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Uuid of the nodes
     */

    uuid: string;
    /**
     * @ngdoc property
     * @name nodes
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Array of node descendants
     */

    nodes?: ITreeNodeItem<T>[];

    /**
     * @ngdoc property
     * @name initiated
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Flags whether the node is initiated
     */

    initiated?: boolean;
    /**
     * @ngdoc property
     * @name mouseHovered
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Flags whether the UI instance of the node is being mouse hovered
     */

    mouseHovered?: boolean;

    /**
     * @ngdoc property
     * @name isExpanded
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Flags whether the node is expanded revealing it's descendant nodes
     */

    isExpanded?: boolean;

    /**
     * @ngdoc property
     * @name title
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Map of localized titles
     */

    title?: TypedMap<string>;

    /**
     * @ngdoc property
     * @name level
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * A nesting level indicator in which node resides.
     */

    level?: number;

    /**
     * @ngdoc method
     * @name setInitiated
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Sets node initiated property
     *
     * @param {boolean} isInitiated
     */

    setInitiated(isInitiated: boolean): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name addNode
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @param {ITreeNodeItem} node
     * @description
     * Adds single node to nodes array
     */

    addNode(node: ITreeNodeItem<T>): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name addNodes
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @param {ITreeNodeItem[]} nodes
     * @description
     * Adds multiple nodes to nodes array
     */

    addNodes(nodes: ITreeNodeItem<T>[]): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name removeAllNodes
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Clears the node array
     */

    removeAllNodes(): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name removeNode
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @param {ITreeNodeItem} node
     * @description
     * Removes single node from nodes array
     */

    removeNode(node: ITreeNodeItem<T>): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name setParent
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @param {ITreeNodeItem} node
     * @description
     * Sets the immediate parent of the node element
     */

    setParent(node: ITreeNodeItem<T>): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name toggle
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Toggles the node to expand/collapse children
     */

    toggle(): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name collapse
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Collapses the tree hiding children
     */

    collapse(): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name expand
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Expands the tree revealing all children
     */

    expand(): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name setMouseHovered
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @param {boolean} isHovered
     * @description
     * sets mouseHovered flag
     */

    setMouseHovered(isHovered: boolean): ITreeNodeItem<T>;

    /**
     * @ngdoc method
     * @name collapseAll
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Collapses all descendant children
     */

    collapseAll(): void;

    /**
     * @ngdoc method
     * @name expandAll
     * @propertyOf TreeModule.object:ITreeNodeItem
     *
     * @description
     * Expands all descendant children
     */

    expandAll(): void;
}

/**
 * @ngdoc object
 * @name TreeModule.object:TreeDragAndDropOptions
 * @description
 * An object holding callbacks related to nodes drag and drop functionality in the {@link TreeModule.component:TreeComponent TreeComponent}.
 * Each callback exposes the {@link TreeModule.object:TreeDragAndDropEvent TreeDragAndDropEvent}
 */

export interface TreeDragAndDropOptions<T> {
    /**
     * @ngdoc property
     * @name onDropCallback
     * @propertyOf TreeModule.object:TreeDragAndDropOptions
     * @description
     * Callback function executed after the node is dropped.
     */

    onDropCallback: (event: TreeDragAndDropEvent<T>) => void;
    /**
     * @ngdoc property
     * @name beforeDropCallback
     * @propertyOf TreeModule.object:TreeDragAndDropOptions
     * @description
     * Callback function executed before drop. Return true allows drop, false rejects, and an object {confirmDropI18nKey: 'key'} opens a confirmation modal.
     */
    beforeDropCallback: (event: TreeDragAndDropEvent<T>) => Promise<any>;
    /**
     * @ngdoc property
     * @name acceptDropCallback
     * @propertyOf TreeModule.object:TreeDragAndDropOptions
     * @description
     * Callback function executed when hovering over droppable slots, return true to allow, return false to block.
     */
    acceptDropCallback?: (event: TreeDragAndDropEvent<T>) => boolean;
    allowDropCallback: (event: TreeDragAndDropEvent<T>) => boolean;
}

/**
 * @ngdoc object
 * @name TreeModule.object:TreeDragAndDropEvent
 * @description
 * Class representing the event triggered when dragging and dropping nodes in the {@link TreeModule.component:TreeComponent TreeComponent}.
 *
 * @param {Object} sourceNode is the {@link TreeModule.object:ITreeNodeItem ITreeNodeItem} that is being dragged.
 * @param {Object} destinationNodes is the set of the destination's parent's children {@link TreeModule.object:ITreeNodeItem ITreeNodeItem}.
 * @param {Object} sourceParentNode parent {@link TreeModule.object:ITreeNodeItem ITreeNodeItem} of the dragged node
 * @param {Object} destinationParentNode parent {@link TreeModule.object:ITreeNodeItem ITreeNodeItem} of the destination
 * @param {Number} position is the index at which the {@link TreeModule.object:ITreeNodeItem ITreeNodeItem} was dropped.
 */

export class TreeDragAndDropEvent<T> {
    constructor(
        public sourceNode: T,
        public destinationNodes: T[],
        public sourceParentNode: T,
        public destinationParentNode: T,
        public position: number
    ) {}
}

export const TREE_NODE = new InjectionToken('TREE_NODE');

export interface TreeNodeActions<T> {
    getDropdownItems?(): IDropdownMenuItem[];
    fetchData?(...args: any[]): Promise<T>;
    removeItem?(...args: any[]): void;
    moveUp?(...args: any[]): void;
    moveDown?(...args: any[]): void;
    isMoveUpAllowed?(...args: any[]): boolean;
    isMoveDownAllowed?(...args: any[]): boolean;
    performUpdate?(...args: any[]): void;
    refreshList?(...args: any[]): void;
}
