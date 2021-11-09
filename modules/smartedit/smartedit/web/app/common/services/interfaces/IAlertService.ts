/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken } from '@angular/core';
import { IAlertConfig } from '@smart/utils';

import { IAlertServiceType, IBaseAlertService } from 'smarteditcommons/services';
import { CompileHtmlNgController } from '../../directives';

/**
 * When you provide AlertService from '@fundamental-ngx/core' as a dependency
 * it doesn't work, seems to be constructor class name collision.
 * Provide this token in a Module and use it in constructor as follows
 * @Inject(ALERT_SERVICE_TOKEN) fundamentalAlertService: FundamentalAlertService
 */
export const ALERT_SERVICE_TOKEN = new InjectionToken<string>('alertServiceToken');

export interface IAlertConfigLegacy {
    message?: string;
    type?: IAlertServiceType;
    messagePlaceholders?: { [key: string]: any };
    template?: string;
    templateUrl?: string;
    closeable?: boolean;
    timeout?: number;
    successful?: boolean;
    id?: string;
    controller?: CompileHtmlNgController['value'];
}

export abstract class IAlertService implements IBaseAlertService {
    showAlert(alertConf: string | IAlertConfigLegacy | IAlertConfig): void {
        'proxyFunction';
        return;
    }

    showInfo(alertConf: string | IAlertConfigLegacy): void {
        'proxyFunction';
        return;
    }

    showDanger(alertConf: string | IAlertConfigLegacy): void {
        'proxyFunction';
        return;
    }

    showWarning(alertConf: string | IAlertConfigLegacy): void {
        'proxyFunction';
        return;
    }

    showSuccess(alertConf: string | IAlertConfigLegacy): void {
        'proxyFunction';
        return;
    }
}
