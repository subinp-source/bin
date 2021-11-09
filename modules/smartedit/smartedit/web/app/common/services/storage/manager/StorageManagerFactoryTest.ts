/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorageManager } from '../IStorageManager';
import { StorageManagerFactory } from './StorageManagerFactory';

import 'jasmine';

describe('StorageManagerFactory', () => {
    let storageManagerFactory: StorageManagerFactory;
    let baseSM: IStorageManager;

    beforeEach(() => {
        baseSM = jasmine.createSpyObj<IStorageManager>([
            'registerStorageController',
            'hasStorage',
            'getStorage',
            'deleteStorage',
            'deleteExpiredStages'
        ]);

        storageManagerFactory = new StorageManagerFactory(baseSM);
    });

    describe('Namespace Validation', () => {
        let expectedError;

        function getStorageManager(input: any) {
            expectedError = StorageManagerFactory.ERR_INVALID_NAMESPACE(input);
            expect(() => storageManagerFactory.getStorageManager(input)).toThrowError(
                expectedError.message
            );
        }

        it('Throws error for Undefined namespace', () => {
            getStorageManager(undefined);
        });

        it('Throws error for Empty string', () => {
            getStorageManager('');
        });

        it('Throws error for Not a string namespace', () => {
            getStorageManager({});
        });

        it('Does NOT throw error for Valid namespace', () => {
            expect(() => storageManagerFactory.getStorageManager('someNs')).not.toThrow();
        });
    });
});
