/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import * as lo from 'lodash';
import { BehaviorSubject } from 'rxjs';
import { filter, mergeMap, take } from 'rxjs/operators';

import {
    CrossFrameEventService,
    EVENT_SERVICE,
    EVENTS,
    GatewayProxied,
    IBaseCatalog,
    IBaseCatalogVersion,
    ICatalogService,
    ICatalogVersion,
    IDefaultExperienceParams,
    IExperience,
    IExperienceCatalogDescriptor,
    IExperienceCatalogVersion,
    IExperienceParams,
    IExperienceService,
    ILanguage,
    IPreviewData,
    IPreviewService,
    ISharedDataService,
    ISite,
    IStorage,
    IStorageManager,
    IStoragePropertiesService,
    LanguageService,
    LogService,
    LANDING_PAGE_PATH,
    LEGACY_LOCATION,
    LEGACY_ROUTE,
    Payload,
    SeDowngradeService,
    STORE_FRONT_CONTEXT
} from 'smarteditcommons';
import { SiteService } from './SiteService';
import { IframeManagerService } from './iframe/IframeManagerService';

/** @internal */
@SeDowngradeService(IExperienceService)
@GatewayProxied(
    'loadExperience',
    'updateExperiencePageContext',
    'getCurrentExperience',
    'hasCatalogVersionChanged',
    'buildRefreshedPreviewUrl',
    'compareWithCurrentExperience'
)
export class ExperienceService extends IExperienceService {
    static EXPERIENCE_STORAGE_KEY = 'experience';

    private previousExperience: IExperience;
    private experienceStorage: IStorage<string, IExperience>;
    private storageLoaded$: BehaviorSubject<boolean> = new BehaviorSubject(false);

