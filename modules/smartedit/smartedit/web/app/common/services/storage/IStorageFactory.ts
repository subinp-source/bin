/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorage } from './IStorage';
import { IStorageOptions } from './IStorageOptions';

/**
 * @ngdoc interface
 * @name storage.interface:IStorageFactory
 *
 * @description
 *
 * IStorageFactory represents a typical factory of {@link storage.interface:IStorage IStorage}(s).
 *
 * See {@link smarteditServicesModule.service:storageManagerFactory storageManagerFactory}
 */
export interface IStorageFactory {
    /**
     * @ngdoc method
     * @name storage.interface:IStorageFactory#getStorage
     * @methodOf storage.interface:IStorageFactory
     *
     * @param {IStorageOptions} options - {@link storage.interface:IStorageOptions IStorageOptions}
     *
     * @return {angular.IPromise<IStorage<any, any>>} A storage instance
     */
    getStorage(configuration: IStorageOptions): Promise<IStorage<any, any>>;
}
