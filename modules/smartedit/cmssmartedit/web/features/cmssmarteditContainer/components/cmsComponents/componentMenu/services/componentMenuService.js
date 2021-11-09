/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentMenuServiceModule', ['smarteditServicesModule', 'yLoDashModule'])
    /**
     * This service provides functionality to the component menu; it's meant to be used internally. Thus, no ng-docs are added.
     */
    .service('componentMenuService', function(
        $timeout,
        storageService,
        catalogService,
        experienceService,
        lodash
    ) {
        // --------------------------------------------------------------------------------------------------
        // Constants
        // --------------------------------------------------------------------------------------------------
        var SELECTED_CATALOG_VERSION_COOKIE_NAME = 'se_catalogmenu_catalogversion_cookie';

        // --------------------------------------------------------------------------------------------------
        // Methods
        // --------------------------------------------------------------------------------------------------
        /**
         * This method is used to determine whether more than one content catalog is displayed according to the
         * catalogId in the page context.
         */
        this.hasMultipleContentCatalogs = function() {
            return this.getContentCatalogs().then(function(contentCatalogs) {
                return this._getPageContext().then(function(pageContext) {
                    var currentCatalogId = pageContext.catalogId;
                    // The sequence of content catalog in variable contentCatalogs indicates that
                    // the inheritance relationship of content catalog. Index 0 represents global content catalog.
                    var hierarchyIndex = contentCatalogs.findIndex(function(contentCatalog) {
                        return contentCatalog.catalogId === currentCatalogId;
                    });
                    return hierarchyIndex !== 0;
                });
            }.bind(this));
        }.bind(this);

        /**
         * This method is used to retrieve the content catalogs of the site in the page context.
         */
        this.getContentCatalogs = function() {
            return this._getPageContext().then(
                function(pageContext) {
                    return pageContext
                        ? catalogService.getContentCatalogsForSite(pageContext.siteId)
                        : [];
                }.bind(this)
            );
        }.bind(this);

        /**
         * Gets the list of catalog/catalog versions where components can be retrieved from for this page.
         */
        this.getValidContentCatalogVersions = function() {
            return this._getPageContext().then(
                function(pageContext) {
                    return this.getContentCatalogs().then(
                        function(contentCatalogs) {
                            // Return 'active' catalog versions for content catalogs, except for the
                            // catalog in the current experience.
                            var result = contentCatalogs.map(
                                function(catalog) {
                                    return this._getActiveOrCurrentVersionForCatalog(
                                        pageContext,
                                        catalog
                                    );
                                }.bind(this)
                            );

                            return result;
                        }.bind(this)
                    );
                }.bind(this)
            );
        }.bind(this);

        /**
         * Gets the list of catalog/catalog versions where components can be retrieved from for this page.
         */
        this._getActiveOrCurrentVersionForCatalog = function(pageContext, catalog) {
            var catalogVersion = catalog.versions.filter(function(catalogVersion) {
                if (pageContext.catalogId === catalog.catalogId) {
                    return pageContext.catalogVersion === catalogVersion.version;
                }
                return catalogVersion.active;
            })[0];

            return {
                isCurrentCatalog: pageContext.catalogVersion === catalogVersion.version,
                catalogName: catalog.name,
                catalogId: catalog.catalogId,
                catalogVersionId: catalogVersion.version,
                id: catalogVersion.uuid
            };
        };

        this._getPageContext = function() {
            return experienceService.getCurrentExperience().then(function(experience) {
                return experience.pageContext;
            });
        };

        // --------------------------------------------------------------------------------------------------
        // Cookie Management Methods
        // --------------------------------------------------------------------------------------------------
        this.getInitialCatalogVersion = function(catalogVersions) {
            return storageService
                .getValueFromLocalStorage(SELECTED_CATALOG_VERSION_COOKIE_NAME)
                .then(function(rawValue) {
                    var selectedCatalogVersionId = typeof rawValue === 'string' ? rawValue : null;

                    var selectedCatalogVersion = catalogVersions.filter(
                        function(catalogVersion) {
                            return catalogVersion.id === selectedCatalogVersionId;
                        }.bind(this)
                    )[0];

                    return selectedCatalogVersion
                        ? selectedCatalogVersion
                        : lodash.last(catalogVersions);
                });
        };

        this.persistCatalogVersion = function(catalogVersionId) {
            storageService._setValueInLocalStorage(
                SELECTED_CATALOG_VERSION_COOKIE_NAME,
                catalogVersionId
            );
        };
    });
