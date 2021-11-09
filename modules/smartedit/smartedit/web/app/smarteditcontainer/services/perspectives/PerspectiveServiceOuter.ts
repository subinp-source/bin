/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import {
    objectUtils,
    stringUtils,
    ALL_PERSPECTIVE,
    CrossFrameEventService,
    EVENT_PERSPECTIVE_ADDED,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_REFRESHED,
    EVENT_PERSPECTIVE_UNLOADING,
    EVENT_PERSPECTIVE_UPDATED,
    EVENTS,
    GatewayProxied,
    IFeatureService,
    IPermissionService,
    IPerspective,
    IPerspectiveService,
    IStorageService,
    IWaitDialogService,
    LogService,
    LEGACY_LOCATION,
    NONE_PERSPECTIVE,
    PERSPECTIVE_SELECTOR_WIDGET_KEY,
    SeDowngradeService,
    SystemEventService,
    STORE_FRONT_CONTEXT
} from 'smarteditcommons';
import { UpgradeModule } from '@angular/upgrade/static';
import { Inject } from '@angular/core';

interface IPerspectiveData {
    activePerspective: IPerspective;
    previousPerspective: IPerspective;
    previousSwitchToArg: string;
}

/** @internal */
@SeDowngradeService(IPerspectiveService)
@GatewayProxied(
    'register',
    'switchTo',
    'hasActivePerspective',
    'isEmptyPerspectiveActive',
    'selectDefault',
    'refreshPerspective',
    'getActivePerspectiveKey',
    'isHotkeyEnabledForActivePerspective'
)
export class PerspectiveService extends IPerspectiveService {
    private readonly PERSPECTIVE_COOKIE_NAME: string = 'smartedit-perspectives';
    private readonly INITIAL_SWITCHTO_ARG = 'INITIAL_SWITCHTO_ARG';

    private data: IPerspectiveData = {
        activePerspective: undefined,
        previousPerspective: undefined,
        previousSwitchToArg: this.INITIAL_SWITCHTO_ARG
    };

    private immutablePerspectives: IPerspective[] = []; // once a perspective is registered it will always exists in this variable
    private perspectives: IPerspective[] = [];
    private NON_PERSPECTIVE_OBJECT: IPerspective;

    constructor(
        private upgrade: UpgradeModule,
        @Inject(LEGACY_LOCATION) private $location: angular.ILocationService,
        private logService: LogService,
        private systemEventService: SystemEventService,
        private featureService: IFeatureService,
        private waitDialogService: IWaitDialogService,
        private storageService: IStorageService,
        private crossFrameEventService: CrossFrameEventService,
        private permissionService: IPermissionService
    ) {
        super();
        this._addDefaultPerspectives();
        this._registerEventHandlers();

        this.NON_PERSPECTIVE_OBJECT = { key: NONE_PERSPECTIVE, nameI18nKey: '', features: [] };
    }

    register(configuration: IPerspective): Promise<void> {
        this._validate(configuration);

        let perspective: IPerspective = this._findByKey(configuration.key);

        if (!perspective) {
            this._addPerspectiveSelectorWidget(configuration);
            perspective = configuration;
            perspective.isHotkeyDisabled = !!configuration.isHotkeyDisabled;
            this.immutablePerspectives.push(perspective);
            this.perspectives.push(perspective);
            this.systemEventService.publishAsync(EVENT_PERSPECTIVE_ADDED);
        } else {
            perspective.features = objectUtils.uniqueArray(
                perspective.features || [],
                configuration.features || []
            );
            perspective.perspectives = objectUtils.uniqueArray(
                perspective.perspectives || [],
                configuration.perspectives || []
            );
            perspective.permissions = objectUtils.uniqueArray(
                perspective.permissions || [],
                configuration.permissions || []
            );

            this.systemEventService.publishAsync(EVENT_PERSPECTIVE_UPDATED);
        }

        return Promise.resolve();
    }

