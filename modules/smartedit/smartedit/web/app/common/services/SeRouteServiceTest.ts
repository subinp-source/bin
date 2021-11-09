/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { SeLegacyRoute, SeRoutes, SeRouteService, SeRouteShortcutConfig } from 'smarteditcommons';
import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

describe('SeRouteService', () => {
    const NG_ROUTE_PREFIX = 'ng';

    let forRoot: (routes: Routes, config?: ExtraOptions) => ModuleWithProviders<RouterModule>;
    let $routeProvider: ng.route.IRouteProvider;

    beforeEach(() => {
        forRoot = spyOn(RouterModule, 'forRoot').and.callThrough();
        $routeProvider = jasmine.createSpyObj('$routeProvider', ['when']);

        // The service is static => should be cleaned after each test
        (SeRouteService as any).routeShortcuts = [];
        (SeRouteService as any).$routeProvider = undefined;
    });

    it('GIVEN angular route that does not start with ng WHEN registered THEN should throw exception', () => {
        const incorrectNgRoutes: SeRoutes = [
            {
                path: '/abc'
            }
        ];
        expect(() => {
            SeRouteService.provideNgRoute(incorrectNgRoutes);
        }).toThrowError('Angular route must start with ' + NG_ROUTE_PREFIX);
    });

    it('Should register angular route', () => {
        // GIVEN
        const correctNgRoute: SeRoutes = [
            {
                path: NG_ROUTE_PREFIX + '/abc'
            }
        ];
        const config: ExtraOptions = {};

        // WHEN
        SeRouteService.provideNgRoute(correctNgRoute, config);

        // THEN
        expect(forRoot).toHaveBeenCalledWith(correctNgRoute, config);
    });

    it('Should register route shortcut config', () => {
        // GIVEN
        const correctNgRoute: SeRoutes = [
            {
                path: NG_ROUTE_PREFIX + '/abc',
                titleI18nKey: 'title'
            }
        ];

        // WHEN
        SeRouteService.provideNgRoute(correctNgRoute);

        // THEN
        const expectedResult: SeRouteShortcutConfig[] = [
            {
                fullPath: NG_ROUTE_PREFIX + '/abc',
                titleI18nKey: 'title',
                priority: undefined,
                shortcutComponent: undefined
            }
        ];
        const routeShortcutConfigs: SeRouteShortcutConfig[] = SeRouteService.routeShortcutConfigs;
        expect(routeShortcutConfigs).toEqual(expectedResult);
    });

    it('Should throw exception when provideLegacyRoute is called and route provider is missing', () => {
        // GIVEN
        const correctLegacyRoute: SeLegacyRoute = {
            path: '/abc',
            route: {}
        };

        // THEN
        expect(() => {
            SeRouteService.provideLegacyRoute(correctLegacyRoute);
        }).toThrowError('Please call SeRouteService.init($routeProvider) first');
    });

    it('Should register legacy (angularjs) route', () => {
        // GIVEN
        const correctLegacyRoute: SeLegacyRoute = {
            path: '/abc',
            route: {}
        };
        SeRouteService.init($routeProvider);

        // WHEN
        SeRouteService.provideLegacyRoute(correctLegacyRoute);

        // THEN
        expect($routeProvider.when).toHaveBeenCalledWith(
            correctLegacyRoute.path,
            correctLegacyRoute.route
        );
    });

    it('GIVEN angular routes WHEN registered THEN generate shortcut configs for all routes with full path', () => {
        // GIVEN
        const correctNgRoutes: SeRoutes = [
            {
                path: NG_ROUTE_PREFIX + '/:siteId/a',
                children: [
                    {
                        path: 'b'
                    },
                    {
                        path: 'c',
                        titleI18nKey: 'cTitle',
                        children: [
                            {
                                path: 'd',
                                children: [
                                    {
                                        path: 'e',
                                        priority: 100
                                    }
                                ]
                            }
                        ]
                    }
                ],
                shortcutComponent: null
            },
            {
                path: NG_ROUTE_PREFIX + '/:catalogId',
                children: [
                    {
                        // skip path
                        children: [
                            {
                                path: 'f'
                            }
                        ]
                    }
                ]
            }
        ];

        // WHEN
        SeRouteService.provideNgRoute(correctNgRoutes);

        // THEN
        const expectedResult: SeRouteShortcutConfig[] = [
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: null
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a/b',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a/c',
                titleI18nKey: 'cTitle',
                priority: undefined,
                shortcutComponent: undefined
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a/c/d',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a/c/d/e',
                titleI18nKey: undefined,
                priority: 100,
                shortcutComponent: undefined
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/:catalogId',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/:catalogId/f',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            }
        ];
        const routeShortcutConfigs: SeRouteShortcutConfig[] = SeRouteService.routeShortcutConfigs;
        expect(routeShortcutConfigs).toEqual(expectedResult);
    });

    it('GIVEN angular routes WHEN registered THEN filter from shortcutsonfig routes without path and with unknown placeholders', () => {
        // GIVEN
        const correctNgRoutes = [
            {
                path: NG_ROUTE_PREFIX + '/:siteId/a'
            },
            {
                path: NG_ROUTE_PREFIX + '/:label'
            },
            {
                children: [
                    {
                        path: NG_ROUTE_PREFIX + '/b'
                    }
                ]
            }
        ];

        // WHEN
        SeRouteService.provideNgRoute(correctNgRoutes);

        // THEN
        const expectedResult: SeRouteShortcutConfig[] = [
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            },
            {
                fullPath: NG_ROUTE_PREFIX + '/b',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            }
        ];
        const routeShortcutConfigs: SeRouteShortcutConfig[] = SeRouteService.routeShortcutConfigs;
        expect(routeShortcutConfigs).toEqual(expectedResult);
    });

    it('GIVEN legacy(angularjs) routes WHEN registered THEN filter from shortcutConfig routes with unknown placeholders', () => {
        // GIVEN
        const legacyRoute1 = {
            path: NG_ROUTE_PREFIX + '/:siteId/a',
            route: {}
        };
        const legacyRoute2 = {
            path: NG_ROUTE_PREFIX + '/:label',
            route: {}
        };
        SeRouteService.init($routeProvider);

        // WHEN
        SeRouteService.provideLegacyRoute(legacyRoute1);
        SeRouteService.provideLegacyRoute(legacyRoute2);

        // THEN
        const expectedResult: SeRouteShortcutConfig[] = [
            {
                fullPath: NG_ROUTE_PREFIX + '/:siteId/a',
                titleI18nKey: undefined,
                priority: undefined,
                shortcutComponent: undefined
            }
        ];
        const routeShortcutConfigs: SeRouteShortcutConfig[] = SeRouteService.routeShortcutConfigs;
        expect(routeShortcutConfigs).toEqual(expectedResult);
    });
});
