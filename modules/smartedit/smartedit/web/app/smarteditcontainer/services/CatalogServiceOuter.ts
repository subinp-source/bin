/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import {
    catalogEvictionTag,
    rarelyChangingContent,
    Cached,
    ContentCatalogRestService,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    CONTEXT_SITE_ID,
    GatewayProxied,
    IBaseCatalog,
    IBaseCatalogs,
    IBaseCatalogVersion,
    ICatalog,
    ICatalogService,
    ICatalogVersion,
    ISharedDataService,
    ISite,
    IStorageService,
    IUriContext,
    IUrlService,
    LogService,
    ProductCatalogRestService,
    SeDowngradeService
} from 'smarteditcommons';
import { SiteService } from 'smarteditcontainer/services';

/** @internal */
@SeDowngradeService(ICatalogService)
@GatewayProxied()
export class CatalogService extends ICatalogService {
    private SELECTED_SITE_COOKIE_NAME = 'seselectedsite';
    constructor(
        private logService: LogService,
        private sharedDataService: ISharedDataService,
        private siteService: SiteService,
        private urlService: IUrlService,
        private contentCatalogRestService: ContentCatalogRestService,
        private storageService: IStorageService,
        private productCatalogRestService: ProductCatalogRestService
    ) {
        super();
    }

    getContentCatalogsForSite(siteUID: string): Promise<IBaseCatalog[]> {
        return this.contentCatalogRestService
            .get({
                siteUID
            })
            .then((catalogs: IBaseCatalogs) => {
                return catalogs.catalogs;
            });
    }

    getCatalogByVersion(siteUID: string, catalogVersionName: string): Promise<IBaseCatalog[]> {
        return this.getContentCatalogsForSite(siteUID).then((catalogs: IBaseCatalog[]) => {
            return catalogs.filter((catalog) => {
                return catalog.versions.some((currentCatalogVersion: IBaseCatalogVersion) => {
                    return currentCatalogVersion.version === catalogVersionName;
                });
            });
        });
    }

    isContentCatalogVersionNonActive(_uriContext?: IUriContext): Promise<boolean> {
        return this._getContext(_uriContext).then((uriContext: IUriContext) => {
            return this.getContentCatalogsForSite(uriContext[CONTEXT_SITE_ID]).then((catalogs) => {
                const currentCatalog = catalogs.find((catalog) => {
                    return catalog.catalogId === uriContext[CONTEXT_CATALOG];
                });
                const currentCatalogVersion = currentCatalog
                    ? currentCatalog.versions.find((catalogVersion) => {
                          return catalogVersion.version === uriContext[CONTEXT_CATALOG_VERSION];
                      })
                    : null;

                if (!currentCatalogVersion) {
                    throw new Error(
                        `Invalid uriContext ${uriContext}, cannot find catalog version.`
                    );
                }

                return !currentCatalogVersion.active;
            });
        });
    }

    getContentCatalogActiveVersion(_uriContext?: IUriContext): Promise<string> {
        return this._getContext(_uriContext).then((uriContext: IUriContext) => {
            return this.getContentCatalogsForSite(uriContext[CONTEXT_SITE_ID]).then((catalogs) => {
                const currentCatalog = catalogs.find((catalog) => {
                    return catalog.catalogId === uriContext[CONTEXT_CATALOG];
                });

                const activeCatalogVersion = currentCatalog
                    ? currentCatalog.versions.find((catalogVersion) => {
                          return catalogVersion.active;
                      })
                    : null;

                if (!activeCatalogVersion) {
                    throw new Error(
                        `Invalid uriContext ${uriContext}, cannot find catalog version.`
                    );
                }

                return activeCatalogVersion.version;
            });
        });
    }

