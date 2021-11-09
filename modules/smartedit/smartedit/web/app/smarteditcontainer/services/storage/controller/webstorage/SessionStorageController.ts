/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { AbstractWebStorageController } from './AbstractWebStorageController';
import { IStoragePropertiesService } from 'smarteditcommons';

/** @internal */
export class SessionStorageController extends AbstractWebStorageController {
    readonly storageType: string;

    constructor(private storagePropertiesService: IStoragePropertiesService) {
        super();
        this.storageType = this.storagePropertiesService.getProperty(
            'STORAGE_TYPE_SESSION_STORAGE'
        );
    }

    getStorageApi(): Storage {
        return window.sessionStorage;
    }

    getStorageRootKey(): string {
        return this.storagePropertiesService.getProperty('SESSION_STORAGE_ROOT_KEY');
    }
}
