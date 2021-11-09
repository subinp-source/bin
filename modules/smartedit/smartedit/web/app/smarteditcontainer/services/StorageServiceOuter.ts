/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { Injectable } from '@angular/core';
import {
    CryptographicUtils,
    FingerPrintingService,
    GatewayProxied,
    IAuthToken,
    IStorageService,
    LogService,
    SeDowngradeService,
    WindowUtils
} from 'smarteditcommons';
export interface ISessionAuth {
    [index: string]: IAuthToken | any;
}

/** @internal */
@SeDowngradeService(IStorageService)
@GatewayProxied(
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
)
@Injectable()
export class StorageService extends IStorageService {
    private SMARTEDIT_SESSIONS = 'smartedit-sessions';
    private CUSTOM_PROPERTIES = 'custom_properties';

    constructor(
        private logService: LogService,
        private windowUtils: WindowUtils,
        private cryptographicUtils: CryptographicUtils,
        private fingerPrintingService: FingerPrintingService
    ) {
        super();
    }

    isInitialized(): Promise<boolean> {
        const sessions: ISessionAuth = this.getAuthTokens();
        return Promise.resolve(
            lodash.values(lodash.omit(sessions, [this.CUSTOM_PROPERTIES])).length > 0
        );
    }

    storeAuthToken(authURI: string, auth: IAuthToken): Promise<void> {
        const sessions: ISessionAuth = this.getAuthTokens();
        sessions[authURI] = auth;
        this._setSmarteditSessions(sessions);
        return Promise.resolve();
    }

    getAuthToken(authURI: string): Promise<IAuthToken> {
        const sessions: ISessionAuth = this.getAuthTokens();
        return Promise.resolve(sessions[authURI]);
    }

    removeAuthToken(authURI: string): Promise<void> {
        const sessions: ISessionAuth = this.getAuthTokens();
        delete sessions[authURI];
        this._setSmarteditSessions(sessions);
        return Promise.resolve();
    }

    removeAllAuthTokens(): Promise<void> {
        this._removeAllAuthTokens();
        return Promise.resolve();
    }

    getValueFromCookie(cookieName: string, isEncoded: boolean): Promise<any> {
        throw new Error('getValueFromCookie deprecated since 1905, use getValueFromLocalStorage');
    }

    getValueFromLocalStorage(cookieName: string, isEncoded: boolean): Promise<any> {
        return Promise.resolve(this._getValueFromLocalStorage(cookieName, isEncoded));
    }

    getAuthTokens(): ISessionAuth {
        const smarteditSessions = this.windowUtils
            .getWindow()
            .localStorage.getItem(this.SMARTEDIT_SESSIONS);
        let authTokens: ISessionAuth;
        if (smarteditSessions) {
            try {
                const decrypted = this.cryptographicUtils.aesDecrypt(
                    smarteditSessions,
                    this.fingerPrintingService.getFingerprint()
                );
                authTokens = JSON.parse(decodeURIComponent(escape(decrypted)));
            } catch {
                // failed to decrypt token. may occur if fingerprint changed.
                this.logService.info(
                    'Failed to read authentication token. Forcing a re-authentication.'
                );
            }
        }
        return authTokens || {};
    }

    putValueInCookie(cookieName: string, value: any, encode: boolean) {
        throw new Error('putValueInCookie deprecated since 1905, use setValueInLocalStorage');
    }

    setValueInLocalStorage(cookieName: string, value: any, encode: boolean) {
        return Promise.resolve(this._setValueInLocalStorage(cookieName, value, encode));
    }

    setItem(key: string, value: any) {
        const sessions = this.getAuthTokens();
        sessions[this.CUSTOM_PROPERTIES] = sessions[this.CUSTOM_PROPERTIES] || {};
        sessions[this.CUSTOM_PROPERTIES][key] = value;
        this._setSmarteditSessions(sessions);
        return Promise.resolve();
    }

    getItem(key: string) {
        const sessions = this.getAuthTokens();
        sessions[this.CUSTOM_PROPERTIES] = sessions[this.CUSTOM_PROPERTIES] || {};
        return Promise.resolve(sessions[this.CUSTOM_PROPERTIES][key]);
    }

    private _removeAllAuthTokens() {
        const sessions: ISessionAuth = this.getAuthTokens();
        const newSessions = lodash.pick(sessions, [this.CUSTOM_PROPERTIES]);
        this._setSmarteditSessions(newSessions);
    }

    private _getValueFromLocalStorage(cookieName: string, isEncoded: boolean): any {
        const rawValue: string = this.windowUtils.getWindow().localStorage.getItem(cookieName);
        let value = null;
        if (rawValue) {
            try {
                value = JSON.parse(
                    isEncoded ? decodeURIComponent(escape(window.atob(rawValue))) : rawValue
                );
            } catch (e) {
                // protecting against deserialization issue
                this.logService.error('Failed during deserialization ', e);
            }
        }
        return value;
    }

    private _setSmarteditSessions(sessions: ISessionAuth) {
        const sessionsJSONString = btoa(unescape(encodeURIComponent(JSON.stringify(sessions))));
        const sessionsEncrypted = this.cryptographicUtils.aesBase64Encrypt(
            sessionsJSONString,
            this.fingerPrintingService.getFingerprint()
        );
        this.windowUtils
            .getWindow()
            .localStorage.setItem(this.SMARTEDIT_SESSIONS, sessionsEncrypted);
    }

    private _setValueInLocalStorage(cookieName: string, value: any, encode: boolean) {
        let processedValue: string = JSON.stringify(value);
        processedValue = encode
            ? btoa(unescape(encodeURIComponent(processedValue)))
            : processedValue;
        this.windowUtils.getWindow().localStorage.setItem(cookieName, processedValue);
    }
}
