/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';
import { AlertRef } from '@fundamental-ngx/core';

import { CompileHtmlNgController } from 'smarteditcommons';

interface ILegacyAlertConfigData {
    template?: string;
    templateUrl?: string;
    controller?: CompileHtmlNgController;
}

@Component({
    selector: 'fundamental-alert-template',
    template: `
        <div
            *ngIf="data.templateUrl"
            [ngInclude]="data.templateUrl"
            [compileHtmlNgController]="data.controller"
            [scope]="data"
        ></div>
        <div
            *ngIf="data.template"
            [seCompileHtml]="data.template"
            [compileHtmlNgController]="data.controller"
            [scope]="data"
        ></div>
    `
})
export class AlertTemplateComponent {
    public data: ILegacyAlertConfigData;

    constructor(ref: AlertRef) {
        this.data = ref.data as ILegacyAlertConfigData;
    }
}
