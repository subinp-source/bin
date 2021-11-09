/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { StorageProxy } from './StorageProxy';

import {
    Cloneable,
    GatewayProxied,
    IStorage,
    IStorageController,
    IStorageGateway,
    IStorageManagerGateway,
    IStorageOptions,
    LogService,
    SeDowngradeService
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IStorageManagerGateway)
@GatewayProxied('getStorageSanitityCheck', 'deleteExpiredStorages', 'deleteStorage', 'hasStorage')
export class StorageManagerGateway implements IStorageManagerGateway {
    constructor(private $log: LogService, private storageGateway: IStorageGateway) {}

    /**
     * Disabled for inner app, due not to being able to pass storage controller instances across the gateway
     * @param {IStorageController} controller
     */
    registerStorageController(controller: IStorageController): void {
        throw new Error(
            `registerStorageController() is not supported from the smartedit (inner) application, please register controllers from smarteditContainer`
        );
    }

    getStorage(storageConfiguration: IStorageOptions): Promise<IStorage<Cloneable, Cloneable>> {
        const errMsg = `Unable to get storage ${storageConfiguration.storageId}`;

        return new Promise((resolve, reject) => {
            this.getStorageSanitityCheck(storageConfiguration).then(
                (createdSuccessfully: boolean) => {
                    if (createdSuccessfully) {
                        resolve((new StorageProxy<Cloneable, Cloneable>(
                            storageConfiguration,
                            this.storageGateway
                        ) as unknown) as IStorage<Cloneable, Cloneable>);
                    } else {
                        this.$log.error(errMsg);
                        reject(errMsg);
                    }
                },
                (result: any) => {
                    this.$log.error(errMsg);
                    this.$log.error(result);
                    reject(errMsg);
                }
            );
        });
    }

    // =============================================
    // ============= PROXIED METHODS ===============
    // =============================================

    deleteExpiredStorages(force?: boolean): Promise<boolean> {
        'proxyFunction';
        return undefined;
    }

    deleteStorage(storageId: string, force?: boolean): Promise<boolean> {
        'proxyFunction';
        return undefined;
    }

    getStorageSanitityCheck(storageConfiguration: IStorageOptions): Promise<boolean> {
        'proxyFunction';
        return undefined;
    }

    hasStorage(storageId: string): boolean {
        'proxyFunction';
        return false;
    }
}
