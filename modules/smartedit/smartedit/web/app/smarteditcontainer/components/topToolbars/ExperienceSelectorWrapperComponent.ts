/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';

import { ToolbarComponent } from '../../services/toolbar/components';

@Component({
    selector: 'se-experience-selector-wrapper',
    template: `
        <se-experience-selector-button
            *ngIf="toolbar.isOnStorefront()"
        ></se-experience-selector-button>
    `
})
export class ExperienceSelectorWrapperComponent {
    constructor(@Inject(forwardRef(() => ToolbarComponent)) public toolbar: ToolbarComponent) {}
}
