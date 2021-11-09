/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { forwardRef, Component, Inject } from '@angular/core';
import { ModalWizardTemplateComponent } from './ModalWizardTemplateComponent';

@Component({
    selector: 'se-modal-wizard-nav-bar',
    template: `
        <div
            class="se-modal-wizard__steps-container"
            *ngIf="parent._wizardContext && parent._wizardContext.navActions"
        >
            <div
                *ngFor="let action of parent._wizardContext.navActions; let isLast = last"
                class="se-modal-wizard__step"
            >
                <button
                    [attr.id]="action.id"
                    [ngClass]="{
                        'se-modal-wizard-step__action--enabled': action.enableIfCondition(),
                        'se-modal-wizard-step__action--disabled': !action.enableIfCondition(),
                        'se-modal-wizard-step__action--current': action.isCurrentStep()
                    }"
                    class="se-modal-wizard-step__action"
                    (click)="parent.executeAction(action)"
                    [disabled]="!action.enableIfCondition()"
                >
                    {{ action.i18n | translate }}
                </button>
                <span
                    *ngIf="!isLast"
                    [ngClass]="{
                        'se-modal-wizard__step-icon-next--enabled': action.enableIfCondition(),
                        'se-modal-wizard__step-icon-next--disabled': !action.enableIfCondition()
                    }"
                    class="sap-icon--navigation-right-arrow se-modal-wizard__step-icon-next"
                >
                </span>
            </div>
        </div>
    `
})
export class ModalWizardNavBarComponent {
    constructor(
        @Inject(forwardRef(() => ModalWizardTemplateComponent))
        public parent: ModalWizardTemplateComponent
    ) {}
}
