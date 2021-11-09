/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from 'smarteditcommons';

/** @internal */
export class MetaDataMapStorage<T> {
    constructor(private readonly storageKey: string) {}

    getAll(): T[] {
        const allMetaData: T[] = [];
        const data = this.getDataFromStore();
        Object.keys(data).forEach((key) => {
            allMetaData.push(data[key]);
        });
        return allMetaData;
    }

    get(storageId: string): T {
        return this.getDataFromStore()[storageId];
    }

    put(storageId: string, value: T): void {
        const data = this.getDataFromStore();
        data[storageId] = value;
        this.setDataInStore(data);
    }

    remove(storageId: string): void {
        const data = this.getDataFromStore();
        delete data[storageId];
        this.setDataInStore(data);
    }

    removeAll(): void {
        window.localStorage.removeItem(this.storageKey);
    }

    private getDataFromStore(): TypedMap<T> {
        try {
            const store = window.localStorage.getItem(this.storageKey);
            if (store === null) {
                return {};
            }
            return JSON.parse(store);
        } catch (e) {
            return {};
        }
    }

    private setDataInStore(data: TypedMap<T>): void {
        window.localStorage.setItem(this.storageKey, JSON.stringify(data));
    }
}
