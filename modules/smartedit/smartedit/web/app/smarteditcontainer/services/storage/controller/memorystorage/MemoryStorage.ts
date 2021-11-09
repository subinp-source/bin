/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Cloneable, IStorage, TypedMap } from 'smarteditcommons';

/** @internal */
export class MemoryStorage<Q extends Cloneable, D extends Cloneable> implements IStorage<Q, D> {
    private data: TypedMap<D> = {};

    clear(): Promise<boolean> {
        this.data = {};
        return Promise.resolve(true);
    }

    dispose(): Promise<boolean> {
        return Promise.resolve(true);
    }

    find(queryObject?: Q): Promise<D[]> {
        return this.get(queryObject).then((result) => [result]);
    }

    get(queryObject?: Q): Promise<D> {
        return Promise.resolve(this.data[this.getKey(queryObject)]);
    }

    getLength(): Promise<number> {
        return Promise.resolve(Object.keys(this.data).length);
    }

    put(obj: D, queryObject?: Q): Promise<boolean> {
        this.data[this.getKey(queryObject)] = obj;
        return Promise.resolve(true);
    }

    remove(queryObject?: Q): Promise<D> {
        const originalData = this.data[this.getKey(queryObject)];
        delete this.data[this.getKey(queryObject)];
        return Promise.resolve(originalData);
    }

    entries(): Promise<any[]> {
        const entries: any[] = [];
        Object.keys(this.data).forEach((key) => {
            entries.push([JSON.parse(key), this.data[key]]);
        });
        return Promise.resolve(entries);
    }

    private getKey(queryObject: Q): string {
        return JSON.stringify(queryObject);
    }
}
