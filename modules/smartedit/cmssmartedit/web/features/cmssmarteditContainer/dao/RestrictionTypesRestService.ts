/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IRestService, RestServiceFactory, SeDowngradeService, TypedMap } from 'smarteditcommons';
import { RESTRICTION_TYPES_URI } from 'cmscommons';

export interface IRestrictionType {
    code: string;
    name: TypedMap<string>;
}
export interface IRestrictionTypeList {
    restrictionTypes: IRestrictionType[];
}
@SeDowngradeService()
export class RestrictionTypesRestService {
    private readonly restrictionTypesRestService: IRestService<IRestrictionTypeList>;

    constructor(private restServiceFactory: RestServiceFactory) {
        this.restrictionTypesRestService = this.restServiceFactory.get(RESTRICTION_TYPES_URI);
    }

    getRestrictionTypes(): Promise<IRestrictionTypeList> {
        return this.restrictionTypesRestService.get();
    }
}
