/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as lo from 'lodash';
import {
    IRestService,
    IRestServiceFactory,
    ISeComponent,
    IUriContext,
    NavigationNodeItem,
    NavigationNodeItemDTO,
    SeComponent,
    TreeDragAndDropEvent,
    TreeService,
    URIBuilder
} from 'smarteditcommons';
import { NavigationNodeEditorModalService } from '../navigationNodeEditor/NavigationNodeEditorModalService';
import { NavigationNode, NavigationNodeCMSItem, NavigationNodeEntry } from '../types';
import { CmsitemsRestService } from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';
import './NavigationEditor.scss';

/**
 * @ngdoc directive
 * @name navigationEditorModule.directive:navigationEditor
 * @scope
 * @restrict E
 * @element ANY
 *
 * @description
 * Navigation Editor directive used to display navigation editor tree
 * @param {Object} uriContext the {@link resourceLocationsModule.object:UriContext UriContext} necessary to perform operations
 * @param {Boolean} readOnly when true, no CRUD facility shows on the editor. OPTIONAL, default false.
 * @param {String} rootNodeUid the uid of the node to be taken as root, OPTIONAL, default "root"
 */
@SeComponent({
    templateUrl: 'navigationEditorTreeComponentTemplate.html',
    inputs: ['uriContext', 'readOnly', 'rootNodeUid']
})
export class NavigationEditorTreeComponent implements ISeComponent {
    private static readonly READY_ONLY_ERROR_I18N = 'navigation.in.readonly.mode';

    public nodeTemplateUrl = 'navigationNodeRenderTemplate.html';

    public uriContext: IUriContext;
    public readOnly: boolean;
    public removeDefaultTemplate: boolean;
    public rootNodeUid: string;
    public dragOptions: any;
    public actions: any;
    public nodeURI: string;

    public navigationsRestEndpoint: IRestService<NavigationNode>;

    constructor(
        private NAVIGATION_NODE_ROOT_NODE_UID: string,
        private $q: angular.IQService,
        private $log: angular.ILogService,
        private lodash: lo.LoDashStatic,
        private restServiceFactory: IRestServiceFactory,
        private NAVIGATION_MANAGEMENT_RESOURCE_URI: string,
        private navigationEditorNodeService: any,
        private navigationNodeEditorModalService: NavigationNodeEditorModalService,
        private confirmationModalService: any,
        private cmsitemsRestService: CmsitemsRestService,
        private NAVIGATION_NODE_TYPECODE: string
    ) {}

    $onInit(): void {
        if (!this.readOnly) {
            this.dragOptions = this.buildDragOptions();
        }

        this.nodeURI = new URIBuilder(this.NAVIGATION_MANAGEMENT_RESOURCE_URI)
            .replaceParams(this.uriContext)
            .build();
        this.rootNodeUid = this.rootNodeUid || this.NAVIGATION_NODE_ROOT_NODE_UID;
        this.removeDefaultTemplate = true;

        this.navigationsRestEndpoint = this.restServiceFactory.get(this.nodeURI);

        this.actions = this.buildActions();
    }

    private buildDragOptions() {
        return {
            onDropCallback: (event: TreeDragAndDropEvent<NavigationNodeItem>) => {
                this.actions.dragAndDrop(event);
            },
            allowDropCallback: (event: TreeDragAndDropEvent<NavigationNodeItem>) => {
                if (event.sourceNode.parent.uid !== event.destinationNodes[0].parent.uid) {
                    return false;
                }
                return true;
            },
            beforeDropCallback(event: TreeDragAndDropEvent<NavigationNodeItem>) {
                if (event.sourceNode.parent.uid !== event.destinationNodes[0].parent.uid) {
                    return Promise.resolve({
                        confirmDropI18nKey: 'se.cms.navigationmanagement.navnode.confirmation'
                    });
                } else {
                    return Promise.resolve(true);
                }
            }
        };
    }

