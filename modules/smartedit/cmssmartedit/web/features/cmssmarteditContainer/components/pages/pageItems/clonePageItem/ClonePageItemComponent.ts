/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { MultiNamePermissionContext, SeComponent } from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';

/**
 * @ngdoc directive
 * @name pageComponentsModule.directive:clonePageItem
 * @scope
 * @restrict E
 *
 * @description
 * clonePageItem builds an item allowing for the cloning of a given CMS
 * page.
 *
 * @param {< Object} pageInfo An object defining the context of the CMS
 * page associated to the clonePage item.
 */
@SeComponent({
    templateUrl: 'clonePageItemTemplate.html',
    inputs: ['pageInfo']
})
export class ClonePageItemComponent {
    // ------------------------------------------------------------------------
    //  Properties
    // ------------------------------------------------------------------------
    public pageInfo: ICMSPage;
    public clonePagePermission: MultiNamePermissionContext[];

    constructor(private clonePageWizardService: any) {}

    $onInit(): void {
        this.clonePagePermission = [
            {
                names: ['se.clone.page.type'],
                context: {
                    typeCode: this.pageInfo.typeCode
                }
            }
        ];
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    onClickOnClone() {
        this.clonePageWizardService.openClonePageWizard(this.pageInfo);
    }
}
