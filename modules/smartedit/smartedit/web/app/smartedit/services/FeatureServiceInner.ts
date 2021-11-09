/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CloneableUtils,
    GatewayProxied,
    IContextualMenuButton,
    IContextualMenuService,
    IDecorator,
    IDecoratorService,
    IFeatureService,
    LogService,
    SeDowngradeService,
    TypedMap
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
    constructor(
        private logService: LogService,
        private decoratorService: IDecoratorService,
        cloneableUtils: CloneableUtils,
        private contextualMenuService: IContextualMenuService
    ) {
        super(cloneableUtils);
    }

    addDecorator(configuration: IDecorator): Promise<void> {
        const prevEnablingCallback = configuration.enablingCallback;
        const prevDisablingCallback = configuration.disablingCallback;
        const displayCondition = configuration.displayCondition;

        configuration.enablingCallback = function() {
            this.enable(configuration.key, displayCondition);

            if (prevEnablingCallback) {
                prevEnablingCallback();
            }
        }.bind(this.decoratorService);

        configuration.disablingCallback = function() {
            this.disable(configuration.key);

            if (prevDisablingCallback) {
                prevDisablingCallback();
            }
        }.bind(this.decoratorService);

        delete configuration.displayCondition;

        return this.register(configuration);
    }

    addContextualMenuButton(item: IContextualMenuButton): Promise<void> {
        const clone = { ...item };

        delete item.nameI18nKey;
        delete item.descriptionI18nKey;
        delete item.regexpKeys;

        clone.enablingCallback = function() {
            const mapping: TypedMap<IContextualMenuButton[]> = {};
            clone.regexpKeys.forEach((regexpKey: string) => {
                mapping[regexpKey] = [item];
            });
            if (!this.containsItem(clone.key)) {
                this.addItems(mapping);
            }
        }.bind(this.contextualMenuService);

        clone.disablingCallback = function() {
            this.removeItemByKey(clone.key);
        }.bind(this.contextualMenuService);

        return this.register(clone);
    }

    protected _remoteEnablingFromInner(key: string): Promise<void> {
        if (this._featuresToAlias && this._featuresToAlias[key]) {
            this._featuresToAlias[key].enablingCallback();
        } else {
            this.logService.warn(
                'could not enable feature named ' + key + ', it was not found in the iframe'
            );
        }
        return Promise.resolve();
    }

    protected _remoteDisablingFromInner(key: string): Promise<void> {
        if (this._featuresToAlias && this._featuresToAlias[key]) {
            this._featuresToAlias[key].disablingCallback();
        } else {
            this.logService.warn(
                'could not disable feature named ' + key + ', it was not found in the iframe'
            );
        }
        return Promise.resolve();
    }
}
