/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import { PageVersionsMenuComponent } from './PageVersionsMenuComponent';
import { VersionItemComponent } from './panel/VersionItemComponent';
import { VersionItemMenuComponent } from './panel/itemMenu/VersionItemMenuComponent';
import { VersionItemContextComponent } from './context/VersionItemContextComponent';
import { ManagePageVersionService } from './services/ManagePageVersionService';
import { PageVersionSelectionService } from './services/PageVersionSelectionService';
import { RollbackPageVersionService } from './services/RollbackPageVersionService';
import { CmsSmarteditServicesModule } from 'cmssmarteditcontainer/services/cmsSmarteditServicesModule';

/**
 * @ngdoc overview
 * @name pageVersionsMenuModule
 *
 * @description
 * Module containing all the components and services necessary to display a page versions menu.
 */
@SeModule({
    imports: [
        CmsSmarteditServicesModule,
        'seConstantsModule',
        'smarteditServicesModule',
        'alertServiceModule'
    ],
    declarations: [
        PageVersionsMenuComponent,
        VersionItemComponent,
        VersionItemContextComponent,
        VersionItemMenuComponent
    ],
    providers: [ManagePageVersionService, PageVersionSelectionService, RollbackPageVersionService]
})
export class PageVersionsModule {}
