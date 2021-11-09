/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name basicContextualMenuDecoratorModule
 * @description
 * Provides decorator for basic slot contextual menu.
 */
angular
    .module('basicContextualMenuDecoratorModule', ['smarteditServicesModule'])
    .run(function(decoratorService) {
        decoratorService.addMappings({
            '^.*Slot$': ['se.basicSlotContextualMenu']
        });
        decoratorService.enable('se.basicSlotContextualMenu');
    });
