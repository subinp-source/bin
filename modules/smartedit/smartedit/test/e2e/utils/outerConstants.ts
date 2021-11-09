/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken } from '@angular/core';

import { TypedMap } from 'smarteditcommons';

export const STOREFRONT_URI = 'http://127.0.0.1:9000/test/utils/storefront.html';

export const ADMIN_AUTH_TOKEN = new InjectionToken<TypedMap<string>>('ADMIN_AUTH_TOKEN');
export const CMSMANAGER_AUTH_TOKEN = new InjectionToken<TypedMap<string>>('CMSMANAGER_AUTH_TOKEN');

export const CONFIGURATION_MOCK_TOKEN = new InjectionToken<TypedMap<string>[]>(
    'CONFIGURATION_MOCK'
);
export const CONFIGURATION_AUTHORIZED_TOKEN = new InjectionToken<boolean>(
    'CONFIGURATION_AUTHORIZED'
);
export const CONFIGURATION_FORBIDDEN_TOKEN = new InjectionToken<boolean>('CONFIGURATION_FORBIDDEN');

export const ADMIN_WHOAMI_DATA_TOKEN = new InjectionToken<TypedMap<string>[]>('ADMIN_WHOAMI_DATA');
export const CMSMANAGER_WHOAMI_DATA_TOKEN = new InjectionToken<TypedMap<string>[]>(
    'CMSMANAGER_WHOAMI_DATA'
);

export const ADMIN_AUTH = {
    access_token: 'admin-access-token',
    token_type: 'bearer'
};

export const CMSMANAGER_AUTH = {
    access_token: 'cmsmanager-access-token',
    token_type: 'bearer'
};

export const CMSMANAGER_WHOAMI_DATA = {
    displayName: 'CMS Manager',
    uid: 'cmsmanager'
};

export const ADMIN_WHOAMI_DATA = {
    displayName: 'Administrator',
    uid: 'admin'
};

export const STOREFRONT_URI_TOKEN = new InjectionToken('STOREFRONT_URI');
