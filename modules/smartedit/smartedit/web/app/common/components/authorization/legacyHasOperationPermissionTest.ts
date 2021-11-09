/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { LogService } from '@smart/utils';

import { IPermissionService, MultiNamePermissionContext, SystemEventService } from '../../services';
import { LegacyHasOperationPermissionDirective } from './legacyHasOperationPermission';

function createOnChangesObject<T>(key: string, value: T): angular.IOnChangesObject {
    return {
        [key]: {
            currentValue: value,
            previousValue: null,
            isFirstChange: () => true
        }
    };
}

describe('hasOperationPermissionLegacy', () => {
    const GRANTED_PERMISSION = 'GRANTED_PERMISSION';
    const DENIED_PERMISSION = 'DENIED_PERMISSION';
    const FAILED_OPERATION = 'FAILED_OPERATION';
    const EVENTS = {
        PERMISSION_CACHE_CLEANED: 'PERMISSION_CACHE_CLEANED'
    };

    const INVALID_PERMISSIONS_INPUT = {
        names: ['invalid']
    };

    const GRANTED_PERMISSIONS_ARRAY = [
        {
            names: [GRANTED_PERMISSION]
        }
    ];

    const DENIED_PERMISSION_ARRAY = [
        {
            names: [DENIED_PERMISSION]
        }
    ];

    const FAILED_OPERATION_ARRAY = [
        {
            names: [FAILED_OPERATION]
        }
    ];

    let permissionService: jasmine.SpyObj<IPermissionService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let $log: jasmine.SpyObj<LogService>;
    let hasOperationPermissionDirective: LegacyHasOperationPermissionDirective;

    beforeEach(() => {
        $log = jasmine.createSpyObj('$log', ['error']);

        systemEventService = jasmine.createSpyObj('systemEventsService', ['subscribe']);
        systemEventService.subscribe.and.returnValue(() => {
            //
        });

        permissionService = jasmine.createSpyObj('permissionService', ['isPermitted']);
        permissionService.isPermitted.and.callFake((operation: MultiNamePermissionContext[]) => {
            if (JSON.stringify(operation) === JSON.stringify(FAILED_OPERATION_ARRAY)) {
                return Promise.reject();
            }

            return Promise.resolve(
                JSON.stringify(operation) === JSON.stringify(GRANTED_PERMISSIONS_ARRAY)
            );
        });

        hasOperationPermissionDirective = new LegacyHasOperationPermissionDirective(
            systemEventService,
            permissionService,
            $log
        );
    });

    it('should call the permission service when first instantiated', () => {
        (hasOperationPermissionDirective as any).hasOperationPermission = GRANTED_PERMISSION;
        hasOperationPermissionDirective.$onChanges(
            createOnChangesObject('hasOperationPermission', GRANTED_PERMISSION)
        );

        expect(permissionService.isPermitted).toHaveBeenCalledWith(GRANTED_PERMISSIONS_ARRAY);
    });

    it('should attach an event listener for the authorization success event', () => {
        hasOperationPermissionDirective.$onInit();

        expect(systemEventService.subscribe).toHaveBeenCalledWith(
            EVENTS.PERMISSION_CACHE_CLEANED,
            jasmine.any(Function)
        );
    });

    it('should call the authorization service when the operation is changed', () => {
        (hasOperationPermissionDirective as any).hasOperationPermission = DENIED_PERMISSION;
        hasOperationPermissionDirective.$onChanges(
            createOnChangesObject('hasOperationPermission', DENIED_PERMISSION)
        );

        expect(permissionService.isPermitted.calls.mostRecent().args[0]).toEqual(
            DENIED_PERMISSION_ARRAY
        );
    });

    it('should throw an error if invalid permissions input is provided', () => {
        (hasOperationPermissionDirective as any).hasOperationPermission = INVALID_PERMISSIONS_INPUT;

        expect(() =>
            hasOperationPermissionDirective.$onChanges(
                createOnChangesObject('hasOperationPermission', INVALID_PERMISSIONS_INPUT)
            )
        ).toThrowError('Permission should be string or an array of objects');
    });
});
