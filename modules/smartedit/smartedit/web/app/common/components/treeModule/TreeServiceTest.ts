/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TreeService } from './TreeService';
import { TreeNodeItem } from './TreeNodeItem';
import { TreeNodeItemDTO } from './types';
import { TreeNodeItemFactory } from './TreeNodeItemFactory';
import { IRestService, RestServiceFactory } from '@smart/utils';

describe('TreeService', () => {
    let treeService: TreeService<TreeNodeItem, TreeNodeItemDTO>;
    let treeNodeItemFactory: jasmine.SpyObj<TreeNodeItemFactory>;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let restService: jasmine.SpyObj<IRestService<TreeNodeItemDTO | TreeNodeItemDTO[]>>;

    const nodeUri = 'nodeUri';
    const rootNodeUid = 'rootNodeUid';

    const mockNodeDtos: TreeNodeItemDTO[] = [
        {
            uid: '1',
            name: 'node1',
            title: {
                en: 'node1_en',
                fr: 'node1_fr'
            },
            parentUid: 'rootNodeUid'
        },
        {
            uid: '2',
            name: 'node2',
            title: {
                en: 'node2_en',
                fr: 'node2_fr'
            },
            parentUid: 'rootNodeUid'
        }
    ];

    const mockSaveNodeDto: TreeNodeItemDTO = {
        uid: '3',
        name: 'node3',
        title: {
            en: 'node2_en',
            fr: 'node2_fr'
        },
        parentUid: '1'
    };

    beforeEach(() => {
        treeNodeItemFactory = jasmine.createSpyObj('treeNodeItemFactory', ['get']);
        treeNodeItemFactory.get.and.callFake((dto: TreeNodeItemDTO) => new TreeNodeItem(dto));

        restService = jasmine.createSpyObj('restService', ['get', 'remove', 'save']);
        restService.get.and.returnValue(Promise.resolve({ response: mockNodeDtos }));
        restService.save.and.returnValue(Promise.resolve(mockSaveNodeDto));
        restService.remove.and.returnValue(Promise.resolve());

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.and.returnValue(restService);

        treeService = new TreeService<TreeNodeItem, TreeNodeItemDTO>(
            restServiceFactory,
            treeNodeItemFactory
        );

        treeService.init(nodeUri, rootNodeUid);
    });

    it('initializes properly', () => {
        expect(restServiceFactory.get).toHaveBeenCalledWith(nodeUri);
        expect(treeService.root).toEqual(
            new TreeNodeItem({ uid: rootNodeUid, name: 'root', level: 0 })
        );
    });

    it('fetches children and saves data in the source', async (done) => {
        await treeService.fetchChildren();
        expect(treeService.root.nodes[0].name).toBe('node1');
        expect(treeService.root.nodes[1].name).toBe('node2');
        expect(treeService.root.nodes.length).toBe(2);
        done();
    });

    it('rearranges nodes properly', async (done) => {
        await treeService.fetchChildren();
        const rearrangedNodeName = treeService.root.nodes[1].name;

        treeService.rearrange(treeService.root.nodes[1], treeService.root, 0);

        const { position } = treeService.root.nodes.find(
            (node) => node.name === rearrangedNodeName
        );

        expect(position).toBe(0);
        done();
    });

    it('toggles and fetches node children', async (done) => {
        await treeService.fetchChildren();
        const spyOnFetchChildren = spyOn(treeService, 'fetchChildren');
        const spyOnToggle = spyOn(treeService.root.nodes[0], 'toggle');

        treeService.toggle(treeService.root.nodes[0]);

        expect(spyOnToggle).toHaveBeenCalled();
        expect(spyOnFetchChildren).toHaveBeenCalled();

        done();
    });

    it('when newChild is triggered and node is not expanded toggle will be called', async (done) => {
        await treeService.fetchChildren();

        const spyOnToggle = spyOn(treeService, 'toggle');

        await treeService.newChild(treeService.root.nodes[0]);

        expect(spyOnToggle).toHaveBeenCalled();

        done();
    });

    it('when newChild is triggered and node is expanded addNode will be called', async (done) => {
        await treeService.fetchChildren();

        const node = treeService.root.nodes[0];
        const spyOnAddNode = spyOn(node, 'addNode');
        const spyOnToggle = spyOn(treeService, 'toggle');

        node.expand();

        await treeService.newChild(node);

        expect(spyOnAddNode).toHaveBeenCalled();
        expect(spyOnToggle).not.toHaveBeenCalled();

        done();
    });

    it('when newSibling is new item is present', async (done) => {
        await treeService.fetchChildren();

        const node = treeService.root.nodes[0];

        await treeService.newSibling(node);

        expect(treeService.root.nodes.length).toBe(3);

        done();
    });

    it('calls removeNode on parent when triggering removeNode', async (done) => {
        await treeService.fetchChildren();

        const node = treeService.root.nodes[0];
        const spyOnRemoveNode = spyOn(node.parent, 'removeNode');

        await treeService.removeNode(node);

        expect(spyOnRemoveNode).toHaveBeenCalled();

        done();
    });

    it('removeNode succesfully removes item', async (done) => {
        await treeService.fetchChildren();

        const node = treeService.root.nodes[0];

        await treeService.removeNode(node);

        expect(treeService.root.nodes.length).toBe(1);

        done();
    });

    it('updates data source correctly', async (done) => {
        await treeService.fetchChildren();

        const spy = spyOn(treeService.dataSource, 'set');

        treeService.update();

        expect(spy).toHaveBeenCalledTimes(2);
        done();
    });

    it('expandAll calls expandAll on root node', async (done) => {
        await treeService.fetchChildren();

        const updateSpy = spyOn(treeService, 'update');
        const expandAllSpy = spyOn(treeService.root, 'expandAll');

        treeService.expandAll();

        expect(updateSpy).toHaveBeenCalled();
        expect(expandAllSpy).toHaveBeenCalled();

        done();
    });

    it('expandAll flags all nodes as expanded', async (done) => {
        await treeService.fetchChildren();

        treeService.expandAll();

        expect(treeService.root.nodes.every((node) => node.isExpanded)).toBe(true);

        done();
    });

    it('collapseAll calls collapseAll on root node', async (done) => {
        await treeService.fetchChildren();

        const updateSpy = spyOn(treeService, 'update');
        const collapseAllSpy = spyOn(treeService.root, 'collapseAll');

        treeService.collapseAll();

        expect(updateSpy).toHaveBeenCalled();
        expect(collapseAllSpy).toHaveBeenCalled();

        done();
    });

    it('collapseAll flags all nodes as collapsed', async (done) => {
        await treeService.fetchChildren();

        treeService.collapseAll();

        expect(treeService.root.nodes.every((node) => !node.isExpanded)).toBe(true);

        done();
    });

    it('getNodeById returns correct node provided id', async (done) => {
        await treeService.fetchChildren();

        const found = treeService.getNodeById('1');

        expect(found.name).toBe('node1');

        done();
    });
});
