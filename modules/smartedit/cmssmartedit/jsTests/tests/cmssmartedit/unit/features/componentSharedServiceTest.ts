/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { annotationService, GatewayProxied } from 'smarteditcommons';
import { ICMSComponent } from 'cmscommons';
import { ComponentSharedService } from 'cmssmartedit/services';
import { promiseHelper } from 'testhelpers';

describe('Component Shared Service', () => {
    // --------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------
    const COMPONENT_NAME = 'some component name';
    const COMPONENT_TYPE = 'some component type';
    const COMPONENT_UID = 'some component uid';
    const COMPONENT_UUID = 'some component uuid';
    const SLOT_UUID_1 = 'some slot uuid_1';
    const SLOT_UUID_2 = 'some slot uuid_2';

    const SLOTS_NOT_FOUND_EXCEPTION_MSG =
        'ComponentSharedService::isComponentShared - Component must have slots property.';

    const COMPONENT_WITH_NO_SLOTS: ICMSComponent = {
        name: COMPONENT_NAME,
        typeCode: COMPONENT_TYPE,
        itemtype: COMPONENT_TYPE,
        uid: COMPONENT_UID,
        uuid: COMPONENT_UUID,
        visible: true,
        cloneable: true,
        slots: undefined,
        catalogVersion: undefined
    };

    const NON_SHARED_COMPONENT: ICMSComponent = {
        name: COMPONENT_NAME,
        typeCode: COMPONENT_TYPE,
        itemtype: COMPONENT_TYPE,
        uid: COMPONENT_UID,
        uuid: COMPONENT_UUID,
        visible: true,
        cloneable: true,
        slots: [SLOT_UUID_1],
        catalogVersion: undefined
    };

    const SHARED_COMPONENT: ICMSComponent = {
        name: COMPONENT_NAME,
        typeCode: COMPONENT_TYPE,
        itemtype: COMPONENT_TYPE,
        uid: COMPONENT_UID,
        uuid: COMPONENT_UUID,
        visible: true,
        cloneable: true,
        slots: [SLOT_UUID_1, SLOT_UUID_2],
        catalogVersion: undefined
    };

    // --------------------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------------------
    const $q: jasmine.SpyObj<angular.IQService> = promiseHelper.$q();
    let componentInfoService: jasmine.SpyObj<any>;
    let componentSharedService: ComponentSharedService;

    beforeEach(() => {
        componentInfoService = jasmine.createSpyObj('componentSharedService', ['getById']);
        componentSharedService = new ComponentSharedService(componentInfoService, $q);
    });

    it('WHEN component is set-up THEN it must be proxied', () => {
        const decoratorObj = annotationService.getClassAnnotation(
            ComponentSharedService,
            GatewayProxied
        );
        expect(decoratorObj).toEqual([]);
    });

    it('GIVEN component with no slots WHEN isSlotShared is called THEN it must throw an exception', () => {
        // WHEN
        const promise = componentSharedService.isComponentShared(COMPONENT_WITH_NO_SLOTS);

        // THEN
        expect(promise).toBeRejectedWithData(SLOTS_NOT_FOUND_EXCEPTION_MSG);
    });

    it('GIVEN uuid of component with no slots WHEN isSlotShared is called THEN it must throw an exception', () => {
        // GIVEN
        componentInfoService.getById.and.returnValue($q.when(COMPONENT_WITH_NO_SLOTS));

        // WHEN
        const promise = componentSharedService.isComponentShared(COMPONENT_UUID);

        // THEN
        expect(promise).toBeRejectedWithData(SLOTS_NOT_FOUND_EXCEPTION_MSG);
    });

    it('GIVEN non-shared component WHEN isSlotShared is called THEN it must return false', () => {
        // WHEN
        const promise = componentSharedService.isComponentShared(NON_SHARED_COMPONENT);

        // THEN
        expect(promise).toBeResolvedWithData(false);
    });

    it('GIVEN uuid of non-shared component WHEN isSlotShared is called THEN it must return false', () => {
        // GIVEN
        componentInfoService.getById.and.returnValue($q.when(NON_SHARED_COMPONENT));

        // WHEN
        const promise = componentSharedService.isComponentShared(COMPONENT_UUID);

        // THEN
        expect(promise).toBeResolvedWithData(false);
    });

    it('GIVEN shared component WHEN isSlotShared is called THEN it must return true', () => {
        // WHEN
        const promise = componentSharedService.isComponentShared(SHARED_COMPONENT);

        // THEN
        expect(promise).toBeResolvedWithData(true);
    });

    it('GIVEN uuid of shared component WHEN isSlotShared is called THEN it must return true', () => {
        // GIVEN
        componentInfoService.getById.and.returnValue($q.when(SHARED_COMPONENT));

        // WHEN
        const promise = componentSharedService.isComponentShared(COMPONENT_UUID);

        // THEN
        expect(promise).toBeResolvedWithData(true);
    });
});
