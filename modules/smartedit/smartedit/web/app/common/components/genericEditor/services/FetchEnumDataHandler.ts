/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { stringUtils, IRestService, Payload, RestServiceFactory, TypedMap } from '@smart/utils';
import { IFetchDataHandler } from './IFetchDataHandler';
import { GenericEditorField } from '../types';
import { Injectable } from '@angular/core';
import { SeDowngradeService } from '../../../di';
import { ENUM_RESOURCE_URI } from '../../../utils';

/* @internal  */
@SeDowngradeService()
@Injectable()
export class FetchEnumDataHandler implements IFetchDataHandler {
    public static resetForTests() {
        FetchEnumDataHandler.cache = {};
    }

    private static cache: TypedMap<any> = {};

    private restServiceForEnum: IRestService<Payload>;

    constructor(private restServiceFactory: RestServiceFactory) {
        this.restServiceForEnum = this.restServiceFactory.get<Payload>(ENUM_RESOURCE_URI);
    }

    findByMask(field: GenericEditorField, search?: string): Promise<string[]> {
        return (FetchEnumDataHandler.cache[field.cmsStructureEnumType]
            ? Promise.resolve(FetchEnumDataHandler.cache[field.cmsStructureEnumType])
            : Promise.resolve(
                  this.restServiceForEnum.get({
                      enumClass: field.cmsStructureEnumType
                  })
              )
        ).then((response) => {
            FetchEnumDataHandler.cache[field.cmsStructureEnumType] = response;
            return FetchEnumDataHandler.cache[field.cmsStructureEnumType].enums.filter(
                (element: Payload) => {
                    return (
                        stringUtils.isBlank(search) ||
                        (element.label as string).toUpperCase().indexOf(search.toUpperCase()) > -1
                    );
                }
            );
        });
    }

    getById(field: GenericEditorField, identifier: string): Promise<string> {
        return null;
    }
}
