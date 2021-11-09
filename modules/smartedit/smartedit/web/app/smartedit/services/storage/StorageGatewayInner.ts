/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    Cloneable,
    GatewayProxied,
    IStorage,
    IStorageGateway,
    IStorageOptions,
    SeDowngradeService
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IStorageGateway)
@GatewayProxied('handleStorageRequest')
export class StorageGateway implements IStorageGateway {
    handleStorageRequest(
        storageConfiguration: IStorageOptions,
        method: keyof IStorage<Cloneable, Cloneable>,
        args: Cloneable[]
    ): Promise<any> {
        'proxyFunction';
        return null;
    }
}
