/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seNavigationNodeSelector', [
        'seBreadcrumbModule',
        'seNavigationNodePickerModule',
        'resourceLocationsModule',
        'cmsSmarteditServicesModule'
    ])
    .controller('NavigationNodeSelectorController', function(
        SELECTED_NODE,
        catalogService,
        cmsitemsRestService,
        systemEventService
    ) {
        this.isReady = function() {
            return this.ready;
        };

        this.remove = function() {
            delete this.model[this.qualifier];
        }.bind(this);

        this.$onInit = function() {
            this.ready = false;
            catalogService.retrieveUriContext().then(
                function(uriContext) {
                    this.uriContext = uriContext;

                    if (this.model[this.qualifier]) {
                        cmsitemsRestService.getById(this.model[this.qualifier]).then(
                            function(response) {
                                this.nodeUid = response.uid;
                                this.ready = true;
                            }.bind(this)
                        );
                    } else {
                        this.ready = true;
                    }
                }.bind(this)
            );

            this.unregFn = systemEventService.subscribe(
                SELECTED_NODE,
                function(eventId, idObj) {
                    this.nodeUid = idObj.nodeUid;
                    this.model[this.qualifier] = idObj.nodeUuid;
                }.bind(this)
            );
        };

        this.$onDestroy = function() {
            this.unregFn();
        };
    })
    .component('seNavigationNodeSelector', {
        templateUrl: 'seNavigationNodeSelectorTemplate.html',
        controller: 'NavigationNodeSelectorController',
        controllerAs: 'nav',
        bindings: {
            field: '<',
            model: '<',
            qualifier: '<'
        }
    });
