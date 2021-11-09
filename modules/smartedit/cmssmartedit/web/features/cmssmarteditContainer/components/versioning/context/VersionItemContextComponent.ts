/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as lo from 'lodash';

import { ISeComponent, SeComponent } from 'smarteditcommons';
import { IPageVersion } from 'cmssmarteditcontainer/services';
import { PageVersionSelectionService } from 'cmssmarteditcontainer/components/versioning/services/PageVersionSelectionService';

/**
 * This component is used to show selected page versions in the toolbar context.
 */
@SeComponent({
    templateUrl: 'versionItemContextTemplate.html'
})
export class VersionItemContextComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    private EMPTY_DESCRIPTION_MSG_KEY: string = 'se.cms.versions.no.description';

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------
    constructor(
        private pageVersionSelectionService: PageVersionSelectionService,
        private $translate: angular.translate.ITranslateService,
        private lodash: lo.LoDashStatic
    ) {}

    $onInit(): void {
        this.pageVersionSelectionService.hideToolbarContextIfNotNeeded();
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    public deselectPageVersion(): void {
        this.pageVersionSelectionService.deselectPageVersion();
    }

    public getPageVersion(): IPageVersion {
        return this.pageVersionSelectionService.getSelectedPageVersion();
    }

    public getPopoverTemplate(): string {
        const pageVersion = this.getPageVersion();

        const description =
            pageVersion && pageVersion.description
                ? this.lodash.escape(pageVersion.description)
                : this.$translate.instant(this.EMPTY_DESCRIPTION_MSG_KEY);

        return `<div class="se-version-item-context--description__wrapper">
			<span class="se-version-item-context--description">${description}</span>
		</div>`;
    }
}
