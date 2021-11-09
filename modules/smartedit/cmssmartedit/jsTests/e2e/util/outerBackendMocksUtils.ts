/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */
/* forbiddenNameSpaces angular.module:false */
import * as angular from 'angular';
import { SeInjectable, SeModule, SeModuleConstructor, TypedMap } from 'smarteditcommons';

@SeInjectable()
export class BackendMocksUtils {
    private _backendMocks: TypedMap<angular.mock.IRequestHandler> = {};

    storeBackendMock(key: string, backendMock: angular.mock.IRequestHandler) {
        this._backendMocks[key] = backendMock;
    }

    getBackendMock(key: string) {
        return this._backendMocks[key];
    }
}
@SeModule({
    providers: [BackendMocksUtils]
})
export class BackendMocksUtilsModule {}

const moduleName = (BackendMocksUtilsModule as SeModuleConstructor).moduleName;

angular.module('smarteditloader').requires.push(moduleName);
angular.module('smarteditcontainer').requires.push(moduleName);
