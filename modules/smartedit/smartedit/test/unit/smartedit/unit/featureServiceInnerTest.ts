/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FeatureService } from 'smartedit/services';
import {
    annotationService,
    GatewayProxied,
    IContextualMenuButton,
    IDecorator
} from 'smarteditcommons';
describe('inner featureService', () => {
    const logService = jasmine.createSpyObj('logService', ['warn', 'debug']);
    const decoratorService = jasmine.createSpyObj('decoratorService', ['enable', 'disable']);
    const contextualMenuService = jasmine.createSpyObj('contextualMenuService', [
        'addItems',
        'removeItemByKey',
        'containsItem'
    ]);
    const cloneableUtils = jasmine.createSpyObj('cloneableUtils', ['makeCloneable']);
    cloneableUtils.makeCloneable.and.callFake((arg: any) => arg);

    let featureService: FeatureService;
    let featureServiceRegisterSpy: jasmine.Spy;

    beforeEach(() => {
        featureService = new FeatureService(
            logService,
            decoratorService,
            cloneableUtils,
            contextualMenuService
        );
        featureServiceRegisterSpy = spyOn(featureService, 'register').and.returnValue(
            Promise.resolve()
        );
        contextualMenuService.containsItem.and.returnValue(false);
        logService.warn.calls.reset();
    });

    it('checks GatewayProxied', () => {
        const decoratorObj = annotationService.getClassAnnotation(FeatureService, GatewayProxied);
        expect(decoratorObj).toEqual([
            '_registerAliases',
            'addToolbarItem',
            'register',
            'enable',
            'disable',
            '_remoteEnablingFromInner',
            '_remoteDisablingFromInner',
            'addDecorator',
            'getFeatureProperty',
            'addContextualMenuButton'
        ]);
    });

    it('leaves _registerAliases unimplemented', function() {
        expect((featureService as any)._registerAliases).toBeEmptyFunction();
    });

    it('leaves addToolbarItem unimplemented', function() {
        expect(featureService.addToolbarItem).toBeEmptyFunction();
    });

    it('leaves getFeatureProperty unimplemented', function() {
        expect(featureService.getFeatureProperty).toBeEmptyFunction();
    });

    describe('addDecorator', () => {
        let config;
        let subconfig: IDecorator;
        let promise: Promise<void>;

        beforeEach(() => {
            config = {
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                displayFunction: undefined
            } as IDecorator;
            promise = featureService.addDecorator(config);

            expect(featureServiceRegisterSpy.calls.count()).toBe(1);
            expect(featureService.register).toHaveBeenCalledWith({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                enablingCallback: jasmine.any(Function),
                disablingCallback: jasmine.any(Function),
                displayFunction: undefined
            });
            subconfig = featureServiceRegisterSpy.calls.argsFor(0)[0];
        });

        it('resolves promise', function(done) {
            promise.then(function(value: any) {
                expect(value).toBeUndefined();
                done();
            });
        });

        it('addDecorator will delegate to decoratorService and prepare callback with decoratorService.enable function', function() {
            expect(decoratorService.enable).not.toHaveBeenCalled();
            subconfig.enablingCallback();
            expect(decoratorService.enable).toHaveBeenCalledWith('somekey', undefined);
            expect(decoratorService.enable.calls.count()).toBe(1);
        });
        it('addDecorator will delegate to decoratorService and prepare callback with decoratorService.disable function', function() {
            expect(decoratorService.disable).not.toHaveBeenCalled();
            subconfig.disablingCallback();
            expect(decoratorService.disable).toHaveBeenCalledWith('somekey');
            expect(decoratorService.disable.calls.count()).toBe(1);
        });
    });

    describe('addContextualMenuButton will call register', () => {
        let button: IContextualMenuButton;
        let registeredButton: IContextualMenuButton;
        let subconfig: IContextualMenuButton;
        let promise: Promise<void>;
        beforeEach(() => {
            contextualMenuService.addItems.calls.reset();
            contextualMenuService.removeItemByKey.calls.reset();
            button = {
                key: 'somekey',
                regexpKeys: ['someregexpKey', 'strictType'],
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                i18nKey: 'somei18nKey',
                condition: jasmine.any(Function),
                action: {
                    callback: jasmine.any(Function)
                },
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                displaySmallIconClass: 'pathToSmallIcon'
            };

            registeredButton = {
                key: 'somekey',
                regexpKeys: ['someregexpKey', 'strictType'],
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                i18nKey: 'somei18nKey',
                condition: jasmine.any(Function),
                action: {
                    callback: jasmine.any(Function)
                },
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                displaySmallIconClass: 'pathToSmallIcon',
                enablingCallback: jasmine.any(Function),
                disablingCallback: jasmine.any(Function)
            };
            promise = featureService.addContextualMenuButton(button);
            expect(featureServiceRegisterSpy.calls.count()).toBe(1);
            expect(featureService.register).toHaveBeenCalledWith(registeredButton);
            subconfig = featureServiceRegisterSpy.calls.argsFor(0)[0];
        });
        it('resolves promise', function(done) {
            promise.then(function(value: any) {
                expect(value).toBeUndefined();
                done();
            });
        });
        it('add contextualMenuService.addItems into the callbacks', function() {
            expect(contextualMenuService.addItems).not.toHaveBeenCalled();

            subconfig.enablingCallback();

            expect(contextualMenuService.addItems).toHaveBeenCalledWith({
                someregexpKey: [
                    {
                        key: 'somekey',
                        i18nKey: 'somei18nKey',
                        condition: button.condition,
                        action: {
                            callback: button.action.callback
                        },
                        displayClass: 'displayClass1 displayClass2',
                        iconIdle: 'pathToIconIdle',
                        iconNonIdle: 'pathToIconNonIdle',
                        displaySmallIconClass: 'pathToSmallIcon'
                    }
                ],
                strictType: [
                    {
                        key: 'somekey',
                        i18nKey: 'somei18nKey',
                        condition: button.condition,
                        action: {
                            callback: button.action.callback
                        },
                        displayClass: 'displayClass1 displayClass2',
                        iconIdle: 'pathToIconIdle',
                        iconNonIdle: 'pathToIconNonIdle',
                        displaySmallIconClass: 'pathToSmallIcon'
                    }
                ]
            });

            expect(contextualMenuService.addItems.calls.count()).toBe(1);
        });
        it('add contextualMenuService.removeItemByKey into the callbacks', function() {
            expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();

            subconfig.disablingCallback();

            expect(contextualMenuService.removeItemByKey).toHaveBeenCalledWith('somekey');
            expect(contextualMenuService.removeItemByKey.calls.count()).toBe(1);
        });
    });

    describe('addSlotContextualMenuButton', function() {
        let button: IContextualMenuButton;
        let expectedFeatureCall: IContextualMenuButton;
        let expectedContextualMenuServiceCall: any;

        beforeEach(function() {
            contextualMenuService.addItems.calls.reset();
            contextualMenuService.removeItemByKey.calls.reset();
            button = {
                key: 'somekey',
                regexpKeys: ['someregexpKey', 'strictType'],
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                i18nKey: 'somei18nKey',
                condition: jasmine.any(Function),
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                displaySmallIconClass: 'pathToSmallIcon',
                action: {
                    callback: jasmine.any(Function)
                }
            };

            expectedFeatureCall = {
                key: 'somekey',
                regexpKeys: ['someregexpKey', 'strictType'],
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                i18nKey: 'somei18nKey',
                condition: jasmine.any(Function),
                action: {
                    callback: jasmine.any(Function)
                },
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                displaySmallIconClass: 'pathToSmallIcon',
                enablingCallback: jasmine.any(Function),
                disablingCallback: jasmine.any(Function)
            };

            expectedContextualMenuServiceCall = {
                someregexpKey: [
                    {
                        key: 'somekey',
                        i18nKey: 'somei18nKey',
                        condition: jasmine.any(Function),
                        action: {
                            callback: jasmine.any(Function)
                        },
                        displayClass: 'displayClass1 displayClass2',
                        iconIdle: 'pathToIconIdle',
                        iconNonIdle: 'pathToIconNonIdle',
                        displaySmallIconClass: 'pathToSmallIcon'
                    }
                ],
                strictType: [
                    {
                        key: 'somekey',
                        i18nKey: 'somei18nKey',
                        condition: jasmine.any(Function),
                        action: {
                            callback: jasmine.any(Function)
                        },
                        displayClass: 'displayClass1 displayClass2',
                        iconIdle: 'pathToIconIdle',
                        iconNonIdle: 'pathToIconNonIdle',
                        displaySmallIconClass: 'pathToSmallIcon'
                    }
                ]
            };

            featureService.addContextualMenuButton(button);
        });

        it('should call register', function() {
            expect(featureServiceRegisterSpy.calls.count()).toBe(1);
            expect(featureService.register).toHaveBeenCalledWith(expectedFeatureCall);

            expect(contextualMenuService.addItems).not.toHaveBeenCalled();
            expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();
        });

        it('should add template by enabling callback', function() {
            const subconfig = featureServiceRegisterSpy.calls.argsFor(0)[0];

            subconfig.enablingCallback();

            expect(contextualMenuService.addItems).toHaveBeenCalledWith(
                expectedContextualMenuServiceCall
            );
            expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();
        });

        it('should remove template by disabling callback', function() {
            const subconfig = featureServiceRegisterSpy.calls.argsFor(0)[0];
            subconfig.enablingCallback();
            subconfig.disablingCallback();

            expect(contextualMenuService.addItems.calls.count()).toBe(1);
            expect(contextualMenuService.removeItemByKey).toHaveBeenCalledWith('somekey');
        });

        it('should not add item if it already was added', function() {
            const subconfig = featureServiceRegisterSpy.calls.argsFor(0)[0];
            subconfig.enablingCallback();
            contextualMenuService.containsItem.and.returnValue(true);
            subconfig.enablingCallback();

            expect(contextualMenuService.addItems.calls.count()).toBe(1);
        });
    });
});
