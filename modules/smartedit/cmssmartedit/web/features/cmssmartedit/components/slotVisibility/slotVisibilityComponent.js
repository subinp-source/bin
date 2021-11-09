/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name slotVisibilityComponentModule
 *
 * @description
 * The slot visibility component module provides a directive and controller to display the hidden components of a specified content slot.
 *
 * @requires componentVisibilityAlertServiceModule
 * @requires editorModalServiceModule
 */
angular
    .module('slotVisibilityComponentModule', [
        'componentVisibilityAlertServiceModule',
        'cmsSmarteditServicesModule',
        'hiddenComponentMenuModule'
    ])

    .controller('slotVisibilityComponentController', function(
        $injector,
        componentVisibilityAlertService,
        editorModalService,
        catalogService,
        catalogVersionPermissionService,
        componentSharedService,
        domain
    ) {
        this.openEditorModal = function() {
            editorModalService
                .openAndRerenderSlot(this.component.typeCode, this.component.uuid, 'visible')
                .then(
                    function(item) {
                        componentVisibilityAlertService.checkAndAlertOnComponentVisibility({
                            itemId: item.uuid,
                            itemType: item.itemtype,
                            catalogVersion: item.catalogVersion,
                            restricted: item.restricted,
                            slotId: this.slotId,
                            visible: item.visible
                        });
                    }.bind(this)
                );
        };

        this.$onInit = function() {
            this.isReady = false;
            this.imageRoot = domain + '/cmssmartedit/images';
            componentSharedService.isComponentShared(this.component).then(
                function(isShared) {
                    this.isSharedComponent = isShared;
                }.bind(this)
            );

            catalogService.getCatalogVersionByUuid(this.component.catalogVersion).then(
                function(catalogVersionObj) {
                    catalogVersionPermissionService
                        .hasWritePermission(catalogVersionObj.catalogId, catalogVersionObj.version)
                        .then(
                            function(isWritable) {
                                this.readOnly = !isWritable || this.component.isExternal;
                                this.componentVisibilitySwitch = this.component.visible
                                    ? 'se.cms.component.visibility.status.on'
                                    : 'se.cms.component.visibility.status.off';
                                this.componentRestrictionsCount =
                                    '(' +
                                    (this.component.restrictions
                                        ? this.component.restrictions.length
                                        : 0) +
                                    ')';
                                this.isReady = true;
                            }.bind(this)
                        );
                }.bind(this)
            );
        };
    })

    /**
     * @ngdoc directive
     * @name slotVisibilityComponentModule.directive:slotVisibilityComponent
     *
     * @description
     * The slot visibility component directive is used to display information about a specified hidden component.
     * It receives the component on its scope and it binds it to its own controller.
     */
    .component('slotVisibilityComponent', {
        templateUrl: 'slotVisibilityComponentTemplate.html',
        transclude: false,
        controller: 'slotVisibilityComponentController',
        controllerAs: 'ctrl',
        bindings: {
            component: '=',
            slotId: '@',
            componentId: '@'
        }
    });
