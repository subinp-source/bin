/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorage } from './IStorage';
import { IStorageOptions } from './IStorageOptions';

import { Cloneable } from '@smart/utils';

export abstract class IStorageGateway {
    handleStorageRequest(
        storageConfiguration: IStorageOptions,
        method: keyof IStorage<Cloneable, Cloneable>,
        args: Cloneable[]
    ): Promise<any> {
        'proxyFunction';
        return Promise.resolve();
    }
}
