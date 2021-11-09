/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { AbstractWebStorageController } from './AbstractWebStorageController';
import { IStoragePropertiesService } from 'smarteditcommons';

/** @internal */
export class LocalStorageController extends AbstractWebStorageController {
    readonly storageType: string;

    constructor(private storagePropertiesService: IStoragePropertiesService) {
        super();
        this.storageType = this.storagePropertiesService.getProperty('STORAGE_TYPE_LOCAL_STORAGE');
    }

    getStorageApi(): Storage {
        return window.localStorage;
    }

    getStorageRootKey(): string {
        return this.storagePropertiesService.getProperty('LOCAL_STORAGE_ROOT_KEY');
    }
}
