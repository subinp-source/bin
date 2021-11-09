/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ClonePageItemComponent } from 'cmssmarteditcontainer/components/pages/pageItems/clonePageItem/ClonePageItemComponent';
import { ICMSPage } from 'cmscommons';

describe('ClonePageItemController', () => {
    let controller: ClonePageItemComponent;

    let clonePageWizardService: any;

    const MOCKED_PAGE_INFO = {
        name: 'MOCKED_PAGE'
    } as ICMSPage;

    beforeEach(() => {
        clonePageWizardService = jasmine.createSpyObj('clonePageWizardService', [
            'openClonePageWizard'
        ]);

        controller = new ClonePageItemComponent(clonePageWizardService);
        controller.pageInfo = MOCKED_PAGE_INFO;
    });

    it("calls clonePageWizardService to display a 'clone page' wizard", function() {
        // WHEN
        controller.onClickOnClone();

        // ASSERT
        expect(clonePageWizardService.openClonePageWizard).toHaveBeenCalledWith(MOCKED_PAGE_INFO);
    });
});
