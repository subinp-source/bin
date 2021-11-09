/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as uib from 'angular-ui-bootstrap';
import { ModalManager } from './ModalManager';
import { IModalConfig } from './IModalConfig';
import { SeConstructor } from '../../di';
import { StringUtils } from '../../utils';

export interface ModalControllerScope extends angular.IScope {
    modalController: IModalController;
    [name: string]: any;
}

export interface IModalController extends angular.IController {
    _modalManager: ModalManager;
    templateUrl: string;
    templateInline: string;

    init(): void;
    close(data?: any): void;
    dismiss(data?: any): void;
    _getModalType(): string;
}

export function modalControllerClassFactory(conf: IModalConfig): SeConstructor<IModalController> {
    // @ngInject
    class ModalController implements IModalController {
        public _modalManager: ModalManager;
        public templateUrl: string;
        public templateInline: string;

        constructor(
            private $scope: ModalControllerScope,
            private $uibModalInstance: uib.IModalServiceInstance,
            private $translate: angular.translate.ITranslateService,
            private $controller: angular.IControllerService,
            private $templateCache: angular.ITemplateCacheService,
            private stringUtils: StringUtils
        ) {
            this._modalManager = new ModalManager(conf, this.$translate, this.$uibModalInstance);
            this.$scope.modalController = this;

            if (conf.controller) {
                angular.extend(
                    this,
                    this.$controller(conf.controller, {
                        $scope,
                        modalManager: this._modalManager
                    })
                );

                if (this.init) {
                    this.init();
                }
            }

            if (conf.controllerAs) {
                this.$scope[conf.controllerAs] = this;
            }

            if (conf.templateInline) {
                this.templateUrl = 'modalTemplateKey' + btoa(this.stringUtils.generateIdentifier());
                this.$templateCache.put(this.templateUrl, conf.templateInline);
            } else {
                this.templateUrl = conf.templateUrl;
            }
        }

        init(): void {
            // ModalController doensn't provide init implementation
        }

        close(data?: any): void {
            this._modalManager.close(data);
            this.$templateCache.remove(this.templateUrl);
        }

        dismiss(data?: any): void {
            this._modalManager.dismiss(data);
            this.$templateCache.remove(this.templateUrl);
        }

        _getModalType(): string {
            const type = conf.templateInline
                ? conf.inlineTemplateSelector
                : this.templateUrl.split('.')[0];
            return 'se-modal--' + type;
        }
    }

    return ModalController;
}
