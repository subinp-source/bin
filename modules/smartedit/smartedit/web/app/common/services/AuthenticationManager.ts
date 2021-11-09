/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { Injectable } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';

import { stringUtils, IAuthenticationManagerService } from '@smart/utils';
import { LANDING_PAGE_PATH } from '../utils';

@Injectable()
export class AuthenticationManager extends IAuthenticationManagerService {
    constructor(private upgrade: UpgradeModule) {
        super();
    }

    private get $route(): angular.route.IRouteService {
        return this.upgrade.$injector.get('$route') as angular.route.IRouteService;
    }

    private get $location(): angular.ILocationService {
        return this.upgrade.$injector.get('$location') as angular.ILocationService;
    }

    public onLogout(): void {
        const currentLocation = this.$location.url();
        if (stringUtils.isBlank(currentLocation) || currentLocation === LANDING_PAGE_PATH) {
            this.$route.reload();
        } else {
            this.$location.url(LANDING_PAGE_PATH);
        }
    }

    public onUserHasChanged(): void {
        this.$route.reload();
    }
}
