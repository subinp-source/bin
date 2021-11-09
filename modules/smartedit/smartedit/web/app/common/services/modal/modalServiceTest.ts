/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import * as angular from 'angular';
import * as uib from 'angular-ui-bootstrap';
import { ModalService as FundamentalModalService } from '@fundamental-ngx/core';

import { ModalService } from './ModalService';
import { promiseHelper } from '../../../../../smartedit-build/test/unit';
import { IModalConfig } from './IModalConfig';
import { SeConstructor } from '../../di';
import { IModalController } from './modalControllerClassFactory';
import { IAnimateService } from '../interfaces/IAnimateService';
import { Type } from '@angular/core';
import { IFundamentalModalConfig } from './';
import { ModalService as BaseModalService } from '@smart/utils';
import { UpgradeModule } from '@angular/upgrade/static';
import { stringUtils } from 'smarteditcommons/utils/StringUtils';

function getScope(): angular.IScope {
    return {} as angular.IScope;
}

describe('ModalService', function() {
    let $uibModal: jasmine.SpyObj<uib.IModalService>;
    let $uibModalStackService: jasmine.SpyObj<uib.IModalStackService>;
    let upgrade: jasmine.SpyObj<UpgradeModule>;
    let injector: jasmine.SpyObj<any>;
    let $controllerHolder: jasmine.SpyObj<{ $controller: angular.IControllerService }>;
    let modalService: ModalService;
    let $translate: jasmine.Spy;
    let $templateCache: angular.ITemplateCacheService;
    let $uibModalInstance: jasmine.SpyObj<uib.IModalInstanceService>;
    let $animate: jasmine.SpyObj<IAnimateService>;
    let $fundamentalModal: jasmine.SpyObj<FundamentalModalService>;
    const result: angular.IPromise<void> = promiseHelper.$q().resolve();

    beforeEach(() => {
        $animate = jasmine.createSpyObj('$animate', ['enabled']);

        $fundamentalModal = jasmine.createSpyObj('$fundamentalModal', [
            'open',
            'dismissAll',
            'hasOpenModals'
        ]);
        $fundamentalModal.hasOpenModals.and.returnValue(true);

        $uibModal = jasmine.createSpyObj('$uibModal', ['open']);
        $uibModal.open.and.returnValue({ result });

        $uibModalStackService = jasmine.createSpyObj('$uibModalStackSevice', [
            'dismissAll',
            'getTop'
        ]);
        $uibModalStackService.getTop.and.returnValue(true);

        $uibModalInstance = jasmine.createSpyObj('$uibModalInstance', ['dismiss', 'close']);

        $translate = jasmine.createSpy('$translate');

        $controllerHolder = jasmine.createSpyObj('$controllerHolder', ['$controller']);

        $templateCache = jasmine.createSpyObj('$templateCache', ['put', 'remove']);

        injector = jasmine.createSpyObj('injector', ['get']);
        upgrade = jasmine.createSpyObj('upgrade', ['$injector']);
        upgrade.$injector = injector;

        injector.get.and.callFake((key: string) => {
            switch (key) {
                case '$animate':
                    return $animate;
                case '$uibModalStack':
                    return $uibModalStackService;
                default:
                    return $uibModal;
            }
        });

        modalService = new ModalService($fundamentalModal, upgrade);
    });

    it('will throw exception when both templateUrl and template provided', function() {
        expect(function() {
            return modalService.open({
                templateUrl: 'sometemplateurl',
                templateInline: 'sometemplate'
            });
        }).toThrow(new Error('modalService.configuration.errors.2.templates.provided'));
    });

    it('open with just templateUrl or templateInline will call $uibModal.open with expected configuration', function() {
        expect(
            modalService.open({
                templateUrl: 'someURL'
            })
        ).toBe(result);

        expect($uibModal.open).toHaveBeenCalled();

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];

        expect(modalConfiguration).toEqual(
            jasmine.objectContaining({
                templateUrl: 'modalTemplate.html',
                size: 'lg'
            })
        );
    });

    it('open with templateUrl and cssClasses will call $uibModal.open with expected configuration', function() {
        expect(
            modalService.open({
                templateUrl: 'someURL',
                cssClasses: 'class1 class2 class3'
            })
        ).toBe(result);

        expect($uibModal.open).toHaveBeenCalled();

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];

        expect(modalConfiguration).toEqual(
            jasmine.objectContaining({
                templateUrl: 'modalTemplate.html',
                size: 'lg',
                windowClass: 'class1 class2 class3'
            })
        );
    });

    it('when no controller is passed default controller is set : properties, cancel and close functions', function() {
        expect(
            modalService.open({
                templateUrl: 'someURL'
            })
        ).toBe(result);

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect(controller.templateUrl).toBe('someURL');
        expect(controller.templateInline).toBeUndefined();

        expect($uibModalInstance.dismiss).not.toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.dismiss();
        expect($uibModalInstance.dismiss).toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.close();
        expect($uibModalInstance.close).toHaveBeenCalled();

        expect($controllerHolder.$controller).not.toHaveBeenCalled();
    });

    it('when a controller ID is passed, $controller instantiates the controller and sets it with properties, cancel and close functions', function() {
        expect(
            modalService.open({
                templateUrl: 'someURL',
                controller: 'controllerID'
            })
        ).toBe(result);

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect(controller.templateUrl).toBe('someURL');
        expect(controller.templateInline).toBeUndefined();

        expect($uibModalInstance.dismiss).not.toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.dismiss();
        expect($uibModalInstance.dismiss).toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.close();
        expect($uibModalInstance.close).toHaveBeenCalled();

        expect($controllerHolder.$controller).toHaveBeenCalledWith('controllerID', {
            $scope,
            modalManager: controller._modalManager
        });
    });

    it('when a controller function is passed, $controller executes the controller and sets it with properties, cancel and close functions', function() {
        function MockController() {
            //
        }

        const configuration: IModalConfig = {
            templateUrl: 'someURL',
            controller: MockController
        };

        const spy: jasmine.Spy = spyOn(configuration, 'controller').and.returnValue(null);

        expect(modalService.open(configuration)).toBe(result);

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect(controller.templateUrl).toBe('someURL');
        expect(controller.templateInline).toBeUndefined();

        expect($uibModalInstance.dismiss).not.toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.dismiss();
        expect($uibModalInstance.dismiss).toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.close();
        expect($uibModalInstance.close).toHaveBeenCalled();

        expect($controllerHolder.$controller).toHaveBeenCalledWith(spy, {
            $scope,
            modalManager: controller._modalManager
        });
    });

    it('open with inline template will call $uibModal.open with expected configuration and will register template in the cache', function() {
        const inlineTemplate: string = 'sometemplate';

        expect(
            modalService.open({
                templateInline: inlineTemplate
            })
        ).toBe(result);

        expect($uibModal.open).toHaveBeenCalled();

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect(controller.templateUrl).toContain('modalTemplateKey');
        expect($templateCache.put).toHaveBeenCalledWith(controller.templateUrl, inlineTemplate);
    });

    it('when a template url is passed, $controller does not add anything to the template cache', function() {
        expect(
            modalService.open({
                templateUrl: 'someURL'
            })
        ).toBe(result);

        expect($uibModal.open).toHaveBeenCalled();

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect($templateCache.put).not.toHaveBeenCalledWith(
            controller.templateUrl,
            jasmine.any(String)
        );
    });

    it('when an inline template is passed, $controller clears the templateCache entries after a cancel operation', function() {
        const templateInline: string = 'sometemplate';
        expect(
            modalService.open({
                templateInline
            })
        ).toBe(result);

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect($uibModalInstance.dismiss).not.toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.dismiss();
        expect($templateCache.remove).toHaveBeenCalledWith(controller.templateUrl);
        expect($uibModalInstance.dismiss).toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
    });

    it('when an inline template is passed, $controller clears the templateCache entries after a close operation', function() {
        const templateInline: string = 'sometemplate';
        expect(
            modalService.open({
                templateInline
            })
        ).toBe(result);

        const modalConfiguration: IModalConfig = $uibModal.open.calls.argsFor(0)[0];
        const $scope: angular.IScope = getScope();

        const controllerConstructor: SeConstructor<
            IModalController
        > = modalConfiguration.controller as SeConstructor<IModalController>;
        const controller: IModalController = new controllerConstructor(
            $scope,
            $uibModalInstance,
            $translate,
            $controllerHolder.$controller,
            $templateCache,
            stringUtils
        );

        expect($uibModalInstance.dismiss).not.toHaveBeenCalled();
        expect($uibModalInstance.close).not.toHaveBeenCalled();
        controller.close();
        expect($templateCache.remove).toHaveBeenCalledWith(controller.templateUrl);
        expect($uibModalInstance.dismiss).not.toHaveBeenCalled();
        expect($uibModalInstance.close).toHaveBeenCalled();
    });

    // Dismiss all

    it('calls dismissAll on both uib service and fundamental service', () => {
        modalService.dismissAll();

        expect($uibModalStackService.dismissAll).toHaveBeenCalled();
        expect($fundamentalModal.dismissAll).toHaveBeenCalled();
    });

    it('calls dismissAll on uib service', () => {
        $fundamentalModal.hasOpenModals.and.returnValue(false);

        modalService.dismissAll();

        expect($uibModalStackService.dismissAll).toHaveBeenCalled();
        expect($fundamentalModal.dismissAll).not.toHaveBeenCalled();
    });

    it('calls dismissAll on fundamental modal sevice', () => {
        $uibModalStackService.getTop.and.returnValue(false);

        modalService.dismissAll();

        expect($uibModalStackService.dismissAll).not.toHaveBeenCalled();
        expect($fundamentalModal.dismissAll).toHaveBeenCalled();
    });

    it('doesnt call dismissAll on neither fundamental or uib', () => {
        $uibModalStackService.getTop.and.returnValue(false);
        $fundamentalModal.hasOpenModals.and.returnValue(false);

        modalService.dismissAll();

        expect($uibModalStackService.dismissAll).not.toHaveBeenCalled();
        expect($fundamentalModal.dismissAll).not.toHaveBeenCalled();
    });

    // Open overload

    it('calls uib service when no component is provided in dto', () => {
        const config: IModalConfig = { templateUrl: 'some-path' };

        modalService.open(config);

        expect($uibModal.open).toHaveBeenCalled();
    });

    it('calls fundamental service when component is provided in dto', () => {
        const config: IFundamentalModalConfig<null> = {
            component: {} as Type<any>,
            config: {}
        };

        spyOn(BaseModalService.prototype, 'open');

        modalService.open(config);

        expect(BaseModalService.prototype.open).toHaveBeenCalled();
    });
});
