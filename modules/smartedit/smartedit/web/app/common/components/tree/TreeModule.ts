/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule, SeValueProvider } from 'smarteditcommons/di';

import { TreeServiceFactory } from './TreeServiceFactory';
import { TreeDndOptionFactory } from './TreeDndOptionsFactory';
import { YtreeComponent } from './YtreeComponent';

const TREE_CONFIGURATION_PROVIDER: SeValueProvider = {
    provide: 'treeConfig',
    useValue: {
        treeClass: 'angular-ui-tree',
        hiddenClass: 'angular-ui-tree-hidden',
        nodesClass: 'angular-ui-tree-nodes',
        nodeClass: 'angular-ui-tree-node',
        handleClass: 'angular-ui-tree-handle',
        placeholderClass: 'angular-ui-tree-placeholder',
        dragClass: 'angular-ui-tree-drag',
        dragThreshold: 3,
        levelThreshold: 30,
        defaultCollapsed: true,
        dragDelay: 200
    }
};

/**
 * @ngdoc overview
 * @name treeModule
 * @description
 * <h1>This module deals with rendering and management of node trees</h1>
 */
@SeModule({
    imports: [
        'ui.tree',
        'includeReplaceModule',
        'functionsModule',
        'smarteditServicesModule',
        'translationServiceModule',
        'confirmationModalServiceModule',
        'yLoDashModule'
    ],
    declarations: [YtreeComponent],
    providers: [
        TREE_CONFIGURATION_PROVIDER,
        {
            provide: 'TreeService',
            useFactory: TreeServiceFactory
        },
        {
            provide: '_TreeDndOptions',
            useFactory: TreeDndOptionFactory
        },
        TreeServiceFactory
    ]
})
export class TreeModule {}
