/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */

import '../../../../web/app/vendor/polyfills';
import '../../../../web/app/vendor/thirdparties';
import { Component, NgModule } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { UpgradeModule } from '@angular/upgrade/static';
import { RouterModule } from '@angular/router';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import {
    commonNgZone,
    diBridgeUtils,
    moduleUtils,
    windowUtils,
    AuthenticationService,
    ALERT_CONFIG_DEFAULTS,
    ALERT_CONFIG_DEFAULTS_TOKEN,
    ALERT_SERVICE_TOKEN,
    ConfirmDialogComponent,
    GatewayFactory,
    HttpInterceptorModule,
    I18N_RESOURCE_URI,
    I18N_RESOURCE_URI_TOKEN,
    IAlertService,
    IAuthenticationService,
    IBaseAlertService,
    ICatalogService,
    ICatalogVersionPermissionService,
    IConfirmationModalService,
    IDragAndDropCrossOrigin,
    IExperienceService,
    IFeatureService,
    IIframeClickDetectionService,
    INotificationService,
    IPageInfoService,
    IPermissionService,
    IPerspectiveService,
    IPreviewService,
    IRenderService,
    ISessionService,
    ISharedDataService,
    IStorageService,
    IToolbarServiceFactory,
    IUrlService,
    IWaitDialogService,
    L10nService,
    LegacyGEWidgetToCustomElementConverter,
    LogService,
    NonValidationErrorInterceptor,
    OPERATION_CONTEXT,
    PermissionErrorInterceptor,
    PreviewErrorInterceptor,
    PromiseUtils,
    ResourceNotFoundErrorInterceptor,
    RestServiceFactory,
    RetryInterceptor,
    SeTranslationModule,
    SharedComponentsModule,
    SmarteditBootstrapGateway,
    SmarteditCommonsModule,
    SSOAuthenticationHelper,
    UnauthorizedErrorInterceptor,
    WindowUtils,
    WHO_AM_I_RESOURCE_URI,
    WHO_AM_I_RESOURCE_URI_TOKEN
} from 'smarteditcommons';
import {
    BrowserService,
    OPERATION_CONTEXT_TOKEN,
    WindowUtils as ParentWindowUtils
} from '@smart/utils';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AlertService as FundamentalAlertService } from '@fundamental-ngx/core';

@Component({ selector: 'empty', template: "<div>This page doesn't exist</div>" })
export class InvalidRouteE2EComponent {}

const customAppAttribute = 'custom-app';

/*
 * Utility method to bootstrap a small hybrid app
 * made of the Angular SmarteditCommonsModule and the Angular JS
 * module found under a custom-app DOM attribute
 * it triggers automatically if such attribute is found
 */
