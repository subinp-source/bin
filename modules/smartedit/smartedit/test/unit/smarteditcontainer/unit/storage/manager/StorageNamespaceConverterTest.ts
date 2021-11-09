/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { StorageNamespaceConverter } from 'smarteditcommons';

describe('StorageNamespaceConverter', () => {
    const invalidId = 'invalid';
    const expectedError = StorageNamespaceConverter.ERR_INVALID_NAMESPACED_ID(invalidId);

    it('Bi-directional conversion', () => {
        const namespace = 'someNs';
        const storageId = 'someSid';
        const nsSid = StorageNamespaceConverter.getNamespacedStorageId(namespace, storageId);

        expect(StorageNamespaceConverter.getNamespaceFromNamespacedId(nsSid)).toEqual(namespace);
        expect(StorageNamespaceConverter.getStorageIdFromNamespacedId(nsSid)).toEqual(storageId);
    });

    it('Throws error for invalid namespace on getNamespaceFromNamespacedId()', () => {
        const errFn = () => StorageNamespaceConverter.getNamespaceFromNamespacedId(invalidId);
        expect(errFn).toThrowError(expectedError.message);
    });

    it('Throws error for invalid namespace on getStorageIdFromNamespacedId()', () => {
        const errFn = () => StorageNamespaceConverter.getStorageIdFromNamespacedId(invalidId);
        expect(errFn).toThrowError(expectedError.message);
    });
});
