/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ICatalogService,
    IOnChangesObject,
    ISeComponent,
    IUriContext,
    SeComponent
} from 'smarteditcommons';
import { HomepageService, HomepageType } from 'cmssmarteditcontainer/services';
import { ICMSPage } from 'cmscommons';
import './HomepageIcon.scss';

/**
 * @ngdoc directive
 * @name pageComponentsModule.directive:homepageIconComponent
 * @scope
 * @restrict E
 * @element homepage-icon
 *
 * @description
 * Component responsible for displaying a homepage icon with the passed cms page and uri context inputs.
 */
@SeComponent({
    templateUrl: 'HomepageIconTemplate.html',
    inputs: ['cmsPage', 'uriContext']
})
export class HomepageIconComponent implements ISeComponent {
    public type: HomepageType = null;

    private cmsPage: ICMSPage;
    private uriContext: IUriContext;

    constructor(public homepageService: HomepageService, public catalogService: ICatalogService) {}

    $onChanges(changes: IOnChangesObject): void {
        if (
            changes.cmsPage.previousValue === changes.cmsPage.currentValue &&
            changes.uriContext.previousValue === changes.uriContext.currentValue
        ) {
            return;
        }

        if (this.cmsPage) {
            this.homepageService
                .getHomepageType(this.cmsPage, this.uriContext)
                .then((homepageType: HomepageType) => {
                    this.type = homepageType;
                });
        }
    }

    public isVisible() {
        return this.type !== null;
    }

    public getHomepageIcon() {
        return {
            'sap-icon--home se-home-page--current': HomepageType.CURRENT === this.type,
            'sap-icon--home se-home-page--old': HomepageType.OLD === this.type
        };
    }

    public getTooltipTemplate() {
        const state = HomepageType.CURRENT === this.type ? `current` : `previous`;
        return `<div data-translate="se.cms.homepage.tooltip.message.${state}"></div>`;
    }
}
