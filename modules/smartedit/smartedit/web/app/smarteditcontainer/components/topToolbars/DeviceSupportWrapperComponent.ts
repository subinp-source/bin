/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';

import { ToolbarComponent } from '../../services/toolbar/components';

@Component({
    selector: 'se-device-support-wrapper',
    template: `
        <inflection-point-selector
            *ngIf="toolbar.isOnStorefront()"
            class="toolbar-action"
        ></inflection-point-selector>
    `
})
export class DeviceSupportWrapperComponent {
    constructor(@Inject(forwardRef(() => ToolbarComponent)) public toolbar: ToolbarComponent) {}
}