export function legacyCustomAppBootstrap(): void {
    const providers = [
        {
            provide: ALERT_SERVICE_TOKEN,
            useClass: FundamentalAlertService
        },
        {
            provide: ALERT_CONFIG_DEFAULTS_TOKEN,
            useValue: ALERT_CONFIG_DEFAULTS
        },
        BrowserService,
        PromiseUtils,
        SmarteditBootstrapGateway,
        {
            provide: ParentWindowUtils,
            useClass: WindowUtils
        },
        SSOAuthenticationHelper,
        moduleUtils.provideValues({
            [OPERATION_CONTEXT_TOKEN]: OPERATION_CONTEXT,
            [WHO_AM_I_RESOURCE_URI_TOKEN]: WHO_AM_I_RESOURCE_URI,
            [I18N_RESOURCE_URI_TOKEN]: I18N_RESOURCE_URI
        }),
        {
            provide: IAuthenticationService,
            useClass: AuthenticationService
        },
        LegacyGEWidgetToCustomElementConverter,
        moduleUtils.initialize(
            (
                gatewayFactory: GatewayFactory,
                httpClient: HttpClient,
                legacyGEWidgetToCustomElementConverter: LegacyGEWidgetToCustomElementConverter,
                l10nService: L10nService
            ) => {
                gatewayFactory.initListener();
                diBridgeUtils.downgradeService('httpClient', HttpClient);

                if (!windowUtils.isIframe()) {
                    diBridgeUtils.downgradeService('restServiceFactory', RestServiceFactory);
                }
                diBridgeUtils.downgradeService('ssoAuthenticationHelper', SSOAuthenticationHelper);

                legacyGEWidgetToCustomElementConverter.convert();

                return l10nService.resolveLanguage();
            },
            [GatewayFactory, HttpClient, LegacyGEWidgetToCustomElementConverter, L10nService]
        )
    ];
    const downgradedServices: { marker: string; token?: any }[] = [
        { marker: 'authenticationService', token: IAuthenticationService },
        { marker: 'delegateRestService' },
        { marker: 'productCatalogRestService' },
        { marker: 'contentCatalogRestService' },
        { marker: 'catalogService', token: ICatalogService },
        { marker: 'restServiceFactory' },
        { marker: 'smarteditBootstrapGateway' },
        { marker: 'loadConfigManagerService' },
        { marker: 'alertCollection' },
        { marker: 'pageInfoService', token: IPageInfoService },
        { marker: 'alertCollectionLegacySupport' },
        { marker: 'alertFactory' },
        { marker: 'siteService' },
        { marker: 'iframeManagerService' },
        { marker: 'alertService', token: IAlertService },
        { marker: 'confirmationModalService', token: IConfirmationModalService },
        { marker: 'alertService', token: IBaseAlertService },
        { marker: 'sharedDataService', token: ISharedDataService },
        { marker: 'urlService', token: IUrlService },
        { marker: 'storageService', token: IStorageService },
        { marker: 'sessionService', token: ISessionService },
        { marker: 'permissionService', token: IPermissionService },
        { marker: 'iframeClickDetectionService', token: IIframeClickDetectionService },
        { marker: 'retryInterceptor' },
        { marker: 'toolbarServiceFactory', token: IToolbarServiceFactory },
        { marker: 'waitDialogService', token: IWaitDialogService },
        { marker: 'previewService', token: IPreviewService },
        { marker: 'experienceService', token: IExperienceService },
        { marker: 'featureService', token: IFeatureService },
        { marker: 'perspectiveService', token: IPerspectiveService },
        { marker: 'catalogVersionPermissionService', token: ICatalogVersionPermissionService },
        { marker: 'dragAndDropCrossOrigin', token: IDragAndDropCrossOrigin },
        { marker: 'notificationService', token: INotificationService },
        { marker: 'renderService', token: IRenderService }
    ];

    if (windowUtils.isIframe()) {
        downgradedServices.push({ marker: 'restServiceFactory' });
    } else {
        providers.unshift(RestServiceFactory);
    }

    downgradedServices.forEach((downgradedService) => {
        const DowngradedServiceClass = (window as any).__smartedit__.downgradedService[
            downgradedService.marker
        ];
        if (DowngradedServiceClass) {
            if (downgradedService.token) {
                providers.unshift({
                    provide: downgradedService.token,
                    useClass: DowngradedServiceClass
                });
            } else {
                providers.unshift(DowngradedServiceClass);
            }
        }
    });

    const TranslationsFetchServiceClass = (window as any).__smartedit__.downgradedService
        .translationsFetchService;

    const pushedModules: any[] = [...(window as any).__smartedit__.pushedModules];

    @NgModule({
        imports: [
            BrowserModule,
            UpgradeModule,
            HttpClientModule,
            FormsModule,
            ReactiveFormsModule,
            SmarteditCommonsModule,
            SharedComponentsModule,
            ...pushedModules,
            HttpInterceptorModule.forRoot(
                UnauthorizedErrorInterceptor,
                ResourceNotFoundErrorInterceptor,
                RetryInterceptor,
                NonValidationErrorInterceptor,
                PreviewErrorInterceptor,
                PermissionErrorInterceptor
            ),
            SeTranslationModule.forRoot(TranslationsFetchServiceClass),
            RouterModule.forRoot(
                [
                    {
                        path: 'ng',
                        component: InvalidRouteE2EComponent
                    }
                ],
                { useHash: true, initialNavigation: true }
            )
        ],
        declarations: [InvalidRouteE2EComponent, ConfirmDialogComponent],
        entryComponents: [ConfirmDialogComponent],
        providers
    })
    class WrapperModule {
        constructor(private upgrade: UpgradeModule) {}

        ngDoBootstrap() {
            const customAppNode = document.querySelector(`[${customAppAttribute}]`);
            const appName = customAppNode.getAttribute(customAppAttribute);
            this.upgrade.bootstrap(customAppNode, [appName], { strictDi: false });
        }
    }
    window.smarteditJQuery(document).ready(() => {
        if (document.querySelector(`[${customAppAttribute}]`)) {
            platformBrowserDynamic()
                .bootstrapModule(WrapperModule, { ngZone: commonNgZone })
                .catch((error: any) => {
                    new LogService().error(error);
                });
        }
    });
}
legacyCustomAppBootstrap();
