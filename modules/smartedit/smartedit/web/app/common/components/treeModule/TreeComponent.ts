/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, EventEmitter, Input, Output, Type } from '@angular/core';
import { TypedMap } from '@smart/utils';

import { SeDowngradeComponent } from '../../di';
import { TreeService } from './TreeService';
import { TreeDragAndDropService } from './TreeDragAndDropService';
import { ITreeNodeItem, TreeDragAndDropOptions } from './types';
import { TreeNodeItemFactory } from './TreeNodeItemFactory';

/**
 * @ngdoc component
 * @name TreeModule.component:TreeComponent
 *
 * @element se-tree
 * @description
 * This components renders a tree of nodes and manages CRUD operations around the nodes.
 * <br/>It relies on {@link https://material.angular.io/cdk angular/cdk} third party library
 * @param {String} nodeTemplateUrl an HTML node template to be included besides each node to enhance rendering and behaviour of the tree. This template may use the nodeActions defined hereunder.
 * @param {Type<any>} nodeComponent Angular component to be rendered as tree node
 * @param {String} nodeUri the REST entry point to be used to manage the nodes (GET, POST, PUT and DELETE).
 * @param {Object} dragOptions a {@link TreeModule.object:TreeDragAndDropOptions map} of callback functions to customize the drag and drop behaviour of the tree by exposing the {@link TreeModule.object:TreeDragAndDropEvent TreeDragAndDropEvent}.
 * @param {String} nodeActions a map of methods to be closure-bound to the instance of the component in order to manage the tree from the parent scope or from the optional node template.
 * <br/> All nodeActions methods must take {@link TreeModule.service:TreeService TreeService} instance as first parameter.
 * <br/> {@link TreeModule.service:TreeService treeService} instance will then prebound in the closure made available in the node template or in the parent scope.
 * <br/> Example in a parent controller:
 * <pre>
 * 	this.actions = {
 * 		myMethod(treeService, arg1, arg2) {
 * 			//some action expecting 'this'
 * 			//to be the YTreeController
 * 			this.newChild(treeService.root.nodes[0]);
 * 		}
 * 	};
 * </pre>
 * Passed to the component through:
 * <pre>
 * AngularJS: <se-tree [node-uri]='ctrl.nodeURI' [node-template-url]='ctrl.nodeTemplateUrl' [node-actions]='ctrl.actions'/>
 * Angular: <se-tree [nodeUri]='nodeURI' [nodeComponent]='nodeComponent' [nodeActions]='actions'/>
 * </pre>
 * The legacy template is bound to AngularJS controller, actions may be invoked it this way:
 * <pre>
 * <button data-ng-click="$ctrl.myMethod('arg1', 'arg2')">my action</button>
 * </pre>
 *
 * <br>
 *     Note: "this" context in the template node no longer refers to AngularJS tree handle as the library is no longer used, instead use "node"
 *     variable accessible from the template scope which represents
 *     current node. The TreeService is updated to accept node reference.
 * </br>
 *
 * Example(before):
 *
 * <pre>
 *     <button data-ng-click="$ctrl.myMethod(this)">my action</button>
 * </pre>
 *
 * Example(now):
 *
 * <pre>
 *     <button data-ng-click="$ctrl.myMethod(node)">my action</button>
 * </pre>
 *
 * In Angular component inject the parent TreeComponent reference into your node component constructor to access actions and invoke it like this:
 *
 * <pre>
 *     <button (click)="treeComponentRef.myMethod('arg1', 'arg2')">my action</button>
 * </pre>
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-tree',
    providers: [TreeService, TreeDragAndDropService, TreeNodeItemFactory],
    host: {
        class: 'se-tree'
    },
    templateUrl: './TreeComponent.html'
})
export class TreeComponent<T, D> {
    @Input() nodeTemplateUrl: string;
    @Input() nodeComponent: Type<any>;
    @Input() nodeUri: string;
    @Input() nodeActions: TypedMap<(...args: any[]) => void>;
    @Input() rootNodeUid: string;
    @Input() dragOptions: TreeDragAndDropOptions<T>;
    @Input() removeDefaultTemplate: string;
    @Input() showAsList: boolean;

    @Output() onTreeUpdated: EventEmitter<ITreeNodeItem<T>[]> = new EventEmitter<
        ITreeNodeItem<T>[]
    >();

    public isDropDisabled: boolean;

    constructor(
        private treeService: TreeService<T, D>,
        private treeDragAndDropService: TreeDragAndDropService<T, D>
    ) {}

    ngOnInit() {
        this.setNodeActions();

        this.treeService.init(this.nodeUri, this.rootNodeUid);
        this.treeDragAndDropService.init(this.dragOptions);

        this.fetchData(this.treeService.root);

        this.treeService
            .onTreeUpdated()
            .subscribe(() => this.onTreeUpdated.emit(this.treeService.root.nodes));
    }

    public fetchData(nodeData: ITreeNodeItem<T>): Promise<ITreeNodeItem<T>[]> {
        return this.treeService.fetchChildren(nodeData);
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#hasChildren
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Return a boolean to determine if the node is expandable or not by checking if a given node has children
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem ITreeNodeItem}
     */

    public hasChildren(node: ITreeNodeItem<T>) {
        return node.hasChildren;
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#collapseAll
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Causes all the nodes of the tree to collapse.
     * It does not affect their "initiated" status though.
     */

    public collapseAll() {
        this.treeService.collapseAll();
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#expandAll
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Causes all the nodes of the tree to expand.
     * It does not affect their "initiated" status though.
     */

    public expandAll() {
        this.treeService.expandAll();
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#remove
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Will remove node referenced by the parameter.
     * <br/>The child is added only if {@link TreeModule.service:TreeService#removeNode removeNode} from {@link TreeModule.service:TreeService TreeService} is successful.
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem ITreeNodeItem}
     */

    public remove(node: ITreeNodeItem<T>): void {
        this.treeService.removeNode(node);
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#newChild
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Will add a new sibling to the node referenced by the parameter.
     * <br/>The child is added only if {@link TreeModule.service:TreeService#saveNode saveNode} from {@link TreeModule.service:TreeService TreeService} is successful.
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem ITreeNodeItem}
     */

    public newSibling(node: ITreeNodeItem<T>): void {
        this.treeService.newSibling(node);
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#refresh
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Will refresh a node, causing it to expand after fetch if it was expanded before.
     */

    public refresh(node: ITreeNodeItem<T>): Promise<ITreeNodeItem<T>[]> {
        node.setInitiated(false);

        return this.treeService.fetchChildren(node);
    }
    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#refreshParent
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Will refresh a node, causing it to expand after fetch if it was expanded before.
     */

    public refreshParent(node: ITreeNodeItem<T>): void {
        this.refresh(node.parent);
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#newChild
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Will add a new child to the node referenced by the parameter.
     * <br/>The child is added only if {@link TreeModule.service:TreeService#saveNode saveNode} from {@link TreeModule.service:TreeService TreeService} is successful.
     * @param {ITreeNodeItem} node {@link TreeModule.object:ITreeNodeItem ITreeNodeItem}
     */

    async newChild(node?: ITreeNodeItem<T>): Promise<void> {
        this.treeService.newChild(node);
    }

    /**
     * @ngdoc method
     * @name TreeModule.component:TreeComponent#getNodeById
     * @methodOf TreeModule.component:TreeComponent
     * @description
     * Will fetch from the existing tree the node whose identifier is the given nodeUid
     * @param {String} nodeUid the identifier of the node to fetched
     */

    getNodeById(nodeUid: string, nodeArray?: ITreeNodeItem<T>[]): ITreeNodeItem<T> {
        return this.treeService.getNodeById(nodeUid, nodeArray);
    }

    public get dragEnabled(): boolean {
        return this.treeDragAndDropService.isDragEnabled;
    }

    private setNodeActions(): void {
        Object.keys(this.nodeActions).forEach((functionName: Extract<keyof this, string>) => {
            this[functionName] = this.nodeActions[functionName].bind(this, this.treeService);
            this.nodeActions[functionName] = this.nodeActions[functionName].bind(
                this,
                this.treeService
            );
        });
    }
}
