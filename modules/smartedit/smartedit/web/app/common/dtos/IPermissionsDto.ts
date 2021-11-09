/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SearchParams } from 'smarteditcommons/services';

/**
 * Key value pairs where the key is the permission and the value is a boolean string
 * Used in IPermissionsRestServiceResult
 */
export interface IPermissionsRestServicePair {
    key: string;
    value: string;
}

/**
 * Result of getting permissions form the PermissionsRestService.get
 */
export interface IPermissionsRestServiceResult {
    id?: string;
    permissions: IPermissionsRestServicePair[];
}

/**
 * The input param type for PermissionsRestService.get
 */
export interface IPermissionsRestServiceQueryData extends SearchParams {
    user: string;
    permissionNames: string;
}
