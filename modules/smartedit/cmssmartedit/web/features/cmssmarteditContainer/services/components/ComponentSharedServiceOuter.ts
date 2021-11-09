/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, SeInjectable } from 'smarteditcommons';
import { IComponentSharedService } from 'cmscommons';

@GatewayProxied()
@SeInjectable()
export class ComponentSharedService extends IComponentSharedService {
    constructor() {
        super();
    }
}
