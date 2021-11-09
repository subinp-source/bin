/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as lo from 'lodash';
import { HttpClient } from '@angular/common/http';
import {
    scriptUtils,
    BootstrapPayload,
    Cloneable,
    ISharedDataService,
    LogService,
    ModuleUtils,
    PromiseUtils,
    SeDowngradeService,
    SmarteditBootstrapGateway,
    TypedMap
} from 'smarteditcommons';
import {
    ConfigurationModules,
    Module
} from 'smarteditcontainer/services/bootstrap/ConfigurationModules';
import { ConfigurationObject } from 'smarteditcontainer/services/bootstrap/Configuration';
import { SmarteditBundle } from 'smarteditcontainer/services/bootstrap/SmarteditBundle';
import { ConfigurationExtractorService } from './ConfigurationExtractorService';

@SeDowngradeService()
export class BootstrapService {
    constructor(
        private configurationExtractorService: ConfigurationExtractorService,
        private sharedDataService: ISharedDataService,
        private logService: LogService,
        private httpClient: HttpClient,
        private promiseUtils: PromiseUtils,
        private smarteditBootstrapGateway: SmarteditBootstrapGateway,
        private moduleUtils: ModuleUtils
    ) {}

    bootstrapContainerModules(configurations: ConfigurationObject): Promise<BootstrapPayload> {
        const deferred = this.promiseUtils.defer<BootstrapPayload>();
        const seContainerModules: ConfigurationModules = this.configurationExtractorService.extractSEContainerModules(
            configurations
        );
        const orderedApplications = this.configurationExtractorService.orderApplications(
            seContainerModules.applications
        );

        this.logService.debug('outerAppLocations are:', orderedApplications);

        this.sharedDataService.set('authenticationMap', seContainerModules.authenticationMap);
        this.sharedDataService.set('credentialsMap', configurations['authentication.credentials']);

        const constants = this._getConstants(configurations);

        Object.keys(constants).forEach((key) => {
            this._getLegacyContainerModule().constant(key, constants[key]);
        });

        this._getValidApplications(orderedApplications).then((validApplications: Module[]) => {
            scriptUtils.injectJS().execute({
                srcs: validApplications.map((app) => app.location),
                callback: () => {
                    const modules = [...window.__smartedit__.pushedModules];

                    validApplications.forEach((app) => {
                        this.moduleUtils.addModuleToAngularJSApp('smarteditcontainer', app.name);
                        const esModule = this.moduleUtils.getNgModule(app.name);
                        if (esModule) {
                            modules.push(esModule);
                        }
                    });
                    deferred.resolve({
                        modules,
                        constants
                    });
                }
            });
        });

        return deferred.promise;
    }
    /**
     * Retrieve SmartEdit inner application configuration and dispatch 'bundle' event with list of resources.
     * @param configurations
     */
    bootstrapSEApp(configurations: ConfigurationObject): Promise<void> {
        const seModules: ConfigurationModules = this.configurationExtractorService.extractSEModules(
            configurations
        );
        const orderedApplications = this.configurationExtractorService.orderApplications(
            seModules.applications
        );

        this.sharedDataService.set('authenticationMap', seModules.authenticationMap);
        this.sharedDataService.set('credentialsMap', configurations['authentication.credentials']);

        const resources = {
            properties: this._getConstants(configurations),
            js: [
                {
                    src:
                        configurations.smarteditroot +
                        '/static-resources/thirdparties/blockumd/blockumd.js'
                },
                {
                    src:
                        configurations.smarteditroot +
                        '/static-resources/dist/smartedit/js/prelibraries.js'
                },
                {
                    src:
                        configurations.smarteditroot +
                        '/static-resources/dist/smarteditcommons/js/vendor_chunk.js'
                },
                {
                    src:
                        configurations.smarteditroot +
                        '/static-resources/thirdparties/ckeditor/ckeditor.js'
                },
                {
                    src:
                        configurations.smarteditroot +
                        '/static-resources/dist/smarteditcommons/js/smarteditcommons.js'
                },
                {
                    src:
                        configurations.smarteditroot +
                        '/static-resources/dist/smartedit/js/smartedit.js'
                }
            ]
        } as SmarteditBundle;

        return this._getValidApplications(orderedApplications).then(
            (validApplications: Module[]) => {
                resources.js = resources.js.concat(
                    validApplications.map((app) => {
                        const source = { src: app.location };
                        return source;
                    })
                );
                resources.js.push({
                    src:
                        configurations.smarteditroot +
                        '/static-resources/dist/smartedit/js/smarteditbootstrap.js'
                });
                resources.properties.applications = validApplications.map((app) => app.name);

                this.smarteditBootstrapGateway
                    .getInstance()
                    .publish('bundle', ({ resources } as unknown) as Cloneable);
            }
        );
    }

    private _getLegacyContainerModule() {
        /* forbiddenNameSpaces angular.module:false */
        return angular.module('smarteditcontainer');
    }

    private _getConstants(configurations: ConfigurationObject): TypedMap<string> {
        return {
            domain: configurations.domain as string,
            smarteditroot: configurations.smarteditroot as string
        };
    }
    /**
     * Applications are considered valid if they can be retrieved over the wire
     */
    private _getValidApplications(applications: Module[]): Promise<Module[]> {
        return Promise.all(
            applications.map((application) => {
                const deferred = this.promiseUtils.defer();
                this.httpClient.get(application.location, { responseType: 'text' }).subscribe(
                    () => {
                        deferred.resolve(application);
                    },
                    (e) => {
                        this.logService.error(
                            `Failed to load application '${application.name}' from location ${
                                application.location
                            }; SmartEdit functionality may be compromised.`
                        );
                        deferred.resolve();
                    }
                );
                return deferred.promise;
            })
        ).then((validApplications: any) => {
            return lo.merge(lo.compact(validApplications));
        });
    }
}
