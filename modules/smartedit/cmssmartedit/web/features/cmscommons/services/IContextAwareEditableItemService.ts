/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:contextAwareEditableItemService
 *
 * @description
 * Service to verify whether the item is editable in a particular context.
 */
export abstract class IContextAwareEditableItemService {
    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:contextAwareEditableItemService#isItemEditable
     * @methodOf cmsSmarteditServicesModule.service:contextAwareEditableItemService
     *
     * @description
     * Verifies whether the item is editable in current context or not.
     *
     * @param {string} itemUid The item uid.
     *
     * @returns {Promise} A promise that resolves to a boolean. It will be true, if the item is editable, false otherwise.
     */
    public isItemEditable(itemUid: string): angular.IPromise<boolean> {
        'proxyFunction';
        return null;
    }
}