    private buildActions() {
        const vm = this;

        const dropdownItems = [
            {
                key: 'se.cms.navigationmanagement.navnode.edit',
                callback(node: NavigationNode) {
                    vm.actions.editNavigationNode(node);
                }
            },
            {
                key: 'se.cms.navigationmanagement.navnode.removenode',
                customCss: 'se-dropdown-item--delete',
                callback(node: NavigationNode) {
                    vm.actions.removeItem(node);
                }
            },
            {
                key: 'se.cms.navigationmanagement.navnode.move.up',
                condition(node: NavigationNode) {
                    return vm.actions.isMoveUpAllowed(node);
                },
                callback(node: NavigationNode) {
                    vm.actions.moveUp(node);
                }
            },
            {
                key: 'se.cms.navigationmanagement.navnode.move.down',
                condition(node: NavigationNode) {
                    return vm.actions.isMoveDownAllowed(node);
                },
                callback(node: NavigationNode) {
                    vm.actions.moveDown(node);
                }
            },
            {
                key: 'se.cms.navigationmanagement.navnode.addchild',
                callback(node: NavigationNode) {
                    vm.actions.addNewChild(node);
                }
            },
            {
                key: 'se.cms.navigationmanagement.navnode.addsibling',
                callback(node: any) {
                    vm.actions.addNewSibling(node);
                }
            }
        ];

        // those functions will be closure bound inside ytree
        const actions = {
            isReadOnly() {
                return vm.readOnly;
            },

            hasChildren(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                return nodeData.hasChildren;
            },

            fetchData(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                if (nodeData.uid === vm.NAVIGATION_NODE_ROOT_NODE_UID) {
                    nodeData.initiated = false;
                }

                if (nodeData.initiated) {
                    return Promise.resolve(nodeData.nodes);
                }

                let promise = Promise.resolve(null);

                if (nodeData.uid === vm.NAVIGATION_NODE_ROOT_NODE_UID) {
                    promise = vm.getNavigationNodeCMSItemByUid(this.rootNodeUid).then((node) => {
                        vm.lodash.assign(nodeData, node);
                    });
                }

                return promise.then(function() {
                    nodeData.removeAllNodes();
                    return treeService.fetchChildren(nodeData);
                });
            },

            removeItem(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                node: NavigationNodeItem
            ) {
                if (vm.readOnly) {
                    throw NavigationEditorTreeComponent.READY_ONLY_ERROR_I18N;
                }
                vm.confirmationModalService
                    .confirm({
                        description:
                            'se.cms.navigationmanagement.navnode.removenode.confirmation.message',
                        title: 'se.cms.navigationmanagement.navnode.removenode.confirmation.title'
                    })
                    .then(() => {
                        vm.cmsitemsRestService.delete(node.uuid).then(() => {
                            vm.actions.refreshParentNode(node);
                        });
                    });
            },

            performMove(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                node: NavigationNodeItem,
                refreshNodeItself?: boolean
            ) {
                if (vm.readOnly) {
                    throw NavigationEditorTreeComponent.READY_ONLY_ERROR_I18N;
                }
                return vm.navigationEditorNodeService
                    .updateNavigationNodePosition(node, vm.uriContext)
                    .then(
                        () => {
                            if (!node) {
                                vm.actions.fetchData(treeService.root);
                            } else if (refreshNodeItself) {
                                vm.actions.refreshNode(node);
                            } else {
                                vm.actions.refreshParentNode(node);
                            }
                        },
                        (err: any) => {
                            vm.$log.error(`Error updating node position:\n${err}`);
                        }
                    );
            },

            dragAndDrop(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                event: TreeDragAndDropEvent<NavigationNodeItem>
            ) {
                const nodeData = event.sourceNode;
                const destinationNodes = event.destinationNodes;

                const destination = vm.lodash.find(destinationNodes, (node) => {
                    return node.uid !== nodeData.uid;
                });

                // this method is still triggered on drop, even if drop is not allowed
                // so its possible that destination does not exist, in which case we return silently
                if (!destination) {
                    return;
                }
                const destinationParent = destination.parent;

                if (vm.hasNotMoved(nodeData, event.position, destinationParent)) {
                    return;
                }

                nodeData.position = event.position;

                nodeData.setParent(destinationParent);

                vm.actions.performMove(nodeData, true).then(() => {
                    if (event.sourceParentNode.uid !== event.destinationParentNode.uid) {
                        vm.actions.refreshNode(event.sourceParentNode);
                    }
                });
            },

            moveUp(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                if (vm.readOnly) {
                    throw NavigationEditorTreeComponent.READY_ONLY_ERROR_I18N;
                }
                nodeData.position = parseInt((nodeData.position as unknown) as string, 10) - 1;
                vm.actions.performMove(nodeData);
            },

            moveDown(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                if (vm.readOnly) {
                    throw NavigationEditorTreeComponent.READY_ONLY_ERROR_I18N;
                }
                nodeData.position = parseInt((nodeData.position as unknown) as string, 10) + 1;
                vm.actions.performMove(nodeData);
            },

            isMoveUpAllowed(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                return parseInt((nodeData.position as unknown) as string, 10) !== 0;
            },

            isMoveDownAllowed(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                nodeData.parent.nodes = nodeData.parent.nodes || [];
                return parseInt(nodeData.position + '', 10) !== nodeData.parent.nodes.length - 1;
            },

            refreshNode(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                return this.refresh(nodeData);
            },

            refreshParentNode(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                return this.refreshParent(nodeData);
            },

            editNavigationNode(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                const target: any = {};

                target.nodeUid = nodeData.uid;
                target.entryIndex = undefined;

                return vm.navigationNodeEditorModalService
                    .open(
                        vm.uriContext,
                        (nodeData.parent as unknown) as NavigationNode,
                        (nodeData as unknown) as NavigationNode
                    )
                    .then(() => {
                        vm.actions.refreshNode(nodeData.parent);

                        let targetNode: NavigationNodeItem;

                        if (nodeData.parent.uid === vm.NAVIGATION_NODE_ROOT_NODE_UID) {
                            targetNode = nodeData;
                        } else {
                            targetNode = nodeData.parent;
                        }

                        return vm.navigationEditorNodeService
                            .getNavigationNode(targetNode.uid, vm.uriContext)
                            .then((refreshedNode: NavigationNode) => {
                                vm.lodash.assign(targetNode, refreshedNode);
                                if (nodeData.parent.uid === vm.NAVIGATION_NODE_ROOT_NODE_UID) {
                                    return vm.actions.refreshNode(nodeData);
                                }
                                return vm.actions.refreshParentNode(nodeData);
                            });
                    });
            },

            addTopLevelNode() {
                return vm.actions.addNewChild().then(() => {
                    return vm.getNavigationNodeCMSItemByUid(this.rootNodeUid).then((node) => {
                        vm.actions.fetchData(node);
                    });
                });
            },

            getEntryString(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                node: NavigationNodeItem
            ) {
                return vm.getEntriesCommaSeparated(node.entries || []);
            },

            getEntryTooltipString(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                node: NavigationNodeItem
            ) {
                return [
                    '<div>',
                    ...(node.entries || []).map(
                        (entry: NavigationNodeEntry) =>
                            `<div>${entry.name} (${entry.itemType})</div>`
                    ),
                    '</div>'
                ].join('');
            },

            addNewChild(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                const parent = nodeData ? nodeData : vm.actions._findNodeById(vm.rootNodeUid);
                return vm.actions
                    ._expandIfNeeded(nodeData)
                    .then(() => {
                        return vm.navigationNodeEditorModalService.open(vm.uriContext, parent);
                    })
                    .then((node: any) => vm.actions.refreshNode(parent));
            },

            addNewSibling(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                const parent = nodeData.parent;
                return vm.navigationNodeEditorModalService
                    .open(vm.uriContext, parent)
                    .then((node: any) => vm.actions.refreshNode(parent));
            },

            getDropdownItems() {
                return dropdownItems;
            },

            _findNodeById(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeUid: string
            ): NavigationNode {
                return this.getNodeById(nodeUid);
            },
            _expandIfNeeded(
                treeService: TreeService<NavigationNodeItem, NavigationNodeItemDTO>,
                nodeData: NavigationNodeItem
            ) {
                return nodeData && !nodeData.isExpanded
                    ? treeService.toggle(nodeData)
                    : vm.$q.when();
            }
        };

        return actions;
    }

    private hasNotMoved(
        source: NavigationNode,
        destinationPosition: number,
        destinationParent: NavigationNode
    ): boolean {
        return (
            source.position === destinationPosition && source.parentUid === destinationParent.uid
        );
    }

    private getNavigationNodeCMSItemByUid(uid: string): Promise<NavigationNodeCMSItem> {
        return this.cmsitemsRestService
            .get<NavigationNodeCMSItem>({
                typeCode: this.NAVIGATION_NODE_TYPECODE,
                pageSize: 1,
                currentPage: 0,
                itemSearchParams: 'uid:' + uid
            })
            .then((data: { response: NavigationNodeCMSItem[] }) => {
                return data.response[0];
            });
    }

    private getEntriesCommaSeparated(entries: NavigationNodeEntry[]): string {
        return entries.map((entry) => `${entry.name} (${entry.itemType})`).join(', ');
    }
}
