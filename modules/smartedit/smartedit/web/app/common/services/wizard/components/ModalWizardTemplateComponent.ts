/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces useValue:false */

import {
    Component,
    ComponentFactory,
    ComponentFactoryResolver,
    Injector,
    Type
} from '@angular/core';
import {
    FundamentalModalButtonAction,
    FundamentalModalButtonStyle,
    FundamentalModalManagerService,
    IFundamentalModalButtonOptions
} from '@smart/utils';
import { of } from 'rxjs';
import * as lo from 'lodash';
import { UpgradeModule } from '@angular/upgrade/static';

import { stringUtils } from '../../../utils/StringUtils';
import { CompileHtmlNgController } from '../../../directives/CompileHtml';
import { WizardAction, WizardActions } from '../services/WizardActions';
import { WizardConfig, WizardService, WizardStep, WIZARD_MANAGER } from '../services/WizardService';
import { DefaultWizardActionStrategy } from '../services/DefaultWizardActionStrategy';

@Component({
    selector: 'se-modal-wizard-template',
    template: `
        <div id="yModalWizard">
            <div class="se-modal-wizard-template">
                <se-modal-wizard-nav-bar></se-modal-wizard-nav-bar>
                <se-modal-wizard-step-outlet></se-modal-wizard-step-outlet>
            </div>
        </div>
    `
})
export class ModalWizardTemplateComponent {
    public executeAction: (action: WizardAction) => void;
    public legacyController: CompileHtmlNgController;
    public _wizardContext: {
        _steps: WizardStep[];
        templateUrl?: string;
        component?: Type<any>;
        navActions?: WizardAction[];
        templateOverride?: string;
    };

    private getWizardConfig: () => WizardConfig;

    private wizardService: WizardService;

    constructor(
        private modalManager: FundamentalModalManagerService,
        private wizardActions: WizardActions,
        private defaultWizardActionStrategy: DefaultWizardActionStrategy,
        private upgrade: UpgradeModule,
        private componentFactoryResolver: ComponentFactoryResolver,
        private injector: Injector
    ) {}

    ngOnInit() {
        this.modalManager.getModalData().subscribe((config: WizardAction) => {
            this.wizardService = new WizardService(this.defaultWizardActionStrategy, stringUtils);
            this.wizardService.properties = config.properties;

            this.assignExternalController(config);

            if (typeof this.getWizardConfig !== 'function') {
                throw new Error(
                    'The provided controller must provide a getWizardConfig() function.'
                );
            }

            const modalConfig = this.getWizardConfig();

            this._wizardContext = {
                _steps: modalConfig.steps
            };

            this.executeAction = (action: WizardAction) => {
                this.wizardService.executeAction(action);
            };

            this.wizardService.onLoadStep = (stepIndex: number, step: WizardStep) => {
                this.modalManager.setTitle(step.title);

                if (step.templateUrl) {
                    this._wizardContext.templateUrl = step.templateUrl;
                } else if (step.component) {
                    this._wizardContext.component = step.component;
                }

                this.modalManager.removeAllButtons();
                (step.actions || []).forEach((action) => {
                    this.modalManager.addButton(this.convertActionToButtonConf(action));
                });
            };

            this.wizardService.onClose = (result: unknown) => {
                this.modalManager.close(result);
            };

            this.wizardService.onCancel = () => {
                this.modalManager.close();
            };

            this.wizardService.onStepsUpdated = (steps: WizardStep[]) => {
                this.setupNavBar(steps);
                this._wizardContext._steps = steps;
            };

            this.wizardService.initialize(modalConfig);
            this.setupModal(modalConfig);
        });
    }

    private setupNavBar(steps: WizardStep[]) {
        this._wizardContext.navActions = steps.map((step, index: number) => {
            const action = this.wizardActions.navBarAction({
                id: 'NAV-' + step.id,
                stepIndex: index,
                wizardService: this.wizardService,
                destinationIndex: index,
                i18n: step.name,
                isCurrentStep: () => {
                    return action.stepIndex === this.wizardService.getCurrentStepIndex();
                }
            });
            return action;
        });
    }

    private setupModal(setupConfig: WizardConfig) {
        this._wizardContext.templateOverride = setupConfig.templateOverride;

        if (setupConfig.cancelAction) {
            this.modalManager.setDismissCallback(() => {
                return this.wizardService.executeAction(setupConfig.cancelAction);
            });
        }

        this.setupNavBar(setupConfig.steps);
    }

    private convertActionToButtonConf(action: WizardAction): IFundamentalModalButtonOptions {
        return {
            id: action.id,
            style: action.isMainAction
                ? FundamentalModalButtonStyle.Primary
                : FundamentalModalButtonStyle.Default,
            label: action.i18n,
            action: FundamentalModalButtonAction.None,
            disabledFn: () => !action.enableIfCondition(),
            callback: () => {
                this.wizardService.executeAction(action);

                return of(null);
            }
        };
    }

    private assignExternalController(config: WizardAction): void {
        if (config.controller) {
            this.assignLegacyController(config);
        }

        if (config.component) {
            this.assignAngularController(config);
        }
    }

    private assignLegacyController(config: WizardAction): void {
        lo.assign(
            this,
            this.$controller(config.controller, {
                wizardManager: this.wizardService
            })
        );

        this.legacyController = {
            value: () => this,
            alias: config.controllerAs
        };
    }

    private assignAngularController(config: WizardAction): void {
        const injector: Injector = Injector.create({
            providers: [{ provide: WIZARD_MANAGER, useValue: this.wizardService }],
            parent: this.injector
        });
        const factory: ComponentFactory<
            any
        > = this.componentFactoryResolver.resolveComponentFactory(config.component);

        lo.assign(this, factory.create(injector).instance);
    }

    private get $controller(): angular.IControllerService {
        return this.upgrade.$injector.get('$controller');
    }
}
