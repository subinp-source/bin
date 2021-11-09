/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { annotationService, GatewayProxied, WindowUtils } from 'smarteditcommons';
import { UrlService } from 'smarteditcontainer/services';

describe('test urlService ', () => {
    let urlService: UrlService;

    let $window: jasmine.SpyObj<angular.IWindowService>;
    let popupWindow: jasmine.SpyObj<Window>;

    let router: jasmine.SpyObj<Router>;
    let location: jasmine.SpyObj<Location>;
    let windowUtils: WindowUtils;

    beforeEach(() => {
        location = jasmine.createSpyObj<Location>('location', ['go']);
        router = jasmine.createSpyObj<Router>('router', ['navigateByUrl']);

        $window = jasmine.createSpyObj<Window>('$window', ['open', 'focus']);
        popupWindow = jasmine.createSpyObj<Window>('popupWindow', ['open', 'focus']);

        $window.open.and.returnValue(popupWindow);

        windowUtils = new WindowUtils();
        spyOn(windowUtils, 'getWindow').and.returnValue($window);

        urlService = new UrlService(router, location, windowUtils);
    });

    it('url service inits a private gateway', function() {
        expect(annotationService.getClassAnnotation(UrlService, GatewayProxied)).toEqual([
            'openUrlInPopup',
            'path'
        ]);
    });

    it('WHEN openUrlInPopup is called THEM it should open a the url in a popup', function() {
        const ANY_URL = 'http://a.com/';
        urlService.openUrlInPopup(ANY_URL);
        expect($window.open).toHaveBeenCalledWith(
            ANY_URL,
            '_blank',
            'toolbar=no, scrollbars=yes, resizable=yes'
        );
        expect(popupWindow.focus).toHaveBeenCalled();
    });

    it('path calls location and router', function() {
        const ANY_URL = '/some/path';
        urlService.path(ANY_URL);
        expect(location.go).toHaveBeenCalledWith(ANY_URL);
        expect(router.navigateByUrl).toHaveBeenCalledWith(ANY_URL);
    });
});
