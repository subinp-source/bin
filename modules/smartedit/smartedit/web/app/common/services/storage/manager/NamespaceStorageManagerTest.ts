/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorageManager } from '../IStorageManager';
import { IStorageOptions } from '../IStorageOptions';
import { NamespacedStorageManager } from './NamespacedStorageManager';
import { StorageNamespaceConverter } from './StorageNamespaceConverter';

import 'jasmine';

describe('NamepsaceStorageManager', () => {
    const NAMESPACE = 'someStorageNamespaceForTesting';
    const STORAGE_ID = 'sid';

    let baseSM: IStorageManager;
    let nsStorageManager: IStorageManager;

    beforeEach(() => {
        baseSM = jasmine.createSpyObj<IStorageManager>([
            'registerStorageController',
            'hasStorage',
            'getStorage',
            'deleteStorage',
            'deleteExpiredStorages'
        ]);

        nsStorageManager = new NamespacedStorageManager(baseSM, NAMESPACE);
    });

    describe('Decorates the StorageManager', () => {
        const dummyController: any = null;

        const namespacedStorageId = StorageNamespaceConverter.getNamespacedStorageId(
            NAMESPACE,
            STORAGE_ID
        );

        const storageConf: IStorageOptions = {
            storageId: STORAGE_ID,
            storageType: 'bla'
        };

        function tryFinally(fnTry: any, fnFinally: any): void {
            try {
                fnTry();
            } catch (e) {
                // ignore
            } finally {
                fnFinally();
            }
        }

        it('decorates registerStorageController method', (done) => {
            tryFinally(
                () => nsStorageManager.registerStorageController(dummyController),
                () => {
                    expect(baseSM.registerStorageController).toHaveBeenCalledWith(dummyController);
                    done();
                }
            );
        });

        it('decorates getStorage method', (done) => {
            tryFinally(
                () => nsStorageManager.getStorage(storageConf),
                () => {
                    expect(baseSM.getStorage).toHaveBeenCalledWith(
                        jasmine.objectContaining({
                            storageId: namespacedStorageId
                        })
                    );
                    done();
                }
            );
        });

        it('decorates hasStorage method', (done) => {
            tryFinally(
                () => nsStorageManager.hasStorage(STORAGE_ID),
                () => {
                    expect(baseSM.hasStorage).toHaveBeenCalledWith(namespacedStorageId);
                    done();
                }
            );
        });

        it('decorates deleteStorage method - no force', (done) => {
            tryFinally(
                () => {
                    nsStorageManager.deleteStorage(STORAGE_ID);
                },
                () => {
                    expect(baseSM.deleteStorage).toHaveBeenCalledWith(namespacedStorageId, false);
                    done();
                }
            );
        });
        it('decorates deleteStorage method - force true', (done) => {
            tryFinally(
                () => {
                    nsStorageManager.deleteStorage(STORAGE_ID, true);
                },
                () => {
                    expect(baseSM.deleteStorage).toHaveBeenCalledWith(namespacedStorageId, true);
                    done();
                }
            );
        });
        it('decorates deleteStorage method - force false', (done) => {
            tryFinally(
                () => {
                    nsStorageManager.deleteStorage(STORAGE_ID, false);
                },
                () => {
                    expect(baseSM.deleteStorage).toHaveBeenCalledWith(namespacedStorageId, false);
                    done();
                }
            );
        });

        it('decorates deleteExpiredStorages method - no force', (done) => {
            tryFinally(
                () => {
                    nsStorageManager.deleteExpiredStorages();
                },
                () => {
                    expect(baseSM.deleteExpiredStorages).toHaveBeenCalledWith(false);
                    done();
                }
            );
        });

        it('decorates deleteExpiredStorages method - force true', (done) => {
            tryFinally(
                () => {
                    nsStorageManager.deleteExpiredStorages(true);
                },
                () => {
                    expect(baseSM.deleteExpiredStorages).toHaveBeenCalledWith(true);
                    done();
                }
            );
        });

        it('decorates deleteExpiredStorages method -  force false', (done) => {
            tryFinally(
                () => {
                    nsStorageManager.deleteExpiredStorages(false);
                },
                () => {
                    expect(baseSM.deleteExpiredStorages).toHaveBeenCalledWith(false);
                    done();
                }
            );
        });
    });
});
