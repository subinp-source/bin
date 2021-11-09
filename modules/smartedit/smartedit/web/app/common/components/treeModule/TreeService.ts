/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IRestService, RestServiceFactory } from '@smart/utils';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

import { apiUtils } from '../../utils';
import { TreeNestedDataSource } from './TreeNestedDataSource';
import { ITreeNodeItem, TreeNodeItemDTO } from './types';
import { TreeNodeItemFactory } from './TreeNodeItemFactory';

/**
 * @ngdoc service
 * @name TreeModule.service:TreeService
 *
 * @description
 * A class to manage tree nodes through a REST API.
 */

@Injectable()
export class TreeService<T, D extends TreeNodeItemDTO> {
    public nodesRestService: IRestService<D | D[]>;
    public dataSource: TreeNestedDataSource<ITreeNodeItem<T>> = new TreeNestedDataSource();
    public root: ITreeNodeItem<T>;

    private $onTreeUpdated: BehaviorSubject<boolean> = new BehaviorSubject(false);

    constructor(
        private restServiceFactory: RestServiceFactory,
        private treeNodeItemFactory: TreeNodeItemFactory
    ) {}

    public onTreeUpdated(): Observable<boolean> {
        return this.$onTreeUpdated.pipe(filter((value) => !!value));
    }
    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#init
     * @methodOf TreeModule.service:TreeService
     * @param {string} nodeUri URI passed to {@link TreeModule.component:TreeComponent TreeComponent}
     * @param {string} rootNodeUid root uid passed to {@link TreeModule.component:TreeComponent TreeComponent}
     *
     * @description
     *
     * Initializes the REST service and sets root node
     */

    init(nodeUri: string, rootNodeUid: string): void {
        if (nodeUri) {
            this.nodesRestService = this.restServiceFactory.get(nodeUri);
        }

        this.setRoot(rootNodeUid);
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#rearrange
     * @methodOf TreeModule.service:TreeService
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem node} to be rearranged
     * @param {number} position New position of node
     *
     * @description
     *
     * Updates the position of the node within the tree
     */

    public rearrange(node: ITreeNodeItem<T>, parent: ITreeNodeItem<T>, position: number) {
        const siblings: ITreeNodeItem<T>[] = parent.nodes.filter(
            (_node: ITreeNodeItem<T>) => _node.uid !== node.uid
        );
        const rearranged: ITreeNodeItem<T>[] = [
            ...siblings.slice(0, position),
            node,
            ...siblings.slice(position, siblings.length)
        ];

        node.parent.removeNode(node);
        parent.removeAllNodes().addNodes(rearranged);
        this.update();
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#fetchChildren
     * @methodOf TreeModule.service:TreeService
     * @param {ITreeNodeItem} parent {@link TreeModule.object:ITreeNodeItem node}
     *
     * @description
     *
     * Fetches the node children and updates the tree
     *
     */

    async fetchChildren(_parent?: ITreeNodeItem<T>): Promise<ITreeNodeItem<T>[]> {
        const parent = _parent || this.root;

        if (parent.initiated) {
            this.update();

            return Promise.resolve(parent.nodes);
        } else {
            const response = await this.nodesRestService.get({ parentUid: parent.uid });
            const children = (apiUtils.getDataFromResponse(response) || []).map((dto: D) =>
                this.treeNodeItemFactory.get<T, D>(dto)
            );

            parent
                .removeAllNodes()
                .addNodes(children)
                .setInitiated(true);

            this.update();

            return children;
        }
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#toggle
     * @methodOf TreeModule.service:TreeService
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem node} to be rearranged
     *
     * @description
     *
     * Toggles the passed node and fetches children
     */

    async toggle(node: ITreeNodeItem<T>) {
        node.toggle();

        return this.fetchChildren(node);
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#newChild
     * @methodOf TreeModule.service:TreeService
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem node} to be rearranged
     *
     * @description
     *
     * Adds a new child to passed node
     */

    async newChild(node?: ITreeNodeItem<T>): Promise<void> {
        const nodeData = node || this.root;
        const response: D = await this.saveNode(nodeData);

        if (!nodeData.isExpanded) {
            this.toggle(nodeData);
        } else {
            const elm = nodeData.nodes.find(
                (_node: ITreeNodeItem<T>) => _node.uid === response.uid
            );

            if (!elm) {
                nodeData.addNode(this.treeNodeItemFactory.get<T, D>(response));
                this.update();
            }
        }
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#newSibling
     * @methodOf TreeModule.service:TreeService
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem node} to be rearranged
     *
     * @description
     *
     * Adds new sibling to passed node
     */

    async newSibling(node: ITreeNodeItem<T>): Promise<void> {
        const response: D = await this.saveNode(node.parent);

        node.parent.addNode(this.treeNodeItemFactory.get<T, D>(response));

        this.update();
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#remvoeNode
     * @methodOf TreeModule.service:TreeService
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem node} to be rearranged
     *
     * @description
     *
     * Removes passed node
     */

    async removeNode(node: ITreeNodeItem<T>): Promise<void> {
        await this.nodesRestService.remove({ identifier: node.uid });

        node.parent.removeNode(node);

        this.update();
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#update
     * @methodOf TreeModule.service:TreeService
     *
     * @description
     *
     * Updates the data source from where the nodes are retrieved
     */

    public update() {
        this.dataSource.set(null);
        this.dataSource.set(this.root.nodes);
        this.$onTreeUpdated.next(true);
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#expandAll
     * @methodOf TreeModule.service:TreeService
     *
     * @description
     *
     * Expands all nodes from the root node
     */

    public expandAll(): void {
        this.root.expandAll();
        this.update();
    }

    /**
     * @ngdoc method
     * @name TreeModule.service:TreeService#collapseAll
     * @methodOf TreeModule.service:TreeService
     *
     * @description
     *
     * Collapses all nodes from the root node
     */

    public collapseAll(): void {
        this.root.collapseAll();
        this.update();
    }

    public getNodePositionById(nodeUid: string): number {
        return this.getNodeById(nodeUid).position;
    }

    public getNodeById(nodeUid: string, nodeArray?: ITreeNodeItem<T>[]): ITreeNodeItem<T> {
        if (nodeUid === this.root.uid) {
            return this.root;
        }

        const nodes = nodeArray || this.root.nodes;

        for (const i in nodes) {
            if (nodes.hasOwnProperty(i)) {
                if (nodes[i].uid === nodeUid) {
                    return nodes[i];
                }
                if (nodes[i].hasChildren) {
                    nodes[i].nodes = nodes[i].nodes || [];
                    const result = this.getNodeById(nodeUid, nodes[i].nodes);
                    if (result) {
                        return result;
                    }
                }
            }
        }

        return null;
    }
    private async saveNode(_parent: ITreeNodeItem<T>): Promise<D> {
        const response: D = await this.nodesRestService.save({
            parentUid: _parent.uid,
            name: (_parent.name ? _parent.name : _parent.uid) + _parent.nodes.length
        });

        return response;
    }

    private setRoot(uid: string) {
        this.root = this.treeNodeItemFactory.get({ uid, name: 'root', level: 0 });
    }
}
