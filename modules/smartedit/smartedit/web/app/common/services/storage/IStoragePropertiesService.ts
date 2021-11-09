/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name storage.interface:IStoragePropertiesService
 *
 * @description
 *
 * IStoragePropertiesService defines the interface for the angularJs provider that allows you to mutate the default
 * storage properties before the storage system is initialized.
 */
export abstract class IStoragePropertiesService {
    /**
     * @ngdoc method
     * @name storage.interface:IStoragePropertiesService#getProperty
     * @methodOf storage.interface:IStoragePropertiesService
     *
     * @param {string} propertyName - A property of {@link storage.interface:IStorageProperties IStorageProperties}
     *
     * @returns {any} The value of the requested property, or undefined.
     */
    getProperty(propertyName: string): any {
        'proxyFunction';
        return null;
    }
}
