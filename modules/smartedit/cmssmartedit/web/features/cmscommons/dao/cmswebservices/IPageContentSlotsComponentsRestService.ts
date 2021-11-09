/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from 'smarteditcommons';
import { ICMSComponent } from 'cmscommons';

/**
 * @description
 * Rest Service to retrieve page content slots for components.
 */
export abstract class IPageContentSlotsComponentsRestService {
    /**
     * @description
     * Clears the slotId - components list map in the cache.
     */
    clearCache(): void {
        'proxyFunction';
    }

    /**
     * @description
     * Retrieves a list of pageContentSlotsComponents associated to a page and Converts the list of pageContentSlotsComponents to slotId - components list map.
     * If the map is already stored in the cache, it will return the cache info.
     *
     * @param {string} pageUid The uid of the page to retrieve the content slots to components map.
     * @return {Promise} A promise that resolves to slotId - components list map.
     */
    getSlotsToComponentsMapForPageUid(pageUid: string): Promise<TypedMap<ICMSComponent[]>> {
        'proxyFunction';
        return null;
    }

    /**
     * @description
     * Retrieves a list of all components for a given slot which is part of the page being loaded.
     * It returns all the components irrespective of their visibility.
     *
     * @param {string} slotUuid The uid of the slot to retrieve the list of components.
     * @return {Promise} A promise that resolves to components list.
     */
    getComponentsForSlot(slotUuid: string): Promise<ICMSComponent[]> {
        'proxyFunction';
        return null;
    }
}
