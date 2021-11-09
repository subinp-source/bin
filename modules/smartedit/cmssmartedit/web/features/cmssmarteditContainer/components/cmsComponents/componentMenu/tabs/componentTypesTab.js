/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentTypesTabModule', [
        'smarteditServicesModule',
        'componentSearchModule',
        'componentTypeModule',
        'nameFilterModule',
        'cmsSmarteditServicesModule'
    ])
    .controller('componentTypesTabController', function(
        $q,
        $log,
        componentService,
        pageService,
        catalogService
    ) {
        // --------------------------------------------------------------------------------------------------
        // Constants
        // --------------------------------------------------------------------------------------------------
        this.pageInfo = null;
        this.uriContext = null;

        // --------------------------------------------------------------------------------------------------
        // Event Handlers
        // --------------------------------------------------------------------------------------------------
        this.onSearchTermChanged = function(searchTerm) {
            this.searchTerm = searchTerm;
        }.bind(this);

        this.loadPageContext = function() {
            if (this.pageInfo) {
                return $q.when();
            }

            return $q
                .all([
                    pageService.getCurrentPageInfo(),
                    $q.when(catalogService.retrieveUriContext())
                ])
                .then(
                    function(dataRetrieved) {
                        this.pageInfo = dataRetrieved[0];
                        this.uriContext = dataRetrieved[1];
                    }.bind(this)
                );
        };

        this.loadComponentTypes = function(mask, pageSize, currentPage) {
            return this.loadPageContext()
                .then(
                    function() {
                        var payload = {
                            pageId: this.pageInfo.uid,
                            catalogId: this.uriContext.CURRENT_CONTEXT_CATALOG,
                            catalogVersion: this.uriContext.CURRENT_CONTEXT_CATALOG_VERSION,
                            mask: mask,
                            pageSize: pageSize,
                            currentPage: currentPage
                        };

                        return componentService.getSupportedComponentTypesForCurrentPage(payload);
                    }.bind(this)
                )
                .then(
                    function(pageLoaded) {
                        return pageLoaded;
                    }.bind(this)
                )
                .catch(function(errData) {
                    $log.error(
                        'ComponentMenuController.$onInit() - error loading types. ' + errData
                    );
                });
        }.bind(this);
    })
    .component('componentTypesTab', {
        templateUrl: 'componentTypesTabTemplate.html',
        controller: 'componentTypesTabController',
        bindings: {
            isTabActive: '<',
            isMenuOpen: '<'
        }
    });
