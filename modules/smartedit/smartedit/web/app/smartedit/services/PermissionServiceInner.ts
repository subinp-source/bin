/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    GatewayProxied,
    IPermissionService,
    LogService,
    PermissionContext,
    SeDowngradeService
} from 'smarteditcommons';

@SeDowngradeService(IPermissionService)
@GatewayProxied()
export class PermissionService extends IPermissionService {
    constructor(private logService: LogService) {
        super();
    }

    _remoteCallRuleVerify(ruleKey: string, permissionNameObjs: PermissionContext[]) {
        if (this.ruleVerifyFunctions && this.ruleVerifyFunctions[ruleKey]) {
            return this.ruleVerifyFunctions[ruleKey].verify(permissionNameObjs);
        }

        this.logService.warn(
            'could not call rule verify function for rule key: ' +
                ruleKey +
                ', it was not found in the iframe'
        );
        return null;
    }
}
