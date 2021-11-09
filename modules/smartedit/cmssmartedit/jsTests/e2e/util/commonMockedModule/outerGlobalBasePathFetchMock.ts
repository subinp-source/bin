/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// @ts-ignore
import fetchMock from 'fetch-mock';

fetchMock.mock('path:/smartedit/settings', {
    'smartedit.globalBasePath': 'http://localhost:3333'
});
