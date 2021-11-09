/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { AuthenticationManager } from './AuthenticationManager';
import { UpgradeModule } from '@angular/upgrade/static';
import { LANDING_PAGE_PATH } from '../utils';

describe('AuthenticationManager', () => {
    let $route: jasmine.SpyObj<angular.route.IRouteService>;
    let $location: jasmine.SpyObj<angular.ILocationService>;
    let upgrade: jasmine.SpyObj<UpgradeModule>;
    let manager: AuthenticationManager;

    beforeEach(() => {
        $route = jasmine.createSpyObj('$route', ['reload']);
        $location = jasmine.createSpyObj('$location', ['url']);

        upgrade = jasmine.createSpyObj('upgrade', ['$injector']);
        upgrade.$injector = jasmine.createSpyObj('$injector', ['get']);
        upgrade.$injector.get.and.callFake((key: string) => {
            switch (key) {
                case '$route':
                    return $route;
                case '$location':
                    return $location;
                default:
                    return null;
            }
        });

        manager = new AuthenticationManager(upgrade);
    });

    it('will reload current page if current page is landing page', () => {
        $location.url.and.callFake((arg: any) => {
            if (!arg) {
                return LANDING_PAGE_PATH;
            }
            return null;
        });

        manager.onLogout();

        expect($location.url).toHaveBeenCalledWith();
        expect($route.reload).toHaveBeenCalled();
    });

    it('will reload current page if current page is empty', () => {
        $location.url.and.callFake((arg: any) => {
            if (!arg) {
                return '';
            }
            return null;
        });

        manager.onLogout();

        expect($location.url).toHaveBeenCalledWith();
        expect($route.reload).toHaveBeenCalled();
    });

    it('will redirect to landing page if current page is not landing page', () => {
        $location.url.and.callFake((arg: any) => {
            if (!arg) {
                return '/somepage';
            }
            return null;
        });

        manager.onLogout();

        expect($location.url.calls.count()).toBe(2);
        expect($location.url.calls.argsFor(0)).toEqual([]);
        expect($location.url.calls.argsFor(1)).toEqual([LANDING_PAGE_PATH]);
    });
});
