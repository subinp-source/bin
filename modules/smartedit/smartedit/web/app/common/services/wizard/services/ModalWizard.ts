/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IModalService } from '@smart/utils';

import { SeDowngradeService } from '../../../di/SeDowngradeService';
import { WizardAction } from './WizardActions';
import { ModalWizardTemplateComponent } from '../components/ModalWizardTemplateComponent';

import '../components/modalWizardNavBar.scss';

/**
 * @ngdoc service
 * @name wizardServiceModule.modalWizard
 *
 * @description
 * The modalWizard service is used to create wizards that are embedded into the {@link modalServiceModule modalService}
 */
@SeDowngradeService()
export class ModalWizard {
    constructor(private modalService: IModalService) {}

    /**
     * @ngdoc method
     * @name wizardServiceModule.modalWizard#open
     * @methodOf wizardServiceModule.modalWizard
     *
     * @description
     * Open provides a simple way to create modal wizards, with much of the boilerplate taken care of for you, such as look
     * and feel, and wizard navigation.
     *
     * @param {WizardAction} conf configuration
     * @param {String|function|Array} conf.controller Deprecated, use conf.component. An angular controller which will be the underlying controller
     * for all of the wizard. This controller MUST implement the function <strong>getWizardConfig()</strong> which
     * returns a {@link wizardServiceModule.object:ModalWizardConfig ModalWizardConfig}.<br />
     * If you need to do any manual wizard manipulation, 'wizardManager' can be injected into your controller.
     * See {@link wizardServiceModule.WizardManager WizardManager}
     * @param {String} conf.controllerAs (OPTIONAL) Deprecated, use conf.component. An alternate controller name that can be used in your wizard step
     * @param {=String=} conf.properties A map of properties to initialize the wizardManager with. They are accessible under wizardManager.properties.
     * templates. By default the controller name is wizardController.
     * @param {Type<any>} conf.component Component to be used as a wizard controller
     * @returns {function} {@link https://docs.angularjs.org/api/ng/service/$q promise} that will either be resolved (wizard finished) or
     * rejected (wizard cancelled).
     */

    open(config: WizardAction): Promise<any> {
        this.validateConfig(config);

        return new Promise((resolve, reject) => {
            const ref = this.modalService.open<WizardAction>({
                component: ModalWizardTemplateComponent,
                templateConfig: { isDismissButtonVisible: true },
                data: config,
                config: {
                    focusTrapped: false,
                    backdropClickCloseable: false
                }
            });

            ref.afterClosed.subscribe(resolve, reject);
        });
    }
    private validateConfig(config: WizardAction): void {
        if (!config.controller && !config.component) {
            throw new Error(
                'WizardService - initialization exception. No controller nor component provided'
            );
        }

        if (config.controller && config.component) {
            throw new Error(
                'WizardService - initialization exception. Provide either controller or component'
            );
        }
    }
}
