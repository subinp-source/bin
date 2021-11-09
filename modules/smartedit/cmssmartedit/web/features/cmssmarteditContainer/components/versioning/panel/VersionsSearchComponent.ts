/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons';

/**
 * This component renders controls that can allow the user to search for a specific page version or
 * manage them.
 */
@SeComponent({
    templateUrl: 'versionsSearchTemplate.html',
    inputs: ['onSearchTermChanged: &', 'versionsFound', 'showSearchControls']
})
export class VersionsSearchComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public showResetButton: boolean;
    public showSearchControls: boolean;
    public versionsFound: number;
    public onSearchTermChanged: (param: any) => void;

    private previousSearchTerm: string;
    private searchTerm: string;

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------

    $onInit(): void {
        this.previousSearchTerm = '';
        this.showResetButton = false;
        this.resetSearchBox();
    }

    $doCheck(): void {
        if (this.searchTerm !== this.previousSearchTerm) {
            this.previousSearchTerm = this.searchTerm;
            this.showResetButton = this.searchTerm !== '';
            this.onSearchTermChanged({
                newSearchTerm: this.searchTerm
            });
        }
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    public resetSearchBox(): void {
        this.searchTerm = '';
    }

    public getVersionsFoundTranslationData(): any {
        return {
            versionsFound: this.versionsFound
        };
    }
}
