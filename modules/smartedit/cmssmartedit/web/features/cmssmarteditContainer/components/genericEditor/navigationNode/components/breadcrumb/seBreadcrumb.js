/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seBreadcrumbModule', [
        'resourceLocationsModule',
        'navigationEditorNodeServiceModule',
        'resourceModule',
        'functionsModule',
        'cmsSmarteditServicesModule'
    ])
    .controller('BreadcrumbController', function(
        $q,
        navigationEditorNodeService,
        cmsitemsRestService
    ) {
        this.$onInit = function() {
            if (!this.nodeUid && !this.nodeUuid) {
                throw new Error('seBreadcrumb component requires either nodeUid or nodeUuid');
            }

            var promise = this.nodeUid
                ? $q.when({
                      uid: this.nodeUid
                  })
                : cmsitemsRestService.getById(this.nodeUuid);

            promise.then(
                function(response) {
                    navigationEditorNodeService
                        .getNavigationNodeAncestry(response.uid, this.uriContext)
                        .then(
                            function(ancestry) {
                                this.breadcrumb = ancestry;
                            }.bind(this)
                        );
                }.bind(this)
            );
        };
    })
    /**
     * @ngdoc directive
     * @name seBreadcrumbModule.directive:seBreadcrumb
     * @scope
     * @restrict E
     * @element ANY
     *
     * @description
     * Directive that will build a navigation breadcrumb for the Node identified by either uuid or uid.
     * @param {String=} nodeUid the uid of the node the breadcrumb of which we want to build.
     * @param {String=} nodeUuid the uuid of the node the breadcrumb of which we want to build.
     * @param {Object} uriContext the {@link resourceLocationsModule.object:UriContext UriContext} necessary to perform operations.
     */
    .component('seBreadcrumb', {
        templateUrl: 'seBreadcrumbTemplate.html',
        bindings: {
            nodeUid: '<?',
            nodeUuid: '<?',
            uriContext: '<'
        },
        controller: 'BreadcrumbController',
        controllerAs: 'bc'
    });