    // Filters immutablePerspectives to determine which perspectives are available, taking into account security
    getPerspectives(): Promise<IPerspective[]> {
        const promises: Promise<boolean>[] = [];
        this.immutablePerspectives.forEach((perspective: IPerspective) => {
            let promise: Promise<boolean>;

            if (perspective.permissions && perspective.permissions.length > 0) {
                promise = this.permissionService.isPermitted([
                    {
                        names: perspective.permissions
                    }
                ]);
            } else {
                promise = Promise.resolve(true);
            }
            promises.push(promise);
        });

        return Promise.all(promises).then((results: boolean[]) => {
            return this.immutablePerspectives.filter(
                (perspective: IPerspective, index: number) => results[index]
            );
        });
    }

    hasActivePerspective(): Promise<boolean> {
        return Promise.resolve(Boolean(this.data.activePerspective));
    }

    getActivePerspective(): IPerspective {
        return this.data.activePerspective
            ? { ...this._findByKey(this.data.activePerspective.key) }
            : null;
    }

    isEmptyPerspectiveActive(): Promise<boolean> {
        return Promise.resolve(
            !!this.data.activePerspective && this.data.activePerspective.key === NONE_PERSPECTIVE
        );
    }

    switchTo(key: string): Promise<void> {
        if (!this._changeActivePerspective(key)) {
            this.waitDialogService.hideWaitModal();
            return Promise.resolve();
        }

        this._handleUnloadEvent(key);

        this.waitDialogService.showWaitModal();
        const featuresFromPreviousPerspective: string[] = [];
        if (this.data.previousPerspective) {
            this._fetchAllFeatures(this.data.previousPerspective, featuresFromPreviousPerspective);
        }
        const featuresFromNewPerspective: string[] = [];
        this._fetchAllFeatures(this.data.activePerspective, featuresFromNewPerspective);

        // deactivating any active feature not belonging to either the perspective or one of its nested pespectives
        featuresFromPreviousPerspective
            .filter((featureKey: string) => {
                return !featuresFromNewPerspective.some((f: string) => {
                    return featureKey === f;
                });
            })
            .forEach((featureKey: string) => {
                this.featureService.disable(featureKey);
            });

        // activating any feature belonging to either the perspective or one of its nested pespectives
        const permissionPromises: Promise<void>[] = [];
        featuresFromNewPerspective
            .filter((feature: string) => {
                return !featuresFromPreviousPerspective.some((f: string) => {
                    return feature === f;
                });
            })
            .forEach((featureKey: string) => {
                permissionPromises.push(this._enableOrDisableFeature(featureKey));
            });

        return Promise.all(permissionPromises).then(() => {
            if (this.data.activePerspective.key === NONE_PERSPECTIVE) {
                this.waitDialogService.hideWaitModal();
            }
            this.crossFrameEventService.publish(
                EVENT_PERSPECTIVE_CHANGED,
                this.data.activePerspective.key !== NONE_PERSPECTIVE
            );
        });
    }

    selectDefault(): Promise<void> {
        return this.getPerspectives().then((perspectives: IPerspective[]) => {
            return this.storageService
                .getValueFromLocalStorage(this.PERSPECTIVE_COOKIE_NAME, true)
                .then((cookieValue: string) => {
                    //  restricted by permission
                    const perspectiveAvailable = perspectives.find(
                        (p: IPerspective) => p.key === cookieValue
                    );
                    let defaultPerspective: IPerspective;
                    let perspective: IPerspective;
                    if (!perspectiveAvailable) {
                        this.logService.warn(
                            'Cannot select mode "' +
                                cookieValue +
                                '" It might not exist or is restricted.'
                        );
                        // from full list of pespectives, regardless of permissions
                        const perspectiveFromCookie = this._findByKey(cookieValue);
                        if (!!perspectiveFromCookie) {
                            this._disableAllFeaturesForPerspective(perspectiveFromCookie);
                        }
                        defaultPerspective = this.NON_PERSPECTIVE_OBJECT;
                        perspective = this.NON_PERSPECTIVE_OBJECT;
                    } else {
                        const perspectiveFromCookie = this._findByKey(cookieValue);

                        defaultPerspective = perspectiveFromCookie
                            ? perspectiveFromCookie
                            : this.NON_PERSPECTIVE_OBJECT;
                        perspective = this.data.previousPerspective
                            ? this.data.previousPerspective
                            : defaultPerspective;
                    }
                    if (defaultPerspective.key !== this.NON_PERSPECTIVE_OBJECT.key) {
                        this._disableAllFeaturesForPerspective(defaultPerspective);
                    }
                    return this.switchTo(perspective.key);
                });
        });
    }

