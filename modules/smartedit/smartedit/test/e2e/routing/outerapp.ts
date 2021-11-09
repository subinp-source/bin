/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
/* tslint:disable:max-classes-per-file */
import * as angular from 'angular';
import { SeEntryModule, SeRouteService, SeTranslationModule, WindowUtils } from 'smarteditcommons';

angular
    .module('outerapp', ['ui.bootstrap', 'ngRoute', 'smarteditServicesModule'])
    .config(function($routeProvider: ng.route.IRouteProvider) {
        SeRouteService.init($routeProvider);
        SeRouteService.provideLegacyRoute({
            path: '/test',
            route: {
                templateUrl: 'web/test.html'
            }
        });

        SeRouteService.provideLegacyRoute({
            path: '/customView',
            route: {
                templateUrl: 'web/customView.html'
            },
            titleI18nKey: 'CustomViewShortcutLink'
        });
    })
    .directive('customView', (windowUtils: WindowUtils) => {
        return {
            restrict: 'C',
            controller() {
                windowUtils.setTrustedIframeDomain('http://127.0.0.1:7000');
            }
        };
    })
    .run(function($templateCache: angular.ITemplateCacheService) {
        $templateCache.put(
            'web/test.html',
            '<div class="customView"> \n' +
                'Test View' +
                '</div>' +
                '<iframe src="/test/e2e/routing/customiframe.html" style="width:100%;height:800px"></iframe>'
        );

        $templateCache.put(
            'web/customView.html',
            `<div >
				<div class= 'content'>
				<br/>
				custom view {{ 1+1 }}
				</div>
				</div>`
        );
    })
    .controller('DefaultCtrl', function(urlService: any) {
        this.navigateToJSView = function() {
            urlService.path('/customView');
        };
        this.navigateToAngularView = function() {
            urlService.path('/ng/a');
        };
    });

angular.module('smarteditcontainer').requires.push('outerapp');

@Component({
    selector: 'tests-page',
    template: `
        <div class="content" [translate]="'setestpage'"></div>
    `
})
export class TestPageComponent {}

@SeEntryModule('outerapp')
@NgModule({
    imports: [
        SeTranslationModule.forChild(),
        SeRouteService.provideNgRoute(
            [
                {
                    path: 'ng/a',
                    titleI18nKey: 'NgTestPage',
                    pathMatch: 'full',
                    component: TestPageComponent
                }
            ],
            { useHash: true, initialNavigation: true }
        )
    ],
    declarations: [TestPageComponent]
})
export class SeRouteTestModule {}
