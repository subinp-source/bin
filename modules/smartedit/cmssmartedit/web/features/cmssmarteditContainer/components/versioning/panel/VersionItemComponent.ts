/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons';
import { CMSModesService } from 'cmscommons';
import { IPageVersion } from 'cmssmarteditcontainer/services';
import { PageVersionSelectionService } from '../services/PageVersionSelectionService';

/**
 * This component displays a page version entry in the list of versions.
 */
@SeComponent({
    templateUrl: 'versionItemTemplate.html',
    inputs: ['pageVersion']
})
export class VersionItemComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    private pageVersion: IPageVersion;
    private VERSIONING_MODE_KEY: string = CMSModesService.VERSIONING_PERSPECTIVE_KEY;
    private truncated: boolean;

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------
    constructor(
        private pageVersionSelectionService: PageVersionSelectionService,
        private perspectiveService: any,
        private cMSModesService: CMSModesService
    ) {}

    $onInit(): void {
        this.truncated = true;
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    public selectVersion(): void {
        this.cMSModesService
            .isVersioningPerspectiveActive()
            .then((isVersioningModeActive: boolean) => {
                if (!isVersioningModeActive) {
                    this.perspectiveService.switchTo(this.VERSIONING_MODE_KEY);
                }
                this.pageVersionSelectionService.selectPageVersion(this.pageVersion);
            });
    }

    public isSelectedVersion(): boolean {
        const selectedVersion = this.pageVersionSelectionService.getSelectedPageVersion();
        return selectedVersion && selectedVersion.uid === this.pageVersion.uid;
    }

    public isVersionMenuEnabled(): boolean {
        const activePerspective = this.perspectiveService.getActivePerspective();
        return activePerspective && activePerspective.key === this.VERSIONING_MODE_KEY;
    }

    public toggleTruncate(): void {
        this.truncated = !this.truncated;
    }

    public isTruncated(): boolean {
        return this.truncated;
    }

    public getLinkLabel(): string {
        return this.truncated ? 'se.moretext.more.link' : 'se.moretext.less.link';
    }

    public hasMoreText(): boolean {
        if (this.pageVersion.description) {
            return this.pageVersion.description.length > 32;
        }
        return false;
    }
}
