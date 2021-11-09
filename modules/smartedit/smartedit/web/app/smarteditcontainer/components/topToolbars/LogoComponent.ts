/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';

@Component({
    selector: 'se-logo',
    template: `
        <div class="se-app-logo">
            <img src="static-resources/images/SAP_scrn_R.png" class="se-logo-image" />
            <div class="se-logo-text">{{ 'se.application.name' | translate }}</div>
        </div>
    `
})
export class LogoComponent {}
