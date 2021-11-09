/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { MemoryStorage } from '../memorystorage/MemoryStorage';

import {
    IStorage,
    IStorageController,
    IStorageOptions,
    IStoragePropertiesService
} from 'smarteditcommons';

/** @internal */
export class MemoryStorageController implements IStorageController {
    readonly storageType: string;

    private readonly storages: any = {};

    constructor(storagePropertiesService: IStoragePropertiesService) {
        this.storageType = storagePropertiesService.getProperty('STORAGE_TYPE_IN_MEMORY');
    }

    getStorage(options: IStorageOptions): Promise<IStorage<any, any>> {
        let storage: IStorage<any, any> = this.storages[options.storageId];
        if (!storage) {
            storage = new MemoryStorage();
        }
        this.storages[options.storageId] = storage;
        return Promise.resolve(storage);
    }

    deleteStorage(storageId: string): Promise<boolean> {
        delete this.storages[storageId];
        return Promise.resolve(true);
    }

    getStorageIds(): Promise<string[]> {
        return Promise.resolve(Object.keys(this.storages));
    }
}
