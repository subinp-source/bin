/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { GatewayProxied, ISessionService, SeDowngradeService } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(ISessionService)
@GatewayProxied()
@Injectable()
export class SessionService extends ISessionService {
    constructor() {
        super();
    }
}
