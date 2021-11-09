/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { SeModule } from '../../di';
import { YSelectComponent } from './ySelect';
import { ItemPrinterComponent } from './itemPrinter';
import { ActionableSearchItemComponent } from './ySelectExtensions/yActionableSearchItem';

/**
 * @ngdoc overview
 * @name ySelectModule
 * @description
 * # The ySelectModule
 *
 */

@SeModule({
    imports: [
        'legacySmarteditCommonsModule',
        'functionsModule',
        'ui.select',
        'ngSanitize',
        'coretemplates',
        'l10nModule',
        'seConstantsModule'
    ],
    declarations: [YSelectComponent, ItemPrinterComponent, ActionableSearchItemComponent],
    initialize: ($templateCache: angular.ITemplateCacheService) => {
        'ngInject';

        // This fixes an issue with the multi-select. It will allow displaying the 'CREATE' button if no results are displayed.

        $templateCache.put(
            'select2/select-multiple.tpl.html',
            $templateCache
                .get<string>('select2/select-multiple.tpl.html')
                .replace(
                    "{'select2-display-none': !$select.open || $select.items.length === 0}",
                    "{'select2-display-none': !$select.open }"
                )
        );
        // placeholder for search input field.
        $templateCache.put(
            'select2/select-multiple.tpl.html',
            $templateCache
                .get<string>('select2/select-multiple.tpl.html')
                .replace(
                    'placeholder="{{$selectMultiple.getPlaceholder()}}"',
                    'placeholder="{{$select.placeholder}}"'
                )
        );
        $templateCache.put(
            'select2/select.tpl.html',
            $templateCache
                .get<string>('select2/select.tpl.html')
                .replace(
                    '<input type="search"',
                    '<input type="search" placeholder="{{$select.isEmpty() ? null : $select.placeholder}}"'
                )
        );
        // Prevents AngularJS from adding 'unsafe:javascript' to the href value which triggers safari to go to a blank page.
        $templateCache.put(
            'select2/match-multiple.tpl.html',
            $templateCache
                .get<string>('select2/match-multiple.tpl.html')
                .replace('href="javascript:;"', '')
        );

        // use a copy of select2

        $templateCache.put(
            'pagedSelect2/match-multiple.tpl.html',
            $templateCache.get<string>('select2/match-multiple.tpl.html')
        );
        $templateCache.put(
            'pagedSelect2/match.tpl.html',
            $templateCache.get<string>('select2/match.tpl.html')
        );
        $templateCache.put(
            'pagedSelect2/no-choice.tpl.html',
            $templateCache.get<string>('select2/no-choice.tpl.html')
        );
        $templateCache.put(
            'pagedSelect2/select-multiple.tpl.html',
            $templateCache.get<string>('select2/select-multiple.tpl.html')
        );
        $templateCache.put(
            'pagedSelect2/select.tpl.html',
            $templateCache.get<string>('select2/select.tpl.html')
        );

        // our own flavor of select2 for paging that makes use of yInfiniteScrolling component
        $templateCache.put(
            'pagedSelect2/choices.tpl.html',
            $templateCache.get<string>('uiSelectPagedChoicesTemplate.html')
        );
        $templateCache.put(
            'select2/choices.tpl.html',
            $templateCache.get<string>('uiSelectChoicesTemplate.html')
        );
    }
})
export class YSelectModule {}