    refreshPerspective(): Promise<void> {
        return this.getPerspectives().then((result: IPerspective[]) => {
            const activePerspective: IPerspective = this.getActivePerspective();
            if (!activePerspective) {
                return this.selectDefault();
            } else {
                this.perspectives = result;
                if (!this._findByKey(activePerspective.key)) {
                    return this.switchTo(NONE_PERSPECTIVE);
                } else {
                    const features: string[] = [];
                    const permissionPromises: Promise<void>[] = [];

                    this._fetchAllFeatures(activePerspective, features);
                    features.forEach((featureKey: string) => {
                        permissionPromises.push(this._enableOrDisableFeature(featureKey));
                    });

                    return Promise.all(permissionPromises).then(() => {
                        this.waitDialogService.hideWaitModal();
                        this.crossFrameEventService.publish(
                            EVENT_PERSPECTIVE_REFRESHED,
                            activePerspective.key !== NONE_PERSPECTIVE
                        );
                    });
                }
            }
        });
    }

    getActivePerspectiveKey(): Promise<string> {
        const activePerspective: IPerspective = this.getActivePerspective();
        return Promise.resolve(activePerspective ? activePerspective.key : null);
    }

    isHotkeyEnabledForActivePerspective(): Promise<boolean> {
        const activePerspective: IPerspective = this.getActivePerspective();
        return Promise.resolve(activePerspective && !activePerspective.isHotkeyDisabled);
    }

    private _addPerspectiveSelectorWidget(configuration: IPerspective) {
        configuration.features = configuration.features || [];
        if (configuration.features.indexOf(PERSPECTIVE_SELECTOR_WIDGET_KEY) === -1) {
            configuration.features.unshift(PERSPECTIVE_SELECTOR_WIDGET_KEY);
        }
    }

    private _addDefaultPerspectives() {
        this.register({
            key: NONE_PERSPECTIVE,
            nameI18nKey: 'se.perspective.none.name',
            isHotkeyDisabled: true,
            descriptionI18nKey: 'se.perspective.none.description.disabled'
        } as IPerspective);

        this.register({
            key: ALL_PERSPECTIVE,
            nameI18nKey: 'se.perspective.all.name',
            descriptionI18nKey: 'se.perspective.all.description'
        } as IPerspective);
    }

    private _registerEventHandlers() {
        this.systemEventService.subscribe(EVENTS.LOGOUT, this._clearPerspectiveFeatures.bind(this));
        this.crossFrameEventService.subscribe(
            EVENTS.USER_HAS_CHANGED,
            this._clearPerspectiveFeatures.bind(this)
        );
        // clear the features when navigating to another page than the storefront. this is preventing a flickering of toolbar icons when goign back to storefront on another page.
        this.$rootScope.$on('$routeChangeSuccess', () => {
            if (this.$location.path() !== STORE_FRONT_CONTEXT) {
                this._clearPerspectiveFeatures();
            }
        });
    }

    private _validate(configuration: IPerspective) {
        if (stringUtils.isBlank(configuration.key)) {
            throw new Error('perspectiveService.configuration.key.error.required');
        }
        if (stringUtils.isBlank(configuration.nameI18nKey)) {
            throw new Error('perspectiveService.configuration.nameI18nKey.error.required');
        }
        if (
            [NONE_PERSPECTIVE, ALL_PERSPECTIVE].indexOf(configuration.key) === -1 &&
            (stringUtils.isBlank(configuration.features) || configuration.features.length === 0)
        ) {
            throw new Error('perspectiveService.configuration.features.error.required');
        }
    }

