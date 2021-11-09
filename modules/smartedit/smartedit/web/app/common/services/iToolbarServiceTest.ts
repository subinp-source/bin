/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { LogService } from '@smart/utils';
import { IToolbarService, ToolbarItemType } from 'smarteditcommons/services/IToolbarService';
import { IPermissionService } from 'smarteditcommons';
import { promiseHelper } from 'testhelpers';

describe('outer toolbarInterfaceModule', () => {
    let logService: LogService;
    let iToolbarService: IToolbarService;
    let permissionService: jasmine.SpyObj<IPermissionService>;
    let $templateCache: angular.ITemplateCacheService;

    const $q = promiseHelper.$q();

    class ToolbarService extends IToolbarService {}

    beforeEach(() => {
        logService = jasmine.createSpyObj<LogService>('logService', ['error']);
        permissionService = jasmine.createSpyObj<IPermissionService>('permissionService', [
            'isPermitted'
        ]);
        permissionService.isPermitted.and.returnValue($q.when(true));
        $templateCache = jasmine.createSpyObj<angular.ITemplateCacheService>('$templateCache', [
            'get'
        ]);

        iToolbarService = new ToolbarService(logService, $templateCache, permissionService);
    });

    it('ToolbarServiceInterface declares the expected set of empty functions', () => {
        expect(IToolbarService.prototype.addAliases).toBeEmptyFunction();
        expect(IToolbarService.prototype.removeItemByKey).toBeEmptyFunction();
        expect(IToolbarService.prototype.removeAliasByKey).toBeEmptyFunction();
        expect(IToolbarService.prototype.triggerActionOnInner).toBeEmptyFunction();
    });

    it('ToolbarServiceInterface.addItems converts actions into aliases (key-callback mapping of actions) before appending them by means of addAliases', async () => {
        const addAliasesSpy = spyOn(iToolbarService, 'addAliases').and.callThrough();
        spyOn(iToolbarService, 'getAliases').and.callThrough();

        permissionService.isPermitted.and.returnValue($q.when(false));

        const callback1 = () => {
            //
        };
        const callback2 = () => {
            //
        };

        expect(iToolbarService.getAliases()).toEqualData([]);

        // Execution
        await iToolbarService.addItems([
            {
                key: 'key1',
                nameI18nKey: 'somenameI18nKey1',
                descriptionI18nKey: 'somedescriptionI18nKey1',
                callback: callback1,
                icons: ['icons1'],
                type: ToolbarItemType.ACTION,
                include: 'include1'
            }
        ]);

        await iToolbarService.addItems([
            {
                key: 'key2',
                nameI18nKey: 'somenameI18nKey2',
                descriptionI18nKey: 'somedescriptionI18nKey2',
                callback: callback2,
                icons: ['icons2'],
                type: ToolbarItemType.TEMPLATE,
                include: 'include2'
            }
        ]);

        // Tests
        expect(addAliasesSpy.calls.argsFor(0)[0]).toEqualData([
            {
                key: 'key1',
                name: 'somenameI18nKey1',
                description: 'somedescriptionI18nKey1',
                icons: ['icons1'],
                type: ToolbarItemType.ACTION,
                include: 'include1',
                priority: 500,
                section: 'left',
                isOpen: false,
                keepAliveOnClose: false,
                isPermissionGranted: true
            }
        ]);

        expect(iToolbarService.getActions()).toEqualData({
            key1: callback1
        });

        expect(addAliasesSpy.calls.argsFor(1)[0]).toEqualData([
            {
                key: 'key2',
                name: 'somenameI18nKey2',
                description: 'somedescriptionI18nKey2',
                icons: ['icons2'],
                type: ToolbarItemType.TEMPLATE,
                include: 'include2',
                priority: 500,
                section: 'left',
                isOpen: false,
                keepAliveOnClose: false,
                isPermissionGranted: true
            }
        ]);

        expect(iToolbarService.getActions()).toEqualData({
            key1: callback1,
            key2: callback2
        });
    });

    it('ToolbarServiceInterface.addItems with permissions property', async () => {
        const addAliasesSpy = spyOn(iToolbarService, 'addAliases').and.callThrough();
        spyOn(iToolbarService, 'getAliases').and.callThrough();

        permissionService.isPermitted.and.returnValue($q.when(false));

        const callback1 = () => {
            //
        };

        expect(iToolbarService.getAliases()).toEqualData([]);

        await iToolbarService.addItems([
            {
                key: 'key1',
                nameI18nKey: 'somenameI18nKey1',
                descriptionI18nKey: 'somedescriptionI18nKey1',
                callback: callback1,
                icons: ['icons1'],
                type: ToolbarItemType.ACTION,
                include: 'include1',
                permissions: ['xyz.permissions']
            }
        ]);

        // Tests
        expect(addAliasesSpy.calls.argsFor(0)[0]).toEqualData([
            {
                key: 'key1',
                name: 'somenameI18nKey1',
                description: 'somedescriptionI18nKey1',
                icons: ['icons1'],
                type: ToolbarItemType.ACTION,
                include: 'include1',
                priority: 500,
                section: 'left',
                isOpen: false,
                keepAliveOnClose: false,
                isPermissionGranted: false,
                permissions: ['xyz.permissions']
            }
        ]);
    });

    it('addItems logs an error when key is not provided in the configuration', async () => {
        const addAliasesSpy = spyOn(iToolbarService, 'addAliases');

        const callbacks = {
            callback1() {
                //
            }
        };

        // Act
        await iToolbarService.addItems([
            {
                key: null,
                type: ToolbarItemType.ACTION,
                callback: callbacks.callback1
            }
        ]);

        // Assert
        expect(addAliasesSpy).not.toHaveBeenCalled();
        expect(logService.error).toHaveBeenCalledWith('addItems() - Cannot add item without key.');
    });
});
