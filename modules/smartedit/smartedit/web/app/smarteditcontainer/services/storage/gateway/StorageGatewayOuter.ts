/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';

import {
    Cloneable,
    DO_NOT_USE_STORAGE_MANAGER_TOKEN,
    GatewayProxied,
    IStorage,
    IStorageGateway,
    IStorageManager,
    IStorageOptions,
    SeDowngradeService
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IStorageGateway)
@GatewayProxied()
export class StorageGateway implements IStorageGateway {
    constructor(
        @Inject(DO_NOT_USE_STORAGE_MANAGER_TOKEN) private storageManager: IStorageManager
    ) {}

    handleStorageRequest(
        storageConfiguration: IStorageOptions,
        method: keyof IStorage<Cloneable, Cloneable>,
        args: Cloneable[]
    ): Promise<any> {
        return new Promise((resolve, reject) => {
            this.storageManager
                .getStorage(storageConfiguration)
                .then(
                    (storage: any) => resolve(storage[method](...args)),
                    (reason: any) => reject(reason)
                );
        });
    }
}
