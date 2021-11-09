/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IAuthToken } from 'fixtures/entities/authorization';

export const adminToken: IAuthToken = {
    access_token: 'admin-access-token',
    token_type: 'bearer'
};
