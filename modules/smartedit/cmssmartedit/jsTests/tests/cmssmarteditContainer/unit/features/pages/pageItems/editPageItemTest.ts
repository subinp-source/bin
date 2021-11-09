/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { EditPageItemComponent } from 'cmssmarteditcontainer/components/pages/pageItems/editPageItem/EditPageItemComponent';
import { PageInfoMenuService } from 'cmssmarteditcontainer/components/pages/pageInfoMenu/services/PageInfoMenuService';

import { ICMSPage } from 'cmscommons';

describe('EditPageItemController', () => {
    let controller: EditPageItemComponent;

    let pageInfoMenuService: PageInfoMenuService;

    beforeEach(() => {
        pageInfoMenuService = jasmine.createSpyObj('pageInfoMenuService', ['openPageEditor']);

        controller = new EditPageItemComponent(pageInfoMenuService);
        controller.pageInfo = {
            uid: 'MOCKED_PAGE_INFO_UID'
        } as ICMSPage;
    });

    it('onClickOnEdit - sends an event when the page edition is resolved', function() {
        // When
        controller.onClickOnEdit();

        // Assert
        expect(pageInfoMenuService.openPageEditor).toHaveBeenCalledWith(controller.pageInfo);
    });
});
