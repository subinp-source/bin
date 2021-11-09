/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';

import { SeInjectable, SystemEventService } from 'smarteditcommons';
import {
    DisplayConditionsFacade,
    IDisplayConditionsPageVariation,
    IDisplayConditionsPrimaryPage
} from '../../facades/displayConditionsFacade';
import { ICMSPage } from 'cmscommons';

@SeInjectable()
export class DisplayConditionsEditorModel {
    public variations: IDisplayConditionsPageVariation[] = [];
    public associatedPrimaryPage: Partial<IDisplayConditionsPrimaryPage>;
    public originalPrimaryPage: Partial<IDisplayConditionsPrimaryPage>;
    public isAssociatedPrimaryReadOnly: boolean;
    public pageType: string;
    public pageUid: string;
    public pageName: string;
    public isPrimary: boolean;

    constructor(
        private displayConditionsFacade: DisplayConditionsFacade,
        private systemEventService: SystemEventService
    ) {}

    public async initModel(pageUid: string): Promise<void> {
        this.pageUid = pageUid;

        const page = await this.displayConditionsFacade.getPageInfoForPageUid(pageUid);

        this.pageName = page.pageName;
        this.pageType = page.pageType;
        this.isPrimary = page.isPrimary;

        if (this.isPrimary) {
            return this._initModelForPrimary(pageUid);
        } else {
            return this._initModelForVariation(pageUid);
        }
    }

    public setAssociatedPrimaryPage(associatedPrimaryPage: IDisplayConditionsPrimaryPage): void {
        this.systemEventService.publishAsync(
            'EDIT_PAGE_ASSOCIATED_PRIMARY_UPDATED_EVENT',
            associatedPrimaryPage
        );
        this.associatedPrimaryPage = associatedPrimaryPage;
    }

    public save(): Promise<ICMSPage | boolean> {
        return this.isDirty()
            ? this.displayConditionsFacade.updatePage(this.pageUid, {
                  label: this.associatedPrimaryPage.label
              } as ICMSPage)
            : Promise.resolve(true);
    }

    public isDirty(): boolean {
        return (
            !!this.originalPrimaryPage &&
            !!this.associatedPrimaryPage &&
            !lodash.isEqual(this.originalPrimaryPage, this.associatedPrimaryPage)
        );
    }

    private async _initModelForPrimary(pageUid: string): Promise<void> {
        this.variations = await this.displayConditionsFacade.getVariationsForPageUid(pageUid);
    }

    private async _initModelForVariation(pageUid: string): Promise<void> {
        this.isAssociatedPrimaryReadOnly = this.pageType !== 'ContentPage';

        const associatedPrimaryPage = await this.displayConditionsFacade.getPrimaryPageForVariationPage(
            pageUid
        );

        this.associatedPrimaryPage = associatedPrimaryPage;
        this.originalPrimaryPage = associatedPrimaryPage;
    }
}
