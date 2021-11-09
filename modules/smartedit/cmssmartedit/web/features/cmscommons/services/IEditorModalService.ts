/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from 'smarteditcommons';

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:editorModalService
 *
 * @description
 * Convenience service to open an editor modal window for a given component type and component ID.
 *
 * Example:
 * We pass information about component to open method, and the component editor in form of modal appears.
 */
export abstract class IEditorModalService {
    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:editorModalService#open
     * @methodOf cmsSmarteditServicesModule.service:editorModalService
     *
     * @description
     * Proxy function which delegates opening an editor modal for a given component type and component ID to the
     * SmartEdit container.
     *
     * @param {IComponent} componentAttributes The details of the component to be created/edited
     * @param {string?} componentAttributes.smarteditComponentUuid An optional universally unique UUID of the component if the component is being edited.
     * @param {string?} componentAttributes.smarteditComponentId An optional universally unique ID of the component if the component is being edited.
     * @param {string} componentAttributes.smarteditComponentType The component type
     * @param {string?} componentAttributes.smarteditCatalogVersionUuid The smartedit catalog version UUID to add the component to.
     * @param {string?} componentAttributes.catalogVersionUuid The catalog version UUID to add the component to.
     * @param {boolean} componentAttributes.initialDirty Is the component dirty.
     * @param {Payload?} componentAttributes.content An optional content for create operation. It's ignored if componentAttributes.smarteditComponentUuid is defined.
     * @param {string?} targetSlotId The ID of the slot in which the component is placed.
     * @param {string?} position The position in a given slot where the component should be placed.
     * @param {string?} targetedQualifier Causes the genericEditor to switch to the tab containing a qualifier of the given name.
     * @param {Function} saveCallback The optional function that is executed if the user clicks the Save button and the modal closes successfully. The function provides one parameter: item that has been saved.
     * @param {string} editorStackId The string that identifies the stack of editors being edited together.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    open(
        componentAttributes: IComponent,
        targetSlotId?: string,
        position?: number,
        targetedQualifier?: string,
        saveCallback?: (item: any) => void,
        editorStackId?: string
    ): angular.IPromise<void> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:editorModalService#open
     * @methodOf cmsSmarteditServicesModule.service:editorModalService
     *
     * @description
     * Proxy function which delegates opening an editor modal for a given component type and component ID to the
     * SmartEdit container.
     *
     * @param {string} componentType The type of component as defined in the platform.
     * @param {string} componentUuid The UUID of the component as defined in the database.
     * @param {string?} targetedQualifier Causes the genericEditor to switch to the tab containing a qualifier of the given name.
     * @param {Function} saveCallback The optional function that is executed if the user clicks the Save button and the modal closes successfully. The function provides one parameter: item that has been saved.
     * @param {string} editorStackId The string that identifies the stack of editors being edited together.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    openAndRerenderSlot(
        componentType: string,
        componentUuid: string,
        targetedQualifier?: string,
        saveCallback?: (item: any) => void,
        editorStackId?: string
    ): angular.IPromise<void> {
        'proxyFunction';
        return null;
    }
}

export interface IComponent {
    smarteditComponentUuid?: string;
    smarteditComponentType: string;
    catalogVersionUuid?: string;
    smarteditCatalogVersionUuid?: string;
    smarteditComponentId?: string;
    smarteditElementUuid?: string;
    content?: Payload;
    initialDirty?: boolean;
}
