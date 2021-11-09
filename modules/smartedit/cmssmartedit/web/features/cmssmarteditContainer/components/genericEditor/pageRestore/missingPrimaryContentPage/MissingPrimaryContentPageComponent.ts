/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    FetchStrategy,
    ISeComponent,
    Page as SePage,
    SelectItem,
    SeComponent
} from 'smarteditcommons';
import { CMSItem, ICMSPage, IPageService } from 'cmscommons';

@SeComponent({
    templateUrl: 'missingPrimaryContentPageTemplate.html',
    inputs: ['model']
})
export class MissingPrimaryContentPageComponent implements ISeComponent {
    public model: ICMSPage;
    // ------------------------------------------------------------------------
    // Variables
    // ------------------------------------------------------------------------
    public fetchStrategy: FetchStrategy<SelectItem> = {
        fetchEntity: () => this.fetchEntity(),
        fetchPage: (search: string, pageSize: number, currentPage: number) =>
            this.fetchPage(search, pageSize, currentPage)
    };

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------
    private readonly CONTENT_PAGE_TYPE_CODE = 'ContentPage';

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    constructor(private pageService: IPageService, private $log: angular.ILogService) {}

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    private fetchEntity(): Promise<SelectItem> {
        return Promise.resolve(this.getSelectItemFromPrimaryPage(this.model));
    }

    private async fetchPage(
        search: string,
        pageSize: number,
        currentPage: number
    ): Promise<SePage<SelectItem>> {
        try {
            const page = await this.pageService.getPaginatedPrimaryPagesForPageType(
                this.CONTENT_PAGE_TYPE_CODE,
                null,
                {
                    search,
                    pageSize,
                    currentPage
                }
            );
            const targetPage: SePage<SelectItem> = {
                pagination: page.pagination,
                results: null
            };
            targetPage.results = page.response.map((rawPage) =>
                this.getSelectItemFromPrimaryPage(rawPage)
            );
            return targetPage;
        } catch (error) {
            this.$log.warn(
                '[MissingPrimaryContentPageComponent] - Cannot retrieve list of alternative content primary pages. ',
                error
            );
            return undefined;
        }
    }

    private getSelectItemFromPrimaryPage(page: CMSItem): SelectItem {
        return {
            id: page.label as string,
            label: page.name
        };
    }
}
