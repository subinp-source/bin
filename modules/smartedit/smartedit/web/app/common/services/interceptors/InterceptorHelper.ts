/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from '../../di';
import { LogService } from '@smart/utils';

export interface InterceptorHelperConfig {
    url: string;
    config?: InterceptorHelperConfig;
}

@SeDowngradeService()
export class InterceptorHelper {
    constructor(private logService: LogService) {}

    /**
     * Handles body of an interceptor request
     */

    public handleRequest(
        config: InterceptorHelperConfig,
        callback: () => Promise<any>
    ): InterceptorHelperConfig | Promise<any> {
        return this._handle(config, config, callback, false);
    }

    /**
     * Handles body of an interceptor response success
     */

    public handleResponse(
        response: InterceptorHelperConfig,
        callback: () => Promise<any>
    ): InterceptorHelperConfig | Promise<any> {
        return this._handle(response, response.config, callback, false);
    }

    /**
     * Handles body of an interceptor response error
     */

    public handleResponseError(
        response: InterceptorHelperConfig,
        callback: () => Promise<any>
    ): InterceptorHelperConfig | Promise<any> {
        return this._handle(response, response.config, callback, true);
    }

    private _isEligibleForInterceptors(config: InterceptorHelperConfig): boolean {
        return config && config.url && !/.+\.html$/.test(config.url);
    }

    private _handle(
        chain: InterceptorHelperConfig,
        config: InterceptorHelperConfig,
        callback: () => Promise<any>,
        isError: boolean
    ): InterceptorHelperConfig | Promise<any> {
        try {
            if (this._isEligibleForInterceptors(config)) {
                return callback();
            } else {
                if (isError) {
                    return Promise.reject(chain);
                } else {
                    return chain;
                }
            }
        } catch (e) {
            this.logService.error('caught error in one of the interceptors', e);

            if (isError) {
                return Promise.reject(chain);
            } else {
                return chain;
            }
        }
    }
}
