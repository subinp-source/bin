/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { doImport as forceImport } from './forcedImports';
forceImport();
import { deprecate } from 'smartedit/deprecate';
deprecate();

import * as angular from 'angular';
import { instrument, Cloneable, PageSensitiveDirective, SeModule } from 'smarteditcommons';

import { SystemModule } from 'smartedit/modules';
import { LegacySmarteditServicesModule } from 'smartedit/services';
import { HtmlDirective } from 'smartedit/directives/HtmlDirective';

@SeModule({
    imports: [
        LegacySmarteditServicesModule,
        'templateCacheDecoratorModule',
        'ui.bootstrap',
        'ui.select',
        SystemModule
    ],
    declarations: [HtmlDirective, PageSensitiveDirective],
    config: (
        $provide: angular.auto.IProvideService,
        readObjectStructureFactory: () => (arg: Cloneable) => Cloneable,
        $logProvider: angular.ILogProvider
    ) => {
        'ngInject';

        instrument($provide, readObjectStructureFactory(), 'smartedit');

        $logProvider.debugEnabled(false);
    }
})
export class LegacySmartedit {}
