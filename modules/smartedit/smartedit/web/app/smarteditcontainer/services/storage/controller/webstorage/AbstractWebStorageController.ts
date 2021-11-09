/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WebStorage } from './WebStorage';
import { WebStorageBridge } from './WebStorageBridge';

import { IStorage, IStorageController, IStorageOptions } from 'smarteditcommons';

/** @internal */
export abstract class AbstractWebStorageController implements IStorageController {
    abstract readonly storageType: string;

    abstract getStorageApi(): Storage;

    abstract getStorageRootKey(): string;

    getStorage(configuration: IStorageOptions): Promise<IStorage<any, any>> {
        const bridge = new WebStorageBridge(this, configuration);
        const store: any = new WebStorage<any, any>(bridge, configuration);
        const oldDispose = store.dispose;
        store.dispose = () => {
            return this.deleteStorage(configuration.storageId).then(() => {
                return oldDispose();
            });
        };
        return Promise.resolve(store);
    }

    deleteStorage(storageId: string): Promise<boolean> {
        const container = this.getWebStorageContainer();
        delete container[storageId];
        this.setWebStorageContainer(container);
        return Promise.resolve(true);
    }

    getStorageIds(): Promise<string[]> {
        const keys = Object.keys(this.getWebStorageContainer());
        return Promise.resolve(keys);
    }

    saveStorageData(storageId: string, data: any): Promise<boolean> {
        const root = this.getWebStorageContainer();
        root[storageId] = data;
        this.setWebStorageContainer(root);
        return Promise.resolve(true);
    }

    getStorageData(storageId: string): Promise<any> {
        const root = this.getWebStorageContainer();
        if (root[storageId]) {
            return Promise.resolve(root[storageId]);
        }
        return Promise.resolve({});
    }

    private setWebStorageContainer(data: any): void {
        this.getStorageApi().setItem(this.getStorageRootKey(), JSON.stringify(data));
    }

    private getWebStorageContainer(): any {
        const container = this.getStorageApi().getItem(this.getStorageRootKey());
        if (!container) {
            return {};
        }
        return JSON.parse(container);
    }
}
