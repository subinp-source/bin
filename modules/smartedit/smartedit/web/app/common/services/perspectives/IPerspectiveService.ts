/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPerspective } from '../perspectives/IPerspective';

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IPerspectiveService
 *
 * @description
 * Interface for Perspective Service
 */
export abstract class IPerspectiveService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#register
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method registers a perspective.
     * When an end user selects a perspective in the SmartEdit web application,
     * all features bound to the perspective will be enabled when their respective enablingCallback functions are invoked
     * and all features not bound to the perspective will be disabled when their respective disablingCallback functions are invoked.
     *
     * @param {Object} configuration The perspective's configuration {@link smarteditServicesModule.interface:IPerspective IPerspective}
     *
     * @return {Promise<void>} An empty promise
     */
    register(configuration: IPerspective): Promise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#switchTo
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method activates a perspective identified by its key and deactivates the currently active perspective.
     * Activating a perspective consists in activating any feature that is bound to the perspective
     * or any feature that is bound to the perspective's referenced perspectives and deactivating any features
     * that are not bound to the perspective or to its referenced perspectives.
     * After the perspective is changed, the {@link seConstantsModule.object:EVENT_PERSPECTIVE_CHANGED
     * EVENT_PERSPECTIVE_CHANGED} event is published on the {@link smarteditCommonsModule.service:CrossFrameEventService
     * crossFrameEventService}, with no data.
     *
     * @param {String} key The key that uniquely identifies the perspective to be activated. This is the same key as the key used in the {@link smarteditServicesModule.interface:IPerspectiveService#methods_register register} method.
     * @return {Promise<void>} An empty promise
     */
    switchTo(key: string): Promise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#hasActivePerspective
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method returns true if a perspective is selected.
     *
     * @returns {Promise<boolean>} A promise with the value of the key of the active perspective.
     */
    hasActivePerspective(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#selectDefault
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method switches the currently-selected perspective to the default perspective.
     * It will also disable all features for the default perspective before enabling them all back.
     * If no value has been stored in the smartedit-perspectives cookie, the value of the default perspective is se.none.
     * If a value is stored in the cookie, that value is used as the default perspective.
     *
     * @return {Promise<void>} An empty promise
     */
    selectDefault(): Promise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#isEmptyPerspectiveActive
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method returns true if the current active perspective is the Preview mode (No active overlay).
     *
     * @returns {Promise<boolean>} A promise with the boolean flag that indicates if the current perspective is the Preview mode.
     */
    isEmptyPerspectiveActive(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#refreshPerspective
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method is used to refresh the prespective.
     * If there is an exising perspective set then it is refreshed by replaying all the features associated to the current perspective.
     * If there is no perspective set or if the perspective is not permitted then we set the default perspective.
     *
     * @return {Promise<void>} An empty promise
     */
    refreshPerspective(): Promise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#getActivePerspectiveKey
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * 	This method returns the key of the perspective that is currently loaded.
     *
     * @returns {Promise<string>} A promise that resolves to the key of the current perspective loaded in the storefront, null otherwise.
     */
    getActivePerspectiveKey(): Promise<string> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#isHotkeyEnabledForActivePerspective
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @description
     * This method returns true if the active perspective has the hotkey enabled
     *
     * @returns {Promise<boolean>} A promise with the boolean flag that indicates if the current perspective has the hotkey enabled.
     */
    isHotkeyEnabledForActivePerspective(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#getActivePerspective
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @returns {IPerspective}
     */

    getActivePerspective(): IPerspective {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPerspectiveService#getPerspectives
     * @methodOf smarteditServicesModule.interface:IPerspectiveService
     *
     * @returns {Promise<IPerspective[]>}
     */

    getPerspectives(): Promise<IPerspective[]> {
        'proxyFunction';
        return null;
    }
}
