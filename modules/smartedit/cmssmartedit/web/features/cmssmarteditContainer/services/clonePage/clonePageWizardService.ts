/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogService, ModalWizard, SeInjectable } from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';
import { ClonePageWizardController } from './clonePageWizardController';

/**
 * @ngdoc service
 * @name clonePageWizardServiceModule.service:clonePageWizardService
 *
 * @description
 * The clone page wizard service allows opening a modal wizard to clone a page.
 */

@SeInjectable()
export class ClonePageWizardService {
    constructor(
        private modalWizard: ModalWizard,
        private catalogService: ICatalogService,
        private pageFacade: any
    ) {}

    /**
     * @ngdoc method
     * @name clonePageWizardServiceModule.service:clonePageWizardService#openClonePageWizard
     * @methodOf clonePageWizardServiceModule.service:clonePageWizardService
     *
     * @description
     * When called, this method opens a modal window containing a wizard to clone an existing page.
     *
     * @param {Object} pageData An object containing the pageData when the clone page wizard is opened from the page list.
     * @returns {Promise} A promise that will resolve when the modal wizard is closed or reject if it's canceled.
     *
     */

    public async openClonePageWizard(pageData?: ICMSPage): Promise<any> {
        const uriContext = pageData
            ? await this.catalogService.retrieveUriContext()
            : await this.pageFacade.retrievePageUriContext();

        return this.modalWizard.open({
            controller: ClonePageWizardController,
            controllerAs: 'clonePageWizardCtrl',
            properties: {
                uriContext,
                basePageUUID: pageData ? pageData.uuid : undefined
            }
        });
    }
}
