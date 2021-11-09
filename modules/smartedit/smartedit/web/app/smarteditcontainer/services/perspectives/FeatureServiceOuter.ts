/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    CloneableUtils,
    GatewayProxied,
    InternalFeature,
    IFeatureService,
    IToolbarItem,
    IToolbarServiceFactory,
    SeDowngradeService
} from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IFeatureService)
@GatewayProxied(
    '_registerAliases',
    'addToolbarItem',
    'register',
    'enable',
    'disable',
    '_remoteEnablingFromInner',
    '_remoteDisablingFromInner',
    'addDecorator',
    'getFeatureProperty',
    'addContextualMenuButton'
)
export class FeatureService extends IFeatureService {
    private features: InternalFeature[];

    constructor(
        private toolbarServiceFactory: IToolbarServiceFactory,
        cloneableUtils: CloneableUtils
    ) {
        super(cloneableUtils);
        this.features = [];
    }

    getFeatureProperty(
        featureKey: string,
        propertyName: keyof InternalFeature
    ): Promise<string | string[] | (() => void)> {
        const feature = this._getFeatureByKey(featureKey);
        return Promise.resolve(feature ? feature[propertyName] : null);
    }

    getFeatureKeys(): string[] {
        return this.features.map((feature) => feature.key);
    }

    addToolbarItem(configuration: IToolbarItem): Promise<void> {
        const toolbar = this.toolbarServiceFactory.getToolbarService(configuration.toolbarId);
        configuration.enablingCallback = function() {
            this.addItems([configuration]);
        }.bind(toolbar);

        configuration.disablingCallback = function() {
            this.removeItemByKey(configuration.key);
        }.bind(toolbar);

        return this.register(configuration);
    }

    protected _registerAliases(configuration: InternalFeature): Promise<void> {
        const feature = this._getFeatureByKey(configuration.key);
        if (!feature) {
            (configuration as any).id = btoa(configuration.key);
            this.features.push(configuration);
        }
        return Promise.resolve();
    }

    private _getFeatureByKey(key: string): InternalFeature {
        return this.features.find((feature: InternalFeature) => feature.key === key);
    }
}
