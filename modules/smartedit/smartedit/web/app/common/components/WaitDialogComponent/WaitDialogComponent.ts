/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';
import { ModalRef } from '@fundamental-ngx/core';

@Component({
    template: `
        <div class="panel panel-default ySEPanelSpinner">
            <div class="panel-body">
                <div class="spinner ySESpinner">
                    <div class="spinner-container spinner-container1">
                        <div class="spinner-circle1"></div>
                        <div class="spinner-circle2"></div>
                        <div class="spinner-circle3"></div>
                        <div class="circle4"></div>
                    </div>
                    <div class="spinner-container spinner-container2">
                        <div class="spinner-circle1"></div>
                        <div class="spinner-circle2"></div>
                        <div class="spinner-circle3"></div>
                        <div class="circle4"></div>
                    </div>
                    <div class="spinner-container spinner-container3">
                        <div class="spinner-circle1"></div>
                        <div class="spinner-circle2"></div>
                        <div class="spinner-circle3"></div>
                        <div class="circle4"></div>
                    </div>
                </div>
                <div class="ySESpinnerText">
                    {{
                        modalRef.data.customLoadingMessageLocalizedKey || 'se.wait.dialog.message'
                            | translate
                    }}
                </div>
            </div>
        </div>
    `,
    selector: 'wait-dialog'
})
export class WaitDialogComponent {
    constructor(public modalRef: ModalRef) {}
}
