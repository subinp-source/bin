/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, SeInjectable } from 'smarteditcommons';
import { IPageService } from 'cmscommons';

@SeInjectable()
@GatewayProxied()
export class PageService extends IPageService {
    constructor() {
        super();
    }
}
