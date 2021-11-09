/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, MultiNamePermissionContext, SeComponent } from 'smarteditcommons';
import { ManagePageService } from 'cmssmarteditcontainer/services/pages/ManagePageService';
import { ICMSPage } from 'cmscommons/dtos/ICMSPage';

@SeComponent({
    templateUrl: 'restorePageItemTemplate.html',
    inputs: ['pageInfo']
})
export class RestorePageItemComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public pageInfo: ICMSPage;
    public restorePagePermission: MultiNamePermissionContext[];

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------
    constructor(private managePageService: ManagePageService) {}

    $onInit(): void {
        this.restorePagePermission = [
            {
                names: ['se.restore.page.type'],
                context: {
                    typeCode: this.pageInfo.typeCode
                }
            }
        ];
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    restorePage(): void {
        this.managePageService.restorePage(this.pageInfo);
    }
}
