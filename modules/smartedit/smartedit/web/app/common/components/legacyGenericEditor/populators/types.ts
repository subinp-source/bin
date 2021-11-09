/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';
import { GenericEditorField, GenericEditorOption } from '../../genericEditor/types';

export interface IDropdownPopulator {
    /* @deprecated since 1811 */
    populate(payload: DropdownPopulatorPayload): PromiseLike<GenericEditorOption[]>;
    isPaged(): boolean;
    fetchAll(payload: DropdownPopulatorPayload): Promise<GenericEditorOption[]>;
    fetchPage(payload: DropdownPopulatorPagePayload): Promise<DropdownPopulatorFetchPageResponse>;
    populateAttributes(
        items: GenericEditorOption[],
        idAttribute: string,
        orderedLabelAttributes: string[]
    ): GenericEditorOption[];
    search(
        items: GenericEditorOption[],
        searchTerm: string
    ): Promise<GenericEditorOption[]> | Promise<GenericEditorOption[]>;
    getItem(payload: DropdownPopulatorItemPayload): Promise<GenericEditorOption>;
}

/* @internal */
export interface DropdownPopulatorPayload {
    id?: string;
    field: GenericEditorField;
    model: Payload;
    selection: GenericEditorOption;
    search: string;
}

/* @internal */
export interface DropdownPopulatorItemPayload {
    id: string;
    field: GenericEditorField;
    model: Payload;
}

/* @internal */
export interface DropdownPopulatorPagePayload extends DropdownPopulatorPayload {
    pageSize: number;
    currentPage: number;
}

/* @internal */
export interface DropdownPopulatorFetchPageResponse {
    field: GenericEditorField;
    [index: string]: any;
}
