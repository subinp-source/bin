/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IWaitDialogService
 *
 * @description
 * This service be used in order to display (or hide) a 'loading' overlay. The overlay should display on top of everything, preventing
 * the user from doing any action until the overlay gets hidden.
 */

export abstract class IWaitDialogService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IWaitDialogService#showWaitModal
     * @methodOf smarteditServicesModule.interface:IWaitDialogService
     *
     * @description
     * This method can be called to display the loading overlay.
     *
     * @param {String} [customLoadingMessageLocalizedKey="se.wait.dialog.message"] The i18n key that corresponds to the message to be displayed.
     */
    showWaitModal(customLoadingMessageLocalizedKey?: string): void {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IWaitDialogService#hideWaitModal
     * @methodOf smarteditServicesModule.interface:IWaitDialogService
     *
     * @description
     * Removes the loading overlay.
     */
    hideWaitModal(): void {
        'proxyFunction';
        return null;
    }
}
