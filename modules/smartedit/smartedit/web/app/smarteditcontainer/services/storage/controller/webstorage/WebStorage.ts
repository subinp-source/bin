/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WebStorageBridge } from './WebStorageBridge';

import { Cloneable, IStorage, IStorageOptions, TypedMap } from 'smarteditcommons';

/** @internal */
export class WebStorage<Q extends Cloneable, D extends Cloneable> implements IStorage<Q, D> {
    public static ERR_INVALID_QUERY_OBJECT(queryObjec: any, storageId: string): Error {
        return new Error(
            `WebStorage exception for storage [${storageId}]. Invalid key [${queryObjec}]`
        );
    }

    constructor(
        private readonly controller: WebStorageBridge,
        private readonly storageConfiguration: IStorageOptions
    ) {}

    clear(): Promise<boolean> {
        this.controller.saveStorageData({});
        return Promise.resolve(true);
    }

    find(queryObject?: Q): Promise<D[]> {
        if (queryObject === undefined) {
            throw WebStorage.ERR_INVALID_QUERY_OBJECT(
                queryObject,
                this.storageConfiguration.storageId
            );
        }
        return this.get(queryObject).then((result) => {
            return [result];
        });
    }

    get(queryObject?: Q): Promise<D> {
        return this.controller.getStorageData().then((data) => {
            const key: string = this.getKeyFromQueryObj(queryObject);
            return Promise.resolve(data[key] as D);
        });
    }

    put(obj: D, queryObject?: Q): Promise<boolean> {
        return this.controller.getStorageData().then((data) => {
            data[this.getKeyFromQueryObj(queryObject)] = obj;
            this.controller.saveStorageData(data);
            return Promise.resolve(true);
        });
    }

    remove(queryObject?: Q): Promise<D> {
        if (queryObject === undefined) {
            throw WebStorage.ERR_INVALID_QUERY_OBJECT(
                queryObject,
                this.storageConfiguration.storageId
            );
        }
        const getPromise = this.get(queryObject);
        return this.controller.getStorageData().then((data) => {
            delete data[this.getKeyFromQueryObj(queryObject)];
            this.controller.saveStorageData(data);
            return getPromise;
        });
    }

    getLength(): Promise<number> {
        return this.controller.getStorageData().then((data) => {
            return Promise.resolve(Object.keys(data).length);
        });
    }

    dispose(): Promise<boolean> {
        return Promise.resolve(true);
    }

    entries(): Promise<any[]> {
        const entries: any[] = [];

        return new Promise((resolve) => {
            this.controller.getStorageData().then((data: TypedMap<D>) => {
                Object.keys(data).forEach((key) => {
                    entries.push([JSON.parse(key), data[key]]);
                });
                resolve(entries);
            });
        });
    }

    private getKeyFromQueryObj(queryObj: Q): string {
        return JSON.stringify(queryObj);
    }
}
