/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import {
    annotationService,
    FingerPrintingService,
    GatewayProxied,
    IAuthToken,
    LogService,
    WindowUtils
} from 'smarteditcommons';
import { StorageService } from 'smarteditcontainer/services';

describe('outer storage service', () => {
    const cryptographicUtils: jasmine.SpyObj<any> = jasmine.createSpyObj('cryptographicUtils', [
        'aesBase64Encrypt',
        'aesDecrypt'
    ]);
    const fingerPrintingService: jasmine.SpyObj<FingerPrintingService> = jasmine.createSpyObj(
        'fingerPrintingService',
        ['getFingerprint']
    );
    const windowUtils: jasmine.SpyObj<WindowUtils> = jasmine.createSpyObj('windowUtils', [
        'getWindow'
    ]);
    let store: any = {};
    const $window = {
        location: {
            protocol: jasmine.createSpyObj('$window.location.protocol', ['indexOf'])
        },
        localStorage: {
            getItem: (key: string): string => {
                return key in store ? store[key] : null;
            },
            setItem: (key: string, value: string) => {
                store[key] = `${value}`;
            }
        }
    };
    const MOCK_FINGERPRINT = 'unique-browser-fingerprint';

    let storageService: StorageService;

    beforeEach(() => {
        $window.location.protocol.indexOf.and.returnValue(-1);

        spyOn($window.localStorage, 'getItem').and.callFake(function(key: string) {
            return store[key];
        });
        spyOn($window.localStorage, 'setItem').and.callFake(function(key: string, value: string) {
            store[key] = value;
        });

        windowUtils.getWindow.and.returnValue($window);

        fingerPrintingService.getFingerprint.and.returnValue(MOCK_FINGERPRINT);

        storageService = new StorageService(
            new LogService(),
            windowUtils,
            cryptographicUtils,
            fingerPrintingService
        );
    });

    afterEach(() => {
        store = {};
    });

    function setAuthTokens(authTokens: any) {
        // set fake smartedit-sessions into localStorage
        $window.localStorage.setItem('smartedit-sessions', 'encrypted-smartedit-sessions');
        // decrypt smartedit-sessions to return authTokens
        cryptographicUtils.aesDecrypt.and.callFake((smarteditSessions: any) => {
            if (smarteditSessions === 'encrypted-smartedit-sessions') {
                return JSON.stringify(authTokens);
            }
            throw new Error(`Unexpected smarteditSessions: ${smarteditSessions}`);
        });
    }

    it('checks GatewayProxied', () => {
        const decoratorObj = annotationService.getClassAnnotation(StorageService, GatewayProxied);
        expect(decoratorObj).toEqual([
            'isInitialized',
            'storeAuthToken',
            'getAuthToken',
            'removeAuthToken',
            'removeAllAuthTokens',
            'storePrincipalIdentifier',
            'getPrincipalIdentifier',
            'removePrincipalIdentifier',
            'getValueFromCookie',
            'getValueFromLocalStorage'
        ]);
    });

    it('isInitialized', (done) => {
        // GIVEN
        setAuthTokens({
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            },
            'principal-uid': 'someUserName'
        });

        // WHEN
        storageService.isInitialized().then((value: boolean) => {
            expect(value).toBe(true);
            done();
        });

        // THEN
        expect(cryptographicUtils.aesDecrypt).toHaveBeenCalledWith(
            'encrypted-smartedit-sessions',
            MOCK_FINGERPRINT
        );
    });

    it('removeAllAuthTokens will remove from smartedit-sessions', () => {
        // GIVEN
        const authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            },
            custom_properties: {}
        };
        setAuthTokens(authTokens);

        const sessionsEncrypted = btoa(JSON.stringify({ custom_properties: {} }));
        cryptographicUtils.aesBase64Encrypt.and.returnValue(sessionsEncrypted);

        // WHEN
        storageService.removeAllAuthTokens();

        // THEN
        expect(cryptographicUtils.aesBase64Encrypt).toHaveBeenCalledWith(
            btoa(unescape(encodeURIComponent(JSON.stringify({ custom_properties: {} })))),
            MOCK_FINGERPRINT
        );
        expect($window.localStorage.setItem).toHaveBeenCalledWith(
            'smartedit-sessions',
            sessionsEncrypted
        );
    });

    it('removeAuthToken for entryPoint1 will remove the entry from smartedit-sessions', () => {
        // GIVEN
        setAuthTokens({
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        });

        const sessionsEncrypted = btoa(JSON.stringify({ custom_properties: {} }));
        cryptographicUtils.aesBase64Encrypt.and.returnValue(sessionsEncrypted);

        // WHEN
        storageService.removeAuthToken('entryPoint1');

        // THEN
        expect(cryptographicUtils.aesBase64Encrypt).toHaveBeenCalledWith(
            btoa(
                unescape(
                    encodeURIComponent(
                        JSON.stringify({
                            entryPoint2: {
                                access_token: 'access_token2',
                                token_type: 'bearer'
                            }
                        })
                    )
                )
            ),
            MOCK_FINGERPRINT
        );
        expect($window.localStorage.setItem).toHaveBeenCalledWith(
            'smartedit-sessions',
            sessionsEncrypted
        );
    });

    it('removeAuthToken for entryPoint1 will remove the entire smartedit-sessions', () => {
        // GIVEN
        const authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        };
        setAuthTokens(authTokens);

        const sessionsEncrypted = btoa(JSON.stringify({}));
        cryptographicUtils.aesBase64Encrypt.and.returnValue(sessionsEncrypted);

        // WHEN
        storageService.removeAuthToken('entryPoint1');

        // THEN
        expect(cryptographicUtils.aesBase64Encrypt).toHaveBeenCalledWith(
            btoa(unescape(encodeURIComponent(JSON.stringify({})))),
            MOCK_FINGERPRINT
        );
        expect($window.localStorage.setItem).toHaveBeenCalledWith(
            'smartedit-sessions',
            sessionsEncrypted
        );
    });

    it('getAuthToken will get the auth token specific to the given entry point from smartedit-sessions', (done) => {
        // GIVEN
        const authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        setAuthTokens(authTokens);

        const sessionsEncrypted = btoa(JSON.stringify({}));
        cryptographicUtils.aesBase64Encrypt.and.returnValue(sessionsEncrypted);

        // WHEN
        storageService.getAuthToken('entryPoint2').then((token: IAuthToken) => {
            expect(token).toEqual({
                access_token: 'access_token2',
                token_type: 'bearer'
            } as IAuthToken);
            done();
        });

        // THEN
        expect($window.localStorage.getItem).toHaveBeenCalledWith('smartedit-sessions');
        expect(cryptographicUtils.aesDecrypt).toHaveBeenCalledWith(
            'encrypted-smartedit-sessions',
            MOCK_FINGERPRINT
        );
    });

    it('storeAuthToken will store the given auth token in a new map with the entryPoint as the key in smartedit-sessions', () => {
        // GIVEN
        const sessionsEncrypted = btoa(
            JSON.stringify({
                entryPoint2: {
                    access_token: 'access_token2',
                    token_type: 'bearer'
                }
            })
        );
        cryptographicUtils.aesBase64Encrypt.and.returnValue(sessionsEncrypted);

        // WHEN
        storageService.storeAuthToken('entryPoint1', {
            access_token: 'access_token1',
            token_type: 'bearer'
        } as IAuthToken);

        // THEN
        expect(cryptographicUtils.aesBase64Encrypt).toHaveBeenCalledWith(
            btoa(
                unescape(
                    encodeURIComponent(
                        JSON.stringify({
                            entryPoint1: {
                                access_token: 'access_token1',
                                token_type: 'bearer'
                            }
                        })
                    )
                )
            ),
            MOCK_FINGERPRINT
        );
        expect($window.localStorage.setItem).toHaveBeenCalledWith(
            'smartedit-sessions',
            sessionsEncrypted
        );
    });

    it('storeAuthToken will store the given auth token in existing map with the entryPoint as the key in pre-existing smartedit-sessions', () => {
        // GIVEN
        const expectedTokens = {
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            },
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        };
        const authTokens = {
            entryPoint2: expectedTokens.entryPoint2
        };
        setAuthTokens(authTokens);

        const sessionsEncrypted = btoa(JSON.stringify(expectedTokens));
        cryptographicUtils.aesBase64Encrypt.and.returnValue(sessionsEncrypted);

        // WHEN
        storageService.storeAuthToken('entryPoint1', expectedTokens.entryPoint1 as IAuthToken);

        // THEN
        expect(cryptographicUtils.aesBase64Encrypt).toHaveBeenCalledWith(
            btoa(unescape(encodeURIComponent(JSON.stringify(expectedTokens)))),
            MOCK_FINGERPRINT
        );
        expect($window.localStorage.setItem).toHaveBeenCalledWith(
            'smartedit-sessions',
            sessionsEncrypted
        );
    });

    it('IF no cookie is stored WHEN getValueFromLocalStorage is called THEN null is returned', (done) => {
        // Act
        storageService.getValueFromLocalStorage('someCookie', true).then((value: any) => {
            expect(value).toBe(null);
            done();
        });

        // Assert
        expect($window.localStorage.getItem).toHaveBeenCalledWith('someCookie');
    });

    it('IF cookie value is not JSON parsable WHEN getValueFromLocalStorage is called THEN null is returned', (done) => {
        // Arrange
        $window.localStorage.setItem('someCookie', '{');

        // Act
        storageService.getValueFromLocalStorage('someCookie', true).then((value: any) => {
            expect(value).toBe(null);
            done();
        });

        // Assert
        expect($window.localStorage.getItem).toHaveBeenCalledWith('someCookie');
    });

    it('IF a cookie is stored and its value is not encoded WHEN getValueFromLocalStorage is called THEN the value is returned', (done) => {
        // Arrange
        const rawValue = 'se.none';
        $window.localStorage.setItem('someCookie', JSON.stringify(rawValue));
        // Act
        storageService.getValueFromLocalStorage('someCookie', false).then((value: any) => {
            expect(value).toBe(rawValue);
            done();
        });

        // Assert
        expect($window.localStorage.getItem).toHaveBeenCalledWith('someCookie');
    });

    it('IF no cookie is stored and its value is encoded WHEN getValueFromLocalStorage is called THEN the un-encoded value is returned', (done) => {
        // Arrange
        const rawValue = 'se.none';
        const encodedValue = 'InNlLm5vbmUi';
        $window.localStorage.setItem('someCookie', encodedValue);

        // Act
        storageService.getValueFromLocalStorage('someCookie', true).then((value: any) => {
            expect(value).toBe(rawValue);
            done();
        });

        // Assert
        expect($window.localStorage.getItem).toHaveBeenCalledWith('someCookie');
    });

    it('WHEN setValueInLocalStorage is called and the encode flag is not set THEN the un-encoded value is stored', () => {
        // Arrange
        const rawValue = {
            key: 'se.none'
        };

        // Act
        storageService.setValueInLocalStorage('someCookie', rawValue, false);

        // Assert
        expect($window.localStorage.setItem).toHaveBeenCalledWith(
            'someCookie',
            JSON.stringify(rawValue)
        );
    });

    it('WHEN setValueInLocalStorage is called and the encode flag is set THEN the encoded value is stored', () => {
        // Arrange
        const rawValue = '"se.none"';
        const encodedValue = 'Ilwic2Uubm9uZVwiIg==';

        // Act
        storageService.setValueInLocalStorage('someCookie', rawValue, true);

        // Assert
        expect($window.localStorage.setItem).toHaveBeenCalledWith('someCookie', encodedValue);
    });
});
