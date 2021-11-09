/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxy, IPermissionService, LogService, ToolbarItemType } from 'smarteditcommons';
import { ToolbarService } from 'smartedit/services/ToolbarService';
import { ToolbarServiceFactory } from 'smartedit/services/ToolbarServiceFactory';
import { mockAngularJSLazyDependenciesService, promiseHelper } from 'testhelpers';

describe('test inner toolbarService Module', () => {
    let logService: LogService;
    let toolbarService: ToolbarService;
    let permissionService: jasmine.SpyObj<IPermissionService>;
    let gatewayProxy: GatewayProxy;
    let toolbarServiceFactory: ToolbarServiceFactory;

    const $q = promiseHelper.$q();

    beforeEach(() => {
        logService = jasmine.createSpyObj<LogService>('logService', ['error', 'warn']);
        permissionService = jasmine.createSpyObj<IPermissionService>('permissionService', [
            'isPermitted'
        ]);
        permissionService.isPermitted.and.returnValue($q.when(true));

        gatewayProxy = jasmine.createSpyObj<GatewayProxy>('gatewayProxy', ['initForService']);

        toolbarServiceFactory = new ToolbarServiceFactory(
            gatewayProxy,
            logService,
            mockAngularJSLazyDependenciesService(),
            permissionService
        );
    });

    it('factory called twice on the same toolbar name returns the same instance', () => {
        expect(toolbarServiceFactory.getToolbarService('toolBar1')).toBe(
            toolbarServiceFactory.getToolbarService('toolBar1')
        );
    });

    it('factory called twice on different toolbar names returns different instances', () => {
        expect(toolbarServiceFactory.getToolbarService('toolBar1')).not.toBe(
            toolbarServiceFactory.getToolbarService('toolBar2')
        );
    });

    it('on first acquisiion of a new ToolbarServiceInstance, it is registered with the gateway proxy', () => {
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(toolbarService, [
            'addAliases',
            'removeItemByKey',
            'removeAliasByKey',
            '_removeItemOnInner',
            'triggerActionOnInner'
        ]);
    });

    it('addAliases is an empty function', () => {
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        expect(toolbarService.addAliases).toBeEmptyFunction();
    });

    it('removeAliasByKey is an empty function', () => {
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        expect(toolbarService.removeAliasByKey).toBeEmptyFunction();
    });

    it('triggerActionOnInner will call a callback associated with a given key', async () => {
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');

        const callbacks = {
            callback1: () => {
                //
            }
        };

        spyOn(callbacks, 'callback1');

        await toolbarService.addItems([
            {
                key: 'key1',
                type: ToolbarItemType.ACTION,
                callback: callbacks.callback1
            }
        ]);

        toolbarService.triggerActionOnInner({
            key: 'key1'
        });

        expect(callbacks.callback1).toHaveBeenCalled();
    });

    it('triggerActionOnInner will log an error if an action does not exist for the given key', () => {
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');

        toolbarService.triggerActionOnInner({
            key: 'nonExistentKey'
        });

        expect(logService.error).toHaveBeenCalledWith(
            'triggerActionByKey() - Failed to find action for key nonExistentKey'
        );
    });

    it('_removeItemOnInner logs an error when key is not found', () => {
        // Arrange
        const invalidKey = 'some Invalid Key';
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');

        // Act
        toolbarService._removeItemOnInner(invalidKey);

        // Assert
        expect(logService.warn).toHaveBeenCalledWith(
            'removeItemByKey() - Failed to find action for key ' + invalidKey
        );
    });

    it('_removeItemOnInner removes the action', async () => {
        // Arrange
        const key = 'someKey';
        toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');

        await toolbarService.addItems([
            {
                key,
                type: ToolbarItemType.ACTION
            }
        ]);
        expect(key in toolbarService.getActions()).toBe(true);
        // Act
        toolbarService._removeItemOnInner(key);

        // Assert
        expect(key in toolbarService.getActions()).toBe(false);
    });
});
