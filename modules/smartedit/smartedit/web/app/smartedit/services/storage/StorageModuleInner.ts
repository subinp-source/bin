/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { StorageGateway } from './StorageGatewayInner';
import { StorageManagerGateway } from './StorageManagerGatewayInner';

import {
    diBridgeUtils,
    moduleUtils,
    IStorageGateway,
    IStorageManager,
    IStorageManagerFactory,
    IStorageManagerGateway,
    SeConstructor,
    StorageManagerFactory
} from 'smarteditcommons';

@NgModule({
    providers: [
        /**
         * @ngdoc service
         * @name smarteditServicesModule.service:storageManagerFactory
         *
         * @description
         * The StorageManagerFactory implements the IStorageManagerFactory interface, and produces
         * StorageManager instances. Typically you would only create one StorageManager instance, and expose it through a
         * service for the rest of your application. StorageManagers produced from this factory will take care of
         * name-spacing storage ids, preventing clashes between extensions, or other storages with the same ID.
         * All StorageManagers produced by the storageManagerFactory delegate to the same single root StorageManager.
         *
         * Example:
         * ```
         * @SeModule(
         * 		providers:[
         * 		{
         * 			provide: "yourStorageManager",
         *    			useFactory: (storageManagerFactory: IStorageManagerFactory) => {
         * 	    				'ngInject';
         * 	    				return storageManagerFactory.getStorageManager("your_namespace");
         * 			}
         *         }
         *     ]
         * )
         * export class YourModule {}
         * ```
         */
        { provide: IStorageGateway, useValue: StorageGateway },
        { provide: IStorageManagerGateway, useValue: StorageManagerGateway },
        {
            provide: IStorageManagerFactory,
            deps: [IStorageManagerGateway],
            useFactory: (storageManagerGateway: IStorageManagerGateway) =>
                new StorageManagerFactory(storageManagerGateway)
        },
        {
            provide: IStorageManager,
            deps: [IStorageManagerFactory],
            useFactory: (storageManagerFactory: IStorageManagerFactory) =>
                storageManagerFactory.getStorageManager('se.nsp')
        },
        moduleUtils.initialize(
            (storageManagerFactory: IStorageManagerFactory, storageGateway: IStorageGateway) => {
                diBridgeUtils.downgradeService(
                    'storageManagerFactory',
                    IStorageManagerFactory as SeConstructor<any>
                );
                diBridgeUtils.downgradeService('seStorageManager', IStorageManager as SeConstructor<
                    any
                >);
            },
            [IStorageManagerFactory, IStorageGateway]
        )
    ]
})
export class StorageModule {}
