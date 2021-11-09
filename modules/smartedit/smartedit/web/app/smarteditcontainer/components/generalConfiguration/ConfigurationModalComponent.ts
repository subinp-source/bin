/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { from as rxFrom } from 'rxjs';

import {
    ConfirmationModalConfig,
    FundamentalModalButtonAction,
    FundamentalModalButtonStyle,
    FundamentalModalManagerService,
    IConfirmationModalService
} from 'smarteditcommons';
import { ConfigurationService } from '../../services/ConfigurationService';
import { ConfigurationItem } from '../../services/bootstrap/Configuration';

@Component({
    selector: 'se-configuration-modal',
    templateUrl: './ConfigurationModalComponent.html'
})
export class ConfigurationModalComponent implements OnInit, AfterViewInit {
    @ViewChild('form', { static: false }) public form: NgForm;

    constructor(
        public editor: ConfigurationService,
        public modalManager: FundamentalModalManagerService,
        private confirmationModalService: IConfirmationModalService
    ) {}

    ngAfterViewInit() {
        this.form.statusChanges.subscribe(() => {
            if (this.form.valid && this.form.dirty) {
                this.modalManager.enableButton('save');
            }

            if (this.form.invalid || !this.form.dirty) {
                this.modalManager.disableButton('save');
            }
        });
    }

    ngOnInit() {
        this.editor.init();

        this.modalManager.addButton({
            id: 'cancel',
            label: 'se.cms.component.confirmation.modal.cancel',
            style: FundamentalModalButtonStyle.Default,
            action: FundamentalModalButtonAction.Dismiss,
            callback: () => rxFrom(this.onCancel())
        });

        this.modalManager.addButton({
            id: 'save',
            style: FundamentalModalButtonStyle.Primary,
            label: 'se.cms.component.confirmation.modal.save',
            callback: () => rxFrom(this.onSave()),
            disabled: true
        });
    }

    public trackByFn(_: number, item: ConfigurationItem): string {
        return item.uuid;
    }

    private onCancel(): Promise<void> {
        const { dirty } = this.form;
        const confirmationData: ConfirmationModalConfig = {
            description: 'se.editor.cancel.confirm'
        };

        if (!dirty) {
            return Promise.resolve();
        }

        return this.confirmationModalService
            .confirm(confirmationData)
            .then(() => this.modalManager.close(null));
    }

    private onSave(): Promise<void> {
        return this.editor.submit(this.form).then(() => {
            this.modalManager.close(null);
        });
    }
}
