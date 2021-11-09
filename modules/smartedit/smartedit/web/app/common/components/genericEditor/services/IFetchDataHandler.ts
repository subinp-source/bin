/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorField } from '../';

/**
 * @ngdoc service
 * @name GenericEditorModule.FetchDataHandlerInterface
 *
 * @description
 * Interface describing the contract of a fetchDataHandler fetched through dependency injection by the
 * {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService} to populate dropdowns
 */
export interface IFetchDataHandler {
    /**
     * @ngdoc method
     * @name GenericEditorModule.FetchDataHandlerInterface#getById
     * @methodOf GenericEditorModule.FetchDataHandlerInterface
     *
     * @description
     * will returns a promise resolving to an entity, of type defined by field, matching the given identifier
     *
     * @param {GenericEditorField} field the field descriptor in {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService}
     * @param {String} identifier the value identifying the entity to fetch
     * @returns {String} an entity
     */
    getById(field: GenericEditorField, identifier: string): Promise<string>;

    /**
     * @ngdoc method
     * @name GenericEditorModule.FetchDataHandlerInterface#findBymask
     * @methodOf GenericEditorModule.FetchDataHandlerInterface
     *
     * @description
     * will returns a promise resolving to list of entities, of type defined by field, eligible for a given search mask
     *
     * @param {GenericEditorField} field the field descriptor in {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService}
     * @param {String} mask the value against witch to fetch entities.
     * @returns {String[]} a list of eligible entities
     */
    findByMask(field: GenericEditorField, mask: string): Promise<string[]>;
}
