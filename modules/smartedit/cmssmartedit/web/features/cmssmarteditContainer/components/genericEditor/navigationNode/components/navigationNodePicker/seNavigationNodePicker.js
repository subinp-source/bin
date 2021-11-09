/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seNavigationNodePickerModule', [
        'functionsModule',
        'resourceLocationsModule',
        'cmsSmarteditServicesModule',
        'smarteditServicesModule'
    ])
    .constant('SELECTED_NODE', 'selected_node')
    .controller('NavigationNodePickerController', function(
        URIBuilder,
        NAVIGATION_MANAGEMENT_RESOURCE_URI,
        NAVIGATION_NODE_ROOT_NODE_UID,
        SELECTED_NODE,
        cmsitemsRestService,
        $log,
        systemEventService
    ) {
        this.$onInit = function() {
            this.nodeURI = new URIBuilder(NAVIGATION_MANAGEMENT_RESOURCE_URI)
                .replaceParams(this.uriContext)
                .build();
            this.removeDefaultTemplate = true;

            if (this.editable === undefined) {
                this.editable = true;
            }
        };

        this.nodeTemplateUrl = 'navigationNodePickerRenderTemplate.html';
        this.rootNodeUid = NAVIGATION_NODE_ROOT_NODE_UID;

        this.actions = {
            pick: function(treeService, node) {
                var requestParams = {
                    pageSize: 10,
                    currentPage: 0,
                    mask: node.uid
                };

                cmsitemsRestService.get(requestParams).then(
                    function(response) {
                        var idObj = {
                            nodeUuid: response.response.find(function(element) {
                                return element.uid === node.uid;
                            }).uuid,
                            nodeUid: node.uid
                        };
                        systemEventService.publishAsync(SELECTED_NODE, idObj);
                    }.bind(this)
                );
            }.bind(this),

            isEditable: function() {
                return this.editable === true;
            }.bind(this)
        };
    })
    /**
     * @ngdoc directive
     * @name seNavigationNodePickerModule.directive:seNavigationPicker
     * @scope
     * @restrict E
     * @element ANY
     *
     * @description
     * Directive that will build a navigation node picker and assign the uid of the selected node to model[qualifier].
     * @param {Object} uriContext the {@link resourceLocationsModule.object:UriContext UriContext} necessary to perform operations.
     * @param {Object} model the model a property of which will be set to the selected node uid.
     * @param {String} qualifier the name of the model property that will be set to the selected node uid.
     * @param {<Boolean=} editable This property specifies whether the selector can be edited or not. If this flag is false,
     * then the selector is treated as read-only; the selection cannot be modified in any way. Optional, default value is true.
     */
    .component('seNavigationPicker', {
        templateUrl: 'seNavigationNodePickerTemplate.html',
        bindings: {
            uriContext: '<',
            model: '=',
            qualifier: '=',
            editable: '<?'
        },
        controller: 'NavigationNodePickerController',
        controllerAs: 'ctrl'
    });
