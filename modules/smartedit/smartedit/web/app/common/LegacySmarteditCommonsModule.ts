/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { deprecate } from './services/deprecate';
import './services/forcedImport';

import {
    YEventMessageComponent,
    YInfiniteScrollingComponent,
    YMoreTextComponent
} from 'smarteditcommons/components';
import { CompileHtmlLegacyDirective } from 'smarteditcommons/directives';
import { TranslationServiceModule } from 'smarteditcommons/modules/translations/translationServiceModule';
import { FunctionsModule } from 'smarteditcommons/utils/functionsModule';
import { SeModule } from 'smarteditcommons/di';

import { ConfigModule, L10nModule, LanguageServiceGateway } from 'smarteditcommons/services';
import { SmarteditRootModule } from 'smarteditcommons/services/SmarteditRootModule';
import { YSliderPanelComponent } from './components/sliderPanel';
import { RecompileDomDirective } from './directives/recompileDom';
import { YMessageComponent } from './components/yMessage/yMessage';
import { YDropDownMenuComponent } from './components/dropdown/dropdownMenu';
import { LegacyHasOperationPermissionDirective } from './components/authorization/legacyHasOperationPermission';
import { YPaginationComponent } from './components/pagination/yPagination';
import { StartFromFilter } from './components/pagination/startFromFilter';
import { IncludeReplaceDirective } from './directives';
import { LegacyDynamicPagedListComponent } from './components/dynamicPagedList';

deprecate();

/**
 * @ngdoc overview
 * @name smarteditCommonsModule
 *
 * @description
 * Module containing all the services shared within the smartedit commons.
 */
@SeModule({
    imports: [
        SmarteditRootModule,
        FunctionsModule,
        'infinite-scroll',
        L10nModule,
        'resourceLocationsModule',
        'seConstantsModule',
        'yLoDashModule',
        TranslationServiceModule,
        ConfigModule,
        'ui.select',
        'ngSanitize'
    ],
    providers: [LanguageServiceGateway],
    declarations: [
        CompileHtmlLegacyDirective,
        StartFromFilter,
        LegacyDynamicPagedListComponent,
        YPaginationComponent,
        YSliderPanelComponent,
        YInfiniteScrollingComponent,
        YEventMessageComponent,
        YMoreTextComponent,
        RecompileDomDirective,
        IncludeReplaceDirective,
        YDropDownMenuComponent,
        YMessageComponent,
        LegacyHasOperationPermissionDirective
    ]
})
export class LegacySmarteditCommonsModule {}
