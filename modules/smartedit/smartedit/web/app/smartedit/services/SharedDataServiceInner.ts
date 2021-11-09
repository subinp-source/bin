/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, ISharedDataService, SeDowngradeService } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(ISharedDataService)
@GatewayProxied()
export class SharedDataService extends ISharedDataService {
    constructor() {
        super();
    }
}
