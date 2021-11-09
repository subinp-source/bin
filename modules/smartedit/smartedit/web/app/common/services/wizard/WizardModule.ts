/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { CompileHtmlModule } from '../../directives/CompileHtmlModule';
import { ModalWizard } from './services/ModalWizard';
import { WizardActions } from './services/WizardActions';
import { DefaultWizardActionStrategy } from './services/DefaultWizardActionStrategy';
import { ModalWizardNavBarComponent } from './components/ModalWizardNavBarComponent';
import { ModalWizardStepOutletComponent } from './components/ModalWizardStepOutletComponent';
import { ModalWizardTemplateComponent } from './components/ModalWizardTemplateComponent';

/**
 * @ngdoc overview
 * @name wizardServiceModule
 *
 * @description
 * # The wizardServiceModule
 * The wizardServiceModule is a module containing all wizard related services
 * # Creating a modal wizard in a few simple steps
 *
 * For AngularJS usage:
 *
 * 1. Inject {@link wizardServiceModule.modalWizard modalWizard} where you want to use the wizard.
 * 2. Create a new controller for your wizard. This controller will be used for all steps of the wizard.
 * 3. Implement a function in your new controller called getWizardConfig that returns a {@link wizardServiceModule.object:ModalWizardConfig ModalWizardConfig}
 * 4. Use {@link wizardServiceModule.modalWizard#methods_open modalWizard.open()} passing in your new controller
 *
 * For Angular usage
 * 1. Import WizardModule from "smarteditcommons" to your module imports
 * 2. Inject {@link wizardServiceModule.modalWizard modalWizard} using ModalWizard constructor from "smarteditcommons".
 * 3. Create a new component controller for your wizard. This component will be used for all steps of the wizard.
 * 4. Create step components to be rendered inside the wizard. If access to component controller is needed, inject the parent reference
 * 5. Implement a method in your new controller called getWizardConfig that returns a {@link wizardServiceModule.object:ModalWizardConfig ModalWizardConfig}
 * 6. Use {@link wizardServiceModule.modalWizard#methods_open modalWizard.open()} passing in your new controller
 *
 * <pre>
 *
 * AngularJS:
 *
 * @SeInjectable()
 * export class MyAngularJsWizardService {
 * 		constructor(private modalWizard) {}
 * 		open() {
 * 			this.modalWizard.open({
 * 				controller: (wizardManager: any) => {
 * 					'ngInject';
 * 					return {
 * 						steps: [{
 * 							id: 'step1',
 * 							name: 'i18n.step1.name',
 * 							title: 'i18n.step1.title',
 * 							templateUrl: 'some/template1.html'
 * 						}, {
 * 							id: 'step2',
 * 							name: 'i18n.step2.name',
 * 							title: 'i18n.step2.title',
 * 							templateUrl: 'some/template2.html'
 * 						}]
 * 					};
 * 				}
 * 			});
 * 		}
 * }
 *
 *   export class MyAngularWizardService {
 * 		constructor(private modalWizard: ModalWizard) {}
 * 		open() {
 * 			this.modalWizard.open({
 *              component: MyWizardControllerComponent,
 * 			});
 * 		}
 * }
 * </pre>
 */

@NgModule({
    imports: [CommonModule, CompileHtmlModule, TranslateModule],
    providers: [ModalWizard, WizardActions, DefaultWizardActionStrategy],
    declarations: [
        ModalWizardTemplateComponent,
        ModalWizardNavBarComponent,
        ModalWizardStepOutletComponent
    ],
    entryComponents: [
        ModalWizardTemplateComponent,
        ModalWizardNavBarComponent,
        ModalWizardStepOutletComponent
    ]
})
export class WizardModule {}
