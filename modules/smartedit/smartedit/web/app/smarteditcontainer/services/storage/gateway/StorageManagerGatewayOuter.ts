/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';

import {
    Cloneable,
    DO_NOT_USE_STORAGE_MANAGER_TOKEN,
    GatewayProxied,
    IStorage,
    IStorageController,
    IStorageManager,
    IStorageManagerGateway,
    IStorageOptions,
    SeDowngradeService
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IStorageManagerGateway)
@GatewayProxied('getStorageSanitityCheck', 'deleteExpiredStorages', 'deleteStorage', 'hasStorage')
export class StorageManagerGateway implements IStorageManagerGateway {
    constructor(
        @Inject(DO_NOT_USE_STORAGE_MANAGER_TOKEN) private storageManager: IStorageManager
    ) {}

    getStorageSanitityCheck(storageConfiguration: IStorageOptions): Promise<boolean> {
        return this.storageManager.getStorage(storageConfiguration).then(() => true, () => false);
    }

    deleteExpiredStorages(force?: boolean): Promise<boolean> {
        return this.storageManager.deleteExpiredStorages(force);
    }

    deleteStorage(storageId: string, force?: boolean): Promise<boolean> {
        return this.storageManager.deleteStorage(storageId, force);
    }

    hasStorage(storageId: string): boolean {
        return this.storageManager.hasStorage(storageId);
    }

    getStorage(storageConfiguration: IStorageOptions): Promise<IStorage<Cloneable, Cloneable>> {
        throw new Error(
            `getStorage() is not supported from the StorageManagerGateway, please use the storage manager directly`
        );
    }

    registerStorageController(controller: IStorageController): void {
        throw new Error(
            `registerStorageController() is not supported from the StorageManagerGateway, please use the storage manager directly`
        );
    }
}
