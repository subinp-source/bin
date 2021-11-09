/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { ConfirmationModalConfig, LegacyConfirmationModalConfig } from './IConfirmationModal';

export abstract class IConfirmationModalService {
    confirm(conf: LegacyConfirmationModalConfig): angular.IPromise<any> | Promise<any>;
    confirm(conf: ConfirmationModalConfig): Promise<any>;
    confirm(
        conf: LegacyConfirmationModalConfig | ConfirmationModalConfig
    ): angular.IPromise<any> | Promise<any> {
        'proxyFunction';

        return Promise.resolve();
    }
}
