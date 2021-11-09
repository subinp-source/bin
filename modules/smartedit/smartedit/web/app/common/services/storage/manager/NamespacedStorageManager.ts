/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorage } from '../IStorage';
import { IStorageController } from '../IStorageController';
import { IStorageManager } from '../IStorageManager';
import { IStorageOptions } from '../IStorageOptions';
import { StorageNamespaceConverter } from './StorageNamespaceConverter';

/** @internal */
export class NamespacedStorageManager implements IStorageManager {
    constructor(private readonly storageManager: IStorageManager, private namespace: string) {}

    getStorage(storageConfiguration: IStorageOptions): Promise<IStorage<any, any>> {
        storageConfiguration.storageId = this.getNamespaceStorageId(storageConfiguration.storageId);
        return this.storageManager.getStorage(storageConfiguration);
    }

    deleteStorage(storageId: string, force: boolean = false): Promise<boolean> {
        return this.storageManager.deleteStorage(this.getNamespaceStorageId(storageId), force);
    }

    deleteExpiredStorages(force: boolean = false): Promise<boolean> {
        return this.storageManager.deleteExpiredStorages(force);
    }

    hasStorage(storageId: string): boolean {
        return this.storageManager.hasStorage(this.getNamespaceStorageId(storageId));
    }

    registerStorageController(controller: IStorageController): void {
        return this.storageManager.registerStorageController(controller);
    }

    getNamespaceStorageId(storageId: string): string {
        return StorageNamespaceConverter.getNamespacedStorageId(this.namespace, storageId);
    }

    getStorageManager(): IStorageManager {
        return this.storageManager;
    }
}
