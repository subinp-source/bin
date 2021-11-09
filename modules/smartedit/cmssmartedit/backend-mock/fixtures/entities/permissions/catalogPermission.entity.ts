/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPermission, ISyncPermission } from './index';

export interface ICatalogPermission {
    catalogId: string;
    catalogVersion: string;
    permissions: IPermission[];
    syncPermissions: ISyncPermission[] | [{}];
}
