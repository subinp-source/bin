/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name ProxiedService.interface:IProxiedService
 * @description
 * The IProxiedService interface represents a service that has one or more methods proxied over the
 * {@link smarteditCommonsModule.service:GatewayProxy gatewayProxy}
 */
export interface IProxiedService {
    /**
     * @ngdoc property
     * @name gatewayId - (readonly) string
     * @propertyOf ProxiedService.interface:IProxiedService
     * @description
     * gatewayId Is a unique string used to create the gateway communications channel between the smartedit and
     * smarteditContainer applications. This value should be unique to the application.
     *
     * A typical implementation would look as follows:
     *
     * Note: We use abstract class as a pseudo-interface for proxied services, due to technical constraints.
     *
     * ```js
     * // commons/.../IMyService.ts
     * abstract class IMyService implements IProxiedService {
     *
     *      get gatewayId(): string {
     *          return 'IMyService';
     *      };
     *
     *      myMethod(): string {
     *          'proxyFunction';
     *          return null;
     *      }
     * }
     * ```
     */
    readonly gatewayId?: string;

    [index: string]: any;
}
