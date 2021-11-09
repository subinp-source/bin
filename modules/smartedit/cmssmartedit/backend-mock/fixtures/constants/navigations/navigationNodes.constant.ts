/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { INavigationNode } from '../../entities/navigations/navigationNode.entity';
export const navigationNodes: INavigationNode[] = [
    {
        uid: 'root',
        name: 'ROOT',
        uuid: 'CMSNavigationNode-root',
        itemtype: 'CMSNavigationNode',
        position: 0,
        hasChildren: true,
        children: ['CMSNavigationNode-1', 'CMSNavigationNode-2'],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '1',
        name: 'node1',
        title: {
            en: 'node1_en',
            fr: 'node1_fr'
        },
        uuid: 'CMSNavigationNode-1',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-root',
        parentUid: 'root',
        position: 0,
        hasChildren: true,
        hasEntries: true,
        children: ['CMSNavigationNode-4', 'CMSNavigationNode-5', 'CMSNavigationNode-7'],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '2',
        name: 'node2',
        title: {
            en: 'node2_en',
            fr: 'node2_fr'
        },
        uuid: 'CMSNavigationNode-2',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-root',
        parentUid: 'root',
        position: 1,
        hasChildren: true,
        hasEntries: false,
        children: ['CMSNavigationNode-6'],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '4',
        name: 'node4',
        title: {
            en: 'node4_en',
            fr: 'node4_fr'
        },
        uuid: 'CMSNavigationNode-4',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-1',
        parentUid: '1',
        position: 0,
        hasChildren: true,
        hasEntries: true,
        children: ['CMSNavigationNode-8'],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '5',
        name: 'node5',
        title: {
            en: 'node5_en',
            fr: 'node5_fr'
        },
        uuid: 'CMSNavigationNode-5',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-1',
        parentUid: '1',
        position: 1,
        hasChildren: false,
        hasEntries: false,
        children: [],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '6',
        name: 'node6',
        title: {
            en: 'node6_en',
            fr: 'node6_fr'
        },
        uuid: 'CMSNavigationNode-6',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-2',
        parentUid: '2',
        position: 0,
        hasChildren: false,
        hasEntries: false,
        children: [],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '7',
        name: 'node7',
        title: {
            en: 'node7_en',
            fr: 'node7_fr'
        },
        uuid: 'CMSNavigationNode-7',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-1',
        parentUid: '1',
        position: 2,
        hasChildren: false,
        hasEntries: false,
        children: [],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '8',
        name: 'node8',
        title: {
            en: 'node8_en',
            fr: 'node8_fr'
        },
        uuid: 'CMSNavigationNode-8',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-4',
        parentUid: '4',
        position: 0,
        hasChildren: true,
        hasEntries: true,
        children: ['CMSNavigationNode-9'],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uid: '9',
        name: 'node9',
        title: {
            en: 'node9_en',
            fr: 'node9_fr'
        },
        uuid: 'CMSNavigationNode-9',
        itemtype: 'CMSNavigationNode',
        parent: 'CMSNavigationNode-8',
        parentUid: '8',
        position: 0,
        hasChildren: false,
        hasEntries: false,
        children: [],
        catalogVersion: 'apparel-ukContentCatalog/Online'
    },
    {
        uuid: 'navigationComponent',
        uid: 'navigationComponent',
        navigationComponent: 'CMSNavigationNode-8',
        catalogVersion: 'apparel-ukContentCatalog/Online'
    }
];
