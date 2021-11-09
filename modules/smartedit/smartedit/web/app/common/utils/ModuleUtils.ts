/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { Injectable, NgModule } from '@angular/core';
import { LogService, ModuleUtils as ParentModuleUtils } from '@smart/utils';

/*
 * internal utility service to handle ES6 modules
 */
/* forbiddenNameSpaces angular.module:false */
/** @internal */
@Injectable({ providedIn: 'root' })
export class ModuleUtils extends ParentModuleUtils {
    constructor(private logService: LogService) {
        super();
    }

    public getNgModule(appName: string): NgModule {
        if (window.__smartedit__.modules) {
            const moduleKey: string = Object.keys(window.__smartedit__.modules).find((key) =>
                key.endsWith(appName)
            );

            if (!moduleKey) {
                // this.logService.debug(
                // 	`No module was found under window.__smartedit__.modules.${moduleKey}
                // 	make sure to declare at least one of your import modules
                // 	with @SeModule annotation having ${appName} for its id property`
                // );
                return null;
            }
            return window.__smartedit__.modules[moduleKey];
        }
        return null;
    }

    public addModuleToAngularJSApp(legacyAngularModuleName: string, app: string) {
        try {
            angular.module(app);
            angular.module(legacyAngularModuleName).requires.push(app);
        } catch (ex) {
            this.logService.error(
                `Failed to load module ${app} into ${legacyAngularModuleName}; SmartEdit functionality may be compromised.`
            );
        }
    }
}

export const moduleUtils = new ModuleUtils(new LogService());
