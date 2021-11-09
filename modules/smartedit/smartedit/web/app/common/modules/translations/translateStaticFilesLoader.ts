/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { ITranslationsFetchService, TranslationMap } from '@smart/utils';

/*
 * this custom implementations of $translateStaticFilesLoader needed by 'pascalprecht.translate' package leverages
 * our restServiceFactory as opposed to httpClient in order to proxy the i18n loading to the container.
 * This is required for our cross-origin compliancy
 */
export function $translateStaticFilesLoader(
    $q: angular.IQService,
    translationsFetchService: ITranslationsFetchService
): (option: { key: string }) => angular.IPromise<TranslationMap> {
    'ngInject';
    return (options: { key: string }): angular.IPromise<TranslationMap> =>
        $q.when(translationsFetchService.get(options.key));
}
