/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import {
    DragAndDropService,
    GatewayFactory,
    ISharedDataService,
    MessageGateway,
    SystemEventService
} from 'smarteditcommons';
import { CmsDragAndDropService } from 'cmssmarteditcontainer/components/dragAndDrop';
import { jQueryHelper, promiseHelper } from 'testhelpers';

describe('cmsDragAndDropService', () => {
    // Constants
    const ID_ATTRIBUTE = 'ID';
    const UUID_ATTRIBUTE = 'UUID';
    const TYPE_ATTRIBUTE = 'TYPE';
    const DRAG_AND_DROP_ID = 'se.cms.dragAndDrop';
    const DRAG_AND_DROP_EVENTS = {
        DRAG_STARTED: 'CMS_DRAG_STARTED',
        DRAG_STOPPED: 'CMS_DRAG_STOPPED'
    };
    const ENABLE_CLONE_ON_DROP = 'ENABLE_CLONE_ON_DROP';

    let $q: angular.IQService;

    // Variables
    let cmsDragAndDropService: any;
    let dragAndDropService: jasmine.SpyObj<DragAndDropService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let gateway: jasmine.SpyObj<MessageGateway>;
    let gatewayFactory: jasmine.SpyObj<GatewayFactory>;

    beforeEach(() => {
        $q = promiseHelper.$q();

        dragAndDropService = jasmine.createSpyObj<DragAndDropService>('dragAndDropService', [
            'register',
            'unregister',
            'apply',
            'update'
        ]);

        systemEventService = jasmine.createSpyObj<SystemEventService>('systemEventService', [
            'publishAsync'
        ]);
        sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', ['get']);

        gateway = jasmine.createSpyObj<MessageGateway>('gateway', ['publish']);
        gatewayFactory = jasmine.createSpyObj<GatewayFactory>('gatewayFactory', ['createGateway']);
        gatewayFactory.createGateway.and.returnValue(gateway);

        cmsDragAndDropService = new CmsDragAndDropService(
            jQueryHelper.jQuery(),
            dragAndDropService,
            systemEventService,
            sharedDataService,
            ID_ATTRIBUTE,
            UUID_ATTRIBUTE,
            TYPE_ATTRIBUTE,
            DRAG_AND_DROP_EVENTS,
            ENABLE_CLONE_ON_DROP,
            gatewayFactory
        );
    });

    it('WHEN cmsDragAndDropService is created THEN a gateway is created to communicate with the inner frame', () => {
        // Assert
        expect(gatewayFactory.createGateway).toHaveBeenCalledWith('cmsDragAndDrop');
        expect(cmsDragAndDropService.gateway).toBe(gateway);
    });

    describe('register', () => {
        it('WHEN register is called THEN it is registered in the base drag and drop service.', () => {
            // Arrange

            // Act
            cmsDragAndDropService.register();

            // Assert
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            expect(dragAndDropService.register).toHaveBeenCalled();
            expect(arg.id).toBe(DRAG_AND_DROP_ID);
            expect(arg.sourceSelector).toBe(
                ".smartEditComponent[data-smartedit-component-type!='ContentSlot']"
            );
            expect(arg.targetSelector).toBe('');
            expect(arg.enableScrolling).toBe(false);
        });

        it('WHEN register is called THEN it is registered with the right onStart callback.', () => {
            // Arrange
            const expectedResult = 'some result';
            spyOn(cmsDragAndDropService, 'onStart').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.startCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN it is registered with the right onStop callback.', () => {
            // Arrange
            const expectedResult = 'some result';
            spyOn(cmsDragAndDropService, 'onStop').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.stopCallback();
            expect(result).toBe(expectedResult);
        });
    });

    it('WHEN apply is called THEN the cms service is applied in the base drag and drop service', () => {
        // Arrange

        // Act
        cmsDragAndDropService.apply();

        // Assert
        expect(dragAndDropService.apply).toHaveBeenCalled();
    });

    it('WHEN update is called THEN the cms service is updated in the base drag and drop service', () => {
        // Arrange

        // Act
        cmsDragAndDropService.update();

        // Assert
        expect(dragAndDropService.update).toHaveBeenCalledWith(DRAG_AND_DROP_ID);
    });

    it('WHEN unregister is called THEN the cms service is unregistered from the base drag and drop service', () => {
        // Arrange

        // Act
        cmsDragAndDropService.unregister();

        // Assert
        expect(dragAndDropService.unregister).toHaveBeenCalledWith([DRAG_AND_DROP_ID]);
    });

    it('WHEN drag is started THEN the service informs other components', () => {
        // Arrange
        const componentInfo = {
            id: 'some id',
            uuid: 'some uuid',
            type: 'some type'
        };
        const component = jasmine.createSpyObj('component', ['attr']);
        component.attr.and.callFake((arg: string) => {
            if (arg === ID_ATTRIBUTE) {
                return componentInfo.id;
            } else if (arg === UUID_ATTRIBUTE) {
                return componentInfo.uuid;
            } else if (arg === TYPE_ATTRIBUTE) {
                return componentInfo.type;
            }
            throw new Error('attribute not found');
        });

        const event = {
            target: 'some target'
        };
        const draggedElement = {
            closest() {
                return component;
            }
        };

        spyOn(cmsDragAndDropService, 'getSelector').and.returnValue(draggedElement);
        sharedDataService.get.and.returnValue($q.when(false));

        // Act
        cmsDragAndDropService.onStart(event);

        // Assert
        expect(cmsDragAndDropService.gateway.publish).toHaveBeenCalledWith('CMS_DRAG_STARTED', {
            componentId: componentInfo.id,
            componentUuid: componentInfo.uuid,
            componentType: componentInfo.type,
            slotId: null,
            slotUuid: null,
            cloneOnDrop: false
        });
        expect(systemEventService.publishAsync).toHaveBeenCalledWith('CMS_DRAG_STARTED');
    });

    it('WHEN drag is stopped THEN the inner frame is informed', () => {
        // Arrange

        // Act
        cmsDragAndDropService.onStop();

        // Assert
        expect(cmsDragAndDropService.gateway.publish).toHaveBeenCalledWith(
            'CMS_DRAG_STOPPED',
            null
        );
    });
});
