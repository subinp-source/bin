/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IPermissionsRestServiceResult,
    IRestService,
    PermissionsRestService,
    RestServiceFactory
} from 'smarteditcommons';
import { promiseHelper, PromiseType } from 'testhelpers';

describe('PermisionsRestService', () => {
    // Service Under Test
    let permissionsRestService: PermissionsRestService;

    // MOCKS
    const mockResource = jasmine.createSpyObj<IRestService<IPermissionsRestServiceResult>>(
        'mockResource',
        ['get']
    );
    const mockRestServiceFactory = jasmine.createSpyObj<RestServiceFactory>(
        'mockRestServiceFactory',
        ['get']
    );

    beforeEach(() => {
        mockRestServiceFactory.get.and.returnValue(mockResource);
        permissionsRestService = new PermissionsRestService(mockRestServiceFactory);
    });

    it('Successfully returns permissions', () => {
        const results: IPermissionsRestServiceResult = {
            permissions: [
                {
                    key: 'k1',
                    value: 'v1'
                },
                {
                    key: 'k2',
                    value: 'v2'
                }
            ]
        };
        mockResource.get.and.returnValue(
            promiseHelper.buildPromise('success', PromiseType.RESOLVES, results)
        );

        const result = permissionsRestService.get({
            user: '',
            permissionNames: ''
        });

        expect(result).toBeResolvedWithData({
            permissions: results.permissions
        });
    });

    it('Failed resource to be rejected with reason', () => {
        const failureReason = '42';
        mockResource.get.and.returnValue(
            promiseHelper.buildPromise('fail', PromiseType.REJECTS, failureReason)
        );

        const result = permissionsRestService.get({
            user: '',
            permissionNames: ''
        });

        expect(result).toBeRejectedWithData(failureReason);
    });
});
