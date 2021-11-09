/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IGlobalPermission } from '../../entities/permissions';

export const globalPermissions: IGlobalPermission = {
    id: 'global',
    permissions: [
        {
            key: 'smartedit.configurationcenter.read',
            value: 'true'
        }
    ]
};
