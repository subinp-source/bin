/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import * as lodash from 'lodash';
import {
    Cloneable,
    GatewayProxied,
    ISharedDataService,
    SeDowngradeService,
    TypedMap
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService(ISharedDataService)
@GatewayProxied()
@Injectable()
export class SharedDataService extends ISharedDataService {
    private storage: TypedMap<Cloneable> = {};

    constructor() {
        super();
    }

    get(key: string): Promise<Cloneable> {
        return Promise.resolve(this.storage[key]);
    }

    set(key: string, value: Cloneable): Promise<void> {
        this.storage[key] = value;
        return Promise.resolve();
    }

    update(key: string, modifyingCallback: (oldValue: any) => any): Promise<void> {
        return this.get(key).then((oldValue: any) => {
            return modifyingCallback(oldValue).then((newValue: any) => {
                return this.set(key, newValue);
            });
        });
    }

    remove(key: string): Promise<Cloneable> {
        const value = this.storage[key];
        delete this.storage[key];
        return Promise.resolve(value);
    }

    containsKey(key: string): Promise<boolean> {
        return Promise.resolve(lodash.has(this.storage, key));
    }
}
