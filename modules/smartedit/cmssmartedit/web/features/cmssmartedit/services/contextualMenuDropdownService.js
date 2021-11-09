/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name contextualMenuDropdownServiceModule.contextualMenuDropdownService
 * @description
 * contextualMenuDropdownService is an internal service that provides methods for interaction between
 * Drag and Drop Service and the Contextual Menu.
 */

angular
    .module('contextualMenuDropdownServiceModule', [
        'contextualMenuDecoratorModule',
        'smarteditServicesModule'
    ])
    .run(function(contextualMenuDropdownService) {
        contextualMenuDropdownService._registerIsOpenEvent();
    })

    /**
     *  Note: The contextualMenuDropdownService functions as a glue between the Drag and Drop Service and the Contextual Menu.
     *  The service was created to solve the issue of closing any contextual menu that is open whenever a drag operation is started.
     *  It does so while keeping the DnD and Contextual Menu services decoupled.
     */
    .service('contextualMenuDropdownService', function(
        systemEventService,
        CTX_MENU_DROPDOWN_IS_OPEN,
        CLOSE_CTX_MENU,
        DRAG_AND_DROP_EVENTS
    ) {
        var unsubscribeFn;

        this._registerIsOpenEvent = function() {
            systemEventService.subscribe(
                CTX_MENU_DROPDOWN_IS_OPEN,
                function() {
                    this._registerDragStarted();
                }.bind(this)
            );
        };

        this._registerDragStarted = function() {
            unsubscribeFn = systemEventService.subscribe(
                DRAG_AND_DROP_EVENTS.DRAG_STARTED,
                this._triggerCloseOperation
            );
        };

        this._triggerCloseOperation = function() {
            systemEventService.publishAsync(CLOSE_CTX_MENU);
            if (unsubscribeFn) {
                unsubscribeFn();
            }
        }.bind(this);
    });
