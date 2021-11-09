/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Cloneable, IStorage, IStorageGateway, IStorageOptions } from 'smarteditcommons';

/** @internal */
export class StorageProxy<Q extends Cloneable, D extends Cloneable> implements IStorage<Q, D> {
    constructor(private configuration: IStorageOptions, private storageGateway: IStorageGateway) {}

    clear(): Promise<boolean> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'clear',
            this.arrayFromArguments(arguments)
        );
    }

    dispose(): Promise<boolean> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'dispose',
            this.arrayFromArguments(arguments)
        );
    }

    entries(): Promise<any[]> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'entries',
            this.arrayFromArguments(arguments)
        );
    }

    find(queryObject?: Q): Promise<D[]> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'find',
            this.arrayFromArguments(arguments)
        );
    }

    get(queryObject?: Q): Promise<D> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'get',
            this.arrayFromArguments(arguments)
        );
    }

    getLength(): Promise<number> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'getLength',
            this.arrayFromArguments(arguments)
        );
    }

    put(obj: D, queryObject?: Q): Promise<boolean> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'put',
            this.arrayFromArguments(arguments)
        );
    }

    remove(queryObject?: Q): Promise<D> {
        return this.storageGateway.handleStorageRequest(
            this.configuration,
            'remove',
            this.arrayFromArguments(arguments)
        );
    }

    private arrayFromArguments(args: IArguments): any[] {
        return Array.prototype.slice.call(args);
    }
}
