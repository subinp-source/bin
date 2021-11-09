/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, Input } from '@angular/core';
import { Location } from '@angular/common';
import { IframeManagerService } from 'smarteditcontainer/services/iframe/IframeManagerService';
import './sitesLink.scss';

@Component({
    selector: 'sites-link',
    templateUrl: './sitesLinkTemplate.html'
})
export class SitesLinkComponent {
    @Input()
    cssClass: string;

    @Input()
    iconCssClass: string = 'sap-icon--navigation-right-arrow';

    @Input()
    shortcutLink: any;

    constructor(
        private location: Location,
        private iframeManagerService: IframeManagerService,
        @Inject('LANDING_PAGE_PATH') private LANDING_PAGE_PATH: string
    ) {}

    goToSites() {
        this.iframeManagerService.setCurrentLocation(null);
        this.location.go(this.LANDING_PAGE_PATH);
    }
}
