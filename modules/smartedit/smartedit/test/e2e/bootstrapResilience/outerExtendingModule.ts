/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { moduleUtils, IFeatureService, SeEntryModule } from 'smarteditcommons';
import { NgModule } from '@angular/core';

@SeEntryModule('OuterExtendingModule')
@NgModule({
    providers: [
        moduleUtils.bootstrap(
            (featureService: IFeatureService) => {
                featureService.addToolbarItem({
                    toolbarId: 'smartEditPerspectiveToolbar',
                    key: 'dummyToolbar',
                    type: 'HYBRID_ACTION',
                    nameI18nKey: 'OVVERIDEN_DUMMYTOOLBAR',
                    priority: 1,
                    section: 'left',
                    iconClassName: 'hyicon hyicon-addlg se-toolbar-menu-ddlb--button__icon',
                    callback() {
                        //
                    }
                });
            },
            [IFeatureService]
        )
    ]
})
export class OuterExtendingModule {}
