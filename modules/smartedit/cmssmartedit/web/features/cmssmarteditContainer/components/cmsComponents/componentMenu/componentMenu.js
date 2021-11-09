/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentMenuModule', [
        'smarteditServicesModule',
        'componentTypesTabModule',
        'componentsTabModule',
        'cmsDragAndDropServiceModule',
        'componentMenuServiceModule'
    ])
    .controller('componentMenuController', function(
        $q,
        crossFrameEventService,
        componentMenuService,
        SMARTEDIT_DRAG_AND_DROP_EVENTS,
        EVENTS,
        DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME
    ) {
        // --------------------------------------------------------------------------------------------------
        // Constants
        // --------------------------------------------------------------------------------------------------
        var OPEN_COMPONENT_EVENT = 'ySEComponentMenuOpen';
        var OVERLAY_DISABLED_EVENT = 'OVERLAY_DISABLED';
        var RESET_COMPONENT_MENU_EVENT = 'RESET_COMPONENT_MENU_EVENT';

        var TAB_IDS = {
            COMPONENT_TYPES_TAB_ID: 'componentTypesTab',
            COMPONENTS_TAB_ID: 'componentsTab'
        };

        // --------------------------------------------------------------------------------------------------
        // Instance Variables
        // --------------------------------------------------------------------------------------------------
        this.isDragging = false;
        this.model = {};

        // --------------------------------------------------------------------------------------------------
        // Methods
        // --------------------------------------------------------------------------------------------------
        this.$onInit = function() {
            this._recompileDom = function() {};
            this._initializeComponentMenu();
            this.removePageChangeEventHandler = crossFrameEventService.subscribe(
                EVENTS.PAGE_CHANGE,
                this._initializeComponentMenu
            );
            this.removeOpenComponentEventHandler = crossFrameEventService.subscribe(
                OPEN_COMPONENT_EVENT,
                this._resetComponentMenu
            );
            this.removeDropdownEvent = crossFrameEventService.subscribe(
                SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_START,
                function() {
                    this.isDragging = true;
                    this._closeMenu();
                }.bind(this)
            );
            this.removeOverlapEvent = crossFrameEventService.subscribe(
                OVERLAY_DISABLED_EVENT,
                this._closeMenu
            );
            this.removeDragEndEvent = crossFrameEventService.subscribe(
                SMARTEDIT_DRAG_AND_DROP_EVENTS.DRAG_DROP_END,
                function() {
                    this.isDragging = false;
                    crossFrameEventService.publish(DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME.END);
                }.bind(this)
            );
        };

        this.$onDestroy = function() {
            this.removePageChangeEventHandler();
            this.removeOpenComponentEventHandler();
            this.removeDropdownEvent();
            this.removeOverlapEvent();
            this.removeDragEndEvent();
        };

        // --------------------------------------------------------------------------------------------------
        // Helper Methods
        // --------------------------------------------------------------------------------------------------
        this._initializeComponentMenu = function() {
            // This is to ensure that the component menu DOM is completely clean, even after a page change.
            this.tabsList = null;
            this._recompileDom();

            componentMenuService.hasMultipleContentCatalogs().then(
                function(hasMultipleContentCatalogs) {
                    this.hasMultipleContentCatalogs = hasMultipleContentCatalogs;

                    var componentTypesTab = {
                        id: TAB_IDS.COMPONENT_TYPES_TAB_ID,
                        title: 'se.cms.compomentmenu.tabs.componenttypes',
                        templateUrl: 'componentTypesTabWrapperTemplate.html'
                    };
                    var componentsTab = {
                        id: TAB_IDS.COMPONENTS_TAB_ID,
                        title: 'se.cms.compomentmenu.tabs.customizedcomp',
                        templateUrl: 'componentsTabWrapperTemplate.html'
                    };

                    this.model = {
                        componentsTab: { hasMultipleContentCatalogs: hasMultipleContentCatalogs },
                        isOpen: this.isOpen
                    };
                    this.tabsList = [componentTypesTab, componentsTab];

                    // This variable is assigned in the tabsList to make sure there's no
                    // tight coupling with the view (instead of relying on a position in the array).
                    this._resetComponentMenu();
                }.bind(this)
            );
        }.bind(this);

        this._resetComponentMenu = function() {
            if (this.tabsList) {
                this.tabsList.map(function(tab) {
                    tab.active = tab.id === TAB_IDS.COMPONENT_TYPES_TAB_ID;
                    return tab.active;
                });

                // Reset tab contents.
                crossFrameEventService.publish(RESET_COMPONENT_MENU_EVENT);
            }
        }.bind(this);

        this._closeMenu = function() {
            if (this.actionItem) {
                this.actionItem.isOpen = false;
            }
        }.bind(this);

        this.$onChanges = function() {
            if (this.isOpen) {
                crossFrameEventService.publish(DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME.START);
            } else if (!this.isOpen && !this.isDragging) {
                crossFrameEventService.publish(DRAG_AND_DROP_CROSS_ORIGIN_BEFORE_TIME.END);
            }
        };
    })
    .component('componentMenu', {
        templateUrl: 'componentMenuTemplate.html',
        controller: 'componentMenuController',
        bindings: {
            actionItem: '<',
            isOpen: '<'
        }
    });
