/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogPermission } from '../../entities/permissions';

export const stagedCatalogPermissions: ICatalogPermission[] = [
    {
        catalogId: 'some.catalog.id',
        catalogVersion: 'Staged',
        permissions: [
            {
                key: 'read',
                value: 'true'
            },
            {
                key: 'write',
                value: 'true'
            }
        ],
        syncPermissions: [
            {
                canSynchronize: true,
                targetCatalogVersion: 'Online'
            }
        ]
    }
];
