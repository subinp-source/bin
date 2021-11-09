/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { Cloneable, LogService, Payload, SeDowngradeService } from 'smarteditcommons';

import { ConfigurationObject } from './Configuration';
import { ConfigurationModules, Module } from './ConfigurationModules';

enum ApplicationLayer {
    SMARTEDITCONTAINER,
    SMARTEDIT
}
/** @internal */
@SeDowngradeService()
export class ConfigurationExtractorService {
    constructor(private logService: LogService) {}

    extractSEContainerModules(configurations: ConfigurationObject) {
        return this._getAppsAndLocations(configurations, ApplicationLayer.SMARTEDITCONTAINER);
    }

    extractSEModules(configurations: ConfigurationObject) {
        return this._getAppsAndLocations(configurations, ApplicationLayer.SMARTEDIT);
    }

    orderApplications(applications: Module[]) {
        const simpleApps = applications.filter((item: Module) => !item.extends);
        const extendingApps = applications
            .filter((item: Module) => !!item.extends)
            /*
             * filer out extendingApps thata do extend an unknown app
             * other recursive _addExtendingAppsInOrder will never end
             */
            .filter((extendingApp) => {
                const index = applications.findIndex(
                    (item: Module) => item.name === extendingApp.extends
                );

                if (index === -1) {
                    this.logService.error(
                        `Application ${extendingApp.name} located at ${
                            extendingApp.location
                        } is ignored because it extends an unknown application '${
                            extendingApp.extends
                        }'; SmartEdit functionality may be compromised.`
                    );
                }
                return index > -1;
            });

        return this._addExtendingAppsInOrder(simpleApps, extendingApps);
    }

    private _addExtendingAppsInOrder(
        simpleApps: Module[],
        extendingApps: Module[],
        pass?: number
    ): Module[] {
        pass = pass || 1;

        const remainingApps: Module[] = [];

        extendingApps.forEach((extendingApp) => {
            const index = simpleApps.findIndex((item: Module) => {
                return item.name === extendingApp.extends;
            });
            if (index > -1) {
                console.debug(
                    `pass ${pass}, ${extendingApp.name} requiring ${
                        extendingApp.extends
                    } found it at index ${index} (${simpleApps.map((app) => app.name)})`
                );
                simpleApps.splice(index + 1, 0, extendingApp);
            } else {
                console.debug(
                    `pass ${pass}, ${extendingApp.name} requiring ${
                        extendingApp.extends
                    } did not find it  (${simpleApps.map((app) => app.name)})`
                );
                remainingApps.push(extendingApp);
            }
        });

        if (remainingApps.length) {
            return this._addExtendingAppsInOrder(simpleApps, remainingApps, ++pass);
        } else {
            return simpleApps;
        }
    }

    private _getAppsAndLocations(
        configurations: ConfigurationObject,
        applicationLayer: ApplicationLayer
    ): ConfigurationModules {
        let locationName: string;
        switch (applicationLayer) {
            case ApplicationLayer.SMARTEDITCONTAINER:
                locationName = 'smartEditContainerLocation';
                break;
            case ApplicationLayer.SMARTEDIT:
                locationName = 'smartEditLocation';
                break;
        }

        const appsAndLocations = lo
            .map(configurations, (value: Cloneable, prop: string) => {
                return { key: prop, value };
            })
            .reduce(
                (holder: ConfigurationModules, current: { key: string; value: Cloneable }) => {
                    if (
                        current.key.indexOf('applications') === 0 &&
                        typeof (current.value as Payload)[locationName] === 'string'
                    ) {
                        const app = {} as Module;
                        app.name = current.key.split('.')[1];
                        const location = (current.value as Payload)[locationName] as string;
                        if (/^https?\:\/\//.test(location)) {
                            app.location = location;
                        } else {
                            app.location = configurations.domain + location;
                        }
                        const _extends = (current.value as Payload).extends as string;
                        if (_extends) {
                            app.extends = _extends;
                        }

                        holder.applications.push(app);
                        // authenticationMaps from smartedit modules
                        holder.authenticationMap = lo.merge(
                            holder.authenticationMap,
                            (current.value as Payload).authenticationMap
                        );
                    } else if (current.key === 'authenticationMap') {
                        // authenticationMap from smartedit
                        holder.authenticationMap = lo.merge(
                            holder.authenticationMap,
                            current.value as Payload
                        );
                    }
                    return holder;
                },
                {
                    applications: [],
                    authenticationMap: {}
                } as ConfigurationModules
            );

        return appsAndLocations;
    }
}
