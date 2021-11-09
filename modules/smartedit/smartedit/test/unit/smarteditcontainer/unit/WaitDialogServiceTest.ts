/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { IFundamentalModalConfig, ModalService, WaitDialogComponent } from 'smarteditcommons';
import { WaitDialogService } from '../../../../web/app/smarteditcontainer/services';
import { ModalRef } from '@fundamental-ngx/core';

const defaultModalConfig: IFundamentalModalConfig<{ customLoadingMessageLocalizedKey: string }> = {
    component: WaitDialogComponent,
    data: { customLoadingMessageLocalizedKey: undefined },
    config: {
        backdropClickCloseable: false,
        modalPanelClass: 'se-wait-spinner-dialog',
        focusTrapped: false
    }
};

describe('WaitDialogService', () => {
    let modalService: jasmine.SpyObj<ModalService>;
    let waitDialogService: WaitDialogService;
    let modalRef: jasmine.SpyObj<ModalRef>;

    beforeEach(() => {
        modalRef = jasmine.createSpyObj('modalRef', ['close']);

        modalService = jasmine.createSpyObj('modalService', ['open']);
        modalService.open.and.returnValue(modalRef);

        waitDialogService = new WaitDialogService(modalService);
    });

    it('showWaitModal triggers open on modalService', () => {
        waitDialogService.showWaitModal();

        expect(modalService.open).toHaveBeenCalledWith(defaultModalConfig);
    });

    it('showWaitModal triggers open on modalService with custom translation string', () => {
        waitDialogService.showWaitModal('se.translation.key');

        expect(modalService.open).toHaveBeenCalledWith({
            ...defaultModalConfig,
            config: {
                ...defaultModalConfig.config
            },
            data: {
                customLoadingMessageLocalizedKey: 'se.translation.key'
            }
        });
    });

    it('showWaitModal triggers open on modalService just once', () => {
        waitDialogService.showWaitModal();
        waitDialogService.showWaitModal();

        expect(modalService.open).toHaveBeenCalledTimes(1);
    });

    it('hideWaitModal does not call close when the ref is null', () => {
        waitDialogService.hideWaitModal();

        expect(modalRef.close).not.toHaveBeenCalled();
    });

    it('hideWaitModal calls close when the ref is present', () => {
        waitDialogService.showWaitModal();
        waitDialogService.hideWaitModal();

        expect(modalRef.close).toHaveBeenCalled();
    });
});
