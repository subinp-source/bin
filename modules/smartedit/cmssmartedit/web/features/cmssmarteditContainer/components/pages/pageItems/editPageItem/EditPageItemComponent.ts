/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { MultiNamePermissionContext, SeComponent } from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';
import { PageInfoMenuService } from '../../pageInfoMenu/services/PageInfoMenuService';

/**
 * @ngdoc directive
 * @name pageComponentsModule.directive:editPageItem
 * @scope
 * @restrict E
 *
 * @description
 * editPageItem builds an action item allowing for the edition of a given
 * CMS page .
 *
 * @param {< Object} pageInfo An object defining the context of the
 * CMS page associated to the editPage item.
 */
@SeComponent({
    templateUrl: 'editPageItemTemplate.html',
    inputs: ['pageInfo']
})
export class EditPageItemComponent {
    public pageInfo: ICMSPage;
    public editPagePermission: MultiNamePermissionContext[];

    constructor(private pageInfoMenuService: PageInfoMenuService) {}

    $onInit(): void {
        this.editPagePermission = [
            {
                names: ['se.edit.page.type'],
                context: {
                    typeCode: this.pageInfo.typeCode
                }
            },
            {
                names: ['se.act.on.page.in.workflow'],
                context: {
                    pageInfo: this.pageInfo
                }
            }
        ];
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    onClickOnEdit() {
        return this.pageInfoMenuService.openPageEditor(this.pageInfo);
    }
}
