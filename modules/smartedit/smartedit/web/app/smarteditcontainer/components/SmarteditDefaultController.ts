/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeInjectable } from 'smarteditcommons';
import { ExperienceService, IframeManagerService } from 'smarteditcontainer/services';

/** @internal */
@SeInjectable()
export class SmarteditDefaultController {
    constructor(
        iframeManagerService: IframeManagerService,
        experienceService: ExperienceService,
        yjQuery: JQueryStatic
    ) {
        iframeManagerService.applyDefault();
        experienceService.initializeExperience();
        yjQuery(document.body).addClass('is-storefront');
    }
}
