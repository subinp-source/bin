/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { BrowserService, IStorageService, OperationContextService } from '@smart/utils';
import { DEFAULT_LANGUAGE, I18N_RESOURCE_URI } from 'smarteditcommons/utils/smarteditconstants';
import { SeModule } from 'smarteditcommons/di';
import { $translateStaticFilesLoader } from './translateStaticFilesLoader';
import { CrossFrameEventService } from '../../services';

/**
 * @ngdoc service
 * @name translationServiceModule
 *
 * @description
 *
 * This module is used to configure the translate service, the filter, and the directives from the 'pascalprecht.translate' package. The configuration consists of:
 *
 */

@SeModule({
    imports: ['pascalprecht.translate', 'legacySmarteditCommonsModule'],
    providers: [$translateStaticFilesLoader],
    config: ($translateProvider: any) => {
        'ngInject';

        $translateProvider.useStaticFilesLoader({
            prefix: I18N_RESOURCE_URI + '/',
            suffix: ''
        });

        // Tell the module what language to use by default
        $translateProvider.preferredLanguage(DEFAULT_LANGUAGE);

        // Using 'escapeParameters' strategy. 'sanitize' not supported in current version.
        // see https://angular-translate.github.io/docs/#/guide/19_security
        // Note that this is the only option that should be used for now.
        // The options 'sanitizeParameters' and 'escape' are causing issues (& replaced by &amp; and interpolation parameters values are not displayed correctly).
        $translateProvider.useSanitizeValueStrategy('escapeParameters');
    },
    initialize: (
        $translate: angular.translate.ITranslateService,
        browserService: BrowserService,
        operationContextService: OperationContextService,
        crossFrameEventService: CrossFrameEventService,
        storageService: IStorageService,
        SWITCH_LANGUAGE_EVENT: string,
        OPERATION_CONTEXT: any
    ) => {
        'ngInject';
        storageService
            .getValueFromLocalStorage('SELECTED_LANGUAGE', false)
            .then(
                (lang: { name: string; isoCode: string }) => {
                    return lang ? lang.isoCode : browserService.getBrowserLocale();
                },
                () => {
                    return browserService.getBrowserLocale();
                }
            )
            .then((lang: string) => {
                $translate.use(lang);
            });

        operationContextService.register(I18N_RESOURCE_URI, OPERATION_CONTEXT.TOOLING);
        crossFrameEventService.subscribe<{ isoCode: string }>(SWITCH_LANGUAGE_EVENT, (id, data) => {
            $translate.use(data.isoCode);
        });
    }
})
export class TranslationServiceModule {}
