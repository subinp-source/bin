/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from 'smarteditcommons/di';
import {
    IPermissionsRestServiceQueryData,
    IPermissionsRestServiceResult
} from 'smarteditcommons/dtos';
import { IRestService, RestServiceFactory } from '@smart/utils';

@SeDowngradeService()
export class PermissionsRestService {
    private readonly URI = '/permissionswebservices/v1/permissions/principals/:user/global';
    private readonly resource: IRestService<IPermissionsRestServiceResult>;

    constructor(restServiceFactory: RestServiceFactory) {
        this.resource = restServiceFactory.get<IPermissionsRestServiceResult>(this.URI);
    }

    get(queryData: IPermissionsRestServiceQueryData): Promise<IPermissionsRestServiceResult> {
        return this.resource.get(queryData).then((data: IPermissionsRestServiceResult) => ({
            permissions: data.permissions
        }));
    }
}
