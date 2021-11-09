/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { doImport as forcedImport } from './forcedImports';
forcedImport();
import { deprecate } from 'smarteditcontainer/deprecate';
deprecate();

import * as angular from 'angular';
import {
    instrument,
    Cloneable,
    GenericEditorModule,
    PageSensitiveDirective,
    SeModule,
    SeRouteService,
    TreeModule,
    TypedMap,
    YCollapsibleContainerComponent,
    YHelpModule
} from 'smarteditcommons';
import {
    CatalogAwareRouteResolverModule,
    LegacySmarteditServicesModule
} from 'smarteditcontainer/services';
import { SmarteditDefaultController } from 'smarteditcontainer/components/SmarteditDefaultController';
import { AdministrationModule } from 'smarteditcontainer/modules';
import { SitesLinkComponent } from './components/sitesLink/SitesLinkComponent';
import { LandingPageComponent } from './components/landingPage/LandingPageComponent';
import { LegacyCatalogDetailsModule } from './services/widgets/catalogDetails/legacy/LegacyCatalogDetailsModule';
import { ClientPagedListModule as LegacyClientPagedListModule } from './services/widgets/clientPagedList';

declare global {
    /* @internal */
    interface InternalSmartedit {
        smartEditBootstrapped: TypedMap<boolean>;
    }
}

const TOP_LEVEL_MODULE_NAME = 'smarteditcontainer';

// @ts-ignore
@SeModule({
    declarations: [PageSensitiveDirective, YCollapsibleContainerComponent, LandingPageComponent],
    imports: [
        LegacySmarteditServicesModule,
        LegacyCatalogDetailsModule,
        AdministrationModule,
        'templateCacheDecoratorModule',
        'ngRoute',
        'ui.bootstrap',
        'coretemplates',
        'modalServiceModule',
        LegacyClientPagedListModule,
        'paginationFilterModule',
        'resourceLocationsModule',
        TreeModule,
        'ySelectModule',
        YHelpModule,
        CatalogAwareRouteResolverModule,
        'seConstantsModule',
        GenericEditorModule,
        'recompileDomModule'
    ],
    providers: [],
    config: (
        $provide: angular.auto.IProvideService,
        readObjectStructureFactory: () => (arg: Cloneable) => Cloneable,
        LANDING_PAGE_PATH: string,
        STORE_FRONT_CONTEXT: string,
        $routeProvider: angular.route.IRouteProvider,
        $logProvider: angular.ILogProvider,
        catalogAwareRouteResolverFunctions: any
    ) => {
        'ngInject';

        instrument($provide, readObjectStructureFactory(), TOP_LEVEL_MODULE_NAME);

        /*
         * no .otherwise({redirectTo: '/'}); otherwise legacy router will kick in even for ng routes
         */
        SeRouteService.init($routeProvider);
        SeRouteService.provideLegacyRoute({
            path: LANDING_PAGE_PATH,
            route: {
                template: '<landing-page></landing-page>'
            },
            priority: 1,
            shortcutComponent: SitesLinkComponent
        });

        SeRouteService.provideLegacyRoute({
            path: LANDING_PAGE_PATH + 'sites/:siteId',
            route: {
                template: '<landing-page></landing-page>'
            }
        });

        SeRouteService.provideLegacyRoute({
            path: STORE_FRONT_CONTEXT,
            route: {
                templateUrl: 'mainview.html',
                controller: SmarteditDefaultController,
                resolve: {
                    setExperience: catalogAwareRouteResolverFunctions.storefrontResolve
                }
            },
            priority: 30,
            titleI18nKey: 'se.route.storefront.title'
        });
        $logProvider.debugEnabled(false);
    }
})
/** @internal */
export class Smarteditcontainer {}
