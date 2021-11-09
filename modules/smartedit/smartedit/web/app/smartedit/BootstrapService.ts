/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { NgModule } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { moduleUtils, SMARTEDIT_COMPONENT_NAME } from 'smarteditcommons';

class BootstrapService {
    bootstrap(): void {
        const smarteditNamespace = window.smartedit;

        // for legacy requires.push
        const modules = (smarteditNamespace.applications || [])
            .map((application: string) => {
                moduleUtils.addModuleToAngularJSApp('legacySmartedit', application);
                return moduleUtils.getNgModule(application);
            })
            .filter((module: NgModule) => !!module);

        /* forbiddenNameSpaces angular.module:false */
        angular
            .module('legacySmartedit')
            .constant('domain', smarteditNamespace.domain)
            .constant('smarteditroot', smarteditNamespace.smarteditroot);

        const constants = {
            domain: smarteditNamespace.domain,
            smarteditroot: smarteditNamespace.smarteditroot
        };

        /*
         * Bootstrap needs a reference ot the module hence cannot be achieved
         * in smarteditbootstrap.js that would then pull dependencies since it is an entry point.
         * We would then duplicate code AND kill overriding capabilities of "plugins"
         */
        document.body.appendChild(document.createElement(SMARTEDIT_COMPONENT_NAME));

        /* forbiddenNameSpaces window._:false */
        platformBrowserDynamic()
            .bootstrapModule(window.__smartedit__.SmarteditFactory({ modules, constants }))
            .then(() => {
                delete window.__smartedit__.SmarteditFactory;
            })
            .catch((err) => console.log(err));
    }
}
/** @internal */
export const bootstrapService = new BootstrapService();
