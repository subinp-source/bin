/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('RenderToolbarItemModule', ['smarteditServicesModule'])
    .run(function(toolbarServiceFactory, renderService) {
        var toolbarService = toolbarServiceFactory.getToolbarService('smartEditPerspectiveToolbar');
        toolbarService.addItems([
            {
                key: 'toolbar.action.render.component',
                type: 'ACTION',
                nameI18nKey: 'toolbar.action.render.component',
                priority: 1,
                callback: function() {
                    renderService.renderComponent('component1', 'componentType1');
                },
                icons: ['render.png']
            },
            {
                key: 'toolbar.action.render.slot',
                type: 'ACTION',
                nameI18nKey: 'toolbar.action.render.slot',
                priority: 2,
                callback: function() {
                    renderService.renderSlots(['topHeaderSlot']);
                },
                icons: ['render.png']
            }
        ]);
    });

angular.module('smarteditcontainer').requires.push('RenderToolbarItemModule');
