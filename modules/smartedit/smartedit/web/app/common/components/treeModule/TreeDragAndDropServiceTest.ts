/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TreeDragAndDropService } from './TreeDragAndDropService';
import { IAlertService, IConfirmationModalService } from '../../services/interfaces';
import { TreeService } from './TreeService';
import { TreeNodeItem } from './TreeNodeItem';
import { TreeDragAndDropEvent, TreeDragAndDropOptions, TreeNodeItemDTO } from './types';

const mockCdkDropEvent = (override?: any) => ({
    item: {
        data: new TreeNodeItem({ uid: '1' })
    },
    container: {
        data: [new TreeNodeItem({ uid: '1' })]
    },
    currentIndex: 1,
    isPointerOverContainer: true,
    ...override
});

const dndEvent = () =>
    new TreeDragAndDropEvent(
        mockCdkDropEvent().item.data,
        mockCdkDropEvent().container.data,
        mockCdkDropEvent().item.data.parent,
        mockCdkDropEvent().container.data[0].parent,
        mockCdkDropEvent().currentIndex
    );
const getDndOptions = (override?: TreeDragAndDropOptions<TreeNodeItem>) => ({
    onDropCallback(event: TreeDragAndDropEvent<TreeNodeItem>) {
        //
    },
    beforeDropCallback(event: TreeDragAndDropEvent<TreeNodeItem>) {
        return Promise.resolve(true);
    },
    allowDropCallback(event: TreeDragAndDropEvent<TreeNodeItem>) {
        return true;
    },
    ...(override || {})
});

describe('TreeDragAndDropService - ', () => {
    let treeDragAndDropService: TreeDragAndDropService<TreeNodeItem, TreeNodeItemDTO>;
    let confirmationModalService: jasmine.SpyObj<IConfirmationModalService>;
    let alertService: jasmine.SpyObj<IAlertService>;
    let treeService: jasmine.SpyObj<TreeService<TreeNodeItem, TreeNodeItemDTO>>;

    beforeEach(() => {
        confirmationModalService = jasmine.createSpyObj('confirmationModalService', ['confirm']);
        alertService = jasmine.createSpyObj('alertService', ['showDanger']);
        treeService = jasmine.createSpyObj('treeService', ['rearrange']);

        treeDragAndDropService = new TreeDragAndDropService<TreeNodeItem, TreeNodeItemDTO>(
            confirmationModalService,
            alertService,
            treeService
        );
    });

    describe('Initializing - ', () => {
        it('is enabled after config validation', () => {
            treeDragAndDropService.init(getDndOptions());

            expect(treeDragAndDropService.isDragEnabled).toBe(true);
        });

        it('is disabled after config validation', () => {
            treeDragAndDropService.init(
                getDndOptions({
                    onDropCallback: null,
                    beforeDropCallback: null,
                    allowDropCallback: null
                } as TreeDragAndDropOptions<TreeNodeItem>)
            );

            expect(treeDragAndDropService.isDragEnabled).toBe(false);
        });
    });

    describe('Callbacks', () => {
        it('allowDrop calls allowDropCallback', () => {
            treeDragAndDropService.init(getDndOptions());

            const spy = spyOn((treeDragAndDropService as any).config, 'allowDropCallback');

            (treeDragAndDropService as any).allowDrop(mockCdkDropEvent());

            expect(spy).toHaveBeenCalledWith(jasmine.objectContaining(dndEvent()));
        });

        it('beforeDrop calls beforeDropCallback', (done) => {
            treeDragAndDropService.init(getDndOptions());

            const spy = spyOn((treeDragAndDropService as any).config, 'beforeDropCallback');
            spy.and.returnValue(Promise.resolve());

            (treeDragAndDropService as any).beforeDrop(mockCdkDropEvent()).then(() => {
                expect(spy).toHaveBeenCalledWith(jasmine.objectContaining(dndEvent()));
                done();
            });
        });
    });

    describe('Drop handling - ', () => {
        it('stops executing handleDrop if pointer is not over container', (done) => {
            treeDragAndDropService.init(getDndOptions());

            const spy = spyOn(treeDragAndDropService as any, 'allowDrop');

            return treeDragAndDropService
                .handleDrop(mockCdkDropEvent({ isPointerOverContainer: false }))
                .then(() => {
                    expect(spy).not.toHaveBeenCalled();
                    done();
                });
        });

        it('stops executing handleDrop if config is not present', (done) => {
            const spy = spyOn(treeDragAndDropService as any, 'allowDrop');

            return treeDragAndDropService.handleDrop(mockCdkDropEvent()).then(() => {
                expect(spy).not.toHaveBeenCalled();
                done();
            });
        });

        it('stops executing handleDrop if allowDrop conditions are not met', (done) => {
            const spy = spyOn(treeDragAndDropService as any, 'beforeDrop');

            treeDragAndDropService.init(
                getDndOptions({
                    allowDropCallback: (e: TreeDragAndDropEvent<TreeNodeItem>) => false
                } as TreeDragAndDropOptions<TreeNodeItem>)
            );

            return treeDragAndDropService.handleDrop(mockCdkDropEvent()).then(() => {
                expect(spy).not.toHaveBeenCalled();
                done();
            });
        });

        it('executes handleDrop if all validation is passed', (done) => {
            const spy = spyOn(treeDragAndDropService as any, 'onDrop');

            treeDragAndDropService.init(getDndOptions());

            return treeDragAndDropService.handleDrop(mockCdkDropEvent()).then(() => {
                expect(spy).toHaveBeenCalledWith(jasmine.objectContaining(mockCdkDropEvent()));
                done();
            });
        });
    });
});