    getActiveContentCatalogVersionByCatalogId(contentCatalogId: string): Promise<string> {
        return this._getContext().then((uriContext: IUriContext) => {
            return this.getContentCatalogsForSite(uriContext[CONTEXT_SITE_ID]).then((catalogs) => {
                const currentCatalog = catalogs.find((catalog) => {
                    return catalog.catalogId === contentCatalogId;
                });

                const currentCatalogVersion = currentCatalog
                    ? currentCatalog.versions.find((catalogVersion) => {
                          return catalogVersion.active;
                      })
                    : null;

                if (!currentCatalogVersion) {
                    throw new Error(
                        `Invalid content catalog ${contentCatalogId}, cannot find any active catalog version.`
                    );
                }

                return currentCatalogVersion.version;
            });
        });
    }

    getContentCatalogVersion(_uriContext?: IUriContext): Promise<IBaseCatalogVersion> {
        return this._getContext(_uriContext).then((uriContext: IUriContext) => {
            return this.getContentCatalogsForSite(uriContext[CONTEXT_SITE_ID]).then((catalogs) => {
                const catalog = catalogs.find((c) => {
                    return c.catalogId === uriContext[CONTEXT_CATALOG];
                });
                if (!catalog) {
                    throw new Error(
                        'no catalog ' +
                            uriContext[CONTEXT_CATALOG] +
                            ' found for site ' +
                            uriContext[CONTEXT_SITE_ID]
                    );
                }
                const catalogVersion = catalog.versions.find((version) => {
                    return version.version === uriContext[CONTEXT_CATALOG_VERSION];
                });
                if (!catalogVersion) {
                    throw new Error(
                        `no catalogVersion ${uriContext[CONTEXT_CATALOG_VERSION]} for catalog ${
                            uriContext[CONTEXT_CATALOG]
                        } and site ${uriContext[CONTEXT_SITE_ID]}`
                    );
                }
                catalogVersion.catalogName = catalog.name;
                catalogVersion.catalogId = catalog.catalogId;
                return catalogVersion;
            });
        });
    }

    getCurrentSiteID(): Promise<string> {
		return this.storageService.getValueFromLocalStorage(this.SELECTED_SITE_COOKIE_NAME, false);
	}

    /**
     * Finds the ID of the default site configured for the provided content catalog.
     * @param contentCatalogId The UID of content catalog for which to retrieve its default site ID.
     * @returns The ID of the default site found.
     */
    getDefaultSiteForContentCatalog(contentCatalogId: string): Promise<ISite> {
        return ((this.siteService.getSites() as unknown) as Promise<ISite[]>).then((sites) => {
            const defaultSitesForCatalog = sites.filter((site) => {
                // ContentCatalogs in the site object are sorted. The last one is considered
                // the default one for a given site.
                const siteDefaultContentCatalog = lodash.last(site.contentCatalogs);
                return siteDefaultContentCatalog && siteDefaultContentCatalog === contentCatalogId;
            });

            if (defaultSitesForCatalog.length === 0) {
                this.logService.warn(
                    `[catalogService] - No default site found for content catalog ${contentCatalogId}`
                );
            } else if (defaultSitesForCatalog.length > 1) {
                this.logService.warn(
                    `[catalogService] - Many default sites found for content catalog ${contentCatalogId}`
                );
            }

            return defaultSitesForCatalog[0];
        });
    }

    getCatalogVersionByUuid(catalogVersionUuid: string, siteId?: string): Promise<ICatalogVersion> {
        return this.getAllContentCatalogsGroupedById().then(
            (contentCatalogsGrouped: ICatalog[][]) => {
                const catalogs = lodash.reduce(
                    contentCatalogsGrouped,
                    (allCatalogs, siteCatalogs) => {
                        return allCatalogs.concat(siteCatalogs);
                    },
                    []
                );

                const catalogVersionFound = lodash
                    .flatten(
                        catalogs.map((catalog) => {
                            return lodash
                                .cloneDeep(catalog.versions)
                                .map((version: ICatalogVersion) => {
                                    version.catalogName = catalog.name;
                                    version.catalogId = catalog.catalogId;
                                    return version;
                                });
                        })
                    )
                    .filter((version) => {
                        return (
                            catalogVersionUuid === version.uuid &&
                            (!siteId || siteId === version.siteDescriptor.uid)
                        );
                    })[0];

                if (!catalogVersionFound) {
                    const errorMessage =
                        'Cannot find catalog version with UUID ' +
                        catalogVersionUuid +
                        (siteId ? ' in site ' + siteId : '');
                    throw new Error(errorMessage);
                }

                return this.getCurrentSiteID().then(
                    (defaultSiteID) => {
                        catalogVersionFound.siteId = defaultSiteID;
                        return catalogVersionFound;
                    }
                );
            }
        );
    }

