/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorageManager } from './IStorageManager';
import { IStorageOptions } from './IStorageOptions';

export abstract class IStorageManagerGateway extends IStorageManager {
    getStorageSanitityCheck(storageConfiguration: IStorageOptions): Promise<boolean> {
        'proxyFunction';

        return Promise.resolve(true);
    }
}
