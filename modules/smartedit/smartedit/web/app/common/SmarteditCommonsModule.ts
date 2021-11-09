/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FundamentalsModule } from './FundamentalsModule';
import { InjectionToken, Injector, NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import {
    booleanUtils,
    retriableErrorPredicate,
    timeoutErrorPredicate,
    updatePredicate,
    BooleanUtils,
    BrowserService,
    CachedAnnotationFactory,
    CacheConfigAnnotationFactory,
    CacheEngine,
    CacheService,
    Class,
    CloneableUtils,
    CryptographicUtils,
    DefaultRetryStrategy,
    ExponentialRetryStrategy,
    EVENT_SERVICE,
    FingerPrintingService,
    FunctionsUtils,
    HttpBackendService,
    HttpErrorInterceptorService,
    HttpUtils,
    I18N_RESOURCE_URI_TOKEN,
    InvalidateCacheAnnotationFactory,
    IModalService,
    IRetryStrategy,
    ISettingsService,
    LinearRetryStrategy,
    LoginDialogResourceProvider,
    LogService,
    LANGUAGE_SERVICE,
    LANGUAGE_SERVICE_CONSTANTS,
    OperationContextAnnotationFactory,
    OperationContextService,
    OPERATION_CONTEXT_TOKEN,
    PromiseUtils,
    RestServiceFactory,
    RetryInterceptor,
    StringUtils as ParentStringUtils,
    TESTMODESERVICE,
    UrlUtils,
    WindowUtils as ParentWindowUtils,
    WHO_AM_I_RESOURCE_URI_TOKEN
} from '@smart/utils';
import {
    operationContextCMSPredicate,
    operationContextInteractivePredicate,
    operationContextNonInteractivePredicate,
    operationContextToolingPredicate,
    AngularJSBootstrapIndicatorService,
    AngularJSLazyDependenciesService,
    AuthorizationService,
    CrossFrameEventService,
    CrossFrameEventServiceGateway,
    GatewayFactory,
    GatewayProxiedAnnotationFactory,
    GatewayProxy,
    InterceptorHelper,
    ITemplateCacheService,
    LEGACY_LOCATION,
    LEGACY_ROUTE,
    PermissionsRestService,
    SmarteditBootstrapGateway,
    TestModeService,
    WizardModule,
    YjqueryModule
} from 'smarteditcommons/services';
import {
    moduleUtils,
    DiscardablePromiseUtils,
    I18N_RESOURCE_URI,
    NodeUtils,
    OPERATION_CONTEXT,
    StringUtils,
    SMARTEDIT_RESOURCE_URI_REGEXP,
    SMARTEDIT_ROOT,
    SSO_AUTHENTICATION_ENTRY_POINT,
    SSO_LOGOUT_ENTRY_POINT,
    SSO_OAUTH2_AUTHENTICATION_ENTRY_POINT,
    USER_GLOBAL_PERMISSIONS_RESOURCE_URI,
    WindowUtils,
    WHO_AM_I_RESOURCE_URI
} from 'smarteditcommons/utils';
import { diBridgeUtils } from 'smarteditcommons/di';
import { ExperienceInterceptor } from 'smarteditcommons/services/interceptors/ExperienceInterceptor';
import { LocationUpgradeModule } from '@angular/common/upgrade';
import {
    IUIBootstrapModalService,
    IUIBootstrapModalStackService,
    JQueryUtilsService,
    LanguageService,
    LanguageServiceGateway,
    ModalService,
    SettingsService,
    TimerService
} from './services';
import { SystemEventService } from './services/SystemEventService';
import { IAnimateService } from './services/interfaces/IAnimateService';
import { PriorityService } from './services/PriorityService';
import {
    I18N_LANGUAGES_RESOURCE_URI,
    LANGUAGE_RESOURCE_URI,
    SMARTEDIT_LOGIN_DIALOG_RESOURCES
} from './utils';
import { SeGenericEditorModule } from './components/genericEditor';
import { SliderPanelServiceFactory } from './components/sliderPanel';
import { SmarteditConstantsModule } from './SmarteditConstantsModule';

/**
 * @ngdoc overview
 * @name smarteditCommonsModule
 *
 * @description
 * Module containing all the services shared within the smartedit commons.
 */
const gatewayProxiedAnnotationFactoryToken = new InjectionToken<string>(
    'gatewayProxiedAnnotationFactoryToKen'
);
const cachedAnnotationFactoryToken = new InjectionToken<string>('cachedAnnotationFactoryToken');
const cacheConfigAnnotationFactoryToken = new InjectionToken<string>(
    'cacheConfigAnnotationFactoryToken'
);
const invalidateCacheAnnotationFactoryToken = new InjectionToken<string>(
    'invalidateCacheAnnotationFactoryToken'
);
const operationContextAnnotationFactoryToken = new InjectionToken<string>(
    'operationContextAnnotationFactoryToken'
);

@NgModule({
    imports: [
        FundamentalsModule,
        LocationUpgradeModule.config(),
        YjqueryModule,
        SeGenericEditorModule,
        WizardModule,
        SmarteditConstantsModule
    ],
    providers: [
        diBridgeUtils.upgradeProvider('$animate', IAnimateService),
        diBridgeUtils.upgradeProvider('$uibModal', IUIBootstrapModalService),
        diBridgeUtils.upgradeProvider('$uibModalStack', IUIBootstrapModalStackService),
        diBridgeUtils.upgradeProvider('$route', LEGACY_ROUTE),
        diBridgeUtils.upgradeProvider('$location', LEGACY_LOCATION),
        diBridgeUtils.upgradeProvider('$templateCache', ITemplateCacheService),
        SmarteditBootstrapGateway,
        AngularJSLazyDependenciesService,
        AngularJSBootstrapIndicatorService,
        LanguageServiceGateway,
        LanguageService,
        {
            provide: LANGUAGE_SERVICE,
            useClass: LanguageService
        },
        TimerService,
        DiscardablePromiseUtils,
        moduleUtils.provideValues({
            SSO_LOGOUT_ENTRY_POINT,
            SSO_AUTHENTICATION_ENTRY_POINT,
            SSO_OAUTH2_AUTHENTICATION_ENTRY_POINT,
            SMARTEDIT_RESOURCE_URI_REGEXP,
            SMARTEDIT_ROOT,
            USER_GLOBAL_PERMISSIONS_RESOURCE_URI,
            [OPERATION_CONTEXT_TOKEN]: OPERATION_CONTEXT,
            [WHO_AM_I_RESOURCE_URI_TOKEN]: WHO_AM_I_RESOURCE_URI,
            [I18N_RESOURCE_URI_TOKEN]: I18N_RESOURCE_URI
        }),
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ExperienceInterceptor,
            multi: true
        },
        { provide: IModalService, useClass: ModalService },
        { provide: ISettingsService, useClass: SettingsService },
        SliderPanelServiceFactory,
        LogService,
        BrowserService,
        FingerPrintingService,
        CacheEngine,
        CacheService,
        CloneableUtils,
        CrossFrameEventService,
        /* forbiddenNameSpaces:false */
        {
            provide: LoginDialogResourceProvider,
            useValue: SMARTEDIT_LOGIN_DIALOG_RESOURCES
        },
        {
            provide: LANGUAGE_SERVICE_CONSTANTS,
            useValue: {
                LANGUAGE_RESOURCE_URI,
                I18N_LANGUAGES_RESOURCE_URI
            }
        },
        {
            provide: EVENT_SERVICE,
            useFactory: (crossFrameEventService: CrossFrameEventService) => {
                return crossFrameEventService;
            },
            deps: [CrossFrameEventService]
        },
        {
            provide: CrossFrameEventServiceGateway.crossFrameEventServiceGatewayToken,
            useClass: CrossFrameEventServiceGateway
        },
        OperationContextService,
        BooleanUtils,
        CryptographicUtils,
        FunctionsUtils,
        HttpUtils,
        NodeUtils,
        PromiseUtils,
        JQueryUtilsService,
        {
            provide: ParentStringUtils,
            useClass: StringUtils
        },
        StringUtils,
        UrlUtils,
        {
            provide: ParentWindowUtils,
            useClass: WindowUtils
        },
        WindowUtils,
        SystemEventService,
        PriorityService,
        TestModeService,
        {
            provide: TESTMODESERVICE,
            useFactory: (testModeService: TestModeService) => {
                return testModeService;
            },
            deps: [TestModeService]
        },
        GatewayFactory,
        GatewayProxy,
        InterceptorHelper,
        {
            provide: gatewayProxiedAnnotationFactoryToken,
            useFactory: GatewayProxiedAnnotationFactory,
            deps: [GatewayProxy]
        },
        {
            provide: cachedAnnotationFactoryToken,
            useFactory: CachedAnnotationFactory,
            deps: [CacheService]
        },
        {
            provide: cacheConfigAnnotationFactoryToken,
            useFactory: CacheConfigAnnotationFactory,
            deps: [LogService]
        },
        {
            provide: invalidateCacheAnnotationFactoryToken,
            useFactory: InvalidateCacheAnnotationFactory,
            deps: [CacheService]
        },
        {
            provide: operationContextAnnotationFactoryToken,
            useFactory: OperationContextAnnotationFactory,
            deps: [Injector, OperationContextService, OPERATION_CONTEXT_TOKEN]
        },
        RestServiceFactory,
        PermissionsRestService,
        AuthorizationService,
        moduleUtils.initialize(
            (
                gatewayProxiedAnnotationFactory: any,
                cachedAnnotationFactory: any,
                cacheConfigAnnotationFactory: any,
                invalidateCacheAnnotationFactory: any,
                operationContextAnnotationFactory: any
            ) => {
                diBridgeUtils.downgradeService('translateService', TranslateService);
                diBridgeUtils.downgradeService('browserService', BrowserService);
                diBridgeUtils.downgradeService('httpBackendService', HttpBackendService);
                diBridgeUtils.downgradeService('operationContextService', OperationContextService);
                diBridgeUtils.downgradeService('retryInterceptor', RetryInterceptor);
                diBridgeUtils.downgradeService(
                    'httpErrorInterceptorService',
                    HttpErrorInterceptorService
                );
            },
            [
                gatewayProxiedAnnotationFactoryToken,
                cachedAnnotationFactoryToken,
                cacheConfigAnnotationFactoryToken,
                invalidateCacheAnnotationFactoryToken,
                operationContextAnnotationFactoryToken
            ]
        ),
        moduleUtils.bootstrap(
            (
                retryInterceptor: RetryInterceptor,
                defaultRetryStrategy: Class<IRetryStrategy>,
                exponentialRetryStrategy: Class<IRetryStrategy>,
                linearRetryStrategy: Class<IRetryStrategy>,
                operationContextService: OperationContextService
            ) => {
                retryInterceptor
                    .register(
                        booleanUtils.areAllTruthy(
                            operationContextInteractivePredicate,
                            retriableErrorPredicate
                        ),
                        defaultRetryStrategy
                    )
                    .register(
                        booleanUtils.areAllTruthy(
                            operationContextNonInteractivePredicate,
                            retriableErrorPredicate
                        ),
                        exponentialRetryStrategy
                    )
                    .register(
                        booleanUtils.areAllTruthy(
                            operationContextCMSPredicate,
                            timeoutErrorPredicate,
                            updatePredicate
                        ),
                        exponentialRetryStrategy
                    )
                    .register(
                        booleanUtils.areAllTruthy(
                            operationContextToolingPredicate,
                            timeoutErrorPredicate,
                            updatePredicate
                        ),
                        linearRetryStrategy
                    );
                operationContextService.register(LANGUAGE_RESOURCE_URI, OPERATION_CONTEXT.TOOLING);
            },
            [
                RetryInterceptor,
                DefaultRetryStrategy,
                ExponentialRetryStrategy,
                LinearRetryStrategy,
                OperationContextService,
                LogService
            ]
        )
    ]
})
export class SmarteditCommonsModule {}
