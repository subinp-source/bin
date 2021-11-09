/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FeatureService, PerspectiveService, StorageService } from 'smarteditcontainer/services';

import {
    annotationService,
    ALL_PERSPECTIVE,
    CrossFrameEventService,
    EVENT_PERSPECTIVE_ADDED,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_UPDATED,
    EVENTS,
    GatewayProxied,
    IPerspective,
    IWaitDialogService,
    LogService,
    NONE_PERSPECTIVE,
    PERSPECTIVE_SELECTOR_WIDGET_KEY,
    SystemEventService
} from 'smarteditcommons';

import { UpgradeModule } from '@angular/upgrade/static';

describe('outer perspectiveService', () => {
    const upgrade = {
        $injector: jasmine.createSpyObj('$injector', ['get'])
    } as UpgradeModule;
    const $rootScope = jasmine.createSpyObj<angular.IRootScopeService>('$rootScope', ['$on']);

    upgrade.$injector.get.and.returnValue($rootScope);

    const $location = jasmine.createSpyObj<angular.ILocationService>('$location', ['path']);
    const $log: jasmine.SpyObj<LogService> = jasmine.createSpyObj('log', [
        'error',
        'debug',
        'warn'
    ]);
    const systemEventService: jasmine.SpyObj<SystemEventService> = jasmine.createSpyObj(
        'systemEventService',
        ['publishAsync', 'subscribe']
    );
    const waitDialogService: jasmine.SpyObj<IWaitDialogService> = jasmine.createSpyObj(
        'waitDialogService',
        ['showWaitModal', 'hideWaitModal']
    );
    const featureService: jasmine.SpyObj<FeatureService> = jasmine.createSpyObj('featureService', [
        'enable',
        'disable',
        'getFeatureKeys',
        'getFeatureProperty'
    ]);
    const storageService: jasmine.SpyObj<StorageService> = jasmine.createSpyObj('storageService', [
        'getValueFromLocalStorage',
        'setValueInLocalStorage'
    ]);
    const crossFrameEventService: jasmine.SpyObj<CrossFrameEventService> = jasmine.createSpyObj(
        'crossFrameEventService',
        ['publish', 'subscribe']
    );
    const permissionService = jasmine.createSpyObj('permissionService', ['isPermitted']);

    let perspectiveService: PerspectiveService;

    beforeEach(() => {
        systemEventService.subscribe.calls.reset();
        featureService.enable.calls.reset();
        crossFrameEventService.publish.calls.reset();
        crossFrameEventService.subscribe.calls.reset();
        featureService.disable.calls.reset();

        perspectiveService = new PerspectiveService(
            upgrade,
            $location,
            $log,
            systemEventService,
            featureService,
            waitDialogService,
            storageService,
            crossFrameEventService,
            permissionService
        );
    });

    /*
     * Default returns values for:
     * featureService
     * permissionService
     */
    beforeEach(() => {
        featureService.getFeatureKeys.and.returnValue([]);
        featureService.getFeatureProperty.and.returnValue(Promise.resolve([]));
        permissionService.isPermitted.and.returnValue(Promise.resolve(true));
    });

    describe('initialization', () => {
        it('checks GatewayProxied', () => {
            expect(
                annotationService.getClassAnnotation(PerspectiveService, GatewayProxied)
            ).toEqual([
                'register',
                'switchTo',
                'hasActivePerspective',
                'isEmptyPerspectiveActive',
                'selectDefault',
                'refreshPerspective',
                'getActivePerspectiveKey',
                'isHotkeyEnabledForActivePerspective'
            ]);
        });

        it('initializes', () => {
            expect(systemEventService.subscribe.calls.count()).toBe(1);
            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                EVENTS.LOGOUT,
                jasmine.any(Function)
            );

            expect(crossFrameEventService.subscribe.calls.count()).toBe(1);
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                EVENTS.USER_HAS_CHANGED,
                jasmine.any(Function)
            );
        });

        it('_registerEventHandlers will register event handlers', () => {
            // GIVEN
            spyOn(perspectiveService as any, '_registerEventHandlers').and.callThrough();
            spyOn(perspectiveService as any, '_clearPerspectiveFeatures').and.callThrough();
            systemEventService.subscribe.calls.reset();
            crossFrameEventService.subscribe.calls.reset();
            $rootScope.$on.calls.reset();

            // WHEN
            (perspectiveService as any)._registerEventHandlers();

            // THEN
            expect(systemEventService.subscribe.calls.count()).toBe(1);
            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                EVENTS.LOGOUT,
                jasmine.any(Function)
            );

            expect(crossFrameEventService.subscribe.calls.count()).toBe(1);
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                EVENTS.USER_HAS_CHANGED,
                jasmine.any(Function)
            );

            expect($rootScope.$on.calls.count()).toBe(1);
            expect($rootScope.$on).toHaveBeenCalledWith(
                '$routeChangeSuccess',
                jasmine.any(Function)
            );

            const logoutCallback = systemEventService.subscribe.calls.argsFor(0)[1];
            logoutCallback();

            const clearCallback = crossFrameEventService.subscribe.calls.argsFor(0)[1];
            clearCallback();

            const routeChangeCallback = $rootScope.$on.calls.argsFor(0)[1];
            routeChangeCallback();

            expect((perspectiveService as any)._clearPerspectiveFeatures).toHaveBeenCalled();
        });

        it('GIVEN perspectives are initialized', async (done) => {
            // WHEN
            const perspectives = await getPerspectives();

            const nonePerspective = findPerspective(perspectives, NONE_PERSPECTIVE);
            const allPerspective = findPerspective(perspectives, ALL_PERSPECTIVE);

            // THEN
            expect(perspectives.length).toBe(2);
            expect(nonePerspective.nameI18nKey).toBe('se.perspective.none.name');
            expect(allPerspective.nameI18nKey).toBe('se.perspective.all.name');

            done();
        });
    });

    describe('register', () => {
        it('throws error if key is not provided', () => {
            expect(() => {
                perspectiveService.register({
                    nameI18nKey: 'somenameI18nKey',
                    features: ['abc', 'xyz']
                } as IPerspective);
            }).toThrowError('perspectiveService.configuration.key.error.required');
        });

        it('throws error if nameI18nKey is not provided', () => {
            expect(() => {
                perspectiveService.register({
                    key: 'somekey',
                    features: ['abc', 'xyz']
                } as IPerspective);
            }).toThrowError('perspectiveService.configuration.nameI18nKey.error.required');
        });

        it('throws error if features is not provided and perspective is neither se.none nor se.all', () => {
            expect(() => {
                perspectiveService.register({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey'
                } as IPerspective);
            }).toThrowError('perspectiveService.configuration.features.error.required');
        });

        it('throws error is features is empty', () => {
            expect(() => {
                perspectiveService.register({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey',
                    features: []
                } as IPerspective);
            }).toThrowError('perspectiveService.configuration.features.error.required');
        });

        it(
            'is successful when perspective key is ' +
                NONE_PERSPECTIVE +
                ' and features are not provided',
            async (done) => {
                // GIVEN
                const configuration = {
                    key: NONE_PERSPECTIVE,
                    nameI18nKey: 'somenameI18nKey'
                } as IPerspective;

                // WHEN
                await perspectiveService.register(configuration);
                const perspective = await getPerspective(configuration.key);

                // THEN
                expect(perspective).toBeDefined();

                done();
            }
        );

        it(
            'is successful when perspective key is ' +
                ALL_PERSPECTIVE +
                ' and features are not provided',
            async (done) => {
                // GIVEN
                const configuration = {
                    key: ALL_PERSPECTIVE,
                    nameI18nKey: 'somenameI18nKey'
                } as IPerspective;

                // WHEN
                await perspectiveService.register(configuration);

                const perspective = await getPerspective(configuration.key);

                // THEN
                expect(perspective).toBeTruthy();

                done();
            }
        );

        it('GIVEN that perspective configuration has features, THEN register pushes to the features list a Perspective instantiated from configuration and sends a notification', async (done) => {
            // GIVEN
            await perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1', 'persp2']
            } as IPerspective);

            expect(systemEventService.publishAsync).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);

            // WHEN
            const perspectives = await getPerspectives();

            // THEN Expect to have 2 default + 1 registered
            expect(perspectives.length).toBe(3);
            const perspective = perspectives[2];

            expect(perspective).toEqual(
                jasmine.objectContaining({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey',
                    features: [PERSPECTIVE_SELECTOR_WIDGET_KEY, 'abc', 'xyz'],
                    perspectives: ['persp1', 'persp2'],
                    descriptionI18nKey: 'somedescriptionI18nKey'
                })
            );

            done();
        });

        it('should send EVENT_PERSPECTIVE_ADDED event if register is called the first time and EVENT_PERSPECTIVE_UPDATED is register is called the second time with the same key', async (done) => {
            // WHEN
            await perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1', 'persp2']
            });

            // THEN
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);

            // WHEN
            await perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['def']
            });
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(EVENT_PERSPECTIVE_UPDATED);

            done();
        });

        it('does not override existing perspectives but merges features and nested perspectives', async (done) => {
            // GIVEN
            await perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                perspectives: ['persp1', 'persp2']
            });

            await perspectiveService.register({
                key: 'somekey3',
                nameI18nKey: 'somenameI18nKey3',
                descriptionI18nKey: 'somedescriptionI18nKey3',
                features: ['zzz'],
                perspectives: ['xxx']
            });

            await perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey2',
                descriptionI18nKey: 'somedescriptionI18nKey2',
                features: ['xyz', 'def'],
                perspectives: ['persp2', 'persp3']
            });

            expect(systemEventService.publishAsync).toHaveBeenCalledWith(EVENT_PERSPECTIVE_ADDED);

            // WHEN
            const perspectives = await getPerspectives();
            const res1 = findPerspective(perspectives, 'somekey');
            const res2 = findPerspective(perspectives, 'somekey3');

            // THEN
            expect(res1).toEqual(
                jasmine.objectContaining({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey',
                    features: [PERSPECTIVE_SELECTOR_WIDGET_KEY, 'abc', 'xyz', 'def'],
                    perspectives: ['persp1', 'persp2', 'persp3'],
                    descriptionI18nKey: 'somedescriptionI18nKey'
                })
            );

            expect(res2).toEqual(
                jasmine.objectContaining({
                    key: 'somekey3',
                    nameI18nKey: 'somenameI18nKey3',
                    features: [PERSPECTIVE_SELECTOR_WIDGET_KEY, 'zzz'],
                    perspectives: ['xxx'],
                    descriptionI18nKey: 'somedescriptionI18nKey3'
                })
            );

            done();
        });

        it(
            'adds a perspective with its permissions and sends an ' +
                EVENT_PERSPECTIVE_ADDED +
                ' event',
            async (done) => {
                // GIVEN
                const configuration = {
                    key: 'perspectiveKey',
                    nameI18nKey: 'somenameI18nKey',
                    descriptionI18nKey: 'somedescriptionI18nKey',
                    features: ['abc', 'xyz'],
                    perspectives: ['persp1'],
                    permissions: ['permission1', 'permission2']
                };

                permissionService.isPermitted.and.returnValue(Promise.resolve(true));

                // WHEN
                await perspectiveService.register(configuration);

                const perspective = await getPerspective(configuration.key);

                // THEN
                expect(perspective.permissions).toEqual(configuration.permissions);
                expect(systemEventService.publishAsync).toHaveBeenCalledWith(
                    EVENT_PERSPECTIVE_ADDED
                );

                done();
            }
        );

        it(
            'does not override existing perspectives but merges permissions and sends an ' +
                EVENT_PERSPECTIVE_ADDED +
                ' event',
            async (done) => {
                // GIVEN
                const configuration1 = {
                    key: 'perspectiveKey',
                    nameI18nKey: 'somenameI18nKey',
                    descriptionI18nKey: 'somedescriptionI18nKey',
                    features: ['abc', 'xyz'],
                    perspectives: ['persp1'],
                    permissions: ['permission1', 'permission2']
                };

                const configuration2 = {
                    key: 'perspectiveKey',
                    nameI18nKey: 'somenameI18nKey2',
                    descriptionI18nKey: 'somedescriptionI18nKey',
                    features: ['abc', 'xyz'],
                    perspectives: ['persp1'],
                    permissions: ['permission2', 'permission3']
                };

                permissionService.isPermitted.and.returnValue(Promise.resolve(true));

                // WHEN
                await perspectiveService.register(configuration1);
                await perspectiveService.register(configuration2);
                const perspective = await getPerspective(configuration1.key);

                // THEN
                expect(perspective.permissions).toEqual([
                    'permission1',
                    'permission2',
                    'permission3'
                ]);
                expect(systemEventService.publishAsync).toHaveBeenCalledWith(
                    EVENT_PERSPECTIVE_ADDED
                );

                done();
            }
        );
    });

    describe('_fetchAllFeatures', () => {
        it('collects all features of nested perspectives, returns a set of unique features, and log message if a nested perspective does not exist', async (done) => {
            // GIVEN
            const perspective1 = {
                key: 'persp1',
                nameI18nKey: 'persp1',
                features: ['feat1', 'feat2'],
                perspectives: ['persp2']
            };
            const perspective2 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3'],
                perspectives: ['persp3', 'persp4']
            };
            const perspective4 = {
                key: 'persp4',
                nameI18nKey: 'persp4',
                features: ['feat3', 'feat4'],
                perspectives: [] as string[]
            };
            await perspectiveService.register(perspective1);
            await perspectiveService.register(perspective2);
            await perspectiveService.register(perspective4);

            const holder: string[] = [];

            // WHEN
            (perspectiveService as any)._fetchAllFeatures(perspective1, holder);

            // THEN
            expect(holder).toEqual([
                PERSPECTIVE_SELECTOR_WIDGET_KEY,
                'feat1',
                'feat2',
                'feat3',
                'feat4'
            ]);
            expect($log.debug).toHaveBeenCalledWith(
                'nested perspective persp3 was not found in the registry'
            );
            done();
        });
    });

    describe('refreshPerspective', () => {
        it('should select the default perspective if there is no active perspective', (done) => {
            // GIVEN
            spyOn(perspectiveService, 'switchTo').and.returnValue(Promise.resolve());
            storageService.getValueFromLocalStorage.and.returnValue(Promise.resolve(null));

            expect(perspectiveService.getActivePerspective()).toBeNull();

            // WHEN
            const result = perspectiveService.refreshPerspective();

            // THEN

            result.then(() => {
                expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
                expect(storageService.getValueFromLocalStorage).toHaveBeenCalledWith(
                    'smartedit-perspectives',
                    true
                );
                done();
            });
        });

        it('will publish a perspective refreshed event after a successful refreshPerspective', async (done) => {
            // GIVEN
            const perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2']
            };

            await perspectiveService.register(perspective0);
            await perspectiveService.switchTo(perspective0.key);

            expect(featureService.enable.calls.count()).toBe(3);
            expect(featureService.enable.calls.argsFor(1)).toEqual(['feat0']);
            expect(featureService.enable.calls.argsFor(2)).toEqual(['feat2']);
            expect(featureService.disable).not.toHaveBeenCalled();

            // WHEN
            featureService.enable.calls.reset();
            featureService.disable.calls.reset();
            await perspectiveService.refreshPerspective();

            // THEN
            expect(featureService.enable.calls.count()).toBe(3);
            expect(featureService.enable.calls.argsFor(1)).toEqual(['feat0']);
            expect(featureService.enable.calls.argsFor(2)).toEqual(['feat2']);
            expect(featureService.disable).not.toHaveBeenCalled();

            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                'EVENT_PERSPECTIVE_REFRESHED',
                true
            );

            done();
        });
    });

    describe('refreshPerspective - not permitted perspective', () => {
        it('WILL select NONE_PERSPECTIVE if the active perspective is not permitted', async (done) => {
            const PERMISSION1 = 'permission1';

            // GIVEN
            const perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2'],
                permissions: [PERMISSION1]
            };

            await perspectiveService.register(perspective0);
            await perspectiveService.switchTo(perspective0.key);

            permissionService.isPermitted.and.callFake(() => {
                return Promise.resolve(false);
            });

            featureService.disable.calls.reset();
            featureService.enable.calls.reset();
            featureService.getFeatureProperty.calls.reset();

            // WHEN
            const result = perspectiveService.refreshPerspective();

            result.then(() => {
                expect(featureService.enable).not.toHaveBeenCalled();
                expect(featureService.disable.calls.count()).toBeGreaterThanOrEqual(2);
                expect(featureService.disable.calls.allArgs()).toContain(['feat0']);
                expect(featureService.disable.calls.allArgs()).toContain(['feat2']);
                done();
            });
        });
    });

    describe('switchTo', () => {
        beforeEach(() => {
            storageService.setValueInLocalStorage.calls.reset();
            waitDialogService.showWaitModal.calls.reset();
            waitDialogService.hideWaitModal.calls.reset();
            featureService.enable.calls.reset();
            featureService.disable.calls.reset();
        });

        it('WILL silently do nothing if trying to switch to the same perspecitve as the activated one', async (done) => {
            // GIVEN
            await perspectiveService.register({
                key: 'aperspective',
                nameI18nKey: 'perspective.none.name',
                descriptionI18nKey: 'perspective.none.description',
                features: ['fakeFeature']
            });

            // WHEN
            await perspectiveService.switchTo('aperspective');
            await perspectiveService.switchTo('aperspective');

            // THEN
            expect(storageService.setValueInLocalStorage).toHaveBeenCalledTimes(1);
            expect(waitDialogService.showWaitModal).toHaveBeenCalledTimes(1);
            expect(featureService.enable).toHaveBeenCalledTimes(2);
            expect(featureService.enable.calls.argsFor(0)).toEqual([
                PERSPECTIVE_SELECTOR_WIDGET_KEY
            ]);
            expect(featureService.enable.calls.argsFor(1)).toEqual(['fakeFeature']);
            expect(featureService.disable).not.toHaveBeenCalled();
            done();
        });

        it('WILL throw an error if required perspective is not found', () => {
            // GIVEN
            spyOn(perspectiveService as any, '_findByKey').and.returnValue(null);

            // WHEN/THEN
            expect(() => {
                perspectiveService.switchTo('aperspective');
            }).toThrowError("switchTo() - Couldn't find perspective with key aperspective");

            expect((perspectiveService as any)._findByKey).toHaveBeenCalledWith('aperspective');
            expect(storageService.setValueInLocalStorage).not.toHaveBeenCalled();
            expect(waitDialogService.showWaitModal).not.toHaveBeenCalled();
            expect(featureService.enable).not.toHaveBeenCalled();
            expect(featureService.disable).not.toHaveBeenCalled();
        });

        it('NONE_PERSPECTIVE WILL publish a rerender/false and hide the wait modal', async (done) => {
            // WHEN
            await perspectiveService.switchTo(NONE_PERSPECTIVE);

            // THEN
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(waitDialogService.hideWaitModal).toHaveBeenCalled();

            done();
        });

        it('WILL activate all (nested) features of a perspective and notify to rerender', async (done) => {
            // GIVEN
            const perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0']
            };
            const perspective1 = {
                key: 'persp1',
                nameI18nKey: 'persp1',
                features: ['feat1', 'feat2'],
                perspectives: ['persp2']
            };
            const perspective2 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3'],
                perspectives: ['persp3', 'persp4']
            };
            const perspective4 = {
                key: 'persp4',
                nameI18nKey: 'persp4',
                features: ['feat3', 'feat4'],
                perspectives: [] as string[]
            };
            await perspectiveService.register(perspective0);
            await perspectiveService.register(perspective1);
            await perspectiveService.register(perspective2);
            await perspectiveService.register(perspective4);

            // WHEN
            await perspectiveService.switchTo('persp1');

            // THEN
            expect(storageService.setValueInLocalStorage).toHaveBeenCalledWith(
                'smartedit-perspectives',
                'persp1',
                true
            );
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(featureService.enable.calls.count()).toBe(5);
            expect(featureService.enable.calls.argsFor(1)).toEqual(['feat1']);
            expect(featureService.enable.calls.argsFor(2)).toEqual(['feat2']);
            expect(featureService.enable.calls.argsFor(3)).toEqual(['feat3']);
            expect(featureService.enable.calls.argsFor(4)).toEqual(['feat4']);
            expect(waitDialogService.hideWaitModal).not.toHaveBeenCalled();

            done();
        });

        it('WILL disable features of previous perspective not present in new one and activate features of new perspective not present in previous one', async (done) => {
            // GIVEN
            const perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2', 'feat3']
            };

            const perspective1 = {
                key: 'persp1',
                nameI18nKey: 'persp1',
                features: ['feat1', 'feat2'],
                perspectives: ['persp2']
            };
            const perspective2 = {
                key: 'persp2',
                nameI18nKey: 'persp2',
                features: ['feat2', 'feat3'],
                perspectives: ['persp3', 'persp4']
            };
            const perspective4 = {
                key: 'persp4',
                nameI18nKey: 'persp4',
                features: ['feat3', 'feat4'],
                perspectives: [] as string[]
            };
            await perspectiveService.register(perspective0);
            await perspectiveService.register(perspective1);
            await perspectiveService.register(perspective2);
            await perspectiveService.register(perspective4);

            // WHEN
            await perspectiveService.switchTo('persp0');
            await perspectiveService.switchTo('persp1');

            // THEN
            expect(storageService.setValueInLocalStorage).toHaveBeenCalledWith(
                'smartedit-perspectives',
                'persp1',
                true
            );
            expect(featureService.disable.calls.count()).toBe(1);
            expect(featureService.disable).toHaveBeenCalledWith('feat0');
            expect(waitDialogService.showWaitModal).toHaveBeenCalled();
            expect(featureService.enable.calls.count()).toBe(6);
            expect(featureService.enable.calls.argsFor(4)).toEqual(['feat1']);
            expect(featureService.enable.calls.argsFor(5)).toEqual(['feat4']);
            expect(waitDialogService.hideWaitModal).not.toHaveBeenCalled();

            done();
        });

        it('WILL throw an error WHEN the perspective is not found', () => {
            expect(() => {
                perspectiveService.switchTo('whatever');
            }).toThrow();
            expect(waitDialogService.showWaitModal).not.toHaveBeenCalled();
        });

        it('will publish a perspective changed event after a successful switch', async (done) => {
            // GIVEN
            const perspective0 = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2', 'feat3']
            };

            await perspectiveService.register(perspective0);

            // WHEN
            await perspectiveService.switchTo(perspective0.key);

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                EVENT_PERSPECTIVE_CHANGED,
                true
            );

            done();
        });

        it('enable function called when feature has permission', async (done) => {
            // GIVEN
            featureService.getFeatureProperty.and.returnValue(
                Promise.resolve(['se.fake.permission'])
            );
            permissionService.isPermitted.and.returnValue(Promise.resolve(true));

            const perspective = {
                key: 'persp',
                nameI18nKey: 'persp',
                features: ['feat']
            };

            // WHEN
            await perspectiveService.register(perspective);
            await perspectiveService.switchTo(perspective.key);

            // THEN
            expect(featureService.getFeatureProperty).toHaveBeenCalledWith('feat', 'permissions');
            expect(permissionService.isPermitted).toHaveBeenCalledWith([
                {
                    names: ['se.fake.permission']
                }
            ]);
            expect(featureService.enable).toHaveBeenCalled();

            done();
        });

        it('when getFeatureProperty for permissions returns undefined, an empty array is used for permission.names', async (done) => {
            // GIVEN
            featureService.getFeatureProperty.and.returnValue(Promise.resolve(undefined));

            const perspective = {
                key: 'persp',
                nameI18nKey: 'persp',
                features: ['feat']
            };

            // WHEN
            await perspectiveService.register(perspective);
            await perspectiveService.switchTo(perspective.key);

            // THEN
            expect(featureService.getFeatureProperty).toHaveBeenCalledWith('feat', 'permissions');
            expect(permissionService.isPermitted).toHaveBeenCalledWith([
                {
                    names: []
                }
            ]);

            done();
        });

        it('enable function is not called when feature does not have permission', async (done) => {
            // GIVEN
            featureService.getFeatureProperty.and.returnValue(
                Promise.resolve(['se.fake.permission'])
            );
            permissionService.isPermitted.and.returnValue(Promise.resolve(false));

            const perspective = {
                key: 'persp',
                nameI18nKey: 'persp',
                features: ['feat']
            };

            // WHEN
            await perspectiveService.register(perspective);
            await perspectiveService.switchTo(perspective.key);

            // THEN
            expect(featureService.getFeatureProperty).toHaveBeenCalledWith('feat', 'permissions');
            expect(permissionService.isPermitted).toHaveBeenCalledWith([
                {
                    names: ['se.fake.permission']
                }
            ]);
            expect(featureService.enable).not.toHaveBeenCalled();

            done();
        });

        it('getPerspectives should return all perspectives after permissions are removed', async (done) => {
            // GIVEN
            const PERSPECTIVE_NAME_1 = 'persp_1';
            const perspective1 = {
                key: PERSPECTIVE_NAME_1,
                nameI18nKey: PERSPECTIVE_NAME_1,
                features: ['feat_1'],
                permissions: ['permission_1']
            };

            // WHEN
            permissionService.isPermitted.and.returnValue(Promise.resolve(false));
            await perspectiveService.register(perspective1);
            await perspectiveService.switchTo(perspective1.key);
            await perspectiveService.refreshPerspective();

            // THEN PERMISSIONS APPLIED
            perspectiveService.getPerspectives().then(function(result: IPerspective[]) {
                expect(result.length).toEqual(2);
            });

            // THEN PERMISSIONS REMOVED
            permissionService.isPermitted.and.returnValue(Promise.resolve(true));
            perspectiveService.getPerspectives().then(function(result: IPerspective[]) {
                expect(result.length).toEqual(3);
            });

            done();
        });
    });

    describe('selectDefault', () => {
        it('WILL select NONE_PERSPECTIVE if none is found in smartedit-perspectives cookie', async (done) => {
            // GIVEN
            spyOn(perspectiveService, 'switchTo').and.returnValue(Promise.resolve());
            spyOn(perspectiveService as any, '_disableAllFeaturesForPerspective');
            storageService.getValueFromLocalStorage.and.returnValue(Promise.resolve(null));

            expect(perspectiveService.getActivePerspective()).toBeNull();

            // WHEN
            await perspectiveService.selectDefault();

            // THEN
            expect(storageService.getValueFromLocalStorage).toHaveBeenCalledWith(
                'smartedit-perspectives',
                true
            );
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
            expect(
                (perspectiveService as any)._disableAllFeaturesForPerspective
            ).not.toHaveBeenCalled();
            done();
        });

        it('WILL select the perspective by disabling all features first if a perspective already exists', async (done) => {
            // GIVEN
            spyOn(perspectiveService, 'switchTo').and.returnValue(Promise.resolve());
            spyOn(perspectiveService as any, '_disableAllFeaturesForPerspective');
            storageService.getValueFromLocalStorage.and.returnValue(
                Promise.resolve(ALL_PERSPECTIVE)
            );

            expect(perspectiveService.getActivePerspective()).toBeNull();

            // WHEN
            await perspectiveService.selectDefault();

            // THEN
            expect(storageService.getValueFromLocalStorage).toHaveBeenCalledWith(
                'smartedit-perspectives',
                true
            );
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(ALL_PERSPECTIVE);
            expect(
                (perspectiveService as any)._disableAllFeaturesForPerspective
            ).toHaveBeenCalledWith(jasmine.objectContaining({ key: ALL_PERSPECTIVE }));
            done();
        });

        it('WILL select NONE_PERSPECTIVE if perspective stored in cookie has no permission', async (done) => {
            // GIVEN
            const PERMISSION1 = 'permission1';
            const PERSPECTIVE_WITHOUT_PERMISSIONS = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2'],
                permissions: [PERMISSION1]
            };
            await perspectiveService.register(PERSPECTIVE_WITHOUT_PERMISSIONS);

            permissionService.isPermitted.and.callFake(() => {
                return Promise.resolve(false);
            });

            spyOn(perspectiveService, 'switchTo').and.returnValue(Promise.resolve());
            spyOn(perspectiveService as any, '_disableAllFeaturesForPerspective');
            storageService.getValueFromLocalStorage.and.returnValue(
                Promise.resolve(PERSPECTIVE_WITHOUT_PERMISSIONS.key)
            );

            // WHEN
            await perspectiveService.selectDefault();

            // THEN
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
            expect(
                (perspectiveService as any)._disableAllFeaturesForPerspective
            ).toHaveBeenCalledWith(PERSPECTIVE_WITHOUT_PERMISSIONS);
            done();
        });

        it('WILL select perspective stored in cookie if perspective has permission', async (done) => {
            // GIVEN
            const PERMISSION1 = 'permission1';
            const PERSPECTIVE_WITHOUT_PERMISSIONS = {
                key: 'persp0',
                nameI18nKey: 'persp0',
                features: ['feat0', 'feat2'],
                permissions: [PERMISSION1]
            };
            await perspectiveService.register(PERSPECTIVE_WITHOUT_PERMISSIONS);

            permissionService.isPermitted.and.callFake(() => {
                return Promise.resolve(true);
            });

            spyOn(perspectiveService, 'switchTo').and.returnValue(Promise.resolve());
            storageService.getValueFromLocalStorage.and.returnValue(
                Promise.resolve(PERSPECTIVE_WITHOUT_PERMISSIONS.key)
            );

            // WHEN
            await perspectiveService.selectDefault();

            // THEN
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(
                PERSPECTIVE_WITHOUT_PERMISSIONS.key
            );

            done();
        });
    });

    describe('getActivePerspective', () => {
        it('returns null when there is no active perspective', () => {
            expect(perspectiveService.getActivePerspective()).toBeNull();
        });

        it('returns active perspective', async (done) => {
            // GIVEN
            perspectiveService.switchTo(NONE_PERSPECTIVE);

            const nonePerspective = await getPerspective(NONE_PERSPECTIVE);

            // WHEN
            const activePerspective = perspectiveService.getActivePerspective();

            // THEN
            expect(activePerspective).toEqual(nonePerspective);
            done();
        });
    });

    describe('isEmptyPerspectiveActive', () => {
        it('returns true if active perspective is NONE_PERSPECTIVE', async (done) => {
            // GIVEN
            const nonePerspective = {
                key: NONE_PERSPECTIVE,
                nameI18nKey: NONE_PERSPECTIVE,
                features: ['feat0']
            };
            perspectiveService.register(nonePerspective);

            // WHEN
            perspectiveService.switchTo(nonePerspective.key);

            // THEN
            expect(await perspectiveService.isEmptyPerspectiveActive()).toBe(true);
            done();
        });

        it('returns false if active perspective is not NONE_PERSPECTIVE', async (done) => {
            expect(await perspectiveService.isEmptyPerspectiveActive()).toBe(false);
            done();
        });
    });

    describe('getPerspectives', () => {
        it('returns perspectives for which user is granted permission', async (done) => {
            // GIVEN
            const PERMISSION1 = 'permission1';
            const PERMISSION2 = 'permission2';

            permissionService.isPermitted.calls.reset();

            const perspectiveConfig1 = {
                key: 'perspectiveKey1',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                permissions: [PERMISSION1]
            };

            const perspectiveConfig2 = {
                key: 'perspectiveKey2',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz'],
                permissions: [PERMISSION2]
            };

            const perspectiveConfig3 = {
                key: 'perspectiveKey3',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                features: ['abc', 'xyz']
            };

            permissionService.isPermitted.and.callFake(function(
                permissions: [{ names: string[] }]
            ) {
                switch (permissions[0].names[0]) {
                    case PERMISSION1:
                        return Promise.resolve(false);

                    case PERMISSION2:
                        return Promise.resolve(true);

                    default:
                        return Promise.resolve(false);
                }
            });

            await perspectiveService.register(perspectiveConfig1);
            await perspectiveService.register(perspectiveConfig2);
            await perspectiveService.register(perspectiveConfig3);

            // WHEN
            const perspectives = await getPerspectives();

            const perspective1 = findPerspective(perspectives, perspectiveConfig1.key);
            const perspective2 = findPerspective(perspectives, perspectiveConfig2.key);
            const perspective3 = findPerspective(perspectives, perspectiveConfig3.key);

            // THEN
            expect(perspective1).toBeUndefined();
            expect(perspective2).toBeDefined();
            expect(perspective3).toBeDefined();

            expect(permissionService.isPermitted).toHaveBeenCalledTimes(2);
            expect(permissionService.isPermitted.calls.argsFor(0)[0]).toEqual([
                jasmine.objectContaining({
                    names: perspectiveConfig1.permissions
                })
            ]);
            expect(permissionService.isPermitted.calls.argsFor(1)[0]).toEqual([
                jasmine.objectContaining({
                    names: perspectiveConfig2.permissions
                })
            ]);

            done();
        });
    });

    describe('getActivePerspectiveKey', function() {
        it(' will return the key of the perspective loaded in the storefront', async (done) => {
            // GIVEN
            const SOME_PERSPECTIVE_KEY = 'some.perspective';
            const somePerspective = {
                key: SOME_PERSPECTIVE_KEY,
                nameI18nKey: SOME_PERSPECTIVE_KEY,
                features: ['feat0']
            };
            await perspectiveService.register(somePerspective);

            // WHEN
            await perspectiveService.switchTo(SOME_PERSPECTIVE_KEY);

            const result = await perspectiveService.getActivePerspectiveKey();
            // THEN
            expect(result).toBe(SOME_PERSPECTIVE_KEY);
            done();
        });

        it('returns null if no perspective is loaded', async (done) => {
            const result = await perspectiveService.getActivePerspectiveKey();

            expect(result).toBe(null);
            done();
        });
    });

    /*
     * This function is used to simply calling perspectiveService.getPerspectives(). It returns
     * a promise.
     */
    function getPerspectives(): Promise<IPerspective[]> {
        return perspectiveService.getPerspectives().then((result: IPerspective[]) => {
            return result;
        });
    }

    async function getPerspective(key: string): Promise<IPerspective> {
        return findPerspective(await getPerspectives(), key);
    }

    function findPerspective(perspectives: IPerspective[], key: string): IPerspective {
        return perspectives.find((perspective) => perspective.key === key);
    }
});
