/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file

import { TypedMap } from '@smart/utils';
import { ITreeNodeItem, NavigationNodeItemDTO, TreeNodeItemDTO } from './types';
import { NavigationNodeEntry } from '../tree';

export class TreeNodeItem implements ITreeNodeItem<TreeNodeItem> {
    public hasChildren: boolean;
    public name: string;
    public parent: TreeNodeItem;
    public parentUid: string;
    public position: number = 0;
    public itemType: string;
    public uid: string;
    public uuid: string;
    public nodes?: TreeNodeItem[] = [];
    public initiated?: boolean;
    public mouseHovered?: boolean;
    public isExpanded?: boolean;
    public title?: TypedMap<string>;
    public level?: number;

    constructor(config: TreeNodeItemDTO) {
        this.hasChildren = config.hasChildren;
        this.name = config.name;
        this.title = config.title;
        this.parentUid = config.parentUid;
        this.position = config.position;
        this.itemType = config.itemType;
        this.uid = config.uid;
        this.uuid = config.uuid;
        this.level = config.level;
        this.nodes = [];
    }

    setPosition(position: number): TreeNodeItem {
        this.position = position;

        return this;
    }

    setMouseHovered(isHovered: boolean): TreeNodeItem {
        this.mouseHovered = isHovered;
        return this;
    }

    setInitiated(isInitiated: boolean): TreeNodeItem {
        this.initiated = isInitiated;

        return this;
    }

    setLevel(level: number): TreeNodeItem {
        this.level = level;

        return this;
    }

    addNode(node: TreeNodeItem): TreeNodeItem {
        this.nodes = [...this.nodes, node.setParent(this)].map((item: TreeNodeItem, idx: number) =>
            item.setPosition(idx).setLevel(this.level + 1)
        );
        this.updateHasChildren();

        return this;
    }

    addNodes(nodes: TreeNodeItem[]): TreeNodeItem {
        this.nodes = [
            ...this.nodes,
            ...nodes.map((node: TreeNodeItem) => node.setParent(this))
        ].map((item: TreeNodeItem, idx: number) => item.setPosition(idx).setLevel(this.level + 1));
        this.updateHasChildren();

        return this;
    }

    removeAllNodes(): TreeNodeItem {
        this.nodes = [];
        this.updateHasChildren();

        return this;
    }

    removeNode(node: TreeNodeItem): TreeNodeItem {
        this.nodes = this.nodes
            .filter((_node: TreeNodeItem) => _node.uid !== node.uid)
            .map((item: TreeNodeItem, idx: number) => item.setPosition(idx));
        this.updateHasChildren();

        return this;
    }

    setParent(node: TreeNodeItem) {
        this.parent = node;
        this.parentUid = node.uid;

        return this;
    }

    toggle(): TreeNodeItem {
        this.isExpanded = !this.isExpanded;

        return this;
    }

    collapse(): TreeNodeItem {
        this.isExpanded = false;

        return this;
    }

    expand(): TreeNodeItem {
        this.isExpanded = true;

        return this;
    }

    collapseAll(): void {
        this.nodes.forEach((node: TreeNodeItem) => this.collapseRecursively(node));
    }

    expandAll(): void {
        this.nodes.forEach((node: TreeNodeItem) => this.expandRecursively(node));
    }

    private collapseRecursively(_node: TreeNodeItem) {
        _node.collapse().nodes.forEach((node: TreeNodeItem) => this.collapseRecursively(node));
    }

    private expandRecursively(_node: TreeNodeItem) {
        _node.expand().nodes.forEach((node: TreeNodeItem) => this.expandRecursively(node));
    }

    private updateHasChildren(): void {
        this.hasChildren = !!this.nodes.length;
    }
}

export class NavigationNodeItem extends TreeNodeItem implements ITreeNodeItem<NavigationNodeItem> {
    id: string;
    nodes: NavigationNodeItem[];
    entries?: NavigationNodeEntry[];
    parent: NavigationNodeItem;

    constructor(config: NavigationNodeItemDTO) {
        super(config);

        this.entries = config.entries;
    }
}
