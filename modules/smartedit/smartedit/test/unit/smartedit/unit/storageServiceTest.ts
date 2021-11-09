/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { StorageService } from 'smartedit/services';
import { annotationService, GatewayProxied } from 'smarteditcommons';

describe('inner storage service', () => {
    let storageService: StorageService;

    beforeEach(() => {
        storageService = new StorageService();
    });

    it('checks GatewayProxied', () => {
        expect(annotationService.getClassAnnotation(StorageService, GatewayProxied)).toEqual([]);
    });

    it('all functions are left empty', function() {
        expect(storageService.isInitialized).toBeEmptyFunction();
        expect(storageService.storeAuthToken).toBeEmptyFunction();
        expect(storageService.getAuthToken).toBeEmptyFunction();
        expect(storageService.removeAuthToken).toBeEmptyFunction();
        expect(storageService.removeAllAuthTokens).toBeEmptyFunction();
    });
});
