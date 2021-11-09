/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { modalControllerClassFactory } from './modalControllerClassFactory';
import { IModalConfig } from './IModalConfig';
import { SeDowngradeService } from '../../di';
import {
    ModalRef as FundamentalModalRef,
    ModalService as FundamentalModalService
} from '@fundamental-ngx/core';
import { Injectable } from '@angular/core';
import { IUIBootstrapModalStackService } from './IUIBootstrapModalStackService';
import { IUIBootstrapModalService } from './IUIBootstrapModalService';
import { IAnimateService } from '../interfaces/IAnimateService';
import { IFundamentalModalConfig } from './';
import { ModalService as BaseModalService } from '@smart/utils';
import { UpgradeModule } from '@angular/upgrade/static';

/**
 * @ngdoc service
 * @name modalServiceModule.service:ModalService
 *
 * @description
 * Service responsible for opening the modals after providing configuration
 *
 */

@SeDowngradeService()
@Injectable({ providedIn: 'root' })
export class ModalService extends BaseModalService {
    constructor(
        public fundamentalModalService: FundamentalModalService,
        private upgrade: UpgradeModule
    ) {
        super(fundamentalModalService);
    }

    public hasOpenModals(): boolean {
        return !!this.$uibModalStack.getTop() || this.fundamentalModalService.hasOpenModals();
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalService#dismissAll
     * @methodOf modalServiceModule.service:ModalService
     *
     * @description
     * Dismisses all instances of modals both produced by angular bootstrap ui and Fundamental
     */

    public dismissAll(): void {
        if (this.$uibModalStack.getTop()) {
            this.$uibModalStack.dismissAll();
        }

        if (this.fundamentalModalService.hasOpenModals()) {
            this.fundamentalModalService.dismissAll();
        }
    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalService#open
     * @methodOf modalServiceModule.service:ModalService
     *
     * @description
     * Opens a Fundamental Ngx modal
     *
     * @param {IFundamentalModalConfig} conf {@link modalServiceModule.interface:IFundamentalModalConfig IFundamentalModalConfig}
     *
     * @returns {ModalRef} {@link https://sap.github.io/fundamental-ngx/#/core/modal modal}
     */

    public open<T>(conf: IFundamentalModalConfig<T>): FundamentalModalRef;

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalService#open
     * @methodOf modalServiceModule.service:ModalService
     *
     * @description
     * Open provides a simple way to open modal windows with custom content, that share a common look and feel.
     *
     * The modal window can be closed multiple ways, through {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button actions},
     * by explicitly calling the {@link modalServiceModule.service:ModalManager#methods_close close} or
     * {@link modalServiceModule.service:ModalManager#methods_close dismiss} functions, etc... Depending on how you
     * choose to close a modal, either the modal {@link https://docs.angularjs.org/api/ng/service/$q promise's}
     * {@link https://docs.angularjs.org/api/ng/service/$q successCallback} or {@link https://docs.angularjs.org/api/ng/service/$q errorCallback}
     * will be called. You can use the callbacks to return data from the modal content to the caller of this function.
     *
     * @param {IModalConfig} conf {@link modalServiceModule.interface:IModalConfig IModalConfig}
     *
     * @returns {Promise} {@link https://docs.angularjs.org/api/ng/service/$q promise} that will either be resolved or
     * rejected when the modal window is closed.
     */

    public open(conf: IModalConfig): angular.IPromise<any>;
    public open<T = {}>(
        conf: IFundamentalModalConfig<T> | IModalConfig
    ): FundamentalModalRef | angular.IPromise<any> {
        return (conf as IFundamentalModalConfig<T>).component
            ? super.open(conf as IFundamentalModalConfig<T>)
            : this.angularJSOpen(conf as IModalConfig);
    }

    private angularJSOpen(conf: IModalConfig): angular.IPromise<any> {
        const configuration = conf || ({} as IModalConfig);

        if (configuration.templateUrl && configuration.templateInline) {
            throw new Error('modalService.configuration.errors.2.templates.provided');
        }

        return this.$uibModal.open({
            templateUrl: 'modalTemplate.html',
            size: configuration.size || 'lg',
            backdrop: 'static',
            keyboard: false,
            controller: modalControllerClassFactory(configuration),
            controllerAs: 'modalController',
            windowClass: configuration.cssClasses || null,
            animation: !conf.animation && this.$animate.enabled()
        }).result;
    }

    private get $uibModalStack(): IUIBootstrapModalStackService {
        return this.upgrade.$injector.get('$uibModalStack');
    }

    private get $uibModal(): IUIBootstrapModalService {
        return this.upgrade.$injector.get('$uibModal');
    }

    private get $animate(): IAnimateService {
        return this.upgrade.$injector.get('$animate');
    }
}