    private _findByKey(key: string): IPerspective {
        return this.perspectives.find((perspective: IPerspective) => perspective.key === key);
    }

    private _fetchAllFeatures(perspective: IPerspective, holder: string[]) {
        if (!holder) {
            holder = [];
        }

        if (perspective.key === ALL_PERSPECTIVE) {
            objectUtils.uniqueArray(holder, this.featureService.getFeatureKeys() || []);
        } else {
            objectUtils.uniqueArray(holder, perspective.features || []);

            (perspective.perspectives || []).forEach((perspectiveKey: string) => {
                const nestedPerspective = this._findByKey(perspectiveKey);
                if (nestedPerspective) {
                    this._fetchAllFeatures(nestedPerspective, holder);
                } else {
                    this.logService.debug(
                        'nested perspective ' + perspectiveKey + ' was not found in the registry'
                    );
                }
            });
        }
    }

    private _enableOrDisableFeature(featureKey: string): Promise<void> {
        return this.featureService
            .getFeatureProperty(featureKey, 'permissions')
            .then((permissionNames: string[]) => {
                if (!Array.isArray(permissionNames)) {
                    permissionNames = [];
                }
                return this.permissionService
                    .isPermitted([
                        {
                            names: permissionNames
                        }
                    ])
                    .then((allowCallback: boolean) => {
                        if (allowCallback) {
                            this.featureService.enable(featureKey);
                        } else {
                            this.featureService.disable(featureKey);
                        }
                    });
            });
    }

    /**
     * Takes care of sending EVENT_PERSPECTIVE_UNLOADING when perspectives change.
     *
     * This function tracks the "key" argument in calls to switchTo(..) function in order to detect when a
     * perspective is being switched.
     */
    private _handleUnloadEvent(nextPerspectiveKey: string) {
        if (
            nextPerspectiveKey !== this.data.previousSwitchToArg &&
            this.data.previousSwitchToArg !== this.INITIAL_SWITCHTO_ARG
        ) {
            this.crossFrameEventService.publish(
                EVENT_PERSPECTIVE_UNLOADING,
                this.data.previousSwitchToArg
            );
        }
        this.data.previousSwitchToArg = nextPerspectiveKey;
    }

    private _retrievePerspective(key: string): IPerspective {
        // Validation
        // Change the perspective only if it makes sense.
        if (this.data.activePerspective && this.data.activePerspective.key === key) {
            return null;
        }

        const newPerspective: IPerspective = this._findByKey(key);
        if (!newPerspective) {
            throw new Error("switchTo() - Couldn't find perspective with key " + key);
        }

        return newPerspective;
    }

    private _changeActivePerspective(newPerspectiveKey: string) {
        const newPerspective = this._retrievePerspective(newPerspectiveKey);
        if (newPerspective) {
            this.data.previousPerspective = this.data.activePerspective;
            this.data.activePerspective = newPerspective;
            this.storageService.setValueInLocalStorage(
                this.PERSPECTIVE_COOKIE_NAME,
                newPerspective.key,
                true
            );
        }
        return newPerspective;
    }

    private _disableAllFeaturesForPerspective(perspective: IPerspective) {
        const features: string[] = [];
        this._fetchAllFeatures(perspective, features);
        features.forEach((featureKey: string) => {
            this.featureService.disable(featureKey);
        });
    }

    private _clearPerspectiveFeatures() {
        // De-activates all current perspective's features (Still leaves the cookie in the system).
        const perspectiveFeatures: string[] = [];
        if (this.data && this.data.activePerspective) {
            this._fetchAllFeatures(this.data.activePerspective, perspectiveFeatures);
        }

        perspectiveFeatures.forEach((feature: string) => {
            this.featureService.disable(feature);
        });
        return Promise.resolve();
    }

    private get $rootScope(): angular.IRootScopeService {
        // We need to remove rootScope reference as soon as we migrate the routing.

        return this.upgrade.$injector.get('$rootScope');
    }
}
