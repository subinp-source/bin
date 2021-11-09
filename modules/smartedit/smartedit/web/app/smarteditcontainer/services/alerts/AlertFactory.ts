/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject, Injectable, SecurityContext } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { AlertConfig, AlertService as FundamentalAlertService } from '@fundamental-ngx/core';
import * as lodash from 'lodash';
import {
    Alert,
    ALERT_CONFIG_DEFAULTS_TOKEN,
    ALERT_SERVICE_TOKEN,
    BaseAlertFactory,
    IAlertConfig,
    IAlertConfigLegacy,
    IAlertServiceType,
    LogService,
    SeDowngradeService
} from 'smarteditcommons';
import { AlertTemplateComponent } from './AlertTemplateComponent';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:AlertFactory
 */
@SeDowngradeService()
@Injectable()
export class AlertFactory extends BaseAlertFactory {
    constructor(
        private logService: LogService,
        private domSanitizer: DomSanitizer,
        @Inject(ALERT_SERVICE_TOKEN) fundamentalAlertService: FundamentalAlertService,
        translateService: TranslateService,
        @Inject(ALERT_CONFIG_DEFAULTS_TOKEN) ALERT_CONFIG_DEFAULTS: AlertConfig
    ) {
        super(fundamentalAlertService, translateService, ALERT_CONFIG_DEFAULTS);
    }

    public createAlert(alertConf: string | IAlertConfigLegacy | IAlertConfig): Alert {
        alertConf = this.getAlertConfigFromStringOrConfig(alertConf);
        return super.createAlert(alertConf);
    }

    public createInfo(alertConf: string | IAlertConfigLegacy | IAlertConfig): Alert {
        alertConf = this.getAlertConfigFromStringOrConfig(alertConf);
        alertConf.type = IAlertServiceType.INFO;
        return super.createInfo(alertConf);
    }

    public createDanger(alertConf: string | IAlertConfigLegacy | IAlertConfig): Alert {
        alertConf = this.getAlertConfigFromStringOrConfig(alertConf);
        alertConf.type = IAlertServiceType.DANGER;
        return super.createDanger(alertConf);
    }

    public createWarning(alertConf: string | IAlertConfigLegacy | IAlertConfig): Alert {
        alertConf = this.getAlertConfigFromStringOrConfig(alertConf);
        alertConf.type = IAlertServiceType.WARNING;
        return super.createWarning(alertConf);
    }

    public createSuccess(alertConf: string | IAlertConfigLegacy | IAlertConfig): Alert {
        alertConf = this.getAlertConfigFromStringOrConfig(alertConf);
        alertConf.type = IAlertServiceType.SUCCESS;
        return super.createSuccess(alertConf);
    }

    /**
     * Accepts message string or config object
     * Will convert a str param to { message: str }
     */
    public getAlertConfigFromStringOrConfig(
        strOrConf: string | IAlertConfigLegacy | IAlertConfig
    ): IAlertConfigLegacy | IAlertConfig {
        if (typeof strOrConf === 'string') {
            return {
                message: strOrConf
            };
        }

        const config = strOrConf;
        if (this.isFundamentalAlertConfig(config)) {
            return {
                ...config
            };
        }
        return this.validateAndGetAlertConfigFromLegacyConfig(config);
    }

    private isFundamentalAlertConfig(config: any): config is IAlertConfig {
        const legacyConfig: IAlertConfigLegacy = {
            template: undefined,
            templateUrl: undefined,
            closeable: undefined,
            timeout: undefined,
            successful: undefined
        };

        const hasInvalidKey = Object.keys(config).some((key: string) =>
            legacyConfig.hasOwnProperty(key)
        );
        return !hasInvalidKey;
    }

    private validateAndGetAlertConfigFromLegacyConfig(alertConf: IAlertConfigLegacy): IAlertConfig {
        this.validateLegacyAlertConfig(alertConf);
        this.sanitizeTemplates(alertConf);
        return this.getAlertConfigFromLegacyConfig(alertConf);
    }

    private getAlertConfigFromLegacyConfig(alertConf: IAlertConfigLegacy): IAlertConfig {
        const config: IAlertConfig = {
            message: alertConf.message,
            messagePlaceholders: alertConf.messagePlaceholders,
            dismissible: alertConf.closeable,
            type: alertConf.type,
            id: alertConf.id,
            duration: alertConf.timeout
        };

        if (alertConf.template || alertConf.templateUrl) {
            config.component = AlertTemplateComponent;
            config.data = {
                template: alertConf.template,
                templateUrl: alertConf.templateUrl
            };
            if (alertConf.controller) {
                config.data.controller = {
                    alias: '$alertInjectedCtrl',
                    value: alertConf.controller
                };
            }
        }
        return config;
    }

    private validateLegacyAlertConfig(alertConf: IAlertConfigLegacy): void {
        this.fixLegacyAlert(alertConf);
        if (!alertConf.message && !alertConf.template && !alertConf.templateUrl) {
            this.logService.warn(
                'alertService._validateAlertConfig - alert must contain at least a message, template, or templateUrl property',
                alertConf
            );
        }
        if (alertConf.messagePlaceholders && !lodash.isObject(alertConf.messagePlaceholders)) {
            throw new Error(
                'alertService._validateAlertConfig - property messagePlaceholders should be an object'
            );
        }
        if (
            (alertConf.message && (alertConf.template || alertConf.templateUrl)) ||
            (alertConf.template && (alertConf.message || alertConf.templateUrl)) ||
            (alertConf.templateUrl && (alertConf.message || alertConf.template))
        ) {
            throw new Error(
                'alertService._validateAlertConfig - only one template type is allowed for the alert: message, template, or templateUrl'
            );
        }
    }

    private sanitizeTemplates(alertConf: IAlertConfigLegacy): void {
        if (alertConf.message) {
            alertConf.message = this.domSanitizer.sanitize(SecurityContext.HTML, alertConf.message);
        }
    }

    /**
     * @deprecated since 1905
     */
    private fixLegacyAlert(legacyAlertConf: IAlertConfigLegacy): void {
        if (legacyAlertConf.successful) {
            if (legacyAlertConf.type) {
                this.logService.warn(
                    'alertService validation warning: alert contains both legacy successful ' +
                        'property and an alert type for alert: ',
                    legacyAlertConf
                );
            } else {
                if (typeof legacyAlertConf.successful !== 'boolean') {
                    this.logService.warn(
                        'alertService validation warning: legacyAlertConf.successful not a boolean value for alert: ',
                        legacyAlertConf
                    );
                }
                legacyAlertConf.type = legacyAlertConf.successful
                    ? IAlertServiceType.SUCCESS
                    : IAlertServiceType.DANGER;
            }
            delete legacyAlertConf.successful;
        }
    }
}
