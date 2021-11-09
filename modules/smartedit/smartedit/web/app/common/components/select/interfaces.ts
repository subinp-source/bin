/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Page } from '@smart/utils';

import { TypedMap } from '../../dtos';

/** Either `label` or `name` must be provided */
export interface SelectItem {
    id: string;
    uid?: string;
    label?: TypedMap<string> | string;
    name?: TypedMap<string> | string;
}

export type FetchPageStrategy<T> = (
    search?: string,
    pageSize?: number,
    currentPage?: number
) => angular.IPromise<Page<T>> | Promise<Page<T>>;
export type FetchAllStrategy<T> = (mask?: string) => angular.IPromise<T[]> | Promise<T[]>;

export interface FetchStrategy<T> {
    fetchAll?: FetchAllStrategy<T>;
    fetchPage?: FetchPageStrategy<T>;
    fetchEntity?: (id: string) => angular.IPromise<T> | Promise<T>;
    fetchEntities?: (ids?: string[]) => angular.IPromise<T[]> | Promise<T[]>;
}

export type SelectDisableChoice<T> = (item: T) => boolean;
export type SelectReset = (forceReset: boolean) => void;
export type SelectKeyup = (event: Event, search: string) => void;
export type SelectOnChange = () => void;
export type SelectOnRemove<T> = (item: T, model: string) => void;
export type SelectOnSelect<T> = (item: T, model: string) => void;

/**
 * The Select Component api object exposing public functionality
 */
export interface SelectApi {
    /**
     * A method that sets the validation state of the selector
     *
     * @param state A validation state message type constant. See {@link seConstantsModule.object:VALIDATION_MESSAGE_TYPES SeConstantsModule} for more information.
     */
    setValidationState(state: string): void;

    /** A method that resets the validation state to default */
    resetValidationState(): void;
}
