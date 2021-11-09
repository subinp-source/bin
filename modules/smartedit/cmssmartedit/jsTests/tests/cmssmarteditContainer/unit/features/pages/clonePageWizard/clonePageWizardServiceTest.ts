/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogService, ModalWizard } from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';
import { ClonePageWizardController, ClonePageWizardService } from 'cmssmarteditcontainer/services';

describe('clonePageWizardService', () => {
    let clonePageWizardService: ClonePageWizardService;
    let modalWizard: jasmine.SpyObj<ModalWizard>;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let pageFacade: jasmine.SpyObj<any>;

    const uriContext = {
        a: 'b'
    };

    beforeEach(() => {
        modalWizard = jasmine.createSpyObj('modalWizard', ['open']);
        catalogService = jasmine.createSpyObj('catalogService', ['retrieveUriContext']);
        pageFacade = jasmine.createSpyObj('pageFacade', ['retrievePageUriContext']);

        catalogService.retrieveUriContext.and.returnValue(Promise.resolve(uriContext));
        pageFacade.retrievePageUriContext.and.returnValue(Promise.resolve(uriContext));

        clonePageWizardService = new ClonePageWizardService(
            modalWizard,
            catalogService,
            pageFacade
        );
    });

    describe('clonePageWizardService', () => {
        it('GIVEN pageData is sent to the method THEN will delegate to the modal wizard a valid basePageUid', async (done) => {
            await clonePageWizardService.openClonePageWizard({
                uuid: 'some uuid'
            } as ICMSPage);

            expect(modalWizard.open).toHaveBeenCalledWith({
                controller: ClonePageWizardController,
                controllerAs: 'clonePageWizardCtrl',
                properties: {
                    uriContext,
                    basePageUUID: 'some uuid'
                }
            });

            done();
        });

        it('GIVEN no pageData sent to the method THEN will delegate to the modal wizard with a undefined basePageUid', async (done) => {
            await clonePageWizardService.openClonePageWizard();

            expect(modalWizard.open).toHaveBeenCalledWith({
                controller: ClonePageWizardController,
                controllerAs: 'clonePageWizardCtrl',
                properties: {
                    uriContext,
                    basePageUUID: undefined
                }
            });

            done();
        });
    });
});
