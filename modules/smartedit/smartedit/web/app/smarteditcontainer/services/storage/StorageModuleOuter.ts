/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule } from '@angular/core';

import {
    diBridgeUtils,
    moduleUtils,
    DO_NOT_USE_STORAGE_MANAGER_TOKEN,
    IStorageController,
    IStorageGateway,
    IStorageManager,
    IStorageManagerFactory,
    IStorageManagerGateway,
    IStoragePropertiesService,
    LogService,
    SeConstructor,
    StorageManagerFactory,
    STORAGE_PROPERTIES_TOKEN,
    TypedMap
} from 'smarteditcommons';

import { MemoryStorageController } from './controller/memorystorage/MemoryStorageController';
import { LocalStorageController } from './controller/webstorage/LocalStorageController';
import { SessionStorageController } from './controller/webstorage/SessionStorageController';
import { StorageGateway } from './gateway/StorageGatewayOuter';
import { StorageManager } from './manager/StorageManager';
import { StoragePropertiesService } from './StoragePropertiesService';
import { StorageManagerGateway } from './gateway/StorageManagerGatewayOuter';

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
        {
            provide: IStoragePropertiesService,
            useClass: StoragePropertiesService
        },
        {
            provide: DO_NOT_USE_STORAGE_MANAGER_TOKEN,
            useClass: StorageManager
        },
        {
            provide: IStorageGateway,
            useClass: StorageGateway
        },
        {
            provide: IStorageManagerGateway,
            useClass: StorageManagerGateway
        },
        {
            provide: IStorageManagerFactory,
            useFactory: (logService: LogService, doNotUseStorageManager: IStorageManager) =>
                new StorageManagerFactory(doNotUseStorageManager),
            deps: [LogService, DO_NOT_USE_STORAGE_MANAGER_TOKEN]
        },
        {
            provide: IStorageManager,
            useFactory: (storageManagerFactory: IStorageManagerFactory) =>
                storageManagerFactory.getStorageManager('se.nsp'),
            deps: [IStorageManagerFactory]
        },
        moduleUtils.initialize(
            (
                storagePropertiesService: IStoragePropertiesService,
                seStorageManager: IStorageManager
            ) => {
                seStorageManager.registerStorageController((new LocalStorageController(
                    storagePropertiesService
                ) as unknown) as IStorageController);
                seStorageManager.registerStorageController((new SessionStorageController(
                    storagePropertiesService
                ) as unknown) as IStorageController);
                seStorageManager.registerStorageController((new MemoryStorageController(
                    storagePropertiesService
                ) as unknown) as IStorageController);

                diBridgeUtils.downgradeService(
                    'storageManagerFactory',
                    IStorageManagerFactory as SeConstructor<any>
                );
                diBridgeUtils.downgradeService('seStorageManager', IStorageManager as SeConstructor<
                    any
                >);
            },
            [IStoragePropertiesService, IStorageManager]
        )
    ]
})
export class StorageModule {
    static forRoot(properties: TypedMap<any> = {}): ModuleWithProviders {
        return {
            ngModule: StorageModule,
            providers: [
                {
                    provide: STORAGE_PROPERTIES_TOKEN,
                    multi: true,
                    useValue: properties
                }
            ]
        };
    }
}
