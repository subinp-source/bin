/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { Component, Injectable, Provider } from '@angular/core';
import { downgradeComponent, downgradeInjectable } from '@angular/upgrade/static';
import { SeConstructor, SeModuleConstructor } from './types';
import { parseDirectiveName } from './SeDirective';
import { functionsUtils } from '@smart/utils';
import { DIBridgeModule } from './DIBridgeModule';

/** @internal */
export class DIBridgeUtils {
    downgradeComponent(definition: Component, componentConstructor: SeConstructor): void {
        if (!functionsUtils.isUnitTestMode()) {
            const nameSet = parseDirectiveName(definition.selector, componentConstructor);

            this._getBridgeModule().directive(nameSet.name, downgradeComponent({
                component: componentConstructor
            }) as angular.IDirectiveFactory);
        }
    }

    downgradeService(name: string, serviceConstructor: SeConstructor, token?: any): void {
        if (!functionsUtils.isUnitTestMode()) {
            window.__smartedit__.downgradedService[name] = serviceConstructor;
            if (serviceConstructor) {
                const definition: Injectable = window.__smartedit__.getDecoratorPayload(
                    'Injectable',
                    serviceConstructor
                );
                if (definition && definition.providedIn !== 'root') {
                    throw new Error(
                        `service ${name} is meant to be downgraded but is not provied in root: make sure to mark it with @Injectable({providedIn: 'root'})`
                    );
                }
            }
            this._getBridgeModule().factory(name, downgradeInjectable(
                token ? token : serviceConstructor
            ) as any);
        }
    }

    /* forbiddenNameSpaces useFactory:false */
    upgradeProvider(angularJSInjectionKey: string, angularInjectionToken?: any): Provider {
        return {
            provide: angularInjectionToken ? angularInjectionToken : angularJSInjectionKey,
            useFactory: ($injector: angular.auto.IInjectorService) =>
                $injector.get(angularJSInjectionKey),
            deps: ['$injector'] // $injector is provided by UpgradeModule
        };
    }

    private _getBridgeModule(): angular.IModule {
        /* forbiddenNameSpaces angular.module:false */
        return angular.module((DIBridgeModule as SeModuleConstructor).moduleName);
    }
}

export const diBridgeUtils = new DIBridgeUtils();
