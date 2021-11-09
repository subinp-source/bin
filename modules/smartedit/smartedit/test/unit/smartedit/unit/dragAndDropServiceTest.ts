/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    DragAndDropScrollingService,
    DragAndDropService,
    InViewElementObserver,
    SystemEventService
} from 'smarteditcommons';
import { jQueryHelper } from 'testhelpers';

describe('dragAndDropService', () => {
    // Constants
    const DRAGGABLE_ATTR = 'draggable';
    const DROPPABLE_ATTR = 'data-droppable';

    // Variables
    let dragAndDropService: DragAndDropService;

    let dragAndDropScrollingService: jasmine.SpyObj<DragAndDropScrollingService>;
    let inViewElementObserver: jasmine.SpyObj<InViewElementObserver>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let dragAndDropCrossOrigin: jasmine.SpyObj<any>;

    beforeEach(() => {
        dragAndDropCrossOrigin = jasmine.createSpyObj<any>('dragAndDropCrossOrigin', [
            'initialize'
        ]);
        systemEventService = jasmine.createSpyObj<SystemEventService>('systemEventService', [
            'publish'
        ]);
        inViewElementObserver = jasmine.createSpyObj<InViewElementObserver>(
            'inViewElementObserver',
            ['addSelector']
        );
        dragAndDropScrollingService = jasmine.createSpyObj<DragAndDropScrollingService>(
            'dragAndDropScrollingService',
            ['enable', 'disable', 'deactivate']
        );

        dragAndDropScrollingService.enable.and.returnValue(null);
        dragAndDropScrollingService.enable.and.returnValue(null);
        dragAndDropScrollingService.enable.and.returnValue(null);

        dragAndDropService = new DragAndDropService(
            jQueryHelper.jQuery(),
            dragAndDropScrollingService,
            inViewElementObserver,
            systemEventService,
            dragAndDropCrossOrigin
        );
    });

    describe('register', () => {
        it('GIVEN no id is provided THEN it will throw an error and not add the configuration', () => {
            // Arrange
            const configuration: any = {
                property: 'some property'
            };
            expect(getNumPropertiesInObject((dragAndDropService as any).configurations)).toBe(0);

            // Act
            const registerFunction = function() {
                dragAndDropService.register(configuration);
            };

            // Assert
            expect(registerFunction).toThrow(
                new Error('dragAndDropService - register(): Configuration needs an ID.')
            );
            expect(getNumPropertiesInObject((dragAndDropService as any).configurations)).toBe(0);
        });

        it('GIVEN an id is provided THEN the configuration is added', () => {
            // Arrange
            const id = 'id1';
            const configuration = {
                property: 'some property',
                targetSelector: 'someTargetSelector',
                id
            } as any;
            expect(getNumPropertiesInObject((dragAndDropService as any).configurations)).toBe(0);

            // Act
            dragAndDropService.register(configuration);

            // Assert
            expect(getNumPropertiesInObject((dragAndDropService as any).configurations)).toBe(1);

            expect(inViewElementObserver.addSelector.calls.count()).toBe(1);
            expect(inViewElementObserver.addSelector).toHaveBeenCalledWith('someTargetSelector');
        });
    });

    describe('update', () => {
        const configurations: any = [
            {
                id: 'id1',
                sourceSelector: 'some source selector',
                targetSelector: 'some target selector'
            }
        ];

        beforeEach(function() {
            spyOn(dragAndDropService, 'update').and.callThrough();
            spyOn(dragAndDropService as any, '_update').and.callThrough();
            spyOn(dragAndDropService as any, 'initializeScrolling').and.callThrough();
            spyOn(dragAndDropService as any, 'cacheDragImages').and.callThrough();
            spyOn(dragAndDropService as any, 'deactivateConfiguration');

            dragAndDropService.register(configurations[0]);
        });

        it('WHEN apply is called THEN it updates the registered configurations', () => {
            // Arrange

            // Act
            dragAndDropService.apply(configurations[0].id);

            // Assert
            expect(dragAndDropService.update).toHaveBeenCalledWith('id1');
            expect((dragAndDropService as any).initializeScrolling).toHaveBeenCalledWith(
                configurations[0]
            );
            expect((dragAndDropService as any).cacheDragImages).toHaveBeenCalledWith(
                configurations[0]
            );
        });

        it('WHEN update is called THEN it adds events and attributes as necessary', () => {
            // Arrange
            const droppable = jasmine.createSpyObj('droppable', ['attr', 'on', 'filter']);
            const draggable = jasmine.createSpyObj('draggable', ['attr', 'on', 'filter']);

            spyOn(dragAndDropService as any, 'getSelector').and.callFake((attr: string) => {
                if (attr === configurations[0].sourceSelector + ':not([draggable])') {
                    return draggable;
                } else if (attr === configurations[0].targetSelector + ':not([draggable])') {
                    return droppable;
                }
            });

            draggable.filter.and.returnValue(draggable);
            droppable.filter.and.returnValue(droppable);

            // Act
            dragAndDropService.update(configurations[0].id);

            // Assert
            expect((dragAndDropService as any).deactivateConfiguration).toHaveBeenCalledWith(
                configurations[0]
            );

            expect(droppable.attr).toHaveBeenCalledWith(DROPPABLE_ATTR, 'true');
            expect(droppable.on).toHaveBeenCalledWith('dragenter', jasmine.any(Function));
            expect(droppable.on).toHaveBeenCalledWith('dragover', jasmine.any(Function));
            expect(droppable.on).toHaveBeenCalledWith('drop', jasmine.any(Function));
            expect(droppable.on).toHaveBeenCalledWith('dragleave', jasmine.any(Function));

            expect(draggable.attr).toHaveBeenCalledWith(DRAGGABLE_ATTR, 'true');
            expect(draggable.on).toHaveBeenCalledWith('dragstart', jasmine.any(Function));
            expect(draggable.on).toHaveBeenCalledWith('dragend', jasmine.any(Function));
        });
    });

    describe('unregister', () => {
        const configurations = [
            {
                id: 'id1',
                sourceSelector: 'some source selector',
                targetSelector: 'some target selector'
            },
            {
                id: 'id2',
                enableScrolling: false
            },
            {
                id: 'id3',
                enableScrolling: true
            }
        ];

        beforeEach(function() {
            spyOn(dragAndDropService as any, 'deactivateConfiguration').and.callThrough();
            spyOn(dragAndDropService as any, 'deactivateScrolling').and.callThrough();

            expect(getNumPropertiesInObject((dragAndDropService as any).configurations)).toBe(0);
            expect((dragAndDropService as any).deactivateConfiguration.calls.count()).toBe(0);
            expect((dragAndDropService as any).deactivateScrolling.calls.count()).toBe(0);

            configurations.forEach((conf: any) => {
                dragAndDropService.register(conf);
            });
        });

        it('GIVEN a list of configurations THEN each configuration is unregistered if found', () => {
            // Arrange
            const configurationsIDList = ['id1', 'id2', 'id4'];

            // Act
            dragAndDropService.unregister(configurationsIDList);

            // Assert
            expect((dragAndDropService as any).deactivateConfiguration.calls.count()).toBe(2);
            expect((dragAndDropService as any).deactivateConfiguration).toHaveBeenCalledWith(
                configurations[0]
            );
            expect((dragAndDropService as any).deactivateConfiguration).toHaveBeenCalledWith(
                configurations[1]
            );

            expect((dragAndDropService as any).deactivateScrolling.calls.count()).toBe(2);
            expect((dragAndDropService as any).deactivateScrolling).toHaveBeenCalledWith(
                configurations[0]
            );
            expect((dragAndDropService as any).deactivateScrolling).toHaveBeenCalledWith(
                configurations[1]
            );

            expect(getNumPropertiesInObject((dragAndDropService as any).configurations)).toBe(1);
        });

        it('WHEN deactivateConfiguration is called THEN all attributes and event listeners are removed ', () => {
            // Arrange
            const droppable = jasmine.createSpyObj('droppable', ['removeAttr', 'off']);
            const draggable = jasmine.createSpyObj('draggable', ['removeAttr', 'off']);

            spyOn(dragAndDropService as any, 'getSelector').and.callFake((attr: any) => {
                if (attr === configurations[0].sourceSelector) {
                    return draggable;
                } else {
                    return droppable;
                }
            });

            // Act
            (dragAndDropService as any).deactivateConfiguration(configurations[0]);

            // Assert
            expect(draggable.removeAttr).toHaveBeenCalledWith(DRAGGABLE_ATTR);
            expect(draggable.off).toHaveBeenCalledWith('dragstart');
            expect(draggable.off).toHaveBeenCalledWith('dragend');

            expect(droppable.removeAttr).toHaveBeenCalledWith(DROPPABLE_ATTR);
            expect(droppable.off).toHaveBeenCalledWith('dragenter');
            expect(droppable.off).toHaveBeenCalledWith('dragover');
            expect(droppable.off).toHaveBeenCalledWith('dragleave');
            expect(droppable.off).toHaveBeenCalledWith('drop');
        });

        it('GIVEN scrolling is enabled WHEN deactivateScrolling is called THEN scrolling is disabled', () => {
            // Assert
            const configuration = configurations[2];
            expect(configuration.enableScrolling).toBe(true);

            // Act
            (dragAndDropService as any).deactivateScrolling(configuration);

            // Assert
            expect(dragAndDropScrollingService.deactivate).toHaveBeenCalled();
        });

        it('GIVEN scrolling is not enabled WHEN deactivateScrolling is called THEN scrolling is left untouched', () => {
            // Assert
            const configuration = configurations[1];
            expect(configuration.enableScrolling).toBe(false);

            // Act
            (dragAndDropService as any).deactivateScrolling(configuration);

            // Assert
            expect(dragAndDropScrollingService.deactivate).not.toHaveBeenCalled();
        });
    });

    describe('event handlers', () => {
        const configuration = jasmine.createSpyObj('configuration', [
            'startCallback',
            'stopCallback',
            'dragOverCallback',
            'dropCallback',
            'outCallback'
        ]);
        let event: any;
        let evt: any;
        let dataTransfer: any;

        beforeEach(function() {
            spyOn(dragAndDropService as any, 'setDragAndDropExecutionStatus').and.callThrough();

            dataTransfer = jasmine.createSpyObj('dataTransfer', ['setData']);
            evt = jasmine.createSpyObj('evt', [
                'preventDefault',
                'stopPropagation',
                'relatedTarget',
                'target'
            ]);
            evt.dataTransfer = dataTransfer;
            event = {
                originalEvent: evt
            };
        });

        it('WHEN onDragStart is called THEN the drag is started', () => {
            // Arrange

            // Act
            jasmine.clock().install();

            (dragAndDropService as any).onDragStart(configuration, event);

            jasmine.clock().tick(0);
            // Assert
            expect(configuration.startCallback).toHaveBeenCalledWith(evt);
            expect(dataTransfer.setData).toHaveBeenCalledWith('Text', configuration.id);
            expect((dragAndDropService as any).setDragAndDropExecutionStatus).toHaveBeenCalledWith(
                true,
                jasmine.anything()
            );
            expect(dragAndDropScrollingService.enable).toHaveBeenCalled();
        });

        it('WHEN onDragEnd is called THEN the drag is ended', () => {
            // Arrange
            (dragAndDropService as any).setDragAndDropExecutionStatus(true);

            // Act
            (dragAndDropService as any).onDragEnd(configuration, event);

            // Assert
            expect(configuration.stopCallback).toHaveBeenCalledWith(evt);
            expect((dragAndDropService as any).setDragAndDropExecutionStatus).toHaveBeenCalledWith(
                false
            );
            expect(dragAndDropScrollingService.disable).toHaveBeenCalled();
        });

        it('WHEN mouse is over droppable area THEN drag over is handled', () => {
            // Arrange
            (dragAndDropService as any).setDragAndDropExecutionStatus(true);

            // Act
            (dragAndDropService as any).onDragOver(configuration, event);

            // Assert
            expect(evt.preventDefault).toHaveBeenCalled();
            expect(configuration.dragOverCallback).toHaveBeenCalledWith(evt);
        });

        it('WHEN mouse is released THEN on drop is handled', () => {
            // Arrange
            (dragAndDropService as any).setDragAndDropExecutionStatus(true);

            // Act
            (dragAndDropService as any).onDrop(configuration, event);

            // Assert;
            expect(evt.preventDefault).toHaveBeenCalled();
            expect(evt.stopPropagation).toHaveBeenCalled();
            expect(configuration.dropCallback).toHaveBeenCalledWith(evt);
        });

        it('onDragLeave', () => {
            // Arrange
            (dragAndDropService as any).setDragAndDropExecutionStatus(true);

            // Act
            (dragAndDropService as any).onDragLeave(configuration, event);

            // Assert
            expect(evt.preventDefault).toHaveBeenCalled();
            expect(configuration.outCallback).toHaveBeenCalledWith(evt);
        });
    });

    it('WHEN markDragStarted is executed THEN drag and drop is started.', () => {
        // Arrange
        spyOn(dragAndDropService as any, 'setDragAndDropExecutionStatus');

        // Act
        dragAndDropService.markDragStarted();

        // Assert
        expect((dragAndDropService as any).setDragAndDropExecutionStatus).toHaveBeenCalledWith(
            true
        );
        expect(dragAndDropScrollingService.enable).toHaveBeenCalled();
    });

    it('WHEN markDragStopped is executed THEN drag and drop is stopped.', () => {
        // Arrange
        spyOn(dragAndDropService as any, 'setDragAndDropExecutionStatus');

        // Act
        (dragAndDropService as any).markDragStopped();

        // Assert
        expect((dragAndDropService as any).setDragAndDropExecutionStatus).toHaveBeenCalledWith(
            false
        );
        expect(dragAndDropScrollingService.disable).toHaveBeenCalled();
    });

    it('GIVEN registered helper function returns an image path WHEN cacheDragImages is called THEN it caches an image with that path', () => {
        // Arrange
        const imageSrc = 'somePath';
        const configuration: any = {
            helper() {
                return imageSrc;
            }
        };

        // Act
        (dragAndDropService as any).cacheDragImages(configuration);

        // Assert
        expect(configuration._cachedDragImage).not.toBeNull();
        expect(configuration._cachedDragImage.src).toContain(imageSrc);
    });

    it('GIVEN registered helper function returns an image WHEN cacheDragImages is called THEN it caches that image', () => {
        // Arrange
        const image = new Image();
        const configuration: any = {
            helper() {
                return image;
            }
        };

        // Act
        (dragAndDropService as any).cacheDragImages(configuration);

        // Assert
        expect(configuration._cachedDragImage).not.toBeNull();
        expect(configuration._cachedDragImage).toBe(image);
    });

    // Helper method
    function getNumPropertiesInObject(obj: any) {
        return Object.keys(obj).length;
    }
});
