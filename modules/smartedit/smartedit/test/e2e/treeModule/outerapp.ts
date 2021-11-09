/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
/* forbiddenNameSpaces angular.module:false */
import { forwardRef, Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    IConfirmationModalService,
    NgTreeModule,
    SeDowngradeComponent,
    SeEntryModule,
    TreeComponent,
    TreeDragAndDropEvent,
    TreeNodeItem,
    TreeNodeItemDTO,
    TreeService,
    TREE_NODE
} from 'smarteditcommons';

@Component({
    selector: 'node-component',
    template: `
        <div id="node-component" style="display: flex; justify-content: flex-end">
            <a
                class="pull-right btn btn-danger btn-xs angular-show-modal"
                (click)="treeRef.showModal(node)"
            >
                <span class="glyphicon glyphicon-th"></span>
            </a>
            <a
                class="pull-right btn btn-danger btn-xs angular-remove"
                (click)="treeRef.remove(node)"
            >
                <span class="glyphicon glyphicon-remove"></span>
            </a>
            <a
                class="pull-right btn btn-primary btn-xs angular-new-child"
                (click)="treeRef.newChild(node)"
                style="margin-right: 8px;"
            >
                <span class="glyphicon glyphicon-plus">child</span>
            </a>
            <a
                class="pull-right btn btn-success btn-xs angular-new-sibling"
                (click)="treeRef.newSibling(node)"
                style="margin-right: 8px;"
            >
                <span class="glyphicon glyphicon-plus">sibling</span>
            </a>
        </div>
    `
})
export class NodeComponent {
    constructor(
        @Inject(TREE_NODE) public node: TreeNodeItem,
        @Inject(forwardRef(() => TreeComponent))
        public treeRef: TreeComponent<TreeNodeItem, TreeNodeItemDTO>
    ) {}
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <div class="treeDemo">
                <button
                    class="legacy-outside-add-sibling"
                    (click)="actionsLegacy.addSiblingToFirstChild()"
                >
                    Add sibling to first child
                </button>

                <se-tree
                    style="margin-top: 50px; display:block"
                    id="legacy-tree"
                    [rootNodeUid]="rootNodeUid"
                    [nodeUri]="nodeURI"
                    [nodeTemplateUrl]="nodeTemplateUrl"
                    [nodeActions]="actionsLegacy"
                    [dragOptions]="dragOptions()"
                >
                </se-tree>

                <button
                    class="angular-outside-add-sibling"
                    (click)="actionsAngular.addSiblingToFirstChild()"
                >
                    Add sibling to first child
                </button>

                <se-tree
                    style="margin-top: 50px; display:block"
                    id="angular-tree"
                    [rootNodeUid]="rootNodeUid"
                    [nodeUri]="nodeURI"
                    [nodeComponent]="component"
                    [nodeActions]="actionsAngular"
                    [dragOptions]="dragOptions()"
                >
                </se-tree>
            </div>
        </div>
    `
})
export class AppRootComponent {
    public nodeTemplateUrl = 'someTreeNodeRenderTemplate.html';
    public nodeURI = 'someNodeURI';
    public rootNodeUid = 'root';
    public component = NodeComponent;

    public actionsLegacy = {
        addSiblingToFirstChild() {
            this.newSibling(this.treeService.root.nodes[0]);
        },

        showModal: (_: TreeService<TreeNodeItem, TreeNodeItemDTO>, node: TreeNodeItem) => {
            this.confirmationModalService.confirm({
                description: 'Custom action triggered: ' + node.name
            });
        }
    };

    public actionsAngular = {
        addSiblingToFirstChild() {
            this.newSibling(this.treeService.root.nodes[0]);
        },
        showModal: (_: TreeService<TreeNodeItem, TreeNodeItemDTO>, node: TreeNodeItem) => {
            this.confirmationModalService.confirm({
                description: 'Custom action triggered: ' + node.name
            });
        }
    };

    constructor(private confirmationModalService: IConfirmationModalService) {}

    dragOptions() {
        return {
            onDropCallback: (event: TreeDragAndDropEvent<TreeNodeItem>) => {
                this.confirmationModalService.confirm({
                    description: 'Node dropped succesfully!'
                });
            },
            allowDropCallback: (event: TreeDragAndDropEvent<TreeNodeItem>) => {
                return event.sourceNode.parent.uid === event.destinationNodes[0].parent.uid;
            },
            beforeDropCallback: (event: TreeDragAndDropEvent<TreeNodeItem>) => {
                if (event.position === 0) {
                    return Promise.resolve(true);
                }
                if (event.position === 1) {
                    return Promise.resolve({
                        confirmDropI18nKey: 'se.tree.confirm.message'
                    });
                }
                return Promise.resolve({
                    rejectDropI18nKey: 'se.tree.reject.message'
                });
            }
        };
    }
}

@SeEntryModule('treeModuleApp')
@NgModule({
    imports: [NgTreeModule, CommonModule],
    declarations: [AppRootComponent, NodeComponent],
    entryComponents: [AppRootComponent, NodeComponent]
})
export class TabsAppNg {}
