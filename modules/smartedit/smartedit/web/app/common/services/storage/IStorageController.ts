/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorageFactory } from './IStorageFactory';

/**
 * @ngdoc interface
 * @name storage.interface:IStorageController
 * @description
 *  IStorageController extends {@link storage.interface:IStorageFactory IStorageFactory}
 *  The IStorageController interface represents the controller for a specific type of storage.
 *
 *  When the storage manager receives a request to access, create or remove a storage, it delegates to the storage
 *  controller of the appropriate storage type to handle these operations.
 *
 *  The controller must be able to create {@link storage.interface:IStorage storages} even across multiple instances
 *  of the smartedit application, if it handles persisted storages.
 *
 */
export interface IStorageController extends IStorageFactory {
    /**
     * @ngdoc property
     * @name storageType
     * @propertyOf storage.interface:IStorageController
     *
     * @description
     * storageType: string
     *
     * The storage type handled by this controller. A StorageManager can only have 1 controller per type registered.
     */
    readonly storageType: string;

    /**
     * @ngdoc method
     * @name storage.interface:IStorageController#getStorageIds
     * @methodOf storage.interface:IStorageController
     *
     * @returns {string[]} An array of storageId's manged by this controller
     */
    getStorageIds(): Promise<string[]>;

    /**
     * @ngdoc method
     * @name storage.interface:IStorageController#deleteStorage
     * @methodOf storage.interface:IStorageController
     *
     * @description
     * Permanently remove a storage and all its data.
     *
     * @param {string} storageId - The ID of the storage to be deleted
     *
     * @returns {Promise<boolean>} A promise resolving to true when the delete operation is complete
     */
    deleteStorage(storageId: string): Promise<boolean>;
}