    getAllContentCatalogsGroupedById(): Promise<ICatalog[][]> {
        return ((this.siteService.getSites() as unknown) as Promise<ISite[]>).then(
            (sites: ISite[]) => {
                const promisesToResolve = sites.map((site: ISite) => {
                    return this.getContentCatalogsForSite(site.uid).then((catalogs: ICatalog[]) => {
                        catalogs.forEach((catalog) => {
                            catalog.versions = catalog.versions.map(
                                (catalogVersion: ICatalogVersion) => {
                                    catalogVersion.siteDescriptor = site;
                                    return catalogVersion;
                                }
                            );
                        });

                        return catalogs;
                    });
                });

                return Promise.all(promisesToResolve);
            }
        );
    }

    getProductCatalogsBySiteKey(siteUIDKey: string): Promise<IBaseCatalog[]> {
        return this._getContext().then((uriContext: IUriContext) =>
            this.getProductCatalogsForSite(uriContext[siteUIDKey])
        );
    }

    /* =====================================================================================================================
      * `getProductCatalogsBySite` is to get product catalogs by site value
      * `siteUIDValue` - is the site value rather than site key
      * if you want to get product catalogs by site key, please refer to function `getProductCatalogsBySiteKey`
       =====================================================================================================================
    */
    @Cached({ actions: [rarelyChangingContent], tags: [catalogEvictionTag] })
    getProductCatalogsForSite(siteUIDValue: string): Promise<IBaseCatalog[]> {
        return this.productCatalogRestService
            .get({
                siteUID: siteUIDValue
            })
            .then((catalogs: IBaseCatalogs) => {
                return catalogs.catalogs;
            });
    }

    getActiveProductCatalogVersionByCatalogId(productCatalogId: string): Promise<string> {
        return this.getProductCatalogsBySiteKey(CONTEXT_SITE_ID).then((catalogs) => {
            const currentCatalog = catalogs.find((catalog) => {
                return catalog.catalogId === productCatalogId;
            });

            const currentCatalogVersion = currentCatalog
                ? currentCatalog.versions.find((catalogVersion) => {
                      return catalogVersion.active;
                  })
                : null;

            if (!currentCatalogVersion) {
                throw new Error(
                    `Invalid product catalog ${productCatalogId}, cannot find any active catalog version.`
                );
            }

            return currentCatalogVersion.version;
        });
    }

    // =====================================================================================================================
    //  Helper Methods
    // =====================================================================================================================

    getCatalogVersionUUid(_uriContext?: IUriContext): Promise<string> {
        return this.getContentCatalogVersion(_uriContext).then((catalogVersion) => {
            return catalogVersion.uuid;
        });
    }

    retrieveUriContext(_uriContext?: IUriContext): Promise<IUriContext> {
        return this._getContext(_uriContext);
    }

    returnActiveCatalogVersionUIDs(catalogs: ICatalog[]): string[] {
        return catalogs.reduce((accumulator, catalog) => {
            accumulator.push(
                catalog.versions.find((version) => {
                    return version.active;
                }).uuid
            );
            return accumulator;
        }, []);
    }

    private _getContext(_uriContext?: IUriContext): Promise<IUriContext> {
        // TODO: once refactored by Nick, use definition of experience
        return _uriContext
            ? Promise.resolve(_uriContext)
            : this.sharedDataService.get('experience').then((experience: any) => {
                  if (!experience) {
                      throw new Error(
                          'catalogService was not provided with a uriContext and could not retrive an experience from sharedDataService'
                      );
                  }
                  return this.urlService.buildUriContext(
                      experience.siteDescriptor.uid,
                      experience.catalogDescriptor.catalogId,
                      experience.catalogDescriptor.catalogVersion
                  );
              });
    }
}
