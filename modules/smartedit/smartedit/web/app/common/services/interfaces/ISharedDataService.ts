/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Cloneable } from '@smart/utils';

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:ISharedDataService
 *
 * @description
 * Provides an abstract extensible shared data service. Used to store any data to be used either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
export abstract class ISharedDataService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ISharedDataService#get
     * @methodOf smarteditServicesModule.interface:ISharedDataService
     *
     * @description
     * Get the data for the given key.
     *
     * @param {String} key The key of the data to fetch
     */
    get(key: string): Promise<Cloneable> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ISharedDataService#set
     * @methodOf smarteditServicesModule.interface:ISharedDataService
     *
     * @description
     * Set data for the given key.
     *
     * @param {String} key The key of the data to set
     * @param {object} value The value of the data to set
     */
    set(key: string, value: Cloneable): Promise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ISharedDataService#update
     * @methodOf smarteditServicesModule.interface:ISharedDataService
     *
     * @description
     * Convenience method to retrieve and modify on the fly the content stored under a given key
     *
     * @param {String} key The key of the data to store
     * @param {Function} modifyingCallback callback fed with the value stored under the given key. The callback must return the new value of the object to update.
     */
    update(key: string, modifyingCallback: (oldValue: any) => any): Promise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ISharedDataService#remove
     * @methodOf smarteditServicesModule.interface:ISharedDataService
     *
     * @description
     * Remove the entry for the given key.
     *
     * @param {String} key The key of the data to remove.
     * @returns {Promise<Cloneable>} A promise which resolves to the removed data for the given key.
     */
    remove(key: string): Promise<Cloneable> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ISharedDataService#containsKey
     * @methodOf smarteditServicesModule.interface:ISharedDataService
     *
     * @description
     * Checks the given key exists or not.
     *
     * @param {String} key The key of the data to check.
     * @returns {Promise<boolean>} A promise which resolves to true if the given key is found. Otherwise false.
     */
    containsKey(key: string): Promise<boolean> {
        'proxyFunction';
        return null;
    }
}
