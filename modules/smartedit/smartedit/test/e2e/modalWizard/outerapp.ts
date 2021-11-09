/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

// tslint:disable:max-classes-per-file
/* forbiddenNameSpaces angular.module:false */

import { forwardRef, Component, Inject, NgModule } from '@angular/core';
import * as angular from 'angular';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
    IConfirmationModalService,
    ModalWizard,
    ModalWizardTemplateComponent,
    SeDowngradeComponent,
    SeEntryModule,
    WizardModule,
    WizardService,
    WizardStep,
    WIZARD_MANAGER
} from 'smarteditcommons';

class PersonValidator {
    static isValidPerson(person: any) {
        return PersonValidator.isValidName(person);
    }

    static isValidName(person: any) {
        if (person.name) {
            return true;
        } else {
            return false;
        }
    }
}
@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <button id="legacy-open" (click)="openLegacy()">Open Legacy</button>
            <button id="angular-open" (click)="openAngular()">Open Angular</button>
        </div>
    `
})
export class AppRootComponent {
    constructor(private modalWizard: ModalWizard) {}

    openLegacy() {
        this.modalWizard.open({
            controller: 'personBuilderController',
            controllerAs: 'personController'
        } as any);
    }

    openAngular() {
        this.modalWizard.open({
            component: ModalWizardControllerComponent
        } as any);
    }
}

@Component({
    selector: 'wizard-step-name',
    template: `
        <div id="nameStep">
            Name: <input id="nameInput" type="text" [(ngModel)]="parent.person.name" />
        </div>
    `
})
export class WizardStepNameComponent {
    constructor(
        @Inject(forwardRef(() => ModalWizardTemplateComponent))
        public parent: ModalWizardTemplateComponent
    ) {}
}

@Component({
    selector: 'wizard-step-extra',
    template: `
        <div id="offendedStep">Aha! The sensitive type! Sorry for that...</div>
    `
})
export class WizardStepExtraComponent {}

@Component({
    selector: 'wizard-step-done',
    template: `
        <div id="doneStep">Your new Person: <br />{{ parent.person }}</div>
    `
})
export class WizardStepDoneComponent {
    constructor(
        @Inject(forwardRef(() => ModalWizardTemplateComponent))
        public parent: ModalWizardTemplateComponent
    ) {}
}

@Component({
    selector: 'wizard-step-sex',
    template:
        '<div id="genderStep">\n' +
        '    Select a Gender:\n' +
        '    <select [(ngModel)]="parent.person.gender">\n' +
        '        <option value="male">Male</option>\n' +
        '        <option value="female">Female</option>\n' +
        '    </select>\n' +
        '    <br />\n' +
        '    <br />\n' +
        '    <label>\n' +
        '        <input\n' +
        '            id="offendedCheck"\n' +
        '            type="checkbox"\n' +
        '            [(ngModel)]="parent.person.offended"\n' +
        '        />\n' +
        '        How DARE you?\n' +
        '    </label>\n' +
        '    <button type="button" (click)="parent.toggleExtraStep()">\n' +
        '        Manual Step Toggle\n' +
        '    </button>\n' +
        '</div>\n'
})
export class WizardStepSexComponent {
    constructor(
        @Inject(forwardRef(() => ModalWizardTemplateComponent))
        public parent: ModalWizardTemplateComponent
    ) {}
}

@Component({
    selector: 'wizard-step-age',
    template:
        '<div id="ageStep">\n' +
        '    Age: <input type="number" name="input" [(ngModel)]="parent.person.age" />\n' +
        '</div>\n'
})
export class WizardStepAgeComponent {
    constructor(
        @Inject(forwardRef(() => ModalWizardTemplateComponent))
        public parent: ModalWizardTemplateComponent
    ) {}
}

// Angular
@Component({
    selector: 'modal-wizard-controller',
    template: ''
})
class ModalWizardControllerComponent {
    extraStep = {
        id: 'extra',
        name: 'Appology',
        title: 'wizard.title.extra',
        component: WizardStepExtraComponent
    };

    stepToggle = false;
    person = { offended: false, name: '' };
    constructor(
        @Inject(WIZARD_MANAGER) private wizardManager: WizardService,
        private confirmationModalService: IConfirmationModalService
    ) {}

    updateOffendedStep(toggle: boolean) {
        const stepIsAdded = this.wizardManager.containsStep('extra');
        if (toggle) {
            if (!stepIsAdded) {
                this.wizardManager.addStep(this.extraStep as WizardStep, 2);
            }
        } else if (stepIsAdded) {
            this.wizardManager.removeStepById('extra');
        }
    }

    toggleExtraStep() {
        this.stepToggle = !this.stepToggle;
        this.updateOffendedStep(this.stepToggle);
    }

    isFormValid = (stepId: string) => {
        switch (stepId) {
            case 'name':
                return PersonValidator.isValidName(this.person);
            default:
                return true;
        }
    };

    onNext = (stepId: string) => {
        if (stepId === 'gender') {
            this.updateOffendedStep(this.person.offended);
        }
    };

    onCancel = () => {
        return this.confirmationModalService.confirm({
            description: 'Are you sure you want to cancel?'
        });
    };

    onDone = () => {
        return this.confirmationModalService.confirm({
            description: 'Save and close?'
        });
    };

    getResult = () => {
        return this.person;
    };

    getWizardConfig = () => {
        return {
            isFormValid: this.isFormValid,
            onNext: this.onNext,
            onCancel: this.onCancel,
            onDone: this.onDone,
            doneLabel: 'action.save',
            resultFn: this.getResult,
            // wizard model
            steps: [
                {
                    id: 'name',
                    name: 'Name',
                    title: 'wizard.title.name',
                    component: WizardStepNameComponent
                },
                {
                    id: 'gender',
                    name: 'Gender',
                    title: 'wizard.title.sex',
                    component: WizardStepSexComponent
                },
                {
                    id: 'age',
                    name: 'Age',
                    title: 'wizard.title.age',
                    component: WizardStepAgeComponent
                },
                {
                    id: 'summary',
                    name: 'Summary',
                    title: 'wizard.title.save',
                    component: WizardStepDoneComponent
                }
            ]
        };
    };
}

// Legacy

angular
    .module('ModalWizardApp', [
        'templateCacheDecoratorModule',
        'coretemplates',
        'resourceLocationsModule',
        'confirmationModalServiceModule',
        'smarteditServicesModule'
    ])
    .service('personValidator', function() {
        return {
            isValidPerson(person: any) {
                return this.isValidName(person);
            },

            isValidName(person: any) {
                if (person.name) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    })

    .controller('personBuilderController', [
        '$q',
        '$log',
        'wizardActions',
        'wizardManager',
        'personValidator',
        'confirmationModalService',
        function(
            $q,
            $log,
            wizardActions,
            wizardManager,
            personValidator,
            confirmationModalService
        ) {
            const extraStep = {
                id: 'extra',
                name: 'Appology',
                title: 'wizard.title.extra',
                templateUrl: 'test/e2e/wizard/stepExtra.html'
            };

            let stepToggle = false;

            function updateOffendedStep(toggle: boolean) {
                const stepIsAdded = wizardManager.containsStep('extra');
                if (toggle) {
                    if (!stepIsAdded) {
                        wizardManager.addStep(extraStep, 2);
                    }
                } else if (stepIsAdded) {
                    wizardManager.removeStepById('extra');
                }
            }

            this.toggleExtraStep = function() {
                stepToggle = !stepToggle;
                updateOffendedStep(stepToggle);
            };

            this.person = {};

            this.isFormValid = function(stepId: string) {
                switch (stepId) {
                    case 'name':
                        return personValidator.isValidName(this.person);
                    default:
                        return true;
                }
            }.bind(this);

            this.onNext = function(stepId: string) {
                if (stepId === 'gender') {
                    updateOffendedStep(this.person.offended);
                }
            }.bind(this);

            this.onCancel = function() {
                return confirmationModalService.confirm({
                    description: 'Are you sure you want to cancel?'
                });
            };

            this.onDone = function() {
                return confirmationModalService.confirm({
                    description: 'Save and close?'
                });
            };

            this.getResult = function() {
                return this.person;
            }.bind(this);

            this.getWizardConfig = function() {
                return {
                    isFormValid: this.isFormValid,
                    onNext: this.onNext,
                    onCancel: this.onCancel,
                    onDone: this.onDone,
                    doneLabel: 'action.save',

                    resultFn: this.getResult,

                    // wizard model
                    steps: [
                        {
                            id: 'name',
                            name: 'Name',
                            title: 'wizard.title.name',
                            templateUrl: 'test/e2e/wizard/stepName.html'
                        },
                        {
                            id: 'gender',
                            name: 'Gender',
                            title: 'wizard.title.sex',
                            templateUrl: 'test/e2e/wizard/stepSex.html'
                        },
                        {
                            id: 'age',
                            name: 'Age',
                            title: 'wizard.title.age',
                            templateUrl: 'test/e2e/wizard/stepAge.html'
                        },
                        {
                            id: 'summary',
                            name: 'Summary',
                            title: 'wizard.title.save',
                            templateUrl: 'test/e2e/wizard/stepDone.html'
                        }
                    ]
                };
            }.bind(this);
        }
    ]);

angular.module('smarteditcontainer').requires.push('ModalWizardApp');

@SeEntryModule('ModalWizardApp')
@NgModule({
    imports: [CommonModule, WizardModule, FormsModule],
    declarations: [
        AppRootComponent,
        ModalWizardControllerComponent,
        WizardStepNameComponent,
        WizardStepExtraComponent,
        WizardStepDoneComponent,
        WizardStepSexComponent,
        WizardStepAgeComponent
    ],
    entryComponents: [
        AppRootComponent,
        ModalWizardControllerComponent,
        WizardStepNameComponent,
        WizardStepExtraComponent,
        WizardStepDoneComponent,
        WizardStepSexComponent,
        WizardStepAgeComponent
    ]
})
export class SelectApp {}
