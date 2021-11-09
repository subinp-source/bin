/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, LOCALE_ID, OnDestroy, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import {
    setupL10nFilter,
    CrossFrameEventService,
    DATE_CONSTANTS,
    EVENT_SERVICE,
    EVENTS,
    IExperience,
    IExperienceCatalogVersion,
    ISharedDataService,
    LanguageService,
    SeDowngradeComponent,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import * as moment from 'moment';

/** @internal  */
@SeDowngradeComponent()
@Component({
    selector: 'se-experience-selector-button',
    templateUrl: './ExperienceSelectorButtonComponent.html'
})
export class ExperienceSelectorButtonComponent implements OnInit, OnDestroy {
    public resetExperienceSelector: () => void;
    public status: { isOpen: boolean } = { isOpen: false };
    public isCurrentPageFromParent: boolean = false;
    public parentCatalogVersion: string;
    public experience: IExperience;

    private l10nFilter: (localizedMap: TypedMap<string> | string) => TypedMap<string> | string;
    private unregFn: () => void;
    private unRegNewPageContextEventFn: () => void;

    constructor(
        private systemEventService: SystemEventService,
        @Inject(EVENT_SERVICE) private crossFrameEventService: CrossFrameEventService,
        @Inject(LOCALE_ID) private locale: string,
        private sharedDataService: ISharedDataService,
        private languageService: LanguageService
    ) {
        this.l10nFilter = setupL10nFilter(this.languageService, crossFrameEventService);
    }

    ngOnInit() {
        this.updateExperience();
        this.unregFn = this.systemEventService.subscribe(EVENTS.EXPERIENCE_UPDATE, () =>
            this.updateExperience()
        );

        this.unRegNewPageContextEventFn = this.crossFrameEventService.subscribe(
            EVENTS.PAGE_CHANGE,
            (eventId: string, data: IExperience) => this.setPageFromParent(data)
        );
    }

    ngOnDestroy() {
        this.unregFn();
        this.unRegNewPageContextEventFn();
    }

    public async updateExperience(): Promise<void> {
        this.experience = (await this.sharedDataService.get('experience')) as IExperience;
    }

    public buildExperienceText(): string {
        if (!this.experience) {
            return '';
        }

        const {
            catalogDescriptor: { name, catalogVersion },
            languageDescriptor: { nativeName },
            time
        } = this.experience;
        const pipe = new DatePipe(this.locale);

        const transformedTime = time
            ? `  |  ${pipe.transform(
                  moment(time).isValid() ? time : moment.now(),
                  DATE_CONSTANTS.ANGULAR_SHORT
              )}`
            : '';

        return `${this.l10nFilter(
            name
        )} - ${catalogVersion}  |  ${nativeName}${transformedTime}${this._returnProductCatalogVersionTextByUuids()}`;
    }

    public setPageFromParent(data: IExperience): void {
        const {
            pageContext: {
                catalogName,
                catalogVersion,
                catalogVersionUuid: pageContextCatalogVersionUuid
            },
            catalogDescriptor: { catalogVersionUuid: catalogDescriptorCatalogVersionUuid }
        } = data;

        this.parentCatalogVersion = `${this.l10nFilter(catalogName)} (${catalogVersion})`;

        this.isCurrentPageFromParent =
            catalogDescriptorCatalogVersionUuid !== pageContextCatalogVersionUuid;
    }

    private _returnProductCatalogVersionTextByUuids(): string {
        const { productCatalogVersions } = this.experience;

        return productCatalogVersions.reduce(
            (acc: string, { catalogName, catalogVersion }: IExperienceCatalogVersion) =>
                `${acc} | ${this.l10nFilter(catalogName)} (${catalogVersion})`,
            ''
        );
    }
}
