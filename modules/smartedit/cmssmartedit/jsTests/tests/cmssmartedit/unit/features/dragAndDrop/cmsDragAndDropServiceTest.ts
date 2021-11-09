/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
import * as lo from 'lodash';
import {
    DragAndDropService,
    GatewayFactory,
    IAlertService,
    IBrowserService,
    IWaitDialogService,
    MessageGateway,
    SystemEventService
} from 'smarteditcommons';
import {
    CmsDragAndDropCachedComponentHint,
    CmsDragAndDropService
} from 'cmssmartedit/services/dragAndDrop/CmsDragAndDropServiceInner';
import { jQueryHelper, promiseHelper } from 'testhelpers';
import { AssetsService, IPageContentSlotsComponentsRestService } from 'cmscommons';

describe('cmsDragAndDropService', () => {
    // Constants
    const DRAG_AND_DROP_ID = 'se.cms.dragAndDrop';

    // Variables
    let cmsDragAndDropService: any;
    let gateway: jasmine.SpyObj<MessageGateway>;
    let $q: angular.IQService;
    let jq: JQueryStatic;
    let highlightedHint: CmsDragAndDropCachedComponentHint;
    const $window = {
        pageYOffset: 1000
    } as angular.IWindowService;

    let componentHandlerService: jasmine.SpyObj<any>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
    let gatewayFactory: jasmine.SpyObj<GatewayFactory>;
    let slotRestrictionsService: any;
    let alertService: jasmine.SpyObj<IAlertService>;
    let assetsService: jasmine.SpyObj<AssetsService>;
    let browserService: jasmine.SpyObj<IBrowserService>;
    let componentEditingFacade: any;
    let waitDialogService: jasmine.SpyObj<IWaitDialogService>;
    let dragAndDropService: jasmine.SpyObj<DragAndDropService>;
    let pageContentSlotsComponentsRestService: jasmine.SpyObj<IPageContentSlotsComponentsRestService>;
    const DRAG_AND_DROP_EVENTS = {
        DRAG_STARTED: 'CMS_DRAG_STARTED',
        DRAG_STOPPED: 'CMS_DRAG_STOPPED',
        DRAG_OVER: 'CMS_DRAG_OVER',
        DRAG_LEAVE: 'CMS_DRAG_LEAVE'
    };
    const COMPONENT_REMOVED_EVENT = 'COMPONENT_REMOVED_EVENT';
    const CONTENT_SLOT_TYPE = 'CONTENT_SLOT_TYPE';
    const OVERLAY_RERENDERED_EVENT = 'overlayRerendered';
    const CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS = {
        PROCESS_COMPONENTS: 'contractChangeListenerProcessComponents',
        RESTART_PROCESS: 'contractChangeListenerRestartProcess'
    };
    const SMARTEDIT_COMPONENT_PROCESS_STATUS = 'smartEditComponentProcessStatus';
    const CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS = {
        PROCESS: 'processComponent',
        REMOVE: 'removeComponent',
        KEEP_VISIBLE: 'keepComponentVisible'
    };
    const COMPONENT_IN_SLOT_STATUS = {
        ALLOWED: 'allowed',
        DISALLOWED: 'disallowed',
        MAYBEALLOWED: 'mayBeAllowed'
    };

    const domain = '';

    beforeEach(function() {
        componentHandlerService = jasmine.createSpyObj<any>('componentHandlerService', [
            'getSlotOperationRelatedId',
            'getSlotOperationRelatedUuid',
            'getSlotOperationRelatedType',
            'getId',
            'getUuid',
            'getOverlay',
            'getCatalogVersionUuid',
            'getComponentPositionInSlot',
            'getComponentUnderSlot',
            'getComponent'
        ]);
        componentHandlerService.getCatalogVersionUuid.and.returnValue('ANY_UUID');

        systemEventService = jasmine.createSpyObj<SystemEventService>('systemEventService', [
            'subscribe',
            'publishAsync',
            'publish'
        ]);
        systemEventService.publishAsync.and.returnValue(Promise.resolve(true));
        systemEventService.publish.and.returnValue(Promise.resolve(true));

        $translate = jasmine.createSpyObj<angular.translate.ITranslateService>('$translate', [
            'instant'
        ]);
        $translate.instant.and.callFake((s: string) => {
            return s;
        });

        gateway = jasmine.createSpyObj('gateway', ['subscribe']);
        gatewayFactory = jasmine.createSpyObj<GatewayFactory>('gatewayFactory', ['createGateway']);
        gatewayFactory.createGateway.and.returnValue(gateway);

        slotRestrictionsService = jasmine.createSpyObj<any>('slotRestrictionsService', [
            'isComponentAllowedInSlot',
            'isSlotEditable'
        ]);
        alertService = jasmine.createSpyObj<IAlertService>('alertService', ['showDanger']);
        assetsService = jasmine.createSpyObj<AssetsService>('assetsService', ['getAssetsRoot']);
        browserService = jasmine.createSpyObj<IBrowserService>('browserService', ['isSafari']);
        componentEditingFacade = jasmine.createSpyObj<any>('componentEditingFacade', [
            'addNewComponentToSlot',
            'cloneExistingComponentToSlot',
            'moveComponent',
            'addExistingComponentToSlot'
        ]);
        waitDialogService = jasmine.createSpyObj<IWaitDialogService>('waitDialogService', [
            'showWaitModal',
            'hideWaitModal'
        ]);
        dragAndDropService = jasmine.createSpyObj<DragAndDropService>('dragAndDropService', [
            'register',
            'apply',
            'update',
            'unregister',
            'markDragStarted',
            'markDragStopped'
        ]);
        pageContentSlotsComponentsRestService = jasmine.createSpyObj<
            IPageContentSlotsComponentsRestService
        >('pageContentSlotsComponentsRestService', ['getComponentsForSlot']);
        pageContentSlotsComponentsRestService.getComponentsForSlot.and.returnValue(null);

        $q = promiseHelper.$q();
        jq = jQueryHelper.jQuery();

        cmsDragAndDropService = new CmsDragAndDropService(
            $q,
            $window,
            $translate,
            jq,
            dragAndDropService,
            componentHandlerService,
            systemEventService,
            gatewayFactory,
            slotRestrictionsService,
            alertService,
            assetsService,
            browserService,
            componentEditingFacade,
            waitDialogService,
            pageContentSlotsComponentsRestService,
            DRAG_AND_DROP_EVENTS,
            COMPONENT_REMOVED_EVENT,
            CONTENT_SLOT_TYPE,
            OVERLAY_RERENDERED_EVENT,
            CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS,
            SMARTEDIT_COMPONENT_PROCESS_STATUS,
            CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS,
            COMPONENT_IN_SLOT_STATUS,
            domain
        );
    });

    it('WHEN cmsDragAndDropService is created THEN the service creates a gateway to communicate with the other frame.', () => {
        // Assert
        expect(gatewayFactory.createGateway).toHaveBeenCalledWith('cmsDragAndDrop');
        expect(cmsDragAndDropService.gateway).toBe(gateway);
    });

    describe('register', () => {
        it('WHEN register is called THEN the right configuration is stored in the base drag and drop service.', () => {
            // Arrange

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            expect(arg.id).toBe(DRAG_AND_DROP_ID);
            expect(arg.sourceSelector).toEqual([
                "#smarteditoverlay .smartEditComponentX[data-smartedit-component-type!='ContentSlot'] .movebutton",
                '.movebutton'
            ]);
            expect(arg.targetSelector).toBe(
                "#smarteditoverlay .smartEditComponentX[data-smartedit-component-type='ContentSlot']"
            );
            expect(arg.enableScrolling).toBe(true);
        });

        it('WHEN register is called THEN the right onStart callback is registered', () => {
            // Arrange
            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onStart').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.startCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN the right onStop callback is registered', () => {
            // Arrange
            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onStop').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.stopCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN the right onDragEnter callback is registered', () => {
            // Arrange
            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onDragEnter').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.dragEnterCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN the right onDragOver callback is registered', () => {
            // Arrange
            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onDragOver').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.dragOverCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN the right onDragEnd callback is registered', () => {
            // Arrange
            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onStop').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.stopCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN the right onDrop callback is registered', () => {
            // Arrange
            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onDrop').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.dropCallback();
            expect(result).toBe(expectedResult);
        });

        it('WHEN register is called THEN the right helper function is registered', () => {
            // Arrange
            const expectedResult = 'some Result';
            spyOn(cmsDragAndDropService, 'getDragImageSrc').and.returnValue(expectedResult);

            // Act
            cmsDragAndDropService.register();

            // Assert
            expect(dragAndDropService.register).toHaveBeenCalled();
            const arg = dragAndDropService.register.calls.argsFor(0)[0];
            const result = arg.helper();
            expect(result).toBe(expectedResult);
        });
    });

    it('WHEN unregister is called THEN the service is cleaned.', () => {
        spyOn(cmsDragAndDropService, 'overlayRenderedUnSubscribeFn');
        spyOn(cmsDragAndDropService, 'componentRemovedUnSubscribeFn');

        // Act
        cmsDragAndDropService.unregister();

        // Assert
        expect(dragAndDropService.unregister).toHaveBeenCalledWith([DRAG_AND_DROP_ID]);

        expect(cmsDragAndDropService.overlayRenderedUnSubscribeFn).toHaveBeenCalled();
        expect(cmsDragAndDropService.componentRemovedUnSubscribeFn).toHaveBeenCalled();
    });

    describe('apply', () => {
        beforeEach(function() {
            spyOn(cmsDragAndDropService, 'addUIHelpers');
            spyOn(cmsDragAndDropService, 'initializeDragOperation');
            spyOn(cmsDragAndDropService, 'cleanDragOperation');
        });

        it('WHEN apply is called THEN the page is prepared for the drag and drop operations', () => {
            // Arrange

            // Act
            cmsDragAndDropService.apply();

            // Assert
            expect(dragAndDropService.apply).toHaveBeenCalled();
            expect(cmsDragAndDropService.addUIHelpers).toHaveBeenCalled();
            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                'overlayRerendered',
                jasmine.any(Function)
            );
            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                'COMPONENT_REMOVED_EVENT',
                jasmine.any(Function)
            );
            expect(gateway.subscribe).toHaveBeenCalledWith(
                'CMS_DRAG_STARTED',
                jasmine.any(Function)
            );
            expect(gateway.subscribe).toHaveBeenCalledWith(
                'CMS_DRAG_STOPPED',
                jasmine.any(Function)
            );
        });

        it('WHEN drag is started in the outer frame THEN the right callback is called.', () => {
            // Arrange

            // Act
            const eventId = '';
            const data = 'some data';
            cmsDragAndDropService.apply();
            const callback = gateway.subscribe.calls.argsFor(0)[1];
            callback(eventId, data);

            // Assert
            expect(dragAndDropService.markDragStarted).toHaveBeenCalled();
            expect(cmsDragAndDropService.initializeDragOperation).toHaveBeenCalledWith(data);
        });

        it('WHEN drop is stopped from the outer frame THEN the right callback is called.', () => {
            // Arrange

            // Act
            const eventId = '';
            const data = 'some data';
            cmsDragAndDropService.apply();
            const callback = gateway.subscribe.calls.argsFor(1)[1];
            callback(eventId, data);

            // Assert
            expect(dragAndDropService.markDragStopped).toHaveBeenCalled();
            expect(cmsDragAndDropService.cleanDragOperation).toHaveBeenCalled();
        });
    });

    // Event Handlers
    describe('event handlers', () => {
        let event: any;
        let component: any;
        let hint: any;
        let otherComponent: any;
        let otherHint: any;
        let slot: any;
        let componentId: any;
        let componentType: any;
        let slotId: any;
        let initialValues: any;
        let componentUuid: any;
        let slotUuid: any;

        beforeEach(function() {
            componentId = 'some component id';
            componentUuid = 'some component Uuid';
            componentType = 'some component type';
            slotId = 'some slot id';
            slotUuid = 'some slot id';

            event = {
                target: 'someTarget'
            };

            slot = jasmine.createSpyObj('slot', ['closest', 'addClass', 'removeClass']);
            slot.id = 'initial slot ID';
            slot.isAllowed = true;

            component = jasmine.createSpyObj('component', [
                'closest',
                'addClass',
                'removeClass',
                'attr'
            ]);
            component.id = 'initial component ID';
            component.original = component;

            hint = jasmine.createSpyObj('hint', ['addClass', 'removeClass']);
            hint.id = 'some hint id';
            hint.original = hint;

            component.closest.and.callFake((arg: string) => {
                if (arg === ".smartEditComponentX[data-smartedit-component-type!='ContentSlot']") {
                    return component;
                } else {
                    return slot;
                }
            });

            component.attr.and.callFake((arg: string) => {
                if (arg === 'data-component-id') {
                    return componentId;
                } else if (arg === 'data-component-uuid') {
                    return componentUuid;
                } else if (arg === 'data-component-type') {
                    return componentType;
                } else {
                    return slotId;
                }
            });

            componentHandlerService.getSlotOperationRelatedId.and.returnValue(componentId);
            componentHandlerService.getSlotOperationRelatedUuid.and.returnValue(componentUuid);
            componentHandlerService.getSlotOperationRelatedType.and.returnValue(componentType);
            componentHandlerService.getId.and.returnValue(slotId);
            componentHandlerService.getUuid.and.returnValue(slotUuid);

            initialValues = {
                hint,
                component,
                slot
            };

            cmsDragAndDropService.highlightedHint = initialValues.hint;
            cmsDragAndDropService.highlightedComponent = initialValues.component;
            cmsDragAndDropService.highlightedSlot = initialValues.slot;

            cmsDragAndDropService.dragInfo = {
                componentId: 'dragged component'
            };

            otherComponent = jasmine.createSpyObj('otherComponent', ['addClass']);
            otherComponent.id = 'other component ID';

            cmsDragAndDropService.cachedSlots = {};
            cmsDragAndDropService.cachedSlots[slotId] = {
                components: [component, otherComponent]
            };

            otherHint = jasmine.createSpyObj('otherHint', ['addClass', 'removeClass']);
            otherHint.id = 'other hint id';
            otherHint.original = otherHint;

            otherComponent.hints = [initialValues.hint, otherHint];
        });

        it('WHEN onStart is called THEN it prepares the page for the drag operation', () => {
            // Arrange
            const expectedDragInfo = {
                componentId,
                componentUuid,
                componentType,
                slotId,
                slotUuid,
                slotOperationRelatedId: componentId,
                slotOperationRelatedType: componentType
            };
            spyOn(cmsDragAndDropService, 'initializeDragOperation');
            spyOn(cmsDragAndDropService, 'getSelector').and.returnValue(component);

            // Act
            cmsDragAndDropService.onStart(event);

            // Assert
            expect(component.addClass).toHaveBeenCalledWith('component_dragged');
            expect(cmsDragAndDropService.initializeDragOperation).toHaveBeenCalledWith(
                expectedDragInfo
            );
        });

        it('GIVEN the cursor enters a slot WHEN onDragEnter is called THEN the slot is highlighted', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'highlightSlot');

            // Act
            cmsDragAndDropService.onDragEnter(event);

            // Assert
            expect(cmsDragAndDropService.highlightSlot).toHaveBeenCalledWith(event);
        });

        it('GIVEN the cursor enters a slot and if the component allowed in slot status is mayBeAllowed WHEN onDragEnter is called THEN the css classes are added to the slot', () => {
            // Arrange
            ((jq as any) as jasmine.Spy).and.callFake(() => {
                return {
                    closest: (selector: string) => {
                        if (
                            selector ===
                            ".smartEditComponentX[data-smartedit-component-type='ContentSlot']"
                        ) {
                            return slot;
                        }
                        return {};
                    }
                };
            });

            cmsDragAndDropService.highlightedSlot.isAllowed = undefined;
            cmsDragAndDropService.highlightedSlot.id = 'initial slot ID';
            slotRestrictionsService.isComponentAllowedInSlot.and.returnValue(
                $q.when(COMPONENT_IN_SLOT_STATUS.MAYBEALLOWED)
            );
            slotRestrictionsService.isSlotEditable.and.returnValue($q.when(true));
            cmsDragAndDropService.cachedSlots[slotId].id = slotId;

            // Act
            cmsDragAndDropService.onDragEnter(event);

            // Assert
            expect(slot.addClass).toHaveBeenCalledWith('over-slot-enabled');
            expect(slot.addClass).toHaveBeenCalledWith('over-slot-maybeenabled');
        });

        it('GIVEN the cursor is over a slot and the hints are already highlighted WHEN onDragOver is called THEN nothing is done', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.returnValue(true);
            spyOn(cmsDragAndDropService, 'clearHighlightedHint');
            spyOn(cmsDragAndDropService, 'clearHighlightedComponent');

            // Act
            cmsDragAndDropService.onDragOver(event);

            // Assert
            expect(cmsDragAndDropService.clearHighlightedHint).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.clearHighlightedComponent).not.toHaveBeenCalled();

            expect(cmsDragAndDropService.highlightedHint).toBe(initialValues.hint);
            expect(cmsDragAndDropService.highlightedComponent).toBe(initialValues.component);
            expect(cmsDragAndDropService.highlightedSlot).toBe(initialValues.slot);
        });

        it('GIVEN the cursor is over a slot and the hint changes WHEN onDragOver is called THEN the hints are updated', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.callFake((evt: any, item: any) => {
                if (item === initialValues.hint) {
                    return false;
                } else {
                    return true;
                }
            });
            spyOn(cmsDragAndDropService, 'clearHighlightedHint');
            spyOn(cmsDragAndDropService, 'clearHighlightedComponent');

            // Act
            cmsDragAndDropService.onDragOver(event);

            // Assert
            expect(cmsDragAndDropService.clearHighlightedHint).toHaveBeenCalled();
            expect(cmsDragAndDropService.clearHighlightedComponent).not.toHaveBeenCalled();

            expect(cmsDragAndDropService.highlightedHint).toBe(initialValues.hint);
            expect(cmsDragAndDropService.highlightedComponent).toBe(initialValues.component);
            expect(cmsDragAndDropService.highlightedSlot).toBe(initialValues.slot);
        });

        it('GIVEN the cursor is over a slot and the component changes WHEN onDragOver is called THEN the component hints are updated', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.callFake((evt: any, item: any) => {
                if (item === initialValues.hint) {
                    return false;
                } else if (item === initialValues.component) {
                    return false;
                } else if (item === otherComponent) {
                    return true;
                }

                return true;
            });
            spyOn(cmsDragAndDropService, 'clearHighlightedHint').and.callThrough();
            spyOn(cmsDragAndDropService, 'clearHighlightedComponent').and.callThrough();

            // Act
            cmsDragAndDropService.onDragOver(event);

            // Assert
            expect(cmsDragAndDropService.clearHighlightedHint).toHaveBeenCalled();
            expect(cmsDragAndDropService.clearHighlightedComponent).toHaveBeenCalled();

            expect(cmsDragAndDropService.highlightedHint).toBe(otherHint);
            expect(cmsDragAndDropService.highlightedComponent).toBe(otherComponent);
            expect(cmsDragAndDropService.highlightedSlot).toBe(initialValues.slot);
        });

        it('GIVEN the cursor is over a slot and the slot changes WHEN onDragOver is called THEN the slot hints are updated', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.callFake((evt: any, item: any) => {
                if (item === initialValues.hint) {
                    return false;
                } else if (item === initialValues.component) {
                    return false;
                } else if (item === otherComponent) {
                    return true;
                }

                return true;
            });
            spyOn(cmsDragAndDropService, 'clearHighlightedHint').and.callThrough();
            spyOn(cmsDragAndDropService, 'clearHighlightedComponent').and.callThrough();

            cmsDragAndDropService.cachedSlots = {};
            cmsDragAndDropService.cachedSlots[slotId] = {
                components: [component, otherComponent]
            };

            // Act
            cmsDragAndDropService.onDragOver(event);

            // Assert
            expect(cmsDragAndDropService.clearHighlightedHint).toHaveBeenCalled();
            expect(cmsDragAndDropService.clearHighlightedComponent).toHaveBeenCalled();

            expect(cmsDragAndDropService.highlightedHint).toBe(otherHint);
            expect(cmsDragAndDropService.highlightedComponent).toBe(otherComponent);
            expect(cmsDragAndDropService.highlightedSlot).toBe(initialValues.slot);

            expect(cmsDragAndDropService.highlightedHint.addClass).toHaveBeenCalledWith(
                'overlayDropzone--hovered'
            );
        });

        it('GIVEN the cursor is over a slot and if the component allowed in slot status is mayBeAllowed and the slot has no components WHEN onDragOver is called THEN the entire slot has to be in yellow', () => {
            // Arrange

            ((jq as any) as jasmine.Spy).and.callFake(() => {
                return {
                    closest: (selector: string) => {
                        if (
                            selector ===
                            ".smartEditComponentX[data-smartedit-component-type='ContentSlot']"
                        ) {
                            slot.id = slotId;
                            return slot;
                        }
                        return {};
                    }
                };
            });

            componentHandlerService.getId.and.callFake((_slot: any) => {
                if (_slot.id === slotId) {
                    return slotId;
                } else if (_slot.id === initialValues.slot.id) {
                    return initialValues.slot.id;
                }
                return slotId;
            });
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.callFake((evt: any, item: any) => {
                if (item === initialValues.hint) {
                    return false;
                } else if (item === initialValues.component) {
                    return false;
                } else if (item === otherComponent) {
                    return true;
                }

                return true;
            });
            spyOn(cmsDragAndDropService, 'clearHighlightedSlot').and.callThrough();
            spyOn(cmsDragAndDropService, 'clearHighlightedHint').and.callThrough();
            spyOn(cmsDragAndDropService, 'clearHighlightedComponent').and.callThrough();

            initialValues.slot = lo.cloneDeep(slot);
            cmsDragAndDropService.highlightedSlot.original = initialValues.slot;
            cmsDragAndDropService.highlightedSlot.isAllowed = undefined;

            slotRestrictionsService.isComponentAllowedInSlot.and.returnValue(
                $q.when(COMPONENT_IN_SLOT_STATUS.MAYBEALLOWED)
            );
            slotRestrictionsService.isSlotEditable.and.returnValue($q.when(true));

            cmsDragAndDropService.cachedSlots[slotId] = {
                id: slotId,
                components: [], // slot is empty with no components
                original: slot
            };

            // Act
            cmsDragAndDropService.onDragOver(event);

            // Assert
            expect(slot.addClass).toHaveBeenCalledWith('over-slot-enabled');
            expect(slot.addClass).toHaveBeenCalledWith('over-slot-maybeenabled');

            expect(cmsDragAndDropService.clearHighlightedSlot).toHaveBeenCalled();
            expect(cmsDragAndDropService.clearHighlightedHint).toHaveBeenCalled();
            expect(cmsDragAndDropService.clearHighlightedComponent).toHaveBeenCalled();

            expect(cmsDragAndDropService.highlightedHint).toBe(null);
            expect(cmsDragAndDropService.highlightedComponent).toBe(null);
            expect(cmsDragAndDropService.highlightedSlot).toBe(
                cmsDragAndDropService.cachedSlots[slotId]
            );
        });

        it('GIVEN the cursor is over the dragged element WHEN onDragOver is called THEN the component is highlighted', () => {
            // Arrange
            cmsDragAndDropService.dragInfo.slotOperationRelatedId = initialValues.component.id;
            cmsDragAndDropService.highlightedComponent = null;
            componentHandlerService.getSlotOperationRelatedId.and.returnValue(component.id);
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.callFake((evt: any, item: any) => {
                if (item === initialValues.hint) {
                    return false;
                } else if (item === initialValues.component) {
                    return true;
                } else if (item === otherComponent) {
                    return false;
                }

                return true;
            });
            spyOn(cmsDragAndDropService, 'clearHighlightedHint').and.callThrough();
            spyOn(cmsDragAndDropService, 'clearHighlightedComponent').and.callThrough();

            // Act
            cmsDragAndDropService.onDragOver(event);

            // Assert
            expect(cmsDragAndDropService.clearHighlightedHint).toHaveBeenCalled();

            expect(cmsDragAndDropService.highlightedHint).toBe(null);
            expect(cmsDragAndDropService.highlightedComponent).toBe(component);
            expect(cmsDragAndDropService.highlightedSlot).toBe(initialValues.slot);

            expect(component.addClass).toHaveBeenCalledWith('component_dragged_hovered');
        });

        it('WHEN mouse leaves drag area THEN the slot is cleared', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.returnValue(false);
            spyOn(cmsDragAndDropService, 'clearHighlightedSlot').and.callThrough();
            const currentSlot = jasmine.createSpyObj('highlightedSlot', ['removeClass']);
            currentSlot.original = currentSlot;
            cmsDragAndDropService.highlightedSlot = currentSlot;

            // Act
            cmsDragAndDropService.onDragLeave();

            // Assert
            expect(cmsDragAndDropService.clearHighlightedSlot).toHaveBeenCalled();
            expect(systemEventService.publish.calls.count()).toBe(2);
            expect(systemEventService.publish.calls.argsFor(0)[0]).toEqual('HIDE_SLOT_MENU');
            expect(systemEventService.publish.calls.argsFor(1)[0]).toEqual('CMS_DRAG_LEAVE');
        });

        it('GIVEN the mouse is still in a drag area WHEN a drag leave event is triggered THEN the slot is kept highlighted', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'isMouseInRegion').and.returnValue(true);
            spyOn(cmsDragAndDropService, 'clearHighlightedSlot');

            // Act
            cmsDragAndDropService.onDragLeave(event);

            // Assert
            expect(cmsDragAndDropService.clearHighlightedSlot).not.toHaveBeenCalled();
        });

        it('WHEN onStop is called THEN it cleans the drag operation', () => {
            // Arrange
            spyOn(cmsDragAndDropService, 'cleanDragOperation');
            spyOn(cmsDragAndDropService, 'getSelector').and.returnValue(component);

            // Act
            cmsDragAndDropService.onStop(event);

            // Assert
            expect(systemEventService.publish).toHaveBeenCalledWith(
                'contractChangeListenerRestartProcess'
            );
            expect(cmsDragAndDropService.cleanDragOperation).toHaveBeenCalledWith(component);
        });
    });

    describe('onDrop', () => {
        let event: any;
        let dragInfo: any;
        let targetSlotId: any;
        let targetSlotUuid: any;
        let slotInfo: any;

        beforeEach(function() {
            spyOn(cmsDragAndDropService, 'scrollToModifiedSlot');
            componentEditingFacade.addNewComponentToSlot.and.returnValue($q.when());
            componentEditingFacade.addExistingComponentToSlot.and.returnValue($q.when());
            componentEditingFacade.moveComponent.and.returnValue($q.when());

            const expectedResult = 'someResult';
            spyOn(cmsDragAndDropService, 'onStop').and.returnValue(expectedResult);

            dragInfo = {
                slotId: 'some slot id',
                componentId: 'some component id',
                componentType: 'some component type',
                slotOperationRelatedId: 'some component id'
            };
            cmsDragAndDropService.dragInfo = dragInfo;
            cmsDragAndDropService.highlightedSlot = {
                isAllowed: true,
                components: ['some component']
            };

            event = {
                target: 'someTarget'
            };

            targetSlotId = 'some target slot id';
            targetSlotUuid = 'some target slot uuid';

            slotInfo = {
                targetSlotId,
                targetSlotUUId: targetSlotUuid
            };

            componentHandlerService.getId.and.returnValue(targetSlotId);
            componentHandlerService.getUuid.and.returnValue(targetSlotUuid);

            highlightedHint = {
                position: 2
            } as any;
        });

        it('WHEN a component is dropped outside a drop area THEN nothing happens', () => {
            // Arrange
            cmsDragAndDropService.highlightedSlot = null;

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(alertService.showDanger).not.toHaveBeenCalled();
        });

        it('WHEN a component is dropped in an invalid slot THEN an alert is displayed', () => {
            // Arrange
            cmsDragAndDropService.highlightedSlot.isAllowed = false;
            const expectedTranslation = 'se.drag.and.drop.not.valid.component.type';
            const expectedResult = {
                message: expectedTranslation
            };

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(alertService.showDanger).toHaveBeenCalledWith(expectedResult);
            expect(componentEditingFacade.addNewComponentToSlot).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.scrollToModifiedSlot).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.onStop).not.toHaveBeenCalled();
        });

        it('WHEN a new component is dropped THEN the generic editor modal is displayed for it.', () => {
            // Arrange
            cmsDragAndDropService.dragInfo.slotId = null;
            cmsDragAndDropService.dragInfo.componentId = null;
            cmsDragAndDropService.highlightedHint = highlightedHint;
            const expectedPosition = highlightedHint.position;

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).toHaveBeenCalledWith(
                slotInfo,
                'ANY_UUID',
                dragInfo.componentType,
                expectedPosition
            );
            expect(componentEditingFacade.addExistingComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.moveComponent).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN an existing component is dropped into a slot that already has an instance THEN an error message is displayed.', () => {
            // Arrange
            componentEditingFacade.addExistingComponentToSlot.and.returnValue($q.reject());
            cmsDragAndDropService.dragInfo.slotId = null;
            cmsDragAndDropService.highlightedHint = highlightedHint;
            const expectedPosition = highlightedHint.position;
            const expectedDragInfo = {
                componentId: dragInfo.componentId,
                componentUuid: dragInfo.componentUuid,
                componentType: dragInfo.componentType
            };

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addExistingComponentToSlot).toHaveBeenCalledWith(
                targetSlotId,
                expectedDragInfo,
                expectedPosition
            );
            expect(cmsDragAndDropService.onStop).toHaveBeenCalled();
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN an existing component is dropped THEN the generic editor modal is displayed for it.', () => {
            // Arrange
            cmsDragAndDropService.dragInfo.slotId = null;
            cmsDragAndDropService.highlightedHint = highlightedHint;
            const expectedPosition = highlightedHint.position;
            const expectedDragInfo = {
                componentId: dragInfo.componentId,
                componentUuid: dragInfo.componentUuid,
                componentType: dragInfo.componentType
            };

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.addExistingComponentToSlot).toHaveBeenCalledWith(
                targetSlotId,
                expectedDragInfo,
                expectedPosition
            );
            expect(componentEditingFacade.moveComponent).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN a new component is moved between slots THEN page is updated accordingly.', () => {
            // Arrange
            cmsDragAndDropService.highlightedHint = highlightedHint;
            const expectedPosition = highlightedHint.position;

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.addExistingComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.moveComponent).toHaveBeenCalledWith(
                dragInfo.slotId,
                targetSlotId,
                dragInfo.componentId,
                expectedPosition
            );
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN a new component is moved within the same slot before THEN page is updated accordingly.', () => {
            // Arrange
            const currentPosition = 3;
            componentHandlerService.getComponentPositionInSlot.and.returnValue(currentPosition);

            cmsDragAndDropService.highlightedHint = highlightedHint;
            const expectedPosition = highlightedHint.position;
            cmsDragAndDropService.dragInfo.slotId = targetSlotId;

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.addExistingComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.moveComponent).toHaveBeenCalledWith(
                dragInfo.slotId,
                targetSlotId,
                dragInfo.componentId,
                expectedPosition
            );
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN a new component is moved within the same slot after THEN page is updated accordingly.', () => {
            // Arrange
            const currentPosition = 1;
            componentHandlerService.getComponentPositionInSlot.and.returnValue(currentPosition);

            cmsDragAndDropService.highlightedHint = highlightedHint;
            const expectedPosition = highlightedHint.position - 1;
            cmsDragAndDropService.dragInfo.slotId = targetSlotId;

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.addExistingComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.moveComponent).toHaveBeenCalledWith(
                dragInfo.slotId,
                targetSlotId,
                dragInfo.componentId,
                expectedPosition
            );
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN a new component is dropped on an empty slot THEN the generic editor modal is displayed for it.', () => {
            // Arrange
            cmsDragAndDropService.dragInfo.slotId = null;
            cmsDragAndDropService.dragInfo.componentId = null;
            cmsDragAndDropService.highlightedSlot.components = [];
            const expectedPosition = 0;

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).toHaveBeenCalledWith(
                slotInfo,
                'ANY_UUID',
                dragInfo.componentType,
                expectedPosition
            );
            expect(componentEditingFacade.addExistingComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.moveComponent).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });

        it('WHEN an existing component is dropped on an empty slot THEN the generic editor modal is displayed for it.', () => {
            // Arrange
            cmsDragAndDropService.dragInfo.slotId = null;
            cmsDragAndDropService.highlightedSlot.components = [];
            const expectedPosition = 0;
            const expectedDragInfo = {
                componentId: dragInfo.componentId,
                componentUuid: dragInfo.componentUuid,
                componentType: dragInfo.componentType
            };

            // Act
            cmsDragAndDropService.onDrop(event);

            // Assert
            expect(componentEditingFacade.addNewComponentToSlot).not.toHaveBeenCalled();
            expect(componentEditingFacade.addExistingComponentToSlot).toHaveBeenCalledWith(
                targetSlotId,
                expectedDragInfo,
                expectedPosition
            );
            expect(componentEditingFacade.moveComponent).not.toHaveBeenCalled();
            expect(cmsDragAndDropService.scrollToModifiedSlot).toHaveBeenCalledWith(targetSlotId);
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();
        });
    });

    it('WHEN a hint is cleared THEN the classes are removed', () => {
        // Arrange
        const currentHint = jasmine.createSpyObj('currentHint', ['removeClass']);
        currentHint.original = currentHint;
        cmsDragAndDropService.highlightedHint = currentHint;

        // Act
        cmsDragAndDropService.clearHighlightedHint();

        // Assert
        expect(currentHint.removeClass).toHaveBeenCalledWith('overlayDropzone--hovered');
        expect(currentHint.removeClass).toHaveBeenCalledWith('overlayDropzone--mayBeAllowed');
        expect(cmsDragAndDropService.highlightedHint).toBe(null);
    });

    it('WHEN a component is cleared THEN the classes are removed', () => {
        // Arrange
        spyOn(cmsDragAndDropService, 'clearHighlightedHint');

        const currentComponent = jasmine.createSpyObj('highlightedComponent', ['removeClass']);
        currentComponent.original = currentComponent;
        cmsDragAndDropService.highlightedComponent = currentComponent;

        // Act
        cmsDragAndDropService.clearHighlightedComponent();

        // Assert
        expect(cmsDragAndDropService.clearHighlightedHint).toHaveBeenCalled();
        expect(currentComponent.removeClass).toHaveBeenCalledWith('component_dragged_hovered');
        expect(cmsDragAndDropService.highlightedComponent).toBe(null);
    });

    it('WHEN a slot is cleared THEN the classes are removed', () => {
        // Arrange
        spyOn(cmsDragAndDropService, 'clearHighlightedComponent');
        const currentSlot = jasmine.createSpyObj('highlightedSlot', ['removeClass']);
        currentSlot.original = currentSlot;
        cmsDragAndDropService.highlightedSlot = currentSlot;

        // Act
        cmsDragAndDropService.clearHighlightedSlot();

        // Assert
        expect(cmsDragAndDropService.clearHighlightedComponent).toHaveBeenCalled();
        expect(currentSlot.removeClass).toHaveBeenCalledWith('over-slot-enabled');
        expect(currentSlot.removeClass).toHaveBeenCalledWith('over-slot-disabled');
        expect(currentSlot.removeClass).toHaveBeenCalledWith('over-slot-maybeenabled');
        expect(cmsDragAndDropService.highlightedSlot).toBe(null);
    });

    // Helper Methods
    it('WHEN onOverlayUpdate is called THEN update is called', () => {
        // Arrange
        spyOn(cmsDragAndDropService, 'update');

        // Act
        const result = cmsDragAndDropService.onOverlayUpdate();

        // Assert
        result.then(function() {
            expect(cmsDragAndDropService.update).toHaveBeenCalled();
        });
    });

    it('WHEN update is called THEN the page is refreshed', () => {
        // Arrange'
        spyOn(cmsDragAndDropService, 'addUIHelpers');
        spyOn(cmsDragAndDropService, 'cacheElements');

        // Act
        cmsDragAndDropService.update();

        // Assert
        expect(dragAndDropService.update).toHaveBeenCalledWith(DRAG_AND_DROP_ID);
        expect(cmsDragAndDropService.addUIHelpers).toHaveBeenCalled();
        expect(cmsDragAndDropService.cacheElements).toHaveBeenCalled();
    });

    it('WHEN initializeDragOperation is called THEN the page is prepared for dragging components', () => {
        // Arrange
        const dragInfo = 'some drag info';
        const overlay = jasmine.createSpyObj('overlay', ['addClass']);
        componentHandlerService.getOverlay.and.returnValue(overlay);
        spyOn(cmsDragAndDropService, 'cacheElements');

        // Act
        cmsDragAndDropService.initializeDragOperation(dragInfo);

        // Assert
        expect(cmsDragAndDropService.cacheElements).toHaveBeenCalled();
        expect(cmsDragAndDropService.dragInfo).toBe(dragInfo);
        expect(overlay.addClass).toHaveBeenCalledWith('smarteditoverlay_dndRendering');
        expect(systemEventService.publishAsync).toHaveBeenCalledWith('CMS_DRAG_STARTED');
    });

    it('WHEN cleanDragOperation is called THEN the page is cleaned up', () => {
        // Arrange
        const draggedComponent = jasmine.createSpyObj('draggedComponent', ['removeClass']);
        const overlay = jasmine.createSpyObj('overlay', ['removeClass']);
        componentHandlerService.getOverlay.and.returnValue(overlay);
        spyOn(cmsDragAndDropService, 'clearHighlightedSlot');

        // Act
        cmsDragAndDropService.cleanDragOperation(draggedComponent);

        // Assert
        expect(cmsDragAndDropService.clearHighlightedSlot).toHaveBeenCalled();
        expect(draggedComponent.removeClass).toHaveBeenCalledWith('component_dragged');
        expect(overlay.removeClass).toHaveBeenCalledWith('smarteditoverlay_dndRendering');
        expect(systemEventService.publishAsync).toHaveBeenCalledWith('CMS_DRAG_STOPPED');
        expect(cmsDragAndDropService.dragInfo).toBe(null);
        expect(cmsDragAndDropService.cachedSlots).toEqual({});
        expect(cmsDragAndDropService.highlightedSlot).toBe(null);
    });

    it('GIVEN user is in Safari WHEN getDragImageSrc is called THEN it returns the right image path', () => {
        // Arrange
        const basePath = '/some_base';
        browserService.isSafari.and.returnValue(true);
        assetsService.getAssetsRoot.and.returnValue(basePath);

        const expectedPath = basePath + '/images/contextualmenu_move_on.png';

        // Act
        const resultPath = cmsDragAndDropService.getDragImageSrc();

        // Assert
        expect(resultPath).toBe(expectedPath);
    });

    it('GIVEN user is not in Safari WHEN getDragImageSrc is called THEN it returns empty image path', () => {
        // Arrange
        const expectedResult = '';

        // Act
        // - Chrome
        browserService.isSafari.and.returnValue(false);

        // Assert
        expect(cmsDragAndDropService.getDragImageSrc()).toBe(
            expectedResult,
            'No drag image needed for other browsers than safari'
        );
        expect(browserService.isSafari).toHaveBeenCalled();
    });
});
