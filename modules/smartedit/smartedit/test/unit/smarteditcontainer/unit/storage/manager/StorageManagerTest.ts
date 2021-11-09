/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { IStorageOptions, LogService } from 'smarteditcommons';
import {
    IStorageMetaData,
    StorageManager,
    StoragePropertiesService
} from 'smarteditcontainer/services';

describe('StorageManager', () => {
    const propsService = new StoragePropertiesService([]);

    let storageManager: StorageManager;

    // Metas Data mocking
    let logService: jasmine.SpyObj<LogService>;
    let mockStorageType: string;
    let mockControllerType2: string;
    let mockStorageId: string;
    let mockStorageConfig: IStorageOptions;
    let localstorageKey: string;
    let mockMetaDataItem: IStorageMetaData;
    let mockMetaDataObject: { [index: string]: IStorageMetaData };
    let mockController: any;
    let mockController2: any;
    let mockController3: any;
    let mockStorage: any;

    beforeEach(() => {
        logService = jasmine.createSpyObj('LogService', ['log', 'warn', 'error']);

        // mock storages
        mockStorage = {
            dispose: () => {
                return Promise.resolve(true);
            }
        };

        // Mock controllers
        mockStorageType = 'someStorageType';
        mockStorageId = 'someStorageId';
        mockController = jasmine.createSpyObj(['getStorage', 'deleteStorage']);
        mockController.deleteStorage.and.returnValue(Promise.resolve(true));
        mockController.getStorage.and.returnValue(Promise.resolve(mockStorage));
        mockController.storageType = mockStorageType;
        mockControllerType2 = '2';
        mockController2 = jasmine.createSpyObj(['getStorage', 'deleteStorage']);
        mockController2.deleteStorage.and.returnValue(Promise.resolve(true));
        mockController2.getStorage.and.returnValue(Promise.resolve(mockStorage));
        mockController2.storageType = mockControllerType2;
        mockController3 = { storageType: '3' };

        // Mock config
        mockStorageConfig = {
            storageId: mockStorageId,
            storageType: mockStorageType
        };
        localstorageKey = propsService.getProperty('LOCAL_STORAGE_KEY_STORAGE_MANAGER_METADATA');
        mockMetaDataItem = {
            storageType: mockStorageType,
            storageId: mockStorageId,
            storageVersion: '0',
            lastAccess: Date.now() - 1000 * 60 // 1 min old
        };
        mockMetaDataObject = {};
        mockMetaDataObject[mockStorageId] = mockMetaDataItem;

        window.localStorage.setItem(localstorageKey, JSON.stringify(mockMetaDataObject));
        storageManager = new StorageManager(logService, propsService);
    });

    afterEach(() => {
        window.localStorage.clear();
    });

    describe('controller registration + storage type metadata', () => {
        it('Finds correct controller', (done) => {
            storageManager.registerStorageController(mockController2);
            storageManager.registerStorageController(mockController);
            storageManager.registerStorageController(mockController3);

            // don't care about errors, as long as getStorage was called on the correct storageController, then success
            storageManager.getStorage(mockStorageConfig).then(() => {
                expect(mockController.getStorage).toHaveBeenCalledWith(mockStorageConfig);
                done();
            });
        });

        it('Error on no controllers', (done) => {
            const expectedError = StorageManager.ERR_NO_STORAGE_TYPE_CONTROLLER(mockStorageType);

            storageManager.getStorage(mockStorageConfig).catch((e) => {
                expect(e).toEqual(expectedError);
                done();
            });
        });

        it('Error on no matching controller type', (done) => {
            storageManager.registerStorageController(mockController2);
            storageManager.registerStorageController(mockController3);
            const expectedError = StorageManager.ERR_NO_STORAGE_TYPE_CONTROLLER(mockStorageType);

            storageManager.getStorage(mockStorageConfig).catch((e) => {
                expect(e).toEqual(expectedError);
                done();
            });
        });
    });

    describe('getStorage', () => {
        it('Gets the storage from the controller', (done) => {
            storageManager.registerStorageController(mockController);
            storageManager.getStorage(mockStorageConfig).then((storage: any) => {
                expect(storage).toBe(mockStorage);
                done();
            });
        });

        it("Correctly delegates the Storage's dispose method to the StorageManager", (done) => {
            storageManager.registerStorageController(mockController);
            storageManager.getStorage(mockStorageConfig).then((storage: any) => {
                expect(storage).toBe(mockStorage);
                spyOn(storageManager, 'deleteStorage').and.callThrough();
                storage.dispose().then(() => {
                    expect(storageManager.deleteStorage).toHaveBeenCalled();
                    done();
                });
            });
        });

        it('Subsequent storage retrievals in a single session should pull from memory instead of controller', (done) => {
            storageManager.registerStorageController(mockController);
            storageManager.getStorage(mockStorageConfig).then(() => {
                storageManager.getStorage(mockStorageConfig).then(() => {
                    expect(mockController.getStorage).toHaveBeenCalledTimes(1);
                    done();
                });
            });
        });
    });

    describe('deleteStorage', () => {
        it('Deletes storage from controller and remove storage manager metadata', (done) => {
            storageManager.registerStorageController(mockController);
            storageManager.deleteStorage(mockStorageId).then(() => {
                expect(mockController.deleteStorage).toHaveBeenCalledWith(mockStorageId);
                expect(JSON.parse(window.localStorage.getItem(localstorageKey))).toEqual({});
                done();
            });
        });

        it('Delete force will remove metadata when controller is not available', (done) => {
            // don't register controller
            storageManager.deleteStorage(mockStorageId, true).then(() => {
                const metaData = JSON.parse(window.localStorage.getItem(localstorageKey));
                expect(metaData[mockStorageId]).toBe(undefined);
                done();
            });
        });

        it('Delete no-force will not remove metadata when controller is not available', (done) => {
            // don't register controller
            storageManager.deleteStorage(mockStorageId, false).then(() => {
                const metaData = JSON.parse(window.localStorage.getItem(localstorageKey));
                expect(metaData[mockStorageId]).toEqual(mockMetaDataItem);
                done();
            });
        });

        it('Gracefully handles unknown storageId', (done) => {
            storageManager.deleteStorage('some unknown StorageId').then(() => {
                // as long as the promise resolves, its good
                done();
            });
        });
    });

    describe('Mismatch Version, and and  Expired storages', () => {
        it('Removes expired storages', (done) => {
            // put an expired storage in local storage, and make sure its removed

            storageManager.registerStorageController(mockController);

            const expiredStorageId = 'expired';
            const expiredMockMetaDataItem: IStorageMetaData = {
                storageId: expiredStorageId,
                storageType: mockStorageType,
                storageVersion: '0',
                lastAccess: Date.UTC(2000, 1)
            };
            mockMetaDataObject = {};
            mockMetaDataObject[mockStorageId] = mockMetaDataItem;
            mockMetaDataObject[expiredStorageId] = expiredMockMetaDataItem;
            window.localStorage.setItem(localstorageKey, JSON.stringify(mockMetaDataObject));

            expect(storageManager.hasStorage(mockStorageId)).toBe(true);
            expect(storageManager.hasStorage(expiredStorageId)).toBe(true);

            // trigger SM constructor, which flushes expired caches
            storageManager.deleteExpiredStorages(true).then(() => {
                expect(storageManager.hasStorage(mockStorageId)).toBe(true);
                expect(storageManager.hasStorage(expiredStorageId)).toBe(false);
                done();
            });
        });

        it('Removes old storage on version change', (done) => {
            // get a storage with different version and expect the metaData is the updated one

            storageManager.registerStorageController(mockController);

            // the original is already persisted in localstorage at this point
            mockStorageConfig.storageVersion = 'some other version';

            spyOn(storageManager, 'deleteStorage').and.callThrough();
            storageManager.getStorage(mockStorageConfig).then(() => {
                expect(storageManager.deleteStorage).toHaveBeenCalled();

                // check if last access was updated (and this a new record was created)
                const metaData = JSON.parse(window.localStorage.getItem(localstorageKey));
                expect(metaData[mockStorageId].lastAccess).not.toEqual(mockMetaDataItem.lastAccess);
                done();
            });
        });

        it('Removes old storage on controller type change', (done) => {
            // get a storage with different version and expect the metaData is the updated one

            storageManager.registerStorageController(mockController);
            storageManager.registerStorageController(mockController2);

            // the original is already persisted in localstorage at this point
            mockStorageConfig.storageType = mockControllerType2;

            spyOn(storageManager, 'deleteStorage').and.callThrough();

            storageManager.getStorage(mockStorageConfig).then(() => {
                expect(storageManager.deleteStorage).toHaveBeenCalled();

                // check if last access was updated (and this a new record was created)
                const metaData = JSON.parse(window.localStorage.getItem(localstorageKey));
                expect(metaData[mockStorageId].lastAccess).not.toEqual(mockMetaDataItem.lastAccess);
                done();
            });
        });
    });
});
