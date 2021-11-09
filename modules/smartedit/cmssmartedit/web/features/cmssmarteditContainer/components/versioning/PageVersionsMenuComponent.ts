/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPageInfoService, ISeComponent, SeComponent } from 'smarteditcommons';
import { VersionsPanelComponent } from './panel/VersionsPanelComponent';
import { PageVersionSelectionService } from './services/PageVersionSelectionService';

@SeComponent({
    entryComponents: [VersionsPanelComponent],
    templateUrl: 'pageVersionsMenuTemplate.html',
    inputs: ['actionItem']
})
export class PageVersionsMenuComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public pageUuid: string;

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------
    constructor(
        private pageInfoService: IPageInfoService,
        private pageVersionSelectionService: PageVersionSelectionService
    ) {}

    $onInit(): void {
        this.pageVersionSelectionService.showToolbarContextIfNeeded();
        this.pageInfoService.getPageUUID().then((pageUUID: string) => {
            this.pageUuid = pageUUID;
        });
    }
}
