/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { IRestService, IRestServiceFactory, ISessionService } from 'smarteditcommons';
import { TypePermissionsRestService } from 'cmscommons/services/TypePermissionsRestService';

describe('TypePermissionsRestService', () => {
    // --------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------
    const restServiceFactory: jasmine.SpyObj<IRestServiceFactory> = jasmine.createSpyObj<
        IRestServiceFactory
    >('restServiceFactory', ['get']);
    const typePermissionsRestResource: jasmine.SpyObj<IRestService<any>> = jasmine.createSpyObj<
        IRestService<any>
    >('typePermissionsRestResource', ['get']);

    const sessionService: any = jasmine.createSpyObj<ISessionService>('sessionService', [
        'getCurrentUsername'
    ]);

    const $log = jasmine.createSpyObj<angular.ILogService>('$log', ['error']);

    const typeCodeA = 'typeA';
    const typeCodeB = 'typeB';

    // --------------------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------------------
    let typePermissionsRestService: TypePermissionsRestService;
    let typeABPermissionResult: any;

    beforeEach(() => {
        typeABPermissionResult = {
            permissionsList: [
                {
                    id: typeCodeA,
                    permissions: [
                        {
                            key: 'read',
                            value: 'true'
                        },
                        {
                            key: 'change',
                            value: 'false'
                        },
                        {
                            key: 'create',
                            value: 'false'
                        },
                        {
                            key: 'remove',
                            value: 'true'
                        }
                    ]
                },
                {
                    id: typeCodeB,
                    permissions: [
                        {
                            key: 'read',
                            value: 'true'
                        },
                        {
                            key: 'change',
                            value: 'true'
                        },
                        {
                            key: 'create',
                            value: 'false'
                        },
                        {
                            key: 'remove',
                            value: 'false'
                        }
                    ]
                }
            ]
        };

        restServiceFactory.get.and.returnValue(typePermissionsRestResource);
        typePermissionsRestResource.get.and.returnValue(Promise.resolve(typeABPermissionResult));
        sessionService.getCurrentUsername.and.returnValue(Promise.resolve('someUser'));

        // call service
        typePermissionsRestService = new TypePermissionsRestService(
            $log,
            sessionService,
            restServiceFactory
        );
    });

    it(`GIVEN types exist
        WHEN hasCreatePermissionForTypes is called
        THEN should return TypedMap object`, async (done) => {
        const value = await typePermissionsRestService.hasCreatePermissionForTypes([
            typeCodeA,
            typeCodeB
        ]);

        expect(value).toEqual(
            jasmine.objectContaining({
                typeA: false,
                typeB: false
            })
        );
        done();
    });

    it(`GIVEN types exist
        WHEN hasReadPermissionForTypes is called
        THEN should return TypedMap object`, async (done) => {
        const value = await typePermissionsRestService.hasReadPermissionForTypes([
            typeCodeA,
            typeCodeB
        ]);

        expect(value).toEqual(
            jasmine.objectContaining({
                typeA: true,
                typeB: true
            })
        );
        done();
    });

    it(`GIVEN types exist
        WHEN hasUpdatePermissionForTypes is called
        THEN should return TypedMap object`, async (done) => {
        const value = await typePermissionsRestService.hasUpdatePermissionForTypes([
            typeCodeA,
            typeCodeB
        ]);

        expect(value).toEqual(
            jasmine.objectContaining({
                typeA: false,
                typeB: true
            })
        );
        done();
    });

    it(`GIVEN types exist
        WHEN hasDeletePermissionForTypes is called
        THEN should return TypedMap object`, async (done) => {
        const value = await typePermissionsRestService.hasDeletePermissionForTypes([
            typeCodeA,
            typeCodeB
        ]);

        expect(value).toEqual(
            jasmine.objectContaining({
                typeA: true,
                typeB: false
            })
        );
        done();
    });

    it(`GIVEN types doesnot exist
        WHEN hasDeletePermissionForTypes is called
        THEN promise should be rejected`, async (done) => {
        typePermissionsRestResource.get.and.returnValue(Promise.reject('rejected'));

        try {
            await typePermissionsRestService.hasDeletePermissionForTypes([typeCodeA, typeCodeB]);
        } catch (e) {
            expect(e).toEqual('rejected');
            done();
        }
    });

    it(`GIVEN types exist
		WHEN hasAllPermissionsForTypes is called
		THEN should return TypedMap object`, async (done) => {
        const value = await typePermissionsRestService.hasAllPermissionsForTypes([
            typeCodeA,
            typeCodeB
        ]);

        expect(value).toEqual(
            jasmine.objectContaining({
                typeA: {
                    read: true,
                    change: false,
                    create: false,
                    remove: true
                },
                typeB: {
                    read: true,
                    change: true,
                    create: false,
                    remove: false
                }
            })
        );
        done();
    });
});
