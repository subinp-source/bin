/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    pageChangeEvictionTag,
    rarelyChangingContent,
    Cached,
    ICatalogService,
    IPageInfoService,
    IRestService,
    RestServiceFactory,
    SeInjectable,
    TypedMap
} from 'smarteditcommons';
import {
    CmsitemsRestService,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    IPageContentSlotsComponentsRestService,
    Page,
    PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI
} from '../dao';
import { CMSItem, CMSItemStructure, ICMSComponent } from '../dtos';

export interface LoadPagedComponentsRequestPayload {
    page: number;
    pageSize: number;
    mask: string;
    typeCode: string;
    catalogId: string;
    catalogVersion: string;
}

export interface LoadPagedComponentTypesRequestPayload {
    catalogId: string;
    catalogVersion: string;
    pageId: string;
    pageSize: number;
    currentPage: number;
    mask: string;
}

export interface ComponentInfo {
    name: string;
    slotId: string;
    pageId: string;
    position: number;
    typeCode: string;
    itemtype: string;
    catalogVersion: string;
    targetSlotId?: string;
    componentType?: string;
    catalogVersionUuid?: string;
}

/**
 * @ngdoc service
 * @name componentMenuModule.ComponentService
 *
 * @description
 * Service which manages component types and items
 */

@SeInjectable()
export class ComponentService {
    private pageComponentTypesRestService: IRestService<Page<CMSItemStructure>>;
    private restServiceForAddExistingComponent: IRestService<void>;

