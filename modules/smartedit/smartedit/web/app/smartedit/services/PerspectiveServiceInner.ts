/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, IPerspectiveService, SeDowngradeService } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IPerspectiveService)
@GatewayProxied()
export class PerspectiveService extends IPerspectiveService {
    constructor() {
        super();
    }
}
