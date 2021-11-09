/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { IRestService, IRestServiceFactory, ISessionService, TypedMap } from 'smarteditcommons';
import { AttributePermissionsRestService } from 'cmscommons/services/AttributePermissionsRestService';
import { promiseHelper, IExtensiblePromise } from 'testhelpers';

describe('AttributePermissionsRestService', () => {
    // --------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------
    const TYPE_1 = 'typeA';
    const ATTR_1 = 'attrA';
    const ATTR_2 = 'attrB';

    const DEFAULT_USER_NAME = 'some user name';

    const $q = promiseHelper.$q();
    const $log = jasmine.createSpyObj<angular.ILogService>('$log', ['error']);

    const restServiceFactory: jasmine.SpyObj<IRestServiceFactory> = jasmine.createSpyObj<
        IRestServiceFactory
    >('restServiceFactory', ['get']);
    const restService: jasmine.SpyObj<IRestService<any>> = jasmine.createSpyObj<IRestService<any>>(
        'typePermissionsRestResource',
        ['get']
    );
    const sessionService: any = jasmine.createSpyObj<ISessionService>('sessionService', [
        'getCurrentUsername'
    ]);

    // --------------------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------------------
    let attributePermissionsRestService: AttributePermissionsRestService;
    let attributePermissionsList: any;

    beforeEach(() => {
        attributePermissionsList = {
            permissionsList: [
                {
                    id: TYPE_1 + '.' + ATTR_1,
                    permissions: [
                        {
                            key: 'read',
                            value: 'false'
                        },
                        {
                            key: 'change',
                            value: 'true'
                        }
                    ]
                },
                {
                    id: TYPE_1 + '.' + ATTR_2,
                    permissions: [
                        {
                            key: 'read',
                            value: 'true'
                        },
                        {
                            key: 'change',
                            value: 'false'
                        }
                    ]
                }
            ]
        };

        restServiceFactory.get.and.returnValue(restService);
        restService.get.and.returnValue($q.when(attributePermissionsList));
        sessionService.getCurrentUsername.and.returnValue($q.when(DEFAULT_USER_NAME));

        attributePermissionsRestService = new AttributePermissionsRestService(
            restServiceFactory,
            sessionService,
            $q,
            $log,
            lo
        );
    });

    it('GIVEN attributes are found WHEN hasReadPermissionOnAttributesInType is called THEN the promise is resolved with a typed map with the right permissions', () => {
        // WHEN
        const promise = attributePermissionsRestService.hasReadPermissionOnAttributesInType(
            TYPE_1,
            [ATTR_1, ATTR_2]
        ) as IExtensiblePromise<TypedMap<boolean>>;

        // THEN
        expect(promise.value).toEqual(
            jasmine.objectContaining({
                attrA: false,
                attrB: true
            })
        );
    });

    it('GIVEN attribute is not found WHEN hasReadPermissionOnAttributesInType is called THEN the promise is rejected', () => {
        // GIVEN
        restService.get.and.returnValue($q.reject('rejected'));

        // WHEN
        const promise = attributePermissionsRestService.hasReadPermissionOnAttributesInType(
            TYPE_1,
            [ATTR_1, ATTR_2]
        ) as IExtensiblePromise<string>;

        // THEN
        expect(promise.value).toEqual('rejected');
    });

    it('GIVEN attributes are found WHEN hasChangePermissionOnAttributesInType is called THEN the promise is rejected with the right permissions', () => {
        // WHEN
        const promise = attributePermissionsRestService.hasChangePermissionOnAttributesInType(
            TYPE_1,
            [ATTR_1, ATTR_2]
        ) as IExtensiblePromise<TypedMap<boolean>>;

        // THEN
        expect(promise.value).toEqual(
            jasmine.objectContaining({
                attrA: true,
                attrB: false
            })
        );
    });

    it('GIVEN attribute is not found WHEN hasChangePermissionOnAttributesInType is called THEN the promise is rejected', () => {
        // GIVEN
        restService.get.and.returnValue($q.reject('rejected'));

        // WHEN
        const promise = attributePermissionsRestService.hasChangePermissionOnAttributesInType(
            TYPE_1,
            [ATTR_1, ATTR_2]
        ) as IExtensiblePromise<string>;

        // THEN
        expect(promise.value).toEqual('rejected');
    });
});
