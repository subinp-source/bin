/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    GatewayProxied,
    IFundamentalModalConfig,
    IWaitDialogService,
    ModalService,
    SeDowngradeService,
    WaitDialogComponent
} from 'smarteditcommons';
import { ModalRef } from '@fundamental-ngx/core';

/** @internal */
@SeDowngradeService(IWaitDialogService)
@GatewayProxied()
export class WaitDialogService extends IWaitDialogService {
    private modalRef: ModalRef;

    constructor(private modalService: ModalService) {
        super();
        this.modalRef = null;
    }

    showWaitModal(customLoadingMessageLocalizedKey?: string): void {
        const config: IFundamentalModalConfig<{ customLoadingMessageLocalizedKey: string }> = {
            component: WaitDialogComponent,
            data: { customLoadingMessageLocalizedKey },
            config: {
                backdropClickCloseable: false,
                modalPanelClass: 'se-wait-spinner-dialog',
                focusTrapped: false
            }
        };

        if (this.modalRef === null) {
            this.modalRef = this.modalService.open(config);
        }
    }

    hideWaitModal(): void {
        if (this.modalRef != null) {
            this.modalRef.close();
            this.modalRef = null;
        }
    }
}
