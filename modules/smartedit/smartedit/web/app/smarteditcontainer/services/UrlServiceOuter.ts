/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { GatewayProxied, IUrlService, SeDowngradeService, WindowUtils } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IUrlService)
@GatewayProxied('openUrlInPopup', 'path')
@Injectable()
export class UrlService extends IUrlService {
    constructor(
        private router: Router,
        private location: Location,
        private windowUtils: WindowUtils
    ) {
        super();
    }

    openUrlInPopup(url: string): void {
        const win: Window = this.windowUtils
            .getWindow()
            .open(url, '_blank', 'toolbar=no, scrollbars=yes, resizable=yes');
        win.focus();
    }

    path(url: string) {
        /**
         * Route registered in angularjs application does not work
         * if the angular route has been used to navigate to currently previewed page.
         * Same happening if we first navigate to angularjs page and angular routing stops working.
         * The possible solution: use location and router service at the same time.
         */
        this.location.go(url);
        this.router.navigateByUrl(url);
    }
}
