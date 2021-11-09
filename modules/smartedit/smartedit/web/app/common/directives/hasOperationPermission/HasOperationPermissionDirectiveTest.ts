/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { SimpleChange, SimpleChanges, ViewContainerRef } from '@angular/core';

import { IPermissionService, MultiNamePermissionContext } from '../../services/interfaces';
import { HasOperationPermissionDirective } from './HasOperationPermissionDirective';
import { LogService, SystemEventService } from 'smarteditcommons';

function createOnChangesObject<T>(key: string, value: T): SimpleChanges {
    return {
        [key]: new SimpleChange(null, value, true)
    };
}

describe('hasOperationPermission', () => {
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

    let viewContainerRef: jasmine.SpyObj<ViewContainerRef>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let permissionService: jasmine.SpyObj<IPermissionService>;
    let logService: jasmine.SpyObj<LogService>;
    let hasOperationPermissionDirective: HasOperationPermissionDirective;

    beforeEach(() => {
        viewContainerRef = jasmine.createSpyObj('viewContainerRef', [
            'createEmbeddedView',
            'clear'
        ]);

        systemEventService = jasmine.createSpyObj('systemEventsService', ['subscribe']);
        systemEventService.subscribe.and.returnValue(() => {
            //
        });

        permissionService = jasmine.createSpyObj('permissionService', ['isPermitted']);
        permissionService.isPermitted.and.callFake((operation: MultiNamePermissionContext[]) => {
            if (JSON.stringify(operation) === JSON.stringify(FAILED_OPERATION_ARRAY)) {
                return Promise.reject(false);
            }

            return Promise.resolve(
                JSON.stringify(operation) === JSON.stringify(GRANTED_PERMISSIONS_ARRAY)
            );
        });

        logService = jasmine.createSpyObj('logService', ['error']);

        hasOperationPermissionDirective = new HasOperationPermissionDirective(
            null,
            viewContainerRef,
            systemEventService,
            permissionService,
            logService
        );
    });

    it('should call Permission Service when first instantiated', () => {
        hasOperationPermissionDirective.seHasOperationPermission = GRANTED_PERMISSION;
        hasOperationPermissionDirective.ngOnChanges(
            createOnChangesObject('seHasOperationPermission', GRANTED_PERMISSION)
        );

        expect(permissionService.isPermitted).toHaveBeenCalledWith(GRANTED_PERMISSIONS_ARRAY);
    });

    it('should attach an event listener for the authorization success event', () => {
        hasOperationPermissionDirective.ngOnInit();

        expect(systemEventService.subscribe).toHaveBeenCalledWith(
            EVENTS.PERMISSION_CACHE_CLEANED,
            jasmine.any(Function)
        );
    });

    it('should call the Permission Service when the operation is changed', () => {
        hasOperationPermissionDirective.seHasOperationPermission = DENIED_PERMISSION;
        hasOperationPermissionDirective.ngOnChanges(
            createOnChangesObject('seHasOperationPermission', DENIED_PERMISSION)
        );

        expect(permissionService.isPermitted.calls.mostRecent().args[0]).toEqual(
            DENIED_PERMISSION_ARRAY
        );
    });

    it('should throw an error if invalid permissions input is provided', () => {
        hasOperationPermissionDirective.seHasOperationPermission = INVALID_PERMISSIONS_INPUT as any;

        expect(() =>
            hasOperationPermissionDirective.ngOnChanges(
                createOnChangesObject('seHasOperationPermission', INVALID_PERMISSIONS_INPUT)
            )
        ).toThrowError('Permission should be string or an array of objects');
    });

    describe('create embedded view', () => {
        it('should create when view not exists and the operation is changed and permission is granted', (done) => {
            const isPermissionGrantedSpy = spyOn(
                hasOperationPermissionDirective as any,
                'isPermissionGranted'
            ).and.callThrough();

            hasOperationPermissionDirective.seHasOperationPermission = GRANTED_PERMISSION;
            hasOperationPermissionDirective.ngOnChanges(
                createOnChangesObject('seHasOperationPermission', GRANTED_PERMISSION)
            );

            isPermissionGrantedSpy.calls.mostRecent().returnValue.then(() => {
                expect(viewContainerRef.createEmbeddedView).toHaveBeenCalled();
                done();
            });
        });

        it('should not re-create when view already exists and the operation is changed and permission is granted', (done) => {
            const isPermissionGrantedSpy = spyOn(
                hasOperationPermissionDirective as any,
                'isPermissionGranted'
            ).and.callThrough();

            hasOperationPermissionDirective.seHasOperationPermission = GRANTED_PERMISSION;
            hasOperationPermissionDirective.ngOnChanges(
                createOnChangesObject('seHasOperationPermission', GRANTED_PERMISSION)
            );
            hasOperationPermissionDirective.ngOnChanges(
                createOnChangesObject('seHasOperationPermission', GRANTED_PERMISSION)
            );

            isPermissionGrantedSpy.calls.mostRecent().returnValue.then(() => {
                expect(viewContainerRef.createEmbeddedView).toHaveBeenCalledTimes(1);
                done();
            });
        });
    });

    describe('remove embedded view', () => {
        it('should remove when view exists and when the operation is changed and permission is not granted', (done) => {
            const isPermissionGrantedSpy = spyOn(
                hasOperationPermissionDirective as any,
                'isPermissionGranted'
            ).and.callThrough();

            hasOperationPermissionDirective.seHasOperationPermission = GRANTED_PERMISSION;
            hasOperationPermissionDirective.ngOnChanges(
                createOnChangesObject('seHasOperationPermission', GRANTED_PERMISSION)
            );

            isPermissionGrantedSpy.calls.mostRecent().returnValue.then(() => {
                expect(viewContainerRef.clear).toHaveBeenCalledTimes(0);

                hasOperationPermissionDirective.seHasOperationPermission = DENIED_PERMISSION;
                hasOperationPermissionDirective.ngOnChanges(
                    createOnChangesObject('seHasOperationPermission', DENIED_PERMISSION)
                );

                isPermissionGrantedSpy.calls.mostRecent().returnValue.then(() => {
                    expect(viewContainerRef.clear).toHaveBeenCalledTimes(1);
                    done();
                });
            });
        });
    });
});
