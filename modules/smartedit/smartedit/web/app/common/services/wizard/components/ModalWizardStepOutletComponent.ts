/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';
import { ModalWizardTemplateComponent } from './ModalWizardTemplateComponent';

@Component({
    selector: 'se-modal-wizard-step-outlet',
    template: `
        <!-- AngularJS -->

        <div
            *ngIf="parent?._wizardContext?.templateUrl"
            [ngInclude]="parent._wizardContext.templateUrl"
            [compileHtmlNgController]="parent.legacyController"
            class="se-modal-wizard__content"
        ></div>

        <!-- Angular -->

        <div *ngIf="parent?._wizardContext?.component" class="se-modal-wizard__content">
            <ng-container *ngComponentOutlet="parent._wizardContext.component"></ng-container>
        </div>
    `
})
export class ModalWizardStepOutletComponent {
    constructor(
        @Inject(forwardRef(() => ModalWizardTemplateComponent))
        public parent: ModalWizardTemplateComponent
    ) {}
}
