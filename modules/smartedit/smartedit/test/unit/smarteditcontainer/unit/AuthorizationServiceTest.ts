/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    AuthorizationService,
    ISessionService,
    LogService,
    PermissionsRestService
} from 'smarteditcommons';
import { promiseHelper, PromiseType } from 'testhelpers';

describe('authorizationService', () => {
    // Service under test
    let authorizationService: AuthorizationService;

    // Mocks
    let mockLogService: LogService;
    let mockSessionService: jasmine.SpyObj<ISessionService>;
    let mockPermissionsRestService: jasmine.SpyObj<PermissionsRestService>;

    // Test data
    const DUMMY_USERNAME = 'dummy_username';
    const READ_PERMISSION_NAME = 'smartedit.configurationcenter.read';
    const WRITE_PERMISSION_NAME = 'smartedit.configurationcenter.write';
    const DELETE_PERMISSION_NAME = 'smartedit.configurationcenter.delete';
    const UNKNOWN_PERMISSION_NAME = 'smartedit.configurationcenter.unknown';

    beforeEach(() => {
        mockLogService = jasmine.createSpyObj<LogService>('mockLogService', ['error']);
        mockSessionService = jasmine.createSpyObj<ISessionService>('mockSessionService', [
            'getCurrentUsername'
        ]);
        mockPermissionsRestService = jasmine.createSpyObj<PermissionsRestService>(
            'mockPermissionsRestService',
            ['get']
        );

        authorizationService = new AuthorizationService(
            mockLogService,
            mockSessionService,
            mockPermissionsRestService
        );
    });

    describe('hasGlobalPermissions', () => {
        it('throws error for invalid permissionNames', () => {
            expect(() => authorizationService.hasGlobalPermissions([])).toThrowError(
                'permissionNames must be a non-empty array'
            );
        });

        it('returns false when the query to the Global Permission REST API fails', async () => {
            // Given
            const resolved = Promise.resolve(DUMMY_USERNAME);

            const rejected = promiseHelper.buildPromise(
                'rejectedRestService',
                PromiseType.REJECTS,
                'unable.to.get.permissions'
            );
            mockSessionService.getCurrentUsername.and.returnValue(resolved);
            mockPermissionsRestService.get.and.returnValue(rejected);

            // When
            const result = await authorizationService.hasGlobalPermissions([READ_PERMISSION_NAME]);

            // Then
            expect(result).toBe(false);
        });

        it('queries the Global Permissions REST API with the principal identifier and the permission names as a CSV string', async () => {
            // Given
            const permissionNames = [READ_PERMISSION_NAME, WRITE_PERMISSION_NAME];
            mockSessionService.getCurrentUsername.and.returnValue(Promise.resolve(DUMMY_USERNAME));
            mockPermissionsRestService.get.and.returnValue(Promise.resolve({ permissions: [] }));

            // When
            await authorizationService.hasGlobalPermissions(permissionNames);

            // Then
            expect(mockPermissionsRestService.get).toHaveBeenCalledWith(
                jasmine.objectContaining({
                    user: DUMMY_USERNAME,
                    permissionNames: permissionNames.join(',')
                })
            );
        });

        it('returns false when one permission is checked and is denied', async () => {
            // Given
            const permissionNames = [DELETE_PERMISSION_NAME];
            const response = {
                id: 'global',
                permissions: [
                    {
                        key: DELETE_PERMISSION_NAME,
                        value: 'false'
                    }
                ]
            };
            mockSessionService.getCurrentUsername.and.returnValue(Promise.resolve(DUMMY_USERNAME));
            mockPermissionsRestService.get.and.returnValue(Promise.resolve(response));

            // When

            const result = await authorizationService.hasGlobalPermissions(permissionNames);

            // Then
            expect(result).toBe(false);
        });

        it('returns true when one permission is checked and is granted', async () => {
            // Given
            const permissionNames = [READ_PERMISSION_NAME];
            const response = {
                id: 'global',
                permissions: [
                    {
                        key: READ_PERMISSION_NAME,
                        value: 'true'
                    }
                ]
            };

            mockSessionService.getCurrentUsername.and.returnValue(Promise.resolve(DUMMY_USERNAME));
            mockPermissionsRestService.get.and.returnValue(Promise.resolve(response));

            // When
            const result = await authorizationService.hasGlobalPermissions(permissionNames);

            // Then
            expect(result).toBe(true);
        });

        it('returns false when one of the multiple permissions checked is denied', async () => {
            // Given
            const permissionNames = [
                READ_PERMISSION_NAME,
                DELETE_PERMISSION_NAME,
                WRITE_PERMISSION_NAME
            ];
            const response = {
                id: 'global',
                permissions: [
                    {
                        key: READ_PERMISSION_NAME,
                        value: 'true'
                    },
                    {
                        key: DELETE_PERMISSION_NAME,
                        value: 'false'
                    },
                    {
                        key: WRITE_PERMISSION_NAME,
                        valeu: 'true'
                    }
                ]
            };

            mockSessionService.getCurrentUsername.and.returnValue(Promise.resolve(DUMMY_USERNAME));
            mockPermissionsRestService.get.and.returnValue(Promise.resolve(response));

            // When
            const result = await authorizationService.hasGlobalPermissions(permissionNames);

            // Then

            expect(result).toBe(false);
        });

        it('should return true if all of the multiple permissions checked are granted', async () => {
            // Given
            const permissionNames = [READ_PERMISSION_NAME, WRITE_PERMISSION_NAME];
            const response = {
                id: 'global',
                permissions: [
                    {
                        key: READ_PERMISSION_NAME,
                        value: 'true'
                    },
                    {
                        key: WRITE_PERMISSION_NAME,
                        value: 'true'
                    }
                ]
            };

            mockSessionService.getCurrentUsername.and.returnValue(Promise.resolve(DUMMY_USERNAME));
            mockPermissionsRestService.get.and.returnValue(Promise.resolve(response));

            // When
            const result = await authorizationService.hasGlobalPermissions(permissionNames);

            // Then
            expect(result).toBe(true);
        });

        it('should return false if a new requested permission is passed that does not exist in the permissions object returned from the API', async () => {
            // Given
            const permissionNames = [READ_PERMISSION_NAME, UNKNOWN_PERMISSION_NAME];
            const response = {
                id: 'global',
                permissions: [
                    {
                        key: READ_PERMISSION_NAME,
                        value: 'true'
                    }
                ]
            };

            mockSessionService.getCurrentUsername.and.returnValue(Promise.resolve(DUMMY_USERNAME));
            mockPermissionsRestService.get.and.returnValue(Promise.resolve(response));

            // When
            const result = await authorizationService.hasGlobalPermissions(permissionNames);

            // Then
            expect(result).toBe(false);
        });
    });
});
