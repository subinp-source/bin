/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, IStorageService, SeDowngradeService } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IStorageService)
@GatewayProxied()
export class StorageService extends IStorageService {
    constructor() {
        super();
    }
}