    constructor(
        private restServiceFactory: RestServiceFactory,
        private cmsitemsRestService: CmsitemsRestService,
        private catalogService: ICatalogService,
        private pageInfoService: IPageInfoService,
        private pageContentSlotsComponentsRestService: IPageContentSlotsComponentsRestService
    ) {
        const pageComponentTypesRestServiceURI =
            '/cmssmarteditwebservices/v1/catalogs/:catalogId/versions/:catalogVersion/pages/:pageId/types';
        this.pageComponentTypesRestService = this.restServiceFactory.get(
            pageComponentTypesRestServiceURI
        );

        this.restServiceForAddExistingComponent = this.restServiceFactory.get(
            PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI
        );
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#createNewComponent
     * @methodOf componentMenuModule.ComponentService
     *
     * @description given a component info and the component payload, a new componentItem is created and added to a slot
     *
     * @param {Object} componentInfo The basic information of the ComponentType to be created and added to the slot.
     * @param {String} componentInfo.componenCode componenCode of the ComponentType to be created and added to the slot.
     * @param {String} componentInfo.name name of the new component to be created.
     * @param {String} componentInfo.pageId pageId used to identify the current page template.
     * @param {String} componentInfo.slotId slotId used to identify the slot in the current template.
     * @param {String} componentInfo.position position used to identify the position in the slot in the current template.
     * @param {String} componentInfo.type type of the component being created.
     * @param {Object} componentPayload payload of the new component to be created.
     */
    createNewComponent(componentInfo: ComponentInfo, componentPayload: CMSItem): Promise<CMSItem> {
        const _payload: CMSItem = {
            name: componentInfo.name,
            slotId: componentInfo.targetSlotId,
            pageId: componentInfo.pageId,
            position: componentInfo.position,
            typeCode: componentInfo.componentType,
            itemtype: componentInfo.componentType,
            catalogVersion: componentInfo.catalogVersionUuid,
            uid: '',
            uuid: ''
        };

        if (typeof componentPayload === 'object') {
            for (const property in componentPayload) {
                if (componentPayload.hasOwnProperty(property)) {
                    _payload[property] = componentPayload[property];
                }
            }
        } else if (componentPayload) {
            throw new Error(
                `ComponentService.createNewComponent() - Illegal componentPayload - [${componentPayload}]`
            );
        }

        return this.cmsitemsRestService.create(_payload);
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#updateComponent
     * @methodOf componentMenuModule.ComponentService
     *
     * @description Given a component info and the payload related to an existing component, the latter will be updated with the new supplied values.
     *
     * @param {Object} componentPayload of the new component to be created, including the info.
     * @param {String} componentPayload.componenCode of the ComponentType to be created and added to the slot.
     * @param {String} componentPayload.name of the new component to be created.
     * @param {String} componentPayload.pageId used to identify the current page template.
     * @param {String} componentPayload.slotId used to identify the slot in the current template.
     * @param {String} componentPayload.position used to identify the position in the slot in the current template.
     * @param {String} componentPayload.type of the component being created.
     */
    updateComponent(componentPayload: CMSItem): Promise<CMSItem> {
        return this.cmsitemsRestService.update(componentPayload);
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#addExistingComponent
     * @methodOf componentMenuModule.ComponentService
     *
     * @description add an existing component item to a slot
     *
     * @param {String} pageId used to identify the page containing the slot in the current template.
     * @param {String} componentId used to identify the existing component which will be added to the slot.
     * @param {String} slotId used to identify the slot in the current template.
     * @param {String} position used to identify the position in the slot in the current template.
     */
    addExistingComponent(
        pageId: string,
        componentId: string,
        slotId: string,
        position: number
    ): Promise<void> {
        return this.restServiceForAddExistingComponent.save({
            pageId,
            slotId,
            componentId,
            position
        });
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#getSupportedComponentTypesForCurrentPage
     * @methodOf componentMenuModule.ComponentService
     *
     * @description Fetches all component types that are applicable to the current page.
     *
     * @param {Object} payload The payload that contains the information of the component types to load
     * @returns {Promise} A promise resolving to a page of component types applicable to the current page.
     */
    @Cached({ actions: [rarelyChangingContent], tags: [pageChangeEvictionTag] })
    getSupportedComponentTypesForCurrentPage(
        payload: LoadPagedComponentTypesRequestPayload
    ): Promise<Page<CMSItemStructure>> {
        return this.pageComponentTypesRestService.get(payload as any);
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#loadComponentItem
     * @methodOf componentMenuModule.ComponentService
     *
     * @description load a component identified by its id
     */
    loadComponentItem(id: string): Promise<CMSItem> {
        return this.cmsitemsRestService.getById(id);
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#loadPagedComponentItems
     * @methodOf componentMenuModule.ComponentService
     *
     * @description all existing component items for the current catalog are retrieved in the form of pages
     * used for pagination especially when the result set is very large.
     *
     * @param {String} mask the search string to filter the results.
     * @param {String} pageSize the number of elements that a page can contain.
     * @param {String} page the current page number.
     */
    loadPagedComponentItems(
        mask: string,
        pageSize: number,
        page: number
    ): Promise<CMSItem | Page<CMSItem>> {
        return this.catalogService.retrieveUriContext().then((uriContext) => {
            const requestParams = {
                pageSize,
                currentPage: page,
                mask,
                sort: 'name',
                typeCode: 'AbstractCMSComponent',
                catalogId: uriContext[CONTEXT_CATALOG],
                catalogVersion: uriContext[CONTEXT_CATALOG_VERSION],
                itemSearchParams: ''
            };

            return this.cmsitemsRestService.get(requestParams);
        });
    }

    /**
     * @ngdoc method
     * @name componentMenuModule.ComponentService#loadPagedComponentItemsByCatalogVersion
     * @methodOf componentMenuModule.ComponentService
     *
     * @description all existing component items for the provided content catalog are retrieved in the form of pages
     * used for pagination especially when the result set is very large.
     *
     * @param {Object} payload The payload that contains the information of the page of components to load
     * @param {String} payload.catalogId the id of the catalog for which to retrieve the component items.
     * @param {String} payload.catalogVersion the id of the catalog version for which to retrieve the component items.
     * @param {String} payload.mask the search string to filter the results.
     * @param {String} payload.pageSize the number of elements that a page can contain.
     * @param {String} payload.page the current page number.
     *
     * @returns {Promise} A promise resolving to a page of component items retrieved from the provided catalog version.
     */
    loadPagedComponentItemsByCatalogVersion(
        payload: LoadPagedComponentsRequestPayload
    ): Promise<Page<CMSItem>> {
        const requestParams = {
            pageSize: payload.pageSize,
            currentPage: payload.page,
            mask: payload.mask,
            sort: 'name',
            typeCode: 'AbstractCMSComponent',
            catalogId: payload.catalogId,
            catalogVersion: payload.catalogVersion,
            itemSearchParams: ''
        };

        return this.cmsitemsRestService.get(requestParams);
    }

    async getSlotsForComponent(componentUuid: string): Promise<string[]> {
        const slotIds: string[] = [];
        const allSlotsToComponents = await this._getContentSlotsForComponents();

        Object.keys(allSlotsToComponents).forEach(function(slotId) {
            if (
                allSlotsToComponents[slotId].find(function(component: ICMSComponent) {
                    return component.uuid === componentUuid;
                })
            ) {
                slotIds.push(slotId);
            }
        });
        return slotIds;
    }

    private async _getContentSlotsForComponents(): Promise<TypedMap<ICMSComponent[]>> {
        const pageId = await this.pageInfoService.getPageUID();
        const slots: TypedMap<
            ICMSComponent[]
        > = await this.pageContentSlotsComponentsRestService.getSlotsToComponentsMapForPageUid(
            pageId
        );

        return slots;
    }
}