    constructor(
        public seStorageManager: IStorageManager,
        public storagePropertiesService: IStoragePropertiesService,
        private logService: LogService,
        @Inject(EVENT_SERVICE) private crossFrameEventService: CrossFrameEventService,
        private siteService: SiteService,
        private previewService: IPreviewService,
        private catalogService: ICatalogService,
        private languageService: LanguageService,
        private sharedDataService: ISharedDataService,
        private iframeManagerService: IframeManagerService,
        @Inject(LEGACY_LOCATION) private $location: angular.ILocationService,
        @Inject(LEGACY_ROUTE) private $route: angular.route.IRouteService
    ) {
        super();
        seStorageManager
            .getStorage({
                storageId: ExperienceService.EXPERIENCE_STORAGE_KEY,
                storageType: storagePropertiesService.getProperty('STORAGE_TYPE_SESSION_STORAGE')
            })
            .then((_storage: IStorage<string, IExperience>) => {
                this.experienceStorage = _storage;
                this.storageLoaded$.next(true);
            });
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:ExperienceService#buildAndSetExperience
     * @methodOf smarteditServicesModule.service:ExperienceService
     *
     * @description
     * Given an object containing a siteId, catalogId, catalogVersion and catalogVersions (array of product catalog version uuid's), will return a reconstructed experience
     *
     * @param {IExperienceParams} params
     * @returns {Promise<IExperience>} an experience
     */
    buildAndSetExperience(params: IExperienceParams): Promise<IExperience> {
        const siteId = params.siteId;
        const catalogId = params.catalogId;
        const catalogVersion = params.catalogVersion;
        const productCatalogVersions = params.productCatalogVersions;

        return Promise.all<ISite, IBaseCatalog[], IBaseCatalog[], ILanguage[]>([
            this.siteService.getSiteById(siteId),
            this.catalogService.getContentCatalogsForSite(siteId),
            this.catalogService.getProductCatalogsForSite(siteId),
            this.languageService.getLanguagesForSite(siteId)
        ]).then(([siteDescriptor, catalogs, productCatalogs, languages]) => {
            const currentCatalog: IBaseCatalog = catalogs.find(
                (catalog) => catalog.catalogId === catalogId
            );
            const currentCatalogVersion: IBaseCatalogVersion = currentCatalog
                ? currentCatalog.versions.find(
                      (result: IBaseCatalogVersion) => result.version === catalogVersion
                  )
                : null;

            if (!currentCatalogVersion) {
                return Promise.reject(
                    `no catalogVersionDescriptor found for ${catalogId} catalogId and ${catalogVersion} catalogVersion`
                );
            }

            const currentExperienceProductCatalogVersions: IExperienceCatalogVersion[] = [];

            productCatalogs.forEach((productCatalog: IBaseCatalog) => {
                // for each product catalog either choose the version already present in the params or choose the active version.
                const currentProductCatalogVersion: IBaseCatalogVersion = productCatalog.versions.find(
                    (version: IBaseCatalogVersion) => {
                        return productCatalogVersions
                            ? productCatalogVersions.indexOf(version.uuid) > -1
                            : version.active === true;
                    }
                );
                currentExperienceProductCatalogVersions.push({
                    catalog: productCatalog.catalogId,
                    catalogName: productCatalog.name,
                    catalogVersion: currentProductCatalogVersion.version,
                    active: currentProductCatalogVersion.active,
                    uuid: currentProductCatalogVersion.uuid
                });
            });

            const languageDescriptor: ILanguage = params.language
                ? languages.find((lang: ILanguage) => lang.isocode === params.language)
                : languages[0];

            const defaultExperience: any = lo.cloneDeep(params);

            delete defaultExperience.siteId;
            delete defaultExperience.catalogId;
            delete defaultExperience.catalogVersion;

            defaultExperience.siteDescriptor = siteDescriptor;
            defaultExperience.catalogDescriptor = {
                catalogId,
                catalogVersion: currentCatalogVersion.version,
                catalogVersionUuid: currentCatalogVersion.uuid,
                name: currentCatalog.name,
                siteId,
                active: currentCatalogVersion.active
            } as IExperienceCatalogDescriptor;
            defaultExperience.languageDescriptor = languageDescriptor;
            defaultExperience.time = defaultExperience.time || null;

            defaultExperience.productCatalogVersions = currentExperienceProductCatalogVersions;

            return this.setCurrentExperience(defaultExperience);
        });
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:ExperienceService#updateExperiencePageId
     * @methodOf smarteditServicesModule.service:ExperienceService
     *
     * @description
     * Used to update the page ID stored in the current experience and reloads the page to make the changes visible.
     *
     * @param {String} newPageID the ID of the page that must be stored in the current experience.
     *
     */
    updateExperiencePageId(newPageID: string) {
        return this.getCurrentExperience().then((currentExperience: IExperience) => {
            if (!currentExperience) {
                // Experience haven't been set. Thus, the experience hasn't been loaded.
                // No need to update the experience then.
                return null;
            }

            currentExperience.pageId = newPageID;
            this.setCurrentExperience(currentExperience);
            this.reloadPage();
        });
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:ExperienceService#loadExperience
     * @methodOf smarteditServicesModule.service:ExperienceService
     *
     * @description
     * Used to update the experience with the parameters provided and reloads the page to make the changes visible.
     *
     * @param {IDefaultExperienceParams} params The object containing the paratements for the experience to be loaded.
     * @param {String} params.siteId the ID of the site that must be stored in the current experience.
     * @param {String} params.catalogId the ID of the catalog that must be stored in the current experience.
     * @param {String} params.catalogVersion the version of the catalog that must be stored in the current experience.
     * @param {? String} params.pageId the ID of the page that must be stored in the current experience.
     *
     */
    loadExperience(params: IDefaultExperienceParams): Promise<angular.ILocationService | void> {
        return this.buildAndSetExperience(params).then(() => {
            return this.reloadPage();
        });
    }

    reloadPage(): void {
        if (this.$location.path() === STORE_FRONT_CONTEXT) {
            this.$route.reload();
        } else {
            this.$location.path(STORE_FRONT_CONTEXT).replace();
        }
    }

    updateExperiencePageContext(
        pageCatalogVersionUuid: string,
        pageId: string
    ): Promise<IExperience> {
        return this.getCurrentExperience()
            .then((currentExperience: IExperience) => {
                return this.catalogService
                    .getContentCatalogsForSite(currentExperience.catalogDescriptor.siteId)
                    .then((catalogs: IBaseCatalog[]) => {
                        if (!currentExperience) {
                            // Experience haven't been set. Thus, the experience hasn't been loaded. No need to update the
                            // experience then.
                            return null;
                        }

                        const pageCatalogVersion: ICatalogVersion = lo
                            .flatten(
                                catalogs.map((catalog: IBaseCatalog) => {
                                    return lo
                                        .cloneDeep(catalog.versions)
                                        .map((version: ICatalogVersion) => {
                                            version.catalogName = catalog.name;
                                            version.catalogId = catalog.catalogId;
                                            return version;
                                        });
                                })
                            )
                            .filter(
                                (version: ICatalogVersion) =>
                                    version.uuid === pageCatalogVersionUuid
                            )[0];

                        return this.catalogService
                            .getCurrentSiteID()
                            .then((siteID: string) => {
                                currentExperience.pageId = pageId;
                                currentExperience.pageContext = {
                                    catalogId: pageCatalogVersion.catalogId,
                                    catalogName: pageCatalogVersion.catalogName,
                                    catalogVersion: pageCatalogVersion.version,
                                    catalogVersionUuid: pageCatalogVersion.uuid,
                                    siteId: siteID,
                                    active: pageCatalogVersion.active
                                };

                                return this.setCurrentExperience(currentExperience);
                            });
                    });
            })
            .then((experience: IExperience) => {
                this.crossFrameEventService.publish(EVENTS.PAGE_CHANGE, experience);
                return experience;
            });
    }

    getCurrentExperience(): Promise<IExperience> {
        // After Angular porting of StorageModule the experienceStorage load promise seems to be resolved after execution of getCurrentExperience.
        // To avoid errors the method is triggered once experienceStorage is present.

        return this.storageLoaded$
            .pipe(
                filter((value) => value),
                take(1),
                mergeMap(() => this.experienceStorage.get(ExperienceService.EXPERIENCE_STORAGE_KEY))
            )
            .toPromise();
    }

    setCurrentExperience(experience: IExperience): Promise<IExperience> {
        return this.getCurrentExperience().then((previousExperience: IExperience) => {
            this.previousExperience = previousExperience;

            return this.experienceStorage
                .put(experience, ExperienceService.EXPERIENCE_STORAGE_KEY)
                .then(() => {
                    this.sharedDataService.set(
                        ExperienceService.EXPERIENCE_STORAGE_KEY,
                        experience
                    );
                    return Promise.resolve(experience);
                });
        });
    }

    hasCatalogVersionChanged(): Promise<boolean> {
        return this.getCurrentExperience().then((currentExperience: IExperience) => {
            return (
                this.previousExperience === undefined ||
                currentExperience.catalogDescriptor.catalogId !==
                    this.previousExperience.catalogDescriptor.catalogId ||
                currentExperience.catalogDescriptor.catalogVersion !==
                    this.previousExperience.catalogDescriptor.catalogVersion
            );
        });
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:ExperienceService#initializeExperience
     * @methodOf smarteditServicesModule.service:ExperienceService
     *
     * @description
     * If an experience is set in the shared data service, this method will load the preview for this experience (such as Catalog, language, date and time).
     * Otherwise, the user will be redirected to the landing page to select an experience.
     * To load a preview, we need to get a preview ticket from an API.
     * Here we set current location to null initially so that the iframe manager loads the provided url and set the location.
     *
     * @returns {Promise<IExperience>} a promise returning the experience
     */
    initializeExperience(): Promise<IExperience> {
        this.iframeManagerService.setCurrentLocation(null);
        return this.getCurrentExperience().then(
            (experience: IExperience) => {
                if (!experience) {
                    this.$location.url(LANDING_PAGE_PATH);
                    return null;
                }
                return this.updateExperience();
            },
            (err: any) => {
                this.logService.error(
                    'ExperienceService.initializeExperience() - failed to retrieve experience'
                );
                return Promise.reject(err);
            }
        );
    }

    updateExperience(newExperience?: Payload): Promise<IExperience> {
        return this.getCurrentExperience().then(
            (experience: IExperience) => {
                // create a deep copy of the current experience
                experience = lo.cloneDeep(experience);

                // merge the new experience into the copy of the current experience
                lo.merge(experience, newExperience);

                return this.previewService
                    .getResourcePathFromPreviewUrl(experience.siteDescriptor.previewUrl)
                    .then(
                        (resourcePath: string) => {
                            const previewData: IPreviewData = this._convertExperienceToPreviewData(
                                experience,
                                resourcePath
                            );

                            return this.previewService.createPreview(previewData).then(
                                (previewResponse: IPreviewData) => {
                                    /* forbiddenNameSpaces window._:false */
                                    window.__smartedit__.smartEditBootstrapped = {};
                                    this.iframeManagerService.loadPreview(
                                        previewResponse.resourcePath,
                                        previewResponse.ticketId
                                    );
                                    return this.setCurrentExperience(experience);
                                },
                                (err: any) => {
                                    this.logService.error(
                                        'iframeManagerService.updateExperience() - failed to update experience'
                                    );
                                    return Promise.reject(err);
                                }
                            );
                        },
                        (err: any) => {
                            this.logService.error(
                                'ExperienceService.updateExperience() - failed to retrieve resource path'
                            );
                            return Promise.reject(err);
                        }
                    );
            },
            (err: any) => {
                this.logService.error(
                    'ExperienceService.updateExperience() - failed to retrieve current experience'
                );
                return Promise.reject(err);
            }
        );
    }

    compareWithCurrentExperience(experience: IDefaultExperienceParams): Promise<boolean> {
        if (!experience) {
            return Promise.resolve(false);
        }

        return this.getCurrentExperience().then((currentExperience: IExperience) => {
            if (!currentExperience) {
                return Promise.resolve(false);
            }

            if (
                currentExperience.pageId === experience.pageId &&
                currentExperience.siteDescriptor.uid === experience.siteId &&
                currentExperience.catalogDescriptor.catalogId === experience.catalogId &&
                currentExperience.catalogDescriptor.catalogVersion === experience.catalogVersion
            ) {
                return Promise.resolve(true);
            }
            return Promise.resolve(false);
        });
    }
}
