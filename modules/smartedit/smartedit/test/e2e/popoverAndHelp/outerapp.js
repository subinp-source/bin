/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('outerapp', ['smarteditServicesModule', 'templateCacheDecoratorModule', 'yHelpModule'])
    .run(function($templateCache) {
        $templateCache.put('web/body.html', '<b>some HTML body</b>');
    })
    .controller('defaultController', function() {
        this.templateUrl = 'web/body.html';
        this.template = '<div>some inline template</div>';
        this.title = 'someTitle.key';
        this.placement1 = 'top';
        this.placement2 = 'right';
        this.trigger1 = 'hover';
        this.trigger2 = 'click';
    });
